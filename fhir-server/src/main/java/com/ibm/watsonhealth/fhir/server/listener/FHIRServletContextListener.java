/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.server.ServerContainer;

import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;
import com.ibm.watsonhealth.fhir.server.notification.endpoint.FHIRNotificationServiceEndpointConfig;
import com.ibm.watsonhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.validation.Validator;

@WebListener
public class FHIRServletContextListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(FHIRServletContextListener.class.getName());
	
	private static final String ATTRNAME_WEBSOCKET_SERVERCONTAINER = "javax.websocket.server.ServerContainer";
	private static final String JNDINAME_WEBSOCKET_ENABLED = "com.ibm.watsonhealth.fhir.notification.websocket.enabled";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (log.isLoggable(Level.FINER)) {
			log.entering(FHIRServletContextListener.class.getName(), "contextInitialized");
		}
		try {
			// For any singleton resources that need to be shared among our resource class instances,
		    // we'll add them to our servlet context so that the resource class can easily retrieve them.
		    
		    // Set the shared Validator.
			event.getServletContext().setAttribute(Validator.class.getName(), new Validator());
			log.fine("Set shared validator on servlet context.");
			
			// Set the shared FHIRPersistenceHelper.
            event.getServletContext().setAttribute(FHIRPersistenceHelper.class.getName(), new FHIRPersistenceHelper());
            log.fine("Set shared persistence helper on servlet context.");

            // Set the shared interceptor mgr.
            event.getServletContext().setAttribute(FHIRPersistenceInterceptorMgr.class.getName(), FHIRPersistenceInterceptorMgr.getInstance());
            log.fine("Set shared persistence interceptor mgr on servlet context.");
            
            // Grab the "websocket.enabled" jndi value.
            Boolean websocketEnabled = getBooleanJNDIValue(JNDINAME_WEBSOCKET_ENABLED, Boolean.FALSE);
            
            // If configured, start up our WebSocket endpoint for notifications.
            if (websocketEnabled) {
                log.info("Opening websocket for notifications.");
                ServerContainer container = (ServerContainer) event.getServletContext().getAttribute(ATTRNAME_WEBSOCKET_SERVERCONTAINER);
                container.addEndpoint(new FHIRNotificationServiceEndpointConfig());
            } else {
                log.info("Bypassing websocket notification initialization.");
            }

		} catch (Exception e) {
			// ignore exceptions here
		} finally {
			if (log.isLoggable(Level.FINER)) {
				log.exiting(FHIRServletContextListener.class.getName(), "contextInitialized");
			}
		}
	}

    @Override
	public void contextDestroyed(ServletContextEvent event) {
		if (log.isLoggable(Level.FINER)) {
			log.entering(FHIRServletContextListener.class.getName(), "contextDestroyed");
		}
		try {
			// TODO
		} catch (Exception e) {
		} finally {
			if (log.isLoggable(Level.FINER)) {
				log.exiting(FHIRServletContextListener.class.getName(), "contextDestroyed");
			}
		}
	}

    /**
     * Retrieves the specified JNDI entry and interprets it as a Boolean value.
     * @param jndiName the name of the JNDI entry to search for
     * @param defaultValue the defaultValue to be returned if the JNDI entry isn't found
     */
    private Boolean getBooleanJNDIValue(String jndiName, Boolean defaultValue) {
        Boolean result = defaultValue;
        try {
            InitialContext ctx = new InitialContext();
            Boolean jndiValue = (Boolean) ctx.lookup(jndiName);
            if (jndiValue != null ) {
                log.fine("JNDI entry " + jndiName + "=" + jndiValue);
                result = jndiValue;
            }
        } catch (Throwable t) {
            // Ignore any exceptions while looking up the JNDI entry.
            log.fine("Caught exception while looking up JNDI entry " + jndiName + ": " + t);
        }
        return result;
    }
    
}
