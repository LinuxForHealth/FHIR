/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notification.websocket.impl;

import java.util.logging.Logger;
import javax.websocket.Session;
import com.ibm.watsonhealth.fhir.notification.FHIRNotificationSubscriber;
import com.ibm.watsonhealth.fhir.notification.exception.FHIRNotificationException;
import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationEvent;
import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationUtil;

public class FHIRNotificationSubscriberImpl implements FHIRNotificationSubscriber {
    private static final Logger log = java.util.logging.Logger.getLogger(FHIRNotificationSubscriberImpl.class.getName());
    private Session session = null;
    
    public FHIRNotificationSubscriberImpl(Session session) {
        this.session = session;
    }
    
    public void notify(FHIRNotificationEvent event) throws FHIRNotificationException {
    	log.entering(this.getClass().getName(), "notify");
        try {
            String message = FHIRNotificationUtil.toJsonString(event);
            log.info("Session ID" + session.getId() + "message:" + message);
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            throw new FHIRNotificationException("Error sending message to WebSocket :" + session.getId(), e);
        }finally {
        	log.exiting(this.getClass().getName(), "notify");
        }
    }
}
