/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Server properties for embedded derby which is used in unit tests and server integration tests,
 * equal to setting in derby.properties.
 */
public class DerbyServerPropertiesMgr {
    private static final Logger logger = Logger.getLogger(DerbyServerPropertiesMgr.class.getName());
    
    // Preallocation - https://db.apache.org/derby/docs/10.9/ref/rrefproperpreallocator.html
    private static final String DERBY_LANGUAGE_SEQUENCE_PREALLOCATOR_VALUE = "10000";

    private DerbyServerPropertiesMgr() {
        // No Operation
    }

    /**
     * sets the properties in the system properties.
     * 
     * @param isDebug
     */
    public static void setServerProperties(boolean isDebug) {
        Properties sysProperties = System.getProperties();
        // This speeds up sequence fetching by pre-creating 10000 instead of the default 100.
        System.setProperty("derby.language.sequence.preallocator", DERBY_LANGUAGE_SEQUENCE_PREALLOCATOR_VALUE);
        if (isDebug) {
            sysProperties.put("derby.language.logQueryPlan", "true");
            sysProperties.put("derby.language.logStatementText", "true");
            sysProperties.put("derby.locks.deadlockTrace", "true");
            sysProperties.put("derby.infolog.append", "true");
        }
    }

    /**
     * sets the server properties in
     * 
     * @param isDebug
     * @param conn
     * @throws SQLException
     */
    public static void setServerProperties(boolean isDebug, Connection conn) throws SQLException {
        // Link https://db.apache.org/derby/docs/10.9/ref/crefproper22250.html
        try (Statement s = conn.createStatement()) {
            setProperty(s, "derby.language.sequence.preallocator", DERBY_LANGUAGE_SEQUENCE_PREALLOCATOR_VALUE);
            if (isDebug) {
                setProperty(s, "derby.language.logQueryPlan", "true");
                setProperty(s, "derby.language.logStatementText", "true");
                setProperty(s, "derby.locks.deadlockTrace", "true");
                setProperty(s, "derby.infolog.append", "true");
            }
        }
    }

    public static void setProperty(Statement s, String name, String value) throws SQLException {
        // Check the value
        s.execute("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('" + name + "')");
        ResultSet r = s.getResultSet();
        r.next();
        logger.info(
                "Database Property [" + name + "] desired value [" + value + "] and is currently [" + r.getString(1) + "]");
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('" + name + "', '" + value + "')");
        s.execute("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('" + name + "')");
        r = s.getResultSet();
        r.next();
        logger.info(
                "Database Property [" + name + "] desired value [" + value + "] and is now [" + r.getString(1) + "]");
    }
}
