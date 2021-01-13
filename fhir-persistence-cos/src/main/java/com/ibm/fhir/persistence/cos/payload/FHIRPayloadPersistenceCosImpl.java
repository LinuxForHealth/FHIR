/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.payload;

import com.ibm.fhir.persistence.payload.FHIRPayloadPersistence;


/**
 * Provides storage and retrieval of FHIR payload data using IBM
 * cloud object storage (Cos).
 */
public class FHIRPayloadPersistenceCosImpl implements FHIRPayloadPersistence {

    @Override
    public int storePayload(String logicalId, byte[] data) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte[] readPayload(String logicalId, int version) {
        // TODO Auto-generated method stub
        return null;
    }

}
