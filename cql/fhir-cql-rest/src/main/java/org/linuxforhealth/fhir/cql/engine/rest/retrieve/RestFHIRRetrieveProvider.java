/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.cql.engine.rest.retrieve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.client.FHIRClient;
import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.cql.engine.retrieve.SearchParameterFHIRRetrieveProvider;
import org.linuxforhealth.fhir.cql.engine.retrieve.SearchParameterMap;
import org.linuxforhealth.fhir.cql.engine.searchparam.IQueryParameter;
import org.linuxforhealth.fhir.cql.engine.searchparam.SearchParameterResolver;
import org.linuxforhealth.fhir.cql.engine.util.FHIRClientUtil;
import org.linuxforhealth.fhir.cql.helpers.FHIRBundleCursor;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ModelSupport;

/**
 * This is an implementation of a retrieve provider for the CQL Engine that uses
 * the IBM FHIR Server REST Client to access data.
 */
public class RestFHIRRetrieveProvider extends SearchParameterFHIRRetrieveProvider {

    private FHIRClient fhirClient;

    public RestFHIRRetrieveProvider(SearchParameterResolver searchParameterResolver, FHIRClient fhirClient) {
        super(searchParameterResolver);
        this.fhirClient = fhirClient;
    }

    @Override
    protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) throws Exception {
        if (queries == null || queries.isEmpty()) {
            return Collections.emptyList();
        }

        List<Resource> resources = new ArrayList<>();
        for (SearchParameterMap query : queries) {
            Resource resource = executeQuery(dataType, query);
            resources.add(resource);
        }

        List<Object> results = new ArrayList<>();
        resources.stream().forEach(resource -> {
            if (resource instanceof Bundle) {
                FHIRBundleCursor cursor = new FHIRBundleCursor(url -> {
                    try {
                        Response response = fhirClient.getWebTarget().path(url).request().get();
                        FHIRClientUtil.handleErrorResponse(response);
                        return response.readEntity(Bundle.class);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }, (Bundle) resource);
                
                Iterator<?> it = cursor.iterator();
                while(it.hasNext()) {
                    results.add(it.next());
                }
            } else {
                results.add(resource);
            }
        });

        return results;
    }

    protected Resource executeQuery(String dataType, SearchParameterMap map) throws Exception {
        Resource result = null;

        FHIRParameters query = getFHIRParameters(map);

        if (query.getParameterMap().containsKey("_id")) {
            FHIRResponse response = fhirClient.read(dataType, query.getParameterMap().getFirst("_id"));
            FHIRClientUtil.handleErrorResponse(response);

            Class<? extends Resource> clazz = ModelSupport.getResourceType(dataType);
            result = response.getResource(clazz);
        } else {
            FHIRResponse response = fhirClient.search(dataType, query);
            FHIRClientUtil.handleErrorResponse(response);
            result = response.getResource(Bundle.class);
        }

        return result;
    }

    protected FHIRParameters getFHIRParameters(SearchParameterMap map) {
        FHIRParameters result = new FHIRParameters();

        Map<String, List<String>> parameters = getParameterValueMap(map);

        for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
            result.searchParam(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
        }

        if (map.count() != null) {
            result.count(map.count());
        }

        return result;
    }

    private Map<String, List<String>> getParameterValueMap(SearchParameterMap map) {
        Map<String, List<String>> parameters = new HashMap<>();
        for (Map.Entry<String, List<List<? extends IQueryParameter>>> entry : map.entrySet()) {
            for (List<? extends IQueryParameter> sublist : entry.getValue()) {
                for (IQueryParameter p : sublist) {
                    String name = getModifiedName(entry.getKey(), p);

                    List<String> values = parameters.computeIfAbsent(name, k -> new ArrayList<>());
                    values.add(FHIRClientUtil.urlencode(p.getParameterValue()));
                }
            }
        }
        return parameters;
    }
}
