/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.watsonhealth.fhir.model.format.Format;

/**
 * This class is used to manage runtime configuration for the FHIR model APIs.
 */
public final class FHIRModel {
    public static final String PROPERTY_TO_STRING_FORMAT = "com.ibm.watsonhealth.fhir.model.toStringFormat";
    public static final String PROPERTY_TO_STRING_INDENT_AMOUNT = "com.ibm.watsonhealth.fhir.model.toStringIndentAmount";
    public static final String PROPERTY_TO_STRING_PRETTY_PRINTING = "com.ibm.watsonhealth.fhir.model.toStringPrettyPrinting";

    private static final Map<String, Object> properties = new ConcurrentHashMap<>();
   
    private FHIRModel() { }
   
    public static void setToStringFormat(Format format) {
        setProperty(PROPERTY_TO_STRING_FORMAT, format);
    }
   
    public static Format getToStringFormatOrDefault(Format defaultFormat) {
        return getPropertyOrDefault(PROPERTY_TO_STRING_FORMAT, defaultFormat, Format.class);
    }
    
    public static void setToStringIndentAmount(int indentAmount) {
        setProperty(PROPERTY_TO_STRING_INDENT_AMOUNT, indentAmount);
    }
   
    public static int getToStringIndentAmountOrDefault(int defaultIndentAmount) {
        return getPropertyOrDefault(PROPERTY_TO_STRING_INDENT_AMOUNT, defaultIndentAmount, Integer.class);
    }
    
    public static void setToStringPrettyPrinting(boolean prettyPrinting) {
        setProperty(PROPERTY_TO_STRING_PRETTY_PRINTING, prettyPrinting);
    }
   
    public static boolean getToStringPrettyPrintingOrDefault(boolean defaultPrettyPrinting) {
        return getPropertyOrDefault(PROPERTY_TO_STRING_PRETTY_PRINTING, defaultPrettyPrinting, Boolean.class);
    }
   
    public static void setProperty(String name, Object value) {
        properties.put(requireNonNull(name), requireNonNull(value));
    }
   
    public static Object removeProperty(String name) {
        return properties.remove(requireNonNull(name));
    }
   
    public static <T> T removeProperty(String name, Class<T> type) {
        return requireNonNull(type).cast(removeProperty(name));
    }
   
    public static Object getProperty(String name) {
        return properties.get(requireNonNull(name));
    }
   
    public static Object getPropertyOrDefault(String name, Object defaultValue) {
        return properties.getOrDefault(requireNonNull(name), requireNonNull(defaultValue));
    }
   
    public static <T> T getProperty(String name, Class<T> type) {
        return requireNonNull(type).cast(getProperty(name));
    }
   
    public static <T> T getPropertyOrDefault(String name, T defaultValue, Class<T> type) {
        return requireNonNull(type).cast(getPropertyOrDefault(name, defaultValue));
    }
   
    public static Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }
   
    public static Set<String> getPropertyNames() {
        return Collections.unmodifiableSet(properties.keySet());
    }
}
