/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.validation.test;

import static com.ibm.fhir.model.type.String.string;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Appointment;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.AppointmentStatus;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.ParticipantRequired;
import com.ibm.fhir.model.type.code.ParticipationStatus;
import com.ibm.fhir.validation.FHIRValidator;

/**
 * Demonstrates the Creation of an Appointment and Subsequent validation against the specification.
 * 
 * Further, shows how to add ID/META inline.
 * 
 * @author pbastide
 *
 */
public class AppointmentTest {

    @Test
    public void testAppointment() {
        Appointment appointment = buildAppointment();
        checkForIssuesWithValidation(appointment, false, false);
    }

    /**
     * builds a simple appointment 
     * @return
     */
    public Appointment buildAppointment() {
        String id = UUID.randomUUID().toString();

        Meta meta =
            Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();
    
        // Identify as generated
        Narrative narrative =
                Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build();
        
        // Build the participant details
        List<CodeableConcept> types = Arrays.asList(getCodeableConcept(Collections.emptyList(),"doctor"));
        Reference actor = Reference.builder().display(string("Practitioner/my-docs-id")).build();
        List<Appointment.Participant> participants = Arrays.asList(getAppointmentParticipant(types, actor));
        
        return Appointment.builder().id(id)
                .meta(meta)
                .text(narrative).status(AppointmentStatus.PROPOSED)
                .participant(participants)
                .build();
    }
    
    /**
     * builds an appointment participant. 
     * 
     * @param types
     * @param actor
     * @return
     */
    public Appointment.Participant getAppointmentParticipant(List<CodeableConcept> types,
        Reference actor) {        
        
        return Appointment.Participant.builder()
                .type(types)
                .actor(actor)
                .required(ParticipantRequired.REQUIRED)
                .status(ParticipationStatus.ACCEPTED).build();
    }
    
    /**
     * if youw ant to get more sophisticated and add to the input, you could do something like this. 
     * 
     * @param appointment
     * @param identifiers
     * @param status
     * @param cancelationReason
     * @param serviceCategories
     * @param serviceTypes
     * @param specialities
     * @param appointmentType
     * @param reasonCodes
     * @param reasonReferences
     * @param priority
     * @param description
     * @param supportingInformations
     * @param start
     * @param end
     * @param minutesDuration
     * @param slots
     * @param dateTime
     * @param comment
     * @param patientInstruction
     * @param basedOns
     * @param participants
     * @param requestedPeriods
     * @return
     * @throws FHIRException
     */
    public Appointment addToAppointment(Appointment appointment, List<Identifier> identifiers, String status,
        String cancelationReason, List<String> serviceCategories,
        List<String> serviceTypes, List<String> specialities, String appointmentType,
        List<String> reasonCodes, List<String> reasonReferences,
        java.lang.Integer priority, String description, List<String> supportingInformations,
        String start, String end, java.lang.Integer minutesDuration,
        List<String> slots, String dateTime, String comment, String patientInstruction,
        List<String> basedOns, List<Appointment.Participant> participants,
        List<Period> requestedPeriods) throws FHIRException {
        
        Appointment.Builder builder = appointment.toBuilder();

        if (identifiers != null) {
            for (Identifier identifier : identifiers) {
                builder.identifier(identifier);
            }
        }

        if (cancelationReason != null) {

            List<Coding> codings = new ArrayList<>();
            codings.add(getCoding("http://terminology.hl7.org/CodeSystem/appointment-cancellation-reason", "4.0.0", cancelationReason, cancelationReason, java.lang.Boolean.FALSE));

            CodeableConcept concept = getCodeableConcept(codings, cancelationReason);
            builder.cancelationReason(concept);
        }

        if (serviceCategories != null) {
            for (String serviceCategory : serviceCategories) {
                List<Coding> codings = new ArrayList<>();

                codings.add(getCoding("http://terminology.hl7.org/CodeSystem/service-category", "4.0.0", serviceCategory, serviceCategory, java.lang.Boolean.FALSE));

                CodeableConcept concept = getCodeableConcept(codings, serviceCategory);

                builder.serviceCategory(concept);
            }
        }

        if (serviceTypes != null) {
            for (String serviceType : serviceTypes) {
                List<Coding> codings = new ArrayList<>();
                codings.add(getCoding("http://terminology.hl7.org/CodeSystem/service-type", "4.0.0", serviceType, serviceType, java.lang.Boolean.FALSE));

                CodeableConcept concept = getCodeableConcept(codings, serviceType);

                builder.serviceType(concept);
            }
        }

        if (specialities != null) {
            for (String specialty : specialities) {
                List<Coding> codings = new ArrayList<>();
                codings.add(getCoding("http://hl7.org/fhir/ValueSet/c80-practice-codes", "4.0.0", specialty, specialty, java.lang.Boolean.FALSE));

                CodeableConcept concept = getCodeableConcept(codings, specialty);

                builder.serviceType(concept);
            }
        }

        if (appointmentType != null) {
            List<Coding> codings = new ArrayList<>();
            codings.add(getCoding("http://terminology.hl7.org/CodeSystem/v2-0276", "2.9", appointmentType, appointmentType, java.lang.Boolean.FALSE));

            CodeableConcept concept = getCodeableConcept(codings, appointmentType);

            builder.appointmentType(concept);
        }

        if (reasonCodes != null) {
            for (String reasonCode : reasonCodes) {
                List<Coding> codings = new ArrayList<>();
                codings.add(getCoding("http://hl7.org/fhir/ValueSet/encounter-reason", "4.0.0", reasonCode, reasonCode, java.lang.Boolean.FALSE));

                CodeableConcept concept = getCodeableConcept(codings, reasonCode);

                builder.serviceType(concept);
            }
        }

        if (reasonReferences != null) {
            for (String reasonReference : reasonReferences) {
                builder.reasonReference(Reference.builder().display(string(reasonReference)).build());
            }
        }

        if (priority != null) {
            builder.priority(UnsignedInt.of(priority));
        }

        if (description != null) {
            builder.description(string(description));
        }

        if (supportingInformations != null) {
            for (String supportingInformation : supportingInformations) {
                builder.supportingInformation(Reference.builder().display(string(supportingInformation)).build());
            }
        }

        if (start != null) {
            builder.start(Instant.of(start));
        }

        if (end != null) {
            builder.end(Instant.of(end));
        }

        if (minutesDuration != null) {
            builder.minutesDuration(PositiveInt.of(minutesDuration));
        }

        if (slots != null) {
            for (String slot : slots) {
                builder.slot(Reference.builder().display(string(slot)).build());
            }
        }

        if (dateTime != null) {
            builder.created(DateTime.of(dateTime));
        }

        if (comment != null) {
            builder.comment(string(comment));
        }

        if (patientInstruction != null) {
            builder.patientInstruction(string(patientInstruction));
        }

        if (basedOns != null) {
            for (String basedOn : basedOns) {
                builder.basedOn(Reference.builder().display(string(basedOn)).build());
            }
        }

        if (requestedPeriods != null) {
            for (Period period : requestedPeriods) {
                builder.requestedPeriod(period);
            }
        }

        return builder.build();
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
