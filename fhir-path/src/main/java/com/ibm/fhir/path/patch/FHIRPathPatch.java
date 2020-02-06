/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;

public class FHIRPathPatch implements FHIRPatch {
    private List<FHIRPathPatchOperation> operations;

    /**
     * @throws IllegalArgumentException if the Parameters object does not satisfy the requirements of a FHIRPath Patch
     */
    public FHIRPathPatch(Parameters params) {
        Objects.requireNonNull(params);
        operations = new ArrayList<FHIRPathPatchOperation>(2);
        for (Parameter param : params.getParameter()) {
            if (!"operation".equals(param.getName().getValue())) {
                throw new IllegalArgumentException("Each FHIRPath patch operation must have a name of 'operation'");
            }
            operations.add(FHIRPathPatchOperation.parse(param));
        }
    }

    public FHIRPathPatch(FHIRPathPatchOperation... operation) {
        this.operations = Collections.unmodifiableList(Arrays.asList(operation));
    }

    @Override
    public <T extends Resource> T apply(T resource) throws FHIRPatchException {
        for (FHIRPathPatchOperation fhirPathPatchOperation : operations) {
            resource = fhirPathPatchOperation.apply(resource);
        }
        return resource;
    }
}
