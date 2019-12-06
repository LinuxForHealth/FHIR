/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.swagger.generator;

import java.io.InputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.StructureDefinition;

public class LoadStructureDefinitions {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        InputStream stream = LoadStructureDefinitions.class.getClassLoader().getResourceAsStream("profiles-resources.json");
        Bundle bundle = FHIRParser.parser(Format.JSON).parse(stream);
        int entryCount = 0;
        for (Entry entry : bundle.getEntry()) {
            if (entry.getResource() instanceof StructureDefinition) {
                StructureDefinition structureDefinition = (StructureDefinition) entry.getResource();
                if (structureDefinition != null) {
                    String id = structureDefinition.getId();
                    if (id != null) {
                        System.out.println("StructureDefinition.id: " + id);
                    }
                }
            }
            if (entry.getResource() instanceof OperationDefinition) {
                OperationDefinition operationDefinition = (OperationDefinition) entry.getResource();
                if (operationDefinition != null) {
                    String id = operationDefinition.getId();
                    if (id != null) {
                        System.out.println("OperationDefinition.id: " + id);
                    }
                }
            }
            entryCount++;
        }
        System.out.println("entryCount: " + entryCount);
        long end = System.currentTimeMillis();
        System.out.println("Processing time: " + (end - start) + " milliseconds.");
    }
}
