/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import java.util.stream.Stream;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.testng.annotations.Test;

import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.FHIRTermGraphFactory;

public class FHIRTermGraphTest {
    @Test
    public void testTermGraph() throws Exception {
        FHIRTermGraph graph = FHIRTermGraphFactory.open("conf/local-graph.properties");

        GraphTraversalSource g = graph.traversal();

        g.V().drop().iterate();
        g.E().drop().iterate();

        Vertex v1 = g.addV("Concept").property("code", "a").next();
        System.out.println(v1.id());
        Vertex v2 = g.addV("Concept").property("code", "b").next();
        System.out.println(v2.id());
        Vertex v3 = g.addV("Concept").property("code", "c").next();
        System.out.println(v3.id());

        g.V(v1).addE("isA").from(v2).next();
        g.V(v2).addE("isA").from(v3).next();

        Stream.concat(
            g.V(v1)
                .elementMap()
                .toStream(),
            g.V(v1)
                .repeat(__.in("isA").simplePath())
                .emit()
                .elementMap()
                .toStream())
        .forEach(System.out::println);

        graph.close();
    }
}
