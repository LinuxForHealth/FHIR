/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationEvent;
import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationUtils;

public class FHIRNotificationService {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationService.class.getName());
    private static final String className = FHIRNotificationService.class.getName();
    private List<FHIRNotificationSubscriber> subscribers = new CopyOnWriteArrayList<FHIRNotificationSubscriber>();
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
    public void publish(FHIRNotificationEvent obj) {
        String methodName = "publish";
        try {
            log.entering(className, methodName);
            for (FHIRNotificationSubscriber subscriber : subscribers) {
                try {
                	subscriber.publish(FHIRNotificationUtils.convertNotificationObjectToJson(obj));
                } catch (FHIRNotificationException ne) {
                	subscribers.remove(subscriber);
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
    public void subscribe(FHIRNotificationSubscriber notification) {
        String methodName = "subscribe";
        try {
            log.entering(className, methodName);
            subscribers.add(notification);
        } finally {
            log.exiting(className, methodName);
        }
    }

    /**
     * Method to unsubscribe the target notification implementation
     * 
     * @param notifier
     */

    public void unsubscribe(FHIRNotificationSubscriber notification) {
        String methodName = "unsubscribe";
        try {
            log.entering(className, methodName);
            subscribers.remove(notification);
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
    public boolean subscribed(FHIRNotificationSubscriber notification) {
        String methodName = "subscribed";
        try {
            log.entering(className, methodName);
            return subscribers.contains(notification);
        } finally {
            log.exiting(className, methodName);
        }
    }
}
