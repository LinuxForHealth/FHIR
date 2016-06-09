/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.listener;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.server.ServerContainer;

import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.notification.websocket.impl.FHIRNotificationServiceEndpointConfig;
import com.ibm.watsonhealth.fhir.notifications.kafka.impl.FHIRNotificationKafkaPublisher;
import com.ibm.watsonhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

@WebListener("IBM Watson Health FHIR Server Servlet Context Listener")
public class FHIRServletContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(FHIRServletContextListener.class.getName());
	
	private static final String ATTRNAME_WEBSOCKET_SERVERCONTAINER = "javax.websocket.server.ServerContainer";
	private static final String JNDINAME_WEBSOCKET_ENABLED = "com.ibm.watsonhealth.fhir.notification.websocket.enabled";
    private static final String JNDINAME_KAFKA_ENABLED = "com.ibm.watsonhealth.fhir.notification.kafka.enabled";
    private static final String JNDINAME_KAFKA_TOPICNAME = "com.ibm.watsonhealth.fhir.notification.kafka.topicName";
    private static final String JNDINAME_KAFKA_CONNINFO = "com.ibm.watsonhealth.fhir.notification.kafka.connectionInfo";

    private static final String DEFAULT_KAFKA_TOPICNAME = "fhirNotifications";
    private static final String DEFAULT_KAFKA_CONNINFO = "localhost:9092";
    
	private static final String JNDINAME_ALLOWABLE_VIRTUAL_RESOURCE_TYPES = "com.ibm.watsonhealth.fhir.allowable.virtual.resource.types";
    private static final String JNDINAME_VIRTUAL_RESOURCE_TYPES_FEATURE_ENABLED = "com.ibm.watsonhealth.fhir.virtual.resource.types.feature.enabled";
    private static FHIRNotificationKafkaPublisher kafkaPublisher = null;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (log.isLoggable(Level.FINER)) {
			log.entering(FHIRServletContextListener.class.getName(), "contextInitialized");
		}
		try {
		    log.fine("Initializing FHIRUtil...");
		    FHIRUtil.init();
		    
		    log.fine("Initializing SearchUtil...");
		    SearchUtil.init();
		    
			// For any singleton resources that need to be shared among our resource class instances,
		    // we'll add them to our servlet context so that the resource class can easily retrieve them.
		    
			// Set the shared FHIRPersistenceHelper.
            event.getServletContext().setAttribute(FHIRPersistenceHelper.class.getName(), new FHIRPersistenceHelper());
            log.fine("Set shared persistence helper on servlet context.");

            event.getServletContext().setAttribute(JNDINAME_ALLOWABLE_VIRTUAL_RESOURCE_TYPES, lookupAllowableVirtualResourceTypes());
            log.fine("Set shared list of allowable virtual resource types.");
            
            Boolean virtualResourceTypesEnabled = getJNDIValue(JNDINAME_VIRTUAL_RESOURCE_TYPES_FEATURE_ENABLED, Boolean.FALSE);
            event.getServletContext().setAttribute(JNDINAME_VIRTUAL_RESOURCE_TYPES_FEATURE_ENABLED, virtualResourceTypesEnabled);
            log.fine("Set shared virtual resource types enabled flag.");
            
            // Grab the "websocket.enabled" jndi value.
            Boolean websocketEnabled = getJNDIValue(JNDINAME_WEBSOCKET_ENABLED, Boolean.FALSE);
            
            // If configured, start up our WebSocket endpoint for notifications.
            if (websocketEnabled) {
                log.info("Initializing WebSocket notification publisher.");
                ServerContainer container = (ServerContainer) event.getServletContext().getAttribute(ATTRNAME_WEBSOCKET_SERVERCONTAINER);
                container.addEndpoint(new FHIRNotificationServiceEndpointConfig());
            } else {
                log.info("Bypassing WebSocket notification init.");
            }
            
            // Grab the "kafka.enabled" jndi value.
            Boolean kafkaEnabled = getJNDIValue(JNDINAME_KAFKA_ENABLED, Boolean.FALSE);
            
            // If configured, start up our Kafka notification publisher.
            if (kafkaEnabled) {
                log.info("Initializing Kafka notification publisher.");
                String topicName = getJNDIValue(JNDINAME_KAFKA_TOPICNAME, DEFAULT_KAFKA_TOPICNAME);
                String connectionInfo = getJNDIValue(JNDINAME_KAFKA_CONNINFO, DEFAULT_KAFKA_CONNINFO);
                kafkaPublisher = new FHIRNotificationKafkaPublisher(topicName, connectionInfo);
            } else {
                log.info("Bypassing Kafka notification init.");
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
		    
		    // If we previously intialized the Kafka publisher, then shut it down now.
		    if (kafkaPublisher != null) {
		        kafkaPublisher.shutdown();
		        kafkaPublisher = null;
		    }
		} catch (Exception e) {
		} finally {
			if (log.isLoggable(Level.FINER)) {
				log.exiting(FHIRServletContextListener.class.getName(), "contextDestroyed");
			}
		}
	}

    /**
     * Retrieves the specified JNDI entry and interprets it as a value of type "T".
     * @param jndiName the name of the JNDI entry to search for
     * @param defaultValue the defaultValue to be returned if the JNDI entry isn't found
     */
    @SuppressWarnings("unchecked")
    private <T> T getJNDIValue(String jndiName, T defaultValue) {
        T result = defaultValue;
        try {
            InitialContext ctx = new InitialContext();
            T jndiValue = (T) ctx.lookup(jndiName);
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
    
    private List<String> lookupAllowableVirtualResourceTypes() {
        String s = getJNDIValue(JNDINAME_ALLOWABLE_VIRTUAL_RESOURCE_TYPES, "*");
        return Arrays.asList(s.split(","));
    }
}
