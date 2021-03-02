/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.context.FHIRSystemHistoryContext;
import com.ibm.fhir.persistence.context.impl.FHIRSystemHistoryContextImpl;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

public class FHIRPersistenceUtil {
    private static final Logger log = Logger.getLogger(FHIRPersistenceUtil.class.getName());

    private FHIRPersistenceUtil() {
        // No operation
    }

    // Parse history parameters into a FHIRHistoryContext
    public static FHIRHistoryContext parseHistoryParameters(Map<String, List<String>> queryParameters, boolean lenient) throws FHIRPersistenceException {
        log.entering(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
        FHIRHistoryContext context = FHIRPersistenceContextFactory.createHistoryContext();
        context.setLenient(lenient);
        try {
            for (String name : queryParameters.keySet()) {
                List<String> values = queryParameters.get(name);
                String first = values.get(0);
                if ("_page".equals(name)) {
                    int pageNumber = Integer.parseInt(first);
                    context.setPageNumber(pageNumber);
                } else if ("_count".equals(name)) {
                    int pageSize = Integer.parseInt(first);
                    context.setPageSize(pageSize);
                } else if ("_since".equals(name)) {
                    DateTime dt = DateTime.of(first);
                    if (!dt.isPartial()) {
                        Instant since = Instant.of(ZonedDateTime.from(dt.getValue()));
                        context.setSince(since);
                    }
                    else {
                        throw new FHIRPersistenceException("The '_since' parameter must be a fully specified ISO 8601 date/time");
                    }
                } else if ("_format".equals(name)) {
                    // safely ignore
                    continue;
                } else {
                    throw new FHIRPersistenceException("Unrecognized history parameter: '" + name + "'");
                }
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIRPersistenceException("Error parsing history parameters", e);
        } finally {
            log.exiting(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
        }
        return context;
    }

    // Parse history parameters into a FHIRHistoryContext

    /**
     *
     * @param queryParameters
     * @param lenient
     * @return
     * @throws FHIRPersistenceException
     */
    public static FHIRSystemHistoryContext parseSystemHistoryParameters(Map<String, List<String>> queryParameters, boolean lenient) throws FHIRPersistenceException {
        log.entering(FHIRPersistenceUtil.class.getName(), "parseSystemHistoryParameters");
        FHIRSystemHistoryContextImpl context = new FHIRSystemHistoryContextImpl();
        context.setLenient(lenient);
        try {
            for (String name : queryParameters.keySet()) {
                List<String> values = queryParameters.get(name);
                String first = values.get(0);
                if ("_afterHistoryId".equals(name)) {
                    long id = Long.parseLong(first);
                    context.setAfterHistoryId(id);
                } else if ("_count".equals(name)) {
                    int resourceCount = Integer.parseInt(first);
                    context.setCount(resourceCount);
                } else if ("_since".equals(name)) {
                    DateTime dt = DateTime.of(first);
                    if (!dt.isPartial()) {
                        Instant since = Instant.of(ZonedDateTime.from(dt.getValue()));
                        context.setSince(since);
                    }
                    else {
                        throw new FHIRPersistenceException("The '_since' parameter must be a fully specified ISO 8601 date/time");
                    }
                } else if ("_format".equals(name)) {
                    // safely ignore
                    continue;
                } else {
                    throw new FHIRPersistenceException("Unrecognized history parameter: '" + name + "'");
                }
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIRPersistenceException("Error parsing history parameters", e);
        } finally {
            log.exiting(FHIRPersistenceUtil.class.getName(), "parseSystemHistoryParameters");
        }
        return context;
    }


    /**
     * Create a minimal deleted resource marker from the given resource
     *
     * @param deletedResource
     * @return deletedResourceMarker
     */
    public static Resource createDeletedResourceMarker(Resource deletedResource) {
        try {
            // Build a fresh meta with only versionid/lastupdated defined
            Meta meta = Meta.builder()
                    .versionId(deletedResource.getMeta().getVersionId())
                    .lastUpdated(deletedResource.getMeta().getLastUpdated())
                    .build();

            // TODO this will clone the entire resource, but we only want the minimal parameters
            Resource deletedResourceMarker = deletedResource.toBuilder()
                    .id(deletedResource.getId())
                    .meta(meta)
                    .build();

            return deletedResourceMarker;
        } catch (Exception e) {
            throw new IllegalStateException("Error while creating deletion marker for resource of type "
                    + deletedResource.getClass().getSimpleName());
        }
    }
}
