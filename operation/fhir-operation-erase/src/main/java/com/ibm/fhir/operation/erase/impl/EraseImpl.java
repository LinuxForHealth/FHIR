/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.SecurityContext;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 * Erase Implementation
 */
public class EraseImpl implements Erase {

    private static final Logger LOG = Logger.getLogger(EraseImpl.class.getName());
    private boolean supportedMethod = false;
    private SecurityContext ctx = null;

    public EraseImpl(HttpMethod method, SecurityContext securityContext, FHIRResourceHelpers resourceHelper, Parameters parameters, Class<? extends Resource> resourceType,
        String logicalId, String versionId) {
        supportedMethod = HttpMethod.POST.equals(method);


    }


    @Override
    public void verifyMethod() throws FHIROperationException {
        if (!supportedMethod) {

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
    public void verifyParameters() throws FHIROperationException {

         FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
         EvaluationContext evaluationContext = null;
        try {
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'inputFormat').value");
            Iterator<FHIRPathNode> iter = result.iterator();
            while (iter.hasNext()) {
                FHIRPathElementNode node = iter.next().as(FHIRPathElementNode.class);
                String val = node.asElementNode().element().as(com.ibm.fhir.model.type.String.class).getValue();
                //if (OperationConstants.INPUT_FORMATS.contains(val)) {
                    //return val;
                //}
            }
        } catch (ClassCastException | FHIRPathException e) {
            //throw common.buildExceptionWithIssue("invalid $import parameter value in 'inputFormat'", e, IssueType.INVALID);
        }

        ///throw common.buildExceptionWithIssue("$import requires 'inputFormat' is not found", IssueType.INVALID);
    }

    @Override
    public void exists() throws FHIROperationException {

    }

    @Override
    public Parameters erase() throws FHIROperationException {

        return null;
    }


    @Override
    public void enabled() throws FHIROperationException {
        boolean enabled = FHIRConfigHelper.getBooleanProperty("fhirServer/operations/erase/enabled", true);
        if (!enabled) {
            throw buildExceptionWithIssue("Operation[$erase] is not enabled", IssueType.NOT_SUPPORTED);
        }
    }

    /**
     * verifies the authorization to the operation based on the allowedRoles
     *
     * @param operationContext
     * @throws FHIROperationException
     */
    @Override
    public void authorize() throws FHIROperationException {
        List<String> allowedRoles = FHIRConfigHelper.getStringListProperty("fhirServer/operations/erase/allowedRoles");

        if (allowedRoles != null) {
            if (allowedRoles.isEmpty()) {
                throw buildExceptionWithIssue("Roles allowed to access [$reindex] is empty", IssueType.FORBIDDEN);
            }



            boolean authorize = false;

                for(String allowedRole : allowedRoles) {
                    authorize = ctx.isUserInRole(allowedRole);
                    if (authorize) {
                        return;
                    }
                }


            if (!authorize) {
                throw buildExceptionWithIssue("Access to operation [$reindex] is forbidden", IssueType.FORBIDDEN);
            }
        }
    }
}