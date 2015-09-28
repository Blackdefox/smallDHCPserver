package dhcp.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPClient;
import dhcp.DHCPConstants;
import dhcp.DHCPProperties;
import dhcp.DHCPUtils;
import dhcp.packet.DHCPPacketHandler;

public class DHCPserver extends Observable implements Runnable{
	
	private static DHCPserver instance;
	
	public enum serverState {
		RUNNING,
		STOPPED,
		ERROR
	}
	
	boolean isInterrupted = false;
	
	private serverState currentState = serverState.STOPPED;
	
	static Logger log = LogManager.getLogger(DHCPserver.class.getName());
	
	private DHCPlistener current_dhcp_listener = null;
	private DHCPsender current_dhcp_sender = null;
	
	private InetAddress current_server_ip_address = null;
	private InetAddress current_broadcast_ip_address = null;
	
	private BlockingQueue<DatagramPacket> listener_queue = new LinkedBlockingQueue<>();
	private BlockingQueue<byte[]> sender_queue = new LinkedBlockingQueue<>();
	
	private DatagramSocket serverSocket;
	
	private List<DHCPClient> clientListe = new LinkedList<DHCPClient>();
	
	
	private DHCPserver(InetAddress server_ip_adress) {
		
		this.current_server_ip_address = server_ip_adress;
		
		log.info("creating new dhcp-server for " + current_server_ip_address.toString());
		
		try {
			this.current_broadcast_ip_address = InetAddress.getByName("255.255.255.255");
		} catch (Exception ex) {
			log.error("can't create broadcast ip");
			log.error(ex.toString());
		}
	}

	
	/**
	 * Main-Funktion des Threads.
	 */
	public void run() {
		this.currentState = serverState.RUNNING;
		
		try {
			serverSocket = new DatagramSocket(null);
			serverSocket.setReuseAddress(true);
			serverSocket.setBroadcast(true);
			serverSocket.bind(new InetSocketAddress(current_server_ip_address, DHCPConstants.DHCP_V4_SERVER_PORT));
			
			log.debug("Socket for " + current_server_ip_address.getHostAddress() + ":" + DHCPConstants.DHCP_V4_SERVER_PORT + " created");
			
			current_dhcp_listener = new DHCPlistener(this.serverSocket, listener_queue);
			current_dhcp_sender = new DHCPsender(this.serverSocket, current_broadcast_ip_address,DHCPConstants.DHCP_V4_CLIENT_PORT, this.sender_queue);
			
			DHCPPacketHandler handler = new DHCPPacketHandler(this, listener_queue, clientListe);
			
			current_dhcp_listener.addObserver(handler);
			
			Thread current_dhcp_listener_thread = new Thread(current_dhcp_listener);
			current_dhcp_listener_thread.start();
			
			this.addObserver(current_dhcp_sender);
			
			//this.setPriority(MIN_PRIORITY);
			
			//Auf das Ende der beiden Threads warten
			current_dhcp_listener_thread.join();
			
		} catch (Exception e) {
			log.error("Error in sender thread: " + e.getMessage() + " - " + e.getStackTrace());
		} finally {
			//Socket für die nächte Verwendung wieder schließen!
			serverSocket.close();
			
			if (serverSocket.isClosed()) {
				log.debug("socket for " + current_server_ip_address.getHostAddress() + ":" + DHCPConstants.DHCP_V4_SERVER_PORT + " closed");
			}
		}
	}
	
	
	public void interrupt() {
		isInterrupted = true;
		
		this.currentState = serverState.STOPPED;
	}

	public serverState getState() {
		return currentState;
	}
	
	
	public synchronized void sendResponse(byte[] data) {
		sender_queue.add(data);
		
		log.debug("new data: " + DHCPUtils.bytesToHex(data));
		
		//Änderungen setzen und Beobachter informieren
		setChanged();
	    notifyObservers(null);
	}
	
	
	public static synchronized DHCPserver getInstance () {
	    if (DHCPserver.instance == null) {
	    	DHCPserver.instance = new DHCPserver(DHCPProperties.getInstance().getServerAddress());
	    }
	    return DHCPserver.instance;
	}
	
	
	public BlockingQueue<DatagramPacket> getListener_queue() {
		return listener_queue;
	}


	public BlockingQueue<byte[]> getSender_queue() {
		return sender_queue;
	}
	
	

}
