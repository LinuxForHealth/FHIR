/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * Character Large OBject (CLOB) Column
 */
public class ClobColumn extends ColumnBase {
    public ClobColumn(String name, boolean nullable) {
        super(name, nullable);
    }

    public ClobColumn(String name, boolean nullable, String defaultVal) {
        super(name, nullable, defaultVal);
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return adapter.clobClause();
    }
}