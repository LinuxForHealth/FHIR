/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/guide-parameter-code")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class GuideParameterCode extends Code {
    /**
     * Apply Metadata Value
     * 
     * <p>If the value of this string 0..* parameter is one of the metadata fields then all conformance resources will have 
     * any specified [Resource].[field] overwritten with the ImplementationGuide.[field], where field is one of: version, 
     * date, status, publisher, contact, copyright, experimental, jurisdiction, useContext.
     */
    public static final GuideParameterCode APPLY = GuideParameterCode.builder().value(Value.APPLY).build();

    /**
     * Resource Path
     * 
     * <p>The value of this string 0..* parameter is a subfolder of the build context's location that is to be scanned to 
     * load resources. Scope is (if present) a particular resource type.
     */
    public static final GuideParameterCode PATH_RESOURCE = GuideParameterCode.builder().value(Value.PATH_RESOURCE).build();

    /**
     * Pages Path
     * 
     * <p>The value of this string 0..1 parameter is a subfolder of the build context's location that contains files that are 
     * part of the html content processed by the builder.
     */
    public static final GuideParameterCode PATH_PAGES = GuideParameterCode.builder().value(Value.PATH_PAGES).build();

    /**
     * Terminology Cache Path
     * 
     * <p>The value of this string 0..1 parameter is a subfolder of the build context's location that is used as the 
     * terminology cache. If this is not present, the terminology cache is on the local system, not under version control.
     */
    public static final GuideParameterCode PATH_TX_CACHE = GuideParameterCode.builder().value(Value.PATH_TX_CACHE).build();

    /**
     * Expansion Profile
     * 
     * <p>The value of this string 0..* parameter is a parameter (name=value) when expanding value sets for this 
     * implementation guide. This is particularly used to specify the versions of published terminologies such as SNOMED CT.
     */
    public static final GuideParameterCode EXPANSION_PARAMETER = GuideParameterCode.builder().value(Value.EXPANSION_PARAMETER).build();

    /**
     * Broken Links Rule
     * 
     * <p>The value of this string 0..1 parameter is either "warning" or "error" (default = "error"). If the value is 
     * "warning" then IG build tools allow the IG to be considered successfully build even when there is no internal broken 
     * links.
     */
    public static final GuideParameterCode RULE_BROKEN_LINKS = GuideParameterCode.builder().value(Value.RULE_BROKEN_LINKS).build();

    /**
     * Generate XML
     * 
     * <p>The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in XML format. If not 
     * present, the Publication Tool decides whether to generate XML.
     */
    public static final GuideParameterCode GENERATE_XML = GuideParameterCode.builder().value(Value.GENERATE_XML).build();

    /**
     * Generate JSON
     * 
     * <p>The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in JSON format. If not 
     * present, the Publication Tool decides whether to generate JSON.
     */
    public static final GuideParameterCode GENERATE_JSON = GuideParameterCode.builder().value(Value.GENERATE_JSON).build();

    /**
     * Generate Turtle
     * 
     * <p>The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in Turtle format. If 
     * not present, the Publication Tool decides whether to generate Turtle.
     */
    public static final GuideParameterCode GENERATE_TURTLE = GuideParameterCode.builder().value(Value.GENERATE_TURTLE).build();

    /**
     * HTML Template
     * 
     * <p>The value of this string singleton parameter is the name of the file to use as the builder template for each 
     * generated page (see templating).
     */
    public static final GuideParameterCode HTML_TEMPLATE = GuideParameterCode.builder().value(Value.HTML_TEMPLATE).build();

    private volatile int hashCode;

    private GuideParameterCode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this GuideParameterCode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating GuideParameterCode objects from a passed enum value.
     */
    public static GuideParameterCode of(Value value) {
        switch (value) {
        case APPLY:
            return APPLY;
        case PATH_RESOURCE:
            return PATH_RESOURCE;
        case PATH_PAGES:
            return PATH_PAGES;
        case PATH_TX_CACHE:
            return PATH_TX_CACHE;
        case EXPANSION_PARAMETER:
            return EXPANSION_PARAMETER;
        case RULE_BROKEN_LINKS:
            return RULE_BROKEN_LINKS;
        case GENERATE_XML:
            return GENERATE_XML;
        case GENERATE_JSON:
            return GENERATE_JSON;
        case GENERATE_TURTLE:
            return GENERATE_TURTLE;
        case HTML_TEMPLATE:
            return HTML_TEMPLATE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating GuideParameterCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static GuideParameterCode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating GuideParameterCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating GuideParameterCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GuideParameterCode other = (GuideParameterCode) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for GuideParameterCode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public GuideParameterCode build() {
            GuideParameterCode guideParameterCode = new GuideParameterCode(this);
            if (validating) {
                validate(guideParameterCode);
            }
            return guideParameterCode;
        }

        protected void validate(GuideParameterCode guideParameterCode) {
            super.validate(guideParameterCode);
        }

        protected Builder from(GuideParameterCode guideParameterCode) {
            super.from(guideParameterCode);
            return this;
        }
    }

    public enum Value {
        /**
         * Apply Metadata Value
         * 
         * <p>If the value of this string 0..* parameter is one of the metadata fields then all conformance resources will have 
         * any specified [Resource].[field] overwritten with the ImplementationGuide.[field], where field is one of: version, 
         * date, status, publisher, contact, copyright, experimental, jurisdiction, useContext.
         */
        APPLY("apply"),

        /**
         * Resource Path
         * 
         * <p>The value of this string 0..* parameter is a subfolder of the build context's location that is to be scanned to 
         * load resources. Scope is (if present) a particular resource type.
         */
        PATH_RESOURCE("path-resource"),

        /**
         * Pages Path
         * 
         * <p>The value of this string 0..1 parameter is a subfolder of the build context's location that contains files that are 
         * part of the html content processed by the builder.
         */
        PATH_PAGES("path-pages"),

        /**
         * Terminology Cache Path
         * 
         * <p>The value of this string 0..1 parameter is a subfolder of the build context's location that is used as the 
         * terminology cache. If this is not present, the terminology cache is on the local system, not under version control.
         */
        PATH_TX_CACHE("path-tx-cache"),

        /**
         * Expansion Profile
         * 
         * <p>The value of this string 0..* parameter is a parameter (name=value) when expanding value sets for this 
         * implementation guide. This is particularly used to specify the versions of published terminologies such as SNOMED CT.
         */
        EXPANSION_PARAMETER("expansion-parameter"),

        /**
         * Broken Links Rule
         * 
         * <p>The value of this string 0..1 parameter is either "warning" or "error" (default = "error"). If the value is 
         * "warning" then IG build tools allow the IG to be considered successfully build even when there is no internal broken 
         * links.
         */
        RULE_BROKEN_LINKS("rule-broken-links"),

        /**
         * Generate XML
         * 
         * <p>The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in XML format. If not 
         * present, the Publication Tool decides whether to generate XML.
         */
        GENERATE_XML("generate-xml"),

        /**
         * Generate JSON
         * 
         * <p>The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in JSON format. If not 
         * present, the Publication Tool decides whether to generate JSON.
         */
        GENERATE_JSON("generate-json"),

        /**
         * Generate Turtle
         * 
         * <p>The value of this boolean 0..1 parameter specifies whether the IG publisher creates examples in Turtle format. If 
         * not present, the Publication Tool decides whether to generate Turtle.
         */
        GENERATE_TURTLE("generate-turtle"),

        /**
         * HTML Template
         * 
         * <p>The value of this string singleton parameter is the name of the file to use as the builder template for each 
         * generated page (see templating).
         */
        HTML_TEMPLATE("html-template");

        private final java.lang.String value;

        Value(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating GuideParameterCode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding GuideParameterCode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "apply":
                return APPLY;
            case "path-resource":
                return PATH_RESOURCE;
            case "path-pages":
                return PATH_PAGES;
            case "path-tx-cache":
                return PATH_TX_CACHE;
            case "expansion-parameter":
                return EXPANSION_PARAMETER;
            case "rule-broken-links":
                return RULE_BROKEN_LINKS;
            case "generate-xml":
                return GENERATE_XML;
            case "generate-json":
                return GENERATE_JSON;
            case "generate-turtle":
                return GENERATE_TURTLE;
            case "html-template":
                return HTML_TEMPLATE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
