/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;

/**
 * Operational Outcome Test
 */
public class OperationOutcomeTest {

    @Test
    public void buildOperationOutcomeIssue() {
        OperationOutcome.Issue issue = FHIRUtil.buildOperationOutcomeIssue("test", IssueType.INVALID);
        assertNotNull(issue);
        assertEquals(issue.getSeverity(), IssueSeverity.FATAL);
        assertEquals(issue.getCode(), IssueType.INVALID);
        assertEquals(issue.getDetails().getText().getValue(), "test");
    }
}
