/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.util.Properties;

/**
 * Base adapter for common JDBC connection properties
 */
public class JdbcPropertyAdapter {
    protected final Properties properties;

    // Property keys for stuff making up the URL
    public static final String DATABASE_KEY = "db.database";
    public static final String USER_KEY = "db.user";
    public static final String PASSWORD_KEY = "db.password";
    public static final String HOST_KEY = "db.host";
    public static final String PORT_KEY = "db.port";
    public static final String DEFAULT_SCHEMA_KEY = "db.default.schema";
    
    
    public JdbcPropertyAdapter(Properties properties) {
        this.properties = properties;
    }
    
    public String getDatabase() {
        return properties.getProperty(DATABASE_KEY);
    }
    
    public void setDatabase(String db) {
        properties.setProperty(DATABASE_KEY, db);
    }
    
    public void setHost(String host) {
        properties.setProperty(HOST_KEY, host);
    }
    
    public String getHost() {
        return properties.getProperty(HOST_KEY);
    }
    
    public void setPort(int port) {
        properties.setProperty(PORT_KEY, Integer.toString(port));
    }
    
    public int getPort() {
        String value = properties.getProperty(PORT_KEY);
        if (value == null) {
            throw new IllegalStateException("Property for port is missing");
        }
        
        return Integer.parseInt(value);
    }
    
    public String getUser() {
        return properties.getProperty(USER_KEY);
    }
    
    public void setUser(String user) {
        properties.setProperty(USER_KEY, user);
    }
    
    public String getPassword() {
        return properties.getProperty(PASSWORD_KEY);
    }
    
    public void setPassword(String pw) {
        properties.setProperty(PASSWORD_KEY, pw);
    }
    
    public void setDefaultSchema(String schema) {
        properties.setProperty(DEFAULT_SCHEMA_KEY, schema);
    }
    
    public String getDefaultSchema() {
        return properties.getProperty(DEFAULT_SCHEMA_KEY);        
    }

    
    protected Properties getProperties() {
        return this.properties;
    }
    
    public boolean isValid() {
        return getUser() != null && getPassword() != null;
    }

    /**
     * Get all the extra properties into the given props argument
     * @param props
     */
    public void getExtraProperties(Properties props) {
        for (String key: properties.stringPropertyNames()) {
            if (!key.startsWith("db.")) {
                props.setProperty(key, properties.getProperty(key));
            }
        }
    }
}
