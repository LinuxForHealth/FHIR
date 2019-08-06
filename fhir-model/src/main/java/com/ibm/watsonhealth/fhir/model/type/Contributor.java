/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    private volatile int hashCode;

    private Contributor(Builder builder) {
        super(builder);
        type = ValidationSupport.requireNonNull(builder.type, "type");
        name = ValidationSupport.requireNonNull(builder.name, "name");
        contact = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.contact, "contact"));
        ValidationSupport.requireValueOrChildren(this);
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
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
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
         * <p>
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * The type of contributor.
         * </p>
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
         * <p>
         * The name of the individual or organization responsible for the contribution.
         * </p>
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
         * <p>
         * Contact details to assist a user in finding and communicating with the contributor.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Contact details to assist a user in finding and communicating with the contributor.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact details of the contributor
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        @Override
        public Contributor build() {
            return new Contributor(this);
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
