/**
 * Copyright 2015 (C)
 *  
 * Created on : 20.06.2015 
 * Authors    : Florian Behrend
 * 				Christoph Palme
 * Email      : s60119@beuth-hochschule.de
 * 
 *----------------------------------------------------------------------------- 
 * Revision History (Release 1.0.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |
 *          |  20.06.2015        |  Initial Create.
 *-----------------------------------------------------------------------------
 * 1.1      |  Florian Behrend   |
 *          |  21.09.2015        |  Fix behave on discovery request 
 *          |					 |  (security risky)
 *          |					 |	Add handle of Inform und Release
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * ...
 * 
 * <h4>Notes</h4>
 * none
 * 
 * @author      Florian Behrend, s60119@beuth-hochschule.de
 * @version     Version 1.0
 */

package dhcp.packet;

import java.net.DatagramPacket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPClient;
import dhcp.DHCPConstants;
import dhcp.DHCPProperties;
import dhcp.DHCPUtils;
import dhcp.server.DHCPserver;

public class DHCPPacketHandler implements Observer {
	
	public enum clientState {
		FIRST,
		SECOND,
		THIRD,
		FOURTH
	}
	
	static Logger log = LogManager.getLogger(DHCPPacketHandler.class.getName());
	
	private DHCPserver currentServer;
	private List<DHCPClient> currentClientList;
	
	private BlockingQueue<DatagramPacket> receiveQueue;
	
	
	public DHCPPacketHandler(DHCPserver currentServer, BlockingQueue<DatagramPacket> receiveQueue, List<DHCPClient> currentClientList) {
		this.currentServer = currentServer;
		this.currentClientList = currentClientList;
		this.receiveQueue = receiveQueue;
		
		log.debug("packet-handler was created.");
	}
	
	
	@Override public void update( Observable o, Object arg ) {
		
		log.debug("get a new call from hollywood: there is something to do");
		log.debug("elements in clients list: " + currentClientList.size());
		
		while (!receiveQueue.isEmpty()) {
			try {
				DatagramPacket tmp = receiveQueue.take();
				DHCPClient oldClient, newClient;
				
				DHCPpacket clientPacket = new DHCPpacket(); 
				
				switch (clientPacket.decode(tmp.getData(), tmp.getLength())) {
				case DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER:
					log.debug("handle new discover message");
					
					//Neuen Client aus dem Empfangenden-Paket erzeugen
					newClient = new DHCPClient(clientPacket);
					
					if (!currentClientList.contains(newClient)) { //Der Client ist noch nicht in der Liste
						
						log.debug("add new client to list of clients");
						log.debug("\n" + newClient.toString());
						
						//Zeit der Discovery-Anfrage festhalten
						newClient.setDiscoveryTimestamp();
						
						//Automitische Bestimmung der IP für den Client.
						byte[] currentIP = DHCPProperties.getInstance().takeIP(newClient.getClientIdentifier());
						
						if (currentIP != null) {
							//Es wurde eine freie IP gefunden
							log.debug("found free ip for client: " + ((int)currentIP[0] & 0xFF) + "." + ((int)currentIP[1] & 0xFF) + "." + ((int)currentIP[2]& 0xFF) + "." + ((int)currentIP[3] & 0xFF));
							
							//Client Adressen zuweisen
							newClient.setCurrent_ip_address(currentIP);
							newClient.setCurrent_subnet_mask(DHCPProperties.getInstance().getSubnetMask());
							
							//Zusätzliche Informationen des Clients speichern
							newClient.setHostname(clientPacket.getHostname());
							newClient.setClientIdentifier(clientPacket.getClientIdentifier());
							
							//Mitzählen wie oft schon ein Discover von diesem Client empfangen worden ist.
							newClient.countDiscovery();
							
							//Client zur Liste der Clients hinzufügen
							currentClientList.add(newClient);
							
							//Antwort an den Client senden (Offer)
							DHCPpacket dp = new DHCPpacket();
							byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER, newClient);
							
							//Antwort in die Sende-Queue eintragen
							currentServer.sendResponse(bytes);
							
						} else {
							//Es wurde keine freie IP für den Client gefunden
							log.debug("found no free ip for client");
						}
						
					} else { //Der Client ist schon in der Liste
						log.debug("client is already in the list of clients");
						
						//Referenz auf den Client aus der Liste der Clients heraussuchen.
						oldClient = currentClientList.get(currentClientList.indexOf(newClient));
						
						//Mitzählen wie oft schon ein Discover von diesem Client empfangen worden ist.
						oldClient.countDiscovery();
						
						log.debug("current count of discovers: " + oldClient.getDiscoveryCounter());
						
						//Wenn weniger als 10 Anfragen gekommen sind wird einfach nochmal ein Offer gesendet!
						if ((oldClient.getDiscoveryCounter()) < 10 || (oldClient.getDiscoverySeconds() >= DHCPProperties.getInstance().getLeasetime())) {
							
							//Die neue XID in den client einfügen
							oldClient.setCurrent_xid(newClient.getCurrent_xid());
							
							//Zusätzliche Informationen des Clients speichern
							newClient.setHostname(clientPacket.getHostname());
							newClient.setClientIdentifier(clientPacket.getClientIdentifier());
							
							//Antwort an den Client senden (Offer)
							DHCPpacket dp = new DHCPpacket();
							byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER, oldClient);
							
							//Antwort in die Sende-Queue eintragen
							currentServer.sendResponse(bytes);
						} else {
							if (oldClient.getDiscoveryCounter() > 10) {
								log.warn("thera are more than " + oldClient.getDiscoveryCounter() + " discovery request in the last few seconds");
							} else {
								log.debug("leasetime of client not expired: " + oldClient.getTime().toLocalTime());
								
								log.debug(LocalDateTime.now().isAfter(oldClient.getTime()));
							}
						}
					}
					
					//Erweitertes Logging im Debug
					log.debug("client MAC-address: " + DHCPUtils.bytesToHex(newClient.getHardware_address()));
					log.debug("client xid: " +  DHCPUtils.bytesToHex(newClient.getCurrent_xid()));
					
					break;
				case DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST:
					log.debug("handle request message");
					
					newClient = new DHCPClient(clientPacket);
					
					if (currentClientList.contains(newClient)) {
						
						log.debug("found client in list of clients");
						
						//Referenz auf den client in der Liste erzeugen
						oldClient = currentClientList.get(currentClientList.indexOf(newClient));
						
						//Zeitpunkt des Request im client festhalten
						oldClient.setRequestTimestamp();
						
						//Mitzählen wie oft schon ein Request von diesem Client empfangen worden ist.
						oldClient.countRequest();
						
						if (!Arrays.equals(oldClient.getCurrent_xid(), newClient.getCurrent_xid())) {
							//Der Request hat eine neue xid. Dies passiert wenn ein Request gesendet wird weil die
							//Lease Time abgelaufen ist. (Bei Windows 7 ist das so!)
							
							log.debug("xid has changed");
							
							//Die neue XID in den client einfügen
							oldClient.setCurrent_xid(newClient.getCurrent_xid());
							
						} else {
							log.debug("xid hasn't changed");
						}
						
						//Überprüfen ob die Daten im Request zu dem Client passen
						log.debug(DHCPUtils.bytesToHex(oldClient.getCurrent_xid()) + " -- " + DHCPUtils.bytesToHex(newClient.getCurrent_xid()));
						
						//Zeit in den Client eintragen
						oldClient.setLeaseTime(DHCPProperties.getInstance().getLeasetime());
						
						//Antwort an den Client senden (Offer)
						DHCPpacket dp = new DHCPpacket();
						byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_ACK, oldClient);
						
						//Antwort in die Sende-Queue eintragen
						currentServer.sendResponse(bytes);
					} else {
						log.debug("client not found in list of clients");
					}
					
					break;
				case DHCPConstants.DHCP_V4_MESSAGE_TYPE_INFORM:
					log.debug("handle inform message");
					
					newClient = new DHCPClient(clientPacket);
					
					if (currentClientList.contains(newClient)) {
						
						log.debug("found client in list of clients");
						
						//Referenz auf den client in der Liste erzeugen
						oldClient = currentClientList.get(currentClientList.indexOf(newClient));
						
						//Zeitpunkt des Informs im client festhalten
						oldClient.setInformTimestamp();
						
						//Mitzählen wie oft schon ein Inform von diesem Client empfangen worden ist.
						oldClient.countInform();
						
						
						if (!Arrays.equals(oldClient.getCurrent_xid(), newClient.getCurrent_xid())) {
							//Der Request hat eine neue xid. Dies passiert wenn ein Request gesendet wird weil die
							//Lease Time abgelaufen ist. (Bei Windows 7 ist das so!)
							
							log.debug("xid has changed");
							
							//Die neue XID in den client einfügen
							//TODO: Hier muss noch gestet werden wie sich die System verhalten!
							//oldClient.setCurrent_xid(newClient.getCurrent_xid());
							
						} else {
							log.debug("xid hasn't changed");
						}
						
						//Überprüfen ob die Daten im Request zu dem Client passen
						log.debug(DHCPUtils.bytesToHex(oldClient.getCurrent_xid()) + " -- " + DHCPUtils.bytesToHex(newClient.getCurrent_xid()));
						
						//TODO: Eventuell muss die Leasetime hier an die aktuelle Zeit angepasst werden.
						
						//Antwort an den Client senden (Offer)
						DHCPpacket dp = new DHCPpacket();
						byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_ACK, oldClient);
						
						//Antwort in die Sende-Queue eintragen
						currentServer.sendResponse(bytes);
					} else {
						log.debug("client not found in list of clients");
					}
					
					break;
				case DHCPConstants.DHCP_V4_MESSAGE_TYPE_RELEASE:
					log.debug("handle release message");
					
					newClient = new DHCPClient(clientPacket);
					
					if (currentClientList.contains(newClient)) {
						
						log.debug("found client in list of clients");
						
						//Referenz auf den client in der Liste erzeugen
						oldClient = currentClientList.get(currentClientList.indexOf(newClient));
						
						//Zeitpunkt des Informs im client festhalten
						oldClient.setReleaseTimestamp();
						
						//Mitzählen wie oft schon ein Inform von diesem Client empfangen worden ist.
						oldClient.countRelease();
						
						//IP-Adresse des Clients wieder freigeben
						DHCPProperties.getInstance().releaseIP(oldClient.getClientIdentifier());
						
						//Clinet aus der Liste der Clients löschen
						currentClientList.remove(oldClient);
						
					} else {
						log.debug("client not found in list of clients");
					}
					
					break;
				}
				
			} catch (Exception ex) {
				log.error("keine Daten in der Queue.");
			}
		}
		
	}

}
