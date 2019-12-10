/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

public class ExampleProcessorException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public ExampleProcessorException(String filename, Expectation expected, Expectation actual) {
        this(filename, expected, actual, null);
    }
    
    public ExampleProcessorException(String filename, Expectation expected, Expectation actual, Throwable error) {
        super(String.format("%s: wanted:%11s got:%11s", filename, expected, actual), error);
    }
    
    @Override
    public String toString() {
        return getMessage();
    }
}
