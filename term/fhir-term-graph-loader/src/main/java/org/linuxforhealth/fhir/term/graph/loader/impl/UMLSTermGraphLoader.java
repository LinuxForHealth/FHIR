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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.schema.JanusGraphManagement;

import org.linuxforhealth.fhir.term.graph.loader.FHIRTermGraphLoader;
import org.linuxforhealth.fhir.term.graph.loader.util.COSObject;
import org.linuxforhealth.fhir.term.graph.loader.util.ConfigLoader;

/*
 * This class will load UMLS concepts and relationships into a JanusGraph.
 */
public class UMLSTermGraphLoader extends AbstractTermGraphLoader {
    private static final Logger LOG = Logger.getLogger(UMLSTermGraphLoader.class.getName());

    private static final String UMLS_CONCEPT_NAMES_AND_SOURCES_FILE = "MRCONSO.RRF";
    private static final String UMLS_SOURCE_INFORMATION_FILE = "MRSAB.RRF";
    private static final String UMLS_RELATED_CONCEPTS_FILE = "MRREL.RRF";
    private static final String UMLS_DELIMITER = "\\|";
    public static final String COS_BUCKET_NAME = "TERM_COS_BUCKET_NAME";

    // Map to track AUI to SCUI relationships, since MRREL uses AUI, but granularity of concepts used in MRCONSO is at SCUI/CODE level
    private Map<String, String> auiToCodeMap = new ConcurrentHashMap<>(1000000);

    // Map of code system name to preferred label, configured in properties file
    private Properties codeSystemMap = new Properties();

    // Map of code system id to corresponding vertex
    private Map<String, Vertex> codeSystemVertices = new ConcurrentHashMap<>();

    // Map of abbreviated source name to the current version of that source
    private Map<String, String> sabToVersion = new HashMap<>();

    // Map of concept name to corresponding vertex
    private Map<String, Vertex> vertexMap = null;

    private String cosBucketName = null;

    public UMLSTermGraphLoader(Map<String, String> options) {
        super(options);
        cosBucketName = System.getenv(COS_BUCKET_NAME);
        vertexMap = new HashMap<>(250000);
    }

    /**
     * Initialize a UMLSTermGraphLoader
     *
     * @param options
     */
    public UMLSTermGraphLoader(Map<String, String> options, Configuration configuration) {
        super(options, configuration);
        cosBucketName = System.getenv(COS_BUCKET_NAME);
        vertexMap = new HashMap<>(250000);
    }

    /**
     * Loads UMLS data into JanusGraph
     *
     * @throws RuntimeException
     */
    @Override
    public void load() {
        try {
            loadSourceAttributes();
            loadConcepts();
            loadRelations();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a code system vertex for the provided abbreviated source name
     *
     * @param sab
     * @return
     */
    private final Vertex createCodeSystemVertex(String sab) {
        String version = sabToVersion.get(sab);
        String url = (String) codeSystemMap.getOrDefault(sab, sab);
        Vertex csv = g.addV("CodeSystem").property("url", url).property("version", version).next();
        g.tx().commit();
        return csv;

    }

    /**
     * Loads all UMLS concept data from the provided conceptFile
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void loadConcepts() throws FileNotFoundException, IOException {
        // MRCONSO.RRF
        // CUI, LAT, TS, LUI, STT, SUI, ISPREF, AUI, SAUI, SCUI, SDUI, SAB, TTY, CODE, STR, SRL, SUPPRESS, CVF
        // https://www.ncbi.nlm.nih.gov/books/NBK9685/table/ch03.T.concept_names_and_sources_file_mr/
        //
        LOG.info("Loading concepts.....");

        Map<String, AtomicInteger> sabCounterMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(COSObject.getItem(cosBucketName, UMLS_CONCEPT_NAMES_AND_SOURCES_FILE))) {
            reader.lines().forEach(line -> {
                if (line == null) {
                    return;
                }
                String[] tokens = line.split(UMLS_DELIMITER);
                if (tokens.length < 18) {
                    // Expect 18 tokens per valid line
                    return;
                }
                String lat = tokens[1];
                String aui = tokens[7];
                String scui = tokens[9];
                String sab = tokens[11];
                String tty = tokens[12];
                String str = tokens[14];
                String suppress = tokens[16];

                String code = scui;
                if (code == null || code.trim().length() == 0) {
                    code = tokens[13];
                }

                if (!"O".equals(suppress)) {
                    auiToCodeMap.put(aui, code);

                    Vertex codeSystemVertex = codeSystemVertices.computeIfAbsent(sab, s -> createCodeSystemVertex(s));

                    AtomicInteger counter = sabCounterMap.computeIfAbsent(sab, s -> new AtomicInteger(0));
                    counter.incrementAndGet();

                    Vertex v = null;
                    if (vertexMap.containsKey(code)) {
                        v = vertexMap.get(code);
                    } else {
                        String codeLowerCase = normalize(code);
                        v = g.addV("Concept").property("code", code).property("codeLowerCase", codeLowerCase).next();
                        vertexMap.put(code, v);

                        g.V(codeSystemVertex).addE("concept").to(v).next();
                    }
                    if (v == null) {
                        LOG.severe("Could not find SCUI in vertexMap");
                    } else {
                        if (tty.equals("PT")) { // Preferred entries provide preferred name and language
                            v.property("display", str);
                            v.property("language", lat);
                        }
                        // add new designation
                        Vertex w = g.addV("Designation").property("language", lat).property("value", str).next();
                        g.V(v).addE("designation").to(w).next();

                        if ((sabCounterMap.values().stream().collect(Collectors.summingInt(AtomicInteger::get)) % 10000) == 0) {
                            String counters = sabCounterMap.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(","));
                            LOG.info("counter: " + counters);
                            g.tx().commit();
                        }

                    }
                }
            });

            for (Entry<String, AtomicInteger> entry : sabCounterMap.entrySet()) {
                Vertex codeSystemVertex = codeSystemVertices.get(entry.getKey());
                g.V(codeSystemVertex).property("count", entry.getValue().get()).next();
            }
            // commit any uncommitted work
            g.tx().commit();
        }

        g.tx().commit();
        LOG.info("Done loading concepts.....");
    }

    /**
     * Loads all UMLS relationship data from the provided relationshipFile
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void loadRelations() throws FileNotFoundException, IOException {
        // MRREL
        // CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RELA, RUI, SRUI, SAB, SL, RG, DIR, SUPPRESS, CVF
        // https://www.ncbi.nlm.nih.gov/books/NBK9685/table/ch03.T.related_concepts_file_mrrel_rrf/
        //
        LOG.info("Loading relations.....");

        AtomicInteger counter = new AtomicInteger(0);

        try (BufferedReader reader = new BufferedReader(COSObject.getItem(cosBucketName, UMLS_RELATED_CONCEPTS_FILE))) {
            reader.lines().forEach(line -> {
                if (line == null) {
                    return;
                }
                String[] tokens = line.split(UMLS_DELIMITER);
                if (tokens.length < 16) {
                    // Expect 16 tokens per valid line
                    return;
                }
                String aui1 = tokens[1];
                String rel = tokens[3];
                String rela = tokens[7];
                String aui2 = tokens[5];
                String rg = tokens[12]; // relationship group
                String dir = tokens[13];
                String suppress = tokens[14];

                if (!"N".equals(dir) && !"O".equals(suppress)) { // Don't load relations that are not in source order or suppressed
                    String code1 = auiToCodeMap.get(aui1);
                    String code2 = auiToCodeMap.get(aui2);

                    if (code1 != null && code2 != null) {
                        Vertex v1 = vertexMap.get(code1);
                        Vertex v2 = vertexMap.get(code2);

                        if (v1 != null && v2 != null) {
                            String label = null;
                            if (rela != null && !rela.isEmpty()) {
                                label = toLabel(rela);
                            }
                            if (label == null || label.isEmpty()) {
                                label = rel;
                            }
                            if (labelFilter.accept(label)) {
                                if (janusGraph.getEdgeLabel(label) == null) {
                                    LOG.info("Adding label: " + label);
                                    JanusGraphManagement management = janusGraph.openManagement();
                                    management.makeEdgeLabel(label).make();
                                    management.commit();
                                }

                                Edge e = g.V(v2).addE(label).to(v1).next();

                                if (!"".equals(rg)) {
                                    g.E(e).property("group", rg).next();
                                }
                            }
                        }

                        if ((counter.get() % 10000) == 0) {
                            LOG.info("counter: " + counter.get());
                            g.tx().commit();
                        }

                        counter.getAndIncrement();
                    }
                }
            });

            // commit any uncommitted work
            g.tx().commit();
            LOG.info("Done loading relations.....");
        }
    }

    /**
     * Loads all UMLS source attribute data from the provided sourceAttributeFile
     *
     * @throws IOException
     */
    private void loadSourceAttributes() throws IOException {
        try (Reader codeSystemReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("conf/umlsCodesystemMap.properties"))) {
            // Load UMLS name to preferred CodeSystem name map
            codeSystemMap.load(codeSystemReader);
        }

        try (BufferedReader reader = new BufferedReader(COSObject.getItem(cosBucketName, UMLS_SOURCE_INFORMATION_FILE))) {
            // Load latest version for code systems in UMLS
            reader.lines().forEach(line -> {
                if (line==null) {
                    return;
                }
                String[] tokens = line.split(UMLS_DELIMITER);
                if (tokens.length < 25) {
                    // Expect 25 tokens per valid line
                    return;
                }
                String rsab = tokens[3];
                String sver = tokens[6];
                String curver = tokens[21];

                if ("Y".equals(curver)) {
                    if ("SNOMEDCT_US".equals(rsab)) {
                        // special case version for SNOMED
                        sver = "http://snomed.info/sct/731000124108/version/" + sver.replaceAll("_", "");
                    }
                    sabToVersion.put(rsab, sver);
                }
            });
        }
    }

    /**
     * Load UMLS data using properties provided in arguments
     *
     * @param args
     */
    public static void main(String[] args) {
        UMLSTermGraphLoader loader = null;
        Options options = null;
        try {
            long start = System.currentTimeMillis();

            options = FHIRTermGraphLoader.Type.UMLS.options();

            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);
            Map<String, String> commandLineMap = toMap(commandLine);
            String propFileName = commandLineMap.get("config");
            
            Configuration configuration = ConfigLoader.load(propFileName);
            loader = new UMLSTermGraphLoader(commandLineMap, configuration);
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