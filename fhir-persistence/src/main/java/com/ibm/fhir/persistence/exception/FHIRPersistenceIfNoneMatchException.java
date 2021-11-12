/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.exception;

import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 * This exception is thrown when an IfNoneMatch precondition check
 * fails and the server is configured to treat this as an error
 * (412 Precondition Failed). 
 * See FHIRConfiguration.PROPERTY_IF_NONE_MATCH_RETURNS_NOT_MODIFIED.
 */
public class FHIRPersistenceIfNoneMatchException extends FHIRPersistenceException {

    // Generated serialVersionUID
    private static final long serialVersionUID = -6078163237462666409L;

    public FHIRPersistenceIfNoneMatchException(String message) {
        super(message);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(getMessage(), IssueType.CONFLICT));
    }
}