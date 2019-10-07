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
public class DaysOfWeek extends Code {
    /**
     * Monday
     */
    public static final DaysOfWeek MON = DaysOfWeek.of(ValueSet.MON);

    /**
     * Tuesday
     */
    public static final DaysOfWeek TUE = DaysOfWeek.of(ValueSet.TUE);

    /**
     * Wednesday
     */
    public static final DaysOfWeek WED = DaysOfWeek.of(ValueSet.WED);

    /**
     * Thursday
     */
    public static final DaysOfWeek THU = DaysOfWeek.of(ValueSet.THU);

    /**
     * Friday
     */
    public static final DaysOfWeek FRI = DaysOfWeek.of(ValueSet.FRI);

    /**
     * Saturday
     */
    public static final DaysOfWeek SAT = DaysOfWeek.of(ValueSet.SAT);

    /**
     * Sunday
     */
    public static final DaysOfWeek SUN = DaysOfWeek.of(ValueSet.SUN);

    private volatile int hashCode;

    private DaysOfWeek(Builder builder) {
        super(builder);
    }

    public static DaysOfWeek of(java.lang.String value) {
        return DaysOfWeek.builder().value(value).build();
    }

    public static DaysOfWeek of(ValueSet value) {
        return DaysOfWeek.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return DaysOfWeek.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return DaysOfWeek.builder().value(value).build();
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
        DaysOfWeek other = (DaysOfWeek) obj;
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
        public DaysOfWeek build() {
            return new DaysOfWeek(this);
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
