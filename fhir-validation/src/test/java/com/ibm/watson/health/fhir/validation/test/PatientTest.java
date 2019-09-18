/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.validation.test;

import static com.ibm.watson.health.fhir.model.type.String.string;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.resource.Patient;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.Date;
import com.ibm.watson.health.fhir.model.type.Extension;
import com.ibm.watson.health.fhir.model.type.HumanName;
import com.ibm.watson.health.fhir.model.type.Id;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.IssueSeverity;
import com.ibm.watson.health.fhir.model.type.Meta;
import com.ibm.watson.health.fhir.model.type.Narrative;
import com.ibm.watson.health.fhir.model.type.NarrativeStatus;
import com.ibm.watson.health.fhir.model.type.Reference;
import com.ibm.watson.health.fhir.model.type.Xhtml;
import com.ibm.watson.health.fhir.validation.FHIRValidator;

/**
 * Demonstrates the Creation of a Patient and Subsequent validation against the specification.
 * 
 * Further, shows how to add ID/META inline. 
 * 
 * @author pbastide 
 *
 */
public class PatientTest {

    @Test
    public void testPatient() {
        Patient p = buildTestPatient();
        checkForIssuesWithValidation(p,false,false);
    }
    
    /**
     * Builds an example Patient as part of the FHIR Bundle
     * 
     * @return
     */
    public static Patient buildTestPatient() {
        Id id = Id.builder()
                    .value(UUID.randomUUID().toString())
                    .extension(Extension.builder()
                        .url("http://www.ibm.com/someExtension")
                        .value(string("Hello, World!"))
                        .build())
                    .build();

        Meta meta =
                Meta.builder()
                    .versionId(Id.of("1"))
                    .lastUpdated(Instant.now(ZoneOffset.UTC))
                    .build();

        com.ibm.watson.health.fhir.model.type.String given =
                com.ibm.watson.health.fhir.model.type.String.builder()
                .value("John").extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(string("value and extension"))
                    .build())
                .build();

        com.ibm.watson.health.fhir.model.type.String otherGiven =
                com.ibm.watson.health.fhir.model.type.String.builder()
                .extension(Extension.builder().url("http://www.ibm.com/someExtension")
                    .value(string("extension only")).build())
                .build();

        HumanName name =
                HumanName.builder()
                    .id("someId").given(given)
                    .given(otherGiven)
                    .given(string("value no extension"))
                    .family(string("Doe")).build();

        java.lang.String uUID = UUID.randomUUID().toString();

        Reference providerRef =
                Reference.builder().reference(string("urn:uuid:" + uUID)).build();

        return Patient.builder().id(id)
                .active(com.ibm.watson.health.fhir.model.type.Boolean.TRUE)
                .multipleBirth(com.ibm.watson.health.fhir.model.type.Integer.of(2))
                .meta(meta).name(name).birthDate(Date.of(LocalDate.now()))
                .generalPractitioner(providerRef).text(
                    Narrative.builder()
                        .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                        .status(NarrativeStatus.GENERATED).build())
                .build();
    }
    
    
    /**
     * Code to check your resource is valid.
     * 
     * @param resource
     * @param failOnValidationException
     * @param failOnWarning
     */
    public static void checkForIssuesWithValidation(Resource resource,
        boolean failOnValidationException,
        boolean failOnWarning) {

        List<Issue> issues = Collections.emptyList();
        try {
            issues = FHIRValidator.validator(resource).validate();
        } catch (Exception e) {
            if (failOnValidationException) {
                System.out.println("Unable to validate the resource");
                // System.exit(-3);
            }
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
                System.out.println("Fail on Errors " + nonWarning);
                // System.exit(-1);
            }

            if (failOnWarning && allOtherIssues > 0) {
                System.out.println("Fail on Warnings " + allOtherIssues);
                // System.exit(-2);
            }
        } else {
            System.out.println("Passed with no issues in validation");
        }

    }
}
