/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph;

import java.util.stream.Stream;

import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphIndexQuery.Result;
import org.janusgraph.core.JanusGraphVertex;

/*
 * A graph that represents FHIR CodeSystem content and is backed by a graph database (Janusgraph)
 */
public interface FHIRTermGraph {
    /**
     * The edge label that represents an is-a relationship in the graph
     */
    public static final String IS_A = "isa";

    /**
     * Get the configuration used to create this {@link FHIRTermGraph}.
     *
     * @return
     *     the configuration
     */
    Configuration configuration();

    /**
     * Get the underlying {@link JanusGraph} instance behind this {@link FHIRTermGraph}.
     *
     * @return
     *     the {@link JanusGraph} instance
     */
    JanusGraph getJanusGraph();

    /**
     * Get the graph traversal source associated with the underlying {@link JanusGraph} instance.
     *
     * @return
     *     the graph traversal source
     */
    GraphTraversalSource traversal();

    /**
     * Query the indexing backend using the <a href="https://lucene.apache.org/core/2_9_4/queryparsersyntax.html">Lucene query parser syntax</a>.
     *
     * @param query
     *     the query
     * @return
     *     results of the specified query
     */
    default Stream<Result<JanusGraphVertex>> indexQuery(String query) {
        return indexQuery(query, Integer.MAX_VALUE - 1, 0);
    }

    /**
     * Query the indexing backend using the <a href="https://lucene.apache.org/core/2_9_4/queryparsersyntax.html">Lucene query parser syntax</a>
     * and the provided limit and offset.
     *
     * @param query
     *     the query
     * @param limit
     *     the limit
     * @param offset
     *     the offset
     * @return
     *     results of the specified query using the provided limit and offset
     */
    Stream<Result<JanusGraphVertex>> indexQuery(String query, int limit, int offset);

    /**
     * Close the graph and its underlying resources.
     */
    void close();

    /**
     * Drop the graph.
     */
    void drop();

    /**
     * Drop all vertices and edges from the graph.
     */
    void dropAllVertices();
}
