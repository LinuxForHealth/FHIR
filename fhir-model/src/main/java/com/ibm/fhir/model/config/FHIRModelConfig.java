/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.config;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.fhir.model.format.Format;

/**
 * This class is used to manage runtime configuration for the FHIR model APIs.
 */
public final class FHIRModelConfig {
    /**
     * The format (JSON or XML) to use with the toString method
     */
    public static final String PROPERTY_TO_STRING_FORMAT = "com.ibm.fhir.model.toStringFormat";

    /**
     * The number of spaces to use when indenting (pretty printing must be enabled)
     */
    public static final String PROPERTY_TO_STRING_INDENT_AMOUNT = "com.ibm.fhir.model.toStringIndentAmount";

    /**
     * Used to determine whether the toString method return value should be formatted
     */
    public static final String PROPERTY_TO_STRING_PRETTY_PRINTING = "com.ibm.fhir.model.toStringPrettyPrinting";

    /**
     * Used to determine whether reference types are checked during object construction
     */
    public static final String PROPERTY_CHECK_REFERENCE_TYPES = "com.ibm.fhir.model.checkReferenceTypes";

    /**
     * Used to determine:
     * 1. whether CodeableConcepts that don't contain both system and code are checked during object construction
     * 2. whether syntax-based validation of UCUM and language codes is done during object construction
     */
    public static final String PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION = "com.ibm.fhir.model.extendedCodeableConceptValidation";

    /**
     * Used to determine whether control characters are checked
     */
    public static final String PROPERTY_CHECK_CONTROL_CHARS = "com.ibm.fhir.model.checkControlChars";

    private static final Format DEFAULT_TO_STRING_FORMAT = Format.JSON;
    private static final int DEFAULT_TO_STRING_INDENT_AMOUNT = 2;
    private static final boolean DEFAULT_TO_STRING_PRETTY_PRINTING = true;
    private static final boolean DEFAULT_CHECK_REFERENCE_TYPES = true;
    private static final boolean DEFAULT_CHECK_UNICODE_CONTROL_CHARS = true;
    private static final boolean DEFAULT_EXTENDED_CODEABLE_CONCEPT_VALIDATION = true;

    private static final Map<String, Object> properties = new ConcurrentHashMap<>();

    private FHIRModelConfig() { }

    public static void setToStringFormat(Format format) {
        setProperty(PROPERTY_TO_STRING_FORMAT, format);
    }

    public static Format getToStringFormat() {
        return getPropertyOrDefault(PROPERTY_TO_STRING_FORMAT, DEFAULT_TO_STRING_FORMAT, Format.class);
    }

    public static void setToStringIndentAmount(int indentAmount) {
        setProperty(PROPERTY_TO_STRING_INDENT_AMOUNT, indentAmount);
    }

    public static int getToStringIndentAmount() {
        return getPropertyOrDefault(PROPERTY_TO_STRING_INDENT_AMOUNT, DEFAULT_TO_STRING_INDENT_AMOUNT, Integer.class);
    }

    public static void setToStringPrettyPrinting(boolean prettyPrinting) {
        setProperty(PROPERTY_TO_STRING_PRETTY_PRINTING, prettyPrinting);
    }

    public static boolean getToStringPrettyPrinting() {
        return getPropertyOrDefault(PROPERTY_TO_STRING_PRETTY_PRINTING, DEFAULT_TO_STRING_PRETTY_PRINTING, Boolean.class);
    }

    public static void setCheckReferenceTypes(boolean checkReferenceTypes) {
        setProperty(PROPERTY_CHECK_REFERENCE_TYPES, checkReferenceTypes);
    }

    public static boolean getCheckReferenceTypes() {
        return getPropertyOrDefault(PROPERTY_CHECK_REFERENCE_TYPES, DEFAULT_CHECK_REFERENCE_TYPES, Boolean.class);
    }

    public static void setCheckForControlChars(boolean checkControlChars) {
        setProperty(PROPERTY_CHECK_CONTROL_CHARS, checkControlChars);
    }

    public static boolean shouldCheckForControlChars() {
        return getPropertyOrDefault(PROPERTY_CHECK_CONTROL_CHARS, DEFAULT_CHECK_UNICODE_CONTROL_CHARS, Boolean.class);
    }

    public static void setExtendedCodeableConceptValidation(boolean extendedCodeableConceptValidation) {
        setProperty(PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, extendedCodeableConceptValidation);
    }

    public static boolean getExtendedCodeableConceptValidation() {
        return getPropertyOrDefault(PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION, DEFAULT_EXTENDED_CODEABLE_CONCEPT_VALIDATION, Boolean.class);
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
