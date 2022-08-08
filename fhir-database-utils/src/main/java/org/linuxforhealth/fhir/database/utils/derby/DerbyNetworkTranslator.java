/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.derby;

import java.util.Properties;

/**
 * translates database access to Derby supported access.
 */
public class DerbyNetworkTranslator extends DerbyTranslator {

    @Override
    public String getDriverClassName() {
        return "org.apache.derby.jdbc.ClientXADataSource";
    }

    @Override
    public String getUrl(Properties connectionProperties) {
        return connectionProperties.getProperty("dbUrl");
    }
}
