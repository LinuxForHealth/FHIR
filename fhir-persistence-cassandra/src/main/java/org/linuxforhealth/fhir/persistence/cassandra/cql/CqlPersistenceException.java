/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.cql;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

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