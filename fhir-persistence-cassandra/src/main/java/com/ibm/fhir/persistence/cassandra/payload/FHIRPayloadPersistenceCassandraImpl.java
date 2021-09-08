/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import java.util.List;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPartitionStrategy;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;
import com.ibm.fhir.persistence.util.InputOutputByteStream;


/**
 * Implementation to store and retrieve FHIR payload data using Cassandra.
 * Because of Cassandra's consistency model, we can't rely on it to support
 * deterministic version of FHIR resources. The version number must be known
 * ahead of time (using another service with true ACID support).
 */
public class FHIRPayloadPersistenceCassandraImpl implements FHIRPayloadPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPayloadPersistenceCassandraImpl.class.getName());
    private static final long NANOS = 1000000000L;
    
    // The strategy used to obtain the partition name for a given resource
    private final FHIRPayloadPartitionStrategy partitionStrategy;

    /**
     * Public constructor
     * @param ps the partition strategy
     */
    public FHIRPayloadPersistenceCassandraImpl(FHIRPayloadPartitionStrategy ps) {
        this.partitionStrategy = ps;
    }

    /**
     * Gets a partition strategy which uses a constant partition name
     * of "default"
     * @return the partition strategy
     */
    public static FHIRPayloadPartitionStrategy defaultPartitionStrategy() {
        return new FHIRPayloadPartitionStrategy() {
            
            @Override
            public String getPartitionName() {
                return "default";
            }
        };
    }

    /**
     * Get a tenant-specific session for Cassandra
     */
    protected CqlSession getCqlSession() {
        return  DatasourceSessions.getSessionForTenantDatasource();
    }

    @Override
    public void storePayload(String resourceType, int resourceTypeId, String logicalId, int version, InputOutputByteStream payloadStream)
        throws FHIRPersistenceException {

        try (CqlSession session = getCqlSession()) {
            CqlStorePayload spl = new CqlStorePayload(partitionStrategy.getPartitionName(), resourceTypeId, logicalId, version, payloadStream);
            spl.run(session);
        }
    }

    @Override
    public <T extends Resource> T readResource(Class<T> resourceType, int resourceTypeId, String logicalId, int version, List<String> elements) throws FHIRPersistenceException {

        try (CqlSession session = getCqlSession()) {
            CqlReadResource spl = new CqlReadResource(partitionStrategy.getPartitionName(), resourceTypeId, logicalId, version, elements);
            return spl.run(resourceType, session);
        }
    }

    @Override
    public void deletePayload(int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException {
        // TODO Auto-generated method stub

    }
}