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
 * 
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

import java.nio.ByteBuffer;
import java.util.List;

public class DHCPOption {
	
	private byte code;
	private byte len;
	private byte[] data;
	
	
	public DHCPOption(byte code, byte len, byte[] data) {
		this.code = code;
		this.len = len;
		this.data = data;
	}
	
	public DHCPOption(byte code, byte len, int data) {
		this.code = code;
		this.len = len;
		this.data = ByteBuffer.allocate(4).putInt(data).array();;
	}
	
	public DHCPOption(byte code, byte len, byte data) {
		this.code = code;
		this.len = len;
		
		byte[] sData = new byte[1];
		sData[0] = data;
		
		this.data = sData;
	}
	
	public DHCPOption(byte code) {
		this.code = code;
		this.len = 0;
		this.data = null;
	}
	

	public byte getCode() {
		return code;
	}

	public byte getLen() {
		return len;
	}

	public byte[] getData() {
		return data;
	}
	
	public byte[] encode() {
		
		if (len == 0) {
			byte[] sCode = new byte[1];
			sCode[0] = code;
			return sCode;
		}
		
		ByteBuffer option = ByteBuffer.allocate(len+2);
		option.put(code);
		option.put(len);
		option.put(data);
		
		option.clear();
		byte[] bytes = new byte[option.capacity()];
		option.get(bytes,0,bytes.length);
		
		return bytes;
	}
	
	public static int indexOfByCode(byte code, List<DHCPOption> list) {
		int i = 0;
		
		for (DHCPOption op : list) {
			if (op.getCode() == code) return i;
			i++;
		}
		
		return -1;
	}
	
}
