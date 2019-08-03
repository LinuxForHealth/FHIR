/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.exception;

import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

/**
 * SearchExceptionUtil encapsulates the logic for throwing a Search Exception
 * 
 * @author pbastide
 *
 */
public class SearchExceptionUtil {

    private static final String PARSE_EXCEPTION = "Unable to parse search result parameter named: '%s";

    private static final String ILLEGAL_EXCEPTION = "SearchParameter filter property values must be an array of String.";

    private static final String ILLEGAL_ARGUMENT_EXCEPTION = "No constant with value %s found.";

    private static final String PARSE_PARAMETER_EXCEPTION = "An error occurred while parsing search parameter '%s'.";

    private static final String CHAINED_PARAMETER_EXCEPTION = "Unable to parse chained parameter: '%s'";
    
    private static final String GET_SEARCH_FAILED = "Unable to process getSearch ";
    
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
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.ValueSet.INVALID);
        return new FHIRSearchException(msg).withIssue(ooi);
    }

    /**
     * create new parse exception
     * 
     * @param name
     * @param e
     * @return
     */
    public static FHIRSearchException buildNewParseException(final String name, Exception e) {
        return new FHIRSearchException(String.format(PARSE_EXCEPTION, name), e);
    }

    /**
     * creates a new parse parameter exception
     * 
     * @param name
     * @param e
     * @return
     */
    public static FHIRSearchException buildNewParseParameterException(final String name, Exception e) {
        return new FHIRSearchException(String.format(PARSE_PARAMETER_EXCEPTION, name), e);
    }

    /**
     * creates a new chained parameter exception
     * 
     * @param name
     * @param e
     * @return
     */
    public static FHIRSearchException buildNewChainedParameterException(final String name, Exception e) {
        return new FHIRSearchException(String.format(CHAINED_PARAMETER_EXCEPTION, name), e);
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
    public static IllegalArgumentException buildNewIllegalArumentException(final String val) {
        return new IllegalArgumentException(String.format(ILLEGAL_ARGUMENT_EXCEPTION, val));
    }
    
    /**
     * builds a fhir search exception. 
     * 
     * @param e
     * @return
     */
    public static FHIRSearchException buildNewFHIRSearchExecption(Exception e) {
        return new FHIRSearchException(GET_SEARCH_FAILED, e);
    }

}
