/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;

import com.ibm.watsonhealth.fhir.model.annotation.Choice;
import com.ibm.watsonhealth.fhir.model.annotation.Required;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Contributor;
import com.ibm.watsonhealth.fhir.model.type.Count;
import com.ibm.watsonhealth.fhir.model.type.DataRequirement;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Distance;
import com.ibm.watsonhealth.fhir.model.type.Dosage;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Expression;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.MoneyQuantity;
import com.ibm.watsonhealth.fhir.model.type.Oid;
import com.ibm.watsonhealth.fhir.model.type.ParameterDefinition;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.SampledData;
import com.ibm.watsonhealth.fhir.model.type.Signature;
import com.ibm.watsonhealth.fhir.model.type.SimpleQuantity;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.TriggerDefinition;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.type.Uuid;
import com.ibm.watsonhealth.fhir.model.type.Xhtml;

public final class ModelSupport {
    public static boolean DEBUG = false;    
    private static final Map<Class<?>, Map<String, ElementInfo>> MODEL_CLASS_ELEMENT_INFO_MAP = buildModelClassElementInfoMap();
    private static final Map<String, Class<?>> RESOURCE_TYPE_MAP = buildResourceTypeMap();
    private static final Map<Class<?>, Class<?>> CONCRETE_TYPE_MAP = buildConcreteTypeMap();
    
    private static final Set<Class<?>> CHOICE_ELEMENT_TYPES = new HashSet<>(Arrays.asList(
        Base64Binary.class, 
        com.ibm.watsonhealth.fhir.model.type.Boolean.class, 
        Canonical.class, 
        Code.class, 
        Date.class, 
        DateTime.class, 
        Decimal.class, 
        Id.class, 
        Instant.class, 
        com.ibm.watsonhealth.fhir.model.type.Integer.class, 
        Markdown.class, 
        Oid.class, 
        PositiveInt.class, 
        com.ibm.watsonhealth.fhir.model.type.String.class, 
        Time.class, 
        UnsignedInt.class, 
        Uri.class, 
        Url.class, 
        Uuid.class, 
        Address.class, 
        Age.class, 
        Annotation.class, 
        Attachment.class, 
        CodeableConcept.class, 
        Coding.class, 
        ContactPoint.class, 
        Count.class, 
        Distance.class, 
        Duration.class, 
        HumanName.class, 
        Identifier.class, 
        Money.class, 
        MoneyQuantity.class, // profiled type
        Period.class, 
        Quantity.class, 
        Range.class, 
        Ratio.class, 
        Reference.class, 
        SampledData.class, 
        SimpleQuantity.class, // profiled type
        Signature.class, 
        Timing.class, 
        ContactDetail.class, 
        Contributor.class, 
        DataRequirement.class, 
        Expression.class, 
        ParameterDefinition.class, 
        RelatedArtifact.class, 
        TriggerDefinition.class, 
        UsageContext.class, 
        Dosage.class));

    private ModelSupport() {
    }

    public static void init() {
        // allows us to initialize this class during startup
    }

    private static final class ElementInfo {
        private final Class<?> type;
        private final boolean required;
        private final boolean repeating;
        private final boolean choice;
        private final Set<Class<?>> choiceTypes;
        
        public ElementInfo(Class<?> type, boolean required, boolean repeating, boolean choice, Set<Class<?>> choiceTypes) {
            this.type = type;
            this.required = required;
            this.repeating = repeating;
            this.choice = choice;
            this.choiceTypes = choiceTypes;
        }
        
        public Set<Class<?>> getChoiceTypes() {
            return choiceTypes;
        }
        
        public Class<?> getType() {
            return type;
        }
        
        public boolean isChoice() {
            return choice;
        }
    
        public boolean isRepeating() {
            return repeating;
        }
        
        public boolean isRequired() {
            return required;
        }
    }

    private static Map<Class<?>, Class<?>> buildConcreteTypeMap() {
        Map<Class<?>, Class<?>> concreteTypeMap = new LinkedHashMap<>();
        concreteTypeMap.put(SimpleQuantity.class, Quantity.class);
        concreteTypeMap.put(MoneyQuantity.class, Quantity.class);
        return Collections.unmodifiableMap(concreteTypeMap);
    }

    private static Map<Class<?>, Map<String, ElementInfo>> buildModelClassElementInfoMap() {
        try (InputStream in = ModelSupport.class.getClassLoader().getResourceAsStream("modelClasses")) {
            Map<Class<?>, Map<String, ElementInfo>> modelClassElementInfoMap = new LinkedHashMap<>();
            List<String> lines = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
            for (String className : lines) {
                Class<?> modelClass = Class.forName(className);
                Map<String, ElementInfo> elementInfoMap = new LinkedHashMap<>();
                for (Field field : getAllFields(modelClass)) {
                    String elementName = getElementName(field);
                    Class<?> type = getFieldType(field);
                    boolean required = isRequired(field);
                    boolean repeating = isRepeating(field);
                    boolean choice = isChoice(field);
                    Set<Class<?>> choiceTypes = choice ? Collections.unmodifiableSet(getChoiceTypes(field)) : Collections.emptySet();
                    ElementInfo elementInfo = new ElementInfo(type, required, repeating, choice, choiceTypes);
                    elementInfoMap.put(elementName, elementInfo);
                }
                modelClassElementInfoMap.put(modelClass, Collections.unmodifiableMap(elementInfoMap));
            }
            return modelClassElementInfoMap;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static Map<String, Class<?>> buildResourceTypeMap() {
        Map<String, Class<?>> resourceTypeMap = new LinkedHashMap<>();
        for (Class<?> modelClass : getModelClasses()) {
            if (isResourceType(modelClass)) {
                resourceTypeMap.put(modelClass.getSimpleName(), modelClass);
            }
        }
        return Collections.unmodifiableMap(resourceTypeMap);
    }
    
    private static List<Field> getAllFields(Class<?> modelClass) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz : getClosure(modelClass)) {
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isVolatile(modifiers)) {
                    continue;
                }
                fields.add(field);
            }
        }
        return fields;
    }

    public static String getChoiceElementName(String name, Class<?> type) {
        return name + getConcreteType(type).getSimpleName();
    }
    
    public static Set<Class<?>> getChoiceElementTypes(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getChoiceTypes();
        }
        return Collections.emptySet();
    }
    
    private static Set<Class<?>> getChoiceTypes(Field field) {
        return new LinkedHashSet<>(Arrays.asList(field.getAnnotation(Choice.class).value()));
    }

    public static List<Class<?>> getClosure(Class<?> modelClass) {
        List<Class<?>> closure = new ArrayList<>();
        while (!Object.class.equals(modelClass)) {
            closure.add(modelClass);
            modelClass = modelClass.getSuperclass();
        }
        Collections.reverse(closure);
        return closure;
    }
    
    public static Class<?> getConcreteType(Class<?> type) {
        if (isProfiledType(type)) {
            return CONCRETE_TYPE_MAP.get(type);
        }
        return type;
    }

    private static ElementInfo getElementInfo(Class<?> modelClass, String elementName) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).get(elementName);
    }
    
    public static String getElementName(Field field) {
        return getElementName(field.getName());
    }
    
    public static String getElementName(String fieldName) {
        if ("clazz".equals(fieldName)) {
            return "class";
        }
        if (fieldName.startsWith("_")) {
            return fieldName.substring(1);
        }
        return fieldName;
    }
    
    public static Set<String> getElementNames(Class<?> modelClass) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).keySet();
    }
    
    public static Class<?> getElementType(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getType();
        }
        return null;
    }
    
    public static String getFieldName(String elementName) {
        if ("class".equals(elementName)) {
            return "clazz";
        }
        if (SourceVersion.isKeyword(elementName)) {
            return "_" + elementName;
        }
        return elementName;
    }
    
    private static Class<?> getFieldType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        return field.getType();
    }
    
    public static Set<Class<?>> getModelClasses() {
        return MODEL_CLASS_ELEMENT_INFO_MAP.keySet();
    }
    
    public static Class<?> getResourceType(String name) {
        return RESOURCE_TYPE_MAP.get(name);
    }
    
    public static Collection<Class<?>> getResourceTypes() {
        return RESOURCE_TYPE_MAP.values();
    }
    
    public static String getTypeName(Class<?> type) {
        String typeName = type.getSimpleName();
        if (Code.class.isAssignableFrom(type)) {
            typeName = "code";
        } else if (isPrimitiveType(type)) {
            typeName = typeName.substring(0, 1).toLowerCase() + typeName.substring(1);
        }
        return typeName;
    }
    
    public static boolean isBackboneElementType(Class<?> modelClass) {
        return BackboneElement.class.isAssignableFrom(modelClass);
    }
    
    private static boolean isChoice(Field field) {
        return field.isAnnotationPresent(Choice.class);
    }
    
    public static boolean isChoiceElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isChoice();
        }
        return false;
    }
    
    public static boolean isChoiceElementType(Class<?> type) {
        return CHOICE_ELEMENT_TYPES.contains(type);
    }

    public static boolean isElement(Object modelObject) {
        return (modelObject instanceof Element);
    }
    
    public static boolean isElementType(Class<?> modelClass) {
        return Element.class.isAssignableFrom(modelClass);
    }
    
    public static boolean isModelClass(Class<?> type) {
        return isResourceType(type) || isElementType(type);
    }

    public static boolean isPrimitiveType(Class<?> type) {
        return Base64Binary.class.equals(type) ||
            com.ibm.watsonhealth.fhir.model.type.Boolean.class.equals(type) ||
            com.ibm.watsonhealth.fhir.model.type.String.class.isAssignableFrom(type) || 
            Uri.class.isAssignableFrom(type) ||
            DateTime.class.equals(type) || 
            Date.class.equals(type) ||
            Time.class.equals(type) || 
            Instant.class.equals(type) || 
            com.ibm.watsonhealth.fhir.model.type.Integer.class.isAssignableFrom(type) || 
            Decimal.class.equals(type) || 
            Xhtml.class.equals(type);
    }
    
    public static boolean isProfiledType(Class<?> type) {
        return CONCRETE_TYPE_MAP.containsKey(type);
    }
    
    private static boolean isRepeating(Field field) {
        return List.class.equals(field.getType());
    }
    
    public static boolean isRepeatingElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isRepeating();
        }
        return false;
    }
    
    private static boolean isRequired(Field field) {
        return field.isAnnotationPresent(Required.class);
    }
    
    public static boolean isRequiredElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isRequired();
        }
        return false;
    }
    
    public static boolean isResource(Object modelObject) {
        return (modelObject instanceof Resource);
    }
    
    public static boolean isResourceType(Class<?> modelClass) {
        return Resource.class.isAssignableFrom(modelClass);
    }
    
    public static boolean isResourceType(String name) {
        return RESOURCE_TYPE_MAP.containsKey(name);
    }
    
    public static void main(String[] args) throws Exception {
        ModelSupport.init();
    }
}
