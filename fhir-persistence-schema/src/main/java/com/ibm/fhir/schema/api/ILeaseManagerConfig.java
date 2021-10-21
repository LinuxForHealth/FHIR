/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.api;


/**
 * Provides configuration items used by the LeaseManager
 */
public interface ILeaseManagerConfig {

    /**
     * The number of seconds each lease is held for. The LeaseManager holding
     * a lease will renew that lease periodically as long as it receives
     * a heartbeat call within lease-time-seconds / 2. 
     * @return
     */
    int getLeaseTimeSeconds();
    
    /**
     * Get the hostname used to help identify which instant currently owns a lease
     * @return
     */
    String getHost();
    
    /**
     * Flag to indicate we want the LeaseManager to stay alive without
     * requiring regular heartbeats
     * @return
     */
    boolean stayAlive();
}
