/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.term.util.CodeSystemSupport.getCodeSystem;
import static com.ibm.fhir.term.util.CodeSystemSupport.getConceptPropertyValue;
import static com.ibm.fhir.term.util.CodeSystemSupport.hasCodeSystemProperty;
import static com.ibm.fhir.term.util.CodeSystemSupport.hasConceptProperty;
import static com.ibm.fhir.term.util.CodeSystemSupport.isCaseSensitive;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Compose;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.resource.ValueSet.Expansion;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.service.FHIRTermService;

/**
 * A utility class for expanding FHIR value sets
 */
public final class ValueSetSupport {
    private static final Logger log = Logger.getLogger(ValueSetSupport.class.getName());

    private static final java.lang.String VERSION_UNKNOWN = "<version unknown>";
    private static final Map<java.lang.String, Map<java.lang.String, Set<java.lang.String>>> CODE_SET_MAP_CACHE = createLRUCache(1024);

    private ValueSetSupport() { }

    /**
     * Expand the given value set per the algorithm here: http://hl7.org/fhir/valueset.html#expansion
     *
     * @param valueSet
     *     the value set to be expanded
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    public static ValueSet expand(ValueSet valueSet) {
        if (!isExpanded(valueSet) && isExpandable(valueSet)) {
            Set<Contains> result = expand(valueSet.getCompose());
            return valueSet.toBuilder()
                .expansion(Expansion.builder()
                    .total(Integer.of(result.size()))
                    .timestamp(DateTime.now(ZoneOffset.UTC))
                    .contains(unwrap(result))
                    .build())
                .build();
        }
        return valueSet;
    }

    public static boolean isExpanded(ValueSet valueSet) {
        return valueSet != null && valueSet.getExpansion() != null;
    }

    public static boolean isExpandable(ValueSet valueSet) {
        if (valueSet == null || valueSet.getCompose() == null) {
            return false;
        }

        Compose compose = valueSet.getCompose();

        List<Include> includesAndExcludes = new ArrayList<>(compose.getInclude());
        includesAndExcludes.addAll(compose.getExclude());

        for (java.lang.String codeSystemReference : getCodeSystemReferences(includesAndExcludes)) {
            if (!hasResource(codeSystemReference, CodeSystem.class)) {
                return false;
            }
            CodeSystem codeSystem = getCodeSystem(codeSystemReference);
            if (!FHIRTermService.getInstance().isSupported(codeSystem)) {
                return false;
            }
        }

        for (java.lang.String valueSetReference : getValueSetReferences(includesAndExcludes)) {
            if (!hasResource(valueSetReference, ValueSet.class)) {
                return false;
            }
            ValueSet vs = getValueSet(valueSetReference);
            if (!isExpanded(vs) && !isExpandable(vs)) {
                return false;
            }
        }

        return true;
    }

    private static Set<java.lang.String> getCodeSystemReferences(List<Include> includesAndExcludes) {
        return includesAndExcludes.stream()
                .filter(includeOrExclude -> includeOrExclude.getConcept().isEmpty())
                .map(ValueSetSupport::getCodeSystemReference)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static java.lang.String getCodeSystemReference(Include includeOrExclude) {
        if (includeOrExclude.getSystem() != null && includeOrExclude.getSystem().getValue() != null) {
            StringBuilder sb = new StringBuilder(includeOrExclude.getSystem().getValue());
            if (includeOrExclude.getVersion() != null && includeOrExclude.getVersion().getValue() != null) {
                sb.append("|").append(includeOrExclude.getVersion().getValue());
            }
            return sb.toString();
        }
        return null;
    }

    private static Set<java.lang.String> getValueSetReferences(List<Include> includesAndExcludes) {
        return includesAndExcludes.stream()
                .map(includeOrExclude -> includeOrExclude.getValueSet())
                .flatMap(List::stream)
                .map(canonical -> canonical.getValue())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Get a set containing {@link ValueSet.Expansion.Contains} instances where all structural
     * hierarchies have been flattened.
     *
     * @param expansion
     *     the expansion containing the list of Contains instances to flatten
     * @return
     *     flattened set of Contains instances for the given expansion
     */
    public static Set<Expansion.Contains> getContains(Expansion expansion) {
        if (expansion == null) {
            return Collections.emptySet();
        }
        Set<Expansion.Contains> result = (expansion.getTotal() != null) ? new LinkedHashSet<>(expansion.getTotal().getValue()) : new LinkedHashSet<>();
        for (Expansion.Contains contains : expansion.getContains()) {
            result.addAll(getContains(contains));
        }
        return result;
    }

    /**
     * Get the value set associated with the given url from the FHIR registry.
     *
     * @param url
     *     the url of the value set
     * @return
     *     the value set associated with the given input parameter, or null if no such value set exists
     */
    public static ValueSet getValueSet(java.lang.String url) {
        return FHIRRegistry.getInstance().getResource(url, ValueSet.class);
    }

    private static boolean accept(List<ConceptFilter> conceptFilters, Concept concept) {
        for (ConceptFilter conceptFilter : conceptFilters) {
            if (!conceptFilter.accept(concept)) {
                return false;
            }
        }
        return true;
    }

    private static List<ConceptFilter> buildConceptFilters(CodeSystem codeSystem, List<Filter> filters) {
        List<ConceptFilter> conceptFilters = new ArrayList<>(filters.size());
        for (Filter filter : filters) {
            ConceptFilter conceptFilter = null;
            switch (FilterOperator.ValueSet.from(filter.getOp().getValue())) {
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

    private static Contains buildContains(Uri system, String version, Code code, String display) {
        return wrap(Expansion.Contains.builder()
            .system(system)
            .version(version)
            .code(code)
            .display(display)
            .build());
    }

    private static Contains buildContains(Uri system, String version, Concept concept) {
        Code code = (concept.getCode() != null) ? concept.getCode() : null;
        if (code != null) {
            return buildContains(system, version, code, concept.getDisplay());
        }
        return null;
    }

    private static Code code(String value) {
        return Code.of(value.getValue());
    }

    private static Element convert(String value, Class<?> targetType) {
        if (Code.class.equals(targetType)) {
            return Code.of(value.getValue());
        }
        if (Integer.class.equals(targetType)) {
            return Integer.of(value.getValue());
        }
        if (Boolean.class.equals(targetType)) {
            return Boolean.of(value.getValue());
        }
        if (DateTime.class.equals(targetType)) {
            return DateTime.of(value.getValue());
        }
        if (Decimal.class.equals(targetType)) {
            return Decimal.of(value.getValue());
        }
        return value;
    }

    private static boolean convertsToBoolean(String value) {
        return "true".equals(value.getValue()) || "false".equals(value.getValue());
    }

    private static ConceptFilter createDescendentOfFilter(CodeSystem codeSystem, Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = FHIRTermService.getInstance().getConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new DescendentOfFilter(codeSystem, concept);
            }
        }
        return null;
    }

    private static ConceptFilter createEqualsFilter(CodeSystem codeSystem, Filter filter) {
        Code property = filter.getProperty();
        if ("parent".equals(property.getValue()) ||
                "child".equals(property.getValue()) ||
                hasCodeSystemProperty(codeSystem, property)) {
            return new EqualsFilter(codeSystem, property, filter.getValue());
        }
        return null;
    }

    private static ConceptFilter createExistsFilter(CodeSystem codeSystem, Filter filter) {
        Code property = filter.getProperty();
        String value = filter.getValue();
        if (hasCodeSystemProperty(codeSystem, property) && convertsToBoolean(value)) {
            return new ExistsFilter(property, toBoolean(value));
        }
        return null;
    }

    private static ConceptFilter createGeneralizesFilter(CodeSystem codeSystem, Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = FHIRTermService.getInstance().getConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new GeneralizesFilter(codeSystem, concept);
            }
        }
        return null;
    }

    private static ConceptFilter createInFilter(CodeSystem codeSystem, Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
             return new InFilter(property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                 .map(Code::of)
                 .collect(Collectors.toSet()));
        }
        return null;
    }

    private static ConceptFilter createIsAFilter(CodeSystem codeSystem, Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = FHIRTermService.getInstance().getConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsAFilter(codeSystem, concept);
            }
        }
        return null;
    }

    private static ConceptFilter createIsNotAFilter(CodeSystem codeSystem, Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = FHIRTermService.getInstance().getConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsNotAFilter(codeSystem, concept);
            }
        }
        return null;
    }

    private static ConceptFilter createNotInFilter(CodeSystem codeSystem, Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
             return new NotInFilter(property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                 .map(Code::of)
                 .collect(Collectors.toSet()));
        }
        return null;
    }

    private static ConceptFilter createRegexFilter(CodeSystem codeSystem, Filter filter) {
        Code property = filter.getProperty();
        if (hasCodeSystemProperty(codeSystem, property)) {
            return new RegexFilter(property, filter.getValue());
        }
        return null;
    }

    private static Set<Contains> expand(Compose compose) {
        if (compose == null) {
            return Collections.emptySet();
        }

        Set<Contains> result = new LinkedHashSet<>();

        Set<Contains> included = new LinkedHashSet<>();
        for (Include include : compose.getInclude()) {
            included.addAll(expand(include));
        }

        Set<Contains> excluded = new LinkedHashSet<>();
        for (Include exclude : compose.getExclude()) {
            excluded.addAll(expand(exclude));
        }

        Set<Contains> difference = new LinkedHashSet<>(included);
        difference.removeAll(excluded);
        result.addAll(difference);

        return result;
    }

    private static Set<Contains> expand(Include includeOrExclude) {
        if (includeOrExclude == null) {
            return Collections.emptySet();
        }

        Set<Contains> systemContains = new LinkedHashSet<>();
        if (includeOrExclude.getSystem() != null) {
            Uri system = includeOrExclude.getSystem();
            String version = (includeOrExclude.getVersion() != null) ?
                    includeOrExclude.getVersion() : getLatestVersion(system);
            if (!includeOrExclude.getConcept().isEmpty()) {
                for (Include.Concept concept : includeOrExclude.getConcept()) {
                    Code code = (concept.getCode() != null) ? concept.getCode() : null;
                    if (code != null) {
                        systemContains.add(buildContains(system, version, code, concept.getDisplay()));
                    }
                }
            } else {
                java.lang.String url = system.getValue();
                if (version != null) {
                    url = url + "|" + version.getValue();
                }
                if (hasResource(url, CodeSystem.class)) {
                    CodeSystem codeSystem = getCodeSystem(url);
                    List<ConceptFilter> conceptFilters = buildConceptFilters(codeSystem, includeOrExclude.getFilter());
                    for (Concept concept : FHIRTermService.getInstance().getConcepts(codeSystem)) {
                        if (accept(conceptFilters, concept)) {
                            Contains contains = buildContains(system, version, concept);
                            if (contains != null) {
                                systemContains.add(contains);
                            }
                        }
                    }
                }
            }
        }

        Set<Contains> valueSetContains = new LinkedHashSet<>();
        for (Canonical valueSet : includeOrExclude.getValueSet()) {
            java.lang.String url = valueSet.getValue();
            if (hasResource(url, ValueSet.class)) {
                valueSetContains.addAll(wrap(getContains(expand(getValueSet(url)).getExpansion())));
            }
        }

        if (!systemContains.isEmpty() && !valueSetContains.isEmpty()) {
            Set<Contains> intersection = new LinkedHashSet<>(systemContains);
            intersection.retainAll(valueSetContains);
            return intersection;
        }

        return !systemContains.isEmpty() ? systemContains : valueSetContains;
    }

    private static Set<Expansion.Contains> getContains(Expansion.Contains contains) {
        if (contains == null) {
            return Collections.emptySet();
        }
        Set<Expansion.Contains> result = new LinkedHashSet<>();
        result.add(contains);
        for (Expansion.Contains c : contains.getContains()) {
            result.addAll(getContains(c));
        }
        return result;
    }

    private static String getLatestVersion(Uri system) {
        java.lang.String version = FHIRRegistry.getInstance().getLatestVersion(system.getValue(), CodeSystem.class);
        return (version != null) ? string(version) : null;
    }

    private static boolean hasResource(java.lang.String url, Class<? extends Resource> resourceType) {
        return FHIRRegistry.getInstance().hasResource(url, resourceType);
    }

    private static Boolean toBoolean(String value) {
        return "true".equals(value.getValue()) ? Boolean.TRUE : Boolean.FALSE;
    }

    private interface ConceptFilter {
        boolean accept(Concept concept);
    }

    private static class DescendentOfFilter extends IsAFilter {
        public DescendentOfFilter(CodeSystem codeSystem, Concept concept) {
            super(codeSystem, concept);
        }

        @Override
        public boolean accept(Concept concept) {
            return !this.concept.equals(concept) && super.accept(concept);
        }
    }

    private static class EqualsFilter implements ConceptFilter {
        private final Code property;
        private final String value;
        private final Set<Concept> children;
        private final Concept child;

        public EqualsFilter(CodeSystem codeSystem, Code property, String value) {
            this.property = property;
            this.value = value;
            children = new LinkedHashSet<>();
            if ("parent".equals(property.getValue())) {
                Concept parent = FHIRTermService.getInstance().getConcept(codeSystem, code(value));
                if (parent != null) {
                    children.addAll(parent.getConcept());
                }
            }
            this.child = "child".equals(property.getValue()) ? FHIRTermService.getInstance().getConcept(codeSystem, code(value)) : null;
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
                if (value != null && !value.is(CodeableConcept.class)) {
                    return value.equals(convert(this.value, value.getClass()));
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
        private final CodeSystem codeSystem;
        private final Concept concept;

        public GeneralizesFilter(CodeSystem codeSystem, Concept concept) {
            this.codeSystem = codeSystem;
            this.concept = concept;
        }

        @Override
        public boolean accept(Concept concept) {
            return FHIRTermService.getInstance().subsumes(codeSystem, concept.getCode(), this.concept.getCode());
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
        protected final CodeSystem codeSystem;
        protected final Concept concept;

        public IsAFilter(CodeSystem codeSystem, Concept concept) {
            this.codeSystem = codeSystem;
            this.concept = concept;
        }

        @Override
        public boolean accept(Concept concept) {
            return FHIRTermService.getInstance().subsumes(codeSystem, this.concept.getCode(), concept.getCode());
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

    private static Contains wrap(Expansion.Contains contains) {
        return new Contains(contains);
    }

    private static Expansion.Contains unwrap(Contains contains) {
        return contains.getContains();
    }

    private static Set<Contains> wrap(Set<Expansion.Contains> unwrapped) {
        return unwrapped.stream()
            .map(ValueSetSupport::wrap)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static Set<Expansion.Contains> unwrap(Set<Contains> wrapped) {
        return wrapped.stream()
            .map(ValueSetSupport::unwrap)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static class Contains {
        private final Expansion.Contains contains;
        private final int hashCode;

        public Contains(Expansion.Contains contains) {
            this.contains = contains;
            hashCode = Objects.hash(contains.getSystem(), contains.getVersion(), contains.getCode());
        }

        public Expansion.Contains getContains() {
            return contains;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Contains other = (Contains) obj;
            return Objects.equals(contains.getSystem(), other.contains.getSystem()) &&
                    Objects.equals(contains.getVersion(), other.contains.getVersion()) &&
                    Objects.equals(contains.getCode(), other.contains.getCode());
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

    public static boolean validateCode(ValueSet valueSet, Code code) {
        return validateCode(getCodeSetMap(valueSet), code);
    }

    public static boolean validateCode(ValueSet valueSet, Coding coding) {
        return validateCode(getCodeSetMap(valueSet), coding);
    }

    private static boolean validateCode(Map<java.lang.String, Set<java.lang.String>> codeSetMap, Code code) {
        java.lang.String codeString = (code != null) ? code.getValue() : null;
        if (codeString != null) {
            for (Set<java.lang.String> codeSet : codeSetMap.values()) {
                if (codeSet.contains(codeString) || codeSet.contains(codeString.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean validateCode(Map<java.lang.String, Set<java.lang.String>> codeSetMap, Coding coding) {
        java.lang.String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        java.lang.String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
        java.lang.String code = (coding.getCode() != null) ? coding.getCode().getValue() : null;
        return validateCode(codeSetMap, system, version, code);
    }

    /**
     * Determine whether the provided code is in the codeSet associated with the provided system and version.
     *
     * <p>If the system or code is null, return false. If the version is non-null, it is concatenated with the
     * system to form a key into the codeSetMap. If not found, then the system is concatenated with the
     * "VERSION_UNKNOWN" constant (in cases where the expanded value set did not have a version available during the
     * expansion). If only the system is non-null, then the codeSetMap keys are checked for startsWith(system).
     *
     * <p>If the system and version are non-null, then they are concatenated to form a key into the codeSetMap. If
     * not found, then the system is concatenated with the "VERSION_UNKNOWN" constant (in cases where the expanded
     * value set did not have a version available during the expansion). If only the system is non-null, then the
     * codeSetMap keys are checked for startsWith(system). Finally, if both system and version are null, map keys
     * are ignored and the values of the map are checked directly.
     *
     * @param codeSetMap
     *     the code set map
     * @param system
     *     the system of the focal coded element
     * @param version
     *     the version of the focal coded element (can be null)
     * @param code
     *     the code used in the membership check
     * @return
     *     true if a codeSet is found and the provided code is a member of that codeSet, false otherwise
     */
    private static boolean validateCode(Map<java.lang.String, Set<java.lang.String>> codeSetMap, java.lang.String system, java.lang.String version, java.lang.String code) {
        if (system == null || code == null) {
            return false;
        }
        java.lang.String url = (version != null) ? system + "|" + version : system;
        if (!isCaseSensitive(url)) {
            code = code.toLowerCase();
        }
        if (version != null) {
            Set<java.lang.String> codeSet = codeSetMap.get(system + "|" + version);
            if (codeSet != null) {
                if (codeSet.contains(code)) {
                    return true;
                } else {
                    codeSet = codeSetMap.get(system + "|" + VERSION_UNKNOWN);
                    if (codeSet != null) {
                        return codeSet.contains(code);
                    }
                }
            }
        } else {
            java.lang.String prefix = system + "|";
            for (java.lang.String key : codeSetMap.keySet()) {
                if (key.startsWith(prefix)) {
                    return codeSetMap.get(key).contains(code);
                }
            }
        }
        return false;
    }

    private static Map<java.lang.String, Set<java.lang.String>> getCodeSetMap(ValueSet valueSet) {
        if (valueSet.getUrl() == null || valueSet.getVersion() == null) {
            return computeCodeSetMap(valueSet);
        }
        java.lang.String url = valueSet.getUrl().getValue() + "|" + valueSet.getVersion().getValue();
        return CODE_SET_MAP_CACHE.computeIfAbsent(url, k -> computeCodeSetMap(valueSet));
    }

    private static Map<java.lang.String, Set<java.lang.String>> computeCodeSetMap(ValueSet valueSet) {
        try {
            ValueSet expanded = expand(valueSet);
            if (expanded == null || expanded.getExpansion() == null) {
                return Collections.emptyMap();
            }
            Map<java.lang.String, Set<java.lang.String>> codeSetMap = new LinkedHashMap<>();
            Expansion expansion = expanded.getExpansion();
            for (Expansion.Contains contains : getContains(expansion)) {
                java.lang.String system = (contains.getSystem() != null) ? contains.getSystem().getValue() : null;
                java.lang.String version = (contains.getVersion() != null && contains.getVersion().getValue() != null) ? contains.getVersion().getValue() : VERSION_UNKNOWN;
                java.lang.String code = (contains.getCode() != null) ? contains.getCode().getValue() : null;
                if (system != null && code != null) {
                    java.lang.String url = !VERSION_UNKNOWN.equals(version) ? system + "|" + version : system;
                    if (!isCaseSensitive(url)) {
                        code = code.toLowerCase();
                    }
                    codeSetMap.computeIfAbsent(system + "|" + version, k -> new LinkedHashSet<>()).add(code);
                }
            }
            return codeSetMap;
        } catch (Exception e) {
            java.lang.String url = (valueSet.getUrl() != null) ? valueSet.getUrl().getValue() : "<no url>";
            java.lang.String version = (valueSet.getVersion() != null) ? valueSet.getVersion().getValue() : "<no version>";
            log.log(Level.WARNING, java.lang.String.format("Unable to expand value set with url: %s and version: %s", url, version), e);
        }
        return Collections.emptyMap();
    }
}