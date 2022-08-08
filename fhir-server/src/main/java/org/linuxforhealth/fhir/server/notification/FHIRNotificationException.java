/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.notification;

public class FHIRNotificationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * a notification exception
     * @param message
     */
    public FHIRNotificationException(String message) {
        this(message, null);
    }

    /**
     * a notification exception with a throwable
     * @param message
     * @param cause
     */
    public FHIRNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
