/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification;

import com.ibm.watsonhealth.fhir.notification.exception.FHIRNotificationException;
import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationEvent;

public interface FHIRNotificationSubscriber {
    /**
     * Notify subscriber of an event
     * @param event
     * @throws FHIRNotificationException
     */
    void notify(FHIRNotificationEvent event) throws FHIRNotificationException;
}
