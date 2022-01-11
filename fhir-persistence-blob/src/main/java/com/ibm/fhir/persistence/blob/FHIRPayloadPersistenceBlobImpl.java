/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.blob;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistenceSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResult;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResult.Status;
import com.ibm.fhir.persistence.util.InputOutputByteStream;


/**
 * Implementation to store and retrieve FHIR payload data using Azure Blob.
 */
public class FHIRPayloadPersistenceBlobImpl implements FHIRPayloadPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPayloadPersistenceBlobImpl.class.getName());
    private static final long NANOS = 1000000000L;
    public static final boolean PAYLOAD_COMPRESSED = true;
    
    /**
     * Public constructor
     */
    public FHIRPayloadPersistenceBlobImpl() {
    }

    /**
     * Get a tenant-specific connection
     */
    protected BlobManagedContainer getBlobContainerClient() {
        return  BlobContainerManager.getSessionForTenantDatasource();
    }

    @Override
    public PayloadPersistenceResponse storePayload(String resourceType, int resourceTypeId, String logicalId, int version, String resourcePayloadKey, Resource resource)
        throws FHIRPersistenceException {
        Future<PayloadPersistenceResult> result;
        try {
            // We leave compression to the storage platform, making it easier for other clients
            // to read the resource data if they want
            InputOutputByteStream ioStream = FHIRPersistenceSupport.render(resource, !PAYLOAD_COMPRESSED);
            BlobStorePayload spl = new BlobStorePayload(resourceTypeId, logicalId, version, resourcePayloadKey, ioStream);
            spl.run(getBlobContainerClient());

            // TODO actual async behavior
            result = CompletableFuture.completedFuture(new PayloadPersistenceResult(Status.OK));
        } catch (Exception x) {
            logger.log(Level.SEVERE, "storePayload failed for resource '" 
        + resourceType + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version + "'", x);
            result = CompletableFuture.completedFuture(new PayloadPersistenceResult(Status.FAILED));
        }
        return new PayloadPersistenceResponse(resourcePayloadKey, resourceType, resourceTypeId, logicalId, version, result);
    }

    @Override
    public <T extends Resource> T readResource(Class<T> resourceType, String rowResourceTypeName, int resourceTypeId, String logicalId, 
            int version, String resourcePayloadKey, List<String> elements) throws FHIRPersistenceException {

        logger.fine(() -> "readResource " + rowResourceTypeName + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version);
        final BlobPropertyGroupAdapter config = getConfigAdapter();
        BlobReadPayload cmd = new BlobReadPayload(resourceTypeId, logicalId, version, resourcePayloadKey, elements, !PAYLOAD_COMPRESSED);
        return cmd.run(resourceType, getBlobContainerClient());
    }

    @Override
    public void deletePayload(String resourceType, int resourceTypeId, String logicalId, Integer version, String resourcePayloadKey) throws FHIRPersistenceException {
        logger.fine(() -> "deletePayload " + resourceType + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version);
        BlobDeletePayload cmd = new BlobDeletePayload(resourceTypeId, logicalId, version, resourcePayloadKey);
        cmd.run(getBlobContainerClient());
    }

    /**
     * Get the config adapter for the current tenant/datasource
     * @return
     */
    private BlobPropertyGroupAdapter getConfigAdapter() {
        final String dsId = FHIRRequestContext.get().getDataStoreId();
        final String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + dsId;
        return BlobContainerManager.getPropertyGroupAdapter(dsPropertyName);
    }
}