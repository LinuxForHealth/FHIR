/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.transaction.Status;
import javax.transaction.Synchronization;

/**
 * This is an abstract base class encapsulating functionality related to adding entries to a 
 * JDBC PL in-memory cache.
 */
public abstract class CacheUpdater implements Synchronization {
    private static final String CLASSNAME = CacheUpdater.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);
        
    private Map<String, Integer> cacheCandidates;
    private String tenantDatastoreCacheName;
    
    
    public CacheUpdater(String tenantDatastoreCacheName, Map<String, Integer> newCacheCandidates) {
        super();
        Objects.requireNonNull(newCacheCandidates);
        this.setCacheCandidates(newCacheCandidates);
        this.setTenantDatastoreCacheName(tenantDatastoreCacheName);
    }
    
     /**
      * Writes candidate cache entries to the appropriate cache after being notified by
      * the Trx Synchronization service that the transaction on the current thread 
      * has successfully committed.
      */
    @Override
    public void afterCompletion(int completionStatus) {
        final String METHODNAME = "afterCompletion";
        log.entering(CLASSNAME, METHODNAME);
        
        if (completionStatus == Status.STATUS_COMMITTED) {
            this.commitCacheCandidates();
        }
                
        this.clearCacheCandidates();
        
        log.exiting(CLASSNAME, METHODNAME);
    }

     
    @Override
    public void beforeCompletion() {
    }
    
    /**
     * Implemented by subclasses to perform the mechanics of writing cache candidate entries
     * to the appropriate cache.
     */
    public abstract void commitCacheCandidates();
    
    /**
     * Empties the collection of cache candidates.
     */
    public void clearCacheCandidates() {
        final String METHODNAME = "clearCacheCandidates";
        log.entering(CLASSNAME, METHODNAME);
        
        this.getCacheCandidates().clear();
        
        log.exiting(CLASSNAME, METHODNAME);
    }

    public Map<String, Integer> getCacheCandidates() {
        return cacheCandidates;
    }

    public void setCacheCandidates(Map<String, Integer> cacheCandidates) {
        this.cacheCandidates = cacheCandidates;
    }

    public String getTenantDatastoreCacheName() {
        return tenantDatastoreCacheName;
    }

    public void setTenantDatastoreCacheName(String tenantDatastoreCacheName) {
        this.tenantDatastoreCacheName = tenantDatastoreCacheName;
    }

}
