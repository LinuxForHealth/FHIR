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

import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.SubstanceAmount;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Todo.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class SubstancePolymer extends DomainResource {
    private final CodeableConcept clazz;
    private final CodeableConcept geometry;
    private final List<CodeableConcept> copolymerConnectivity;
    private final List<String> modification;
    private final List<MonomerSet> monomerSet;
    private final List<Repeat> repeat;

    private volatile int hashCode;

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
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getClazz() {
        return clazz;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getGeometry() {
        return geometry;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCopolymerConnectivity() {
        return copolymerConnectivity;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String}.
     */
    public List<String> getModification() {
        return modification;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MonomerSet}.
     */
    public List<MonomerSet> getMonomerSet() {
        return monomerSet;
    }

    /**
     * <p>
     * Todo.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Repeat}.
     */
    public List<Repeat> getRepeat() {
        return repeat;
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
                accept(clazz, "class", visitor);
                accept(geometry, "geometry", visitor);
                accept(copolymerConnectivity, "copolymerConnectivity", visitor, CodeableConcept.class);
                accept(modification, "modification", visitor, String.class);
                accept(monomerSet, "monomerSet", visitor, MonomerSet.class);
                accept(repeat, "repeat", visitor, Repeat.class);
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
        // optional
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
         * <p>
         * Todo.
         * </p>
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param copolymerConnectivity
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder copolymerConnectivity(Collection<CodeableConcept> copolymerConnectivity) {
            this.copolymerConnectivity = new ArrayList<>(copolymerConnectivity);
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modification
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder modification(Collection<String> modification) {
            this.modification = new ArrayList<>(modification);
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param monomerSet
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder monomerSet(Collection<MonomerSet> monomerSet) {
            this.monomerSet = new ArrayList<>(monomerSet);
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
         * <p>
         * Todo.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param repeat
         *     Todo
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder repeat(Collection<Repeat> repeat) {
            this.repeat = new ArrayList<>(repeat);
            return this;
        }

        @Override
        public SubstancePolymer build() {
            return new SubstancePolymer(this);
        }

        private Builder from(SubstancePolymer substancePolymer) {
            id = substancePolymer.id;
            meta = substancePolymer.meta;
            implicitRules = substancePolymer.implicitRules;
            language = substancePolymer.language;
            text = substancePolymer.text;
            contained.addAll(substancePolymer.contained);
            extension.addAll(substancePolymer.extension);
            modifierExtension.addAll(substancePolymer.modifierExtension);
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
     * <p>
     * Todo.
     * </p>
     */
    public static class MonomerSet extends BackboneElement {
        private final CodeableConcept ratioType;
        private final List<StartingMaterial> startingMaterial;

        private volatile int hashCode;

        private MonomerSet(Builder builder) {
            super(builder);
            ratioType = builder.ratioType;
            startingMaterial = Collections.unmodifiableList(builder.startingMaterial);
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRatioType() {
            return ratioType;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link StartingMaterial}.
         */
        public List<StartingMaterial> getStartingMaterial() {
            return startingMaterial;
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
                    accept(ratioType, "ratioType", visitor);
                    accept(startingMaterial, "startingMaterial", visitor, StartingMaterial.class);
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
            // optional
            private CodeableConcept ratioType;
            private List<StartingMaterial> startingMaterial = new ArrayList<>();

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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param startingMaterial
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder startingMaterial(Collection<StartingMaterial> startingMaterial) {
                this.startingMaterial = new ArrayList<>(startingMaterial);
                return this;
            }

            @Override
            public MonomerSet build() {
                return new MonomerSet(this);
            }

            private Builder from(MonomerSet monomerSet) {
                id = monomerSet.id;
                extension.addAll(monomerSet.extension);
                modifierExtension.addAll(monomerSet.modifierExtension);
                ratioType = monomerSet.ratioType;
                startingMaterial.addAll(monomerSet.startingMaterial);
                return this;
            }
        }

        /**
         * <p>
         * Todo.
         * </p>
         */
        public static class StartingMaterial extends BackboneElement {
            private final CodeableConcept material;
            private final CodeableConcept type;
            private final Boolean isDefining;
            private final SubstanceAmount amount;

            private volatile int hashCode;

            private StartingMaterial(Builder builder) {
                super(builder);
                material = builder.material;
                type = builder.type;
                isDefining = builder.isDefining;
                amount = builder.amount;
            }

            /**
             * <p>
             * Todo.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getMaterial() {
                return material;
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
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getIsDefining() {
                return isDefining;
            }

            /**
             * <p>
             * Todo.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link SubstanceAmount}.
             */
            public SubstanceAmount getAmount() {
                return amount;
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
                        accept(material, "material", visitor);
                        accept(type, "type", visitor);
                        accept(isDefining, "isDefining", visitor);
                        accept(amount, "amount", visitor);
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
                // optional
                private CodeableConcept material;
                private CodeableConcept type;
                private Boolean isDefining;
                private SubstanceAmount amount;

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
                public Builder amount(SubstanceAmount amount) {
                    this.amount = amount;
                    return this;
                }

                @Override
                public StartingMaterial build() {
                    return new StartingMaterial(this);
                }

                private Builder from(StartingMaterial startingMaterial) {
                    id = startingMaterial.id;
                    extension.addAll(startingMaterial.extension);
                    modifierExtension.addAll(startingMaterial.modifierExtension);
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
     * <p>
     * Todo.
     * </p>
     */
    public static class Repeat extends BackboneElement {
        private final Integer numberOfUnits;
        private final String averageMolecularFormula;
        private final CodeableConcept repeatUnitAmountType;
        private final List<RepeatUnit> repeatUnit;

        private volatile int hashCode;

        private Repeat(Builder builder) {
            super(builder);
            numberOfUnits = builder.numberOfUnits;
            averageMolecularFormula = builder.averageMolecularFormula;
            repeatUnitAmountType = builder.repeatUnitAmountType;
            repeatUnit = Collections.unmodifiableList(builder.repeatUnit);
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getNumberOfUnits() {
            return numberOfUnits;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getAverageMolecularFormula() {
            return averageMolecularFormula;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRepeatUnitAmountType() {
            return repeatUnitAmountType;
        }

        /**
         * <p>
         * Todo.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link RepeatUnit}.
         */
        public List<RepeatUnit> getRepeatUnit() {
            return repeatUnit;
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
                    accept(numberOfUnits, "numberOfUnits", visitor);
                    accept(averageMolecularFormula, "averageMolecularFormula", visitor);
                    accept(repeatUnitAmountType, "repeatUnitAmountType", visitor);
                    accept(repeatUnit, "repeatUnit", visitor, RepeatUnit.class);
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
            // optional
            private Integer numberOfUnits;
            private String averageMolecularFormula;
            private CodeableConcept repeatUnitAmountType;
            private List<RepeatUnit> repeatUnit = new ArrayList<>();

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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Todo.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param repeatUnit
             *     Todo
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder repeatUnit(Collection<RepeatUnit> repeatUnit) {
                this.repeatUnit = new ArrayList<>(repeatUnit);
                return this;
            }

            @Override
            public Repeat build() {
                return new Repeat(this);
            }

            private Builder from(Repeat repeat) {
                id = repeat.id;
                extension.addAll(repeat.extension);
                modifierExtension.addAll(repeat.modifierExtension);
                numberOfUnits = repeat.numberOfUnits;
                averageMolecularFormula = repeat.averageMolecularFormula;
                repeatUnitAmountType = repeat.repeatUnitAmountType;
                repeatUnit.addAll(repeat.repeatUnit);
                return this;
            }
        }

        /**
         * <p>
         * Todo.
         * </p>
         */
        public static class RepeatUnit extends BackboneElement {
            private final CodeableConcept orientationOfPolymerisation;
            private final String repeatUnit;
            private final SubstanceAmount amount;
            private final List<DegreeOfPolymerisation> degreeOfPolymerisation;
            private final List<StructuralRepresentation> structuralRepresentation;

            private volatile int hashCode;

            private RepeatUnit(Builder builder) {
                super(builder);
                orientationOfPolymerisation = builder.orientationOfPolymerisation;
                repeatUnit = builder.repeatUnit;
                amount = builder.amount;
                degreeOfPolymerisation = Collections.unmodifiableList(builder.degreeOfPolymerisation);
                structuralRepresentation = Collections.unmodifiableList(builder.structuralRepresentation);
            }

            /**
             * <p>
             * Todo.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getOrientationOfPolymerisation() {
                return orientationOfPolymerisation;
            }

            /**
             * <p>
             * Todo.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getRepeatUnit() {
                return repeatUnit;
            }

            /**
             * <p>
             * Todo.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link SubstanceAmount}.
             */
            public SubstanceAmount getAmount() {
                return amount;
            }

            /**
             * <p>
             * Todo.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link DegreeOfPolymerisation}.
             */
            public List<DegreeOfPolymerisation> getDegreeOfPolymerisation() {
                return degreeOfPolymerisation;
            }

            /**
             * <p>
             * Todo.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link StructuralRepresentation}.
             */
            public List<StructuralRepresentation> getStructuralRepresentation() {
                return structuralRepresentation;
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
                        accept(orientationOfPolymerisation, "orientationOfPolymerisation", visitor);
                        accept(repeatUnit, "repeatUnit", visitor);
                        accept(amount, "amount", visitor);
                        accept(degreeOfPolymerisation, "degreeOfPolymerisation", visitor, DegreeOfPolymerisation.class);
                        accept(structuralRepresentation, "structuralRepresentation", visitor, StructuralRepresentation.class);
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
                // optional
                private CodeableConcept orientationOfPolymerisation;
                private String repeatUnit;
                private SubstanceAmount amount;
                private List<DegreeOfPolymerisation> degreeOfPolymerisation = new ArrayList<>();
                private List<StructuralRepresentation> structuralRepresentation = new ArrayList<>();

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
                 * <p>
                 * Todo.
                 * </p>
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
                public Builder amount(SubstanceAmount amount) {
                    this.amount = amount;
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
                 * <p>
                 * Todo.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param degreeOfPolymerisation
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder degreeOfPolymerisation(Collection<DegreeOfPolymerisation> degreeOfPolymerisation) {
                    this.degreeOfPolymerisation = new ArrayList<>(degreeOfPolymerisation);
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
                 * <p>
                 * Todo.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param structuralRepresentation
                 *     Todo
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder structuralRepresentation(Collection<StructuralRepresentation> structuralRepresentation) {
                    this.structuralRepresentation = new ArrayList<>(structuralRepresentation);
                    return this;
                }

                @Override
                public RepeatUnit build() {
                    return new RepeatUnit(this);
                }

                private Builder from(RepeatUnit repeatUnit) {
                    id = repeatUnit.id;
                    extension.addAll(repeatUnit.extension);
                    modifierExtension.addAll(repeatUnit.modifierExtension);
                    orientationOfPolymerisation = repeatUnit.orientationOfPolymerisation;
                    this.repeatUnit = repeatUnit.repeatUnit;
                    amount = repeatUnit.amount;
                    degreeOfPolymerisation.addAll(repeatUnit.degreeOfPolymerisation);
                    structuralRepresentation.addAll(repeatUnit.structuralRepresentation);
                    return this;
                }
            }

            /**
             * <p>
             * Todo.
             * </p>
             */
            public static class DegreeOfPolymerisation extends BackboneElement {
                private final CodeableConcept degree;
                private final SubstanceAmount amount;

                private volatile int hashCode;

                private DegreeOfPolymerisation(Builder builder) {
                    super(builder);
                    degree = builder.degree;
                    amount = builder.amount;
                }

                /**
                 * <p>
                 * Todo.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getDegree() {
                    return degree;
                }

                /**
                 * <p>
                 * Todo.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link SubstanceAmount}.
                 */
                public SubstanceAmount getAmount() {
                    return amount;
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
                            accept(degree, "degree", visitor);
                            accept(amount, "amount", visitor);
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
                    // optional
                    private CodeableConcept degree;
                    private SubstanceAmount amount;

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
                    public Builder amount(SubstanceAmount amount) {
                        this.amount = amount;
                        return this;
                    }

                    @Override
                    public DegreeOfPolymerisation build() {
                        return new DegreeOfPolymerisation(this);
                    }

                    private Builder from(DegreeOfPolymerisation degreeOfPolymerisation) {
                        id = degreeOfPolymerisation.id;
                        extension.addAll(degreeOfPolymerisation.extension);
                        modifierExtension.addAll(degreeOfPolymerisation.modifierExtension);
                        degree = degreeOfPolymerisation.degree;
                        amount = degreeOfPolymerisation.amount;
                        return this;
                    }
                }
            }

            /**
             * <p>
             * Todo.
             * </p>
             */
            public static class StructuralRepresentation extends BackboneElement {
                private final CodeableConcept type;
                private final String representation;
                private final Attachment attachment;

                private volatile int hashCode;

                private StructuralRepresentation(Builder builder) {
                    super(builder);
                    type = builder.type;
                    representation = builder.representation;
                    attachment = builder.attachment;
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
                 *     An immutable object of type {@link String}.
                 */
                public String getRepresentation() {
                    return representation;
                }

                /**
                 * <p>
                 * Todo.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Attachment}.
                 */
                public Attachment getAttachment() {
                    return attachment;
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
                            accept(representation, "representation", visitor);
                            accept(attachment, "attachment", visitor);
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
                    // optional
                    private CodeableConcept type;
                    private String representation;
                    private Attachment attachment;

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
                     * <p>
                     * Todo.
                     * </p>
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

                    @Override
                    public StructuralRepresentation build() {
                        return new StructuralRepresentation(this);
                    }

                    private Builder from(StructuralRepresentation structuralRepresentation) {
                        id = structuralRepresentation.id;
                        extension.addAll(structuralRepresentation.extension);
                        modifierExtension.addAll(structuralRepresentation.modifierExtension);
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
