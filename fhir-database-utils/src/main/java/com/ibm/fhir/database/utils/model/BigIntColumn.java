/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * Big Int Column 
 */
public class BigIntColumn extends ColumnBase {

    /**
     * @param name
     */
    public BigIntColumn(String name, boolean nullable) {
        super(name, nullable);
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter translator) {
        return "BIGINT";
    }

}
