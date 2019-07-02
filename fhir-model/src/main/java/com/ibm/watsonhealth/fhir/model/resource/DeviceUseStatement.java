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
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DeviceUseStatementStatus;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A record of a device being used by a patient where the record is the result of a report from the patient or another 
 * clinician.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DeviceUseStatement extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> basedOn;
    private final DeviceUseStatementStatus status;
    private final Reference subject;
    private final List<Reference> derivedFrom;
    private final Element timing;
    private final DateTime recordedOn;
    private final Reference source;
    private final Reference device;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final CodeableConcept bodySite;
    private final List<Annotation> note;

    private volatile int hashCode;

    private DeviceUseStatement(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        derivedFrom = Collections.unmodifiableList(builder.derivedFrom);
        timing = ValidationSupport.choiceElement(builder.timing, "timing", Timing.class, Period.class, DateTime.class);
        recordedOn = builder.recordedOn;
        source = builder.source;
        device = ValidationSupport.requireNonNull(builder.device, "device");
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        bodySite = builder.bodySite;
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * <p>
     * An external identifier for this statement such as an IRI.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * A code representing the patient or other source's judgment about the state of the device used that this statement is 
     * about. Generally this will be active or completed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DeviceUseStatementStatus}.
     */
    public DeviceUseStatementStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The patient who used the device.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * <p>
     * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
     * derive the DeviceUseStatement.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * <p>
     * How often the device was used.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getTiming() {
        return timing;
    }

    /**
     * <p>
     * The time at which the statement was made/recorded.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getRecordedOn() {
        return recordedOn;
    }

    /**
     * <p>
     * Who reported the device was being used by the patient.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * <p>
     * The details of the device used.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * <p>
     * Reason or justification for the use of the device.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * Indicates another resource whose existence justifies this DeviceUseStatement.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * Indicates the anotomic location on the subject's body where the device was used ( i.e. the target).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getBodySite() {
        return bodySite;
    }

    /**
     * <p>
     * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
     * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
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
        return new Builder(status, subject, device).from(this);
    }

    public Builder toBuilder(DeviceUseStatementStatus status, Reference subject, Reference device) {
        return new Builder(status, subject, device).from(this);
    }

    public static Builder builder(DeviceUseStatementStatus status, Reference subject, Reference device) {
        return new Builder(status, subject, device);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final DeviceUseStatementStatus status;
        private final Reference subject;
        private final Reference device;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> derivedFrom = new ArrayList<>();
        private Element timing;
        private DateTime recordedOn;
        private Reference source;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private CodeableConcept bodySite;
        private List<Annotation> note = new ArrayList<>();

        private Builder(DeviceUseStatementStatus status, Reference subject, Reference device) {
            super();
            this.status = status;
            this.subject = subject;
            this.device = device;
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
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
         * An external identifier for this statement such as an IRI.
         * </p>
         * 
         * @param identifier
         *     External identifier for this record
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An external identifier for this statement such as an IRI.
         * </p>
         * 
         * @param identifier
         *     External identifier for this record
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
         * </p>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
         * </p>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn.addAll(basedOn);
            return this;
        }

        /**
         * <p>
         * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
         * derive the DeviceUseStatement.
         * </p>
         * 
         * @param derivedFrom
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFrom(Reference... derivedFrom) {
            for (Reference value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
         * derive the DeviceUseStatement.
         * </p>
         * 
         * @param derivedFrom
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFrom(Collection<Reference> derivedFrom) {
            this.derivedFrom.addAll(derivedFrom);
            return this;
        }

        /**
         * <p>
         * How often the device was used.
         * </p>
         * 
         * @param timing
         *     How often the device was used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder timing(Element timing) {
            this.timing = timing;
            return this;
        }

        /**
         * <p>
         * The time at which the statement was made/recorded.
         * </p>
         * 
         * @param recordedOn
         *     When statement was recorded
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder recordedOn(DateTime recordedOn) {
            this.recordedOn = recordedOn;
            return this;
        }

        /**
         * <p>
         * Who reported the device was being used by the patient.
         * </p>
         * 
         * @param source
         *     Who made the statement
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder source(Reference source) {
            this.source = source;
            return this;
        }

        /**
         * <p>
         * Reason or justification for the use of the device.
         * </p>
         * 
         * @param reasonCode
         *     Why device was used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reason or justification for the use of the device.
         * </p>
         * 
         * @param reasonCode
         *     Why device was used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode.addAll(reasonCode);
            return this;
        }

        /**
         * <p>
         * Indicates another resource whose existence justifies this DeviceUseStatement.
         * </p>
         * 
         * @param reasonReference
         *     Why was DeviceUseStatement performed?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates another resource whose existence justifies this DeviceUseStatement.
         * </p>
         * 
         * @param reasonReference
         *     Why was DeviceUseStatement performed?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference.addAll(reasonReference);
            return this;
        }

        /**
         * <p>
         * Indicates the anotomic location on the subject's body where the device was used ( i.e. the target).
         * </p>
         * 
         * @param bodySite
         *     Target body site
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodySite(CodeableConcept bodySite) {
            this.bodySite = bodySite;
            return this;
        }

        /**
         * <p>
         * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
         * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
         * </p>
         * 
         * @param note
         *     Addition details (comments, instructions)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
         * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
         * </p>
         * 
         * @param note
         *     Addition details (comments, instructions)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        @Override
        public DeviceUseStatement build() {
            return new DeviceUseStatement(this);
        }

        private Builder from(DeviceUseStatement deviceUseStatement) {
            id = deviceUseStatement.id;
            meta = deviceUseStatement.meta;
            implicitRules = deviceUseStatement.implicitRules;
            language = deviceUseStatement.language;
            text = deviceUseStatement.text;
            contained.addAll(deviceUseStatement.contained);
            extension.addAll(deviceUseStatement.extension);
            modifierExtension.addAll(deviceUseStatement.modifierExtension);
            identifier.addAll(deviceUseStatement.identifier);
            basedOn.addAll(deviceUseStatement.basedOn);
            derivedFrom.addAll(deviceUseStatement.derivedFrom);
            timing = deviceUseStatement.timing;
            recordedOn = deviceUseStatement.recordedOn;
            source = deviceUseStatement.source;
            reasonCode.addAll(deviceUseStatement.reasonCode);
            reasonReference.addAll(deviceUseStatement.reasonReference);
            bodySite = deviceUseStatement.bodySite;
            note.addAll(deviceUseStatement.note);
            return this;
        }
    }
}
