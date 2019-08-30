/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.util.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.model.resource.OperationOutcome;
import com.ibm.watson.health.fhir.model.type.IssueSeverity;
import com.ibm.watson.health.fhir.model.type.IssueType;
import com.ibm.watson.health.fhir.model.util.FHIRUtil;

/**
 * @author rarnold
 *
 */
public class OperationOutcomeTest {

    @Test
    public void buildOperationOutcomeIssue() {
        OperationOutcome.Issue issue = FHIRUtil.buildOperationOutcomeIssue("test", IssueType.ValueSet.INVALID);
        assertNotNull(issue);
        assertEquals(issue.getSeverity(), IssueSeverity.FATAL);
        assertEquals(issue.getCode(), IssueType.INVALID);
        assertEquals(issue.getDetails().getText().getValue(), "test");
    }
}
