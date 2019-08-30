/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.notification.exception;

public class FHIRNotificationException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param message
     */
    public FHIRNotificationException(String message) {
        this(message, null);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public FHIRNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
