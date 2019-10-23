/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

/**
 * Where we expect a particular example to succeed or fail.
 * <ul>
 * <li>OK - the file should parse, be validation and processed successfully</li>
 * <li>PARSE - we expect an error during parsing</li>
 * <li>VALIDATION - we expect an error during validation</li>
 * <li>PROCESS - we expect an error during processing</li>
 * </ul>
 */
public enum Expectation {
    OK,
    PARSE,
    VALIDATION,
    PROCESS
}
