/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class RepositoryType extends Code {
    /**
     * Click and see
     */
    public static final RepositoryType DIRECTLINK = RepositoryType.of(ValueSet.DIRECTLINK);

    /**
     * The URL is the RESTful or other kind of API that can access to the result.
     */
    public static final RepositoryType OPENAPI = RepositoryType.of(ValueSet.OPENAPI);

    /**
     * Result cannot be access unless an account is logged in
     */
    public static final RepositoryType LOGIN = RepositoryType.of(ValueSet.LOGIN);

    /**
     * Result need to be fetched with API and need LOGIN( or cookies are required when visiting the link of resource)
     */
    public static final RepositoryType OAUTH = RepositoryType.of(ValueSet.OAUTH);

    /**
     * Some other complicated or particular way to get resource from URL.
     */
    public static final RepositoryType OTHER = RepositoryType.of(ValueSet.OTHER);

    private RepositoryType(Builder builder) {
        super(builder);
    }

    public static RepositoryType of(java.lang.String value) {
        return RepositoryType.builder().value(value).build();
    }

    public static RepositoryType of(ValueSet value) {
        return RepositoryType.builder().value(value).build();
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
        public RepositoryType build() {
            return new RepositoryType(this);
        }
    }

    public enum ValueSet {
        /**
         * Click and see
         */
        DIRECTLINK("directlink"),

        /**
         * The URL is the RESTful or other kind of API that can access to the result.
         */
        OPENAPI("openapi"),

        /**
         * Result cannot be access unless an account is logged in
         */
        LOGIN("login"),

        /**
         * Result need to be fetched with API and need LOGIN( or cookies are required when visiting the link of resource)
         */
        OAUTH("oauth"),

        /**
         * Some other complicated or particular way to get resource from URL.
         */
        OTHER("other");

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
