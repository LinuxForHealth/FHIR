/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.loader.impl;

import static org.linuxforhealth.fhir.term.graph.loader.util.FHIRTermGraphLoaderUtil.toMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.schema.JanusGraphManagement;

import org.linuxforhealth.fhir.term.graph.loader.FHIRTermGraphLoader;
import org.linuxforhealth.fhir.term.graph.loader.util.COSObject;
import org.linuxforhealth.fhir.term.graph.loader.util.ConfigLoader;

/*
 * This class will load edges between Snomed codes and ICD10 codes based on the UMLS into a JanusGraph.
 */
public class SnomedICD10MapTermGraphLoader extends AbstractTermGraphLoader {
    private static final Logger LOG = Logger.getLogger(SnomedICD10MapTermGraphLoader.class.getName());

    public static final String MAPS_TO = "mapsTo";

    private static final String SNOMED_TO_ICD_MAP_FILE = "der2_iisssccRefset_ExtendedMapFull_US1000124_20210901.txt";

    private static final String UMLS_DELIMITER = "\t";

    private String cosBucketName = null;

    /**
     * Load the Snomed->ICD map from file, respecting effectiveDate and active status
     * 
     * @param cosBucketName
     * @return a map from Snomed code to ICD10 code(s)
     * @throws IOException
     */
    public static final Map<String,Set<String>> loadMap(String cosBucketName) throws IOException {
        // For a given Snomed code, find the most recent active row. If the rule is
        // always map to a single ICD code, add an edge. If not, skip that Snomed code.

        Map<String, String> snomedToDateMap = new HashMap<>(); // Snomed->ICD, date
        Map<String, Set<String>> snomedToICDMap = new HashMap<>(); // Snomed->ICD, date

        final AtomicInteger rowCount = new AtomicInteger(0);
        try (BufferedReader reader = new BufferedReader(COSObject.getItem(cosBucketName, SNOMED_TO_ICD_MAP_FILE))) {
            reader.lines().forEach(line -> {
                if (rowCount.incrementAndGet() % 100000 == 0) {
                    LOG.info("Row Count: " + rowCount.get());
                }
                
                if (line == null) {
                    return;
                }
                String[] tokens = line.split(UMLS_DELIMITER);

                if (tokens.length < 11) {
                    // Expect at least 11 tokens per valid row of data
                    return;
                }
                String active = tokens[2];
                if (!active.equals("1")) { // Skip inactive rows
                    return;
                }
                String effectiveTime = tokens[1];
                String snomed = tokens[5];

                String curEffectiveTime = null;
                if (snomedToDateMap.containsKey(snomed)) {
                    curEffectiveTime = snomedToDateMap.get(snomed);
                }
                if (curEffectiveTime != null && curEffectiveTime.compareTo(effectiveTime) > 0) {
                            // Only look at the most recent effectiveTime values for a given Snomed code
                    return;
                }
                if (curEffectiveTime!=null && !effectiveTime.equals(curEffectiveTime)) {
                    snomedToICDMap.remove(snomed);
                }
                snomedToDateMap.put(snomed, effectiveTime);

                String mapRule = tokens[8];
                if (!Boolean.parseBoolean(mapRule)) {
                    return;
                }
                String advice = tokens[9];
                String icd = tokens[10];
                if (!advice.equals("ALWAYS " + icd)) {
                    // Only support map rows where the rules are ALWAYS mapping 
                    return;
                }
                Set<String> icds = snomedToICDMap.computeIfAbsent(snomed, s -> new HashSet<>());
                icds.add(icd);
            });
        }
        return snomedToICDMap;
    }

    /**
     * Initialize a SnoMedICD10MapTermGraphLoader
     *
     * @param options
     * @param configuration
     */
    public SnomedICD10MapTermGraphLoader(Map<String, String> options, Configuration configuration) {
        super(options, configuration);
        cosBucketName = System.getenv(UMLSTermGraphLoader.COS_BUCKET_NAME);
    }
   

    /**
     * Loads edges into JanusGraph
     *
     * @throws RuntimeException
     */
    @Override
    public void load() {
        try {
            LOG.info("Loading map.....");

            if (janusGraph.getEdgeLabel(MAPS_TO) == null) {
                LOG.info("Adding label: " + MAPS_TO);
                JanusGraphManagement management = janusGraph.openManagement();
                management.makeEdgeLabel(MAPS_TO).make();
                management.commit();
            }

            Map<String, Set<String>> snomedToICDMap = loadMap(cosBucketName); 
            
            LOG.info("Loading " + snomedToICDMap.size() + " edges into TermGraph");
            AtomicInteger edgeCount = new AtomicInteger(0);
            snomedToICDMap.forEach((snomed, icds) -> {
                List<Vertex> snomedConcepts = g.V().hasLabel("Concept").has("code", snomed).toList();
                icds.forEach(icd -> {
                    List<Vertex> icdConcepts = g.V().hasLabel("Concept").has("code", icd).toList();

                    for (Vertex snomedConcept : snomedConcepts) {
                        for (Vertex icdConcept : icdConcepts) {
                            g.V(icdConcept).addE(MAPS_TO).to(snomedConcept).next();
                            edgeCount.incrementAndGet();
                        }
                    }

                    if ((edgeCount.get() % Math.floor(snomedToICDMap.size() / 10)) == 0) {
                        LOG.info("Committed edges: " + edgeCount.get() + "/" + snomedToICDMap.size());
                        g.tx().commit();
                    }
                });
            });

            // commit any uncommitted work
            g.tx().commit();
            LOG.info("Committed Edges: " + edgeCount.get() + "/" + snomedToICDMap.size());
            LOG.info("Done loading Snomed to ICD10 map.....");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load UMLS data using properties provided in arguments
     *
     * @param args
     */
    public static void main(String[] args) {
        SnomedICD10MapTermGraphLoader loader = null;
        Options options = null;
        try {
            long start = System.currentTimeMillis();

            options = FHIRTermGraphLoader.Type.UMLS.options();

            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);
            Map<String, String> commandLineMap = toMap(commandLine);

            String propFileName = commandLineMap.get("config");
            Configuration configuration = ConfigLoader.load(propFileName);
            
            loader = new SnomedICD10MapTermGraphLoader(toMap(commandLine), configuration);
            loader.load();

            long end = System.currentTimeMillis();
            LOG.info("Loading time (milliseconds): " + (end - start));
        } catch (MissingOptionException e) {
            LOG.log(Level.SEVERE, "MissingOptionException: ", e);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("UMLSTermGraphLoader", options);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "An error occurred: " + e.getMessage());
        } finally {
            if (loader != null) {
                loader.close();
            }
        }
    }
}