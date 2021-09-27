/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.test;

import static com.ibm.fhir.term.util.ValueSetSupport.getValueSet;

import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.term.service.FHIRTermService;

public class ExpansionPerformanceTest {
    public static final int ITERATIONS = 1000000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        ValueSet valueSet = getValueSet("http://ibm.com/fhir/ValueSet/vs4|1.0.0");

        for (int i = 0; i < ITERATIONS; i++) {
            FHIRTermService.getInstance().expand(valueSet);
        }

        long end = System.currentTimeMillis();

        System.out.println("Processing time for " + ITERATIONS + " iterations: " + (end - start) + " milliseconds");
    }
}
