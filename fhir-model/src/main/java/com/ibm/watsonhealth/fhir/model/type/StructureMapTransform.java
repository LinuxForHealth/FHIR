/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class StructureMapTransform extends Code {
    /**
     * create
     */
    public static final StructureMapTransform CREATE = StructureMapTransform.of(ValueSet.CREATE);

    /**
     * copy
     */
    public static final StructureMapTransform COPY = StructureMapTransform.of(ValueSet.COPY);

    /**
     * truncate
     */
    public static final StructureMapTransform TRUNCATE = StructureMapTransform.of(ValueSet.TRUNCATE);

    /**
     * escape
     */
    public static final StructureMapTransform ESCAPE = StructureMapTransform.of(ValueSet.ESCAPE);

    /**
     * cast
     */
    public static final StructureMapTransform CAST = StructureMapTransform.of(ValueSet.CAST);

    /**
     * append
     */
    public static final StructureMapTransform APPEND = StructureMapTransform.of(ValueSet.APPEND);

    /**
     * translate
     */
    public static final StructureMapTransform TRANSLATE = StructureMapTransform.of(ValueSet.TRANSLATE);

    /**
     * reference
     */
    public static final StructureMapTransform REFERENCE = StructureMapTransform.of(ValueSet.REFERENCE);

    /**
     * dateOp
     */
    public static final StructureMapTransform DATE_OP = StructureMapTransform.of(ValueSet.DATE_OP);

    /**
     * uuid
     */
    public static final StructureMapTransform UUID = StructureMapTransform.of(ValueSet.UUID);

    /**
     * pointer
     */
    public static final StructureMapTransform POINTER = StructureMapTransform.of(ValueSet.POINTER);

    /**
     * evaluate
     */
    public static final StructureMapTransform EVALUATE = StructureMapTransform.of(ValueSet.EVALUATE);

    /**
     * cc
     */
    public static final StructureMapTransform CC = StructureMapTransform.of(ValueSet.CC);

    /**
     * c
     */
    public static final StructureMapTransform C = StructureMapTransform.of(ValueSet.C);

    /**
     * qty
     */
    public static final StructureMapTransform QTY = StructureMapTransform.of(ValueSet.QTY);

    /**
     * id
     */
    public static final StructureMapTransform ID = StructureMapTransform.of(ValueSet.ID);

    /**
     * cp
     */
    public static final StructureMapTransform CP = StructureMapTransform.of(ValueSet.CP);

    private StructureMapTransform(Builder builder) {
        super(builder);
    }

    public static StructureMapTransform of(java.lang.String value) {
        return StructureMapTransform.builder().value(value).build();
    }

    public static StructureMapTransform of(ValueSet value) {
        return StructureMapTransform.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return StructureMapTransform.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return StructureMapTransform.builder().value(value).build();
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
        public StructureMapTransform build() {
            return new StructureMapTransform(this);
        }
    }

    public enum ValueSet {
        /**
         * create
         */
        CREATE("create"),

        /**
         * copy
         */
        COPY("copy"),

        /**
         * truncate
         */
        TRUNCATE("truncate"),

        /**
         * escape
         */
        ESCAPE("escape"),

        /**
         * cast
         */
        CAST("cast"),

        /**
         * append
         */
        APPEND("append"),

        /**
         * translate
         */
        TRANSLATE("translate"),

        /**
         * reference
         */
        REFERENCE("reference"),

        /**
         * dateOp
         */
        DATE_OP("dateOp"),

        /**
         * uuid
         */
        UUID("uuid"),

        /**
         * pointer
         */
        POINTER("pointer"),

        /**
         * evaluate
         */
        EVALUATE("evaluate"),

        /**
         * cc
         */
        CC("cc"),

        /**
         * c
         */
        C("c"),

        /**
         * qty
         */
        QTY("qty"),

        /**
         * id
         */
        ID("id"),

        /**
         * cp
         */
        CP("cp");

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
