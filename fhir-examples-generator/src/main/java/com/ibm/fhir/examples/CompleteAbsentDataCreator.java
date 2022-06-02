/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples;

import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

import com.ibm.fhir.model.builder.Builder;
import com.ibm.fhir.model.resource.Appointment;
import com.ibm.fhir.model.resource.AuditEvent;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.ClinicalUseDefinition;
import com.ibm.fhir.model.resource.Composition;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.CoverageEligibilityResponse;
import com.ibm.fhir.model.resource.FamilyMemberHistory;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.MessageDefinition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Questionnaire;
import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ModelSupport.ElementInfo;

public class CompleteAbsentDataCreator extends DataCreatorBase {
    private static final Quantity ABSENT_QUANTITY = Quantity.builder().extension(DATA_ABSENT).build();

    public CompleteAbsentDataCreator() throws IOException {
        super();
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
                    method.getName().equals("extension") ||
                    method.getName().equals("modifierExtension")) {
                continue;
            }

            Class<?>[] parameterClasses = method.getParameterTypes();

            if (parameterClasses.length != 1) {
                throw new RuntimeException("Error adding data via builder " + builder.getClass() + "; expected 1 parameter, but found " + parameterClasses.length);
//                return (Builder) method.invoke(builder);
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
                        // tim-4: duration SHALL be a non-negative value
                        // tim-7: If there's a durationMax, there must be a duration
                        builder instanceof Timing.Repeat.Builder && (method.getName().equals("duration") || method.getName().equals("durationMax")) ||
                        // tim-5: period SHALL be a non-negative value
                        // tim-6: If there's a periodMax, there must be a period
                        builder instanceof Timing.Repeat.Builder && (method.getName().equals("period") || method.getName().equals("periodMax")) ||
                        // tim-9: If there's an offset, there must be a when (and not C, CM, CD, CV)
                        builder instanceof Timing.Repeat.Builder && method.getName().equals("offset") ||
                        // tim-10: If there's a timeOfDay, there cannot be a when, or vice versa
                        builder instanceof Timing.Repeat.Builder && method.getName().equals("timeOfDay") ||
                        // drt-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.
                        builder instanceof Duration.Builder && method.getName().equals("value") ||
                        // age-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.  If value is present, it SHALL be positive.
                        builder instanceof Age.Builder && method.getName().equals("value") ||
                        builder instanceof Age.Builder && method.getName().equals("code") ||
                        builder instanceof Age.Builder && method.getName().equals("system") ||
                        // md-1:  Max must be postive int or *
                        builder instanceof MessageDefinition.Focus.Builder && method.getName().equals("max") ||
                        // inv-1: Last modified date must be greater than or equal to authored-on date.
                        builder instanceof Task.Builder && method.getName().equals("authoredOn") ||
                        // obs-6: dataAbsentReason SHALL only be present if Observation.value[x] is not present
                        // obs-7: If Observation.code is the same as an Observation.component.code then the value element associated with the code SHALL NOT be present
                        builder instanceof Observation.Builder && method.getName().equals("value") ||
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
                        builder instanceof FamilyMemberHistory.Builder && method.getName().equals("age") ||
                        // cmp-2: A section can only have an emptyReason if it is empty
                        builder instanceof Composition.Section.Builder && method.getName().equals("emptyReason") ||
                        // app-4: Cancelation reason is only used for appointments that have been cancelled, or no-show
                        builder instanceof Appointment.Builder && method.getName().equals("cancelationReason") ||
                        // con-4: If condition is abated, then clinicalStatus must be either inactive, resolved, or remission
                        builder instanceof Condition.Builder && method.getName().equals("abatement") ||
                        // trd-1: Either timing, or a data requirement, but not both
                        builder instanceof TriggerDefinition.Builder && method.getName().equals("timing") ||
                        // ces-1: SHALL contain a category or a billcode but not both.
                        builder instanceof CoverageEligibilityResponse.Insurance.Item.Builder && method.getName().equals("productOrService") ||
                        // sev-1: Either a name or a query (NOT both)
                        builder instanceof AuditEvent.Entity.Builder && method.getName().equals("name") ||
                        // mea-1: Stratifier SHALL be either a single criteria or a set of criteria components
                        builder instanceof Measure.Group.Stratifier.Builder && method.getName().equals("component") ||
                        // cud-1: Indication, Contraindication, Interaction, UndesirableEffect and Warning cannot be used in the same instance
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 0 && method.getName().equals("indication") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 1 && method.getName().equals("contraindication") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 2 && method.getName().equals("interaction") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 3 && method.getName().equals("undesirableEffect") ||
                        builder instanceof ClinicalUseDefinition.Builder && choiceIndicator % 5 != 4 && method.getName().equals("warning") ||
                        // cpl-3: Provide a reference or detail, not both
                        builder instanceof CarePlan.Activity.Builder && method.getName().equals("detail")) {

                        continue;
                    }
                    /////////////////
                    // Everything else
                    /////////////////

                    // coded elements with required bindings
                    ElementInfo elementInfo = ModelSupport.getElementInfo(builder.getClass().getEnclosingClass(), method.getName());
                    if (elementInfo != null && elementInfo.hasBinding() &&
                            (BindingStrength.Value.REQUIRED == elementInfo.getBinding().strength() ||
                             elementInfo.getBinding().maxValueSet() != null)) {

                        Class<?> realType = parameterType;
                        boolean isCollection = Collection.class.isAssignableFrom(parameterType);
                        if (isCollection) {
                            // The parameter is a Collection so infer the generic type
                            Type[] parameterTypes = method.getGenericParameterTypes();
                            if (parameterTypes[0] instanceof ParameterizedType) {
                                realType = (Class<?>) ((ParameterizedType) parameterTypes[0]).getActualTypeArguments()[0];
                            } else {
                                // TODO how can we find the generic type of the collection?!
                                throw new Exception("Type '" + parameterTypes[0].getTypeName() + "' is not generic!?");
                            }
                        }

                        if (CodeableConcept.class.isAssignableFrom(realType)) {
                            argument = ABSENT_CODEABLE_CONCEPT;
                        } else if (Coding.class.isAssignableFrom(realType)) {
                            argument = ABSENT_CODING;
                        } else if (Quantity.class.isAssignableFrom(realType)) {
                            argument = ABSENT_QUANTITY;
                        }

                        if (isCollection && argument != null) {
                            argument = Collections.singleton(argument);
                        }
                    }

                    if (argument == null){
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
                setDataAbsentReason((Element.Builder) builder);
            } else {
                throw new RuntimeException("Something went wrong; builder of type " + builder.getClass() + " doesn't have any valid entries.");
            }
        }
        return builder;
    }
}
