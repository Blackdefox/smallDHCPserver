/**
 * Copyright 2015 (C)
 *  
 * Created on : 20.06.2015 
 * Authors    : Florian Behrend
 * 				Christoph Palme
 * Email      : fb: s60119@beuth-hochschule.de
 * 				cp: s-----@beuth-hochschule.de
 * 
 *----------------------------------------------------------------------------- 
 * Revision History (Release 1.1.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |
 *          |  20.06.2015        |  Initial Create.
 * 1.1      |  Florian Behrend   |
 *          |  22.08.2015        |  Fix bug with length of byte array.
 *          |					 |  Now it should work.       
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * RFC2132 (Router Option)
 * 
 * The router option specifies a list of IP addresses for routers on the
 * client's subnet.  Routers SHOULD be listed in order of preference.
 * 
 * The code for the router option is 3.  The minimum length for the
 * router option is 4 octets, and the length MUST always be a multiple
 * of 4.
 *
 *  Code   Len         Address 1               Address 2
 * +-----+-----+-----+-----+-----+-----+-----+-----+--
 * |  3  |  n  |  a1 |  a2 |  a3 |  a4 |  a1 |  a2 |  ...
 * +-----+-----+-----+-----+-----+-----+-----+-----+--
 *
 * 
 * 
 * <h4>Notes</h4>
 * none
 * 
 * @author      Florian Behrend, s60119@beuth-hochschule.de
 * 				Christoph Palme, s-----@beuth-hochschule.de
 * @version     Version 1.1
 */

package dhcp.options;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.List;

import dhcp.DHCPConstants;

public class DHCPRoutersOption extends DHCPOption{
	
	public DHCPRoutersOption(byte len, byte[] routers) {
		super(DHCPConstants.DHCP_V4_OPTION_ROUTERS, len, routers);
	}
	
	public DHCPRoutersOption(List<InetAddress> routers) {
		//Die Anzahl der Router mit mit 4 multipliziert werden, damit für jeden Router 4 Byte zurverfügung stehen!
		this((byte)(routers.size()*4), routerList2ByteArray(routers));
	}
	

	public byte[] encode() {
		return super.encode();
	}
	
	
	private static byte[] routerList2ByteArray(List<InetAddress> routers) {
		byte len = (byte)routers.size();
		
		if (len > 0) {
			ByteBuffer data = ByteBuffer.allocateDirect(len*4);

			for (InetAddress router : routers) {
				data.put(router.getAddress());
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
