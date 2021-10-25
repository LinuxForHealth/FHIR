/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.notification.websocket;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

import com.ibm.fhir.server.notification.FHIRNotificationEvent;
import com.ibm.fhir.server.notification.FHIRNotificationException;
import com.ibm.fhir.server.notification.FHIRNotificationSubscriber;
import com.ibm.fhir.server.notification.FHIRNotificationUtil;

public class FHIRNotificationSubscriberImpl implements FHIRNotificationSubscriber {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationSubscriberImpl.class.getName());
    private Session session = null;

    public FHIRNotificationSubscriberImpl(Session session) {
        this.session = session;
    }

    public void notify(FHIRNotificationEvent event) throws FHIRNotificationException {
        log.entering(this.getClass().getName(), "notify");
        try {
            String message = FHIRNotificationUtil.toJsonString(event, false);
            if (log.isLoggable(Level.FINE)) { 
                log.fine("Publishing websocket notification event on session [id=" + session.getId() + "],\nmessage:" + message);
            }
            session.getAsyncRemote().sendText(message);
            log.info("Successfully published websocket notification event for resource: " + event.getLocation());
        } catch (Exception e) {
            String msg = "Error publishing websocket notification event to websocket: " + session.getId();
            log.log(Level.SEVERE, msg, e);
            throw new FHIRNotificationException(msg, e);
        } finally {
            log.exiting(this.getClass().getName(), "notify");
        }
    }
}
