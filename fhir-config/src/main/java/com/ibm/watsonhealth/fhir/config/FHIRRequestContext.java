/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config;

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;

/**
 * This class is used to hold FHIR REST API context information.
 * There are non-static methods for maintaining individual instances, as 
 * well as static methods for setting instances on and getting instances from thread local.
 * The overall strategy is for the REST API servlet filter to retrieve the request context
 * information, create an instance of this class and set it on the current thread for use
 * by the FHIR Server as it processes the request.
 */
public class FHIRRequestContext {
    private String tenantId;
    private String dataStoreId;
    
    private static ThreadLocal<FHIRRequestContext> contexts = new ThreadLocal<FHIRRequestContext>() {
        @Override
        public FHIRRequestContext initialValue() {
            return new FHIRRequestContext(FHIRConfiguration.DEFAULT_TENANT_ID);
        }
    };
    
    public FHIRRequestContext() {
    }
    
    public FHIRRequestContext(String tenantId) {
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
    }
    
    /**
     * Returns the FHIRRequestContext on the current thread.
     */
    public static FHIRRequestContext get() {
        return contexts.get();
    }
    
    /**
     * Removes the FHIRRequestContext that's set on the current thread.
     * This method is called when the FHIR Server is finished processing a request.
     */
    public static void remove() {
        contexts.remove();
    }
}
