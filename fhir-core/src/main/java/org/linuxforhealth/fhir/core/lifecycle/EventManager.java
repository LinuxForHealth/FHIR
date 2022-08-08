/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.core.lifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages distribution of lifecycle events to registered callback
 * handlers.
 * @implNote Singleton scoped to a FHIR server instance
 */
public class EventManager {
    
    // The list of registered callbacks
    private final List<EventCallback> callbacks = new ArrayList<>();
    
    // Only the registered serviceManager is allowed to initiate the lifecycle events
    private Object serviceManagerId;
    
    /**
     * For simple and safe implementation of the singleton pattern
     */
    private static class Helper {
        private static final EventManager INSTANCE = new EventManager();
    }

    /**
     * Get the singleton instance
     * @return
     */
    public static EventManager getInstance() {
        return Helper.INSTANCE;
    }
    
    /**
     * Called once by the object managing the lifecycle of the system. The serviceManagerId
     * is simply an object private to the managing object. The identity of this object is
     * checked when making subsequent lifecycle calls, making it simple to enforce the
     * restriction that only one component in the system can manage these lifecycle events.
     * @param serviceManagerId
     */
    public static void registerServiceManagerId(Object serviceManagerId) {
        EventManager impl = getInstance();
        if (impl.serviceManagerId != null) {
            // the serviceManagerId must only be registered once
            throw new IllegalStateException("serviceManagerId already registered");
        }
        impl.serviceManagerId = serviceManagerId;
    }

    /**
     * Register the given {@link EventCallback} to receive lifecycle events
     * @param cb
     */
    public static void register(EventCallback cb) {
        getInstance().addCallback(cb);
    }
    
    /**
     * Add the callback to the callbacks list
     * @param cb
     */
    private void addCallback(EventCallback cb) {
        this.callbacks.add(cb);
    }

    /**
     * Check the given serviceManagerId matches the registered value
     * @param serviceManagerId
     */
    private void checkServiceManagerId(Object serviceManagerId) {
        EventManager impl = getInstance();
        if (impl.serviceManagerId == null) {
            throw new IllegalStateException("serviceManagerId has not been registered yet");
        }
        
        // identity comparison on purpose. We want the same object
        if (impl.serviceManagerId != serviceManagerId) {
            throw new IllegalArgumentException("serviceManagerId is incorrect");
        }
    }

    /**
     * Called by the lifecycle manager to advertise that the server is ready
     * @param serviceManager
     */
    public static void serverReady(Object serviceManagerId) {
        EventManager impl = getInstance();
        impl.checkServiceManagerId(serviceManagerId);
        
        // Distribute the callbacks
        for (EventCallback cb: impl.callbacks) {
            cb.serverReady();
        }
    }

    /**
     * Called by the lifecycle manager to advertise that the server shutdown
     * has been initiated
     * @param serviceManagerId
     */
    public static void startShutdown(Object serviceManagerId) {
        EventManager impl = getInstance();
        impl.checkServiceManagerId(serviceManagerId);
        
        // Distribute the callbacks
        for (EventCallback cb: impl.callbacks) {
            cb.startShutdown();
        }
    }
    
    /**
     * Called by the lifecycle manager to advertise that the server shutdown
     * should be completed
     * @param serviceManagerId
     */
    public static void finalShutdown(Object serviceManagerId) {
        EventManager impl = getInstance();
        impl.checkServiceManagerId(serviceManagerId);
        
        // Distribute the callbacks
        for (EventCallback cb: impl.callbacks) {
            cb.finalShutdown();
        }
    }    
}