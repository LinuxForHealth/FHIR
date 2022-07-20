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
     * Citus (Distributed PostgreSQL)
     */
    CITUS("citus");

    private String value;

    private DbType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    /**
     * Translate the string value into the corresponding enum constant
     * @param value
     * @return
     */
    public static DbType from(java.lang.String value) {
        // linear search over such a small set isn't too painful
        for (DbType t : DbType.values()) {
            if (t.value.equals(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
