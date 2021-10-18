/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.cql;


import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Command to perform a read operation on a Cassandra CqlSession
 */
public interface ICqlReader<T> {

    /**
     * Execute the statement using the connection and return the value
     * session connection to Cassandra
     * @param session
     */
    public T run(CqlSession session) throws FHIRPersistenceException;
}
