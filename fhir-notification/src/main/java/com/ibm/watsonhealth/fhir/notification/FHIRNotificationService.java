/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.notification.exception.FHIRNotificationException;
import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationEvent;

public class FHIRNotificationService {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationService.class.getName());
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
    public void publish(FHIRNotificationEvent event) {
        log.entering(this.getClass().getName(), "publish");
        try {
            for (FHIRNotificationSubscriber subscriber : subscribers) {
                try {
                    subscriber.notify(event);
                } catch (FHIRNotificationException ne) {
                    ne.printStackTrace();
                    subscribers.remove(subscriber);
                }
            }
        } finally {
            log.exiting(this.getClass().getName(), "publish");
        }
    }

    /**
     * Method to subscribe the target notification implementation
     * 
     * @param notifier
     */
    public void subscribe(FHIRNotificationSubscriber subscriber) {
        log.entering(this.getClass().getName(), "subscribe");
        try {
            subscribers.add(subscriber);
        } finally {
            log.exiting(this.getClass().getName(), "subscribe");
        }
    }

    /**
     * Method to unsubscribe the target notification implementation
     * 
     * @param notifier
     */
    public void unsubscribe(FHIRNotificationSubscriber subscriber) {
        log.entering(this.getClass().getName(), "unsubscribe");
        try {
            subscribers.remove(subscriber);
        } finally {
            log.exiting(this.getClass().getName(), "unsubscribe");
        }
    }

    /**
     * Check if this subscriber has subscribed to this service
     * 
     * @param subscriber
     * @return
     */
    public boolean subscribed(FHIRNotificationSubscriber subscriber) {
        log.entering(this.getClass().getName(), "subscribed");
        try {
            return subscribers.contains(subscriber);
        } finally {
            log.exiting(this.getClass().getName(), "subscribed");
        }
    }
}
