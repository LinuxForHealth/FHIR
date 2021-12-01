/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra;

import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;
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
        
        // If payload persistence is configured for this tenant, provide
        // the impl otherwise null
        FHIRPayloadPersistence result = null;
        if (DatasourceSessions.isPayloadPersistenceConfigured()) {
            result = new FHIRPayloadPersistenceCassandraImpl(FHIRPayloadPersistenceCassandraImpl.defaultPartitionStrategy());
        }
        
        return result;
    }
}
