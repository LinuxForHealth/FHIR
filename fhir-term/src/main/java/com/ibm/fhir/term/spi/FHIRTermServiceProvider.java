/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

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

public interface FHIRTermServiceProvider {
    /**
     * Indicates whether the given value set is expandable
     *
     * @param valueSet
     *     the value set
     * @return
     *     true if the given value set is expandable, false otherwise
     */
    boolean isExpandable(ValueSet valueSet);

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
    ValueSet expand(ValueSet valueSet, ExpansionParameters parameters);

    /**
     * Expand the given value set
     *
     * @param valueSet
     *     the value set to expand
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    default ValueSet expand(ValueSet valueSet) {
        return expand(valueSet, ExpansionParameters.EMPTY);
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
    default LookupOutcome lookup(Uri system, String version, Code code, LookupParameters parameters) {
        Coding coding = Coding.builder()
                .system(system)
                .version(version)
                .code(code)
                .build();
        return lookup(coding, parameters);
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
    default LookupOutcome lookup(Uri system, String version, Code code) {
        return lookup(system, version, code, LookupParameters.EMPTY);
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
    LookupOutcome lookup(Coding coding, LookupParameters parameters);

    /**
     * Lookup the code system concept for the given coding
     *
     * @param coding
     *     the coding to lookup
     * @return
     *     the outcome of the lookup
     */
    default LookupOutcome lookup(Coding coding) {
        return lookup(coding, LookupParameters.EMPTY);
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
    ConceptSubsumptionOutcome subsumes(Coding codingA, Coding codingB);

    /**
     * Generate the transitive closure for the code system concept represented by the given coding
     *
     * @param coding
     *     the coding
     * @return
     *     a set containing the transitive closure for the code system concept represented by the given coding
     */
    Set<Concept> closure(Coding coding);

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
    default ValidationOutcome validateCode(CodeSystem codeSystem, String version, Code code, String display, ValidationParameters parameters) {
        Coding coding = Coding.builder()
                .version(version)
                .code(code)
                .display(display)
                .build();
        return validateCode(codeSystem, coding, parameters);
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
    default ValidationOutcome validateCode(CodeSystem codeSystem, String version, Code code, String display) {
        return validateCode(codeSystem, version, code, display, ValidationParameters.EMPTY);
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
    ValidationOutcome validateCode(CodeSystem codeSystem, Coding coding, ValidationParameters parameters);

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
    default ValidationOutcome validateCode(CodeSystem codeSystem, Coding coding) {
        return validateCode(codeSystem, coding, ValidationParameters.EMPTY);
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
    ValidationOutcome validateCode(CodeSystem codeSystem, CodeableConcept codeableConcept, ValidationParameters parameters);

    /**
     * Validate a codeable concept using the provided code system
     *
     * @param codeableConcept
     *     the codeable concept
     * @return
     *     the outcome of validation
     */
    default ValidationOutcome validateCode(CodeSystem codeSystem, CodeableConcept codeableConcept) {
        return validateCode(codeSystem, codeableConcept, ValidationParameters.EMPTY);
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
    ValidationOutcome validateCode(ValueSet valueSet, Code code, ValidationParameters parameters);

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
    default ValidationOutcome validateCode(ValueSet valueSet, Code code) {
        return validateCode(valueSet, code, ValidationParameters.EMPTY);
    }

    /**
     * Validate a code, system, version, and display using the provided value set and validation parameters
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
     * @throws
     *     IllegalStateException if a coding cannot be constructed from the passed arguments
     */
    default ValidationOutcome validateCode(ValueSet valueSet, Uri system, String version, Code code, String display, ValidationParameters parameters) {
        Coding coding = Coding.builder()
                .system(system)
                .version(version)
                .code(code)
                .display(display)
                .build();
        return validateCode(valueSet, coding, parameters);
    }

    /**
     * Validate a code, system, version, and display using the provided value set
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
    default ValidationOutcome validateCode(ValueSet valueSet, Uri system, String version, Code code, String display) {
        return validateCode(valueSet, system, version, code, display, ValidationParameters.EMPTY);
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
    ValidationOutcome validateCode(ValueSet valueSet, Coding coding, ValidationParameters parameters);

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
    default ValidationOutcome validateCode(ValueSet valueSet, Coding coding) {
        return validateCode(valueSet, coding, ValidationParameters.EMPTY);
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
    ValidationOutcome validateCode(ValueSet valueSet, CodeableConcept codeableConcept, ValidationParameters parameters);

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
    default ValidationOutcome validateCode(ValueSet valueSet, CodeableConcept codeableConcept) {
        return validateCode(valueSet, codeableConcept, ValidationParameters.EMPTY);
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
    default TranslationOutcome translate(ConceptMap conceptMap, Uri system, String version, Code code, TranslationParameters parameters) {
        Coding coding = Coding.builder()
                .system(system)
                .version(version)
                .code(code)
                .build();
        return translate(conceptMap, coding, parameters);
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
    default TranslationOutcome translate(ConceptMap conceptMap, Uri system, String version, Code code) {
        return translate(conceptMap, system, version, code, TranslationParameters.EMPTY);
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
    TranslationOutcome translate(ConceptMap conceptMap, Coding coding, TranslationParameters parameters);

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
    default TranslationOutcome translate(ConceptMap conceptMap, Coding coding) {
        return translate(conceptMap, coding, TranslationParameters.EMPTY);
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
    TranslationOutcome translate(ConceptMap conceptMap, CodeableConcept codeableConcept, TranslationParameters parameters);

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
    default TranslationOutcome translate(ConceptMap conceptMap, CodeableConcept codeableConcept) {
        return translate(conceptMap, codeableConcept, TranslationParameters.EMPTY);
    }
}