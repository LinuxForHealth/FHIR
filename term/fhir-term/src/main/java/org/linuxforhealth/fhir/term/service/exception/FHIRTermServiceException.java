/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service.exception;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.term.service.FHIRTermService;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;

/**
 * A runtime exception class intended to be thrown by the {@link FHIRTermService} singleton and {@link FHIRTermServiceProvider} implementations
 */
public class FHIRTermServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final List<Issue> issues;

    public FHIRTermServiceException(String message, List<Issue> issues) {
        super(message);
        this.issues = Collections.unmodifiableList(Objects.requireNonNull(issues, "issues"));
    }

    public FHIRTermServiceException(String message, Throwable cause, List<Issue> issues) {
        super(message, cause);
        this.issues = Collections.unmodifiableList(Objects.requireNonNull(issues, "issues"));
    }

    public List<Issue> getIssues() {
        return issues;
    }
}
