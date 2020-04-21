/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * Double Column
 */
public class DoubleColumn extends ColumnBase {

    /**
     * @param name
     */
    public DoubleColumn(String name, boolean nullable) {
        super(name, nullable);
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return adapter.doubleClause();
    }

}
