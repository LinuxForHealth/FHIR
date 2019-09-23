/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class ActionRelationshipType extends Code {
    /**
     * Before Start
     */
    public static final ActionRelationshipType BEFORE_START = ActionRelationshipType.of(ValueSet.BEFORE_START);

    /**
     * Before
     */
    public static final ActionRelationshipType BEFORE = ActionRelationshipType.of(ValueSet.BEFORE);

    /**
     * Before End
     */
    public static final ActionRelationshipType BEFORE_END = ActionRelationshipType.of(ValueSet.BEFORE_END);

    /**
     * Concurrent With Start
     */
    public static final ActionRelationshipType CONCURRENT_WITH_START = ActionRelationshipType.of(ValueSet.CONCURRENT_WITH_START);

    /**
     * Concurrent
     */
    public static final ActionRelationshipType CONCURRENT = ActionRelationshipType.of(ValueSet.CONCURRENT);

    /**
     * Concurrent With End
     */
    public static final ActionRelationshipType CONCURRENT_WITH_END = ActionRelationshipType.of(ValueSet.CONCURRENT_WITH_END);

    /**
     * After Start
     */
    public static final ActionRelationshipType AFTER_START = ActionRelationshipType.of(ValueSet.AFTER_START);

    /**
     * After
     */
    public static final ActionRelationshipType AFTER = ActionRelationshipType.of(ValueSet.AFTER);

    /**
     * After End
     */
    public static final ActionRelationshipType AFTER_END = ActionRelationshipType.of(ValueSet.AFTER_END);

    private volatile int hashCode;

    private ActionRelationshipType(Builder builder) {
        super(builder);
    }

    public static ActionRelationshipType of(java.lang.String value) {
        return ActionRelationshipType.builder().value(value).build();
    }

    public static ActionRelationshipType of(ValueSet value) {
        return ActionRelationshipType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ActionRelationshipType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ActionRelationshipType.builder().value(value).build();
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
        ActionRelationshipType other = (ActionRelationshipType) obj;
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
        public ActionRelationshipType build() {
            return new ActionRelationshipType(this);
        }
    }

    public enum ValueSet {
        /**
         * Before Start
         */
        BEFORE_START("before-start"),

        /**
         * Before
         */
        BEFORE("before"),

        /**
         * Before End
         */
        BEFORE_END("before-end"),

        /**
         * Concurrent With Start
         */
        CONCURRENT_WITH_START("concurrent-with-start"),

        /**
         * Concurrent
         */
        CONCURRENT("concurrent"),

        /**
         * Concurrent With End
         */
        CONCURRENT_WITH_END("concurrent-with-end"),

        /**
         * After Start
         */
        AFTER_START("after-start"),

        /**
         * After
         */
        AFTER("after"),

        /**
         * After End
         */
        AFTER_END("after-end");

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
