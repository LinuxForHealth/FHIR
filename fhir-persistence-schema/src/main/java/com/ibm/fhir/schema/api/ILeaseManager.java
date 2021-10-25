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
     * Marks the heartbeat flag true in the LeaseManager implementation to signal that forward
     * progress is being made so the lease should continue to be held.
     * Ignored (not required) if {@link ILeaseManagerConfig#stayAlive()} is true (which is default).
     * @throws IllegalStateException if the lease is no longer owned by this instance
     */
    void signalHeartbeat();
}