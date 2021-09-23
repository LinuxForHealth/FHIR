/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A populatioof people with some set of grouping criteria.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Population extends BackboneElement {
    @Summary
    @Choice({ Range.class, CodeableConcept.class })
    private final Element age;
    @Summary
    private final CodeableConcept gender;
    @Summary
    private final CodeableConcept race;
    @Summary
    private final CodeableConcept physiologicalCondition;

    private Population(Builder builder) {
        super(builder);
        age = builder.age;
        gender = builder.gender;
        race = builder.race;
        physiologicalCondition = builder.physiologicalCondition;
    }

    /**
     * The age of the specific population.
     * 
     * @return
     *     An immutable object of type {@link Range} or {@link CodeableConcept} that may be null.
     */
    public Element getAge() {
        return age;
    }

    /**
     * The gender of the specific population.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getGender() {
        return gender;
    }

    /**
     * Race of the specific population.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getRace() {
        return race;
    }

    /**
     * The existing physiological conditions of the specific population to which this applies.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPhysiologicalCondition() {
        return physiologicalCondition;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (age != null) || 
            (gender != null) || 
            (race != null) || 
            (physiologicalCondition != null);
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
                accept(age, "age", visitor);
                accept(gender, "gender", visitor);
                accept(race, "race", visitor);
                accept(physiologicalCondition, "physiologicalCondition", visitor);
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
        Population other = (Population) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(age, other.age) && 
            Objects.equals(gender, other.gender) && 
            Objects.equals(race, other.race) && 
            Objects.equals(physiologicalCondition, other.physiologicalCondition);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                modifierExtension, 
                age, 
                gender, 
                race, 
                physiologicalCondition);
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
        private Element age;
        private CodeableConcept gender;
        private CodeableConcept race;
        private CodeableConcept physiologicalCondition;

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
         * The age of the specific population.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Range}</li>
         * <li>{@link CodeableConcept}</li>
         * </ul>
         * 
         * @param age
         *     The age of the specific population
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder age(Element age) {
            this.age = age;
            return this;
        }

        /**
         * The gender of the specific population.
         * 
         * @param gender
         *     The gender of the specific population
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder gender(CodeableConcept gender) {
            this.gender = gender;
            return this;
        }

        /**
         * Race of the specific population.
         * 
         * @param race
         *     Race of the specific population
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder race(CodeableConcept race) {
            this.race = race;
            return this;
        }

        /**
         * The existing physiological conditions of the specific population to which this applies.
         * 
         * @param physiologicalCondition
         *     The existing physiological conditions of the specific population to which this applies
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder physiologicalCondition(CodeableConcept physiologicalCondition) {
            this.physiologicalCondition = physiologicalCondition;
            return this;
        }

        /**
         * Build the {@link Population}
         * 
         * @return
         *     An immutable object of type {@link Population}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Population per the base specification
         */
        @Override
        public Population build() {
            Population population = new Population(this);
            if (validating) {
                validate(population);
            }
            return population;
        }

        protected void validate(Population population) {
            super.validate(population);
            ValidationSupport.choiceElement(population.age, "age", Range.class, CodeableConcept.class);
            ValidationSupport.requireValueOrChildren(population);
        }

        protected Builder from(Population population) {
            super.from(population);
            age = population.age;
            gender = population.gender;
            race = population.race;
            physiologicalCondition = population.physiologicalCondition;
            return this;
        }
    }
}
