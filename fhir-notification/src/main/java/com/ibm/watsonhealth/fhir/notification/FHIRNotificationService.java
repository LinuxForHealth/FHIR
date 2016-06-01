/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.notification.exception.FHIRNotificationException;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;

/**
 * This class coordinates the activities of the FHIR Server notification service.
 */
public class FHIRNotificationService implements FHIRPersistenceInterceptor {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationService.class.getName());
    private List<FHIRNotificationSubscriber> subscribers = new CopyOnWriteArrayList<FHIRNotificationSubscriber>();
    private static final FHIRNotificationService INSTANCE = new FHIRNotificationService();

    private FHIRNotificationService() {

        // Register the notification service as an interceptor so we can rely on the
        // interceptor methods to trigger the 'publish' of the notification events.
        FHIRPersistenceInterceptorMgr.getInstance().addPrioritizedInterceptor(this);
    }

    public static FHIRNotificationService getInstance() {
        return INSTANCE;
    }

    /**
     * Method for broadcasting message to each subscriber.
     * 
     * @param event
     */
    public void publish(FHIRNotificationEvent event) {
        log.entering(this.getClass().getName(), "publish");
        for (FHIRNotificationSubscriber subscriber : subscribers) {
            try {
                subscriber.notify(event);
            } catch (FHIRNotificationException e) {
                subscribers.remove(subscriber);
                log.log(Level.WARNING, FHIRNotificationService.class.getName() + ": unable to publish event", e);
            }
        }
        log.exiting(this.getClass().getName(), "publish");
    }

    /**
     * Method to subscribe the target notification implementation
     * 
     * @param subscriber
     */
    public void subscribe(FHIRNotificationSubscriber subscriber) {
        log.entering(this.getClass().getName(), "subscribe");
        try {
            if (!subscribers.contains(subscriber)) {
                subscribers.add(subscriber);
            }
        } finally {
            log.exiting(this.getClass().getName(), "subscribe");
        }
    }

    /**
     * Method to unsubscribe the target notification implementation
     * 
     * @param subscriber
     */
    public void unsubscribe(FHIRNotificationSubscriber subscriber) {
        log.entering(this.getClass().getName(), "unsubscribe");
        try {
            if (subscribers.contains(subscriber)) {
                subscribers.remove(subscriber);
            }
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
    public boolean isSubscribed(FHIRNotificationSubscriber subscriber) {
        log.entering(this.getClass().getName(), "isSubscribed");
        try {
            return subscribers.contains(subscriber);
        } finally {
            log.exiting(this.getClass().getName(), "isSubscribed");
        }
    }

    /**
     * The following set of methods are from the FHIRPersistenceInterceptor interface and are implemented here to allow
     * the notification service to be registered as a persistence interceptor. All we really need to do in these methods
     * is perform the "publish" action.
     */

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptor#afterCreate(com.ibm.watsonhealth.
     * fhir.persistence.interceptor.FHIRPersistenceEvent)
     */
    @Override
    public void afterCreate(FHIRPersistenceEvent pEvent) throws FHIRPersistenceInterceptorException {
        this.publish(buildNotificationEvent("create", pEvent));
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptor#afterUpdate(com.ibm.watsonhealth.
     * fhir.persistence.interceptor.FHIRPersistenceEvent)
     */
    @Override
    public void afterUpdate(FHIRPersistenceEvent pEvent) throws FHIRPersistenceInterceptorException {
        this.publish(buildNotificationEvent("update", pEvent));
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptor#beforeCreate(com.ibm.watsonhealth.
     * fhir.persistence.interceptor.FHIRPersistenceEvent)
     */
    @Override
    public void beforeCreate(FHIRPersistenceEvent pEvent) throws FHIRPersistenceInterceptorException {
        // Nothing to do for 'beforeCreate'.
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptor#beforeUpdate(com.ibm.watsonhealth.
     * fhir.persistence.interceptor.FHIRPersistenceEvent)
     */
    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        // Nothing to do for 'beforeUpdate'.
    }

    /**
     * Builds a FHIRNotificationEvent object from the specified FHIRPersistenceEvent.
     */
    private FHIRNotificationEvent buildNotificationEvent(String operation, FHIRPersistenceEvent pEvent) {
        try {
            FHIRNotificationEvent event = new FHIRNotificationEvent();
            event.setOperationType(operation);
            Resource resource = pEvent.getFhirResource();
            event.setLastUpdated(resource.getMeta().getLastUpdated().getValue().toString());
            event.setLocation((String) pEvent.getProperty(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI));
            event.setResourceId(resource.getId().getValue());
            event.setResource(resource);
            return event;
        } catch (Exception e) {
            log.log(Level.SEVERE, this.getClass().getName() + ": unable to build notification event", e);
            throw e;
        }
    }
}
