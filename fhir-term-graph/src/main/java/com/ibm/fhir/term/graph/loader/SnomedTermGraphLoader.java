/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.LoggerFactory;

import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.FHIRTermGraphFactory;

public class SnomedTermGraphLoader {
    private static final Logger log = Logger.getLogger(SnomedTermGraphLoader.class.getName());

    private static final String PREFERRED = "900000000000548007";
    private static final String FULLY_SPECIFIED_NAME = "900000000000003001";

    public static void main(String[] args) throws Exception {
        Options options = null;
        FHIRTermGraph graph = null;
        try {
            long start = System.currentTimeMillis();

            ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);

            options = new Options()
                    .addRequiredOption("file", null, true, "Configuration properties file")
                    .addRequiredOption("base", null, true, "SNOMED-CT base directory")
                    .addRequiredOption("concept", null, true, "SNOMED-CT concept file")
                    .addRequiredOption("relation", null, true, "SNOMED-CT relationship file")
                    .addRequiredOption("desc", null, true, "SNOMED-CT description file")
                    .addRequiredOption("lang", null, true, "SNOMED-CT language refset file");

            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);

            String propFileName = commandLine.getOptionValue("file");

            graph = FHIRTermGraphFactory.open(propFileName);
            final JanusGraph janusGraph = graph.getJanusGraph();

            GraphTraversalSource g = graph.traversal();

            String baseDir = commandLine.getOptionValue("base");

            AtomicInteger counter = new AtomicInteger(0);
            Map<String, Vertex> vertexMap = new HashMap<>(250000);

            Vertex codeSystemVertex = g.addV("CodeSystem").property("url", "http://snomed.info").next();
            g.tx().commit();

            // concept file
            String conceptFile = baseDir + "/" + commandLine.getOptionValue("concept");
            try (BufferedReader reader = new BufferedReader(new FileReader(conceptFile))) {
                reader.lines().skip(1).forEach(new SnomedReleaseFileConsumer() {
                    @Override
                    public void processLine(String line) {
                        String[] tokens = line.split("\\t");
                        String id = tokens[0];
                        String active = tokens[2];

                        if ("1".equals(active)) {
                            if (!vertexMap.containsKey(id)) {
                                Vertex v = g.addV("Concept").property("code", id).next();
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

            // language refset file
            log.info("Processing language refset file...");
            Set<String> preferred = new HashSet<>(500000);
            String languageRefsetFile = baseDir + "/../Refset/Language/" + commandLine.getOptionValue("lang");
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

            counter.set(0);

            // description file
            String descriptionFile = baseDir + "/" + commandLine.getOptionValue("desc");
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
                                g.V(v).property("display", term).next();
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

            counter.set(0);

            Set<String> labels = new HashSet<>(getLabels(janusGraph));

            // relationship file
            String relationshipFile = baseDir + "/" + commandLine.getOptionValue("relation");
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

                                if (!labels.contains(label)) {
                                    JanusGraphManagement management = janusGraph.openManagement();
                                    management.makeEdgeLabel(label).make();
                                    management.commit();
                                    labels.add(label);
                                }

                                Edge e = g.V(u).addE(label).to(v).next();

                                if (!"0".equals(relationshipGroup)) {
                                    g.E(e).property("group", Integer.parseInt(relationshipGroup)).next();
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

            long end = System.currentTimeMillis();

            log.info("Loading time (milliseconds): " + (end - start));
            log.info(labels.toString());

            graph.close();
            graph = null;
        } catch (MissingOptionException e) {
            System.out.println("MissingOptionException: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("SnomedTermGraphLoader", options);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            if (graph != null) {
                graph.close();
            }
        }
    }

    private static Set<String> getLabels(JanusGraph janusGraph) {
        Set<String> labels = new LinkedHashSet<>();
        JanusGraphManagement management = janusGraph.openManagement();
        for (EdgeLabel edgeLabel : management.getRelationTypes(EdgeLabel.class)) {
            labels.add(edgeLabel.label());
        }
        management.rollback();
        return labels;
    }

    private static String toLabel(String typeName) {
        List<String> tokens = Arrays.asList(typeName.split(" "));
        String label = tokens.stream()
                .map(token -> token.substring(0, 1).toUpperCase() + token.substring(1))
                .collect(Collectors.joining(""));
        label = label.substring(0, 1).toLowerCase() + label.substring(1);
        return "property".equals(label) ? "property_" : label;
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
}
