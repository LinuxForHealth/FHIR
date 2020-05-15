/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.util;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.term.util.CodeSystemSupport.findConcept;
import static com.ibm.fhir.term.util.CodeSystemSupport.getCodeSystem;
import static com.ibm.fhir.term.util.CodeSystemSupport.getConceptPropertyValue;
import static com.ibm.fhir.term.util.CodeSystemSupport.getConcepts;
import static com.ibm.fhir.term.util.CodeSystemSupport.hasCodeSystemProperty;
import static com.ibm.fhir.term.util.CodeSystemSupport.hasConceptProperty;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
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
import com.ibm.fhir.model.resource.ValueSet.Expansion.Contains;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CodeSystemContentMode;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * A utility class for expanding FHIR value sets
 */
public final class ValueSetSupport {
    private static final Logger log = Logger.getLogger(ValueSetSupport.class.getName());

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
                    .contains(result)
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
            if (!CodeSystemContentMode.COMPLETE.equals(codeSystem.getContent())) {
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
    public static Set<Contains> getContains(Expansion expansion) {
        if (expansion == null) {
            return Collections.emptySet();
        }
        Set<Contains> result = (expansion.getTotal() != null) ? new LinkedHashSet<>(expansion.getTotal().getValue()) : new LinkedHashSet<>();
        for (Contains contains : expansion.getContains()) {
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

    private static Contains buildContains(Uri system, String version, Code code) {
        return Contains.builder()
            .system(system)
            .version(version)
            .code(code)
            .build();
    }

    private static Contains buildContains(Uri system, String version, Concept concept) {
        Code code = (concept.getCode() != null) ? concept.getCode() : null;
        if (code != null) {
            return buildContains(system, version, code);
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
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new DescendentOfFilter(concept);
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
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new GeneralizesFilter(concept);
            }
        }
        return null;
    }

    private static ConceptFilter createInFilter(CodeSystem codeSystem, Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
             return new InFilter(property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                 .map(s -> Code.of(s))
                 .collect(Collectors.toSet()));
        }
        return null;
    }

    private static ConceptFilter createIsAFilter(CodeSystem codeSystem, Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsAFilter(concept);
            }
        }
        return null;
    }

    private static ConceptFilter createIsNotAFilter(CodeSystem codeSystem, Filter filter) {
        if ("concept".equals(filter.getProperty().getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            Concept concept = findConcept(codeSystem, code(filter.getValue()));
            if (concept != null) {
                return new IsNotAFilter(concept);
            }
        }
        return null;
    }

    private static ConceptFilter createNotInFilter(CodeSystem codeSystem, Filter filter) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
             return new NotInFilter(property, Arrays.asList(filter.getValue().getValue().split(",")).stream()
                 .map(s -> Code.of(s))
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
                        systemContains.add(buildContains(system, version, code));
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
                    for (Concept concept : getConcepts(codeSystem)) {
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
                valueSetContains.addAll(getContains(expand(getValueSet(url)).getExpansion()));
            }
        }

        if (!systemContains.isEmpty() && !valueSetContains.isEmpty()) {
            Set<Contains> intersection = new LinkedHashSet<>(systemContains);
            intersection.retainAll(valueSetContains);
            return intersection;
        }

        return !systemContains.isEmpty() ? systemContains : valueSetContains;
    }

    private static Set<Contains> getContains(Contains contains) {
        if (contains == null) {
            return Collections.emptySet();
        }
        Set<Contains> result = new LinkedHashSet<>();
        result.add(contains);
        for (Contains c : contains.getContains()) {
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
        private final Code property;
        private final String value;
        private final Set<Concept> children;
        private final Concept child;

        public EqualsFilter(CodeSystem codeSystem, Code property, String value) {
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