/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * A utility class for FHIR code systems
 */
public final class CodeSystemSupport {
    private CodeSystemSupport() { }

    /**
     * Find the concept in the provided code system that matches the specified code.
     * 
     * @param codeSystem
     *     the code system to search
     * @param code
     *     the code to match
     * @return
     *     the code system concept that matches the specified code, or null if no such concept exists
     */
    public static Concept findConcept(CodeSystem codeSystem, Code code) {
        Concept result = null;
        for (Concept concept : codeSystem.getConcept()) {
            result = findConcept(concept, code);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    /**
     * Find the concept in tree rooted by the provided concept that matches the specified code.
     * 
     * @param concept
     *    the root of the tree to search
     * @param code
     *    the code to match
     * @return
     *    the code system concept that matches the specified code, or null if not such concept exists
     */
    public static Concept findConcept(Concept concept, Code code) {
        if (code.equals(concept.getCode())) {
            return concept;
        }
        Concept result = null;
        for (Concept child : concept.getConcept()) {
            result = findConcept(child, code);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    /**
     * Get the code system associated with the given url from the FHIR registry.
     * 
     * @param url
     *    the url of the code system
     * @return
     *    the code system associated with the given input parameter, or null if no such code system exists
     */
    public static CodeSystem getCodeSystem(java.lang.String url) {
        return FHIRRegistry.getInstance().getResource(url, CodeSystem.class);
    }

    /**
     * Get the code system property that matches the specified code.
     * 
     * @param codeSystem
     *     the code system
     * @param code
     *     the property code to match
     * @return
     *     the code system property that matches the specified code, or null if no such property exists
     */
    public static CodeSystem.Property getCodeSystemProperty(CodeSystem codeSystem, Code code) {
        for (CodeSystem.Property property : codeSystem.getProperty()) {
            if (code.equals(property.getCode())) {
                return property;
            }
        }
        return null;
    }

    /**
     * Get the concept property that matches the specified code.
     * 
     * @param concept
     *     the concept
     * @param code
     *     the property code to match
     * @return
     *     the concept property that matches the specified code, or null if no such property exists
     */
    public static Concept.Property getConceptProperty(Concept concept, Code code) {
        for (Concept.Property property : concept.getProperty()) {
            if (code.equals(property.getCode())) {
                return property;
            }
        }
        return null;
    }

    /**
     * Get the value of the concept property that matches the specified code.
     * 
     * @param concept
     *     the concept
     * @param code
     *     the property code to match
     * @return
     *     the value of the concept property that matches the specified code, or null if no such property exists
     */
    public static Element getConceptPropertyValue(Concept concept, Code code) {
        Concept.Property property = getConceptProperty(concept, code);
        return (property != null) ? property.getValue() : null;
    }

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened.
     * 
     * @param codeSystem
     *     the code system containing the list of of Concept instances to be flattened
     * @return
     *     flattened list of Concept instances for the given code system
     */
    public static Set<Concept> getConcepts(CodeSystem codeSystem) {
        Set<Concept> concepts = (codeSystem.getCount() != null) ? new LinkedHashSet<>(codeSystem.getCount().getValue()) : new LinkedHashSet<>();
        for (Concept concept : codeSystem.getConcept()) {
            concepts.addAll(getConcepts(concept));
        }
        return concepts;
    }

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened.
     * 
     * @param concept
     *     the root of the tree containing the Concept instances to be flattened
     * @return
     *     flattened set of Concept instances for the given tree
     */
    public static Set<Concept> getConcepts(Concept concept) {
        if (concept == null) {
            return Collections.emptySet();
        }
        Set<Concept> concepts = new LinkedHashSet<>();
        concepts.add(concept);
        for (Concept c : concept.getConcept()) {
            concepts.addAll(getConcepts(c));
        }
        return concepts;
    }

    /**
     * Determine whether a code system property with the specified code exists in the
     * provided code system.
     * 
     * @param codeSystem
     *     the code system
     * @param code
     *     the property code
     * @return
     *     true if the code system property exists, false otherwise
     */
    public static boolean hasCodeSystemProperty(CodeSystem codeSystem, Code code) {
        return getCodeSystemProperty(codeSystem, code) != null;
    }

    /**
     * Determine whether a concept property with the specified code exists on the
     * provided concept.
     * 
     * @param concept
     *     the concept
     * @param code
     *     the property code
     * @return
     *     true if the concept property exists, false otherwise
     */
    public static boolean hasConceptProperty(Concept concept, Code code) {
        return getConceptProperty(concept, code) != null;
    }
}