/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.notification;

public interface FHIRNotificationSubscriber {
    /**
     * Notify subscriber of an event
     * @param event
     * @throws FHIRNotificationException
     */
    void notify(FHIRNotificationEvent event) throws FHIRNotificationException;
}
