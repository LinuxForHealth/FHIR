/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

/**
 * Column Types used in FHIR
 */
public enum ColumnType {
    INT,
    BIGINT,
    VARCHAR,
    VARBINARY,
    CHAR,
    DECIMAL,
    DOUBLE,
    TIMESTAMP,
    BLOB,
    CLOB,
    SMALLINT,
    SMALLINT_BOOLEAN // for JavaBatch
}
