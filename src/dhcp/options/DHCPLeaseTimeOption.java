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
 * RFC2132 (IP Address Lease Time Option)
 * 
 * This option is used in a client request (DHCPDISCOVER or DHCPREQUEST)
 * to allow the client to request a lease time for the IP address.  In a
 * server reply (DHCPOFFER), a DHCP server uses this option to specify
 * the lease time it is willing to offer.
 * 
 * The time is in units of seconds, and is specified as a 32-bit
 * unsigned integer.
 * 
 * The code for this option is 51, and its length is 4.
 *
 *  Code   Len         Lease Time
 * +-----+-----+-----+-----+-----+-----+
 * |  51 |  4  |  t1 |  t2 |  t3 |  t4 |
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

public class DHCPLeaseTimeOption extends DHCPOption{

	public DHCPLeaseTimeOption(int seconds) {
		super(DHCPConstants.DHCP_V4_OPTION_DHCP_LEASE_TIME, (byte)4, seconds);
	}

	public byte[] encode() {
		return super.encode();
	}
	
}
