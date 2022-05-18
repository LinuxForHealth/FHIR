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
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.CodeableReference;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ClinicalUseDefinitionType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A single issue - either an indication, contraindication, interaction or an undesirable effect for a medicinal product, 
 * medication, device or procedure.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cud-1",
    level = "Rule",
    location = "(base)",
    description = "Indication, Contraindication, Interaction, UndesirableEffect and Warning cannot be used in the same instance",
    expression = "(ClinicalUseDefinition.indication.count() + ClinicalUseDefinition.contraindication.count() + ClinicalUseDefinition.interaction.count() + ClinicalUseDefinition.undesirableEffect.count() + ClinicalUseDefinition.warning.count())  < 2",
    source = "http://hl7.org/fhir/StructureDefinition/ClinicalUseDefinition"
)
@Constraint(
    id = "clinicalUseDefinition-2",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/clinical-use-definition-category",
    expression = "category.exists() implies (category.all(memberOf('http://hl7.org/fhir/ValueSet/clinical-use-definition-category', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/ClinicalUseDefinition",
    generated = true
)
@Constraint(
    id = "clinicalUseDefinition-3",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/publication-status",
    expression = "status.exists() implies (status.memberOf('http://hl7.org/fhir/ValueSet/publication-status', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/ClinicalUseDefinition",
    generated = true
)
@Constraint(
    id = "clinicalUseDefinition-4",
    level = "Warning",
    location = "contraindication.otherTherapy.relationshipType",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/therapy-relationship-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/therapy-relationship-type', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/ClinicalUseDefinition",
    generated = true
)
@Constraint(
    id = "clinicalUseDefinition-5",
    level = "Warning",
    location = "indication.intendedEffect",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/product-intended-use",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/product-intended-use', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/ClinicalUseDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ClinicalUseDefinition extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ClinicalUseDefinitionType",
        strength = BindingStrength.Value.REQUIRED,
        description = "Overall defining type of this clinical use definition.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinical-use-definition-type|4.3.0-cibuild"
    )
    @Required
    private final ClinicalUseDefinitionType type;
    @Summary
    @Binding(
        bindingName = "ClinicalUseDefinitionCategory",
        strength = BindingStrength.Value.PREFERRED,
        description = "A categorisation for a clinical use information item.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinical-use-definition-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @ReferenceTarget({ "MedicinalProductDefinition", "Medication", "ActivityDefinition", "PlanDefinition", "Device", "DeviceDefinition", "Substance" })
    private final List<Reference> subject;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.PREFERRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status"
    )
    private final CodeableConcept status;
    @Summary
    private final Contraindication contraindication;
    @Summary
    private final Indication indication;
    @Summary
    private final Interaction interaction;
    @Summary
    @ReferenceTarget({ "Group" })
    private final List<Reference> population;
    @Summary
    private final UndesirableEffect undesirableEffect;
    @Summary
    private final Warning warning;

    private ClinicalUseDefinition(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        type = builder.type;
        category = Collections.unmodifiableList(builder.category);
        subject = Collections.unmodifiableList(builder.subject);
        status = builder.status;
        contraindication = builder.contraindication;
        indication = builder.indication;
        interaction = builder.interaction;
        population = Collections.unmodifiableList(builder.population);
        undesirableEffect = builder.undesirableEffect;
        warning = builder.warning;
    }

    /**
     * Business identifier for this issue.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * indication | contraindication | interaction | undesirable-effect | warning.
     * 
     * @return
     *     An immutable object of type {@link ClinicalUseDefinitionType} that is non-null.
     */
    public ClinicalUseDefinitionType getType() {
        return type;
    }

    /**
     * A categorisation of the issue, primarily for dividing warnings into subject heading areas such as "Pregnancy and 
     * Lactation", "Overdose", "Effects on Ability to Drive and Use Machines".
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * The medication or procedure for which this is an indication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * Whether this is a current issue or one that has been retired etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * Specifics for when this is a contraindication.
     * 
     * @return
     *     An immutable object of type {@link Contraindication} that may be null.
     */
    public Contraindication getContraindication() {
        return contraindication;
    }

    /**
     * Specifics for when this is an indication.
     * 
     * @return
     *     An immutable object of type {@link Indication} that may be null.
     */
    public Indication getIndication() {
        return indication;
    }

    /**
     * Specifics for when this is an interaction.
     * 
     * @return
     *     An immutable object of type {@link Interaction} that may be null.
     */
    public Interaction getInteraction() {
        return interaction;
    }

    /**
     * The population group to which this applies.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPopulation() {
        return population;
    }

    /**
     * Describe the possible undesirable effects (negative outcomes) from the use of the medicinal product as treatment.
     * 
     * @return
     *     An immutable object of type {@link UndesirableEffect} that may be null.
     */
    public UndesirableEffect getUndesirableEffect() {
        return undesirableEffect;
    }

    /**
     * A critical piece of information about environmental, health or physical risks or hazards that serve as caution to the 
     * user. For example 'Do not operate heavy machinery', 'May cause drowsiness', or 'Get medical advice/attention if you 
     * feel unwell'.
     * 
     * @return
     *     An immutable object of type {@link Warning} that may be null.
     */
    public Warning getWarning() {
        return warning;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (type != null) || 
            !category.isEmpty() || 
            !subject.isEmpty() || 
            (status != null) || 
            (contraindication != null) || 
            (indication != null) || 
            (interaction != null) || 
            !population.isEmpty() || 
            (undesirableEffect != null) || 
            (warning != null);
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
                accept(type, "type", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(subject, "subject", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(contraindication, "contraindication", visitor);
                accept(indication, "indication", visitor);
                accept(interaction, "interaction", visitor);
                accept(population, "population", visitor, Reference.class);
                accept(undesirableEffect, "undesirableEffect", visitor);
                accept(warning, "warning", visitor);
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
        ClinicalUseDefinition other = (ClinicalUseDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(category, other.category) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(status, other.status) && 
            Objects.equals(contraindication, other.contraindication) && 
            Objects.equals(indication, other.indication) && 
            Objects.equals(interaction, other.interaction) && 
            Objects.equals(population, other.population) && 
            Objects.equals(undesirableEffect, other.undesirableEffect) && 
            Objects.equals(warning, other.warning);
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
                type, 
                category, 
                subject, 
                status, 
                contraindication, 
                indication, 
                interaction, 
                population, 
                undesirableEffect, 
                warning);
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
        private ClinicalUseDefinitionType type;
        private List<CodeableConcept> category = new ArrayList<>();
        private List<Reference> subject = new ArrayList<>();
        private CodeableConcept status;
        private Contraindication contraindication;
        private Indication indication;
        private Interaction interaction;
        private List<Reference> population = new ArrayList<>();
        private UndesirableEffect undesirableEffect;
        private Warning warning;

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
         * Business identifier for this issue.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for this issue
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
         * Business identifier for this issue.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for this issue
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
         * indication | contraindication | interaction | undesirable-effect | warning.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     indication | contraindication | interaction | undesirable-effect | warning
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(ClinicalUseDefinitionType type) {
            this.type = type;
            return this;
        }

        /**
         * A categorisation of the issue, primarily for dividing warnings into subject heading areas such as "Pregnancy and 
         * Lactation", "Overdose", "Effects on Ability to Drive and Use Machines".
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     A categorisation of the issue, primarily for dividing warnings into subject heading areas such as "Pregnancy", 
         *     "Overdose"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * A categorisation of the issue, primarily for dividing warnings into subject heading areas such as "Pregnancy and 
         * Lactation", "Overdose", "Effects on Ability to Drive and Use Machines".
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     A categorisation of the issue, primarily for dividing warnings into subject heading areas such as "Pregnancy", 
         *     "Overdose"
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * The medication or procedure for which this is an indication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * <li>{@link Medication}</li>
         * <li>{@link ActivityDefinition}</li>
         * <li>{@link PlanDefinition}</li>
         * <li>{@link Device}</li>
         * <li>{@link DeviceDefinition}</li>
         * <li>{@link Substance}</li>
         * </ul>
         * 
         * @param subject
         *     The medication or procedure for which this is an indication
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
         * The medication or procedure for which this is an indication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * <li>{@link Medication}</li>
         * <li>{@link ActivityDefinition}</li>
         * <li>{@link PlanDefinition}</li>
         * <li>{@link Device}</li>
         * <li>{@link DeviceDefinition}</li>
         * <li>{@link Substance}</li>
         * </ul>
         * 
         * @param subject
         *     The medication or procedure for which this is an indication
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
         * Whether this is a current issue or one that has been retired etc.
         * 
         * @param status
         *     Whether this is a current issue or one that has been retired etc
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * Specifics for when this is a contraindication.
         * 
         * @param contraindication
         *     Specifics for when this is a contraindication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contraindication(Contraindication contraindication) {
            this.contraindication = contraindication;
            return this;
        }

        /**
         * Specifics for when this is an indication.
         * 
         * @param indication
         *     Specifics for when this is an indication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder indication(Indication indication) {
            this.indication = indication;
            return this;
        }

        /**
         * Specifics for when this is an interaction.
         * 
         * @param interaction
         *     Specifics for when this is an interaction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder interaction(Interaction interaction) {
            this.interaction = interaction;
            return this;
        }

        /**
         * The population group to which this applies.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param population
         *     The population group to which this applies
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder population(Reference... population) {
            for (Reference value : population) {
                this.population.add(value);
            }
            return this;
        }

        /**
         * The population group to which this applies.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param population
         *     The population group to which this applies
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder population(Collection<Reference> population) {
            this.population = new ArrayList<>(population);
            return this;
        }

        /**
         * Describe the possible undesirable effects (negative outcomes) from the use of the medicinal product as treatment.
         * 
         * @param undesirableEffect
         *     A possible negative outcome from the use of this treatment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder undesirableEffect(UndesirableEffect undesirableEffect) {
            this.undesirableEffect = undesirableEffect;
            return this;
        }

        /**
         * A critical piece of information about environmental, health or physical risks or hazards that serve as caution to the 
         * user. For example 'Do not operate heavy machinery', 'May cause drowsiness', or 'Get medical advice/attention if you 
         * feel unwell'.
         * 
         * @param warning
         *     Critical environmental, health or physical risks or hazards. For example 'Do not operate heavy machinery', 'May cause 
         *     drowsiness'
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder warning(Warning warning) {
            this.warning = warning;
            return this;
        }

        /**
         * Build the {@link ClinicalUseDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ClinicalUseDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ClinicalUseDefinition per the base specification
         */
        @Override
        public ClinicalUseDefinition build() {
            ClinicalUseDefinition clinicalUseDefinition = new ClinicalUseDefinition(this);
            if (validating) {
                validate(clinicalUseDefinition);
            }
            return clinicalUseDefinition;
        }

        protected void validate(ClinicalUseDefinition clinicalUseDefinition) {
            super.validate(clinicalUseDefinition);
            ValidationSupport.checkList(clinicalUseDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(clinicalUseDefinition.type, "type");
            ValidationSupport.checkList(clinicalUseDefinition.category, "category", CodeableConcept.class);
            ValidationSupport.checkList(clinicalUseDefinition.subject, "subject", Reference.class);
            ValidationSupport.checkList(clinicalUseDefinition.population, "population", Reference.class);
            ValidationSupport.checkReferenceType(clinicalUseDefinition.subject, "subject", "MedicinalProductDefinition", "Medication", "ActivityDefinition", "PlanDefinition", "Device", "DeviceDefinition", "Substance");
            ValidationSupport.checkReferenceType(clinicalUseDefinition.population, "population", "Group");
        }

        protected Builder from(ClinicalUseDefinition clinicalUseDefinition) {
            super.from(clinicalUseDefinition);
            identifier.addAll(clinicalUseDefinition.identifier);
            type = clinicalUseDefinition.type;
            category.addAll(clinicalUseDefinition.category);
            subject.addAll(clinicalUseDefinition.subject);
            status = clinicalUseDefinition.status;
            contraindication = clinicalUseDefinition.contraindication;
            indication = clinicalUseDefinition.indication;
            interaction = clinicalUseDefinition.interaction;
            population.addAll(clinicalUseDefinition.population);
            undesirableEffect = clinicalUseDefinition.undesirableEffect;
            warning = clinicalUseDefinition.warning;
            return this;
        }
    }

    /**
     * Specifics for when this is a contraindication.
     */
    public static class Contraindication extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "DiseaseSymptomProcedure",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A symptom, disease or procedure.",
            valueSet = "http://hl7.org/fhir/ValueSet/disease-symptom-procedure"
        )
        private final CodeableReference diseaseSymptomProcedure;
        @Summary
        @Binding(
            bindingName = "DiseaseStatus",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The status of a disease or symptom.",
            valueSet = "http://hl7.org/fhir/ValueSet/disease-status"
        )
        private final CodeableReference diseaseStatus;
        @Summary
        @Binding(
            bindingName = "DiseaseSymptomProcedure",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A symptom, disease or procedure.",
            valueSet = "http://hl7.org/fhir/ValueSet/disease-symptom-procedure"
        )
        private final List<CodeableReference> comorbidity;
        @Summary
        @ReferenceTarget({ "ClinicalUseDefinition" })
        private final List<Reference> indication;
        @Summary
        private final List<OtherTherapy> otherTherapy;

        private Contraindication(Builder builder) {
            super(builder);
            diseaseSymptomProcedure = builder.diseaseSymptomProcedure;
            diseaseStatus = builder.diseaseStatus;
            comorbidity = Collections.unmodifiableList(builder.comorbidity);
            indication = Collections.unmodifiableList(builder.indication);
            otherTherapy = Collections.unmodifiableList(builder.otherTherapy);
        }

        /**
         * The situation that is being documented as contraindicating against this item.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getDiseaseSymptomProcedure() {
            return diseaseSymptomProcedure;
        }

        /**
         * The status of the disease or symptom for the contraindication, for example "chronic" or "metastatic".
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getDiseaseStatus() {
            return diseaseStatus;
        }

        /**
         * A comorbidity (concurrent condition) or coinfection.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableReference} that may be empty.
         */
        public List<CodeableReference> getComorbidity() {
            return comorbidity;
        }

        /**
         * The indication which this is a contraidication for.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getIndication() {
            return indication;
        }

        /**
         * Information about the use of the medicinal product in relation to other therapies described as part of the 
         * contraindication.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link OtherTherapy} that may be empty.
         */
        public List<OtherTherapy> getOtherTherapy() {
            return otherTherapy;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (diseaseSymptomProcedure != null) || 
                (diseaseStatus != null) || 
                !comorbidity.isEmpty() || 
                !indication.isEmpty() || 
                !otherTherapy.isEmpty();
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
                    accept(diseaseSymptomProcedure, "diseaseSymptomProcedure", visitor);
                    accept(diseaseStatus, "diseaseStatus", visitor);
                    accept(comorbidity, "comorbidity", visitor, CodeableReference.class);
                    accept(indication, "indication", visitor, Reference.class);
                    accept(otherTherapy, "otherTherapy", visitor, OtherTherapy.class);
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
            Contraindication other = (Contraindication) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(diseaseSymptomProcedure, other.diseaseSymptomProcedure) && 
                Objects.equals(diseaseStatus, other.diseaseStatus) && 
                Objects.equals(comorbidity, other.comorbidity) && 
                Objects.equals(indication, other.indication) && 
                Objects.equals(otherTherapy, other.otherTherapy);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    diseaseSymptomProcedure, 
                    diseaseStatus, 
                    comorbidity, 
                    indication, 
                    otherTherapy);
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
            private CodeableReference diseaseSymptomProcedure;
            private CodeableReference diseaseStatus;
            private List<CodeableReference> comorbidity = new ArrayList<>();
            private List<Reference> indication = new ArrayList<>();
            private List<OtherTherapy> otherTherapy = new ArrayList<>();

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
             * The situation that is being documented as contraindicating against this item.
             * 
             * @param diseaseSymptomProcedure
             *     The situation that is being documented as contraindicating against this item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diseaseSymptomProcedure(CodeableReference diseaseSymptomProcedure) {
                this.diseaseSymptomProcedure = diseaseSymptomProcedure;
                return this;
            }

            /**
             * The status of the disease or symptom for the contraindication, for example "chronic" or "metastatic".
             * 
             * @param diseaseStatus
             *     The status of the disease or symptom for the contraindication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diseaseStatus(CodeableReference diseaseStatus) {
                this.diseaseStatus = diseaseStatus;
                return this;
            }

            /**
             * A comorbidity (concurrent condition) or coinfection.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param comorbidity
             *     A comorbidity (concurrent condition) or coinfection
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comorbidity(CodeableReference... comorbidity) {
                for (CodeableReference value : comorbidity) {
                    this.comorbidity.add(value);
                }
                return this;
            }

            /**
             * A comorbidity (concurrent condition) or coinfection.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param comorbidity
             *     A comorbidity (concurrent condition) or coinfection
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder comorbidity(Collection<CodeableReference> comorbidity) {
                this.comorbidity = new ArrayList<>(comorbidity);
                return this;
            }

            /**
             * The indication which this is a contraidication for.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link ClinicalUseDefinition}</li>
             * </ul>
             * 
             * @param indication
             *     The indication which this is a contraidication for
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder indication(Reference... indication) {
                for (Reference value : indication) {
                    this.indication.add(value);
                }
                return this;
            }

            /**
             * The indication which this is a contraidication for.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link ClinicalUseDefinition}</li>
             * </ul>
             * 
             * @param indication
             *     The indication which this is a contraidication for
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder indication(Collection<Reference> indication) {
                this.indication = new ArrayList<>(indication);
                return this;
            }

            /**
             * Information about the use of the medicinal product in relation to other therapies described as part of the 
             * contraindication.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param otherTherapy
             *     Information about use of the product in relation to other therapies described as part of the contraindication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder otherTherapy(OtherTherapy... otherTherapy) {
                for (OtherTherapy value : otherTherapy) {
                    this.otherTherapy.add(value);
                }
                return this;
            }

            /**
             * Information about the use of the medicinal product in relation to other therapies described as part of the 
             * contraindication.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param otherTherapy
             *     Information about use of the product in relation to other therapies described as part of the contraindication
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder otherTherapy(Collection<OtherTherapy> otherTherapy) {
                this.otherTherapy = new ArrayList<>(otherTherapy);
                return this;
            }

            /**
             * Build the {@link Contraindication}
             * 
             * @return
             *     An immutable object of type {@link Contraindication}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Contraindication per the base specification
             */
            @Override
            public Contraindication build() {
                Contraindication contraindication = new Contraindication(this);
                if (validating) {
                    validate(contraindication);
                }
                return contraindication;
            }

            protected void validate(Contraindication contraindication) {
                super.validate(contraindication);
                ValidationSupport.checkList(contraindication.comorbidity, "comorbidity", CodeableReference.class);
                ValidationSupport.checkList(contraindication.indication, "indication", Reference.class);
                ValidationSupport.checkList(contraindication.otherTherapy, "otherTherapy", OtherTherapy.class);
                ValidationSupport.checkReferenceType(contraindication.indication, "indication", "ClinicalUseDefinition");
                ValidationSupport.requireValueOrChildren(contraindication);
            }

            protected Builder from(Contraindication contraindication) {
                super.from(contraindication);
                diseaseSymptomProcedure = contraindication.diseaseSymptomProcedure;
                diseaseStatus = contraindication.diseaseStatus;
                comorbidity.addAll(contraindication.comorbidity);
                indication.addAll(contraindication.indication);
                otherTherapy.addAll(contraindication.otherTherapy);
                return this;
            }
        }

        /**
         * Information about the use of the medicinal product in relation to other therapies described as part of the 
         * contraindication.
         */
        public static class OtherTherapy extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "TherapyRelationshipType",
                strength = BindingStrength.Value.PREFERRED,
                description = "Classification of relationship between a therapy and a contraindication or an indication.",
                valueSet = "http://hl7.org/fhir/ValueSet/therapy-relationship-type"
            )
            @Required
            private final CodeableConcept relationshipType;
            @Summary
            @Binding(
                bindingName = "Therapy",
                strength = BindingStrength.Value.EXAMPLE,
                description = "A therapy.",
                valueSet = "http://hl7.org/fhir/ValueSet/therapy"
            )
            @Required
            private final CodeableReference therapy;

            private OtherTherapy(Builder builder) {
                super(builder);
                relationshipType = builder.relationshipType;
                therapy = builder.therapy;
            }

            /**
             * The type of relationship between the medicinal product indication or contraindication and another therapy.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getRelationshipType() {
                return relationshipType;
            }

            /**
             * Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication 
             * or contraindication.
             * 
             * @return
             *     An immutable object of type {@link CodeableReference} that is non-null.
             */
            public CodeableReference getTherapy() {
                return therapy;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (relationshipType != null) || 
                    (therapy != null);
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
                        accept(relationshipType, "relationshipType", visitor);
                        accept(therapy, "therapy", visitor);
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
                OtherTherapy other = (OtherTherapy) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(relationshipType, other.relationshipType) && 
                    Objects.equals(therapy, other.therapy);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        relationshipType, 
                        therapy);
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
                private CodeableConcept relationshipType;
                private CodeableReference therapy;

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
                 * The type of relationship between the medicinal product indication or contraindication and another therapy.
                 * 
                 * <p>This element is required.
                 * 
                 * @param relationshipType
                 *     The type of relationship between the product indication/contraindication and another therapy
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder relationshipType(CodeableConcept relationshipType) {
                    this.relationshipType = relationshipType;
                    return this;
                }

                /**
                 * Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication 
                 * or contraindication.
                 * 
                 * <p>This element is required.
                 * 
                 * @param therapy
                 *     Reference to a specific medication as part of an indication or contraindication
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder therapy(CodeableReference therapy) {
                    this.therapy = therapy;
                    return this;
                }

                /**
                 * Build the {@link OtherTherapy}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>relationshipType</li>
                 * <li>therapy</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link OtherTherapy}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid OtherTherapy per the base specification
                 */
                @Override
                public OtherTherapy build() {
                    OtherTherapy otherTherapy = new OtherTherapy(this);
                    if (validating) {
                        validate(otherTherapy);
                    }
                    return otherTherapy;
                }

                protected void validate(OtherTherapy otherTherapy) {
                    super.validate(otherTherapy);
                    ValidationSupport.requireNonNull(otherTherapy.relationshipType, "relationshipType");
                    ValidationSupport.requireNonNull(otherTherapy.therapy, "therapy");
                    ValidationSupport.requireValueOrChildren(otherTherapy);
                }

                protected Builder from(OtherTherapy otherTherapy) {
                    super.from(otherTherapy);
                    relationshipType = otherTherapy.relationshipType;
                    therapy = otherTherapy.therapy;
                    return this;
                }
            }
        }
    }

    /**
     * Specifics for when this is an indication.
     */
    public static class Indication extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "DiseaseSymptomProcedure",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A symptom, disease or procedure.",
            valueSet = "http://hl7.org/fhir/ValueSet/disease-symptom-procedure"
        )
        private final CodeableReference diseaseSymptomProcedure;
        @Summary
        @Binding(
            bindingName = "DiseaseStatus",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The status of a disease or symptom.",
            valueSet = "http://hl7.org/fhir/ValueSet/disease-status"
        )
        private final CodeableReference diseaseStatus;
        @Summary
        @Binding(
            bindingName = "DiseaseSymptomProcedure",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A symptom, disease or procedure.",
            valueSet = "http://hl7.org/fhir/ValueSet/disease-symptom-procedure"
        )
        private final List<CodeableReference> comorbidity;
        @Summary
        @Binding(
            bindingName = "ProductIntendedUse",
            strength = BindingStrength.Value.PREFERRED,
            description = "The overall intended use of a product.",
            valueSet = "http://hl7.org/fhir/ValueSet/product-intended-use"
        )
        private final CodeableReference intendedEffect;
        @Summary
        @Choice({ Range.class, String.class })
        private final Element duration;
        @Summary
        @ReferenceTarget({ "ClinicalUseDefinition" })
        private final List<Reference> undesirableEffect;
        @Summary
        private final List<ClinicalUseDefinition.Contraindication.OtherTherapy> otherTherapy;

        private Indication(Builder builder) {
            super(builder);
            diseaseSymptomProcedure = builder.diseaseSymptomProcedure;
            diseaseStatus = builder.diseaseStatus;
            comorbidity = Collections.unmodifiableList(builder.comorbidity);
            intendedEffect = builder.intendedEffect;
            duration = builder.duration;
            undesirableEffect = Collections.unmodifiableList(builder.undesirableEffect);
            otherTherapy = Collections.unmodifiableList(builder.otherTherapy);
        }

        /**
         * The situation that is being documented as an indicaton for this item.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getDiseaseSymptomProcedure() {
            return diseaseSymptomProcedure;
        }

        /**
         * The status of the disease or symptom for the indication, for example "chronic" or "metastatic".
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getDiseaseStatus() {
            return diseaseStatus;
        }

        /**
         * A comorbidity (concurrent condition) or coinfection as part of the indication.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableReference} that may be empty.
         */
        public List<CodeableReference> getComorbidity() {
            return comorbidity;
        }

        /**
         * The intended effect, aim or strategy to be achieved.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getIntendedEffect() {
            return intendedEffect;
        }

        /**
         * Timing or duration information, that may be associated with use with the indicated condition e.g. Adult patients 
         * suffering from myocardial infarction (from a few days until less than 35 days), ischaemic stroke (from 7 days until 
         * less than 6 months).
         * 
         * @return
         *     An immutable object of type {@link Range} or {@link String} that may be null.
         */
        public Element getDuration() {
            return duration;
        }

        /**
         * An unwanted side effect or negative outcome that may happen if you use the drug (or other subject of this resource) 
         * for this indication.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getUndesirableEffect() {
            return undesirableEffect;
        }

        /**
         * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link OtherTherapy} that may be empty.
         */
        public List<ClinicalUseDefinition.Contraindication.OtherTherapy> getOtherTherapy() {
            return otherTherapy;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (diseaseSymptomProcedure != null) || 
                (diseaseStatus != null) || 
                !comorbidity.isEmpty() || 
                (intendedEffect != null) || 
                (duration != null) || 
                !undesirableEffect.isEmpty() || 
                !otherTherapy.isEmpty();
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
                    accept(diseaseSymptomProcedure, "diseaseSymptomProcedure", visitor);
                    accept(diseaseStatus, "diseaseStatus", visitor);
                    accept(comorbidity, "comorbidity", visitor, CodeableReference.class);
                    accept(intendedEffect, "intendedEffect", visitor);
                    accept(duration, "duration", visitor);
                    accept(undesirableEffect, "undesirableEffect", visitor, Reference.class);
                    accept(otherTherapy, "otherTherapy", visitor, ClinicalUseDefinition.Contraindication.OtherTherapy.class);
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
            Indication other = (Indication) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(diseaseSymptomProcedure, other.diseaseSymptomProcedure) && 
                Objects.equals(diseaseStatus, other.diseaseStatus) && 
                Objects.equals(comorbidity, other.comorbidity) && 
                Objects.equals(intendedEffect, other.intendedEffect) && 
                Objects.equals(duration, other.duration) && 
                Objects.equals(undesirableEffect, other.undesirableEffect) && 
                Objects.equals(otherTherapy, other.otherTherapy);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    diseaseSymptomProcedure, 
                    diseaseStatus, 
                    comorbidity, 
                    intendedEffect, 
                    duration, 
                    undesirableEffect, 
                    otherTherapy);
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
            private CodeableReference diseaseSymptomProcedure;
            private CodeableReference diseaseStatus;
            private List<CodeableReference> comorbidity = new ArrayList<>();
            private CodeableReference intendedEffect;
            private Element duration;
            private List<Reference> undesirableEffect = new ArrayList<>();
            private List<ClinicalUseDefinition.Contraindication.OtherTherapy> otherTherapy = new ArrayList<>();

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
             * The situation that is being documented as an indicaton for this item.
             * 
             * @param diseaseSymptomProcedure
             *     The situation that is being documented as an indicaton for this item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diseaseSymptomProcedure(CodeableReference diseaseSymptomProcedure) {
                this.diseaseSymptomProcedure = diseaseSymptomProcedure;
                return this;
            }

            /**
             * The status of the disease or symptom for the indication, for example "chronic" or "metastatic".
             * 
             * @param diseaseStatus
             *     The status of the disease or symptom for the indication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diseaseStatus(CodeableReference diseaseStatus) {
                this.diseaseStatus = diseaseStatus;
                return this;
            }

            /**
             * A comorbidity (concurrent condition) or coinfection as part of the indication.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param comorbidity
             *     A comorbidity or coinfection as part of the indication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comorbidity(CodeableReference... comorbidity) {
                for (CodeableReference value : comorbidity) {
                    this.comorbidity.add(value);
                }
                return this;
            }

            /**
             * A comorbidity (concurrent condition) or coinfection as part of the indication.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param comorbidity
             *     A comorbidity or coinfection as part of the indication
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder comorbidity(Collection<CodeableReference> comorbidity) {
                this.comorbidity = new ArrayList<>(comorbidity);
                return this;
            }

            /**
             * The intended effect, aim or strategy to be achieved.
             * 
             * @param intendedEffect
             *     The intended effect, aim or strategy to be achieved
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder intendedEffect(CodeableReference intendedEffect) {
                this.intendedEffect = intendedEffect;
                return this;
            }

            /**
             * Convenience method for setting {@code duration} with choice type String.
             * 
             * @param duration
             *     Timing or duration information
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #duration(Element)
             */
            public Builder duration(java.lang.String duration) {
                this.duration = (duration == null) ? null : String.of(duration);
                return this;
            }

            /**
             * Timing or duration information, that may be associated with use with the indicated condition e.g. Adult patients 
             * suffering from myocardial infarction (from a few days until less than 35 days), ischaemic stroke (from 7 days until 
             * less than 6 months).
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Range}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param duration
             *     Timing or duration information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder duration(Element duration) {
                this.duration = duration;
                return this;
            }

            /**
             * An unwanted side effect or negative outcome that may happen if you use the drug (or other subject of this resource) 
             * for this indication.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link ClinicalUseDefinition}</li>
             * </ul>
             * 
             * @param undesirableEffect
             *     An unwanted side effect or negative outcome of the subject of this resource when being used for this indication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder undesirableEffect(Reference... undesirableEffect) {
                for (Reference value : undesirableEffect) {
                    this.undesirableEffect.add(value);
                }
                return this;
            }

            /**
             * An unwanted side effect or negative outcome that may happen if you use the drug (or other subject of this resource) 
             * for this indication.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link ClinicalUseDefinition}</li>
             * </ul>
             * 
             * @param undesirableEffect
             *     An unwanted side effect or negative outcome of the subject of this resource when being used for this indication
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder undesirableEffect(Collection<Reference> undesirableEffect) {
                this.undesirableEffect = new ArrayList<>(undesirableEffect);
                return this;
            }

            /**
             * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param otherTherapy
             *     The use of the medicinal product in relation to other therapies described as part of the indication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder otherTherapy(ClinicalUseDefinition.Contraindication.OtherTherapy... otherTherapy) {
                for (ClinicalUseDefinition.Contraindication.OtherTherapy value : otherTherapy) {
                    this.otherTherapy.add(value);
                }
                return this;
            }

            /**
             * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param otherTherapy
             *     The use of the medicinal product in relation to other therapies described as part of the indication
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder otherTherapy(Collection<ClinicalUseDefinition.Contraindication.OtherTherapy> otherTherapy) {
                this.otherTherapy = new ArrayList<>(otherTherapy);
                return this;
            }

            /**
             * Build the {@link Indication}
             * 
             * @return
             *     An immutable object of type {@link Indication}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Indication per the base specification
             */
            @Override
            public Indication build() {
                Indication indication = new Indication(this);
                if (validating) {
                    validate(indication);
                }
                return indication;
            }

            protected void validate(Indication indication) {
                super.validate(indication);
                ValidationSupport.checkList(indication.comorbidity, "comorbidity", CodeableReference.class);
                ValidationSupport.choiceElement(indication.duration, "duration", Range.class, String.class);
                ValidationSupport.checkList(indication.undesirableEffect, "undesirableEffect", Reference.class);
                ValidationSupport.checkList(indication.otherTherapy, "otherTherapy", ClinicalUseDefinition.Contraindication.OtherTherapy.class);
                ValidationSupport.checkReferenceType(indication.undesirableEffect, "undesirableEffect", "ClinicalUseDefinition");
                ValidationSupport.requireValueOrChildren(indication);
            }

            protected Builder from(Indication indication) {
                super.from(indication);
                diseaseSymptomProcedure = indication.diseaseSymptomProcedure;
                diseaseStatus = indication.diseaseStatus;
                comorbidity.addAll(indication.comorbidity);
                intendedEffect = indication.intendedEffect;
                duration = indication.duration;
                undesirableEffect.addAll(indication.undesirableEffect);
                otherTherapy.addAll(indication.otherTherapy);
                return this;
            }
        }
    }

    /**
     * Specifics for when this is an interaction.
     */
    public static class Interaction extends BackboneElement {
        @Summary
        private final List<Interactant> interactant;
        @Summary
        @Binding(
            bindingName = "InteractionType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A categorisation for an interaction between two substances.",
            valueSet = "http://hl7.org/fhir/ValueSet/interaction-type"
        )
        private final CodeableConcept type;
        @Summary
        @Binding(
            bindingName = "InteractionEffect",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A interaction effect of clinical use of a medication or other substance.",
            valueSet = "http://hl7.org/fhir/ValueSet/interaction-effect"
        )
        private final CodeableReference effect;
        @Summary
        @Binding(
            bindingName = "UndesirableEffectSymptom",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A categorisation for incidence of occurence of an interaction.",
            valueSet = "http://hl7.org/fhir/ValueSet/interaction-incidence"
        )
        private final CodeableConcept incidence;
        @Summary
        @Binding(
            bindingName = "InteractionManagement",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A type of management for an interaction of a medication or other substance.",
            valueSet = "http://hl7.org/fhir/ValueSet/interaction-management"
        )
        private final List<CodeableConcept> management;

        private Interaction(Builder builder) {
            super(builder);
            interactant = Collections.unmodifiableList(builder.interactant);
            type = builder.type;
            effect = builder.effect;
            incidence = builder.incidence;
            management = Collections.unmodifiableList(builder.management);
        }

        /**
         * The specific medication, food, substance or laboratory test that interacts.
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
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getEffect() {
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
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getManagement() {
            return management;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !interactant.isEmpty() || 
                (type != null) || 
                (effect != null) || 
                (incidence != null) || 
                !management.isEmpty();
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
                    accept(interactant, "interactant", visitor, Interactant.class);
                    accept(type, "type", visitor);
                    accept(effect, "effect", visitor);
                    accept(incidence, "incidence", visitor);
                    accept(management, "management", visitor, CodeableConcept.class);
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
            Interaction other = (Interaction) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
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
                    extension, 
                    modifierExtension, 
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

        public static class Builder extends BackboneElement.Builder {
            private List<Interactant> interactant = new ArrayList<>();
            private CodeableConcept type;
            private CodeableReference effect;
            private CodeableConcept incidence;
            private List<CodeableConcept> management = new ArrayList<>();

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
             * The specific medication, food, substance or laboratory test that interacts.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param interactant
             *     The specific medication, food, substance or laboratory test that interacts
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
             * The specific medication, food, substance or laboratory test that interacts.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param interactant
             *     The specific medication, food, substance or laboratory test that interacts
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
             *     The type of the interaction e.g. drug-drug interaction, drug-lab test interaction
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
            public Builder effect(CodeableReference effect) {
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
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param management
             *     Actions for managing the interaction
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder management(CodeableConcept... management) {
                for (CodeableConcept value : management) {
                    this.management.add(value);
                }
                return this;
            }

            /**
             * Actions for managing the interaction.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param management
             *     Actions for managing the interaction
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder management(Collection<CodeableConcept> management) {
                this.management = new ArrayList<>(management);
                return this;
            }

            /**
             * Build the {@link Interaction}
             * 
             * @return
             *     An immutable object of type {@link Interaction}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Interaction per the base specification
             */
            @Override
            public Interaction build() {
                Interaction interaction = new Interaction(this);
                if (validating) {
                    validate(interaction);
                }
                return interaction;
            }

            protected void validate(Interaction interaction) {
                super.validate(interaction);
                ValidationSupport.checkList(interaction.interactant, "interactant", Interactant.class);
                ValidationSupport.checkList(interaction.management, "management", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(interaction);
            }

            protected Builder from(Interaction interaction) {
                super.from(interaction);
                interactant.addAll(interaction.interactant);
                type = interaction.type;
                effect = interaction.effect;
                incidence = interaction.incidence;
                management.addAll(interaction.management);
                return this;
            }
        }

        /**
         * The specific medication, food, substance or laboratory test that interacts.
         */
        public static class Interactant extends BackboneElement {
            @Summary
            @ReferenceTarget({ "MedicinalProductDefinition", "Medication", "Substance", "ObservationDefinition" })
            @Choice({ Reference.class, CodeableConcept.class })
            @Binding(
                bindingName = "Interactant",
                strength = BindingStrength.Value.EXAMPLE,
                description = "An interactant - a substance that may have an clinically significant effect on another.",
                valueSet = "http://hl7.org/fhir/ValueSet/interactant"
            )
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
                 * <li>{@link MedicinalProductDefinition}</li>
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
                    ValidationSupport.checkReferenceType(interactant.item, "item", "MedicinalProductDefinition", "Medication", "Substance", "ObservationDefinition");
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

    /**
     * Describe the possible undesirable effects (negative outcomes) from the use of the medicinal product as treatment.
     */
    public static class UndesirableEffect extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "UndesirableEffectSymptom",
            strength = BindingStrength.Value.EXAMPLE,
            description = "An undesirable effect of clinical use.",
            valueSet = "http://hl7.org/fhir/ValueSet/undesirable-effect-symptom"
        )
        private final CodeableReference symptomConditionEffect;
        @Summary
        @Binding(
            bindingName = "UndesirableEffectClassification",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A categorisation for an undesirable effect.",
            valueSet = "http://hl7.org/fhir/ValueSet/undesirable-effect-classification"
        )
        private final CodeableConcept classification;
        @Summary
        @Binding(
            bindingName = "UndesirablEffectFrequency",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A categorisation for a frequency of occurence of an undesirable effect.",
            valueSet = "http://hl7.org/fhir/ValueSet/undesirable-effect-frequency"
        )
        private final CodeableConcept frequencyOfOccurrence;

        private UndesirableEffect(Builder builder) {
            super(builder);
            symptomConditionEffect = builder.symptomConditionEffect;
            classification = builder.classification;
            frequencyOfOccurrence = builder.frequencyOfOccurrence;
        }

        /**
         * The situation in which the undesirable effect may manifest.
         * 
         * @return
         *     An immutable object of type {@link CodeableReference} that may be null.
         */
        public CodeableReference getSymptomConditionEffect() {
            return symptomConditionEffect;
        }

        /**
         * High level classification of the effect.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getClassification() {
            return classification;
        }

        /**
         * How often the effect is seen.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFrequencyOfOccurrence() {
            return frequencyOfOccurrence;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (symptomConditionEffect != null) || 
                (classification != null) || 
                (frequencyOfOccurrence != null);
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
                    accept(symptomConditionEffect, "symptomConditionEffect", visitor);
                    accept(classification, "classification", visitor);
                    accept(frequencyOfOccurrence, "frequencyOfOccurrence", visitor);
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
            UndesirableEffect other = (UndesirableEffect) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(symptomConditionEffect, other.symptomConditionEffect) && 
                Objects.equals(classification, other.classification) && 
                Objects.equals(frequencyOfOccurrence, other.frequencyOfOccurrence);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    symptomConditionEffect, 
                    classification, 
                    frequencyOfOccurrence);
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
            private CodeableReference symptomConditionEffect;
            private CodeableConcept classification;
            private CodeableConcept frequencyOfOccurrence;

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
             * The situation in which the undesirable effect may manifest.
             * 
             * @param symptomConditionEffect
             *     The situation in which the undesirable effect may manifest
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder symptomConditionEffect(CodeableReference symptomConditionEffect) {
                this.symptomConditionEffect = symptomConditionEffect;
                return this;
            }

            /**
             * High level classification of the effect.
             * 
             * @param classification
             *     High level classification of the effect
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder classification(CodeableConcept classification) {
                this.classification = classification;
                return this;
            }

            /**
             * How often the effect is seen.
             * 
             * @param frequencyOfOccurrence
             *     How often the effect is seen
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder frequencyOfOccurrence(CodeableConcept frequencyOfOccurrence) {
                this.frequencyOfOccurrence = frequencyOfOccurrence;
                return this;
            }

            /**
             * Build the {@link UndesirableEffect}
             * 
             * @return
             *     An immutable object of type {@link UndesirableEffect}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid UndesirableEffect per the base specification
             */
            @Override
            public UndesirableEffect build() {
                UndesirableEffect undesirableEffect = new UndesirableEffect(this);
                if (validating) {
                    validate(undesirableEffect);
                }
                return undesirableEffect;
            }

            protected void validate(UndesirableEffect undesirableEffect) {
                super.validate(undesirableEffect);
                ValidationSupport.requireValueOrChildren(undesirableEffect);
            }

            protected Builder from(UndesirableEffect undesirableEffect) {
                super.from(undesirableEffect);
                symptomConditionEffect = undesirableEffect.symptomConditionEffect;
                classification = undesirableEffect.classification;
                frequencyOfOccurrence = undesirableEffect.frequencyOfOccurrence;
                return this;
            }
        }
    }

    /**
     * A critical piece of information about environmental, health or physical risks or hazards that serve as caution to the 
     * user. For example 'Do not operate heavy machinery', 'May cause drowsiness', or 'Get medical advice/attention if you 
     * feel unwell'.
     */
    public static class Warning extends BackboneElement {
        @Summary
        private final Markdown description;
        @Summary
        @Binding(
            bindingName = "WarningType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Classification of warning type.",
            valueSet = "http://hl7.org/fhir/ValueSet/warning-type"
        )
        private final CodeableConcept code;

        private Warning(Builder builder) {
            super(builder);
            description = builder.description;
            code = builder.code;
        }

        /**
         * A textual definition of this warning, with formatting.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        /**
         * A coded or unformatted textual definition of this warning.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (code != null);
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
                    accept(description, "description", visitor);
                    accept(code, "code", visitor);
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
            Warning other = (Warning) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(code, other.code);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    code);
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
            private Markdown description;
            private CodeableConcept code;

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
             * A textual definition of this warning, with formatting.
             * 
             * @param description
             *     A textual definition of this warning, with formatting
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * A coded or unformatted textual definition of this warning.
             * 
             * @param code
             *     A coded or unformatted textual definition of this warning
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Build the {@link Warning}
             * 
             * @return
             *     An immutable object of type {@link Warning}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Warning per the base specification
             */
            @Override
            public Warning build() {
                Warning warning = new Warning(this);
                if (validating) {
                    validate(warning);
                }
                return warning;
            }

            protected void validate(Warning warning) {
                super.validate(warning);
                ValidationSupport.requireValueOrChildren(warning);
            }

            protected Builder from(Warning warning) {
                super.from(warning);
                description = warning.description;
                code = warning.code;
                return this;
            }
        }
    }
}
