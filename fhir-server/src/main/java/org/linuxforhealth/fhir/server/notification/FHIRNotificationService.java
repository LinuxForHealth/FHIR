/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.notification;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.server.interceptor.FHIRPersistenceInterceptorMgr;
import org.linuxforhealth.fhir.server.spi.interceptor.FHIRPersistenceInterceptor;
import org.linuxforhealth.fhir.server.spi.interceptor.FHIRPersistenceInterceptorException;

/**
 * This class coordinates the activities of the FHIR Server notification service.
 */
public class FHIRNotificationService implements FHIRPersistenceInterceptor {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationService.class.getName());
    private List<FHIRNotificationSubscriber> subscribers = new CopyOnWriteArrayList<>();
    private static final FHIRNotificationService INSTANCE = new FHIRNotificationService();
    private Set<String> includedResourceTypes = Collections.synchronizedSortedSet(new TreeSet<String>());

    private FHIRNotificationService() {
        log.entering(this.getClass().getName(), "FHIRNotificationService");
        try {
            // Register the notification service as an interceptor so we can rely on the
            // interceptor methods to trigger the 'publish' of the notification events.
            FHIRPersistenceInterceptorMgr.getInstance().addPrioritizedInterceptor(this);

            initNotificationResourceTypes();
        } catch (Throwable t) {
            throw new RuntimeException("Unexpected error during initialization.", t);
        }
        log.exiting(this.getClass().getName(), "FHIRNotificationService");
    }

    private void initNotificationResourceTypes() throws Exception {
        List<String> types = FHIRConfiguration.getInstance().loadConfiguration().getStringListProperty(FHIRConfiguration.PROPERTY_NOTIFICATION_RESOURCE_TYPES);
        if (types != null) {
            for (String type : types) {
                includedResourceTypes.add(type);
            }
        }

        log.info("Notification service, when enabled, will publish events for these resource types: '"
                + (includedResourceTypes.isEmpty() ? "ALL" : includedResourceTypes.toString()) + "'");
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // The following set of methods are from the FHIRPersistenceInterceptor interface and are implemented here to allow
    // the notification service to be registered as a persistence interceptor. All we really need to do in these methods
    // is perform the "publish" action.
    // Only the 'after' methods are enabled.
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void afterCreate(FHIRPersistenceEvent pEvent) throws FHIRPersistenceInterceptorException {
        if (shouldPublish(pEvent)) {
            this.publish(buildNotificationEvent("create", pEvent));
        }
    }

    @Override
    public void afterUpdate(FHIRPersistenceEvent pEvent) throws FHIRPersistenceInterceptorException {
        if (shouldPublish(pEvent)) {
            this.publish(buildNotificationEvent("update", pEvent));
        }
    }

    @Override
    public void afterDelete(FHIRPersistenceEvent pEvent) throws FHIRPersistenceInterceptorException {
        if (shouldPublish(pEvent)) {
            this.publish(buildNotificationEvent("delete", pEvent));
        }
    }

    @Override
    public void afterPatch(FHIRPersistenceEvent pEvent) throws FHIRPersistenceInterceptorException {
        if (shouldPublish(pEvent)) {
            this.publish(buildNotificationEvent("patch", pEvent));
        }
    }

    /**
     * Returns true iff we should publish the specified persistence event as a notification event.
     */
    private boolean shouldPublish(FHIRPersistenceEvent pEvent) {
        log.entering(this.getClass().getName(), "shouldPublish");

        try {
            // If our resource type filter is empty, then we should publish all notification events.
            if (includedResourceTypes == null || includedResourceTypes.isEmpty()) {
                log.finer("Resource type filter not specified, publishing all events.");
                return true;
            }

            // If the resource type filter contains "*", then publish all events.
            if (includedResourceTypes.contains("*")) {
                log.finer("Resource type filter contains '*', publishing all events.");
                return true;
            }

            // Retrieve the resource type associated with the event.
            String resourceType = (String) pEvent.getProperty(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE);
            return (resourceType != null ? includedResourceTypes.contains(resourceType) : false);
        } catch (Throwable t) {
            throw new IllegalStateException("Unexpected exception while checking notification resource type inclusion.", t);
        } finally {
            log.exiting(this.getClass().getName(), "shouldPublish");
        }
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
            String location = (String) pEvent.getProperty(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI);
            if (location == null) {
                // Must be a delete irrespective of version.
                location = pEvent.getFhirResourceType() + "/" + pEvent.getFhirResourceId();
            }
            event.setLocation(location);
            event.setResourceId(resource.getId());
            event.setResource(resource);

            // Adds the tenant id and datastore id
            String tenantId = FHIRRequestContext.get().getTenantId();
            String dsId = FHIRRequestContext.get().getDataStoreId();
            event.setDatasourceId(dsId);
            event.setTenantId(tenantId);

            return event;
        } catch (Exception e) {
            log.log(Level.SEVERE, this.getClass().getName() + ": unable to build notification event", e);
            throw e;
        }
    }
}
