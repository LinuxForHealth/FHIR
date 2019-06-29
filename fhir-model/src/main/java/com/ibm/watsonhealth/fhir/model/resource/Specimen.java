/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.SpecimenStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A sample to be used for analysis.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Specimen extends DomainResource {
    private final List<Identifier> identifier;
    private final Identifier accessionIdentifier;
    private final SpecimenStatus status;
    private final CodeableConcept type;
    private final Reference subject;
    private final DateTime receivedTime;
    private final List<Reference> parent;
    private final List<Reference> request;
    private final Collection collection;
    private final List<Processing> processing;
    private final List<Container> container;
    private final List<CodeableConcept> condition;
    private final List<Annotation> note;

    private Specimen(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        accessionIdentifier = builder.accessionIdentifier;
        status = builder.status;
        type = builder.type;
        subject = builder.subject;
        receivedTime = builder.receivedTime;
        parent = Collections.unmodifiableList(builder.parent);
        request = Collections.unmodifiableList(builder.request);
        collection = builder.collection;
        processing = Collections.unmodifiableList(builder.processing);
        container = Collections.unmodifiableList(builder.container);
        condition = Collections.unmodifiableList(builder.condition);
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * <p>
     * Id for specimen.
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
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen 
     * identifier, depending on local lab procedures.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getAccessionIdentifier() {
        return accessionIdentifier;
    }

    /**
     * <p>
     * The availability of the specimen.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link SpecimenStatus}.
     */
    public SpecimenStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The kind of material that forms the specimen.
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
     * Where the specimen came from. This may be from patient(s), from a location (e.g., the source of an environmental 
     * sample), or a sampling of a substance or a device.
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
     * Time when specimen was received for processing or testing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getReceivedTime() {
        return receivedTime;
    }

    /**
     * <p>
     * Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of 
     * another specimen.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getParent() {
        return parent;
    }

    /**
     * <p>
     * Details concerning a service request that required a specimen to be collected.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRequest() {
        return request;
    }

    /**
     * <p>
     * Details concerning the specimen collection.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Collection}.
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * <p>
     * Details concerning processing and processing steps for the specimen.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Processing}.
     */
    public List<Processing> getProcessing() {
        return processing;
    }

    /**
     * <p>
     * The container holding the specimen. The recursive nature of containers; i.e. blood in tube in tray in rack is not 
     * addressed here.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Container}.
     */
    public List<Container> getContainer() {
        return container;
    }

    /**
     * <p>
     * A mode or state of being that describes the nature of the specimen.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCondition() {
        return condition;
    }

    /**
     * <p>
     * To communicate any details or issues about the specimen or during the specimen collection. (for example: broken vial, 
     * sent with patient, frozen).
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
                accept(accessionIdentifier, "accessionIdentifier", visitor);
                accept(status, "status", visitor);
                accept(type, "type", visitor);
                accept(subject, "subject", visitor);
                accept(receivedTime, "receivedTime", visitor);
                accept(parent, "parent", visitor, Reference.class);
                accept(request, "request", visitor, Reference.class);
                accept(collection, "collection", visitor);
                accept(processing, "processing", visitor, Processing.class);
                accept(container, "container", visitor, Container.class);
                accept(condition, "condition", visitor, CodeableConcept.class);
                accept(note, "note", visitor, Annotation.class);
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

    public static class Builder extends DomainResource.Builder {
        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Identifier accessionIdentifier;
        private SpecimenStatus status;
        private CodeableConcept type;
        private Reference subject;
        private DateTime receivedTime;
        private List<Reference> parent = new ArrayList<>();
        private List<Reference> request = new ArrayList<>();
        private Collection collection;
        private List<Processing> processing = new ArrayList<>();
        private List<Container> container = new ArrayList<>();
        private List<CodeableConcept> condition = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();

        private Builder() {
            super();
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
        public Builder contained(java.util.Collection<Resource> contained) {
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
        public Builder extension(java.util.Collection<Extension> extension) {
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
        public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Id for specimen.
         * </p>
         * 
         * @param identifier
         *     External Identifier
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
         * Id for specimen.
         * </p>
         * 
         * @param identifier
         *     External Identifier
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(java.util.Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen 
         * identifier, depending on local lab procedures.
         * </p>
         * 
         * @param accessionIdentifier
         *     Identifier assigned by the lab
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder accessionIdentifier(Identifier accessionIdentifier) {
            this.accessionIdentifier = accessionIdentifier;
            return this;
        }

        /**
         * <p>
         * The availability of the specimen.
         * </p>
         * 
         * @param status
         *     available | unavailable | unsatisfactory | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder status(SpecimenStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * The kind of material that forms the specimen.
         * </p>
         * 
         * @param type
         *     Kind of material that forms the specimen
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
         * Where the specimen came from. This may be from patient(s), from a location (e.g., the source of an environmental 
         * sample), or a sampling of a substance or a device.
         * </p>
         * 
         * @param subject
         *     Where the specimen came from. This may be from patient(s), from a location (e.g., the source of an environmental 
         *     sample), or a sampling of a substance or a device
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
         * Time when specimen was received for processing or testing.
         * </p>
         * 
         * @param receivedTime
         *     The time when specimen was received for processing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder receivedTime(DateTime receivedTime) {
            this.receivedTime = receivedTime;
            return this;
        }

        /**
         * <p>
         * Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of 
         * another specimen.
         * </p>
         * 
         * @param parent
         *     Specimen from which this specimen originated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parent(Reference... parent) {
            for (Reference value : parent) {
                this.parent.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of 
         * another specimen.
         * </p>
         * 
         * @param parent
         *     Specimen from which this specimen originated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parent(java.util.Collection<Reference> parent) {
            this.parent.addAll(parent);
            return this;
        }

        /**
         * <p>
         * Details concerning a service request that required a specimen to be collected.
         * </p>
         * 
         * @param request
         *     Why the specimen was collected
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder request(Reference... request) {
            for (Reference value : request) {
                this.request.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Details concerning a service request that required a specimen to be collected.
         * </p>
         * 
         * @param request
         *     Why the specimen was collected
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder request(java.util.Collection<Reference> request) {
            this.request.addAll(request);
            return this;
        }

        /**
         * <p>
         * Details concerning the specimen collection.
         * </p>
         * 
         * @param collection
         *     Collection details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder collection(Collection collection) {
            this.collection = collection;
            return this;
        }

        /**
         * <p>
         * Details concerning processing and processing steps for the specimen.
         * </p>
         * 
         * @param processing
         *     Processing and processing step details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder processing(Processing... processing) {
            for (Processing value : processing) {
                this.processing.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Details concerning processing and processing steps for the specimen.
         * </p>
         * 
         * @param processing
         *     Processing and processing step details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder processing(java.util.Collection<Processing> processing) {
            this.processing.addAll(processing);
            return this;
        }

        /**
         * <p>
         * The container holding the specimen. The recursive nature of containers; i.e. blood in tube in tray in rack is not 
         * addressed here.
         * </p>
         * 
         * @param container
         *     Direct container of specimen (tube/slide, etc.)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder container(Container... container) {
            for (Container value : container) {
                this.container.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The container holding the specimen. The recursive nature of containers; i.e. blood in tube in tray in rack is not 
         * addressed here.
         * </p>
         * 
         * @param container
         *     Direct container of specimen (tube/slide, etc.)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder container(java.util.Collection<Container> container) {
            this.container.addAll(container);
            return this;
        }

        /**
         * <p>
         * A mode or state of being that describes the nature of the specimen.
         * </p>
         * 
         * @param condition
         *     State of the specimen
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(CodeableConcept... condition) {
            for (CodeableConcept value : condition) {
                this.condition.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A mode or state of being that describes the nature of the specimen.
         * </p>
         * 
         * @param condition
         *     State of the specimen
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(java.util.Collection<CodeableConcept> condition) {
            this.condition.addAll(condition);
            return this;
        }

        /**
         * <p>
         * To communicate any details or issues about the specimen or during the specimen collection. (for example: broken vial, 
         * sent with patient, frozen).
         * </p>
         * 
         * @param note
         *     Comments
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
         * To communicate any details or issues about the specimen or during the specimen collection. (for example: broken vial, 
         * sent with patient, frozen).
         * </p>
         * 
         * @param note
         *     Comments
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(java.util.Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        @Override
        public Specimen build() {
            return new Specimen(this);
        }

        private Builder from(Specimen specimen) {
            id = specimen.id;
            meta = specimen.meta;
            implicitRules = specimen.implicitRules;
            language = specimen.language;
            text = specimen.text;
            contained.addAll(specimen.contained);
            extension.addAll(specimen.extension);
            modifierExtension.addAll(specimen.modifierExtension);
            identifier.addAll(specimen.identifier);
            accessionIdentifier = specimen.accessionIdentifier;
            status = specimen.status;
            type = specimen.type;
            subject = specimen.subject;
            receivedTime = specimen.receivedTime;
            parent.addAll(specimen.parent);
            request.addAll(specimen.request);
            collection = specimen.collection;
            processing.addAll(specimen.processing);
            container.addAll(specimen.container);
            condition.addAll(specimen.condition);
            note.addAll(specimen.note);
            return this;
        }
    }

    /**
     * <p>
     * Details concerning the specimen collection.
     * </p>
     */
    public static class Collection extends BackboneElement {
        private final Reference collector;
        private final Element collected;
        private final Duration duration;
        private final Quantity quantity;
        private final CodeableConcept method;
        private final CodeableConcept bodySite;
        private final Element fastingStatus;

        private Collection(Builder builder) {
            super(builder);
            collector = builder.collector;
            collected = ValidationSupport.choiceElement(builder.collected, "collected", DateTime.class, Period.class);
            duration = builder.duration;
            quantity = builder.quantity;
            method = builder.method;
            bodySite = builder.bodySite;
            fastingStatus = ValidationSupport.choiceElement(builder.fastingStatus, "fastingStatus", CodeableConcept.class, Duration.class);
        }

        /**
         * <p>
         * Person who collected the specimen.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getCollector() {
            return collector;
        }

        /**
         * <p>
         * Time when specimen was collected from subject - the physiologically relevant time.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getCollected() {
            return collected;
        }

        /**
         * <p>
         * The span of time over which the collection of a specimen occurred.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getDuration() {
            return duration;
        }

        /**
         * <p>
         * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an 
         * anatomic pathology sample.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getQuantity() {
            return quantity;
        }

        /**
         * <p>
         * A coded value specifying the technique that is used to perform the procedure.
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
         * Anatomical location from which the specimen was collected (if subject is a patient). This is the target site. This 
         * element is not used for environmental specimens.
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
         * Abstinence or reduction from some or all food, drink, or both, for a period of time prior to sample collection.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getFastingStatus() {
            return fastingStatus;
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
                    accept(collector, "collector", visitor);
                    accept(collected, "collected", visitor, true);
                    accept(duration, "duration", visitor);
                    accept(quantity, "quantity", visitor);
                    accept(method, "method", visitor);
                    accept(bodySite, "bodySite", visitor);
                    accept(fastingStatus, "fastingStatus", visitor, true);
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
            private Reference collector;
            private Element collected;
            private Duration duration;
            private Quantity quantity;
            private CodeableConcept method;
            private CodeableConcept bodySite;
            private Element fastingStatus;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Person who collected the specimen.
             * </p>
             * 
             * @param collector
             *     Who collected the specimen
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder collector(Reference collector) {
                this.collector = collector;
                return this;
            }

            /**
             * <p>
             * Time when specimen was collected from subject - the physiologically relevant time.
             * </p>
             * 
             * @param collected
             *     Collection time
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder collected(Element collected) {
                this.collected = collected;
                return this;
            }

            /**
             * <p>
             * The span of time over which the collection of a specimen occurred.
             * </p>
             * 
             * @param duration
             *     How long it took to collect specimen
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder duration(Duration duration) {
                this.duration = duration;
                return this;
            }

            /**
             * <p>
             * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an 
             * anatomic pathology sample.
             * </p>
             * 
             * @param quantity
             *     The quantity of specimen collected
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * <p>
             * A coded value specifying the technique that is used to perform the procedure.
             * </p>
             * 
             * @param method
             *     Technique used to perform collection
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder method(CodeableConcept method) {
                this.method = method;
                return this;
            }

            /**
             * <p>
             * Anatomical location from which the specimen was collected (if subject is a patient). This is the target site. This 
             * element is not used for environmental specimens.
             * </p>
             * 
             * @param bodySite
             *     Anatomical collection site
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
             * Abstinence or reduction from some or all food, drink, or both, for a period of time prior to sample collection.
             * </p>
             * 
             * @param fastingStatus
             *     Whether or how long patient abstained from food and/or drink
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder fastingStatus(Element fastingStatus) {
                this.fastingStatus = fastingStatus;
                return this;
            }

            @Override
            public Collection build() {
                return new Collection(this);
            }

            private Builder from(Collection collection) {
                id = collection.id;
                extension.addAll(collection.extension);
                modifierExtension.addAll(collection.modifierExtension);
                collector = collection.collector;
                collected = collection.collected;
                duration = collection.duration;
                quantity = collection.quantity;
                method = collection.method;
                bodySite = collection.bodySite;
                fastingStatus = collection.fastingStatus;
                return this;
            }
        }
    }

    /**
     * <p>
     * Details concerning processing and processing steps for the specimen.
     * </p>
     */
    public static class Processing extends BackboneElement {
        private final String description;
        private final CodeableConcept procedure;
        private final List<Reference> additive;
        private final Element time;

        private Processing(Builder builder) {
            super(builder);
            description = builder.description;
            procedure = builder.procedure;
            additive = Collections.unmodifiableList(builder.additive);
            time = ValidationSupport.choiceElement(builder.time, "time", DateTime.class, Period.class);
        }

        /**
         * <p>
         * Textual description of procedure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * A coded value specifying the procedure used to process the specimen.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getProcedure() {
            return procedure;
        }

        /**
         * <p>
         * Material used in the processing step.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getAdditive() {
            return additive;
        }

        /**
         * <p>
         * A record of the time or period when the specimen processing occurred. For example the time of sample fixation or the 
         * period of time the sample was in formalin.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTime() {
            return time;
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
                    accept(description, "description", visitor);
                    accept(procedure, "procedure", visitor);
                    accept(additive, "additive", visitor, Reference.class);
                    accept(time, "time", visitor, true);
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
            private String description;
            private CodeableConcept procedure;
            private List<Reference> additive = new ArrayList<>();
            private Element time;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Textual description of procedure.
             * </p>
             * 
             * @param description
             *     Textual description of procedure
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * A coded value specifying the procedure used to process the specimen.
             * </p>
             * 
             * @param procedure
             *     Indicates the treatment step applied to the specimen
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder procedure(CodeableConcept procedure) {
                this.procedure = procedure;
                return this;
            }

            /**
             * <p>
             * Material used in the processing step.
             * </p>
             * 
             * @param additive
             *     Material used in the processing step
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder additive(Reference... additive) {
                for (Reference value : additive) {
                    this.additive.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Material used in the processing step.
             * </p>
             * 
             * @param additive
             *     Material used in the processing step
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder additive(java.util.Collection<Reference> additive) {
                this.additive.addAll(additive);
                return this;
            }

            /**
             * <p>
             * A record of the time or period when the specimen processing occurred. For example the time of sample fixation or the 
             * period of time the sample was in formalin.
             * </p>
             * 
             * @param time
             *     Date and time of specimen processing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder time(Element time) {
                this.time = time;
                return this;
            }

            @Override
            public Processing build() {
                return new Processing(this);
            }

            private Builder from(Processing processing) {
                id = processing.id;
                extension.addAll(processing.extension);
                modifierExtension.addAll(processing.modifierExtension);
                description = processing.description;
                procedure = processing.procedure;
                additive.addAll(processing.additive);
                time = processing.time;
                return this;
            }
        }
    }

    /**
     * <p>
     * The container holding the specimen. The recursive nature of containers; i.e. blood in tube in tray in rack is not 
     * addressed here.
     * </p>
     */
    public static class Container extends BackboneElement {
        private final List<Identifier> identifier;
        private final String description;
        private final CodeableConcept type;
        private final Quantity capacity;
        private final Quantity specimenQuantity;
        private final Element additive;

        private Container(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            description = builder.description;
            type = builder.type;
            capacity = builder.capacity;
            specimenQuantity = builder.specimenQuantity;
            additive = ValidationSupport.choiceElement(builder.additive, "additive", CodeableConcept.class, Reference.class);
        }

        /**
         * <p>
         * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may 
         * differ from the specimen id in some circumstances.
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
         * Textual description of the container.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * The type of container associated with the specimen (e.g. slide, aliquot, etc.).
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
         * The capacity (volume or other measure) the container may contain.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getCapacity() {
            return capacity;
        }

        /**
         * <p>
         * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on 
         * the specimen type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getSpecimenQuantity() {
            return specimenQuantity;
        }

        /**
         * <p>
         * Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getAdditive() {
            return additive;
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
                    accept(identifier, "identifier", visitor, Identifier.class);
                    accept(description, "description", visitor);
                    accept(type, "type", visitor);
                    accept(capacity, "capacity", visitor);
                    accept(specimenQuantity, "specimenQuantity", visitor);
                    accept(additive, "additive", visitor, true);
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
            private List<Identifier> identifier = new ArrayList<>();
            private String description;
            private CodeableConcept type;
            private Quantity capacity;
            private Quantity specimenQuantity;
            private Element additive;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may 
             * differ from the specimen id in some circumstances.
             * </p>
             * 
             * @param identifier
             *     Id for the container
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
             * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may 
             * differ from the specimen id in some circumstances.
             * </p>
             * 
             * @param identifier
             *     Id for the container
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder identifier(java.util.Collection<Identifier> identifier) {
                this.identifier.addAll(identifier);
                return this;
            }

            /**
             * <p>
             * Textual description of the container.
             * </p>
             * 
             * @param description
             *     Textual description of the container
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * The type of container associated with the specimen (e.g. slide, aliquot, etc.).
             * </p>
             * 
             * @param type
             *     Kind of container directly associated with specimen
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
             * The capacity (volume or other measure) the container may contain.
             * </p>
             * 
             * @param capacity
             *     Container volume or size
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder capacity(Quantity capacity) {
                this.capacity = capacity;
                return this;
            }

            /**
             * <p>
             * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on 
             * the specimen type.
             * </p>
             * 
             * @param specimenQuantity
             *     Quantity of specimen within container
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder specimenQuantity(Quantity specimenQuantity) {
                this.specimenQuantity = specimenQuantity;
                return this;
            }

            /**
             * <p>
             * Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.
             * </p>
             * 
             * @param additive
             *     Additive associated with container
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder additive(Element additive) {
                this.additive = additive;
                return this;
            }

            @Override
            public Container build() {
                return new Container(this);
            }

            private Builder from(Container container) {
                id = container.id;
                extension.addAll(container.extension);
                modifierExtension.addAll(container.modifierExtension);
                identifier.addAll(container.identifier);
                description = container.description;
                type = container.type;
                capacity = container.capacity;
                specimenQuantity = container.specimenQuantity;
                additive = container.additive;
                return this;
            }
        }
    }
}
