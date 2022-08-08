/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.us.core.tool;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource;

/**
 * Tool for expanding ValueSets from the VSAC Terminology Server
 */
public class VSACRegistryResource extends FHIRRegistryResource {
    private static final String CANONICAL_BASE = "http://cts.nlm.nih.gov/fhir/ValueSet/";
    private static final String REQUEST_BASE = "https://cts.nlm.nih.gov/fhir/ValueSet/";

    private final Client client;

    public VSACRegistryResource(String id, String url, Version version, Client client) {
        super(ValueSet.class, id, url, version, null, null);
        if (!url.startsWith(CANONICAL_BASE)) {
            throw new IllegalArgumentException("Expected URL to begin with " + CANONICAL_BASE
                    + " but found '" + url + "'");
        }
        this.client = client;
    }

    /**
     * @throws RuntimeException if there was a problem retrieving the expanded ValueSet from VSAC
     */
    @Override
    public Resource getResource() {
        String requestPath = url.substring(CANONICAL_BASE.length()) + "/$expand";

        Response response = client.target(REQUEST_BASE)
                .path(requestPath)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();

        if (response.getStatus() == 200) {
            return response.readEntity(ValueSet.class);
        } else {
            throw new RuntimeException("Unexpected " + response.getStatus() + " response while invoking " +
                    requestPath + ": " + response.readEntity(String.class));
        }
    }
}
