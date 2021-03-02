/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

import java.util.List;
import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Code;

public interface FHIRTermServiceProvider {
    /**
     * Indicates whether the given code system is supported.
     *
     * @param codeSystem
     *     the code system
     * @return
     *     true if the given code system is supported, false otherwise
     */
    boolean isSupported(CodeSystem codeSystem);

    /**
     * Indicates whether the given code system contains a concept with the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the code
     * @return
     *     true if the given code system contains a concept with the specified code, false otherwise
     */
    boolean hasConcept(CodeSystem codeSystem, Code code);

    /**
     * Get the concept in the provided code system with the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the code
     * @return
     *     the code system concept with the specified code, or null if no such concept exists
     */
    Concept getConcept(CodeSystem codeSystem, Code code);

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened.
     *
     * @param codeSystem
     *     the code system
     * @return
     *     flattened list of Concept instances for the given code system
     */
    Set<Concept> getConcepts(CodeSystem codeSystem);

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened and filtered by the given set of value set include filters.
     *
     * @param codeSystem
     *     the code system
     * @param filters
     *     the value set include filters
     * @return
     *     flattened / filtered list of Concept instances for the given code system
     */
    Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters);

    /**
     * Find the concept in tree rooted by the provided concept that matches the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param codeA
     *     the root of the hierarchy to search
     * @param codeB
     *     the code to match
     * @return
     *     the code system concept that matches the specified code, or null if not such concept exists
     */
    boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB);

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the root of the hierarchy containing the Concept instances to be flattened
     * @return
     *     flattened set of Concept instances for the given tree
     */
    Set<Concept> closure(CodeSystem codeSystem, Code code);
}