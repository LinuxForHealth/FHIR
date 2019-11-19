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
public class MessageHeaderResponseRequest extends Code {
    /**
     * Always
     */
    public static final MessageHeaderResponseRequest ALWAYS = MessageHeaderResponseRequest.builder().value(ValueSet.ALWAYS).build();

    /**
     * Error/reject conditions only
     */
    public static final MessageHeaderResponseRequest ON_ERROR = MessageHeaderResponseRequest.builder().value(ValueSet.ON_ERROR).build();

    /**
     * Never
     */
    public static final MessageHeaderResponseRequest NEVER = MessageHeaderResponseRequest.builder().value(ValueSet.NEVER).build();

    /**
     * Successful completion only
     */
    public static final MessageHeaderResponseRequest ON_SUCCESS = MessageHeaderResponseRequest.builder().value(ValueSet.ON_SUCCESS).build();

    private volatile int hashCode;

    private MessageHeaderResponseRequest(Builder builder) {
        super(builder);
    }

    public static MessageHeaderResponseRequest of(ValueSet value) {
        switch (value) {
        case ALWAYS:
            return ALWAYS;
        case ON_ERROR:
            return ON_ERROR;
        case NEVER:
            return NEVER;
        case ON_SUCCESS:
            return ON_SUCCESS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static MessageHeaderResponseRequest of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

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
        MessageHeaderResponseRequest other = (MessageHeaderResponseRequest) obj;
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
