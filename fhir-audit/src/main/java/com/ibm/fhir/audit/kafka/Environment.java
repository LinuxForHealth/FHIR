/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.kafka;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.exception.FHIRException;

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

        String kubEventStreamBinding = System.getenv(KUB_EVENTSTREAMS_BINDING);
        logger.info(KUB_EVENTSTREAMS_BINDING + ": \n" + kubEventStreamBinding);
        return parseEventStreamsCredentials(kubEventStreamBinding);
    }

    /**
     * parses the envents
     * @param kubEventStreamBinding
     * @return
     */
    public static EventStreamsCredentials parseEventStreamsCredentials(String kubEventStreamBinding) {
        if (kubEventStreamBinding != null) {
            try {
                return EventStreamsCredentials.Parser.parse(kubEventStreamBinding);
            } catch (FHIRException e) {
                logger.log(Level.SEVERE,
                        "Parsing of environment variable '" + KUB_EVENTSTREAMS_BINDING + "' has failed.");
            }
        }
        return null;
    }
}
