/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;


/**
 * An interface to a cache of entities used for managing references between
 * resources (local or external).
 */
public interface IResourceReferenceCache {

    /**
     * Clear the thread-local and shared caches
     */
    public void reset();
}
