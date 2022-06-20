/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.helper;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.ibm.fhir.persistence.index.RemoteIndexMessage;

/**
 * Utility methods supporting the fhir-remote-index consumer
 */
public class RemoteIndexSupport {
    private static final Logger logger = Logger.getLogger(RemoteIndexSupport.class.getName());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    /**
     * Get an instance of Gson configured to support serialization/deserialization of
     * remote index messages (sent through Kafka as strings)
     * @return
     */
    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (value, type, context) ->
                    new JsonPrimitive(formatter.format(value))
                )
                .registerTypeAdapter(Instant.class, (JsonDeserializer<Instant>) (jsonElement, type, context) ->
                        formatter.parse(jsonElement.getAsString(), Instant::from)
                )
                .create();
        
        return gson;
    }

    /**
     * Unmarshall the JSON payload parameter as a RemoteIndexMessage 
     * @param jsonPayload
     * @return
     */
    public static RemoteIndexMessage unmarshall(String jsonPayload) {
        try {
            Gson gson = getGson();
            return gson.fromJson(jsonPayload, RemoteIndexMessage.class);
        } catch (Throwable t) {
            // We need to sink this error to avoid poison messages from 
            // blocking the queues.
            // TODO. Perhaps push this to a dedicated error topic
            logger.severe("Not a RemoteIndexMessage. Ignoring: '" + jsonPayload + "'");
        }
        return null;

    }

    /**
     * Marshall the RemoteIndexMessage to a JSON string
     * @param message
     * @return
     */
    public static String marshallToString(RemoteIndexMessage message) {
        Gson gson = getGson();
        return gson.toJson(message);
    }
}
