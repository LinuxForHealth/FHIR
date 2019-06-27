/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

import com.ibm.watsonhealth.fhir.model.resource.IssueType;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

public class FHIRPersistenceResourceNotFoundException extends FHIRPersistenceException {
    private static final long serialVersionUID = 1L;
    
    public FHIRPersistenceResourceNotFoundException(String message) {
        super(message);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(message, IssueTypeList.NOT_FOUND));
    }
    
    public FHIRPersistenceResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(message, IssueTypeList.NOT_FOUND));
    }
    
}
