/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.locationtech.jts.util.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;
import com.ibm.fhir.term.graph.loader.FHIRTermGraphLoader;
import com.ibm.fhir.term.graph.loader.impl.CodeSystemTermGraphLoader;
import com.ibm.fhir.term.graph.provider.GraphTermServiceProvider;
import com.ibm.fhir.term.graph.util.FHIRTermGraphUtil;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;

import ch.qos.logback.classic.Level;

public class CodeSystemTermGraphLoaderTest {
    @Test
    public void testCodeSystemTermGraphLoader() throws Exception {
        FHIRTermGraphUtil.setRootLoggerLevel(Level.INFO);

        FHIRTermGraph graph = null;
        try (InputStream in = CodeSystemTermGraphLoaderTest.class.getClassLoader().getResourceAsStream("JSON/CodeSystem-cs5.json")) {
            graph = FHIRTermGraphFactory.open(new PropertiesConfiguration("conf/janusgraph-berkeleyje-lucene.properties"));
            graph.dropAllVertices();

            CodeSystem codeSystem = FHIRParser.parser(Format.JSON).parse(in);
            FHIRTermGraphLoader loader = new CodeSystemTermGraphLoader(graph, codeSystem);
            loader.load();

            FHIRTermServiceProvider provider = new GraphTermServiceProvider(graph);

            Set<Concept> expected = new LinkedHashSet<>();
            for (Concept concept : CodeSystemSupport.getConcepts(codeSystem)) {
                expected.add(concept.toBuilder()
                    .concept(Collections.emptyList())
                    .build());
            }

            Set<Concept> actual = new LinkedHashSet<>();
            for (Concept concept : provider.getConcepts(codeSystem)) {
                actual.add(provider.getConcept(codeSystem, concept.getCode()));
            }

            Assert.equals(expected, actual);
        } finally {
            if (graph != null) {
                graph.close();
            }
        }
    }
}