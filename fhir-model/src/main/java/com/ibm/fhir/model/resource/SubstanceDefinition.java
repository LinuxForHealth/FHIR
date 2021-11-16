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
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
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
 * The detailed description of a substance, typically at a level beyond what is used for prescribing.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubstanceDefinition extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String version;
    @Summary
    private final CodeableConcept status;
    @Summary
    private final List<CodeableConcept> classification;
    @Summary
    private final CodeableConcept domain;
    @Summary
    private final List<CodeableConcept> grade;
    @Summary
    private final Markdown description;
    @Summary
    @ReferenceTarget({ "Citation" })
    private final List<Reference> informationSource;
    @Summary
    private final List<Annotation> note;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> manufacturer;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> supplier;
    @Summary
    private final List<Moiety> moiety;
    @Summary
    private final List<Property> property;
    @Summary
    private final List<MolecularWeight> molecularWeight;
    @Summary
    private final Structure structure;
    @Summary
    private final List<Code> code;
    @Summary
    private final List<Name> name;
    @Summary
    private final List<Relationship> relationship;
    @Summary
    private final SourceMaterial sourceMaterial;

    private SubstanceDefinition(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        status = builder.status;
        classification = Collections.unmodifiableList(builder.classification);
        domain = builder.domain;
        grade = Collections.unmodifiableList(builder.grade);
        description = builder.description;
        informationSource = Collections.unmodifiableList(builder.informationSource);
        note = Collections.unmodifiableList(builder.note);
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        supplier = Collections.unmodifiableList(builder.supplier);
        moiety = Collections.unmodifiableList(builder.moiety);
        property = Collections.unmodifiableList(builder.property);
        molecularWeight = Collections.unmodifiableList(builder.molecularWeight);
        structure = builder.structure;
        code = Collections.unmodifiableList(builder.code);
        name = Collections.unmodifiableList(builder.name);
        relationship = Collections.unmodifiableList(builder.relationship);
        sourceMaterial = builder.sourceMaterial;
    }

    /**
     * Identifier by which this substance is known.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A business level identifier of the substance.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Status of substance within the catalogue e.g. approved.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * A high level categorization, e.g. polymer or nucleic acid, or food, chemical, biological, or a lower level such as the 
     * general types of polymer (linear or branch chain) or type of impurity (process related or contaminant).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getClassification() {
        return classification;
    }

    /**
     * If the substance applies to only human or veterinary use.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDomain() {
        return domain;
    }

    /**
     * The quality standard, established benchmark, to which substance complies (e.g. USP/NF, Ph. Eur, JP, BP, Company 
     * Standard).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getGrade() {
        return grade;
    }

    /**
     * Textual description of the substance.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * Supporting literature.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getInformationSource() {
        return informationSource;
    }

    /**
     * Textual comment about the substance's catalogue or registry record.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * A company that makes this substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * A company that supplies this substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupplier() {
        return supplier;
    }

    /**
     * Moiety, for structural modifications.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Moiety} that may be empty.
     */
    public List<Moiety> getMoiety() {
        return moiety;
    }

    /**
     * General specifications for this substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Property} that may be empty.
     */
    public List<Property> getProperty() {
        return property;
    }

    /**
     * The molecular weight or weight range (for proteins, polymers or nucleic acids).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MolecularWeight} that may be empty.
     */
    public List<MolecularWeight> getMolecularWeight() {
        return molecularWeight;
    }

    /**
     * Structural information.
     * 
     * @return
     *     An immutable object of type {@link Structure} that may be null.
     */
    public Structure getStructure() {
        return structure;
    }

    /**
     * Codes associated with the substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Code} that may be empty.
     */
    public List<Code> getCode() {
        return code;
    }

    /**
     * Names applicable to this substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Name} that may be empty.
     */
    public List<Name> getName() {
        return name;
    }

    /**
     * A link between this substance and another, with details of the relationship.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Relationship} that may be empty.
     */
    public List<Relationship> getRelationship() {
        return relationship;
    }

    /**
     * Material or taxonomic/anatomical source for the substance.
     * 
     * @return
     *     An immutable object of type {@link SourceMaterial} that may be null.
     */
    public SourceMaterial getSourceMaterial() {
        return sourceMaterial;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (version != null) || 
            (status != null) || 
            !classification.isEmpty() || 
            (domain != null) || 
            !grade.isEmpty() || 
            (description != null) || 
            !informationSource.isEmpty() || 
            !note.isEmpty() || 
            !manufacturer.isEmpty() || 
            !supplier.isEmpty() || 
            !moiety.isEmpty() || 
            !property.isEmpty() || 
            !molecularWeight.isEmpty() || 
            (structure != null) || 
            !code.isEmpty() || 
            !name.isEmpty() || 
            !relationship.isEmpty() || 
            (sourceMaterial != null);
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
                accept(version, "version", visitor);
                accept(status, "status", visitor);
                accept(classification, "classification", visitor, CodeableConcept.class);
                accept(domain, "domain", visitor);
                accept(grade, "grade", visitor, CodeableConcept.class);
                accept(description, "description", visitor);
                accept(informationSource, "informationSource", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(manufacturer, "manufacturer", visitor, Reference.class);
                accept(supplier, "supplier", visitor, Reference.class);
                accept(moiety, "moiety", visitor, Moiety.class);
                accept(property, "property", visitor, Property.class);
                accept(molecularWeight, "molecularWeight", visitor, MolecularWeight.class);
                accept(structure, "structure", visitor);
                accept(code, "code", visitor, Code.class);
                accept(name, "name", visitor, Name.class);
                accept(relationship, "relationship", visitor, Relationship.class);
                accept(sourceMaterial, "sourceMaterial", visitor);
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
        SubstanceDefinition other = (SubstanceDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(version, other.version) && 
            Objects.equals(status, other.status) && 
            Objects.equals(classification, other.classification) && 
            Objects.equals(domain, other.domain) && 
            Objects.equals(grade, other.grade) && 
            Objects.equals(description, other.description) && 
            Objects.equals(informationSource, other.informationSource) && 
            Objects.equals(note, other.note) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(supplier, other.supplier) && 
            Objects.equals(moiety, other.moiety) && 
            Objects.equals(property, other.property) && 
            Objects.equals(molecularWeight, other.molecularWeight) && 
            Objects.equals(structure, other.structure) && 
            Objects.equals(code, other.code) && 
            Objects.equals(name, other.name) && 
            Objects.equals(relationship, other.relationship) && 
            Objects.equals(sourceMaterial, other.sourceMaterial);
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
                version, 
                status, 
                classification, 
                domain, 
                grade, 
                description, 
                informationSource, 
                note, 
                manufacturer, 
                supplier, 
                moiety, 
                property, 
                molecularWeight, 
                structure, 
                code, 
                name, 
                relationship, 
                sourceMaterial);
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
        private String version;
        private CodeableConcept status;
        private List<CodeableConcept> classification = new ArrayList<>();
        private CodeableConcept domain;
        private List<CodeableConcept> grade = new ArrayList<>();
        private Markdown description;
        private List<Reference> informationSource = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Reference> manufacturer = new ArrayList<>();
        private List<Reference> supplier = new ArrayList<>();
        private List<Moiety> moiety = new ArrayList<>();
        private List<Property> property = new ArrayList<>();
        private List<MolecularWeight> molecularWeight = new ArrayList<>();
        private Structure structure;
        private List<Code> code = new ArrayList<>();
        private List<Name> name = new ArrayList<>();
        private List<Relationship> relationship = new ArrayList<>();
        private SourceMaterial sourceMaterial;

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
        public Builder language(com.ibm.fhir.model.type.Code language) {
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
         * Identifier by which this substance is known.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier by which this substance is known
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
         * Identifier by which this substance is known.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier by which this substance is known
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
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     A business level identifier of the substance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(com.ibm.fhir.model.type.String)
         */
        public Builder version(java.lang.String version) {
            this.version = (version == null) ? null : String.of(version);
            return this;
        }

        /**
         * A business level identifier of the substance.
         * 
         * @param version
         *     A business level identifier of the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Status of substance within the catalogue e.g. approved.
         * 
         * @param status
         *     Status of substance within the catalogue e.g. approved
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * A high level categorization, e.g. polymer or nucleic acid, or food, chemical, biological, or a lower level such as the 
         * general types of polymer (linear or branch chain) or type of impurity (process related or contaminant).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     A high level categorization, e.g. polymer or nucleic acid, or food, chemical, biological, or a lower level such as the 
         *     general types of polymer (linear or branch chain) or type of impurity (process related or contaminant)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder classification(CodeableConcept... classification) {
            for (CodeableConcept value : classification) {
                this.classification.add(value);
            }
            return this;
        }

        /**
         * A high level categorization, e.g. polymer or nucleic acid, or food, chemical, biological, or a lower level such as the 
         * general types of polymer (linear or branch chain) or type of impurity (process related or contaminant).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     A high level categorization, e.g. polymer or nucleic acid, or food, chemical, biological, or a lower level such as the 
         *     general types of polymer (linear or branch chain) or type of impurity (process related or contaminant)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder classification(Collection<CodeableConcept> classification) {
            this.classification = new ArrayList<>(classification);
            return this;
        }

        /**
         * If the substance applies to only human or veterinary use.
         * 
         * @param domain
         *     If the substance applies to only human or veterinary use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder domain(CodeableConcept domain) {
            this.domain = domain;
            return this;
        }

        /**
         * The quality standard, established benchmark, to which substance complies (e.g. USP/NF, Ph. Eur, JP, BP, Company 
         * Standard).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param grade
         *     The quality standard, established benchmark, to which substance complies (e.g. USP/NF, Ph. Eur, JP, BP, Company 
         *     Standard)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder grade(CodeableConcept... grade) {
            for (CodeableConcept value : grade) {
                this.grade.add(value);
            }
            return this;
        }

        /**
         * The quality standard, established benchmark, to which substance complies (e.g. USP/NF, Ph. Eur, JP, BP, Company 
         * Standard).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param grade
         *     The quality standard, established benchmark, to which substance complies (e.g. USP/NF, Ph. Eur, JP, BP, Company 
         *     Standard)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder grade(Collection<CodeableConcept> grade) {
            this.grade = new ArrayList<>(grade);
            return this;
        }

        /**
         * Textual description of the substance.
         * 
         * @param description
         *     Textual description of the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * Supporting literature.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Citation}</li>
         * </ul>
         * 
         * @param informationSource
         *     Supporting literature
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder informationSource(Reference... informationSource) {
            for (Reference value : informationSource) {
                this.informationSource.add(value);
            }
            return this;
        }

        /**
         * Supporting literature.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Citation}</li>
         * </ul>
         * 
         * @param informationSource
         *     Supporting literature
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder informationSource(Collection<Reference> informationSource) {
            this.informationSource = new ArrayList<>(informationSource);
            return this;
        }

        /**
         * Textual comment about the substance's catalogue or registry record.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Textual comment about the substance's catalogue or registry record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * Textual comment about the substance's catalogue or registry record.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Textual comment about the substance's catalogue or registry record
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * A company that makes this substance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param manufacturer
         *     A company that makes this substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(Reference... manufacturer) {
            for (Reference value : manufacturer) {
                this.manufacturer.add(value);
            }
            return this;
        }

        /**
         * A company that makes this substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param manufacturer
         *     A company that makes this substance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder manufacturer(Collection<Reference> manufacturer) {
            this.manufacturer = new ArrayList<>(manufacturer);
            return this;
        }

        /**
         * A company that supplies this substance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param supplier
         *     A company that supplies this substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supplier(Reference... supplier) {
            for (Reference value : supplier) {
                this.supplier.add(value);
            }
            return this;
        }

        /**
         * A company that supplies this substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param supplier
         *     A company that supplies this substance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder supplier(Collection<Reference> supplier) {
            this.supplier = new ArrayList<>(supplier);
            return this;
        }

        /**
         * Moiety, for structural modifications.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param moiety
         *     Moiety, for structural modifications
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder moiety(Moiety... moiety) {
            for (Moiety value : moiety) {
                this.moiety.add(value);
            }
            return this;
        }

        /**
         * Moiety, for structural modifications.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param moiety
         *     Moiety, for structural modifications
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder moiety(Collection<Moiety> moiety) {
            this.moiety = new ArrayList<>(moiety);
            return this;
        }

        /**
         * General specifications for this substance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param property
         *     General specifications for this substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder property(Property... property) {
            for (Property value : property) {
                this.property.add(value);
            }
            return this;
        }

        /**
         * General specifications for this substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param property
         *     General specifications for this substance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder property(Collection<Property> property) {
            this.property = new ArrayList<>(property);
            return this;
        }

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param molecularWeight
         *     The molecular weight or weight range (for proteins, polymers or nucleic acids)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder molecularWeight(MolecularWeight... molecularWeight) {
            for (MolecularWeight value : molecularWeight) {
                this.molecularWeight.add(value);
            }
            return this;
        }

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param molecularWeight
         *     The molecular weight or weight range (for proteins, polymers or nucleic acids)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder molecularWeight(Collection<MolecularWeight> molecularWeight) {
            this.molecularWeight = new ArrayList<>(molecularWeight);
            return this;
        }

        /**
         * Structural information.
         * 
         * @param structure
         *     Structural information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder structure(Structure structure) {
            this.structure = structure;
            return this;
        }

        /**
         * Codes associated with the substance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param code
         *     Codes associated with the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Code... code) {
            for (Code value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * Codes associated with the substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param code
         *     Codes associated with the substance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder code(Collection<Code> code) {
            this.code = new ArrayList<>(code);
            return this;
        }

        /**
         * Names applicable to this substance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param name
         *     Names applicable to this substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(Name... name) {
            for (Name value : name) {
                this.name.add(value);
            }
            return this;
        }

        /**
         * Names applicable to this substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param name
         *     Names applicable to this substance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder name(Collection<Name> name) {
            this.name = new ArrayList<>(name);
            return this;
        }

        /**
         * A link between this substance and another, with details of the relationship.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relationship
         *     A link between this substance and another, with details of the relationship
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relationship(Relationship... relationship) {
            for (Relationship value : relationship) {
                this.relationship.add(value);
            }
            return this;
        }

        /**
         * A link between this substance and another, with details of the relationship.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relationship
         *     A link between this substance and another, with details of the relationship
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relationship(Collection<Relationship> relationship) {
            this.relationship = new ArrayList<>(relationship);
            return this;
        }

        /**
         * Material or taxonomic/anatomical source for the substance.
         * 
         * @param sourceMaterial
         *     Material or taxonomic/anatomical source for the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sourceMaterial(SourceMaterial sourceMaterial) {
            this.sourceMaterial = sourceMaterial;
            return this;
        }

        /**
         * Build the {@link SubstanceDefinition}
         * 
         * @return
         *     An immutable object of type {@link SubstanceDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstanceDefinition per the base specification
         */
        @Override
        public SubstanceDefinition build() {
            SubstanceDefinition substanceDefinition = new SubstanceDefinition(this);
            if (validating) {
                validate(substanceDefinition);
            }
            return substanceDefinition;
        }

        protected void validate(SubstanceDefinition substanceDefinition) {
            super.validate(substanceDefinition);
            ValidationSupport.checkList(substanceDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(substanceDefinition.classification, "classification", CodeableConcept.class);
            ValidationSupport.checkList(substanceDefinition.grade, "grade", CodeableConcept.class);
            ValidationSupport.checkList(substanceDefinition.informationSource, "informationSource", Reference.class);
            ValidationSupport.checkList(substanceDefinition.note, "note", Annotation.class);
            ValidationSupport.checkList(substanceDefinition.manufacturer, "manufacturer", Reference.class);
            ValidationSupport.checkList(substanceDefinition.supplier, "supplier", Reference.class);
            ValidationSupport.checkList(substanceDefinition.moiety, "moiety", Moiety.class);
            ValidationSupport.checkList(substanceDefinition.property, "property", Property.class);
            ValidationSupport.checkList(substanceDefinition.molecularWeight, "molecularWeight", MolecularWeight.class);
            ValidationSupport.checkList(substanceDefinition.code, "code", Code.class);
            ValidationSupport.checkList(substanceDefinition.name, "name", Name.class);
            ValidationSupport.checkList(substanceDefinition.relationship, "relationship", Relationship.class);
            ValidationSupport.checkReferenceType(substanceDefinition.informationSource, "informationSource", "Citation");
            ValidationSupport.checkReferenceType(substanceDefinition.manufacturer, "manufacturer", "Organization");
            ValidationSupport.checkReferenceType(substanceDefinition.supplier, "supplier", "Organization");
        }

        protected Builder from(SubstanceDefinition substanceDefinition) {
            super.from(substanceDefinition);
            identifier.addAll(substanceDefinition.identifier);
            version = substanceDefinition.version;
            status = substanceDefinition.status;
            classification.addAll(substanceDefinition.classification);
            domain = substanceDefinition.domain;
            grade.addAll(substanceDefinition.grade);
            description = substanceDefinition.description;
            informationSource.addAll(substanceDefinition.informationSource);
            note.addAll(substanceDefinition.note);
            manufacturer.addAll(substanceDefinition.manufacturer);
            supplier.addAll(substanceDefinition.supplier);
            moiety.addAll(substanceDefinition.moiety);
            property.addAll(substanceDefinition.property);
            molecularWeight.addAll(substanceDefinition.molecularWeight);
            structure = substanceDefinition.structure;
            code.addAll(substanceDefinition.code);
            name.addAll(substanceDefinition.name);
            relationship.addAll(substanceDefinition.relationship);
            sourceMaterial = substanceDefinition.sourceMaterial;
            return this;
        }
    }

    /**
     * Moiety, for structural modifications.
     */
    public static class Moiety extends BackboneElement {
        @Summary
        private final CodeableConcept role;
        @Summary
        private final Identifier identifier;
        @Summary
        private final String name;
        @Summary
        private final CodeableConcept stereochemistry;
        @Summary
        private final CodeableConcept opticalActivity;
        @Summary
        private final String molecularFormula;
        @Summary
        @Choice({ Quantity.class, String.class })
        private final Element amount;
        @Summary
        private final CodeableConcept amountType;

        private Moiety(Builder builder) {
            super(builder);
            role = builder.role;
            identifier = builder.identifier;
            name = builder.name;
            stereochemistry = builder.stereochemistry;
            opticalActivity = builder.opticalActivity;
            molecularFormula = builder.molecularFormula;
            amount = builder.amount;
            amountType = builder.amountType;
        }

        /**
         * Role that the moiety is playing.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRole() {
            return role;
        }

        /**
         * Identifier by which this moiety substance is known.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * Textual name for this moiety substance.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Stereochemistry type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStereochemistry() {
            return stereochemistry;
        }

        /**
         * Optical activity type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOpticalActivity() {
            return opticalActivity;
        }

        /**
         * Molecular formula for this moiety of this substance, typically using the Hill system.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getMolecularFormula() {
            return molecularFormula;
        }

        /**
         * Quantitative value for this moiety.
         * 
         * @return
         *     An immutable object of type {@link Quantity} or {@link String} that may be null.
         */
        public Element getAmount() {
            return amount;
        }

        /**
         * The measurement type of the quantitative value.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getAmountType() {
            return amountType;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (role != null) || 
                (identifier != null) || 
                (name != null) || 
                (stereochemistry != null) || 
                (opticalActivity != null) || 
                (molecularFormula != null) || 
                (amount != null) || 
                (amountType != null);
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
                    accept(identifier, "identifier", visitor);
                    accept(name, "name", visitor);
                    accept(stereochemistry, "stereochemistry", visitor);
                    accept(opticalActivity, "opticalActivity", visitor);
                    accept(molecularFormula, "molecularFormula", visitor);
                    accept(amount, "amount", visitor);
                    accept(amountType, "amountType", visitor);
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
            Moiety other = (Moiety) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(role, other.role) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(name, other.name) && 
                Objects.equals(stereochemistry, other.stereochemistry) && 
                Objects.equals(opticalActivity, other.opticalActivity) && 
                Objects.equals(molecularFormula, other.molecularFormula) && 
                Objects.equals(amount, other.amount) && 
                Objects.equals(amountType, other.amountType);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    role, 
                    identifier, 
                    name, 
                    stereochemistry, 
                    opticalActivity, 
                    molecularFormula, 
                    amount, 
                    amountType);
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
            private CodeableConcept role;
            private Identifier identifier;
            private String name;
            private CodeableConcept stereochemistry;
            private CodeableConcept opticalActivity;
            private String molecularFormula;
            private Element amount;
            private CodeableConcept amountType;

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
             * Role that the moiety is playing.
             * 
             * @param role
             *     Role that the moiety is playing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(CodeableConcept role) {
                this.role = role;
                return this;
            }

            /**
             * Identifier by which this moiety substance is known.
             * 
             * @param identifier
             *     Identifier by which this moiety substance is known
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Textual name for this moiety substance
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
             * Textual name for this moiety substance.
             * 
             * @param name
             *     Textual name for this moiety substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Stereochemistry type.
             * 
             * @param stereochemistry
             *     Stereochemistry type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder stereochemistry(CodeableConcept stereochemistry) {
                this.stereochemistry = stereochemistry;
                return this;
            }

            /**
             * Optical activity type.
             * 
             * @param opticalActivity
             *     Optical activity type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder opticalActivity(CodeableConcept opticalActivity) {
                this.opticalActivity = opticalActivity;
                return this;
            }

            /**
             * Convenience method for setting {@code molecularFormula}.
             * 
             * @param molecularFormula
             *     Molecular formula for this moiety of this substance, typically using the Hill system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #molecularFormula(com.ibm.fhir.model.type.String)
             */
            public Builder molecularFormula(java.lang.String molecularFormula) {
                this.molecularFormula = (molecularFormula == null) ? null : String.of(molecularFormula);
                return this;
            }

            /**
             * Molecular formula for this moiety of this substance, typically using the Hill system.
             * 
             * @param molecularFormula
             *     Molecular formula for this moiety of this substance, typically using the Hill system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularFormula(String molecularFormula) {
                this.molecularFormula = molecularFormula;
                return this;
            }

            /**
             * Convenience method for setting {@code amount} with choice type String.
             * 
             * @param amount
             *     Quantitative value for this moiety
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #amount(Element)
             */
            public Builder amount(java.lang.String amount) {
                this.amount = (amount == null) ? null : String.of(amount);
                return this;
            }

            /**
             * Quantitative value for this moiety.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param amount
             *     Quantitative value for this moiety
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Element amount) {
                this.amount = amount;
                return this;
            }

            /**
             * The measurement type of the quantitative value.
             * 
             * @param amountType
             *     The measurement type of the quantitative value
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amountType(CodeableConcept amountType) {
                this.amountType = amountType;
                return this;
            }

            /**
             * Build the {@link Moiety}
             * 
             * @return
             *     An immutable object of type {@link Moiety}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Moiety per the base specification
             */
            @Override
            public Moiety build() {
                Moiety moiety = new Moiety(this);
                if (validating) {
                    validate(moiety);
                }
                return moiety;
            }

            protected void validate(Moiety moiety) {
                super.validate(moiety);
                ValidationSupport.choiceElement(moiety.amount, "amount", Quantity.class, String.class);
                ValidationSupport.requireValueOrChildren(moiety);
            }

            protected Builder from(Moiety moiety) {
                super.from(moiety);
                role = moiety.role;
                identifier = moiety.identifier;
                name = moiety.name;
                stereochemistry = moiety.stereochemistry;
                opticalActivity = moiety.opticalActivity;
                molecularFormula = moiety.molecularFormula;
                amount = moiety.amount;
                amountType = moiety.amountType;
                return this;
            }
        }
    }

    /**
     * General specifications for this substance.
     */
    public static class Property extends BackboneElement {
        @Summary
        @Required
        private final CodeableConcept type;
        @Summary
        @Choice({ CodeableConcept.class, Quantity.class, Date.class, Boolean.class, Attachment.class })
        private final Element value;

        private Property(Builder builder) {
            super(builder);
            type = builder.type;
            value = builder.value;
        }

        /**
         * A code expressing the type of characteristic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * A value for the characteristic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Quantity}, {@link Date}, {@link Boolean} or {@link 
         *     Attachment} that may be null.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (value != null);
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
                    accept(type, "type", visitor);
                    accept(value, "value", visitor);
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
            Property other = (Property) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    value);
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
            private CodeableConcept type;
            private Element value;

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
             * A code expressing the type of characteristic.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     A code expressing the type of characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Date.
             * 
             * @param value
             *     A value for the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalDate value) {
                this.value = (value == null) ? null : Date.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * @param value
             *     A value for the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Boolean value) {
                this.value = (value == null) ? null : Boolean.of(value);
                return this;
            }

            /**
             * A value for the characteristic.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Date}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Attachment}</li>
             * </ul>
             * 
             * @param value
             *     A value for the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Property}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Property}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Property per the base specification
             */
            @Override
            public Property build() {
                Property property = new Property(this);
                if (validating) {
                    validate(property);
                }
                return property;
            }

            protected void validate(Property property) {
                super.validate(property);
                ValidationSupport.requireNonNull(property.type, "type");
                ValidationSupport.choiceElement(property.value, "value", CodeableConcept.class, Quantity.class, Date.class, Boolean.class, Attachment.class);
                ValidationSupport.requireValueOrChildren(property);
            }

            protected Builder from(Property property) {
                super.from(property);
                type = property.type;
                value = property.value;
                return this;
            }
        }
    }

    /**
     * The molecular weight or weight range (for proteins, polymers or nucleic acids).
     */
    public static class MolecularWeight extends BackboneElement {
        @Summary
        private final CodeableConcept method;
        @Summary
        private final CodeableConcept type;
        @Summary
        @Required
        private final Quantity amount;

        private MolecularWeight(Builder builder) {
            super(builder);
            method = builder.method;
            type = builder.type;
            amount = builder.amount;
        }

        /**
         * The method by which the molecular weight was determined.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getMethod() {
            return method;
        }

        /**
         * Type of molecular weight such as exact, average (also known as. number average), weight average.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
         * the average. If only a single definite value for a given element is given, it would be captured in this field.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that is non-null.
         */
        public Quantity getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (method != null) || 
                (type != null) || 
                (amount != null);
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
                    accept(method, "method", visitor);
                    accept(type, "type", visitor);
                    accept(amount, "amount", visitor);
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
            MolecularWeight other = (MolecularWeight) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(method, other.method) && 
                Objects.equals(type, other.type) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    method, 
                    type, 
                    amount);
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
            private CodeableConcept method;
            private CodeableConcept type;
            private Quantity amount;

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
             * The method by which the molecular weight was determined.
             * 
             * @param method
             *     The method by which the molecular weight was determined
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder method(CodeableConcept method) {
                this.method = method;
                return this;
            }

            /**
             * Type of molecular weight such as exact, average (also known as. number average), weight average.
             * 
             * @param type
             *     Type of molecular weight such as exact, average (also known as. number average), weight average
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
             * the average. If only a single definite value for a given element is given, it would be captured in this field.
             * 
             * <p>This element is required.
             * 
             * @param amount
             *     Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
             *     the average. If only a single definite value for a given element is given, it would be captured in this field
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Quantity amount) {
                this.amount = amount;
                return this;
            }

            /**
             * Build the {@link MolecularWeight}
             * 
             * <p>Required elements:
             * <ul>
             * <li>amount</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link MolecularWeight}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid MolecularWeight per the base specification
             */
            @Override
            public MolecularWeight build() {
                MolecularWeight molecularWeight = new MolecularWeight(this);
                if (validating) {
                    validate(molecularWeight);
                }
                return molecularWeight;
            }

            protected void validate(MolecularWeight molecularWeight) {
                super.validate(molecularWeight);
                ValidationSupport.requireNonNull(molecularWeight.amount, "amount");
                ValidationSupport.requireValueOrChildren(molecularWeight);
            }

            protected Builder from(MolecularWeight molecularWeight) {
                super.from(molecularWeight);
                method = molecularWeight.method;
                type = molecularWeight.type;
                amount = molecularWeight.amount;
                return this;
            }
        }
    }

    /**
     * Structural information.
     */
    public static class Structure extends BackboneElement {
        @Summary
        private final CodeableConcept stereochemistry;
        @Summary
        private final CodeableConcept opticalActivity;
        @Summary
        private final String molecularFormula;
        @Summary
        private final String molecularFormulaByMoiety;
        @Summary
        private final SubstanceDefinition.MolecularWeight molecularWeight;
        @Summary
        private final List<CodeableConcept> technique;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> sourceDocument;
        @Summary
        private final List<Representation> representation;

        private Structure(Builder builder) {
            super(builder);
            stereochemistry = builder.stereochemistry;
            opticalActivity = builder.opticalActivity;
            molecularFormula = builder.molecularFormula;
            molecularFormulaByMoiety = builder.molecularFormulaByMoiety;
            molecularWeight = builder.molecularWeight;
            technique = Collections.unmodifiableList(builder.technique);
            sourceDocument = Collections.unmodifiableList(builder.sourceDocument);
            representation = Collections.unmodifiableList(builder.representation);
        }

        /**
         * Stereochemistry type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStereochemistry() {
            return stereochemistry;
        }

        /**
         * Optical activity type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOpticalActivity() {
            return opticalActivity;
        }

        /**
         * Molecular formula of this substance, typically using the Hill system.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getMolecularFormula() {
            return molecularFormula;
        }

        /**
         * Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a 
         * dot.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getMolecularFormulaByMoiety() {
            return molecularFormulaByMoiety;
        }

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         * 
         * @return
         *     An immutable object of type {@link SubstanceDefinition.MolecularWeight} that may be null.
         */
        public SubstanceDefinition.MolecularWeight getMolecularWeight() {
            return molecularWeight;
        }

        /**
         * The method used to elucidate the structure or characterization of the drug substance. Examples: X-ray, HPLC, NMR, 
         * Peptide mapping, Ligand binding assay.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getTechnique() {
            return technique;
        }

        /**
         * Supporting literature about the source of information.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSourceDocument() {
            return sourceDocument;
        }

        /**
         * A depiction of the structure or characterization of the substance.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Representation} that may be empty.
         */
        public List<Representation> getRepresentation() {
            return representation;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (stereochemistry != null) || 
                (opticalActivity != null) || 
                (molecularFormula != null) || 
                (molecularFormulaByMoiety != null) || 
                (molecularWeight != null) || 
                !technique.isEmpty() || 
                !sourceDocument.isEmpty() || 
                !representation.isEmpty();
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
                    accept(stereochemistry, "stereochemistry", visitor);
                    accept(opticalActivity, "opticalActivity", visitor);
                    accept(molecularFormula, "molecularFormula", visitor);
                    accept(molecularFormulaByMoiety, "molecularFormulaByMoiety", visitor);
                    accept(molecularWeight, "molecularWeight", visitor);
                    accept(technique, "technique", visitor, CodeableConcept.class);
                    accept(sourceDocument, "sourceDocument", visitor, Reference.class);
                    accept(representation, "representation", visitor, Representation.class);
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
            Structure other = (Structure) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(stereochemistry, other.stereochemistry) && 
                Objects.equals(opticalActivity, other.opticalActivity) && 
                Objects.equals(molecularFormula, other.molecularFormula) && 
                Objects.equals(molecularFormulaByMoiety, other.molecularFormulaByMoiety) && 
                Objects.equals(molecularWeight, other.molecularWeight) && 
                Objects.equals(technique, other.technique) && 
                Objects.equals(sourceDocument, other.sourceDocument) && 
                Objects.equals(representation, other.representation);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    stereochemistry, 
                    opticalActivity, 
                    molecularFormula, 
                    molecularFormulaByMoiety, 
                    molecularWeight, 
                    technique, 
                    sourceDocument, 
                    representation);
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
            private CodeableConcept stereochemistry;
            private CodeableConcept opticalActivity;
            private String molecularFormula;
            private String molecularFormulaByMoiety;
            private SubstanceDefinition.MolecularWeight molecularWeight;
            private List<CodeableConcept> technique = new ArrayList<>();
            private List<Reference> sourceDocument = new ArrayList<>();
            private List<Representation> representation = new ArrayList<>();

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
             * Stereochemistry type.
             * 
             * @param stereochemistry
             *     Stereochemistry type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder stereochemistry(CodeableConcept stereochemistry) {
                this.stereochemistry = stereochemistry;
                return this;
            }

            /**
             * Optical activity type.
             * 
             * @param opticalActivity
             *     Optical activity type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder opticalActivity(CodeableConcept opticalActivity) {
                this.opticalActivity = opticalActivity;
                return this;
            }

            /**
             * Convenience method for setting {@code molecularFormula}.
             * 
             * @param molecularFormula
             *     Molecular formula of this substance, typically using the Hill system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #molecularFormula(com.ibm.fhir.model.type.String)
             */
            public Builder molecularFormula(java.lang.String molecularFormula) {
                this.molecularFormula = (molecularFormula == null) ? null : String.of(molecularFormula);
                return this;
            }

            /**
             * Molecular formula of this substance, typically using the Hill system.
             * 
             * @param molecularFormula
             *     Molecular formula of this substance, typically using the Hill system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularFormula(String molecularFormula) {
                this.molecularFormula = molecularFormula;
                return this;
            }

            /**
             * Convenience method for setting {@code molecularFormulaByMoiety}.
             * 
             * @param molecularFormulaByMoiety
             *     Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a 
             *     dot
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #molecularFormulaByMoiety(com.ibm.fhir.model.type.String)
             */
            public Builder molecularFormulaByMoiety(java.lang.String molecularFormulaByMoiety) {
                this.molecularFormulaByMoiety = (molecularFormulaByMoiety == null) ? null : String.of(molecularFormulaByMoiety);
                return this;
            }

            /**
             * Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a 
             * dot.
             * 
             * @param molecularFormulaByMoiety
             *     Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a 
             *     dot
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularFormulaByMoiety(String molecularFormulaByMoiety) {
                this.molecularFormulaByMoiety = molecularFormulaByMoiety;
                return this;
            }

            /**
             * The molecular weight or weight range (for proteins, polymers or nucleic acids).
             * 
             * @param molecularWeight
             *     The molecular weight or weight range (for proteins, polymers or nucleic acids)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularWeight(SubstanceDefinition.MolecularWeight molecularWeight) {
                this.molecularWeight = molecularWeight;
                return this;
            }

            /**
             * The method used to elucidate the structure or characterization of the drug substance. Examples: X-ray, HPLC, NMR, 
             * Peptide mapping, Ligand binding assay.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param technique
             *     The method used to elucidate the structure or characterization of the drug substance. Examples: X-ray, HPLC, NMR, 
             *     Peptide mapping, Ligand binding assay
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder technique(CodeableConcept... technique) {
                for (CodeableConcept value : technique) {
                    this.technique.add(value);
                }
                return this;
            }

            /**
             * The method used to elucidate the structure or characterization of the drug substance. Examples: X-ray, HPLC, NMR, 
             * Peptide mapping, Ligand binding assay.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param technique
             *     The method used to elucidate the structure or characterization of the drug substance. Examples: X-ray, HPLC, NMR, 
             *     Peptide mapping, Ligand binding assay
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder technique(Collection<CodeableConcept> technique) {
                this.technique = new ArrayList<>(technique);
                return this;
            }

            /**
             * Supporting literature about the source of information.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param sourceDocument
             *     Supporting literature about the source of information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sourceDocument(Reference... sourceDocument) {
                for (Reference value : sourceDocument) {
                    this.sourceDocument.add(value);
                }
                return this;
            }

            /**
             * Supporting literature about the source of information.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param sourceDocument
             *     Supporting literature about the source of information
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder sourceDocument(Collection<Reference> sourceDocument) {
                this.sourceDocument = new ArrayList<>(sourceDocument);
                return this;
            }

            /**
             * A depiction of the structure or characterization of the substance.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param representation
             *     A depiction of the structure or characterization of the substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder representation(Representation... representation) {
                for (Representation value : representation) {
                    this.representation.add(value);
                }
                return this;
            }

            /**
             * A depiction of the structure or characterization of the substance.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param representation
             *     A depiction of the structure or characterization of the substance
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder representation(Collection<Representation> representation) {
                this.representation = new ArrayList<>(representation);
                return this;
            }

            /**
             * Build the {@link Structure}
             * 
             * @return
             *     An immutable object of type {@link Structure}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Structure per the base specification
             */
            @Override
            public Structure build() {
                Structure structure = new Structure(this);
                if (validating) {
                    validate(structure);
                }
                return structure;
            }

            protected void validate(Structure structure) {
                super.validate(structure);
                ValidationSupport.checkList(structure.technique, "technique", CodeableConcept.class);
                ValidationSupport.checkList(structure.sourceDocument, "sourceDocument", Reference.class);
                ValidationSupport.checkList(structure.representation, "representation", Representation.class);
                ValidationSupport.checkReferenceType(structure.sourceDocument, "sourceDocument", "DocumentReference");
                ValidationSupport.requireValueOrChildren(structure);
            }

            protected Builder from(Structure structure) {
                super.from(structure);
                stereochemistry = structure.stereochemistry;
                opticalActivity = structure.opticalActivity;
                molecularFormula = structure.molecularFormula;
                molecularFormulaByMoiety = structure.molecularFormulaByMoiety;
                molecularWeight = structure.molecularWeight;
                technique.addAll(structure.technique);
                sourceDocument.addAll(structure.sourceDocument);
                representation.addAll(structure.representation);
                return this;
            }
        }

        /**
         * A depiction of the structure or characterization of the substance.
         */
        public static class Representation extends BackboneElement {
            @Summary
            private final CodeableConcept type;
            @Summary
            private final String representation;
            @Summary
            private final CodeableConcept format;
            @Summary
            @ReferenceTarget({ "DocumentReference" })
            private final Reference document;

            private Representation(Builder builder) {
                super(builder);
                type = builder.type;
                representation = builder.representation;
                format = builder.format;
                document = builder.document;
            }

            /**
             * The kind of structural representation (e.g. full, partial) or the technique used to derive the analytical 
             * characterization of the substance (e.g. x-ray, HPLC, NMR, peptide mapping, ligand binding assay, etc.).
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * The structural representation or characterization as a text string in a standard format.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getRepresentation() {
                return representation;
            }

            /**
             * The format of the representation e.g. InChI, SMILES, MOLFILE, CDX, SDF, PDB, mmCIF. The logical content type rather 
             * than the physical file format of a document.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getFormat() {
                return format;
            }

            /**
             * An attached file with the structural representation or characterization e.g. a molecular structure graphic of the 
             * substance, a JCAMP or AnIML file.
             * 
             * @return
             *     An immutable object of type {@link Reference} that may be null.
             */
            public Reference getDocument() {
                return document;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (representation != null) || 
                    (format != null) || 
                    (document != null);
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
                        accept(type, "type", visitor);
                        accept(representation, "representation", visitor);
                        accept(format, "format", visitor);
                        accept(document, "document", visitor);
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
                Representation other = (Representation) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(representation, other.representation) && 
                    Objects.equals(format, other.format) && 
                    Objects.equals(document, other.document);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        representation, 
                        format, 
                        document);
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
                private CodeableConcept type;
                private String representation;
                private CodeableConcept format;
                private Reference document;

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
                 * The kind of structural representation (e.g. full, partial) or the technique used to derive the analytical 
                 * characterization of the substance (e.g. x-ray, HPLC, NMR, peptide mapping, ligand binding assay, etc.).
                 * 
                 * @param type
                 *     The kind of structural representation (e.g. full, partial) or the technique used to derive the analytical 
                 *     characterization of the substance (e.g. x-ray, HPLC, NMR, peptide mapping, ligand binding assay, etc.)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Convenience method for setting {@code representation}.
                 * 
                 * @param representation
                 *     The structural representation or characterization as a text string in a standard format
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #representation(com.ibm.fhir.model.type.String)
                 */
                public Builder representation(java.lang.String representation) {
                    this.representation = (representation == null) ? null : String.of(representation);
                    return this;
                }

                /**
                 * The structural representation or characterization as a text string in a standard format.
                 * 
                 * @param representation
                 *     The structural representation or characterization as a text string in a standard format
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder representation(String representation) {
                    this.representation = representation;
                    return this;
                }

                /**
                 * The format of the representation e.g. InChI, SMILES, MOLFILE, CDX, SDF, PDB, mmCIF. The logical content type rather 
                 * than the physical file format of a document.
                 * 
                 * @param format
                 *     The format of the representation e.g. InChI, SMILES, MOLFILE, CDX, SDF, PDB, mmCIF. The logical content type rather 
                 *     than the physical file format of a document
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder format(CodeableConcept format) {
                    this.format = format;
                    return this;
                }

                /**
                 * An attached file with the structural representation or characterization e.g. a molecular structure graphic of the 
                 * substance, a JCAMP or AnIML file.
                 * 
                 * <p>Allowed resource types for this reference:
                 * <ul>
                 * <li>{@link DocumentReference}</li>
                 * </ul>
                 * 
                 * @param document
                 *     An attached file with the structural representation or characterization e.g. a molecular structure graphic of the 
                 *     substance, a JCAMP or AnIML file
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder document(Reference document) {
                    this.document = document;
                    return this;
                }

                /**
                 * Build the {@link Representation}
                 * 
                 * @return
                 *     An immutable object of type {@link Representation}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Representation per the base specification
                 */
                @Override
                public Representation build() {
                    Representation representation = new Representation(this);
                    if (validating) {
                        validate(representation);
                    }
                    return representation;
                }

                protected void validate(Representation representation) {
                    super.validate(representation);
                    ValidationSupport.checkReferenceType(representation.document, "document", "DocumentReference");
                    ValidationSupport.requireValueOrChildren(representation);
                }

                protected Builder from(Representation representation) {
                    super.from(representation);
                    type = representation.type;
                    this.representation = representation.representation;
                    format = representation.format;
                    document = representation.document;
                    return this;
                }
            }
        }
    }

    /**
     * Codes associated with the substance.
     */
    public static class Code extends BackboneElement {
        @Summary
        private final CodeableConcept code;
        @Summary
        private final CodeableConcept status;
        @Summary
        private final DateTime statusDate;
        @Summary
        private final List<Annotation> note;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private Code(Builder builder) {
            super(builder);
            code = builder.code;
            status = builder.status;
            statusDate = builder.statusDate;
            note = Collections.unmodifiableList(builder.note);
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * The specific code.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Status of the code assignment, for example 'provisional', 'approved'.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStatus() {
            return status;
        }

        /**
         * The date at which the code status is changed as part of the terminology maintenance.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getStatusDate() {
            return statusDate;
        }

        /**
         * Any comment can be provided in this field, if necessary.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
         */
        public List<Annotation> getNote() {
            return note;
        }

        /**
         * Supporting literature.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (status != null) || 
                (statusDate != null) || 
                !note.isEmpty() || 
                !source.isEmpty();
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
                    accept(statusDate, "statusDate", visitor);
                    accept(note, "note", visitor, Annotation.class);
                    accept(source, "source", visitor, Reference.class);
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
            Code other = (Code) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(status, other.status) && 
                Objects.equals(statusDate, other.statusDate) && 
                Objects.equals(note, other.note) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    status, 
                    statusDate, 
                    note, 
                    source);
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
            private DateTime statusDate;
            private List<Annotation> note = new ArrayList<>();
            private List<Reference> source = new ArrayList<>();

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
             * The specific code.
             * 
             * @param code
             *     The specific code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Status of the code assignment, for example 'provisional', 'approved'.
             * 
             * @param status
             *     Status of the code assignment, for example 'provisional', 'approved'
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(CodeableConcept status) {
                this.status = status;
                return this;
            }

            /**
             * The date at which the code status is changed as part of the terminology maintenance.
             * 
             * @param statusDate
             *     The date at which the code status is changed as part of the terminology maintenance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder statusDate(DateTime statusDate) {
                this.statusDate = statusDate;
                return this;
            }

            /**
             * Any comment can be provided in this field, if necessary.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Any comment can be provided in this field, if necessary
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder note(Annotation... note) {
                for (Annotation value : note) {
                    this.note.add(value);
                }
                return this;
            }

            /**
             * Any comment can be provided in this field, if necessary.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Any comment can be provided in this field, if necessary
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder note(Collection<Annotation> note) {
                this.note = new ArrayList<>(note);
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference... source) {
                for (Reference value : source) {
                    this.source.add(value);
                }
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            /**
             * Build the {@link Code}
             * 
             * @return
             *     An immutable object of type {@link Code}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Code per the base specification
             */
            @Override
            public Code build() {
                Code code = new Code(this);
                if (validating) {
                    validate(code);
                }
                return code;
            }

            protected void validate(Code code) {
                super.validate(code);
                ValidationSupport.checkList(code.note, "note", Annotation.class);
                ValidationSupport.checkList(code.source, "source", Reference.class);
                ValidationSupport.checkReferenceType(code.source, "source", "DocumentReference");
                ValidationSupport.requireValueOrChildren(code);
            }

            protected Builder from(Code code) {
                super.from(code);
                this.code = code.code;
                status = code.status;
                statusDate = code.statusDate;
                note.addAll(code.note);
                source.addAll(code.source);
                return this;
            }
        }
    }

    /**
     * Names applicable to this substance.
     */
    public static class Name extends BackboneElement {
        @Summary
        @Required
        private final String name;
        @Summary
        private final CodeableConcept type;
        @Summary
        private final CodeableConcept status;
        @Summary
        private final Boolean preferred;
        @Summary
        private final List<CodeableConcept> language;
        @Summary
        private final List<CodeableConcept> domain;
        @Summary
        private final List<CodeableConcept> jurisdiction;
        @Summary
        private final List<SubstanceDefinition.Name> synonym;
        @Summary
        private final List<SubstanceDefinition.Name> translation;
        @Summary
        private final List<Official> official;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private Name(Builder builder) {
            super(builder);
            name = builder.name;
            type = builder.type;
            status = builder.status;
            preferred = builder.preferred;
            language = Collections.unmodifiableList(builder.language);
            domain = Collections.unmodifiableList(builder.domain);
            jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
            synonym = Collections.unmodifiableList(builder.synonym);
            translation = Collections.unmodifiableList(builder.translation);
            official = Collections.unmodifiableList(builder.official);
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * The actual name.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * Name type, for example 'systematic', 'scientific, 'brand'.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The status of the name, for example 'current', 'proposed'.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStatus() {
            return status;
        }

        /**
         * If this is the preferred name for this substance.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getPreferred() {
            return preferred;
        }

        /**
         * Human language that the name is written in.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getLanguage() {
            return language;
        }

        /**
         * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
         * colour additive.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getDomain() {
            return domain;
        }

        /**
         * The jurisdiction where this name applies.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getJurisdiction() {
            return jurisdiction;
        }

        /**
         * A synonym of this particular name, by which the substance is also known.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Name} that may be empty.
         */
        public List<SubstanceDefinition.Name> getSynonym() {
            return synonym;
        }

        /**
         * A translation for this name into another human language.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Name} that may be empty.
         */
        public List<SubstanceDefinition.Name> getTranslation() {
            return translation;
        }

        /**
         * Details of the official nature of this name.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Official} that may be empty.
         */
        public List<Official> getOfficial() {
            return official;
        }

        /**
         * Supporting literature.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (type != null) || 
                (status != null) || 
                (preferred != null) || 
                !language.isEmpty() || 
                !domain.isEmpty() || 
                !jurisdiction.isEmpty() || 
                !synonym.isEmpty() || 
                !translation.isEmpty() || 
                !official.isEmpty() || 
                !source.isEmpty();
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
                    accept(status, "status", visitor);
                    accept(preferred, "preferred", visitor);
                    accept(language, "language", visitor, CodeableConcept.class);
                    accept(domain, "domain", visitor, CodeableConcept.class);
                    accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                    accept(synonym, "synonym", visitor, SubstanceDefinition.Name.class);
                    accept(translation, "translation", visitor, SubstanceDefinition.Name.class);
                    accept(official, "official", visitor, Official.class);
                    accept(source, "source", visitor, Reference.class);
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
            Name other = (Name) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(type, other.type) && 
                Objects.equals(status, other.status) && 
                Objects.equals(preferred, other.preferred) && 
                Objects.equals(language, other.language) && 
                Objects.equals(domain, other.domain) && 
                Objects.equals(jurisdiction, other.jurisdiction) && 
                Objects.equals(synonym, other.synonym) && 
                Objects.equals(translation, other.translation) && 
                Objects.equals(official, other.official) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    type, 
                    status, 
                    preferred, 
                    language, 
                    domain, 
                    jurisdiction, 
                    synonym, 
                    translation, 
                    official, 
                    source);
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
            private String name;
            private CodeableConcept type;
            private CodeableConcept status;
            private Boolean preferred;
            private List<CodeableConcept> language = new ArrayList<>();
            private List<CodeableConcept> domain = new ArrayList<>();
            private List<CodeableConcept> jurisdiction = new ArrayList<>();
            private List<SubstanceDefinition.Name> synonym = new ArrayList<>();
            private List<SubstanceDefinition.Name> translation = new ArrayList<>();
            private List<Official> official = new ArrayList<>();
            private List<Reference> source = new ArrayList<>();

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
             * Convenience method for setting {@code name}.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     The actual name
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
             * The actual name.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     The actual name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Name type, for example 'systematic', 'scientific, 'brand'.
             * 
             * @param type
             *     Name type, for example 'systematic', 'scientific, 'brand'
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The status of the name, for example 'current', 'proposed'.
             * 
             * @param status
             *     The status of the name, for example 'current', 'proposed'
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(CodeableConcept status) {
                this.status = status;
                return this;
            }

            /**
             * Convenience method for setting {@code preferred}.
             * 
             * @param preferred
             *     If this is the preferred name for this substance
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #preferred(com.ibm.fhir.model.type.Boolean)
             */
            public Builder preferred(java.lang.Boolean preferred) {
                this.preferred = (preferred == null) ? null : Boolean.of(preferred);
                return this;
            }

            /**
             * If this is the preferred name for this substance.
             * 
             * @param preferred
             *     If this is the preferred name for this substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preferred(Boolean preferred) {
                this.preferred = preferred;
                return this;
            }

            /**
             * Human language that the name is written in.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param language
             *     Human language that the name is written in
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(CodeableConcept... language) {
                for (CodeableConcept value : language) {
                    this.language.add(value);
                }
                return this;
            }

            /**
             * Human language that the name is written in.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param language
             *     Human language that the name is written in
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder language(Collection<CodeableConcept> language) {
                this.language = new ArrayList<>(language);
                return this;
            }

            /**
             * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             * colour additive.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param domain
             *     The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             *     colour additive
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder domain(CodeableConcept... domain) {
                for (CodeableConcept value : domain) {
                    this.domain.add(value);
                }
                return this;
            }

            /**
             * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             * colour additive.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param domain
             *     The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             *     colour additive
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder domain(Collection<CodeableConcept> domain) {
                this.domain = new ArrayList<>(domain);
                return this;
            }

            /**
             * The jurisdiction where this name applies.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param jurisdiction
             *     The jurisdiction where this name applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder jurisdiction(CodeableConcept... jurisdiction) {
                for (CodeableConcept value : jurisdiction) {
                    this.jurisdiction.add(value);
                }
                return this;
            }

            /**
             * The jurisdiction where this name applies.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param jurisdiction
             *     The jurisdiction where this name applies
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
                this.jurisdiction = new ArrayList<>(jurisdiction);
                return this;
            }

            /**
             * A synonym of this particular name, by which the substance is also known.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param synonym
             *     A synonym of this particular name, by which the substance is also known
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder synonym(SubstanceDefinition.Name... synonym) {
                for (SubstanceDefinition.Name value : synonym) {
                    this.synonym.add(value);
                }
                return this;
            }

            /**
             * A synonym of this particular name, by which the substance is also known.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param synonym
             *     A synonym of this particular name, by which the substance is also known
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder synonym(Collection<SubstanceDefinition.Name> synonym) {
                this.synonym = new ArrayList<>(synonym);
                return this;
            }

            /**
             * A translation for this name into another human language.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param translation
             *     A translation for this name into another human language
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder translation(SubstanceDefinition.Name... translation) {
                for (SubstanceDefinition.Name value : translation) {
                    this.translation.add(value);
                }
                return this;
            }

            /**
             * A translation for this name into another human language.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param translation
             *     A translation for this name into another human language
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder translation(Collection<SubstanceDefinition.Name> translation) {
                this.translation = new ArrayList<>(translation);
                return this;
            }

            /**
             * Details of the official nature of this name.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param official
             *     Details of the official nature of this name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder official(Official... official) {
                for (Official value : official) {
                    this.official.add(value);
                }
                return this;
            }

            /**
             * Details of the official nature of this name.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param official
             *     Details of the official nature of this name
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder official(Collection<Official> official) {
                this.official = new ArrayList<>(official);
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference... source) {
                for (Reference value : source) {
                    this.source.add(value);
                }
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            /**
             * Build the {@link Name}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Name}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Name per the base specification
             */
            @Override
            public Name build() {
                Name name = new Name(this);
                if (validating) {
                    validate(name);
                }
                return name;
            }

            protected void validate(Name name) {
                super.validate(name);
                ValidationSupport.requireNonNull(name.name, "name");
                ValidationSupport.checkList(name.language, "language", CodeableConcept.class);
                ValidationSupport.checkList(name.domain, "domain", CodeableConcept.class);
                ValidationSupport.checkList(name.jurisdiction, "jurisdiction", CodeableConcept.class);
                ValidationSupport.checkList(name.synonym, "synonym", SubstanceDefinition.Name.class);
                ValidationSupport.checkList(name.translation, "translation", SubstanceDefinition.Name.class);
                ValidationSupport.checkList(name.official, "official", Official.class);
                ValidationSupport.checkList(name.source, "source", Reference.class);
                ValidationSupport.checkReferenceType(name.source, "source", "DocumentReference");
                ValidationSupport.requireValueOrChildren(name);
            }

            protected Builder from(Name name) {
                super.from(name);
                this.name = name.name;
                type = name.type;
                status = name.status;
                preferred = name.preferred;
                language.addAll(name.language);
                domain.addAll(name.domain);
                jurisdiction.addAll(name.jurisdiction);
                synonym.addAll(name.synonym);
                translation.addAll(name.translation);
                official.addAll(name.official);
                source.addAll(name.source);
                return this;
            }
        }

        /**
         * Details of the official nature of this name.
         */
        public static class Official extends BackboneElement {
            @Summary
            private final CodeableConcept authority;
            @Summary
            private final CodeableConcept status;
            @Summary
            private final DateTime date;

            private Official(Builder builder) {
                super(builder);
                authority = builder.authority;
                status = builder.status;
                date = builder.date;
            }

            /**
             * Which authority uses this official name.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getAuthority() {
                return authority;
            }

            /**
             * The status of the official name, for example 'provisional', 'approved'.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getStatus() {
                return status;
            }

            /**
             * Date of official name change.
             * 
             * @return
             *     An immutable object of type {@link DateTime} that may be null.
             */
            public DateTime getDate() {
                return date;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (authority != null) || 
                    (status != null) || 
                    (date != null);
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
                        accept(authority, "authority", visitor);
                        accept(status, "status", visitor);
                        accept(date, "date", visitor);
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
                Official other = (Official) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(authority, other.authority) && 
                    Objects.equals(status, other.status) && 
                    Objects.equals(date, other.date);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        authority, 
                        status, 
                        date);
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
                private CodeableConcept authority;
                private CodeableConcept status;
                private DateTime date;

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
                 * Which authority uses this official name.
                 * 
                 * @param authority
                 *     Which authority uses this official name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder authority(CodeableConcept authority) {
                    this.authority = authority;
                    return this;
                }

                /**
                 * The status of the official name, for example 'provisional', 'approved'.
                 * 
                 * @param status
                 *     The status of the official name, for example 'provisional', 'approved'
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder status(CodeableConcept status) {
                    this.status = status;
                    return this;
                }

                /**
                 * Date of official name change.
                 * 
                 * @param date
                 *     Date of official name change
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder date(DateTime date) {
                    this.date = date;
                    return this;
                }

                /**
                 * Build the {@link Official}
                 * 
                 * @return
                 *     An immutable object of type {@link Official}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Official per the base specification
                 */
                @Override
                public Official build() {
                    Official official = new Official(this);
                    if (validating) {
                        validate(official);
                    }
                    return official;
                }

                protected void validate(Official official) {
                    super.validate(official);
                    ValidationSupport.requireValueOrChildren(official);
                }

                protected Builder from(Official official) {
                    super.from(official);
                    authority = official.authority;
                    status = official.status;
                    date = official.date;
                    return this;
                }
            }
        }
    }

    /**
     * A link between this substance and another, with details of the relationship.
     */
    public static class Relationship extends BackboneElement {
        @Summary
        @ReferenceTarget({ "SubstanceDefinition" })
        @Choice({ Reference.class, CodeableConcept.class })
        private final Element substanceDefinition;
        @Summary
        @Required
        private final CodeableConcept type;
        @Summary
        private final Boolean isDefining;
        @Summary
        @Choice({ Quantity.class, Ratio.class, String.class })
        private final Element amount;
        @Summary
        private final Ratio amountRatioHighLimit;
        @Summary
        private final CodeableConcept amountType;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private Relationship(Builder builder) {
            super(builder);
            substanceDefinition = builder.substanceDefinition;
            type = builder.type;
            isDefining = builder.isDefining;
            amount = builder.amount;
            amountRatioHighLimit = builder.amountRatioHighLimit;
            amountType = builder.amountType;
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * A pointer to another substance, as a resource or just a representational code.
         * 
         * @return
         *     An immutable object of type {@link Reference} or {@link CodeableConcept} that may be null.
         */
        public Element getSubstanceDefinition() {
            return substanceDefinition;
        }

        /**
         * For example "salt to parent", "active moiety", "starting material", "polymorph", "impurity of".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that 
         * enzyme, out of several possible substance relationships.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getIsDefining() {
            return isDefining;
        }

        /**
         * A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the 
         * active substance in relation to some other.
         * 
         * @return
         *     An immutable object of type {@link Quantity}, {@link Ratio} or {@link String} that may be null.
         */
        public Element getAmount() {
            return amount;
        }

        /**
         * For use when the numeric has an uncertain range.
         * 
         * @return
         *     An immutable object of type {@link Ratio} that may be null.
         */
        public Ratio getAmountRatioHighLimit() {
            return amountRatioHighLimit;
        }

        /**
         * An operator for the amount, for example "average", "approximately", "less than".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getAmountType() {
            return amountType;
        }

        /**
         * Supporting literature.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (substanceDefinition != null) || 
                (type != null) || 
                (isDefining != null) || 
                (amount != null) || 
                (amountRatioHighLimit != null) || 
                (amountType != null) || 
                !source.isEmpty();
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
                    accept(substanceDefinition, "substanceDefinition", visitor);
                    accept(type, "type", visitor);
                    accept(isDefining, "isDefining", visitor);
                    accept(amount, "amount", visitor);
                    accept(amountRatioHighLimit, "amountRatioHighLimit", visitor);
                    accept(amountType, "amountType", visitor);
                    accept(source, "source", visitor, Reference.class);
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
            Relationship other = (Relationship) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(substanceDefinition, other.substanceDefinition) && 
                Objects.equals(type, other.type) && 
                Objects.equals(isDefining, other.isDefining) && 
                Objects.equals(amount, other.amount) && 
                Objects.equals(amountRatioHighLimit, other.amountRatioHighLimit) && 
                Objects.equals(amountType, other.amountType) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    substanceDefinition, 
                    type, 
                    isDefining, 
                    amount, 
                    amountRatioHighLimit, 
                    amountType, 
                    source);
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
            private Element substanceDefinition;
            private CodeableConcept type;
            private Boolean isDefining;
            private Element amount;
            private Ratio amountRatioHighLimit;
            private CodeableConcept amountType;
            private List<Reference> source = new ArrayList<>();

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
             * A pointer to another substance, as a resource or just a representational code.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Reference}</li>
             * <li>{@link CodeableConcept}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link SubstanceDefinition}</li>
             * </ul>
             * 
             * @param substanceDefinition
             *     A pointer to another substance, as a resource or just a representational code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder substanceDefinition(Element substanceDefinition) {
                this.substanceDefinition = substanceDefinition;
                return this;
            }

            /**
             * For example "salt to parent", "active moiety", "starting material", "polymorph", "impurity of".
             * 
             * <p>This element is required.
             * 
             * @param type
             *     For example "salt to parent", "active moiety", "starting material", "polymorph", "impurity of"
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code isDefining}.
             * 
             * @param isDefining
             *     For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that 
             *     enzyme, out of several possible substance relationships
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #isDefining(com.ibm.fhir.model.type.Boolean)
             */
            public Builder isDefining(java.lang.Boolean isDefining) {
                this.isDefining = (isDefining == null) ? null : Boolean.of(isDefining);
                return this;
            }

            /**
             * For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that 
             * enzyme, out of several possible substance relationships.
             * 
             * @param isDefining
             *     For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that 
             *     enzyme, out of several possible substance relationships
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder isDefining(Boolean isDefining) {
                this.isDefining = isDefining;
                return this;
            }

            /**
             * Convenience method for setting {@code amount} with choice type String.
             * 
             * @param amount
             *     A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the 
             *     active substance in relation to some other
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #amount(Element)
             */
            public Builder amount(java.lang.String amount) {
                this.amount = (amount == null) ? null : String.of(amount);
                return this;
            }

            /**
             * A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the 
             * active substance in relation to some other.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link Ratio}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param amount
             *     A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the 
             *     active substance in relation to some other
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Element amount) {
                this.amount = amount;
                return this;
            }

            /**
             * For use when the numeric has an uncertain range.
             * 
             * @param amountRatioHighLimit
             *     For use when the numeric has an uncertain range
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amountRatioHighLimit(Ratio amountRatioHighLimit) {
                this.amountRatioHighLimit = amountRatioHighLimit;
                return this;
            }

            /**
             * An operator for the amount, for example "average", "approximately", "less than".
             * 
             * @param amountType
             *     An operator for the amount, for example "average", "approximately", "less than"
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amountType(CodeableConcept amountType) {
                this.amountType = amountType;
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference... source) {
                for (Reference value : source) {
                    this.source.add(value);
                }
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            /**
             * Build the {@link Relationship}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Relationship}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Relationship per the base specification
             */
            @Override
            public Relationship build() {
                Relationship relationship = new Relationship(this);
                if (validating) {
                    validate(relationship);
                }
                return relationship;
            }

            protected void validate(Relationship relationship) {
                super.validate(relationship);
                ValidationSupport.choiceElement(relationship.substanceDefinition, "substanceDefinition", Reference.class, CodeableConcept.class);
                ValidationSupport.requireNonNull(relationship.type, "type");
                ValidationSupport.choiceElement(relationship.amount, "amount", Quantity.class, Ratio.class, String.class);
                ValidationSupport.checkList(relationship.source, "source", Reference.class);
                ValidationSupport.checkReferenceType(relationship.substanceDefinition, "substanceDefinition", "SubstanceDefinition");
                ValidationSupport.checkReferenceType(relationship.source, "source", "DocumentReference");
                ValidationSupport.requireValueOrChildren(relationship);
            }

            protected Builder from(Relationship relationship) {
                super.from(relationship);
                substanceDefinition = relationship.substanceDefinition;
                type = relationship.type;
                isDefining = relationship.isDefining;
                amount = relationship.amount;
                amountRatioHighLimit = relationship.amountRatioHighLimit;
                amountType = relationship.amountType;
                source.addAll(relationship.source);
                return this;
            }
        }
    }

    /**
     * Material or taxonomic/anatomical source for the substance.
     */
    public static class SourceMaterial extends BackboneElement {
        @Summary
        private final CodeableConcept type;
        @Summary
        private final CodeableConcept genus;
        @Summary
        private final CodeableConcept species;
        @Summary
        private final CodeableConcept part;
        @Summary
        private final List<CodeableConcept> countryOfOrigin;

        private SourceMaterial(Builder builder) {
            super(builder);
            type = builder.type;
            genus = builder.genus;
            species = builder.species;
            part = builder.part;
            countryOfOrigin = Collections.unmodifiableList(builder.countryOfOrigin);
        }

        /**
         * A classification that provides the origin of the raw material. Example: cat hair would be an Animal source type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The genus of an organism, typically referring to the Latin epithet of the genus element of the plant/animal scientific 
         * name.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getGenus() {
            return genus;
        }

        /**
         * The species of an organism, typically referring to the Latin epithet of the species of the plant/animal.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getSpecies() {
            return species;
        }

        /**
         * An anatomical origin of the source material within an organism.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPart() {
            return part;
        }

        /**
         * The country or countries where the material is harvested.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getCountryOfOrigin() {
            return countryOfOrigin;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (genus != null) || 
                (species != null) || 
                (part != null) || 
                !countryOfOrigin.isEmpty();
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
                    accept(type, "type", visitor);
                    accept(genus, "genus", visitor);
                    accept(species, "species", visitor);
                    accept(part, "part", visitor);
                    accept(countryOfOrigin, "countryOfOrigin", visitor, CodeableConcept.class);
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
            SourceMaterial other = (SourceMaterial) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(genus, other.genus) && 
                Objects.equals(species, other.species) && 
                Objects.equals(part, other.part) && 
                Objects.equals(countryOfOrigin, other.countryOfOrigin);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    genus, 
                    species, 
                    part, 
                    countryOfOrigin);
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
            private CodeableConcept type;
            private CodeableConcept genus;
            private CodeableConcept species;
            private CodeableConcept part;
            private List<CodeableConcept> countryOfOrigin = new ArrayList<>();

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
             * A classification that provides the origin of the raw material. Example: cat hair would be an Animal source type.
             * 
             * @param type
             *     A classification that provides the origin of the raw material. Example: cat hair would be an Animal source type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The genus of an organism, typically referring to the Latin epithet of the genus element of the plant/animal scientific 
             * name.
             * 
             * @param genus
             *     The genus of an organism, typically referring to the Latin epithet of the genus element of the plant/animal scientific 
             *     name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder genus(CodeableConcept genus) {
                this.genus = genus;
                return this;
            }

            /**
             * The species of an organism, typically referring to the Latin epithet of the species of the plant/animal.
             * 
             * @param species
             *     The species of an organism, typically referring to the Latin epithet of the species of the plant/animal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder species(CodeableConcept species) {
                this.species = species;
                return this;
            }

            /**
             * An anatomical origin of the source material within an organism.
             * 
             * @param part
             *     An anatomical origin of the source material within an organism
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder part(CodeableConcept part) {
                this.part = part;
                return this;
            }

            /**
             * The country or countries where the material is harvested.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param countryOfOrigin
             *     The country or countries where the material is harvested
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder countryOfOrigin(CodeableConcept... countryOfOrigin) {
                for (CodeableConcept value : countryOfOrigin) {
                    this.countryOfOrigin.add(value);
                }
                return this;
            }

            /**
             * The country or countries where the material is harvested.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param countryOfOrigin
             *     The country or countries where the material is harvested
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder countryOfOrigin(Collection<CodeableConcept> countryOfOrigin) {
                this.countryOfOrigin = new ArrayList<>(countryOfOrigin);
                return this;
            }

            /**
             * Build the {@link SourceMaterial}
             * 
             * @return
             *     An immutable object of type {@link SourceMaterial}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid SourceMaterial per the base specification
             */
            @Override
            public SourceMaterial build() {
                SourceMaterial sourceMaterial = new SourceMaterial(this);
                if (validating) {
                    validate(sourceMaterial);
                }
                return sourceMaterial;
            }

            protected void validate(SourceMaterial sourceMaterial) {
                super.validate(sourceMaterial);
                ValidationSupport.checkList(sourceMaterial.countryOfOrigin, "countryOfOrigin", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(sourceMaterial);
            }

            protected Builder from(SourceMaterial sourceMaterial) {
                super.from(sourceMaterial);
                type = sourceMaterial.type;
                genus = sourceMaterial.genus;
                species = sourceMaterial.species;
                part = sourceMaterial.part;
                countryOfOrigin.addAll(sourceMaterial.countryOfOrigin);
                return this;
            }
        }
    }
}
