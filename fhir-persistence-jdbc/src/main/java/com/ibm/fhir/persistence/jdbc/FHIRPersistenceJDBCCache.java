/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IIdNameCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import com.ibm.fhir.persistence.jdbc.dao.api.INameIdCache;

/**
 * Manages caches separated by tenant
 */
public interface FHIRPersistenceJDBCCache {

    /**
     * Returns true if the caller should attempt to prefill the caches. Prefilling must
     * only be done before any new records are inserted to ensure the shared caches
     * contain only data which has been previously committed to the database.
     * @return
     */
    boolean needToPrefill();
    
    /**
     * Clear the needToPrefill flag - call after the prefill has been done
     */
    void clearNeedToPrefill();

    /**
     * Getter for the common token values cache
     * @return
     */
    ICommonTokenValuesCache getResourceReferenceCache();

    /**
     * Getter for the cache handling lookups for logical_resource_id values
     * @return
     */
    ILogicalResourceIdentCache getLogicalResourceIdentCache();

    /**
     * Getter for the cache of resource types used to look up resource type id
     * @return
     */
    INameIdCache<Integer> getResourceTypeCache();

    /**
     * Getter for the cache of resource type ids used to look up resource type name
     * @return
     */
    IIdNameCache<Integer> getResourceTypeNameCache();

    /**
     * Getter for the cache of parameter names
     * @return
     */
    INameIdCache<Integer> getParameterNameCache();

    /**
     * Tell any caches that the transaction on the current thread has just committed
     */
    public void transactionCommitted();

    /**
     * The transaction on the current thread was rolled back, so throw away anything
     * held in thread-local caches
     */
    public void transactionRolledBack();
}