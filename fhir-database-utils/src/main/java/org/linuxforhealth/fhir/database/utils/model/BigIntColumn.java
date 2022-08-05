/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTypeAdapter;

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

    /**
     * Public constructor with a default value
     * @param name
     * @param nullable
     * @param defaultValue
     */
    public BigIntColumn(String name, boolean nullable, String defaultValue) {
        super(name, nullable, defaultValue);
    }

    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return "BIGINT";
    }
}