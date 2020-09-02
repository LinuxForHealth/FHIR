/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.service.provider.DefaultTermServiceProvider;
import com.ibm.fhir.term.spi.ExpansionParameters;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.spi.LookupOutcome;
import com.ibm.fhir.term.spi.LookupParameters;
import com.ibm.fhir.term.spi.TranslationOutcome;
import com.ibm.fhir.term.spi.TranslationParameters;
import com.ibm.fhir.term.spi.ValidationOutcome;
import com.ibm.fhir.term.spi.ValidationParameters;

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
     * Expand the given value set and expansion parameters
     *
     * @param valueSet
     *     the value set to expand
     * @param parameters
     *     the expansion parameters
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    @Override
    public ValueSet expand(ValueSet valueSet, ExpansionParameters parameters) {
        return provider.expand(valueSet, parameters);
    }

    /**
     * Expand the given value set
     *
     * @param valueSet
     *     the value set to expand
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    @Override
    public ValueSet expand(ValueSet valueSet) {
        return provider.expand(valueSet);
    }

    /**
     * Lookup the code system concept for the given system, version, code and lookup parameters
     *
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param parameters
     *     the lookup parameters
     * @return
     *     the outcome of the lookup
     */
    @Override
    public LookupOutcome lookup(Uri system, String version, Code code, LookupParameters parameters) {
        return provider.lookup(system, version, code, parameters);
    }

    /**
     * Lookup the code system concept for the given system, version, and code
     *
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @return
     *     the outcome of the lookup
     */
    @Override
    public LookupOutcome lookup(Uri system, String version, Code code) {
        return provider.lookup(system, version, code);
    }

    /**
     * Lookup the code system concept for the given coding and lookup parameters
     *
     * @param coding
     *     the coding to lookup
     * @param parameters
     *     the lookup parameters
     * @return
     *     the outcome of the lookup
     */
    @Override
    public LookupOutcome lookup(Coding coding, LookupParameters parameters) {
        return provider.lookup(coding, parameters);
    }

    /**
     * Lookup the code system concept for the given coding
     *
     * @param coding
     *     the coding to lookup
     * @return
     *     the outcome of the lookup
     */
    @Override
    public LookupOutcome lookup(Coding coding) {
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
     * Validate a code and display using the provided code system, version and validation parameters
     *
     * @param codeSystem
     *     the code system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param display
     *     the display
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(CodeSystem codeSystem, String version, Code code, String display, ValidationParameters parameters) {
        return provider.validateCode(codeSystem, version, code, display, parameters);
    }

    /**
     * Validate a code and display using the provided code system and version
     *
     * @param code system
     *     the code system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param display
     *     the display
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(CodeSystem codeSystem, String version, Code code, String display) {
        return provider.validateCode(codeSystem, version, code, display);
    }

    /**
     * Validate a coding using the provided code system and validation parameters
     *
     * @param codeSystem
     *     the code system
     * @param coding
     *     the coding
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(CodeSystem codeSystem, Coding coding, ValidationParameters parameters) {
        return provider.validateCode(codeSystem, coding, parameters);
    }

    /**
     * Validate a coding using the provided code system
     *
     * @param codeSystem
     *     the codeSystem
     * @param coding
     *     the coding
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(CodeSystem codeSystem, Coding coding) {
        return provider.validateCode(codeSystem, coding);
    }

    /**
     * Validate a codeable concept using the provided code system and validation parameters
     *
     * @param codeSystem
     *     the code system
     * @param codeableConcept
     *     the codeable concept
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(CodeSystem codeSystem, CodeableConcept codeableConcept, ValidationParameters parameters) {
        return provider.validateCode(codeSystem, codeableConcept, parameters);
    }

    /**
     * Validate a codeable concept using the provided code system
     *
     * @param codeableConcept
     *     the codeable concept
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(CodeSystem codeSystem, CodeableConcept codeableConcept) {
        return provider.validateCode(codeSystem, codeableConcept);
    }

    /**
     * Validate a code using the provided value set and validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param code
     *     the code
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, Code code, ValidationParameters parameters) {
        return provider.validateCode(valueSet, code, parameters);
    }

    /**
     * Validate a code using the provided value set
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param code
     *     the code
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, Code code) {
        return provider.validateCode(valueSet, code);
    }

    /**
     * Validate a code and display using the provided value set and validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param display
     *     the display
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, Uri system, String version, Code code, String display, ValidationParameters parameters) {
        return provider.validateCode(valueSet, system, version, code, display, parameters);
    }

    /**
     * Validate a code and display using the provided value set
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param display
     *     the display
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, Uri system, String version, Code code, String display) {
        return provider.validateCode(valueSet, system, version, code, display);
    }

    /**
     * Validate a coding using the provided value set using the provided validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param coding
     *     the coding
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, Coding coding, ValidationParameters parameters) {
        return provider.validateCode(valueSet, coding, parameters);
    }

    /**
     * Validate a coding using the provided value set using the provided validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param coding
     *     the coding
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, Coding coding) {
        return provider.validateCode(valueSet, coding);
    }

    /**
     * Validate a codeable concept using the provided value set using the provided validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param codeable concept
     *     the codeable concept
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, CodeableConcept codeableConcept, ValidationParameters parameters) {
        return provider.validateCode(valueSet, codeableConcept, parameters);
    }

    /**
     * Validate a codeable concept using the provided value set
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param codeable concept
     *     the codeable concept
     * @return
     *     the outcome of validation
     */
    @Override
    public ValidationOutcome validateCode(ValueSet valueSet, CodeableConcept codeableConcept) {
        return provider.validateCode(valueSet, codeableConcept);
    }

    /**
     * Translate the given system, version and code using the provided concept map and translation parameters
     *
     * @param conceptMap
     *     the concept map
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param parameters
     *     the translation parameters
     * @return
     *     the outcome of translation
     */
    @Override
    public TranslationOutcome translate(ConceptMap conceptMap, Uri system, String version, Code code, TranslationParameters parameters) {
        return provider.translate(conceptMap, system, version, code, parameters);
    }

    /**
     * Translate the given system, version and code using the provided concept map
     *
     * @param conceptMap
     *     the concept map
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @return
     *     the outcome of translation
     */
    @Override
    public TranslationOutcome translate(ConceptMap conceptMap, Uri system, String version, Code code) {
        return provider.translate(conceptMap, system, version, code);
    }

    /**
     * Translate the given coding using the provided concept map and translation parameters
     *
     * @param conceptMap
     *     the concept map
     * @param coding
     *     the coding
     * @param parameters
     *     the translation parameters
     * @return
     *     the outcome of translation
     */
    @Override
    public TranslationOutcome translate(ConceptMap conceptMap, Coding coding, TranslationParameters parameters) {
        return provider.translate(conceptMap, coding, parameters);
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

    /**
     * Translate the given codeable concept using the provided concept map and translation parameters
     *
     * @param conceptMap
     *     the concept map
     * @param codeableConcept
     *     the codeable concept
     * @param parameters
     *     the translation parameters
     * @return
     *     the outcome of translation
     */
    @Override
    public TranslationOutcome translate(ConceptMap conceptMap, CodeableConcept codeableConcept, TranslationParameters parameters) {
        return provider.translate(conceptMap, codeableConcept, parameters);
    }

    /**
     * Translate the given coding using the provided concept map
     *
     * @param conceptMap
     *     the concept map
     * @param codeable concept
     *     the codeable concept
     * @return
     *     the outcome of translation
     */
    @Override
    public TranslationOutcome translate(ConceptMap conceptMap, CodeableConcept codeableConcept) {
        return provider.translate(conceptMap, codeableConcept);
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
