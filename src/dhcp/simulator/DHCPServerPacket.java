package dhcp.simulator;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPConstants;
import dhcp.DHCPUtils;
import dhcp.options.DHCPOption;
import dhcp.packet.DHCPPacketHandler;

public class DHCPServerPacket {

	static Logger log = LogManager.getLogger(DHCPPacketHandler.class.getName());
	
	
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
			log.warn("DHCP-Paket kommt nicht von einem Server!");
		} else {
			log.error("Kein gültiges DHCP-Paket!");
		}
				
		return (byte)0;
	}
	
	
	public byte getOp() {
		return op;
	}

	public byte getHtype() {
		return htype;
	}

	public byte getHlen() {
		return hlen;
	}

	public byte getHops() {
		return hops;
	}

	public byte[] getXid() {
		return xid;
	}

	public byte[] getSecs() {
		return secs;
	}

	public byte[] getFlags() {
		return flags;
	}

	public byte[] getCiaddr() {
		return ciaddr;
	}

	public byte[] getYiaddr() {
		return yiaddr;
	}

	public byte[] getSiaddr() {
		return siaddr;
	}

	public byte[] getGiaddr() {
		return giaddr;
	}

	public byte[] getChaddr() {
		return chaddr;
	}

	public byte[] getSname() {
		return sname;
	}

	public byte[] getFile() {
		return file;
	}

	public byte[] getMagic_cookie() {
		return magic_cookie;
	}

	public List<DHCPOption> getOptions() {
		return optionList;
	}

	public byte getDhcp_message_type() {
		return dhcp_message_type;
	}

	public String getHostname() {
		return host_name;
	}

	public byte[] getClientIdentifier() {
		return client_identifier;
	}
	
}
