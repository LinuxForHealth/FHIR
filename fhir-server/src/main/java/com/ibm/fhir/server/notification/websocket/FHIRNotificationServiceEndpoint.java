/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.notification.websocket;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import com.ibm.fhir.server.notification.FHIRNotificationService;
import com.ibm.fhir.server.notification.FHIRNotificationSubscriber;

/**
 * This class serves as our websocket "endpoint".
 */
public class FHIRNotificationServiceEndpoint extends Endpoint {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationServiceEndpoint.class.getName());

    /**
     * Currently active notification subscribers
     */
    private static final Map<Session, FHIRNotificationSubscriber> subscribers = new ConcurrentHashMap<Session, FHIRNotificationSubscriber>();

    /**
     * Singleton instance of the FHIR Notification Service
     */
    private final FHIRNotificationService notificationService = FHIRNotificationService.getInstance();

    /**
     * To process new end point client
     * 
     * @param session
     */
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        log.entering(this.getClass().getName(), "onOpen");
        try {
            FHIRNotificationSubscriber subscriber = new FHIRNotificationSubscriberImpl(session);
            notificationService.subscribe(subscriber);
            subscribers.put(session, subscriber);
            log.info(String.format("Notification client [sessionId=%s] has registered.", session.getId()));
        } finally {
            log.exiting(this.getClass().getName(), "onOpen");
        }
    }

    /**
     * Process incoming message
     * 
     * @param message
     * @param session
     * @return
     */
    public String onMessage(String message, Session session) {
        log.entering(this.getClass().getName(), "onMessage");
        try {
            String response = "{\"heartbeat\":\"pong\"}";
            return response;
        } finally {
            log.exiting(this.getClass().getName(), "onMessage");
        }
    }

    /**
     * process message which is trying to disconnect
     * 
     * @param session
     * @param closeReason
     */
    public void onClose(Session session, CloseReason closeReason) {
        log.entering(this.getClass().getName(), "onClose");
        try {
            FHIRNotificationSubscriber subscriber = subscribers.remove(session);
            if (subscriber != null) {
                notificationService.unsubscribe(subscriber);
            }
            log.info(String.format("Notification client [sessionId=%s] has disconnected, reason: %s", session.getId(), closeReason));
            cleanup();
        } finally {
            log.exiting(this.getClass().getName(), "onClose");
        }
    }

    /**
     * Clean up method
     */
    private void cleanup() {
        log.entering(this.getClass().getName(), "cleanup");
        try {
            for (Entry<Session, FHIRNotificationSubscriber> entry : subscribers.entrySet()) {
                if (!entry.getKey().isOpen()) {
                    notificationService.unsubscribe(entry.getValue());
                    subscribers.remove(entry.getKey());
                }
            }
        } finally {
            log.exiting(this.getClass().getName(), "cleanup");
        }
    }
}
