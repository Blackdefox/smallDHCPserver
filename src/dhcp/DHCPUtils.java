/**
 * Copyright 2015 (C)
 *  
 * Created on : 08.06.2015 
 * Authors    : Florian Behrend
 * 				Christoph Palme
 * Email      : s60119@beuth-hochschule.de
 * 
 *----------------------------------------------------------------------------- 
 * Revision History (Release 1.1.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |
 *          |  08.06.2015        |  Initial Create.
 *-----------------------------------------------------------------------------
 * 1.1      |  Florian Behrend   |
 *          |  20.06.2015        |  Add functions for listing NICs
 *-----------------------------------------------------------------------------
 *
 */

/**
 * <h4>Description</h4>
 * Class for the protocol of the user interface.
 * 
 * <h4>Notes</h4>
 * none
 * 
 * @author      Florian Behrend, s60119@beuth-hochschule.de
 * @version     Version 1.1
 */

package dhcp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class DHCPUtils {

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    
	    return new String(hexChars);
	}
	
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    
	    return data;
	}
	
	public static String socketAddressToString(byte[] socketAddressAsByteArray) {
	
		return "";
	}
	
	
	public static List<NetworkInterface> getNetworkInterfaces() {
		List<NetworkInterface> currentInterfaces = new ArrayList<NetworkInterface>();
		
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			
			for (NetworkInterface netint : Collections.list(nets))
				if (netint.isUp() && !netint.isVirtual() && !netint.isLoopback()) {
					//Nur Netzwerkkarten eintragen die aktiv, nicht virtuell und kein loopback sind
					currentInterfaces.add(netint);
				}
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		
		return currentInterfaces;
	}
	
	public static String displayInterfaceInformation(List<NetworkInterface> netints) {
		StringBuffer sb = new StringBuffer();

		for (NetworkInterface netint : netints) {
        	Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            
            if (inetAddresses.hasMoreElements()) {
            	
            	sb.append("Display name: "+ netint.getDisplayName() + "\n");
            	sb.append("Name: " + netint.getName() + "\n");
                
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                	
                	sb.append("InetAddress: " + inetAddress.getHostAddress() + "\n");
                }
                
                sb.append("\n");
            }
        }
        
        return sb.toString();
     }
	
}
