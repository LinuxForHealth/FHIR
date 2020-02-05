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

public class FHIRPathPatchInsert extends FHIRPathPatchOperation {
    String fhirPath;
    Element value;
    int index;
    String elementName;

    public FHIRPathPatchInsert(String fhirPath, Element value, Integer index) {
        this.fhirPath = Objects.requireNonNull(fhirPath);
        this.value = Objects.requireNonNull(value);
        this.index = Objects.requireNonNull(index);
        this.elementName = getElementName(fhirPath);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            return FHIRPathUtil.insert(resource, fhirPath, elementName, index, value);
        } catch (FHIRPathException e) {
            throw new FHIRPatchException("Error executing fhirPath", fhirPath);
        }
    }
}