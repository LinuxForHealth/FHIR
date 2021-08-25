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
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A SubstanceProtein is defined as a single unit of a linear amino acid sequence, or a combination of subunits that are 
 * either covalently linked or have a defined invariant stoichiometric relationship. This includes all synthetic, 
 * recombinant and purified SubstanceProteins of defined sequence, whether the use is therapeutic or prophylactic. This 
 * set of elements will be used to describe albumins, coagulation factors, cytokines, growth factors, 
 * peptide/SubstanceProtein hormones, enzymes, toxins, toxoids, recombinant vaccines, and immunomodulators.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubstanceProtein extends DomainResource {
    @Summary
    private final CodeableConcept sequenceType;
    @Summary
    private final Integer numberOfSubunits;
    @Summary
    private final List<String> disulfideLinkage;
    @Summary
    private final List<Subunit> subunit;

    private SubstanceProtein(Builder builder) {
        super(builder);
        sequenceType = builder.sequenceType;
        numberOfSubunits = builder.numberOfSubunits;
        disulfideLinkage = Collections.unmodifiableList(builder.disulfideLinkage);
        subunit = Collections.unmodifiableList(builder.subunit);
    }

    /**
     * The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is 
     * available or derivable from a nucleic acid sequence.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSequenceType() {
        return sequenceType;
    }

    /**
     * Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the 
     * SubstanceProtein shall be described. It is possible that the number of subunits can be variable.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getNumberOfSubunits() {
        return numberOfSubunits;
    }

    /**
     * The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be 
     * described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit 
     * number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage 
     * positions shall actually contain the amino acid Cysteine at the respective positions.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getDisulfideLinkage() {
        return disulfideLinkage;
    }

    /**
     * This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear 
     * sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished 
     * SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single 
     * sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by 
     * decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Subunit} that may be empty.
     */
    public List<Subunit> getSubunit() {
        return subunit;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (sequenceType != null) || 
            (numberOfSubunits != null) || 
            !disulfideLinkage.isEmpty() || 
            !subunit.isEmpty();
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
                accept(sequenceType, "sequenceType", visitor);
                accept(numberOfSubunits, "numberOfSubunits", visitor);
                accept(disulfideLinkage, "disulfideLinkage", visitor, String.class);
                accept(subunit, "subunit", visitor, Subunit.class);
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
        SubstanceProtein other = (SubstanceProtein) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(sequenceType, other.sequenceType) && 
            Objects.equals(numberOfSubunits, other.numberOfSubunits) && 
            Objects.equals(disulfideLinkage, other.disulfideLinkage) && 
            Objects.equals(subunit, other.subunit);
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
                sequenceType, 
                numberOfSubunits, 
                disulfideLinkage, 
                subunit);
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
        private CodeableConcept sequenceType;
        private Integer numberOfSubunits;
        private List<String> disulfideLinkage = new ArrayList<>();
        private List<Subunit> subunit = new ArrayList<>();

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
         * The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is 
         * available or derivable from a nucleic acid sequence.
         * 
         * @param sequenceType
         *     The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is 
         *     available or derivable from a nucleic acid sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sequenceType(CodeableConcept sequenceType) {
            this.sequenceType = sequenceType;
            return this;
        }

        /**
         * Convenience method for setting {@code numberOfSubunits}.
         * 
         * @param numberOfSubunits
         *     Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the 
         *     SubstanceProtein shall be described. It is possible that the number of subunits can be variable
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #numberOfSubunits(com.ibm.fhir.model.type.Integer)
         */
        public Builder numberOfSubunits(java.lang.Integer numberOfSubunits) {
            this.numberOfSubunits = (numberOfSubunits == null) ? null : Integer.of(numberOfSubunits);
            return this;
        }

        /**
         * Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the 
         * SubstanceProtein shall be described. It is possible that the number of subunits can be variable.
         * 
         * @param numberOfSubunits
         *     Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the 
         *     SubstanceProtein shall be described. It is possible that the number of subunits can be variable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder numberOfSubunits(Integer numberOfSubunits) {
            this.numberOfSubunits = numberOfSubunits;
            return this;
        }

        /**
         * Convenience method for setting {@code disulfideLinkage}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param disulfideLinkage
         *     The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be 
         *     described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit 
         *     number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage 
         *     positions shall actually contain the amino acid Cysteine at the respective positions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #disulfideLinkage(com.ibm.fhir.model.type.String)
         */
        public Builder disulfideLinkage(java.lang.String... disulfideLinkage) {
            for (java.lang.String value : disulfideLinkage) {
                this.disulfideLinkage.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be 
         * described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit 
         * number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage 
         * positions shall actually contain the amino acid Cysteine at the respective positions.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param disulfideLinkage
         *     The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be 
         *     described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit 
         *     number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage 
         *     positions shall actually contain the amino acid Cysteine at the respective positions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder disulfideLinkage(String... disulfideLinkage) {
            for (String value : disulfideLinkage) {
                this.disulfideLinkage.add(value);
            }
            return this;
        }

        /**
         * The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be 
         * described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit 
         * number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage 
         * positions shall actually contain the amino acid Cysteine at the respective positions.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param disulfideLinkage
         *     The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be 
         *     described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit 
         *     number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage 
         *     positions shall actually contain the amino acid Cysteine at the respective positions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder disulfideLinkage(Collection<String> disulfideLinkage) {
            this.disulfideLinkage = new ArrayList<>(disulfideLinkage);
            return this;
        }

        /**
         * This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear 
         * sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished 
         * SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single 
         * sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by 
         * decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param subunit
         *     This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear 
         *     sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished 
         *     SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single 
         *     sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by 
         *     decreasing molecular weight; subunits that have identical sequences will be repeated multiple times
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subunit(Subunit... subunit) {
            for (Subunit value : subunit) {
                this.subunit.add(value);
            }
            return this;
        }

        /**
         * This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear 
         * sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished 
         * SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single 
         * sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by 
         * decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param subunit
         *     This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear 
         *     sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished 
         *     SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single 
         *     sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by 
         *     decreasing molecular weight; subunits that have identical sequences will be repeated multiple times
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subunit(Collection<Subunit> subunit) {
            this.subunit = new ArrayList<>(subunit);
            return this;
        }

        /**
         * Build the {@link SubstanceProtein}
         * 
         * @return
         *     An immutable object of type {@link SubstanceProtein}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstanceProtein per the base specification
         */
        @Override
        public SubstanceProtein build() {
            SubstanceProtein substanceProtein = new SubstanceProtein(this);
            if (validating) {
                validate(substanceProtein);
            }
            return substanceProtein;
        }

        protected void validate(SubstanceProtein substanceProtein) {
            super.validate(substanceProtein);
            ValidationSupport.checkList(substanceProtein.disulfideLinkage, "disulfideLinkage", String.class);
            ValidationSupport.checkList(substanceProtein.subunit, "subunit", Subunit.class);
        }

        protected Builder from(SubstanceProtein substanceProtein) {
            super.from(substanceProtein);
            sequenceType = substanceProtein.sequenceType;
            numberOfSubunits = substanceProtein.numberOfSubunits;
            disulfideLinkage.addAll(substanceProtein.disulfideLinkage);
            subunit.addAll(substanceProtein.subunit);
            return this;
        }
    }

    /**
     * This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear 
     * sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished 
     * SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single 
     * sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by 
     * decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.
     */
    public static class Subunit extends BackboneElement {
        @Summary
        private final Integer subunit;
        @Summary
        private final String sequence;
        @Summary
        private final Integer length;
        @Summary
        private final Attachment sequenceAttachment;
        @Summary
        private final Identifier nTerminalModificationId;
        @Summary
        private final String nTerminalModification;
        @Summary
        private final Identifier cTerminalModificationId;
        @Summary
        private final String cTerminalModification;

        private Subunit(Builder builder) {
            super(builder);
            subunit = builder.subunit;
            sequence = builder.sequence;
            length = builder.length;
            sequenceAttachment = builder.sequenceAttachment;
            nTerminalModificationId = builder.nTerminalModificationId;
            nTerminalModification = builder.nTerminalModification;
            cTerminalModificationId = builder.cTerminalModificationId;
            cTerminalModification = builder.cTerminalModification;
        }

        /**
         * Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the 
         * same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have 
         * sequential subscripts.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getSubunit() {
            return subunit;
        }

        /**
         * The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-
         * letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed 
         * SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids 
         * that are not represented with a single letter code an X should be used within the sequence. The modified amino acids 
         * will be distinguished by their position in the sequence.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSequence() {
            return sequence;
        }

        /**
         * Length of linear sequences of amino acids contained in the subunit.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getLength() {
            return length;
        }

        /**
         * The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-
         * letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed 
         * SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids 
         * that are not represented with a single letter code an X should be used within the sequence. The modified amino acids 
         * will be distinguished by their position in the sequence.
         * 
         * @return
         *     An immutable object of type {@link Attachment} that may be null.
         */
        public Attachment getSequenceAttachment() {
            return sequenceAttachment;
        }

        /**
         * Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getNTerminalModificationId() {
            return nTerminalModificationId;
        }

        /**
         * The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getNTerminalModification() {
            return nTerminalModification;
        }

        /**
         * Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getCTerminalModificationId() {
            return cTerminalModificationId;
        }

        /**
         * The modification at the C-terminal shall be specified.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getCTerminalModification() {
            return cTerminalModification;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (subunit != null) || 
                (sequence != null) || 
                (length != null) || 
                (sequenceAttachment != null) || 
                (nTerminalModificationId != null) || 
                (nTerminalModification != null) || 
                (cTerminalModificationId != null) || 
                (cTerminalModification != null);
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
                    accept(subunit, "subunit", visitor);
                    accept(sequence, "sequence", visitor);
                    accept(length, "length", visitor);
                    accept(sequenceAttachment, "sequenceAttachment", visitor);
                    accept(nTerminalModificationId, "nTerminalModificationId", visitor);
                    accept(nTerminalModification, "nTerminalModification", visitor);
                    accept(cTerminalModificationId, "cTerminalModificationId", visitor);
                    accept(cTerminalModification, "cTerminalModification", visitor);
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
            Subunit other = (Subunit) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(subunit, other.subunit) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(length, other.length) && 
                Objects.equals(sequenceAttachment, other.sequenceAttachment) && 
                Objects.equals(nTerminalModificationId, other.nTerminalModificationId) && 
                Objects.equals(nTerminalModification, other.nTerminalModification) && 
                Objects.equals(cTerminalModificationId, other.cTerminalModificationId) && 
                Objects.equals(cTerminalModification, other.cTerminalModification);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    subunit, 
                    sequence, 
                    length, 
                    sequenceAttachment, 
                    nTerminalModificationId, 
                    nTerminalModification, 
                    cTerminalModificationId, 
                    cTerminalModification);
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
            private Integer subunit;
            private String sequence;
            private Integer length;
            private Attachment sequenceAttachment;
            private Identifier nTerminalModificationId;
            private String nTerminalModification;
            private Identifier cTerminalModificationId;
            private String cTerminalModification;

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
             * Convenience method for setting {@code subunit}.
             * 
             * @param subunit
             *     Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the 
             *     same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have 
             *     sequential subscripts
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #subunit(com.ibm.fhir.model.type.Integer)
             */
            public Builder subunit(java.lang.Integer subunit) {
                this.subunit = (subunit == null) ? null : Integer.of(subunit);
                return this;
            }

            /**
             * Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the 
             * same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have 
             * sequential subscripts.
             * 
             * @param subunit
             *     Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the 
             *     same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have 
             *     sequential subscripts
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subunit(Integer subunit) {
                this.subunit = subunit;
                return this;
            }

            /**
             * Convenience method for setting {@code sequence}.
             * 
             * @param sequence
             *     The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-
             *     letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed 
             *     SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids 
             *     that are not represented with a single letter code an X should be used within the sequence. The modified amino acids 
             *     will be distinguished by their position in the sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #sequence(com.ibm.fhir.model.type.String)
             */
            public Builder sequence(java.lang.String sequence) {
                this.sequence = (sequence == null) ? null : String.of(sequence);
                return this;
            }

            /**
             * The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-
             * letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed 
             * SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids 
             * that are not represented with a single letter code an X should be used within the sequence. The modified amino acids 
             * will be distinguished by their position in the sequence.
             * 
             * @param sequence
             *     The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-
             *     letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed 
             *     SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids 
             *     that are not represented with a single letter code an X should be used within the sequence. The modified amino acids 
             *     will be distinguished by their position in the sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(String sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * Convenience method for setting {@code length}.
             * 
             * @param length
             *     Length of linear sequences of amino acids contained in the subunit
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #length(com.ibm.fhir.model.type.Integer)
             */
            public Builder length(java.lang.Integer length) {
                this.length = (length == null) ? null : Integer.of(length);
                return this;
            }

            /**
             * Length of linear sequences of amino acids contained in the subunit.
             * 
             * @param length
             *     Length of linear sequences of amino acids contained in the subunit
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder length(Integer length) {
                this.length = length;
                return this;
            }

            /**
             * The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-
             * letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed 
             * SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids 
             * that are not represented with a single letter code an X should be used within the sequence. The modified amino acids 
             * will be distinguished by their position in the sequence.
             * 
             * @param sequenceAttachment
             *     The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-
             *     letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed 
             *     SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids 
             *     that are not represented with a single letter code an X should be used within the sequence. The modified amino acids 
             *     will be distinguished by their position in the sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequenceAttachment(Attachment sequenceAttachment) {
                this.sequenceAttachment = sequenceAttachment;
                return this;
            }

            /**
             * Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.
             * 
             * @param nTerminalModificationId
             *     Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder nTerminalModificationId(Identifier nTerminalModificationId) {
                this.nTerminalModificationId = nTerminalModificationId;
                return this;
            }

            /**
             * Convenience method for setting {@code nTerminalModification}.
             * 
             * @param nTerminalModification
             *     The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #nTerminalModification(com.ibm.fhir.model.type.String)
             */
            public Builder nTerminalModification(java.lang.String nTerminalModification) {
                this.nTerminalModification = (nTerminalModification == null) ? null : String.of(nTerminalModification);
                return this;
            }

            /**
             * The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.
             * 
             * @param nTerminalModification
             *     The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder nTerminalModification(String nTerminalModification) {
                this.nTerminalModification = nTerminalModification;
                return this;
            }

            /**
             * Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.
             * 
             * @param cTerminalModificationId
             *     Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder cTerminalModificationId(Identifier cTerminalModificationId) {
                this.cTerminalModificationId = cTerminalModificationId;
                return this;
            }

            /**
             * Convenience method for setting {@code cTerminalModification}.
             * 
             * @param cTerminalModification
             *     The modification at the C-terminal shall be specified
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #cTerminalModification(com.ibm.fhir.model.type.String)
             */
            public Builder cTerminalModification(java.lang.String cTerminalModification) {
                this.cTerminalModification = (cTerminalModification == null) ? null : String.of(cTerminalModification);
                return this;
            }

            /**
             * The modification at the C-terminal shall be specified.
             * 
             * @param cTerminalModification
             *     The modification at the C-terminal shall be specified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder cTerminalModification(String cTerminalModification) {
                this.cTerminalModification = cTerminalModification;
                return this;
            }

            /**
             * Build the {@link Subunit}
             * 
             * @return
             *     An immutable object of type {@link Subunit}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Subunit per the base specification
             */
            @Override
            public Subunit build() {
                Subunit subunit = new Subunit(this);
                if (validating) {
                    validate(subunit);
                }
                return subunit;
            }

            protected void validate(Subunit subunit) {
                super.validate(subunit);
                ValidationSupport.requireValueOrChildren(subunit);
            }

            protected Builder from(Subunit subunit) {
                super.from(subunit);
                this.subunit = subunit.subunit;
                sequence = subunit.sequence;
                length = subunit.length;
                sequenceAttachment = subunit.sequenceAttachment;
                nTerminalModificationId = subunit.nTerminalModificationId;
                nTerminalModification = subunit.nTerminalModification;
                cTerminalModificationId = subunit.cTerminalModificationId;
                cTerminalModification = subunit.cTerminalModification;
                return this;
            }
        }
    }
}
