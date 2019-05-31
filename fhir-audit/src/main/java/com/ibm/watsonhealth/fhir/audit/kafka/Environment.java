/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.kafka;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.*;

public class Environment {

    private static final Logger logger = java.util.logging.Logger.getLogger(Environment.class.getName());
    private static final String CLASSNAME = Environment.class.getName();
    private static final String SERVICE_NAME = "messagehub";
    public static final String VCAP_SERVICES = "VCAP_SERVICES";
    public static final String KUB_EVENTSTREAMS_BINDING = "EVENT_STREAMS_AUDIT_BINDING";

    /*
     * Return a CSV-String from a String array
     */
    public static String stringArrayToCSV(String[] sArray) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sArray.length; i++) {
            sb.append(sArray[i]);
            if (i < sArray.length - 1)
                sb.append(",");
        }
        return sb.toString();
    }

    /**
     * @return EventStreamsCredentials
     */
    public static EventStreamsCredentials getEventStreamsCredentials() {
        final String METHODNAME = "getEventStreamsCredentials";
        logger.entering(CLASSNAME, METHODNAME);

        String vcapServices = System.getenv(VCAP_SERVICES);
        String kubEventStreamBinging = System.getenv(KUB_EVENTSTREAMS_BINDING);
        logger.info("VCAP_SERVICES: \n" + vcapServices);
        logger.info("binding-eventstreams-fhir1: \n" + kubEventStreamBinging);
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (vcapServices != null) {
                JsonNode mhub = parseVcapServices(vcapServices);
                if (mhub != null) {
                return mapper.readValue(mhub.toString(), EventStreamsCredentials.class);
                }
            } else if (kubEventStreamBinging != null) {
                return mapper.readValue(kubEventStreamBinging, EventStreamsCredentials.class);
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,
                    VCAP_SERVICES + " or " + KUB_EVENTSTREAMS_BINDING + " environment variable parses failed.");
        } finally {
            logger.exiting(CLASSNAME, METHODNAME);
        }
        return null;
    }

    /**
     * @param vcapServices
     * @return JsonNode
     * @throws IOException
     */
    private static JsonNode parseVcapServices(String vcapServices) {
        final String METHODNAME = "parseVcapServices";
        logger.entering(CLASSNAME, METHODNAME);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode vcapServicesJson = null;
        try {
            vcapServicesJson = mapper.readValue(vcapServices, JsonNode.class);

            // when running in CloudFoundry VCAP_SERVICES is wrapped into a bigger JSON
            // object
            // so it needs to be extracted. We attempt to read the "instance_id" field to
            // identify
            // if it has been wrapped
            if (vcapServicesJson.get("instance_id") != null) {
                logger.exiting(CLASSNAME, METHODNAME);
                return vcapServicesJson;
            } else {
                String vcapKey = null;
                Iterator<String> it = vcapServicesJson.fieldNames();
                // Find the Event Streams service bound to this application.
                while (it.hasNext() && vcapKey == null) {
                    String potentialKey = it.next();
                    if (potentialKey.startsWith(SERVICE_NAME)) {
                        logger.log(Level.INFO, "Using the '" + potentialKey + "' key from VCAP_SERVICES.");
                        vcapKey = potentialKey;
                    }
                }

                if (vcapKey == null) {
                    logger.log(Level.SEVERE, "Unable to find eventStreams key from VCAP_SERVICES!");
                    return null;
                } else {
                    return vcapServicesJson.get(vcapKey).get(0).get("credentials");
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to parser VCAP_SERVICES!");
            return null;

        } finally {
            logger.exiting(CLASSNAME, METHODNAME);
        }
    }
}
