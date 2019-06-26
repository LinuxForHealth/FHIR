/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class MessageHeaderResponseRequest extends Code {
    /**
     * Always
     */
    public static final MessageHeaderResponseRequest ALWAYS = MessageHeaderResponseRequest.of(ValueSet.ALWAYS);

    /**
     * Error/reject conditions only
     */
    public static final MessageHeaderResponseRequest ON_ERROR = MessageHeaderResponseRequest.of(ValueSet.ON_ERROR);

    /**
     * Never
     */
    public static final MessageHeaderResponseRequest NEVER = MessageHeaderResponseRequest.of(ValueSet.NEVER);

    /**
     * Successful completion only
     */
    public static final MessageHeaderResponseRequest ON_SUCCESS = MessageHeaderResponseRequest.of(ValueSet.ON_SUCCESS);

    private MessageHeaderResponseRequest(Builder builder) {
        super(builder);
    }

    public static MessageHeaderResponseRequest of(java.lang.String value) {
        return MessageHeaderResponseRequest.builder().value(value).build();
    }

    public static MessageHeaderResponseRequest of(ValueSet value) {
        return MessageHeaderResponseRequest.builder().value(value).build();
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
        public MessageHeaderResponseRequest build() {
            return new MessageHeaderResponseRequest(this);
        }
    }

    public enum ValueSet {
        /**
         * Always
         */
        ALWAYS("always"),

        /**
         * Error/reject conditions only
         */
        ON_ERROR("on-error"),

        /**
         * Never
         */
        NEVER("never"),

        /**
         * Successful completion only
         */
        ON_SUCCESS("on-success");

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
