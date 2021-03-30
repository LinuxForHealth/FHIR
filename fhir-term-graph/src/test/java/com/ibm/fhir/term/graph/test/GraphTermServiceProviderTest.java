/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.AfterClass;

import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;
import com.ibm.fhir.term.graph.loader.FHIRTermGraphLoader;
import com.ibm.fhir.term.graph.loader.impl.CodeSystemTermGraphLoader;
import com.ibm.fhir.term.graph.provider.GraphTermServiceProvider;
import com.ibm.fhir.term.graph.util.FHIRTermGraphUtil;
import com.ibm.fhir.term.service.provider.test.FHIRTermServiceProviderTest;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

import ch.qos.logback.classic.Level;

public class GraphTermServiceProviderTest extends FHIRTermServiceProviderTest {
    private FHIRTermGraph graph = null;

    @Override
    public FHIRTermServiceProvider createProvider() throws Exception {
        FHIRTermGraphUtil.setRootLoggerLevel(Level.INFO);

        graph = FHIRTermGraphFactory.open(new PropertiesConfiguration("conf/janusgraph-berkeleyje-lucene.properties"));
        graph.dropAllVertices();

        FHIRTermGraphLoader loader = new CodeSystemTermGraphLoader(graph, codeSystem);
        loader.load();

        return new GraphTermServiceProvider(graph);
    }

    @AfterClass
    public void afterClass() {
        if (graph != null) {
            graph.close();
        }
    }
}
