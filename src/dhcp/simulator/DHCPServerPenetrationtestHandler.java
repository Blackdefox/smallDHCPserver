package dhcp.simulator;

import java.net.DatagramPacket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPConstants;
import dhcp.DHCPUtils;
import dhcp.packet.DHCPpacket;

public class DHCPServerPenetrationtestHandler implements Observer {

	static Logger log = LogManager.getLogger(DHCPClientSimulator.class.getName());
	
	private int message_counter; 
	
	private DHCPClientSimulator currentSimulator;
	
	private BlockingQueue<DatagramPacket> receiveQueue;
	
	private List<DHCPClientXid> currentListOfXids;
	
	public DHCPServerPenetrationtestHandler(DHCPClientSimulator currentSimulator, BlockingQueue<DatagramPacket> receiveQueue, List<DHCPClientXid> currentXidList) {
		this.currentSimulator = currentSimulator;
		this.receiveQueue = receiveQueue;
		this.currentListOfXids = currentXidList;
		
		log.debug("new penetration test handler created");
	}
	
	
	@Override public void update( Observable o, Object arg ) {
		//Es wurde eine neue  Nachricht empfangen was soll nun passieren?
		log.debug("received new dhcp message");
		
		while (!receiveQueue.isEmpty()) {
			
			try {
				DatagramPacket tmp = receiveQueue.take();
				
				message_counter++;
				
				log.debug("leght of received data: " + tmp.getData().length);
				log.debug("received data: " + DHCPUtils.bytesToHex(tmp.getData()));
				
				log.debug("count of received messages: " + message_counter);
				
				//Was für eine Nachricht wurde eigentlich empfangen???
				DHCPServerPacket clientPacket = new DHCPServerPacket();
				
				switch (clientPacket.decode(tmp.getData(), tmp.getLength())) {
				case DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER:
					log.debug("should be a offer from server");
					
					//XID ermitteln
					log.debug("XID: " + clientPacket.getXid());
					log.debug("");
					
					for (DHCPClientXid cli : currentListOfXids) {
						if (Arrays.equals(cli.getCurrent_xid(), clientPacket.getXid())) {
							log.debug("found xid");
							
							log.debug("send time: " + cli.getCurrent_create_timestamp());
							log.debug("receive time: " + LocalDateTime.now());
							
						} else {
							log.debug("didn't found xid");
						}
					}
					
					break;
				}
				
			} catch (Exception ex) {
				log.error("keine Daten in der Queue.");
			}
			
		}
	}
	
	
	public void reset() {
		message_counter = 0;
	}
	
}
