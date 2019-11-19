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
public class ActionSelectionBehavior extends Code {
    /**
     * Any
     */
    public static final ActionSelectionBehavior ANY = ActionSelectionBehavior.builder().value(ValueSet.ANY).build();

    /**
     * All
     */
    public static final ActionSelectionBehavior ALL = ActionSelectionBehavior.builder().value(ValueSet.ALL).build();

    /**
     * All Or None
     */
    public static final ActionSelectionBehavior ALL_OR_NONE = ActionSelectionBehavior.builder().value(ValueSet.ALL_OR_NONE).build();

    /**
     * Exactly One
     */
    public static final ActionSelectionBehavior EXACTLY_ONE = ActionSelectionBehavior.builder().value(ValueSet.EXACTLY_ONE).build();

    /**
     * At Most One
     */
    public static final ActionSelectionBehavior AT_MOST_ONE = ActionSelectionBehavior.builder().value(ValueSet.AT_MOST_ONE).build();

    /**
     * One Or More
     */
    public static final ActionSelectionBehavior ONE_OR_MORE = ActionSelectionBehavior.builder().value(ValueSet.ONE_OR_MORE).build();

    private volatile int hashCode;

    private ActionSelectionBehavior(Builder builder) {
        super(builder);
    }

    public static ActionSelectionBehavior of(ValueSet value) {
        switch (value) {
        case ANY:
            return ANY;
        case ALL:
            return ALL;
        case ALL_OR_NONE:
            return ALL_OR_NONE;
        case EXACTLY_ONE:
            return EXACTLY_ONE;
        case AT_MOST_ONE:
            return AT_MOST_ONE;
        case ONE_OR_MORE:
            return ONE_OR_MORE;
        default:
            throw new IllegalArgumentException(value.name());
        }
    }

    public static ActionSelectionBehavior of(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.valueOf(value));
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
        ActionSelectionBehavior other = (ActionSelectionBehavior) obj;
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
