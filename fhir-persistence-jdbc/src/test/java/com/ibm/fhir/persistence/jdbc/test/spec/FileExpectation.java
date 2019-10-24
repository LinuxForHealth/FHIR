/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import com.ibm.fhir.model.spec.test.Expectation;

/**
 * Represents a file to be processed and the expected outcome
 *
 */
public class FileExpectation {
    private final String filename;
    private final Expectation expectation;
    
    public FileExpectation(String filename, Expectation expectation) {
        this.filename = filename;
        this.expectation = expectation;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return the expectation
     */
    public Expectation getExpectation() {
        return expectation;
    }
}
