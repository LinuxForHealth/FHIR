/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.PropertyGroup;

/**
 * Provides a facade on top of the fhir-server-config PropertyGroup structure
 * to simplify access to configuration elements we need for connecting to
 * Cassandra
 */
public class CassandraPropertyGroupAdapter {
    private static final Logger logger = Logger.getLogger(CassandraPropertyGroupAdapter.class.getName());
    
    public static final String PROP_CONTACT_POINTS = "contactPoints";
    public static final String PROP_HOST = "host";
    public static final String PROP_PORT = "port";
    public static final String PROP_LOCAL_DATACENTER = "localDatacenter";


    // The property group we are wrapping
    private final PropertyGroup propertyGroup;
    
    public CassandraPropertyGroupAdapter(PropertyGroup pg) {
        this.propertyGroup = pg;
    }
    
    public List<ContactPoint> getContactPoints() {
        List<ContactPoint> result;
        try {
            Object[] cp = propertyGroup.getArrayProperty(PROP_CONTACT_POINTS);
            if (cp != null) {
                result = new ArrayList<>();
                
                for (int i=0; i < cp.length; i++) {
                    if (cp[i] instanceof PropertyGroup) {
                        PropertyGroup pg = (PropertyGroup)cp[i];
                        String host = pg.getStringProperty(PROP_HOST);
                        int port = pg.getIntProperty(PROP_PORT);
                        result.add(new ContactPoint(host, port));
                    }
                }
            } else {
                result = null;
            }
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_CONTACT_POINTS, x);
            throw new IllegalArgumentException("Property group not configured for property: " + PROP_CONTACT_POINTS);
        }
        
        if (result == null) {
            throw new IllegalArgumentException("Property not configured: " + PROP_CONTACT_POINTS);
        }
        
        return result;
    }
    
    /**
     * Get the configured value for the local datacenter
     * @return
     */
    public String getLocalDatacenter() {
        try {
            return propertyGroup.getStringProperty(PROP_LOCAL_DATACENTER);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_LOCAL_DATACENTER, x);
            throw new IllegalArgumentException("Property group not configured " + PROP_LOCAL_DATACENTER);
        }
    }
}
