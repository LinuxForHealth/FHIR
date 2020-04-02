/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.notifications.nats.impl;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.nats.streaming.AckHandler;
import io.nats.streaming.Options;
import io.nats.streaming.StreamingConnection;
import io.nats.streaming.StreamingConnectionFactory;

import com.ibm.fhir.notification.FHIRNotificationEvent;
import com.ibm.fhir.notification.FHIRNotificationService;
import com.ibm.fhir.notification.FHIRNotificationSubscriber;
import com.ibm.fhir.notification.exception.FHIRNotificationException;
import com.ibm.fhir.notification.util.FHIRNotificationUtil;

/**
 * This class implements the FHIR server notification service via a NATS channel.
 */
public class FHIRNotificationNATSPublisher implements FHIRNotificationSubscriber {
    private static final Logger log = Logger.getLogger(FHIRNotificationNATSPublisher.class.getName());
    private static FHIRNotificationService service = FHIRNotificationService.getInstance();

    private StreamingConnection sc = null;
    private AckHandler acb = null;
    private String channelName = null;

    // "Hide" the default constructor.
    protected FHIRNotificationNATSPublisher() {
    }

    public FHIRNotificationNATSPublisher(String clusterId, String channelName, String servers) {
        log.entering(this.getClass().getName(), "constructor");
        try {
            init(clusterId, channelName, servers);
        } finally {
            log.exiting(this.getClass().getName(), "constructor");
        }
    }

    /**
     * Performs any required initialization to allow us to publish events to the channel.
     */
    private void init(String clusterId, String channelName, String servers) {
        log.entering(this.getClass().getName(), "init");

        try {
            this.channelName = channelName;
            String clientId = "fhir-server";
            if (log.isLoggable(Level.FINER)) {
                log.finer("ClusterId: " + clusterId);
                log.finer("Channel name: " + channelName);
                log.finer("Servers: " + servers);
            }

            // Make sure that the properties file contains the servers property at a minimum.
            if (servers == null || clusterId == null) {
                throw new IllegalStateException("Config property missing from the NATS connection properties.");
            }

            // Create the NATS connection
            Options streamingOptions = new Options.Builder().natsUrl(servers).clusterId(clusterId).clientId(clientId).build();
            StreamingConnectionFactory cf = new StreamingConnectionFactory(streamingOptions);
            sc = cf.createConnection();

            // Create the publish callback
            acb = new AckHandler() {
                @Override
                public void onAck(String nuid, Exception ex) {
                    log.finer("Received ACK for guid: " + nuid);
                    if (ex != null) {
                        log.log(Level.SEVERE, "Error in server ack for guid " + nuid + ": " + ex.getMessage(), ex);
                    }
                    // TODO: Do something else here
                }
            };

            // Register this NATS implementation as a "subscriber" with our Notification Service.
            // This means that our "notify" method will be called when the server publishes an event.
            service.subscribe(this);
            log.info("Initialized NATS publisher for channel '" + channelName + "' using servers: " + servers + ".");
        } catch (Throwable t) {
            String msg = "Caught exception while initializing NATS publisher.";
            log.log(Level.SEVERE, msg, t);
            throw new IllegalStateException(msg, t);
        } finally {
            log.exiting(this.getClass().getName(), "init");
        }
    }

    /**
     * Performs any necessary "shutdown" logic to disconnect from the channel.
     */
    public void shutdown() {
        log.entering(this.getClass().getName(), "shutdown");

        try {
            if (log.isLoggable(Level.FINE)) {   
                log.fine("Shutting down NATS publisher for channel: '" + channelName + "'.");
            }
            if (sc != null) {
               sc.close();
            }
        } catch (Throwable t) {
            String msg = "Caught exception shutting down NATS publisher for channel: '" + channelName + "'.";
            log.log(Level.SEVERE, msg, t);
            throw new IllegalStateException(msg, t);
        } finally {
            log.exiting(this.getClass().getName(), "shutdown");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.fhir.notification.FHIRNotificationSubscriber#notify(com.ibm.fhir.notification.
     * util.FHIRNotificationEvent)
     */
    @Override
    public void notify(FHIRNotificationEvent event) throws FHIRNotificationException {
        log.entering(this.getClass().getName(), "notify");
        String jsonString = null;
        try {
            jsonString = FHIRNotificationUtil.toJsonString(event, true);

            if (log.isLoggable(Level.FINE)) { 
                log.fine("Publishing NATS notification event to channel '" + channelName + "',\nmessage: " + jsonString);
            }
            
            sc.publish("FHIRNotificationEvent", jsonString.getBytes(), acb);
    
            if (log.isLoggable(Level.FINE)) {
                log.fine("Published NATS notification event to channel '" + channelName + "'");
            }
        } catch (Throwable e) {
            String msg = buildNotificationErrorMessage(channelName, (jsonString == null ? "<null>" : jsonString));
            log.log(Level.SEVERE, msg , e);
            throw new FHIRNotificationException(msg, e);
        } finally {
            log.exiting(this.getClass().getName(), "notify");
        }
    }
    
    /**
     * Builds a formatted error message to indicate a notification publication failure.
     */
    private String buildNotificationErrorMessage(String channelName, String notificationEvent) {
        return String.format("NATS publication failure; channel '%s'\nNotification event: %s\n.", channelName, notificationEvent);
    }
}
