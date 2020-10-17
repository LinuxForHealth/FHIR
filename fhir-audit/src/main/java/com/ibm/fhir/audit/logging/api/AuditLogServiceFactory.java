/*
 * (C) Copyright IBM Corp. 2016,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.logging.api;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.owasp.encoder.Encode;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;

/**
 * Manages access to the IBM FHIR Server AuditLogService.
 *
 * @implNote The Audit Log Service is not started UNTIL the first REST request executes
 *           an action that needs to be logged.
 */
public class AuditLogServiceFactory {

    private static final Logger log = java.util.logging.Logger.getLogger(AuditLogServiceFactory.class.getName());
    private static final String CLASSNAME = AuditLogServiceFactory.class.getName();

    private static final Map<String, String> PRIOR_VERSION_CLASSNAME = generateMap();

    // To prevent java writing re-order issue of the Double check locking
    // singleton, using volatile for the service instance. or we can use the
    // Eager initialization pattern or inner static classes pattern for this
    // singleton
    private static volatile AuditLogService serviceInstance = null;

    /**
     * Gets the AuditLogService to be used by all IBM FHIR Server components.
     *
     * @return AuditLogService
     */
    public static AuditLogService getService() {
        if (serviceInstance == null) {
            createService();
        }
        return serviceInstance;
    }

    /*
     * generates the map from prior versions to the current naming scheme
     * and maintains backwards compatibility.
     */
    private static Map<String, String> generateMap() {
        Map<String, String> mapped = new HashMap<>(2);
        mapped.put("com.ibm.fhir.audit.logging.impl.DisabledAuditLogService", "com.ibm.fhir.audit.logging.api.impl.NoOpService");
        mapped.put("com.ibm.fhir.audit.logging.impl.WhcAuditCadfLogService", "com.ibm.fhir.audit.logging.api.impl.KafkaService");
        return mapped;
    }

    /*
     * used to remap from prior versions
     */
    private static String remap(String inputName) {
        if (PRIOR_VERSION_CLASSNAME.containsKey(inputName)) {
            return PRIOR_VERSION_CLASSNAME.get(inputName);
        }
        return inputName;
    }

    /*
     * Creates and caches a singleton instance of an audit log service
     * based on audit log configuration settings.
     */
    private static synchronized void createService() {
        final String METHODNAME = "createService";
        log.entering(CLASSNAME, METHODNAME);

        if (serviceInstance == null) {
            String serviceClassName;

            PropertyGroup auditLogProperties;

            StringBuilder errMsg = new StringBuilder();
            serviceClassName = remap(FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_CLASS_NAME, null));
            if (serviceClassName == null || serviceClassName.isEmpty()) {
                errMsg.append("Audit log service class name not configured.");
            } else {
                try {
                    Class<?> serviceClass = Class.forName(serviceClassName);
                    if (AuditLogService.class.isAssignableFrom(serviceClass)) {
                        try {
                            serviceInstance = (AuditLogService) serviceClass.newInstance();
                            auditLogProperties = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
                            serviceInstance.initialize(auditLogProperties);
                            log.info("Successfully initialized audit log service: '" + serviceClassName + "'");
                        } catch (IllegalAccessException | InstantiationException e) {
                            errMsg.append("Could not instantiate ").append(serviceClassName).append(System.lineSeparator()).append(e.toString()).append('\'');
                        } catch (Throwable e) {
                            errMsg.append("Failure initializing audit log service: '").append(serviceClassName).append(System.lineSeparator()).append(e.toString()).append('\'');
                        }
                    } else {
                        errMsg.append("Audit log service class does not implement the AuditLogService interface.");
                    }
                } catch (ClassNotFoundException e) {
                    errMsg.append("Audit log service class name not found: '").append(serviceClassName).append('\'');
                }
            }
            if (errMsg.length() > 0) {
                errMsg.append("   ").append("Audit logging is disabled.");
                log.severe(Encode.forHtml(errMsg.toString()));

                // Have to exit if fails to start audit
                // <pre>serviceInstance = new DisabledAuditLogService();</pre>
                System.exit(1);
            }
        }
        log.exiting(CLASSNAME, METHODNAME);
    }
}