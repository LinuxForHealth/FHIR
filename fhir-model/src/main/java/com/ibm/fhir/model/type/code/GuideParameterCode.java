/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class GuideParameterCode extends Code {
    /**
     * Apply Metadata Value
     */
    public static final GuideParameterCode APPLY = GuideParameterCode.builder().value(ValueSet.APPLY).build();

    /**
     * Resource Path
     */
    public static final GuideParameterCode PATH_RESOURCE = GuideParameterCode.builder().value(ValueSet.PATH_RESOURCE).build();

    /**
     * Pages Path
     */
    public static final GuideParameterCode PATH_PAGES = GuideParameterCode.builder().value(ValueSet.PATH_PAGES).build();

    /**
     * Terminology Cache Path
     */
    public static final GuideParameterCode PATH_TX_CACHE = GuideParameterCode.builder().value(ValueSet.PATH_TX_CACHE).build();

    /**
     * Expansion Profile
     */
    public static final GuideParameterCode EXPANSION_PARAMETER = GuideParameterCode.builder().value(ValueSet.EXPANSION_PARAMETER).build();

    /**
     * Broken Links Rule
     */
    public static final GuideParameterCode RULE_BROKEN_LINKS = GuideParameterCode.builder().value(ValueSet.RULE_BROKEN_LINKS).build();

    /**
     * Generate XML
     */
    public static final GuideParameterCode GENERATE_XML = GuideParameterCode.builder().value(ValueSet.GENERATE_XML).build();

    /**
     * Generate JSON
     */
    public static final GuideParameterCode GENERATE_JSON = GuideParameterCode.builder().value(ValueSet.GENERATE_JSON).build();

    /**
     * Generate Turtle
     */
    public static final GuideParameterCode GENERATE_TURTLE = GuideParameterCode.builder().value(ValueSet.GENERATE_TURTLE).build();

    /**
     * HTML Template
     */
    public static final GuideParameterCode HTML_TEMPLATE = GuideParameterCode.builder().value(ValueSet.HTML_TEMPLATE).build();

    private volatile int hashCode;

    private GuideParameterCode(Builder builder) {
        super(builder);
    }

    public static GuideParameterCode of(ValueSet value) {
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

    public static GuideParameterCode of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        Builder builder = new Builder();
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
        return builder;
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public GuideParameterCode build() {
            return new GuideParameterCode(this);
        }
    }

    public enum ValueSet {
        /**
         * Apply Metadata Value
         */
        APPLY("apply"),

        /**
         * Resource Path
         */
        PATH_RESOURCE("path-resource"),

        /**
         * Pages Path
         */
        PATH_PAGES("path-pages"),

        /**
         * Terminology Cache Path
         */
        PATH_TX_CACHE("path-tx-cache"),

        /**
         * Expansion Profile
         */
        EXPANSION_PARAMETER("expansion-parameter"),

        /**
         * Broken Links Rule
         */
        RULE_BROKEN_LINKS("rule-broken-links"),

        /**
         * Generate XML
         */
        GENERATE_XML("generate-xml"),

        /**
         * Generate JSON
         */
        GENERATE_JSON("generate-json"),

        /**
         * Generate Turtle
         */
        GENERATE_TURTLE("generate-turtle"),

        /**
         * HTML Template
         */
        HTML_TEMPLATE("html-template");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
