/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A pharmaceutical product described in terms of its composition and dose form.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductPharmaceutical extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Required
    private final CodeableConcept administrableDoseForm;
    @Summary
    private final CodeableConcept unitOfPresentation;
    @Summary
    @ReferenceTarget({ "MedicinalProductIngredient" })
    private final List<Reference> ingredient;
    @Summary
    @ReferenceTarget({ "DeviceDefinition" })
    private final List<Reference> device;
    @Summary
    private final List<Characteristics> characteristics;
    @Summary
    @Required
    private final List<RouteOfAdministration> routeOfAdministration;

    private MedicinalProductPharmaceutical(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        administrableDoseForm = builder.administrableDoseForm;
        unitOfPresentation = builder.unitOfPresentation;
        ingredient = Collections.unmodifiableList(builder.ingredient);
        device = Collections.unmodifiableList(builder.device);
        characteristics = Collections.unmodifiableList(builder.characteristics);
        routeOfAdministration = Collections.unmodifiableList(builder.routeOfAdministration);
    }

    /**
     * An identifier for the pharmaceutical medicinal product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The administrable dose form, after necessary reconstitution.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getAdministrableDoseForm() {
        return administrableDoseForm;
    }

    /**
     * Todo.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getUnitOfPresentation() {
        return unitOfPresentation;
    }

    /**
     * Ingredient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getIngredient() {
        return ingredient;
    }

    /**
     * Accompanying device.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDevice() {
        return device;
    }

    /**
     * Characteristics e.g. a products onset of action.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Characteristics} that may be empty.
     */
    public List<Characteristics> getCharacteristics() {
        return characteristics;
    }

    /**
     * The path by which the pharmaceutical product is taken into or makes contact with the body.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RouteOfAdministration} that is non-empty.
     */
    public List<RouteOfAdministration> getRouteOfAdministration() {
        return routeOfAdministration;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (administrableDoseForm != null) || 
            (unitOfPresentation != null) || 
            !ingredient.isEmpty() || 
            !device.isEmpty() || 
            !characteristics.isEmpty() || 
            !routeOfAdministration.isEmpty();
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept administrableDoseForm;
        private CodeableConcept unitOfPresentation;
        private List<Reference> ingredient = new ArrayList<>();
        private List<Reference> device = new ArrayList<>();
        private List<Characteristics> characteristics = new ArrayList<>();
        private List<RouteOfAdministration> routeOfAdministration = new ArrayList<>();

        private Builder() {
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
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
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
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
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
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
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
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
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
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * An identifier for the pharmaceutical medicinal product.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * An identifier for the pharmaceutical medicinal product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     An identifier for the pharmaceutical medicinal product
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * The administrable dose form, after necessary reconstitution.
         * 
         * <p>This element is required.
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
         * Todo.
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
         * Ingredient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductIngredient}</li>
         * </ul>
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
         * Ingredient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductIngredient}</li>
         * </ul>
         * 
         * @param ingredient
         *     Ingredient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder ingredient(Collection<Reference> ingredient) {
            this.ingredient = new ArrayList<>(ingredient);
            return this;
        }

        /**
         * Accompanying device.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DeviceDefinition}</li>
         * </ul>
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
         * Accompanying device.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DeviceDefinition}</li>
         * </ul>
         * 
         * @param device
         *     Accompanying device
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder device(Collection<Reference> device) {
            this.device = new ArrayList<>(device);
            return this;
        }

        /**
         * Characteristics e.g. a products onset of action.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Characteristics e.g. a products onset of action.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristics
         *     Characteristics e.g. a products onset of action
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder characteristics(Collection<Characteristics> characteristics) {
            this.characteristics = new ArrayList<>(characteristics);
            return this;
        }

        /**
         * The path by which the pharmaceutical product is taken into or makes contact with the body.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
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
         * The path by which the pharmaceutical product is taken into or makes contact with the body.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param routeOfAdministration
         *     The path by which the pharmaceutical product is taken into or makes contact with the body
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder routeOfAdministration(Collection<RouteOfAdministration> routeOfAdministration) {
            this.routeOfAdministration = new ArrayList<>(routeOfAdministration);
            return this;
        }

        /**
         * Build the {@link MedicinalProductPharmaceutical}
         * 
         * <p>Required elements:
         * <ul>
         * <li>administrableDoseForm</li>
         * <li>routeOfAdministration</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductPharmaceutical}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductPharmaceutical per the base specification
         */
        @Override
        public MedicinalProductPharmaceutical build() {
            MedicinalProductPharmaceutical medicinalProductPharmaceutical = new MedicinalProductPharmaceutical(this);
            if (validating) {
                validate(medicinalProductPharmaceutical);
            }
            return medicinalProductPharmaceutical;
        }

        protected void validate(MedicinalProductPharmaceutical medicinalProductPharmaceutical) {
            super.validate(medicinalProductPharmaceutical);
            ValidationSupport.checkList(medicinalProductPharmaceutical.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(medicinalProductPharmaceutical.administrableDoseForm, "administrableDoseForm");
            ValidationSupport.checkList(medicinalProductPharmaceutical.ingredient, "ingredient", Reference.class);
            ValidationSupport.checkList(medicinalProductPharmaceutical.device, "device", Reference.class);
            ValidationSupport.checkList(medicinalProductPharmaceutical.characteristics, "characteristics", Characteristics.class);
            ValidationSupport.checkNonEmptyList(medicinalProductPharmaceutical.routeOfAdministration, "routeOfAdministration", RouteOfAdministration.class);
            ValidationSupport.checkReferenceType(medicinalProductPharmaceutical.ingredient, "ingredient", "MedicinalProductIngredient");
            ValidationSupport.checkReferenceType(medicinalProductPharmaceutical.device, "device", "DeviceDefinition");
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
     * Characteristics e.g. a products onset of action.
     */
    public static class Characteristics extends BackboneElement {
        @Summary
        @Required
        private final CodeableConcept code;
        @Summary
        private final CodeableConcept status;

        private Characteristics(Builder builder) {
            super(builder);
            code = builder.code;
            status = builder.status;
        }

        /**
         * A coded characteristic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * The status of characteristic e.g. assigned or pending.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private CodeableConcept status;

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * A coded characteristic.
             * 
             * <p>This element is required.
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
             * The status of characteristic e.g. assigned or pending.
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

            /**
             * Build the {@link Characteristics}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Characteristics}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Characteristics per the base specification
             */
            @Override
            public Characteristics build() {
                Characteristics characteristics = new Characteristics(this);
                if (validating) {
                    validate(characteristics);
                }
                return characteristics;
            }

            protected void validate(Characteristics characteristics) {
                super.validate(characteristics);
                ValidationSupport.requireNonNull(characteristics.code, "code");
                ValidationSupport.requireValueOrChildren(characteristics);
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
     * The path by which the pharmaceutical product is taken into or makes contact with the body.
     */
    public static class RouteOfAdministration extends BackboneElement {
        @Summary
        @Required
        private final CodeableConcept code;
        @Summary
        private final Quantity firstDose;
        @Summary
        private final Quantity maxSingleDose;
        @Summary
        private final Quantity maxDosePerDay;
        @Summary
        private final Ratio maxDosePerTreatmentPeriod;
        @Summary
        private final Duration maxTreatmentPeriod;
        @Summary
        private final List<TargetSpecies> targetSpecies;

        private RouteOfAdministration(Builder builder) {
            super(builder);
            code = builder.code;
            firstDose = builder.firstDose;
            maxSingleDose = builder.maxSingleDose;
            maxDosePerDay = builder.maxDosePerDay;
            maxDosePerTreatmentPeriod = builder.maxDosePerTreatmentPeriod;
            maxTreatmentPeriod = builder.maxTreatmentPeriod;
            targetSpecies = Collections.unmodifiableList(builder.targetSpecies);
        }

        /**
         * Coded expression for the route.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a 
         * numerical value and its unit of measurement.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that may be null.
         */
        public Quantity getFirstDose() {
            return firstDose;
        }

        /**
         * The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a 
         * numerical value and its unit of measurement.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that may be null.
         */
        public Quantity getMaxSingleDose() {
            return maxSingleDose;
        }

        /**
         * The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as 
         * per the protocol referenced in the clinical trial authorisation.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that may be null.
         */
        public Quantity getMaxDosePerDay() {
            return maxDosePerDay;
        }

        /**
         * The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial 
         * authorisation.
         * 
         * @return
         *     An immutable object of type {@link Ratio} that may be null.
         */
        public Ratio getMaxDosePerTreatmentPeriod() {
            return maxDosePerTreatmentPeriod;
        }

        /**
         * The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol 
         * referenced in the clinical trial authorisation.
         * 
         * @return
         *     An immutable object of type {@link Duration} that may be null.
         */
        public Duration getMaxTreatmentPeriod() {
            return maxTreatmentPeriod;
        }

        /**
         * A species for which this route applies.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link TargetSpecies} that may be empty.
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

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private Quantity firstDose;
            private Quantity maxSingleDose;
            private Quantity maxDosePerDay;
            private Ratio maxDosePerTreatmentPeriod;
            private Duration maxTreatmentPeriod;
            private List<TargetSpecies> targetSpecies = new ArrayList<>();

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Coded expression for the route.
             * 
             * <p>This element is required.
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
             * The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a 
             * numerical value and its unit of measurement.
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
             * The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a 
             * numerical value and its unit of measurement.
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
             * The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as 
             * per the protocol referenced in the clinical trial authorisation.
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
             * The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial 
             * authorisation.
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
             * The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol 
             * referenced in the clinical trial authorisation.
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
             * A species for which this route applies.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * A species for which this route applies.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param targetSpecies
             *     A species for which this route applies
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder targetSpecies(Collection<TargetSpecies> targetSpecies) {
                this.targetSpecies = new ArrayList<>(targetSpecies);
                return this;
            }

            /**
             * Build the {@link RouteOfAdministration}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link RouteOfAdministration}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid RouteOfAdministration per the base specification
             */
            @Override
            public RouteOfAdministration build() {
                RouteOfAdministration routeOfAdministration = new RouteOfAdministration(this);
                if (validating) {
                    validate(routeOfAdministration);
                }
                return routeOfAdministration;
            }

            protected void validate(RouteOfAdministration routeOfAdministration) {
                super.validate(routeOfAdministration);
                ValidationSupport.requireNonNull(routeOfAdministration.code, "code");
                ValidationSupport.checkList(routeOfAdministration.targetSpecies, "targetSpecies", TargetSpecies.class);
                ValidationSupport.requireValueOrChildren(routeOfAdministration);
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
         * A species for which this route applies.
         */
        public static class TargetSpecies extends BackboneElement {
            @Summary
            @Required
            private final CodeableConcept code;
            @Summary
            private final List<WithdrawalPeriod> withdrawalPeriod;

            private TargetSpecies(Builder builder) {
                super(builder);
                code = builder.code;
                withdrawalPeriod = Collections.unmodifiableList(builder.withdrawalPeriod);
            }

            /**
             * Coded expression for the species.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * A species specific time during which consumption of animal product is not appropriate.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link WithdrawalPeriod} that may be empty.
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

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept code;
                private List<WithdrawalPeriod> withdrawalPeriod = new ArrayList<>();

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
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * Coded expression for the species.
                 * 
                 * <p>This element is required.
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
                 * A species specific time during which consumption of animal product is not appropriate.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * A species specific time during which consumption of animal product is not appropriate.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param withdrawalPeriod
                 *     A species specific time during which consumption of animal product is not appropriate
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder withdrawalPeriod(Collection<WithdrawalPeriod> withdrawalPeriod) {
                    this.withdrawalPeriod = new ArrayList<>(withdrawalPeriod);
                    return this;
                }

                /**
                 * Build the {@link TargetSpecies}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link TargetSpecies}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid TargetSpecies per the base specification
                 */
                @Override
                public TargetSpecies build() {
                    TargetSpecies targetSpecies = new TargetSpecies(this);
                    if (validating) {
                        validate(targetSpecies);
                    }
                    return targetSpecies;
                }

                protected void validate(TargetSpecies targetSpecies) {
                    super.validate(targetSpecies);
                    ValidationSupport.requireNonNull(targetSpecies.code, "code");
                    ValidationSupport.checkList(targetSpecies.withdrawalPeriod, "withdrawalPeriod", WithdrawalPeriod.class);
                    ValidationSupport.requireValueOrChildren(targetSpecies);
                }

                protected Builder from(TargetSpecies targetSpecies) {
                    super.from(targetSpecies);
                    code = targetSpecies.code;
                    withdrawalPeriod.addAll(targetSpecies.withdrawalPeriod);
                    return this;
                }
            }

            /**
             * A species specific time during which consumption of animal product is not appropriate.
             */
            public static class WithdrawalPeriod extends BackboneElement {
                @Summary
                @Required
                private final CodeableConcept tissue;
                @Summary
                @Required
                private final Quantity value;
                @Summary
                private final String supportingInformation;

                private WithdrawalPeriod(Builder builder) {
                    super(builder);
                    tissue = builder.tissue;
                    value = builder.value;
                    supportingInformation = builder.supportingInformation;
                }

                /**
                 * Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that is non-null.
                 */
                public CodeableConcept getTissue() {
                    return tissue;
                }

                /**
                 * A value for the time.
                 * 
                 * @return
                 *     An immutable object of type {@link Quantity} that is non-null.
                 */
                public Quantity getValue() {
                    return value;
                }

                /**
                 * Extra information about the withdrawal period.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
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

                public static Builder builder() {
                    return new Builder();
                }

                public static class Builder extends BackboneElement.Builder {
                    private CodeableConcept tissue;
                    private Quantity value;
                    private String supportingInformation;

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
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * 
                     * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
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
                     * May be used to represent additional information that is not part of the basic definition of the element and that 
                     * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                     * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                     * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                     * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                     * extension. Applications processing a resource are required to check for modifier extensions.
                     * 
                     * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                     * change the meaning of modifierExtension itself).
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    @Override
                    public Builder modifierExtension(Collection<Extension> modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.
                     * 
                     * <p>This element is required.
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
                     * A value for the time.
                     * 
                     * <p>This element is required.
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
                     * Convenience method for setting {@code supportingInformation}.
                     * 
                     * @param supportingInformation
                     *     Extra information about the withdrawal period
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #supportingInformation(com.ibm.fhir.model.type.String)
                     */
                    public Builder supportingInformation(java.lang.String supportingInformation) {
                        this.supportingInformation = (supportingInformation == null) ? null : String.of(supportingInformation);
                        return this;
                    }

                    /**
                     * Extra information about the withdrawal period.
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

                    /**
                     * Build the {@link WithdrawalPeriod}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>tissue</li>
                     * <li>value</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link WithdrawalPeriod}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid WithdrawalPeriod per the base specification
                     */
                    @Override
                    public WithdrawalPeriod build() {
                        WithdrawalPeriod withdrawalPeriod = new WithdrawalPeriod(this);
                        if (validating) {
                            validate(withdrawalPeriod);
                        }
                        return withdrawalPeriod;
                    }

                    protected void validate(WithdrawalPeriod withdrawalPeriod) {
                        super.validate(withdrawalPeriod);
                        ValidationSupport.requireNonNull(withdrawalPeriod.tissue, "tissue");
                        ValidationSupport.requireNonNull(withdrawalPeriod.value, "value");
                        ValidationSupport.requireValueOrChildren(withdrawalPeriod);
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
