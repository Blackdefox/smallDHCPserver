/**
 * Copyright 2015 (C)
 *  
 * Created on : 12.07.2015 
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
 *          |  12.07.2015        |  Initial Create.
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * RFC2132 (Broadcast Address Option)
 * 
 * This option specifies the broadcast address in use on the client's
 * subnet.  Legal values for broadcast addresses are specified in
 * section 3.2.1.3 of [4].
 * 
 * The code for this option is 28, and its length is 4.
 *
 *   Code   Len     Broadcast Address
 *  +-----+-----+-----+-----+-----+-----+
 *  |  28 |  4  |  b1 |  b2 |  b3 |  b4 |
 *  +-----+-----+-----+-----+-----+-----+
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

import dhcp.DHCPConstants;

public class DHCPBroadcastAddressOption extends DHCPOption{
	
	public DHCPBroadcastAddressOption(byte[] broadcastAddress) {
		super(DHCPConstants.DHCP_V4_OPTION_BROADCAST_ADDRESS, (byte)4, broadcastAddress);
	}
	
	public byte[] encode() {
		return super.encode();
	}

}
