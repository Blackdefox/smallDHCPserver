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
 *          |  21.09.2015        |  Add new timestamps or Discovery,
 *          |					 |  Request, Inform and Release.
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * Class for representing Clients. 
 * 
 * <h4>Notes</h4>
 * none
 * 
 * @author      Florian Behrend, s60119@beuth-hochschule.de
 * @version     Version 1.0
 */

package dhcp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import dhcp.options.DHCPOption;
import dhcp.packet.DHCPpacket;

public class DHCPClient {

	private byte[] hardware_address = new byte[16];
	
	private byte[] current_ip_address = new byte[4];
	private byte[] current_subnet_mask = new byte[4];
	private byte[] current_xid = new byte[4];
	
	private byte[] current_standard_gateway = new byte[4];
	private byte[] current_dhcp_server = new byte[4];
	private byte[] current_dns_server = new byte[4];
	
	private String current_host_name;
	private byte[] current_client_identifier;
	
	List<DHCPOption> current_client_options;
	
	private long current_client_leasetime_secs;
	
	private LocalDateTime current_client_create_timestamp;
	private LocalDateTime current_client_leasetime;
	
	private LocalDateTime last_discovery_request_timestamp;
	private LocalDateTime last_request_request_timestamp;
	private LocalDateTime last_inform_request_timestamp;
	private LocalDateTime last_release_request_timestamp;
	
	private short current_discovery_counter = 0;
	private short current_request_counter = 0;
	private short current_inform_counter = 0;
	private short current_release_counter = 0;
	
	
	public DHCPClient(DHCPpacket currentPacket) {
		this(currentPacket.getChaddr(), currentPacket.getXid());
		
		current_client_options = currentPacket.getOptions();
	}
	
	public DHCPClient(byte[] mac_address, byte[] xid) {
		this.hardware_address = mac_address;
		this.current_xid = xid;

		current_ip_address = null;
		current_subnet_mask = null;
		
		current_host_name = "";
		current_client_identifier = null;
		
		current_client_create_timestamp = LocalDateTime.now();
		current_client_leasetime = null;
		
		last_discovery_request_timestamp = null;
		last_request_request_timestamp = null;
		last_inform_request_timestamp = null;
		last_release_request_timestamp = null;
	}
	
	
	public byte[] getHardware_address() {
		return hardware_address;
	}

	public byte[] getCurrent_xid() {
		return current_xid;
	}

	public void setCurrent_xid(byte[] data) {
		this.current_xid = data;
	}
	
	public byte[] getCurrent_ip_address() {
		return current_ip_address;
	}

	public void setCurrent_ip_address(byte[] current_ip_address) {
		this.current_ip_address = current_ip_address;
	}

	public byte[] getCurrent_subnet_mask() {
		return current_subnet_mask;
	}

	public void setCurrent_subnet_mask(byte[] current_subnet_mask) {
		this.current_subnet_mask = current_subnet_mask;
	}

	public byte[] getCurrent_standard_gateway() {
		return current_standard_gateway;
	}

	
	public void setCurrent_standard_gateway(byte[] current_standard_gateway) {
		this.current_standard_gateway = current_standard_gateway;
	}

	public byte[] getCurrent_dhcp_server() {
		return current_dhcp_server;
	}

	public void setCurrent_dhcp_server(byte[] current_dhcp_server) {
		this.current_dhcp_server = current_dhcp_server;
	}

	public byte[] getCurrent_dns_server() {
		return current_dns_server;
	}

	public void setCurrent_dns_server(byte[] current_dns_server) {
		this.current_dns_server = current_dns_server;
	}
	
	public String getHostname() {
		return current_host_name;
	}

	public void setHostname(String current_host_name) {
		this.current_host_name = current_host_name;
	}

	public byte[] getClientIdentifier() {
		return current_client_identifier;
	}

	public void setClientIdentifier(byte[] current_client_identifier) {
		this.current_client_identifier = current_client_identifier;
	}

	
	
	public LocalDateTime getTime() {
		return current_client_leasetime;
	}
	
	public void setLeaseTime(long secs) {
		current_client_leasetime_secs = secs;
		current_client_leasetime = LocalDateTime.now().plusSeconds(secs);
	}
	
	public long getLeaseTime() {
		return current_client_leasetime_secs;
	}
	
	public boolean getLeftIpTime() {
		if (current_client_leasetime == null) {
			return false;
		}
		
		return LocalDateTime.now().isAfter(current_client_leasetime);
	}

	
	public void setDiscoveryTimestamp() {
		this.last_discovery_request_timestamp = LocalDateTime.now();
	}
	
	public void setRequestTimestamp() {
		this.last_discovery_request_timestamp = LocalDateTime.now();
	}
	
	public void setInformTimestamp() {
		this.last_discovery_request_timestamp = LocalDateTime.now();
	}
	
	public void setReleaseTimestamp() {
		this.last_discovery_request_timestamp = LocalDateTime.now();
	}
	
	
	public long getDiscoverySeconds() {
		if (last_discovery_request_timestamp != null) {
			return Duration.between(last_discovery_request_timestamp, LocalDateTime.now()).toMinutes()/60;
		}
		
		return 0;
	}
	
	public long getRequestSeconds() {
		if (last_request_request_timestamp != null) {
			return Duration.between(last_request_request_timestamp, LocalDateTime.now()).toMinutes()/60;
		}
		
		return 0;
	}
	
	public long getInformSeconds() {
		if (last_inform_request_timestamp != null) {
			return Duration.between(last_inform_request_timestamp, LocalDateTime.now()).toMinutes()/60;
		}
		
		return 0;
	}
	
	public long getReleaseSeconds() {
		if (last_release_request_timestamp != null) {
			return Duration.between(last_release_request_timestamp, LocalDateTime.now()).toMinutes()/60;
		}
		
		return 0;
	}
	
	
	public void countDiscovery() {
		this.current_discovery_counter += 1;
	}
	
	public void countRequest() {
		this.current_request_counter += 1;
	}
	
	public void countInform() {
		this.current_inform_counter += 1;
	}
	
	public void countRelease() {
		this.current_release_counter += 1;
	}
	
	public short getDiscoveryCounter() {
		return current_discovery_counter;
	}

	public short getRequestCounter() {
		return current_request_counter;
	}

	public short getInformCounter() {
		return current_inform_counter;
	}

	public short getReleaseCounter() {
		return current_release_counter;
	}
	
	

	//Methode um zwei Clients vergleichen zu können
	@Override
    public boolean equals(Object object) {
        boolean equClient = false;

        if (object != null && object instanceof DHCPClient) {
        	equClient = Arrays.equals(this.getHardware_address(), ((DHCPClient)object).getHardware_address());
        }
        
        return equClient;
    }
	
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("information for client:\n");
		sb.append("- client created: " + current_client_create_timestamp.toString() + "\n");
		sb.append("- hardware-address: " + String.format("%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X", hardware_address[0], hardware_address[1], hardware_address[2], hardware_address[3], hardware_address[4], hardware_address[5], hardware_address[6], hardware_address[7]) + "\n");
		sb.append("- XID: " + DHCPUtils.bytesToHex(current_xid) + "\n");
		
		//Die IP wird nur ausgegeben wenn sie auch belegt worden ist!
		if (current_ip_address != null)	{
			sb.append("IP-Address: " + current_ip_address[0] + "." + current_ip_address[1] + "." + current_ip_address[2] + "." + current_ip_address[3] + "\n");
		}
		
		//Die Subnetmaske wird nur ausgegeben wenn sie auch belegt worden ist!
		if (current_subnet_mask != null) {
			sb.append("Subnetmask: " + current_subnet_mask[0] + "." + current_subnet_mask[1] + "." + current_subnet_mask[2] + "." + current_subnet_mask[3] + "\n");
		}
		
		if (current_client_leasetime != null) {
			sb.append("Leasetime: " + current_client_leasetime.toString() + "\n");
		}
		
		sb.append("- hostname: " + current_host_name);
		
		if (current_client_identifier != null) {
			sb.append("- client-identifier: " + DHCPUtils.bytesToHex(current_client_identifier));
		}
		
		return sb.toString();
	}
	
}
