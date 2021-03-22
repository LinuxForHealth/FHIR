/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.provider;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.term.graph.util.FHIRTermGraphUtil.toLong;
import static com.ibm.fhir.term.graph.util.FHIRTermGraphUtil.toObject;
import static com.ibm.fhir.term.util.CodeSystemSupport.convertsToBoolean;
import static com.ibm.fhir.term.util.CodeSystemSupport.getCodeSystemPropertyType;
import static com.ibm.fhir.term.util.CodeSystemSupport.hasCodeSystemProperty;
import static com.ibm.fhir.term.util.CodeSystemSupport.isCaseSensitive;
import static com.ibm.fhir.term.util.CodeSystemSupport.normalize;
import static com.ibm.fhir.term.util.CodeSystemSupport.toElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.filter.TimeLimitStep;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.attribute.Text;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Designation;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Property;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.PropertyType;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;
import com.ibm.fhir.term.service.exception.FHIRTermServiceException;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;

/**
 * Graph-based implementation of the {@link FHIRTermServiceProvider} interface using {@link FHIRTermGraph}
 */
public class GraphTermServiceProvider implements FHIRTermServiceProvider {
    public static final int DEFAULT_TIME_LIMIT = 90000;   // 90 seconds
    private static final int DEFAULT_COUNT = 1000;

    private final FHIRTermGraph graph;
    private final int timeLimit;

    public GraphTermServiceProvider(Configuration configuration) {
        Objects.requireNonNull(configuration, "configuration");
        graph = FHIRTermGraphFactory.open(configuration);
        timeLimit = DEFAULT_TIME_LIMIT;
    }

    public GraphTermServiceProvider(Configuration configuration, int timeLimit) {
        Objects.requireNonNull(configuration, "configuration");
        graph = FHIRTermGraphFactory.open(configuration);
        this.timeLimit = timeLimit;
    }

    public GraphTermServiceProvider(FHIRTermGraph graph) {
        this.graph = Objects.requireNonNull(graph, "graph");
        timeLimit = DEFAULT_TIME_LIMIT;
    }

    public GraphTermServiceProvider(FHIRTermGraph graph, int timeLimit) {
        this.graph = Objects.requireNonNull(graph, "graph");
        this.timeLimit = timeLimit;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Concept> closure(CodeSystem codeSystem, Code code) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");

        Set<Concept> concepts = new LinkedHashSet<>();

        boolean caseSensitive = isCaseSensitive(codeSystem);

        GraphTraversal<Vertex, Vertex> g = whereCodeSystem(hasCode(vertices(), code.getValue(), caseSensitive), codeSystem)
                .union(__.identity(), whereCodeSystem(hasCode(vertices(), code.getValue(), caseSensitive), codeSystem)
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

    @Override
    public Concept getConcept(CodeSystem codeSystem, Code code) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        return getConcept(codeSystem, code, true, true);
    }

    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");

        Set<Concept> concepts = new LinkedHashSet<>(getCount(codeSystem));

        GraphTraversal<Vertex, Vertex> g = hasVersion(hasUrl(vertices(), codeSystem.getUrl()), codeSystem.getVersion())
                .out("concept")
                .timeLimit(timeLimit);
        TimeLimitStep<?> timeLimitStep = getTimeLimitStep(g);

        g.elementMap().toStream().forEach(elementMap -> concepts.add(createConcept(elementMap)));

        checkTimeLimit(timeLimitStep);

        return concepts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");

        Set<Concept> concepts = new LinkedHashSet<>();

        GraphTraversal<Vertex, Vertex> g = vertices();

        boolean caseSensitive = isCaseSensitive(codeSystem);

        boolean first = true;
        for (Filter filter : filters) {
            Code property = filter.getProperty();
            com.ibm.fhir.model.type.String value = filter.getValue();
            PropertyType type = getCodeSystemPropertyType(codeSystem, property);

            switch (filter.getOp().getValueAsEnumConstant()) {
            case DESCENDENT_OF:
                // descendants
                if ("concept".equals(property.getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                    g = whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                            .repeat(__.in(FHIRTermGraph.IS_A)
                                .simplePath()
                                .dedup())
                            .emit();
                }
                break;
            case EQUALS:
                if ((("parent".equals(property.getValue()) || "child".equals(property.getValue())) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()))
                        || (hasCodeSystemProperty(codeSystem, property) && !PropertyType.CODING.equals(type))) {
                    if ("parent".equals(property.getValue())) {
                        g = whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem).in(FHIRTermGraph.IS_A);
                    } else if ("child".equals(property.getValue())) {
                        g = whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem).out(FHIRTermGraph.IS_A);
                    } else {
                        Element element = toElement(value, type);
                        if (first) {
                            g = whereCodeSystem(g.has(getPropertyKey(type), element.is(DateTime.class) ? toLong(element.as(DateTime.class)) : toObject(element)).in("property_"), codeSystem);
                        } else {
                            g = whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), element.is(DateTime.class) ? toLong(element.as(DateTime.class)) : toObject(element))), codeSystem);
                        }
                    }
                }
                break;
            case EXISTS:
                if (hasCodeSystemProperty(codeSystem, property) && convertsToBoolean(value)) {
                    if (Boolean.valueOf(value.getValue())) {
                        if (first) {
                            g = whereCodeSystem(g.has("code", property.getValue()).in("property_"), codeSystem);
                        } else {
                            g = whereCodeSystem(g.where(__.out("property_").has("code", property.getValue())), codeSystem);
                        }
                    } else {
                        g = whereCodeSystem(g.not(__.out("property_").has("code", property.getValue())).hasLabel("Concept"), codeSystem);
                    }
                }
                break;
            case GENERALIZES:
                // ancestors and self
                if ("concept".equals(property.getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                    g = whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                            .union(__.identity(), whereCodeSystem(hasCode(vertices(), value.getValue(), caseSensitive), codeSystem)
                                .repeat(__.out(FHIRTermGraph.IS_A)
                                    .simplePath()
                                    .dedup())
                                .emit());
                }
                break;
            case IN:
                if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
                    if ("concept".equals(property.getValue())) {
                        if (caseSensitive) {
                            g = whereCodeSystem(g.has("code", P.within(Arrays.stream(value.getValue().split(","))
                                .collect(Collectors.toSet()))), codeSystem);
                        } else {
                            g = whereCodeSystem(g.has("codeLowerCase", P.within(Arrays.stream(value.getValue().split(","))
                                .map(CodeSystemSupport::normalize)
                                .collect(Collectors.toSet()))), codeSystem);
                        }
                    } else {
                        if (first) {
                            g = whereCodeSystem(g.has(getPropertyKey(type), P.within(Arrays.stream(value.getValue().split(","))
                                .map(v -> toElement(v, type))
                                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                                .collect(Collectors.toSet()))).in("property_"), codeSystem);
                        } else {
                            g = whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), P.within(Arrays.stream(value.getValue().split(","))
                                .map(v -> toElement(v, type))
                                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                                .collect(Collectors.toSet())))), codeSystem);
                        }
                    }
                }
                break;
            case IS_A:
                // descendants and self
                if ("concept".equals(property.getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                    g = whereCodeSystem(hasCode(g, value.getValue(), caseSensitive), codeSystem)
                            .union(__.identity(), whereCodeSystem(hasCode(vertices(), value.getValue(), caseSensitive), codeSystem)
                                .repeat(__.in(FHIRTermGraph.IS_A)
                                    .simplePath()
                                    .dedup())
                                .emit());
                }
                break;
            case IS_NOT_A:
                // not descendants or self
                if ("concept".equals(property.getValue()) && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                    g = whereCodeSystem(g.not(__.repeat(__.out(FHIRTermGraph.IS_A))
                            .until(hasCode(value.getValue(), caseSensitive)))
                            .not(hasCode(value.getValue(), caseSensitive))
                            .hasLabel("Concept"), codeSystem);
                }
                break;
            case NOT_IN:
                if ("concept".equals(property.getValue()) || hasCodeSystemProperty(codeSystem, property)) {
                    if ("concept".equals(property.getValue())) {
                        if (caseSensitive) {
                            g = whereCodeSystem(g.has("code", P.without(Arrays.stream(value.getValue().split(","))
                                .collect(Collectors.toSet()))), codeSystem);
                        } else {
                            g = whereCodeSystem(g.has("codeLowerCase", P.without(Arrays.stream(value.getValue().split(","))
                                .map(CodeSystemSupport::normalize)
                                .collect(Collectors.toSet()))), codeSystem);
                        }
                    } else {
                        if (first) {
                            g = whereCodeSystem(g.has(getPropertyKey(type), P.without(Arrays.stream(value.getValue().split(","))
                                .map(v -> toElement(v, type))
                                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                                .collect(Collectors.toSet()))).in("property_"), codeSystem);
                        } else {
                            g = whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), P.without(Arrays.stream(value.getValue().split(","))
                                .map(v -> toElement(v, type))
                                .map(e -> e.is(DateTime.class) ? toLong(e.as(DateTime.class)) : toObject(e))
                                .collect(Collectors.toSet())))), codeSystem);
                        }
                    }
                }
                break;
            case REGEX:
                if (hasCodeSystemProperty(codeSystem, property) && (PropertyType.CODE.equals(type) || PropertyType.STRING.equals(type))) {
                    if (first) {
                        g = whereCodeSystem(g.has(getPropertyKey(type), Text.textRegex(value.getValue())).in("property_"), codeSystem);
                    } else {
                        g = whereCodeSystem(g.where(__.out("property_").has(getPropertyKey(type), Text.textRegex(value.getValue()))), codeSystem);
                    }
                }
                break;
            }
            first = false;
        }

        g = g.timeLimit(timeLimit);
        TimeLimitStep<?> timeLimitStep = getTimeLimitStep(g);

        g.elementMap().toStream().forEach(elementMap -> concepts.add(createConcept(elementMap)));

        checkTimeLimit(timeLimitStep);

        return concepts;
    }

    public FHIRTermGraph getGraph() {
        return graph;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    @Override
    public boolean hasConcept(CodeSystem codeSystem, Code code) {
        return whereCodeSystem(hasCode(vertices(), code.getValue(), isCaseSensitive(codeSystem)), codeSystem).hasNext();
    }

    @Override
    public boolean isSupported(CodeSystem codeSystem) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        return hasVersion(hasUrl(vertices(), codeSystem.getUrl()), codeSystem.getVersion()).hasNext();
    }

    @Override
    public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");

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

    private Concept getConcept(CodeSystem codeSystem, Code code, boolean includeDesignations, boolean includeProperties) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
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
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        List<Designation> designations = new ArrayList<>();
        whereCodeSystem(hasCode(vertices(), code, isCaseSensitive(codeSystem)), codeSystem)
            .out("designation")
            .elementMap()
            .toStream()
            .forEach(elementMap -> designations.add(createDesignation(elementMap, codeSystem.getUrl().getValue())));
        return designations;
    }

    private List<Property> getProperties(CodeSystem codeSystem, String code) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        List<Property> properties = new ArrayList<>();
        whereCodeSystem(hasCode(vertices(), code, isCaseSensitive(codeSystem)), codeSystem)
            .out("property_")
            .elementMap()
            .toStream()
            .forEach(elementMap -> properties.add(createProperty(elementMap)));
        return properties;
    }

    private String getPropertyKey(PropertyType type) {
        switch (type.getValueAsEnumConstant()) {
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
            return com.ibm.fhir.model.type.Boolean.of((Boolean) elementMap.get("valueBoolean"));
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
            return com.ibm.fhir.model.type.Integer.of((Integer) elementMap.get("valueInteger"));
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

    private GraphTraversal<Vertex, Vertex> hasVersion(GraphTraversal<Vertex, Vertex> g, com.ibm.fhir.model.type.String version) {
        return (version != null) ? g.has("version", version.getValue()) : g;
    }

    private GraphTraversal<Vertex, Vertex> vertices(Object... vertexIds) {
        return graph.traversal().V(vertexIds);
    }

    private GraphTraversal<Vertex, Vertex> whereCodeSystem(GraphTraversal<Vertex, Vertex> g, CodeSystem codeSystem) {
        return g.where(hasVersion(hasUrl(__.in("concept").hasLabel("CodeSystem"), codeSystem.getUrl()), codeSystem.getVersion()));
    }
}
