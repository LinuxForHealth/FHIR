/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Translation of a duplicate schema to an exception
 */
public class DuplicateSchemaException extends DataAccessException {
    // Generated serial number
    private static final long serialVersionUID = -2753101341101219540L;

    public DuplicateSchemaException(Throwable t) {
        super(t);
    }

    public DuplicateSchemaException(String msg, Throwable t) {
        super(msg, t);
    }
}