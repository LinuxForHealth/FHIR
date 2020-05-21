/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * Defining the VarcharColumn
 */
public class VarcharColumn extends ColumnBase {
    private final int size;

    /**
     * @param name
     */
    public VarcharColumn(String name, int size, boolean nullable) {
        super(name, nullable);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return adapter.varcharClause(size);
    }
}