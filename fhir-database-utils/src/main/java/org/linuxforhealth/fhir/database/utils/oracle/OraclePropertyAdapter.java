/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.oracle;

import java.util.Properties;

import org.linuxforhealth.fhir.database.utils.common.JdbcPropertyAdapter;

/**
 * An adapter for Oracle properties
 */
public class OraclePropertyAdapter extends JdbcPropertyAdapter {
    public OraclePropertyAdapter(Properties properties) {
        super(properties);
    }
}