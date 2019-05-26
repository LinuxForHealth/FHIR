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

import org.apache.kafka.common.errors.IllegalSaslStateException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class Environment {

    private static final Logger logger = java.util.logging.Logger.getLogger(Environment.class.getName());
    private static final String CLASSNAME = Environment.class.getName();
    private static final String SERVICE_NAME = "messagehub";

    public static EventStreamsCredentials getEventStreamsCredentials() {
        final String METHODNAME = "getEventStreamsCredentials";
        logger.entering(CLASSNAME, METHODNAME);

        String vcapServices = System.getenv("VCAP_SERVICES");
        logger.info("VCAP_SERVICES: \n" + vcapServices);
        try {
            if (vcapServices != null) {
                JsonNode mhub = parseVcapServices(vcapServices);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(mhub.toString(), EventStreamsCredentials.class);
            } else {

                logger.log(Level.SEVERE, "VCAP_SERVICES environment variable is null.");
                throw new IllegalStateException("VCAP_SERVICES environment variable is null.");
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "VCAP_SERVICES environment variable parses failed.");
            throw new IllegalStateException("VCAP_SERVICES environment variable parses failed.", ioe);
        } finally {
            logger.exiting(CLASSNAME, METHODNAME);
        }
    }

    private static JsonNode parseVcapServices(String vcapServices) throws IOException {
        final String METHODNAME = "parseVcapServices";
        logger.entering(CLASSNAME, METHODNAME);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode vcapServicesJson = mapper.readValue(vcapServices, JsonNode.class);

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
            Iterator<String> it = vcapServicesJson.getFieldNames();
            // Find the Event Streams service bound to this application.
            while (it.hasNext() && vcapKey == null) {
                String potentialKey = it.next();
                if (potentialKey.startsWith(SERVICE_NAME)) {
                    logger.log(Level.WARNING, "Using the '" + potentialKey + "' key from VCAP_SERVICES.");
                    vcapKey = potentialKey;
                }
            }

            if (vcapKey == null) {
                logger.exiting(CLASSNAME, METHODNAME);
                throw new IllegalSaslStateException("No Event Streams service bound");
            } else {
                logger.exiting(CLASSNAME, METHODNAME);
                return vcapServicesJson.get(vcapKey).get(0).get("credentials");
            }
        }
    }
}
