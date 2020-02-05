/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import java.util.Objects;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.util.FHIRPathUtil;

public class FHIRPatchMove extends FHIRPathPatchOperation {
    String fhirPath;
    int source;
    int destination;
    String elementName;

    public FHIRPatchMove(String fhirPath, Integer source, Integer destination) {
        this.fhirPath = Objects.requireNonNull(fhirPath);
        this.source = Objects.requireNonNull(source);
        this.destination = Objects.requireNonNull(destination);
        this.elementName = getElementName(fhirPath);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            return FHIRPathUtil.move(resource, fhirPath, elementName, source, destination);
        } catch (FHIRPathException e) {
            throw new FHIRPatchException("Error executing fhirPath", fhirPath);
        }
    }
}