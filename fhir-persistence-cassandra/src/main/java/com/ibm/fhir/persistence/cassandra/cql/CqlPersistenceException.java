/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.cql;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * An exception which occurred processing a CQL operation
 */
public class CqlPersistenceException extends FHIRPersistenceException {

    private static final long serialVersionUID = -3499947162325228628L;

    /**
     * Public constructor
     * @param msg
     * @param t
     */
    public CqlPersistenceException(String msg, Throwable t) {
        super(msg, t);
    }
}