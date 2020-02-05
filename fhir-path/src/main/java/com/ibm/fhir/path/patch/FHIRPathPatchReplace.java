/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import java.util.Objects;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.util.FHIRPathUtil;

public class FHIRPathPatchReplace extends FHIRPathPatchOperation {
    String fhirPath;
    Element value;
    String elementName;

    public FHIRPathPatchReplace(String fhirPath, Element value) {
        this.fhirPath = Objects.requireNonNull(fhirPath);
        this.value = Objects.requireNonNull(value);
        this.elementName = getElementName(fhirPath);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            return FHIRPathUtil.replace(resource, fhirPath, elementName, value);
        } catch (FHIRPathException e) {
            throw new FHIRPatchException("Error executing fhirPath", fhirPath);
        }
    }
}