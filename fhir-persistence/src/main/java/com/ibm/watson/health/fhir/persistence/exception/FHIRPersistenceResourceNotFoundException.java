/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.exception;

import com.ibm.watson.health.fhir.model.type.IssueType;
import com.ibm.watson.health.fhir.model.util.FHIRUtil;

public class FHIRPersistenceResourceNotFoundException extends FHIRPersistenceException {
    private static final long serialVersionUID = 1L;
    
    public FHIRPersistenceResourceNotFoundException(String message) {
        super(message);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(message, IssueType.ValueSet.NOT_FOUND));
    }
    
    public FHIRPersistenceResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(message, IssueType.ValueSet.NOT_FOUND));
    }
    
}
