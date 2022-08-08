/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.exception;

import java.util.Collection;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;

/**
 * 
 * @author pbastide 
 *
 */
public class FHIRSearchException extends FHIROperationException {
    private static final long serialVersionUID = 1L;

    public FHIRSearchException(String message) {
        super(message);
    }

    public FHIRSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRSearchException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }

    @Override
    public FHIRSearchException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
