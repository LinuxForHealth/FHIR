/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.test;

import static com.ibm.fhir.term.util.CodeSystemSupport.toObject;

import java.util.stream.Stream;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.filter.TimeLimitStep;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.factory.FHIRTermGraphFactory;

public class FHIRTermGraphTest {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void main(String[] args) throws Exception {
        FHIRTermGraph graph = FHIRTermGraphFactory.open("conf/janusgraph-berkeleyje-lucene.properties");

        GraphTraversalSource g = graph.traversal();

        g.V().drop().iterate();

        Vertex v1 = g.addV("Concept").property("code", "a").next();
        System.out.println(v1.id());
        Vertex v2 = g.addV("Concept").property("code", "b").next();
        System.out.println(v2.id());
        Vertex v3 = g.addV("Concept").property("code", "c").next();
        System.out.println(v3.id());
        Vertex v4 = g.addV("Property_").next();
        System.out.println(v4.id());

        g.V(v1).addE(FHIRTermGraph.IS_A).from(v2).next();
        g.V(v2).addE(FHIRTermGraph.IS_A).from(v3).next();

        g.V(v4).property("valueCode", toObject(Code.of("someCode"))).next();
        g.V(v4).property("valueDecimal", toObject(Decimal.of(100.0))).next();
        g.V(v4).property("valueInteger", toObject(com.ibm.fhir.model.type.Integer.of(0))).next();

        g.tx().commit();

        System.out.println(g.V().has("valueCode", toObject(Code.of("someCode"))).hasNext());
        System.out.println(g.V().has("valueDecimal", toObject(Decimal.of(100))).hasNext());
        System.out.println(g.V().has("valueInteger", toObject(com.ibm.fhir.model.type.Integer.of(-0))).hasNext());
        System.out.println(Integer.valueOf(0).equals(Integer.valueOf(-0)));

        System.out.println("");

        // Descendants of 'a' and self (using stream concatenation)
        Stream.concat(
            g.V(v1)
                .elementMap()
                .toStream(),
            g.V(v1)
                .repeat(__.in(FHIRTermGraph.IS_A).simplePath())
                .emit()
                .elementMap()
                .toStream())
        .forEach(System.out::println);

        System.out.println("");

        // Descendants of 'a' and self
        g.V().has("code", "a").union(__.identity(), g.V().has("code", "a")
                .repeat(__.in("isa")
                    .simplePath()
                    .dedup())
                .emit())
            .elementMap()
            .toStream()
            .forEach(System.out::println);

        System.out.println("");

        // Descendants of 'b' and self
        g.V().has("code", "b").union(__.identity(),
            g.V().has("code", "b")
                .repeat(__.in("isa")
                    .simplePath()
                    .dedup())
                .emit())
            .elementMap()
            .toStream()
            .forEach(System.out::println);

        System.out.println("");

        // Union of 'a', 'b', and 'c'
        g.V().has("code", "a").union(__.identity(),
                g.V().has("code", "b"),
                g.V().has("code", "c"))
            .elementMap()
            .toStream()
            .forEach(System.out::println);

        System.out.println("");

        // Descendants of 'b'
        g.V().filter(__.repeat(__.out("isa")).until(__.has("code", "b"))).elementMap().toStream().forEach(System.out::println);

        System.out.println("");

        // Not descendants of 'b'
        GraphTraversal<Vertex, Vertex> graphTraversal = g.V();

        graphTraversal = graphTraversal.timeLimit(100L);

        TimeLimitStep timeLimitStep = (TimeLimitStep) graphTraversal.asAdmin().getEndStep();

        graphTraversal.not(__.repeat(__.out("isa")).until(__.has("code", "b"))).hasLabel("Concept").elementMap().toStream().forEach(System.out::println);

        System.out.println(timeLimitStep.getTimedOut());

        System.out.println("");

        // Not descendants of 'b' or self
        g.V().not(__.repeat(__.out("isa")).until(__.has("code", "b"))).not(__.has("code", "b")).hasLabel("Concept").elementMap().toStream().forEach(System.out::println);

        System.out.println("");

        g.V().not(__.until(__.has("code", "b")).repeat((GraphTraversal) __.out("isa"))).hasLabel("Concept").elementMap().toStream().forEach(System.out::println);

        System.out.println("");

        g.V().not(__.out("property_").has("code", "someCode")).hasLabel("Concept").elementMap().toStream().forEach(System.out::println);

        graph.close();
    }
}
