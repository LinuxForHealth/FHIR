/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.graph.registry.SnomedRegistryResourceProvider;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

public class SnomedRegistryResourceProviderTest {
    @BeforeClass
    public void beforeClass() {
        FHIRTermService.getInstance().addProvider(new FHIRTermServiceProvider() {
            @Override
            public Set<Concept> closure(CodeSystem codeSystem, Code code) {
                return Collections.emptySet();
            }

            @Override
            public Map<Code, Set<Concept>> closure(CodeSystem codeSystem, Set<Code> codes) {
                return Collections.emptyMap();
            }

            @Override
            public Concept getConcept(CodeSystem codeSystem, Code code) {
                return Concept.builder()
                        .code(code)
                        .build();
            }

            @Override
            public Set<Concept> getConcepts(CodeSystem codeSystem) {
                return Collections.emptySet();
            }

            @Override
            public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
                return Collections.emptySet();
            }

            @Override
            public boolean hasConcept(CodeSystem codeSystem, Code code) {
                return true;
            }

            @Override
            public boolean isSupported(CodeSystem codeSystem) {
                return SnomedRegistryResourceProvider.SNOMED_CODE_SYSTEM.equals(codeSystem);
            }

            @Override
            public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
                return false;
            }
        });
    }

    @Test
    public void testGetAllConceptsImplicitValueSet() {
        ValueSet valueSet = FHIRRegistry.getInstance().getResource("http://snomed.info/sct?fhir_vs", ValueSet.class);
        Assert.assertNotNull(valueSet);
    }

    @Test
    public void testGetSubsumedByImplicitValueSet() {
        ValueSet valueSet = FHIRRegistry.getInstance().getResource("http://snomed.info/sct?fhir_vs=isa/195967001", ValueSet.class);
        Assert.assertNotNull(valueSet);
    }

    @Test
    public void testGetCodeSystem() {
        CodeSystem codeSystem = FHIRRegistry.getInstance().getResource("http://snomed.info/sct", CodeSystem.class);
        Assert.assertNotNull(codeSystem);
    }
}
