/*
 * (C) Copyright IBM Corp. 2019, 2020
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

@System("http://hl7.org/fhir/event-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CommunicationStatus extends Code {
    /**
     * Preparation
     * 
     * <p>The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation). 
     * Preparation stages may be tracked for billing purposes.
     */
    public static final CommunicationStatus PREPARATION = CommunicationStatus.builder().value(ValueSet.PREPARATION).build();

    /**
     * In Progress
     * 
     * <p>The event is currently occurring.
     */
    public static final CommunicationStatus IN_PROGRESS = CommunicationStatus.builder().value(ValueSet.IN_PROGRESS).build();

    /**
     * Not Done
     * 
     * <p>The event was terminated prior to any activity beyond preparation. I.e. The 'main' activity has not yet begun. The 
     * boundary between preparatory and the 'main' activity is context-specific.
     */
    public static final CommunicationStatus NOT_DONE = CommunicationStatus.builder().value(ValueSet.NOT_DONE).build();

    /**
     * On Hold
     * 
     * <p>The event has been temporarily stopped but is expected to resume in the future.
     */
    public static final CommunicationStatus ON_HOLD = CommunicationStatus.builder().value(ValueSet.ON_HOLD).build();

    /**
     * Stopped
     * 
     * <p>The event was terminated prior to the full completion of the intended activity but after at least some of the 
     * 'main' activity (beyond preparation) has occurred.
     */
    public static final CommunicationStatus STOPPED = CommunicationStatus.builder().value(ValueSet.STOPPED).build();

    /**
     * Completed
     * 
     * <p>The event has now concluded.
     */
    public static final CommunicationStatus COMPLETED = CommunicationStatus.builder().value(ValueSet.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>This electronic record should never have existed, though it is possible that real-world decisions were based on it. 
     * (If real-world activity has occurred, the status should be "stopped" rather than "entered-in-error".).
     */
    public static final CommunicationStatus ENTERED_IN_ERROR = CommunicationStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The authoring/source system does not know which of the status values currently applies for this event. Note: This 
     * concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
     * system does not know which.
     */
    public static final CommunicationStatus UNKNOWN = CommunicationStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private CommunicationStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating CommunicationStatus objects from a passed enum value.
     */
    public static CommunicationStatus of(ValueSet value) {
        switch (value) {
        case PREPARATION:
            return PREPARATION;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case NOT_DONE:
            return NOT_DONE;
        case ON_HOLD:
            return ON_HOLD;
        case STOPPED:
            return STOPPED;
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CommunicationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CommunicationStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating CommunicationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating CommunicationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
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
        CommunicationStatus other = (CommunicationStatus) obj;
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
        public CommunicationStatus build() {
            return new CommunicationStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Preparation
         * 
         * <p>The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation). 
         * Preparation stages may be tracked for billing purposes.
         */
        PREPARATION("preparation"),

        /**
         * In Progress
         * 
         * <p>The event is currently occurring.
         */
        IN_PROGRESS("in-progress"),

        /**
         * Not Done
         * 
         * <p>The event was terminated prior to any activity beyond preparation. I.e. The 'main' activity has not yet begun. The 
         * boundary between preparatory and the 'main' activity is context-specific.
         */
        NOT_DONE("not-done"),

        /**
         * On Hold
         * 
         * <p>The event has been temporarily stopped but is expected to resume in the future.
         */
        ON_HOLD("on-hold"),

        /**
         * Stopped
         * 
         * <p>The event was terminated prior to the full completion of the intended activity but after at least some of the 
         * 'main' activity (beyond preparation) has occurred.
         */
        STOPPED("stopped"),

        /**
         * Completed
         * 
         * <p>The event has now concluded.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>This electronic record should never have existed, though it is possible that real-world decisions were based on it. 
         * (If real-world activity has occurred, the status should be "stopped" rather than "entered-in-error".).
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         * 
         * <p>The authoring/source system does not know which of the status values currently applies for this event. Note: This 
         * concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
         * system does not know which.
         */
        UNKNOWN("unknown");

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
         * Factory method for creating CommunicationStatus.ValueSet values from a passed string value.
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
}
