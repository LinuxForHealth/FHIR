/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Simple wrapper to encapsulate database connection info
 */
public class ConnectionDetails {
    private static final String HOST = ".host";
    private static final String PORT = ".port";
    private static final String DATABASE = ".database";
    private static final String USER  = ".user";
    private static final String PASSWORD = ".password";
    private static final String SCHEMA = ".schema";
    private static final String SSL = ".ssl";
    private static final String TRUSTSTORE_LOCATION = ".ssl.truststore.location";
    private static final String TRUSTSTORE_PASSWORD = ".ssl.truststore.password";
    
    private static final String HA = ".ha";    
    private static final String CLIENT_REROUTE_ALTERNATE_SERVER_NAME = ".clientRerouteAlternateServerName";
    private static final String CLIENT_REROUTE_ALTERNATE_PORT_NUMBER = ".clientRerouteAlternatePortNumber";
    private static final String ENABLE_SEAMLESS_FAILOVER = ".enableSeamlessFailover";
    
    private static final String MAX_RETRIES_FOR_CLIENT_REROUTE = ".maxRetriesForClientReroute";
    private static final String RETRY_INTERVAL_FOR_CLIENT_REROUTE = ".retryIntervalForClientReroute";
    private static final String ENABLE_CLIENT_AFFINITIES_LIST = ".enableClientAffinitiesList";
    private static final String CONNECTION_TIMEOUT = ".connectionTimeout";
    private static final String LOGIN_TIMEOUT = ".loginTimeout";
    
    // The database host name or ip
    private String host;
    
    // The database port listening for JDBC connections (e.g. 50000/50443)
    private int port;
    
    // The name of the database
    private String database;
    
    // The DB access user name
    private String user;
    
    // The DB access user password
    private String password;
    
    // The default schema
    private String schema;
    
    // Connect using SSL?
    private boolean ssl;
    
    // Client Routing configuration for HADR
    private boolean ha;
    
    private String clientRerouteAlternateServerName;
    
    private String clientRerouteAlternatePortNumber;

    // The location of the truststore used for SSL/TLS connections
    private String trustStoreLocation;

    // The password for the above truststore
    private String trustStorePassword;
    
    private int enableSeamlessFailover = 0;
    
    private int maxRetriesForClientReroute;
    
    private int retryIntervalForClientReroute;
    
    private int enableClientAffinitiesList;
    
    private int connectionTimeout;
    
    private int loginTimeout;

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the schema
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * @return the ssl
     */
    public boolean isSsl() {
        return ssl;
    }

    /**
     * @param ssl the ssl to set
     */
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
    
    /**
     * Copy values from another ConnectionDetails
     * @param cd
     */
    public void init(ConnectionDetails cd) {
        this.host = cd.getHost();
        this.port = cd.getPort();
        this.database = cd.getDatabase();
        this.user = cd.getUser();
        this.password = cd.getPassword();
        this.schema = cd.getSchema();
        this.ssl = cd.isSsl();
        
        this.ha = cd.isHA();
        this.clientRerouteAlternateServerName = cd.getClientRerouteAlternateServerName();
        this.clientRerouteAlternatePortNumber = cd.getClientRerouteAlternatePortNumber();
        
        this.trustStoreLocation = cd.getTrustStoreLocation();
        this.trustStorePassword = cd.getTrustStorePassword();
        this.enableSeamlessFailover = cd.getEnableSeamlessFailover();
        this.maxRetriesForClientReroute = cd.getMaxRetriesForClientReroute();
        this.retryIntervalForClientReroute = cd.getRetryIntervalForClientReroute();
        this.enableClientAffinitiesList = cd.getEnableClientAffinitiesList();
        this.connectionTimeout = cd.getConnectionTimeout();
        this.loginTimeout = cd.getLoginTimeout();
    }

    /**
     * Initialize connection properties from the given properties, using
     * keys based on the given prefix
     * @param props
     * @param prefix
     */
    public void init(Properties props, String prefix) {
        this.host = getTrimmedProperty(props, prefix + HOST);
        this.port = getIntProperty(props, prefix + PORT);
        this.database = getTrimmedProperty(props, prefix + DATABASE);
        this.user = getTrimmedProperty(props, prefix + USER);
        this.password = getTrimmedProperty(props, prefix + PASSWORD);
        this.schema = getTrimmedProperty(props, prefix + SCHEMA);
        this.ssl = "Y".equals(getTrimmedProperty(props, prefix + SSL));
        this.trustStoreLocation = getTrimmedProperty(props, prefix + TRUSTSTORE_LOCATION);
        this.trustStorePassword = getTrimmedProperty(props, prefix + TRUSTSTORE_PASSWORD);

        // Extended configuration to support client reroute with DB2 HADR
        this.ha = "Y".equals(getTrimmedProperty(props, prefix + HA));
        if (this.ha) {
            this.clientRerouteAlternateServerName = getTrimmedProperty(props, prefix + CLIENT_REROUTE_ALTERNATE_SERVER_NAME);
            this.clientRerouteAlternatePortNumber = getTrimmedProperty(props, prefix + CLIENT_REROUTE_ALTERNATE_PORT_NUMBER);
            
            if (props.containsKey(prefix + ENABLE_SEAMLESS_FAILOVER)) {
                this.enableSeamlessFailover = getIntProperty(props, prefix + ENABLE_SEAMLESS_FAILOVER);
            }
            this.maxRetriesForClientReroute = getIntProperty(props, prefix + MAX_RETRIES_FOR_CLIENT_REROUTE);
            this.retryIntervalForClientReroute = getIntProperty(props, prefix + RETRY_INTERVAL_FOR_CLIENT_REROUTE);
            this.enableClientAffinitiesList = getIntProperty(props, prefix + ENABLE_CLIENT_AFFINITIES_LIST);
            this.connectionTimeout = getIntProperty(props, prefix + CONNECTION_TIMEOUT);
            this.loginTimeout = getIntProperty(props, prefix + LOGIN_TIMEOUT);
            
        }
    }
    
    /**
     * Trim the given property (because sometimes we see spaces, which breaks stuff
     * like JDBC connections)
     * @param props
     * @param propName
     * @return
     */
    private String getTrimmedProperty(Properties props, String propName) {
        String result = props.getProperty(propName);
        if (result != null) {
            result = result.trim();
        }
        return result;
    }

    /**
     * Read an integer value from the properties, with good error handling
     * if the value is invalid
     * @param props
     * @param propName
     * @return
     */
    private int getIntProperty(Properties props, String propName) {
        String str = getTrimmedProperty(props, propName);
        if (str != null && !str.isEmpty()) {            
            try {
                return Integer.parseInt(str);
            }
            catch (NumberFormatException x) {
                throw new IllegalArgumentException("Property value for '" + propName + "' is not an integer: '" + str + "'");
            }
        }
        else {
            throw new IllegalArgumentException("Integer property '" + propName + "' must have a value");            
        }
    }
    
    /**
     * Initialize the values using these command line arguments
     * @param args
     */
    public String[] init(String[] args) {
        List<String> remaining = new ArrayList<>();
        
        // extract database arguments from the list
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--host":
                if (++i < args.length) {
                    this.setHost(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing <string> arg for host");
                }
                break;
            case "--port":
                if (++i < args.length) {
                    this.setPort(Integer.parseInt(args[i]));
                }
                else {
                    throw new IllegalArgumentException("Missing <int> arg for port");
                }
                break;
            case "--pass":
                if (++i < args.length) {
                    this.setPassword(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing <string> arg for password");
                }
                break;
            case "--user":
                if (++i < args.length) {
                    this.setUser(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing <string> arg for user");
                }
                break;
            case "--schema":
                if (++i < args.length) {
                    this.setSchema(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing <string> arg for schema");
                }
                break;
            case "--database":
                if (++i < args.length) {
                    this.setDatabase(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing <string> arg for database");
                }
                break;
            case "--ssl":
                this.setSsl(true);
                break;
            default:
                remaining.add(args[i]);
            }
            
        }
        return remaining.toArray(new String[remaining.size()]);
    }

    /**
     * @return the ha
     */
    public boolean isHA() {
        return ha;
    }

    /**
     * @param ha the ha to set
     */
    public void setHa(boolean ha) {
        this.ha = ha;
    }

    /**
     * @return the clientRerouteAlternateServerName
     */
    public String getClientRerouteAlternateServerName() {
        return clientRerouteAlternateServerName;
    }

    /**
     * @param clientRerouteAlternateServerName the clientRerouteAlternateServerName to set
     */
    public void setClientRerouteAlternateServerName(String clientRerouteAlternateServerName) {
        this.clientRerouteAlternateServerName = clientRerouteAlternateServerName;
    }

    /**
     * @return the clientRerouteAlternatePortNumber
     */
    public String getClientRerouteAlternatePortNumber() {
        return clientRerouteAlternatePortNumber;
    }

    /**
     * @param clientRerouteAlternatePortNumber the clientRerouteAlternatePortNumber to set
     */
    public void setClientRerouteAlternatePortNumber(String clientRerouteAlternatePortNumber) {
        this.clientRerouteAlternatePortNumber = clientRerouteAlternatePortNumber;
    }

    /**
     * @return the trustStoreLocation
     */
    public String getTrustStoreLocation() {
        return trustStoreLocation;
    }

    /**
     * @param trustStoreLocation the trustStoreLocation to set
     */
    public void setTrustStoreLocation(String trustStoreLocation) {
        this.trustStoreLocation = trustStoreLocation;
    }

    /**
     * @return the trustStorePassword
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * @param trustStorePassword the trustStorePassword to set
     */
    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public int getEnableSeamlessFailover() {
        return this.enableSeamlessFailover;
    }
    
    public int getMaxRetriesForClientReroute() {
        return this.maxRetriesForClientReroute;
    }
    
    public int getRetryIntervalForClientReroute() {
        return this.retryIntervalForClientReroute;
    }
    
    public int getEnableClientAffinitiesList() {
        return this.enableClientAffinitiesList;
    }
    
    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }
    
    public int getLoginTimeout() {
        return this.loginTimeout;
    }
}
