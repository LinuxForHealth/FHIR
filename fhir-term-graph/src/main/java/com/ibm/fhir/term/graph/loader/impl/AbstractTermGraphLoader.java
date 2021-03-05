/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.loader.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;

import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;
import com.ibm.fhir.term.graph.loader.FHIRTermGraphLoader;
import com.ibm.fhir.term.graph.loader.util.LabelFilter;

public abstract class AbstractTermGraphLoader implements FHIRTermGraphLoader {
    protected final Map<String, String> options;
    protected final FHIRTermGraph graph;
    protected final JanusGraph janusGraph;
    protected final GraphTraversalSource g;
    protected final LabelFilter labelFilter;

    public AbstractTermGraphLoader(Map<String, String> options) {
        this.options = Objects.requireNonNull(options, "options");

        String propFileName = options.get("config");
        graph = FHIRTermGraphFactory.open(propFileName);
        janusGraph = graph.getJanusGraph();
        g = graph.traversal();

        // create label filter
        if (options.containsKey("labels")) {
            labelFilter = new LabelFilter(new HashSet<>(Arrays.asList(options.get("labels").split(","))));
        } else {
            labelFilter = LabelFilter.ACCEPT_ALL;
        }
    }

    @Override
    public abstract void load();

    @Override
    public final void close() {
        graph.close();
    }

    @Override
    public final Map<String, String> options() {
        return options;
    }
}
