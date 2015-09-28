package dhcp.simulator;

import java.time.LocalDateTime;

public class DHCPClientXid {
	
	private byte[] current_xid;
	private LocalDateTime current_create_timestamp;
	
	
	public DHCPClientXid(byte[] xid) {
		this.current_xid = xid;
		this.current_create_timestamp = LocalDateTime.now();
	}


	public byte[] getCurrent_xid() {
		return current_xid;
	}


	public LocalDateTime getCurrent_create_timestamp() {
		return current_create_timestamp;
	}
	
	

}
