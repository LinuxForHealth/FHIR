/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.search.exception;

import java.util.Collection;

import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome;

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
