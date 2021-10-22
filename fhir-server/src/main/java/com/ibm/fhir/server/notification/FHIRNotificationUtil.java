/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.notification;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.JsonSupport;
import com.ibm.fhir.search.SearchConstants;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

/**
 * FHIRNotificationUtil supports serializing and deserializing the FHIRNotificationEvent based on conditions.
 */
public class FHIRNotificationUtil {
    private static final Logger LOG = Logger.getLogger(FHIRNotificationUtil.class.getSimpleName());
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);
    private static final JsonBuilderFactory JSON_BUILDER_FACTORY = Json.createBuilderFactory(null);

    private static final int DEFAULT_MAX_SIZE = 1000000;

    private FHIRNotificationUtil() {
        // No Operation
    }

    /**
     * serialize the FHIRNotificationEvent
     *
     * @param jsonString the input string
     * @return FHIRNotificationEvent without the Resource
     */
    public static FHIRNotificationEvent toNotificationEvent(String jsonString) {
        try (JsonReader reader = JSON_READER_FACTORY.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = reader.readObject();
            FHIRNotificationEvent event = new FHIRNotificationEvent();
            event.setLastUpdated(jsonObject.getString("lastUpdated"));
            event.setLocation(jsonObject.getString("location"));
            event.setOperationType(jsonObject.getString("operationType"));
            event.setResourceId(jsonObject.getString("resourceId"));
            event.setDatasourceId(jsonObject.getString("datasourceId"));
            event.setTenantId(jsonObject.getString("tenantId"));
            return event;
        } catch (JsonException e) {
            LOG.warning("Failed to parse json string: " + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Serializes the notification event into a JSON string.
     * @param event the FHIRNotificationEvent structure to be serialized
     * @param includeResource a flag that controls whether or not the resource object within
     * the event structure should be included in the serialized message.
     * @return the serialized message as a String
     * @throws FHIRException
     */
    public static String toJsonString(FHIRNotificationEvent event, boolean includeResource) throws FHIRException {
        JsonObjectBuilder builder = JSON_BUILDER_FACTORY.createObjectBuilder();
        builder.add("lastUpdated", event.getLastUpdated());
        builder.add("location", event.getLocation());
        builder.add("operationType", event.getOperationType());
        builder.add("resourceId", event.getResourceId());
        builder.add("datasourceId", event.getDatasourceId());
        builder.add("tenantId", event.getTenantId());

        // If it's a delete operation, don't add as there is no actual resource in the event.
        String jsonString;
        if (!"delete".equals(event.getOperationType()) && includeResource && event.getResource() != null) {
            builder.add("resource", JsonSupport.toJsonObject(event.getResource()));
            JsonObject jsonObject = builder.build();

            jsonString = jsonObject.toString();
            long length = jsonString.getBytes().length;

            int maxSize = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_NOTIFICATION_MAX_SIZE, DEFAULT_MAX_SIZE);
            if (length > maxSize) {
                LOG.fine(() -> event.getResource().getClass().getSimpleName() + "/" + event.getResourceId() + " is over the size limit - '" + length + "' > '" + maxSize + "'");

                // the build method wipes out any preexisting values, we have to add them back.
                builder.add("lastUpdated", event.getLastUpdated());
                builder.add("location", event.getLocation());
                builder.add("operationType", event.getOperationType());
                builder.add("resourceId", event.getResourceId());
                builder.add("datasourceId", event.getDatasourceId());
                builder.add("tenantId", event.getTenantId());

                // If we are including a subset, we'll add here.
                String subset = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_NOTIFICATION_NOTIFICATION_SIZE_BEHAVIOR, "subset");
                if ("subset".equals(subset)) {
                    List<String> elements = Arrays.asList("id", "meta", "resourceType");
                    com.ibm.fhir.model.resource.Resource resource = FHIRParser.parser(Format.JSON)
                                                                        .as(FHIRJsonParser.class)
                                                                        .parseAndFilter(JsonSupport.toJsonObject(event.getResource()), elements);
                    // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                    if (!FHIRUtil.hasTag(resource, SearchConstants.SUBSETTED_TAG)) {
                        resource = FHIRUtil.addTag(resource, SearchConstants.SUBSETTED_TAG);
                    }
                    builder.add("resource", JsonSupport.toJsonObject(resource));
                } else {
                    LOG.fine(() -> "Omitting the resource in FHIRNotificationEvent");
                }

                jsonObject = builder.build();
                jsonString = jsonObject.toString();
            }
        } else {
            JsonObject jsonObject = builder.build();
            jsonString = jsonObject.toString();
        }
        return jsonString;
    }
}