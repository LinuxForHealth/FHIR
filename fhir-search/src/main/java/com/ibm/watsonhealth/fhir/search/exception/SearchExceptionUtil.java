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
    
    private SearchExceptionUtil() {
        // No Op
    }

    public static FHIRSearchException buildNewInvalidSearchException(final String msg) throws FHIRSearchException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.ValueSet.INVALID);
        return new FHIRSearchException(msg).withIssue(ooi);
    }
    
    public static FHIRSearchException buildNewParseException(String name, Exception e) throws FHIRSearchException {
        String msg = "Unable to parse search result parameter named: '" + name + "'";
        return new FHIRSearchException(msg, e);
    }
    
}
