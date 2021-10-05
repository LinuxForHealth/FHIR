/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import static com.ibm.fhir.cache.CacheKey.key;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_BOOLEAN;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_INTEGER;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ibm.fhir.cache.CacheKey;
import com.ibm.fhir.cache.CacheManager;
import com.ibm.fhir.cache.CacheManager.Configuration;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.PropertyType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.config.FHIRTermConfig;
import com.ibm.fhir.term.exception.FHIRTermException;
import com.ibm.fhir.term.service.FHIRTermService;

/**
 * A utility class for working with FHIR code systems
 */
public final class CodeSystemSupport {
    private static final Logger LOG = Logger.getLogger(CodeSystemSupport.class.getName());
    public static final java.lang.String ANCESTORS_AND_SELF_CACHE_NAME = "com.ibm.fhir.term.util.CodeSystemSupport.ancestorsAndSelfCache";
    public static final java.lang.String DESCENDANTS_AND_SELF_CACHE_NAME = "com.ibm.fhir.term.util.CodeSystemSupport.descendantsAndSelfCache";
    public static final Configuration ANCESTORS_AND_SELF_CACHE_CONFIG = Configuration.of(128);
    public static final Configuration DESCENDANTS_AND_SELF_CACHE_CONFIG = Configuration.of(128);

    /**
     * A function that maps a code system concept to its code value
     */
    public static final Function<Concept, java.lang.String> CODE_VALUE_FUNCTION = new Function<Concept, java.lang.String>() {
        @Override
        public java.lang.String apply(Concept concept) {
            return concept.getCode().getValue();
        }
    };

    /**
     * A function that maps a code system concept to its normalized code value
     */
    public static final Function<Concept, java.lang.String> NORMALIZED_CODE_VALUE_FUNCTION = new Function<Concept, java.lang.String>() {
        @Override
        public java.lang.String apply(Concept concept) {
            return normalize(concept.getCode().getValue());
        }
    };

    /**
     * A function that maps a code system concept to its display value
     */
    public static final Function<Concept, java.lang.String> DISPLAY_VALUE_FUNCTION = new Function<Concept, java.lang.String>() {
        @Override
        public java.lang.String apply(Concept concept) {
            return concept.getDisplay().getValue();
        }
    };

    /**
     * A function that maps a code system concept to its normalized display value
     */
    public static final Function<Concept, java.lang.String> NORMALIZED_DISPLAY_VALUE_FUNCTION = new Function<Concept, java.lang.String>() {
        @Override
        public java.lang.String apply(Concept concept) {
            return normalize(concept.getCode().getValue());
        }
    };

    /**
     * A function that maps a code system concept to a code system concept with child concepts removed
     */
    public static final Function<Concept, Concept> CONCEPT_NO_CHILDREN_FUNCTION = new Function<Concept, Concept>() {
        @Override
        public Concept apply(Concept concept) {
            return concept.toBuilder()
                .concept(Collections.emptyList())
                .build();
        }
    };

    /**
     * A function that maps a code system concept to a code system concept with only a code and display value
     */
    public static final Function<Concept, Concept> SIMPLE_CONCEPT_FUNCTION = new Function<Concept, Concept>() {
        @Override
        public Concept apply(Concept concept) {
            return Concept.builder()
                .code(concept.getCode())
                .display(concept.getDisplay())
                .build();
        }
    };

    private static final Pattern IN_COMBINING_DIACRITICAL_MARKS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

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
        if (concept.getCode().equals(code) || (!isCaseSensitive(codeSystem) && normalize(concept.getCode().getValue()).equals(normalize(code.getValue())))) {
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

    /**
     * Get all of the concepts, from the provided code system, that subsume the concept with the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the code
     * @return
     *     a set containing the code value for all concepts that subsume the concept with the specified code
     */
    public static Set<java.lang.String> getAncestorsAndSelf(CodeSystem codeSystem, Code code) {
        if (FHIRTermConfig.isCachingDisabled()) {
            return computeAncestorsAndSelf(codeSystem, code);
        }
        CacheKey key = key(codeSystem, code);
        Map<CacheKey, Set<java.lang.String>> cacheAsMap = CacheManager.getCacheAsMap(ANCESTORS_AND_SELF_CACHE_NAME, ANCESTORS_AND_SELF_CACHE_CONFIG);
        CacheManager.reportCacheStats(LOG, ANCESTORS_AND_SELF_CACHE_NAME);
        try {
            return cacheAsMap.computeIfAbsent(key, k -> computeAncestorsAndSelf(codeSystem, code));
        } finally {
            CacheManager.reportCacheStats(LOG, ANCESTORS_AND_SELF_CACHE_NAME);
        }
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
     * Get the appropriate code value function for the given code system based on its case sensitivity.
     *
     * @param codeSystem
     *     the code system
     * @return
     *     the appropriate code value function for the given code system based on its case sensitivity
     */
    public static Function<Concept, java.lang.String> getCodeValueFunction(CodeSystem codeSystem) {
        return isCaseSensitive(codeSystem) ? CODE_VALUE_FUNCTION : NORMALIZED_CODE_VALUE_FUNCTION;
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
     *     the code system containing the set of Concept instances to be flattened
     * @return
     *     flattened set of Concept instances for the given code system
     */
    public static Set<Concept> getConcepts(CodeSystem codeSystem) {
        return getConcepts(codeSystem, Function.identity());
    }

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
    public static <R> Set<R> getConcepts(CodeSystem codeSystem, Function<Concept, ? extends R> function) {
        Set<R> result = (codeSystem.getCount() != null) ? new LinkedHashSet<>(codeSystem.getCount().getValue()) : new LinkedHashSet<>();
        for (Concept concept : codeSystem.getConcept()) {
            result.addAll(getConcepts(concept, function));
        }
        return result;
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
    public static Set<Concept> getConcepts(CodeSystem codeSystem, List<Include.Filter> filters) {
        return getConcepts(codeSystem, filters, Function.identity());
    }

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
    public static <R> Set<R> getConcepts(CodeSystem codeSystem, List<Include.Filter> filters, Function<Concept, ? extends R> function) {
        Set<R> result = new LinkedHashSet<>();
        List<ConceptFilter> conceptFilters = buildConceptFilters(codeSystem, filters);
        for (Concept concept : getConcepts(codeSystem)) {
            if (accept(conceptFilters, concept)) {
                result.add(function.apply(concept));
            }
        }
        return result;
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
        return getConcepts(concept, Function.identity());
    }

    /**
     * Get a set containing {@link R} instances mapped from concepts where all structural
     * hierarchies have been flattened.
     *
     * @param <R>
     *     the element type of the result set
     * @param concept
     *     the root of the tree containing the Concept instances to be flattened
     * @param function
     *     the function to apply to each element of the result set
     * @return
     *     flattened set of {@link R} instances mapped from concepts for the given tree
     */
    public static <R> Set<R> getConcepts(Concept concept, Function<Concept, ? extends R> function) {
        if (concept == null) {
            return Collections.emptySet();
        }
        Set<R> result = new LinkedHashSet<>();
        result.add(function.apply(concept));
        for (Concept c : concept.getConcept()) {
            result.addAll(getConcepts(c, function));
        }
        return result;
    }

    /**
     * Get all of the concepts, from the provided code system, that are subsumed by the concept with the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the code
     * @return
     *     a set containing the code value for all concepts that are subsumed by the concept with the specified code
     */
    public static Set<java.lang.String> getDescendantsAndSelf(CodeSystem codeSystem, Code code) {
        if (FHIRTermConfig.isCachingDisabled()) {
            return computeDescendantsAndSelf(codeSystem, code);
        }
        CacheKey key = key(codeSystem, code);
        Map<CacheKey, Set<java.lang.String>> cacheAsMap = CacheManager.getCacheAsMap(DESCENDANTS_AND_SELF_CACHE_NAME, DESCENDANTS_AND_SELF_CACHE_CONFIG);
        CacheManager.reportCacheStats(LOG, DESCENDANTS_AND_SELF_CACHE_NAME);
        try {
            return cacheAsMap.computeIfAbsent(key, k -> computeDescendantsAndSelf(codeSystem, code));
        } finally {
            CacheManager.reportCacheStats(LOG, DESCENDANTS_AND_SELF_CACHE_NAME);
        }
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
        return isCaseSensitive(getCodeSystem(url));
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
        switch (type.getValueAsEnum()) {
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
        switch (type.getValueAsEnum()) {
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

    /**
     * Convert the {@link DateTime} value to a Long value.
     *
     * @param dateTime
     *     the dateTime value
     * @return
     *     the Long equivalent value (milliseconds from the epoch)
     */
    public static Long toLong(DateTime dateTime) {
        TemporalAccessor value = dateTime.getValue();
        if (value instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) value;
            return zonedDateTime.toInstant().toEpochMilli();
        }
        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            return localDate.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        }
        if (value instanceof YearMonth) {
            YearMonth yearMonth = (YearMonth) value;
            return yearMonth.atDay(1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        }
        if (value instanceof Year) {
            Year year = (Year) value;
            return year.atMonth(1).atDay(1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        }
        throw new IllegalArgumentException();
    }

    /**
     * Convert the given element value to an object value.
     *
     * @param value
     *     the element value
     * @return
     *     an object value that is compatible with the graph schema
     */
    public static Object toObject(Element value) {
        if (value.is(FHIR_BOOLEAN)) {
            return value.as(FHIR_BOOLEAN).getValue();
        }
        if (value.is(Code.class)) {
            return value.as(Code.class).getValue();
        }
        if (value.is(DateTime.class)) {
            return DateTime.PARSER_FORMATTER.format(value.as(DateTime.class).getValue());
        }
        if (value.is(Decimal.class)) {
            return value.as(Decimal.class).getValue().doubleValue();
        }
        if (value.is(FHIR_INTEGER)) {
            return value.as(FHIR_INTEGER).getValue();
        }
        if (value.is(FHIR_STRING)) {
            return value.as(FHIR_STRING).getValue();
        }
        throw new IllegalArgumentException();
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
            switch (filter.getOp().getValueAsEnum()) {
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
            conceptFilters.add(conceptFilter);
        }
        return conceptFilters;
    }

    private static FHIRTermException conceptFilterNotCreated(Class<? extends ConceptFilter> conceptFilterType, Filter filter) {
        java.lang.String message = java.lang.String.format("%s not created (property: %s, op: %s, value: %s)",
            conceptFilterType.getSimpleName(),
            filter.getProperty().getValue(),
            filter.getOp().getValue(),
            filter.getValue().getValue());
        throw new FHIRTermException(message, Collections.singletonList(Issue.builder()
            .severity(IssueSeverity.ERROR)
            .code(IssueType.NOT_SUPPORTED)
            .details(CodeableConcept.builder()
                .text(string(message))
                .build())
            .build()));
    }

    private static Code code(String value) {
        return Code.of(value.getValue());
    }

    private static Set<java.lang.String> computeAncestorsAndSelf(CodeSystem codeSystem, Code code) {
        return FHIRTermService.getInstance().getConcepts(codeSystem, Collections.singletonList(Filter.builder()
            .property(Code.of("concept"))
            .op(FilterOperator.GENERALIZES)
            .value(code)
            .build()), getCodeValueFunction(codeSystem));
    }

    private static Set<java.lang.String> computeDescendantsAndSelf(CodeSystem codeSystem, Code code) {
        return FHIRTermService.getInstance().getConcepts(codeSystem, Collections.singletonList(Filter.builder()
            .property(Code.of("concept"))
            .op(FilterOperator.IS_A)
            .value(code)
            .build()), getCodeValueFunction(codeSystem));
    }

    private static ConceptFilter createDescendentOfFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new DescendentOfFilter(codeSystem, concept);
            }
        }
        throw conceptFilterNotCreated(DescendentOfFilter.class, filter);
    }

    private static ConceptFilter createEqualsFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        if ((("parent".equals(property.getValue()) || "child".equals(property.getValue())) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) ||
                (hasCodeSystemProperty(codeSystem, property) && !PropertyType.CODING.equals(getCodeSystemPropertyType(codeSystem, property)))) {
            return new EqualsFilter(codeSystem, property, filter.getValue());
        }
        throw conceptFilterNotCreated(EqualsFilter.class, filter);
    }

    private static ConceptFilter createExistsFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        String value = filter.getValue();
        if (hasCodeSystemProperty(codeSystem, property) && convertsToBoolean(value)) {
            return new ExistsFilter(property, toBoolean(value));
        }
        throw conceptFilterNotCreated(ExistsFilter.class, filter);
    }

    private static ConceptFilter createGeneralizesFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) &&
                (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                        codeSystem.getHierarchyMeaning() == null)) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new GeneralizesFilter(codeSystem, concept);
            }
        }
        throw conceptFilterNotCreated(GeneralizesFilter.class, filter);
    }

    private static ConceptFilter createInFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
            if ("concept".equals(property.getValue())) {
                return isCaseSensitive(codeSystem) ?
                    new InFilter(codeSystem, property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                        .collect(Collectors.toSet())) :
                    new InFilter(codeSystem, property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                        .map(CodeSystemSupport::normalize)
                        .collect(Collectors.toSet()));
            } else {
                PropertyType type = getCodeSystemPropertyType(codeSystem, property);
                return new InFilter(codeSystem, property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                    .map(s -> toElement(s, type))
                    .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                    .collect(Collectors.toSet()));
            }
        }
        throw conceptFilterNotCreated(InFilter.class, filter);
    }

    private static ConceptFilter createIsAFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) &&
                (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                        codeSystem.getHierarchyMeaning() == null)) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsAFilter(codeSystem, concept);
            }
        }
        throw conceptFilterNotCreated(IsAFilter.class, filter);
    }

    private static ConceptFilter createIsNotAFilter(CodeSystem codeSystem, Include.Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) &&
                (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                        codeSystem.getHierarchyMeaning() == null)) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsNotAFilter(codeSystem, concept);
            }
        }
        throw conceptFilterNotCreated(IsNotAFilter.class, filter);
    }

    private static ConceptFilter createNotInFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
            if ("concept".equals(property.getValue())) {
                return isCaseSensitive(codeSystem) ?
                    new NotInFilter(codeSystem, property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                        .collect(Collectors.toSet())) :
                    new NotInFilter(codeSystem, property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                        .map(CodeSystemSupport::normalize)
                        .collect(Collectors.toSet()));
            } else {
                PropertyType type = getCodeSystemPropertyType(codeSystem, property);
                return new NotInFilter(codeSystem, property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                    .map(s -> toElement(s, type))
                    .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                    .collect(Collectors.toSet()));
            }
        }
        throw conceptFilterNotCreated(NotInFilter.class, filter);
    }

    private static ConceptFilter createRegexFilter(CodeSystem codeSystem, Include.Filter filter) {
        Code property = filter.getProperty();
        PropertyType type = getCodeSystemPropertyType(codeSystem, property);
        if (hasCodeSystemProperty(codeSystem, property) && (PropertyType.CODE.equals(type) || PropertyType.STRING.equals(type))) {
            return new RegexFilter(property, filter.getValue());
        }
        throw conceptFilterNotCreated(RegexFilter.class, filter);
    }

    private static java.lang.String toCodeValue(CodeSystem codeSystem, Concept concept) {
        return getCodeValueFunction(codeSystem).apply(concept);
    }

    private interface ConceptFilter {
        boolean accept(Concept concept);
    }

    private static class DescendentOfFilter implements ConceptFilter {
        private final CodeSystem codeSystem;
        private final Set<java.lang.String> descendants;

        public DescendentOfFilter(CodeSystem codeSystem, Concept concept) {
            this.codeSystem = codeSystem;
            descendants = getConcepts(concept, getCodeValueFunction(codeSystem));
            descendants.remove(toCodeValue(codeSystem, concept));
        }

        @Override
        public boolean accept(Concept concept) {
            return descendants.contains(toCodeValue(codeSystem, concept));
        }
    }

    private static class EqualsFilter implements ConceptFilter {
        private final CodeSystem codeSystem;
        private final PropertyType type;
        private final Code property;
        private final Object value;
        private final Set<java.lang.String> children;
        private final java.lang.String child;

        public EqualsFilter(CodeSystem codeSystem, Code property, String value) {
            this.codeSystem = codeSystem;
            type = getCodeSystemPropertyType(codeSystem, property);
            this.property = property;

            if (hasCodeSystemProperty(codeSystem, property)) {
                Element e = toElement(value.getValue(), type);
                this.value = e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e);
            } else {
                this.value = null;
            }

            children = new LinkedHashSet<>();
            if ("parent".equals(property.getValue())) {
                Concept parent = findConcept(codeSystem, code(value));
                if (parent != null) {
                    children.addAll(parent.getConcept().stream()
                        .map(getCodeValueFunction(codeSystem))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
                }
            }

            this.child = "child".equals(property.getValue()) ?
                toCodeValue(codeSystem, findConcept(codeSystem, code(value))) :
                null;
        }

        @Override
        public boolean accept(Concept concept) {
            if ("parent".equals(property.getValue())) {
                return children.contains(toCodeValue(codeSystem, concept));
            }

            if ("child".equals(property.getValue())) {
                return concept.getConcept().stream()
                    .map(getCodeValueFunction(codeSystem))
                    .collect(Collectors.toCollection(LinkedHashSet::new))
                    .contains(child);
            }

            if (hasConceptProperty(concept, property)) {
                Element value = getConceptPropertyValue(concept, property);
                return this.value.equals(value.is(DateTime.class) ?
                    toLong(value.as(DateTime.class)) :
                    toObject(value));
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
        private final CodeSystem codeSystem;
        private final java.lang.String concept;

        public GeneralizesFilter(CodeSystem codeSystem, Concept concept) {
            this.codeSystem = codeSystem;
            this.concept = toCodeValue(codeSystem, concept);
        }

        @Override
        public boolean accept(Concept concept) {
            return getConcepts(concept, getCodeValueFunction(codeSystem)).contains(this.concept);
        }
    }

    private static class InFilter implements ConceptFilter {
        protected final CodeSystem codeSystem;
        protected final Code property;
        protected final Set<Object> set;

        public InFilter(CodeSystem codeSystem, Code property, Set<Object> set) {
            this.codeSystem = codeSystem;
            this.property = property;
            this.set = set;
        }

        @Override
        public boolean accept(Concept concept) {
            if ("concept".equals(property.getValue())) {
                return set.contains(toCodeValue(codeSystem, concept));
            }

            if (hasConceptProperty(concept, property)) {
                Element value = getConceptPropertyValue(concept, property);
                return set.contains(value.is(DateTime.class) ?
                    toLong(value.as(DateTime.class)) :
                    toObject(value));
            }

            return false;
        }
    }

    private static class IsAFilter implements ConceptFilter {
        private final CodeSystem codeSystem;
        private final Set<java.lang.String> descendantsAndSelf;

        public IsAFilter(CodeSystem codeSystem, Concept concept) {
            this.codeSystem = codeSystem;
            descendantsAndSelf = getConcepts(concept, getCodeValueFunction(codeSystem));
        }

        @Override
        public boolean accept(Concept concept) {
            return descendantsAndSelf.contains(toCodeValue(codeSystem, concept));
        }
    }

    private static class IsNotAFilter extends IsAFilter {
        public IsNotAFilter(CodeSystem codeSystem, Concept concept) {
            super(codeSystem, concept);
        }

        @Override
        public boolean accept(Concept concept) {
            return !super.accept(concept);
        }
    }

    private static class NotInFilter extends InFilter {
        public NotInFilter(CodeSystem codeSystem, Code property, Set<Object> set) {
            super(codeSystem, property, set);
        }

        @Override
        public boolean accept(Concept concept) {
            if ("concept".equals(property.getValue())) {
                return !set.contains(toCodeValue(codeSystem, concept));
            }

            if (hasConceptProperty(concept, property)) {
                Element value = getConceptPropertyValue(concept, property);
                return !set.contains(value.is(DateTime.class) ?
                    toLong(value.as(DateTime.class)) :
                    toObject(value));
            }

            return false;
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
