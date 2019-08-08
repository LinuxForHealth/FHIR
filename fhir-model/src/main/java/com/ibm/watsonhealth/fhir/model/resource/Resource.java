/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.builder.AbstractBuilder;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.AbstractVisitable;

/**
 * This is the base resource type for everything.
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public abstract class Resource extends AbstractVisitable {
    protected final Id id;
    protected final Meta meta;
    protected final Uri implicitRules;
    protected final Code language;

    protected Resource(Builder builder) {
        id = builder.id;
        meta = builder.meta;
        implicitRules = builder.implicitRules;
        language = builder.language;
    }

    /**
     * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
     * 
     * @return
     *     An immutable object of type {@link Id}.
     */
    public Id getId() {
        return id;
    }

    /**
     * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
     * might not always be associated with version changes to the resource.
     * 
     * @return
     *     An immutable object of type {@link Meta}.
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
     * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
     * with other profiles etc.
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getImplicitRules() {
        return implicitRules;
    }

    /**
     * The base language in which the resource is written.
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getLanguage() {
        return language;
    }

    public <T extends Resource> boolean is(Class<T> resourceType) {
        return resourceType.isInstance(this);
    }

    public <T extends Resource> T as(Class<T> resourceType) {
        return resourceType.cast(this);
    }

    public abstract Builder toBuilder();

    public static abstract class Builder extends AbstractBuilder<Resource> {
        protected Id id;
        protected Meta meta;
        protected Uri implicitRules;
        protected Code language;

        protected Builder() {
            super();
        }

        /**
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder id(Id id) {
            this.id = id;
            return this;
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder meta(Meta meta) {
            this.meta = meta;
            return this;
        }

        /**
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder implicitRules(Uri implicitRules) {
            this.implicitRules = implicitRules;
            return this;
        }

        /**
         * The base language in which the resource is written.
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder language(Code language) {
            this.language = language;
            return this;
        }

        @Override
        public abstract Resource build();

        protected Builder from(Resource resource) {
            id = resource.id;
            meta = resource.meta;
            implicitRules = resource.implicitRules;
            language = resource.language;
            return this;
        }
    }
}
