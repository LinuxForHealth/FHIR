/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ActionCardinalityBehavior extends Code {
    /**
     * Single
     */
    public static final ActionCardinalityBehavior SINGLE = ActionCardinalityBehavior.of(ValueSet.SINGLE);

    /**
     * Multiple
     */
    public static final ActionCardinalityBehavior MULTIPLE = ActionCardinalityBehavior.of(ValueSet.MULTIPLE);

    private ActionCardinalityBehavior(Builder builder) {
        super(builder);
    }

    public static ActionCardinalityBehavior of(java.lang.String value) {
        return ActionCardinalityBehavior.builder().value(value).build();
    }

    public static ActionCardinalityBehavior of(ValueSet value) {
        return ActionCardinalityBehavior.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
        public ActionCardinalityBehavior build() {
            return new ActionCardinalityBehavior(this);
        }
    }

    public enum ValueSet {
        /**
         * Single
         */
        SINGLE("single"),

        /**
         * Multiple
         */
        MULTIPLE("multiple");

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
