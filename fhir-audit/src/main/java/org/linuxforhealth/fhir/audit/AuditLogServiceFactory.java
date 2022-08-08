/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit;

import java.util.logging.Logger;

import org.owasp.encoder.Encode;

import org.linuxforhealth.fhir.audit.configuration.ConfigurationTranslator;
import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.PropertyGroup;

/**
 * Instantiates and returns an implementation of the FHIR server audit log service.
 */
public class AuditLogServiceFactory {

    private static final Logger log = java.util.logging.Logger.getLogger(AuditLogServiceFactory.class.getName());
    private static final String CLASSNAME = AuditLogServiceFactory.class.getName();

    private static final ConfigurationTranslator translator = new ConfigurationTranslator();

    // To prevent java writing re-order issue of the Double check locking singleton, using volatile
    // for the service instance. or we can use the Eager initialization pattern or
    // inner static classes pattern for this singleton
    private static volatile AuditLogService serviceInstance = null;

    /**
     * Returns the AuditLogService to be used by all FHIR server components.
     * @return AuditLogService
     * @throws AuditLoggingException
     */
    public static AuditLogService getService() {

        if (serviceInstance == null) {
            createService();
        }

        return serviceInstance;
    }

    /**
     * Creates and caches a singleton instance of an audit log service, based on audit log configuration settings.
     */
    private static synchronized void createService() {
        final String METHODNAME = "createService";
        log.entering(CLASSNAME, METHODNAME);


        final String NEWLINE = System.getProperty("line.separator");
        String serviceClassName;
        Class<?> serviceClass;
        StringBuilder errMsg;
        PropertyGroup auditLogProperties;

        if (serviceInstance == null) {
            errMsg = new StringBuilder();
            serviceClassName = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_CLASS_NAME, null);
            if (serviceClassName == null || serviceClassName.isEmpty()) {
                errMsg.append("Audit log service class name not configured.");
            } else {
                serviceClassName = translator.remap(serviceClassName);
                try {
                    serviceClass = Class.forName(serviceClassName);
                    if (AuditLogService.class.isAssignableFrom(serviceClass)) {
                        try {
                            serviceInstance = (AuditLogService)serviceClass.newInstance();
                            auditLogProperties = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
                            serviceInstance.initialize(auditLogProperties);
                            log.info("Successfully initialized audit log service: " + serviceClassName);
                        } catch(IllegalAccessException | InstantiationException e) {
                            errMsg.append("Could not instantiate " + serviceClassName + NEWLINE + e.toString());
                        } catch(Throwable e) {
                            errMsg.append("Failure initializing audit log service: " + serviceClassName + NEWLINE + e.toString());
                        }
                    } else {
                        errMsg.append("Audit log service class does not implement AuditLogService interface.");
                    }
                } catch (ClassNotFoundException e) {
                    errMsg.append("Audit log service class name not found: " + serviceClassName);
                }
            }
            if (errMsg.length() > 0) {
                errMsg.append("   ").append("Audit logging is disabled.");
                log.severe(Encode.forHtml(errMsg.toString()));

                // Have to exit if fails to start audit
                //<pre>serviceInstance = new DisabledAuditLogService();</pre>
                System.exit(1);
            }
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Resets the singleton instance of the audit logger service object that is cached by this factory class,
     * then creates, caches, and returns a new service object instance.
     *
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
}