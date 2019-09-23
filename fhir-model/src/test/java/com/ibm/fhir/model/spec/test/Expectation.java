/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

/**
 * Where we expect a particular example to succeed or fail.
 * OK - the file should parse, be validation and processed successfully
 * PARSE - we expect an error during parsing
 * VALIDATION - we expect an error during validation
 * PROCESS - we expect an error during processing
 * @author rarnold
 *
 */
public enum Expectation {
    OK,
    PARSE,
    VALIDATION,
    PROCESS
}
