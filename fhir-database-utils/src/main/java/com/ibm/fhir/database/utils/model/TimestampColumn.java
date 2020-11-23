/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * Timestamp Column 
 */
public class TimestampColumn extends ColumnBase {
    /**
     * The number of fractional second decimals to store or null to use the database's default
     */
    private final Integer precision;

    public TimestampColumn(String name, boolean nullable) {
        super(name, nullable);
        this.precision = null;
    }

    public TimestampColumn(String name, boolean nullable, Integer precision, String defaultValue) {
        super(name, nullable, defaultValue);
        this.precision = precision;
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return adapter.timestampClause(precision);
    }
}