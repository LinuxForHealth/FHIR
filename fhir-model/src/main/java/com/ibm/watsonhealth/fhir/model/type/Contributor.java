/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.ContributorType;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Contributor extends Element {
    private final ContributorType type;
    private final String name;
    private final List<ContactDetail> contact;

    private Contributor(Builder builder) {
        super(builder);
        this.type = ValidationSupport.requireNonNull(builder.type, "type");
        this.name = ValidationSupport.requireNonNull(builder.name, "name");
        this.contact = builder.contact;
    }

    /**
     * <p>
     * The type of contributor.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ContributorType}.
     */
    public ContributorType getType() {
        return type;
    }

    /**
     * <p>
     * The name of the individual or organization responsible for the contribution.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Contact details to assist a user in finding and communicating with the contributor.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(type, "type", visitor);
                accept(name, "name", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(type, name);
        builder.id = id;
        builder.extension.addAll(extension);
        builder.contact.addAll(contact);
        return builder;
    }

    public static Builder builder(ContributorType type, String name) {
        return new Builder(type, name);
    }

    public static class Builder extends Element.Builder {
        // required
        private final ContributorType type;
        private final String name;

        // optional
        private List<ContactDetail> contact = new ArrayList<>();

        private Builder(ContributorType type, String name) {
            super();
            this.type = type;
            this.name = name;
        }

        /**
         * <p>
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * </p>
         * 
         * @param id
         *     Unique id for inter-element referencing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the contributor.
         * </p>
         * 
         * @param contact
         *     Contact details of the contributor
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the contributor.
         * </p>
         * 
         * @param contact
         *     Contact details of the contributor
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact.addAll(contact);
            return this;
        }

        @Override
        public Contributor build() {
            return new Contributor(this);
        }
    }
}
