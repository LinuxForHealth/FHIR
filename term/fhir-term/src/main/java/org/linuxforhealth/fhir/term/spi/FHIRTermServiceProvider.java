/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.spi;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose.Include.Filter;
import org.linuxforhealth.fhir.model.type.Code;

public interface FHIRTermServiceProvider {
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

    /**
     * Get a map of sets containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened
     *
     * @param codeSystem
     *     the code system
     * @param codes
     *     the set of roots of hierarchies containing the Concept instances to be flattened
     * @return
     *     a map containing flattened sets of Concept instances for the given trees
     */
    default Map<Code, Set<Concept>> closure(CodeSystem codeSystem, Set<Code> codes) {
        Map<Code, Set<Concept>> result = new LinkedHashMap<>();
        for (Code code : codes) {
            Set<Concept> closure = closure(codeSystem, code);
            result.put(code, closure);
        }
        return result;
    }

    /**
     * Get the concept in the provided code system with the specified code.
     * Consumers should not expect the returned Concept to contain child concepts, even where
     * such concepts exist in the underlying CodeSystem.
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
     *     the code system containing the set of Concept instances to be flattened
     * @return
     *     flattened set of Concept instances for the given code system
     */
    Set<Concept> getConcepts(CodeSystem codeSystem);

    /**
     * Get a set containing {@link R} instances mapped from concepts where all structural
     * hierarchies have been flattened.
     *
     * @param <R>
     *     the element type of the result set
     * @param codeSystem
     *     the code system containing the set of Concept instances to be flattened
     * @param function
     *     the function to apply to each element of the result set
     * @return
     *     flattened set of {@link R} instances mapped from concepts for the given code system
     */
    default <R> Set<R> getConcepts(CodeSystem codeSystem, Function<Concept, ? extends R> function) {
        return getConcepts(codeSystem).stream()
            .map(function)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened and filtered by the given set of value set include filters.
     *
     * @param codeSystem
     *     the code system containing the set of Concept instances to be flattened / filtered
     * @param filters
     *     the value set include filters
     * @return
     *     flattened / filtered set of Concept instances for the given code system
     */
    Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters);

    /**
     * Get a set containing {@link R} instances mapped from concepts where all structural
     * hierarchies have been flattened and filtered by the given set of value set include filters.
     *
     * @param <R>
     *     the element type of the result set
     * @param codeSystem
     *     the code system containing the set of Concept instances to be flattened / filtered
     * @param filters
     *     the value set include filters
     * @param function
     *     the function to apply to each element of the result set
     * @return
     *     flattened / filtered set of {@link R} instances mapped from concepts for the given code system
     */
    default <R> Set<R> getConcepts(CodeSystem codeSystem, List<Filter> filters, Function<Concept, ? extends R> function) {
        return getConcepts(codeSystem, filters).stream()
            .map(function)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

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
     * Indicates whether the given code system contains a concept for each of the specified codes.
     *
     * @param codeSystem
     *     the code system
     * @param codes
     *     the codes
     * @return
     *     true if the given code system contains a concept for each of the specified codes, false otherwise
     */
    default boolean hasConcepts(CodeSystem codeSystem, Set<Code> codes) {
        for (Code code : codes) {
            if (!hasConcept(codeSystem, code)) {
                return false;
            }
        }
        return true;
    }

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
     * Indicates whether the concept for {@code CodeA} subsumes the concept for {@code codeB}
     * in the passed CodeSystem.
     *
     * @param codeSystem
     *     the code system
     * @param codeA
     *     the root of the hierarchy to search
     * @param codeB
     *     the code to match
     * @return
     *     true if the code system concept for {@code codeB} exists in the tree rooted by the concept for
     *     {@code CodeA}, false otherwise
     */
    boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB);
}
