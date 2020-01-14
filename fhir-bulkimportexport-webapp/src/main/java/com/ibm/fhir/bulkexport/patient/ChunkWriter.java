/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.patient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.batch.api.BatchProperty;
import javax.inject.Inject;

import com.ibm.fhir.search.compartment.CompartmentUtil;

/**
 * Bulk patient export Chunk implementation - the Writer.
 *
 */

public class ChunkWriter extends com.ibm.fhir.bulkexport.system.ChunkWriter {

    /**
     * Fhir ResourceType.
     */
    @Inject
    @BatchProperty(name = "fhir.resourcetype")
    String fhirResourceType;

    @Override
    protected List<String> getResourceTypes() throws Exception {
        List<String> resourceTypes;
        List <String> allCompartmentResourceTypes = CompartmentUtil.getCompartmentResourceTypes("Patient");
        if (fhirResourceType == null ) {
            resourceTypes = allCompartmentResourceTypes;
        } else {
            List<String> tmpResourceTypes = Arrays.asList(fhirResourceType.split("\\s*,\\s*"));
            resourceTypes = tmpResourceTypes.stream().filter(item-> allCompartmentResourceTypes.contains(item)).collect(Collectors.toList());
            if (resourceTypes == null || resourceTypes.isEmpty()) {
                throw new Exception("readItem: None of the input resource types is valid!");
            }
        }
        return resourceTypes;
    }
}
