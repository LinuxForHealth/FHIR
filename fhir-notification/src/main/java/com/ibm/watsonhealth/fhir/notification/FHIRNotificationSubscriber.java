/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification;

public interface FHIRNotificationSubscriber {
    /**
     * Abstract method to send message
     * @param Message
     * @throws FHIRNotificationException
     */
    void publish(String message) throws FHIRNotificationException;
}
