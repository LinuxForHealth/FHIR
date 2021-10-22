/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.glassfish.tyrus.core.CloseReasons;

import com.ibm.fhir.server.notification.FHIRNotificationEvent;
import com.ibm.fhir.server.notification.FHIRNotificationUtil;

public class FHIRNotificationServiceClientEndpoint extends Endpoint {
    private boolean DEBUG = false;

    private Session initSession = null;

    private ConcurrentLinkedQueue<String> events = new ConcurrentLinkedQueue<>();

    private CountDownLatch latch = null;

    public FHIRNotificationServiceClientEndpoint() {
        latch = new CountDownLatch(1);
    }

    public void onOpen(Session session, EndpointConfig config) {

        System.out.println(">>> Session opened: " + session.getId());
        System.out.println(">>> Idle Timeout: " + session.getMaxIdleTimeout());

        session.addMessageHandler(new MessageHandler.Whole<Reader>() {

            /*
             * The code is changed from <code>session.addMessageHandler(new MessageHandler.Whole<String>()</code> which
             * parses the Reader to String.
             */
            @Override
            public void onMessage(Reader in) {

                try (BufferedReader br = new BufferedReader(in);) {
                    // The following lines code, can further be broken down using for each.
                    String text = br.lines().collect(Collectors.joining("\n"));

                    if(DEBUG) {
                        System.out.println(">>> Received message: " + text);
                    }
                    // Receive raw message string for better performance, we found that using
                    // FHIRNotificationUtil.toNotificationEvent here could cost up to 10 seconds
                    // for each message during integration test of the CI pipeline.
                    events.add(text);

                } catch (IOException e1) {
                    System.out.println("IO Exception closing the readers");
                    e1.printStackTrace();
                } finally {
                    // JsonReader is already closed here.

                    try {
                        in.close();
                    } catch (IOException e) {
                        System.out.println("IO Exception closing the inputstream");
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    @Override
    public void onError(Session session, Throwable thr) {
        System.out.println(">>> Session error: " + session.getId());
        System.out.println(">>> Stack Trace as follows -> ");
        thr.printStackTrace();
        super.onError(session, thr);
    }

    public void onClose(Session session, CloseReason reason) {
        System.out.println(">>> Session closed: " + session.getId());
        latch.countDown();
    }

    /**
     * close
     */
    public void close() {
        try {
            // Forcing the close of the system.
            initSession.close(CloseReasons.GOING_AWAY.getCloseReason());
        } catch (IOException e) {
            System.err.println(">>> Issue closing the session");
            e.printStackTrace();
        }
    }

    public FHIRNotificationEvent getFirstEvent() {

        if (!events.isEmpty()) {
            return FHIRNotificationUtil.toNotificationEvent(events.remove());
        }
        return null;
    }

    public List<String> getEvents() {
        return events.stream().collect(Collectors.toList());
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public static void main(String[] args) throws Exception {
        int limit = 5;
        int timeout = 5;
        TimeUnit timeUnit = TimeUnit.MINUTES;

        System.out.println("Connecting to server...");
        System.out.println("");

        FHIRNotificationServiceClientEndpoint endpoint =
                new FHIRNotificationServiceClientEndpoint();

        ClientEndpointConfig config =
                ClientEndpointConfig.Builder.create().configurator(new Configurator() {
                    public void beforeRequest(Map<String, List<String>> headers) {
                        String encoding =
                                Base64.getEncoder().encodeToString("fhiruser:change-password".getBytes());
                        List<String> values = new ArrayList<String>();
                        values.add("Basic " + encoding);
                        headers.put("Authorization", values);
                    }
                }).build();

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(endpoint, config, new URI("ws://localhost:9080/fhir-server/api/v4/notification"));
        System.out.println("");
        System.out.println("Connected.");
        System.out.println("");

        String text = "event";
        if (limit > 1) {
            text += "s";
        }

        String unit = timeUnit.toString().toLowerCase();
        unit = unit.substring(0, unit.length() - 1);

        System.out.println("Waiting for " + limit + " " + text + " or " + timeout + " " + unit
                + " timeout period...");
        System.out.println("");

        boolean result = endpoint.getLatch().await(timeout, timeUnit);
        if (result) {
            System.out.println("");
            System.out.println("Disconnected.");
            System.out.println("");
        } else {
            System.out.println(timeout + " " + unit + " timeout period expired.");
            System.out.println("");
        }

        List<String> events = endpoint.getEvents();

        if (events.size() == 0) {
            text = "no events.";
        } else if (events.size() == 1) {
            text = "the following event:";
        } else {
            text = "the following " + events.size() + " events:";
        }

        System.out.println("Client received " + text);
        System.out.println("");
        for (String event : events) {
            FHIRNotificationEvent notifyEvent = FHIRNotificationUtil.toNotificationEvent(event);
            System.out.println("    location:      " + notifyEvent.getLocation());
            System.out.println("    operationType: " + notifyEvent.getOperationType());
            System.out.println("    lastUpdated:   " + notifyEvent.getLastUpdated());
            System.out.println("    resourceId:    " + notifyEvent.getResourceId());
            System.out.println("");
        }
    }

    /**
     * @param session
     */
    public void setSession(Session session) {
        System.out.println(">>> Session created: " + session.getId());
        this.initSession = session;
    }

    /**
     * check for event.
     * 
     * @param eventId
     * @return
     */
    public FHIRNotificationEvent checkForEvent(String eventId) {

        System.out.println(" >>> event total - " + events.size());

        Iterator<String> eventIter = events.iterator();
        while (eventIter.hasNext()) {
            String eventString = eventIter.next();

            FHIRNotificationEvent event = FHIRNotificationUtil.toNotificationEvent(eventString);
            System.out.println(" >>> event - " + event.getResourceId());

            if (eventId.compareTo(event.getResourceId()) == 0) {
                // Wipe out the current value if it matches (and is the first one)
                eventIter.remove();
                return event;
            }
        }
        return null;
    }

}
