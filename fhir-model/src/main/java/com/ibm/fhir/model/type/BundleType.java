/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class BundleType extends Code {
    /**
     * Document
     */
    public static final BundleType DOCUMENT = BundleType.of(ValueSet.DOCUMENT);

    /**
     * Message
     */
    public static final BundleType MESSAGE = BundleType.of(ValueSet.MESSAGE);

    /**
     * Transaction
     */
    public static final BundleType TRANSACTION = BundleType.of(ValueSet.TRANSACTION);

    /**
     * Transaction Response
     */
    public static final BundleType TRANSACTION_RESPONSE = BundleType.of(ValueSet.TRANSACTION_RESPONSE);

    /**
     * Batch
     */
    public static final BundleType BATCH = BundleType.of(ValueSet.BATCH);

    /**
     * Batch Response
     */
    public static final BundleType BATCH_RESPONSE = BundleType.of(ValueSet.BATCH_RESPONSE);

    /**
     * History List
     */
    public static final BundleType HISTORY = BundleType.of(ValueSet.HISTORY);

    /**
     * Search Results
     */
    public static final BundleType SEARCHSET = BundleType.of(ValueSet.SEARCHSET);

    /**
     * Collection
     */
    public static final BundleType COLLECTION = BundleType.of(ValueSet.COLLECTION);

    private volatile int hashCode;

    private BundleType(Builder builder) {
        super(builder);
    }

    public static BundleType of(java.lang.String value) {
        return BundleType.builder().value(value).build();
    }

    public static BundleType of(ValueSet value) {
        return BundleType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return BundleType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return BundleType.builder().value(value).build();
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
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
