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
 *          |  22.09.2015        |  Fix bug with ip assignment.
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * Class for reading properties.
 * 
 * <h4>Notes</h4>
 * none
 * 
 * @author      Florian Behrend, s60119@beuth-hochschule.de
 * @version     Version 1.0
 */

package dhcp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


public class DHCPProperties {
	
	static Logger log = LogManager.getLogger(DHCPProperties.class.getName());
	
	private static DHCPProperties instance;
	
	//Liste der verfügbaren Router
	private List<InetAddress> currentRouters = new ArrayList<InetAddress>();
	
	//Liste der verfügbaren Nameserver
	private List<InetAddress> currentNameServers = new ArrayList<InetAddress>();
	
	//Liste der verfügbaren IPs für die Clients
	private List<DHCPIpAddress> currentIPAddresses = new ArrayList<DHCPIpAddress>();
	
	//Liste der verfügbaren Zeitserver
	private List<InetAddress> current_global_time_servers = new ArrayList<InetAddress>();
	
	private byte[] currentSubnetmask = new byte[4];
	
	private byte[] current_global_broadcast_address = new byte[4];
	
	//IP-Adresse des Servers
	private InetAddress currentServerIP;
	
	private int current_global_leasetime;
	private int current_global_renewaltime;
	private int current_global_rebindingtime;
	
	private String current_global_domainname;
	
	private DHCPProperties() {
		log.trace("DHCPProperties object was created");
		
		readXML();
	}
	
	
	private void readXML() {
		log.debug("start reading of properties");
		SAXBuilder sb = new SAXBuilder();

		try {
			//Datei öffnen
			Document myDoc = sb.build("./bin/settings.xml");
			
			//Das Root-Element einlesen (sollte settings sein!)
			Element currentRootElement = myDoc.getRootElement();
			
			
			log.debug("start reading properties of server");
			
			//Liste mit den Servereinstellungen laden 
			List<Element> serverSettings = currentRootElement.getChildren("server");
			
			for (Element setting : serverSettings) {
				
				//IP-Adresse des Servers
				currentServerIP = InetAddress.getByName(setting.getChildText("ip-address"));
				
				//Debugausgabe der aktuellen IP des Servers
				log.debug("Server IP-Adress: " + currentServerIP.getHostAddress());
			}
			
			
			log.debug("start reading properties of clients");
			
			//Liste mit den Servereinstellungen laden 
			List<Element> clientSettings = currentRootElement.getChildren("clients");
			
			log.debug("number of clients: " + clientSettings.size());
			
			for (Element setting : clientSettings) {
				
				List<Element> currentChilds = setting.getChildren();
				
				for (Element currentChild : currentChilds) {
					
					//Nach globalen Einstellingen für alle nicht explizit aufgelisteten Clients suchen
					if (currentChild.getAttribute("id").getValue().equals("all")) {
						List<Element> ipRange = currentChild.getChildren("ip-range");
						List<Element> routers = currentChild.getChildren("router");
						List<Element> nameservers = currentChild.getChildren("nameserver");
						Element leasetime = currentChild.getChild("leasetime");
						Element renewaltime = currentChild.getChild("renewaltime");
						Element rebindingtime = currentChild.getChild("rebindingtime");
						Element domainname = currentChild.getChild("domainname");
						Element broadcast = currentChild.getChild("broadcast");
						List<Element> timeservers = currentChild.getChildren("ntp-server");
						
						for (Element e :currentChild.getChildren())
						{
							log.debug(e.getName());
						}
						
						log.debug("range: " + ipRange.size());
						
						for (Element range : ipRange) {
							String start = range.getChild("start").getValue();
							String ende = range.getChild("end").getValue();
							String maske = range.getChild("subnetmask").getValue();
							
							log.debug("start: " + start);
							log.debug("end: " + ende);
							
							String[] ipstart = start.split("\\.");
							String[] ipend = ende.split("\\.");
							String[] imask = maske.split("\\.");
							
							int ip1 = Integer.parseInt(ipstart[3]);
							int ip2 = Integer.parseInt(ipend[3]);
							
							for (int i=ip1; i <= ip2; i++) {
								log.debug("IP: " + ipstart[0] + "." + ipstart[1] + "." + ipstart[2] + "." + i);
								
								currentIPAddresses.add(new DHCPIpAddress(InetAddress.getByName(ipstart[0] + "." + ipstart[1] + "." + ipstart[2] + "." + i)));
							}
							
							log.debug("ips: " + currentIPAddresses.size());
							
							for (DHCPIpAddress ip : currentIPAddresses) {
								log.debug("IP: " + ip.getIpAddress().toString());
							}
							
							currentSubnetmask[0] = (byte)Integer.parseInt(imask[0]);
							currentSubnetmask[1] = (byte)Integer.parseInt(imask[1]);
							currentSubnetmask[2] = (byte)Integer.parseInt(imask[2]);
							currentSubnetmask[3] = (byte)Integer.parseInt(imask[3]);
						}
						
						for (Element router : routers) {
							currentRouters.add(InetAddress.getByName(router.getValue()));
							
							log.debug("new router-addresss: " + router.getValue());
						}
						
						for (Element nameserver : nameservers) {
							currentNameServers.add(InetAddress.getByName(nameserver.getValue()));
							
							log.debug("new nameserver-addresss: " + nameserver.getValue());
						}
						
						
						if (leasetime != null) {
							current_global_leasetime = Integer.parseInt(leasetime.getValue());
							log.debug("current global leasetime: " + current_global_leasetime + "s");
						}
						
						if (renewaltime != null) {
							current_global_renewaltime = Integer.parseInt(renewaltime.getValue());
							log.debug("current global renewaltime: " + current_global_renewaltime + "s");
						}
						
						if (rebindingtime != null) {
							current_global_rebindingtime = Integer.parseInt(rebindingtime.getValue());
							log.debug("current global rebindingtime: " + current_global_rebindingtime + "s");
						}
						
						if (domainname != null) {
							current_global_domainname = domainname.getValue();
							log.debug("current global domain name: " + current_global_domainname);
						}
						
						if (broadcast != null) {
							String bcast = broadcast.getValue();
							String[] ibcast = bcast.split("\\.");
							
							current_global_broadcast_address[0] = (byte)Integer.parseInt(ibcast[0]);
							current_global_broadcast_address[1] = (byte)Integer.parseInt(ibcast[1]);
							current_global_broadcast_address[2] = (byte)Integer.parseInt(ibcast[2]);
							current_global_broadcast_address[3] = (byte)Integer.parseInt(ibcast[3]);
							
							log.debug("current global broadcast address: " + current_global_broadcast_address[0] + "." + current_global_broadcast_address[1] + "." + current_global_broadcast_address[2] + "." + current_global_broadcast_address[3]);
						}
						
						if (timeservers != null) {
							for (Element server : timeservers) {
								current_global_time_servers.add(InetAddress.getByName(server.getValue()));
								
								log.debug("new ntp-server: " + server.getValue());
							}
						}
						
					}
				}
			}
			
			log.debug("finish reading of properties");
			
		} catch (Exception ex) {
			log.error(ex.getMessage());
			log.debug(ex.toString());
			log.trace(ex.getStackTrace());
		}
		
	}
	
	
	public static synchronized DHCPProperties getInstance () {
	    if (DHCPProperties.instance == null) {
	    	DHCPProperties.instance = new DHCPProperties();
	    }
	    return DHCPProperties.instance;
	}

	
	public InetAddress getServerAddress() {
		return currentServerIP;
	}
	
	public List<InetAddress> getRouters() {
		return currentRouters;
	}
	
	public List<InetAddress> getNameservers() {
		return currentNameServers;
	}
	
	public List<DHCPIpAddress> getIPs() {
		return currentIPAddresses;
	}
	
	public byte[] getSubnetMask() {
		return this.currentSubnetmask;
	}
	
	public int getLeasetime() {
		return this.current_global_leasetime;
	}
	
	public int getRenewaltime() {
		return current_global_renewaltime;
	}

	public int getRebindingtime() {
		return current_global_rebindingtime;
	}
	
	public String getDomainName() {
		return current_global_domainname;
	}
	
	public byte[] getBroadcastAddress() {
		return current_global_broadcast_address;
	}
	
	public List<InetAddress> getTimeServers() {
		return current_global_time_servers;
	}


	public synchronized byte[] takeIP(byte[] client_identifier) {
		
		log.debug("search free ip");
		
		for (DHCPIpAddress ip : currentIPAddresses) {
			
			log.debug("--> current selected ip: " + ip.getIpAddress().toString());
			
			if (!ip.isTaken()) {
				log.debug("----> ip was free and will be taken");
				
				return ip.take(client_identifier).getAddress();
			} else {
				log.warn("----> ip was already taken");
			}
		}
		
		return null;
	}
	
	public synchronized void releaseIP(byte[] client_identifier) {
		
		log.debug("release ip -> client: " + DHCPUtils.bytesToHex(client_identifier));
		
		for (DHCPIpAddress ip : currentIPAddresses) {
			
			log.debug("--> current selected ip: " + ip.getIpAddress().toString());
			
			log.debug("----> current client identifier of ip: " + ip.getClientIdentifier());
			
			if (Arrays.equals(client_identifier, ip.getClientIdentifier())) {
				log.debug("------> identifiers are identical, ip will be released now");
				
				ip.release();
			} else {
				log.debug("------> identifiers are not identical");
			}
		}
	}
}
