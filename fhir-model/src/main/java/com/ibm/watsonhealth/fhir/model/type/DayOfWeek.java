/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class DayOfWeek extends Code {
    /**
     * Monday
     */
    public static final DayOfWeek MON = DayOfWeek.of(ValueSet.MON);

    /**
     * Tuesday
     */
    public static final DayOfWeek TUE = DayOfWeek.of(ValueSet.TUE);

    /**
     * Wednesday
     */
    public static final DayOfWeek WED = DayOfWeek.of(ValueSet.WED);

    /**
     * Thursday
     */
    public static final DayOfWeek THU = DayOfWeek.of(ValueSet.THU);

    /**
     * Friday
     */
    public static final DayOfWeek FRI = DayOfWeek.of(ValueSet.FRI);

    /**
     * Saturday
     */
    public static final DayOfWeek SAT = DayOfWeek.of(ValueSet.SAT);

    /**
     * Sunday
     */
    public static final DayOfWeek SUN = DayOfWeek.of(ValueSet.SUN);

    private volatile int hashCode;

    private DayOfWeek(Builder builder) {
        super(builder);
    }

    public static DayOfWeek of(java.lang.String value) {
        return DayOfWeek.builder().value(value).build();
    }

    public static DayOfWeek of(ValueSet value) {
        return DayOfWeek.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return DayOfWeek.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return DayOfWeek.builder().value(value).build();
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
        DayOfWeek other = (DayOfWeek) obj;
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
        public DayOfWeek build() {
            return new DayOfWeek(this);
        }
    }

    public enum ValueSet {
        /**
         * Monday
         */
        MON("mon"),

        /**
         * Tuesday
         */
        TUE("tue"),

        /**
         * Wednesday
         */
        WED("wed"),

        /**
         * Thursday
         */
        THU("thu"),

        /**
         * Friday
         */
        FRI("fri"),

        /**
         * Saturday
         */
        SAT("sat"),

        /**
         * Sunday
         */
        SUN("sun");

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
