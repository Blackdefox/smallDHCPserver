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
 * RFC2132 (Domain Name Option)
 * 
 * This option specifies the domain name that client should use when
 * resolving hostnames via the Domain Name System.
 * 
 * This option specifies the domain name that client should use when
 * resolving hostnames via the Domain Name System.
 *
 * The code for this option is 15.  Its minimum length is 1.
 *
 *  Code   Len        Domain Name
 * +-----+-----+-----+-----+-----+-----+--
 * |  15 |  n  |  d1 |  d2 |  d3 |  d4 |  ...
 * +-----+-----+-----+-----+-----+-----+--
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

public class DHCPDomainNameOption extends DHCPOption {
	
	public DHCPDomainNameOption(byte len, byte[] name) {
		super(DHCPConstants.DHCP_V4_OPTION_DOMAIN_NAME, len, name);
	}
	
	public DHCPDomainNameOption(String name) {
		//Die Anzahl der Server mit mit 4 multipliziert werden, damit für jeden Router 4 Byte zurverfügung stehen!
		this((byte)(name.getBytes().length), name.getBytes());
	}
	

	public byte[] encode() {
		return super.encode();
	}

}
