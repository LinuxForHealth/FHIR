/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.payload;

import java.io.InputStream;
import java.util.logging.Logger;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.cos.client.COSPayloadClient;
import com.ibm.fhir.persistence.cos.impl.COSClientManager;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;
import com.ibm.fhir.persistence.payload.PayloadPersistenceHelper;


/**
 * Provides storage and retrieval of FHIR payload data using IBM
 * cloud object storage (Cos).
 */
public class FHIRPayloadPersistenceCosImpl implements FHIRPayloadPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPayloadPersistenceCosImpl.class.getName());

    @Override
    public void storePayload(String partitionId, int resourceTypeId, String logicalId, int version, byte[] compressedPayload)
        throws FHIRPersistenceException {

        // Single resources are small enough that we can write the object
        // in a single call without having to resort to multi-part uploads.
        COSPayloadClient cpc = COSClientManager.getClientForTenantDatasource();

        final String objectName = makeObjectName(resourceTypeId, logicalId, version);
        cpc.write(objectName, compressedPayload);
    }

    @Override
    public <T extends Resource> T readResource(String partitionId, int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException {

        COSPayloadClient cpc = COSClientManager.getClientForTenantDatasource();

        final String objectName = makeObjectName(resourceTypeId, logicalId, version);
        try {
            return cpc.read(objectName, FHIRPayloadPersistenceCosImpl::parse);
        } catch (RuntimeException x) {
            logger.severe("Failed to read payload for: '" + resourceTypeId + "/" + logicalId + "/" + version + "'");
            throw new FHIRPersistenceException("Failed to parse resource", x);
        }
    }

    /**
     * Parse the resource from the data read from the given stream
     * @param <T>
     * @param is
     * @return
     */
    private static <T extends Resource> T parse(InputStream is) {
        try {
            return PayloadPersistenceHelper.parse(is);
        } catch (FHIRParserException x) {
            // this is called as a lambda so we need to wrap the checked exception
            throw new RuntimeException(x);
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
}
