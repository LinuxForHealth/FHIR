/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.feature;

import java.util.logging.Logger;

/**
 * The ExitFeature class encapsulate the logic related to Exit Status of the Schema App module.
 */
public class ExitFeature {
    private static final Logger logger = Logger.getLogger(ExitFeature.class.getName());

    public static final int EXIT_OK = 0; // validation was successful
    public static final int EXIT_BAD_ARGS = 1; // invalid CLI arguments
    public static final int EXIT_RUNTIME_ERROR = 2; // programming error
    public static final int EXIT_VALIDATION_FAILED = 3; // validation test failed

    // What status to leave with
    private int exitStatus = EXIT_OK;

    public ExitFeature() {
        // No Operation
    }

    /**
     * Get the program exit status from the environment
     * 
     * @return
     */
    public int getExitStatus() {
        return exitStatus;
    }

    /**
     * set the exit status to an int value, ideal this value is one of the specific values noted in this class.
     * 
     * @param exitStatus
     */
    public void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }

    /**
     * Write a final status message - useful for QA to review when checking the
     * output
     */
    public void logStatusMessage() {
        switch (exitStatus) {
        case EXIT_OK:
            logger.info("RESULT: OK");
            break;
        case EXIT_BAD_ARGS:
            logger.severe("RESULT: BAD ARGS");
            break;
        case EXIT_RUNTIME_ERROR:
            logger.severe("RESULT: RUNTIME ERROR");
            break;
        case EXIT_VALIDATION_FAILED:
            logger.warning("RESULT: FAILED");
            break;
        default:
            logger.severe("RESULT: RUNTIME ERROR");
            break;
        }
    }
}