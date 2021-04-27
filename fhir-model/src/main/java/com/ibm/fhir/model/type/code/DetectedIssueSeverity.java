/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/detectedissue-severity")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DetectedIssueSeverity extends Code {
    /**
     * High
     * 
     * <p>Indicates the issue may be life-threatening or has the potential to cause permanent injury.
     */
    public static final DetectedIssueSeverity HIGH = DetectedIssueSeverity.builder().value(Value.HIGH).build();

    /**
     * Moderate
     * 
     * <p>Indicates the issue may result in noticeable adverse consequences but is unlikely to be life-threatening or cause 
     * permanent injury.
     */
    public static final DetectedIssueSeverity MODERATE = DetectedIssueSeverity.builder().value(Value.MODERATE).build();

    /**
     * Low
     * 
     * <p>Indicates the issue may result in some adverse consequences but is unlikely to substantially affect the situation 
     * of the subject.
     */
    public static final DetectedIssueSeverity LOW = DetectedIssueSeverity.builder().value(Value.LOW).build();

    private volatile int hashCode;

    private DetectedIssueSeverity(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DetectedIssueSeverity as an enum constant.
     * @deprecated replaced by {@link #getValueAsEnum()}
     */
    @Deprecated
    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Get the value of this DetectedIssueSeverity as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DetectedIssueSeverity objects from a passed enum value.
     * @deprecated replaced by {@link #of(Value)}
     */
    @Deprecated
    public static DetectedIssueSeverity of(ValueSet value) {
        switch (value) {
        case HIGH:
            return HIGH;
        case MODERATE:
            return MODERATE;
        case LOW:
            return LOW;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DetectedIssueSeverity objects from a passed enum value.
     */
    public static DetectedIssueSeverity of(Value value) {
        switch (value) {
        case HIGH:
            return HIGH;
        case MODERATE:
            return MODERATE;
        case LOW:
            return LOW;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DetectedIssueSeverity objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DetectedIssueSeverity of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DetectedIssueSeverity objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DetectedIssueSeverity objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
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
        DetectedIssueSeverity other = (DetectedIssueSeverity) obj;
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * @deprecated replaced by  {@link #value(Value)}
         */
        @Deprecated
        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for DetectedIssueSeverity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DetectedIssueSeverity build() {
            return new DetectedIssueSeverity(this);
        }
    }

    @Deprecated
    public enum ValueSet {
        /**
         * High
         * 
         * <p>Indicates the issue may be life-threatening or has the potential to cause permanent injury.
         */
        HIGH("high"),

        /**
         * Moderate
         * 
         * <p>Indicates the issue may result in noticeable adverse consequences but is unlikely to be life-threatening or cause 
         * permanent injury.
         */
        MODERATE("moderate"),

        /**
         * Low
         * 
         * <p>Indicates the issue may result in some adverse consequences but is unlikely to substantially affect the situation 
         * of the subject.
         */
        LOW("low");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating DetectedIssueSeverity.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }

    public enum Value {
        /**
         * High
         * 
         * <p>Indicates the issue may be life-threatening or has the potential to cause permanent injury.
         */
        HIGH("high"),

        /**
         * Moderate
         * 
         * <p>Indicates the issue may result in noticeable adverse consequences but is unlikely to be life-threatening or cause 
         * permanent injury.
         */
        MODERATE("moderate"),

        /**
         * Low
         * 
         * <p>Indicates the issue may result in some adverse consequences but is unlikely to substantially affect the situation 
         * of the subject.
         */
        LOW("low");

        private final java.lang.String value;

        Value(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating DetectedIssueSeverity.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            for (Value c : Value.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
