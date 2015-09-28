/**
 * Copyright 2015 (C)
 *  
 * Created on : 08.06.2015 
 * Authors    : Florian Behrend
 * 				Christoph Palme
 * Email      : s60119@beuth-hochschule.de
 * 
 *----------------------------------------------------------------------------- 
 * Revision History (Release 1.0.0.0) 
 *----------------------------------------------------------------------------- 
 * VERSION     AUTHOR/              DESCRIPTION OF CHANGE 
 *             DATE                 RFC NO 
 *----------------------------------------------------------------------------- 
 * 1.0      |  Florian Behrend   |
 *          |  08.06.2015        |  Initial Create.
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
 * @version     Version 1.0
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dhcp.DHCPConstants;
import dhcp.DHCPUtils;
import dhcp.packet.DHCPpacket;
import dhcp.server.DHCPserver;
import dhcp.simulator.DHCPClientSimulator;


public class ConsolProtocol {
	
	static Logger log = LogManager.getLogger(ConsolProtocol.class.getName());
	
	/**
	 * Reference on the current server.
	 */
	private DHCPserver currentServer;
	private Thread currentServerThread;
	
	private DHCPClientSimulator currentSimulator;
	private Thread currentSimulatorThread;
	
	/**
	 * Constructor for the class.
	 */
	public ConsolProtocol(DHCPserver currentServer, DHCPClientSimulator currentSimulator) {
		this.currentServer = currentServer;
		this.currentSimulator = currentSimulator;
		
		if (this.currentServer != null) {
			currentServerThread = new Thread(currentServer);
		}
		
		if (this.currentSimulator != null) {
			currentSimulatorThread = new Thread(currentSimulator);
		}
		
		log.info("protocol for consol was initialized");
	}

	/**
	 * Starts the action of the simulator and send a string back to the client.
	 * 
	 * @param input	String which was received.
	 * @return	String for the client.
	 */
	public String processInput(String input) {
		
		String output = ":-(";
		
		if (input.startsWith("start")) {
			if (input.equals("start server")) {
				if (this.currentServer != null && this.currentServerThread != null) {
					currentServerThread.start();
					output = "server was started";
				} else {
					if (this.currentServer == null) {
						output = "there are problems with the server";
					} else {
						output = "server is still running";
					}
				}
			} else if (input.equals("start simulator")) {
				if (this.currentSimulator != null && this.currentSimulatorThread != null) {
					currentSimulatorThread.start();
					output = "simulator was started";
				} else {
					if (this.currentSimulator == null) {
						output = "there are problems with the simulator";
					} else {
						output = "simulator is still running";
					}
				}
			}
			/* else if (input.equals("start vehicles")) {
				if (this.currentSimulator != null && this.currentSimulator.isRunning()) {
					this.currentSimulator.startVehicles();
					output = "all vehicles of the simulator was started";
				} else {
					if (this.currentSimulator == null) {
						output = "there are problems with the simulator";
					} else {
						output = "vehicles are still running";
					}
				}
			} else if (input.equals("start vehicle")) {
				if (this.vehicleNumber >= 1) {
					currentSimulator.startVehicle(this.vehicleNumber);
					output = "vehicle " + this.vehicleNumber + " was started";
				} else {
					output = "you must select a vehicle first";
				}
			} else if (input.equals("start autodeviation")) {
				if (this.vehicleNumber >= 1) {
					currentSimulator.startAtomaticDeviation(vehicleNumber, (short)-600, (short)600);
					output = "autodeviation for vehicle " + this.vehicleNumber + " was started";
				} else {
					output = "you must select a vehicle first";
				}
			} else {
				output = "unknown command";
			}
		*/}else if (input.startsWith("stop")) {
			if (input.equals("stop simulator")) {
				if (this.currentServer != null) {
					//this.currentServer.interrupt();
					output = "server was stopped";
				} else {
					if (this.currentServer == null) {
						output = "there are problems with the server";
					} else {
						output = "server already stopped";
					}
				}
			} /*else if (input.equals("stop vehicles")) {
				if (this.currentSimulator != null && this.currentSimulator.isRunning()) {
					this.currentSimulator.stopVehicles();
					output = "all vehicles of the simulator was stopped";
				} else {
					if (this.currentSimulator == null) {
						output = "there are problems with the simulator";
					} else {
						output = "vehicles already stopped";
					}
				}
			} else if (input.equals("stop vehicle")) {
				if (this.vehicleNumber >= 1) {
					this.currentSimulator.stopVehicle(vehicleNumber);
					output = "vehicle " + this.vehicleNumber + " was stopped";
				} else {
					output = "you must select a vehicle first";
				}
			} else if (input.equals("stop autodeviation")) {
				if (this.vehicleNumber >= 1) {
					currentSimulator.stopAtomaticDeviation(vehicleNumber);
					output = "autodeviation for vehicle " + this.vehicleNumber + " was stopped";
				} else {
					output = "you must select a vehicle first";
				}
			} else {
				output = "unknown command";
			}*/
		} else if (input.startsWith("list")) {
			if (input.equals("list interfaces")) {
				output =  DHCPUtils.displayInterfaceInformation(DHCPUtils.getNetworkInterfaces());
			} else {
				output = "unknown command";
			}
			/*
		} else if (input.startsWith("select")) {
			if (input.startsWith("select vehicle")) {
				//Regex für den gültigen Befehl
				Pattern pattern = Pattern.compile("^select vehicle[ ]+(\\d+)$");
		    	Matcher matcher = pattern.matcher(input);
		    	
		    	//Prüfen of gefunden
		    	if (matcher.matches()) {
		    		vehicleNumber = Integer.parseInt(matcher.group(1));
		    		
		    		if (vehicleNumber >= 1 && vehicleNumber <= currentSimulator.getNumberOfVehicles()) {
		    			//Es wurde ein gültige Nummer eingegeben.
		    			output = "vehicle " + vehicleNumber + " was selected";
		    		} else {
		    			//Die angegbene Nummer wurder nicht gefunden
		    			vehicleNumber = -1;
		    			output = "unknown number for vehicle";
		    		}
		    	} else {
		    		//Die angegbene Nummer wurder nicht gefunden
		    		vehicleNumber = -1;
		    		output = "unknown number for vehicle";
		    	}
			} else {
				output = "unknown command";
			}
		} else if (input.startsWith("drop")) {
			if (input.equals("drop journey")) {
				if (this.vehicleNumber >= 1) {
					this.currentSimulator.dropJourney(this.vehicleNumber);
					output = "journey of vehicle " + this.vehicleNumber + " was dropped";
				} else {
					output = "you must select a vehicle first";
				}
			} else if (input.equals("drop course")) {
				if (this.vehicleNumber >= 1) {
					this.currentSimulator.dropCourse(this.vehicleNumber);
					output = "course of vehicle " + this.vehicleNumber + " was dropped";
				} else {
					output = "you must select a vehicle first";
				}
			} else {
				output = "unknown command";
			}
		} else if (input.startsWith("set")) {
			if (input.startsWith("set deviation")) {
				//Regex für den gültigen Befehl
				Pattern pattern = Pattern.compile("^set deviation[ ]+(\\d+)$");
		    	Matcher matcher = pattern.matcher(input);
		    	
		    	//Prüfen of gefunden
		    	if (matcher.matches()) {
		    		int deviation = Integer.parseInt(matcher.group(1));
		    		
		    		if (deviation >= -1800 && deviation <=1800) {
		    			if (this.vehicleNumber >= 1) {
							this.currentSimulator.setVehicleDeviation(vehicleNumber, (short)deviation);
							output = "course of vehicle " + this.vehicleNumber + " was dropped";
						} else {
							output = "you must select a vehicle first";
						}
		    		} else {
		    			output = "deviation is out of range";
		    		}
		    	}
			}
		*/} else if (input.startsWith("send")) {
			if (input.equals("send packet")) {
				if (this.currentServer != null) {
					
					DHCPpacket dp = new DHCPpacket();
					byte[] xid = {(byte)-1, (byte)-1, (byte)-1, (byte)-1};
					byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_OFFER, xid);
					
					/*
					try{
						sq.add(bytes);}
					catch(Exception ex){
						System.out.println(ex.toString());
					}
					*/
					
					//this.currentServer.sendResponse(new DatagramPacket(bytes, bytes.length));
					
					System.out.println(DHCPUtils.bytesToHex(bytes));	
					
				} else {
					if (this.currentServer == null) {
						output = "there are problems with the server";
					} else {
						output = "server already stopped";
					}
				}
			} else if (input.equals("send discover")) {
				DHCPpacket dp = new DHCPpacket();
				byte[] xid = {(byte)1, (byte)2, (byte)3, (byte)4};
				byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_DISCOVER, xid);
				
				currentSimulator.sendPacket(bytes);
				
				output = "OK";
			} else if (input.equals("send request")) {
				DHCPpacket dp = new DHCPpacket();
				byte[] xid = {(byte)1, (byte)2, (byte)3, (byte)4};
				byte[] bytes = dp.encode(DHCPConstants.DHCP_V4_MESSAGE_TYPE_REQUEST, xid);
				
				currentSimulator.sendPacket(bytes);
				
				output = "OK";
			} else if (input.equals("send endless discovery")) {
				currentSimulator.startStresstest_DHCPACK(100, 300);
				
				output = "OK";
			} else if (input.equals("send endless request")) {
				currentSimulator.startStresstest_DHCPACK(100, 300);
				
				output = "OK";
			}
		} else if (input.equals("exit")) {
			//this.currentServer.interrupt();
			//while (this.currentServer.isAlive());
			
			//Beenden der Anwendung
			System.exit(0);
			
			output = "application was terminated";
		} else {
			//Unbekannter Befehl
			output = "unknown command";
		}
		
		return output;
	}
	
}
