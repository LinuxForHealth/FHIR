/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.api;


/**
 * Provides feedback on whether this instance owns the lease to the
 * configured FHIR data schema
 */
public interface ILeaseManager {

    /**
     * Do we have the lease?
     * @return
     */
    boolean hasLease();
    
    /**
     * Should be called frequently during the schema update operation to indicate we're still alive
     * and processing. Some care is required during long update operations so we don't inadvertently
     * lose our lease
     * @throws IllegalStateException if the lease is no longer owned by this instance
     */
    void setHeartbeat();
}
