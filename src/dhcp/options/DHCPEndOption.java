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
 * RFC2132 (End Option)
 * 
 * The end option marks the end of valid information in the vendor
 * field.  Subsequent octets should be filled with pad options.
 *
 * The code for the end option is 255, and its length is 1 octet.
 *
 *  Code
 * +-----+
 * | 255 |
 * +-----+
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

public class DHCPEndOption extends DHCPOption {
	
	public DHCPEndOption() {
		super(DHCPConstants.DHCP_V4_OPTION_END);
	}
	
	public byte[] encode() {
		return super.encode();
	}
	

}
