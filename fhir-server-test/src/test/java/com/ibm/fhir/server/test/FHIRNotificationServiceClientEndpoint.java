/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;

import com.ibm.fhir.notification.FHIRNotificationEvent;
import com.ibm.fhir.notification.util.FHIRNotificationUtil;

import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class FHIRNotificationServiceClientEndpoint extends Endpoint {
    private Session session = null;    
    private List<String> events = null;
    
    private CountDownLatch latch = null;    
    private int limit = 1;
    
    public FHIRNotificationServiceClientEndpoint() {
        this(1);
    }
    
    public FHIRNotificationServiceClientEndpoint(int limit) {
        latch = new CountDownLatch(1);
        this.limit = limit;
        events = Collections.synchronizedList(new ArrayList<String>(limit));
    }
    
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println(">>> Session opened: " + session.getId());
        this.session = session;
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String text) {
                System.out.println(">>> Received message: " + text);
                //Receive raw message string for better performance, we found that using 
                // FHIRNotificationUtil.toNotificationEvent here could cost up to 10 seconds 
                // for each message during integration test of the CI pipeline.
                events.add(text);
                if (events.size() >= limit) {
                    close();
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
    
    public FHIRNotificationEvent getFirstEvent() {
        List<String> view = getEvents();
        if (!view.isEmpty() && view.get(0) != null) {
            return FHIRNotificationUtil.toNotificationEvent(view.get(0));
        } 
        return null;
    }
    
    public List<String> getEvents() {
        return Collections.unmodifiableList(new ArrayList<String>(events));
    }
    
    public int getLimit() {
        return limit;
    }
    
    public CountDownLatch getLatch() {
        return latch;
    }
    
    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        int limit = 5;
        int timeout = 5;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        
        System.out.println("Connecting to server...");
        System.out.println("");
        
        FHIRNotificationServiceClientEndpoint endpoint = new FHIRNotificationServiceClientEndpoint(limit);
        
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(new Configurator() {
            public void beforeRequest(Map<String, List<String>> headers) {
                String encoding = Base64.getEncoder().encodeToString("fhiruser:fhiruser".getBytes());
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
        
        System.out.println("Waiting for " + limit + " " + text + " or " + timeout + " " + unit + " timeout period...");
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
}
