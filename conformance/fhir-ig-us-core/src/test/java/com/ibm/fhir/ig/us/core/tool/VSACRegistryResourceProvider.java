/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.tool;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.provider.FHIRJsonProvider;
import com.ibm.fhir.provider.FHIRProvider;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.registry.spi.AbstractRegistryResourceProvider;

/**
 * Provide ValueSets from the VSAC Terminology Server.
 * Used to expand US Core ValueSets that compose VSAC ones.
 */
public class VSACRegistryResourceProvider extends AbstractRegistryResourceProvider {
    // set your UMLS API Key (but don't commit it!)
    private static final String API_KEY = "";

    private Client client;
    private static final String CANONICAL_BASE = "http://cts.nlm.nih.gov/fhir/ValueSet/";
    private static final String REQUEST_BASE = "https://cts.nlm.nih.gov/fhir/ValueSet/";

    public VSACRegistryResourceProvider() {
        if (API_KEY == null || API_KEY.isEmpty()) {
            return;
        }
        try {
            ClientBuilder cb = ClientBuilder.newBuilder();
            cb.register(new FHIRProvider(RuntimeType.CLIENT));
            cb.register(new FHIRJsonProvider(RuntimeType.CLIENT));
            cb.register(new BasicAuthFilter("apikey", API_KEY));
            cb.property("http.receive.timeout", 30000); // 30,000 ms = 30 seconds

            client = cb.build();
        } catch (Exception e) {
            throw new Error("An error occured while creating RemoteTermServiceProvider client", e);
        }
    }

    @Override
    protected List<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType, String url) {
        if (client == null || ValueSet.class != resourceType || !url.startsWith(CANONICAL_BASE)) {
            return List.of();
        }

        WebTarget target = client.target(REQUEST_BASE);

        Response response = target
                .path(url.substring(CANONICAL_BASE.length()) + "/$expand")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();

        if (response.getStatus() == 200) {
            ValueSet vs = response.readEntity(ValueSet.class);
            return List.of(FHIRRegistryResource.from(vs));
        } else {
            System.err.println("Unexpected " + response.getStatus() + " response from server: ");
            System.err.println(response.getStringHeaders());
            System.err.println(response.readEntity(String.class));
            return List.of();
        }
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        if (ValueSet.class != resourceType) {
            return List.of();
        }

        Set<FHIRRegistryResource> results = new HashSet<>();

        String targetUrl = REQUEST_BASE + "?_elements=version";

        while (targetUrl != null) {
            WebTarget target = client.target(targetUrl);
            Response response = target.request(FHIRMediaType.APPLICATION_FHIR_JSON).get();

            if (response.getStatus() != 200) {
                System.err.println("Unexpected " + response.getStatus() + " response from server: ");
                System.err.println(response.getStringHeaders());
                System.err.println(response.readEntity(String.class));
                break;
            }

            Bundle bundle = response.readEntity(Bundle.class);

            for (Bundle.Entry entry : bundle.getEntry()) {
                Resource resource = entry.getResource();
                if (resource instanceof ValueSet) {
                    ValueSet vs = resource.as(ValueSet.class);
                    if (entry.getFullUrl() == null || !entry.getFullUrl().hasValue() || vs.getId() == null) {
                        System.err.println("Skipping entry that is missing either its fullUrl or a resource id: " + entry);
                        continue;
                    }
                    Version version = (vs.getVersion() == null) ? null : Version.from(vs.getVersion().getValue());

                    results.add(new VSACRegistryResource(vs.getId(), entry.getFullUrl().getValue(), version, client));
                }
            }

            targetUrl = null;
            for (Bundle.Link link : bundle.getLink()) {
                if ("next".equals(link.getRelation().getValue())) {
                    targetUrl = link.getUrl().getValue();

                    // make sure the next link starts with our REQUEST_BASE just to be safe
                    if (!targetUrl.startsWith(REQUEST_BASE)) {
                        throw new IllegalStateException("Unexpected next link from server: " + targetUrl);
                    }
                }
            }
        }

        return results;
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        return getRegistryResources(ValueSet.class);
    }

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptySet();
    }

    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        return Collections.emptySet();
    }

    private static class BasicAuthFilter implements ClientRequestFilter {
        private final String authHeaderValue;

        public BasicAuthFilter(String user, String pass) {
            authHeaderValue = buildAuthHeaderValue(user, pass);
        }

        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.getHeaders().add("Authorization", authHeaderValue);
        }

        private String buildAuthHeaderValue(String user, String pass) {
            String basicAuthToken = user + ":" + pass;
            return "Basic " + Base64.getEncoder().encodeToString(basicAuthToken.getBytes());
        }
    }
}
