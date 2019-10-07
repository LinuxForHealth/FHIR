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
public class AdverseEventActuality extends Code {
    /**
     * Adverse Event
     */
    public static final AdverseEventActuality ACTUAL = AdverseEventActuality.of(ValueSet.ACTUAL);

    /**
     * Potential Adverse Event
     */
    public static final AdverseEventActuality POTENTIAL = AdverseEventActuality.of(ValueSet.POTENTIAL);

    private volatile int hashCode;

    private AdverseEventActuality(Builder builder) {
        super(builder);
    }

    public static AdverseEventActuality of(java.lang.String value) {
        return AdverseEventActuality.builder().value(value).build();
    }

    public static AdverseEventActuality of(ValueSet value) {
        return AdverseEventActuality.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return AdverseEventActuality.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return AdverseEventActuality.builder().value(value).build();
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
        AdverseEventActuality other = (AdverseEventActuality) obj;
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
        public AdverseEventActuality build() {
            return new AdverseEventActuality(this);
        }
    }

    public enum ValueSet {
        /**
         * Adverse Event
         */
        ACTUAL("actual"),

        /**
         * Potential Adverse Event
         */
        POTENTIAL("potential");

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
