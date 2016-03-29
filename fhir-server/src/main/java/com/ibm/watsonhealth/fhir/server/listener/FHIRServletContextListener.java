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

import com.ibm.watsonhealth.fhir.validation.Validator;

@WebListener
public class FHIRServletContextListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(FHIRServletContextListener.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (logger.isLoggable(Level.FINE)) {
			logger.entering(FHIRServletContextListener.class.getName(), "contextInitialized");
		}
		try {
			// put a validator instance in the servlet context so that it can be shared
			// between FHIRResource class instances
			event.getServletContext().setAttribute(Validator.class.getName(), new Validator());
		} catch (Exception e) {
			
		} finally {
			if (logger.isLoggable(Level.FINE)) {
				logger.exiting(FHIRServletContextListener.class.getName(), "contextInitialized");
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (logger.isLoggable(Level.FINE)) {
			logger.entering(FHIRServletContextListener.class.getName(), "contextDestroyed");
		}
		try {
			// TODO
		} catch (Exception e) {
		} finally {
			if (logger.isLoggable(Level.FINE)) {
				logger.exiting(FHIRServletContextListener.class.getName(), "contextDestroyed");
			}
		}
	}
}
