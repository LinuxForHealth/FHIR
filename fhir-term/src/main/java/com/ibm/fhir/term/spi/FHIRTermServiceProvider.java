/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;

public interface FHIRTermServiceProvider {
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
     *     the code system concept that matches the specified coding, or null if no such concept exists
     */
    Concept lookup(Coding coding);

    /**
     * Perform a subsumption test to determine if the code system concept represented by coding "A" subsumes
     * the code system concept represented by coding "B"
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
     * Generate the transitive closure for the provided coding
     *
     * @param coding
     *     the coding
     * @return
     *     a set containing the transitive closure for the provided coding
     */
    Set<Concept> closure(Coding coding);
}
