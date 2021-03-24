/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.impl;

import java.util.logging.Logger;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 * Erase Implementation
 */
public class EraseImpl implements Erase {

    private static final Logger logger = Logger.getLogger(EraseImpl.class.getName());

    @Override
    public void checkAllowedMethod(Object method) throws FHIROperationException {

    }

    protected FHIROperationException buildExceptionWithIssue(String msg, IssueType issueType)
        throws FHIROperationException {
        return buildExceptionWithIssue(msg, issueType, null);
    }

    protected FHIROperationException buildExceptionWithIssue(String msg, IssueType issueType, Throwable cause)
        throws FHIROperationException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg, cause).withIssue(ooi);
    }

    @Override
    public void checkPatientId() throws FHIROperationException {

    }

    @Override
    public void exists() throws FHIROperationException {

    }

    @Override
    public Parameters erase() throws FHIROperationException {

        return null;
    }
}