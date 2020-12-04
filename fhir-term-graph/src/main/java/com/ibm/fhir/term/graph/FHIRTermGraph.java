/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph;

import java.util.stream.Stream;

import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphIndexQuery.Result;
import org.janusgraph.core.JanusGraphVertex;

public interface FHIRTermGraph {
    Configuration configuration();
    JanusGraph getJanusGraph();
    GraphTraversalSource traversal();
    default Stream<Result<JanusGraphVertex>> indexQuery(String query) {
        return indexQuery(query, Integer.MAX_VALUE - 1);
    }
    Stream<Result<JanusGraphVertex>> indexQuery(String query, int limit);
    void close();
    void drop();
}
