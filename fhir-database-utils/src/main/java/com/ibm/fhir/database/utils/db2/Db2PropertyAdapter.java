/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.util.Properties;

import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;

/**
 * An adapter for DB2 properties
 */
public class Db2PropertyAdapter extends JdbcPropertyAdapter {
    private static final String API_KEY = "apiKey";
    private static final String SECURITY_MECHANISM = "securityMechanism";
    private static final String PLUGIN_NAME = "pluginName";
    
    public Db2PropertyAdapter(Properties properties) {
        super(properties);
    }

    /**
     * Fill the given properties with the IAM API KEY configuration if so configured
     * @param props
     */
    public void fillIAMProperties(Properties props) {
        if (this.getProperties().containsKey(API_KEY)) {
            props.setProperty(API_KEY, this.getProperties().getProperty(API_KEY));
            props.setProperty(SECURITY_MECHANISM, this.getProperties().getProperty(SECURITY_MECHANISM));
            props.setProperty(PLUGIN_NAME, this.getProperties().getProperty(PLUGIN_NAME));
        }
    }
}
