/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A populatioof people with some set of grouping criteria.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Population extends BackboneElement {
    private final Element age;
    private final CodeableConcept gender;
    private final CodeableConcept race;
    private final CodeableConcept physiologicalCondition;

    private Population(Builder builder) {
        super(builder);
        age = ValidationSupport.choiceElement(builder.age, "age", Range.class, CodeableConcept.class);
        gender = builder.gender;
        race = builder.race;
        physiologicalCondition = builder.physiologicalCondition;
    }

    /**
     * <p>
     * The age of the specific population.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getAge() {
        return age;
    }

    /**
     * <p>
     * The gender of the specific population.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getGender() {
        return gender;
    }

    /**
     * <p>
     * Race of the specific population.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getRace() {
        return race;
    }

    /**
     * <p>
     * The existing physiological conditions of the specific population to which this applies.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPhysiologicalCondition() {
        return physiologicalCondition;
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
                accept(age, "age", visitor, true);
                accept(gender, "gender", visitor);
                accept(race, "race", visitor);
                accept(physiologicalCondition, "physiologicalCondition", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
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
        private Element age;
        private CodeableConcept gender;
        private CodeableConcept race;
        private CodeableConcept physiologicalCondition;

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
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored even if unrecognized
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored even if unrecognized
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * The age of the specific population.
         * </p>
         * 
         * @param age
         *     The age of the specific population
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder age(Element age) {
            this.age = age;
            return this;
        }

        /**
         * <p>
         * The gender of the specific population.
         * </p>
         * 
         * @param gender
         *     The gender of the specific population
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder gender(CodeableConcept gender) {
            this.gender = gender;
            return this;
        }

        /**
         * <p>
         * Race of the specific population.
         * </p>
         * 
         * @param race
         *     Race of the specific population
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder race(CodeableConcept race) {
            this.race = race;
            return this;
        }

        /**
         * <p>
         * The existing physiological conditions of the specific population to which this applies.
         * </p>
         * 
         * @param physiologicalCondition
         *     The existing physiological conditions of the specific population to which this applies
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder physiologicalCondition(CodeableConcept physiologicalCondition) {
            this.physiologicalCondition = physiologicalCondition;
            return this;
        }

        @Override
        public Population build() {
            return new Population(this);
        }

        private Builder from(Population population) {
            id = population.id;
            extension.addAll(population.extension);
            modifierExtension.addAll(population.modifierExtension);
            age = population.age;
            gender = population.gender;
            race = population.race;
            physiologicalCondition = population.physiologicalCondition;
            return this;
        }
    }
}
