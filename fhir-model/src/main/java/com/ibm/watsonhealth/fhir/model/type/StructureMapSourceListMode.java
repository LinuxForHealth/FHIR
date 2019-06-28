/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class StructureMapSourceListMode extends Code {
    /**
     * First
     */
    public static final StructureMapSourceListMode FIRST = StructureMapSourceListMode.of(ValueSet.FIRST);

    /**
     * All but the first
     */
    public static final StructureMapSourceListMode NOT_FIRST = StructureMapSourceListMode.of(ValueSet.NOT_FIRST);

    /**
     * Last
     */
    public static final StructureMapSourceListMode LAST = StructureMapSourceListMode.of(ValueSet.LAST);

    /**
     * All but the last
     */
    public static final StructureMapSourceListMode NOT_LAST = StructureMapSourceListMode.of(ValueSet.NOT_LAST);

    /**
     * Enforce only one
     */
    public static final StructureMapSourceListMode ONLY_ONE = StructureMapSourceListMode.of(ValueSet.ONLY_ONE);

    private StructureMapSourceListMode(Builder builder) {
        super(builder);
    }

    public static StructureMapSourceListMode of(java.lang.String value) {
        return StructureMapSourceListMode.builder().value(value).build();
    }

    public static StructureMapSourceListMode of(ValueSet value) {
        return StructureMapSourceListMode.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return StructureMapSourceListMode.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return StructureMapSourceListMode.builder().value(value).build();
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
        public StructureMapSourceListMode build() {
            return new StructureMapSourceListMode(this);
        }
    }

    public enum ValueSet {
        /**
         * First
         */
        FIRST("first"),

        /**
         * All but the first
         */
        NOT_FIRST("not_first"),

        /**
         * Last
         */
        LAST("last"),

        /**
         * All but the last
         */
        NOT_LAST("not_last"),

        /**
         * Enforce only one
         */
        ONLY_ONE("only_one");

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
