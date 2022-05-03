/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.exception;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;

public class FHIRResourceNotFoundException extends FHIROperationException {
    private static final long serialVersionUID = 1L;

    /**
     * Create an exception with a single OperationOutcome.Issue of type NOT_FOUND,
     * both with the passed message
     */
    public FHIRResourceNotFoundException(String message) {
        super(message);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(message, IssueType.NOT_FOUND));
    }

    /**
     * Create an exception with the passed message and cause and
     * a single OperationOutcome.Issue with the passed message and an IssueType of NOT_FOUND
     */
    public FHIRResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(message, IssueType.NOT_FOUND));
    }
}
