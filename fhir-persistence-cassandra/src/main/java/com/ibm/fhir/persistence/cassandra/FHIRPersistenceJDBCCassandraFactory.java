/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra;

import com.ibm.fhir.persistence.cassandra.payload.FHIRPayloadPersistenceCassandraImpl;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;

/**
 * Factory for creating a hybrid JDBC/Cassandra persistence implementation
 */
public class FHIRPersistenceJDBCCassandraFactory extends FHIRPersistenceJDBCFactory {

    @Override
    public FHIRPayloadPersistence getPayloadPersistence() throws FHIRPersistenceException {
        // Store the payload in Cassandra
        // TODO use a real strategy
        return new FHIRPayloadPersistenceCassandraImpl(FHIRPayloadPersistenceCassandraImpl.defaultPartitionStrategy());
    };
}
