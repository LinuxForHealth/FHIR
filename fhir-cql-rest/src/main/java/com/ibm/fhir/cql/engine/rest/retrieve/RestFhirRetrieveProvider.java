package com.ibm.fhir.cql.engine.rest.retrieve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.cql.engine.retrieve.FhirBundleCursor;
import com.ibm.fhir.cql.engine.retrieve.SearchParameterFhirRetrieveProvider;
import com.ibm.fhir.cql.engine.retrieve.SearchParameterMap;
import com.ibm.fhir.cql.engine.searchparam.IQueryParameter;
import com.ibm.fhir.cql.engine.searchparam.ReferenceParameter;
import com.ibm.fhir.cql.engine.searchparam.SearchParameterResolver;
import com.ibm.fhir.cql.engine.util.FHIRClientUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;

public class RestFhirRetrieveProvider extends SearchParameterFhirRetrieveProvider {

    private FHIRClient fhirClient;

    public RestFhirRetrieveProvider(SearchParameterResolver searchParameterResolver, FHIRClient fhirClient) {
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
                FhirBundleCursor cursor = new FhirBundleCursor(url -> {
                    try {
                        Response response = fhirClient.getWebTarget().path(url).request().get();
                        FHIRClientUtil.handleErrorResponse(response);
                        return response.readEntity(Bundle.class);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }, (Bundle) resource);
                cursor.forEach(results::add);
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

    protected String getModifiedName(String name, IQueryParameter param) {
        StringBuilder paramName = new StringBuilder(name);
        if (param instanceof ReferenceParameter) {
            ReferenceParameter rp = (ReferenceParameter) param;
            if (rp.getResourceTypeModifier() != null) {
                paramName.append(":");
                paramName.append(rp.getResourceTypeModifier().getValue());
            }

            if (rp.getChainedProperty() != null) {
                paramName.append(".");
                paramName.append(rp.getChainedProperty());
            }

        } else {
            if (param.getMissing() != null) {
                paramName.append(":missing");
            } else if (param.getModifier() != null) {
                paramName.append(":");
                paramName.append(param.getModifier().value());
            }
        }

        return paramName.toString();
    }
}
