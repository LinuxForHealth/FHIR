/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.api;


/**
 * Used to control how the schema gets applied
 */
public class SchemaApplyContext {
    private final boolean includeForeignKeys;

    /**
     * Protected constructor
     * @param includeForeignKeys
     */
    protected SchemaApplyContext(boolean includeForeignKeys) {
        this.includeForeignKeys = includeForeignKeys;
    }
    public boolean isIncludeForeignKeys() {
        return this.includeForeignKeys;
    }

    /**
     * Create a new {@link Builder} instance
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link SchemaApplyContext}
     */
    public static class Builder {
        private boolean includeForeignKeys;

        /**
         * Setter for includeForeignKeys
         * @param flag
         * @return
         */
        public Builder setIncludeForeignKeys(boolean flag) {
            this.includeForeignKeys = flag;
            return this;
        }
        /**
         * Build an immutable instance of {@link SchemaApplyContext} using
         * the current state of this
         * @return
         */
        public SchemaApplyContext build() {
            return new SchemaApplyContext(this.includeForeignKeys);
        }
    }

    /**
     * Get a default instance of the schema apply context
     * @return
     */
    public static SchemaApplyContext getDefault() {
        return new SchemaApplyContext(true);
    }
}
