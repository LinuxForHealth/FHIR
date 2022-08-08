/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.exception;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;
import org.linuxforhealth.fhir.term.util.ConceptMapSupport;
import org.linuxforhealth.fhir.term.util.ValueSetSupport;

/**
 * A runtime exception class intended to be thrown by the {@link CodeSystemSupport}, {@link ConceptMapSupport},
 * and {@link ValueSetSupport} utility classes
 */
public class FHIRTermException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final List<Issue> issues;

    public FHIRTermException(String message, List<Issue> issues) {
        super(message);
        this.issues = Collections.unmodifiableList(Objects.requireNonNull(issues, "issues"));
    }

    public List<Issue> getIssues() {
        return issues;
    }
}
