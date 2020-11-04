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

@System("http://hl7.org/fhir/composition-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ReferredDocumentStatus extends Code {
    /**
     * Preliminary
     * 
     * <p>This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or 
     * unverified.
     */
    public static final ReferredDocumentStatus PRELIMINARY = ReferredDocumentStatus.builder().value(ValueSet.PRELIMINARY).build();

    /**
     * Final
     * 
     * <p>This version of the composition is complete and verified by an appropriate person and no further work is planned. 
     * Any subsequent updates would be on a new version of the composition.
     */
    public static final ReferredDocumentStatus FINAL = ReferredDocumentStatus.builder().value(ValueSet.FINAL).build();

    /**
     * Amended
     * 
     * <p>The composition content or the referenced resources have been modified (edited or added to) subsequent to being 
     * released as "final" and the composition is complete and verified by an authorized person.
     */
    public static final ReferredDocumentStatus AMENDED = ReferredDocumentStatus.builder().value(ValueSet.AMENDED).build();

    /**
     * Entered in Error
     * 
     * <p>The composition or document was originally created/issued in error, and this is an amendment that marks that the 
     * entire series should not be considered as valid.
     */
    public static final ReferredDocumentStatus ENTERED_IN_ERROR = ReferredDocumentStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private ReferredDocumentStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating ReferredDocumentStatus objects from a passed enum value.
     */
    public static ReferredDocumentStatus of(ValueSet value) {
        switch (value) {
        case PRELIMINARY:
            return PRELIMINARY;
        case FINAL:
            return FINAL;
        case AMENDED:
            return AMENDED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ReferredDocumentStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ReferredDocumentStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating ReferredDocumentStatus objects from a passed string value.
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
     * Inherited factory method for creating ReferredDocumentStatus objects from a passed string value.
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
        ReferredDocumentStatus other = (ReferredDocumentStatus) obj;
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
        public ReferredDocumentStatus build() {
            return new ReferredDocumentStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Preliminary
         * 
         * <p>This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or 
         * unverified.
         */
        PRELIMINARY("preliminary"),

        /**
         * Final
         * 
         * <p>This version of the composition is complete and verified by an appropriate person and no further work is planned. 
         * Any subsequent updates would be on a new version of the composition.
         */
        FINAL("final"),

        /**
         * Amended
         * 
         * <p>The composition content or the referenced resources have been modified (edited or added to) subsequent to being 
         * released as "final" and the composition is complete and verified by an authorized person.
         */
        AMENDED("amended"),

        /**
         * Entered in Error
         * 
         * <p>The composition or document was originally created/issued in error, and this is an amendment that marks that the 
         * entire series should not be considered as valid.
         */
        ENTERED_IN_ERROR("entered-in-error");

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
         * Factory method for creating ReferredDocumentStatus.ValueSet values from a passed string value.
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
