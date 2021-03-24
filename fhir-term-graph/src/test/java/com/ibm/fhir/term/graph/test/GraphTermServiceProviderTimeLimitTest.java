/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import static org.testng.Assert.fail;

import java.io.InputStream;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;
import com.ibm.fhir.term.graph.loader.FHIRTermGraphLoader;
import com.ibm.fhir.term.graph.loader.impl.CodeSystemTermGraphLoader;
import com.ibm.fhir.term.graph.provider.GraphTermServiceProvider;
import com.ibm.fhir.term.graph.util.FHIRTermGraphUtil;
import com.ibm.fhir.term.service.exception.FHIRTermServiceException;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

import ch.qos.logback.classic.Level;

public class GraphTermServiceProviderTimeLimitTest {
    @Test
    public void testGraphTermServiceProviderTimeLimit() throws Exception {
        FHIRTermGraphUtil.setRootLoggerLevel(Level.INFO);

        FHIRTermGraph graph = null;
        try (InputStream in = GraphTermServiceProviderTimeLimitTest.class.getClassLoader().getResourceAsStream("JSON/CodeSystem-test.json")) {
            graph = FHIRTermGraphFactory.open(new PropertiesConfiguration("conf/janusgraph-berkeleyje-lucene.properties"));
            graph.dropAllVertices();

            CodeSystem codeSystem = FHIRParser.parser(Format.JSON).parse(in);
            FHIRTermGraphLoader loader = new CodeSystemTermGraphLoader(graph, codeSystem);
            loader.load();

            FHIRTermServiceProvider provider = new GraphTermServiceProvider(graph, 1);

            provider.getConcepts(codeSystem);

            fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof FHIRTermServiceException);
            Assert.assertEquals(e.getMessage(), "Graph traversal timed out");
        } finally {
            if (graph != null) {
                graph.close();
            }
        }
    }
}