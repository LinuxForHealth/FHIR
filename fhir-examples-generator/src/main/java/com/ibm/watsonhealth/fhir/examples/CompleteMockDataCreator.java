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
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.datatype.DatatypeConfigurationException;

import com.ibm.watsonhealth.fhir.model.builder.Builder;
import com.ibm.watsonhealth.fhir.model.resource.List;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Oid;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Uuid;
import com.ibm.watsonhealth.fhir.examples.DataCreatorBase;

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
        Method[] methods = builder.getClass().getMethods();
        
        boolean empty = true;
        for (Method method : methods) {
            if (method.getName().equals("build") ||
                method.getName().equals("getClass") || 
                method.getName().equals("wait") ||
                method.getName().equals("toString") ||
                method.getName().equals("hashCode") ||
                method.getName().equals("notify") ||
                method.getName().equals("notifyAll")) {
                
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
            
            Object argument;
            if (Element.class.isAssignableFrom(parameterType)
                || List.class.isAssignableFrom(parameterType)) {
            
                argument = createArgument(builder.getClass().getEnclosingClass(), method, parameterType, 0, choiceIndicator);
                method.invoke(builder, argument);
                empty = false;
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
