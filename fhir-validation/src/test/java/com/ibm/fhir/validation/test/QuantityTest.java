/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.validation.test;

import static org.testng.Assert.fail;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Distance;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.validation.FHIRValidator;

/**
 * Tests FHIR spec validation of the Quantity element variations.
 */
public class QuantityTest {

    @Test
    public void testValidPatient() {
        Patient p = buildTestPatient();
        checkForIssuesWithValidation(p, 0);
    }

    @Test
    public void testAgeExtensionWithValidValue() {
        Extension ageExtension =
                Extension.builder().url("http://example.com/favorite-age").value(Age.builder().system(Uri.of("http://unitsofmeasure.org")).code(Code.of("a")).value(Decimal.of(10)).build()).build();
        Patient p = buildTestPatient().toBuilder().extension(Collections.singletonList(ageExtension)).build();
        // Will get one warning for the extension being unknown
        checkForIssuesWithValidation(p, 1);
    }

    @Test
    public void testAgeExtensionWithInvalidValue() {
        Extension ageExtension =
                Extension.builder().url("http://example.com/favorite-age").value(Age.builder().system(Uri.of("http://unitsofmeasure.org")).code(Code.of("INVALIDVALUE")).value(Decimal.of(10)).build()).build();
        Patient p = buildTestPatient().toBuilder().extension(Collections.singletonList(ageExtension)).build();
        // Will get one warning for the extension being unknown, and two more for the invalid value
        checkForIssuesWithValidation(p, 3);
    }

    @Test
    public void testDistanceExtensionWithValidValue() {
        Extension distanceExtension =
                Extension.builder().url("http://example.com/favorite-distance").value(Distance.builder().system(Uri.of("http://unitsofmeasure.org")).code(Code.of("m")).value(Decimal.of(10)).build()).build();
        Patient p = buildTestPatient().toBuilder().extension(Collections.singletonList(distanceExtension)).build();
        // Will get one warning for the extension being unknown
        checkForIssuesWithValidation(p, 1);
    }

    @Test
    public void testDistanceExtensionWithInvalidValue() {
        Extension distanceExtension =
                Extension.builder().url("http://example.com/favorite-distance").value(Distance.builder().system(Uri.of("http://unitsofmeasure.org")).code(Code.of("INVALIDVALUE")).value(Decimal.of(10)).build()).build();
        Patient p = buildTestPatient().toBuilder().extension(Collections.singletonList(distanceExtension)).build();
        // Will get one warning for the extension being unknown, and two more for the invalid value
        checkForIssuesWithValidation(p, 3);
    }

    @Test
    public void testDurationExtensionWithValidValue() {
        Extension durationExtension =
                Extension.builder().url("http://example.com/favorite-duration").value(Duration.builder().system(Uri.of("http://unitsofmeasure.org")).code(Code.of("d")).value(Decimal.of(10)).build()).build();
        Patient p = buildTestPatient().toBuilder().extension(Collections.singletonList(durationExtension)).build();
        // Will get one warning for the extension being unknown
        checkForIssuesWithValidation(p, 1);
    }

    @Test
    public void testDurationExtensionWithInvalidValue() {
        Extension durationExtension =
                Extension.builder().url("http://example.com/favorite-duration").value(Duration.builder().system(Uri.of("http://unitsofmeasure.org")).code(Code.of("INVALID")).value(Decimal.of(10)).build()).build();
        Patient p = buildTestPatient().toBuilder().extension(Collections.singletonList(durationExtension)).build();
        // Will get one warning for the extension being unknown, and two more for the invalid value
        checkForIssuesWithValidation(p, 3);
    }

    /**
     * Builds a valid Patient.
     * 
     * @return a valid Patient
     */
    public static Patient buildTestPatient() {
        String id = UUID.randomUUID().toString();
        Meta meta = Meta.builder().versionId(Id.of("1")).lastUpdated(Instant.now(ZoneOffset.UTC)).build();
        return Patient.builder().id(id).meta(meta).text(Narrative.builder().div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>")).status(NarrativeStatus.GENERATED).build()).build();
    }

    /**
     * Checks for validation issues.
     * 
     * @param resource
     *            the resource
     * @param numWarningsExpected
     *            number of expected validation warnings
     */
    public static void checkForIssuesWithValidation(Resource resource, int numWarningsExpected) {

        List<Issue> issues = Collections.emptyList();
        try {
            issues = FHIRValidator.validator().validate(resource);
        } catch (Exception e) {
            fail("Unable to validate the resource");
        }

        if (!issues.isEmpty()) {
            System.out.println("Printing Issue with Validation");
            int nonWarning = 0;
            int allOtherIssues = 0;
            for (Issue issue : issues) {
                if (IssueSeverity.ERROR.getValue().compareTo(issue.getSeverity().getValue()) == 0
                        || IssueSeverity.FATAL.getValue().compareTo(issue.getSeverity().getValue()) == 0) {
                    nonWarning++;
                } else {
                    allOtherIssues++;
                }
                System.out.println("level: " + issue.getSeverity().getValue() + ", details: "
                        + issue.getDetails().getText().getValue() + ", expression: "
                        + issue.getExpression().get(0).getValue());
            }

            System.out.println("count = [" + issues.size() + "]");

            if (nonWarning > 0) {
                fail("Fail on Errors " + nonWarning);
            }

            if (numWarningsExpected != allOtherIssues) {
                fail("Fail on Warnings " + allOtherIssues);
            }
        } else {
            System.out.println("Passed with no issues in validation");
        }
    }
}
