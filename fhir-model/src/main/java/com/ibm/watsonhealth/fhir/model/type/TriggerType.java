/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class TriggerType extends Code {
    /**
     * Named Event
     */
    public static final TriggerType NAMED_EVENT = TriggerType.of(ValueSet.NAMED_EVENT);

    /**
     * Periodic
     */
    public static final TriggerType PERIODIC = TriggerType.of(ValueSet.PERIODIC);

    /**
     * Data Changed
     */
    public static final TriggerType DATA_CHANGED = TriggerType.of(ValueSet.DATA_CHANGED);

    /**
     * Data Added
     */
    public static final TriggerType DATA_ADDED = TriggerType.of(ValueSet.DATA_ADDED);

    /**
     * Data Updated
     */
    public static final TriggerType DATA_MODIFIED = TriggerType.of(ValueSet.DATA_MODIFIED);

    /**
     * Data Removed
     */
    public static final TriggerType DATA_REMOVED = TriggerType.of(ValueSet.DATA_REMOVED);

    /**
     * Data Accessed
     */
    public static final TriggerType DATA_ACCESSED = TriggerType.of(ValueSet.DATA_ACCESSED);

    /**
     * Data Access Ended
     */
    public static final TriggerType DATA_ACCESS_ENDED = TriggerType.of(ValueSet.DATA_ACCESS_ENDED);

    private volatile int hashCode;

    private TriggerType(Builder builder) {
        super(builder);
    }

    public static TriggerType of(java.lang.String value) {
        return TriggerType.builder().value(value).build();
    }

    public static TriggerType of(ValueSet value) {
        return TriggerType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return TriggerType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return TriggerType.builder().value(value).build();
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
        TriggerType other = (TriggerType) obj;
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
        public TriggerType build() {
            return new TriggerType(this);
        }
    }

    public enum ValueSet {
        /**
         * Named Event
         */
        NAMED_EVENT("named-event"),

        /**
         * Periodic
         */
        PERIODIC("periodic"),

        /**
         * Data Changed
         */
        DATA_CHANGED("data-changed"),

        /**
         * Data Added
         */
        DATA_ADDED("data-added"),

        /**
         * Data Updated
         */
        DATA_MODIFIED("data-modified"),

        /**
         * Data Removed
         */
        DATA_REMOVED("data-removed"),

        /**
         * Data Accessed
         */
        DATA_ACCESSED("data-accessed"),

        /**
         * Data Access Ended
         */
        DATA_ACCESS_ENDED("data-access-ended");

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
