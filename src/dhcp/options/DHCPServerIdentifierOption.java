/**
 * Copyright 2015 (C)
 *  
 * Created on : 12.07.2015 
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
 *          |  12.07.2015        |  Initial Create.
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * RFC2132 (Server Identifier Option)
 * 
 * This option is used in DHCPOFFER and DHCPREQUEST messages, and may
 * optionally be included in the DHCPACK and DHCPNAK messages.  DHCP
 * servers include this option in the DHCPOFFER in order to allow the
 * client to distinguish between lease offers.  DHCP clients use the
 * contents of the 'server identifier' field as the destination address
 * for any DHCP messages unicast to the DHCP server.  DHCP clients also
 * indicate which of several lease offers is being accepted by including
 * this option in a DHCPREQUEST message.
 *
 * The identifier is the IP address of the selected server.
 * 
 * The code for this option is 54, and its length is 4.
 * 
 *  Code   Len            Address
 * +-----+-----+-----+-----+-----+-----+
 * |  54 |  4  |  a1 |  a2 |  a3 |  a4 |
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

public class DHCPServerIdentifierOption extends DHCPOption {
	
	public DHCPServerIdentifierOption(byte[] serverIP) {
		super(DHCPConstants.DHCP_V4_OPTION_DHCP_SERVER_IDENTIFIER, (byte)4, serverIP);
	}
	
	public byte[] encode() {
		return super.encode();
	}

}
