/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
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
     * Expand the given value set per the algorithm here: http://hl7.org/fhir/valueset.html#expansion
     *
     * @param valueSet
     *     the value set to be expanded
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    ValueSet expand(ValueSet valueSet);

    /**
     * Lookup the code system concept for the given coding
     *
     * @param coding
     *     the coding to lookup
     * @return
     *     the code system concept that matches the given coding, or null if no such concept exists
     */
    Concept lookup(Coding coding);

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
     * Indicates whether a code system concept matches the given coding
     *
     * @param coding
     *     the coding
     * @return
     *     true if a code system concept matches the given coding, false otherwise
     */
    boolean validateCode(Coding coding);

    /**
     * Indicates whether the given code is a member of the provided value set
     *
     * @apiNote
     *     the implementation should expand the provided value set if needed
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
    boolean validateCode(ValueSet valueSet, String system, String version, String code);

    /**
     * Indicates whether the given coding is a member of the provided value set
     *
     * @apiNote
     *     the implementation should expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param coding
     *     the coding
     * @return
     *     true if the given coding is a member of the provided value set, false otherwise
     */
    boolean validateCode(ValueSet valueSet, Coding coding);

    /**
     * Indicates whether the given codeable concept contains a coding that is a member of the provided value set
     *
     * @apiNote
     *     the implementation should expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param codeableConcept
     *     the codeable concept
     * @return
     *     true if the given codeable concept contains a coding that is a member of the provided value set, false otherwise
     */
    boolean validateCode(ValueSet valueSet, CodeableConcept codeableConcept);

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
    TranslationOutcome translate(ConceptMap conceptMap, Coding coding);
}
