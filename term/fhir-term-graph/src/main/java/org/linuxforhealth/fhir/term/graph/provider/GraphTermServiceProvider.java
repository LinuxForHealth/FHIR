/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.provider;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.convertsToBoolean;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.getCodeSystemPropertyType;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.hasCodeSystemProperty;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.isCaseSensitive;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.normalize;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.toElement;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.toLong;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.toObject;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.filter.TimeLimitStep;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.attribute.Text;

import org.linuxforhealth.fhir.cache.annotation.Cacheable;
import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept.Designation;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept.Property;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose.Include.Filter;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.CodeSystemHierarchyMeaning;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.PropertyType;
import org.linuxforhealth.fhir.term.graph.FHIRTermGraph;
import org.linuxforhealth.fhir.term.graph.factory.FHIRTermGraphFactory;
import org.linuxforhealth.fhir.term.service.exception.FHIRTermServiceException;
import org.linuxforhealth.fhir.term.spi.AbstractTermServiceProvider;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;

/**
 * Graph-based implementation of the {@link FHIRTermServiceProvider} interface using {@link FHIRTermGraph}
 */
public class GraphTermServiceProvider extends AbstractTermServiceProvider {
    public static final int DEFAULT_TIME_LIMIT = 90000;   // 90 seconds
    private static final int DEFAULT_COUNT = 1000;

    private final FHIRTermGraph graph;
    private final int timeLimit;

    /**
     * @param configuration
     */
    public GraphTermServiceProvider(Configuration configuration) {
        requireNonNull(configuration, "configuration");
        graph = FHIRTermGraphFactory.open(configuration);
        timeLimit = DEFAULT_TIME_LIMIT;
    }

    /**
     * @param configuration
     * @param timeLimit the default time to wait for queries to complete (in milliseconds)
     */
    public GraphTermServiceProvider(Configuration configuration, int timeLimit) {
        requireNonNull(configuration, "configuration");
        graph = FHIRTermGraphFactory.open(configuration);
        this.timeLimit = timeLimit;
    }

    /**
     * @param graph
     */
    public GraphTermServiceProvider(FHIRTermGraph graph) {
        this.graph = requireNonNull(graph, "graph");
        timeLimit = DEFAULT_TIME_LIMIT;
    }

    /**
     * @param graph
     * @param timeLimit the default time to wait for queries to complete (in milliseconds)
     */
    public GraphTermServiceProvider(FHIRTermGraph graph, int timeLimit) {
        this.graph = requireNonNull(graph, "graph");
        this.timeLimit = timeLimit;
    }

    @SuppressWarnings("unchecked")
    @Cacheable
    @Override
    public Set<Concept> closure(CodeSystem codeSystem, Code code) {
        checkArguments(codeSystem, code);

        Set<Concept> concepts = new LinkedHashSet<>();

        boolean caseSensitive = isCaseSensitive(codeSystem);

        GraphTraversal<Vertex, Vertex> g = whereCodeSystem(hasCode(vertices(), code.getValue(), caseSensitive), codeSystem)
            .union(__.identity(), whereCodeSystem(hasCode(code.getValue(), caseSensitive), codeSystem)
                .repeat(__.in(FHIRTermGraph.IS_A)
                    .simplePath()
                    .dedup())
                .emit())
            .timeLimit(timeLimit);
        TimeLimitStep<?> timeLimitStep = getTimeLimitStep(g);

        g.elementMap().toStream().forEach(elementMap -> concepts.add(createConcept(elementMap)));

        checkTimeLimit(timeLimitStep);

        return concepts;
    }

    @Cacheable
    @Override
    public Map<Code, Set<Concept>> closure(CodeSystem codeSystem, Set<Code> codes) {
        return super.closure(codeSystem, codes);
    }

    @Cacheable
    @Override
    public Concept getConcept(CodeSystem codeSystem, Code code) {
        checkArguments(codeSystem, code);
        return getConcept(codeSystem, code, true, true);
    }

    @Cacheable
    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem) {
        return getConcepts(codeSystem, Function.identity());
    }

    @Cacheable
    @Override
    public <R> Set<R> getConcepts(CodeSystem codeSystem, Function<Concept, ? extends R> function) {
        checkArguments(codeSystem, function);

        Set<R> concepts = new LinkedHashSet<>(getCount(codeSystem));

        GraphTraversal<Vertex, Vertex> g = hasVersion(hasUrl(vertices(), codeSystem.getUrl()), codeSystem.getVersion())
            .out("concept")
            .timeLimit(timeLimit);
        TimeLimitStep<?> timeLimitStep = getTimeLimitStep(g);

        g.elementMap().toStream().forEach(elementMap -> concepts.add(function.apply(createConcept(elementMap))));

        checkTimeLimit(timeLimitStep);

        return concepts;
    }

    @Cacheable
    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
        return getConcepts(codeSystem, filters, Function.identity());
    }

    @Cacheable
    @Override
    public <R> Set<R> getConcepts(CodeSystem codeSystem, List<Filter> filters, Function<Concept, ? extends R> function) {
        checkArguments(codeSystem, filters);

        Set<R> concepts = new LinkedHashSet<>();

        GraphTraversal<Vertex, Vertex> g = vertices();

        boolean first = true;
        for (Filter filter : filters) {
            switch (filter.getOp().getValueAsEnum()) {
            case DESCENDENT_OF:
                // descendants
                g = applyDescendentOfFilter(codeSystem, filter, g);
                break;
            case EQUALS:
                g = applyEqualsFilter(codeSystem, filter, first, g);
                break;
            case EXISTS:
                g = applyExistsFilter(codeSystem, filter, first, g);
                break;
            case GENERALIZES:
                // ancestors and self
                g = applyGeneralizesFilter(codeSystem, filter, first, g);
                break;
            case IN:
                g = applyInFilter(codeSystem, filter, first, g);
                break;
            case IS_A:
                // descendants and self
                g = applyIsAFilter(codeSystem, filter, first, g);
                break;
            case IS_NOT_A:
                // not descendants or self
                g = applyIsNotAFilter(codeSystem, filter, first, g);
                break;
            case NOT_IN:
                g = applyNotInFilter(codeSystem, filter, first, g);
                break;
            case REGEX:
                g = applyRegexFilter(codeSystem, filter, first, g);
                break;
            }
            first = false;
        }

        if (filters.isEmpty()) {
            g = g.hasLabel("Concept");
        }

        g = g.timeLimit(timeLimit);
        TimeLimitStep<?> timeLimitStep = getTimeLimitStep(g);

        g.elementMap().toStream().forEach(elementMap -> concepts.add(function.apply(createConcept(elementMap))));

        checkTimeLimit(timeLimitStep);

        return concepts;
    }

    public FHIRTermGraph getGraph() {
        return graph;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    @Cacheable
    @Override
    public boolean hasConcept(CodeSystem codeSystem, Code code) {
        checkArguments(codeSystem, code);
        return whereCodeSystem(hasCode(vertices(), code.getValue(), isCaseSensitive(codeSystem)), codeSystem).hasNext();
    }

    @Cacheable
    @Override
    public boolean hasConcepts(CodeSystem codeSystem, Set<Code> codes) {
        return super.hasConcepts(codeSystem, codes);
    }

    @Cacheable
    @Override
    public boolean isSupported(CodeSystem codeSystem) {
        checkArgument(codeSystem);
        return hasVersion(hasUrl(vertices(), codeSystem.getUrl()), codeSystem.getVersion()).hasNext();
    }

    @Cacheable
    @Override
    public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
        checkArguments(codeSystem, codeA, codeB);

        boolean caseSensitive = isCaseSensitive(codeSystem);

        if (codeA.equals(codeB) || (!caseSensitive && normalize(codeA.getValue()).equals(normalize(codeB.getValue())))) {
            return true;
        }

        GraphTraversal<Vertex, Vertex> g = whereCodeSystem(hasCode(vertices(), codeA.getValue(), caseSensitive), codeSystem)
            .repeat(__.in(FHIRTermGraph.IS_A)
                .simplePath())
            .until(hasCode(codeB.getValue(), caseSensitive))
            .timeLimit(timeLimit);
        TimeLimitStep<?> timeLimitStep = getTimeLimitStep(g);

        boolean subsumes = g.hasNext();

        checkTimeLimit(timeLimitStep);

        return subsumes;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private GraphTraversal<Vertex, Vertex> applyChildEqualsFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        return first ?
            whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                .out(FHIRTermGraph.IS_A) :
            // intersection with previous results
            (GraphTraversal) whereCodeSystem(hasCode(g.as("a").V(), value.getValue(), caseSensitive), codeSystem)
                .out(FHIRTermGraph.IS_A)
                .as("b")
                .select("a")
                .where("a", P.eq("b"));
    }

    private GraphTraversal<Vertex, Vertex> applyConceptInFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        return caseSensitive ?
            whereCodeSystem(g.has("code", P.within(Arrays.stream(value.getValue().split(","))
                .collect(Collectors.toSet()))), codeSystem) :
            whereCodeSystem(g.has("codeLowerCase", P.within(Arrays.stream(value.getValue().split(","))
                .map(CodeSystemSupport::normalize)
                .collect(Collectors.toSet()))), codeSystem);
    }

    private GraphTraversal<Vertex, Vertex> applyConceptNotInFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        return caseSensitive ?
            whereCodeSystem(g.has("code", P.without(Arrays.stream(value.getValue().split(","))
                .collect(Collectors.toSet()))), codeSystem) :
            whereCodeSystem(g.has("codeLowerCase", P.without(Arrays.stream(value.getValue().split(","))
                .map(CodeSystemSupport::normalize)
                .collect(Collectors.toSet()))), codeSystem);
    }

    private GraphTraversal<Vertex, Vertex> applyDescendentOfFilter(CodeSystem codeSystem, Filter filter, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        if ("concept".equals(property.getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
            return whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                .repeat(__.in(FHIRTermGraph.IS_A)
                    .simplePath()
                    .dedup())
                .emit();
        }
        throw filterNotApplied(filter);
    }

    private GraphTraversal<Vertex, Vertex> applyEqualsFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        PropertyType type = getCodeSystemPropertyType(codeSystem, property);
        if ((("parent".equals(property.getValue()) || "child".equals(property.getValue())) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()))
                || (hasCodeSystemProperty(codeSystem, property) && !PropertyType.CODING.equals(type))) {
            return "parent".equals(property.getValue()) ?
                applyParentEqualsFilter(codeSystem, filter, first, g) :
                "child".equals(property.getValue()) ?
                    applyChildEqualsFilter(codeSystem, filter, first, g) :
                    applyPropertyEqualsFilter(codeSystem, filter, first, g);
        }
        throw filterNotApplied(filter);
    }

    private GraphTraversal<Vertex, Vertex> applyExistsFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        if (hasCodeSystemProperty(codeSystem, property) && convertsToBoolean(value)) {
            return Boolean.valueOf(value.getValue()) ?
                applyPropertyExistsFilter(codeSystem, filter, first, g) :
                // not exists
                applyPropertyNotExistsFilter(codeSystem, filter, g);
        }
        throw filterNotApplied(filter);
    }

    @SuppressWarnings("unchecked")
    private GraphTraversal<Vertex, Vertex> applyGeneralizesFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        if ("concept".equals(property.getValue()) &&
                (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                        codeSystem.getHierarchyMeaning() == null)) {
            return whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                .union(__.identity(), whereCodeSystem(hasCode(value.getValue(), caseSensitive), codeSystem)
                    .repeat(__.out(FHIRTermGraph.IS_A)
                        .simplePath()
                        .dedup())
                    .emit());
        }
        throw filterNotApplied(filter);
    }

    private GraphTraversal<Vertex, Vertex> applyInFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
            return "concept".equals(property.getValue()) ?
                applyConceptInFilter(codeSystem, filter, first, g) :
                applyPropertyInFilter(codeSystem, filter, first, g);
        }
        throw filterNotApplied(filter);
    }

    @SuppressWarnings("unchecked")
    private GraphTraversal<Vertex, Vertex> applyIsAFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        if ("concept".equals(property.getValue()) &&
                (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                        codeSystem.getHierarchyMeaning() == null)) {
            return whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                .union(__.identity(), whereCodeSystem(hasCode(value.getValue(), caseSensitive), codeSystem)
                    .repeat(__.in(FHIRTermGraph.IS_A)
                        .simplePath()
                        .dedup())
                    .emit());
        }
        throw filterNotApplied(filter);
    }

    private GraphTraversal<Vertex, Vertex> applyIsNotAFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        if ("concept".equals(property.getValue()) &&
                (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                        codeSystem.getHierarchyMeaning() == null)) {
            return whereCodeSystem(g.not(__.repeat(__.out(FHIRTermGraph.IS_A))
                .until(hasCode(value.getValue(), caseSensitive)))
                .not(hasCode(value.getValue(), caseSensitive))
                .hasLabel("Concept"), codeSystem);
        }
        throw filterNotApplied(filter);
    }

    private GraphTraversal<Vertex, Vertex> applyNotInFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
            return "concept".equals(property.getValue()) ?
                applyConceptNotInFilter(codeSystem, filter, first, g) :
                applyPropertyNotInFilter(codeSystem, filter, first, g);
        }
        throw filterNotApplied(filter);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private GraphTraversal<Vertex, Vertex> applyParentEqualsFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        boolean caseSensitive = isCaseSensitive(codeSystem);
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        return first ?
            whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                .in(FHIRTermGraph.IS_A) :
            // intersection with previous results
            (GraphTraversal) whereCodeSystem(hasCode(g.as("a").V(), value.getValue(), caseSensitive), codeSystem)
                .in(FHIRTermGraph.IS_A)
                .as("b")
                .select("a")
                .where("a", P.eq("b"));
    }

    private GraphTraversal<Vertex, Vertex> applyPropertyEqualsFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        PropertyType type = getCodeSystemPropertyType(codeSystem, property);
        Element element = toElement(value, type);
        return first ?
            whereCodeSystem(g.has(getPropertyKey(type), element.is(DateTime.class) ? toLong(element.as(DateTime.class)) : toObject(element))
                .in("property_"), codeSystem) :
            whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), element.is(DateTime.class) ? toLong(element.as(DateTime.class)) : toObject(element))), codeSystem);
    }

    private GraphTraversal<Vertex, Vertex> applyPropertyExistsFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        return first ?
            whereCodeSystem(g.has("code", property.getValue()).in("property_"), codeSystem) :
            whereCodeSystem(g.where(__.out("property_").has("code", property.getValue())), codeSystem);
    }

    private GraphTraversal<Vertex, Vertex> applyPropertyInFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        PropertyType type = getCodeSystemPropertyType(codeSystem, property);
        return first ?
            whereCodeSystem(g.has(getPropertyKey(type), P.within(Arrays.stream(value.getValue().split(","))
                .map(v -> toElement(v, type))
                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                .collect(Collectors.toSet()))).in("property_"), codeSystem) :
            whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), P.within(Arrays.stream(value.getValue().split(","))
                .map(v -> toElement(v, type))
                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                .collect(Collectors.toSet())))), codeSystem);
    }

    private GraphTraversal<Vertex, Vertex> applyPropertyNotExistsFilter(CodeSystem codeSystem, Filter filter, GraphTraversal<Vertex, Vertex> g) {
        return whereCodeSystem(g.not(__.out("property_").has("code", filter.getProperty().getValue())).hasLabel("Concept"), codeSystem);
    }

    private GraphTraversal<Vertex, Vertex> applyPropertyNotInFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        PropertyType type = getCodeSystemPropertyType(codeSystem, property);
        return first ?
            whereCodeSystem(g.has(getPropertyKey(type), P.without(Arrays.stream(value.getValue().split(","))
                .map(v -> toElement(v, type))
                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                .collect(Collectors.toSet()))).in("property_"), codeSystem) :
            whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), P.without(Arrays.stream(value.getValue().split(","))
                .map(v -> toElement(v, type))
                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                .collect(Collectors.toSet())))), codeSystem);
    }

    private GraphTraversal<Vertex, Vertex> applyRegexFilter(CodeSystem codeSystem, Filter filter, boolean first, GraphTraversal<Vertex, Vertex> g) {
        Code property = filter.getProperty();
        org.linuxforhealth.fhir.model.type.String value = filter.getValue();
        PropertyType type = getCodeSystemPropertyType(codeSystem, property);
        if (hasCodeSystemProperty(codeSystem, property) && (PropertyType.CODE.equals(type) || PropertyType.STRING.equals(type))) {
            return first ?
                whereCodeSystem(g.has(getPropertyKey(type), Text.textRegex(value.getValue())).in("property_"), codeSystem) :
                whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), Text.textRegex(value.getValue()))), codeSystem);
        }
        throw filterNotApplied(filter);
    }

    private void checkTimeLimit(TimeLimitStep<?> timeLimitStep) {
        if (timeLimitStep.getTimedOut()) {
            throw new FHIRTermServiceException("Graph traversal timed out", Collections.singletonList(Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.TOO_COSTLY)
                .details(CodeableConcept.builder()
                    .text(string("Graph traversal timed out"))
                    .build())
                .build()));
        }
    }

    private Concept createConcept(CodeSystem codeSystem, String code, Optional<Map<Object, Object>> optional, boolean includeDesignations, boolean includeProperties) {
        if (optional.isPresent()) {
            return createConcept(optional.get(), includeDesignations ? getDesignations(codeSystem, code) : Collections.emptyList(), includeProperties ? getProperties(codeSystem, code) : Collections.emptyList());
        }
        return null;
    }

    private Concept createConcept(Map<Object, Object> elementMap) {
        return createConcept(elementMap, Collections.emptyList(), Collections.emptyList());
    }

    private Concept createConcept(Map<Object, Object> elementMap, List<Designation> designations, List<Property> properties) {
        return Concept.builder()
            .code(Code.of((String) elementMap.get("code")))
            .display(string((String) elementMap.get("display")))
            .designation(designations)
            .property(properties)
            .build();
    }

    private Designation createDesignation(Map<Object, Object> elementMap, String designationUseSystem) {
        Designation.Builder builder = Designation.builder();
        if (elementMap.containsKey("language")) {
            builder.language(Code.of((String) elementMap.get("language")));
        }
        if (elementMap.containsKey("use")) {
            builder.use(Coding.builder()
                .system((designationUseSystem != null) ? Uri.of(designationUseSystem) : null)
                .code(Code.of((String) elementMap.get("use")))
                .build());
        }
        builder.value(string((String) elementMap.get("value")));
        return builder.build();
    }

    private Property createProperty(Map<Object, Object> elementMap) {
        return Property.builder()
            .code(Code.of((String) elementMap.get("code")))
            .value(getValue(elementMap))
            .build();
    }

    private FHIRTermServiceException filterNotApplied(Filter filter) {
        String message = String.format("Filter not applied (property: %s, op: %s, value: %s)",
            filter.getProperty().getValue(),
            filter.getOp().getValue(),
            filter.getValue().getValue());
        throw new FHIRTermServiceException(message, Collections.singletonList(Issue.builder()
            .severity(IssueSeverity.ERROR)
            .code(IssueType.NOT_SUPPORTED)
            .details(CodeableConcept.builder()
                .text(string(message))
                .build())
            .build()));
    }

    private Concept getConcept(CodeSystem codeSystem, Code code, boolean includeDesignations, boolean includeProperties) {
        return createConcept(
            codeSystem,
            code.getValue(),
            whereCodeSystem(hasCode(vertices(), code.getValue(), isCaseSensitive(codeSystem)), codeSystem)
                .elementMap()
                .tryNext(),
            includeDesignations,
            includeProperties);
    }

    private int getCount(CodeSystem codeSystem) {
        Optional<Map<Object, Object>> optional = hasVersion(hasUrl(vertices(), codeSystem.getUrl()), codeSystem.getVersion())
            .elementMap("count")
            .tryNext();
        if (optional.isPresent()) {
            return (Integer) optional.get().get("count");
        }
        return DEFAULT_COUNT;
    }

    private List<Designation> getDesignations(CodeSystem codeSystem, String code) {
        List<Designation> designations = new ArrayList<>();
        whereCodeSystem(hasCode(vertices(), code, isCaseSensitive(codeSystem)), codeSystem)
            .out("designation")
            .elementMap()
            .toStream()
            .forEach(elementMap -> designations.add(createDesignation(elementMap, codeSystem.getUrl().getValue())));
        return designations;
    }

    private List<Property> getProperties(CodeSystem codeSystem, String code) {
        List<Property> properties = new ArrayList<>();
        whereCodeSystem(hasCode(vertices(), code, isCaseSensitive(codeSystem)), codeSystem)
            .out("property_")
            .elementMap()
            .toStream()
            .forEach(elementMap -> properties.add(createProperty(elementMap)));
        return properties;
    }

    private String getPropertyKey(PropertyType type) {
        switch (type.getValueAsEnum()) {
        case BOOLEAN:
            return "valueBoolean";
        case CODE:
            return "valueCode";
        case DATE_TIME:
            return "valueDateTimeLong";
        case DECIMAL:
            return "valueDecimal";
        case INTEGER:
            return "valueInteger";
        case STRING:
            return "valueString";
        default:
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    private TimeLimitStep<?> getTimeLimitStep(GraphTraversal<Vertex, Vertex> g) {
        return (TimeLimitStep) g.asAdmin().getEndStep();
    }

    private Element getValue(Map<Object, Object> elementMap) {
        if (elementMap.containsKey("valueBoolean")) {
            return org.linuxforhealth.fhir.model.type.Boolean.of((Boolean) elementMap.get("valueBoolean"));
        }
        if (elementMap.containsKey("valueCode")) {
            return Code.of((String) elementMap.get("valueCode"));
        }
        if (elementMap.containsKey("valueDateTime")) {
            return DateTime.of((String) elementMap.get("valueDateTime"));
        }
        if (elementMap.containsKey("valueDecimalString")) {
            return Decimal.of((String) elementMap.get("valueDecimalString"));
        }
        if (elementMap.containsKey("valueInteger")) {
            return org.linuxforhealth.fhir.model.type.Integer.of((Integer) elementMap.get("valueInteger"));
        }
        if (elementMap.containsKey("valueString")) {
            return string((String) elementMap.get("valueString"));
        }
        return null;
    }

    private GraphTraversal<Vertex, Vertex> hasCode(GraphTraversal<Vertex, Vertex> g, String code, boolean caseSensitive) {
        return caseSensitive ? g.has("code", code) : g.has("codeLowerCase", normalize(code));
    }

    // anonymous graph traversal
    private GraphTraversal<Vertex, Vertex> hasCode(String code, boolean caseSensitive) {
        return caseSensitive ? __.has("code", code) : __.has("codeLowerCase", normalize(code));
    }

    private GraphTraversal<Vertex, Vertex> hasUrl(GraphTraversal<Vertex, Vertex> g, Uri url) {
        return g.has("url", url.getValue());
    }

    private GraphTraversal<Vertex, Vertex> hasVersion(GraphTraversal<Vertex, Vertex> g, org.linuxforhealth.fhir.model.type.String version) {
        return (version != null) ? g.has("version", version.getValue()) : g;
    }

    private GraphTraversal<Vertex, Vertex> vertices(Object... vertexIds) {
        return graph.traversal().V(vertexIds);
    }

    private GraphTraversal<Vertex, Vertex> whereCodeSystem(GraphTraversal<Vertex, Vertex> g, CodeSystem codeSystem) {
        return g.where(hasVersion(hasUrl(__.in("concept").hasLabel("CodeSystem"), codeSystem.getUrl()), codeSystem.getVersion()));
    }
}
