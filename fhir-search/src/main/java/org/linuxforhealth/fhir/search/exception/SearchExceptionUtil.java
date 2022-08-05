/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.exception;

import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;

/**
 * SearchExceptionUtil encapsulates the logic for throwing a Search Exception
 */
public class SearchExceptionUtil {

    private static final String ILLEGAL_EXCEPTION = "SearchParameter filter property values must be an array of String.";
    private static final String ILLEGAL_ARGUMENT_EXCEPTION = "No constant with value '%s' found.";
    private static final String PARSE_PARAMETER_EXCEPTION = "An error occurred while parsing parameter '%s'.";
    private static final String PARSE_PARAMETERS_EXCEPTION = "An error occurred while parsing parameters.";
    private static final String CHAINED_PARAMETER_EXCEPTION = "Unable to parse chained parameter: '%s'";
    private static final String REVERSE_CHAINED_PARAMETER_EXCEPTION = "Unable to parse reverse chained parameter: '%s'";
    private static final String BADFORMAT_EXCEPTION = "Invalid Date Time Format found please use 'yyyy-mm-ddThh:mm:ss[Z|(+|-)hh:mm].'";

    private SearchExceptionUtil() {
        // No Op
    }

    /**
     * creates an invalid search exception.
     *
     * @param msg
     * @return
     */
    public static FHIRSearchException buildNewInvalidSearchException(final String msg) {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID);
        return new FHIRSearchException(msg).withIssue(ooi);
    }

    /**
     * creates a new parse parameter exception
     *
     * @param name
     * @param e
     * @return
     */
    public static FHIRSearchException buildNewParseParameterException(final String name, Exception e) {
        String msg = String.format(PARSE_PARAMETER_EXCEPTION, name);
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID);
        return new FHIRSearchException(msg, e).withIssue(ooi);
    }

    /**
     * creates a new parse parameters exception
     *
     * @param name
     * @param e
     * @return
     */
    public static FHIRSearchException buildNewParseParametersException(Exception e) {
        String msg = String.format(PARSE_PARAMETERS_EXCEPTION);
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID);
        return new FHIRSearchException(msg, e).withIssue(ooi);
    }

    /**
     * creates a new chained parameter exception
     *
     * @param name
     * @param e
     * @return
     */
    public static FHIRSearchException buildNewChainedParameterException(final String name, Exception e) {
        String msg = String.format(CHAINED_PARAMETER_EXCEPTION, name);
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID);
        return new FHIRSearchException(msg, e).withIssue(ooi);
    }

    /**
     * creates a new reverse chained parameter exception
     *
     * @param name
     *        The search parameter name
     * @param e
     *        An exception
     * @return
     *        A FHIRSearchException
     */
    public static FHIRSearchException buildNewReverseChainedParameterException(final String name, Exception e) {
        String msg = String.format(REVERSE_CHAINED_PARAMETER_EXCEPTION, name);
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID);
        return new FHIRSearchException(msg, e).withIssue(ooi);
    }

    /**
     * builds an illegal state exception for a search filter execution
     *
     * @return
     */
    public static IllegalStateException buildNewIllegalStateException() {
        return new IllegalStateException(ILLEGAL_EXCEPTION);
    }

    /**
     * builds an illegal Argument exception.
     *
     * @param val
     * @return
     */
    public static IllegalArgumentException buildNewIllegalArgumentException(final String val) {
        return new IllegalArgumentException(String.format(ILLEGAL_ARGUMENT_EXCEPTION, val));
    }

    /**
     * build data time format exception
     *
     * @param exception e
     * @return
     */
    public static FHIRSearchException buildNewDateTimeFormatException(Exception e) {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(BADFORMAT_EXCEPTION, IssueType.INVALID);
        return new FHIRSearchException(BADFORMAT_EXCEPTION, e).withIssue(ooi);
    }
}