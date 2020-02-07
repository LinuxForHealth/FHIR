/*
 * (C) Copyright IBM Corp. 2019, 2020
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
public class ActionGroupingBehavior extends Code {
    /**
     * Visual Group
     */
    public static final ActionGroupingBehavior VISUAL_GROUP = ActionGroupingBehavior.builder().value(ValueSet.VISUAL_GROUP).build();

    /**
     * Logical Group
     */
    public static final ActionGroupingBehavior LOGICAL_GROUP = ActionGroupingBehavior.builder().value(ValueSet.LOGICAL_GROUP).build();

    /**
     * Sentence Group
     */
    public static final ActionGroupingBehavior SENTENCE_GROUP = ActionGroupingBehavior.builder().value(ValueSet.SENTENCE_GROUP).build();

    private volatile int hashCode;

    private ActionGroupingBehavior(Builder builder) {
        super(builder);
    }

    public static ActionGroupingBehavior of(ValueSet value) {
        switch (value) {
        case VISUAL_GROUP:
            return VISUAL_GROUP;
        case LOGICAL_GROUP:
            return LOGICAL_GROUP;
        case SENTENCE_GROUP:
            return SENTENCE_GROUP;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ActionGroupingBehavior of(java.lang.String value) {
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
        ActionGroupingBehavior other = (ActionGroupingBehavior) obj;
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
        public ActionGroupingBehavior build() {
            return new ActionGroupingBehavior(this);
        }
    }

    public enum ValueSet {
        /**
         * Visual Group
         */
        VISUAL_GROUP("visual-group"),

        /**
         * Logical Group
         */
        LOGICAL_GROUP("logical-group"),

        /**
         * Sentence Group
         */
        SENTENCE_GROUP("sentence-group");

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
