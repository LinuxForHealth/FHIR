/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.provider.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult.ResponseType;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.util.FHIROperationUtil;

import jakarta.ws.rs.core.Response;

/**
 * The MemberMatch Strategy Template which controls the order
 * of processing of the methods and provides integral logic
 * for MemberMatch.
 */
public abstract class AbstractMemberMatch implements MemberMatchStrategy {

    private FHIROperationContext ctx;

    @Override
    public final Parameters execute(FHIROperationContext ctx, Parameters input, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        setFHIROperationContext(ctx);

        // 1 - Validate the Input
        verifyNonNull(input);

        EvaluationContext evaluationContext = new EvaluationContext(input);
        validate(evaluationContext);

        // 3 - Execute $member-match
        // 4 - Translate to Parameters output
        return output(executeMemberMatch());
    }

    /**
     * sets the context used in the MemberMatch.
     * @param ctx
     */
    public void setFHIROperationContext(FHIROperationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Check the Parameters is non-null.
     *
     * @param input
     * @throws FHIROperationException
     */
    private final void verifyNonNull(Parameters input) throws FHIROperationException {
        if (input == null) {
            throw FHIROperationUtil.buildExceptionWithIssue("A parameters object must be provided for MemberMatch", IssueType.INVALID);
        }
    }

    /**
     * specific to Member Match the output is generally a custom response code.
     *
     * @param ex
     * @param code
     */
    private void addCustomResponse(Exception ex, int code) {
        OperationOutcome operationOutcome = FHIRUtil.buildOperationOutcome(ex, false);

        // As we are now 'modifying' the response, we're PUSHING it into the operation context. The
        // OperationContext is checked for custom response, and picks out the custom response.
        Response response = Response.status(code).entity(operationOutcome).build();
        ctx.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Integer.valueOf(code));
        ctx.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
    }

    /**
     * Translates the output to Parameters
     *
     * @param result
     *            of the MemberMatch execution
     * @return output Parameters object
     */
    public final Parameters output(MemberMatchResult result) {
        if (result.getResponseType() == ResponseType.NO_MATCH) {
            returnNoMatchException();
            return FHIROperationUtil.getOutputParameters(null);
        } else if (result.getResponseType() == ResponseType.MULTIPLE) {
            returnMultipleMatchesException();
            return FHIROperationUtil.getOutputParameters(null);
        }

        Identifier.Builder idBuilder = Identifier.builder().value(result.getValue());

        List<Coding> codings = new ArrayList<>(result.getTypes().size());
        for (Entry<String, String> type : result.getTypes().entrySet()) {
            codings.add(Coding.builder()
                    .system(Uri.of(type.getKey()))
                    .code(Code.of(type.getValue()))
                    .build());
        }

        idBuilder.type(CodeableConcept.builder().coding(codings).build());

        if (result.getSystem() != null) {
            idBuilder.system(Uri.of(result.getSystem()));
        }

        // Per Spec, name is always MemberIdentifier
        return FHIROperationUtil.getOutputParameters("MemberIdentifier", idBuilder.build());
    }

    /**
     * Generates the no-match exception.
     */
    public final void returnNoMatchException() {
        // No match SHALL return a 422 status code.
        FHIROperationException ex = FHIROperationUtil.buildExceptionWithIssue("No match found for $member-match", IssueType.INVALID);
        addCustomResponse(ex, 422);
    }

    /**
     * Generates the multiple-match exception.
     */
    public final void returnMultipleMatchesException() {
        // Multiple matches SHALL return a 422 status code.
        FHIROperationException ex = FHIROperationUtil.buildExceptionWithIssue("Multiple matches found for $member-match", IssueType.INVALID);
        addCustomResponse(ex, 422);
    }

    /**
     *
     * @throws FHIRMemberMatchOperationException
     *             indicating a MemberMatch execution Error
     */
    abstract MemberMatchResult executeMemberMatch() throws FHIROperationException;

    /**
     * validates the contents and type of the member match
     *
     * @param evaluationContext
     *            the current evaluationContext
     * @throws FHIRMemberMatchOperationException
     */
    abstract void validate(EvaluationContext evaluationContext) throws FHIROperationException;
}