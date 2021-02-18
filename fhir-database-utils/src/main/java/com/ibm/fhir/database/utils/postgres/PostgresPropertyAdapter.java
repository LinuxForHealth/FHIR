/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.postgres;

import java.util.Properties;

import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;

/**
 * An adapter for Postgres properties
 */
public class PostgresPropertyAdapter extends JdbcPropertyAdapter {
    public PostgresPropertyAdapter(Properties properties) {
        super(properties);
    }
}