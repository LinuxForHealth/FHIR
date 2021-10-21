/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.api;


/**
 * Schema update could not be completed because another instance of the update
 * tool was running
 */
public class ConcurrentUpdateException extends RuntimeException {

    // Generated value
    private static final long serialVersionUID = 9153893059192284468L;

    /**
     * Public constructor
     * 
     * @param msg some context about the error
     */
    public ConcurrentUpdateException(String msg) {
        super(msg);
    }
}
