/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import java.util.stream.Stream;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.FHIRTermGraphFactory;

public class FHIRTermGraphTest {
    public static void main(String[] args) throws Exception {
        FHIRTermGraph graph = FHIRTermGraphFactory.open("conf/janusgraph-berkeleyje-lucene.properties");

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

        g.V(v1).property("valueCode", Code.of("someCode")).next();
        g.V(v1).property("valueDecimal", Decimal.of(100.0)).next();
        g.V(v1).property("valueInteger", com.ibm.fhir.model.type.Integer.of(0)).next();

        g.tx().commit();

        System.out.println(g.V().has("valueCode", Code.of("someCode")).hasNext());
        System.out.println(g.V().has("valueDecimal", Decimal.of(100)).hasNext());
        System.out.println(g.V().has("valueInteger", com.ibm.fhir.model.type.Integer.of(-0)).hasNext());

        System.out.println(Integer.valueOf(0).equals(Integer.valueOf(-0)));

        graph.close();
    }
}
