/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ValidationSupport {    
    private static final int MIN_LENGTH = 1;
    private static final int MAX_STRING_LENGTH = 1048576; // 1024 * 1024 = 1MB

    private ValidationSupport() { }
    
    public static <T> T requireNonNull(T element, String elementName) {
        if (element == null) {
            throw new IllegalStateException(String.format("Missing required element: '%s'", elementName));
        }
        return element;
    }
        
    public static <T> List<T> requireNonEmpty(List<T> elements, String elementName) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalStateException(String.format("Missing required element: '%s'", elementName));
        }
        return elements;
    }
    
    public static <T> T choiceElement(T element, String elementName, Class<?>... types) {
        if (element != null) {
            List<Class<?>> typeList = Arrays.asList(types);
            Class<?> elementType = element.getClass();
            if (!typeList.contains(elementType)) {
                List<String> typeNameList = typeList.stream().map(Class::getSimpleName).collect(Collectors.toList());
                throw new IllegalStateException(String.format("Invalid type: %s for choice element: '%s' must be one of: %s", elementType.getSimpleName(), elementName, typeNameList.toString()));
            }
        }
        return element;
    }
    
    public static <T> T requireChoiceElement(T element, String elementName, Class<?>... types) {
        requireNonNull(element, elementName);
        return choiceElement(element, elementName, types);
    }
    
    public static void checkMaxLength(String value) {
        if (value != null) {
            if (value.length() > MAX_STRING_LENGTH) {
                throw new IllegalStateException(String.format("String value length: %d is greater than maximum allowed length: %d", value.length(), MAX_STRING_LENGTH));
            }
        }
    }
    
    public static void checkMinLength(String value) {
        if (value != null) {
            if (value.trim().length() < MIN_LENGTH) {
                throw new IllegalStateException(String.format("String value length: %d is less than minimum required length: %d", value.trim().length(), MIN_LENGTH));
            }
        }
    }
    
    public static void checkValue(String value, Pattern pattern) {
        if (value != null) {
            if (!pattern.matcher(value).matches()) {
                throw new IllegalStateException(String.format("String value is not valid with respect to pattern: %s", pattern.pattern()));
            }
        }
    }
    
    public static void checkValue(Integer value, int minValue) {
        if (value != null) {
            if (value < minValue) {
                throw new IllegalStateException(String.format("Integer value: %d is less than minimum required value: %d", value, minValue));
            }
        }
    }
    
    public static <T> T checkValueType(T value, Class<?>... types) {
        if (value != null) {
            List<Class<?>> typeList = Arrays.asList(types);
            Class<?> valueType = value.getClass();
            if (!typeList.contains(valueType)) {
                List<String> typeNameList = typeList.stream().map(Class::getSimpleName).collect(Collectors.toList());
                throw new IllegalStateException(String.format("Invalid value type: '%s' must be one of: %s", valueType.getSimpleName(), typeNameList.toString()));
            }
        }
        return value;
    }
}
