/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util;

import static org.linuxforhealth.fhir.model.util.FHIRUtil.REFERENCE_PATTERN;
import static org.linuxforhealth.fhir.model.util.ModelSupport.FHIR_STRING;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.linuxforhealth.fhir.model.config.FHIRModelConfig;
import org.linuxforhealth.fhir.model.lang.util.LanguageRegistryUtil;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.ucum.util.UCUMUtil;

/**
 * Static helper methods for validating model objects during construction
 *
 * @apiNote In methods where an exception is thrown, IllegalStateException is chosen over IllegalArgumentException
 *          so that Builder.build() methods can throw the most appropriate exception without catching and wrapping
 */
public final class ValidationSupport {
    public static final String ALL_LANG_VALUE_SET_URL = "http://hl7.org/fhir/ValueSet/all-languages";
    public static final String UCUM_UNITS_VALUE_SET_URL = "http://hl7.org/fhir/ValueSet/ucum-units";
    public static final String BCP_47_URN = "urn:ietf:bcp:47";
    public static final String UCUM_CODE_SYSTEM_URL = "http://unitsofmeasure.org";
    public static final String DATA_ABSENT_REASON_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/data-absent-reason";
    private static final int RESOURCE_TYPE_GROUP = 4;
    private static final int MIN_STRING_LENGTH = 1;
    private static final int MAX_STRING_LENGTH = 1048576; // 1024 * 1024 = 1MB
    private static final String FHIR_XHTML_XSD = "fhir-xhtml.xsd";
    private static final String FHIR_XML_XSD = "xml.xsd";
    private static final String FHIR_XMLDSIG_CORE_SCHEMA_XSD = "xmldsig-core-schema.xsd";
    private static final SchemaFactory SCHEMA_FACTORY = createSchemaFactory();
    private static final Schema SCHEMA = createSchema();
    private static final ThreadLocal<Validator> THREAD_LOCAL_VALIDATOR = new ThreadLocal<Validator>() {
        @Override
        public Validator initialValue() {
            return SCHEMA.newValidator();
        }
    };
    private static final Set<Character> WHITESPACE = new HashSet<>(Arrays.asList(' ', '\t', '\r', '\n'));
    private static final Set<Character> UNSUPPORTED_CONTROL_CHARS = buildUnsupportedControlCharacterSet();

    private static final char [] BASE64_CHARS = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };
    private static final Map<Character, Integer> BASE64_INDEX_MAP = buildBase64IndexMap();

    private ValidationSupport() { }

    /**
     * Builds a set of unsupported control characters per the specification.
     * @return
     */
    private static Set<Character> buildUnsupportedControlCharacterSet() {
        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < 32; i++) {
            if (i != 9 && i != 10 && i != 13) {
                chars.add(Character.valueOf((char) i));
            }
        }
        return chars;
    }

    private static Map<Character, Integer> buildBase64IndexMap() {
        Map<Character, Integer> base64IndexMap = new LinkedHashMap<>();
        for (int i = 0; i < BASE64_CHARS.length; i++) {
            base64IndexMap.put(BASE64_CHARS[i], i);
        }
        return base64IndexMap;
    }

    /**
     * A sequence of Unicode characters
     * <pre>
     * pattern:  [ \r\n\t\S]+
     * </pre>
     *
     * @throws IllegalStateException if the passed String is not a valid FHIR String value
     */
    public static void checkString(String s) {
        if (s == null) {
            return;
        }
        if (s.length() > MAX_STRING_LENGTH) {
            throw new IllegalStateException(String.format("String value length: %d is greater than maximum allowed length: %d", s.length(), MAX_STRING_LENGTH));
        }
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!Character.isWhitespace(ch)) {
                checkUnsupportedControlCharacters(s, ch);
                count++;
            } else if (!WHITESPACE.contains(ch)) {
                throw new IllegalStateException(buildIllegalWhiteSpaceException(s));
            }
        }
        if (count < MIN_STRING_LENGTH) {
            throw new IllegalStateException(String.format("Trimmed String value length: %d is less than minimum required length: %d", count, MIN_STRING_LENGTH));
        }
    }

    /**
     * wraps the building of the illegal white space exception.
     * @param s
     * @return
     */
    private static String buildIllegalWhiteSpaceException(String s) {
        return new StringBuilder("String value: '")
            .append(s)
            .append("' is not valid with respect to pattern: [\\\\r\\\\n\\\\t\\\\S]+")
            .toString();
    }

    /**
     * Helper method to check if there is unsupported unicode.
     * @param s the source of the character
     * @param ch the character to check
     * @throws IllegalStateException indicating an invalid unicode character was detected.
     *
     * @implNote Per the specification: Strings SHOULD not contain Unicode character points below 32
     * except for u0009 (horizontal tab), u0010 (carriage return) and u0013 (line feed).
     */
    private static void checkUnsupportedControlCharacters(String s, char ch) {
        if (FHIRModelConfig.shouldCheckForControlChars() && UNSUPPORTED_CONTROL_CHARS.contains(ch)) {
            throw new IllegalStateException(buildUnsupportedControlCharsException(s));
        }
    }

    /**
     * wraps the unsupported control character exception string
     * @param s
     * @return
     */
    private static String buildUnsupportedControlCharsException(String s) {
        return new StringBuilder("String value contains unsupported control characters: decimal range=[\\0000-0008,0011,0012,0014-0031] value=[")
            .append(s)
            .append(']')
            .toString();
    }

    /**
     * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other
     * than single spaces in the contents.
     * <pre>
     * pattern:  [^\s]+(\s[^\s]+)*
     * </pre>
     *
     * @throws IllegalStateException if the passed String is not a valid FHIR Code value
     */
    public static void checkCode(String s) {
        if (s == null) {
            return;
        }
        if (s.length() == 0 || Character.isWhitespace(s.charAt(0))) {
            throw new IllegalStateException(String.format("Code value: '%s' must begin with a non-whitespace character", s));
        }
        if (Character.isWhitespace(s.charAt(s.length() - 1))) {
            throw new IllegalStateException(String.format("Code value: '%s' must end with a non-whitespace character", s));
        }
        boolean previousIsSpace = false;
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (Character.isWhitespace(current)) {
                if (current != ' ') {
                    throw new IllegalStateException(String.format("Code value: '%s' must not contain whitespace other than a single space", s));
                } else if (previousIsSpace) {
                    throw new IllegalStateException(String.format("Code value: '%s' must not contain consecutive spaces", s));
                }
                previousIsSpace = true;
            } else {
                checkUnsupportedControlCharacters(s, current);
                if (previousIsSpace) {
                    previousIsSpace = false;
                }
            }
        }
    }

    /**
     * Any combination of letters, numerals, "-" and ".", with a length limit of 64 characters. (This might be an integer, an
     * unprefixed OID, UUID or any other identifier pattern that meets these constraints.) Ids are case-insensitive.
     * <pre>
     * pattern:  [A-Za-z0-9\-\.]{1,64}
     * </pre>
     *
     * @throws IllegalStateException if the passed String is not a valid FHIR Id value
     */
    public static void checkId(String s) {
        if (s == null) {
            return;
        }
        if (s.isEmpty()) {
            throw new IllegalStateException(String.format("Id value must not be empty"));
        }
        if (s.length() > 64) {
            throw new IllegalStateException(String.format("Id value length: %d is greater than maximum allowed length: %d", s.length(), 64));
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // @implNote By implication, this excludes invalid unicode.
            //45 = '-'
            //46 = '.'
            //48 = '0'
            //57 = '9'
            //65 = 'A'
            //90 = 'Z'
            //97 = 'a'
            //122 = 'z'
            if (c < 45 || c == 47 || (c > 57 && c < 65) || (c > 90 && c < 97) || c > 122 ) {
                throw new IllegalStateException(String.format("Id value: '%s' contain invalid character '%s'", s, c));
            }
        }
    }

    /**
     * String of characters used to identify a name or a resource
     * <pre>
     * pattern:  \S*
     * </pre>
     *
     * @throws IllegalStateException if the passed String is not a valid FHIR uri value
     */
    public static void checkUri(String s) {
        if (s == null) {
            return;
        }
        if (s.length() > MAX_STRING_LENGTH) {
            throw new IllegalStateException(String.format("Uri value length: %d is greater than maximum allowed length: %d", s.length(), MAX_STRING_LENGTH));
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            checkUnsupportedControlCharacters(s, ch);
            if (Character.isWhitespace(ch)) {
                throw new IllegalStateException(String.format("Uri value: '%s' must not contain whitespace", s));
            }
        }
    }

    /**
     * @throws IllegalStateException if the passed String is longer than the maximum string length
     */
    public static void checkMaxLength(String value) {
        if (value != null) {
            if (value.length() > MAX_STRING_LENGTH) {
                throw new IllegalStateException(String.format("String value length: %d is greater than maximum allowed length: %d", value.length(), MAX_STRING_LENGTH));
            }
        }
    }

    /**
     * @throws IllegalStateException if the passed String is shorter than the minimum string length
     */
    public static void checkMinLength(String value) {
        if (value != null) {
            if (value.trim().length() < MIN_STRING_LENGTH) {
                throw new IllegalStateException(String.format("Trimmed String value length: %d is less than minimum required length: %d", value.trim().length(), MIN_STRING_LENGTH));
            }
        }
    }

    /**
     * @throws IllegalStateException if the passed Integer value is less than the passed minValue
     */
    public static void checkValue(Integer value, int minValue) {
        if (value != null) {
            if (value < minValue) {
                throw new IllegalStateException(String.format("Integer value: %d is less than minimum required value: %d", value, minValue));
            }
        }
    }

    /**
     * @throws IllegalStateException if the passed String value does not match the passed pattern
     */
    public static void checkValue(String value, Pattern pattern) {
        if (value != null) {
            if (!pattern.matcher(value).matches()) {
                throw new IllegalStateException(String.format("String value: '%s' is not valid with respect to pattern: %s", value, pattern.pattern()));
            }
        }
    }

    /**
     * @throws IllegalStateException if the type of the passed value is not one of the passed types
     */
    public static <T> T checkValueType(T value, Class<?>... types) {
        if (value != null) {
            List<Class<?>> typeList = Arrays.asList(types);
            Class<?> valueType = value.getClass();
            if (!typeList.contains(valueType)) {
                List<String> typeNameList = typeList.stream().map(Class::getSimpleName).collect(Collectors.toList());
                throw new IllegalStateException(String.format("Invalid value type: %s must be one of: %s", valueType.getSimpleName(), typeNameList.toString()));
            }
        }
        return value;
    }

    /**
     * @throws IllegalStateException if the type of the passed element is not one of the passed types
     * @apiNote Only differs from {@link #checkValueType} in that we can provide a better error message
     */
    public static <T extends Element> T choiceElement(T element, String elementName, Class<?>... types) {
        if (element != null) {
            Class<?> elementType = element.getClass();
            boolean noneMatch = true;
            for (Class<?> type : types) {
                if (type.isAssignableFrom(elementType)) {
                    noneMatch = false;
                    break;
                }
            }
            if (noneMatch) {
                List<String> typeNameList = Arrays.stream(types).map(Class::getSimpleName).collect(Collectors.toList());
                throw new IllegalStateException(String.format("Invalid type: %s for choice element: '%s' must be one of: %s", elementType.getSimpleName(), elementName, typeNameList.toString()));
            }
        }
        return element;
    }

    /**
     * @throws IllegalStateException if the passed String value is not valid XHTML
     */
    public static void checkXHTMLContent(String value) {
        try {
            Validator validator = THREAD_LOCAL_VALIDATOR.get();
            validator.reset();
            validator.validate(new StreamSource(new StringReader(value)));
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Invalid XHTML content: %s", e.getMessage()), e);
        }
    }

    private static Schema createSchema() {
        try {
            StreamSource[] sources = new StreamSource[3];
            sources[0] = new StreamSource(ValidationSupport.class.getClassLoader().getResourceAsStream(FHIR_XML_XSD));
            sources[1] = new StreamSource(ValidationSupport.class.getClassLoader().getResourceAsStream(FHIR_XMLDSIG_CORE_SCHEMA_XSD));
            sources[2] = new StreamSource(ValidationSupport.class.getClassLoader().getResourceAsStream(FHIR_XHTML_XSD));
            return SCHEMA_FACTORY.newSchema(sources);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static SchemaFactory createSchemaFactory() {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return schemaFactory;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * @throws IllegalStateException if the passed element is null or if its type is not one of the passed types
     */
    public static <T extends Element> T requireChoiceElement(T element, String elementName, Class<?>... types) {
        requireNonNull(element, elementName);
        return choiceElement(element, elementName, types);
    }

    /**
     * @throws IllegalStateException if the passed element is null
     */
    public static <T> T requireNonNull(T element, String elementName) {
        if (element == null) {
            throw new IllegalStateException(String.format("Missing required element: '%s'", elementName));
        }
        return element;
    }

    /**
     * @return the same list that was passed
     * @throws IllegalStateException if the passed list is empty or contains any null objects or objects of an incompatible type
     */
    public static <T> List<T> checkNonEmptyList(List<T> elements, String elementName, Class<T> type) {
        if (elements.isEmpty()) {
            throw new IllegalStateException(String.format("Missing required element: '%s'", elementName));
        }
        return checkList(elements, elementName, type);
    }

    /**
     * @return the same list that was passed
     * @throws IllegalStateException if the passed list contains any null objects or objects of an incompatible type
     */
    public static <T> List<T> checkList(List<T> elements, String elementName, Class<T> type) {
        for (T element : elements) {
            if (Objects.isNull(element)) {
                throw new IllegalStateException(String.format("Repeating element: '%s' does not permit null elements", elementName));
            }
            if (!type.isInstance(element)) {
                throw new IllegalStateException(String.format("Invalid type: %s for repeating element: '%s' must be: %s",
                        element.getClass().getSimpleName(), elementName, type.getSimpleName()));
            }
        }
        return elements;
    }

    /**
     * @throws IllegalStateException if the passed element has no value and no children
     */
    public static void requireValueOrChildren(Element element) {
        if (!element.hasValue() && !element.hasChildren()) {
            throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
        }
    }

    /**
     * @throws IllegalStateException if the passed element is not null
     */
    public static void prohibited(Element element, String elementName) {
        if (element != null) {
            throw new IllegalStateException(String.format("Element: '%s' is prohibited.", elementName));
        }
    }

    /**
     * @throws IllegalStateException if the passed list is not empty
     */
    public static <T extends Element> void prohibited(List<T> elements, String elementName) {
        if (!elements.isEmpty()) {
            throw new IllegalStateException(String.format("Element: '%s' is prohibited.", elementName));
        }
    }

    /**
     * Check that the specified list of elements contain a code that is a member of the specified value set.
     *
     * @param elements
     *     the list of elements for which to check codes
     * @param elementName
     *     the name of the element
     * @param valueSet
     *     the URL of the value set to check membership against
     * @param system
     *     the value set system
     * @param codes
     *     the value set codes
     * @throws IllegalStateExeption if each element in the list does not include a code from the required value set
     */
    public static void checkValueSetBinding(List<? extends Element> elements, String elementName, String valueSet, String system, String... valueSetCodes) {
        if (elements != null) {
            for (Element element : elements) {
                checkValueSetBinding(element, elementName, valueSet, system, valueSetCodes);
            }
        }
    }

    /**
     * Check that the specified element contains a code that is a member of the specified value set.
     *
     * @param element
     *     the element for which to check codes
     * @param elementName
     *     the name of the element
     * @param valueSet
     *     the URL of the value set to check membership against
     * @param system
     *     the value set system
     * @param codes
     *     the value set codes
     * @throws IllegalStateExeption if the element does not include a code from the required value set
     */
    public static void checkValueSetBinding(Element element, String elementName, String valueSet, String system, String... codes) {
        if (element != null) {
            boolean extendedCodeableConceptValidation = FHIRModelConfig.getExtendedCodeableConceptValidation();
            List<String> codeList = Arrays.asList(codes);

            if (element instanceof CodeableConcept) {
                checkCodeableConcept((CodeableConcept)element, elementName, valueSet, system, codeList, extendedCodeableConceptValidation);
            } else if (element instanceof Coding || element instanceof Quantity) {
                checkCoding(element, elementName, valueSet, system, codeList, extendedCodeableConceptValidation);
            } else if (element instanceof Code || element instanceof Uri || element instanceof org.linuxforhealth.fhir.model.type.String) {
                checkCode(element, elementName, valueSet, codeList, extendedCodeableConceptValidation);
            }
        }
    }

    /**
     * @throws IllegalStateExeption if the CodeableConcept element does not include a code from the required binding
     */
    private static void checkCodeableConcept(CodeableConcept codeableConcept, String elementName, String valueSet, String system, List<String> codes, boolean extendedCodeableConceptValidation) {
        if (extendedCodeableConceptValidation) {
            if (codeableConcept.getCoding() != null) {
                for (Coding coding : codeableConcept.getCoding()) {
                    try {
                        checkCoding(coding, elementName, valueSet, system, codes, extendedCodeableConceptValidation);
                        return;
                    } catch (IllegalStateException e) {}
                }
            }
            throw new IllegalStateException(String.format("Element '%s': does not contain a Coding element with a valid system and code combination for value set: '%s'", elementName, valueSet));
        } else if (!codes.isEmpty() && !codeableConcept.getCoding().isEmpty() && hasCodingWithSystemAndCodeValues(codeableConcept)) {
            for (Coding coding : codeableConcept.getCoding()) {
                if (hasSystemAndCodeValues(coding) &&
                        system.equals(coding.getSystem().getValue()) &&
                        codes.contains(coding.getCode().getValue())) {
                    return;
                }
            }
            throw new IllegalStateException(String.format("Element '%s': does not contain a Coding element with a valid system and code combination for value set: '%s'", elementName, valueSet));
        }
    }

    /**
     * @throws IllegalStateExeption if the Coding element does not include a code from the required binding
     */
    private static void checkCoding(Element element, String elementName, String valueSet, String system, List<String> codes, boolean extendedCodeableConceptValidation) {
        if (extendedCodeableConceptValidation) {
            if (!hasOnlyDataAbsentReasonExtension(element)) {
                if (hasSystemAndCodeValues(element)) {
                    String codingSystem = null;
                    Code codingCode = null;
                    if (element instanceof Coding) {
                        codingSystem = element.as(Coding.class).getSystem().getValue();
                        codingCode = element.as(Coding.class).getCode();
                    } else if (element instanceof Quantity) {
                        codingSystem = element.as(Quantity.class).getSystem().getValue();
                        codingCode = element.as(Quantity.class).getCode();
                    }
                    if (isSyntaxValidatedValueSet(valueSet)) {
                        checkSyntaxValidatedCode(codingCode.getValue(), codingSystem, elementName, valueSet, extendedCodeableConceptValidation);
                    } else {
                        if (!codingSystem.equals(system)) {
                            throw new IllegalStateException(String.format("Element '%s': '%s' is not a valid system for value set '%s'", elementName, codingSystem, valueSet));
                        } else {
                            checkCode(codingCode, elementName, valueSet, codes, extendedCodeableConceptValidation);
                        }
                    }
                } else {
                    throw new IllegalStateException(String.format("Element '%s': does not contain a valid system and code combination for value set: '%s'", elementName, valueSet));
                }
            }
        }
    }

    /**
     * @throws IllegalStateExeption if the code element is not from the required binding
     */
    private static void checkCode(Element element, String elementName, String valueSet, List<String> codes, boolean extendedCodeableConceptValidation) {
        if (extendedCodeableConceptValidation) {
            if (!hasOnlyDataAbsentReasonExtension(element)) {
                String codeValue = null;
                if (element instanceof Code) {
                    codeValue = ((Code)element).getValue();
                } else if (element instanceof Uri) {
                    codeValue = ((Uri)element).getValue();
                } else if (element instanceof org.linuxforhealth.fhir.model.type.String) {
                    codeValue = ((org.linuxforhealth.fhir.model.type.String)element).getValue();
                }
                if (codeValue != null) {
                    if (isSyntaxValidatedValueSet(valueSet)) {
                        checkSyntaxValidatedCode(codeValue, null, elementName, valueSet, extendedCodeableConceptValidation);
                    } else if (!codes.contains(codeValue)) {
                        throw new IllegalStateException(String.format("Element '%s': '%s' is not a valid code for value set '%s'", elementName, codeValue, valueSet));
                    }
                } else {
                    throw new IllegalStateException(String.format("Element '%s': does not contan a valid code for value set '%s'", elementName, valueSet));
                }
            }
        }
    }

    /**
     * @throws IllegalStateExeption if the code element is not from the required binding
     */
    private static void checkSyntaxValidatedCode(String code, String system, String elementName, String valueSet, boolean extendedCodeableConceptValidation) {
        if (extendedCodeableConceptValidation) {
            if (ALL_LANG_VALUE_SET_URL.contentEquals(valueSet)) {
                if (system != null && !BCP_47_URN.equals(system)) {
                    throw new IllegalStateException(String.format("Element '%s': '%s' is not a valid system for value set '%s'", elementName, system, valueSet));
                } else if (!LanguageRegistryUtil.isValidLanguageTag(code)) {
                    throw new IllegalStateException(String.format("Element '%s': '%s' is not a valid code for value set '%s'", elementName, code, valueSet));
                }
            } else if (UCUM_UNITS_VALUE_SET_URL.equals(valueSet)) {
                if (system != null && !UCUM_CODE_SYSTEM_URL.equals(system)) {
                    throw new IllegalStateException(String.format("Element '%s': '%s' is not a valid system for value set '%s'", elementName, system, valueSet));
                } else if (!UCUMUtil.isValidUcum(code)) {
                    throw new IllegalStateException(String.format("Element '%s': '%s' is not a valid code for value set '%s'", elementName, code, valueSet));
                }
            }
        }
    }

    private static boolean hasCodingWithSystemAndCodeValues(CodeableConcept codeableConcept) {
        for (Coding coding : codeableConcept.getCoding()) {
            if (hasSystemAndCodeValues(coding)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSystemAndCodeValues(Element element) {
        if (element instanceof Coding) {
            Coding coding = (Coding) element;
            return coding.getSystem() != null &&
                    coding.getSystem().getValue() != null &&
                    coding.getCode() != null &&
                    coding.getCode().getValue() != null;
        } else if (element instanceof Quantity) {
            Quantity quantity = (Quantity) element;
            return quantity.getSystem() != null &&
                    quantity.getSystem().getValue() != null &&
                    quantity.getCode() != null &&
                    quantity.getCode().getValue() != null;
        }
        return false;
    }

    private static boolean isSyntaxValidatedValueSet(String valueSet) {
        if (ALL_LANG_VALUE_SET_URL.equals(valueSet) || UCUM_UNITS_VALUE_SET_URL.equals(valueSet)) {
            return true;
        }
        return false;
    }

    /**
     * @throws IllegalStateException if the resource type found in one or more reference values does not match the specified Reference.type
     *                               value or is not one of the allowed reference types for that element
     */
    public static void checkReferenceType(List<Reference> reference, String elementName, String... referenceTypes) {
        for (Reference r : reference) {
            checkReferenceType(r, elementName, referenceTypes);
        }
    }

    /**
     * @throws IllegalStateException if the choiceElement is a Reference and the reference value does not match the specified Reference.type
     *                               value or is not one of the allowed reference types for that element
     */
    public static void checkReferenceType(Element choiceElement, String elementName, String... referenceTypes) {
        if (choiceElement instanceof Reference) {
            checkReferenceType((Reference) choiceElement, elementName, referenceTypes);
        }
    }

    /**
     * Checks that the reference contains valid resource type values.
     * @param reference the reference
     * @param elementName the element name
     * @param referenceTypes the valid resource types for the reference
     * @throws IllegalStateException if the resource type found in the reference value does not match the specified Reference.type value
     *                               or is not one of the allowed reference types for that element
     */
    public static void checkReferenceType(Reference reference, String elementName, String... referenceTypes) {
        if (reference == null || !FHIRModelConfig.getCheckReferenceTypes()) {
            return;
        }

        String resourceType = null;
        String referenceReference = getReferenceReference(reference);
        List<String> referenceTypeList = Arrays.asList(referenceTypes);

        if (referenceReference != null && !referenceReference.startsWith("#") && !hasScheme(referenceReference)) {
            int index = referenceReference.indexOf("?");
            if (index != -1) {
                // conditional reference
                resourceType = referenceReference.substring(0, index);
            } else {
                Matcher matcher = REFERENCE_PATTERN.matcher(referenceReference);
                if (matcher.matches()) {
                    resourceType = matcher.group(RESOURCE_TYPE_GROUP);
                }
            }

            // resourceType is required in the reference value
            if (resourceType == null) {
                throw new IllegalStateException(String.format("Invalid reference value or resource type not found in reference value: '%s' for element: '%s'", referenceReference, elementName));
            }

            if (!ModelSupport.isResourceType(resourceType)) {
                throw new IllegalStateException(String.format("Resource type found in reference value: '%s' for element: '%s' must be a valid resource type name", referenceReference, elementName));
            }

            // If there is a resourceType in the reference value, check that it's an allowed value
            if (!referenceTypeList.contains(resourceType)) {
                throw new IllegalStateException(String.format("Resource type found in reference value: '%s' for element: '%s' must be one of: %s", referenceReference, elementName, referenceTypeList.toString()));
            }
        }

        String referenceType = getReferenceType(reference);
        if (referenceType != null) {
            if (!ModelSupport.isResourceType(referenceType)) {
                throw new IllegalStateException(String.format("Resource type found in Reference.type: '%s' for element: '%s' must be a valid resource type name", referenceType, elementName));
            }

            // If there is an explicit Reference.type, ensure it's an allowed type
            if (!referenceTypeList.contains(referenceType)) {
                throw new IllegalStateException(String.format("Resource type found in Reference.type: '%s' for element: '%s' must be one of: %s", referenceType, elementName, referenceTypeList.toString()));
            }

            // If there is an explicit Reference.type, check that the resourceType pattern matches it
            if (resourceType != null && !resourceType.equals(referenceType)) {
                throw new IllegalStateException(String.format("Resource type found in reference value: '%s' for element: '%s' does not match Reference.type: %s", referenceReference, elementName, referenceType));
            }
        }
    }

    /**
     * @param literalRefValue
     * @return true if literalRefValue has a URI scheme (i.e. a prefix followed by ':')
     *     and a non-empty value; otherwise false
     */
    private static boolean hasScheme(String literalRefValue) {
        int indexOf = literalRefValue.indexOf(':');
        return (indexOf > 0) && (literalRefValue.length() > (indexOf + 1));
    }

    private static String getReferenceReference(Reference reference) {
        if (reference.getReference() != null) {
            return reference.getReference().getValue();
        }
        return null;
    }

    private static String getReferenceType(Reference reference) {
        if (reference.getType() != null) {
            return reference.getType().getValue();
        }
        return null;
    }

    public static void validateBase64EncodedString(String value) {
        int length = value.length();
        if ((length % 4) != 0) {
            throw new IllegalArgumentException("Invalid base64 string length: " + value.length());
        }
        if (value.endsWith("==") || value.endsWith("=")) {
            int charIndex = value.endsWith("==") ? (length - 3) : (length - 2);
            char ch = value.charAt(charIndex);
            if (ch == '=') {
                throw new IllegalArgumentException("Unexpected base64 padding character: '=' found at index: " + charIndex);
            }
            int base64Index = BASE64_INDEX_MAP.getOrDefault(ch, -1);
            if (base64Index == -1) {
                throw new IllegalArgumentException("Illegal base64 character: '" + ch + "' found at index: " + charIndex);
            }
            int mask = value.endsWith("==") ? 0b001111 : 0b000011;
            if ((base64Index & mask) != 0) {
                throw new IllegalArgumentException("Invalid base64 string: non-zero padding bits; character: '" + ch + "' found at index: " + charIndex + " should be: '" + BASE64_CHARS[(base64Index & ~mask)] + "'");
            }
        }
    }

    public static boolean hasOnlyDataAbsentReasonExtension(Element element) {
        if (hasDataAbsentReasonExtension(element) &&
                ((element.is(Code.class) && element.as(Code.class).getValue() == null) ||
                 (element.is(FHIR_STRING) && element.as(FHIR_STRING).getValue() == null) ||
                 (element.is(Uri.class) && element.as(Uri.class).getValue() == null) ||
                 (element.is(Coding.class) && element.as(Coding.class).getSystem() == null && element.as(Coding.class).getCode() == null) ||
                 (element.is(Quantity.class) && element.as(Quantity.class).getSystem() == null && element.as(Quantity.class).getCode() == null))) {
            return true;
        } else if (element.is(CodeableConcept.class) && element.as(CodeableConcept.class).getCoding() != null) {
            for (Coding coding : element.as(CodeableConcept.class).getCoding()) {
                if (hasOnlyDataAbsentReasonExtension(coding)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasDataAbsentReasonExtension(Element element) {
        for (Extension extension : element.getExtension()) {
            if (DATA_ABSENT_REASON_EXTENSION_URL.equals(extension.getUrl())) {
                return true;
            }
        }
        return false;
    }

}