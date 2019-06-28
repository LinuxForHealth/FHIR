/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class SubscriptionChannelType extends Code {
    /**
     * Rest Hook
     */
    public static final SubscriptionChannelType REST_HOOK = SubscriptionChannelType.of(ValueSet.REST_HOOK);

    /**
     * Websocket
     */
    public static final SubscriptionChannelType WEBSOCKET = SubscriptionChannelType.of(ValueSet.WEBSOCKET);

    /**
     * Email
     */
    public static final SubscriptionChannelType EMAIL = SubscriptionChannelType.of(ValueSet.EMAIL);

    /**
     * SMS
     */
    public static final SubscriptionChannelType SMS = SubscriptionChannelType.of(ValueSet.SMS);

    /**
     * Message
     */
    public static final SubscriptionChannelType MESSAGE = SubscriptionChannelType.of(ValueSet.MESSAGE);

    private SubscriptionChannelType(Builder builder) {
        super(builder);
    }

    public static SubscriptionChannelType of(java.lang.String value) {
        return SubscriptionChannelType.builder().value(value).build();
    }

    public static SubscriptionChannelType of(ValueSet value) {
        return SubscriptionChannelType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return SubscriptionChannelType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return SubscriptionChannelType.builder().value(value).build();
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
        public SubscriptionChannelType build() {
            return new SubscriptionChannelType(this);
        }
    }

    public enum ValueSet {
        /**
         * Rest Hook
         */
        REST_HOOK("rest-hook"),

        /**
         * Websocket
         */
        WEBSOCKET("websocket"),

        /**
         * Email
         */
        EMAIL("email"),

        /**
         * SMS
         */
        SMS("sms"),

        /**
         * Message
         */
        MESSAGE("message");

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
