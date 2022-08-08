/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Choice;
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.Annotation;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Period;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Timing;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.DeviceUseStatementStatus;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A record of a device being used by a patient where the record is the result of a report from the patient or another 
 * clinician.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class DeviceUseStatement extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "ServiceRequest" })
    private final List<Reference> basedOn;
    @Summary
    @Binding(
        bindingName = "DeviceUseStatementStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A coded concept indicating the current status of the Device Usage.",
        valueSet = "http://hl7.org/fhir/ValueSet/device-statement-status|4.3.0"
    )
    @Required
    private final DeviceUseStatementStatus status;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    @Required
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "ServiceRequest", "Procedure", "Claim", "Observation", "QuestionnaireResponse", "DocumentReference" })
    private final List<Reference> derivedFrom;
    @Summary
    @Choice({ Timing.class, Period.class, DateTime.class })
    private final Element timing;
    @Summary
    private final DateTime recordedOn;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
    private final Reference source;
    @Summary
    @ReferenceTarget({ "Device" })
    @Required
    private final Reference device;
    @Summary
    private final List<CodeableConcept> reasonCode;
    @Summary
    @ReferenceTarget({ "Condition", "Observation", "DiagnosticReport", "DocumentReference", "Media" })
    private final List<Reference> reasonReference;
    @Summary
    @Binding(
        bindingName = "BodySite",
        strength = BindingStrength.Value.EXAMPLE,
        description = "SNOMED CT Body site concepts",
        valueSet = "http://hl7.org/fhir/ValueSet/body-site"
    )
    private final CodeableConcept bodySite;
    private final List<Annotation> note;

    private DeviceUseStatement(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        status = builder.status;
        subject = builder.subject;
        derivedFrom = Collections.unmodifiableList(builder.derivedFrom);
        timing = builder.timing;
        recordedOn = builder.recordedOn;
        source = builder.source;
        device = builder.device;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        bodySite = builder.bodySite;
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * An external identifier for this statement such as an IRI.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * A code representing the patient or other source's judgment about the state of the device used that this statement is 
     * about. Generally this will be active or completed.
     * 
     * @return
     *     An immutable object of type {@link DeviceUseStatementStatus} that is non-null.
     */
    public DeviceUseStatementStatus getStatus() {
        return status;
    }

    /**
     * The patient who used the device.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
     * derive the DeviceUseStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * How often the device was used.
     * 
     * @return
     *     An immutable object of type {@link Timing}, {@link Period} or {@link DateTime} that may be null.
     */
    public Element getTiming() {
        return timing;
    }

    /**
     * The time at which the statement was made/recorded.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getRecordedOn() {
        return recordedOn;
    }

    /**
     * Who reported the device was being used by the patient.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * The details of the device used.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * Reason or justification for the use of the device.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Indicates another resource whose existence justifies this DeviceUseStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * Indicates the anotomic location on the subject's body where the device was used ( i.e. the target).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getBodySite() {
        return bodySite;
    }

    /**
     * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
     * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
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
            !identifier.isEmpty() || 
            !basedOn.isEmpty() || 
            (status != null) || 
            (subject != null) || 
            !derivedFrom.isEmpty() || 
            (timing != null) || 
            (recordedOn != null) || 
            (source != null) || 
            (device != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            (bodySite != null) || 
            !note.isEmpty();
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
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(subject, "subject", visitor);
                accept(derivedFrom, "derivedFrom", visitor, Reference.class);
                accept(timing, "timing", visitor);
                accept(recordedOn, "recordedOn", visitor);
                accept(source, "source", visitor);
                accept(device, "device", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(bodySite, "bodySite", visitor);
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
        DeviceUseStatement other = (DeviceUseStatement) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(status, other.status) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(derivedFrom, other.derivedFrom) && 
            Objects.equals(timing, other.timing) && 
            Objects.equals(recordedOn, other.recordedOn) && 
            Objects.equals(source, other.source) && 
            Objects.equals(device, other.device) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(bodySite, other.bodySite) && 
            Objects.equals(note, other.note);
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
                basedOn, 
                status, 
                subject, 
                derivedFrom, 
                timing, 
                recordedOn, 
                source, 
                device, 
                reasonCode, 
                reasonReference, 
                bodySite, 
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

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private DeviceUseStatementStatus status;
        private Reference subject;
        private List<Reference> derivedFrom = new ArrayList<>();
        private Element timing;
        private DateTime recordedOn;
        private Reference source;
        private Reference device;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private CodeableConcept bodySite;
        private List<Annotation> note = new ArrayList<>();

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
         * An external identifier for this statement such as an IRI.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External identifier for this record
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
         * An external identifier for this statement such as an IRI.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External identifier for this record
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
         * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * A code representing the patient or other source's judgment about the state of the device used that this statement is 
         * about. Generally this will be active or completed.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | completed | entered-in-error +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(DeviceUseStatementStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The patient who used the device.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     Patient using device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
         * derive the DeviceUseStatement.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Claim}</li>
         * <li>{@link Observation}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param derivedFrom
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Reference... derivedFrom) {
            for (Reference value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
         * derive the DeviceUseStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Claim}</li>
         * <li>{@link Observation}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param derivedFrom
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder derivedFrom(Collection<Reference> derivedFrom) {
            this.derivedFrom = new ArrayList<>(derivedFrom);
            return this;
        }

        /**
         * How often the device was used.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Timing}</li>
         * <li>{@link Period}</li>
         * <li>{@link DateTime}</li>
         * </ul>
         * 
         * @param timing
         *     How often the device was used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder timing(Element timing) {
            this.timing = timing;
            return this;
        }

        /**
         * The time at which the statement was made/recorded.
         * 
         * @param recordedOn
         *     When statement was recorded
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recordedOn(DateTime recordedOn) {
            this.recordedOn = recordedOn;
            return this;
        }

        /**
         * Who reported the device was being used by the patient.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param source
         *     Who made the statement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Reference source) {
            this.source = source;
            return this;
        }

        /**
         * The details of the device used.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param device
         *     Reference to device used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Reference device) {
            this.device = device;
            return this;
        }

        /**
         * Reason or justification for the use of the device.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why device was used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * Reason or justification for the use of the device.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why device was used
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * Indicates another resource whose existence justifies this DeviceUseStatement.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * <li>{@link Media}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why was DeviceUseStatement performed?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * Indicates another resource whose existence justifies this DeviceUseStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * <li>{@link Media}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why was DeviceUseStatement performed?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * Indicates the anotomic location on the subject's body where the device was used ( i.e. the target).
         * 
         * @param bodySite
         *     Target body site
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder bodySite(CodeableConcept bodySite) {
            this.bodySite = bodySite;
            return this;
        }

        /**
         * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
         * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Addition details (comments, instructions)
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
         * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
         * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Addition details (comments, instructions)
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
         * Build the {@link DeviceUseStatement}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>subject</li>
         * <li>device</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DeviceUseStatement}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DeviceUseStatement per the base specification
         */
        @Override
        public DeviceUseStatement build() {
            DeviceUseStatement deviceUseStatement = new DeviceUseStatement(this);
            if (validating) {
                validate(deviceUseStatement);
            }
            return deviceUseStatement;
        }

        protected void validate(DeviceUseStatement deviceUseStatement) {
            super.validate(deviceUseStatement);
            ValidationSupport.checkList(deviceUseStatement.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(deviceUseStatement.basedOn, "basedOn", Reference.class);
            ValidationSupport.requireNonNull(deviceUseStatement.status, "status");
            ValidationSupport.requireNonNull(deviceUseStatement.subject, "subject");
            ValidationSupport.checkList(deviceUseStatement.derivedFrom, "derivedFrom", Reference.class);
            ValidationSupport.choiceElement(deviceUseStatement.timing, "timing", Timing.class, Period.class, DateTime.class);
            ValidationSupport.requireNonNull(deviceUseStatement.device, "device");
            ValidationSupport.checkList(deviceUseStatement.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(deviceUseStatement.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(deviceUseStatement.note, "note", Annotation.class);
            ValidationSupport.checkReferenceType(deviceUseStatement.basedOn, "basedOn", "ServiceRequest");
            ValidationSupport.checkReferenceType(deviceUseStatement.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(deviceUseStatement.derivedFrom, "derivedFrom", "ServiceRequest", "Procedure", "Claim", "Observation", "QuestionnaireResponse", "DocumentReference");
            ValidationSupport.checkReferenceType(deviceUseStatement.source, "source", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
            ValidationSupport.checkReferenceType(deviceUseStatement.device, "device", "Device");
            ValidationSupport.checkReferenceType(deviceUseStatement.reasonReference, "reasonReference", "Condition", "Observation", "DiagnosticReport", "DocumentReference", "Media");
        }

        protected Builder from(DeviceUseStatement deviceUseStatement) {
            super.from(deviceUseStatement);
            identifier.addAll(deviceUseStatement.identifier);
            basedOn.addAll(deviceUseStatement.basedOn);
            status = deviceUseStatement.status;
            subject = deviceUseStatement.subject;
            derivedFrom.addAll(deviceUseStatement.derivedFrom);
            timing = deviceUseStatement.timing;
            recordedOn = deviceUseStatement.recordedOn;
            source = deviceUseStatement.source;
            device = deviceUseStatement.device;
            reasonCode.addAll(deviceUseStatement.reasonCode);
            reasonReference.addAll(deviceUseStatement.reasonReference);
            bodySite = deviceUseStatement.bodySite;
            note.addAll(deviceUseStatement.note);
            return this;
        }
    }
}
