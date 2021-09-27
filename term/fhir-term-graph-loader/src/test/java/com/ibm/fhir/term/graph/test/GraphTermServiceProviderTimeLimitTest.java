/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import static org.testng.Assert.fail;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;
import com.ibm.fhir.term.graph.loader.FHIRTermGraphLoader;
import com.ibm.fhir.term.graph.loader.impl.CodeSystemTermGraphLoader;
import com.ibm.fhir.term.graph.provider.GraphTermServiceProvider;
import com.ibm.fhir.term.service.exception.FHIRTermServiceException;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;

public class GraphTermServiceProviderTimeLimitTest {
    @Test
    public void testGraphTermServiceProviderTimeLimit() throws Exception {
        FHIRTermGraph graph = null;
        try {
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(new Parameters().properties().setFileName("conf/janusgraph-berkeleyje-lucene.properties"));
            graph = FHIRTermGraphFactory.open(builder.getConfiguration());
            graph.dropAllVertices();

            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://ibm.com/fhir/CodeSystem/test");
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