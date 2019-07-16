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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.VisionBase;
import com.ibm.watsonhealth.fhir.model.type.VisionEyes;
import com.ibm.watsonhealth.fhir.model.type.VisionStatus;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An authorization for the provision of glasses and/or contact lenses to a patient.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class VisionPrescription extends DomainResource {
    private final List<Identifier> identifier;
    private final VisionStatus status;
    private final DateTime created;
    private final Reference patient;
    private final Reference encounter;
    private final DateTime dateWritten;
    private final Reference prescriber;
    private final List<LensSpecification> lensSpecification;

    private volatile int hashCode;

    private VisionPrescription(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        created = ValidationSupport.requireNonNull(builder.created, "created");
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        encounter = builder.encounter;
        dateWritten = ValidationSupport.requireNonNull(builder.dateWritten, "dateWritten");
        prescriber = ValidationSupport.requireNonNull(builder.prescriber, "prescriber");
        lensSpecification = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.lensSpecification, "lensSpecification"));
    }

    /**
     * <p>
     * A unique identifier assigned to this vision prescription.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The status of the resource instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link VisionStatus}.
     */
    public VisionStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The date this resource was created.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * <p>
     * A resource reference to the person to whom the vision prescription applies.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * <p>
     * A reference to a resource that identifies the particular occurrence of contact between patient and health care 
     * provider during which the prescription was issued.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * <p>
     * The date (and perhaps time) when the prescription was written.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDateWritten() {
        return dateWritten;
    }

    /**
     * <p>
     * The healthcare professional responsible for authorizing the prescription.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPrescriber() {
        return prescriber;
    }

    /**
     * <p>
     * Contain the details of the individual lens specifications and serves as the authorization for the fullfillment by 
     * certified professionals.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link LensSpecification}.
     */
    public List<LensSpecification> getLensSpecification() {
        return lensSpecification;
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
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(status, "status", visitor);
                accept(created, "created", visitor);
                accept(patient, "patient", visitor);
                accept(encounter, "encounter", visitor);
                accept(dateWritten, "dateWritten", visitor);
                accept(prescriber, "prescriber", visitor);
                accept(lensSpecification, "lensSpecification", visitor, LensSpecification.class);
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
        return new Builder(status, created, patient, dateWritten, prescriber, lensSpecification).from(this);
    }

    public Builder toBuilder(VisionStatus status, DateTime created, Reference patient, DateTime dateWritten, Reference prescriber, Collection<LensSpecification> lensSpecification) {
        return new Builder(status, created, patient, dateWritten, prescriber, lensSpecification).from(this);
    }

    public static Builder builder(VisionStatus status, DateTime created, Reference patient, DateTime dateWritten, Reference prescriber, Collection<LensSpecification> lensSpecification) {
        return new Builder(status, created, patient, dateWritten, prescriber, lensSpecification);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final VisionStatus status;
        private final DateTime created;
        private final Reference patient;
        private final DateTime dateWritten;
        private final Reference prescriber;
        private final List<LensSpecification> lensSpecification;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Reference encounter;

        private Builder(VisionStatus status, DateTime created, Reference patient, DateTime dateWritten, Reference prescriber, Collection<LensSpecification> lensSpecification) {
            super();
            this.status = status;
            this.created = created;
            this.patient = patient;
            this.dateWritten = dateWritten;
            this.prescriber = prescriber;
            this.lensSpecification = new ArrayList<>(lensSpecification);
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * A unique identifier assigned to this vision prescription.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A unique identifier assigned to this vision prescription.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business Identifier for vision prescription
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * A reference to a resource that identifies the particular occurrence of contact between patient and health care 
         * provider during which the prescription was issued.
         * </p>
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

        @Override
        public VisionPrescription build() {
            return new VisionPrescription(this);
        }

        private Builder from(VisionPrescription visionPrescription) {
            id = visionPrescription.id;
            meta = visionPrescription.meta;
            implicitRules = visionPrescription.implicitRules;
            language = visionPrescription.language;
            text = visionPrescription.text;
            contained.addAll(visionPrescription.contained);
            extension.addAll(visionPrescription.extension);
            modifierExtension.addAll(visionPrescription.modifierExtension);
            identifier.addAll(visionPrescription.identifier);
            encounter = visionPrescription.encounter;
            return this;
        }
    }

    /**
     * <p>
     * Contain the details of the individual lens specifications and serves as the authorization for the fullfillment by 
     * certified professionals.
     * </p>
     */
    public static class LensSpecification extends BackboneElement {
        private final CodeableConcept product;
        private final VisionEyes eye;
        private final Decimal sphere;
        private final Decimal cylinder;
        private final Integer axis;
        private final List<Prism> prism;
        private final Decimal add;
        private final Decimal power;
        private final Decimal backCurve;
        private final Decimal diameter;
        private final Quantity duration;
        private final String color;
        private final String brand;
        private final List<Annotation> note;

        private volatile int hashCode;

        private LensSpecification(Builder builder) {
            super(builder);
            product = ValidationSupport.requireNonNull(builder.product, "product");
            eye = ValidationSupport.requireNonNull(builder.eye, "eye");
            sphere = builder.sphere;
            cylinder = builder.cylinder;
            axis = builder.axis;
            prism = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.prism, "prism"));
            add = builder.add;
            power = builder.power;
            backCurve = builder.backCurve;
            diameter = builder.diameter;
            duration = builder.duration;
            color = builder.color;
            brand = builder.brand;
            note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Identifies the type of vision correction product which is required for the patient.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getProduct() {
            return product;
        }

        /**
         * <p>
         * The eye for which the lens specification applies.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link VisionEyes}.
         */
        public VisionEyes getEye() {
            return eye;
        }

        /**
         * <p>
         * Lens power measured in dioptres (0.25 units).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getSphere() {
            return sphere;
        }

        /**
         * <p>
         * Power adjustment for astigmatism measured in dioptres (0.25 units).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getCylinder() {
            return cylinder;
        }

        /**
         * <p>
         * Adjustment for astigmatism measured in integer degrees.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getAxis() {
            return axis;
        }

        /**
         * <p>
         * Allows for adjustment on two axis.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Prism}.
         */
        public List<Prism> getPrism() {
            return prism;
        }

        /**
         * <p>
         * Power adjustment for multifocal lenses measured in dioptres (0.25 units).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getAdd() {
            return add;
        }

        /**
         * <p>
         * Contact lens power measured in dioptres (0.25 units).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getPower() {
            return power;
        }

        /**
         * <p>
         * Back curvature measured in millimetres.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getBackCurve() {
            return backCurve;
        }

        /**
         * <p>
         * Contact lens diameter measured in millimetres.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getDiameter() {
            return diameter;
        }

        /**
         * <p>
         * The recommended maximum wear period for the lens.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getDuration() {
            return duration;
        }

        /**
         * <p>
         * Special color or pattern.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getColor() {
            return color;
        }

        /**
         * <p>
         * Brand recommendations or restrictions.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getBrand() {
            return brand;
        }

        /**
         * <p>
         * Notes for special requirements such as coatings and lens materials.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            return new Builder(product, eye).from(this);
        }

        public Builder toBuilder(CodeableConcept product, VisionEyes eye) {
            return new Builder(product, eye).from(this);
        }

        public static Builder builder(CodeableConcept product, VisionEyes eye) {
            return new Builder(product, eye);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept product;
            private final VisionEyes eye;

            // optional
            private Decimal sphere;
            private Decimal cylinder;
            private Integer axis;
            private List<Prism> prism = new ArrayList<>();
            private Decimal add;
            private Decimal power;
            private Decimal backCurve;
            private Decimal diameter;
            private Quantity duration;
            private String color;
            private String brand;
            private List<Annotation> note = new ArrayList<>();

            private Builder(CodeableConcept product, VisionEyes eye) {
                super();
                this.product = product;
                this.eye = eye;
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
             * Lens power measured in dioptres (0.25 units).
             * </p>
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
             * <p>
             * Power adjustment for astigmatism measured in dioptres (0.25 units).
             * </p>
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
             * <p>
             * Adjustment for astigmatism measured in integer degrees.
             * </p>
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
             * <p>
             * Allows for adjustment on two axis.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Allows for adjustment on two axis.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param prism
             *     Eye alignment compensation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder prism(Collection<Prism> prism) {
                this.prism = new ArrayList<>(prism);
                return this;
            }

            /**
             * <p>
             * Power adjustment for multifocal lenses measured in dioptres (0.25 units).
             * </p>
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
             * <p>
             * Contact lens power measured in dioptres (0.25 units).
             * </p>
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
             * <p>
             * Back curvature measured in millimetres.
             * </p>
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
             * <p>
             * Contact lens diameter measured in millimetres.
             * </p>
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
             * <p>
             * The recommended maximum wear period for the lens.
             * </p>
             * 
             * @param duration
             *     Lens wear duration
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder duration(Quantity duration) {
                this.duration = duration;
                return this;
            }

            /**
             * <p>
             * Special color or pattern.
             * </p>
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
             * <p>
             * Brand recommendations or restrictions.
             * </p>
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
             * <p>
             * Notes for special requirements such as coatings and lens materials.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Notes for special requirements such as coatings and lens materials.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param note
             *     Notes for coatings
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder note(Collection<Annotation> note) {
                this.note = new ArrayList<>(note);
                return this;
            }

            @Override
            public LensSpecification build() {
                return new LensSpecification(this);
            }

            private Builder from(LensSpecification lensSpecification) {
                id = lensSpecification.id;
                extension.addAll(lensSpecification.extension);
                modifierExtension.addAll(lensSpecification.modifierExtension);
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
         * <p>
         * Allows for adjustment on two axis.
         * </p>
         */
        public static class Prism extends BackboneElement {
            private final Decimal amount;
            private final VisionBase base;

            private volatile int hashCode;

            private Prism(Builder builder) {
                super(builder);
                amount = ValidationSupport.requireNonNull(builder.amount, "amount");
                base = ValidationSupport.requireNonNull(builder.base, "base");
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Amount of prism to compensate for eye alignment in fractional units.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getAmount() {
                return amount;
            }

            /**
             * <p>
             * The relative base, or reference lens edge, for the prism.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link VisionBase}.
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
            public void accept(java.lang.String elementName, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, this);
                    if (visitor.visit(elementName, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(amount, "amount", visitor);
                        accept(base, "base", visitor);
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
                return new Builder(amount, base).from(this);
            }

            public Builder toBuilder(Decimal amount, VisionBase base) {
                return new Builder(amount, base).from(this);
            }

            public static Builder builder(Decimal amount, VisionBase base) {
                return new Builder(amount, base);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Decimal amount;
                private final VisionBase base;

                private Builder(Decimal amount, VisionBase base) {
                    super();
                    this.amount = amount;
                    this.base = base;
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

                @Override
                public Prism build() {
                    return new Prism(this);
                }

                private Builder from(Prism prism) {
                    id = prism.id;
                    extension.addAll(prism.extension);
                    modifierExtension.addAll(prism.modifierExtension);
                    return this;
                }
            }
        }
    }
}
