package dhcp.simulator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPConstants;
import dhcp.DHCPProperties;
import dhcp.server.DHCPlistener;
import dhcp.server.DHCPsender;
import dhcp.simulator.DHCPServerPenetrationtest.penetrationTestType;

public class DHCPClientSimulator extends Observable implements Runnable {

	private static DHCPClientSimulator instance;
	
	public enum simulatorState {
		RUNNING,
		STOPPED,
		ERROR
	}
	
	boolean isInterrupted = false;
	
	private simulatorState currentState = simulatorState.STOPPED;
	
	static Logger log = LogManager.getLogger(DHCPClientSimulator.class.getName());
	
	
	//Sender und Listener für den Simulator
	private DHCPlistener current_client_simulator_dhcp_listener = null;
	private DHCPsender current_client_simulator_dhcp_sender = null;
	
	//IP- / und Broadcast-Adresse des Simulators
	private InetAddress current_simulator_ip_address = null;
	private InetAddress current_broadcast_ip_address = null;
	
	//Port für den Empfang und den Versand der DHCP-Nachrichten
	private int current_simulator_port = DHCPConstants.DHCP_V4_CLIENT_PORT;
	private int current_server_port = DHCPConstants.DHCP_V4_SERVER_PORT;
	
	
	private BlockingQueue<DatagramPacket> listener_queue = new LinkedBlockingQueue<>();
	private BlockingQueue<byte[]> sender_queue = new LinkedBlockingQueue<>();
	
	private DatagramSocket simulatorSocket;
	
	DHCPServerPenetrationtest penTest1;
	DHCPServerPenetrationtest penTest2;
	
	
	private DHCPClientSimulator(InetAddress simulator_ip_adress) {
		this.current_simulator_ip_address = simulator_ip_adress;
		
		log.info("creating new dhcp-clint-simulator for " + current_simulator_ip_address.toString() + ":" + current_simulator_port);
		
		try {
			this.current_broadcast_ip_address = InetAddress.getByName("255.255.255.255");
			log.debug("broadcast ip created");
		} catch (Exception ex) {
			log.error("can't create broadcast ip");
			log.error(ex.toString());
		}
	}
	
	public static synchronized DHCPClientSimulator getInstance () {
	    if (DHCPClientSimulator.instance == null) {
	    	DHCPClientSimulator.instance = new DHCPClientSimulator(DHCPProperties.getInstance().getServerAddress());
	    }
	    
	    return DHCPClientSimulator.instance;
	}
	
	
	/**
	 * Main-Funktion des Threads.
	 */
	public void run() {
		try {
			simulatorSocket = new DatagramSocket(null);
			simulatorSocket.setReuseAddress(true);
			simulatorSocket.setBroadcast(true);
			simulatorSocket.bind(new InetSocketAddress(current_simulator_ip_address, current_simulator_port));
			
			log.debug("socket for " + current_simulator_ip_address.getHostAddress() + ":" + current_simulator_port + " created");
			
			
			current_client_simulator_dhcp_listener = new DHCPlistener(simulatorSocket, listener_queue);
			current_client_simulator_dhcp_sender = new DHCPsender(simulatorSocket, current_broadcast_ip_address, current_server_port, sender_queue);
			
			penTest1 = new DHCPServerPenetrationtest(penetrationTestType.DHCPDISCOVER, 100, 300, this);
			penTest2 = new DHCPServerPenetrationtest(penetrationTestType.DHCPREQUEST, 100, 300, this);
			
			DHCPServerPenetrationtestHandler handler = new DHCPServerPenetrationtestHandler(this, listener_queue, penTest1.getCurrentListOfXids());
			
			current_client_simulator_dhcp_listener.addObserver(handler);
			
			Thread current_client_simulator_dhcp_listener_thread = new Thread(current_client_simulator_dhcp_listener);
			current_client_simulator_dhcp_listener_thread.start();
			
			this.addObserver(current_client_simulator_dhcp_sender);
			
			//Auf das Ende der Threads warten
			current_client_simulator_dhcp_listener_thread.join();
			
		} catch (SocketException se) {
			log.error("Error in sender thread: " + se.getMessage() + " - " + se.getStackTrace());
		} catch (Exception ex) {
			log.error(ex.toString());
		} finally {
			//Socket für die nächste Verwendung wieder schließen!
			simulatorSocket.close();
			
			if (simulatorSocket.isClosed()) {
				log.debug("Socket for " + current_simulator_ip_address.getHostAddress() + ":" + current_simulator_port + " closed");
			}
		}
	}
	
	public void interrupt() {
		isInterrupted = true;
		
		this.currentState = simulatorState.STOPPED;
	}

	public simulatorState getState() {
		return currentState;
	}
	
	
	public synchronized void sendPacket(byte[] data) {
		sender_queue.add(data);
		
		//Änderungen setzen und Beobachter informieren
		setChanged();
	    notifyObservers(null);
	}
	
	
	public void startStresstest_DHCPACK(long delay, int loops) {
		//penTest = new DHCPServerPenetrationtest(penetrationTestType.DHCPDISCOVER, delay, loops, this);
		
		Thread t1 = new Thread(penTest1);
		t1.start();
		
	}
	
	public void startPenetrationTest(penetrationTestType type) {
		Thread t1;
		
		if (type == penetrationTestType.DHCPDISCOVER) {
			t1 = new Thread(penTest1);
		} else {
			t1 = new Thread(penTest2);
		}
		
		t1.start();
	}
	
}
