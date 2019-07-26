/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Indicates how the medication is/was taken or should be taken by the patient.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Dosage extends BackboneElement {
    private final Integer sequence;
    private final String text;
    private final List<CodeableConcept> additionalInstruction;
    private final String patientInstruction;
    private final Timing timing;
    private final Element asNeeded;
    private final CodeableConcept site;
    private final CodeableConcept route;
    private final CodeableConcept method;
    private final List<DoseAndRate> doseAndRate;
    private final Ratio maxDosePerPeriod;
    private final Quantity maxDosePerAdministration;
    private final Quantity maxDosePerLifetime;

    private volatile int hashCode;

    private Dosage(Builder builder) {
        super(builder);
        sequence = builder.sequence;
        text = builder.text;
        additionalInstruction = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.additionalInstruction, "additionalInstruction"));
        patientInstruction = builder.patientInstruction;
        timing = builder.timing;
        asNeeded = ValidationSupport.choiceElement(builder.asNeeded, "asNeeded", Boolean.class, CodeableConcept.class);
        site = builder.site;
        route = builder.route;
        method = builder.method;
        doseAndRate = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.doseAndRate, "doseAndRate"));
        maxDosePerPeriod = builder.maxDosePerPeriod;
        maxDosePerAdministration = builder.maxDosePerAdministration;
        maxDosePerLifetime = builder.maxDosePerLifetime;
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * <p>
     * Indicates the order in which the dosage instructions should be applied or interpreted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Integer}.
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * <p>
     * Free text dosage instructions e.g. SIG.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getText() {
        return text;
    }

    /**
     * <p>
     * Supplemental instructions to the patient on how to take the medication (e.g. "with meals" or"take half to one hour 
     * before food") or warnings for the patient about the medication (e.g. "may cause drowsiness" or "avoid exposure of skin 
     * to direct sunlight or sunlamps").
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getAdditionalInstruction() {
        return additionalInstruction;
    }

    /**
     * <p>
     * Instructions in terms that are understood by the patient or consumer.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPatientInstruction() {
        return patientInstruction;
    }

    /**
     * <p>
     * When medication should be administered.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Timing}.
     */
    public Timing getTiming() {
        return timing;
    }

    /**
     * <p>
     * Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it 
     * indicates the precondition for taking the Medication (CodeableConcept).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getAsNeeded() {
        return asNeeded;
    }

    /**
     * <p>
     * Body site to administer to.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSite() {
        return site;
    }

    /**
     * <p>
     * How drug should enter body.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getRoute() {
        return route;
    }

    /**
     * <p>
     * Technique for administering medication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * <p>
     * The amount of medication administered.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DoseAndRate}.
     */
    public List<DoseAndRate> getDoseAndRate() {
        return doseAndRate;
    }

    /**
     * <p>
     * Upper limit on medication per unit of time.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Ratio}.
     */
    public Ratio getMaxDosePerPeriod() {
        return maxDosePerPeriod;
    }

    /**
     * <p>
     * Upper limit on medication per administration.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getMaxDosePerAdministration() {
        return maxDosePerAdministration;
    }

    /**
     * <p>
     * Upper limit on medication per lifetime of the patient.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getMaxDosePerLifetime() {
        return maxDosePerLifetime;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (sequence != null) || 
            (text != null) || 
            !additionalInstruction.isEmpty() || 
            (patientInstruction != null) || 
            (timing != null) || 
            (asNeeded != null) || 
            (site != null) || 
            (route != null) || 
            (method != null) || 
            !doseAndRate.isEmpty() || 
            (maxDosePerPeriod != null) || 
            (maxDosePerAdministration != null) || 
            (maxDosePerLifetime != null);
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
                accept(sequence, "sequence", visitor);
                accept(text, "text", visitor);
                accept(additionalInstruction, "additionalInstruction", visitor, CodeableConcept.class);
                accept(patientInstruction, "patientInstruction", visitor);
                accept(timing, "timing", visitor);
                accept(asNeeded, "asNeeded", visitor);
                accept(site, "site", visitor);
                accept(route, "route", visitor);
                accept(method, "method", visitor);
                accept(doseAndRate, "doseAndRate", visitor, DoseAndRate.class);
                accept(maxDosePerPeriod, "maxDosePerPeriod", visitor);
                accept(maxDosePerAdministration, "maxDosePerAdministration", visitor);
                accept(maxDosePerLifetime, "maxDosePerLifetime", visitor);
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
        Dosage other = (Dosage) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(sequence, other.sequence) && 
            Objects.equals(text, other.text) && 
            Objects.equals(additionalInstruction, other.additionalInstruction) && 
            Objects.equals(patientInstruction, other.patientInstruction) && 
            Objects.equals(timing, other.timing) && 
            Objects.equals(asNeeded, other.asNeeded) && 
            Objects.equals(site, other.site) && 
            Objects.equals(route, other.route) && 
            Objects.equals(method, other.method) && 
            Objects.equals(doseAndRate, other.doseAndRate) && 
            Objects.equals(maxDosePerPeriod, other.maxDosePerPeriod) && 
            Objects.equals(maxDosePerAdministration, other.maxDosePerAdministration) && 
            Objects.equals(maxDosePerLifetime, other.maxDosePerLifetime);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                modifierExtension, 
                sequence, 
                text, 
                additionalInstruction, 
                patientInstruction, 
                timing, 
                asNeeded, 
                site, 
                route, 
                method, 
                doseAndRate, 
                maxDosePerPeriod, 
                maxDosePerAdministration, 
                maxDosePerLifetime);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        Builder builder = new Builder();
        return builder;
    }

    public static class Builder extends BackboneElement.Builder {
        private Integer sequence;
        private String text;
        private List<CodeableConcept> additionalInstruction = new ArrayList<>();
        private String patientInstruction;
        private Timing timing;
        private Element asNeeded;
        private CodeableConcept site;
        private CodeableConcept route;
        private CodeableConcept method;
        private List<DoseAndRate> doseAndRate = new ArrayList<>();
        private Ratio maxDosePerPeriod;
        private Quantity maxDosePerAdministration;
        private Quantity maxDosePerLifetime;

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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * Indicates the order in which the dosage instructions should be applied or interpreted.
         * </p>
         * 
         * @param sequence
         *     The order of the dosage instructions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sequence(Integer sequence) {
            this.sequence = sequence;
            return this;
        }

        /**
         * <p>
         * Free text dosage instructions e.g. SIG.
         * </p>
         * 
         * @param text
         *     Free text dosage instructions e.g. SIG
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * <p>
         * Supplemental instructions to the patient on how to take the medication (e.g. "with meals" or"take half to one hour 
         * before food") or warnings for the patient about the medication (e.g. "may cause drowsiness" or "avoid exposure of skin 
         * to direct sunlight or sunlamps").
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param additionalInstruction
         *     Supplemental instruction or warnings to the patient - e.g. "with meals", "may cause drowsiness"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder additionalInstruction(CodeableConcept... additionalInstruction) {
            for (CodeableConcept value : additionalInstruction) {
                this.additionalInstruction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Supplemental instructions to the patient on how to take the medication (e.g. "with meals" or"take half to one hour 
         * before food") or warnings for the patient about the medication (e.g. "may cause drowsiness" or "avoid exposure of skin 
         * to direct sunlight or sunlamps").
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param additionalInstruction
         *     Supplemental instruction or warnings to the patient - e.g. "with meals", "may cause drowsiness"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder additionalInstruction(Collection<CodeableConcept> additionalInstruction) {
            this.additionalInstruction = new ArrayList<>(additionalInstruction);
            return this;
        }

        /**
         * <p>
         * Instructions in terms that are understood by the patient or consumer.
         * </p>
         * 
         * @param patientInstruction
         *     Patient or consumer oriented instructions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patientInstruction(String patientInstruction) {
            this.patientInstruction = patientInstruction;
            return this;
        }

        /**
         * <p>
         * When medication should be administered.
         * </p>
         * 
         * @param timing
         *     When medication should be administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder timing(Timing timing) {
            this.timing = timing;
            return this;
        }

        /**
         * <p>
         * Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it 
         * indicates the precondition for taking the Medication (CodeableConcept).
         * </p>
         * 
         * @param asNeeded
         *     Take "as needed" (for x)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder asNeeded(Element asNeeded) {
            this.asNeeded = asNeeded;
            return this;
        }

        /**
         * <p>
         * Body site to administer to.
         * </p>
         * 
         * @param site
         *     Body site to administer to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder site(CodeableConcept site) {
            this.site = site;
            return this;
        }

        /**
         * <p>
         * How drug should enter body.
         * </p>
         * 
         * @param route
         *     How drug should enter body
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder route(CodeableConcept route) {
            this.route = route;
            return this;
        }

        /**
         * <p>
         * Technique for administering medication.
         * </p>
         * 
         * @param method
         *     Technique for administering medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder method(CodeableConcept method) {
            this.method = method;
            return this;
        }

        /**
         * <p>
         * The amount of medication administered.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param doseAndRate
         *     Amount of medication administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseAndRate(DoseAndRate... doseAndRate) {
            for (DoseAndRate value : doseAndRate) {
                this.doseAndRate.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The amount of medication administered.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param doseAndRate
         *     Amount of medication administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder doseAndRate(Collection<DoseAndRate> doseAndRate) {
            this.doseAndRate = new ArrayList<>(doseAndRate);
            return this;
        }

        /**
         * <p>
         * Upper limit on medication per unit of time.
         * </p>
         * 
         * @param maxDosePerPeriod
         *     Upper limit on medication per unit of time
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maxDosePerPeriod(Ratio maxDosePerPeriod) {
            this.maxDosePerPeriod = maxDosePerPeriod;
            return this;
        }

        /**
         * <p>
         * Upper limit on medication per administration.
         * </p>
         * 
         * @param maxDosePerAdministration
         *     Upper limit on medication per administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maxDosePerAdministration(Quantity maxDosePerAdministration) {
            this.maxDosePerAdministration = maxDosePerAdministration;
            return this;
        }

        /**
         * <p>
         * Upper limit on medication per lifetime of the patient.
         * </p>
         * 
         * @param maxDosePerLifetime
         *     Upper limit on medication per lifetime of the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maxDosePerLifetime(Quantity maxDosePerLifetime) {
            this.maxDosePerLifetime = maxDosePerLifetime;
            return this;
        }

        @Override
        public Dosage build() {
            return new Dosage(this);
        }

        protected Builder from(Dosage dosage) {
            super.from(dosage);
            sequence = dosage.sequence;
            text = dosage.text;
            additionalInstruction.addAll(dosage.additionalInstruction);
            patientInstruction = dosage.patientInstruction;
            timing = dosage.timing;
            asNeeded = dosage.asNeeded;
            site = dosage.site;
            route = dosage.route;
            method = dosage.method;
            doseAndRate.addAll(dosage.doseAndRate);
            maxDosePerPeriod = dosage.maxDosePerPeriod;
            maxDosePerAdministration = dosage.maxDosePerAdministration;
            maxDosePerLifetime = dosage.maxDosePerLifetime;
            return this;
        }
    }

    /**
     * <p>
     * The amount of medication administered.
     * </p>
     */
    public static class DoseAndRate extends BackboneElement {
        private final CodeableConcept type;
        private final Element dose;
        private final Element rate;

        private volatile int hashCode;

        private DoseAndRate(Builder builder) {
            super(builder);
            type = builder.type;
            dose = ValidationSupport.choiceElement(builder.dose, "dose", Range.class, Quantity.class);
            rate = ValidationSupport.choiceElement(builder.rate, "rate", Ratio.class, Range.class, Quantity.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The kind of dose or rate specified, for example, ordered or calculated.
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
         * Amount of medication per dose.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getDose() {
            return dose;
        }

        /**
         * <p>
         * Amount of medication per unit of time.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getRate() {
            return rate;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (dose != null) || 
                (rate != null);
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
                    accept(dose, "dose", visitor);
                    accept(rate, "rate", visitor);
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
            DoseAndRate other = (DoseAndRate) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(dose, other.dose) && 
                Objects.equals(rate, other.rate);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    dose, 
                    rate);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private Element dose;
            private Element rate;

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
             * Adds new element(s) to existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * </p>
             * <p>
             * Adds new element(s) to existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * The kind of dose or rate specified, for example, ordered or calculated.
             * </p>
             * 
             * @param type
             *     The kind of dose or rate specified
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
             * Amount of medication per dose.
             * </p>
             * 
             * @param dose
             *     Amount of medication per dose
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dose(Element dose) {
                this.dose = dose;
                return this;
            }

            /**
             * <p>
             * Amount of medication per unit of time.
             * </p>
             * 
             * @param rate
             *     Amount of medication per unit of time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rate(Element rate) {
                this.rate = rate;
                return this;
            }

            @Override
            public DoseAndRate build() {
                return new DoseAndRate(this);
            }

            protected Builder from(DoseAndRate doseAndRate) {
                super.from(doseAndRate);
                type = doseAndRate.type;
                dose = doseAndRate.dose;
                rate = doseAndRate.rate;
                return this;
            }
        }
    }
}
