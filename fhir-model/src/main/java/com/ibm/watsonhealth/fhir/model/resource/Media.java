/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.MediaStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by 
 * direct reference.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Media extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> basedOn;
    private final List<Reference> partOf;
    private final MediaStatus status;
    private final CodeableConcept type;
    private final CodeableConcept modality;
    private final CodeableConcept view;
    private final Reference subject;
    private final Reference encounter;
    private final Element created;
    private final Instant issued;
    private final Reference operator;
    private final List<CodeableConcept> reasonCode;
    private final CodeableConcept bodySite;
    private final String deviceName;
    private final Reference device;
    private final PositiveInt height;
    private final PositiveInt width;
    private final PositiveInt frames;
    private final Decimal duration;
    private final Attachment content;
    private final List<Annotation> note;

    private Media(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.basedOn = builder.basedOn;
        this.partOf = builder.partOf;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.type = builder.type;
        this.modality = builder.modality;
        this.view = builder.view;
        this.subject = builder.subject;
        this.encounter = builder.encounter;
        this.created = ValidationSupport.choiceElement(builder.created, "created", DateTime.class, Period.class);
        this.issued = builder.issued;
        this.operator = builder.operator;
        this.reasonCode = builder.reasonCode;
        this.bodySite = builder.bodySite;
        this.deviceName = builder.deviceName;
        this.device = builder.device;
        this.height = builder.height;
        this.width = builder.width;
        this.frames = builder.frames;
        this.duration = builder.duration;
        this.content = ValidationSupport.requireNonNull(builder.content, "content");
        this.note = builder.note;
    }

    /**
     * <p>
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the 
     * context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.
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
     * A procedure that is fulfilled in whole or in part by the creation of this media.
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
     * A larger event of which this particular event is a component or step.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * The current state of the {{title}}.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MediaStatus}.
     */
    public MediaStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A code that classifies whether the media is an image, video or audio recording or some other media category.
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
     * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM 
     * system, are wrapped in a Media resource, then this is the modality.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getModality() {
        return modality;
    }

    /**
     * <p>
     * The name of the imaging view e.g. Lateral or Antero-posterior (AP).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getView() {
        return view;
    }

    /**
     * <p>
     * Who/What this Media is a record of.
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
     * The encounter that establishes the context for this media.
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
     * The date and time(s) at which the media was collected.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getCreated() {
        return created;
    }

    /**
     * <p>
     * The date and time this version of the media was made available to providers, typically after having been reviewed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getIssued() {
        return issued;
    }

    /**
     * <p>
     * The person who administered the collection of the image.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOperator() {
        return operator;
    }

    /**
     * <p>
     * Describes why the event occurred in coded or textual form.
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
     * Indicates the site on the subject's body where the observation was made (i.e. the target site).
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
     * The name of the device / manufacturer of the device that was used to make the recording.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * <p>
     * The device used to collect the media.
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
     * Height of the image in pixels (photo/video).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getHeight() {
        return height;
    }

    /**
     * <p>
     * Width of the image in pixels (photo/video).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getWidth() {
        return width;
    }

    /**
     * <p>
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes 
     * multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in 
     * order to alert interface software that a multi-frame capable rendering widget is required.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getFrames() {
        return frames;
    }

    /**
     * <p>
     * The duration of the recording in seconds - for audio and video.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Decimal}.
     */
    public Decimal getDuration() {
        return duration;
    }

    /**
     * <p>
     * The actual content of the media - inline or by direct reference to the media source file.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Attachment}.
     */
    public Attachment getContent() {
        return content;
    }

    /**
     * <p>
     * Comments made about the media by the performer, subject or other participants.
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
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(type, "type", visitor);
                accept(modality, "modality", visitor);
                accept(view, "view", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(created, "created", visitor, true);
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, content);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.basedOn.addAll(basedOn);
        builder.partOf.addAll(partOf);
        builder.type = type;
        builder.modality = modality;
        builder.view = view;
        builder.subject = subject;
        builder.encounter = encounter;
        builder.created = created;
        builder.issued = issued;
        builder.operator = operator;
        builder.reasonCode.addAll(reasonCode);
        builder.bodySite = bodySite;
        builder.deviceName = deviceName;
        builder.device = device;
        builder.height = height;
        builder.width = width;
        builder.frames = frames;
        builder.duration = duration;
        builder.note.addAll(note);
        return builder;
    }

    public static Builder builder(MediaStatus status, Attachment content) {
        return new Builder(status, content);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final MediaStatus status;
        private final Attachment content;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
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
        private List<Annotation> note = new ArrayList<>();

        private Builder(MediaStatus status, Attachment content) {
            super();
            this.status = status;
            this.content = content;
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
         * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the 
         * context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.
         * </p>
         * 
         * @param identifier
         *     Identifier(s) for the image
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
         * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the 
         * context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.
         * </p>
         * 
         * @param identifier
         *     Identifier(s) for the image
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
         * A procedure that is fulfilled in whole or in part by the creation of this media.
         * </p>
         * 
         * @param basedOn
         *     Procedure that caused this media to be created
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
         * A procedure that is fulfilled in whole or in part by the creation of this media.
         * </p>
         * 
         * @param basedOn
         *     Procedure that caused this media to be created
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
         * A larger event of which this particular event is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A larger event of which this particular event is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf.addAll(partOf);
            return this;
        }

        /**
         * <p>
         * A code that classifies whether the media is an image, video or audio recording or some other media category.
         * </p>
         * 
         * @param type
         *     Classification of media as image, video, or audio
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM 
         * system, are wrapped in a Media resource, then this is the modality.
         * </p>
         * 
         * @param modality
         *     The type of acquisition equipment/process
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder modality(CodeableConcept modality) {
            this.modality = modality;
            return this;
        }

        /**
         * <p>
         * The name of the imaging view e.g. Lateral or Antero-posterior (AP).
         * </p>
         * 
         * @param view
         *     Imaging view, e.g. Lateral or Antero-posterior
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder view(CodeableConcept view) {
            this.view = view;
            return this;
        }

        /**
         * <p>
         * Who/What this Media is a record of.
         * </p>
         * 
         * @param subject
         *     Who/What this Media is a record of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The encounter that establishes the context for this media.
         * </p>
         * 
         * @param encounter
         *     Encounter associated with media
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * The date and time(s) at which the media was collected.
         * </p>
         * 
         * @param created
         *     When Media was collected
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder created(Element created) {
            this.created = created;
            return this;
        }

        /**
         * <p>
         * The date and time this version of the media was made available to providers, typically after having been reviewed.
         * </p>
         * 
         * @param issued
         *     Date/Time this version was made available
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder issued(Instant issued) {
            this.issued = issued;
            return this;
        }

        /**
         * <p>
         * The person who administered the collection of the image.
         * </p>
         * 
         * @param operator
         *     The person who generated the image
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder operator(Reference operator) {
            this.operator = operator;
            return this;
        }

        /**
         * <p>
         * Describes why the event occurred in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why was event performed?
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
         * Describes why the event occurred in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why was event performed?
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
         * Indicates the site on the subject's body where the observation was made (i.e. the target site).
         * </p>
         * 
         * @param bodySite
         *     Observed body part
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
         * The name of the device / manufacturer of the device that was used to make the recording.
         * </p>
         * 
         * @param deviceName
         *     Name of the device/manufacturer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder deviceName(String deviceName) {
            this.deviceName = deviceName;
            return this;
        }

        /**
         * <p>
         * The device used to collect the media.
         * </p>
         * 
         * @param device
         *     Observing Device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder device(Reference device) {
            this.device = device;
            return this;
        }

        /**
         * <p>
         * Height of the image in pixels (photo/video).
         * </p>
         * 
         * @param height
         *     Height of the image in pixels (photo/video)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder height(PositiveInt height) {
            this.height = height;
            return this;
        }

        /**
         * <p>
         * Width of the image in pixels (photo/video).
         * </p>
         * 
         * @param width
         *     Width of the image in pixels (photo/video)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder width(PositiveInt width) {
            this.width = width;
            return this;
        }

        /**
         * <p>
         * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes 
         * multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in 
         * order to alert interface software that a multi-frame capable rendering widget is required.
         * </p>
         * 
         * @param frames
         *     Number of frames if &gt; 1 (photo)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder frames(PositiveInt frames) {
            this.frames = frames;
            return this;
        }

        /**
         * <p>
         * The duration of the recording in seconds - for audio and video.
         * </p>
         * 
         * @param duration
         *     Length in seconds (audio / video)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder duration(Decimal duration) {
            this.duration = duration;
            return this;
        }

        /**
         * <p>
         * Comments made about the media by the performer, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about the media
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
         * Comments made about the media by the performer, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about the media
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        @Override
        public Media build() {
            return new Media(this);
        }
    }
}
