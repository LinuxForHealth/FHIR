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
public class FHIRVersion extends Code {
    /**
     * 0.01
     */
    public static final FHIRVersion VERSION_0_01 = FHIRVersion.of(ValueSet.VERSION_0_01);

    /**
     * 0.05
     */
    public static final FHIRVersion VERSION_0_05 = FHIRVersion.of(ValueSet.VERSION_0_05);

    /**
     * 0.06
     */
    public static final FHIRVersion VERSION_0_06 = FHIRVersion.of(ValueSet.VERSION_0_06);

    /**
     * 0.11
     */
    public static final FHIRVersion VERSION_0_11 = FHIRVersion.of(ValueSet.VERSION_0_11);

    /**
     * 0.0.80
     */
    public static final FHIRVersion VERSION_0_0_80 = FHIRVersion.of(ValueSet.VERSION_0_0_80);

    /**
     * 0.0.81
     */
    public static final FHIRVersion VERSION_0_0_81 = FHIRVersion.of(ValueSet.VERSION_0_0_81);

    /**
     * 0.0.82
     */
    public static final FHIRVersion VERSION_0_0_82 = FHIRVersion.of(ValueSet.VERSION_0_0_82);

    /**
     * 0.4.0
     */
    public static final FHIRVersion VERSION_0_4_0 = FHIRVersion.of(ValueSet.VERSION_0_4_0);

    /**
     * 0.5.0
     */
    public static final FHIRVersion VERSION_0_5_0 = FHIRVersion.of(ValueSet.VERSION_0_5_0);

    /**
     * 1.0.0
     */
    public static final FHIRVersion VERSION_1_0_0 = FHIRVersion.of(ValueSet.VERSION_1_0_0);

    /**
     * 1.0.1
     */
    public static final FHIRVersion VERSION_1_0_1 = FHIRVersion.of(ValueSet.VERSION_1_0_1);

    /**
     * 1.0.2
     */
    public static final FHIRVersion VERSION_1_0_2 = FHIRVersion.of(ValueSet.VERSION_1_0_2);

    /**
     * 1.1.0
     */
    public static final FHIRVersion VERSION_1_1_0 = FHIRVersion.of(ValueSet.VERSION_1_1_0);

    /**
     * 1.4.0
     */
    public static final FHIRVersion VERSION_1_4_0 = FHIRVersion.of(ValueSet.VERSION_1_4_0);

    /**
     * 1.6.0
     */
    public static final FHIRVersion VERSION_1_6_0 = FHIRVersion.of(ValueSet.VERSION_1_6_0);

    /**
     * 1.8.0
     */
    public static final FHIRVersion VERSION_1_8_0 = FHIRVersion.of(ValueSet.VERSION_1_8_0);

    /**
     * 3.0.0
     */
    public static final FHIRVersion VERSION_3_0_0 = FHIRVersion.of(ValueSet.VERSION_3_0_0);

    /**
     * 3.0.1
     */
    public static final FHIRVersion VERSION_3_0_1 = FHIRVersion.of(ValueSet.VERSION_3_0_1);

    /**
     * 3.3.0
     */
    public static final FHIRVersion VERSION_3_3_0 = FHIRVersion.of(ValueSet.VERSION_3_3_0);

    /**
     * 3.5.0
     */
    public static final FHIRVersion VERSION_3_5_0 = FHIRVersion.of(ValueSet.VERSION_3_5_0);

    /**
     * 4.0.0
     */
    public static final FHIRVersion VERSION_4_0_0 = FHIRVersion.of(ValueSet.VERSION_4_0_0);

    private volatile int hashCode;

    private FHIRVersion(Builder builder) {
        super(builder);
    }

    public static FHIRVersion of(java.lang.String value) {
        return FHIRVersion.builder().value(value).build();
    }

    public static FHIRVersion of(ValueSet value) {
        return FHIRVersion.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return FHIRVersion.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return FHIRVersion.builder().value(value).build();
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
        FHIRVersion other = (FHIRVersion) obj;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public FHIRVersion build() {
            return new FHIRVersion(this);
        }
    }

    public enum ValueSet {
        /**
         * 0.01
         */
        VERSION_0_01("0.01"),

        /**
         * 0.05
         */
        VERSION_0_05("0.05"),

        /**
         * 0.06
         */
        VERSION_0_06("0.06"),

        /**
         * 0.11
         */
        VERSION_0_11("0.11"),

        /**
         * 0.0.80
         */
        VERSION_0_0_80("0.0.80"),

        /**
         * 0.0.81
         */
        VERSION_0_0_81("0.0.81"),

        /**
         * 0.0.82
         */
        VERSION_0_0_82("0.0.82"),

        /**
         * 0.4.0
         */
        VERSION_0_4_0("0.4.0"),

        /**
         * 0.5.0
         */
        VERSION_0_5_0("0.5.0"),

        /**
         * 1.0.0
         */
        VERSION_1_0_0("1.0.0"),

        /**
         * 1.0.1
         */
        VERSION_1_0_1("1.0.1"),

        /**
         * 1.0.2
         */
        VERSION_1_0_2("1.0.2"),

        /**
         * 1.1.0
         */
        VERSION_1_1_0("1.1.0"),

        /**
         * 1.4.0
         */
        VERSION_1_4_0("1.4.0"),

        /**
         * 1.6.0
         */
        VERSION_1_6_0("1.6.0"),

        /**
         * 1.8.0
         */
        VERSION_1_8_0("1.8.0"),

        /**
         * 3.0.0
         */
        VERSION_3_0_0("3.0.0"),

        /**
         * 3.0.1
         */
        VERSION_3_0_1("3.0.1"),

        /**
         * 3.3.0
         */
        VERSION_3_3_0("3.3.0"),

        /**
         * 3.5.0
         */
        VERSION_3_5_0("3.5.0"),

        /**
         * 4.0.0
         */
        VERSION_4_0_0("4.0.0");

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
