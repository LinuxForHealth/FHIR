/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.provider;

import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.code.CodeSystemContentMode;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;

/**
 * Default implementation of the FHIRTermServiceProvider interface using CodeSystemSupport
 */
public class DefaultTermServiceProvider implements FHIRTermServiceProvider {
    @Override
    public boolean isSupported(CodeSystem codeSystem) {
        return CodeSystemContentMode.COMPLETE.equals(codeSystem.getContent());
    }

    @Override
    public boolean hasConcept(CodeSystem codeSystem, Code code) {
        return getConcept(codeSystem, code) != null;
    }

    @Override
    public Concept getConcept(CodeSystem codeSystem, Code code) {
        return CodeSystemSupport.findConcept(codeSystem, code);
    }

    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem) {
        return CodeSystemSupport.getConcepts(codeSystem);
    }

    @Override
    public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
        Concept concept = CodeSystemSupport.findConcept(codeSystem, codeA);
        return (CodeSystemSupport.findConcept(codeSystem, concept, codeB) != null);
    }

    @Override
    public Set<Concept> closure(CodeSystem codeSystem, Code code) {
        Concept concept = CodeSystemSupport.findConcept(codeSystem, code);
        return CodeSystemSupport.getConcepts(concept);
    }
}
