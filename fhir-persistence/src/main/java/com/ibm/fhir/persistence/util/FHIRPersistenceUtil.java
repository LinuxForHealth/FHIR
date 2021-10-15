/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Resource.Builder;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
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
                    if (resourceCount >= 0) {
                        context.setCount(resourceCount);
                    }
                } else if ("_since".equals(name)) {
                    DateTime dt = DateTime.of(first);
                    if (!dt.isPartial()) {
                        Instant since = Instant.of(ZonedDateTime.from(dt.getValue()));
                        context.setSince(since);
                    }
                    else {
                        String msg = "The '_since' parameter must be a fully specified ISO 8601 date/time";
                        throw new FHIRPersistenceException(msg)
                                .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                    }
                } else if ("_format".equals(name)) {
                    // safely ignore
                    continue;
                } else {
                    String msg = "Unrecognized history parameter: '" + name + "'";
                    throw new FHIRPersistenceException(msg)
                            .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                }
            }

            if (context.getAfterHistoryId() != null && context.getSince() != null) {
                String msg = "_since and _afterHistoryId can only be used exclusively, not together";
                throw new FHIRPersistenceException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (NumberFormatException | DateTimeParseException e) {
            String msg = "Error parsing history parameters";
            throw new FHIRPersistenceException(msg, e)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
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

    /**
     * Get the current UTC timestamp which can be used as a lastUpdated time when ingesting
     * resources.
     * @return
     */
    public static com.ibm.fhir.model.type.Instant getUpdateTime() {
        return com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);
    }
    
    /**
     * Creates and returns a copy of the passed resource with the {@code Resource.id}
     * {@code Resource.meta.versionId}, and {@code Resource.meta.lastUpdated} elements replaced.
     *
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @param lastUpdated
     * @return the updated resource
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T copyAndSetResourceMetaFields(T resource, String logicalId, int newVersionNumber, com.ibm.fhir.model.type.Instant lastUpdated) {
        Meta meta = resource.getMeta();
        Meta.Builder metaBuilder = meta == null ? Meta.builder() : meta.toBuilder();
        metaBuilder.versionId(Id.of(Integer.toString(newVersionNumber)));
        metaBuilder.lastUpdated(lastUpdated);

        Builder resourceBuilder = resource.toBuilder();
        resourceBuilder.setValidating(false);
        return (T) resourceBuilder
                .id(logicalId)
                .meta(metaBuilder.build())
                .build();
    }

}
