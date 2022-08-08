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

@System("http://hl7.org/fhir/publication-status")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class PublicationStatus extends Code {
    /**
     * Draft
     * 
     * <p>This resource is still under development and is not yet considered to be ready for normal use.
     */
    public static final PublicationStatus DRAFT = PublicationStatus.builder().value(Value.DRAFT).build();

    /**
     * Active
     * 
     * <p>This resource is ready for normal use.
     */
    public static final PublicationStatus ACTIVE = PublicationStatus.builder().value(Value.ACTIVE).build();

    /**
     * Retired
     * 
     * <p>This resource has been withdrawn or superseded and should no longer be used.
     */
    public static final PublicationStatus RETIRED = PublicationStatus.builder().value(Value.RETIRED).build();

    /**
     * Unknown
     * 
     * <p>The authoring system does not know which of the status values currently applies for this resource. Note: This 
     * concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
     */
    public static final PublicationStatus UNKNOWN = PublicationStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private PublicationStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this PublicationStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating PublicationStatus objects from a passed enum value.
     */
    public static PublicationStatus of(Value value) {
        switch (value) {
        case DRAFT:
            return DRAFT;
        case ACTIVE:
            return ACTIVE;
        case RETIRED:
            return RETIRED;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating PublicationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static PublicationStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating PublicationStatus objects from a passed string value.
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
     * Inherited factory method for creating PublicationStatus objects from a passed string value.
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
        PublicationStatus other = (PublicationStatus) obj;
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
         *     An enum constant for PublicationStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public PublicationStatus build() {
            PublicationStatus publicationStatus = new PublicationStatus(this);
            if (validating) {
                validate(publicationStatus);
            }
            return publicationStatus;
        }

        protected void validate(PublicationStatus publicationStatus) {
            super.validate(publicationStatus);
        }

        protected Builder from(PublicationStatus publicationStatus) {
            super.from(publicationStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Draft
         * 
         * <p>This resource is still under development and is not yet considered to be ready for normal use.
         */
        DRAFT("draft"),

        /**
         * Active
         * 
         * <p>This resource is ready for normal use.
         */
        ACTIVE("active"),

        /**
         * Retired
         * 
         * <p>This resource has been withdrawn or superseded and should no longer be used.
         */
        RETIRED("retired"),

        /**
         * Unknown
         * 
         * <p>The authoring system does not know which of the status values currently applies for this resource. Note: This 
         * concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
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
         * Factory method for creating PublicationStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding PublicationStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "draft":
                return DRAFT;
            case "active":
                return ACTIVE;
            case "retired":
                return RETIRED;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
