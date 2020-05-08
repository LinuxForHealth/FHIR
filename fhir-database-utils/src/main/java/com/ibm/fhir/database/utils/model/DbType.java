/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * The type of database
 */
public enum DbType {
    /**
     * Apache Derby
     */
    DERBY("derby"),

    /**
     * PostgreSql
     */
    POSTGRESQL("postgresql"),

    /**
     * IBM Db2
     */
    DB2("db2");

    private String value;

    private DbType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DbType from(java.lang.String value) {
        for (DbType t : DbType.values()) {
            if (t.value.equals(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
