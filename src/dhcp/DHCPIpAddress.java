package dhcp;

import java.net.InetAddress;

public class DHCPIpAddress {
	
	private InetAddress currentIpAddress;
	private boolean isCurrentTaken;
	private byte[] taken;
	
	public DHCPIpAddress(InetAddress currIP) {
		this.currentIpAddress = currIP;
		this.isCurrentTaken = false;
		this.taken = null;
	}
	
	
	public boolean isTaken() {
		return isCurrentTaken;
	}
	
	public InetAddress getIpAddress() {
		return this.currentIpAddress;
	}
	
	public byte[] getIpAddressByte() {
		return this.getIpAddress().getAddress();
	}
	
	
	public byte[] getClientIdentifier() {
		return this.taken;
	}
	
	
	public synchronized InetAddress take(byte[] client_identifier) {
		this.isCurrentTaken = true;
		this.taken = client_identifier;
		
		return getIpAddress();
	}
	
	public synchronized void release() {
		this.isCurrentTaken = false;
		this.taken = null;
	}
}
