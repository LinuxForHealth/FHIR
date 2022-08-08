/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.util;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRConstants;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;

public class IssueTypeToHttpStatusMapperTest {
    @Test
    public void testExtensionCodeNotFound__bad_request() {
        Coding coding = Coding.builder().code(Code.of("value")).system(Uri.of("http://something.com/system")).build();

        IssueType issueType = IssueType.NOT_FOUND.toBuilder()
                .extension(Extension.builder()
                        .url(FHIRConstants.EXT_BASE + "not-found-detail")
                        .value(Code.of("code")).build()).build();

        OperationOutcome.Issue issue = OperationOutcome.Issue.builder().severity(IssueSeverity.ERROR).code(issueType)
                .details(CodeableConcept.builder().coding(coding).build()).build();

        System.out.println(issue.toString());

        Response.Status status = IssueTypeToHttpStatusMapper.issueListToStatus(Arrays.asList(issue));
        assertEquals( status, Response.Status.BAD_REQUEST );
    }
}
