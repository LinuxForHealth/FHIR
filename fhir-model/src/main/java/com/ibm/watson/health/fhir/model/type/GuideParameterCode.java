/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class GuideParameterCode extends Code {
    /**
     * Apply Metadata Value
     */
    public static final GuideParameterCode APPLY = GuideParameterCode.of(ValueSet.APPLY);

    /**
     * Resource Path
     */
    public static final GuideParameterCode PATH_RESOURCE = GuideParameterCode.of(ValueSet.PATH_RESOURCE);

    /**
     * Pages Path
     */
    public static final GuideParameterCode PATH_PAGES = GuideParameterCode.of(ValueSet.PATH_PAGES);

    /**
     * Terminology Cache Path
     */
    public static final GuideParameterCode PATH_TX_CACHE = GuideParameterCode.of(ValueSet.PATH_TX_CACHE);

    /**
     * Expansion Profile
     */
    public static final GuideParameterCode EXPANSION_PARAMETER = GuideParameterCode.of(ValueSet.EXPANSION_PARAMETER);

    /**
     * Broken Links Rule
     */
    public static final GuideParameterCode RULE_BROKEN_LINKS = GuideParameterCode.of(ValueSet.RULE_BROKEN_LINKS);

    /**
     * Generate XML
     */
    public static final GuideParameterCode GENERATE_XML = GuideParameterCode.of(ValueSet.GENERATE_XML);

    /**
     * Generate JSON
     */
    public static final GuideParameterCode GENERATE_JSON = GuideParameterCode.of(ValueSet.GENERATE_JSON);

    /**
     * Generate Turtle
     */
    public static final GuideParameterCode GENERATE_TURTLE = GuideParameterCode.of(ValueSet.GENERATE_TURTLE);

    /**
     * HTML Template
     */
    public static final GuideParameterCode HTML_TEMPLATE = GuideParameterCode.of(ValueSet.HTML_TEMPLATE);

    private volatile int hashCode;

    private GuideParameterCode(Builder builder) {
        super(builder);
    }

    public static GuideParameterCode of(java.lang.String value) {
        return GuideParameterCode.builder().value(value).build();
    }

    public static GuideParameterCode of(ValueSet value) {
        return GuideParameterCode.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return GuideParameterCode.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return GuideParameterCode.builder().value(value).build();
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
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
