/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class GroupMeasure extends Code {
    /**
     * Mean
     */
    public static final GroupMeasure MEAN = GroupMeasure.of(ValueSet.MEAN);

    /**
     * Median
     */
    public static final GroupMeasure MEDIAN = GroupMeasure.of(ValueSet.MEDIAN);

    /**
     * Mean of Study Means
     */
    public static final GroupMeasure MEAN_OF_MEAN = GroupMeasure.of(ValueSet.MEAN_OF_MEAN);

    /**
     * Mean of Study Medins
     */
    public static final GroupMeasure MEAN_OF_MEDIAN = GroupMeasure.of(ValueSet.MEAN_OF_MEDIAN);

    /**
     * Median of Study Means
     */
    public static final GroupMeasure MEDIAN_OF_MEAN = GroupMeasure.of(ValueSet.MEDIAN_OF_MEAN);

    /**
     * Median of Study Medians
     */
    public static final GroupMeasure MEDIAN_OF_MEDIAN = GroupMeasure.of(ValueSet.MEDIAN_OF_MEDIAN);

    private volatile int hashCode;

    private GroupMeasure(Builder builder) {
        super(builder);
    }

    public static GroupMeasure of(java.lang.String value) {
        return GroupMeasure.builder().value(value).build();
    }

    public static GroupMeasure of(ValueSet value) {
        return GroupMeasure.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return GroupMeasure.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return GroupMeasure.builder().value(value).build();
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
        GroupMeasure other = (GroupMeasure) obj;
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
        public GroupMeasure build() {
            return new GroupMeasure(this);
        }
    }

    public enum ValueSet {
        /**
         * Mean
         */
        MEAN("mean"),

        /**
         * Median
         */
        MEDIAN("median"),

        /**
         * Mean of Study Means
         */
        MEAN_OF_MEAN("mean-of-mean"),

        /**
         * Mean of Study Medins
         */
        MEAN_OF_MEDIAN("mean-of-median"),

        /**
         * Median of Study Means
         */
        MEDIAN_OF_MEAN("median-of-mean"),

        /**
         * Median of Study Medians
         */
        MEDIAN_OF_MEDIAN("median-of-median");

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
