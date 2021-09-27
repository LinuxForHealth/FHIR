/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.test;

import static com.ibm.fhir.term.util.ConceptMapSupport.getConceptMap;

import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.term.service.FHIRTermService;

public class TranslationPerformanceTest {
    public static final int ITERATIONS = 10000000;

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < ITERATIONS; i++) {
            ConceptMap conceptMap = getConceptMap("http://ibm.com/fhir/ConceptMap/snomed-ucum");

            Coding coding = Coding.builder()
                    .system(Uri.of("http://snomed.info/sct"))
                    .code(Code.of("258773002"))
                    .build();

            FHIRTermService.getInstance().translate(conceptMap, coding);
        }

        long end = System.currentTimeMillis();

        System.out.println("Processing time for " + ITERATIONS + " iterations: " + (end - start) + " milliseconds");
    }
}
