/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.api;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_AUDIT_LOGPATH;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_AUDIT_LOG_MAXSIZE;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_AUDIT_LOG_MAX_FILE_COUNT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.audit.logging.impl.LoggerAuditLogService;
import com.ibm.watsonhealth.fhir.audit.logging.impl.WhcAuditLogService;
import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
CODE_REMOVED

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
	private static final long AUDIT_LOG_MAX_SIZE_DEFAULT = 20;
	private static final int  AUDIT_LOG_MAX_FILES_DEFAULT = 50;
	private static final String DEFAULT_AUDIT_LOG_PATH = "logs/";
	private static AuditLogService serviceInstance = null;

	
	/**
	 * Returns the AuditLogService to be used by all FHIR server components.
	 * @return AuditLogService
	 * @throws AuditLoggingException 
	 */
	public static AuditLogService getService() {
				
		if (serviceInstance == null) {
			if (serviceInstance == null) {
				buildWhcAuditLogService();
			}
			return serviceInstance;
		}
		return serviceInstance;
	}
	
	/**
	 * Nulls out the singleton instance of the audit logger service object that is cached by this factory class, 
	 * then creates, caches, and returns a new service object instance.
	 * @return AuditLogService - The newly cached audit log service object.
	 * @throws AuditLoggingException 
	 */
	public static AuditLogService resetService() {
		final String METHODNAME = "resetService";
		log.entering(CLASSNAME, METHODNAME);
		
		serviceInstance = null;
		AuditLogService newService = getService();
		log.exiting(CLASSNAME, METHODNAME);
		return newService;
	}
	
	@Deprecated
	/**
	 * @deprecated - the correct method to call is now buildWhcAuditLogService()
	 * Builds and returns a singleton LoggerAuditLogService implementation object that will be cached by this factory class.
	 * @return AuditLogService - A LoggerAuditLogService implementation.
	 */
	private static synchronized AuditLogService buildLoggerAuditLogService() {
		final String METHODNAME = "buildLoggerAuditLogService";
		log.entering(CLASSNAME, METHODNAME);
		
		String auditLogPath;
		long maxAuditLogSize;
		Logger javaLogger = null;
		FileHandler auditLogFileHandler = null;
		
		if (serviceInstance == null) {
			try {
				auditLogPath = buildLoggerAuditLogPath();
				maxAuditLogSize = acquireMaxAuditLogSize();
							 
				// Create a unique Logger instance for audit logging, using a custom FileHandler and Formatter.
				javaLogger = Logger.getLogger(auditLogPath);
				javaLogger.setLevel(Level.INFO);
				javaLogger.setUseParentHandlers(false);
				auditLogFileHandler = new FileHandler(auditLogPath,true);
				auditLogFileHandler.setFormatter(new LoggerAuditLogService.LoggerFormatter());
				javaLogger.addHandler(auditLogFileHandler);
				 
				serviceInstance = new LoggerAuditLogService(javaLogger, maxAuditLogSize);
				log.info("Initialized Audit logging to file: " + auditLogPath);
			}
			catch (Throwable e) {
				log.severe("Failure creating LoggerAuditLog: " + e.getMessage());
				serviceInstance = new LoggerAuditLogService();
			}  
		}
		log.exiting(CLASSNAME, METHODNAME);
		return serviceInstance;
	}
	
	/**
	 * Builds and returns a singleton WhcAuditLogService implementation object that will be cached by this factory class.
	 * @return AuditLogService - A WhcAuditLogService implementation.
	 */
	private static synchronized AuditLogService buildWhcAuditLogService() {
		final String METHODNAME = "buildWhcAuditLogService";
		log.entering(CLASSNAME, METHODNAME);
		
		String auditLogPath;
		int maxAuditLogSize;
		int maxFileCount;
		AuditLogger whcLogger;
		final String fileNamePattern = "fhiraudit.%g-%u.log";
				
		if (serviceInstance == null) {
			try {
				auditLogPath = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_LOGPATH, DEFAULT_AUDIT_LOG_PATH);
				maxAuditLogSize =  (int)acquireMaxAuditLogSize();
				maxFileCount = acquireMaxAuditLogFileCount();
							 
				whcLogger = new AuditLogger(auditLogPath, fileNamePattern, maxAuditLogSize, maxFileCount);
				serviceInstance = new WhcAuditLogService(whcLogger);
CODE_REMOVED
						  " maxAuditLogSize=" + maxAuditLogSize + " maxFileCount=" + maxFileCount);
			}
			catch (Throwable e) {
CODE_REMOVED
				serviceInstance = new WhcAuditLogService();
			}  
		}
		log.exiting(CLASSNAME, METHODNAME);
		return serviceInstance;
	}
	
	/**
	 * Builds and returns the complete path name of the audit log file to be used by the java logger audit log service.
	 * @return String the audit log file path.
	 */
	@Deprecated
	private static String buildLoggerAuditLogPath() {
		final String METHODNAME = "buildLoggerAuditLogPath";
		log.entering(CLASSNAME, METHODNAME);
		
		String currentDateTime;
		String auditLogDir = null;
		StringBuilder auditLogPath = new StringBuilder();
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		currentDateTime = dateTimeFormatter.format(new Date(System.currentTimeMillis()));
		String fileSeparator = System.getProperty("file.separator");
		
		try {
		    auditLogDir = FHIRConfiguration.getInstance().loadConfiguration().getStringProperty(PROPERTY_AUDIT_LOGPATH);
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
	
	/**
	 * Acquire the maximum audit log size via a configuration lookup. If the max log size is not specified or is not a valid
	 * integer, return a default value.
	 * @return long - The maximum audit log size, in bytes.
	 */
	private static long acquireMaxAuditLogSize() {
		final String METHODNAME = "acquireMaxAuditLogSize";
		log.entering(CLASSNAME, METHODNAME);
		
		final long MB = 1024 * 1024;
		long maxAuditLogSize = AUDIT_LOG_MAX_SIZE_DEFAULT * MB;
		int maxAuditLogMB;
		
		try {
			maxAuditLogMB = FHIRConfiguration.getInstance().loadConfiguration().getIntProperty(PROPERTY_AUDIT_LOG_MAXSIZE);
			maxAuditLogSize = maxAuditLogMB * MB;
		}
		catch(Throwable t) {
			log.fine("Could not obtain audit log max file size via configuration.");
		}
		
		
		log.exiting(CLASSNAME, METHODNAME, maxAuditLogSize);
		return maxAuditLogSize;
	}
	
	/**
	 * Acquire the maximum number of audit log files via a configuration lookup. If the max audit log file count is not specified or is not a valid
	 * integer, return a default value.
	 * When the maximum number of audit log files is reached, log recording reuses the oldest audit log file previously allocated.
	 * @return long - The audit log file count.
	 */
	private static int acquireMaxAuditLogFileCount() {
		final String METHODNAME = "acquireMaxAuditLogFileCount";
		log.entering(CLASSNAME, METHODNAME);
		
		int maxFileCount = AUDIT_LOG_MAX_FILES_DEFAULT;
		
		try {
			maxFileCount = FHIRConfigHelper.getIntProperty(PROPERTY_AUDIT_LOG_MAX_FILE_COUNT, AUDIT_LOG_MAX_FILES_DEFAULT);
		}
		catch(Throwable t) {
			log.fine("Could not obtain audit log max file count via configuration.");
		}
		
		
		log.exiting(CLASSNAME, METHODNAME, maxFileCount);
		return maxFileCount;
	}

}
