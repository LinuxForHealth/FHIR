/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;


/**
 * Implementation to store and retrieve FHIR payload data using Cassandra.
 * Because of Cassandra's consistency model, we can't rely on it to support
 * deterministic version of FHIR resources. The version number must be known
 * ahead of time (using another service with true ACID support).
 */
public class FHIRPayloadPersistenceCassandraImpl implements FHIRPayloadPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPayloadPersistenceCassandraImpl.class.getName());
    private static final long NANOS = 1000000000L;

    /**
     * Public constructor
     * @param tenantId
     */
    public FHIRPayloadPersistenceCassandraImpl() {
    }

    protected CqlSession getCqlSession() {
        return  DatasourceSessions.getSessionForTenantDatasource();
    }

    @Override
    public void storePayload(String partitionId, int resourceTypeId, String logicalId, int version, byte[] compressedPayload)
        throws FHIRPersistenceException {

        try (CqlSession session = getCqlSession()) {
            CqlStorePayload spl = new CqlStorePayload(partitionId, resourceTypeId, logicalId, version, compressedPayload);
            spl.run(session);
        }
    }

    @Override
    public <T extends Resource> T readResource(String partitionId, int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException {

        try (CqlSession session = getCqlSession()) {
            CqlReadResource spl = new CqlReadResource(partitionId, resourceTypeId, logicalId, version);
            return spl.run(session);
        }
    }
}