/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/observation-status")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ObservationStatus extends Code {
    /**
     * Registered
     * 
     * <p>The existence of the observation is registered, but there is no result yet available.
     */
    public static final ObservationStatus REGISTERED = ObservationStatus.builder().value(Value.REGISTERED).build();

    /**
     * Preliminary
     * 
     * <p>This is an initial or interim observation: data may be incomplete or unverified.
     */
    public static final ObservationStatus PRELIMINARY = ObservationStatus.builder().value(Value.PRELIMINARY).build();

    /**
     * Final
     * 
     * <p>The observation is complete and there are no further actions needed. Additional information such "released", 
     * "signed", etc would be represented using [Provenance](provenance.html) which provides not only the act but also the 
     * actors and dates and other related data. These act states would be associated with an observation status of 
     * `preliminary` until they are all completed and then a status of `final` would be applied.
     */
    public static final ObservationStatus FINAL = ObservationStatus.builder().value(Value.FINAL).build();

    /**
     * Amended
     * 
     * <p>Subsequent to being Final, the observation has been modified subsequent. This includes updates/new information and 
     * corrections.
     */
    public static final ObservationStatus AMENDED = ObservationStatus.builder().value(Value.AMENDED).build();

    /**
     * Corrected
     * 
     * <p>Subsequent to being Final, the observation has been modified to correct an error in the test result.
     */
    public static final ObservationStatus CORRECTED = ObservationStatus.builder().value(Value.CORRECTED).build();

    /**
     * Cancelled
     * 
     * <p>The observation is unavailable because the measurement was not started or not completed (also sometimes called 
     * "aborted").
     */
    public static final ObservationStatus CANCELLED = ObservationStatus.builder().value(Value.CANCELLED).build();

    /**
     * Entered in Error
     * 
     * <p>The observation has been withdrawn following previous final release. This electronic record should never have 
     * existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the 
     * status should be "cancelled" rather than "entered-in-error".).
     */
    public static final ObservationStatus ENTERED_IN_ERROR = ObservationStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The authoring/source system does not know which of the status values currently applies for this observation. Note: 
     * This concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
     * system does not know which.
     */
    public static final ObservationStatus UNKNOWN = ObservationStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private ObservationStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ObservationStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ObservationStatus objects from a passed enum value.
     */
    public static ObservationStatus of(Value value) {
        switch (value) {
        case REGISTERED:
            return REGISTERED;
        case PRELIMINARY:
            return PRELIMINARY;
        case FINAL:
            return FINAL;
        case AMENDED:
            return AMENDED;
        case CORRECTED:
            return CORRECTED;
        case CANCELLED:
            return CANCELLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ObservationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ObservationStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ObservationStatus objects from a passed string value.
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
     * Inherited factory method for creating ObservationStatus objects from a passed string value.
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
        ObservationStatus other = (ObservationStatus) obj;
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
        return new Builder().from(this);
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
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for ObservationStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ObservationStatus build() {
            ObservationStatus observationStatus = new ObservationStatus(this);
            if (validating) {
                validate(observationStatus);
            }
            return observationStatus;
        }

        protected void validate(ObservationStatus observationStatus) {
            super.validate(observationStatus);
        }

        protected Builder from(ObservationStatus observationStatus) {
            super.from(observationStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Registered
         * 
         * <p>The existence of the observation is registered, but there is no result yet available.
         */
        REGISTERED("registered"),

        /**
         * Preliminary
         * 
         * <p>This is an initial or interim observation: data may be incomplete or unverified.
         */
        PRELIMINARY("preliminary"),

        /**
         * Final
         * 
         * <p>The observation is complete and there are no further actions needed. Additional information such "released", 
         * "signed", etc would be represented using [Provenance](provenance.html) which provides not only the act but also the 
         * actors and dates and other related data. These act states would be associated with an observation status of 
         * `preliminary` until they are all completed and then a status of `final` would be applied.
         */
        FINAL("final"),

        /**
         * Amended
         * 
         * <p>Subsequent to being Final, the observation has been modified subsequent. This includes updates/new information and 
         * corrections.
         */
        AMENDED("amended"),

        /**
         * Corrected
         * 
         * <p>Subsequent to being Final, the observation has been modified to correct an error in the test result.
         */
        CORRECTED("corrected"),

        /**
         * Cancelled
         * 
         * <p>The observation is unavailable because the measurement was not started or not completed (also sometimes called 
         * "aborted").
         */
        CANCELLED("cancelled"),

        /**
         * Entered in Error
         * 
         * <p>The observation has been withdrawn following previous final release. This electronic record should never have 
         * existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the 
         * status should be "cancelled" rather than "entered-in-error".).
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         * 
         * <p>The authoring/source system does not know which of the status values currently applies for this observation. Note: 
         * This concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
         * system does not know which.
         */
        UNKNOWN("unknown");

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
         * Factory method for creating ObservationStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ObservationStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "registered":
                return REGISTERED;
            case "preliminary":
                return PRELIMINARY;
            case "final":
                return FINAL;
            case "amended":
                return AMENDED;
            case "corrected":
                return CORRECTED;
            case "cancelled":
                return CANCELLED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
