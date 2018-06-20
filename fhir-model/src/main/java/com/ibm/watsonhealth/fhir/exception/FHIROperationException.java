/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.Id;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

public class FHIROperationException extends FHIRException {
    private static final long serialVersionUID = 1L;

    private List<OperationOutcomeIssue> issues = new ArrayList<>();

    /**
     * @see Exception#Exception(String)
     */
    public FHIROperationException(String message) {
        super(message);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     */
    public FHIROperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OperationOutcome buildOperationOutcome() {
        Id probeId = FHIRUtil.id(getUniqueId());
        return FHIRUtil.buildOperationOutcome(getIssues()).withId(probeId);
    }

    public List<OperationOutcomeIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<OperationOutcomeIssue> issues) {
        this.issues = issues;
    }
    
    public FHIROperationException withIssue(OperationOutcomeIssue... issues) {
        if (issues != null) {
            for (OperationOutcomeIssue issue : issues) {
                getIssues().add(issue);
            }    
        }
        return this;
    }
    
    public FHIROperationException withIssue(Collection<OperationOutcomeIssue> issues) {
        if (issues != null) {
            getIssues().addAll(issues);
        }
        return this;
    }
}
