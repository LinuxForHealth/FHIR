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

@System("http://hl7.org/fhir/bundle-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class BundleType extends Code {
    /**
     * Document
     * 
     * <p>The bundle is a document. The first resource is a Composition.
     */
    public static final BundleType DOCUMENT = BundleType.builder().value(Value.DOCUMENT).build();

    /**
     * Message
     * 
     * <p>The bundle is a message. The first resource is a MessageHeader.
     */
    public static final BundleType MESSAGE = BundleType.builder().value(Value.MESSAGE).build();

    /**
     * Transaction
     * 
     * <p>The bundle is a transaction - intended to be processed by a server as an atomic commit.
     */
    public static final BundleType TRANSACTION = BundleType.builder().value(Value.TRANSACTION).build();

    /**
     * Transaction Response
     * 
     * <p>The bundle is a transaction response. Because the response is a transaction response, the transaction has 
     * succeeded, and all responses are error free.
     */
    public static final BundleType TRANSACTION_RESPONSE = BundleType.builder().value(Value.TRANSACTION_RESPONSE).build();

    /**
     * Batch
     * 
     * <p>The bundle is a set of actions - intended to be processed by a server as a group of independent actions.
     */
    public static final BundleType BATCH = BundleType.builder().value(Value.BATCH).build();

    /**
     * Batch Response
     * 
     * <p>The bundle is a batch response. Note that as a batch, some responses may indicate failure and others success.
     */
    public static final BundleType BATCH_RESPONSE = BundleType.builder().value(Value.BATCH_RESPONSE).build();

    /**
     * History List
     * 
     * <p>The bundle is a list of resources from a history interaction on a server.
     */
    public static final BundleType HISTORY = BundleType.builder().value(Value.HISTORY).build();

    /**
     * Search Results
     * 
     * <p>The bundle is a list of resources returned as a result of a search/query interaction, operation, or message.
     */
    public static final BundleType SEARCHSET = BundleType.builder().value(Value.SEARCHSET).build();

    /**
     * Collection
     * 
     * <p>The bundle is a set of resources collected into a single package for ease of distribution that imposes no 
     * processing obligations or behavioral rules beyond persistence.
     */
    public static final BundleType COLLECTION = BundleType.builder().value(Value.COLLECTION).build();

    private volatile int hashCode;

    private BundleType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this BundleType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating BundleType objects from a passed enum value.
     */
    public static BundleType of(Value value) {
        switch (value) {
        case DOCUMENT:
            return DOCUMENT;
        case MESSAGE:
            return MESSAGE;
        case TRANSACTION:
            return TRANSACTION;
        case TRANSACTION_RESPONSE:
            return TRANSACTION_RESPONSE;
        case BATCH:
            return BATCH;
        case BATCH_RESPONSE:
            return BATCH_RESPONSE;
        case HISTORY:
            return HISTORY;
        case SEARCHSET:
            return SEARCHSET;
        case COLLECTION:
            return COLLECTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating BundleType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static BundleType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating BundleType objects from a passed string value.
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
     * Inherited factory method for creating BundleType objects from a passed string value.
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
        BundleType other = (BundleType) obj;
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
         *     An enum constant for BundleType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public BundleType build() {
            BundleType bundleType = new BundleType(this);
            if (validating) {
                validate(bundleType);
            }
            return bundleType;
        }

        protected void validate(BundleType bundleType) {
            super.validate(bundleType);
        }

        protected Builder from(BundleType bundleType) {
            super.from(bundleType);
            return this;
        }
    }

    public enum Value {
        /**
         * Document
         * 
         * <p>The bundle is a document. The first resource is a Composition.
         */
        DOCUMENT("document"),

        /**
         * Message
         * 
         * <p>The bundle is a message. The first resource is a MessageHeader.
         */
        MESSAGE("message"),

        /**
         * Transaction
         * 
         * <p>The bundle is a transaction - intended to be processed by a server as an atomic commit.
         */
        TRANSACTION("transaction"),

        /**
         * Transaction Response
         * 
         * <p>The bundle is a transaction response. Because the response is a transaction response, the transaction has 
         * succeeded, and all responses are error free.
         */
        TRANSACTION_RESPONSE("transaction-response"),

        /**
         * Batch
         * 
         * <p>The bundle is a set of actions - intended to be processed by a server as a group of independent actions.
         */
        BATCH("batch"),

        /**
         * Batch Response
         * 
         * <p>The bundle is a batch response. Note that as a batch, some responses may indicate failure and others success.
         */
        BATCH_RESPONSE("batch-response"),

        /**
         * History List
         * 
         * <p>The bundle is a list of resources from a history interaction on a server.
         */
        HISTORY("history"),

        /**
         * Search Results
         * 
         * <p>The bundle is a list of resources returned as a result of a search/query interaction, operation, or message.
         */
        SEARCHSET("searchset"),

        /**
         * Collection
         * 
         * <p>The bundle is a set of resources collected into a single package for ease of distribution that imposes no 
         * processing obligations or behavioral rules beyond persistence.
         */
        COLLECTION("collection");

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
         * Factory method for creating BundleType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding BundleType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "document":
                return DOCUMENT;
            case "message":
                return MESSAGE;
            case "transaction":
                return TRANSACTION;
            case "transaction-response":
                return TRANSACTION_RESPONSE;
            case "batch":
                return BATCH;
            case "batch-response":
                return BATCH_RESPONSE;
            case "history":
                return HISTORY;
            case "searchset":
                return SEARCHSET;
            case "collection":
                return COLLECTION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
