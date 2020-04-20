/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.postgresql;

import java.util.Properties;

import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;

/**
 * An adapter for PostgreSql properties
 */
public class PostgreSqlPropertyAdapter extends JdbcPropertyAdapter {

    public PostgreSqlPropertyAdapter(Properties properties) {
        super(properties);
    }
}
