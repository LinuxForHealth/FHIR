/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;


/**
 * Implementation to store and retrieve FHIR payload data using Cassandra.
 * The {@link #storePayload(String, byte[])} method will iterate to find the
 * next version. Unless concurrency is really high, the version will be found
 * on the first or second attempt. For most cases, the insert is simply going to
 * be a read of a single row (if it exists) followed by an insert.
 */
public class FHIRPayloadPersistenceCassandraImpl implements FHIRPayloadPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPayloadPersistenceCassandraImpl.class.getName());
    private static final long NANOS = 1000000000L;

    // TODO needs to be configurable
    private long maxAttemptTime = 30L * NANOS;

    // Identifies the tenant to which this object is restricted
    private final String tenantId;

    /**
     * Public constructor
     * @param tenantId
     */
    public FHIRPayloadPersistenceCassandraImpl(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public int storePayload(String logicalId, byte[] data) throws FHIRPersistenceException {
        // To handle concurrency, grab the current version and then try to
        // insert current version + 1. If that fails, we assume that someone
        // else beat us, so try current version + 2. If that fails, go back
        // to the beginning and read again
        int version = 0;
        long start = System.nanoTime();
        while (version < 1 && System.nanoTime() - start < maxAttemptTime) {
            // Get the current version...if this is a new resource, we get 0
            version = getCurrentVersion(logicalId);
            version++;
            if (!storePayload(logicalId, version, data)) {
                // Probably a collision, so try with the next
                // version without reading again
                version++;
                if (!storePayload(logicalId, version, data)) {
                    // loop back and read the current version again
                    version = 0;
                }
            }
        }

        if (version == 0) {
            // didn't complete the insert before timeout
            throw new FHIRPersistenceDataAccessException("Timed out attempting to insert payload");
        }

        return version;
    }

    /**
     * Fetch the current version of the logical resource for the given id
     * @param logicalId
     * @return
     */
    private int getCurrentVersion(String logicalId) {
        int result;

        // This is essentially an index range scan (in reverse order) but
        // we only fetch the first record. This relies on the fact that
        // the payload table is collated with (LOGICAL_ID ASC, VERSION DESC),
        // making this fetch a simple index retrieval O(log(N))
        try (CqlSession session = getCqlSession()) {
            CqlGetCurrentVersion cmd = new CqlGetCurrentVersion(resourceType, logicalId);
            result = cmd.execute(session);
        }

        return result;
    }

    protected CqlSession getCqlSession() {
        return  DatasourceSessions.getSessionForTenantDatasource();
    }

    @Override
    public byte[] readPayload(String logicalId, int version) throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Attempt to insert the payload data using the given logicalId/version key.
     * @param logicalId
     * @param version
     * @param payload
     * @return true if the insert was successful, false if it failed due to duplicate key
     */
    private boolean storePayload(String logicalId, int version, byte[] payload) {

        boolean result;

        try (CqlSession session = getCqlSession()) {
            CqlStorePayload spl = new CqlStorePayload(partitionId, resourceTypeId, logicalId, version, payload);
        }

        return result;
        return false;
    }
}
