/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.examples;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.datatype.DatatypeConfigurationException;

import com.ibm.watsonhealth.fhir.model.builder.Builder;
import com.ibm.watsonhealth.fhir.model.resource.Appointment;
import com.ibm.watsonhealth.fhir.model.resource.AuditEvent;
import com.ibm.watsonhealth.fhir.model.resource.CarePlan;
import com.ibm.watsonhealth.fhir.model.resource.Composition;
import com.ibm.watsonhealth.fhir.model.resource.Condition;
import com.ibm.watsonhealth.fhir.model.resource.CoverageEligibilityResponse;
import com.ibm.watsonhealth.fhir.model.resource.FamilyMemberHistory;
import com.ibm.watsonhealth.fhir.model.resource.Measure;
import com.ibm.watsonhealth.fhir.model.resource.MessageDefinition;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.Questionnaire;
import com.ibm.watsonhealth.fhir.model.resource.Task;
import com.ibm.watsonhealth.fhir.model.resource.ValueSet;
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.DataRequirement;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Oid;
import com.ibm.watsonhealth.fhir.model.type.QuestionnaireItemOperator;
import com.ibm.watsonhealth.fhir.model.type.QuestionnaireItemType;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.SimpleQuantity;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.TriggerDefinition;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Uuid;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class CompleteMockDataCreator extends DataCreatorBase {
    protected final PodamFactory podam;

    public CompleteMockDataCreator() throws IOException {
        super();
        podam = new PodamFactoryImpl();
    }

    @Override
    protected Builder<?> addData(Builder<?> builder, int choiceIndicator) throws Exception {
        Method[] methods = builder.getClass().getDeclaredMethods();
        
        boolean empty = true;
        for (Method method : methods) {
            if (method.getName().equals("build") ||
                method.getName().equals("getClass") || 
                method.getName().equals("wait") ||
                method.getName().equals("toString") ||
                method.getName().equals("hashCode") ||
                method.getName().equals("notify") ||
                method.getName().equals("notifyAll") ||
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
            
            Object argument = null;
            if (Element.class.isAssignableFrom(parameterType)
                || Collection.class.isAssignableFrom(parameterType)) {
            
                // filter out inhereted methods like Code.Builder.extension and String.Builder.extension
                if (builder.getClass().equals(method.getReturnType())) {
                    
                    ////////
                    // Skips
                    ////////
                    // sqty-1: The comparator is not used on a SimpleQuantity
                    if (builder instanceof SimpleQuantity.Builder && method.getName().equals("comparator")) {
                        continue;
                    }
                    // rng-2: If present, low SHALL have a lower value than high
                    if (builder instanceof Range.Builder && method.getName().equals("high")) {
                        continue;
                    }
                    // tim-9: If there's an offset, there must be a when (and not C, CM, CD, CV)
                    if (builder instanceof Timing.Repeat.Builder && method.getName().equals("offset")) {
                        continue;
                    }
                    // tim-10: If there's a timeOfDay, there cannot be a when, or vice versa
                    if (builder instanceof Timing.Repeat.Builder && method.getName().equals("timeOfDay")) {
                        continue;
                    }
                    // inv-1: Last modified date must be greater than or equal to authored-on date.
                    if (builder instanceof Task.Builder && method.getName().equals("authoredOn")) {
                        continue;
                    }
                    // obs-6: dataAbsentReason SHALL only be present if Observation.value[x] is not present
                    if (builder instanceof Observation.Builder && method.getName().equals("dataAbsentReason")) {
                        continue;
                    }
                    // que-4: A question cannot have both answerOption and answerValueSet
                    // que-5: Only 'choice' and 'open-choice' items can have answerValueSet
                    if (builder instanceof Questionnaire.Item.Builder && (method.getName().equals("answerValueSet") || method.getName().equals("answerOption"))) {
                        continue;
                    }
                    // que-10: Maximum length can only be declared for simple question types
                    if (builder instanceof Questionnaire.Item.Builder && method.getName().equals("maxLength")) {
                        continue;
                    }
                    // que-11: If one or more answerOption is present, initial[x] must be missing
                    if (builder instanceof Questionnaire.Item.Builder && method.getName().equals("initial")) {
                        continue;
                    }
                    // drq-1: Either a path or a searchParam must be provided, but not both
                    // drq-2: Either a path or a searchParam must be provided, but not both
                    if ((builder instanceof DataRequirement.CodeFilter.Builder || builder instanceof DataRequirement.DateFilter.Builder) && method.getName().equals("searchParam")) {
                        continue;
                    }
                    // vsd-3: Cannot have both concept and filter
                    if (builder instanceof ValueSet.Compose.Include.Builder && method.getName().equals("filter")) {
                        continue;
                    }
                    // fhs-1: Can have age[x] or born[x], but not both
                    if (builder instanceof FamilyMemberHistory.Builder && method.getName().equals("born")) {
                        continue;
                    }
                    // cmp-2: A section can only have an emptyReason if it is empty
                    if (builder instanceof Composition.Section.Builder && method.getName().equals("emptyReason")) {
                        continue;
                    }
                    // app-4: Cancelation reason is only used for appointments that have been cancelled, or no-show
                    if (builder instanceof Appointment.Builder && method.getName().equals("cancelationReason")) {
                        continue;
                    }
                    // con-4: If condition is abated, then clinicalStatus must be either inactive, resolved, or remission
                    if (builder instanceof Condition.Builder && method.getName().equals("abatement")) {
                        continue;
                    }
                    // trd-1: Either timing, or a data requirement, but not both
                    if (builder instanceof TriggerDefinition.Builder && method.getName().equals("timing")) {
                        continue;
                    }
                    // ces-1: SHALL contain a category or a billcode but not both.
                    if (builder instanceof CoverageEligibilityResponse.Insurance.Item.Builder && method.getName().equals("category")) {
                        continue;
                    }
                    // sev-1: Either a name or a query (NOT both)
                    if (builder instanceof AuditEvent.Entity.Builder && method.getName().equals("query")) {
                        continue;
                    }
                    // mea-1: Stratifier SHALL be either a single criteria or a set of criteria components
                    if (builder instanceof Measure.Group.Stratifier.Builder && method.getName().equals("component")) {
                        continue;
                    }
                    // cpl-3: Provide a reference or detail, not both
                    if (builder instanceof CarePlan.Activity.Builder && method.getName().equals("detail")) {
                        continue;
                    }
                    /////////////////
                    // Special values
                    /////////////////
                    // drt-1: There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.
                    if (builder instanceof Duration.Builder && method.getName().equals("code")) {
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
                        argument = com.ibm.watsonhealth.fhir.model.type.String.of("*");
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
        if (builder instanceof com.ibm.watsonhealth.fhir.model.type.Boolean.Builder) {
            handleBoolean((com.ibm.watsonhealth.fhir.model.type.Boolean.Builder) builder);
        } else if (builder instanceof com.ibm.watsonhealth.fhir.model.type.Integer.Builder) {
            handleInteger((com.ibm.watsonhealth.fhir.model.type.Integer.Builder) builder);
        } else if (builder instanceof com.ibm.watsonhealth.fhir.model.type.Decimal.Builder) {
            handleDecimal((com.ibm.watsonhealth.fhir.model.type.Decimal.Builder) builder);
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
        } else if (builder instanceof com.ibm.watsonhealth.fhir.model.type.String.Builder) {
            handleString((com.ibm.watsonhealth.fhir.model.type.String.Builder) builder);
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
    
    protected void handleBoolean(com.ibm.watsonhealth.fhir.model.type.Boolean.Builder bool) {
        bool.value(podam.manufacturePojo(Boolean.class));
    }
    
    protected void handleId(com.ibm.watsonhealth.fhir.model.type.Id.Builder string) {
        string.value(podam.manufacturePojo(String.class).replace("_", "-"));
    }
    
    protected void handleString(com.ibm.watsonhealth.fhir.model.type.String.Builder string) {
        string.value(podam.manufacturePojo(String.class));
    }
    
    protected void handleInteger(com.ibm.watsonhealth.fhir.model.type.Integer.Builder integer) {
        integer.value(podam.manufacturePojo(Integer.class));
    }
    
    protected void handleDecimal(com.ibm.watsonhealth.fhir.model.type.Decimal.Builder decimal) {
        decimal.value(podam.manufacturePojo(BigDecimal.class));
    }
    
    private void handleUuid(com.ibm.watsonhealth.fhir.model.type.Uuid.Builder uuid) {
        uuid.value("urn:uuid:" + UUID.randomUUID().toString());
    }
    
    private void handleOid(com.ibm.watsonhealth.fhir.model.type.Oid.Builder oid) {
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
            if ("ValueSet".equals(clazz.getSimpleName())) {
                Object[] enumConstants = clazz.getEnumConstants();
                Object enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(0, enumConstants.length)];
                
                // que-1: Group items must have nested items, display items cannot have nested items
                if (code instanceof QuestionnaireItemType.Builder) {
                    // Group is the first constant and we skip nested items, so avoid that one
                    enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(1, enumConstants.length)];
                }
                // que-7: If the operator is 'exists', the value must be a boolean
                if (code instanceof QuestionnaireItemOperator.Builder) {
                    // 'exists' is the first constant and we use all kinds of values, so avoid that one
                    enumConstant = enumConstants[ThreadLocalRandom.current().nextInt(1, enumConstants.length)];
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
