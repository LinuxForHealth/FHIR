/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.erase.impl;

import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 *
 */
public class EraseImpl implements Erase {
    private static final Logger logger = Logger.getLogger(EraseImpl.class.getName());

    private String jwtGroupName = "";
    private String scSubject = "MyGroup";

    @Override
    public void checkAllowedMethod(Object method) throws FHIROperationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void authorize(Object securityContext, Object jwt) throws FHIROperationException {
        boolean authorize = false;

        if (jwt != null && jwt instanceof JsonWebToken) {
            JsonWebToken jwtObj = (JsonWebToken) jwt;
            Set<String> groups = jwtObj.getGroups();
            if (groups != null) {
                for (String group : groups) {
                    if (jwtGroupName.equals(group)) {
                        authorize = true;
                        return;
                    }
                }
            }
        }

        if (!authorize && securityContext != null && securityContext instanceof SecurityContext) {
            SecurityContext ctx = (SecurityContext) securityContext;
            authorize = ctx.isUserInRole("FHIROperationAdmin");
        }

        if (!authorize) {
            throw buildExceptionWithIssue("Access not possible", IssueType.FORBIDDEN);
        }
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
        // TODO Auto-generated method stub

    }

    @Override
    public void exists() throws FHIROperationException {
        // TODO Auto-generated method stub

    }

    @Override
    public Parameters erase() throws FHIROperationException {
        // TODO Auto-generated method stub
        return null;
    }


}
