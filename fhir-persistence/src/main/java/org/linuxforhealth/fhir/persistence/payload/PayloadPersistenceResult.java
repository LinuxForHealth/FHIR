/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.payload;

/**
 * The response from the payload persistence operation
 */
public class PayloadPersistenceResult {
    // The status of the payload persistence operation
    private final Status status;
    
    /**
     * Enumeration of status types
     */
    public static enum Status {
        OK, FAILED
    }
    
    public PayloadPersistenceResult(Status status) {
        this.status = status;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }
}