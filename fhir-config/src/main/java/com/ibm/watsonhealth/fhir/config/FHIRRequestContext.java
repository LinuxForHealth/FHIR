/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to hold FHIR REST API context information.
 * There are non-static methods for maintaining individual instances, as 
 * well as static methods for setting instances on and getting instances from thread local.
 * The overall strategy is for the REST API servlet filter to retrieve the request context
 * information, create an instance of this class and set it on the current thread for use
 * by the FHIR Server as it processes the request.
 */
public class FHIRRequestContext {
    private static final Logger log = Logger.getLogger(FHIRRequestContext.class.getName());

    private String tenantId;
    private String dataStoreId;
    private String requestUniqueId;
    
    private static ThreadLocal<FHIRRequestContext> contexts = new ThreadLocal<FHIRRequestContext>() {
        @Override
        public FHIRRequestContext initialValue() {
            return new FHIRRequestContext(FHIRConfiguration.DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_DATASTORE_ID);
        }
    };
    
    public FHIRRequestContext() {
        super();
        this.requestUniqueId = UUID.randomUUID().toString();
    }
    
    public FHIRRequestContext(String tenantId) {
        this();
        setTenantId(tenantId);
    }
    
    public FHIRRequestContext(String tenantId, String dataStoreId) {
        this(tenantId);
        setDataStoreId(dataStoreId);
    }
    
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    public String getDataStoreId() {
        return dataStoreId;
    }
    public void setDataStoreId(String dataStoreId) {
        this.dataStoreId = dataStoreId;
    }
    
    /**
     * Sets the specified FHIRRequestContext instance on the current thread,
     * so that it can be used by FHIR Server downstream processing.
     * This method is called when the FHIR Server starts processing a request.
     */
    public static void set(FHIRRequestContext context) {
        contexts.set(context);
        if (log.isLoggable(Level.FINEST)) {
            log.finest("FHIRRequestContext.set: " + context.toString());
        }
    }
    
    /**
     * Returns the FHIRRequestContext on the current thread.
     */
    public static FHIRRequestContext get() {
        FHIRRequestContext result = contexts.get();
        if (log.isLoggable(Level.FINEST)) {
            log.finest("FHIRRequestContext.get: " + result.toString());
        }
        return result;
    }
    
    /**
     * Removes the FHIRRequestContext that's set on the current thread.
     * This method is called when the FHIR Server is finished processing a request.
     */
    public static void remove() {
        contexts.remove();
        log.finest("FHIRRequestContext.remove invoked.");
    }

    @Override
    public String toString() {
        return "FHIRRequestContext [tenantId=" + tenantId + ", dataStoreId=" + dataStoreId 
                + ", this="+ objectHandle(this) + "]";
    }
    
    public String getRequestUniqueId() {
        return this.requestUniqueId;
    }
    
    private static String objectHandle(Object obj) {
        return '@' + Integer.toHexString(System.identityHashCode(obj));
    }
}
