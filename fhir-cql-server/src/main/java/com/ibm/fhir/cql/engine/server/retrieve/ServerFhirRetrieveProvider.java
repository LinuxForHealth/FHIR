package com.ibm.fhir.cql.engine.server.retrieve;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.cql.engine.retrieve.SearchParameterFhirRetrieveProvider;
import com.ibm.fhir.cql.engine.retrieve.SearchParameterMap;
import com.ibm.fhir.cql.engine.searchparam.IQueryParameter;
import com.ibm.fhir.cql.engine.searchparam.ReferenceParameter;
import com.ibm.fhir.cql.engine.searchparam.SearchParameterResolver;
import com.ibm.fhir.cql.helpers.FhirBundleCursor;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public class ServerFhirRetrieveProvider extends SearchParameterFhirRetrieveProvider {

    private static final String DUMMY_REQUEST_URI = "https://localhost/fhir-server/api/v4";

    private static Logger logger = Logger.getLogger(ServerFhirRetrieveProvider.class.getName());
    
    private FHIRResourceHelpers resourceHelpers;

    public ServerFhirRetrieveProvider(FHIRResourceHelpers resourceHelpers, SearchParameterResolver searchParameterResolver) {
        super(searchParameterResolver);
        this.resourceHelpers = resourceHelpers;
    }

    @Override
    protected Iterable<Object> executeQueries(String dataType, List<SearchParameterMap> queries) throws Exception {
        List<Object> results = new ArrayList<>();

        for (SearchParameterMap map : queries) {
            if( logger.isLoggable(Level.FINE) ) {
                logger.fine(String.format("Executing query %s", map.toString()));
            }
            
            MultivaluedMap<String, String> queryParameters = getQueryParameters(map);

            // _total=none instructs the server to skip the count(*) query which improves performance
            queryParameters.putSingle(SearchConstants.TOTAL, "none");

            Resource resource = executeQuery(dataType, queryParameters);
            if (resource instanceof Bundle) {
                AtomicInteger pageNumber = new AtomicInteger(1);
                FhirBundleCursor cursor = new FhirBundleCursor(url -> {
                    try {
                        int nextPage = pageNumber.incrementAndGet();
                        if( logger.isLoggable(Level.FINE) ) {
                            logger.fine(String.format("Retrieving page %d / %s", nextPage, url));
                        }
                        queryParameters.putSingle(SearchConstants.PAGE, String.valueOf(nextPage));
                        return resourceHelpers.doSearch(dataType, /*compartment=*/null, /*compartmentId=*/null, queryParameters, DUMMY_REQUEST_URI, /*contextResource=*/null);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }, (Bundle) resource);
                cursor.forEach(results::add);
            } else {
                results.add(resource);
            }
        }

        return results;
    }

    private Resource executeQuery(String dataType, MultivaluedMap<String, String> queryParameters) {
        Resource resource = null;

        try {
            if (queryParameters.containsKey("_id")) {
                String id = queryParameters.getFirst("_id");
                SingleResourceResult<?> result = resourceHelpers.doRead(dataType, id, true, false, null);
                if (result.isSuccess()) {
                    resource = result.getResource();
                }
            } else {
                resource = resourceHelpers.doSearch(dataType, /*compartment=*/null, /*compartmentId=*/null, queryParameters, DUMMY_REQUEST_URI, /*contextResource=*/null);
            }
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return resource;
    }

    private MultivaluedMap<String, String> getQueryParameters(SearchParameterMap map) {

        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        for (Map.Entry<String, List<List<? extends IQueryParameter>>> entry : map.entrySet()) {
            for (List<? extends IQueryParameter> sublist : entry.getValue()) {
                for (IQueryParameter p : sublist) {
                    String name = getModifiedName(entry.getKey(), p);

                    List<String> values = parameters.computeIfAbsent(name, k -> new ArrayList<>());
                    values.add(p.getParameterValue());
                }
            }
        }
        if( map.count() != null ) {
            parameters.putSingle(SearchConstants.COUNT, String.valueOf(map.count()));
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
