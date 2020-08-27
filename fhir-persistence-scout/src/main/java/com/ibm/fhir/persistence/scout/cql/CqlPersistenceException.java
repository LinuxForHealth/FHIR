/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout.cql;


/**
 * An exception which occurred processing CQL persistence
 */
public class CqlPersistenceException extends RuntimeException {

    private static final long serialVersionUID = -582107144963087165L;

    public CqlPersistenceException(Throwable t) {
        super(t);
    }

    public CqlPersistenceException(String msg, Throwable t) {
        super(msg, t);
    }

}
