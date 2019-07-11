/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification.util;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.xml.bind.JAXBException;

import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.notification.FHIRNotificationEvent;

public class FHIRNotificationUtil {
    public static FHIRNotificationEvent toNotificationEvent(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        FHIRNotificationEvent event = new FHIRNotificationEvent();
        event.setOperationType(jsonObject.getString("operationType"));
        event.setLocation(jsonObject.getString("location"));
        event.setLastUpdated(jsonObject.getString("lastUpdated"));
        event.setResourceId(jsonObject.getString("resourceId"));
        return event;
    }
    
    /**
     * Serializes the notification event into a JSON string.
     * @param event the FHIRNotificationEvent structure to be serialized
     * @param includeResource a flag that controls whether or not the resource object within
     * the event structure should be included in the serialized message.
     * @return the serialized message as a String
     * @throws JAXBException
     * @throws FHIRException 
     */
    public static String toJsonString(FHIRNotificationEvent event, boolean includeResource) throws JAXBException, FHIRException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("lastUpdated", event.getLastUpdated());
        builder.add("location", event.getLocation());
        builder.add("operationType", event.getOperationType());
        builder.add("resourceId", event.getResourceId());
        if (includeResource && event.getResource() != null) {
            builder.add("resource", FHIRUtil.toJsonObject(event.getResource()));
        }
        JsonObject jsonObject = builder.build();
        String jsonString = jsonObject.toString();
        return jsonString;
    }
}
