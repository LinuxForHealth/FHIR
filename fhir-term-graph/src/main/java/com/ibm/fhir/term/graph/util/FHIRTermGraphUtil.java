/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.util;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class FHIRTermGraphUtil {
    private FHIRTermGraphUtil() { }

    /**
     * Sets the root logger level for the logback classic root logger. See:
     * <a href="https://docs.janusgraph.org/basics/common-questions/#debug-level-logging-slows-execution">JanusGraph common questions</a>
     * for more information.
     *
     * @param level
     *     the level
     */
    public static void setRootLoggerLevel(Level level) {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(level);
    }
}
