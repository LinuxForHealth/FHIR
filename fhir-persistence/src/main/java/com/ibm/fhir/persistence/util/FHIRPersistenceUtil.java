/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.owasp.encoder.Encode;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.Interaction;
import com.ibm.fhir.config.ResourcesConfigAdapter;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Resource.Builder;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.HistorySortOrder;
import com.ibm.fhir.persistence.ResourceResult;
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


    /**
     * Parse history parameters into a FHIRHistoryContext
     *
     * @param queryParameters
     * @param lenient
     * @param resourcesConfig
     * @return
     * @throws FHIRPersistenceException
     */
    public static FHIRSystemHistoryContext parseSystemHistoryParameters(Map<String, List<String>> queryParameters, boolean lenient,
            ResourcesConfigAdapter resourcesConfig) throws FHIRPersistenceException {
        log.entering(FHIRPersistenceUtil.class.getName(), "parseSystemHistoryParameters");
        FHIRSystemHistoryContextImpl context = new FHIRSystemHistoryContextImpl();
        context.setLenient(lenient);
        context.setHistorySortOrder(HistorySortOrder.DESC_LAST_UPDATED); // default is most recent first
        try {
            Set<String> typesSupportingHistory = resourcesConfig.getSupportedResourceTypes(Interaction.HISTORY);

            for (String name : queryParameters.keySet()) {
                List<String> values = queryParameters.get(name);
                String first = values.get(0);
                if ("_changeIdMarker".equals(name)) {
                    long id = Long.parseLong(first);
                    context.setChangeIdMarker(id);
                } else if ("_count".equals(name)) {
                    int resourceCount = Integer.parseInt(first);
                    if (resourceCount >= 0) {
                        context.setCount(resourceCount);
                    }
                } else if ("_type".equals(name)) {
                    for (String v: values) {
                        List<String> resourceTypes = Arrays.asList(v.split("\\s*,\\s*"));
                        for (String resourceType: resourceTypes) {
                            if (!ModelSupport.isConcreteResourceType(resourceType)) {
                                String msg = "_type parameter has invalid resource type: " + Encode.forHtml(resourceType);
                                throw new FHIRPersistenceException(msg)
                                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                            } else if (!typesSupportingHistory.contains(resourceType)) {
                                String msg = "history interaction is not supported for _type parameter value: " + Encode.forHtml(resourceType);
                                throw new FHIRPersistenceException(msg)
                                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.NOT_SUPPORTED));
                            } else {
                                context.addResourceType(resourceType);
                            }
                        }
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
                } else if ("_before".equals(name)) {
                    DateTime dt = DateTime.of(first);
                    if (!dt.isPartial()) {
                        Instant before = Instant.of(ZonedDateTime.from(dt.getValue()));
                        context.setBefore(before);
                    }
                    else {
                        String msg = "The '_before' parameter must be a fully specified ISO 8601 date/time";
                        throw new FHIRPersistenceException(msg)
                                .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                    }
                } else if ("_format".equals(name)) {
                    // safely ignore
                    continue;
                } else if ("_sort".equals(name)) {
                    try {
                        HistorySortOrder hso = HistorySortOrder.of(first);
                        context.setHistorySortOrder(hso);
                    } catch (IllegalArgumentException ex) {
                        // IllegalArgumentException needs to be converted to INVALID which is then a Client Error.
                        final String msg = "The '_sort' parameter must be a '_lastUpdated', '-_lastUpdated' or 'none'";
                        log.throwing(FHIRPersistenceUtil.class.getName(), "parseSystemHistoryParameters", ex);
                        throw new FHIRPersistenceException(msg)
                                .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                    }
                } else if ("_excludeTransactionTimeoutWindow".equals(name)) {
                    if ("true".equalsIgnoreCase(first)) {
                        context.setExcludeTransactionTimeoutWindow(true);
                    }
                } else {
                    String msg = "Unrecognized history parameter: '" + name + "'";
                    throw new FHIRPersistenceException(msg)
                            .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                }
            }

            // if no _type parameter was passed but the history interaction is only supported for some subset of types
            // then we need to set the supported resource types in the context
            if (context.getResourceTypes().isEmpty() && resourcesConfig.isHistoryRestricted()) {
                typesSupportingHistory.stream().forEach(context::addResourceType);
            }

            // Grab the return preference from the request context. We add it to the history
            // context so we have everything we need in one place
            FHIRRequestContext requestContext = FHIRRequestContext.get();
            if (requestContext.getReturnPreference() != null) {
                log.fine("Setting return preference: " + requestContext.getReturnPreference());
                context.setReturnPreference(requestContext.getReturnPreference());
            } else {
                // by default, return the resource in the bundle to make it compliant with the R4 spec.
                log.fine("Setting default return preference: " + HTTPReturnPreference.REPRESENTATION);
                context.setReturnPreference(HTTPReturnPreference.REPRESENTATION);
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
     * Create a new {@link ResourceResult} instance to represent a deleted or partially
     * erased resource
     * @param resourceType
     * @param logicalId
     * @param version
     * @param lastUpdated
     * @return
     */
    public static ResourceResult<Resource> createDeletedResourceResultMarker(String resourceType, String logicalId, int version, java.time.Instant lastUpdated) {

        return ResourceResult.builder()
                .deleted(true)
                .resourceTypeName(resourceType)
                .logicalId(logicalId)
                .version(version)
                .lastUpdated(lastUpdated)
                .build();
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
