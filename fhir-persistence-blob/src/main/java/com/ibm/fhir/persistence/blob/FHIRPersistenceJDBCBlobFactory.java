/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.blob;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;

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