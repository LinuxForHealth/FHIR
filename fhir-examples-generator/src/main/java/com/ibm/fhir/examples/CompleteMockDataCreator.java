/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.datatype.DatatypeConfigurationException;

import com.ibm.fhir.model.builder.Builder;
import com.ibm.fhir.model.resource.AdverseEvent;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Appointment;
import com.ibm.fhir.model.resource.AuditEvent;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.ClaimResponse;
import com.ibm.fhir.model.resource.ClinicalUseDefinition;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.Composition;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.CoverageEligibilityResponse;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.FamilyMemberHistory;
import com.ibm.fhir.model.resource.HealthcareService;
import com.ibm.fhir.model.resource.InsurancePlan;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.resource.MessageDefinition;
import com.ibm.fhir.model.resource.MolecularSequence;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Questionnaire;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.RiskAssessment;
import com.ibm.fhir.model.resource.SupplyDelivery;
import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.code.AddressUse;
import com.ibm.fhir.model.type.code.CapabilityStatementKind;
import com.ibm.fhir.model.type.code.ContactPointUse;
import com.ibm.fhir.model.type.code.QuestionnaireItemOperator;
import com.ibm.fhir.model.type.code.QuestionnaireItemType;
import com.ibm.fhir.model.type.code.TriggerType;
import com.ibm.fhir.model.util.ValidationSupport;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class CompleteMockDataCreator extends DataCreatorBase {
    protected final PodamFactory podam;
    private static final String ENGLISH_US = "en-US";

    public CompleteMockDataCreator() throws IOException {
        super();
        podam = new PodamFactoryImpl();
    }

    @Override
    protected Builder<?> addData(com.ibm.fhir.model.type.Reference.Builder builder, String targetProfile) throws Exception {
        return addData(builder, -1, targetProfile);
    }

    @Override
    protected Builder<?> addData(Builder<?> builder, int choiceIndicator) throws Exception {
        return addData(builder, choiceIndicator, null);
    }

    private Builder<?> addData(Builder<?> builder, int choiceIndicator, String referenceTargetProfile) throws Exception {
        Method[] methods = builder.getClass().getDeclaredMethods();

        boolean empty = true;
        for (Method method : methods) {
            if (method.getName().equals("build") ||
                method.getName().equals("toString") ||
                method.getName().equals("hashCode") ||
                method.getName().equals("from") ||
                method.getName().equals("contained") ||
                method.getName().equals("extension")) {

                continue;
            }

            Class<?>[] parameterClasses = method.getParameterTypes();

            if (parameterClasses.length != 1) {
                throw new RuntimeException("Error adding data via builder " + builder.getClass() + "; expected 1 parameter, but found " + parameterClasses.length);
            }

            Class<?> parameterType = parameterClasses[0];
            // Special case to avoid infinite recursion
            if (builder instanceof Identifier.Builder && Reference.class.isAssignableFrom(parameterType)) {
                continue;
            }
            // Special case for Narrative
            if (builder instanceof Narrative.Builder && method.getName().equals("div")) {
                ((Narrative.Builder) builder).div(xhtml("<div xmlns=\"http://www.w3.org/1999/xhtml\"></div>"));
                continue;
            }

            Object argument = null;
            if (Element.class.isAssignableFrom(parameterType)
                || Collection.class.isAssignableFrom(parameterType)) {

                // filter out inhereted methods like Code.Builder.extension and String.Builder.extension
                if (builder.getClass().equals(method.getReturnType())) {

                    ////////
                    // Skips
                    ////////
                    if (// sqty-1: The comparator is not used on a SimpleQuantity
                        builder instanceof SimpleQuantity.Builder && method.getName().equals("comparator") ||
                        // rng-2: If present, low SHALL have a lower value than high
                        builder instanceof Range.Builder && method.getName().equals("high") ||
                        // tim-9: If there's an offset, there must be a when (and not C, CM, CD, CV)
                        builder instanceof Timing.Repeat.Builder && method.getName().equals("offset") ||
                        // tim-10: If there's a timeOfDay, there cannot be a when, or vice versa
                        builder instanceof Timing.Repeat.Builder && method.getName().equals("timeOfDay") ||
                        // inv-1: Last modified date must be greater than or equal to authored-on date.
                        builder instanceof Task.Builder && method.getName().equals("authoredOn") ||
                        // obs-6: dataAbsentReason SHALL only be present Observation.value[x] is not present
                        builder instanceof Observation.Builder && method.getName().equals("dataAbsentReason") ||
                        // que-4: A question cannot have both answerOption and answerValueSet
                        // que-5: Only 'choice' and 'open-choice' items can have answerValueSet
                        builder instanceof Questionnaire.Item.Builder && (method.getName().equals("answerValueSet") || method.getName().equals("answerOption")) ||
                        // que-10: Maximum length can only be declared for simple question types
                        builder instanceof Questionnaire.Item.Builder && method.getName().equals("maxLength") ||
                        // que-11: If one or more answerOption is present, initial[x] must be missing
                        builder instanceof Questionnaire.Item.Builder && method.getName().equals("initial") ||
                        // drq-1: Either a path or a searchParam must be provided, but not both
                        // drq-2: Either a path or a searchParam must be provided, but not both
                        (builder instanceof DataRequirement.CodeFilter.Builder || builder instanceof DataRequirement.DateFilter.Builder) && method.getName().equals("searchParam") ||
                        // vsd-3: Cannot have both concept and filter
                        builder instanceof ValueSet.Compose.Include.Builder && method.getName().equals("filter") ||
                        // fhs-1: Can have age[x] or born[x], but not both
                        builder instanceof FamilyMemberHistory.Builder && method.getName().equals("born") ||
                        // cmp-2: A section can only have an emptyReason it is empty
                        builder instanceof Composition.Section.Builder && method.getName().equals("emptyReason") ||
                        // app-4: Cancelation reason is only used for appointments that have been cancelled, or no-show
                        builder instanceof Appointment.Builder && method.getName().equals("cancelationReason") ||
                        // con-4: If condition is abated, then clinicalStatus must be either inactive, resolved, or remission
                        builder instanceof Condition.Builder && method.getName().equals("abatement") ||
                        // trd-1: Either timing, or a data requirement, but not both
                        builder instanceof TriggerDefinition.Builder && method.getName().equals("timing") ||
                        // ces-1: SHALL contain a category or a billcode but not both.
                        builder instanceof CoverageEligibilityResponse.Insurance.Item.Builder && method.getName().equals("category") ||
                        // sev-1: Either a name or a query (NOT both)
                        builder instanceof AuditEvent.Entity.Builder && method.getName().equals("query") ||
                        // mea-1: Stratifier SHALL be either a single criteria or a set of criteria components
                        builder instanceof Measure.Group.Stratifier.Builder && method.getName().equals("component") ||
                        // cud-1: Indication, Contraindication, Interaction, UndesirableEffect and Warning cannot be used in the same instance
                        // special-case this one so that we get one of each type (i.e. like a choice type with 5 choices)
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 0 && method.getName().equals("indication") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 1 && method.getName().equals("contraindication") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 2 && method.getName().equals("interaction") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 3 && method.getName().equals("undesirableEffect") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 4 && method.getName().equals("warning") ||
                        // cpl-3: Provide a reference or detail, not both
                        builder instanceof CarePlan.Activity.Builder && method.getName().equals("detail")) {

                        continue;
                    }
                    // per-1: If present, start SHALL have a lower value than end
                    if (builder instanceof Period.Builder) {
                        if (choiceIndicator % 2 == 0 && method.getName().equals("start")) {
                            // on evens, skip start
                            continue;
                        } else if (method.getName().equals("end")) {
                            // on odds, skip end
                            continue;
                        }
                    }
                    /////////////////
                    // Special values
                    /////////////////
                    // Must be a valid BCP-47 code (Code)
                    if ((builder instanceof Attachment.Builder && "language".equals(method.getName()))
                            || (builder instanceof CodeSystem.Concept.Designation.Builder && "language".equals(method.getName()))
                            || (builder instanceof Resource.Builder && "language".equals(method.getName()))
                            || (builder instanceof ValueSet.Compose.Include.Concept.Designation.Builder && "language".equals(method.getName()))) {
                        argument = Code.of(ENGLISH_US);
                    }
                    // Must contain a valid BCP-47 system and code (CodeableConcept)
                    else if ((builder instanceof ClaimResponse.ProcessNote.Builder && "language".equals(method.getName()))
                            || (builder instanceof ExplanationOfBenefit.ProcessNote.Builder && "language".equals(method.getName()))
                            || (builder instanceof Patient.Communication.Builder && "language".equals(method.getName()))
                            || (builder instanceof RelatedPerson.Communication.Builder && "language".equals(method.getName()))) {
                        argument = CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of(ENGLISH_US)).build()).build();
                    }
                    // Must contain a valid BCP-47 system and code (List<CodeableConcept>)
                    else if ((builder instanceof HealthcareService.Builder && "communication".equals(method.getName()))
                            || (builder instanceof Practitioner.Builder && "communication".equals(method.getName()))) {
                        argument = Collections.singletonList(CodeableConcept.builder().coding(Coding.builder().system(Uri.of(ValidationSupport.BCP_47_URN)).code(Code.of(ENGLISH_US)).build()).build());
                    }
                    // drt-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.
                    else if (builder instanceof Duration.Builder && method.getName().equals("code")) {
                        argument = Code.of("h");
                    }
                    else if (builder instanceof Duration.Builder && method.getName().equals("system")) {
                        argument = Uri.of("http://unitsofmeasure.org");
                    }
                    // age-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.  If value is present, it SHALL be positive.
                    else if (builder instanceof Age.Builder && method.getName().equals("code")) {
                        argument = Code.of("a");
                    }
                    else if (builder instanceof Age.Builder && method.getName().equals("system")) {
                        argument = Uri.of("http://unitsofmeasure.org");
                    }
                    // md-1:  Max must be postive int or *
                    else if (builder instanceof MessageDefinition.Focus.Builder && method.getName().equals("max")) {
                        argument = string("*");
                    }
                    // ras-2:  probability is decimal implies (probability as decimal) <= 100
                    else if (builder instanceof RiskAssessment.Prediction.Builder && method.getName().equals("probability")) {
                        argument = Decimal.of(Math.random() * 100);
                    }

                    else if (builder instanceof Reference.Builder && method.getName().equals("type")) {
                        if (referenceTargetProfile != null) {
                            // References with specific target profiles
                            argument = Uri.of(referenceTargetProfile);
                        } else {
                            argument = Uri.of("Basic");
                        }
                    }
                    else if (builder instanceof Reference.Builder && method.getName().equals("reference")) {
                        // References with specific target profiles
                        if (referenceTargetProfile != null) {
                            argument = string(referenceTargetProfile + "/" + podam.manufacturePojo(String.class).replace("_", "-"));
                        } else {
                            argument = string("Basic/" + podam.manufacturePojo(String.class).replace("_", "-"));
                        }
                    }

                    // CodeableConcepts with required bindings
                    else if (builder instanceof AdverseEvent.Builder && method.getName().equals("severity")) {
                        String value = "mild";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/adverse-event-severity"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(titleCase(value)))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof AdverseEvent.Builder && method.getName().equals("outcome")) {
                        String value = "resolved";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/adverse-event-outcome"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(titleCase(value)))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof AllergyIntolerance.Builder && method.getName().equals("clinicalStatus")) {
                        String value = "active";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(titleCase(value)))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof AllergyIntolerance.Builder && method.getName().equals("verificationStatus")) {
                        String value = "unconfirmed";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(titleCase(value)))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof Condition.Builder && method.getName().equals("clinicalStatus")) {
                        String value = "active";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/condition-clinical"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(titleCase(value)))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof Condition.Builder && method.getName().equals("verificationStatus")) {
                        String value = "unconfirmed";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/condition-ver-status"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(titleCase(value)))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof InsurancePlan.Plan.SpecificCost.Benefit.Cost.Builder && method.getName().equals("applicability")) {
                        String value = "other";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/applicability"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(titleCase(value)))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if ((builder instanceof Measure.Builder || builder instanceof MeasureReport.Builder) && method.getName().equals("improvementNotation")) {
                        String value = "increase";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/measure-improvement-notation"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string("Increased score indicates improvement"))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof MolecularSequence.StructureVariant.Builder && method.getName().equals("variantType")) {
                        String value = "LA9658-1";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://loinc.org"))
                                    .version(string("2.67"))
                                    .code(Code.of(value))
                                    .display(string(value))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (builder instanceof SupplyDelivery.Builder && method.getName().equals("type")) {
                        String value = "medication";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/supply-item-type"))
                                    .version(string("4.0.1"))
                                    .code(Code.of(value))
                                    .display(string(value))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    } else if (method.getName().equals("unitOfMeasure")) {
                        String value = "mm[Hg]";
                        argument = CodeableConcept.builder()
                                .coding(Coding.builder()
                                    .system(Uri.of("http://unitsofmeasure.org"))
                                    .version(string("2.1"))
                                    .code(Code.of(value))
                                    .display(string(value))
                                    .userSelected(com.ibm.fhir.model.type.Boolean.FALSE)
                                    .build())
                                .text(string(value))
                                .build();
                    }

                    /////////////////
                    // Everything else
                    /////////////////
                    else {
                        argument = createArgument(builder.getClass().getEnclosingClass(), method, parameterType, 0, choiceIndicator);
                    }

                    if (argument != null && !(argument instanceof Collection && ((Collection<?>) argument).isEmpty())) {
                        method.invoke(builder, argument);
                        empty = false;
                    }
                }
            }
        }

        if (empty) {
            if (builder instanceof Element.Builder){
                // We have a primitive type (i.e. an edge node)
                createPrimitive((Element.Builder) builder);
            } else {
                throw new RuntimeException("Something went wrong; builder of type " + builder.getClass() + " doesn't have any valid entries.");
            }
        }
        return builder;
    }

    protected Element.Builder createPrimitive(Element.Builder builder) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (builder instanceof com.ibm.fhir.model.type.Boolean.Builder) {
            handleBoolean((com.ibm.fhir.model.type.Boolean.Builder) builder);
        } else if (builder instanceof com.ibm.fhir.model.type.Integer.Builder) {
            handleInteger((com.ibm.fhir.model.type.Integer.Builder) builder);
        } else if (builder instanceof com.ibm.fhir.model.type.Decimal.Builder) {
            handleDecimal((com.ibm.fhir.model.type.Decimal.Builder) builder);
        } else if (builder instanceof Id.Builder) {
            handleId((Id.Builder) builder);
        } else if (builder instanceof Code.Builder) {
            handleCode((Code.Builder) builder);
        } else if (builder instanceof Uuid.Builder) {
            handleUuid((Uuid.Builder) builder);
        } else if (builder instanceof Oid.Builder) {
            handleOid((Oid.Builder) builder);
        } else if (builder instanceof Uri.Builder) {
            handleUri((Uri.Builder) builder);
        } else if (builder instanceof com.ibm.fhir.model.type.String.Builder) {
            handleString((com.ibm.fhir.model.type.String.Builder) builder);
        } else if (builder instanceof Base64Binary.Builder) {
            handleBase64Binary((Base64Binary.Builder) builder);
        } else if (builder instanceof Instant.Builder) {
            try {
                handleInstant((Instant.Builder) builder);
            } catch (DatatypeConfigurationException e1) {
                e1.printStackTrace();
                setDataAbsentReason(builder);
            }
        } else if (builder instanceof Date.Builder) {
            handleDate((Date.Builder) builder);
        } else if (builder instanceof DateTime.Builder) {
            handleDateTime((DateTime.Builder) builder);
        } else if (builder instanceof Time.Builder) {
            try {
                handleTime((Time.Builder) builder);
            } catch (DatatypeConfigurationException e1) {
                e1.printStackTrace();
                setDataAbsentReason(builder);
            }
        } else  {
            setDataAbsentReason(builder);
        }
        return builder;
    }

    protected void handleBoolean(com.ibm.fhir.model.type.Boolean.Builder bool) {
        bool.value(podam.manufacturePojo(Boolean.class));
    }

    protected void handleId(com.ibm.fhir.model.type.Id.Builder string) {
        string.value(podam.manufacturePojo(String.class).replace("_", "-"));
    }

    protected void handleString(com.ibm.fhir.model.type.String.Builder string) {
        string.value(podam.manufacturePojo(String.class));
    }

    protected void handleInteger(com.ibm.fhir.model.type.Integer.Builder integer) {
        integer.value(podam.manufacturePojo(Integer.class));
    }

    protected void handleDecimal(com.ibm.fhir.model.type.Decimal.Builder decimal) {
        decimal.value(podam.manufacturePojo(BigDecimal.class));
    }

    private void handleUuid(com.ibm.fhir.model.type.Uuid.Builder uuid) {
        uuid.value("urn:uuid:" + UUID.randomUUID().toString());
    }

    private void handleOid(com.ibm.fhir.model.type.Oid.Builder oid) {
        oid.value("urn:oid:2.16.840.1.113883.3.18");
    }

    protected void handleUri(Uri.Builder uri) {
        uri.value(podam.manufacturePojo(URI.class).toString());
    }

    protected void handleCode(Code.Builder code) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?> elementClass = code.getClass().getEnclosingClass();

        Class<?>[] classes = elementClass.getClasses();
        // If the element has a ValueSet, set one of the values randomly
        for (Class<?> clazz : classes) {
            if ("Value".equals(clazz.getSimpleName())) {
                Object[] enumConstants = clazz.getEnumConstants();
                Object enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(0, enumConstants.length)];

                // que-1: Group items must have nested items, display items cannot have nested items
                // que-3: Display items cannot have a "code" asserted (Questionnaire.item[0])
                // que-6: Required and repeat aren't permitted for display items (Questionnaire.item[0])
                // que-9: Read-only can't be specified for "display" items (Questionnaire.item[0])
                if (code instanceof QuestionnaireItemType.Builder) {
                    // "group" is the first constant and we skip nested items, so avoid that one
                    // "display" is the second constant and that one has a bunch of extra rules, so avoid it too
                    enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(2, enumConstants.length)];
                }
                // que-7: If the operator is 'exists', the value must be a boolean
                if (code instanceof QuestionnaireItemOperator.Builder) {
                    // 'exists' is the first constant and we use all kinds of values, so avoid that one
                    enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(1, enumConstants.length)];
                }
                // org-2:  An address of an organization can never be of use 'home'
                if (code instanceof AddressUse.Builder) {
                    // 'home' is the first constant and we use all kinds of values, so avoid that one
                    enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(1, enumConstants.length)];
                }
                // org-3:  The telecom of an organization can never be of use 'home'
                if (code instanceof ContactPointUse.Builder) {
                    // 'home' is the first constant and we use all kinds of values, so avoid that one
                    enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(1, enumConstants.length)];
                }
                // cpb-3:  Messaging end-point is required (and is only permitted) when a statement is for an implementation.
                // cpb-15: If kind = capability, implementation must be absent, software must be present
                if (code instanceof CapabilityStatementKind.Builder) {
                    // use 'instance' to avoid the other special cases
                    enumConstant = CapabilityStatementKind.Value.INSTANCE;
                }
                // trd-3:   A named event requires a name, a periodic event requires timing, and a data event requires data
                if (code instanceof TriggerType.Builder) {
                    if (enumConstant == TriggerType.Value.PERIODIC) {
                        // trd-1 has prevented us from including a timing element, but we're good with any other type
                        enumConstant = TriggerType.Value.DATA_MODIFIED;
                    }
                }

                String enumValue = (String) clazz.getMethod("value").invoke(enumConstant);
                code.value(enumValue);
                return;
            }
        }
        // Otherwise just set it to a random string
        code.value(podam.manufacturePojo(String.class));
    }

    protected void handleBase64Binary(Base64Binary.Builder base64Binary) {
        base64Binary.value(podam.manufacturePojo(byte[].class));
    }

    protected void handleInstant(Instant.Builder element) throws DatatypeConfigurationException {
        element.value(podam.manufacturePojo(ZonedDateTime.class));
    }

    protected void handleDate(Date.Builder element) {
        element.value(podam.manufacturePojo(LocalDate.class));
//        element.value(randomInt(1900, 2020).toString() + "-" + randomInt(10, 11) + "-" + randomInt(10,28));
    }

    protected void handleDateTime(DateTime.Builder element) {
        element.value(podam.manufacturePojo(ZonedDateTime.class));
//        element.value(randomInt(1900, 2020).toString() + "-" + randomInt(10, 11) + "-" + randomInt(10,28));
    }

    protected void handleTime(Time.Builder element) throws DatatypeConfigurationException {
        element.value(podam.manufacturePojo(LocalTime.class));
    }
}
