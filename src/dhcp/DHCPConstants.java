package dhcp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * Klasse mit den Konstanten für den DHCP-Server.
 * 
 * @author Florian Behrend, Christoph Palme
 * @version 1.00
 */
public class DHCPConstants {
	
	public static final int DHCP_V4_SERVER_PORT = 67;
	public static final int DHCP_V4_CLIENT_PORT = 68;
	
	/** Message op code / message type (RFC 2131) **/
    public static final byte DHCP_V4_OP_CODE_REQUEST    = 1;	// RFC 2131
    public static final byte DHCP_V4_OP_CODE_REPLY	    = 2;	// RFC 2131

    /** Hardware address type (RFC 2131) **/
    public static final byte DHCP_V4_HTYPE_ETHERNET	= 1;	// RFC 2131
    public static final byte DHCP_V4_HTYPE_IEEE802	= 6;
    public static final byte DHCP_V4_HTYPE_FDDI		= 8;
    public static final byte DHCP_V4_HTYPE_IEEE1394	= 24;	// rfc 2855

    /** DHCP Message Type (RFC 1533, RFC 2131, RFC 3203, RFC 4388) **/
    public static final byte DHCP_V4_MESSAGE_TYPE_DISCOVER   =  1;		// RFC 1533
    public static final byte DHCP_V4_MESSAGE_TYPE_OFFER      =  2;		// RFC 1533
    public static final byte DHCP_V4_MESSAGE_TYPE_REQUEST    =  3;		// RFC 1533
    public static final byte DHCP_V4_MESSAGE_TYPE_DECLINE    =  4;		// RFC 1533
    public static final byte DHCP_V4_MESSAGE_TYPE_ACK        =  5;		// RFC 1533
    public static final byte DHCP_V4_MESSAGE_TYPE_NAK        =  6;		// RFC 1533
    public static final byte DHCP_V4_MESSAGE_TYPE_RELEASE    =  7;		// RFC 1533
    public static final byte DHCP_V4_MESSAGE_TYPE_INFORM     =  8;		// RFC 2131
    public static final byte DHCP_V4_MESSAGE_TYPE_FORCERENEW =  9;		// RFC 3203
    public static final byte DHCP_V4_MESSAGE_TYPE_LEASEQUERY = 10;		// RFC 4388
    public static final byte DHCP_V4_MESSAGE_TYPE_LEASEUNASSIGNED = 11;	// RFC 4388
    public static final byte DHCP_V4_MESSAGE_TYPE_LEASEUNKNOWN = 12;	// RFC 4388
    public static final byte DHCP_V4_MESSAGE_TYPE_LEASEACTIVE = 13;		// RFC 4388
    public static final byte DHCP_V4_MESSAGE_TYPE_BULKLEASEQUERY = 14;	// RFC 6926
    public static final byte DHCP_V4_MESSAGE_TYPE_LEASEUERYDONE = 15;	// RFC 6926

    /** DHCP Options (RFC 1533, RFC 2242) **/
    // Alle mögliche Optionen mit RFCs sind unter folgendem Link zu finden:
    // http://www.iana.org/assignments/bootp-dhcp-parameters/bootp-dhcp-parameters.xhtml
    public static final byte DHCP_V4_OPTION_PAD                          =   0;	// RFC 1533
    public static final byte DHCP_V4_OPTION_SUBNET_MASK                  =   1;	// RFC 1533
    public static final byte DHCP_V4_OPTION_TIME_OFFSET                  =   2;	// RFC 1533
    public static final byte DHCP_V4_OPTION_ROUTERS                      =   3;	// RFC 1533
    public static final byte DHCP_V4_OPTION_TIME_SERVERS                 =   4;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NAME_SERVERS                 =   5;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DOMAIN_NAME_SERVERS          =   6;	// RFC 1533
    public static final byte DHCP_V4_OPTION_LOG_SERVERS                  =   7;	// RFC 1533
    public static final byte DHCP_V4_OPTION_COOKIE_SERVERS               =   8;	// RFC 1533
    public static final byte DHCP_V4_OPTION_LPR_SERVERS                  =   9;	// RFC 1533
    public static final byte DHCP_V4_OPTION_IMPRESS_SERVERS              =  10;	// RFC 1533
    public static final byte DHCP_V4_OPTION_RESOURCE_LOCATION_SERVERS    =  11;	// RFC 1533
    public static final byte DHCP_V4_OPTION_HOST_NAME                    =  12;	// RFC 1533
    public static final byte DHCP_V4_OPTION_BOOT_SIZE                    =  13;	// RFC 1533
    public static final byte DHCP_V4_OPTION_MERIT_DUMP                   =  14;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DOMAIN_NAME                  =  15;	// RFC 1533
    public static final byte DHCP_V4_OPTION_SWAP_SERVER                  =  16;	// RFC 1533
    public static final byte DHCP_V4_OPTION_ROOT_PATH                    =  17;	// RFC 1533
    public static final byte DHCP_V4_OPTION_EXTENSIONS_PATH              =  18;	// RFC 1533
    public static final byte DHCP_V4_OPTION_IP_FORWARDING                =  19;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NON_LOCAL_SOURCE_ROUTING     =  20;	// RFC 1533
    public static final byte DHCP_V4_OPTION_POLICY_FILTER                =  21;	// RFC 1533
    public static final byte DHCP_V4_OPTION_MAX_DGRAM_REASSEMBLY         =  22;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DEFAULT_IP_TTL               =  23;	// RFC 1533
    public static final byte DHCP_V4_OPTION_PATH_MTU_AGING_TIMEOUT       =  24;	// RFC 1533
    public static final byte DHCP_V4_OPTION_PATH_MTU_PLATEAU_TABLE       =  25;	// RFC 1533
    public static final byte DHCP_V4_OPTION_INTERFACE_MTU                =  26;	// RFC 1533
    public static final byte DHCP_V4_OPTION_ALL_SUBNETS_LOCAL            =  27;	// RFC 1533
    public static final byte DHCP_V4_OPTION_BROADCAST_ADDRESS            =  28;	// RFC 1533
    public static final byte DHCP_V4_OPTION_PERFORM_MASK_DISCOVERY       =  29;	// RFC 1533
    public static final byte DHCP_V4_OPTION_MASK_SUPPLIER                =  30;	// RFC 1533
    public static final byte DHCP_V4_OPTION_ROUTER_DISCOVERY             =  31;	// RFC 1533
    public static final byte DHCP_V4_OPTION_ROUTER_SOLICITATION_ADDRESS  =  32;	// RFC 1533
    public static final byte DHCP_V4_OPTION_STATIC_ROUTES                =  33;	// RFC 1533
    public static final byte DHCP_V4_OPTION_TRAILER_ENCAPSULATION        =  34;	// RFC 1533
    public static final byte DHCP_V4_OPTION_ARP_CACHE_TIMEOUT            =  35;	// RFC 1533
    public static final byte DHCP_V4_OPTION_IEEE802_3_ENCAPSULATION      =  36;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DEFAULT_TCP_TTL              =  37;	// RFC 1533
    public static final byte DHCP_V4_OPTION_TCP_KEEPALIVE_INTERVAL       =  38;	// RFC 1533
    public static final byte DHCP_V4_OPTION_TCP_KEEPALIVE_GARBAGE        =  39;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NIS_SERVERS                  =  41;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NTP_SERVERS                  =  42;	// RFC 1533
    public static final byte DHCP_V4_OPTION_VENDOR_ENCAPSULATED_OPTIONS  =  43;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NETBIOS_NAME_SERVERS         =  44;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NETBIOS_DD_SERVER            =  45;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NETBIOS_NODE_TYPE            =  46;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NETBIOS_SCOPE                =  47;	// RFC 1533
    public static final byte DHCP_V4_OPTION_FONT_SERVERS                 =  48;	// RFC 1533
    public static final byte DHCP_V4_OPTION_X_DISPLAY_MANAGER            =  49;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_REQUESTED_ADDRESS       =  50;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_LEASE_TIME              =  51;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_OPTION_OVERLOAD         =  52;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_MESSAGE_TYPE            =  53;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_SERVER_IDENTIFIER       =  54;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_PARAMETER_REQUEST_LIST  =  55;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_MESSAGE                 =  56;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_MAX_MESSAGE_SIZE        =  57;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_RENEWAL_TIME            =  58;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_REBINDING_TIME          =  59;	// RFC 1533
    public static final byte DHCP_V4_OPTION_VENDOR_CLASS_IDENTIFIER      =  60;	// RFC 1533
    public static final byte DHCP_V4_OPTION_DHCP_CLIENT_IDENTIFIER       =  61;	// RFC 1533
    public static final byte DHCP_V4_OPTION_NWIP_DOMAIN_NAME             =  62; // RFC 2242
    public static final byte DHCP_V4_OPTION_NWIP_SUBOPTIONS              =  63; // RFC 2242
    public static final byte DHCP_V4_OPTION_NISPLUS_DOMAIN               =  64;
    public static final byte DHCP_V4_OPTION_NISPLUS_SERVER               =  65;
    public static final byte DHCP_V4_OPTION_TFTP_SERVER                  =  66;
    public static final byte DHCP_V4_OPTION_BOOTFILE                     =  67;
    public static final byte DHCP_V4_OPTION_MOBILE_IP_HOME_AGENT         =  68;
    public static final byte DHCP_V4_OPTION_SMTP_SERVER                  =  69;
    public static final byte DHCP_V4_OPTION_POP3_SERVER                  =  70;
    public static final byte DHCP_V4_OPTION_NNTP_SERVER                  =  71;
    public static final byte DHCP_V4_OPTION_WWW_SERVER                   =  72;
    public static final byte DHCP_V4_OPTION_FINGER_SERVER                =  73;
    public static final byte DHCP_V4_OPTION_IRC_SERVER                   =  74;
    public static final byte DHCP_V4_OPTION_STREETTALK_SERVER            =  75;
    public static final byte DHCP_V4_OPTION_STDA_SERVER                  =  76;
    public static final byte DHCP_V4_OPTION_USER_CLASS                   =  77; // rfc 3004
    public static final byte DHCP_V4_OPTION_FQDN                         =  81;
    public static final byte DHCP_V4_OPTION_DHCP_AGENT_OPTIONS           =  82; // rfc 3046
    public static final byte DHCP_V4_OPTION_NDS_SERVERS                  =  85; // rfc 2241
    public static final byte DHCP_V4_OPTION_NDS_TREE_NAME                =  86; // rfc 2241
    public static final byte DHCP_V4_OPTION_NDS_CONTEXT					 =  87; // rfc 2241
    public static final byte DHCP_V4_OPTION_CLIENT_LAST_TRANSACTION_TIME =  91; // rfc 4388
    public static final byte DHCP_V4_OPTION_ASSOCIATED_IP				 =  92; // rfc 4388
    public static final byte DHCP_V4_OPTION_LDAP_SERVERS				 =	95; // rfc 3679 (nur ein Entwurf) [von Apple vorgeschlagen]
    public static final byte DHCP_V4_OPTION_USER_AUTHENTICATION_PROTOCOL =  98;
    public static final byte DHCP_V4_OPTION_AUTO_CONFIGURE               = 116;
    public static final byte DHCP_V4_OPTION_NAME_SERVICE_SEARCH          = 117; // rfc 2937
    public static final byte DHCP_V4_OPTION_SUBNET_SELECTION             = 118; // rfc 3011
    public static final byte DHCP_V4_OPTION_DOMAIN_SEARCH	             = 119; // rfc 3397
    public static final byte DHCP_V4_OPTION_CLASSLESS_ROUTE				 = 121;	// rfc 3442
    public static final byte DHCP_V4_OPTION_CLASSLESS_STATIC_ROUTE		 = (byte)249; // keine rfc vorhanden
    public static final byte DHCP_V4_OPTION_PROXY_AUTODISCOVERY			 = (byte)252; // keine rfc vorhanden
    public static final byte DHCP_V4_OPTION_END                          =  -1;
    
    
    
    static final Map<Byte, String> DHCP_V4_OP_CODES;
    static final Map<Byte, String> DHCP_V4_HTYPES;
    static final Map<Byte, String> DHCP_V4_MESSAGE_TYPES;
    static final Map<Byte, String> DHCP_V4_OPTIONS;
    
    static {
    	Map<Byte, String> dhcp_v4_op_codes  = new LinkedHashMap<Byte, String>();
    	Map<Byte, String> dhcp_v4_htypes = new LinkedHashMap<Byte, String>();
        Map<Byte, String> dhcp_v4_message_types  = new LinkedHashMap<Byte, String>();
        Map<Byte, String> dhcp_v4_options   = new LinkedHashMap<Byte, String>();
        
        Field[] fields = DHCPConstants.class.getDeclaredFields();

        try {
            for (Field field : fields) {
                int    mod  = field.getModifiers();
                String name = field.getName();

                if (Modifier.isFinal(mod) && Modifier.isPublic(mod) && Modifier.isStatic(mod) && field.getType().equals(byte.class)) {
                    byte code = field.getByte(null);

                    if (name.startsWith("DHCP_V4_OP_CODE_")) {
                    	dhcp_v4_op_codes.put(code, name.substring(16));
                    } else if (name.startsWith("DHCP_V4_HTYPE_")) {
                    	dhcp_v4_htypes.put(code, name.substring(14));
                    } else if (name.startsWith("DHCP_V4_MESSAGE_TYPE_")) {
                    	dhcp_v4_message_types.put(code, name.substring(21));
                    } else if (name.startsWith("DHCP_V4_OPTION_")) {
                    	dhcp_v4_options.put(code, name.substring(15));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            // we have a problem
            throw new IllegalStateException("Fatal error while parsing internal fields");
        }
        DHCP_V4_OP_CODES = Collections.unmodifiableMap(dhcp_v4_op_codes);
        DHCP_V4_HTYPES = Collections.unmodifiableMap(dhcp_v4_htypes);
        DHCP_V4_MESSAGE_TYPES = Collections.unmodifiableMap(dhcp_v4_message_types);
        DHCP_V4_OPTIONS = Collections.unmodifiableMap(dhcp_v4_options);
    }
    
    
    public static final String getDhcpV4OpCodeName(byte code) {
    	return DHCP_V4_OP_CODES.get(code);
    }
    
    public static final String getDhcpV4HtypeName(byte code) {
    	return DHCP_V4_HTYPES.get(code);
    }
    
    public static final String getDhcpV4MessageTypeName(byte code) {
    	return DHCP_V4_MESSAGE_TYPES.get(code);
    }
    
    public static final String getDhcpV4OptionName(byte code) {
    	return DHCP_V4_OPTIONS.get(code);
    }
    
    public static final byte[] LOCAL_IP_ADDRESS = getIPAddress();
    
    private static final byte[] getIPAddress() {
    	byte[] IPAddress = {(byte)0, (byte)0, (byte)0, (byte)0};
    	InetAddress lhost = null;
    	try {
    		lhost = DHCPProperties.getInstance().getServerAddress();
    	} catch (Exception ex){
    		System.out.println(ex.toString());
    	}
    			
    	IPAddress = lhost.getAddress();
    	return IPAddress;
    }
    
    public static final int DHCP_V4_MAGIC_COOKIE = 0x63825363;
}
