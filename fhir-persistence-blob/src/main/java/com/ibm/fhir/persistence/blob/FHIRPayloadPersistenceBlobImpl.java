/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.blob;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistenceSupport;
import com.ibm.fhir.persistence.ResourceResult;
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
    protected BlobManagedContainer getBlobManagedContainer() {
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
            logger.fine(() -> "payload size: " + ioStream.getRawBuffer().length + " bytes");
            BlobStorePayload spl = new BlobStorePayload(resourceTypeId, logicalId, version, resourcePayloadKey, ioStream);
            result = spl.run(getBlobManagedContainer());

        } catch (FHIRPersistenceException x) {
            logger.log(Level.SEVERE, "storePayload request failed for resource '" 
        + resourceType + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version + "'");
            throw x;
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
        BlobReadPayload cmd = new BlobReadPayload(resourceTypeId, logicalId, version, resourcePayloadKey, elements, !PAYLOAD_COMPRESSED);
        try {
            // synchronous read
            return cmd.run(resourceType, getBlobManagedContainer()).get();
        } catch (ExecutionException e) {
            // Unwrap the exceptions to avoid over-nesting
            // ExecutionException -> RuntimeException -> FHIRPersistenceException
            if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof FHIRPersistenceException) {
                throw (FHIRPersistenceException)e.getCause().getCause();
            } else {
                throw new FHIRPersistenceException("execution failed", e);
            }
        } catch (InterruptedException e) {
            throw new FHIRPersistenceException("fetch interrupted", e);
        }
    }

    @Override
    public <T extends Resource> CompletableFuture<ResourceResult<? extends Resource>> readResourceAsync(Class<T> resourceType, String rowResourceTypeName,
        int resourceTypeId, String logicalId, int version, String resourcePayloadKey, Instant lastUpdated, List<String> elements) throws FHIRPersistenceException {

        logger.fine(() -> "readResource " + rowResourceTypeName + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version);
        BlobReadPayload cmd = new BlobReadPayload(resourceTypeId, logicalId, version, resourcePayloadKey, elements, !PAYLOAD_COMPRESSED);

        // Initiate the read and add a transformation step (thenApply) which will wrap the
        // resource as a ResourceResult. This makes it easier to consume in lists where
        // we may want to hold onto the identity of an entry but where the resource value
        // is null
        return cmd.run(resourceType, getBlobManagedContainer()).thenApply(resource -> {
            // transform: wrap the retrieved resource in a ResourceResult
            ResourceResult.Builder<T> builder = new ResourceResult.Builder<>();
            builder.logicalId(logicalId);
            builder.resourceTypeName(rowResourceTypeName);
            builder.deleted(false); // we wouldn't be fetching if the resource were deleted
            builder.resource(resource);
            builder.version(version);
            builder.lastUpdated(lastUpdated);
            return builder.build();
        });
    }

    @Override
    public void deletePayload(String resourceType, int resourceTypeId, String logicalId, Integer version, String resourcePayloadKey) throws FHIRPersistenceException {
        // Note that deletePayload should not be confused with the FHIR DELETE interaction. This deletePayload
        // method is used for '$erase' operations and also as part of the reconciliation service implementation
        // which is used clean up orphaned payload records for which there are no corresponding records in the
        // FHIR relational schema.
        if (version != null) {
            logger.fine(() -> "deletePayload " + resourceType + "[" + resourceTypeId + "]/" + logicalId + "/_history/" + version);
        } else {
            logger.fine(() -> "deletePayload " + resourceType + "[" + resourceTypeId + "]/" + logicalId);
        }
        BlobDeletePayload cmd = new BlobDeletePayload(resourceTypeId, logicalId, version, resourcePayloadKey);
        cmd.run(getBlobManagedContainer());
    }
}