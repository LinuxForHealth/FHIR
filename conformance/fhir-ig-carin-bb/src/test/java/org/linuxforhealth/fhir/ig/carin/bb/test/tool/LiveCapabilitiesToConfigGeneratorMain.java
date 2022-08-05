/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.carin.bb.test.tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.CapabilityStatement;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.visitor.DefaultVisitor;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * This class processes the Live CapabilityStatements to fhir-server-config.json.
 * The output is to the sysout, and the user is expected to specify default versions.
 *
 * java LiveCapabilitiesToConfigGeneratorMain http://hl7.org/fhir/us/core/|3.1.1,http://hl7.org/fhir/us/carin-bb/|1.0.0
 */
public class LiveCapabilitiesToConfigGeneratorMain {

    private static final JsonBuilderFactory JSON_BUILDER_FACTORY = Json.createBuilderFactory(null);
    private List<String> include = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        LiveCapabilitiesToConfigGeneratorMain main = new LiveCapabilitiesToConfigGeneratorMain();
        main.generateFilters(args);

        CapabilityStatement statement = FHIRParser.parser(Format.JSON).parse(main.readMetadata()).as(org.linuxforhealth.fhir.model.resource.CapabilityStatement.class);
        JsonObject object = main.adaptCapabilityStatement(statement);
        System.out.println(object);
    }

    public void generateFilters(String[] args) {
        if (args.length != 1) {
           throw new IllegalArgumentException("Args not included to filter the right versions of the profiles");
        }
        for (String pattern : args[0].split(",")) {
            include.add(pattern);
        }
    }

    /**
     * Trusts the access to the given URL
     */
    private class AllTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // NOP
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         // NOP
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }

    }

    /**
     * Creates a local factory to Trust All
     * @return
     * @throws Exception
     */
    private SSLSocketFactory createFactory() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{new AllTrustManager()} , null);
        return sslContext.getSocketFactory();
    }

    /**
     * Read metadata (CapabilityStatement)
     * @return
     * @throws Exception
     */
    public BufferedReader readMetadata() throws Exception {
        URL url = new URL("https://localhost:9443/fhir-server/api/v4/metadata");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new HostnameVerifier() {
            // Ignore Hostname checks
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        conn.setSSLSocketFactory(createFactory());
        return new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }

    /**
     * Runs through the CapabilityStatement
     * @param statement
     * @return
     */
    public JsonObject adaptCapabilityStatement(CapabilityStatement statement) {
        CalculateConfigurationElements cce = new CalculateConfigurationElements(true);
        statement.accept(cce);

        JsonObjectBuilder typesBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
        for (Entry<String, List<ConfigurationResource>> entry : cce.cces.entrySet()) {
            JsonObjectBuilder defaultsBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();

            boolean f = false;
            String resource = entry.getKey();
            for (ConfigurationResource cr : entry.getValue()) {
                for (String in: include) {
                    System.out.println(in);
                    String pre = in.split("\\|")[0];
                    String ver = in.split("\\|")[1];
                    if (cr.version.equals(ver) && cr.url.startsWith(pre)) {
                        defaultsBuilder.add(cr.url, cr.version);
                        f = true;
                    }
                }
            }

            if (f) {
                JsonObjectBuilder profilesBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
                profilesBuilder.add("defaultVersions", defaultsBuilder);

                JsonObjectBuilder typeBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
                typeBuilder.add("profiles", profilesBuilder);

                typesBuilder.add(resource, typeBuilder);
            }
        }

        JsonObjectBuilder rBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
        rBuilder.add("resources", typesBuilder);

        JsonObjectBuilder fsBuilder = JSON_BUILDER_FACTORY.createObjectBuilder();
        fsBuilder.add("fhirServer", rBuilder);

        return fsBuilder.build();
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

        Map<String, List<ConfigurationResource>> cces = new HashMap<>();

        public CalculateConfigurationElements(boolean visitChildren) {
            super(visitChildren);
        }

        @Override
        public boolean visit(String elementName, int elementIndex, BackboneElement backboneElement) {
            if ("resource".equals(elementName) && backboneElement.is(CapabilityStatement.Rest.Resource.class)) {
                CapabilityStatement.Rest.Resource ccr = backboneElement.as(CapabilityStatement.Rest.Resource.class);

                for (Canonical c : ccr.getSupportedProfile()) {
                    ConfigurationResource cce = new ConfigurationResource();
                    cce.resource = ccr.getType().getValue();
                    cce.url = c.getValue().split("\\|")[0];
                    cce.version = c.getValue().split("\\|")[1];
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