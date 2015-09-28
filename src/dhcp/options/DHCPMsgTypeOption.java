/**
 * Copyright 2015 (C)
 *  
 * Created on : 18.06.2015 
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
 *          |  18.06.2015        |  Initial Create.
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * RFC2132 (DHCP Message Type Option)
 * 
 * This option is used to convey the type of the DHCP message.  The code
 * for this option is 53, and its length is 1.  Legal values for this
 * option are:
 *
 *         Value   Message Type
 *         -----   ------------
 *           1     DHCPDISCOVER
 *           2     DHCPOFFER
 *           3     DHCPREQUEST
 *           4     DHCPDECLINE
 *           5     DHCPACK
 *           6     DHCPNAK
 *           7     DHCPRELEASE
 *           8     DHCPINFORM
 *
 *  Code   Len  Type
 * +-----+-----+-----+
 * |  53 |  1  | 1-9 |
 * +-----+-----+-----+
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

public class DHCPMsgTypeOption extends DHCPOption {
	
	public DHCPMsgTypeOption(byte msgType) {
		super(DHCPConstants.DHCP_V4_OPTION_DHCP_MESSAGE_TYPE, (byte)1, msgType);
	}
	
	public byte[] encode() {
		return super.encode();
	}
	
}
