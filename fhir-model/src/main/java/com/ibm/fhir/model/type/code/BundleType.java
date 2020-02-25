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

@System("http://hl7.org/fhir/bundle-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class BundleType extends Code {
    /**
     * Document
     */
    public static final BundleType DOCUMENT = BundleType.builder().value(ValueSet.DOCUMENT).build();

    /**
     * Message
     */
    public static final BundleType MESSAGE = BundleType.builder().value(ValueSet.MESSAGE).build();

    /**
     * Transaction
     */
    public static final BundleType TRANSACTION = BundleType.builder().value(ValueSet.TRANSACTION).build();

    /**
     * Transaction Response
     */
    public static final BundleType TRANSACTION_RESPONSE = BundleType.builder().value(ValueSet.TRANSACTION_RESPONSE).build();

    /**
     * Batch
     */
    public static final BundleType BATCH = BundleType.builder().value(ValueSet.BATCH).build();

    /**
     * Batch Response
     */
    public static final BundleType BATCH_RESPONSE = BundleType.builder().value(ValueSet.BATCH_RESPONSE).build();

    /**
     * History List
     */
    public static final BundleType HISTORY = BundleType.builder().value(ValueSet.HISTORY).build();

    /**
     * Search Results
     */
    public static final BundleType SEARCHSET = BundleType.builder().value(ValueSet.SEARCHSET).build();

    /**
     * Collection
     */
    public static final BundleType COLLECTION = BundleType.builder().value(ValueSet.COLLECTION).build();

    private volatile int hashCode;

    private BundleType(Builder builder) {
        super(builder);
    }

    public static BundleType of(ValueSet value) {
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

    public static BundleType of(java.lang.String value) {
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
        public BundleType build() {
            return new BundleType(this);
        }
    }

    public enum ValueSet {
        /**
         * Document
         */
        DOCUMENT("document"),

        /**
         * Message
         */
        MESSAGE("message"),

        /**
         * Transaction
         */
        TRANSACTION("transaction"),

        /**
         * Transaction Response
         */
        TRANSACTION_RESPONSE("transaction-response"),

        /**
         * Batch
         */
        BATCH("batch"),

        /**
         * Batch Response
         */
        BATCH_RESPONSE("batch-response"),

        /**
         * History List
         */
        HISTORY("history"),

        /**
         * Search Results
         */
        SEARCHSET("searchset"),

        /**
         * Collection
         */
        COLLECTION("collection");

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
