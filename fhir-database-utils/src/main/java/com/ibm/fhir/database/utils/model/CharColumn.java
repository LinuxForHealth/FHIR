/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * CharColumn
 */
public class CharColumn extends ColumnBase {
    private final int size;

    /**
     * @param name
     */
    public CharColumn(String name, int size, boolean nullable, String defaultValue) {
        super(name, nullable, defaultValue);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return "CHAR(" + size + ")";
    }

}
