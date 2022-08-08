/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.derby;

import java.util.Properties;

import org.linuxforhealth.fhir.database.utils.common.JdbcPropertyAdapter;

/**
 * Adapts properties to Derby
 */
public class DerbyPropertyAdapter extends JdbcPropertyAdapter {

    public static final String DERBY_MEMORY_KEY = "db.derby.memory";
    public static final String CREATE_KEY = "db.create";

    /**
     * @param properties
     */
    public DerbyPropertyAdapter(Properties properties) {
        super(properties);
    }

    /**
     * Getter for the Derby in-memory database flag
     * @return
     */
    public boolean isMemory() {
        return "Y".equals(this.properties.getProperty(DERBY_MEMORY_KEY));
    }

    /**
     * Setter for the Derby create flag
     * @param create
     * @return
     */
    public void setAutoCreate(boolean create) {
        if (create) {
            this.properties.setProperty(CREATE_KEY, "Y");
        } else {
            this.properties.setProperty(CREATE_KEY, "N");
        }
    }

    /**
     * Getter for the Derby create flag
     * @return
     */
    public boolean isAutoCreate() {
        return "Y".equals(this.properties.getProperty(CREATE_KEY));
    }

    @Override
    public String getDefaultSchema() {
        String result = super.getDefaultSchema();
        if (result == null) {
            result = "APP";
        }
        return result;
    }
}
