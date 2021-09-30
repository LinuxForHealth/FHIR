/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.loader.impl;

import static com.ibm.fhir.term.graph.loader.util.FHIRTermGraphLoaderUtil.toMap;
import static com.ibm.fhir.term.util.CodeSystemSupport.getConcepts;
import static com.ibm.fhir.term.util.CodeSystemSupport.normalize;
import static com.ibm.fhir.term.util.CodeSystemSupport.toLong;
import static com.ibm.fhir.term.util.CodeSystemSupport.toObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Designation;
import com.ibm.fhir.model.resource.CodeSystem.Concept.Property;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.loader.FHIRTermGraphLoader;

public class CodeSystemTermGraphLoader extends AbstractTermGraphLoader {
    private static final Logger log = Logger.getLogger(CodeSystemTermGraphLoader.class.getName());

    private final CodeSystem codeSystem;
    private final Set<Concept> concepts;
    private final Map<Concept, Vertex> conceptVertexMap;

    private Vertex codeSystemVertex = null;

    public CodeSystemTermGraphLoader(Map<String, String> options) {
        super(options);

        CodeSystem codeSystem = null;

        if (options.containsKey("file")) {
            // load code system from file
            try (InputStream in = new FileInputStream(options.get("file"))) {
                codeSystem = FHIRParser.parser(Format.JSON).parse(in);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (options.containsKey("url")) {
            // get code system from registry
            codeSystem = FHIRRegistry.getInstance().getResource(options.get("url"), CodeSystem.class);
        }

        this.codeSystem = Objects.requireNonNull(codeSystem, "codeSystem");
        concepts = getConcepts(codeSystem);
        conceptVertexMap = new HashMap<>();
    }

    public CodeSystemTermGraphLoader(Configuration configuration, CodeSystem codeSystem) {
        super(Collections.emptyMap(), configuration);

        this.codeSystem = Objects.requireNonNull(codeSystem, "codeSystem");
        concepts = getConcepts(codeSystem);
        conceptVertexMap = new HashMap<>();
    }

    public CodeSystemTermGraphLoader(FHIRTermGraph graph, CodeSystem codeSystem) {
        super(Collections.emptyMap(), graph);

        this.codeSystem = Objects.requireNonNull(codeSystem, "codeSystem");
        concepts = getConcepts(codeSystem);
        conceptVertexMap = new HashMap<>();
    }

    @Override
    public void load() {
        createCodeSystemVertex();
        createConceptVertices();
        if (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                codeSystem.getHierarchyMeaning() == null) {
            createEdges();
        }
    }

    public CodeSystem getCodeSystem() {
        return codeSystem;
    }

    private void createCodeSystemVertex() {
        String url = codeSystem.getUrl().getValue();

        codeSystemVertex = g.addV("CodeSystem")
                .property("url", url)
                .property("count", concepts.size())
                .next();

        if (codeSystem.getVersion() != null) {
            g.V(codeSystemVertex)
                .property("version", codeSystem.getVersion().getValue())
                .next();
        }

        g.tx().commit();
    }

    private void createConceptVertices() {
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

                String key = "value" + value.getClass().getSimpleName();

                Vertex propertyVertex = g.addV("Property_")
                        .property("code", property.getCode().getValue())
                        .property(key, toObject(value))
                        .next();

                if (value.is(DateTime.class)) {
                    g.V(propertyVertex).property("valueDateTimeLong", toLong(value.as(DateTime.class))).next();
                } else if (value.is(Decimal.class)) {
                    g.V(propertyVertex).property("valueDecimalString", value.as(Decimal.class).getValue().toPlainString()).next();
                }

                g.V(conceptVertex).addE("property_").to(propertyVertex).next();
            }

            g.V(codeSystemVertex).addE("concept").to(conceptVertex).next();

            conceptVertexMap.put(concept, conceptVertex);
        }

        g.tx().commit();
    }

    private void createEdges() {
        for (Concept concept : concepts) {
            Vertex v = conceptVertexMap.get(concept);
            for (Concept child : concept.getConcept()) {
                Vertex w = conceptVertexMap.get(child);
                g.V(w).addE(FHIRTermGraph.IS_A).to(v).next();
            }
        }

        g.tx().commit();
    }

    public static void main(String[] args) throws Exception {
        Options options = null;
        CodeSystemTermGraphLoader loader = null;
        try {
            long start = System.currentTimeMillis();

            options = FHIRTermGraphLoader.Type.CODESYSTEM.options();

            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            loader = new CodeSystemTermGraphLoader(toMap(commandLine));
            loader.load();

            long end = System.currentTimeMillis();

            log.info("Loading time (milliseconds): " + (end - start));
        } catch (MissingOptionException e) {
            System.out.println("MissingOptionException: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("CodeSystemTermGraphLoader", options);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            if (loader != null) {
                loader.close();
            }
        }
    }
}
