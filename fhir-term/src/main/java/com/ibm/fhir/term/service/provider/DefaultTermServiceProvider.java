/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.provider;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;
import com.ibm.fhir.term.util.ValueSetSupport;

public class DefaultTermServiceProvider implements FHIRTermServiceProvider {
    @Override
    public ValueSet expand(ValueSet valueSet) {
        return ValueSetSupport.expand(valueSet);
    }

    @Override
    public Concept lookup(Coding coding) {
        String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
        String code = (coding.getCode() != null) ? coding.getCode().getValue() : null;

        if (system != null && code != null) {
            String url = (version != null) ? system + "|" + version : system;
            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(url);
            if (codeSystem != null) {
                return CodeSystemSupport.findConcept(codeSystem, Code.of(code));
            }
        }

        return null;
    }

    @Override
    public ConceptSubsumptionOutcome subsumes(Coding codingA, Coding codingB) {
        String systemA = (codingA.getSystem() != null) ? codingA.getSystem().getValue() : null;
        String versionA = (codingA.getVersion() != null) ? codingA.getVersion().getValue() : null;
        String codeA = (codingA.getCode() != null) ? codingA.getCode().getValue() : null;

        String systemB = (codingB.getSystem() != null) ? codingB.getSystem().getValue() : null;
        String versionB = (codingB.getVersion() != null) ? codingB.getVersion().getValue() : null;
        String codeB = (codingB.getCode() != null) ? codingB.getCode().getValue() : null;

        if (systemA != null && systemB != null && codeA != null && codeB != null && systemA.equals(systemB)) {
            String url = systemA;

            if (versionA != null || versionB != null) {
                if (versionA != null && versionB != null && !versionA.equals(versionB)) {
                    return null;
                }
                url = (versionA != null) ? (url + "|" + versionA) : (url + "|" + versionB);
            }

            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(url);
            if (codeSystem != null && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                Concept conceptA = CodeSystemSupport.findConcept(codeSystem, Code.of(codeA));
                if (conceptA != null) {
                    Concept conceptB = CodeSystemSupport.findConcept(conceptA, Code.of(codeB));
                    if (conceptA.equals(conceptB)) {
                        return ConceptSubsumptionOutcome.EQUIVALENT;
                    }
                    if (conceptB != null) {
                        return ConceptSubsumptionOutcome.SUBSUMES;
                    }
                    conceptB = CodeSystemSupport.findConcept(codeSystem, Code.of(codeB));
                    if (conceptB != null) {
                        conceptA = CodeSystemSupport.findConcept(conceptB, Code.of(codeA));
                        return (conceptA != null) ? ConceptSubsumptionOutcome.SUBSUMED_BY : ConceptSubsumptionOutcome.NOT_SUBSUMED;
                    }
                }
            }
        }

        return null;
    }
}
