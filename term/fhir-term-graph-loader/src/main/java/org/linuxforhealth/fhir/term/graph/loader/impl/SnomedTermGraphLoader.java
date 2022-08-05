/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.loader.impl;

import static org.linuxforhealth.fhir.term.graph.loader.util.FHIRTermGraphLoaderUtil.toLabel;
import static org.linuxforhealth.fhir.term.graph.loader.util.FHIRTermGraphLoaderUtil.toMap;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.normalize;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.schema.JanusGraphManagement;

import org.linuxforhealth.fhir.term.graph.loader.FHIRTermGraphLoader;

public class SnomedTermGraphLoader extends AbstractTermGraphLoader {
    private static final Logger log = Logger.getLogger(SnomedTermGraphLoader.class.getName());

    private static final String PREFERRED = "900000000000548007";
    private static final String FULLY_SPECIFIED_NAME = "900000000000003001";

    private String conceptFile = null;
    private String relationshipFile = null;
    private String descriptionFile = null;
    private String languageRefsetFile = null;

    private AtomicInteger counter = null;
    private Map<String, Vertex> vertexMap = null;
    private Vertex codeSystemVertex = null;
    private Set<String> preferred = null;

    public SnomedTermGraphLoader(Map<String, String> options) {
        super(options);

        String baseDir = options.get("base");
        conceptFile = baseDir + "/" + options.get("concept");
        descriptionFile = baseDir + "/" + options.get("desc");
        relationshipFile = baseDir + "/" + options.get("relation");
        languageRefsetFile = baseDir + "/../Refset/Language/" + options.get("lang");

        counter = new AtomicInteger(0);
        vertexMap = new HashMap<>(250000);
        preferred = new HashSet<>(500000);
    }

    @Override
    public void load() {
        createCodeSystemVertex();
        try {
            processConceptsFile();
            processLanguageRefsetFile();
            processDescriptionFile();
            processRelationshipFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createCodeSystemVertex() {
        codeSystemVertex = g.addV("CodeSystem")
                .property("url", "http://snomed.info/sct")
                .next();
        g.tx().commit();
    }

    private void processConceptsFile() throws IOException, FileNotFoundException {
        // concept file
        log.info("Processing concepts file...");
        try (BufferedReader reader = new BufferedReader(new FileReader(conceptFile))) {
            reader.lines().skip(1).forEach(new SnomedReleaseFileConsumer() {
                @Override
                public void processLine(String line) {
                    String[] tokens = line.split("\\t");
                    String id = tokens[0];
                    String active = tokens[2];

                    if ("1".equals(active)) {
                        if (!vertexMap.containsKey(id)) {
                            Vertex v = g.addV("Concept")
                                    .property("code", id)
                                    .property("codeLowerCase", normalize(id))
                                    .next();
                            vertexMap.put(id, v);
                            g.V(codeSystemVertex).addE("concept").to(v).next();
                        }
                    }

                    if ((counter.get() % 10000) == 0) {
                        log.info("counter: " + counter.get());
                        g.tx().commit();
                    }

                    counter.getAndIncrement();
                }
            });

            // commit any uncommitted work
            g.tx().commit();
        }

        int count = counter.get();
        g.V(codeSystemVertex).property("count", count).next();
        g.tx().commit();
    }

    private void processLanguageRefsetFile() throws IOException, FileNotFoundException {
        // language refset file
        log.info("Processing language refset file...");

        try (BufferedReader reader = new BufferedReader(new FileReader(languageRefsetFile))) {
            reader.lines().skip(1).forEach(new SnomedReleaseFileConsumer() {
                @Override
                public void processLine(String line) {
                    String[] tokens = line.split("\\t");
                    String active = tokens[2];
                    String referencedComponentId = tokens[5];
                    String acceptabilityId = tokens[6];

                    if ("1".equals(active) && PREFERRED.equals(acceptabilityId)) {
                        preferred.add(referencedComponentId);
                    }
                }
            });
        }
    }

    private void processDescriptionFile() throws IOException, FileNotFoundException {
        counter.set(0);

        // description file
        log.info("Processing description file...");
        try (BufferedReader reader = new BufferedReader(new FileReader(descriptionFile))) {
            reader.lines().skip(1).forEach(new SnomedReleaseFileConsumer() {
                @Override
                public void processLine(String line) {
                    String[] tokens = line.split("\\t");
                    String id = tokens[0];
                    String active = tokens[2];
                    String conceptId = tokens[4];
                    String typeId = tokens[6];
                    String term = tokens[7];

                    Vertex v = vertexMap.get(conceptId);

                    if ("1".equals(active) && v != null) {
                        if (preferred.contains(id) && !FULLY_SPECIFIED_NAME.equals(typeId)) {
                            // preferred term
                            g.V(v)
                                .property("display", term)
                                .next();
                        }

                        Vertex w = g.addV("Designation")
                                .property("language", "en")
                                .property("use", typeId)
                                .property("value", term)
                                .next();

                        g.V(v).addE("designation").to(w).next();
                    }

                    if ((counter.get() % 10000) == 0) {
                        log.info("counter: " + counter.get());
                        g.tx().commit();
                    }

                    counter.getAndIncrement();
                }
            });

            // commit any uncommitted work
            g.tx().commit();
        }
    }

    private void processRelationshipFile() throws IOException, FileNotFoundException {
        counter.set(0);

        // relationship file
        log.info("Processing relationship file...");
        try (BufferedReader reader = new BufferedReader(new FileReader(relationshipFile))) {
            reader.lines().skip(1).forEach(new SnomedReleaseFileConsumer() {
                @Override
                public void processLine(String line) {
                    String[] tokens = line.split("\\t");
                    String active = tokens[2];
                    String sourceId = tokens[4];
                    String destinationId = tokens[5];
                    String relationshipGroup = tokens[6];
                    String typeId = tokens[7];

                    if ("1".equals(active)) {
                        Vertex u = vertexMap.get(sourceId);
                        Vertex v = vertexMap.get(destinationId);
                        Vertex w = vertexMap.get(typeId);

                        if (u != null && v != null && w != null) {
                            String display = (String) g.V(w).values("display").next();
                            String label = toLabel(display);

                            if (labelFilter.accept(label)) {
                                if (janusGraph.getEdgeLabel(label) == null) {
                                    log.info("Adding label: " + label);
                                    JanusGraphManagement management = janusGraph.openManagement();
                                    management.makeEdgeLabel(label).make();
                                    management.commit();
                                }

                                Edge e = g.V(u).addE(label).to(v).next();

                                if (!"0".equals(relationshipGroup)) {
                                    g.E(e).property("group", relationshipGroup).next();
                                }
                            }
                        }
                    }

                    if ((counter.get() % 10000) == 0) {
                        log.info("counter: " + counter.get());
                        g.tx().commit();
                    }

                    counter.getAndIncrement();
                }
            });

            // commit any uncommitted work
            g.tx().commit();
        }
    }

    private static abstract class SnomedReleaseFileConsumer implements Consumer<String> {
        private final List<String> lines = new ArrayList<>();
        private String previousId = null;

        @Override
        public void accept(String line) {
            if (collect(line)) {
                lines.add(line);
            } else {
                processLines(Collections.unmodifiableList(lines));
                lines.clear();
                lines.add(line);
            }
        }

        private boolean collect(String line) {
            String[] fields = line.split("\\t");
            String id = fields[0];
            if (!id.equals(previousId)) {
                previousId = id;
                return lines.isEmpty();
            }
            return true;
        }

        private void processLines(List<String> lines) {
            processLine(lines.get(lines.size() - 1));
        }

        // template method
        public abstract void processLine(String line);
    }

    public static void main(String[] args) throws Exception {
        Options options = null;
        SnomedTermGraphLoader loader = null;
        try {
            long start = System.currentTimeMillis();

            options = FHIRTermGraphLoader.Type.SNOMED.options();

            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            loader = new SnomedTermGraphLoader(toMap(commandLine));
            loader.load();

            long end = System.currentTimeMillis();

            log.info("Loading time (milliseconds): " + (end - start));
        } catch (MissingOptionException e) {
            System.out.println("MissingOptionException: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("SnomedTermGraphLoader", options);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            if (loader != null) {
                loader.close();
            }
        }
    }
}
