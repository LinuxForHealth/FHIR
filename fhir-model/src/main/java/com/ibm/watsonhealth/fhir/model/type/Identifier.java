/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.IdentifierUse;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Identifier extends Element {
    private final IdentifierUse use;
    private final CodeableConcept type;
    private final Uri system;
    private final String value;
    private final Period period;
    private final Reference assigner;

    private volatile int hashCode;

    private Identifier(Builder builder) {
        super(builder);
        use = builder.use;
        type = builder.type;
        system = builder.system;
        value = builder.value;
        period = builder.period;
        assigner = builder.assigner;
        if (!hasChildren()) {
            throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
        }
    }

    /**
     * <p>
     * The purpose of this identifier.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link IdentifierUse}.
     */
    public IdentifierUse getUse() {
        return use;
    }

    /**
     * <p>
     * A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * <p>
     * Establishes the namespace for the value - that is, a URL that describes a set values that are unique.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getSystem() {
        return system;
    }

    /**
     * <p>
     * The portion of the identifier typically relevant to the user and which is unique within the context of the system.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>
     * Time period during which identifier is/was valid for use.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * <p>
     * Organization that issued/manages the identifier.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAssigner() {
        return assigner;
    }

    @Override
    protected boolean hasChildren() {
        return super.hasChildren() || 
            (use != null) || 
            (type != null) || 
            (system != null) || 
            (value != null) || 
            (period != null) || 
            (assigner != null);
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(use, "use", visitor);
                accept(type, "type", visitor);
                accept(system, "system", visitor);
                accept(value, "value", visitor);
                accept(period, "period", visitor);
                accept(assigner, "assigner", visitor);
            }
            visitor.visitEnd(elementName, this);
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
        Identifier other = (Identifier) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(use, other.use) && 
            Objects.equals(type, other.type) && 
            Objects.equals(system, other.system) && 
            Objects.equals(value, other.value) && 
            Objects.equals(period, other.period) && 
            Objects.equals(assigner, other.assigner);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                use, 
                type, 
                system, 
                value, 
                period, 
                assigner);
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
        // optional
        private IdentifierUse use;
        private CodeableConcept type;
        private Uri system;
        private String value;
        private Period period;
        private Reference assigner;

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
         * Adds new element(s) to the existing list
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
         * The purpose of this identifier.
         * </p>
         * 
         * @param use
         *     usual | official | temp | secondary | old (If known)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder use(IdentifierUse use) {
            this.use = use;
            return this;
        }

        /**
         * <p>
         * A coded type for the identifier that can be used to determine which identifier to use for a specific purpose.
         * </p>
         * 
         * @param type
         *     Description of identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * Establishes the namespace for the value - that is, a URL that describes a set values that are unique.
         * </p>
         * 
         * @param system
         *     The namespace for the identifier value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder system(Uri system) {
            this.system = system;
            return this;
        }

        /**
         * <p>
         * The portion of the identifier typically relevant to the user and which is unique within the context of the system.
         * </p>
         * 
         * @param value
         *     The value that is unique
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * <p>
         * Time period during which identifier is/was valid for use.
         * </p>
         * 
         * @param period
         *     Time period when id is/was valid for use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * <p>
         * Organization that issued/manages the identifier.
         * </p>
         * 
         * @param assigner
         *     Organization that issued id (may be just text)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder assigner(Reference assigner) {
            this.assigner = assigner;
            return this;
        }

        @Override
        public Identifier build() {
            return new Identifier(this);
        }

        private Builder from(Identifier identifier) {
            id = identifier.id;
            extension.addAll(identifier.extension);
            use = identifier.use;
            type = identifier.type;
            system = identifier.system;
            value = identifier.value;
            period = identifier.period;
            assigner = identifier.assigner;
            return this;
        }
    }
}
