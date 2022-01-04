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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.VisionBase;
import com.ibm.fhir.model.type.code.VisionEyes;
import com.ibm.fhir.model.type.code.VisionStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An authorization for the provision of glasses and/or contact lenses to a patient.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class VisionPrescription extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "VisionStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/fm-status|4.3.0-CIBUILD"
    )
    @Required
    private final VisionStatus status;
    @Summary
    @Required
    private final DateTime created;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    @Required
    private final DateTime dateWritten;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    @Required
    private final Reference prescriber;
    @Summary
    @Required
    private final List<LensSpecification> lensSpecification;

    private VisionPrescription(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        created = builder.created;
        patient = builder.patient;
        encounter = builder.encounter;
        dateWritten = builder.dateWritten;
        prescriber = builder.prescriber;
        lensSpecification = Collections.unmodifiableList(builder.lensSpecification);
    }

    /**
     * A unique identifier assigned to this vision prescription.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The status of the resource instance.
     * 
     * @return
     *     An immutable object of type {@link VisionStatus} that is non-null.
     */
    public VisionStatus getStatus() {
        return status;
    }

    /**
     * The date this resource was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * A resource reference to the person to whom the vision prescription applies.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * A reference to a resource that identifies the particular occurrence of contact between patient and health care 
     * provider during which the prescription was issued.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The date (and perhaps time) when the prescription was written.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getDateWritten() {
        return dateWritten;
    }

    /**
     * The healthcare professional responsible for authorizing the prescription.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPrescriber() {
        return prescriber;
    }

    /**
     * Contain the details of the individual lens specifications and serves as the authorization for the fullfillment by 
     * certified professionals.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link LensSpecification} that is non-empty.
     */
    public List<LensSpecification> getLensSpecification() {
        return lensSpecification;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (created != null) || 
            (patient != null) || 
            (encounter != null) || 
            (dateWritten != null) || 
            (prescriber != null) || 
            !lensSpecification.isEmpty();
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
                accept(status, "status", visitor);
                accept(created, "created", visitor);
                accept(patient, "patient", visitor);
                accept(encounter, "encounter", visitor);
                accept(dateWritten, "dateWritten", visitor);
                accept(prescriber, "prescriber", visitor);
                accept(lensSpecification, "lensSpecification", visitor, LensSpecification.class);
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
        VisionPrescription other = (VisionPrescription) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(created, other.created) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(dateWritten, other.dateWritten) && 
            Objects.equals(prescriber, other.prescriber) && 
            Objects.equals(lensSpecification, other.lensSpecification);
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
                status, 
                created, 
                patient, 
                encounter, 
                dateWritten, 
                prescriber, 
                lensSpecification);
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
        private VisionStatus status;
        private DateTime created;
        private Reference patient;
        private Reference encounter;
        private DateTime dateWritten;
        private Reference prescriber;
        private List<LensSpecification> lensSpecification = new ArrayList<>();

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
         * A unique identifier assigned to this vision prescription.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for vision prescription
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
         * A unique identifier assigned to this vision prescription.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for vision prescription
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
         * The status of the resource instance.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | cancelled | draft | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(VisionStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The date this resource was created.
         * 
         * <p>This element is required.
         * 
         * @param created
         *     Response creation date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * A resource reference to the person to whom the vision prescription applies.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Who prescription is for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * A reference to a resource that identifies the particular occurrence of contact between patient and health care 
         * provider during which the prescription was issued.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Created during encounter / admission / stay
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * The date (and perhaps time) when the prescription was written.
         * 
         * <p>This element is required.
         * 
         * @param dateWritten
         *     When prescription was authorized
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dateWritten(DateTime dateWritten) {
            this.dateWritten = dateWritten;
            return this;
        }

        /**
         * The healthcare professional responsible for authorizing the prescription.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param prescriber
         *     Who authorized the vision prescription
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder prescriber(Reference prescriber) {
            this.prescriber = prescriber;
            return this;
        }

        /**
         * Contain the details of the individual lens specifications and serves as the authorization for the fullfillment by 
         * certified professionals.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param lensSpecification
         *     Vision lens authorization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lensSpecification(LensSpecification... lensSpecification) {
            for (LensSpecification value : lensSpecification) {
                this.lensSpecification.add(value);
            }
            return this;
        }

        /**
         * Contain the details of the individual lens specifications and serves as the authorization for the fullfillment by 
         * certified professionals.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param lensSpecification
         *     Vision lens authorization
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder lensSpecification(Collection<LensSpecification> lensSpecification) {
            this.lensSpecification = new ArrayList<>(lensSpecification);
            return this;
        }

        /**
         * Build the {@link VisionPrescription}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>created</li>
         * <li>patient</li>
         * <li>dateWritten</li>
         * <li>prescriber</li>
         * <li>lensSpecification</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link VisionPrescription}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid VisionPrescription per the base specification
         */
        @Override
        public VisionPrescription build() {
            VisionPrescription visionPrescription = new VisionPrescription(this);
            if (validating) {
                validate(visionPrescription);
            }
            return visionPrescription;
        }

        protected void validate(VisionPrescription visionPrescription) {
            super.validate(visionPrescription);
            ValidationSupport.checkList(visionPrescription.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(visionPrescription.status, "status");
            ValidationSupport.requireNonNull(visionPrescription.created, "created");
            ValidationSupport.requireNonNull(visionPrescription.patient, "patient");
            ValidationSupport.requireNonNull(visionPrescription.dateWritten, "dateWritten");
            ValidationSupport.requireNonNull(visionPrescription.prescriber, "prescriber");
            ValidationSupport.checkNonEmptyList(visionPrescription.lensSpecification, "lensSpecification", LensSpecification.class);
            ValidationSupport.checkReferenceType(visionPrescription.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(visionPrescription.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(visionPrescription.prescriber, "prescriber", "Practitioner", "PractitionerRole");
        }

        protected Builder from(VisionPrescription visionPrescription) {
            super.from(visionPrescription);
            identifier.addAll(visionPrescription.identifier);
            status = visionPrescription.status;
            created = visionPrescription.created;
            patient = visionPrescription.patient;
            encounter = visionPrescription.encounter;
            dateWritten = visionPrescription.dateWritten;
            prescriber = visionPrescription.prescriber;
            lensSpecification.addAll(visionPrescription.lensSpecification);
            return this;
        }
    }

    /**
     * Contain the details of the individual lens specifications and serves as the authorization for the fullfillment by 
     * certified professionals.
     */
    public static class LensSpecification extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "VisionProduct",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept describing the vision products.",
            valueSet = "http://hl7.org/fhir/ValueSet/vision-product"
        )
        @Required
        private final CodeableConcept product;
        @Summary
        @Binding(
            bindingName = "VisionEyes",
            strength = BindingStrength.Value.REQUIRED,
            description = "A coded concept listing the eye codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/vision-eye-codes|4.3.0-CIBUILD"
        )
        @Required
        private final VisionEyes eye;
        private final Decimal sphere;
        private final Decimal cylinder;
        private final Integer axis;
        private final List<Prism> prism;
        private final Decimal add;
        private final Decimal power;
        private final Decimal backCurve;
        private final Decimal diameter;
        private final SimpleQuantity duration;
        private final String color;
        private final String brand;
        private final List<Annotation> note;

        private LensSpecification(Builder builder) {
            super(builder);
            product = builder.product;
            eye = builder.eye;
            sphere = builder.sphere;
            cylinder = builder.cylinder;
            axis = builder.axis;
            prism = Collections.unmodifiableList(builder.prism);
            add = builder.add;
            power = builder.power;
            backCurve = builder.backCurve;
            diameter = builder.diameter;
            duration = builder.duration;
            color = builder.color;
            brand = builder.brand;
            note = Collections.unmodifiableList(builder.note);
        }

        /**
         * Identifies the type of vision correction product which is required for the patient.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getProduct() {
            return product;
        }

        /**
         * The eye for which the lens specification applies.
         * 
         * @return
         *     An immutable object of type {@link VisionEyes} that is non-null.
         */
        public VisionEyes getEye() {
            return eye;
        }

        /**
         * Lens power measured in dioptres (0.25 units).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getSphere() {
            return sphere;
        }

        /**
         * Power adjustment for astigmatism measured in dioptres (0.25 units).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getCylinder() {
            return cylinder;
        }

        /**
         * Adjustment for astigmatism measured in integer degrees.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getAxis() {
            return axis;
        }

        /**
         * Allows for adjustment on two axis.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Prism} that may be empty.
         */
        public List<Prism> getPrism() {
            return prism;
        }

        /**
         * Power adjustment for multifocal lenses measured in dioptres (0.25 units).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getAdd() {
            return add;
        }

        /**
         * Contact lens power measured in dioptres (0.25 units).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getPower() {
            return power;
        }

        /**
         * Back curvature measured in millimetres.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getBackCurve() {
            return backCurve;
        }

        /**
         * Contact lens diameter measured in millimetres.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getDiameter() {
            return diameter;
        }

        /**
         * The recommended maximum wear period for the lens.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getDuration() {
            return duration;
        }

        /**
         * Special color or pattern.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getColor() {
            return color;
        }

        /**
         * Brand recommendations or restrictions.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getBrand() {
            return brand;
        }

        /**
         * Notes for special requirements such as coatings and lens materials.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
         */
        public List<Annotation> getNote() {
            return note;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (product != null) || 
                (eye != null) || 
                (sphere != null) || 
                (cylinder != null) || 
                (axis != null) || 
                !prism.isEmpty() || 
                (add != null) || 
                (power != null) || 
                (backCurve != null) || 
                (diameter != null) || 
                (duration != null) || 
                (color != null) || 
                (brand != null) || 
                !note.isEmpty();
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
                    accept(product, "product", visitor);
                    accept(eye, "eye", visitor);
                    accept(sphere, "sphere", visitor);
                    accept(cylinder, "cylinder", visitor);
                    accept(axis, "axis", visitor);
                    accept(prism, "prism", visitor, Prism.class);
                    accept(add, "add", visitor);
                    accept(power, "power", visitor);
                    accept(backCurve, "backCurve", visitor);
                    accept(diameter, "diameter", visitor);
                    accept(duration, "duration", visitor);
                    accept(color, "color", visitor);
                    accept(brand, "brand", visitor);
                    accept(note, "note", visitor, Annotation.class);
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
            LensSpecification other = (LensSpecification) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(product, other.product) && 
                Objects.equals(eye, other.eye) && 
                Objects.equals(sphere, other.sphere) && 
                Objects.equals(cylinder, other.cylinder) && 
                Objects.equals(axis, other.axis) && 
                Objects.equals(prism, other.prism) && 
                Objects.equals(add, other.add) && 
                Objects.equals(power, other.power) && 
                Objects.equals(backCurve, other.backCurve) && 
                Objects.equals(diameter, other.diameter) && 
                Objects.equals(duration, other.duration) && 
                Objects.equals(color, other.color) && 
                Objects.equals(brand, other.brand) && 
                Objects.equals(note, other.note);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    product, 
                    eye, 
                    sphere, 
                    cylinder, 
                    axis, 
                    prism, 
                    add, 
                    power, 
                    backCurve, 
                    diameter, 
                    duration, 
                    color, 
                    brand, 
                    note);
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
            private CodeableConcept product;
            private VisionEyes eye;
            private Decimal sphere;
            private Decimal cylinder;
            private Integer axis;
            private List<Prism> prism = new ArrayList<>();
            private Decimal add;
            private Decimal power;
            private Decimal backCurve;
            private Decimal diameter;
            private SimpleQuantity duration;
            private String color;
            private String brand;
            private List<Annotation> note = new ArrayList<>();

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
             * Identifies the type of vision correction product which is required for the patient.
             * 
             * <p>This element is required.
             * 
             * @param product
             *     Product to be supplied
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder product(CodeableConcept product) {
                this.product = product;
                return this;
            }

            /**
             * The eye for which the lens specification applies.
             * 
             * <p>This element is required.
             * 
             * @param eye
             *     right | left
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder eye(VisionEyes eye) {
                this.eye = eye;
                return this;
            }

            /**
             * Lens power measured in dioptres (0.25 units).
             * 
             * @param sphere
             *     Power of the lens
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sphere(Decimal sphere) {
                this.sphere = sphere;
                return this;
            }

            /**
             * Power adjustment for astigmatism measured in dioptres (0.25 units).
             * 
             * @param cylinder
             *     Lens power for astigmatism
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder cylinder(Decimal cylinder) {
                this.cylinder = cylinder;
                return this;
            }

            /**
             * Convenience method for setting {@code axis}.
             * 
             * @param axis
             *     Lens meridian which contain no power for astigmatism
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #axis(com.ibm.fhir.model.type.Integer)
             */
            public Builder axis(java.lang.Integer axis) {
                this.axis = (axis == null) ? null : Integer.of(axis);
                return this;
            }

            /**
             * Adjustment for astigmatism measured in integer degrees.
             * 
             * @param axis
             *     Lens meridian which contain no power for astigmatism
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder axis(Integer axis) {
                this.axis = axis;
                return this;
            }

            /**
             * Allows for adjustment on two axis.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param prism
             *     Eye alignment compensation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder prism(Prism... prism) {
                for (Prism value : prism) {
                    this.prism.add(value);
                }
                return this;
            }

            /**
             * Allows for adjustment on two axis.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param prism
             *     Eye alignment compensation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder prism(Collection<Prism> prism) {
                this.prism = new ArrayList<>(prism);
                return this;
            }

            /**
             * Power adjustment for multifocal lenses measured in dioptres (0.25 units).
             * 
             * @param add
             *     Added power for multifocal levels
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder add(Decimal add) {
                this.add = add;
                return this;
            }

            /**
             * Contact lens power measured in dioptres (0.25 units).
             * 
             * @param power
             *     Contact lens power
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder power(Decimal power) {
                this.power = power;
                return this;
            }

            /**
             * Back curvature measured in millimetres.
             * 
             * @param backCurve
             *     Contact lens back curvature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder backCurve(Decimal backCurve) {
                this.backCurve = backCurve;
                return this;
            }

            /**
             * Contact lens diameter measured in millimetres.
             * 
             * @param diameter
             *     Contact lens diameter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diameter(Decimal diameter) {
                this.diameter = diameter;
                return this;
            }

            /**
             * The recommended maximum wear period for the lens.
             * 
             * @param duration
             *     Lens wear duration
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder duration(SimpleQuantity duration) {
                this.duration = duration;
                return this;
            }

            /**
             * Convenience method for setting {@code color}.
             * 
             * @param color
             *     Color required
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #color(com.ibm.fhir.model.type.String)
             */
            public Builder color(java.lang.String color) {
                this.color = (color == null) ? null : String.of(color);
                return this;
            }

            /**
             * Special color or pattern.
             * 
             * @param color
             *     Color required
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder color(String color) {
                this.color = color;
                return this;
            }

            /**
             * Convenience method for setting {@code brand}.
             * 
             * @param brand
             *     Brand required
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #brand(com.ibm.fhir.model.type.String)
             */
            public Builder brand(java.lang.String brand) {
                this.brand = (brand == null) ? null : String.of(brand);
                return this;
            }

            /**
             * Brand recommendations or restrictions.
             * 
             * @param brand
             *     Brand required
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder brand(String brand) {
                this.brand = brand;
                return this;
            }

            /**
             * Notes for special requirements such as coatings and lens materials.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Notes for coatings
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
             * Notes for special requirements such as coatings and lens materials.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Notes for coatings
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
             * Build the {@link LensSpecification}
             * 
             * <p>Required elements:
             * <ul>
             * <li>product</li>
             * <li>eye</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link LensSpecification}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid LensSpecification per the base specification
             */
            @Override
            public LensSpecification build() {
                LensSpecification lensSpecification = new LensSpecification(this);
                if (validating) {
                    validate(lensSpecification);
                }
                return lensSpecification;
            }

            protected void validate(LensSpecification lensSpecification) {
                super.validate(lensSpecification);
                ValidationSupport.requireNonNull(lensSpecification.product, "product");
                ValidationSupport.requireNonNull(lensSpecification.eye, "eye");
                ValidationSupport.checkList(lensSpecification.prism, "prism", Prism.class);
                ValidationSupport.checkList(lensSpecification.note, "note", Annotation.class);
                ValidationSupport.requireValueOrChildren(lensSpecification);
            }

            protected Builder from(LensSpecification lensSpecification) {
                super.from(lensSpecification);
                product = lensSpecification.product;
                eye = lensSpecification.eye;
                sphere = lensSpecification.sphere;
                cylinder = lensSpecification.cylinder;
                axis = lensSpecification.axis;
                prism.addAll(lensSpecification.prism);
                add = lensSpecification.add;
                power = lensSpecification.power;
                backCurve = lensSpecification.backCurve;
                diameter = lensSpecification.diameter;
                duration = lensSpecification.duration;
                color = lensSpecification.color;
                brand = lensSpecification.brand;
                note.addAll(lensSpecification.note);
                return this;
            }
        }

        /**
         * Allows for adjustment on two axis.
         */
        public static class Prism extends BackboneElement {
            @Required
            private final Decimal amount;
            @Binding(
                bindingName = "VisionBase",
                strength = BindingStrength.Value.REQUIRED,
                description = "A coded concept listing the base codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/vision-base-codes|4.3.0-CIBUILD"
            )
            @Required
            private final VisionBase base;

            private Prism(Builder builder) {
                super(builder);
                amount = builder.amount;
                base = builder.base;
            }

            /**
             * Amount of prism to compensate for eye alignment in fractional units.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that is non-null.
             */
            public Decimal getAmount() {
                return amount;
            }

            /**
             * The relative base, or reference lens edge, for the prism.
             * 
             * @return
             *     An immutable object of type {@link VisionBase} that is non-null.
             */
            public VisionBase getBase() {
                return base;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (amount != null) || 
                    (base != null);
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
                        accept(amount, "amount", visitor);
                        accept(base, "base", visitor);
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
                Prism other = (Prism) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(amount, other.amount) && 
                    Objects.equals(base, other.base);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        amount, 
                        base);
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
                private Decimal amount;
                private VisionBase base;

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
                 * Amount of prism to compensate for eye alignment in fractional units.
                 * 
                 * <p>This element is required.
                 * 
                 * @param amount
                 *     Amount of adjustment
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(Decimal amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * The relative base, or reference lens edge, for the prism.
                 * 
                 * <p>This element is required.
                 * 
                 * @param base
                 *     up | down | in | out
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder base(VisionBase base) {
                    this.base = base;
                    return this;
                }

                /**
                 * Build the {@link Prism}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>amount</li>
                 * <li>base</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Prism}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Prism per the base specification
                 */
                @Override
                public Prism build() {
                    Prism prism = new Prism(this);
                    if (validating) {
                        validate(prism);
                    }
                    return prism;
                }

                protected void validate(Prism prism) {
                    super.validate(prism);
                    ValidationSupport.requireNonNull(prism.amount, "amount");
                    ValidationSupport.requireNonNull(prism.base, "base");
                    ValidationSupport.requireValueOrChildren(prism);
                }

                protected Builder from(Prism prism) {
                    super.from(prism);
                    amount = prism.amount;
                    base = prism.base;
                    return this;
                }
            }
        }
    }
}
