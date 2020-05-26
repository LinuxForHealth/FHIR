/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.service.provider.DefaultTermServiceProvider;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.spi.TranslationOutcome;

public class FHIRTermService implements FHIRTermServiceProvider {
    private static final FHIRTermService INSTANCE = new FHIRTermService();

    private final FHIRTermServiceProvider provider;

    private FHIRTermService() {
        provider = loadProvider();
    }

    /**
     * Indicates whether the given value set is expandable
     *
     * @param valueSet
     *     the value set
     * @return
     *     true if the given value set is expandable, false otherwise
     */
    @Override
    public boolean isExpandable(ValueSet valueSet) {
        return provider.isExpandable(valueSet);
    }

    /**
     * Expand the given value set per the algorithm here: http://hl7.org/fhir/valueset.html#expansion
     *
     * @param valueSet
     *     the value set to be expanded
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    @Override
    public ValueSet expand(ValueSet valueSet) {
        return provider.expand(valueSet);
    }

    /**
     * Lookup the code system concept for the given coding
     *
     * @param coding
     *     the coding to lookup
     * @return
     *     the code system concept that matches the given coding, or null if no such concept exists
     */
    @Override
    public Concept lookup(Coding coding) {
        return provider.lookup(coding);
    }

    /**
     * Perform a subsumption test to determine if the code system concept represented by the given coding "A" subsumes
     * the code system concept represented by the given coding "B"
     *
     * @param codingA
     *     the coding "A"
     * @param codingB
     *     the coding "B"
     * @return
     *     the outcome of the subsumption test
     */
    @Override
    public ConceptSubsumptionOutcome subsumes(Coding codingA, Coding codingB) {
        return provider.subsumes(codingA, codingB);
    }

    /**
     * Generate the transitive closure for the code system concept represented by the given coding
     *
     * @param coding
     *     the coding
     * @return
     *     a set containing the transitive closure for the code system concept represented by the given coding
     */
    @Override
    public Set<Concept> closure(Coding coding) {
        return provider.closure(coding);
    }

    /**
     * Indicates whether a code system concept matches the given coding
     *
     * @param coding
     *     the coding
     * @return
     *     true if a code system concept matches the given coding, false otherwise
     */
    @Override
    public boolean validateCode(Coding coding) {
        return provider.validateCode(coding);
    }

    /**
     * Indicates whether the given code is a member of the provided value set
     *
     * @implSpec
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @return
     *     true if the given code is a member of the provided value set, false otherwise
     */
    @Override
    public boolean validateCode(ValueSet valueSet, String system, String version, String code) {
        return provider.validateCode(valueSet, system, version, code);
    }

    /**
     * Indicates whether the given coding is a member of the provided value set
     *
     * @implSpec
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param coding
     *     the coding
     * @return
     *     true if the given coding is a member of the provided value set, false otherwise
     */
    @Override
    public boolean validateCode(ValueSet valueSet, Coding coding) {
        return provider.validateCode(valueSet, coding);
    }

    /**
     * Indicates whether the given codeable concept contains a coding that is a member of the provided value set
     *
     * @implSpec
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param codeableConcept
     *     the codeable concept
     * @return
     *     true if the given codeable concept contains a coding that is a member of the provided value set, false otherwise
     */
    @Override
    public boolean validateCode(ValueSet valueSet, CodeableConcept codeableConcept) {
        return provider.validateCode(valueSet, codeableConcept);
    }

    /**
     * Translate the given coding using the provided concept map
     *
     * @param conceptMap
     *     the concept map
     * @param coding
     *     the coding
     * @return
     *     the outcome of translation
     */
    @Override
    public TranslationOutcome translate(ConceptMap conceptMap, Coding coding) {
        return provider.translate(conceptMap, coding);
    }

    public static FHIRTermService getInstance() {
        return INSTANCE;
    }

    private FHIRTermServiceProvider loadProvider() {
        Iterator<FHIRTermServiceProvider> iterator = ServiceLoader.load(FHIRTermServiceProvider.class).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return new DefaultTermServiceProvider();
    }
}
