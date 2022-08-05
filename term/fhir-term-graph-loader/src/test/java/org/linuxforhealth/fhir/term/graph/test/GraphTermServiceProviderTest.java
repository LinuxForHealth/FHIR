/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.test;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.testng.annotations.AfterClass;

import org.linuxforhealth.fhir.term.graph.FHIRTermGraph;
import org.linuxforhealth.fhir.term.graph.factory.FHIRTermGraphFactory;
import org.linuxforhealth.fhir.term.graph.loader.FHIRTermGraphLoader;
import org.linuxforhealth.fhir.term.graph.loader.impl.CodeSystemTermGraphLoader;
import org.linuxforhealth.fhir.term.graph.provider.GraphTermServiceProvider;
import org.linuxforhealth.fhir.term.service.provider.test.FHIRTermServiceProviderTest;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;

public class GraphTermServiceProviderTest extends FHIRTermServiceProviderTest {
    private FHIRTermGraph graph = null;

    @Override
    public FHIRTermServiceProvider createProvider() throws Exception {
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().properties().setFileName("conf/janusgraph-berkeleyje-lucene.properties"));
        graph = FHIRTermGraphFactory.open(builder.getConfiguration());
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
