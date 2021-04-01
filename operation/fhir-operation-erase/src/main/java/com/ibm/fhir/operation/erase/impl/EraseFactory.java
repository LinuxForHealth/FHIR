/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.erase.impl;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 *
 */
public class EraseFactory {
    private EraseFactory() {
        // No Operation
    }

    public static Erase getInstance(FHIRResourceHelpers resourceHelper, Parameters parameters, Class<? extends Resource> resourceType,
        String logicalId, String versionId) {
        return new EraseImpl();
    }
}
