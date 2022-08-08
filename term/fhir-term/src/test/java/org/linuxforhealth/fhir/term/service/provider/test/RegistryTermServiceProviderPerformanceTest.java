/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service.provider.test;

import org.linuxforhealth.fhir.cache.CachingProxy;
import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.term.service.provider.RegistryTermServiceProvider;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;

public class RegistryTermServiceProviderPerformanceTest {
    public static final int MAX_ITERATIONS = 1000000;

    public static void main(String[] args) {
        FHIRTermServiceProvider provider = new RegistryTermServiceProvider();

        CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode");
        Code code = Code.of("STORE");

        long start = System.currentTimeMillis();

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            provider.getConcept(codeSystem, code);
        }

        long end = System.currentTimeMillis();

        System.out.println("Running time: " + (end - start) + " milliseconds");

        provider = CachingProxy.newInstance(FHIRTermServiceProvider.class, new RegistryTermServiceProvider());

        start = System.currentTimeMillis();

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            provider.getConcept(codeSystem, code);
        }

        end = System.currentTimeMillis();

        System.out.println("Running time: " + (end - start) + " milliseconds");
    }
}
