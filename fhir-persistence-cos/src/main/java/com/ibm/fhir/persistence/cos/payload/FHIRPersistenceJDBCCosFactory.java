/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.payload;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory;
import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;

/**
 *
 */
public class FHIRPersistenceJDBCCosFactory extends FHIRPersistenceJDBCFactory {

    @Override
    public FHIRPayloadPersistence getPayloadPersistence() throws FHIRPersistenceException {
        // Store the payload in Cloud Object Storage (Cos)
        return new FHIRPayloadPersistenceCosImpl();
    };
}