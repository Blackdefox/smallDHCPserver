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
 * RFC2132 (Name Server Option)
 * 
 * The name server option specifies a list of IEN 116 [7] name servers
 * available to the client.  Servers SHOULD be listed in order of
 * preference.
 *
 * The code for the name server option is 5.  The minimum length for
 * this option is 4 octets, and the length MUST always be a multiple of
 * 4.
 *
 *  Code   Len         Address 1               Address 2
 * +-----+-----+-----+-----+-----+-----+-----+-----+--
 * |  5  |  n  |  a1 |  a2 |  a3 |  a4 |  a1 |  a2 |  ...
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

import dhcp.DHCPConstants;

public class DHCPNameServerOption extends DHCPOption {
	
	public DHCPNameServerOption(byte len, byte[] nameservers) {
		super(DHCPConstants.DHCP_V4_OPTION_NAME_SERVERS, len, nameservers);
	}

	public byte[] encode() {
		return super.encode();
	}

}
