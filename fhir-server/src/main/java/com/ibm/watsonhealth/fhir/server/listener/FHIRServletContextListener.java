/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ibm.watsonhealth.fhir.server.helper.FHIRPersistenceHelper;
import com.ibm.watsonhealth.fhir.validation.Validator;

@WebListener
public class FHIRServletContextListener implements ServletContextListener {
	private static final Logger log = Logger.getLogger(FHIRServletContextListener.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (log.isLoggable(Level.FINE)) {
			log.entering(FHIRServletContextListener.class.getName(), "contextInitialized");
		}
		try {
			// For any singleton resources that need to be shared among our resource class instances,
		    // we'll add them to our servlet context so that the resource class can easily retrieve them.
		    
		    // Set the shared Validator.
			event.getServletContext().setAttribute(Validator.class.getName(), new Validator());
			
			// Set the shared FHIRPersistenceHelper.
            event.getServletContext().setAttribute(FHIRPersistenceHelper.class.getName(), new FHIRPersistenceHelper());
		} catch (Exception e) {
			// ignore exceptions here
		} finally {
			if (log.isLoggable(Level.FINE)) {
				log.exiting(FHIRServletContextListener.class.getName(), "contextInitialized");
			}
		}
	}

    @Override
	public void contextDestroyed(ServletContextEvent event) {
		if (log.isLoggable(Level.FINE)) {
			log.entering(FHIRServletContextListener.class.getName(), "contextDestroyed");
		}
		try {
			// TODO
		} catch (Exception e) {
		} finally {
			if (log.isLoggable(Level.FINE)) {
				log.exiting(FHIRServletContextListener.class.getName(), "contextDestroyed");
			}
		}
	}
}
