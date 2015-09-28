/**
 * Copyright 2015 (C)
 *  
 * Created on : 22.08.2015 
 * Authors    : Florian Behrend (fb)
 * 				Christoph Palme (cp)
 * Email      : fb: s60119@beuth-hochschule.de
 * 				cp: s-----@beuth-hochschule.de
 * 
 *----------------------------------------------------------------------------- 
 * Revision History (Release 1.0.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |
 *          |  22.08.2015        |  Initial Create.
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * RFC2132 (Network Time Protocol Servers Option)
 * 
 * The code for this option is 42.  Its minimum length is 4, and the
 * length MUST be a multiple of 4.
 *
 *  Code   Len         Address 1               Address 2
 * +-----+-----+-----+-----+-----+-----+-----+-----+--
 * |  42 |  n  |  a1 |  a2 |  a3 |  a4 |  a1 |  a2 |  ...
 * +-----+-----+-----+-----+-----+-----+-----+-----+--
 * 
 * 
 * 
 * <h4>Notes</h4>
 * none
 * 
 * @author      Florian Behrend, s60119@beuth-hochschule.de
 * 				Christoph Palme, s-----@beuth-hochschule.de
 * @version     Version 1.0
 */

package dhcp.options;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.List;

import dhcp.DHCPConstants;

public class DHCPNtpServerOption extends DHCPOption {
	
	public DHCPNtpServerOption(byte len, byte[] servers) {
		super(DHCPConstants.DHCP_V4_OPTION_NTP_SERVERS, len, servers);
	}
	
	public DHCPNtpServerOption(List<InetAddress> servers) {
		//Die Anzahl der Server mit mit 4 multipliziert werden, damit für jeden Router 4 Byte zurverfügung stehen!
		this((byte)(servers.size()*4), serverList2ByteArray(servers));
	}
	

	public byte[] encode() {
		return super.encode();
	}
	
	
	private static byte[] serverList2ByteArray(List<InetAddress> servers) {
		byte len = (byte)servers.size();
		
		if (len > 0) {
			ByteBuffer data = ByteBuffer.allocateDirect(len*4);

			for (InetAddress server : servers) {
				data.put(server.getAddress());
			}
			
			//Bytearray aus dem Bytebuffer erzeugen
			data.clear();
			byte[] bytes = new byte[data.capacity()];
			data.get(bytes,0,bytes.length);
			
			return bytes;
		}
		
		return null;
	}


}
