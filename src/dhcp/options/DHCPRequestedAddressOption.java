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
 * Revision History (Release 1.0.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |
 *          |  20.06.2015        |  Initial Create.   
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * RFC2132 (Requested IP Address Option)
 * 
 * This option is used in a client request (DHCPDISCOVER) to allow the
 * client to request that a particular IP address be assigned.
 *
 * The code for this option is 50, and its length is 4.
 *
 *  Code   Len          Address
 * +-----+-----+-----+-----+-----+-----+
 * |  50 |  4  |  a1 |  a2 |  a3 |  a4 |
 * +-----+-----+-----+-----+-----+-----+
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

public class DHCPRequestedAddressOption extends DHCPOption {

	public DHCPRequestedAddressOption(byte[] ipAddress) {
		super(DHCPConstants.DHCP_V4_OPTION_DHCP_REQUESTED_ADDRESS, (byte)4, ipAddress);
	}

	public byte[] encode() {
		return super.encode();
	}
	
}
