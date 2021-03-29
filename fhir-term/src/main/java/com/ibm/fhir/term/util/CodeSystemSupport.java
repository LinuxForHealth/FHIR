/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;
import static com.ibm.fhir.model.type.String.string;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.model.type.code.PropertyType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.service.FHIRTermService;

/**
 * A utility class for FHIR code systems
 */
public final class CodeSystemSupport {
    private static final Logger log = Logger.getLogger(CodeSystemSupport.class.getName());

    private static final Map<java.lang.String, java.lang.Boolean> CASE_SENSITIVITY_CACHE = createLRUCache(2048);
    private static final Pattern IN_COMBINING_DIACRITICAL_MARKS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private static final Map<java.lang.String, Set<java.lang.String>> ANCESTORS_AND_SELF_CACHE = createLRUCache(128);
    private static final Map<java.lang.String, Set<java.lang.String>> DESCENDANTS_AND_SELF_CACHE = createLRUCache(128);

    private CodeSystemSupport() { }

    /**
     * Determine if the given FHIR string value can be converted to a FHIR Boolean value.
     *
     * @param value
     *     the FHIR string value
     * @return
     *     true if the given FHIR string value can be converted to a FHIR Boolean value, false otherwise
     */
    public static boolean convertsToBoolean(String value) {
        return "true".equals(value.getValue()) || "false".equals(value.getValue());
    }

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
            result = findConcept(codeSystem, concept, code);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    /**
     * Find the concept in tree rooted by the provided concept that matches the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param concept
     *     the root of the tree to search
     * @param code
     *     the code to match
     * @return
     *     the code system concept that matches the specified code, or null if not such concept exists
     */
    public static Concept findConcept(CodeSystem codeSystem, Concept concept, Code code) {
        if (concept.getCode().equals(code) || (!isCaseSensitive(codeSystem)) && normalize(concept.getCode().getValue()).equals(normalize(code.getValue()))) {
            return concept;
        }
        Concept result = null;
        for (Concept child : concept.getConcept()) {
            result = findConcept(codeSystem, child, code);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public static Set<java.lang.String> getAncestorsAndSelf(CodeSystem codeSystem, Code code) {
        return ANCESTORS_AND_SELF_CACHE.computeIfAbsent(code.getValue(), k -> computeAncestorsAndSelf(codeSystem, code));
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
     * Get the code system filter with the given property code and filter operator.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the property code
     * @param operator
     *     the filter operator
     * @return
     *     the code system filter with the given property code and filter operator, or null if no such filter exists
     */
    public static CodeSystem.Filter getCodeSystemFilter(CodeSystem codeSystem, Code code, FilterOperator operator) {
        for (CodeSystem.Filter filter : codeSystem.getFilter()) {
            if (filter.getCode().equals(code) && filter.getOperator().contains(operator)) {
                return filter;
            }
        }
        return null;
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
            if (property.getCode().equals(code)) {
                return property;
            }
        }
        return null;
    }

    /**
     * Get the type of the code system property that matches the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the property code to match
     * @return
     *     the type of the code system property that matches the specified code, or null if no such property exists
     */
    public static PropertyType getCodeSystemPropertyType(CodeSystem codeSystem, Code code) {
        CodeSystem.Property property = getCodeSystemProperty(codeSystem, code);
        if (property != null) {
            return property.getType();
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
            if (property.getCode().equals(code)) {
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
     * hierarchies have been flattened and filtered by the given set of value set include filters.
     *
     * @param codeSystem
     *     the code system
     * @param filters
     *     the value set include filters
     * @return
     *     flattened / filtered list of Concept instances for the given code system
     */
    public static Set<Concept> getConcepts(CodeSystem codeSystem, List<Include.Filter> filters) {
        Set<Concept> concepts = new LinkedHashSet<>();
        List<ConceptFilter> conceptFilters = buildConceptFilters(codeSystem, filters);
        for (Concept concept : getConcepts(codeSystem)) {
            if (accept(conceptFilters, concept)) {
                concepts.add(concept);
            }
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

    public static Set<java.lang.String> getDescendantsAndSelf(CodeSystem codeSystem, Code code) {
        return DESCENDANTS_AND_SELF_CACHE.computeIfAbsent(code.getValue(), k -> computeDescendantsAndSelf(codeSystem, code));
    }

    /**
     * Determine whether a code system filter with the specified property code and filter operator exists
     * in the provided code system.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the property code
     * @param operator
     *     the filter operator
     * @return
     *     true if the code system filter exists, false otherwise
     */
    public static boolean hasCodeSystemFilter(CodeSystem codeSystem, Code code, FilterOperator operator) {
        return getCodeSystemFilter(codeSystem, code, operator) != null;
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

    /**
     * Indicates whether the code system is case sensitive
     *
     * @param codeSystem
     *     the code system
     * @return
     *     true if the code system is case sensitive, false otherwise
     */
    public static boolean isCaseSensitive(CodeSystem codeSystem) {
        if (codeSystem != null && codeSystem.getCaseSensitive() != null) {
            return java.lang.Boolean.TRUE.equals(codeSystem.getCaseSensitive().getValue());
        }
        return false;
    }

    /**
     * Indicates whether the code system with the given url is case sensitive
     *
     * @param url
     *     the url
     * @return
     *     true if the code system with the given is case sensitive, false otherwise
     */
    public static boolean isCaseSensitive(java.lang.String url) {
        return CASE_SENSITIVITY_CACHE.computeIfAbsent(url, k -> isCaseSensitive(getCodeSystem(url)));
    }

    /**
     * Normalize the string by making it case and accent insensitive.
     *
     * @param value
     *     the string value to normalized
     * @return
     *     the normalized string value
     */
    public static java.lang.String normalize(java.lang.String value) {
        if (value != null) {
            return IN_COMBINING_DIACRITICAL_MARKS_PATTERN.matcher(Normalizer.normalize(value, Form.NFD)).replaceAll("").toLowerCase();
        }
        return null;
    }

    /**
     * Convert the given FHIR string value to a FHIR boolean value.
     *
     * @param value
     *     the FHIR string value
     * @return
     *     the FHIR boolean value equivalent of the provided FHIR string value
     */
    public static Boolean toBoolean(String value) {
        return "true".equals(value.getValue()) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Convert the given Java string value to an Element based on the provided property type.
     *
     * @param value
     *     the Java string value
     * @param type
     *     the property type
     * @return
     *     the Element value equivalent of the given Java string based on the provided property type,
     *     or null if the type isn't supported
     */
    public static Element toElement(java.lang.String value, PropertyType type) {
        switch (type.getValueAsEnumConstant()) {
        case BOOLEAN:
            return Boolean.of(value);
        case CODE:
            return Code.of(value);
        case DATE_TIME:
            return DateTime.of(value);
        case DECIMAL:
            return Decimal.of(value);
        case INTEGER:
            return Integer.of(value);
        case STRING:
            return string(value);
        default:
            return null;
        }
    }

    /**
     * Convert the given FHIR string value to an Element value based on the provided property type.
     *
     * @param value
     *     the FHIR string value
     * @param type
     *     the property type
     * @return
     *     the Element value equivalent of the given FHIR string based on the provided property type,
     *     or null if the type isn't supported
     */
    public static Element toElement(String value, PropertyType type) {
        switch (type.getValueAsEnumConstant()) {
        case BOOLEAN:
            return Boolean.of(value.getValue());
        case CODE:
            return Code.of(value.getValue());
        case DATE_TIME:
            return DateTime.of(value.getValue());
        case DECIMAL:
            return Decimal.of(value.getValue());
        case INTEGER:
            return Integer.of(value.getValue());
        case STRING:
            return value;
        default:
            return null;
        }
    }

    private static boolean accept(List<ConceptFilter> conceptFilters, Concept concept) {
        for (ConceptFilter conceptFilter : conceptFilters) {
            if (!conceptFilter.accept(concept)) {
                return false;
            }
        }
        return true;
    }

    private static List<ConceptFilter> buildConceptFilters(CodeSystem codeSystem, List<Include.Filter> filters) {
        List<ConceptFilter> conceptFilters = new ArrayList<>(filters.size());
        for (Include.Filter filter : filters) {
            ConceptFilter conceptFilter = null;
            switch (filter.getOp().getValueAsEnumConstant()) {
            case DESCENDENT_OF:
                conceptFilter = createDescendentOfFilter(codeSystem, filter);
                break;
            case EQUALS:
                conceptFilter = createEqualsFilter(codeSystem, filter);
                break;
            case EXISTS:
                conceptFilter = createExistsFilter(codeSystem, filter);
                break;
            case GENERALIZES:
                conceptFilter = createGeneralizesFilter(codeSystem, filter);
                break;
            case IN:
                conceptFilter = createInFilter(codeSystem, filter);
                break;
            case IS_A:
                conceptFilter = createIsAFilter(codeSystem, filter);
                break;
            case IS_NOT_A:
                conceptFilter = createIsNotAFilter(codeSystem, filter);
                break;
            case NOT_IN:
                conceptFilter = createNotInFilter(codeSystem, filter);
                break;
            case REGEX:
                conceptFilter = createRegexFilter(codeSystem, filter);
                break;
            }
            if (conceptFilter != null) {
                conceptFilters.add(conceptFilter);
            } else {
                log.log(Level.WARNING, java.lang.String.format("Unable to create concept filter from property: %s, op: %s, value: %s", filter.getProperty().getValue(), filter.getOp().getValue(), filter.getValue().getValue()));
            }
        }
        return conceptFilters;
    }

    private static Code code(String value) {
        return Code.of(value.getValue());
    }

    private static Set<java.lang.String> computeAncestorsAndSelf(CodeSystem codeSystem, Code code) {
        Set<Concept> concepts = FHIRTermService.getInstance().getConcepts(codeSystem, Collections.singletonList(Filter.builder()
            .property(Code.of("concept"))
            .op(FilterOperator.GENERALIZES)
            .value(code)
            .build()));
        Set<java.lang.String> ancestorsAndSelf = new LinkedHashSet<>(concepts.size());
        for (Concept concept : concepts) {
            ancestorsAndSelf.add(concept.getCode().getValue());
        }
        return ancestorsAndSelf;
    }

    private static Set<java.lang.String> computeDescendantsAndSelf(CodeSystem codeSystem, Code code) {
        Set<Concept> concepts = FHIRTermService.getInstance().getConcepts(codeSystem, Collections.singletonList(Filter.builder()
            .property(Code.of("concept"))
            .op(FilterOperator.IS_A)
            .value(code)
            .build()));
        Set<java.lang.String> descendantsAndSelf = new LinkedHashSet<>(concepts.size());
        for (Concept concept : concepts) {
            descendantsAndSelf.add(concept.getCode().getValue());
        }
        return descendantsAndSelf;
    }

    private static ConceptFilter createDescendentOfFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new DescendentOfFilter(concept);
            }
        }
        return null;
    }

    private static ConceptFilter createEqualsFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        if ((("parent".equals(property.getValue()) || "child".equals(property.getValue())) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) ||
                (hasCodeSystemProperty(codeSystem, property) && !PropertyType.CODING.equals(getCodeSystemPropertyType(codeSystem, property)))) {
            return new EqualsFilter(codeSystem, property, filter.getValue());
        }
        return null;
    }

    private static ConceptFilter createExistsFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        String value = filter.getValue();
        if (hasCodeSystemProperty(codeSystem, property) && convertsToBoolean(value)) {
            return new ExistsFilter(property, toBoolean(value));
        }
        return null;
    }

    private static ConceptFilter createGeneralizesFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new GeneralizesFilter(concept);
            }
        }
        return null;
    }

    private static ConceptFilter createInFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
             return new InFilter(property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                 .map(Code::of)
                 .collect(Collectors.toSet()));
        }
        return null;
    }

    private static ConceptFilter createIsAFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsAFilter(concept);
            }
        }
        return null;
    }

    private static ConceptFilter createIsNotAFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsNotAFilter(concept);
            }
        }
        return null;
    }

    private static ConceptFilter createNotInFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
             return new NotInFilter(property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                 .map(Code::of)
                 .collect(Collectors.toSet()));
        }
        return null;
    }

    private static ConceptFilter createRegexFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        PropertyType type = getCodeSystemPropertyType(codeSystem, property);
        if (hasCodeSystemProperty(codeSystem, property) && (PropertyType.CODE.equals(type) || PropertyType.STRING.equals(type))) {
            return new RegexFilter(property, filter.getValue());
        }
        return null;
    }

    private interface ConceptFilter {
        boolean accept(Concept concept);
    }

    private static class DescendentOfFilter implements ConceptFilter {
        private final Set<Concept> descendants;

        public DescendentOfFilter(Concept concept) {
            Set<Concept> descendants = getConcepts(concept);
            descendants.remove(concept);
            this.descendants = descendants;
        }

        @Override
        public boolean accept(Concept concept) {
            return descendants.contains(concept);
        }
    }

    private static class EqualsFilter implements ConceptFilter {
        private final PropertyType type;
        private final Code property;
        private final String value;
        private final Set<Concept> children;
        private final Concept child;

        public EqualsFilter(CodeSystem codeSystem, Code property, String value) {
            this.type = getCodeSystemPropertyType(codeSystem, property);
            this.property = property;
            this.value = value;
            children = new LinkedHashSet<>();
            if ("parent".equals(property.getValue())) {
                Concept parent = findConcept(codeSystem, code(value));
                if (parent != null) {
                    children.addAll(parent.getConcept());
                }
            }
            this.child = "child".equals(property.getValue()) ? findConcept(codeSystem, code(value)) : null;
        }

        @Override
        public boolean accept(Concept concept) {
            if ("parent".equals(property.getValue())) {
                return children.contains(concept);
            }
            if ("child".equals(property.getValue())) {
                return concept.getConcept().contains(child);
            }
            if (hasConceptProperty(concept, property)) {
                Element value = getConceptPropertyValue(concept, property);
                if (value != null) {
                    return value.equals(toElement(this.value, type));
                }
            }
            return false;
        }
    }

    private static class ExistsFilter implements ConceptFilter {
        private Code property;
        private Boolean value;

        public ExistsFilter(Code property, Boolean value) {
            this.property = property;
            this.value = value;
        }

        @Override
        public boolean accept(Concept concept) {
            return Boolean.TRUE.equals(value) ?
                    hasConceptProperty(concept, property) :
                        !hasConceptProperty(concept, property);
        }
    }

    private static class GeneralizesFilter implements ConceptFilter {
        private final Concept concept;

        public GeneralizesFilter(Concept concept) {
            this.concept = concept;
        }

        @Override
        public boolean accept(Concept concept) {
            return getConcepts(concept).contains(this.concept);
        }
    }

    private static class InFilter implements ConceptFilter {
        protected final Code property;
        protected final Set<Code> set;

        public InFilter(Code property, Set<Code> set) {
            this.property = property;
            this.set = set;
        }

        @Override
        public boolean accept(Concept concept) {
            return "concept".equals(property.getValue()) ?
                    set.contains(concept.getCode()) :
                        set.contains(getConceptPropertyValue(concept, property));
        }
    }

    private static class IsAFilter implements ConceptFilter {
        protected final Set<Concept> descendantsAndSelf;

        public IsAFilter(Concept concept) {
            descendantsAndSelf = getConcepts(concept);
        }

        @Override
        public boolean accept(Concept concept) {
            return descendantsAndSelf.contains(concept);
        }
    }

    private static class IsNotAFilter extends IsAFilter {
        public IsNotAFilter(Concept concept) {
            super(concept);
        }

        @Override
        public boolean accept(Concept concept) {
            return !super.accept(concept);
        }
    }

    private static class NotInFilter extends InFilter {
        public NotInFilter(Code property, Set<Code> set) {
            super(property, set);
        }

        @Override
        public boolean accept(Concept concept) {
            return !super.accept(concept);
        }
    }

    private static class RegexFilter implements ConceptFilter {
        private final Code property;
        private final Pattern pattern;

        public RegexFilter(Code property, String value) {
            this.property = property;
            this.pattern = Pattern.compile(value.getValue());
        }

        @Override
        public boolean accept(Concept concept) {
            if (hasConceptProperty(concept, property)) {
                Element value = getConceptPropertyValue(concept, property);
                if (value.is(String.class)) {
                    return pattern.matcher(value.as(String.class).getValue()).matches();
                }
            }
            return false;
        }
    }
}
