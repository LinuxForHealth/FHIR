/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.visitor.DefaultVisitor;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 *This class processes the CapabilityStatements in the ImplementationGuide to fhir-server-config.json.
 * The output is to the sysout, and the user is expected to select between the versions processed.
 */
public class CapabilitiesToConfigGeneratorMain {
    private static final JsonBuilderFactory JSON_BUILDER_FACTORY = Json.createBuilderFactory(null);

    /**
     * List of the Versions
     * @return
     */
    public String[] versions() {
        return new String[] { "311", "400", "500" };
    }

    public JsonObject adaptCapabilityStatement(CapabilityStatement statement, String version) {
        CalculateConfigurationElements cce = new CalculateConfigurationElements(true, version);
        statement.accept(cce);

        JsonObjectBuilder typesBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
        for (Entry<String, List<ConfigurationResource>> entry : cce.cces.entrySet()) {
            JsonObjectBuilder defaultsBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
            String resource = null;

            for (ConfigurationResource cr : entry.getValue()) {
                String uri = cr.url.split("\\|")[0];
                if ("311".equals(cr.version)) {
                    defaultsBuilder.add(uri, "3.1.1");
                } else {
                    defaultsBuilder.add(uri, "4.0.0");
                }
                resource = entry.getKey();
            }

            JsonObjectBuilder profilesBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
            profilesBuilder.add("defaultVersions", defaultsBuilder);

            JsonObjectBuilder typeBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
            typeBuilder.add("profiles", profilesBuilder);


            typesBuilder.add(resource, typeBuilder);


        }

        JsonObjectBuilder rBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
        rBuilder.add("resources", typesBuilder);

        JsonObjectBuilder fsBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
        fsBuilder.add("fhirServer", rBuilder);

        return fsBuilder.build();
    }

    public static void main(String[] args) throws Exception {
        CapabilitiesToConfigGeneratorMain main = new CapabilitiesToConfigGeneratorMain();

        for (String version : main.versions()) {
            File file = new File("src/main/resources/hl7/fhir/us/core/" + version + "/package/CapabilityStatement-us-core-server.json");

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                CapabilityStatement statement = FHIRParser.parser(Format.JSON).parse(reader).as(com.ibm.fhir.model.resource.CapabilityStatement.class);
                JsonObject object = main.adaptCapabilityStatement(statement, version);
                System.out.println(object);
            }

        }
    }

    /**
     * Simple data transfer object
     */
    private static class ConfigurationResource {
        String resource;
        String version;
        String url;

        @Override
        public String toString() {
            return "ConfigurationResource [resource=" + resource + ", version=" + version + ", url=" + url + "]";
        }
    }

    /**
     * Visitor processes through the Capability Statement
     */
    private static class CalculateConfigurationElements extends DefaultVisitor {

        private String version;
        Map<String, List<ConfigurationResource>> cces = new HashMap<>();

        public CalculateConfigurationElements(boolean visitChildren, String version) {
            super(visitChildren);
            this.version = version;
        }

        @Override
        public boolean visit(String elementName, int elementIndex, BackboneElement backboneElement) {
            if ("resource".equals(elementName) && backboneElement.is(CapabilityStatement.Rest.Resource.class)) {
                CapabilityStatement.Rest.Resource ccr = backboneElement.as(CapabilityStatement.Rest.Resource.class);

                for (Canonical c : ccr.getSupportedProfile()) {
                    ConfigurationResource cce = new ConfigurationResource();
                    cce.resource = ccr.getType().getValue();
                    cce.url = c.getValue();
                    cce.version = version;
                    if (!cces.containsKey(cce.resource)) {
                        cces.put(cce.resource, new ArrayList<>(Arrays.asList(cce)));
                    } else {
                        cces.get(cce.resource).add(cce);
                    }
                }
                return false;
            }
            return super.visit(elementName, elementIndex, backboneElement);
        }
    }
}