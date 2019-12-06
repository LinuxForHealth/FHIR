/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.validation.test;

import static com.ibm.fhir.model.type.String.string;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.DiagnosticReport;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.DiagnosticReportStatus;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.validation.FHIRValidator;

/**
 * Demonstrates the Creation of a DiagnosticReport and Subsequent validation against the specification.
 * 
 * Further, shows how to add ID/META inline. 
 * 
 * @author pbastide 
 *
 */
public class DiagnosticReportTest {

    @Test
    public void testDiagnosticReport() {
        DiagnosticReport diagnosticReport = buildDiagnosticReport();
        checkForIssuesWithValidation(diagnosticReport,false,false);
    }
    
    public DiagnosticReport buildDiagnosticReport() {
        
        String id = UUID.randomUUID().toString();

        Meta meta =
            Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();
        
        // Identify as generated. 
        Narrative narrative =
                Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build();
       
        Reference subject = Reference.builder().display(string("Patient/my-ptnt-id")).build();
        Reference performer = Reference.builder().display(string("Practitioner/my-docs-id")).build();
        Reference resultsIntepreter = Reference.builder().display(string("Practitioner/my-radiologist-id")).build();
        
        CodeableConcept code = getCodeableConcept(Collections.emptyList(),"radiology-report");
        
        
        List<Reference> basedOns = Arrays.asList(Reference.builder().display(string("Observation/1-2-3-4")).build());
        
        
        Reference specimen = Reference.builder().display(string("Specimin/1-2-3-4")).build(); 
        
        return DiagnosticReport.builder()
                .id(id).meta(meta).text(narrative)
                .status(DiagnosticReportStatus.PRELIMINARY)
                .subject(subject)
                .performer(performer)
                .resultsInterpreter(resultsIntepreter)
                .effective(DateTime.now())
                .issued(Instant.now())
                .code(code)
                .basedOn(basedOns)
                .specimen(specimen)
                .build(); 
    }
    
    public CodeableConcept getCodeableConcept(List<Coding> codings, String text) {
        CodeableConcept.Builder builder = CodeableConcept.builder();
        if (codings != null) {
            for (Coding coding : codings) {
                builder.coding(coding);
            }
        }

        if (text != null) {
            builder.text(string(text));
        }

        return builder.build();
    }

    public Coding getCoding(String system, String version, String code, String display,
        java.lang.Boolean userSelected) {
        Coding.Builder coding = Coding.builder();
        if (system != null) {
            coding.system(Uri.of(system));
        }

        if (version != null) {
            coding.version(string(version));
        }

        if (code != null) {
            coding.code(Code.code(code));
        }

        if (display != null) {
            coding.display(string(display));
        }

        if (userSelected != null) {
            coding.userSelected(com.ibm.fhir.model.type.Boolean.builder().value(userSelected).build());
        }

        return coding.build();
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
            issues = FHIRValidator.validator().validate(resource);
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
