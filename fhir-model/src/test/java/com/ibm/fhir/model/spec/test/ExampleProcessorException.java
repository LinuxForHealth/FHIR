/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

public class ExampleProcessorException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final String filename;
    private final Expectation expectedResult;
    private final Expectation actualResult;
    private final Throwable error;
    
    public ExampleProcessorException(String filename, Expectation expected, Expectation actual) {
        this.filename = filename;
        this.expectedResult = expected;
        this.actualResult = actual;
        this.error = null;
    }
    
    public ExampleProcessorException(String filename, Expectation expected, Expectation actual, Throwable error) {
        this.filename = filename;
        this.expectedResult = expected;
        this.actualResult = actual;
        this.error = error;
    }
    
    @Override
    public String toString() {
        String result = String.format("%s: wanted:%11s got:%11s", filename, expectedResult, actualResult);
        if (error != null) {
            result = result + "\n" + error.toString();
        }
        return result;
    }
}
