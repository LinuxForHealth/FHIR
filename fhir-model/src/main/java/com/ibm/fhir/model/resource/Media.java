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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.MediaStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by 
 * direct reference.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "media-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/media-type",
    expression = "type.exists() implies (type.memberOf('http://hl7.org/fhir/ValueSet/media-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Media",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Media extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "ServiceRequest", "CarePlan" })
    private final List<Reference> basedOn;
    @Summary
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "MediaStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes identifying the lifecycle stage of an event.",
        valueSet = "http://hl7.org/fhir/ValueSet/event-status|4.3.0-CIBUILD"
    )
    @Required
    private final MediaStatus status;
    @Summary
    @Binding(
        bindingName = "MediaType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Codes for high level media categories.",
        valueSet = "http://hl7.org/fhir/ValueSet/media-type"
    )
    private final CodeableConcept type;
    @Summary
    @Binding(
        bindingName = "MediaModality",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Detailed information about the type of the image - its kind, purpose, or the kind of equipment used to generate it.",
        valueSet = "http://hl7.org/fhir/ValueSet/media-modality"
    )
    private final CodeableConcept modality;
    @Summary
    @Binding(
        bindingName = "MediaView",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Imaging view (projection) used when collecting an image.",
        valueSet = "http://hl7.org/fhir/ValueSet/media-view"
    )
    private final CodeableConcept view;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "Group", "Device", "Specimen", "Location" })
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    @Choice({ DateTime.class, Period.class })
    private final Element created;
    @Summary
    private final Instant issued;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "CareTeam", "Patient", "Device", "RelatedPerson" })
    private final Reference operator;
    @Summary
    @Binding(
        bindingName = "MediaReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The reason for the media.",
        valueSet = "http://hl7.org/fhir/ValueSet/procedure-reason"
    )
    private final List<CodeableConcept> reasonCode;
    @Summary
    @Binding(
        bindingName = "BodySite",
        strength = BindingStrength.Value.EXAMPLE,
        description = "SNOMED CT Body site concepts",
        valueSet = "http://hl7.org/fhir/ValueSet/body-site"
    )
    private final CodeableConcept bodySite;
    @Summary
    private final String deviceName;
    @Summary
    @ReferenceTarget({ "Device", "DeviceMetric", "Device" })
    private final Reference device;
    @Summary
    private final PositiveInt height;
    @Summary
    private final PositiveInt width;
    @Summary
    private final PositiveInt frames;
    @Summary
    private final Decimal duration;
    @Summary
    @Required
    private final Attachment content;
    private final List<Annotation> note;

    private Media(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        type = builder.type;
        modality = builder.modality;
        view = builder.view;
        subject = builder.subject;
        encounter = builder.encounter;
        created = builder.created;
        issued = builder.issued;
        operator = builder.operator;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        bodySite = builder.bodySite;
        deviceName = builder.deviceName;
        device = builder.device;
        height = builder.height;
        width = builder.width;
        frames = builder.frames;
        duration = builder.duration;
        content = builder.content;
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the 
     * context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A procedure that is fulfilled in whole or in part by the creation of this media.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * A larger event of which this particular event is a component or step.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * The current state of the {{title}}.
     * 
     * @return
     *     An immutable object of type {@link MediaStatus} that is non-null.
     */
    public MediaStatus getStatus() {
        return status;
    }

    /**
     * A code that classifies whether the media is an image, video or audio recording or some other media category.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM 
     * system, are wrapped in a Media resource, then this is the modality.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getModality() {
        return modality;
    }

    /**
     * The name of the imaging view e.g. Lateral or Antero-posterior (AP).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getView() {
        return view;
    }

    /**
     * Who/What this Media is a record of.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The encounter that establishes the context for this media.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The date and time(s) at which the media was collected.
     * 
     * @return
     *     An immutable object of type {@link DateTime} or {@link Period} that may be null.
     */
    public Element getCreated() {
        return created;
    }

    /**
     * The date and time this version of the media was made available to providers, typically after having been reviewed.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getIssued() {
        return issued;
    }

    /**
     * The person who administered the collection of the image.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOperator() {
        return operator;
    }

    /**
     * Describes why the event occurred in coded or textual form.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Indicates the site on the subject's body where the observation was made (i.e. the target site).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getBodySite() {
        return bodySite;
    }

    /**
     * The name of the device / manufacturer of the device that was used to make the recording.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * The device used to collect the media.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * Height of the image in pixels (photo/video).
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that may be null.
     */
    public PositiveInt getHeight() {
        return height;
    }

    /**
     * Width of the image in pixels (photo/video).
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that may be null.
     */
    public PositiveInt getWidth() {
        return width;
    }

    /**
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes 
     * multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in 
     * order to alert interface software that a multi-frame capable rendering widget is required.
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that may be null.
     */
    public PositiveInt getFrames() {
        return frames;
    }

    /**
     * The duration of the recording in seconds - for audio and video.
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getDuration() {
        return duration;
    }

    /**
     * The actual content of the media - inline or by direct reference to the media source file.
     * 
     * @return
     *     An immutable object of type {@link Attachment} that is non-null.
     */
    public Attachment getContent() {
        return content;
    }

    /**
     * Comments made about the media by the performer, subject or other participants.
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
            !partOf.isEmpty() || 
            (status != null) || 
            (type != null) || 
            (modality != null) || 
            (view != null) || 
            (subject != null) || 
            (encounter != null) || 
            (created != null) || 
            (issued != null) || 
            (operator != null) || 
            !reasonCode.isEmpty() || 
            (bodySite != null) || 
            (deviceName != null) || 
            (device != null) || 
            (height != null) || 
            (width != null) || 
            (frames != null) || 
            (duration != null) || 
            (content != null) || 
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
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(type, "type", visitor);
                accept(modality, "modality", visitor);
                accept(view, "view", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(created, "created", visitor);
                accept(issued, "issued", visitor);
                accept(operator, "operator", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(bodySite, "bodySite", visitor);
                accept(deviceName, "deviceName", visitor);
                accept(device, "device", visitor);
                accept(height, "height", visitor);
                accept(width, "width", visitor);
                accept(frames, "frames", visitor);
                accept(duration, "duration", visitor);
                accept(content, "content", visitor);
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
        Media other = (Media) obj;
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
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(type, other.type) && 
            Objects.equals(modality, other.modality) && 
            Objects.equals(view, other.view) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(created, other.created) && 
            Objects.equals(issued, other.issued) && 
            Objects.equals(operator, other.operator) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(bodySite, other.bodySite) && 
            Objects.equals(deviceName, other.deviceName) && 
            Objects.equals(device, other.device) && 
            Objects.equals(height, other.height) && 
            Objects.equals(width, other.width) && 
            Objects.equals(frames, other.frames) && 
            Objects.equals(duration, other.duration) && 
            Objects.equals(content, other.content) && 
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
                partOf, 
                status, 
                type, 
                modality, 
                view, 
                subject, 
                encounter, 
                created, 
                issued, 
                operator, 
                reasonCode, 
                bodySite, 
                deviceName, 
                device, 
                height, 
                width, 
                frames, 
                duration, 
                content, 
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
        private List<Reference> partOf = new ArrayList<>();
        private MediaStatus status;
        private CodeableConcept type;
        private CodeableConcept modality;
        private CodeableConcept view;
        private Reference subject;
        private Reference encounter;
        private Element created;
        private Instant issued;
        private Reference operator;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private CodeableConcept bodySite;
        private String deviceName;
        private Reference device;
        private PositiveInt height;
        private PositiveInt width;
        private PositiveInt frames;
        private Decimal duration;
        private Attachment content;
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
         * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the 
         * context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier(s) for the image
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
         * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the 
         * context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier(s) for the image
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
         * A procedure that is fulfilled in whole or in part by the creation of this media.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link CarePlan}</li>
         * </ul>
         * 
         * @param basedOn
         *     Procedure that caused this media to be created
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
         * A procedure that is fulfilled in whole or in part by the creation of this media.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link CarePlan}</li>
         * </ul>
         * 
         * @param basedOn
         *     Procedure that caused this media to be created
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
         * A larger event of which this particular event is a component or step.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * A larger event of which this particular event is a component or step.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * The current state of the {{title}}.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     preparation | in-progress | not-done | on-hold | stopped | completed | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(MediaStatus status) {
            this.status = status;
            return this;
        }

        /**
         * A code that classifies whether the media is an image, video or audio recording or some other media category.
         * 
         * @param type
         *     Classification of media as image, video, or audio
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM 
         * system, are wrapped in a Media resource, then this is the modality.
         * 
         * @param modality
         *     The type of acquisition equipment/process
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder modality(CodeableConcept modality) {
            this.modality = modality;
            return this;
        }

        /**
         * The name of the imaging view e.g. Lateral or Antero-posterior (AP).
         * 
         * @param view
         *     Imaging view, e.g. Lateral or Antero-posterior
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder view(CodeableConcept view) {
            this.view = view;
            return this;
        }

        /**
         * Who/What this Media is a record of.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Group}</li>
         * <li>{@link Device}</li>
         * <li>{@link Specimen}</li>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param subject
         *     Who/What this Media is a record of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The encounter that establishes the context for this media.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Encounter associated with media
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * The date and time(s) at which the media was collected.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param created
         *     When Media was collected
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(Element created) {
            this.created = created;
            return this;
        }

        /**
         * Convenience method for setting {@code issued}.
         * 
         * @param issued
         *     Date/Time this version was made available
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #issued(com.ibm.fhir.model.type.Instant)
         */
        public Builder issued(java.time.ZonedDateTime issued) {
            this.issued = (issued == null) ? null : Instant.of(issued);
            return this;
        }

        /**
         * The date and time this version of the media was made available to providers, typically after having been reviewed.
         * 
         * @param issued
         *     Date/Time this version was made available
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder issued(Instant issued) {
            this.issued = issued;
            return this;
        }

        /**
         * The person who administered the collection of the image.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * <li>{@link CareTeam}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param operator
         *     The person who generated the image
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder operator(Reference operator) {
            this.operator = operator;
            return this;
        }

        /**
         * Describes why the event occurred in coded or textual form.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why was event performed?
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
         * Describes why the event occurred in coded or textual form.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why was event performed?
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
         * Indicates the site on the subject's body where the observation was made (i.e. the target site).
         * 
         * @param bodySite
         *     Observed body part
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder bodySite(CodeableConcept bodySite) {
            this.bodySite = bodySite;
            return this;
        }

        /**
         * Convenience method for setting {@code deviceName}.
         * 
         * @param deviceName
         *     Name of the device/manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #deviceName(com.ibm.fhir.model.type.String)
         */
        public Builder deviceName(java.lang.String deviceName) {
            this.deviceName = (deviceName == null) ? null : String.of(deviceName);
            return this;
        }

        /**
         * The name of the device / manufacturer of the device that was used to make the recording.
         * 
         * @param deviceName
         *     Name of the device/manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder deviceName(String deviceName) {
            this.deviceName = deviceName;
            return this;
        }

        /**
         * The device used to collect the media.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link DeviceMetric}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param device
         *     Observing Device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Reference device) {
            this.device = device;
            return this;
        }

        /**
         * Height of the image in pixels (photo/video).
         * 
         * @param height
         *     Height of the image in pixels (photo/video)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder height(PositiveInt height) {
            this.height = height;
            return this;
        }

        /**
         * Width of the image in pixels (photo/video).
         * 
         * @param width
         *     Width of the image in pixels (photo/video)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder width(PositiveInt width) {
            this.width = width;
            return this;
        }

        /**
         * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes 
         * multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in 
         * order to alert interface software that a multi-frame capable rendering widget is required.
         * 
         * @param frames
         *     Number of frames if &gt; 1 (photo)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder frames(PositiveInt frames) {
            this.frames = frames;
            return this;
        }

        /**
         * The duration of the recording in seconds - for audio and video.
         * 
         * @param duration
         *     Length in seconds (audio / video)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder duration(Decimal duration) {
            this.duration = duration;
            return this;
        }

        /**
         * The actual content of the media - inline or by direct reference to the media source file.
         * 
         * <p>This element is required.
         * 
         * @param content
         *     Actual Media - reference or data
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder content(Attachment content) {
            this.content = content;
            return this;
        }

        /**
         * Comments made about the media by the performer, subject or other participants.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the media
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
         * Comments made about the media by the performer, subject or other participants.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the media
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
         * Build the {@link Media}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>content</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Media}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Media per the base specification
         */
        @Override
        public Media build() {
            Media media = new Media(this);
            if (validating) {
                validate(media);
            }
            return media;
        }

        protected void validate(Media media) {
            super.validate(media);
            ValidationSupport.checkList(media.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(media.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(media.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(media.status, "status");
            ValidationSupport.choiceElement(media.created, "created", DateTime.class, Period.class);
            ValidationSupport.checkList(media.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.requireNonNull(media.content, "content");
            ValidationSupport.checkList(media.note, "note", Annotation.class);
            ValidationSupport.checkReferenceType(media.basedOn, "basedOn", "ServiceRequest", "CarePlan");
            ValidationSupport.checkReferenceType(media.subject, "subject", "Patient", "Practitioner", "PractitionerRole", "Group", "Device", "Specimen", "Location");
            ValidationSupport.checkReferenceType(media.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(media.operator, "operator", "Practitioner", "PractitionerRole", "Organization", "CareTeam", "Patient", "Device", "RelatedPerson");
            ValidationSupport.checkReferenceType(media.device, "device", "Device", "DeviceMetric", "Device");
        }

        protected Builder from(Media media) {
            super.from(media);
            identifier.addAll(media.identifier);
            basedOn.addAll(media.basedOn);
            partOf.addAll(media.partOf);
            status = media.status;
            type = media.type;
            modality = media.modality;
            view = media.view;
            subject = media.subject;
            encounter = media.encounter;
            created = media.created;
            issued = media.issued;
            operator = media.operator;
            reasonCode.addAll(media.reasonCode);
            bodySite = media.bodySite;
            deviceName = media.deviceName;
            device = media.device;
            height = media.height;
            width = media.width;
            frames = media.frames;
            duration = media.duration;
            content = media.content;
            note.addAll(media.note);
            return this;
        }
    }
}
