/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * CharColumn
 */
public class CharColumn extends ColumnBase {

    // The number of characters supported by this column
    private final int size;

    /**
     * Public constructor
     * @param name
     * @param size
     * @param nullable
     * @param defaultValue
     */
    public CharColumn(String name, int size, boolean nullable, String defaultValue) {
        super(name, nullable, defaultValue);
        this.size = size;
    }

    /**
     * Getter for the column size
     * @return
     */
    public int getSize() {
        return size;
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return "CHAR(" + size + ")";
    }

}
