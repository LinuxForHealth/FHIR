/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.notification.util.NotificationObject;
import com.ibm.watsonhealth.fhir.notification.util.NotificationUtils;

public class FHIRNotificationService {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationService.class.getName());
    private static final String className = FHIRNotificationService.class.getName();
    private List<FHIRNotification> listeners = new CopyOnWriteArrayList<FHIRNotification>();
    private static final FHIRNotificationService INSTANCE = new FHIRNotificationService();

    public static FHIRNotificationService getInstance() {
        return INSTANCE;
    }

    /**
     * Method for broadcasting message to any connected client
     * 
     * @param message
     *            to send down to the connected client
     */
    public void broadcast(NotificationObject obj) {
        String methodName = "broadcast";
        try {
            log.entering(className, methodName);
            for (FHIRNotification notifier : listeners) {
                try {
                    notifier.notify(NotificationUtils.convertNotificationObjectToJson(obj));
                } catch (FHIRNotificationException ne) {
                    listeners.remove(notifier);
                }
            }
        } finally {
            log.exiting(className, methodName);
        }
    }

    /**
     * Method to subscribe the target notification implementation
     * 
     * @param notifier
     */
    public void subscribe(FHIRNotification notification) {
        String methodName = "subscribe";
        try {
            log.entering(className, methodName);
            listeners.add(notification);
        } finally {
            log.exiting(className, methodName);
        }
    }

    /**
     * Method to unsubscribe the target notification implementation
     * 
     * @param notifier
     */

    public void unsubscribe(FHIRNotification notification) {
        String methodName = "unsubscribe";
        try {
            log.entering(className, methodName);
            listeners.remove(notification);
        } finally {
            log.exiting(className, methodName);
        }
    }

    /**
     * Method to check
     * 
     * @param notifier
     * @return
     */
    public boolean subscribed(FHIRNotification notification) {
        String methodName = "subscribed";
        try {
            log.entering(className, methodName);
            return listeners.contains(notification);
        } finally {
            log.exiting(className, methodName);
        }
    }
}
