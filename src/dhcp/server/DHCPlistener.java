package dhcp.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPUtils;

public class DHCPlistener extends Observable implements Runnable {
	
	public enum listenerState {
		RUNNING,
		STOPPED,
		ERROR
	}
	
	boolean isInterrupted = false;
	
	private listenerState currentState = listenerState.STOPPED;
	
	private DatagramSocket clientSocket;
	
	static Logger log = LogManager.getLogger(DHCPlistener.class.getName());
	
	//Buffer (Threadsicher)
	private BlockingQueue<DatagramPacket> receiveQueue;
	
	
	/**
	 * Konstruktor der Klasse UPDlistener.
	 * 
	 * @param address		IP-Adresse der simulierten DFI-Anlage. IP-Adresse vom Typ InetAddress
	 * @param port			Port der simulierten DFI-Anlage. Port vom Typ Integer
	 * @param receiveQueue	Queue für die Empfangenden Datagramme.
	 */
	public DHCPlistener(DatagramSocket currentSocket, BlockingQueue<DatagramPacket> receiveQueue) {
		this.clientSocket = currentSocket;
		this.receiveQueue = receiveQueue;
	}

	
	/**
	 * Main-Funktion des Threads.
	 */
	public void run() {
		try {
			log.info("listener thread for " + clientSocket.getLocalAddress() + ":" + clientSocket.getLocalPort() + " was started");
			
			this.currentState = listenerState.RUNNING;
			
			//Packet für das UDP-Datagramm (UDP = max 65535 Byte lang)
			DatagramPacket receivePacket = new DatagramPacket (new byte[65535], 65535);
			
			while (!isInterrupted) {
				try {
					//Packet in der Endlosschleife empfangen.
					clientSocket.receive(receivePacket);
					
					//Empfangende Daten in die Queue eintragen
					receiveQueue.put(receivePacket);
					
					//Änderungen setzen und Beobachter informieren
					setChanged();
				    notifyObservers(null);

					log.debug("Packet with lenght " + receivePacket.getLength() + " received from " + receivePacket.getAddress());
					log.trace("Received pecketdata: " + DHCPUtils.bytesToHex(receivePacket.getData()));
				} catch (SocketTimeoutException ste) {
					//Damit der Timeout uns nicht weiter stört
					log.trace(ste.toString());
				}
			}
		} catch (Exception e) {
			log.error("Error in receiver thread: " + e.getMessage() + " - " + e.getStackTrace());
			this.currentState = listenerState.ERROR;
		} finally {
			//Socket immer schließen (auch wenn ein Fehler aufgetreten ist)
			clientSocket.close();
			
			if (clientSocket.isClosed()) {
				log.info("socket closed");
			}
		}
	}
	
	
	/**
	 * Unterbricht die Endlosschliefe des Empfängertreads.
	 * Es handelt sich hierbei um eine Nachbildung der Funktion aus der Klasse Tread.
	 */
	public void interrupt() {
		isInterrupted = true;
		
		this.currentState = listenerState.STOPPED;
	}
	
	
	/**
	 * Gibt den aktuellen Status des Empfängerthreads zurück.
	 * 
	 * @return currentState Aktueller Status des Empfängerthreads.
	 */
	//Gibt den Status des Empfängers zurück
	public listenerState getState() {
		return currentState;
	}
}
