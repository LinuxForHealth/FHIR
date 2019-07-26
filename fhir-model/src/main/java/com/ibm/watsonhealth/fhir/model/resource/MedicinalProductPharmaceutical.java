/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A pharmaceutical product described in terms of its composition and dose form.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicinalProductPharmaceutical extends DomainResource {
    private final List<Identifier> identifier;
    private final CodeableConcept administrableDoseForm;
    private final CodeableConcept unitOfPresentation;
    private final List<Reference> ingredient;
    private final List<Reference> device;
    private final List<Characteristics> characteristics;
    private final List<RouteOfAdministration> routeOfAdministration;

    private volatile int hashCode;

    private MedicinalProductPharmaceutical(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        administrableDoseForm = ValidationSupport.requireNonNull(builder.administrableDoseForm, "administrableDoseForm");
        unitOfPresentation = builder.unitOfPresentation;
        ingredient = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.ingredient, "ingredient"));
        device = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.device, "device"));
        characteristics = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.characteristics, "characteristics"));
        routeOfAdministration = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.routeOfAdministration, "routeOfAdministration"));
    }

    /**
     * <p>
     * An identifier for the pharmaceutical medicinal product.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The administrable dose form, after necessary reconstitution.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getAdministrableDoseForm() {
        return administrableDoseForm;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getUnitOfPresentation() {
        return unitOfPresentation;
    }

    /**
     * <p>
     * Ingredient.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getIngredient() {
        return ingredient;
    }

    /**
     * <p>
     * Accompanying device.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDevice() {
        return device;
    }

    /**
     * <p>
     * Characteristics e.g. a products onset of action.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Characteristics}.
     */
    public List<Characteristics> getCharacteristics() {
        return characteristics;
    }

    /**
     * <p>
     * The path by which the pharmaceutical product is taken into or makes contact with the body.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RouteOfAdministration}.
     */
    public List<RouteOfAdministration> getRouteOfAdministration() {
        return routeOfAdministration;
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(administrableDoseForm, "administrableDoseForm", visitor);
                accept(unitOfPresentation, "unitOfPresentation", visitor);
                accept(ingredient, "ingredient", visitor, Reference.class);
                accept(device, "device", visitor, Reference.class);
                accept(characteristics, "characteristics", visitor, Characteristics.class);
                accept(routeOfAdministration, "routeOfAdministration", visitor, RouteOfAdministration.class);
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
        MedicinalProductPharmaceutical other = (MedicinalProductPharmaceutical) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(administrableDoseForm, other.administrableDoseForm) && 
            Objects.equals(unitOfPresentation, other.unitOfPresentation) && 
            Objects.equals(ingredient, other.ingredient) && 
            Objects.equals(device, other.device) && 
            Objects.equals(characteristics, other.characteristics) && 
            Objects.equals(routeOfAdministration, other.routeOfAdministration);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                administrableDoseForm, 
                unitOfPresentation, 
                ingredient, 
                device, 
                characteristics, 
                routeOfAdministration);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder(CodeableConcept administrableDoseForm, Collection<RouteOfAdministration> routeOfAdministration) {
        Builder builder = new Builder();
        builder.administrableDoseForm(administrableDoseForm);
        builder.routeOfAdministration(routeOfAdministration);
        return builder;
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept administrableDoseForm;
        private CodeableConcept unitOfPresentation;
        private List<Reference> ingredient = new ArrayList<>();
        private List<Reference> device = new ArrayList<>();
        private List<Characteristics> characteristics = new ArrayList<>();
        private List<RouteOfAdministration> routeOfAdministration = new ArrayList<>();

        /**
         * <p>
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * </p>
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(Id id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * </p>
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * <p>
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * </p>
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * <p>
         * The base language in which the resource is written.
         * </p>
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * <p>
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * </p>
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * An identifier for the pharmaceutical medicinal product.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     An identifier for the pharmaceutical medicinal product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An identifier for the pharmaceutical medicinal product.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     An identifier for the pharmaceutical medicinal product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * The administrable dose form, after necessary reconstitution.
         * </p>
         * 
         * @param administrableDoseForm
         *     The administrable dose form, after necessary reconstitution
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder administrableDoseForm(CodeableConcept administrableDoseForm) {
            this.administrableDoseForm = administrableDoseForm;
            return this;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @param unitOfPresentation
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder unitOfPresentation(CodeableConcept unitOfPresentation) {
            this.unitOfPresentation = unitOfPresentation;
            return this;
        }

        /**
         * <p>
         * Ingredient.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param ingredient
         *     Ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder ingredient(Reference... ingredient) {
            for (Reference value : ingredient) {
                this.ingredient.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Ingredient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param ingredient
         *     Ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder ingredient(Collection<Reference> ingredient) {
            this.ingredient = new ArrayList<>(ingredient);
            return this;
        }

        /**
         * <p>
         * Accompanying device.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param device
         *     Accompanying device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Reference... device) {
            for (Reference value : device) {
                this.device.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Accompanying device.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param device
         *     Accompanying device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Collection<Reference> device) {
            this.device = new ArrayList<>(device);
            return this;
        }

        /**
         * <p>
         * Characteristics e.g. a products onset of action.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param characteristics
         *     Characteristics e.g. a products onset of action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristics(Characteristics... characteristics) {
            for (Characteristics value : characteristics) {
                this.characteristics.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Characteristics e.g. a products onset of action.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param characteristics
         *     Characteristics e.g. a products onset of action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristics(Collection<Characteristics> characteristics) {
            this.characteristics = new ArrayList<>(characteristics);
            return this;
        }

        /**
         * <p>
         * The path by which the pharmaceutical product is taken into or makes contact with the body.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param routeOfAdministration
         *     The path by which the pharmaceutical product is taken into or makes contact with the body
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder routeOfAdministration(RouteOfAdministration... routeOfAdministration) {
            for (RouteOfAdministration value : routeOfAdministration) {
                this.routeOfAdministration.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The path by which the pharmaceutical product is taken into or makes contact with the body.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param routeOfAdministration
         *     The path by which the pharmaceutical product is taken into or makes contact with the body
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder routeOfAdministration(Collection<RouteOfAdministration> routeOfAdministration) {
            this.routeOfAdministration = new ArrayList<>(routeOfAdministration);
            return this;
        }

        @Override
        public MedicinalProductPharmaceutical build() {
            return new MedicinalProductPharmaceutical(this);
        }

        protected Builder from(MedicinalProductPharmaceutical medicinalProductPharmaceutical) {
            super.from(medicinalProductPharmaceutical);
            identifier.addAll(medicinalProductPharmaceutical.identifier);
            administrableDoseForm = medicinalProductPharmaceutical.administrableDoseForm;
            unitOfPresentation = medicinalProductPharmaceutical.unitOfPresentation;
            ingredient.addAll(medicinalProductPharmaceutical.ingredient);
            device.addAll(medicinalProductPharmaceutical.device);
            characteristics.addAll(medicinalProductPharmaceutical.characteristics);
            routeOfAdministration.addAll(medicinalProductPharmaceutical.routeOfAdministration);
            return this;
        }
    }

    /**
     * <p>
     * Characteristics e.g. a products onset of action.
     * </p>
     */
    public static class Characteristics extends BackboneElement {
        private final CodeableConcept code;
        private final CodeableConcept status;

        private volatile int hashCode;

        private Characteristics(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            status = builder.status;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A coded characteristic.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * <p>
         * The status of characteristic e.g. assigned or pending.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getStatus() {
            return status;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (status != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(status, "status", visitor);
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
            Characteristics other = (Characteristics) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(status, other.status);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    status);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder(CodeableConcept code) {
            Builder builder = new Builder();
            builder.code(code);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private CodeableConcept status;

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * A coded characteristic.
             * </p>
             * 
             * @param code
             *     A coded characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * <p>
             * The status of characteristic e.g. assigned or pending.
             * </p>
             * 
             * @param status
             *     The status of characteristic e.g. assigned or pending
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(CodeableConcept status) {
                this.status = status;
                return this;
            }

            @Override
            public Characteristics build() {
                return new Characteristics(this);
            }

            protected Builder from(Characteristics characteristics) {
                super.from(characteristics);
                code = characteristics.code;
                status = characteristics.status;
                return this;
            }
        }
    }

    /**
     * <p>
     * The path by which the pharmaceutical product is taken into or makes contact with the body.
     * </p>
     */
    public static class RouteOfAdministration extends BackboneElement {
        private final CodeableConcept code;
        private final Quantity firstDose;
        private final Quantity maxSingleDose;
        private final Quantity maxDosePerDay;
        private final Ratio maxDosePerTreatmentPeriod;
        private final Duration maxTreatmentPeriod;
        private final List<TargetSpecies> targetSpecies;

        private volatile int hashCode;

        private RouteOfAdministration(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            firstDose = builder.firstDose;
            maxSingleDose = builder.maxSingleDose;
            maxDosePerDay = builder.maxDosePerDay;
            maxDosePerTreatmentPeriod = builder.maxDosePerTreatmentPeriod;
            maxTreatmentPeriod = builder.maxTreatmentPeriod;
            targetSpecies = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.targetSpecies, "targetSpecies"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Coded expression for the route.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * <p>
         * The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a 
         * numerical value and its unit of measurement.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getFirstDose() {
            return firstDose;
        }

        /**
         * <p>
         * The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a 
         * numerical value and its unit of measurement.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getMaxSingleDose() {
            return maxSingleDose;
        }

        /**
         * <p>
         * The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as 
         * per the protocol referenced in the clinical trial authorisation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getMaxDosePerDay() {
            return maxDosePerDay;
        }

        /**
         * <p>
         * The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial 
         * authorisation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Ratio}.
         */
        public Ratio getMaxDosePerTreatmentPeriod() {
            return maxDosePerTreatmentPeriod;
        }

        /**
         * <p>
         * The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol 
         * referenced in the clinical trial authorisation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getMaxTreatmentPeriod() {
            return maxTreatmentPeriod;
        }

        /**
         * <p>
         * A species for which this route applies.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link TargetSpecies}.
         */
        public List<TargetSpecies> getTargetSpecies() {
            return targetSpecies;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (firstDose != null) || 
                (maxSingleDose != null) || 
                (maxDosePerDay != null) || 
                (maxDosePerTreatmentPeriod != null) || 
                (maxTreatmentPeriod != null) || 
                !targetSpecies.isEmpty();
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(firstDose, "firstDose", visitor);
                    accept(maxSingleDose, "maxSingleDose", visitor);
                    accept(maxDosePerDay, "maxDosePerDay", visitor);
                    accept(maxDosePerTreatmentPeriod, "maxDosePerTreatmentPeriod", visitor);
                    accept(maxTreatmentPeriod, "maxTreatmentPeriod", visitor);
                    accept(targetSpecies, "targetSpecies", visitor, TargetSpecies.class);
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
            RouteOfAdministration other = (RouteOfAdministration) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(firstDose, other.firstDose) && 
                Objects.equals(maxSingleDose, other.maxSingleDose) && 
                Objects.equals(maxDosePerDay, other.maxDosePerDay) && 
                Objects.equals(maxDosePerTreatmentPeriod, other.maxDosePerTreatmentPeriod) && 
                Objects.equals(maxTreatmentPeriod, other.maxTreatmentPeriod) && 
                Objects.equals(targetSpecies, other.targetSpecies);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    firstDose, 
                    maxSingleDose, 
                    maxDosePerDay, 
                    maxDosePerTreatmentPeriod, 
                    maxTreatmentPeriod, 
                    targetSpecies);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder(CodeableConcept code) {
            Builder builder = new Builder();
            builder.code(code);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private Quantity firstDose;
            private Quantity maxSingleDose;
            private Quantity maxDosePerDay;
            private Ratio maxDosePerTreatmentPeriod;
            private Duration maxTreatmentPeriod;
            private List<TargetSpecies> targetSpecies = new ArrayList<>();

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Coded expression for the route.
             * </p>
             * 
             * @param code
             *     Coded expression for the route
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * <p>
             * The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a 
             * numerical value and its unit of measurement.
             * </p>
             * 
             * @param firstDose
             *     The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a 
             *     numerical value and its unit of measurement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder firstDose(Quantity firstDose) {
                this.firstDose = firstDose;
                return this;
            }

            /**
             * <p>
             * The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a 
             * numerical value and its unit of measurement.
             * </p>
             * 
             * @param maxSingleDose
             *     The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a 
             *     numerical value and its unit of measurement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder maxSingleDose(Quantity maxSingleDose) {
                this.maxSingleDose = maxSingleDose;
                return this;
            }

            /**
             * <p>
             * The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as 
             * per the protocol referenced in the clinical trial authorisation.
             * </p>
             * 
             * @param maxDosePerDay
             *     The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as 
             *     per the protocol referenced in the clinical trial authorisation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder maxDosePerDay(Quantity maxDosePerDay) {
                this.maxDosePerDay = maxDosePerDay;
                return this;
            }

            /**
             * <p>
             * The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial 
             * authorisation.
             * </p>
             * 
             * @param maxDosePerTreatmentPeriod
             *     The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial 
             *     authorisation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder maxDosePerTreatmentPeriod(Ratio maxDosePerTreatmentPeriod) {
                this.maxDosePerTreatmentPeriod = maxDosePerTreatmentPeriod;
                return this;
            }

            /**
             * <p>
             * The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol 
             * referenced in the clinical trial authorisation.
             * </p>
             * 
             * @param maxTreatmentPeriod
             *     The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol 
             *     referenced in the clinical trial authorisation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder maxTreatmentPeriod(Duration maxTreatmentPeriod) {
                this.maxTreatmentPeriod = maxTreatmentPeriod;
                return this;
            }

            /**
             * <p>
             * A species for which this route applies.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param targetSpecies
             *     A species for which this route applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetSpecies(TargetSpecies... targetSpecies) {
                for (TargetSpecies value : targetSpecies) {
                    this.targetSpecies.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A species for which this route applies.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param targetSpecies
             *     A species for which this route applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetSpecies(Collection<TargetSpecies> targetSpecies) {
                this.targetSpecies = new ArrayList<>(targetSpecies);
                return this;
            }

            @Override
            public RouteOfAdministration build() {
                return new RouteOfAdministration(this);
            }

            protected Builder from(RouteOfAdministration routeOfAdministration) {
                super.from(routeOfAdministration);
                code = routeOfAdministration.code;
                firstDose = routeOfAdministration.firstDose;
                maxSingleDose = routeOfAdministration.maxSingleDose;
                maxDosePerDay = routeOfAdministration.maxDosePerDay;
                maxDosePerTreatmentPeriod = routeOfAdministration.maxDosePerTreatmentPeriod;
                maxTreatmentPeriod = routeOfAdministration.maxTreatmentPeriod;
                targetSpecies.addAll(routeOfAdministration.targetSpecies);
                return this;
            }
        }

        /**
         * <p>
         * A species for which this route applies.
         * </p>
         */
        public static class TargetSpecies extends BackboneElement {
            private final CodeableConcept code;
            private final List<WithdrawalPeriod> withdrawalPeriod;

            private volatile int hashCode;

            private TargetSpecies(Builder builder) {
                super(builder);
                code = ValidationSupport.requireNonNull(builder.code, "code");
                withdrawalPeriod = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.withdrawalPeriod, "withdrawalPeriod"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Coded expression for the species.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * <p>
             * A species specific time during which consumption of animal product is not appropriate.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link WithdrawalPeriod}.
             */
            public List<WithdrawalPeriod> getWithdrawalPeriod() {
                return withdrawalPeriod;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
                    !withdrawalPeriod.isEmpty();
            }

            @Override
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(code, "code", visitor);
                        accept(withdrawalPeriod, "withdrawalPeriod", visitor, WithdrawalPeriod.class);
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
                TargetSpecies other = (TargetSpecies) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(withdrawalPeriod, other.withdrawalPeriod);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
                        withdrawalPeriod);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder().from(this);
            }

            public static Builder builder(CodeableConcept code) {
                Builder builder = new Builder();
                builder.code(code);
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept code;
                private List<WithdrawalPeriod> withdrawalPeriod = new ArrayList<>();

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
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * </p>
                 * <p>
                 * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Extension... modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * </p>
                 * <p>
                 * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * Coded expression for the species.
                 * </p>
                 * 
                 * @param code
                 *     Coded expression for the species
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * <p>
                 * A species specific time during which consumption of animal product is not appropriate.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param withdrawalPeriod
                 *     A species specific time during which consumption of animal product is not appropriate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder withdrawalPeriod(WithdrawalPeriod... withdrawalPeriod) {
                    for (WithdrawalPeriod value : withdrawalPeriod) {
                        this.withdrawalPeriod.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * A species specific time during which consumption of animal product is not appropriate.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param withdrawalPeriod
                 *     A species specific time during which consumption of animal product is not appropriate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder withdrawalPeriod(Collection<WithdrawalPeriod> withdrawalPeriod) {
                    this.withdrawalPeriod = new ArrayList<>(withdrawalPeriod);
                    return this;
                }

                @Override
                public TargetSpecies build() {
                    return new TargetSpecies(this);
                }

                protected Builder from(TargetSpecies targetSpecies) {
                    super.from(targetSpecies);
                    code = targetSpecies.code;
                    withdrawalPeriod.addAll(targetSpecies.withdrawalPeriod);
                    return this;
                }
            }

            /**
             * <p>
             * A species specific time during which consumption of animal product is not appropriate.
             * </p>
             */
            public static class WithdrawalPeriod extends BackboneElement {
                private final CodeableConcept tissue;
                private final Quantity value;
                private final String supportingInformation;

                private volatile int hashCode;

                private WithdrawalPeriod(Builder builder) {
                    super(builder);
                    tissue = ValidationSupport.requireNonNull(builder.tissue, "tissue");
                    value = ValidationSupport.requireNonNull(builder.value, "value");
                    supportingInformation = builder.supportingInformation;
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * <p>
                 * Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getTissue() {
                    return tissue;
                }

                /**
                 * <p>
                 * A value for the time.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Quantity}.
                 */
                public Quantity getValue() {
                    return value;
                }

                /**
                 * <p>
                 * Extra information about the withdrawal period.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getSupportingInformation() {
                    return supportingInformation;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (tissue != null) || 
                        (value != null) || 
                        (supportingInformation != null);
                }

                @Override
                public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                    if (visitor.preVisit(this)) {
                        visitor.visitStart(elementName, elementIndex, this);
                        if (visitor.visit(elementName, elementIndex, this)) {
                            // visit children
                            accept(id, "id", visitor);
                            accept(extension, "extension", visitor, Extension.class);
                            accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                            accept(tissue, "tissue", visitor);
                            accept(value, "value", visitor);
                            accept(supportingInformation, "supportingInformation", visitor);
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
                    WithdrawalPeriod other = (WithdrawalPeriod) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(tissue, other.tissue) && 
                        Objects.equals(value, other.value) && 
                        Objects.equals(supportingInformation, other.supportingInformation);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            tissue, 
                            value, 
                            supportingInformation);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder().from(this);
                }

                public static Builder builder(CodeableConcept tissue, Quantity value) {
                    Builder builder = new Builder();
                    builder.tissue(tissue);
                    builder.value(value);
                    return builder;
                }

                public static class Builder extends BackboneElement.Builder {
                    private CodeableConcept tissue;
                    private Quantity value;
                    private String supportingInformation;

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
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * </p>
                     * <p>
                     * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    @Override
                    public Builder modifierExtension(Extension... modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * <p>
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * </p>
                     * <p>
                     * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    @Override
                    public Builder modifierExtension(Collection<Extension> modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * <p>
                     * Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.
                     * </p>
                     * 
                     * @param tissue
                     *     Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder tissue(CodeableConcept tissue) {
                        this.tissue = tissue;
                        return this;
                    }

                    /**
                     * <p>
                     * A value for the time.
                     * </p>
                     * 
                     * @param value
                     *     A value for the time
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder value(Quantity value) {
                        this.value = value;
                        return this;
                    }

                    /**
                     * <p>
                     * Extra information about the withdrawal period.
                     * </p>
                     * 
                     * @param supportingInformation
                     *     Extra information about the withdrawal period
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder supportingInformation(String supportingInformation) {
                        this.supportingInformation = supportingInformation;
                        return this;
                    }

                    @Override
                    public WithdrawalPeriod build() {
                        return new WithdrawalPeriod(this);
                    }

                    protected Builder from(WithdrawalPeriod withdrawalPeriod) {
                        super.from(withdrawalPeriod);
                        tissue = withdrawalPeriod.tissue;
                        value = withdrawalPeriod.value;
                        supportingInformation = withdrawalPeriod.supportingInformation;
                        return this;
                    }
                }
            }
        }
    }
}
