/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.owasp.encoder.Encode;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.Interaction;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.ResourcesConfigAdapter;
import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.core.util.ResourceTypeHelper;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Resource.Builder;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.code.FHIRVersion;
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
     * Parse history parameters into a FHIRHistoryContext with a fhirVersion of 4.3.0
     *
     * @see #parseSystemHistoryParameters(Map, boolean, FHIRVersion)
     */
    public static FHIRSystemHistoryContext parseSystemHistoryParameters(Map<String, List<String>> queryParameters, boolean lenient)
            throws FHIRPersistenceException {
        return parseSystemHistoryParameters(queryParameters, lenient, FHIRVersionParam.VERSION_43);
    }

    /**
     * Parse history parameters into a FHIRHistoryContext for a given fhirVersion
     *
     * @param queryParameters
     * @param lenient
     * @param fhirVersion
     * @return
     * @throws FHIRPersistenceException
     */
    public static FHIRSystemHistoryContext parseSystemHistoryParameters(Map<String, List<String>> queryParameters, boolean lenient,
            FHIRVersionParam fhirVersion) throws FHIRPersistenceException {
        log.entering(FHIRPersistenceUtil.class.getName(), "parseSystemHistoryParameters");
        FHIRSystemHistoryContextImpl context = new FHIRSystemHistoryContextImpl();
        context.setLenient(lenient);
        context.setHistorySortOrder(HistorySortOrder.DESC_LAST_UPDATED); // default is most recent first
        try {
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
                        String[] resourceTypes = v.split(",");
                        for (String resourceType: resourceTypes) {
                            if (!ModelSupport.isConcreteResourceType(resourceType)) {
                                String msg = "_type parameter has invalid resource type: " + Encode.forHtml(resourceType);
                                throw new FHIRPersistenceException(msg)
                                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                            }
                            // Note: if we decide to support invalid _type values in 'lenient' mode (like search), then the following
                            // if/else block will need to be in an else block for the preceding if
                            if (!isHistoryEnabled(resourceType)) {
                                String msg = "history interaction is not supported for _type parameter value: " + Encode.forHtml(resourceType);
                                throw new FHIRPersistenceException(msg)
                                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.NOT_SUPPORTED));
                            } else if (!ResourceTypeHelper.isCompatible(resourceType, fhirVersion, FHIRVersionParam.VERSION_43)) {
                                String msg = "fhirVersion " + fhirVersion.value() + " interaction for _type parameter value: '" +
                                        resourceType + "' is not supported";
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
                    HistorySortOrder hso = HistorySortOrder.of(first);
                    context.setHistorySortOrder(hso);
                } else if ("_excludeTransactionTimeoutWindow".equals(name)) {
                    if ("true".equalsIgnoreCase(first)) {
                        context.setExcludeTransactionTimeoutWindow(true);
                    }
                } else {
                    String msg = "Unrecognized history parameter: '" + name + "'";
                    throw new FHIRPersistenceException(msg)
                            .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                }
            } // end foreach query parameter loop

            // If no explicit resource types were passed via _type, conditionally add implicit scoping (based on config)
            if (context.getResourceTypes().isEmpty()) {
                Boolean implicitTypeScoping = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_WHOLE_SYSTEM_TYPE_SCOPING, true);
                if (implicitTypeScoping) {
                    PropertyGroup rsrcsGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
                    ResourcesConfigAdapter configAdapter = new ResourcesConfigAdapter(rsrcsGroup, fhirVersion);
                    Set<String> supportedResourceTypes = configAdapter.getSupportedResourceTypes(Interaction.HISTORY);
                    for (String resType : supportedResourceTypes) {
                        context.addResourceType(resType);
                    }
                }
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

    private static boolean isHistoryEnabled(String resourceType) throws FHIROperationException {
        boolean resourceValid = true;
        List<String> interactions = null;

        // Retrieve the interaction configuration
        try {
            StringBuilder defaultInteractionsConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/Resource/")
                    .append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);
            StringBuilder resourceSpecificInteractionsConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/")
                    .append(resourceType).append("/").append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);

            // Get the 'interactions' property
            List<String> resourceSpecificInteractions = FHIRConfigHelper.getStringListProperty(resourceSpecificInteractionsConfigPath.toString());
            if (resourceSpecificInteractions != null) {
                interactions = resourceSpecificInteractions;
            } else {
                // Check the 'open' property, and if that's false, check if resource was specified
                if (!FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_RESOURCES + "/" + FHIRConfiguration.PROPERTY_FIELD_RESOURCES_OPEN, true)) {
                    PropertyGroup resourceGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES + "/" + resourceType);
                    if (resourceGroup == null) {
                        resourceValid = false;
                    }
                }
                if (resourceValid) {
                    // Get the 'Resource' interaction property
                    List<String> defaultInteractions = FHIRConfigHelper.getStringListProperty(defaultInteractionsConfigPath.toString());
                    if (defaultInteractions != null) {
                        interactions = defaultInteractions;
                    }
                }
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Allowed interactions: " + interactions);
            }
        } catch (Exception e) {
            String msg = "Error retrieving interactions configuration.";
            throw new FHIROperationException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.EXCEPTION));
        }

        // Perform validation of specified interaction against specified resourceType
        if (interactions != null && !interactions.contains("history")) {
            return false;
        }
        return resourceValid;
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
