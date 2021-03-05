/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.provider;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.term.graph.util.FHIRTermGraphUtil.normalize;
import static com.ibm.fhir.term.util.CodeSystemSupport.isCaseSensitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Designation;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Property;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

public class GraphTermServiceProvider implements FHIRTermServiceProvider {
    private final FHIRTermGraph graph;

    public GraphTermServiceProvider(Configuration configuration) {
        graph = FHIRTermGraphFactory.open(configuration);
    }

    @Override
    public Set<Concept> closure(CodeSystem codeSystem, Code code) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        Set<Concept> concepts = new LinkedHashSet<>();
        concepts.add(getConcept(codeSystem, code, false, false));
        whereCodeSystem(hasCode(vertices(), code.getValue(), isCaseSensitive(codeSystem)), codeSystem)
            .repeat(__.in(FHIRTermGraph.ISA)
                .simplePath()
                .dedup())
            .emit()
            .elementMap()
            .toStream()
            .forEach(elementMap -> concepts.add(createConcept(elementMap)));
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
        List<Concept> concepts = new ArrayList<>(getCount(codeSystem));
        hasVersion(hasUrl(vertices(), codeSystem.getUrl()), codeSystem.getVersion())
            .out("concept")
            .elementMap()
            .toStream()
            .forEach(elementMap -> concepts.add(createConcept(elementMap)));
        return Collections.emptySet();
    }

    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
        GraphTraversal<Vertex, Vertex> g = vertices();
        for (Filter filter : filters) {
            switch (filter.getOp().getValueAsEnumConstant()) {
            case DESCENDENT_OF:
                break;
            case EQUALS:
                break;
            case EXISTS:
                break;
            case GENERALIZES:
                break;
            case IN:
                break;
            case IS_A:
                break;
            case IS_NOT_A:
                break;
            case NOT_IN:
                break;
            case REGEX:
                break;
            default:
                break;
            }
        }
        throw new UnsupportedOperationException();
    }

    public FHIRTermGraph getGraph() {
        return graph;
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
        return whereCodeSystem(hasCode(vertices(), codeA.getValue(), caseSensitive), codeSystem)
            .repeat(__.in(FHIRTermGraph.ISA)
                .simplePath())
            .until(hasCode(codeB.getValue(), caseSensitive))
            .hasNext();
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
        return -1;
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
        if (elementMap.containsKey("valueDecimal")) {
            return Decimal.of((Double) elementMap.get("valueDecimal"));
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
        if (caseSensitive) {
            return g.has("code", code);
        }
        return g.has("codeLowerCase", normalize(code));
    }

    // anonymous graph traversal
    private GraphTraversal<Object, Object> hasCode(String code, boolean caseSensitive) {
        if (caseSensitive) {
            return __.has("code", code);
        }
        return __.has("codeLowerCase", normalize(code));
    }

    private GraphTraversal<Vertex, Vertex> hasUrl(GraphTraversal<Vertex, Vertex> g, Uri url) {
        return g.has("url", url.getValue());
    }

    private GraphTraversal<Vertex, Vertex> hasVersion(GraphTraversal<Vertex, Vertex> g, com.ibm.fhir.model.type.String version) {
        if (version != null) {
            return g.has("version", version.getValue());
        }
        return g;
    }

    private GraphTraversal<Vertex, Vertex> vertices(Object... vertexIds) {
        return graph.traversal().V(vertexIds);
    }

    private GraphTraversal<Vertex, Vertex> whereCodeSystem(GraphTraversal<Vertex, Vertex> g, CodeSystem codeSystem) {
        return g.where(hasVersion(hasUrl(__.in("concept").hasLabel("CodeSystem"), codeSystem.getUrl()), codeSystem.getVersion()));
    }
}
