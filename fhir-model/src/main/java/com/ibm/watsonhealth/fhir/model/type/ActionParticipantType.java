/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ActionParticipantType extends Code {
    /**
     * Patient
     */
    public static final ActionParticipantType PATIENT = ActionParticipantType.of(ValueSet.PATIENT);

    /**
     * Practitioner
     */
    public static final ActionParticipantType PRACTITIONER = ActionParticipantType.of(ValueSet.PRACTITIONER);

    /**
     * Related Person
     */
    public static final ActionParticipantType RELATED_PERSON = ActionParticipantType.of(ValueSet.RELATED_PERSON);

    /**
     * Device
     */
    public static final ActionParticipantType DEVICE = ActionParticipantType.of(ValueSet.DEVICE);

    private ActionParticipantType(Builder builder) {
        super(builder);
    }

    public static ActionParticipantType of(java.lang.String value) {
        return ActionParticipantType.builder().value(value).build();
    }

    public static ActionParticipantType of(ValueSet value) {
        return ActionParticipantType.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
        public ActionParticipantType build() {
            return new ActionParticipantType(this);
        }
    }

    public enum ValueSet {
        /**
         * Patient
         */
        PATIENT("patient"),

        /**
         * Practitioner
         */
        PRACTITIONER("practitioner"),

        /**
         * Related Person
         */
        RELATED_PERSON("related-person"),

        /**
         * Device
         */
        DEVICE("device");

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
