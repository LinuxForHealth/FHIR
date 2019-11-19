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
public class NameUse extends Code {
    /**
     * Usual
     */
    public static final NameUse USUAL = NameUse.builder().value(ValueSet.USUAL).build();

    /**
     * Official
     */
    public static final NameUse OFFICIAL = NameUse.builder().value(ValueSet.OFFICIAL).build();

    /**
     * Temp
     */
    public static final NameUse TEMP = NameUse.builder().value(ValueSet.TEMP).build();

    /**
     * Nickname
     */
    public static final NameUse NICKNAME = NameUse.builder().value(ValueSet.NICKNAME).build();

    /**
     * Anonymous
     */
    public static final NameUse ANONYMOUS = NameUse.builder().value(ValueSet.ANONYMOUS).build();

    /**
     * Old
     */
    public static final NameUse OLD = NameUse.builder().value(ValueSet.OLD).build();

    /**
     * Name changed for Marriage
     */
    public static final NameUse MAIDEN = NameUse.builder().value(ValueSet.MAIDEN).build();

    private volatile int hashCode;

    private NameUse(Builder builder) {
        super(builder);
    }

    public static NameUse of(ValueSet value) {
        switch (value) {
        case USUAL:
            return USUAL;
        case OFFICIAL:
            return OFFICIAL;
        case TEMP:
            return TEMP;
        case NICKNAME:
            return NICKNAME;
        case ANONYMOUS:
            return ANONYMOUS;
        case OLD:
            return OLD;
        case MAIDEN:
            return MAIDEN;
        default:
            throw new IllegalArgumentException(value.name());
        }
    }

    public static NameUse of(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.valueOf(value));
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
        NameUse other = (NameUse) obj;
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
        public NameUse build() {
            return new NameUse(this);
        }
    }

    public enum ValueSet {
        /**
         * Usual
         */
        USUAL("usual"),

        /**
         * Official
         */
        OFFICIAL("official"),

        /**
         * Temp
         */
        TEMP("temp"),

        /**
         * Nickname
         */
        NICKNAME("nickname"),

        /**
         * Anonymous
         */
        ANONYMOUS("anonymous"),

        /**
         * Old
         */
        OLD("old"),

        /**
         * Name changed for Marriage
         */
        MAIDEN("maiden");

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
