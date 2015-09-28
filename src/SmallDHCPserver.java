
import dhcp.DHCPProperties;
import dhcp.server.DHCPserver;
import dhcp.simulator.DHCPClientSimulator;

public class SmallDHCPserver {

	public static void main(String[] args) {
		
		//Die aktuellen Einstellungen einlesen
		DHCPProperties sp = DHCPProperties.getInstance();
		
		System.out.println("DHCP-Server is running!");
		
		/*
		InetAddress lhost = null;
		InetAddress broadcast = null;
		
		
		try {
			lhost = InetAddress.getByName("192.168.0.1");
			lhost = InetAddress.getByName("192.168.178.41");
			lhost = InetAddress.getByName("141.64.174.40");
			broadcast = InetAddress.getByName("255.255.255.255");
			lhost = InetAddress.getByName("141.64.167.82");
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		*/
		
		
		DHCPserver myServer = DHCPserver.getInstance();
		
		DHCPClientSimulator mySimulator = DHCPClientSimulator.getInstance();
		
		
		ConsolThread ct = new ConsolThread(myServer, mySimulator);
		ct.start();
	}
}
