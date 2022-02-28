/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase.impl;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.SecurityContext;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

/**
 * Erase Rest Implementation implements the parameters processing, authorization
 * and the turning on/off of the feature.
 */
public class EraseRestImpl implements EraseRest {
    private static final String CLZ = EraseRestImpl.class.getName();
    private static final Logger LOG = Logger.getLogger(CLZ);

    private boolean supportedMethod = false;
    private SecurityContext ctx = null;
    private Parameters parameters = null;
    private String resourceType = null;
    private String logicalId = null;

    // For the errors this list aggregates the issues across the inputs.
    private List<OperationOutcome.Issue> issues = new ArrayList<>();

    private final CompartmentUtil compartmentHelper;

    /**
     * create the Erase Rest instance.
     *
     * @param method
     * @param ctx
     * @param parameters
     * @param rt
     * @param logicalId
     */
    public EraseRestImpl(String method, SecurityContext ctx, Parameters parameters, Class<? extends Resource> rt, String logicalId,
            CompartmentUtil compartmentHelper) {
        supportedMethod = "POST".equals(method);
        this.ctx = ctx;
        this.parameters = parameters;
        this.resourceType = rt.getSimpleName();
        this.logicalId = logicalId;
        this.compartmentHelper = compartmentHelper;
    }

    @Override
    public void authorize() throws FHIROperationException {
        List<String> allowedRoles = FHIRConfigHelper.getStringListProperty("fhirServer/operations/erase/allowedRoles");
        if (allowedRoles != null) {
            // if the allowedRoles is empty, we assume it means that they are not allowed.
            if (allowedRoles.isEmpty()) {
                throw FHIROperationUtil
                    .buildExceptionWithIssue("No roles allowed to access [$reindex]",
                        IssueType.FORBIDDEN);
            }

            // Check and find if the user is in at least one role, and then shortcircuit out of the check.
            boolean authorize = false;
            for(String allowedRole : allowedRoles) {
                authorize = ctx.isUserInRole(allowedRole);
                if (authorize) {
                    return;
                }
            }

            // To get here, it means that the user is not authorized.
            throw FHIROperationUtil
                .buildExceptionWithIssue("Access to operation [$reindex] is forbidden",
                    IssueType.FORBIDDEN);
        }
    }

    @Override
    public void enabled() throws FHIROperationException {
        /*
         * Default we're going to disable this feature. Since this mutates the server's data store permanently,
         * it's best not to be on by default.
         */
        boolean enabled = FHIRConfigHelper.getBooleanProperty("fhirServer/operations/erase/enabled", false);
        if (!enabled) {
            throw FHIROperationUtil
                .buildExceptionWithIssue("Operation[$erase] is not enabled", IssueType.NOT_SUPPORTED);
        }
    }

    @Override
    public EraseDTO verify() throws FHIROperationException {
        // We're only supporting POST
        if (!supportedMethod) {
            throw FHIROperationUtil
                .buildExceptionWithIssue("Operation[$erase] is only supported with the HttpMethod POST",
                    IssueType.NOT_SUPPORTED);
        }

        // Return a dto
        EraseDTO dto = new EraseDTO();

        // While  we accept 0..* we want to squash that down.
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(parameters);

        // Parameter: patient
        // Cardinality: 0..1
        processPatient(dto, evaluator, evaluationContext, issues);

        // Parameter: reason
        // Cardinality: 1..1
        processReason(dto, evaluator, evaluationContext, issues);

        // Parameter: version
        // Cardinality: 0..1
        processVersion(dto, evaluator, evaluationContext, issues);

        // This must be a Operation call TYPE, not INSTANCE
        processLogicalId(dto, evaluator, evaluationContext, issues, logicalId);

        // At this point, we've got issues, and need to wrap and throw.
        if (!issues.isEmpty()) {
            FHIROperationException ex = FHIROperationUtil.buildExceptionWithIssue("Request inputs are invalid", IssueType.INVALID);
            ex.setIssues(issues);
            throw ex;
        }

        // Update the resource type and logical id so we can process downstream.
        dto.setResourceType(resourceType);
        return dto;
    }

   /**
    * processes the 'id' from the parameters object in the evaluation context.
    *
    * @param dto the data transfer object that is to be sent to the DAO
    * @param evaluator the FHIRPath Evaluator that is used to evaluate the value
    * @param evaluationContext the context which includes the input Parameters object
    * @param issues the aggregate issues list.
    * @param logicalId the id that identifies the current resource.
    */
    public void processLogicalId(EraseDTO dto, FHIRPathEvaluator evaluator, EvaluationContext evaluationContext, List<Issue> issues, String logicalId) {
       try {
           Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'id').value");
           Iterator<FHIRPathNode> iter = result.iterator();

           boolean hasMoreThanOne = false;
           while (iter.hasNext()) {
               // We don't want any confusion
               if (hasMoreThanOne || logicalId != null) {
                   issues.add(Issue.builder()
                       .diagnostics(string("more than one 'id' parameter provided, or 'id' parameter provided for instance level erase"))
                       .code(IssueType.INVALID)
                       .severity(IssueSeverity.FATAL)
                       .build());
                   break;
               }
               FHIRPathElementNode node = iter.next().as(FHIRPathElementNode.class);
               if (!node.asElementNode().element().is(com.ibm.fhir.model.type.String.class)) {
                   issues.add(Issue.builder()
                       .diagnostics(string("'id' must be of type String"))
                       .code(IssueType.INVALID)
                       .severity(IssueSeverity.FATAL)
                       .build());
               } else {
                   String val = node.asElementNode().element().as(com.ibm.fhir.model.type.String.class).getValue();
                   if (val == null) { // We check null, just in case data absent reason
                       issues.add(Issue.builder()
                           .diagnostics(string("Operation[$erase] must have a non-null id"))
                           .code(IssueType.INVALID)
                           .severity(IssueSeverity.FATAL)
                           .build());
                   } else {
                       dto.setLogicalId(val);
                   }
               }
               hasMoreThanOne = true;
           }
           // Finally, if we processed all that, we want to ensure the logicalId from the resource/id is set.
           if (dto.getLogicalId() == null) {
               dto.setLogicalId(logicalId);
           }
       } catch (FHIRPathException e) {
           logException(issues, e);
       }
   }

   /**
    * processes the 'version' from the parameters object in the evaluation context.
    *
    * @param dto the data transfer object that is to be sent to the DAO
    * @param evaluator the FHIRPath Evaluator that is used to evaluate the value
    * @param evaluationContext the context which includes the input Parameters object
    * @param issues the aggregate issues list.
    */
   public void processVersion(EraseDTO dto, FHIRPathEvaluator evaluator, EvaluationContext evaluationContext, List<Issue> issues) {
       try {
           Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'version').value");
           Iterator<FHIRPathNode> iter = result.iterator();

           boolean hasMoreThanOne = false;
           while (iter.hasNext()) {
               if (hasMoreThanOne) {
                   issues.add(Issue.builder()
                       .diagnostics(string("more than one 'version' parameter provided"))
                       .code(IssueType.INVALID)
                       .severity(IssueSeverity.FATAL)
                       .build());
                   break;
               }
               FHIRPathElementNode node = iter.next().as(FHIRPathElementNode.class);
               if (!node.asElementNode().element().is(com.ibm.fhir.model.type.Integer.class)) {
                   issues.add(Issue.builder()
                       .diagnostics(string("'version' must be of type Integer"))
                       .code(IssueType.INVALID)
                       .severity(IssueSeverity.FATAL)
                       .build());
               } else {
                   Integer val = node.asElementNode().element().as(com.ibm.fhir.model.type.Integer.class).getValue();
                   if (val == null) { // We check null, just in case data absent reason
                       issues.add(Issue.builder()
                           .diagnostics(string("Operation[$erase] must have a non-null version"))
                           .code(IssueType.INVALID)
                           .severity(IssueSeverity.FATAL)
                           .build());
                   } else {
                       dto.setVersion(val);
                   }
               }
               hasMoreThanOne = true;
           }
       } catch (FHIRPathException e) {
           logException(issues, e);
       }
   }

    /**
     * processes the 'reason' from the parameters object in the evaluation context.
     *
     * @param dto the data transfer object that is to be sent to the DAO
     * @param evaluator the FHIRPath Evaluator that is used to evaluate the value
     * @param evaluationContext the context which includes the input Parameters object
     * @param issues the aggregate issues list.
     */
    public void processReason(EraseDTO dto, FHIRPathEvaluator evaluator, EvaluationContext evaluationContext, List<Issue> issues) {
        try {
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'reason').value");
            Iterator<FHIRPathNode> iter = result.iterator();

            boolean hasMoreThanOne = false;
            while (iter.hasNext()) {
                if (hasMoreThanOne) {
                    issues.add(Issue.builder()
                        .diagnostics(string("more than one 'reason' parameter provided"))
                        .code(IssueType.INVALID)
                        .severity(IssueSeverity.FATAL)
                        .build());
                    break;
                }
                FHIRPathElementNode node = iter.next().as(FHIRPathElementNode.class);

                if (!node.asElementNode().element().is(com.ibm.fhir.model.type.String.class)) {
                    issues.add(Issue.builder()
                        .diagnostics(string("'reason' must be of type string"))
                        .code(IssueType.INVALID)
                        .severity(IssueSeverity.FATAL)
                        .build());
                } else {
                    String val = node.asElementNode().element().as(com.ibm.fhir.model.type.String.class).getValue();
                    dto.setReason(val);

                    // Verify not null, and not over limit
                    if (val == null) { // We check null, just in case data absent reason
                        issues.add(Issue.builder()
                            .diagnostics(string("Operation[$erase] must have a non-empty reason parameter"))
                            .code(IssueType.EXCEPTION)
                            .severity(IssueSeverity.FATAL)
                            .build());
                    } else if (dto.getReason().length() > 1000) {
                        issues.add(Issue.builder()
                            .diagnostics(string("Operation[$erase] must have reason parameter exceeds 1000 characters"))
                            .code(IssueType.INVALID)
                            .severity(IssueSeverity.FATAL)
                            .build());
                    }
                }
                hasMoreThanOne = true;
            }
        } catch (FHIRPathException e) {
            logException(issues, e);
        }

        if (dto.getReason() == null) {
            issues.add(Issue.builder()
                .diagnostics(string("Operation[$erase] must have a reason parameter"))
                .code(IssueType.INVALID)
                .severity(IssueSeverity.FATAL)
                .build());
        }
    }

    /**
     * processes the 'patient' from the parameters object in the evaluation context.
     *
     * @param dto the data transfer object that is to be sent to the DAO
     * @param evaluator the FHIRPath Evaluator that is used to evaluate the value
     * @param evaluationContext the context which includes the input Parameters object
     * @param issues the aggregate issues list.
     */
    public void processPatient(EraseDTO dto, FHIRPathEvaluator evaluator, EvaluationContext evaluationContext, List<Issue> issues) {
        try {
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'patient').value");
            Iterator<FHIRPathNode> iter = result.iterator();

            boolean hasMoreThanOne = false;
            while (iter.hasNext()) {
                if (hasMoreThanOne) {
                    issues.add(Issue.builder()
                        .diagnostics(string("more than one 'patient' parameter provided"))
                        .code(IssueType.INVALID)
                        .severity(IssueSeverity.FATAL)
                        .build());
                    break;
                }
                FHIRPathElementNode node = iter.next().as(FHIRPathElementNode.class);
                if (!node.asElementNode().element().is(com.ibm.fhir.model.type.String.class)) {
                    issues.add(Issue.builder()
                        .diagnostics(string("'patient' must be of type string"))
                        .code(IssueType.INVALID)
                        .severity(IssueSeverity.FATAL)
                        .build());
                } else {
                    String val = node.asElementNode().element().as(com.ibm.fhir.model.type.String.class).getValue();
                    dto.setPatient(val);
                    if (val == null) { // We check null, just in case data absent reason
                        issues.add(Issue.builder()
                            .diagnostics(string("Operation[$erase] must have a non-empty patient parameter"))
                            .code(IssueType.EXCEPTION)
                            .severity(IssueSeverity.FATAL)
                            .build());
                    } else if (dto.getPatient().length() > 1000) {
                        issues.add(Issue.builder()
                            .diagnostics(string("Operation[$erase] patient parameter exceeds 1024 characters"))
                            .code(IssueType.EXCEPTION)
                            .severity(IssueSeverity.FATAL)
                            .build());
                    }
                }
                hasMoreThanOne = true;
            }

            // If this is in the patient compartment, then we want throw an issue.
            if (compartmentHelper.getCompartmentResourceTypes("Patient").contains(resourceType) && dto.getPatient() == null) {
                issues.add(Issue.builder()
                    .diagnostics(string("Operation[$erase] on the resource type '" + resourceType + "' requires a reference patient id"))
                    .code(IssueType.INVALID)
                    .severity(IssueSeverity.FATAL)
                    .build());
            }
        } catch (FHIRPathException | FHIRSearchException e) {
            logException(issues, e);
        }
    }

    /**
     * encapsulating repeated logic
     * @param issues
     * @param e
     */
    public void logException(List<Issue> issues, Exception e) {
        issues.add(Issue.builder()
            .diagnostics(string("Unable to process the input " + e.getMessage()))
            .code(IssueType.EXCEPTION)
            .severity(IssueSeverity.FATAL)
            .build());
        LOG.throwing(CLZ, "verify", e);
    }
}
