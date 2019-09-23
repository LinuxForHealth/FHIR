/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.util.Properties;

import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;

/**
 * @author rarnold
 *
 */
public class DerbyPropertyAdapter extends JdbcPropertyAdapter {

    public static final String DERBY_MEMORY_KEY = "db.derby.memory";

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

}
