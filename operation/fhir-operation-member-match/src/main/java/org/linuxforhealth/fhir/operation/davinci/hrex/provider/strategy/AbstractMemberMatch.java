/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.davinci.hrex.provider.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult.ResponseType;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * The MemberMatch Strategy Template which controls the order
 * of processing of the methods and provides integral logic
 * for MemberMatch.
 */
public abstract class AbstractMemberMatch implements MemberMatchStrategy {

    private FHIROperationContext ctx;
    private FHIRResourceHelpers resourceHelper;

    @Override
    public final Parameters execute(FHIROperationContext ctx, Parameters input, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        this.resourceHelper = resourceHelper;
        setFHIROperationContext(ctx);

        // 1 - Validate the Input is not null
        verifyNonNull(input);

        // 2 - validate the conformance of the contents
        validate(input);

        // 3 - Execute $member-match
        // 4 - Translate to Parameters output
        return output(executeMemberMatch());
    }

    /**
     * Get the resource helpers.
     * @return
     */
    public FHIRResourceHelpers resourceHelper() {
        return resourceHelper;
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
     * @throws FHIROperationException
     *             indicating a MemberMatch execution Error
     */
    abstract MemberMatchResult executeMemberMatch() throws FHIROperationException;

    /**
     * validates the contents and type of the member match
     *
     * @param input to the MemberMatchOperation
     * @throws FHIROperationException
     */
    abstract void validate(Parameters input) throws FHIROperationException;
}