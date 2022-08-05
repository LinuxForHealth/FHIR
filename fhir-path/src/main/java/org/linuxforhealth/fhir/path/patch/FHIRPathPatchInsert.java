/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.patch;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.Objects;

import org.linuxforhealth.fhir.model.patch.exception.FHIRPatchException;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;
import org.linuxforhealth.fhir.path.util.FHIRPathUtil;

class FHIRPathPatchInsert extends FHIRPathPatchOperation {
    String fhirPath;
    Element value;
    int index;

    public FHIRPathPatchInsert(String fhirPath, Element value, Integer index) {
        this.fhirPath = Objects.requireNonNull(fhirPath);
        this.value = Objects.requireNonNull(value);
        this.index = Objects.requireNonNull(index);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            return FHIRPathUtil.insert(resource, fhirPath, index, value);
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
                    .value(Code.of(FHIRPathPatchType.INSERT.value()))
                    .build())
                .part(Parameter.builder()
                    .name(string(PATH))
                    .value(string(fhirPath))
                    .build())
                .part(Parameter.builder()
                    .name(string(INDEX))
                    .value(org.linuxforhealth.fhir.model.type.Integer.of(index))
                    .build())
                .part(Parameter.builder()
                    .name(string(VALUE))
                    .value(value)
                    .build())
                .build();
    }
}