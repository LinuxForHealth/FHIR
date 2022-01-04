/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Indicates how the medication is/was taken or should be taken by the patient.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Dosage extends BackboneElement {
    @Summary
    private final Integer sequence;
    @Summary
    private final String text;
    @Summary
    @Binding(
        bindingName = "AdditionalInstruction",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/additional-instruction-codes"
    )
    private final List<CodeableConcept> additionalInstruction;
    @Summary
    private final String patientInstruction;
    @Summary
    private final Timing timing;
    @Summary
    @Choice({ Boolean.class, CodeableConcept.class })
    @Binding(
        bindingName = "MedicationAsNeededReason",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/medication-as-needed-reason"
    )
    private final Element asNeeded;
    @Summary
    @Binding(
        bindingName = "MedicationAdministrationSite",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/approach-site-codes"
    )
    private final CodeableConcept site;
    @Summary
    @Binding(
        bindingName = "RouteOfAdministration",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/route-codes"
    )
    private final CodeableConcept route;
    @Summary
    @Binding(
        bindingName = "MedicationAdministrationMethod",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/administration-method-codes"
    )
    private final CodeableConcept method;
    @Summary
    private final List<DoseAndRate> doseAndRate;
    @Summary
    private final Ratio maxDosePerPeriod;
    @Summary
    private final SimpleQuantity maxDosePerAdministration;
    @Summary
    private final SimpleQuantity maxDosePerLifetime;

    private Dosage(Builder builder) {
        super(builder);
        sequence = builder.sequence;
        text = builder.text;
        additionalInstruction = Collections.unmodifiableList(builder.additionalInstruction);
        patientInstruction = builder.patientInstruction;
        timing = builder.timing;
        asNeeded = builder.asNeeded;
        site = builder.site;
        route = builder.route;
        method = builder.method;
        doseAndRate = Collections.unmodifiableList(builder.doseAndRate);
        maxDosePerPeriod = builder.maxDosePerPeriod;
        maxDosePerAdministration = builder.maxDosePerAdministration;
        maxDosePerLifetime = builder.maxDosePerLifetime;
    }

    /**
     * Indicates the order in which the dosage instructions should be applied or interpreted.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * Free text dosage instructions e.g. SIG.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getText() {
        return text;
    }

    /**
     * Supplemental instructions to the patient on how to take the medication (e.g. "with meals" or"take half to one hour 
     * before food") or warnings for the patient about the medication (e.g. "may cause drowsiness" or "avoid exposure of skin 
     * to direct sunlight or sunlamps").
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getAdditionalInstruction() {
        return additionalInstruction;
    }

    /**
     * Instructions in terms that are understood by the patient or consumer.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPatientInstruction() {
        return patientInstruction;
    }

    /**
     * When medication should be administered.
     * 
     * @return
     *     An immutable object of type {@link Timing} that may be null.
     */
    public Timing getTiming() {
        return timing;
    }

    /**
     * Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it 
     * indicates the precondition for taking the Medication (CodeableConcept).
     * 
     * @return
     *     An immutable object of type {@link Boolean} or {@link CodeableConcept} that may be null.
     */
    public Element getAsNeeded() {
        return asNeeded;
    }

    /**
     * Body site to administer to.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSite() {
        return site;
    }

    /**
     * How drug should enter body.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getRoute() {
        return route;
    }

    /**
     * Technique for administering medication.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * The amount of medication administered.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DoseAndRate} that may be empty.
     */
    public List<DoseAndRate> getDoseAndRate() {
        return doseAndRate;
    }

    /**
     * Upper limit on medication per unit of time.
     * 
     * @return
     *     An immutable object of type {@link Ratio} that may be null.
     */
    public Ratio getMaxDosePerPeriod() {
        return maxDosePerPeriod;
    }

    /**
     * Upper limit on medication per administration.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getMaxDosePerAdministration() {
        return maxDosePerAdministration;
    }

    /**
     * Upper limit on medication per lifetime of the patient.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getMaxDosePerLifetime() {
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
        return new Builder();
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
        private SimpleQuantity maxDosePerAdministration;
        private SimpleQuantity maxDosePerLifetime;

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
         * Convenience method for setting {@code sequence}.
         * 
         * @param sequence
         *     The order of the dosage instructions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #sequence(com.ibm.fhir.model.type.Integer)
         */
        public Builder sequence(java.lang.Integer sequence) {
            this.sequence = (sequence == null) ? null : Integer.of(sequence);
            return this;
        }

        /**
         * Indicates the order in which the dosage instructions should be applied or interpreted.
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
         * Convenience method for setting {@code text}.
         * 
         * @param text
         *     Free text dosage instructions e.g. SIG
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #text(com.ibm.fhir.model.type.String)
         */
        public Builder text(java.lang.String text) {
            this.text = (text == null) ? null : String.of(text);
            return this;
        }

        /**
         * Free text dosage instructions e.g. SIG.
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
         * Supplemental instructions to the patient on how to take the medication (e.g. "with meals" or"take half to one hour 
         * before food") or warnings for the patient about the medication (e.g. "may cause drowsiness" or "avoid exposure of skin 
         * to direct sunlight or sunlamps").
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Supplemental instructions to the patient on how to take the medication (e.g. "with meals" or"take half to one hour 
         * before food") or warnings for the patient about the medication (e.g. "may cause drowsiness" or "avoid exposure of skin 
         * to direct sunlight or sunlamps").
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param additionalInstruction
         *     Supplemental instruction or warnings to the patient - e.g. "with meals", "may cause drowsiness"
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder additionalInstruction(Collection<CodeableConcept> additionalInstruction) {
            this.additionalInstruction = new ArrayList<>(additionalInstruction);
            return this;
        }

        /**
         * Convenience method for setting {@code patientInstruction}.
         * 
         * @param patientInstruction
         *     Patient or consumer oriented instructions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #patientInstruction(com.ibm.fhir.model.type.String)
         */
        public Builder patientInstruction(java.lang.String patientInstruction) {
            this.patientInstruction = (patientInstruction == null) ? null : String.of(patientInstruction);
            return this;
        }

        /**
         * Instructions in terms that are understood by the patient or consumer.
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
         * When medication should be administered.
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
         * Convenience method for setting {@code asNeeded} with choice type Boolean.
         * 
         * @param asNeeded
         *     Take "as needed" (for x)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #asNeeded(Element)
         */
        public Builder asNeeded(java.lang.Boolean asNeeded) {
            this.asNeeded = (asNeeded == null) ? null : Boolean.of(asNeeded);
            return this;
        }

        /**
         * Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it 
         * indicates the precondition for taking the Medication (CodeableConcept).
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Boolean}</li>
         * <li>{@link CodeableConcept}</li>
         * </ul>
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
         * Body site to administer to.
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
         * How drug should enter body.
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
         * Technique for administering medication.
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
         * The amount of medication administered.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The amount of medication administered.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param doseAndRate
         *     Amount of medication administered
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder doseAndRate(Collection<DoseAndRate> doseAndRate) {
            this.doseAndRate = new ArrayList<>(doseAndRate);
            return this;
        }

        /**
         * Upper limit on medication per unit of time.
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
         * Upper limit on medication per administration.
         * 
         * @param maxDosePerAdministration
         *     Upper limit on medication per administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maxDosePerAdministration(SimpleQuantity maxDosePerAdministration) {
            this.maxDosePerAdministration = maxDosePerAdministration;
            return this;
        }

        /**
         * Upper limit on medication per lifetime of the patient.
         * 
         * @param maxDosePerLifetime
         *     Upper limit on medication per lifetime of the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maxDosePerLifetime(SimpleQuantity maxDosePerLifetime) {
            this.maxDosePerLifetime = maxDosePerLifetime;
            return this;
        }

        /**
         * Build the {@link Dosage}
         * 
         * @return
         *     An immutable object of type {@link Dosage}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Dosage per the base specification
         */
        @Override
        public Dosage build() {
            Dosage dosage = new Dosage(this);
            if (validating) {
                validate(dosage);
            }
            return dosage;
        }

        protected void validate(Dosage dosage) {
            super.validate(dosage);
            ValidationSupport.checkList(dosage.additionalInstruction, "additionalInstruction", CodeableConcept.class);
            ValidationSupport.choiceElement(dosage.asNeeded, "asNeeded", Boolean.class, CodeableConcept.class);
            ValidationSupport.checkList(dosage.doseAndRate, "doseAndRate", DoseAndRate.class);
            ValidationSupport.requireValueOrChildren(dosage);
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
     * The amount of medication administered.
     */
    public static class DoseAndRate extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "DoseAndRateType",
            strength = BindingStrength.Value.EXAMPLE,
            valueSet = "http://hl7.org/fhir/ValueSet/dose-rate-type"
        )
        private final CodeableConcept type;
        @Summary
        @Choice({ Range.class, SimpleQuantity.class })
        private final Element dose;
        @Summary
        @Choice({ Ratio.class, Range.class, SimpleQuantity.class })
        private final Element rate;

        private DoseAndRate(Builder builder) {
            super(builder);
            type = builder.type;
            dose = builder.dose;
            rate = builder.rate;
        }

        /**
         * The kind of dose or rate specified, for example, ordered or calculated.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Amount of medication per dose.
         * 
         * @return
         *     An immutable object of type {@link Range} or {@link SimpleQuantity} that may be null.
         */
        public Element getDose() {
            return dose;
        }

        /**
         * Amount of medication per unit of time.
         * 
         * @return
         *     An immutable object of type {@link Ratio}, {@link Range} or {@link SimpleQuantity} that may be null.
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
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept type;
            private Element dose;
            private Element rate;

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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * The kind of dose or rate specified, for example, ordered or calculated.
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
             * Amount of medication per dose.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Range}</li>
             * <li>{@link SimpleQuantity}</li>
             * </ul>
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
             * Amount of medication per unit of time.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Ratio}</li>
             * <li>{@link Range}</li>
             * <li>{@link SimpleQuantity}</li>
             * </ul>
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

            /**
             * Build the {@link DoseAndRate}
             * 
             * @return
             *     An immutable object of type {@link DoseAndRate}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid DoseAndRate per the base specification
             */
            @Override
            public DoseAndRate build() {
                DoseAndRate doseAndRate = new DoseAndRate(this);
                if (validating) {
                    validate(doseAndRate);
                }
                return doseAndRate;
            }

            protected void validate(DoseAndRate doseAndRate) {
                super.validate(doseAndRate);
                ValidationSupport.choiceElement(doseAndRate.dose, "dose", Range.class, SimpleQuantity.class);
                ValidationSupport.choiceElement(doseAndRate.rate, "rate", Ratio.class, Range.class, SimpleQuantity.class);
                ValidationSupport.requireValueOrChildren(doseAndRate);
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
