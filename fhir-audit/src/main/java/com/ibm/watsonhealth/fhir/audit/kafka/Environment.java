/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.kafka;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Environment {

    private static final Logger logger = java.util.logging.Logger.getLogger(Environment.class.getName());
    private static final String CLASSNAME = Environment.class.getName();
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

        String kubEventStreamBinging = System.getenv(KUB_EVENTSTREAMS_BINDING);
        logger.info(KUB_EVENTSTREAMS_BINDING + ": \n" + kubEventStreamBinging);
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (kubEventStreamBinging != null) {
                return mapper.readValue(kubEventStreamBinging, EventStreamsCredentials.class);
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,
                    KUB_EVENTSTREAMS_BINDING + " environment variable parses failed.");
        } finally {
            logger.exiting(CLASSNAME, METHODNAME);
        }
        return null;
    }
}
