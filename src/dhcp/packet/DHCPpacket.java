/**
 * Copyright 2015 (C)
 *  
 * Created on : 20.06.2015 
 * Authors    : Florian Behrend
 * 				Christoph Palme
 * Email      : s60119@beuth-hochschule.de
 * 
 *----------------------------------------------------------------------------- 
 * Revision History (Release 1.1.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |
 *          |  20.06.2015        |  Initial Create.
 *-----------------------------------------------------------------------------
 * 1.1      |  Florian Behrend   |
 *          |  12.07.2015        |  Various fixes and many simplifications.
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
 * @version     Version 1.1
 */

package dhcp.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPClient;
import dhcp.DHCPConstants;
import dhcp.DHCPProperties;
import dhcp.DHCPUtils;
import dhcp.options.DHCPBroadcastAddressOption;
import dhcp.options.DHCPDomainNameOption;
import dhcp.options.DHCPDomainNameServerOption;
import dhcp.options.DHCPEndOption;
import dhcp.options.DHCPLeaseTimeOption;
import dhcp.options.DHCPMsgTypeOption;
import dhcp.options.DHCPNtpServerOption;
import dhcp.options.DHCPOption;
import dhcp.options.DHCPRequestedAddressOption;
import dhcp.options.DHCPRoutersOption;
import dhcp.options.DHCPServerIdentifierOption;
import dhcp.options.DHCPSubnetMaskOption;
import dhcp.server.DHCPserver;

public class DHCPpacket {
	
	DHCPProperties sp = DHCPProperties.getInstance();
	
	static Logger log = LogManager.getLogger(DHCPserver.class.getName());
	
	private byte op;						// OP-Feld (1 Byte)
	private byte htype;						// Hardwaretype-Feld (1 Byte)
	private byte hlen;						// (1 Byte)
	private byte hops;						// (1 Byte)
	private byte[] xid = new byte[4];		// (4 Byte)
	private byte[] secs = new byte[2];		// (2 Byte)
	private byte[] flags = new byte[2];		// (2 Byte)
	private byte[] ciaddr = new byte[4];	// Client-IP-Adresse (4 Byte)
	private byte[] yiaddr = new byte[4];	// Eigene IP-Adresse (4 Byte)
	private byte[] siaddr = new byte[4];	// Server-IP-Adresse (4 Byte)
	private byte[] giaddr = new byte[4];	// Relay-Agent-IP-Adresse (4 Byte)
	private byte[] chaddr = new byte[16];	// Client-MAC-Adresse (16 Byte)
	private byte[] sname = new byte[64];	// Name des DHCP-Servers (64 Byte)
	private byte[] file = new byte[128];	// optional (128 Byte)
	private byte[] magic_cookie = new byte[4];
	private byte[] options;					// optional (variable Byte Anzahl)
	
	private byte dhcp_message_type;			// Type des Pakets
	
	List<DHCPOption> optionList = new ArrayList<DHCPOption>();
	
	//Daten aus den Options eines Pakets
	private String host_name;
	private byte[] client_identifier;
	
	
	public DHCPpacket() {
		//Es wird ein leeres Objekt erzeugt. Wahrscheinlich soll ein neues Paket erstellt werden!
	}
	
	public DHCPpacket(byte[] data) {
		//Wenn ein Paket mit Daten erzeugt wird, müssen diese sofort Decodiert werden!
	
		decode(data, data.length);
	}
	
	
	public byte[] encode(byte type, DHCPClient currentClient) {
		
		ByteBuffer edata = null;// = ByteBuffer.allocateDirect(300);
		
		log.debug("encoding new dhcp message");
		
		switch(type){
		case DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER:
			log.debug("creating new \"DHCPOFFER\" packet");
			log.trace("--> add static information to packet");
			
			//Statischen Teil des Pakets erzeugen
			edata = createPacketData(DHCPConstants.DHCP_V4_OP_CODE_REPLY,
									 currentClient.getCurrent_xid(),
									 new byte[] {(byte)0, (byte)0},
									 new byte[] {(byte)0, (byte)0},
									 new byte[] {(byte)0, (byte)0, (byte)0, (byte)0},
									 currentClient.getCurrent_ip_address(),
									 DHCPConstants.LOCAL_IP_ADDRESS,
									 new byte[] {(byte)0, (byte)0, (byte)0, (byte)0},
									 currentClient.getHardware_address(),
									 new byte[64],
									 new byte[128]);
			
			
			log.trace("--> add options to packet");
			
			//Optionen in das Paket einfügen
			//-------------------------------------------------------------------------------------------------------
			//Was für eine Nachricht wird versendet
			edata.put((new DHCPMsgTypeOption(DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER)).encode());
			log.trace("----> add message type to packet" + "(" + edata.position() + ")");
			
			//Identifikation des Servers
			edata.put((new DHCPServerIdentifierOption(DHCPConstants.LOCAL_IP_ADDRESS)).encode());
			log.trace("----> add server identifier to packet" + "(" + edata.position() + ")");
			
			//IP Lease Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getLeasetime())).encode());
			log.trace("----> add ip lease time to packet" + "(" + edata.position() + ")");
			
			//IP Renewal Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getRenewaltime())).encode());
			log.trace("----> add ip renewal time to packet" + "(" + edata.position() + ")");
			
			//IP Rebinding Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getRebindingtime())).encode());
			log.trace("----> add ip rebinding time to packet" + "(" + edata.position() + ")");
			
			//Subnetmaske
			edata.put((new DHCPSubnetMaskOption(currentClient.getCurrent_subnet_mask()).encode()));
			log.trace("----> add subnet mask to packet" + "(" + edata.position() + ")");
			
			//Routers
			edata.put((new DHCPRoutersOption(DHCPProperties.getInstance().getRouters())).encode());
			log.trace("----> add routers to packet" + "(" + edata.position() + ")");
			
			//Domain Name Servers
			edata.put((new DHCPDomainNameServerOption(DHCPProperties.getInstance().getNameservers())).encode());
			log.trace("----> add domain name servers to packet" + "(" + edata.position() + ")");
			
			//Domainname
			edata.put((new DHCPDomainNameOption(DHCPProperties.getInstance().getDomainName()).encode()));
			log.trace("----> add domain name to packet" + "(" + edata.position() + ")");
			
			//Broadcast-Adresse
			edata.put((new DHCPBroadcastAddressOption(DHCPProperties.getInstance().getBroadcastAddress())).encode());
			log.trace("----> add broadcast address to packet" + "(" + edata.position() + ")");
			
			//NTP-Server
			edata.put((new DHCPNtpServerOption(DHCPProperties.getInstance().getTimeServers())).encode());
			log.trace("----> add ntp-servers to packet" + "(" + edata.position() + ")");
			
			//-------------------------------------------------------------------------------------------------------
			
			//Paket abschließen
			edata.put((new DHCPEndOption()).encode());
			log.trace("--> add packet end");
			
			log.debug("creating \"DHCPOFFER\" packet completed");
			
			break;
		case DHCPConstants.DHCP_V4_MESSAGE_TYPE_ACK:
			log.debug("creating new \"DHCPACK\" packet");
			log.trace("--> add static information to packet");

			//Statischen Teil des Pakets erzeugen
			edata = createPacketData(DHCPConstants.DHCP_V4_OP_CODE_REPLY,
									 currentClient.getCurrent_xid(),
									 new byte[] {(byte)0, (byte)0},
									 new byte[] {(byte)0, (byte)0},
									 new byte[] {(byte)0, (byte)0, (byte)0, (byte)0},
									 currentClient.getCurrent_ip_address(),
									 DHCPConstants.LOCAL_IP_ADDRESS,
									 new byte[] {(byte)0, (byte)0, (byte)0, (byte)0},
									 currentClient.getHardware_address(),
									 new byte[64],
									 new byte[128]);
			
			log.trace("--> add options to packet");
			
			//Dynamische Optionen in das Paket einfügen
			//-------------------------------------------------------------------------------------------------------
			//Was für eine Nachricht wird versendet
			edata.put((new DHCPMsgTypeOption(DHCPConstants.DHCP_V4_MESSAGE_TYPE_ACK)).encode());
			log.trace("----> add message type to packet" + "(" + edata.position() + ")");
			
			//Identifikation des Servers
			edata.put((new DHCPServerIdentifierOption(DHCPConstants.LOCAL_IP_ADDRESS)).encode());
			log.trace("----> add server identifier to packet" + "(" + edata.position() + ")");
			
			//IP Lease Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getLeasetime())).encode());
			log.trace("----> add ip lease time to packet" + "(" + edata.position() + ")");
			
			//IP Renewal Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getRenewaltime())).encode());
			log.trace("----> add ip renewal time to packet" + "(" + edata.position() + ")");
			
			//IP Rebinding Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getRebindingtime())).encode());
			log.trace("----> add ip rebinding time to packet" + "(" + edata.position() + ")");
			
			//Subnetmaske
			edata.put((new DHCPSubnetMaskOption(currentClient.getCurrent_subnet_mask()).encode()));
			log.trace("----> add subnet mask to packet" + "(" + edata.position() + ")");
			
			//Routers
			edata.put((new DHCPRoutersOption(DHCPProperties.getInstance().getRouters())).encode());
			log.trace("----> add routers to packet" + "(" + edata.position() + ")");
			
			//Domain Name Servers
			edata.put((new DHCPDomainNameServerOption(DHCPProperties.getInstance().getNameservers())).encode());
			log.trace("----> add domain name servers to packet" + "(" + edata.position() + ")");
			
			//Domainname
			edata.put((new DHCPDomainNameOption(DHCPProperties.getInstance().getDomainName()).encode()));
			log.trace("----> add domain name to packet" + "(" + edata.position() + ")");
			
			//Broadcast-Adresse
			edata.put((new DHCPBroadcastAddressOption(DHCPProperties.getInstance().getBroadcastAddress())).encode());
			log.trace("----> add broadcast address to packet" + "(" + edata.position() + ")");
			
			//NTP-Server
			edata.put((new DHCPNtpServerOption(DHCPProperties.getInstance().getTimeServers())).encode());
			log.trace("----> add ntp-servers to packet" + "(" + edata.position() + ")");
			
			//-------------------------------------------------------------------------------------------------------
			
			//Paket abschließen
			edata.put((new DHCPEndOption()).encode());
			log.trace("--> add packet end");
			
			log.debug("creating \"DHCPACK\" packet completed");
			
			break;
		case DHCPConstants.DHCP_V4_MESSAGE_TYPE_NAK:
			log.debug("creating new \"DHCPNAK\" packet");
			log.trace("--> add static information to packet");
			
			//Statischen Teil des Pakets erzeugen
			edata = createPacketData(DHCPConstants.DHCP_V4_OP_CODE_REPLY,
									 currentClient.getCurrent_xid(),
									 new byte[] {(byte)0, (byte)0},
									 new byte[] {(byte)0, (byte)0},
									 new byte[] {(byte)0, (byte)0, (byte)0, (byte)0},
									 currentClient.getCurrent_ip_address(),
									 DHCPConstants.LOCAL_IP_ADDRESS,
									 new byte[] {(byte)0, (byte)0, (byte)0, (byte)0},
									 currentClient.getHardware_address(),
									 new byte[64],
									 new byte[128]);
			
			log.trace("--> add options to packet");
			
			//Dynamische Optionen in das Paket einfügen
			//-------------------------------------------------------------------------------------------------------
			//Was für eine Nachricht wird versendet
			edata.put((new DHCPMsgTypeOption(DHCPConstants.DHCP_V4_MESSAGE_TYPE_NAK)).encode());
			log.trace("----> add message type to packet" + "(" + edata.position() + ")");
			
			//Identifikation des Servers
			edata.put((new DHCPServerIdentifierOption(DHCPConstants.LOCAL_IP_ADDRESS)).encode());
			log.trace("----> add server identifier to packet" + "(" + edata.position() + ")");
			
			//IP Lease Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getLeasetime())).encode());
			log.trace("----> add ip lease time to packet" + "(" + edata.position() + ")");
			
			//IP Renewal Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getRenewaltime())).encode());
			log.trace("----> add ip renewal time to packet" + "(" + edata.position() + ")");
			
			//IP Rebinding Time
			edata.put((new DHCPLeaseTimeOption(DHCPProperties.getInstance().getRebindingtime())).encode());
			log.trace("----> add ip rebinding time to packet" + "(" + edata.position() + ")");
			
			//Subnetmaske
			edata.put((new DHCPSubnetMaskOption(currentClient.getCurrent_subnet_mask()).encode()));
			log.trace("----> add subnet mask to packet" + "(" + edata.position() + ")");
			
			//Routers
			edata.put((new DHCPRoutersOption(DHCPProperties.getInstance().getRouters())).encode());
			log.trace("----> add routers to packet" + "(" + edata.position() + ")");
			
			//Domain Name Servers
			edata.put((new DHCPDomainNameServerOption(DHCPProperties.getInstance().getNameservers())).encode());
			log.trace("----> add domain name servers to packet" + "(" + edata.position() + ")");
			
			//Domainname
			edata.put((new DHCPDomainNameOption(DHCPProperties.getInstance().getDomainName()).encode()));
			log.trace("----> add domain name to packet" + "(" + edata.position() + ")");
			
			//Broadcast-Adresse
			edata.put((new DHCPBroadcastAddressOption(DHCPProperties.getInstance().getBroadcastAddress())).encode());
			log.trace("----> add broadcast address to packet" + "(" + edata.position() + ")");
			
			//NTP-Server
			edata.put((new DHCPNtpServerOption(DHCPProperties.getInstance().getTimeServers())).encode());
			log.trace("----> add ntp-servers to packet" + "(" + edata.position() + ")");
			
			//-------------------------------------------------------------------------------------------------------
			
			//Paket abschließen
			edata.put((new DHCPEndOption()).encode());
			log.trace("--> add packet end");
			
			log.debug("creating \"DHCPACK\" packet completed");
			
			break;
		default:
			log.error("type of message not handled: " + Byte.toUnsignedInt(type));
			break;
		}
		
		if (edata != null) {
			//Bytearray aus dem Bytebuffer erzeugen
			edata.clear();
			byte[] bytes = new byte[edata.capacity()];
			edata.get(bytes,0,bytes.length);
			
			log.debug("Daten: " + DHCPUtils.bytesToHex(bytes));
			
			return bytes;
		}
			
		return null;
	}
	
	
	/**
	 * Methode zum erstellen eines DHCP-Pakets
	 * 
	 * @param type			Typ des DHCP-Pakets.
	 * @param xid			XID die vom Client empfangen wurde.
	 */
	public byte[] encode(byte type, byte[] xid) {
		
		log.debug("creating new dhcp-packet for xid: " + DHCPUtils.bytesToHex(xid));
		
		ByteBuffer edata = ByteBuffer.allocateDirect(300);
		
		switch(type){
		case DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER:
			edata.put(DHCPConstants.DHCP_V4_OP_CODE_REQUEST); 
			edata.put(DHCPConstants.DHCP_V4_HTYPE_ETHERNET);
			edata.put((byte)6);
			edata.put((byte)0);
			edata.put(xid);

			edata.put(new byte[] {(byte)0, (byte)0}); //Sekunden
			edata.put(new byte[] {(byte)0, (byte)0}); //Flags
			
			edata.put((byte)0);//client IP Teil1
			edata.put((byte)0);//client IP Teil2
			edata.put((byte)0);//client IP Teil3
			edata.put((byte)0);//client IP Teil4
			
			edata.put(DHCPConstants.LOCAL_IP_ADDRESS);
			edata.put(DHCPConstants.LOCAL_IP_ADDRESS);
			
			edata.put((byte)0);//gateway Teil1
			edata.put((byte)0);//gateway Teil2
			edata.put((byte)0);//gateway Teil3
			edata.put((byte)0);//gateway Teil4
			
			//MAC-Adresse des Clients (16 Bit)
			edata.put((byte)0);//1
			edata.put((byte)36);//2
			edata.put((byte)215);//3
			edata.put((byte)160);//4
			edata.put((byte)164);//5
			edata.put((byte)120);//6
			edata.put((byte)0);//7
			edata.put((byte)0);//8
			edata.put((byte)0);//9
			edata.put((byte)0);//10
			edata.put((byte)0);//11
			edata.put((byte)0);//12
			edata.put((byte)0);//13
			edata.put((byte)0);//14
			edata.put((byte)0);//15
			edata.put((byte)0);//16
			
			edata.put(new byte[64]); //sname
			edata.put(new byte[128]); //filename
			
			edata.putInt(DHCPConstants.DHCP_V4_MAGIC_COOKIE);
//			edata.put(DHCPConstants.DHCP_V4_OPTION_DHCP_MESSAGE_TYPE);//Option DHCP offer
//			edata.put((byte)1);//Länge der option
//			edata.put(DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER);

			edata.put((new DHCPMsgTypeOption(DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER)).encode());
			edata.put((new DHCPEndOption()).encode());
			
			break;
		case DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST:
			edata.put(DHCPConstants.DHCP_V4_OP_CODE_REQUEST); 
			edata.put(DHCPConstants.DHCP_V4_HTYPE_ETHERNET);
			edata.put((byte)6);
			edata.put((byte)0);
			edata.put(xid);
			
			edata.put(new byte[] {(byte)0, (byte)0}); //Sekunden
			edata.put(new byte[] {(byte)0, (byte)0}); //Flags
			
			edata.put((byte)0);//client IP Teil1
			edata.put((byte)0);//client IP Teil2
			edata.put((byte)0);//client IP Teil3
			edata.put((byte)0);//client IP Teil4
			
			edata.put((byte)0); //your IP Teil1
			edata.put((byte)0); //your IP Teil2
			edata.put((byte)0); //your IP Teil3
			edata.put((byte)0); //your IP Teil4
			
			edata.put(DHCPConstants.LOCAL_IP_ADDRESS); //Server IP
			
			edata.put((byte)0);//gateway Teil1
			edata.put((byte)0);//gateway Teil2
			edata.put((byte)0);//gateway Teil3
			edata.put((byte)0);//gateway Teil4
			
			//MAC-Adresse des Clients (16 Bit)
			edata.put((byte)0);//1
			edata.put((byte)36);//2
			edata.put((byte)215);//3
			edata.put((byte)160);//4
			edata.put((byte)164);//5
			edata.put((byte)120);//6
			edata.put((byte)0);//7
			edata.put((byte)0);//8
			edata.put((byte)0);//9
			edata.put((byte)0);//10
			edata.put((byte)0);//11
			edata.put((byte)0);//12
			edata.put((byte)0);//13
			edata.put((byte)0);//14
			edata.put((byte)0);//15
			edata.put((byte)0);//16
			
			edata.put(new byte[64]); //sname
			edata.put(new byte[128]); //filename
			
			edata.putInt(DHCPConstants.DHCP_V4_MAGIC_COOKIE);
			
			edata.put((new DHCPMsgTypeOption(DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST)).encode());
			edata.put((new DHCPRequestedAddressOption(new byte[] {(byte)192,(byte)168,(byte)1,(byte)100}).encode()));
			edata.put((new DHCPServerIdentifierOption(DHCPConstants.LOCAL_IP_ADDRESS)).encode());
			
			edata.put((new DHCPEndOption()).encode());
			break;
		default:
			//throw new Exception("Typ nicht gefunden");
			log.error("Typ nicht gefunden");
		}
		
		//Bytearray aus dem Bytebuffer erzeugen
		edata.clear();
		  byte[] bytes = new byte[edata.capacity()];
		  edata.get(bytes,0,bytes.length);
		
			return bytes;
	}
	
	private ByteBuffer createPacketData(byte op, byte[] xid, byte[] secs, 
										byte[] flags, byte[] ciaddr, byte[] yiaddr,	byte[] siaddr, byte[] giaddr,
										byte[] chaddr, byte[] sname, byte[] file) {
		/*
		 private byte op;						// OP-Feld (1 Byte)
		 private byte htype;					// Hardwaretype-Feld (1 Byte)
		 private byte hlen;						// (1 Byte)
		 private byte hops;						// (1 Byte)
		 private byte[] xid = new byte[4];		// (4 Byte)
		 private byte[] secs = new byte[2];		// (2 Byte)
	 	 private byte[] flags = new byte[2];	// (2 Byte)
		 private byte[] ciaddr = new byte[4];	// Client-IP-Adresse (4 Byte)
		 private byte[] yiaddr = new byte[4];	// Eigene IP-Adresse (4 Byte)
		 private byte[] siaddr = new byte[4];	// Server-IP-Adresse (4 Byte)
		 private byte[] giaddr = new byte[4];	// Relay-Agent-IP-Adresse (4 Byte)
		 private byte[] chaddr = new byte[16];	// Client-MAC-Adresse (16 Byte)
		 private byte[] sname = new byte[64];	// Name des DHCP-Servers (64 Byte)
		 private byte[] file = new byte[128];	// optional (128 Byte)
		 private byte[] magic_cookie = new byte[4];
		 private byte[] options;					// optional (variable Byte Anzahl)
		 */
		
		ByteBuffer edata = ByteBuffer.allocateDirect(500);
		
		edata.put(op);		// OP-Field
		
		//Feste Felder die hier nicht dynamisch vergeben werden
		//Da Ethernet der aktuelle Standard für Verbindungen ist gilt:
		edata.put(DHCPConstants.DHCP_V4_HTYPE_ETHERNET);	// HTYPE-Field
		edata.put((byte)6);	// HLEN-Field
		edata.put((byte)0);	// HOPS-Field
		
		edata.put(xid);		// XID of Client
		
		edata.put(secs);	// Sekunden
		edata.put(flags);	// Flags
		
		edata.put(ciaddr);	// CIADDR (Client IP address)
		edata.put(yiaddr);	// YIADDR (Your IP address)
		edata.put(siaddr);	// SIADDR (Server IP address)
		edata.put(giaddr);	// GIADDR (Gateway IP address)
		edata.put(chaddr);	// CHADDR (Client hardware address)
		
		edata.put(sname); //sname
		edata.put(file); //filename
		
		edata.putInt(DHCPConstants.DHCP_V4_MAGIC_COOKIE);
		
		return edata;
	}
	
	
	
	public byte decode(byte[] data, int len) {
		log.debug("Creating new DHCP-Packet with length of " + len + " bytes.");
				
				if (len > 0) {
					op = data[0];
					htype = data[1];
					hlen = data[2];
					hops = data[3];
					xid = Arrays.copyOfRange(data, 4, 8);
					secs = Arrays.copyOfRange(data, 8, 10);
					flags = Arrays.copyOfRange(data, 10, 12);
					ciaddr = Arrays.copyOfRange(data, 12, 16);
					yiaddr = Arrays.copyOfRange(data, 16, 20);
					siaddr = Arrays.copyOfRange(data, 20, 24);
					giaddr = Arrays.copyOfRange(data, 24, 28);
					chaddr = Arrays.copyOfRange(data, 28, 44);
					sname = Arrays.copyOfRange(data, 44, 108);
					file = Arrays.copyOfRange(data, 108, 236);
					magic_cookie = Arrays.copyOfRange(data, 236, 240);
					options = Arrays.copyOfRange(data, 240, len);
					
					//options auswerten
					try {
						//ByteBuffer tmp = ByteBuffer.wrap(options);
						//log.debug("ByteBufer: " + tmp.toString());
						
						//lokale Variablen für das Auswerten der Options erzeugen
						byte currentCode; //aktueller Code der DHCP-Option
						byte currentLen; //aktuelle Länge der DHCP-Option
						//ByteBuffer currentData; //aktuelle Daten der DHCP-Option
						
						//ByteBuffer aus dem Bytearray der Options erzeugen (lässt sich einfacher verarbeiten)
						ByteBuffer optionsBuffer = ByteBuffer.wrap(options);
						
						//ByteBuffer solange durchlaufen wie noch Bytes vorhanden sind
						while (optionsBuffer.hasRemaining()) {
							currentCode = optionsBuffer.get(); //Das erste Byte aus dem Buffer schneiden (immer der Code der Option)
							
							if (currentCode == DHCPConstants.DHCP_V4_OPTION_END) {
								break; //Ende der DHCP-Options gefunden
							} else {
								currentLen = optionsBuffer.get(); //Das nächste Byte aus dem Buffer schneiden (immer die Länge der Option)
								
								if (currentLen > 0) {
									byte[] tmpB = new byte[currentLen];
									//currentData = optionsBuffer.get(new byte[currentLen]); //aktuelle Daten der Option aus dem ByteBuffer schneiden
									
									for (int i = 0; i < currentLen; i++) {
										tmpB[i] = optionsBuffer.get();
									}
									
									//Option in die Liste einfügen
									optionList.add(new DHCPOption(currentCode, currentLen, tmpB));
									
									//Debuggen was wir gerade gefunden haben
									log.debug("Code: " + currentCode + " --> " + DHCPConstants.getDhcpV4OptionName(currentCode));
									log.debug("Length of current option: " + currentLen);
									log.debug("Data of current option: " + DHCPUtils.bytesToHex(tmpB));
									
									switch (currentCode) {
									case 12: //Hostname
										//Hostname des Clients von byte in String umwandeln
										this.host_name = new String(tmpB, "UTF-8");
										
										log.debug("plain hostname: " + host_name);
										
										break;
									case 53: //Typ der Nachricht
										
										break;
									case 55: //Parameterliste
										//Liste der Parameter übersetzen in lesbare Namen
										for (byte b : tmpB) {
											log.debug("--> " + DHCPConstants.getDhcpV4OptionName(b));
										}
										break;
									case 61: //Client-Identifier
										this.client_identifier = tmpB.clone();
										break;
									}
								}
							}
						}
					} catch (Exception ex) {
						log.debug(ex.toString());
					}
					
					
					
					
					if (log.isDebugEnabled()) {
						StringBuilder sb = new StringBuilder();
						sb.append("op-field:     " + op + "\n");
						sb.append("htype-field:  " + htype + "\n");
						sb.append("hlen-field:   " + hlen + "\n");
						sb.append("hops-field:   " + hops + "\n");
						sb.append("xid-field:    " + DHCPUtils.bytesToHex(xid) + "\n");
						sb.append("secs-field:   " + DHCPUtils.bytesToHex(secs) + "\n");
						sb.append("flags-field:  " + flags + "\n");
						sb.append("ciaddr-field: " + DHCPUtils.bytesToHex(ciaddr) + "\n");
						sb.append("yiaddr-field: " + DHCPUtils.bytesToHex(yiaddr) + "\n");
						sb.append("siaddr-field: " + DHCPUtils.bytesToHex(siaddr) + "\n");
						sb.append("giaddr-field: " + DHCPUtils.bytesToHex(giaddr) + "\n");
						sb.append("chaddr-field: " + DHCPUtils.bytesToHex(chaddr) + "\n");
						sb.append("sname-field:  " + new String(sname) + "\n");
						sb.append("file-field:   " + new String(file) + "\n");
						sb.append("magic-cookie-field:   " + DHCPUtils.bytesToHex(magic_cookie) + "\n");
						sb.append("options-field:   " + DHCPUtils.bytesToHex(options) + "\n");
						
						log.debug(sb.toString());
					}
					
				} else {
					System.out.println("Länge der Daten des DHCP-Pakets ist null.");
				}
				
				// Herausfinden um welchen Type von DHCP-Paket es sich handelt
				
				if (op == DHCPConstants.DHCP_V4_OP_CODE_REQUEST) { //Paket kommt vom Client
					log.debug("Bootrequest:");
					//options auswerten
					log.debug(options[0]);
					if (options[0] == 53) {
						log.debug("found DHCP Message Type option");
						
						byte mst = options[2];
						
						switch (mst) {
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER:
								log.debug("DHCP Discover");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER:
								log.debug("DHCP Offer");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST:
								log.debug("DHCP Request");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_DECLINE:
								log.debug("DHCP Decline");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_DECLINE;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_ACK:
								log.debug("DHCP Ack");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_ACK;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_NAK:
								log.debug("DHCP Nak");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_NAK;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_RELEASE:
								log.debug("DHCP Release");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_RELEASE;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_INFORM:
								log.debug("DHCP Inform");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_INFORM;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_FORCERENEW:
								log.debug("DHCP Forcerenew");					
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_FORCERENEW;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEQUERY:
								log.debug("DHCP Leasequery");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEQUERY;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEUNASSIGNED:
								log.debug("DHCP Leaseunassigned");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEUNASSIGNED;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEUNKNOWN:
								log.debug("DHCP Leaseunknown");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEUNKNOWN;
								
							case DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEACTIVE:
								log.debug("DHCP Leaseactive");
								return DHCPConstants.DHCP_V4_MESSAGE_TYPE_LEASEACTIVE;
						}
					}
					
				} else if (op == DHCPConstants.DHCP_V4_OP_CODE_REPLY) {
					log.warn("DHCP-Paket kommt nicht von einem Client!");
				} else {
					log.error("Kein gültiges DHCP-Paket!");
				}
				
				
				return (byte)0;
			}

	
	public byte getOp() {
		return op;
	}

	public void setOp(byte op) {
		this.op = op;
	}

	public byte getHtype() {
		return htype;
	}

	public void setHtype(byte htype) {
		this.htype = htype;
	}

	public byte getHlen() {
		return hlen;
	}

	public void setHlen(byte hlen) {
		this.hlen = hlen;
	}

	public byte getHops() {
		return hops;
	}

	public void setHops(byte hops) {
		this.hops = hops;
	}

	public byte[] getXid() {
		return xid;
	}

	public byte[] getSecs() {
		return secs;
	}

	public void setSecs(byte[] secs) {
		this.secs = secs;
	}

	public byte[] getFlags() {
		return flags;
	}

	public void setFlags(byte[] flags) {
		this.flags = flags;
	}

	public byte[] getCiaddr() {
		return ciaddr;
	}

	public void setCiaddr(byte[] ciaddr) {
		this.ciaddr = ciaddr;
	}

	public byte[] getYiaddr() {
		return yiaddr;
	}

	public void setYiaddr(byte[] yiaddr) {
		this.yiaddr = yiaddr;
	}

	public byte[] getSiaddr() {
		return siaddr;
	}

	public void setSiaddr(byte[] siaddr) {
		this.siaddr = siaddr;
	}

	public byte[] getGiaddr() {
		return giaddr;
	}

	public void setGiaddr(byte[] giaddr) {
		this.giaddr = giaddr;
	}

	public byte[] getChaddr() {
		return chaddr;
	}

	public byte[] getSname() {
		return sname;
	}

	public void setSname(byte[] sname) {
		this.sname = sname;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public byte[] getMagic_cookie() {
		return magic_cookie;
	}

	public void setMagic_cookie(byte[] magic_cookie) {
		this.magic_cookie = magic_cookie;
	}

	public List<DHCPOption> getOptions() {
		return optionList;
	}

	public void setOptions(byte[] options) {
		this.options = options;
	}

	public byte getDhcp_message_type() {
		return dhcp_message_type;
	}

	public void setDhcp_message_type(byte dhcp_message_type) {
		this.dhcp_message_type = dhcp_message_type;
	}

	public String getHostname() {
		return host_name;
	}

	public byte[] getClientIdentifier() {
		return client_identifier;
	}
	
	

	
	
}
