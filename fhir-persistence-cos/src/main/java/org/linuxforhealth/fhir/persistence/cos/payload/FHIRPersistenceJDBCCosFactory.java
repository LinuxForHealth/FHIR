/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cos.payload;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory;
import org.linuxforhealth.fhir.persistence.payload.FHIRPayloadPersistence;

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