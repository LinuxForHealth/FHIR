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
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.BackboneElement;
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
import org.linuxforhealth.fhir.model.type.SimpleQuantity;
import org.linuxforhealth.fhir.model.type.Timing;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.type.code.SupplyDeliveryStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * Record of delivery of what is supplied.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SupplyDelivery extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "SupplyRequest" })
    private final List<Reference> basedOn;
    @Summary
    @ReferenceTarget({ "SupplyDelivery", "Contract" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "SupplyDeliveryStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Status of the supply delivery.",
        valueSet = "http://hl7.org/fhir/ValueSet/supplydelivery-status|4.3.0"
    )
    private final SupplyDeliveryStatus status;
    @ReferenceTarget({ "Patient" })
    private final Reference patient;
    @Binding(
        bindingName = "SupplyDeliveryType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The type of supply dispense.",
        valueSet = "http://hl7.org/fhir/ValueSet/supplydelivery-type|4.3.0"
    )
    private final CodeableConcept type;
    private final SuppliedItem suppliedItem;
    @Summary
    @Choice({ DateTime.class, Period.class, Timing.class })
    private final Element occurrence;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference supplier;
    @ReferenceTarget({ "Location" })
    private final Reference destination;
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final List<Reference> receiver;

    private SupplyDelivery(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        patient = builder.patient;
        type = builder.type;
        suppliedItem = builder.suppliedItem;
        occurrence = builder.occurrence;
        supplier = builder.supplier;
        destination = builder.destination;
        receiver = Collections.unmodifiableList(builder.receiver);
    }

    /**
     * Identifier for the supply delivery event that is used to identify it across multiple disparate systems.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this event.
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
     * A code specifying the state of the dispense event.
     * 
     * @return
     *     An immutable object of type {@link SupplyDeliveryStatus} that may be null.
     */
    public SupplyDeliveryStatus getStatus() {
        return status;
    }

    /**
     * A link to a resource representing the person whom the delivered item is for.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial 
     * Fill, Emergency Fill, Samples, etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The item that is being delivered or has been supplied.
     * 
     * @return
     *     An immutable object of type {@link SuppliedItem} that may be null.
     */
    public SuppliedItem getSuppliedItem() {
        return suppliedItem;
    }

    /**
     * The date or time(s) the activity occurred.
     * 
     * @return
     *     An immutable object of type {@link DateTime}, {@link Period} or {@link Timing} that may be null.
     */
    public Element getOccurrence() {
        return occurrence;
    }

    /**
     * The individual responsible for dispensing the medication, supplier or device.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSupplier() {
        return supplier;
    }

    /**
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getDestination() {
        return destination;
    }

    /**
     * Identifies the person who picked up the Supply.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReceiver() {
        return receiver;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !basedOn.isEmpty() || 
            !partOf.isEmpty() || 
            (status != null) || 
            (patient != null) || 
            (type != null) || 
            (suppliedItem != null) || 
            (occurrence != null) || 
            (supplier != null) || 
            (destination != null) || 
            !receiver.isEmpty();
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
                accept(patient, "patient", visitor);
                accept(type, "type", visitor);
                accept(suppliedItem, "suppliedItem", visitor);
                accept(occurrence, "occurrence", visitor);
                accept(supplier, "supplier", visitor);
                accept(destination, "destination", visitor);
                accept(receiver, "receiver", visitor, Reference.class);
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
        SupplyDelivery other = (SupplyDelivery) obj;
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
            Objects.equals(patient, other.patient) && 
            Objects.equals(type, other.type) && 
            Objects.equals(suppliedItem, other.suppliedItem) && 
            Objects.equals(occurrence, other.occurrence) && 
            Objects.equals(supplier, other.supplier) && 
            Objects.equals(destination, other.destination) && 
            Objects.equals(receiver, other.receiver);
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
                patient, 
                type, 
                suppliedItem, 
                occurrence, 
                supplier, 
                destination, 
                receiver);
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
        private SupplyDeliveryStatus status;
        private Reference patient;
        private CodeableConcept type;
        private SuppliedItem suppliedItem;
        private Element occurrence;
        private Reference supplier;
        private Reference destination;
        private List<Reference> receiver = new ArrayList<>();

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
         * Identifier for the supply delivery event that is used to identify it across multiple disparate systems.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External identifier
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
         * Identifier for the supply delivery event that is used to identify it across multiple disparate systems.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External identifier
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link SupplyRequest}</li>
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link SupplyRequest}</li>
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
         * A larger event of which this particular event is a component or step.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link SupplyDelivery}</li>
         * <li>{@link Contract}</li>
         * </ul>
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
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link SupplyDelivery}</li>
         * <li>{@link Contract}</li>
         * </ul>
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
         * A code specifying the state of the dispense event.
         * 
         * @param status
         *     in-progress | completed | abandoned | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(SupplyDeliveryStatus status) {
            this.status = status;
            return this;
        }

        /**
         * A link to a resource representing the person whom the delivered item is for.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Patient for whom the item is supplied
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial 
         * Fill, Emergency Fill, Samples, etc.
         * 
         * @param type
         *     Category of dispense event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * The item that is being delivered or has been supplied.
         * 
         * @param suppliedItem
         *     The item that is delivered or supplied
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder suppliedItem(SuppliedItem suppliedItem) {
            this.suppliedItem = suppliedItem;
            return this;
        }

        /**
         * The date or time(s) the activity occurred.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * <li>{@link Timing}</li>
         * </ul>
         * 
         * @param occurrence
         *     When event occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder occurrence(Element occurrence) {
            this.occurrence = occurrence;
            return this;
        }

        /**
         * The individual responsible for dispensing the medication, supplier or device.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param supplier
         *     Dispenser
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supplier(Reference supplier) {
            this.supplier = supplier;
            return this;
        }

        /**
         * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param destination
         *     Where the Supply was sent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder destination(Reference destination) {
            this.destination = destination;
            return this;
        }

        /**
         * Identifies the person who picked up the Supply.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param receiver
         *     Who collected the Supply
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder receiver(Reference... receiver) {
            for (Reference value : receiver) {
                this.receiver.add(value);
            }
            return this;
        }

        /**
         * Identifies the person who picked up the Supply.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param receiver
         *     Who collected the Supply
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder receiver(Collection<Reference> receiver) {
            this.receiver = new ArrayList<>(receiver);
            return this;
        }

        /**
         * Build the {@link SupplyDelivery}
         * 
         * @return
         *     An immutable object of type {@link SupplyDelivery}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SupplyDelivery per the base specification
         */
        @Override
        public SupplyDelivery build() {
            SupplyDelivery supplyDelivery = new SupplyDelivery(this);
            if (validating) {
                validate(supplyDelivery);
            }
            return supplyDelivery;
        }

        protected void validate(SupplyDelivery supplyDelivery) {
            super.validate(supplyDelivery);
            ValidationSupport.checkList(supplyDelivery.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(supplyDelivery.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(supplyDelivery.partOf, "partOf", Reference.class);
            ValidationSupport.choiceElement(supplyDelivery.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
            ValidationSupport.checkList(supplyDelivery.receiver, "receiver", Reference.class);
            ValidationSupport.checkValueSetBinding(supplyDelivery.type, "type", "http://hl7.org/fhir/ValueSet/supplydelivery-type", "http://terminology.hl7.org/CodeSystem/supply-item-type", "medication", "device");
            ValidationSupport.checkReferenceType(supplyDelivery.basedOn, "basedOn", "SupplyRequest");
            ValidationSupport.checkReferenceType(supplyDelivery.partOf, "partOf", "SupplyDelivery", "Contract");
            ValidationSupport.checkReferenceType(supplyDelivery.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(supplyDelivery.supplier, "supplier", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.checkReferenceType(supplyDelivery.destination, "destination", "Location");
            ValidationSupport.checkReferenceType(supplyDelivery.receiver, "receiver", "Practitioner", "PractitionerRole");
        }

        protected Builder from(SupplyDelivery supplyDelivery) {
            super.from(supplyDelivery);
            identifier.addAll(supplyDelivery.identifier);
            basedOn.addAll(supplyDelivery.basedOn);
            partOf.addAll(supplyDelivery.partOf);
            status = supplyDelivery.status;
            patient = supplyDelivery.patient;
            type = supplyDelivery.type;
            suppliedItem = supplyDelivery.suppliedItem;
            occurrence = supplyDelivery.occurrence;
            supplier = supplyDelivery.supplier;
            destination = supplyDelivery.destination;
            receiver.addAll(supplyDelivery.receiver);
            return this;
        }
    }

    /**
     * The item that is being delivered or has been supplied.
     */
    public static class SuppliedItem extends BackboneElement {
        private final SimpleQuantity quantity;
        @ReferenceTarget({ "Medication", "Substance", "Device" })
        @Choice({ CodeableConcept.class, Reference.class })
        @Binding(
            bindingName = "SupplyDeliveryItem",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The item that was delivered.",
            valueSet = "http://hl7.org/fhir/ValueSet/supply-item"
        )
        private final Element item;

        private SuppliedItem(Builder builder) {
            super(builder);
            quantity = builder.quantity;
            item = builder.item;
        }

        /**
         * The amount of supply that has been dispensed. Includes unit of measure.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getQuantity() {
            return quantity;
        }

        /**
         * Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the 
         * details of the item or a code that identifies the item from a known list.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} or {@link Reference} that may be null.
         */
        public Element getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (quantity != null) || 
                (item != null);
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
                    accept(quantity, "quantity", visitor);
                    accept(item, "item", visitor);
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
            SuppliedItem other = (SuppliedItem) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    quantity, 
                    item);
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
            private SimpleQuantity quantity;
            private Element item;

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
             * The amount of supply that has been dispensed. Includes unit of measure.
             * 
             * @param quantity
             *     Amount dispensed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(SimpleQuantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the 
             * details of the item or a code that identifies the item from a known list.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Medication}</li>
             * <li>{@link Substance}</li>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param item
             *     Medication, Substance, or Device supplied
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Element item) {
                this.item = item;
                return this;
            }

            /**
             * Build the {@link SuppliedItem}
             * 
             * @return
             *     An immutable object of type {@link SuppliedItem}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid SuppliedItem per the base specification
             */
            @Override
            public SuppliedItem build() {
                SuppliedItem suppliedItem = new SuppliedItem(this);
                if (validating) {
                    validate(suppliedItem);
                }
                return suppliedItem;
            }

            protected void validate(SuppliedItem suppliedItem) {
                super.validate(suppliedItem);
                ValidationSupport.choiceElement(suppliedItem.item, "item", CodeableConcept.class, Reference.class);
                ValidationSupport.checkReferenceType(suppliedItem.item, "item", "Medication", "Substance", "Device");
                ValidationSupport.requireValueOrChildren(suppliedItem);
            }

            protected Builder from(SuppliedItem suppliedItem) {
                super.from(suppliedItem);
                quantity = suppliedItem.quantity;
                item = suppliedItem.item;
                return this;
            }
        }
    }
}
