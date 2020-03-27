/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.util.Properties;


/**
 * Server properties for embedded derby which is used in unit tests and server integration tests,
 * equal to setting in derby.properties.
 */
public class DerbyServerPropertiesMgr {
    public static void setServerProperties (boolean isDebug) {
        Properties sysProperties = System.getProperties();
        // This speeds up sequence fetching by pre-creating 1000 instead of the default 100.
        sysProperties.put("derby.language.sequence.preallocator", 1000);
        if (isDebug) {
            sysProperties.put("derby.language.logQueryPlan", "true");
            sysProperties.put("derby.language.logStatementText", "true");
            sysProperties.put("derby.locks.deadlockTrace", "true");
            sysProperties.put("derby.infolog.append", "true");
        }
    }

}
