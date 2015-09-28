package dhcp.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DHCPsender implements Observer{
	InetAddress sourceAddress;
	InetAddress destinationAddress;
	int sourcePort;
	int destinationPort;
	
	private DatagramSocket sendSocket;
	
	static Logger log = LogManager.getLogger(DHCPsender.class.getName());
	
	//Buffer (Threadsicher)
	private BlockingQueue<byte[]> sendQueue;
	
	
	/**
	 * Konstruktor der Klasse UPDlistener.
	 * 
	 * @param sourceAddress			IP-Adresse der simulierten DFI-Anlage. IP-Adresse vom Typ InetAddress
	 * @param sourcePort			Port der simulierten DFI-Anlage. Port vom Typ Integer
	 * @param destinationAddress	IP-Adresse des Servers. IP-Adresse vom Typ InetAddress
	 * @param destinationPort		Port des Servers. Port vom Typ Integer
	 * @param sendQueue				Queue für die zu senden Datagramme.
	 */
	public DHCPsender (DatagramSocket currentSocket,
					   InetAddress destinationAddress, int destinationPort,
					   BlockingQueue<byte[]> sendQueue) {
		this.sendSocket = currentSocket;
		this.destinationAddress = destinationAddress;
		this.destinationPort = destinationPort;
		this.sendQueue = sendQueue;
	}
	
	
	// Schnittstelle für den Observer
	// Wenn der Beobachter benachrichtigt wird werden Daten gesendet
	@Override public void update( Observable o, Object arg ) {
		try {
			
			log.info("sender thread for " + sendSocket.getLocalAddress() + ":" + sendSocket.getLocalPort() + " was started");
			
			while (!sendQueue.isEmpty()) {
				//Daten für das Packet aus der Queue holen
				byte[] raw = sendQueue.take();
				
				//Packet zum Versenden erstellen
				DatagramPacket packet = new DatagramPacket(raw, raw.length, destinationAddress, destinationPort);
				
				//Packet versenden
				sendSocket.send(packet);
				
				log.info("Packet with lenght " + packet.getLength() + " sended to " + packet.getAddress() + ":" + packet.getPort());
				log.debug("Sended packetdata: " + packet.getData());
			}
		} catch (Exception e) {
			log.error("Error in sender thread: " + e.getMessage() + " - " + e.getStackTrace());
			
			//Socket für die nächte Verwendung wieder schließen!
			sendSocket.close();
			
			if (sendSocket.isClosed()) {
				log.debug("socket closed");
			}
		}
	}

}
