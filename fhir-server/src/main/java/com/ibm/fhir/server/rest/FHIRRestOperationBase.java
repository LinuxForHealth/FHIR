/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;


/**
 * Base for {@link FHIRRestOperation} implementations, providing
 * common functions
 */
public abstract class FHIRRestOperationBase implements FHIRRestOperation {
    
    // The index of the entry related to this operation in the original bundle
    private final int entryIndex;

    
    public FHIRRestOperationBase(int entryIndex) {
        this.entryIndex = entryIndex;
    }


    /**
     * @return the entryIndex
     */
    public int getEntryIndex() {
        return entryIndex;
    }
}
