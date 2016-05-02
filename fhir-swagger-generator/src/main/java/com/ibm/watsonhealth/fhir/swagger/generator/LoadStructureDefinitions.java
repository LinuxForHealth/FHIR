/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.swagger.generator;

import java.io.InputStream;

import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.Id;
import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.StructureDefinition;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

public class LoadStructureDefinitions {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        InputStream stream = LoadStructureDefinitions.class.getClassLoader().getResourceAsStream("profiles-resources.xml");
        Bundle bundle = FHIRUtil.read(Bundle.class, Format.XML, stream);
        int entryCount = 0;
        for (BundleEntry entry : bundle.getEntry()) {
            StructureDefinition structureDefinition = entry.getResource().getStructureDefinition();
            if (structureDefinition != null) {
                Id id = structureDefinition.getId();
                if (id != null) {
                    System.out.println("StructureDefinition.id: " + id.getValue());
                }
            }
            OperationDefinition operationDefinition = entry.getResource().getOperationDefinition();
            if (operationDefinition != null) {
                Id id = operationDefinition.getId();
                if (id != null) {
                    System.out.println("OperationDefinition.id: " + id.getValue());
                }
            }
            entryCount++;
        }
        System.out.println("entryCount: " + entryCount);
        long end = System.currentTimeMillis();
        System.out.println("Processing time: " + (end - start) + " milliseconds.");
    }
}
