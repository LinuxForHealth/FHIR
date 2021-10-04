/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.payload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.cos.client.COSPayloadClient;
import com.ibm.fhir.persistence.cos.impl.COSClientManager;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;
import com.ibm.fhir.persistence.payload.PayloadKey;
import com.ibm.fhir.persistence.util.InputOutputByteStream;
import com.ibm.fhir.search.SearchConstants;


/**
 * Provides storage and retrieval of FHIR payload data using IBM
 * cloud object storage (Cos).
 */
public class FHIRPayloadPersistenceCosImpl implements FHIRPayloadPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPayloadPersistenceCosImpl.class.getName());

    @Override
    public Future<PayloadKey> storePayload(String resourceTypeName, int resourceTypeId, String logicalId, int version, InputOutputByteStream payloadStream)
        throws FHIRPersistenceException {
        long start = System.nanoTime();

        // Single resources are small enough that we can write the object
        // in a single call without having to resort to multi-part uploads.
        COSPayloadClient cpc = COSClientManager.getClientForTenantDatasource();

        try {
            final String objectName = makeObjectName(resourceTypeId, logicalId, version);
            cpc.write(objectName, payloadStream);
            
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
            return cpc.read(objectName, is -> parse(resourceType, is, elements));

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
            T resource = cpc.read(objectName, is -> parse(resourceType, is, null));
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
     * Parse the given stream, using elements if needed
     * @param <T>
     * @param resourceType
     * @param in
     * @param elements
     * @return
     * @throws IOException
     * @throws FHIRParserException
     */
    public static <T extends Resource> T parse(Class<T> resourceType, InputStream in, List<String> elements) {
        T result;

        try {
            if (elements != null) {
                // parse/filter the resource using elements
                result = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class).parseAndFilter(in, elements);
                if (resourceType.equals(result.getClass()) && !FHIRUtil.hasTag(result, SearchConstants.SUBSETTED_TAG)) {
                    // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                    result = FHIRUtil.addTag(result, SearchConstants.SUBSETTED_TAG);
                }
            } else {
                result = FHIRParser.parser(Format.JSON).parse(in);
            }
        } catch (FHIRParserException x) {
            // need to wrap because this method is being called as a lambda
            throw new RuntimeException(x);
        }

        return result;
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