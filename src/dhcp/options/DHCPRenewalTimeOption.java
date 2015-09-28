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
 * RFC2132 (Renewal (T1) Time Value Option)
 * 
 * This option specifies the time interval from address assignment until
 * the client transitions to the RENEWING state.
 *
 * The value is in units of seconds, and is specified as a 32-bit
 * unsigned integer.
 * 
 * The code for this option is 58, and its length is 4.
 *
 *  Code   Len         T1 Interval
 * +-----+-----+-----+-----+-----+-----+
 * |  58 |  4  |  t1 |  t2 |  t3 |  t4 |
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

public class DHCPRenewalTimeOption extends DHCPOption{

	public DHCPRenewalTimeOption(int seconds) {
		super(DHCPConstants.DHCP_V4_OPTION_DHCP_RENEWAL_TIME, (byte)4, seconds);
	}

	public byte[] encode() {
		return super.encode();
	}
}
