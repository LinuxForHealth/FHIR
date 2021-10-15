/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.payload;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.cos.client.COSPayloadClient;
import com.ibm.fhir.persistence.cos.impl.COSClientManager;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;
import com.ibm.fhir.persistence.payload.PayloadKey;
import com.ibm.fhir.persistence.payload.PayloadPersistenceHelper;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * Provides storage and retrieval of FHIR payload data using IBM
 * cloud object storage (Cos).
 */
public class FHIRPayloadPersistenceCosImpl implements FHIRPayloadPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPayloadPersistenceCosImpl.class.getName());
    
    @Override
    public Future<PayloadKey> storePayload(String resourceTypeName, int resourceTypeId, String logicalId, int version, Resource resource)
        throws FHIRPersistenceException {
        long start = System.nanoTime();

        COSPayloadClient cpc = COSClientManager.getClientForTenantDatasource();

        try {
            // Render the object to a byte-stream but don't compress when storing in Cos
            // (although this could be made a configurable option if we want)
            InputOutputByteStream ioStream = PayloadPersistenceHelper.render(resource, false);
            final String objectName = makeObjectName(resourceTypeId, logicalId, version);
            cpc.write(objectName, ioStream);
            
            PayloadKey payloadKey = new PayloadKey(resourceTypeName, resourceTypeId, logicalId, version, null, objectName, PayloadKey.Status.OK);
            return CompletableFuture.completedFuture(payloadKey);
        } finally {
            if (logger.isLoggable(Level.FINE)) {
                long elapsed = System.nanoTime() - start;
                logger.fine(String.format("Wrote resource payload to COS: '%s/%s/%d' [took %5.3f s]", resourceTypeName, logicalId, version, elapsed/1e9));
            }
        }
    }

    @Override
    public <T extends Resource> T readResource(Class<T> resourceType, int resourceTypeId, String logicalId, int version, List<String> elements) throws FHIRPersistenceException {
        final long start = System.nanoTime();
        COSPayloadClient cpc = COSClientManager.getClientForTenantDatasource();

        final String objectName = makeObjectName(resourceTypeId, logicalId, version);
        try {
            return cpc.read(objectName, is -> PayloadPersistenceHelper.parse(resourceType, is, elements));

        } catch (RuntimeException x) {
            logger.severe("Failed to read payload for: '" + resourceType.getSimpleName() + "/" + logicalId + "/" + version + "', objectName = '" + objectName + "'");
            throw new FHIRPersistenceException("Failed to parse resource", x);
        } finally {
            if (logger.isLoggable(Level.FINE)) {
                long elapsed = System.nanoTime() - start;
                logger.fine(String.format("Read resource payload from COS: '%s/%s/%d' [took %5.3f s]", resourceType.getSimpleName(), logicalId, version, elapsed/1e9));
            }
        }
    }

    @Override
    public <T extends Resource> Future<T> readResource(Class<T> resourceType, PayloadKey payloadKey) throws FHIRPersistenceException {
        final long start = System.nanoTime();
        COSPayloadClient cpc = COSClientManager.getClientForTenantDatasource();

        final String objectName = makeObjectName(payloadKey);
        try {
            // We're not supporting async behavior yet, so we complete right away
            T resource = cpc.read(objectName, is -> PayloadPersistenceHelper.parse(resourceType, is, null));
            return CompletableFuture.completedFuture(resource);
        } catch (RuntimeException x) {
            logger.severe("Failed to read payload for key: '" + payloadKey + "'");
            throw new FHIRPersistenceException("Failed to parse resource", x);
        } finally {
            if (logger.isLoggable(Level.FINE)) {
                long elapsed = System.nanoTime() - start;
                logger.fine(String.format("Direct read of resource payload from COS: '%s/%s' [took %5.3f s]", resourceType.getSimpleName(), payloadKey.toString(), elapsed/1e9));
            }
        }
    }

    /**
     * Generate the COS object name to use for the given set of parameters
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @return
     */
    private static String makeObjectName(int resourceTypeId, String logicalId, int version) {
        StringBuilder objectName = new StringBuilder();
        objectName.append(Integer.toString(resourceTypeId));
        objectName.append("/");
        objectName.append(logicalId);
        objectName.append("/");
        objectName.append(Integer.toString(version));
        return objectName.toString();
    }

    /**
     * Obtain the COS object name using the information encoded in the payloadKey
     * @param payloadKey
     * @return
     */
    private static String makeObjectName(PayloadKey payloadKey) {
        return payloadKey.getPayloadId();
    }
    
    @Override
    public void deletePayload(int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException {
        COSPayloadClient cpc = COSClientManager.getClientForTenantDatasource();

        final String objectName = makeObjectName(resourceTypeId, logicalId, version);
        try {
            cpc.delete(objectName);
        } catch (FHIRPersistenceException x) {
            logger.severe("Failed to delete payload for: '" + resourceTypeId + "/" + logicalId + "/" + version + "'");
            throw x;
        }
    }
}