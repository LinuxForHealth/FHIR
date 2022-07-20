/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.api;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.ProfileParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.ReferenceParameter;
import com.ibm.fhir.persistence.index.SecurityParameter;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TagParameter;
import com.ibm.fhir.persistence.index.TokenParameter;

/**
 * Collects instances of search parameter values associated with a particular resource (identified by the logicalResourceId).
 * This collector can be used to collect parameters from multiple resources being processed over an entire transaction.
 * Collecting all the values in this way makes it more efficient when resolving reference (FK) ids, and means that
 * we hold onto any locks for a shorter period of time.
 */
public interface IParamValueCollector {

    /**
     * Publish the values held in this collector using the given processor
     * @param processor
     */
    void publish(IParamValueProcessor processor) throws FHIRPersistenceException;

    /**
     * Reset the state of the collector by clearing any values that have been previously collected
     */
    void reset();

    /**
     * To be called after all the values are resolved and the database transaction is committed.
     * This call should promote any newly assigned database ids to the cache (it is then up
     * to the cache implementation to decide what to do with those ids).
     */
    void publishValuesToCache();

    /**
     * Process the given LocationParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, LocationParameter p) throws FHIRPersistenceException;

    /**
     * Process the given TokenParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TokenParameter p) throws FHIRPersistenceException;

    /**
     * Process the given TagParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TagParameter p) throws FHIRPersistenceException;

    /**
     * Process the given ProfileParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ProfileParameter p) throws FHIRPersistenceException;

    /**
     * Proces the given SecurityParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, SecurityParameter p) throws FHIRPersistenceException;

    /**
     * Process the given QuantityParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p) throws FHIRPersistenceException;

    /**
     * Process the given NumberParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, NumberParameter p) throws FHIRPersistenceException;

    /**
     * Process the given DateParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, DateParameter p) throws FHIRPersistenceException;

    /**
     * Process the given ReferenceParameter p
     * 
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     * @throws FHIRPersistenceException
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ReferenceParameter p) throws FHIRPersistenceException;

    /**
     * @param tenantId
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param p
     */
    void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, StringParameter p) throws FHIRPersistenceException;
}
