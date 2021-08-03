/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;

/**
 * Preflight is designed to sanity check a request prior to executing the bulkdata request.
 */
public interface Preflight {
    /**
     * The preflight execution checks access to the Source,Outcome.
     *
     * @throws FHIROperationException
     */
    void preflight() throws FHIROperationException;

    /**
     * Checks the storage type is allowed.
     * @param storageDetail
     */
    void checkStorageAllowed(StorageDetail storageDetail) throws FHIROperationException;

    /**
     * checks parquet is enabled for this type
     * @return
     */
    default boolean checkParquet() {
        return false;
    }
}