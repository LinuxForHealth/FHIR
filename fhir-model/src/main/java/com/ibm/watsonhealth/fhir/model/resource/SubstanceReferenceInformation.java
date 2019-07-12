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
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Todo.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class SubstanceReferenceInformation extends DomainResource {
    private final String comment;
    private final List<Gene> gene;
    private final List<GeneElement> geneElement;
    private final List<Classification> classification;
    private final List<Target> target;

    private volatile int hashCode;

    private SubstanceReferenceInformation(Builder builder) {
        super(builder);
        comment = builder.comment;
        gene = Collections.unmodifiableList(builder.gene);
        geneElement = Collections.unmodifiableList(builder.geneElement);
        classification = Collections.unmodifiableList(builder.classification);
        target = Collections.unmodifiableList(builder.target);
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getComment() {
        return comment;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Gene}.
     */
    public List<Gene> getGene() {
        return gene;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link GeneElement}.
     */
    public List<GeneElement> getGeneElement() {
        return geneElement;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Classification}.
     */
    public List<Classification> getClassification() {
        return classification;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Target}.
     */
    public List<Target> getTarget() {
        return target;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
        // optional
        private String comment;
        private List<Gene> gene = new ArrayList<>();
        private List<GeneElement> geneElement = new ArrayList<>();
        private List<Classification> classification = new ArrayList<>();
        private List<Target> target = new ArrayList<>();

        private Builder() {
            super();
        }

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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Todo.
         * </p>
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param gene
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder gene(Collection<Gene> gene) {
            this.gene = new ArrayList<>(gene);
            return this;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param geneElement
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder geneElement(Collection<GeneElement> geneElement) {
            this.geneElement = new ArrayList<>(geneElement);
            return this;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param classification
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder classification(Collection<Classification> classification) {
            this.classification = new ArrayList<>(classification);
            return this;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param target
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder target(Collection<Target> target) {
            this.target = new ArrayList<>(target);
            return this;
        }

        @Override
        public SubstanceReferenceInformation build() {
            return new SubstanceReferenceInformation(this);
        }

        private Builder from(SubstanceReferenceInformation substanceReferenceInformation) {
            id = substanceReferenceInformation.id;
            meta = substanceReferenceInformation.meta;
            implicitRules = substanceReferenceInformation.implicitRules;
            language = substanceReferenceInformation.language;
            text = substanceReferenceInformation.text;
            contained.addAll(substanceReferenceInformation.contained);
            extension.addAll(substanceReferenceInformation.extension);
            modifierExtension.addAll(substanceReferenceInformation.modifierExtension);
            comment = substanceReferenceInformation.comment;
            gene.addAll(substanceReferenceInformation.gene);
            geneElement.addAll(substanceReferenceInformation.geneElement);
            classification.addAll(substanceReferenceInformation.classification);
            target.addAll(substanceReferenceInformation.target);
            return this;
        }
    }

    /**
     * <p>
     * Todo.
     * </p>
     */
    public static class Gene extends BackboneElement {
        private final CodeableConcept geneSequenceOrigin;
        private final CodeableConcept gene;
        private final List<Reference> source;

        private volatile int hashCode;

        private Gene(Builder builder) {
            super(builder);
            geneSequenceOrigin = builder.geneSequenceOrigin;
            gene = builder.gene;
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getGeneSequenceOrigin() {
            return geneSequenceOrigin;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getGene() {
            return gene;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(geneSequenceOrigin, "geneSequenceOrigin", visitor);
                    accept(gene, "gene", visitor);
                    accept(source, "source", visitor, Reference.class);
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
            // optional
            private CodeableConcept geneSequenceOrigin;
            private CodeableConcept gene;
            private List<Reference> source = new ArrayList<>();

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
             * Adds new element(s) to the existing list
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
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param source
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            @Override
            public Gene build() {
                return new Gene(this);
            }

            private Builder from(Gene gene) {
                id = gene.id;
                extension.addAll(gene.extension);
                modifierExtension.addAll(gene.modifierExtension);
                geneSequenceOrigin = gene.geneSequenceOrigin;
                this.gene = gene.gene;
                source.addAll(gene.source);
                return this;
            }
        }
    }

    /**
     * <p>
     * Todo.
     * </p>
     */
    public static class GeneElement extends BackboneElement {
        private final CodeableConcept type;
        private final Identifier element;
        private final List<Reference> source;

        private volatile int hashCode;

        private GeneElement(Builder builder) {
            super(builder);
            type = builder.type;
            element = builder.element;
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * <p>
         * Todo.
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
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getElement() {
            return element;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(type, "type", visitor);
                    accept(element, "element", visitor);
                    accept(source, "source", visitor, Reference.class);
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
            // optional
            private CodeableConcept type;
            private Identifier element;
            private List<Reference> source = new ArrayList<>();

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
             * Adds new element(s) to the existing list
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
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param source
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            @Override
            public GeneElement build() {
                return new GeneElement(this);
            }

            private Builder from(GeneElement geneElement) {
                id = geneElement.id;
                extension.addAll(geneElement.extension);
                modifierExtension.addAll(geneElement.modifierExtension);
                type = geneElement.type;
                element = geneElement.element;
                source.addAll(geneElement.source);
                return this;
            }
        }
    }

    /**
     * <p>
     * Todo.
     * </p>
     */
    public static class Classification extends BackboneElement {
        private final CodeableConcept domain;
        private final CodeableConcept classification;
        private final List<CodeableConcept> subtype;
        private final List<Reference> source;

        private volatile int hashCode;

        private Classification(Builder builder) {
            super(builder);
            domain = builder.domain;
            classification = builder.classification;
            subtype = Collections.unmodifiableList(builder.subtype);
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getDomain() {
            return domain;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getClassification() {
            return classification;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getSubtype() {
            return subtype;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(domain, "domain", visitor);
                    accept(classification, "classification", visitor);
                    accept(subtype, "subtype", visitor, CodeableConcept.class);
                    accept(source, "source", visitor, Reference.class);
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
            // optional
            private CodeableConcept domain;
            private CodeableConcept classification;
            private List<CodeableConcept> subtype = new ArrayList<>();
            private List<Reference> source = new ArrayList<>();

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
             * Adds new element(s) to the existing list
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
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param subtype
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subtype(Collection<CodeableConcept> subtype) {
                this.subtype = new ArrayList<>(subtype);
                return this;
            }

            /**
             * <p>
             * Todo.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param source
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            @Override
            public Classification build() {
                return new Classification(this);
            }

            private Builder from(Classification classification) {
                id = classification.id;
                extension.addAll(classification.extension);
                modifierExtension.addAll(classification.modifierExtension);
                domain = classification.domain;
                this.classification = classification.classification;
                subtype.addAll(classification.subtype);
                source.addAll(classification.source);
                return this;
            }
        }
    }

    /**
     * <p>
     * Todo.
     * </p>
     */
    public static class Target extends BackboneElement {
        private final Identifier target;
        private final CodeableConcept type;
        private final CodeableConcept interaction;
        private final CodeableConcept organism;
        private final CodeableConcept organismType;
        private final Element amount;
        private final CodeableConcept amountType;
        private final List<Reference> source;

        private volatile int hashCode;

        private Target(Builder builder) {
            super(builder);
            target = builder.target;
            type = builder.type;
            interaction = builder.interaction;
            organism = builder.organism;
            organismType = builder.organismType;
            amount = ValidationSupport.choiceElement(builder.amount, "amount", Quantity.class, Range.class, String.class);
            amountType = builder.amountType;
            source = Collections.unmodifiableList(builder.source);
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getTarget() {
            return target;
        }

        /**
         * <p>
         * Todo.
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
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getInteraction() {
            return interaction;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getOrganism() {
            return organism;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getOrganismType() {
            return organismType;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getAmount() {
            return amount;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getAmountType() {
            return amountType;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            // optional
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
             * Adds new element(s) to the existing list
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
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param source
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            @Override
            public Target build() {
                return new Target(this);
            }

            private Builder from(Target target) {
                id = target.id;
                extension.addAll(target.extension);
                modifierExtension.addAll(target.modifierExtension);
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
