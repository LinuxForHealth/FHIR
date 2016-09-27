/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.api;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_AUDIT_LOGPATH;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.audit.logging.impl.DisabledAuditLogService;
import com.ibm.watsonhealth.fhir.audit.logging.impl.LoggerAuditLogService;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;

/**
 * Instantiates and returns an implementation of the FHIR server audit log service.
 * @author markd
 *
 */
public class AuditLogServiceFactory {
	
	private static final Logger log = java.util.logging.Logger.getLogger(AuditLogServiceFactory.class.getName());
	private static final String CLASSNAME = AuditLogServiceFactory.class.getName();
	
	private static final String AUDIT_LOG_FILENAME_PREFIX = "fhiraudit.";
	private static final String AUDIT_LOG_FILENAME_SUFFIX = ".log";
	private static AuditLogService serviceInstance = null;

	
	/**
	 * Returns the AuditLogService to be used by all FHIR server components.
	 * @return AuditLogService
	 */
	public static AuditLogService getService() {
		
		String auditLogPath;
		
		if (serviceInstance == null) {
			auditLogPath = buildAuditLogPath();
			if (!auditLogPath.isEmpty()) {
				serviceInstance = getLoggerAuditLogService(auditLogPath);
			}
			else {
				serviceInstance = new DisabledAuditLogService();
			}
		}
		return serviceInstance;

	}
	
	/**
	 * Creates an audit log service that uses the java.util.logging package to record audit log entries.
	 * @return AuditLogService - An audit log service that delegates to java.util.logging.
	 */
	private static AuditLogService getLoggerAuditLogService(String auditLogFilePath) {
		final String METHODNAME = "getLoggerAuditLogService";
		log.entering(CLASSNAME, METHODNAME);
		
		Logger javaLogger = null;
		FileHandler auditLogFileHandler = null;
				 
		try {
			// Create a unique Logger instance for audit logging, using a custom FileHandler and Formatter.
			javaLogger = Logger.getLogger(AuditLogService.class.getName());
			javaLogger.setLevel(Level.INFO);
			javaLogger.setUseParentHandlers(false);
			auditLogFileHandler = new FileHandler(auditLogFilePath,true);
			auditLogFileHandler.setFormatter(new LoggerAuditLogService.LoggerFormatter());
			javaLogger.addHandler(auditLogFileHandler);
			 
			serviceInstance = new LoggerAuditLogService(javaLogger);
			log.info("Initialized Audit logging to file: " + auditLogFilePath);
		}
		catch (Throwable e) {
			log.severe("Failure creating LoggerAuditLog: " + e.getMessage());
			serviceInstance = new LoggerAuditLogService();
		}  
	     
		log.exiting(CLASSNAME, METHODNAME);
		return serviceInstance;
	}
	
	/**
	 * Builds and returns the complete path name of the audit log file to be used by a file-based audit log service.
	 * @return String the audit log file path.
	 */
	private static String buildAuditLogPath() {
		final String METHODNAME = "buildAuditLogPath";
		log.entering(CLASSNAME, METHODNAME);
		
		String currentDateTime;
		String auditLogDir = null;
		StringBuilder auditLogPath = new StringBuilder();
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		currentDateTime = dateTimeFormatter.format(new Date(System.currentTimeMillis()));
		String fileSeparator = System.getProperty("file.separator");
		
		try {
		    auditLogDir = FHIRConfiguration.loadConfiguration().getStringProperty(PROPERTY_AUDIT_LOGPATH);
		}
		catch(Throwable t) {
			// Use the JVM default temp directory. This should only end up being used when running in junit or testNg mode.
			log.fine("Could not obtain audit log path via configuration.");
		}
		
		if (auditLogDir != null && !auditLogDir.isEmpty()) {
			if (!auditLogDir.endsWith(fileSeparator)) {
				auditLogDir = auditLogDir + fileSeparator;
			}
			// Construct the full path name.
			auditLogPath.append(auditLogDir)
						.append(AUDIT_LOG_FILENAME_PREFIX)
						.append(currentDateTime)
						.append(AUDIT_LOG_FILENAME_SUFFIX);
		}
		 
		log.exiting(CLASSNAME, METHODNAME);
		return auditLogPath.toString();
	}

}
