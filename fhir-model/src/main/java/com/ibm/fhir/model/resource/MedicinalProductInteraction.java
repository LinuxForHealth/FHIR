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

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The interactions of the medicinal product with other medicinal products, or other forms of interactions.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductInteraction extends DomainResource {
    @Summary
    @ReferenceTarget({ "MedicinalProduct", "Medication", "Substance" })
    private final List<Reference> subject;
    @Summary
    private final String description;
    @Summary
    private final List<Interactant> interactant;
    @Summary
    private final CodeableConcept type;
    @Summary
    private final CodeableConcept effect;
    @Summary
    private final CodeableConcept incidence;
    @Summary
    private final CodeableConcept management;

    private MedicinalProductInteraction(Builder builder) {
        super(builder);
        subject = Collections.unmodifiableList(builder.subject);
        description = builder.description;
        interactant = Collections.unmodifiableList(builder.interactant);
        type = builder.type;
        effect = builder.effect;
        incidence = builder.incidence;
        management = builder.management;
    }

    /**
     * The medication for which this is a described interaction.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * The interaction described.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The specific medication, food or laboratory test that interacts.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Interactant} that may be empty.
     */
    public List<Interactant> getInteractant() {
        return interactant;
    }

    /**
     * The type of the interaction e.g. drug-drug interaction, drug-food interaction, drug-lab test interaction.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The effect of the interaction, for example "reduced gastric absorption of primary medication".
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getEffect() {
        return effect;
    }

    /**
     * The incidence of the interaction, e.g. theoretical, observed.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getIncidence() {
        return incidence;
    }

    /**
     * Actions for managing the interaction.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getManagement() {
        return management;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !subject.isEmpty() || 
            (description != null) || 
            !interactant.isEmpty() || 
            (type != null) || 
            (effect != null) || 
            (incidence != null) || 
            (management != null);
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
                accept(subject, "subject", visitor, Reference.class);
                accept(description, "description", visitor);
                accept(interactant, "interactant", visitor, Interactant.class);
                accept(type, "type", visitor);
                accept(effect, "effect", visitor);
                accept(incidence, "incidence", visitor);
                accept(management, "management", visitor);
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
        MedicinalProductInteraction other = (MedicinalProductInteraction) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(description, other.description) && 
            Objects.equals(interactant, other.interactant) && 
            Objects.equals(type, other.type) && 
            Objects.equals(effect, other.effect) && 
            Objects.equals(incidence, other.incidence) && 
            Objects.equals(management, other.management);
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
                subject, 
                description, 
                interactant, 
                type, 
                effect, 
                incidence, 
                management);
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
        private List<Reference> subject = new ArrayList<>();
        private String description;
        private List<Interactant> interactant = new ArrayList<>();
        private CodeableConcept type;
        private CodeableConcept effect;
        private CodeableConcept incidence;
        private CodeableConcept management;

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
         * The medication for which this is a described interaction.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProduct}</li>
         * <li>{@link Medication}</li>
         * <li>{@link Substance}</li>
         * </ul>
         * 
         * @param subject
         *     The medication for which this is a described interaction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference... subject) {
            for (Reference value : subject) {
                this.subject.add(value);
            }
            return this;
        }

        /**
         * The medication for which this is a described interaction.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProduct}</li>
         * <li>{@link Medication}</li>
         * <li>{@link Substance}</li>
         * </ul>
         * 
         * @param subject
         *     The medication for which this is a described interaction
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject = new ArrayList<>(subject);
            return this;
        }

        /**
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     The interaction described
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #description(com.ibm.fhir.model.type.String)
         */
        public Builder description(java.lang.String description) {
            this.description = (description == null) ? null : String.of(description);
            return this;
        }

        /**
         * The interaction described.
         * 
         * @param description
         *     The interaction described
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * The specific medication, food or laboratory test that interacts.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param interactant
         *     The specific medication, food or laboratory test that interacts
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder interactant(Interactant... interactant) {
            for (Interactant value : interactant) {
                this.interactant.add(value);
            }
            return this;
        }

        /**
         * The specific medication, food or laboratory test that interacts.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param interactant
         *     The specific medication, food or laboratory test that interacts
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder interactant(Collection<Interactant> interactant) {
            this.interactant = new ArrayList<>(interactant);
            return this;
        }

        /**
         * The type of the interaction e.g. drug-drug interaction, drug-food interaction, drug-lab test interaction.
         * 
         * @param type
         *     The type of the interaction e.g. drug-drug interaction, drug-food interaction, drug-lab test interaction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * The effect of the interaction, for example "reduced gastric absorption of primary medication".
         * 
         * @param effect
         *     The effect of the interaction, for example "reduced gastric absorption of primary medication"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effect(CodeableConcept effect) {
            this.effect = effect;
            return this;
        }

        /**
         * The incidence of the interaction, e.g. theoretical, observed.
         * 
         * @param incidence
         *     The incidence of the interaction, e.g. theoretical, observed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder incidence(CodeableConcept incidence) {
            this.incidence = incidence;
            return this;
        }

        /**
         * Actions for managing the interaction.
         * 
         * @param management
         *     Actions for managing the interaction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder management(CodeableConcept management) {
            this.management = management;
            return this;
        }

        /**
         * Build the {@link MedicinalProductInteraction}
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductInteraction}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductInteraction per the base specification
         */
        @Override
        public MedicinalProductInteraction build() {
            MedicinalProductInteraction medicinalProductInteraction = new MedicinalProductInteraction(this);
            if (validating) {
                validate(medicinalProductInteraction);
            }
            return medicinalProductInteraction;
        }

        protected void validate(MedicinalProductInteraction medicinalProductInteraction) {
            super.validate(medicinalProductInteraction);
            ValidationSupport.checkList(medicinalProductInteraction.subject, "subject", Reference.class);
            ValidationSupport.checkList(medicinalProductInteraction.interactant, "interactant", Interactant.class);
            ValidationSupport.checkReferenceType(medicinalProductInteraction.subject, "subject", "MedicinalProduct", "Medication", "Substance");
        }

        protected Builder from(MedicinalProductInteraction medicinalProductInteraction) {
            super.from(medicinalProductInteraction);
            subject.addAll(medicinalProductInteraction.subject);
            description = medicinalProductInteraction.description;
            interactant.addAll(medicinalProductInteraction.interactant);
            type = medicinalProductInteraction.type;
            effect = medicinalProductInteraction.effect;
            incidence = medicinalProductInteraction.incidence;
            management = medicinalProductInteraction.management;
            return this;
        }
    }

    /**
     * The specific medication, food or laboratory test that interacts.
     */
    public static class Interactant extends BackboneElement {
        @Summary
        @ReferenceTarget({ "MedicinalProduct", "Medication", "Substance", "ObservationDefinition" })
        @Choice({ Reference.class, CodeableConcept.class })
        @Required
        private final Element item;

        private Interactant(Builder builder) {
            super(builder);
            item = builder.item;
        }

        /**
         * The specific medication, food or laboratory test that interacts.
         * 
         * @return
         *     An immutable object of type {@link Reference} or {@link CodeableConcept} that is non-null.
         */
        public Element getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (item != null);
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
                    accept(item, "item", visitor);
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
            Interactant other = (Interactant) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    item);
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
            private Element item;

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
             * The specific medication, food or laboratory test that interacts.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Reference}</li>
             * <li>{@link CodeableConcept}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link MedicinalProduct}</li>
             * <li>{@link Medication}</li>
             * <li>{@link Substance}</li>
             * <li>{@link ObservationDefinition}</li>
             * </ul>
             * 
             * @param item
             *     The specific medication, food or laboratory test that interacts
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Element item) {
                this.item = item;
                return this;
            }

            /**
             * Build the {@link Interactant}
             * 
             * <p>Required elements:
             * <ul>
             * <li>item</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Interactant}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Interactant per the base specification
             */
            @Override
            public Interactant build() {
                Interactant interactant = new Interactant(this);
                if (validating) {
                    validate(interactant);
                }
                return interactant;
            }

            protected void validate(Interactant interactant) {
                super.validate(interactant);
                ValidationSupport.requireChoiceElement(interactant.item, "item", Reference.class, CodeableConcept.class);
                ValidationSupport.checkReferenceType(interactant.item, "item", "MedicinalProduct", "Medication", "Substance", "ObservationDefinition");
                ValidationSupport.requireValueOrChildren(interactant);
            }

            protected Builder from(Interactant interactant) {
                super.from(interactant);
                item = interactant.item;
                return this;
            }
        }
    }
}
