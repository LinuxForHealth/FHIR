/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.impl;

import static java.util.Objects.requireNonNull;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphIndexQuery.Result;
import org.janusgraph.core.JanusGraphVertex;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;

import org.linuxforhealth.fhir.term.graph.FHIRTermGraph;

public class FHIRTermGraphImpl implements FHIRTermGraph {
    private static final Logger log = Logger.getLogger(FHIRTermGraphImpl.class.getName());

    private static final String STORAGE_READ_ONLY = "storage.read-only";

    private JanusGraph graph;
    private GraphTraversalSource traversal;

    public FHIRTermGraphImpl(Configuration configuration) {
        requireNonNull(configuration);
        try {
            graph = open(configuration);
            traversal = graph.traversal();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private JanusGraph open(Configuration configuration) {
        log.info("Opening graph...");

        boolean readOnly = configuration.getBoolean(STORAGE_READ_ONLY, false);
        configuration.setProperty(STORAGE_READ_ONLY, false);

        JanusGraph graph = JanusGraphFactory.open(configuration);

        if (!schemaExists(graph)) {
            createSchema(graph);
        }

        if (readOnly) {
            graph.close();
            configuration.setProperty(STORAGE_READ_ONLY, true);
            graph = JanusGraphFactory.open(configuration);
        }

        return graph;
    }

    private void createSchema(JanusGraph graph) {
        log.info("Creating schema...");

        JanusGraphManagement management = graph.openManagement();

        // property keys (indexed)
        PropertyKey version = management.makePropertyKey("version").dataType(String.class).make();
        PropertyKey code = management.makePropertyKey("code").dataType(String.class).make();
        PropertyKey codeLowerCase = management.makePropertyKey("codeLowerCase").dataType(String.class).make();
        PropertyKey display = management.makePropertyKey("display").dataType(String.class).make();
        PropertyKey url = management.makePropertyKey("url").dataType(String.class).make();
        PropertyKey value = management.makePropertyKey("value").dataType(String.class).make();

        PropertyKey valueBoolean = management.makePropertyKey("valueBoolean").dataType(Boolean.class).make();
        PropertyKey valueCode = management.makePropertyKey("valueCode").dataType(String.class).make();
        PropertyKey valueDateTimeLong = management.makePropertyKey("valueDateTimeLong").dataType(Long.class).make();
        PropertyKey valueDecimal = management.makePropertyKey("valueDecimal").dataType(Double.class).make();
        PropertyKey valueInteger = management.makePropertyKey("valueInteger").dataType(Integer.class).make();
        PropertyKey valueString = management.makePropertyKey("valueString").dataType(String.class).make();

        // property keys (not indexed)
        management.makePropertyKey("count").dataType(Integer.class).make();
        management.makePropertyKey("group").dataType(String.class).make();
        management.makePropertyKey("language").dataType(String.class).make();
        management.makePropertyKey("system").dataType(String.class).make();
        management.makePropertyKey("use").dataType(String.class).make();
        management.makePropertyKey("valueDateTime").dataType(String.class).make();
        management.makePropertyKey("valueDecimalString").dataType(String.class).make();

        // vertex labels
        management.makeVertexLabel("CodeSystem").make();
        management.makeVertexLabel("Concept").make();
        management.makeVertexLabel("Designation").make();
        management.makeVertexLabel("Property_").make();

        // edge labels
        management.makeEdgeLabel(FHIRTermGraph.IS_A).make();
        management.makeEdgeLabel("concept").make();
        management.makeEdgeLabel("designation").make();
        management.makeEdgeLabel("property_").make();

        // composite indexes
        management.buildIndex("byCode", Vertex.class).addKey(code).buildCompositeIndex();
        management.buildIndex("byCodeLowerCase", Vertex.class).addKey(codeLowerCase).buildCompositeIndex();
        management.buildIndex("byDisplay", Vertex.class).addKey(display).buildCompositeIndex();
        management.buildIndex("byUrl", Vertex.class).addKey(url).buildCompositeIndex();
        management.buildIndex("byUrlAndVersion", Vertex.class).addKey(url).addKey(version).buildCompositeIndex();
        management.buildIndex("byValue", Vertex.class).addKey(value).buildCompositeIndex();

        management.buildIndex("byValueBoolean", Vertex.class).addKey(valueBoolean).buildCompositeIndex();
        management.buildIndex("byValueCode", Vertex.class).addKey(valueCode).buildCompositeIndex();
        management.buildIndex("byValueDateTimeLong", Vertex.class).addKey(valueDateTimeLong).buildCompositeIndex();
        management.buildIndex("byValueDecimal", Vertex.class).addKey(valueDecimal).buildCompositeIndex();
        management.buildIndex("byValueInteger", Vertex.class).addKey(valueInteger).buildCompositeIndex();
        management.buildIndex("byValueString", Vertex.class).addKey(valueString).buildCompositeIndex();

        // mixed indexes
        management.buildIndex("vertices", Vertex.class)
            .addKey(display, Mapping.STRING.asParameter())
            .addKey(value, Mapping.STRING.asParameter())
            .addKey(valueString, Mapping.STRING.asParameter())
            .addKey(valueCode, Mapping.STRING.asParameter())
            .buildMixedIndex("search");

        log.info(System.lineSeparator() + management.printSchema());

        management.commit();
    }

    private boolean schemaExists(JanusGraph graph) {
        JanusGraphManagement management = graph.openManagement();
        if (management.getRelationTypes(RelationType.class).iterator().hasNext()) {
            management.rollback();
            return true;
        }
        return false;
    }

    @Override
    public Configuration configuration() {
        return graph.configuration();
    }

    @Override
    public JanusGraph getJanusGraph() {
        return graph;
    }

    @Override
    public GraphTraversalSource traversal() {
        return traversal;
    }

    @Override
    public Stream<Result<JanusGraphVertex>> indexQuery(String query, int limit, int offset) {
        return graph.indexQuery("vertices", query).limit(limit).offset(offset).vertexStream();
    }

    @Override
    public void close() {
        log.info("Closing graph...");
        try {
            if (traversal != null) {
                traversal.close();
            }
            if (graph != null) {
                graph.close();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occured while closing graph", e);
        } finally {
            traversal = null;
            graph = null;
        }
    }

    @Override
    public void drop() {
        log.info("Dropping graph...");
        try {
            if (traversal != null) {
                traversal.close();
            }
            if (graph != null) {
                JanusGraphFactory.drop(graph);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while dropping graph");
        } finally {
            traversal = null;
            graph = null;
        }
    }

    @Override
    public void dropAllVertices() {
        log.info("Dropping all vertices...");
        if (traversal != null) {
            traversal.V().drop().iterate();
            traversal.tx().commit();
        }
    }
}
