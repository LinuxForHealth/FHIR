/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ActionSelectionBehavior extends Code {
    /**
     * Any
     */
    public static final ActionSelectionBehavior ANY = ActionSelectionBehavior.of(ValueSet.ANY);

    /**
     * All
     */
    public static final ActionSelectionBehavior ALL = ActionSelectionBehavior.of(ValueSet.ALL);

    /**
     * All Or None
     */
    public static final ActionSelectionBehavior ALL_OR_NONE = ActionSelectionBehavior.of(ValueSet.ALL_OR_NONE);

    /**
     * Exactly One
     */
    public static final ActionSelectionBehavior EXACTLY_ONE = ActionSelectionBehavior.of(ValueSet.EXACTLY_ONE);

    /**
     * At Most One
     */
    public static final ActionSelectionBehavior AT_MOST_ONE = ActionSelectionBehavior.of(ValueSet.AT_MOST_ONE);

    /**
     * One Or More
     */
    public static final ActionSelectionBehavior ONE_OR_MORE = ActionSelectionBehavior.of(ValueSet.ONE_OR_MORE);

    private ActionSelectionBehavior(Builder builder) {
        super(builder);
    }

    public static ActionSelectionBehavior of(java.lang.String value) {
        return ActionSelectionBehavior.builder().value(value).build();
    }

    public static ActionSelectionBehavior of(ValueSet value) {
        return ActionSelectionBehavior.builder().value(value).build();
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
        public ActionSelectionBehavior build() {
            return new ActionSelectionBehavior(this);
        }
    }

    public enum ValueSet {
        /**
         * Any
         */
        ANY("any"),

        /**
         * All
         */
        ALL("all"),

        /**
         * All Or None
         */
        ALL_OR_NONE("all-or-none"),

        /**
         * Exactly One
         */
        EXACTLY_ONE("exactly-one"),

        /**
         * At Most One
         */
        AT_MOST_ONE("at-most-one"),

        /**
         * One Or More
         */
        ONE_OR_MORE("one-or-more");

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
