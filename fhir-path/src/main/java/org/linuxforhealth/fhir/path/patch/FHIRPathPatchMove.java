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
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;
import org.linuxforhealth.fhir.path.util.FHIRPathUtil;

class FHIRPathPatchMove extends FHIRPathPatchOperation {
    String fhirPath;
    int source;
    int destination;

    public FHIRPathPatchMove(String fhirPath, Integer source, Integer destination) {
        this.fhirPath = Objects.requireNonNull(fhirPath);
        this.source = Objects.requireNonNull(source);
        this.destination = Objects.requireNonNull(destination);
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        try {
            return FHIRPathUtil.move(resource, fhirPath, source, destination);
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
                    .value(Code.of(FHIRPathPatchType.MOVE.value()))
                    .build())
                .part(Parameter.builder()
                    .name(string(PATH))
                    .value(string(fhirPath))
                    .build())
                .part(Parameter.builder()
                    .name(string(SOURCE))
                    .value(org.linuxforhealth.fhir.model.type.Integer.of(source))
                    .build())
                .part(Parameter.builder()
                    .name(string(DESTINATION))
                    .value(org.linuxforhealth.fhir.model.type.Integer.of(destination))
                    .build())
                .build();
    }
}