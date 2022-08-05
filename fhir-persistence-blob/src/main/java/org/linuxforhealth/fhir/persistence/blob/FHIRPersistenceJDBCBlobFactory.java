/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.blob;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory;
import org.linuxforhealth.fhir.persistence.payload.FHIRPayloadPersistence;

/**
 * Factory for decorating the JDBC persistence layer with a payload
 * persistence implementation using Azure Blob.
 */
public class FHIRPersistenceJDBCBlobFactory extends FHIRPersistenceJDBCFactory {

    @Override
    public FHIRPayloadPersistence getPayloadPersistence() throws FHIRPersistenceException {
        
        // If payload persistence is configured for this tenant, provide
        // the impl otherwise null
        FHIRPayloadPersistence result = null;
        if (BlobContainerManager.isPayloadPersistenceConfigured()) {
            result = new FHIRPayloadPersistenceBlobImpl();
        }
        
        return result;
    }
}