/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class StructureMapModelMode extends Code {
    /**
     * Source Structure Definition
     */
    public static final StructureMapModelMode SOURCE = StructureMapModelMode.of(ValueSet.SOURCE);

    /**
     * Queried Structure Definition
     */
    public static final StructureMapModelMode QUERIED = StructureMapModelMode.of(ValueSet.QUERIED);

    /**
     * Target Structure Definition
     */
    public static final StructureMapModelMode TARGET = StructureMapModelMode.of(ValueSet.TARGET);

    /**
     * Produced Structure Definition
     */
    public static final StructureMapModelMode PRODUCED = StructureMapModelMode.of(ValueSet.PRODUCED);

    private volatile int hashCode;

    private StructureMapModelMode(Builder builder) {
        super(builder);
    }

    public static StructureMapModelMode of(java.lang.String value) {
        return StructureMapModelMode.builder().value(value).build();
    }

    public static StructureMapModelMode of(ValueSet value) {
        return StructureMapModelMode.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return StructureMapModelMode.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return StructureMapModelMode.builder().value(value).build();
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
        StructureMapModelMode other = (StructureMapModelMode) obj;
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
        public StructureMapModelMode build() {
            return new StructureMapModelMode(this);
        }
    }

    public enum ValueSet {
        /**
         * Source Structure Definition
         */
        SOURCE("source"),

        /**
         * Queried Structure Definition
         */
        QUERIED("queried"),

        /**
         * Target Structure Definition
         */
        TARGET("target"),

        /**
         * Produced Structure Definition
         */
        PRODUCED("produced");

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
