/**
 * Copyright 2014 (C) Berliner Verkehrsbetriebe (BVG)
 *  
 * Created on : 08.06.2014 
 * Author     : Florian Behrend
 * 				Christoph Palme
 * Email      : s60119@beuth-hochschule.de
 * 
 *----------------------------------------------------------------------------- 
 * Revision History (Release 1.0.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |  Initial Create. 
 *          |  08.06.2014        | 
 *----------|--------------------|--------------------------------------------- 
 * 
 */

/**
 * <h4>Description</h4>
 * Class for the socket server to provide a telnet interface.
 * 
 * <h4>Notes</h4>
 * none
 * 
 * @author      Florian Behrend, s60119@beuth-hochschule.de
 * @version     Version 1.0
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.server.DHCPserver;
import dhcp.simulator.DHCPClientSimulator;


public class ConsolThread extends Thread {
	
	static Logger log = LogManager.getLogger(ConsolThread.class.getName());
	
	/**
	 * Reference on the current ConsolProtocol object.
	 */
	private ConsolProtocol cp = null;
	
	/**
	 * Constructor for the class.
	 */
    public ConsolThread(DHCPserver currentServer, DHCPClientSimulator currentSimulator) { 	
    	// Erzeugen eines neuen Treads mit dem Namen "DFIconsolThread"
	    super("ConsolThread");
	    log.info("new consol thread was created");
	    
	    this.cp = new ConsolProtocol(currentServer, currentSimulator);
    }
 
    /**
     * Implements the run() abstract method of parent class Thread.
     * 
     * @see java.lang.Thread#run()
     */
    public void run() {
    	BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    	String zeile = null;
    	
    	try {
    		while (!isInterrupted()) {
    			//alle Eingaben in Kleinbuchstaben konvertieren
	    		zeile = console.readLine().toLowerCase();
	    		log.debug("user-input: " + zeile);
	    		
	    		//Eingelesene Zeile an das Protokoll weiterleiten und die Rückgabe ausgeben
	    		System.out.println(cp.processInput(zeile));
    		}
    		
    		//Zum Beenden der Anwendung
    		System.exit(0);
    		
    	} catch (IOException e) {
    		// Sollte eigentlich nie passieren
    		System.out.println(e.getStackTrace());
    		log.error(e.getStackTrace());
    	}
    }
}