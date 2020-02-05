/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.util.FHIRPathUtil;

public class FHIRPatchDelete extends FHIRPathPatchOperation {
    String fhirPath;
    String elementName;

    public FHIRPatchDelete(String fhirPath) {
        this.fhirPath = fhirPath;
        this.elementName = getElementName(fhirPath);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            return FHIRPathUtil.delete(resource, fhirPath, elementName);
        } catch (FHIRPathException e) {
            throw new FHIRPatchException("Error executing fhirPath", fhirPath);
        }
    }
}