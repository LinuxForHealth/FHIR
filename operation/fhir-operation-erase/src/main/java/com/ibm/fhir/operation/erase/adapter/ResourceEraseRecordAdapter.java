/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.adapter;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.ResourceEraseRecord.Status;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

/**
 * Adapts the ResourceEraseRecord and EraseDTO to a Parameters object
 */
public class ResourceEraseRecordAdapter {
    private static final Parameter PARTIAL_TRUE = Parameter.builder()
                                                    .name(string("partial"))
                                                    .value(com.ibm.fhir.model.type.Boolean.TRUE)
                                                    .build();
    private static final Parameter PARTIAL_FALSE = Parameter.builder()
                                                    .name(string("partial"))
                                                    .value(com.ibm.fhir.model.type.Boolean.FALSE)
                                                    .build();

    public ResourceEraseRecordAdapter() {
        // NOP
    }

    /**
     * Adapts from an eraseRecord and eraseDto to the Parameters resource and
     * wraps in an output parameters object.
     *
     * @param eraseRecord the output from the erase dao
     * @param eraseDto the input from the user send to the erase dao
     * @return
     */
    public Parameters adapt(ResourceEraseRecord eraseRecord, EraseDTO eraseDto) {
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        List<Parameter> parameters = new ArrayList<>();

        // Generate Reference indicates the [Type]/[Id] or [Type]/[Id]/_history/[Version]
        parameters.add(Parameter.builder()
                        .name(string("resource"))
                        .value(string(eraseDto.generateReference()))
                        .build());

        if (eraseRecord.getStatus() == Status.PARTIAL) {
            parameters.add(PARTIAL_TRUE);
        } else {
            parameters.add(PARTIAL_FALSE);
        }

        /*
         * @implNote considered including the versions that were erased, however
         * the Parameters object is going to be unwieldy, so we opt for just a count
         * along with partial to indicate that the erase is complete, or not yet complete.
         */
        parameters.add(Parameter.builder()
                        .name(string("total"))
                        .value(com.ibm.fhir.model.type.Integer.of(eraseRecord.getTotal()))
                        .build());

        Parameters resource = builder.parameter(parameters).build();
        return FHIROperationUtil.getOutputParameters(resource);
    }
}
