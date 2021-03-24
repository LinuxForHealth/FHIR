/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.exception;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;

public class FHIRTermServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final List<Issue> issues;

    public FHIRTermServiceException(String message, List<Issue> issues) {
        super(message);
        this.issues = Collections.unmodifiableList(Objects.requireNonNull(issues, "issues"));
    }

    public List<Issue> getIssues() {
        return issues;
    }
}
