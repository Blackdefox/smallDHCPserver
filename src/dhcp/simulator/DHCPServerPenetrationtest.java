package dhcp.simulator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPConstants;
import dhcp.DHCPUtils;
import dhcp.packet.DHCPpacket;

public class DHCPServerPenetrationtest implements Runnable{

	public enum stresstestState {
		RUNNING,
		STOPPED,
		ERROR
	}
	
	public enum penetrationTestType {
		DHCPDISCOVER,
		DHCPREQUEST
	}
	
	static Logger log = LogManager.getLogger(DHCPClientSimulator.class.getName());
	
	private stresstestState currentState = stresstestState.STOPPED;
	private boolean isInterrupted = false;
	
	private penetrationTestType current_stresstestType;
	private long current_delay;
	private int current_loops;
	private DHCPClientSimulator currentSimulator;
	
	//Liste der XIDs für die Nachrichten versendet wurden
	private List<DHCPClientXid> currentListOfXids = new LinkedList<DHCPClientXid>();
	
	public DHCPServerPenetrationtest(penetrationTestType currentTestType, long delay, int loops, DHCPClientSimulator simulator) {
		this.current_stresstestType = currentTestType;
		this.current_delay = delay;
		if (loops > 0) {
			this.current_loops = loops;
		} else {
			this.current_loops = 0;
		}
		this.currentSimulator = simulator;
	}
	
	public DHCPServerPenetrationtest(penetrationTestType currentStresstestType, long delay, DHCPClientSimulator simulator) {
		this(currentStresstestType, delay, 0, simulator);
	}
	
	public void run() {
		if (current_stresstestType == penetrationTestType.DHCPDISCOVER) {
			//Versenden von einstellbar vielen DHCPDISCOVER Nachrichten an eine Server
			if (current_loops == 0) {
				while (!isInterrupted) {
					//neue XID erzeugen
					byte[] xid = generateRandomXid();
					
					//XID für die weitere Analyse speichern
					currentListOfXids.add(new DHCPClientXid(xid));
					
					log.debug("new random xid: " + DHCPUtils.bytesToHex(xid));
					
					//Neue DHCPDISCOVER-Nachricht generieren
					DHCPpacket dp = new DHCPpacket();
					byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER, xid);
					
					//TODO: hier fehlen noch ein paar Optionen die bei der Anfrage mit versendet werden können
					
					currentSimulator.sendPacket(bytes);
					
					log.debug("new DHCPDISCOVER was sended");
					
					//Die Pause mal kurz schlafen! (dormir, dormir, dormir...)
					try {
					Thread.sleep(current_delay);
					} catch (Exception ex) {
						
					}
				}
			} else {
				for(int i = 0; i <= current_loops; i++) {
					//neue XID erzeugen
					byte[] xid = generateRandomXid();
					
					//XID für die weitere Analyse speichern
					currentListOfXids.add(new DHCPClientXid(xid));
					
					log.debug("new random xid: " + DHCPUtils.bytesToHex(xid));
					
					//Neue DHCPDISCOVER-Nachricht generieren
					DHCPpacket dp = new DHCPpacket();
					byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER, xid);
					
					//TODO: hier fehlen noch ein paar Optionen die bei der Anfrage mit versendet werden können
					
					currentSimulator.sendPacket(bytes);
					
					log.debug("new DHCPDISCOVER was sended");
					
					//Die Pause mal kurz schlafen! (dormir, dormir, dormir...)
					try {
					Thread.sleep(current_delay);
					} catch (Exception ex) {
						
					}
				}
			}
			
		} else if (current_stresstestType == penetrationTestType.DHCPREQUEST) {
			if (current_loops == 0) {
				while (!isInterrupted) {
					//neue XID erzeugen
					byte[] xid = generateRandomXid();
					
					log.debug("new random xid: " + DHCPUtils.bytesToHex(xid));
					
					//Neue DHCPDISCOVER-Nachricht generieren
					DHCPpacket dp = new DHCPpacket();
					byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST, xid);
					
					//TODO: hier fehlen noch ein paar Optionen die bei der Anfrage mit versendet werden können
					
					currentSimulator.sendPacket(bytes);
					
					log.debug("new DHCPDISCOVER was sended");
					
					//Die Pause mal kurz schlafen! (dormir, dormir, dormir...)
					try {
					Thread.sleep(current_delay);
					} catch (Exception ex) {
						
					}
				}
			} else {
				for(int i = 0; i <= current_loops; i++) {
					//neue XID erzeugen
					byte[] xid = generateRandomXid();
					
					log.debug("new random xid: " + DHCPUtils.bytesToHex(xid));
					
					//Neue DHCPDISCOVER-Nachricht generieren
					DHCPpacket dp = new DHCPpacket();
					byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST, xid);
					
					//TODO: hier fehlen noch ein paar Optionen die bei der Anfrage mit versendet werden können
					
					currentSimulator.sendPacket(bytes);
					
					log.debug("new DHCPDISCOVER was sended");
					
					//Die Pause mal kurz schlafen! (dormir, dormir, dormir...)
					try {
					Thread.sleep(current_delay);
					} catch (Exception ex) {
						
					}
				}
			}
		}
	}
	
	
	public List<DHCPClientXid> getCurrentListOfXids() {
		return this.currentListOfXids;
	}
	
	
	public void interrupt() {
		isInterrupted = true;
		
		this.currentState = stresstestState.STOPPED;
	}

	public stresstestState getState() {
		return currentState;
	}
	
	
	private static byte [] generateRandomXid() {
		//neue XID erzeugen
		Random r = new Random();
		byte[] xid = {(byte)(r.nextInt(255)+1), (byte)(r.nextInt(255)+1), (byte)(r.nextInt(255)+1), (byte)(r.nextInt(255)+1)};
		
		return xid;
	}
}
