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
 * RFC2132 (Subnet Mask Option)
 * 
 * The subnet mask option specifies the client's subnet mask as per RFC
 * 950 [5].
 *
 * If both the subnet mask and the router option are specified in a DHCP
 * reply, the subnet mask option MUST be first.
 * 
 * The code for the subnet mask option is 1, and its length is 4 octets.
 *
 *  Code   Len        Subnet Mask
 * +-----+-----+-----+-----+-----+-----+
 * |  1  |  4  |  m1 |  m2 |  m3 |  m4 |
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

public class DHCPSubnetMaskOption extends DHCPOption {

	public DHCPSubnetMaskOption(byte[] subnetMask) {
		super(DHCPConstants.DHCP_V4_OPTION_SUBNET_MASK, (byte)4, subnetMask);
	}

	public byte[] encode() {
		return super.encode();
	}

}
