/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.cassandra.CassandraPropertyGroupAdapter;
import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;
import com.ibm.fhir.persistence.cassandra.cql.TenantDatasourceKey;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPartitionStrategy;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;
import com.ibm.fhir.persistence.payload.PayloadPersistenceHelper;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResult;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResult.Status;
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
    
    // ** DO NOT CHANGE** The number of Base64 digits to use in the partition hash (4*6 = 24 bits)
    public static final int PARTITION_HASH_BASE64_DIGITS = 4;
    
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
        return new FHIRPayloadHashPartitionStrategy(PARTITION_HASH_BASE64_DIGITS);
    }

    /**
     * Get a tenant-specific session for Cassandra
     */
    protected CqlSession getCqlSession() {
        return  DatasourceSessions.getSessionForTenantDatasource();
    }

    @Override
    public PayloadPersistenceResponse storePayload(String resourceType, int resourceTypeId, String logicalId, int version, String resourcePayloadKey, Resource resource)
        throws FHIRPersistenceException {
        Future<PayloadPersistenceResult> result;

        final CassandraPropertyGroupAdapter config = getConfigAdapter();
        final String partitionName = partitionStrategy.getPartitionName(resourceType, logicalId);
        try (CqlSession session = getCqlSession()) {
            // Get the IO stream for the rendered resource.
            InputOutputByteStream ioStream = PayloadPersistenceHelper.render(resource, config.isCompress());
            CqlStorePayload spl = new CqlStorePayload(partitionName, resourceTypeId, logicalId, version, resourcePayloadKey, ioStream);
            spl.run(session);

            // TODO actual async behavior
            result = CompletableFuture.completedFuture(new PayloadPersistenceResult(Status.OK));
        } catch (Exception x) {
            logger.log(Level.SEVERE, "storePayload failed for resource '" 
        + resourceType + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version + "'", x);
            result = CompletableFuture.completedFuture(new PayloadPersistenceResult(Status.FAILED));
        }
        return new PayloadPersistenceResponse(resourcePayloadKey, resourceType, resourceTypeId, logicalId, version, partitionName, result);
    }

    @Override
    public <T extends Resource> T readResource(Class<T> resourceType, String rowResourceTypeName, int resourceTypeId, String logicalId, 
            int version, String resourcePayloadKey, List<String> elements) throws FHIRPersistenceException {

        logger.fine(() -> "readResource " + rowResourceTypeName + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version);
        final CassandraPropertyGroupAdapter config = getConfigAdapter();
        try (CqlSession session = getCqlSession()) {
            CqlReadResource spl = new CqlReadResource(partitionStrategy.getPartitionName(rowResourceTypeName, logicalId), resourceTypeId, logicalId, version, resourcePayloadKey, elements, config.isCompress());
            return spl.run(resourceType, session);
        }
    }

    @Override
    public void deletePayload(String resourceType, int resourceTypeId, String logicalId, Integer version, String resourcePayloadKey) throws FHIRPersistenceException {
        try (CqlSession session = getCqlSession()) {
            // Currently not supporting a real async implementation, so we 
            // process synchronously
            CqlDeletePayload spl = new CqlDeletePayload(partitionStrategy.getPartitionName(resourceType, logicalId),
                    resourceTypeId, logicalId, version, resourcePayloadKey);
            spl.run(session);
        }
    }

    /**
     * Get the {@link CassandraPropertyGroupAdapter} describing the configuration to use for
     * this payload offload implementation
     * @return
     */
    private CassandraPropertyGroupAdapter getConfigAdapter() {
        final String tenantId = FHIRRequestContext.get().getTenantId();
        final String dsId = FHIRRequestContext.get().getDataStoreId();
        TenantDatasourceKey key = new TenantDatasourceKey(tenantId, dsId);
        String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + key.getDatasourceId();
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        
        // Wrap the PropertyGroup in an adapter to make it easier to consume
        return new CassandraPropertyGroupAdapter(dsPG);
    }
}