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
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Todo.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubstanceReferenceInformation extends DomainResource {
    @Summary
    private final String comment;
    @Summary
    private final List<Gene> gene;
    @Summary
    private final List<GeneElement> geneElement;
    @Summary
    private final List<Classification> classification;
    @Summary
    private final List<Target> target;

    private SubstanceReferenceInformation(Builder builder) {
        super(builder);
        comment = builder.comment;
        gene = Collections.unmodifiableList(builder.gene);
        geneElement = Collections.unmodifiableList(builder.geneElement);
        classification = Collections.unmodifiableList(builder.classification);
        target = Collections.unmodifiableList(builder.target);
    }

    /**
     * Todo.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Gene} that may be empty.
     */
    public List<Gene> getGene() {
        return gene;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link GeneElement} that may be empty.
     */
    public List<GeneElement> getGeneElement() {
        return geneElement;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Classification} that may be empty.
     */
    public List<Classification> getClassification() {
        return classification;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Target} that may be empty.
     */
    public List<Target> getTarget() {
        return target;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (comment != null) || 
            !gene.isEmpty() || 
            !geneElement.isEmpty() || 
            !classification.isEmpty() || 
            !target.isEmpty();
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
                accept(comment, "comment", visitor);
                accept(gene, "gene", visitor, Gene.class);
                accept(geneElement, "geneElement", visitor, GeneElement.class);
                accept(classification, "classification", visitor, Classification.class);
                accept(target, "target", visitor, Target.class);
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
        SubstanceReferenceInformation other = (SubstanceReferenceInformation) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(comment, other.comment) && 
            Objects.equals(gene, other.gene) && 
            Objects.equals(geneElement, other.geneElement) && 
            Objects.equals(classification, other.classification) && 
            Objects.equals(target, other.target);
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
                comment, 
                gene, 
                geneElement, 
                classification, 
                target);
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
        private String comment;
        private List<Gene> gene = new ArrayList<>();
        private List<GeneElement> geneElement = new ArrayList<>();
        private List<Classification> classification = new ArrayList<>();
        private List<Target> target = new ArrayList<>();

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
         * Convenience method for setting {@code comment}.
         * 
         * @param comment
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #comment(com.ibm.fhir.model.type.String)
         */
        public Builder comment(java.lang.String comment) {
            this.comment = (comment == null) ? null : String.of(comment);
            return this;
        }

        /**
         * Todo.
         * 
         * @param comment
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param gene
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder gene(Gene... gene) {
            for (Gene value : gene) {
                this.gene.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param gene
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder gene(Collection<Gene> gene) {
            this.gene = new ArrayList<>(gene);
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param geneElement
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder geneElement(GeneElement... geneElement) {
            for (GeneElement value : geneElement) {
                this.geneElement.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param geneElement
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder geneElement(Collection<GeneElement> geneElement) {
            this.geneElement = new ArrayList<>(geneElement);
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder classification(Classification... classification) {
            for (Classification value : classification) {
                this.classification.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder classification(Collection<Classification> classification) {
            this.classification = new ArrayList<>(classification);
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param target
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder target(Target... target) {
            for (Target value : target) {
                this.target.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param target
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder target(Collection<Target> target) {
            this.target = new ArrayList<>(target);
            return this;
        }

        /**
         * Build the {@link SubstanceReferenceInformation}
         * 
         * @return
         *     An immutable object of type {@link SubstanceReferenceInformation}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstanceReferenceInformation per the base specification
         */
        @Override
        public SubstanceReferenceInformation build() {
            SubstanceReferenceInformation substanceReferenceInformation = new SubstanceReferenceInformation(this);
            if (validating) {
                validate(substanceReferenceInformation);
            }
            return substanceReferenceInformation;
        }

        protected void validate(SubstanceReferenceInformation substanceReferenceInformation) {
            super.validate(substanceReferenceInformation);
            ValidationSupport.checkList(substanceReferenceInformation.gene, "gene", Gene.class);
            ValidationSupport.checkList(substanceReferenceInformation.geneElement, "geneElement", GeneElement.class);
            ValidationSupport.checkList(substanceReferenceInformation.classification, "classification", Classification.class);
            ValidationSupport.checkList(substanceReferenceInformation.target, "target", Target.class);
        }

        protected Builder from(SubstanceReferenceInformation substanceReferenceInformation) {
            super.from(substanceReferenceInformation);
            comment = substanceReferenceInformation.comment;
            gene.addAll(substanceReferenceInformation.gene);
            geneElement.addAll(substanceReferenceInformation.geneElement);
            classification.addAll(substanceReferenceInformation.classification);
            target.addAll(substanceReferenceInformation.target);
            return this;
        }
    }

    /**
     * Todo.
     */
    public static class Gene extends BackboneElement {
        @Summary
        private final CodeableConcept geneSequenceOrigin;
        @Summary
        private final CodeableConcept gene;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private Gene(Builder builder) {
            super(builder);
            geneSequenceOrigin = builder.geneSequenceOrigin;
            gene = builder.gene;
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getGeneSequenceOrigin() {
            return geneSequenceOrigin;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getGene() {
            return gene;
        }

        /**
         * Todo.
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
                (geneSequenceOrigin != null) || 
                (gene != null) || 
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
                    accept(geneSequenceOrigin, "geneSequenceOrigin", visitor);
                    accept(gene, "gene", visitor);
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
            Gene other = (Gene) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(geneSequenceOrigin, other.geneSequenceOrigin) && 
                Objects.equals(gene, other.gene) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    geneSequenceOrigin, 
                    gene, 
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
            private CodeableConcept geneSequenceOrigin;
            private CodeableConcept gene;
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
             * Todo.
             * 
             * @param geneSequenceOrigin
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder geneSequenceOrigin(CodeableConcept geneSequenceOrigin) {
                this.geneSequenceOrigin = geneSequenceOrigin;
                return this;
            }

            /**
             * Todo.
             * 
             * @param gene
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder gene(CodeableConcept gene) {
                this.gene = gene;
                return this;
            }

            /**
             * Todo.
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
             *     Todo
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
             * Todo.
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
             *     Todo
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
             * Build the {@link Gene}
             * 
             * @return
             *     An immutable object of type {@link Gene}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Gene per the base specification
             */
            @Override
            public Gene build() {
                Gene gene = new Gene(this);
                if (validating) {
                    validate(gene);
                }
                return gene;
            }

            protected void validate(Gene gene) {
                super.validate(gene);
                ValidationSupport.checkList(gene.source, "source", Reference.class);
                ValidationSupport.checkReferenceType(gene.source, "source", "DocumentReference");
                ValidationSupport.requireValueOrChildren(gene);
            }

            protected Builder from(Gene gene) {
                super.from(gene);
                geneSequenceOrigin = gene.geneSequenceOrigin;
                this.gene = gene.gene;
                source.addAll(gene.source);
                return this;
            }
        }
    }

    /**
     * Todo.
     */
    public static class GeneElement extends BackboneElement {
        @Summary
        private final CodeableConcept type;
        @Summary
        private final Identifier element;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private GeneElement(Builder builder) {
            super(builder);
            type = builder.type;
            element = builder.element;
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getElement() {
            return element;
        }

        /**
         * Todo.
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
                (type != null) || 
                (element != null) || 
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
                    accept(type, "type", visitor);
                    accept(element, "element", visitor);
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
            GeneElement other = (GeneElement) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(element, other.element) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    element, 
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
            private CodeableConcept type;
            private Identifier element;
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
             * Todo.
             * 
             * @param type
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Todo.
             * 
             * @param element
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder element(Identifier element) {
                this.element = element;
                return this;
            }

            /**
             * Todo.
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
             *     Todo
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
             * Todo.
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
             *     Todo
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
             * Build the {@link GeneElement}
             * 
             * @return
             *     An immutable object of type {@link GeneElement}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid GeneElement per the base specification
             */
            @Override
            public GeneElement build() {
                GeneElement geneElement = new GeneElement(this);
                if (validating) {
                    validate(geneElement);
                }
                return geneElement;
            }

            protected void validate(GeneElement geneElement) {
                super.validate(geneElement);
                ValidationSupport.checkList(geneElement.source, "source", Reference.class);
                ValidationSupport.checkReferenceType(geneElement.source, "source", "DocumentReference");
                ValidationSupport.requireValueOrChildren(geneElement);
            }

            protected Builder from(GeneElement geneElement) {
                super.from(geneElement);
                type = geneElement.type;
                element = geneElement.element;
                source.addAll(geneElement.source);
                return this;
            }
        }
    }

    /**
     * Todo.
     */
    public static class Classification extends BackboneElement {
        @Summary
        private final CodeableConcept domain;
        @Summary
        private final CodeableConcept classification;
        @Summary
        private final List<CodeableConcept> subtype;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private Classification(Builder builder) {
            super(builder);
            domain = builder.domain;
            classification = builder.classification;
            subtype = Collections.unmodifiableList(builder.subtype);
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getDomain() {
            return domain;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getClassification() {
            return classification;
        }

        /**
         * Todo.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getSubtype() {
            return subtype;
        }

        /**
         * Todo.
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
                (domain != null) || 
                (classification != null) || 
                !subtype.isEmpty() || 
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
                    accept(domain, "domain", visitor);
                    accept(classification, "classification", visitor);
                    accept(subtype, "subtype", visitor, CodeableConcept.class);
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
            Classification other = (Classification) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(domain, other.domain) && 
                Objects.equals(classification, other.classification) && 
                Objects.equals(subtype, other.subtype) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    domain, 
                    classification, 
                    subtype, 
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
            private CodeableConcept domain;
            private CodeableConcept classification;
            private List<CodeableConcept> subtype = new ArrayList<>();
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
             * Todo.
             * 
             * @param domain
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder domain(CodeableConcept domain) {
                this.domain = domain;
                return this;
            }

            /**
             * Todo.
             * 
             * @param classification
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder classification(CodeableConcept classification) {
                this.classification = classification;
                return this;
            }

            /**
             * Todo.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subtype
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subtype(CodeableConcept... subtype) {
                for (CodeableConcept value : subtype) {
                    this.subtype.add(value);
                }
                return this;
            }

            /**
             * Todo.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param subtype
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder subtype(Collection<CodeableConcept> subtype) {
                this.subtype = new ArrayList<>(subtype);
                return this;
            }

            /**
             * Todo.
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
             *     Todo
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
             * Todo.
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
             *     Todo
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
             * Build the {@link Classification}
             * 
             * @return
             *     An immutable object of type {@link Classification}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Classification per the base specification
             */
            @Override
            public Classification build() {
                Classification classification = new Classification(this);
                if (validating) {
                    validate(classification);
                }
                return classification;
            }

            protected void validate(Classification classification) {
                super.validate(classification);
                ValidationSupport.checkList(classification.subtype, "subtype", CodeableConcept.class);
                ValidationSupport.checkList(classification.source, "source", Reference.class);
                ValidationSupport.checkReferenceType(classification.source, "source", "DocumentReference");
                ValidationSupport.requireValueOrChildren(classification);
            }

            protected Builder from(Classification classification) {
                super.from(classification);
                domain = classification.domain;
                this.classification = classification.classification;
                subtype.addAll(classification.subtype);
                source.addAll(classification.source);
                return this;
            }
        }
    }

    /**
     * Todo.
     */
    public static class Target extends BackboneElement {
        @Summary
        private final Identifier target;
        @Summary
        private final CodeableConcept type;
        @Summary
        private final CodeableConcept interaction;
        @Summary
        private final CodeableConcept organism;
        @Summary
        private final CodeableConcept organismType;
        @Summary
        @Choice({ Quantity.class, Range.class, String.class })
        private final Element amount;
        @Summary
        private final CodeableConcept amountType;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private Target(Builder builder) {
            super(builder);
            target = builder.target;
            type = builder.type;
            interaction = builder.interaction;
            organism = builder.organism;
            organismType = builder.organismType;
            amount = builder.amount;
            amountType = builder.amountType;
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getTarget() {
            return target;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getInteraction() {
            return interaction;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOrganism() {
            return organism;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOrganismType() {
            return organismType;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link Quantity}, {@link Range} or {@link String} that may be null.
         */
        public Element getAmount() {
            return amount;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getAmountType() {
            return amountType;
        }

        /**
         * Todo.
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
                (target != null) || 
                (type != null) || 
                (interaction != null) || 
                (organism != null) || 
                (organismType != null) || 
                (amount != null) || 
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
                    accept(target, "target", visitor);
                    accept(type, "type", visitor);
                    accept(interaction, "interaction", visitor);
                    accept(organism, "organism", visitor);
                    accept(organismType, "organismType", visitor);
                    accept(amount, "amount", visitor);
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
            Target other = (Target) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(target, other.target) && 
                Objects.equals(type, other.type) && 
                Objects.equals(interaction, other.interaction) && 
                Objects.equals(organism, other.organism) && 
                Objects.equals(organismType, other.organismType) && 
                Objects.equals(amount, other.amount) && 
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
                    target, 
                    type, 
                    interaction, 
                    organism, 
                    organismType, 
                    amount, 
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
            private Identifier target;
            private CodeableConcept type;
            private CodeableConcept interaction;
            private CodeableConcept organism;
            private CodeableConcept organismType;
            private Element amount;
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
             * Todo.
             * 
             * @param target
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Identifier target) {
                this.target = target;
                return this;
            }

            /**
             * Todo.
             * 
             * @param type
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Todo.
             * 
             * @param interaction
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder interaction(CodeableConcept interaction) {
                this.interaction = interaction;
                return this;
            }

            /**
             * Todo.
             * 
             * @param organism
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder organism(CodeableConcept organism) {
                this.organism = organism;
                return this;
            }

            /**
             * Todo.
             * 
             * @param organismType
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder organismType(CodeableConcept organismType) {
                this.organismType = organismType;
                return this;
            }

            /**
             * Convenience method for setting {@code amount} with choice type String.
             * 
             * @param amount
             *     Todo
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
             * Todo.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param amount
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Element amount) {
                this.amount = amount;
                return this;
            }

            /**
             * Todo.
             * 
             * @param amountType
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amountType(CodeableConcept amountType) {
                this.amountType = amountType;
                return this;
            }

            /**
             * Todo.
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
             *     Todo
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
             * Todo.
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
             *     Todo
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
             * Build the {@link Target}
             * 
             * @return
             *     An immutable object of type {@link Target}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Target per the base specification
             */
            @Override
            public Target build() {
                Target target = new Target(this);
                if (validating) {
                    validate(target);
                }
                return target;
            }

            protected void validate(Target target) {
                super.validate(target);
                ValidationSupport.choiceElement(target.amount, "amount", Quantity.class, Range.class, String.class);
                ValidationSupport.checkList(target.source, "source", Reference.class);
                ValidationSupport.checkReferenceType(target.source, "source", "DocumentReference");
                ValidationSupport.requireValueOrChildren(target);
            }

            protected Builder from(Target target) {
                super.from(target);
                this.target = target.target;
                type = target.type;
                interaction = target.interaction;
                organism = target.organism;
                organismType = target.organismType;
                amount = target.amount;
                amountType = target.amountType;
                source.addAll(target.source);
                return this;
            }
        }
    }
}
