/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.loader;

import static com.ibm.fhir.term.graph.util.FHIRTermGraphUtil.normalize;
import static com.ibm.fhir.term.util.CodeSystemSupport.getConcepts;
import static com.ibm.fhir.term.util.CodeSystemSupport.isCaseSensitive;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Designation;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Property;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.term.graph.FHIRTermGraph;

public class CodeSystemTermGraphLoader {
    private final FHIRTermGraph termGraph;
    private final CodeSystem codeSystem;

    public CodeSystemTermGraphLoader(FHIRTermGraph termGraph, CodeSystem codeSystem) {
        Objects.requireNonNull(codeSystem.getUrl(), "CodeSystem.url");
        this.termGraph = Objects.requireNonNull(termGraph, "termGraph");
        this.codeSystem = Objects.requireNonNull(codeSystem, "codeSystem");
    }

    public void load() {
        GraphTraversalSource g = termGraph.traversal();

        Map<Concept, Vertex> conceptVertexMap = new HashMap<>();

        String url = codeSystem.getUrl().getValue();

        Vertex codeSystemVertex = g.addV("CodeSystem")
                .property("url", url)
                .property("designationUseSystem", url)
                .property("caseSensitive", isCaseSensitive(codeSystem))
                .next();

        if (codeSystem.getVersion() != null) {
            g.V(codeSystemVertex)
                .property("version", codeSystem.getVersion().getValue())
                .next();
        }

        Set<Concept> concepts = getConcepts(codeSystem);

        for (Concept concept : concepts) {
            String code = concept.getCode().getValue();
            Vertex conceptVertex = g.addV("Concept")
                    .property("code", code)
                    .property("codeLowerCase", normalize(code))
                    .next();

            if (concept.getDisplay() != null) {
                String display = concept.getDisplay().getValue();
                g.V(conceptVertex)
                    .property("display", display)
                    .property("displayLowerCase", normalize(display))
                    .next();
            }

            for (Designation designation : concept.getDesignation()) {
                Vertex designationVertex = g.addV("Designation")
                        .property("value", designation.getValue().getValue())
                        .next();

                if (designation.getUse() != null) {
                    g.V(designationVertex)
                        .property("use", designation.getUse().getCode().getValue())
                        .next();
                }

                if (designation.getLanguage() != null) {
                    g.V(designationVertex)
                        .property("language", designation.getLanguage().getValue())
                        .next();
                }

                g.V(conceptVertex).addE("designation").to(designationVertex).next();
            }

            for (Property property : concept.getProperty()) {
                Element value = property.getValue();
                Vertex propertyVertex = g.addV("Property_")
                        .property("code", property.getCode().getValue())
                        .property("value" + value.getClass().getSimpleName(), value)
                        .next();

                g.V(conceptVertex).addE("property_").to(propertyVertex).next();
            }

            g.V(codeSystemVertex).addE("concept").to(conceptVertex).next();

            conceptVertexMap.put(concept, conceptVertex);
        }

        for (Concept concept : concepts) {
            Vertex v = conceptVertexMap.get(concept);
            for (Concept child : concept.getConcept()) {
                Vertex w = conceptVertexMap.get(child);
                g.V(w).addE("isA").to(v).next();
            }
        }

        g.tx().commit();

        termGraph.close();
    }
}
