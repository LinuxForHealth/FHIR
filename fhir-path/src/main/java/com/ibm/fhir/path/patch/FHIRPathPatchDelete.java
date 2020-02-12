/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import static com.ibm.fhir.model.type.String.string;

import java.util.Objects;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.path.util.FHIRPathUtil;

class FHIRPathPatchDelete extends FHIRPathPatchOperation {
    String fhirPath;

    public FHIRPathPatchDelete(String fhirPath) {
        this.fhirPath = Objects.requireNonNull(fhirPath);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            return FHIRPathUtil.delete(resource, fhirPath);
        } catch (FHIRPathException e) {
            throw new FHIRPatchException("Error executing fhirPath", fhirPath);
        }
    }

    @Override
    public Parameter toParameter() {
        return Parameter.builder()
                .name(string(OPERATION))
                .part(Parameter.builder()
                    .name(string(TYPE))
                    .value(Code.of(FHIRPathPatchType.DELETE.value()))
                    .build())
                .part(Parameter.builder()
                    .name(string(PATH))
                    .value(string(fhirPath))
                    .build())
                .build();
    }
}