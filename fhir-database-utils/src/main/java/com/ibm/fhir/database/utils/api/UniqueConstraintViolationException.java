/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Translation of a duplicate key or value constraint SQLException.
 */
public class UniqueConstraintViolationException extends DataAccessException {
    // Generated serial number
    private static final long serialVersionUID = -2753101534110619540L;

    public UniqueConstraintViolationException(Throwable t) {
        super(t);
    }

    public UniqueConstraintViolationException(String msg, Throwable t) {
        super(msg, t);
    }
}