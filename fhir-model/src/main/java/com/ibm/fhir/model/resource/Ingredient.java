/*
 * (C) Copyright IBM Corp. 2019, 2022
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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.CodeableReference;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.RatioRange;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.IngredientManufacturerRole;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An ingredient of a manufactured item or pharmaceutical product.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "ing-1",
    level = "Rule",
    location = "(base)",
    description = "If an ingredient is noted as an allergen (allergenicIndicator) then its substance should be a code. If the substance is a SubstanceDefinition, then the allegen information should be documented in that resource",
    expression = "(Ingredient.allergenicIndicator.where(value='true').count() + Ingredient.substance.code.reference.count())  < 2",
    source = "http://hl7.org/fhir/StructureDefinition/Ingredient"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Ingredient extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    @ReferenceTarget({ "MedicinalProductDefinition", "AdministrableProductDefinition", "ManufacturedItemDefinition" })
    private final List<Reference> _for;
    @Summary
    @Binding(
        bindingName = "IngredientRole",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A classification of the ingredient identifying its purpose within the product, e.g. active, inactive.",
        valueSet = "http://hl7.org/fhir/ValueSet/ingredient-role"
    )
    @Required
    private final CodeableConcept role;
    @Summary
    @Binding(
        bindingName = "IngredientFunction",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A classification of the ingredient identifying its precise purpose(s) in the drug product (beyond e.g. active/inactive).",
        valueSet = "http://hl7.org/fhir/ValueSet/ingredient-function"
    )
    private final List<CodeableConcept> function;
    @Summary
    private final Boolean allergenicIndicator;
    @Summary
    private final List<Manufacturer> manufacturer;
    @Summary
    @Required
    private final Substance substance;

    private Ingredient(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        status = builder.status;
        _for = Collections.unmodifiableList(builder._for);
        role = builder.role;
        function = Collections.unmodifiableList(builder.function);
        allergenicIndicator = builder.allergenicIndicator;
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        substance = builder.substance;
    }

    /**
     * The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct 
     * URL reference to the resource itself is not appropriate.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * The status of this ingredient. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * The product which this ingredient is a constituent part of.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getFor() {
        return _for;
    }

    /**
     * A classification of the ingredient identifying its purpose within the product, e.g. active, inactive.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getRole() {
        return role;
    }

    /**
     * A classification of the ingredient identifying its precise purpose(s) in the drug product. This extends the Ingredient.
     * role to add more detail. Example: antioxidant, alkalizing agent.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getFunction() {
        return function;
    }

    /**
     * If the ingredient is a known or suspected allergen. Note that this is a property of the substance, so if a reference 
     * to a SubstanceDefinition is used to decribe that (rather than just a code), the allergen information should go there, 
     * not here.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getAllergenicIndicator() {
        return allergenicIndicator;
    }

    /**
     * The organization(s) that manufacture this ingredient. Can be used to indicate: 1) Organizations we are aware of that 
     * manufacture this ingredient 2) Specific Manufacturer(s) currently being used 3) Set of organisations allowed to 
     * manufacture this ingredient for this product Users must be clear on the application of context relevant to their use 
     * case.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Manufacturer} that may be empty.
     */
    public List<Manufacturer> getManufacturer() {
        return manufacturer;
    }

    /**
     * The substance that comprises this ingredient.
     * 
     * @return
     *     An immutable object of type {@link Substance} that is non-null.
     */
    public Substance getSubstance() {
        return substance;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (status != null) || 
            !_for.isEmpty() || 
            (role != null) || 
            !function.isEmpty() || 
            (allergenicIndicator != null) || 
            !manufacturer.isEmpty() || 
            (substance != null);
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
                accept(identifier, "identifier", visitor);
                accept(status, "status", visitor);
                accept(_for, "for", visitor, Reference.class);
                accept(role, "role", visitor);
                accept(function, "function", visitor, CodeableConcept.class);
                accept(allergenicIndicator, "allergenicIndicator", visitor);
                accept(manufacturer, "manufacturer", visitor, Manufacturer.class);
                accept(substance, "substance", visitor);
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
        Ingredient other = (Ingredient) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(_for, other._for) && 
            Objects.equals(role, other.role) && 
            Objects.equals(function, other.function) && 
            Objects.equals(allergenicIndicator, other.allergenicIndicator) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(substance, other.substance);
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
                status, 
                _for, 
                role, 
                function, 
                allergenicIndicator, 
                manufacturer, 
                substance);
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
        private Identifier identifier;
        private PublicationStatus status;
        private List<Reference> _for = new ArrayList<>();
        private CodeableConcept role;
        private List<CodeableConcept> function = new ArrayList<>();
        private Boolean allergenicIndicator;
        private List<Manufacturer> manufacturer = new ArrayList<>();
        private Substance substance;

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
         * The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct 
         * URL reference to the resource itself is not appropriate.
         * 
         * @param identifier
         *     An identifier or code by which the ingredient can be referenced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * The status of this ingredient. Enables tracking the life-cycle of the content.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | active | retired | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(PublicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The product which this ingredient is a constituent part of.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * <li>{@link AdministrableProductDefinition}</li>
         * <li>{@link ManufacturedItemDefinition}</li>
         * </ul>
         * 
         * @param _for
         *     The product which this ingredient is a constituent part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _for(Reference... _for) {
            for (Reference value : _for) {
                this._for.add(value);
            }
            return this;
        }

        /**
         * The product which this ingredient is a constituent part of.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * <li>{@link AdministrableProductDefinition}</li>
         * <li>{@link ManufacturedItemDefinition}</li>
         * </ul>
         * 
         * @param _for
         *     The product which this ingredient is a constituent part of
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder _for(Collection<Reference> _for) {
            this._for = new ArrayList<>(_for);
            return this;
        }

        /**
         * A classification of the ingredient identifying its purpose within the product, e.g. active, inactive.
         * 
         * <p>This element is required.
         * 
         * @param role
         *     Purpose of the ingredient within the product, e.g. active, inactive
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder role(CodeableConcept role) {
            this.role = role;
            return this;
        }

        /**
         * A classification of the ingredient identifying its precise purpose(s) in the drug product. This extends the Ingredient.
         * role to add more detail. Example: antioxidant, alkalizing agent.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param function
         *     Precise action within the drug product, e.g. antioxidant, alkalizing agent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder function(CodeableConcept... function) {
            for (CodeableConcept value : function) {
                this.function.add(value);
            }
            return this;
        }

        /**
         * A classification of the ingredient identifying its precise purpose(s) in the drug product. This extends the Ingredient.
         * role to add more detail. Example: antioxidant, alkalizing agent.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param function
         *     Precise action within the drug product, e.g. antioxidant, alkalizing agent
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder function(Collection<CodeableConcept> function) {
            this.function = new ArrayList<>(function);
            return this;
        }

        /**
         * Convenience method for setting {@code allergenicIndicator}.
         * 
         * @param allergenicIndicator
         *     If the ingredient is a known or suspected allergen
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #allergenicIndicator(com.ibm.fhir.model.type.Boolean)
         */
        public Builder allergenicIndicator(java.lang.Boolean allergenicIndicator) {
            this.allergenicIndicator = (allergenicIndicator == null) ? null : Boolean.of(allergenicIndicator);
            return this;
        }

        /**
         * If the ingredient is a known or suspected allergen. Note that this is a property of the substance, so if a reference 
         * to a SubstanceDefinition is used to decribe that (rather than just a code), the allergen information should go there, 
         * not here.
         * 
         * @param allergenicIndicator
         *     If the ingredient is a known or suspected allergen
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder allergenicIndicator(Boolean allergenicIndicator) {
            this.allergenicIndicator = allergenicIndicator;
            return this;
        }

        /**
         * The organization(s) that manufacture this ingredient. Can be used to indicate: 1) Organizations we are aware of that 
         * manufacture this ingredient 2) Specific Manufacturer(s) currently being used 3) Set of organisations allowed to 
         * manufacture this ingredient for this product Users must be clear on the application of context relevant to their use 
         * case.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param manufacturer
         *     An organization that manufactures this ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(Manufacturer... manufacturer) {
            for (Manufacturer value : manufacturer) {
                this.manufacturer.add(value);
            }
            return this;
        }

        /**
         * The organization(s) that manufacture this ingredient. Can be used to indicate: 1) Organizations we are aware of that 
         * manufacture this ingredient 2) Specific Manufacturer(s) currently being used 3) Set of organisations allowed to 
         * manufacture this ingredient for this product Users must be clear on the application of context relevant to their use 
         * case.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param manufacturer
         *     An organization that manufactures this ingredient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder manufacturer(Collection<Manufacturer> manufacturer) {
            this.manufacturer = new ArrayList<>(manufacturer);
            return this;
        }

        /**
         * The substance that comprises this ingredient.
         * 
         * <p>This element is required.
         * 
         * @param substance
         *     The substance that comprises this ingredient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder substance(Substance substance) {
            this.substance = substance;
            return this;
        }

        /**
         * Build the {@link Ingredient}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>role</li>
         * <li>substance</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Ingredient}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Ingredient per the base specification
         */
        @Override
        public Ingredient build() {
            Ingredient ingredient = new Ingredient(this);
            if (validating) {
                validate(ingredient);
            }
            return ingredient;
        }

        protected void validate(Ingredient ingredient) {
            super.validate(ingredient);
            ValidationSupport.requireNonNull(ingredient.status, "status");
            ValidationSupport.checkList(ingredient._for, "for", Reference.class);
            ValidationSupport.requireNonNull(ingredient.role, "role");
            ValidationSupport.checkList(ingredient.function, "function", CodeableConcept.class);
            ValidationSupport.checkList(ingredient.manufacturer, "manufacturer", Manufacturer.class);
            ValidationSupport.requireNonNull(ingredient.substance, "substance");
            ValidationSupport.checkReferenceType(ingredient._for, "for", "MedicinalProductDefinition", "AdministrableProductDefinition", "ManufacturedItemDefinition");
        }

        protected Builder from(Ingredient ingredient) {
            super.from(ingredient);
            identifier = ingredient.identifier;
            status = ingredient.status;
            _for.addAll(ingredient._for);
            role = ingredient.role;
            function.addAll(ingredient.function);
            allergenicIndicator = ingredient.allergenicIndicator;
            manufacturer.addAll(ingredient.manufacturer);
            substance = ingredient.substance;
            return this;
        }
    }

    /**
     * The organization(s) that manufacture this ingredient. Can be used to indicate: 1) Organizations we are aware of that 
     * manufacture this ingredient 2) Specific Manufacturer(s) currently being used 3) Set of organisations allowed to 
     * manufacture this ingredient for this product Users must be clear on the application of context relevant to their use 
     * case.
     */
    public static class Manufacturer extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "IngredientManufacturerRole",
            strength = BindingStrength.Value.REQUIRED,
            description = "The way in which this manufacturer is associated with the ingredient.",
            valueSet = "http://hl7.org/fhir/ValueSet/ingredient-manufacturer-role|4.3.0"
        )
        private final IngredientManufacturerRole role;
        @Summary
        @ReferenceTarget({ "Organization" })
        @Required
        private final Reference manufacturer;

        private Manufacturer(Builder builder) {
            super(builder);
            role = builder.role;
            manufacturer = builder.manufacturer;
        }

        /**
         * The way in which this manufacturer is associated with the ingredient. For example whether it is a possible one (others 
         * allowed), or an exclusive authorized one for this ingredient. Note that this is not the manufacturing process role.
         * 
         * @return
         *     An immutable object of type {@link IngredientManufacturerRole} that may be null.
         */
        public IngredientManufacturerRole getRole() {
            return role;
        }

        /**
         * An organization that manufactures this ingredient.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getManufacturer() {
            return manufacturer;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (role != null) || 
                (manufacturer != null);
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
                    accept(role, "role", visitor);
                    accept(manufacturer, "manufacturer", visitor);
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
            Manufacturer other = (Manufacturer) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(role, other.role) && 
                Objects.equals(manufacturer, other.manufacturer);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    role, 
                    manufacturer);
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
            private IngredientManufacturerRole role;
            private Reference manufacturer;

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
             * The way in which this manufacturer is associated with the ingredient. For example whether it is a possible one (others 
             * allowed), or an exclusive authorized one for this ingredient. Note that this is not the manufacturing process role.
             * 
             * @param role
             *     allowed | possible | actual
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(IngredientManufacturerRole role) {
                this.role = role;
                return this;
            }

            /**
             * An organization that manufactures this ingredient.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param manufacturer
             *     An organization that manufactures this ingredient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manufacturer(Reference manufacturer) {
                this.manufacturer = manufacturer;
                return this;
            }

            /**
             * Build the {@link Manufacturer}
             * 
             * <p>Required elements:
             * <ul>
             * <li>manufacturer</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Manufacturer}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Manufacturer per the base specification
             */
            @Override
            public Manufacturer build() {
                Manufacturer manufacturer = new Manufacturer(this);
                if (validating) {
                    validate(manufacturer);
                }
                return manufacturer;
            }

            protected void validate(Manufacturer manufacturer) {
                super.validate(manufacturer);
                ValidationSupport.requireNonNull(manufacturer.manufacturer, "manufacturer");
                ValidationSupport.checkReferenceType(manufacturer.manufacturer, "manufacturer", "Organization");
                ValidationSupport.requireValueOrChildren(manufacturer);
            }

            protected Builder from(Manufacturer manufacturer) {
                super.from(manufacturer);
                role = manufacturer.role;
                this.manufacturer = manufacturer.manufacturer;
                return this;
            }
        }
    }

    /**
     * The substance that comprises this ingredient.
     */
    public static class Substance extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "SNOMEDCTSubstanceCodes",
            strength = BindingStrength.Value.EXAMPLE,
            description = "This value set includes all substance codes from SNOMED CT - provided as an exemplar value set.",
            valueSet = "http://hl7.org/fhir/ValueSet/substance-codes"
        )
        @Required
        private final CodeableReference code;
        @Summary
        private final List<Strength> strength;

        private Substance(Builder builder) {
            super(builder);
            code = builder.code;
            strength = Collections.unmodifiableList(builder.strength);
        }

        /**
         * A code or full resource that represents the ingredient's substance.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that is non-null.
         */
        public CodeableReference getCode() {
            return code;
        }

        /**
         * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
         * or manufactured item. The allowed repetitions do not represent different strengths, but are different representations 
         * - mathematically equivalent - of a single strength.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Strength} that may be empty.
         */
        public List<Strength> getStrength() {
            return strength;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                !strength.isEmpty();
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
                    accept(strength, "strength", visitor, Strength.class);
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
            Substance other = (Substance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(strength, other.strength);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    strength);
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
            private CodeableReference code;
            private List<Strength> strength = new ArrayList<>();

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
             * A code or full resource that represents the ingredient's substance.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     A code or full resource that represents the ingredient substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableReference code) {
                this.code = code;
                return this;
            }

            /**
             * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
             * or manufactured item. The allowed repetitions do not represent different strengths, but are different representations 
             * - mathematically equivalent - of a single strength.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param strength
             *     The quantity of substance, per presentation, or per volume or mass, and type of quantity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder strength(Strength... strength) {
                for (Strength value : strength) {
                    this.strength.add(value);
                }
                return this;
            }

            /**
             * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
             * or manufactured item. The allowed repetitions do not represent different strengths, but are different representations 
             * - mathematically equivalent - of a single strength.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param strength
             *     The quantity of substance, per presentation, or per volume or mass, and type of quantity
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder strength(Collection<Strength> strength) {
                this.strength = new ArrayList<>(strength);
                return this;
            }

            /**
             * Build the {@link Substance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Substance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Substance per the base specification
             */
            @Override
            public Substance build() {
                Substance substance = new Substance(this);
                if (validating) {
                    validate(substance);
                }
                return substance;
            }

            protected void validate(Substance substance) {
                super.validate(substance);
                ValidationSupport.requireNonNull(substance.code, "code");
                ValidationSupport.checkList(substance.strength, "strength", Strength.class);
                ValidationSupport.requireValueOrChildren(substance);
            }

            protected Builder from(Substance substance) {
                super.from(substance);
                code = substance.code;
                strength.addAll(substance.strength);
                return this;
            }
        }

        /**
         * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
         * or manufactured item. The allowed repetitions do not represent different strengths, but are different representations 
         * - mathematically equivalent - of a single strength.
         */
        public static class Strength extends BackboneElement {
            @Summary
            @Choice({ Ratio.class, RatioRange.class })
            private final Element presentation;
            @Summary
            private final String textPresentation;
            @Summary
            @Choice({ Ratio.class, RatioRange.class })
            private final Element concentration;
            @Summary
            private final String textConcentration;
            @Summary
            private final String measurementPoint;
            @Summary
            @Binding(
                bindingName = "Country",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Jurisdiction codes",
                valueSet = "http://hl7.org/fhir/ValueSet/country"
            )
            private final List<CodeableConcept> country;
            @Summary
            private final List<ReferenceStrength> referenceStrength;

            private Strength(Builder builder) {
                super(builder);
                presentation = builder.presentation;
                textPresentation = builder.textPresentation;
                concentration = builder.concentration;
                textConcentration = builder.textConcentration;
                measurementPoint = builder.measurementPoint;
                country = Collections.unmodifiableList(builder.country);
                referenceStrength = Collections.unmodifiableList(builder.referenceStrength);
            }

            /**
             * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
             * or manufactured item. Unit of presentation refers to the quantity that the item occurs in e.g. a strength per tablet 
             * size, perhaps 'per 20mg' (the size of the tablet). It is not generally normalized as a unitary unit, which would be 
             * 'per mg').
             * 
             * @return
             *     An immutable object of type {@link Ratio} or {@link RatioRange} that may be null.
             */
            public Element getPresentation() {
                return presentation;
            }

            /**
             * A textual represention of either the whole of the presentation strength or a part of it - with the rest being in 
             * Strength.presentation as a ratio.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getTextPresentation() {
                return textPresentation;
            }

            /**
             * The strength per unitary volume (or mass).
             * 
             * @return
             *     An immutable object of type {@link Ratio} or {@link RatioRange} that may be null.
             */
            public Element getConcentration() {
                return concentration;
            }

            /**
             * A textual represention of either the whole of the concentration strength or a part of it - with the rest being in 
             * Strength.concentration as a ratio.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getTextConcentration() {
                return textConcentration;
            }

            /**
             * For when strength is measured at a particular point or distance. There are products where strength is measured at a 
             * particular point. For example, the strength of the ingredient in some inhalers is measured at a particular position 
             * relative to the point of aerosolization.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getMeasurementPoint() {
                return measurementPoint;
            }

            /**
             * The country or countries for which the strength range applies.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getCountry() {
                return country;
            }

            /**
             * Strength expressed in terms of a reference substance. For when the ingredient strength is additionally expressed as 
             * equivalent to the strength of some other closely related substance (e.g. salt vs. base). Reference strength represents 
             * the strength (quantitative composition) of the active moiety of the active substance. There are situations when the 
             * active substance and active moiety are different, therefore both a strength and a reference strength are needed.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link ReferenceStrength} that may be empty.
             */
            public List<ReferenceStrength> getReferenceStrength() {
                return referenceStrength;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (presentation != null) || 
                    (textPresentation != null) || 
                    (concentration != null) || 
                    (textConcentration != null) || 
                    (measurementPoint != null) || 
                    !country.isEmpty() || 
                    !referenceStrength.isEmpty();
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
                        accept(presentation, "presentation", visitor);
                        accept(textPresentation, "textPresentation", visitor);
                        accept(concentration, "concentration", visitor);
                        accept(textConcentration, "textConcentration", visitor);
                        accept(measurementPoint, "measurementPoint", visitor);
                        accept(country, "country", visitor, CodeableConcept.class);
                        accept(referenceStrength, "referenceStrength", visitor, ReferenceStrength.class);
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
                Strength other = (Strength) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(presentation, other.presentation) && 
                    Objects.equals(textPresentation, other.textPresentation) && 
                    Objects.equals(concentration, other.concentration) && 
                    Objects.equals(textConcentration, other.textConcentration) && 
                    Objects.equals(measurementPoint, other.measurementPoint) && 
                    Objects.equals(country, other.country) && 
                    Objects.equals(referenceStrength, other.referenceStrength);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        presentation, 
                        textPresentation, 
                        concentration, 
                        textConcentration, 
                        measurementPoint, 
                        country, 
                        referenceStrength);
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
                private Element presentation;
                private String textPresentation;
                private Element concentration;
                private String textConcentration;
                private String measurementPoint;
                private List<CodeableConcept> country = new ArrayList<>();
                private List<ReferenceStrength> referenceStrength = new ArrayList<>();

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
                 * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product 
                 * or manufactured item. Unit of presentation refers to the quantity that the item occurs in e.g. a strength per tablet 
                 * size, perhaps 'per 20mg' (the size of the tablet). It is not generally normalized as a unitary unit, which would be 
                 * 'per mg').
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Ratio}</li>
                 * <li>{@link RatioRange}</li>
                 * </ul>
                 * 
                 * @param presentation
                 *     The quantity of substance in the unit of presentation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder presentation(Element presentation) {
                    this.presentation = presentation;
                    return this;
                }

                /**
                 * Convenience method for setting {@code textPresentation}.
                 * 
                 * @param textPresentation
                 *     Text of either the whole presentation strength or a part of it (rest being in Strength.presentation as a ratio)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #textPresentation(com.ibm.fhir.model.type.String)
                 */
                public Builder textPresentation(java.lang.String textPresentation) {
                    this.textPresentation = (textPresentation == null) ? null : String.of(textPresentation);
                    return this;
                }

                /**
                 * A textual represention of either the whole of the presentation strength or a part of it - with the rest being in 
                 * Strength.presentation as a ratio.
                 * 
                 * @param textPresentation
                 *     Text of either the whole presentation strength or a part of it (rest being in Strength.presentation as a ratio)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder textPresentation(String textPresentation) {
                    this.textPresentation = textPresentation;
                    return this;
                }

                /**
                 * The strength per unitary volume (or mass).
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Ratio}</li>
                 * <li>{@link RatioRange}</li>
                 * </ul>
                 * 
                 * @param concentration
                 *     The strength per unitary volume (or mass)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder concentration(Element concentration) {
                    this.concentration = concentration;
                    return this;
                }

                /**
                 * Convenience method for setting {@code textConcentration}.
                 * 
                 * @param textConcentration
                 *     Text of either the whole concentration strength or a part of it (rest being in Strength.concentration as a ratio)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #textConcentration(com.ibm.fhir.model.type.String)
                 */
                public Builder textConcentration(java.lang.String textConcentration) {
                    this.textConcentration = (textConcentration == null) ? null : String.of(textConcentration);
                    return this;
                }

                /**
                 * A textual represention of either the whole of the concentration strength or a part of it - with the rest being in 
                 * Strength.concentration as a ratio.
                 * 
                 * @param textConcentration
                 *     Text of either the whole concentration strength or a part of it (rest being in Strength.concentration as a ratio)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder textConcentration(String textConcentration) {
                    this.textConcentration = textConcentration;
                    return this;
                }

                /**
                 * Convenience method for setting {@code measurementPoint}.
                 * 
                 * @param measurementPoint
                 *     When strength is measured at a particular point or distance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #measurementPoint(com.ibm.fhir.model.type.String)
                 */
                public Builder measurementPoint(java.lang.String measurementPoint) {
                    this.measurementPoint = (measurementPoint == null) ? null : String.of(measurementPoint);
                    return this;
                }

                /**
                 * For when strength is measured at a particular point or distance. There are products where strength is measured at a 
                 * particular point. For example, the strength of the ingredient in some inhalers is measured at a particular position 
                 * relative to the point of aerosolization.
                 * 
                 * @param measurementPoint
                 *     When strength is measured at a particular point or distance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder measurementPoint(String measurementPoint) {
                    this.measurementPoint = measurementPoint;
                    return this;
                }

                /**
                 * The country or countries for which the strength range applies.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param country
                 *     Where the strength range applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder country(CodeableConcept... country) {
                    for (CodeableConcept value : country) {
                        this.country.add(value);
                    }
                    return this;
                }

                /**
                 * The country or countries for which the strength range applies.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param country
                 *     Where the strength range applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder country(Collection<CodeableConcept> country) {
                    this.country = new ArrayList<>(country);
                    return this;
                }

                /**
                 * Strength expressed in terms of a reference substance. For when the ingredient strength is additionally expressed as 
                 * equivalent to the strength of some other closely related substance (e.g. salt vs. base). Reference strength represents 
                 * the strength (quantitative composition) of the active moiety of the active substance. There are situations when the 
                 * active substance and active moiety are different, therefore both a strength and a reference strength are needed.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param referenceStrength
                 *     Strength expressed in terms of a reference substance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder referenceStrength(ReferenceStrength... referenceStrength) {
                    for (ReferenceStrength value : referenceStrength) {
                        this.referenceStrength.add(value);
                    }
                    return this;
                }

                /**
                 * Strength expressed in terms of a reference substance. For when the ingredient strength is additionally expressed as 
                 * equivalent to the strength of some other closely related substance (e.g. salt vs. base). Reference strength represents 
                 * the strength (quantitative composition) of the active moiety of the active substance. There are situations when the 
                 * active substance and active moiety are different, therefore both a strength and a reference strength are needed.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param referenceStrength
                 *     Strength expressed in terms of a reference substance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder referenceStrength(Collection<ReferenceStrength> referenceStrength) {
                    this.referenceStrength = new ArrayList<>(referenceStrength);
                    return this;
                }

                /**
                 * Build the {@link Strength}
                 * 
                 * @return
                 *     An immutable object of type {@link Strength}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Strength per the base specification
                 */
                @Override
                public Strength build() {
                    Strength strength = new Strength(this);
                    if (validating) {
                        validate(strength);
                    }
                    return strength;
                }

                protected void validate(Strength strength) {
                    super.validate(strength);
                    ValidationSupport.choiceElement(strength.presentation, "presentation", Ratio.class, RatioRange.class);
                    ValidationSupport.choiceElement(strength.concentration, "concentration", Ratio.class, RatioRange.class);
                    ValidationSupport.checkList(strength.country, "country", CodeableConcept.class);
                    ValidationSupport.checkList(strength.referenceStrength, "referenceStrength", ReferenceStrength.class);
                    ValidationSupport.requireValueOrChildren(strength);
                }

                protected Builder from(Strength strength) {
                    super.from(strength);
                    presentation = strength.presentation;
                    textPresentation = strength.textPresentation;
                    concentration = strength.concentration;
                    textConcentration = strength.textConcentration;
                    measurementPoint = strength.measurementPoint;
                    country.addAll(strength.country);
                    referenceStrength.addAll(strength.referenceStrength);
                    return this;
                }
            }

            /**
             * Strength expressed in terms of a reference substance. For when the ingredient strength is additionally expressed as 
             * equivalent to the strength of some other closely related substance (e.g. salt vs. base). Reference strength represents 
             * the strength (quantitative composition) of the active moiety of the active substance. There are situations when the 
             * active substance and active moiety are different, therefore both a strength and a reference strength are needed.
             */
            public static class ReferenceStrength extends BackboneElement {
                @Summary
                @Binding(
                    bindingName = "SNOMEDCTSubstanceCodes",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "This value set includes all substance codes from SNOMED CT - provided as an exemplar value set.",
                    valueSet = "http://hl7.org/fhir/ValueSet/substance-codes"
                )
                private final CodeableReference substance;
                @Summary
                @Choice({ Ratio.class, RatioRange.class })
                @Required
                private final Element strength;
                @Summary
                private final String measurementPoint;
                @Summary
                @Binding(
                    bindingName = "Country",
                    strength = BindingStrength.Value.EXAMPLE,
                    description = "Jurisdiction codes",
                    valueSet = "http://hl7.org/fhir/ValueSet/country"
                )
                private final List<CodeableConcept> country;

                private ReferenceStrength(Builder builder) {
                    super(builder);
                    substance = builder.substance;
                    strength = builder.strength;
                    measurementPoint = builder.measurementPoint;
                    country = Collections.unmodifiableList(builder.country);
                }

                /**
                 * Relevant reference substance.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableReference} that may be null.
                 */
                public CodeableReference getSubstance() {
                    return substance;
                }

                /**
                 * Strength expressed in terms of a reference substance.
                 * 
                 * @return
                 *     An immutable object of type {@link Ratio} or {@link RatioRange} that is non-null.
                 */
                public Element getStrength() {
                    return strength;
                }

                /**
                 * For when strength is measured at a particular point or distance.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getMeasurementPoint() {
                    return measurementPoint;
                }

                /**
                 * The country or countries for which the strength range applies.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
                 */
                public List<CodeableConcept> getCountry() {
                    return country;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (substance != null) || 
                        (strength != null) || 
                        (measurementPoint != null) || 
                        !country.isEmpty();
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
                            accept(substance, "substance", visitor);
                            accept(strength, "strength", visitor);
                            accept(measurementPoint, "measurementPoint", visitor);
                            accept(country, "country", visitor, CodeableConcept.class);
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
                    ReferenceStrength other = (ReferenceStrength) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(substance, other.substance) && 
                        Objects.equals(strength, other.strength) && 
                        Objects.equals(measurementPoint, other.measurementPoint) && 
                        Objects.equals(country, other.country);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            substance, 
                            strength, 
                            measurementPoint, 
                            country);
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
                    private CodeableReference substance;
                    private Element strength;
                    private String measurementPoint;
                    private List<CodeableConcept> country = new ArrayList<>();

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
                     * Relevant reference substance.
                     * 
                     * @param substance
                     *     Relevant reference substance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder substance(CodeableReference substance) {
                        this.substance = substance;
                        return this;
                    }

                    /**
                     * Strength expressed in terms of a reference substance.
                     * 
                     * <p>This element is required.
                     * 
                     * <p>This is a choice element with the following allowed types:
                     * <ul>
                     * <li>{@link Ratio}</li>
                     * <li>{@link RatioRange}</li>
                     * </ul>
                     * 
                     * @param strength
                     *     Strength expressed in terms of a reference substance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder strength(Element strength) {
                        this.strength = strength;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code measurementPoint}.
                     * 
                     * @param measurementPoint
                     *     When strength is measured at a particular point or distance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #measurementPoint(com.ibm.fhir.model.type.String)
                     */
                    public Builder measurementPoint(java.lang.String measurementPoint) {
                        this.measurementPoint = (measurementPoint == null) ? null : String.of(measurementPoint);
                        return this;
                    }

                    /**
                     * For when strength is measured at a particular point or distance.
                     * 
                     * @param measurementPoint
                     *     When strength is measured at a particular point or distance
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder measurementPoint(String measurementPoint) {
                        this.measurementPoint = measurementPoint;
                        return this;
                    }

                    /**
                     * The country or countries for which the strength range applies.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param country
                     *     Where the strength range applies
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder country(CodeableConcept... country) {
                        for (CodeableConcept value : country) {
                            this.country.add(value);
                        }
                        return this;
                    }

                    /**
                     * The country or countries for which the strength range applies.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param country
                     *     Where the strength range applies
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder country(Collection<CodeableConcept> country) {
                        this.country = new ArrayList<>(country);
                        return this;
                    }

                    /**
                     * Build the {@link ReferenceStrength}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>strength</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link ReferenceStrength}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid ReferenceStrength per the base specification
                     */
                    @Override
                    public ReferenceStrength build() {
                        ReferenceStrength referenceStrength = new ReferenceStrength(this);
                        if (validating) {
                            validate(referenceStrength);
                        }
                        return referenceStrength;
                    }

                    protected void validate(ReferenceStrength referenceStrength) {
                        super.validate(referenceStrength);
                        ValidationSupport.requireChoiceElement(referenceStrength.strength, "strength", Ratio.class, RatioRange.class);
                        ValidationSupport.checkList(referenceStrength.country, "country", CodeableConcept.class);
                        ValidationSupport.requireValueOrChildren(referenceStrength);
                    }

                    protected Builder from(ReferenceStrength referenceStrength) {
                        super.from(referenceStrength);
                        substance = referenceStrength.substance;
                        strength = referenceStrength.strength;
                        measurementPoint = referenceStrength.measurementPoint;
                        country.addAll(referenceStrength.country);
                        return this;
                    }
                }
            }
        }
    }
}
