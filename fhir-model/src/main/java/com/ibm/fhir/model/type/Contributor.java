/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ContributorType;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Contributor extends Element {
    @Summary
    @Binding(
        bindingName = "ContributorType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The type of contributor.",
        valueSet = "http://hl7.org/fhir/ValueSet/contributor-type|4.1.0"
    )
    @Required
    private final ContributorType type;
    @Summary
    @Required
    private final String name;
    @Summary
    private final List<ContactDetail> contact;

    private Contributor(Builder builder) {
        super(builder);
        type = builder.type;
        name = builder.name;
        contact = Collections.unmodifiableList(builder.contact);
    }

    /**
     * The type of contributor.
     * 
     * @return
     *     An immutable object of type {@link ContributorType} that is non-null.
     */
    public ContributorType getType() {
        return type;
    }

    /**
     * The name of the individual or organization responsible for the contribution.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getName() {
        return name;
    }

    /**
     * Contact details to assist a user in finding and communicating with the contributor.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (type != null) || 
            (name != null) || 
            !contact.isEmpty();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(type, "type", visitor);
                accept(name, "name", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
            }
            visitor.visitEnd(elementName, elementIndex, this);
            visitor.postVisit(this);
        }
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
        Contributor other = (Contributor) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(type, other.type) && 
            Objects.equals(name, other.name) && 
            Objects.equals(contact, other.contact);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                type, 
                name, 
                contact);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        private ContributorType type;
        private String name;
        private List<ContactDetail> contact = new ArrayList<>();

        private Builder() {
            super();
        }

        /**
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * 
         * @param id
         *     Unique id for inter-element referencing
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * The type of contributor.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     author | editor | reviewer | endorser
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(ContributorType type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Who contributed the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(com.ibm.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * The name of the individual or organization responsible for the contribution.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Who contributed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the contributor.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details of the contributor
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the contributor.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details of the contributor
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * Build the {@link Contributor}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * <li>name</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Contributor}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Contributor per the base specification
         */
        @Override
        public Contributor build() {
            Contributor contributor = new Contributor(this);
            if (validating) {
                validate(contributor);
            }
            return contributor;
        }

        protected void validate(Contributor contributor) {
            super.validate(contributor);
            ValidationSupport.requireNonNull(contributor.type, "type");
            ValidationSupport.requireNonNull(contributor.name, "name");
            ValidationSupport.checkList(contributor.contact, "contact", ContactDetail.class);
            ValidationSupport.requireValueOrChildren(contributor);
        }

        protected Builder from(Contributor contributor) {
            super.from(contributor);
            type = contributor.type;
            name = contributor.name;
            contact.addAll(contributor.contact);
            return this;
        }
    }
}
