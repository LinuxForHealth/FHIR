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
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.SubstanceAmount;
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
public class SubstancePolymer extends DomainResource {
    @Summary
    private final CodeableConcept clazz;
    @Summary
    private final CodeableConcept geometry;
    @Summary
    private final List<CodeableConcept> copolymerConnectivity;
    @Summary
    private final List<String> modification;
    @Summary
    private final List<MonomerSet> monomerSet;
    @Summary
    private final List<Repeat> repeat;

    private SubstancePolymer(Builder builder) {
        super(builder);
        clazz = builder.clazz;
        geometry = builder.geometry;
        copolymerConnectivity = Collections.unmodifiableList(builder.copolymerConnectivity);
        modification = Collections.unmodifiableList(builder.modification);
        monomerSet = Collections.unmodifiableList(builder.monomerSet);
        repeat = Collections.unmodifiableList(builder.repeat);
    }

    /**
     * Todo.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getClazz() {
        return clazz;
    }

    /**
     * Todo.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getGeometry() {
        return geometry;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCopolymerConnectivity() {
        return copolymerConnectivity;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getModification() {
        return modification;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MonomerSet} that may be empty.
     */
    public List<MonomerSet> getMonomerSet() {
        return monomerSet;
    }

    /**
     * Todo.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Repeat} that may be empty.
     */
    public List<Repeat> getRepeat() {
        return repeat;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (clazz != null) || 
            (geometry != null) || 
            !copolymerConnectivity.isEmpty() || 
            !modification.isEmpty() || 
            !monomerSet.isEmpty() || 
            !repeat.isEmpty();
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
                accept(clazz, "class", visitor);
                accept(geometry, "geometry", visitor);
                accept(copolymerConnectivity, "copolymerConnectivity", visitor, CodeableConcept.class);
                accept(modification, "modification", visitor, String.class);
                accept(monomerSet, "monomerSet", visitor, MonomerSet.class);
                accept(repeat, "repeat", visitor, Repeat.class);
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
        SubstancePolymer other = (SubstancePolymer) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(clazz, other.clazz) && 
            Objects.equals(geometry, other.geometry) && 
            Objects.equals(copolymerConnectivity, other.copolymerConnectivity) && 
            Objects.equals(modification, other.modification) && 
            Objects.equals(monomerSet, other.monomerSet) && 
            Objects.equals(repeat, other.repeat);
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
                clazz, 
                geometry, 
                copolymerConnectivity, 
                modification, 
                monomerSet, 
                repeat);
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
        private CodeableConcept clazz;
        private CodeableConcept geometry;
        private List<CodeableConcept> copolymerConnectivity = new ArrayList<>();
        private List<String> modification = new ArrayList<>();
        private List<MonomerSet> monomerSet = new ArrayList<>();
        private List<Repeat> repeat = new ArrayList<>();

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
         * Todo.
         * 
         * @param clazz
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder clazz(CodeableConcept clazz) {
            this.clazz = clazz;
            return this;
        }

        /**
         * Todo.
         * 
         * @param geometry
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder geometry(CodeableConcept geometry) {
            this.geometry = geometry;
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param copolymerConnectivity
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder copolymerConnectivity(CodeableConcept... copolymerConnectivity) {
            for (CodeableConcept value : copolymerConnectivity) {
                this.copolymerConnectivity.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param copolymerConnectivity
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder copolymerConnectivity(Collection<CodeableConcept> copolymerConnectivity) {
            this.copolymerConnectivity = new ArrayList<>(copolymerConnectivity);
            return this;
        }

        /**
         * Convenience method for setting {@code modification}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modification
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #modification(com.ibm.fhir.model.type.String)
         */
        public Builder modification(java.lang.String... modification) {
            for (java.lang.String value : modification) {
                this.modification.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modification
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder modification(String... modification) {
            for (String value : modification) {
                this.modification.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modification
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder modification(Collection<String> modification) {
            this.modification = new ArrayList<>(modification);
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param monomerSet
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder monomerSet(MonomerSet... monomerSet) {
            for (MonomerSet value : monomerSet) {
                this.monomerSet.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param monomerSet
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder monomerSet(Collection<MonomerSet> monomerSet) {
            this.monomerSet = new ArrayList<>(monomerSet);
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param repeat
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder repeat(Repeat... repeat) {
            for (Repeat value : repeat) {
                this.repeat.add(value);
            }
            return this;
        }

        /**
         * Todo.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param repeat
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder repeat(Collection<Repeat> repeat) {
            this.repeat = new ArrayList<>(repeat);
            return this;
        }

        /**
         * Build the {@link SubstancePolymer}
         * 
         * @return
         *     An immutable object of type {@link SubstancePolymer}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstancePolymer per the base specification
         */
        @Override
        public SubstancePolymer build() {
            SubstancePolymer substancePolymer = new SubstancePolymer(this);
            if (validating) {
                validate(substancePolymer);
            }
            return substancePolymer;
        }

        protected void validate(SubstancePolymer substancePolymer) {
            super.validate(substancePolymer);
            ValidationSupport.checkList(substancePolymer.copolymerConnectivity, "copolymerConnectivity", CodeableConcept.class);
            ValidationSupport.checkList(substancePolymer.modification, "modification", String.class);
            ValidationSupport.checkList(substancePolymer.monomerSet, "monomerSet", MonomerSet.class);
            ValidationSupport.checkList(substancePolymer.repeat, "repeat", Repeat.class);
        }

        protected Builder from(SubstancePolymer substancePolymer) {
            super.from(substancePolymer);
            clazz = substancePolymer.clazz;
            geometry = substancePolymer.geometry;
            copolymerConnectivity.addAll(substancePolymer.copolymerConnectivity);
            modification.addAll(substancePolymer.modification);
            monomerSet.addAll(substancePolymer.monomerSet);
            repeat.addAll(substancePolymer.repeat);
            return this;
        }
    }

    /**
     * Todo.
     */
    public static class MonomerSet extends BackboneElement {
        @Summary
        private final CodeableConcept ratioType;
        @Summary
        private final List<StartingMaterial> startingMaterial;

        private MonomerSet(Builder builder) {
            super(builder);
            ratioType = builder.ratioType;
            startingMaterial = Collections.unmodifiableList(builder.startingMaterial);
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRatioType() {
            return ratioType;
        }

        /**
         * Todo.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link StartingMaterial} that may be empty.
         */
        public List<StartingMaterial> getStartingMaterial() {
            return startingMaterial;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (ratioType != null) || 
                !startingMaterial.isEmpty();
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
                    accept(ratioType, "ratioType", visitor);
                    accept(startingMaterial, "startingMaterial", visitor, StartingMaterial.class);
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
            MonomerSet other = (MonomerSet) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(ratioType, other.ratioType) && 
                Objects.equals(startingMaterial, other.startingMaterial);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    ratioType, 
                    startingMaterial);
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
            private CodeableConcept ratioType;
            private List<StartingMaterial> startingMaterial = new ArrayList<>();

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
             * @param ratioType
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder ratioType(CodeableConcept ratioType) {
                this.ratioType = ratioType;
                return this;
            }

            /**
             * Todo.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param startingMaterial
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder startingMaterial(StartingMaterial... startingMaterial) {
                for (StartingMaterial value : startingMaterial) {
                    this.startingMaterial.add(value);
                }
                return this;
            }

            /**
             * Todo.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param startingMaterial
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder startingMaterial(Collection<StartingMaterial> startingMaterial) {
                this.startingMaterial = new ArrayList<>(startingMaterial);
                return this;
            }

            /**
             * Build the {@link MonomerSet}
             * 
             * @return
             *     An immutable object of type {@link MonomerSet}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid MonomerSet per the base specification
             */
            @Override
            public MonomerSet build() {
                MonomerSet monomerSet = new MonomerSet(this);
                if (validating) {
                    validate(monomerSet);
                }
                return monomerSet;
            }

            protected void validate(MonomerSet monomerSet) {
                super.validate(monomerSet);
                ValidationSupport.checkList(monomerSet.startingMaterial, "startingMaterial", StartingMaterial.class);
                ValidationSupport.requireValueOrChildren(monomerSet);
            }

            protected Builder from(MonomerSet monomerSet) {
                super.from(monomerSet);
                ratioType = monomerSet.ratioType;
                startingMaterial.addAll(monomerSet.startingMaterial);
                return this;
            }
        }

        /**
         * Todo.
         */
        public static class StartingMaterial extends BackboneElement {
            @Summary
            private final CodeableConcept material;
            @Summary
            private final CodeableConcept type;
            @Summary
            private final Boolean isDefining;
            @Summary
            private final SubstanceAmount amount;

            private StartingMaterial(Builder builder) {
                super(builder);
                material = builder.material;
                type = builder.type;
                isDefining = builder.isDefining;
                amount = builder.amount;
            }

            /**
             * Todo.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getMaterial() {
                return material;
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
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getIsDefining() {
                return isDefining;
            }

            /**
             * Todo.
             * 
             * @return
             *     An immutable object of type {@link SubstanceAmount} that may be null.
             */
            public SubstanceAmount getAmount() {
                return amount;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (material != null) || 
                    (type != null) || 
                    (isDefining != null) || 
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
                        accept(material, "material", visitor);
                        accept(type, "type", visitor);
                        accept(isDefining, "isDefining", visitor);
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
                StartingMaterial other = (StartingMaterial) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(material, other.material) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(isDefining, other.isDefining) && 
                    Objects.equals(amount, other.amount);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        material, 
                        type, 
                        isDefining, 
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
                private CodeableConcept material;
                private CodeableConcept type;
                private Boolean isDefining;
                private SubstanceAmount amount;

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
                 * @param material
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder material(CodeableConcept material) {
                    this.material = material;
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
                 * Convenience method for setting {@code isDefining}.
                 * 
                 * @param isDefining
                 *     Todo
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
                 * Todo.
                 * 
                 * @param isDefining
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder isDefining(Boolean isDefining) {
                    this.isDefining = isDefining;
                    return this;
                }

                /**
                 * Todo.
                 * 
                 * @param amount
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(SubstanceAmount amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * Build the {@link StartingMaterial}
                 * 
                 * @return
                 *     An immutable object of type {@link StartingMaterial}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid StartingMaterial per the base specification
                 */
                @Override
                public StartingMaterial build() {
                    StartingMaterial startingMaterial = new StartingMaterial(this);
                    if (validating) {
                        validate(startingMaterial);
                    }
                    return startingMaterial;
                }

                protected void validate(StartingMaterial startingMaterial) {
                    super.validate(startingMaterial);
                    ValidationSupport.requireValueOrChildren(startingMaterial);
                }

                protected Builder from(StartingMaterial startingMaterial) {
                    super.from(startingMaterial);
                    material = startingMaterial.material;
                    type = startingMaterial.type;
                    isDefining = startingMaterial.isDefining;
                    amount = startingMaterial.amount;
                    return this;
                }
            }
        }
    }

    /**
     * Todo.
     */
    public static class Repeat extends BackboneElement {
        @Summary
        private final Integer numberOfUnits;
        @Summary
        private final String averageMolecularFormula;
        @Summary
        private final CodeableConcept repeatUnitAmountType;
        @Summary
        private final List<RepeatUnit> repeatUnit;

        private Repeat(Builder builder) {
            super(builder);
            numberOfUnits = builder.numberOfUnits;
            averageMolecularFormula = builder.averageMolecularFormula;
            repeatUnitAmountType = builder.repeatUnitAmountType;
            repeatUnit = Collections.unmodifiableList(builder.repeatUnit);
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getNumberOfUnits() {
            return numberOfUnits;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getAverageMolecularFormula() {
            return averageMolecularFormula;
        }

        /**
         * Todo.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRepeatUnitAmountType() {
            return repeatUnitAmountType;
        }

        /**
         * Todo.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RepeatUnit} that may be empty.
         */
        public List<RepeatUnit> getRepeatUnit() {
            return repeatUnit;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (numberOfUnits != null) || 
                (averageMolecularFormula != null) || 
                (repeatUnitAmountType != null) || 
                !repeatUnit.isEmpty();
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
                    accept(numberOfUnits, "numberOfUnits", visitor);
                    accept(averageMolecularFormula, "averageMolecularFormula", visitor);
                    accept(repeatUnitAmountType, "repeatUnitAmountType", visitor);
                    accept(repeatUnit, "repeatUnit", visitor, RepeatUnit.class);
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
            Repeat other = (Repeat) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(numberOfUnits, other.numberOfUnits) && 
                Objects.equals(averageMolecularFormula, other.averageMolecularFormula) && 
                Objects.equals(repeatUnitAmountType, other.repeatUnitAmountType) && 
                Objects.equals(repeatUnit, other.repeatUnit);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    numberOfUnits, 
                    averageMolecularFormula, 
                    repeatUnitAmountType, 
                    repeatUnit);
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
            private Integer numberOfUnits;
            private String averageMolecularFormula;
            private CodeableConcept repeatUnitAmountType;
            private List<RepeatUnit> repeatUnit = new ArrayList<>();

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
             * Convenience method for setting {@code numberOfUnits}.
             * 
             * @param numberOfUnits
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #numberOfUnits(com.ibm.fhir.model.type.Integer)
             */
            public Builder numberOfUnits(java.lang.Integer numberOfUnits) {
                this.numberOfUnits = (numberOfUnits == null) ? null : Integer.of(numberOfUnits);
                return this;
            }

            /**
             * Todo.
             * 
             * @param numberOfUnits
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder numberOfUnits(Integer numberOfUnits) {
                this.numberOfUnits = numberOfUnits;
                return this;
            }

            /**
             * Convenience method for setting {@code averageMolecularFormula}.
             * 
             * @param averageMolecularFormula
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #averageMolecularFormula(com.ibm.fhir.model.type.String)
             */
            public Builder averageMolecularFormula(java.lang.String averageMolecularFormula) {
                this.averageMolecularFormula = (averageMolecularFormula == null) ? null : String.of(averageMolecularFormula);
                return this;
            }

            /**
             * Todo.
             * 
             * @param averageMolecularFormula
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder averageMolecularFormula(String averageMolecularFormula) {
                this.averageMolecularFormula = averageMolecularFormula;
                return this;
            }

            /**
             * Todo.
             * 
             * @param repeatUnitAmountType
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder repeatUnitAmountType(CodeableConcept repeatUnitAmountType) {
                this.repeatUnitAmountType = repeatUnitAmountType;
                return this;
            }

            /**
             * Todo.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param repeatUnit
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder repeatUnit(RepeatUnit... repeatUnit) {
                for (RepeatUnit value : repeatUnit) {
                    this.repeatUnit.add(value);
                }
                return this;
            }

            /**
             * Todo.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param repeatUnit
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder repeatUnit(Collection<RepeatUnit> repeatUnit) {
                this.repeatUnit = new ArrayList<>(repeatUnit);
                return this;
            }

            /**
             * Build the {@link Repeat}
             * 
             * @return
             *     An immutable object of type {@link Repeat}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Repeat per the base specification
             */
            @Override
            public Repeat build() {
                Repeat repeat = new Repeat(this);
                if (validating) {
                    validate(repeat);
                }
                return repeat;
            }

            protected void validate(Repeat repeat) {
                super.validate(repeat);
                ValidationSupport.checkList(repeat.repeatUnit, "repeatUnit", RepeatUnit.class);
                ValidationSupport.requireValueOrChildren(repeat);
            }

            protected Builder from(Repeat repeat) {
                super.from(repeat);
                numberOfUnits = repeat.numberOfUnits;
                averageMolecularFormula = repeat.averageMolecularFormula;
                repeatUnitAmountType = repeat.repeatUnitAmountType;
                repeatUnit.addAll(repeat.repeatUnit);
                return this;
            }
        }

        /**
         * Todo.
         */
        public static class RepeatUnit extends BackboneElement {
            @Summary
            private final CodeableConcept orientationOfPolymerisation;
            @Summary
            private final String repeatUnit;
            @Summary
            private final SubstanceAmount amount;
            @Summary
            private final List<DegreeOfPolymerisation> degreeOfPolymerisation;
            @Summary
            private final List<StructuralRepresentation> structuralRepresentation;

            private RepeatUnit(Builder builder) {
                super(builder);
                orientationOfPolymerisation = builder.orientationOfPolymerisation;
                repeatUnit = builder.repeatUnit;
                amount = builder.amount;
                degreeOfPolymerisation = Collections.unmodifiableList(builder.degreeOfPolymerisation);
                structuralRepresentation = Collections.unmodifiableList(builder.structuralRepresentation);
            }

            /**
             * Todo.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getOrientationOfPolymerisation() {
                return orientationOfPolymerisation;
            }

            /**
             * Todo.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getRepeatUnit() {
                return repeatUnit;
            }

            /**
             * Todo.
             * 
             * @return
             *     An immutable object of type {@link SubstanceAmount} that may be null.
             */
            public SubstanceAmount getAmount() {
                return amount;
            }

            /**
             * Todo.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link DegreeOfPolymerisation} that may be empty.
             */
            public List<DegreeOfPolymerisation> getDegreeOfPolymerisation() {
                return degreeOfPolymerisation;
            }

            /**
             * Todo.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link StructuralRepresentation} that may be empty.
             */
            public List<StructuralRepresentation> getStructuralRepresentation() {
                return structuralRepresentation;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (orientationOfPolymerisation != null) || 
                    (repeatUnit != null) || 
                    (amount != null) || 
                    !degreeOfPolymerisation.isEmpty() || 
                    !structuralRepresentation.isEmpty();
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
                        accept(orientationOfPolymerisation, "orientationOfPolymerisation", visitor);
                        accept(repeatUnit, "repeatUnit", visitor);
                        accept(amount, "amount", visitor);
                        accept(degreeOfPolymerisation, "degreeOfPolymerisation", visitor, DegreeOfPolymerisation.class);
                        accept(structuralRepresentation, "structuralRepresentation", visitor, StructuralRepresentation.class);
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
                RepeatUnit other = (RepeatUnit) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(orientationOfPolymerisation, other.orientationOfPolymerisation) && 
                    Objects.equals(repeatUnit, other.repeatUnit) && 
                    Objects.equals(amount, other.amount) && 
                    Objects.equals(degreeOfPolymerisation, other.degreeOfPolymerisation) && 
                    Objects.equals(structuralRepresentation, other.structuralRepresentation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        orientationOfPolymerisation, 
                        repeatUnit, 
                        amount, 
                        degreeOfPolymerisation, 
                        structuralRepresentation);
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
                private CodeableConcept orientationOfPolymerisation;
                private String repeatUnit;
                private SubstanceAmount amount;
                private List<DegreeOfPolymerisation> degreeOfPolymerisation = new ArrayList<>();
                private List<StructuralRepresentation> structuralRepresentation = new ArrayList<>();

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
                 * @param orientationOfPolymerisation
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder orientationOfPolymerisation(CodeableConcept orientationOfPolymerisation) {
                    this.orientationOfPolymerisation = orientationOfPolymerisation;
                    return this;
                }

                /**
                 * Convenience method for setting {@code repeatUnit}.
                 * 
                 * @param repeatUnit
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #repeatUnit(com.ibm.fhir.model.type.String)
                 */
                public Builder repeatUnit(java.lang.String repeatUnit) {
                    this.repeatUnit = (repeatUnit == null) ? null : String.of(repeatUnit);
                    return this;
                }

                /**
                 * Todo.
                 * 
                 * @param repeatUnit
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder repeatUnit(String repeatUnit) {
                    this.repeatUnit = repeatUnit;
                    return this;
                }

                /**
                 * Todo.
                 * 
                 * @param amount
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(SubstanceAmount amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * Todo.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param degreeOfPolymerisation
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder degreeOfPolymerisation(DegreeOfPolymerisation... degreeOfPolymerisation) {
                    for (DegreeOfPolymerisation value : degreeOfPolymerisation) {
                        this.degreeOfPolymerisation.add(value);
                    }
                    return this;
                }

                /**
                 * Todo.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param degreeOfPolymerisation
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder degreeOfPolymerisation(Collection<DegreeOfPolymerisation> degreeOfPolymerisation) {
                    this.degreeOfPolymerisation = new ArrayList<>(degreeOfPolymerisation);
                    return this;
                }

                /**
                 * Todo.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param structuralRepresentation
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder structuralRepresentation(StructuralRepresentation... structuralRepresentation) {
                    for (StructuralRepresentation value : structuralRepresentation) {
                        this.structuralRepresentation.add(value);
                    }
                    return this;
                }

                /**
                 * Todo.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param structuralRepresentation
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder structuralRepresentation(Collection<StructuralRepresentation> structuralRepresentation) {
                    this.structuralRepresentation = new ArrayList<>(structuralRepresentation);
                    return this;
                }

                /**
                 * Build the {@link RepeatUnit}
                 * 
                 * @return
                 *     An immutable object of type {@link RepeatUnit}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid RepeatUnit per the base specification
                 */
                @Override
                public RepeatUnit build() {
                    RepeatUnit repeatUnit = new RepeatUnit(this);
                    if (validating) {
                        validate(repeatUnit);
                    }
                    return repeatUnit;
                }

                protected void validate(RepeatUnit repeatUnit) {
                    super.validate(repeatUnit);
                    ValidationSupport.checkList(repeatUnit.degreeOfPolymerisation, "degreeOfPolymerisation", DegreeOfPolymerisation.class);
                    ValidationSupport.checkList(repeatUnit.structuralRepresentation, "structuralRepresentation", StructuralRepresentation.class);
                    ValidationSupport.requireValueOrChildren(repeatUnit);
                }

                protected Builder from(RepeatUnit repeatUnit) {
                    super.from(repeatUnit);
                    orientationOfPolymerisation = repeatUnit.orientationOfPolymerisation;
                    this.repeatUnit = repeatUnit.repeatUnit;
                    amount = repeatUnit.amount;
                    degreeOfPolymerisation.addAll(repeatUnit.degreeOfPolymerisation);
                    structuralRepresentation.addAll(repeatUnit.structuralRepresentation);
                    return this;
                }
            }

            /**
             * Todo.
             */
            public static class DegreeOfPolymerisation extends BackboneElement {
                @Summary
                private final CodeableConcept degree;
                @Summary
                private final SubstanceAmount amount;

                private DegreeOfPolymerisation(Builder builder) {
                    super(builder);
                    degree = builder.degree;
                    amount = builder.amount;
                }

                /**
                 * Todo.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
                 */
                public CodeableConcept getDegree() {
                    return degree;
                }

                /**
                 * Todo.
                 * 
                 * @return
                 *     An immutable object of type {@link SubstanceAmount} that may be null.
                 */
                public SubstanceAmount getAmount() {
                    return amount;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (degree != null) || 
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
                            accept(degree, "degree", visitor);
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
                    DegreeOfPolymerisation other = (DegreeOfPolymerisation) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(degree, other.degree) && 
                        Objects.equals(amount, other.amount);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            degree, 
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
                    private CodeableConcept degree;
                    private SubstanceAmount amount;

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
                     * @param degree
                     *     Todo
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder degree(CodeableConcept degree) {
                        this.degree = degree;
                        return this;
                    }

                    /**
                     * Todo.
                     * 
                     * @param amount
                     *     Todo
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder amount(SubstanceAmount amount) {
                        this.amount = amount;
                        return this;
                    }

                    /**
                     * Build the {@link DegreeOfPolymerisation}
                     * 
                     * @return
                     *     An immutable object of type {@link DegreeOfPolymerisation}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid DegreeOfPolymerisation per the base specification
                     */
                    @Override
                    public DegreeOfPolymerisation build() {
                        DegreeOfPolymerisation degreeOfPolymerisation = new DegreeOfPolymerisation(this);
                        if (validating) {
                            validate(degreeOfPolymerisation);
                        }
                        return degreeOfPolymerisation;
                    }

                    protected void validate(DegreeOfPolymerisation degreeOfPolymerisation) {
                        super.validate(degreeOfPolymerisation);
                        ValidationSupport.requireValueOrChildren(degreeOfPolymerisation);
                    }

                    protected Builder from(DegreeOfPolymerisation degreeOfPolymerisation) {
                        super.from(degreeOfPolymerisation);
                        degree = degreeOfPolymerisation.degree;
                        amount = degreeOfPolymerisation.amount;
                        return this;
                    }
                }
            }

            /**
             * Todo.
             */
            public static class StructuralRepresentation extends BackboneElement {
                @Summary
                private final CodeableConcept type;
                @Summary
                private final String representation;
                @Summary
                private final Attachment attachment;

                private StructuralRepresentation(Builder builder) {
                    super(builder);
                    type = builder.type;
                    representation = builder.representation;
                    attachment = builder.attachment;
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
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getRepresentation() {
                    return representation;
                }

                /**
                 * Todo.
                 * 
                 * @return
                 *     An immutable object of type {@link Attachment} that may be null.
                 */
                public Attachment getAttachment() {
                    return attachment;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (type != null) || 
                        (representation != null) || 
                        (attachment != null);
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
                            accept(attachment, "attachment", visitor);
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
                    StructuralRepresentation other = (StructuralRepresentation) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(type, other.type) && 
                        Objects.equals(representation, other.representation) && 
                        Objects.equals(attachment, other.attachment);
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
                            attachment);
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
                    private Attachment attachment;

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
                     * Convenience method for setting {@code representation}.
                     * 
                     * @param representation
                     *     Todo
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
                     * Todo.
                     * 
                     * @param representation
                     *     Todo
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder representation(String representation) {
                        this.representation = representation;
                        return this;
                    }

                    /**
                     * Todo.
                     * 
                     * @param attachment
                     *     Todo
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder attachment(Attachment attachment) {
                        this.attachment = attachment;
                        return this;
                    }

                    /**
                     * Build the {@link StructuralRepresentation}
                     * 
                     * @return
                     *     An immutable object of type {@link StructuralRepresentation}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid StructuralRepresentation per the base specification
                     */
                    @Override
                    public StructuralRepresentation build() {
                        StructuralRepresentation structuralRepresentation = new StructuralRepresentation(this);
                        if (validating) {
                            validate(structuralRepresentation);
                        }
                        return structuralRepresentation;
                    }

                    protected void validate(StructuralRepresentation structuralRepresentation) {
                        super.validate(structuralRepresentation);
                        ValidationSupport.requireValueOrChildren(structuralRepresentation);
                    }

                    protected Builder from(StructuralRepresentation structuralRepresentation) {
                        super.from(structuralRepresentation);
                        type = structuralRepresentation.type;
                        representation = structuralRepresentation.representation;
                        attachment = structuralRepresentation.attachment;
                        return this;
                    }
                }
            }
        }
    }
}
