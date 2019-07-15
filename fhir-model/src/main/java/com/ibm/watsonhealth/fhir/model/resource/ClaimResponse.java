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

import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.ClaimResponseStatus;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NoteType;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RemittanceOutcome;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Use;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * This resource provides the adjudication details from the processing of a Claim resource.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ClaimResponse extends DomainResource {
    private final List<Identifier> identifier;
    private final ClaimResponseStatus status;
    private final CodeableConcept type;
    private final CodeableConcept subType;
    private final Use use;
    private final Reference patient;
    private final DateTime created;
    private final Reference insurer;
    private final Reference requestor;
    private final Reference request;
    private final RemittanceOutcome outcome;
    private final String disposition;
    private final String preAuthRef;
    private final Period preAuthPeriod;
    private final CodeableConcept payeeType;
    private final List<Item> item;
    private final List<AddItem> addItem;
    private final List<ClaimResponse.Item.Adjudication> adjudication;
    private final List<Total> total;
    private final Payment payment;
    private final CodeableConcept fundsReserve;
    private final CodeableConcept formCode;
    private final Attachment form;
    private final List<ProcessNote> processNote;
    private final List<Reference> communicationRequest;
    private final List<Insurance> insurance;
    private final List<Error> error;

    private volatile int hashCode;

    private ClaimResponse(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        type = ValidationSupport.requireNonNull(builder.type, "type");
        subType = builder.subType;
        use = ValidationSupport.requireNonNull(builder.use, "use");
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        created = ValidationSupport.requireNonNull(builder.created, "created");
        insurer = ValidationSupport.requireNonNull(builder.insurer, "insurer");
        requestor = builder.requestor;
        request = builder.request;
        outcome = ValidationSupport.requireNonNull(builder.outcome, "outcome");
        disposition = builder.disposition;
        preAuthRef = builder.preAuthRef;
        preAuthPeriod = builder.preAuthPeriod;
        payeeType = builder.payeeType;
        item = Collections.unmodifiableList(builder.item);
        addItem = Collections.unmodifiableList(builder.addItem);
        adjudication = Collections.unmodifiableList(builder.adjudication);
        total = Collections.unmodifiableList(builder.total);
        payment = builder.payment;
        fundsReserve = builder.fundsReserve;
        formCode = builder.formCode;
        form = builder.form;
        processNote = Collections.unmodifiableList(builder.processNote);
        communicationRequest = Collections.unmodifiableList(builder.communicationRequest);
        insurance = Collections.unmodifiableList(builder.insurance);
        error = Collections.unmodifiableList(builder.error);
    }

    /**
     * <p>
     * A unique identifier assigned to this claim response.
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
     *     An immutable object of type {@link ClaimResponseStatus}.
     */
    public ClaimResponseStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
     * and/or a specialty service.
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
     * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
     * and/or a specialty service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSubType() {
        return subType;
    }

    /**
     * <p>
     * A code to indicate whether the nature of the request is: to request adjudication of products and services previously 
     * rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding 
     * adjudication of the listed products and services which could be provided in the future.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Use}.
     */
    public Use getUse() {
        return use;
    }

    /**
     * <p>
     * The party to whom the professional services and/or products have been supplied or are being considered and for whom 
     * actual for facast reimbursement is sought.
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
     * The party responsible for authorization, adjudication and reimbursement.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getInsurer() {
        return insurer;
    }

    /**
     * <p>
     * The provider which is responsible for the claim, predetermination or preauthorization.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequestor() {
        return requestor;
    }

    /**
     * <p>
     * Original request resource reference.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequest() {
        return request;
    }

    /**
     * <p>
     * The outcome of the claim, predetermination, or preauthorization processing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RemittanceOutcome}.
     */
    public RemittanceOutcome getOutcome() {
        return outcome;
    }

    /**
     * <p>
     * A human readable description of the status of the adjudication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * <p>
     * Reference from the Insurer which is used in later communications which refers to this adjudication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPreAuthRef() {
        return preAuthRef;
    }

    /**
     * <p>
     * The time frame during which this authorization is effective.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPreAuthPeriod() {
        return preAuthPeriod;
    }

    /**
     * <p>
     * Type of Party to be reimbursed: subscriber, provider, other.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPayeeType() {
        return payeeType;
    }

    /**
     * <p>
     * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
     * groups of sub-details.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Item}.
     */
    public List<Item> getItem() {
        return item;
    }

    /**
     * <p>
     * The first-tier service adjudications for payor added product or service lines.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AddItem}.
     */
    public List<AddItem> getAddItem() {
        return addItem;
    }

    /**
     * <p>
     * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Adjudication}.
     */
    public List<ClaimResponse.Item.Adjudication> getAdjudication() {
        return adjudication;
    }

    /**
     * <p>
     * Categorized monetary totals for the adjudication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Total}.
     */
    public List<Total> getTotal() {
        return total;
    }

    /**
     * <p>
     * Payment details for the adjudication of the claim.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Payment}.
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * <p>
     * A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and 
     * for whom.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getFundsReserve() {
        return fundsReserve;
    }

    /**
     * <p>
     * A code for the form to be used for printing the content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getFormCode() {
        return formCode;
    }

    /**
     * <p>
     * The actual form, by reference or inclusion, for printing the content or an EOB.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Attachment}.
     */
    public Attachment getForm() {
        return form;
    }

    /**
     * <p>
     * A note that describes or explains adjudication results in a human readable form.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ProcessNote}.
     */
    public List<ProcessNote> getProcessNote() {
        return processNote;
    }

    /**
     * <p>
     * Request for additional supporting or authorizing information.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getCommunicationRequest() {
        return communicationRequest;
    }

    /**
     * <p>
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Insurance}.
     */
    public List<Insurance> getInsurance() {
        return insurance;
    }

    /**
     * <p>
     * Errors encountered during the processing of the adjudication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Error}.
     */
    public List<Error> getError() {
        return error;
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
                accept(type, "type", visitor);
                accept(subType, "subType", visitor);
                accept(use, "use", visitor);
                accept(patient, "patient", visitor);
                accept(created, "created", visitor);
                accept(insurer, "insurer", visitor);
                accept(requestor, "requestor", visitor);
                accept(request, "request", visitor);
                accept(outcome, "outcome", visitor);
                accept(disposition, "disposition", visitor);
                accept(preAuthRef, "preAuthRef", visitor);
                accept(preAuthPeriod, "preAuthPeriod", visitor);
                accept(payeeType, "payeeType", visitor);
                accept(item, "item", visitor, Item.class);
                accept(addItem, "addItem", visitor, AddItem.class);
                accept(adjudication, "adjudication", visitor, ClaimResponse.Item.Adjudication.class);
                accept(total, "total", visitor, Total.class);
                accept(payment, "payment", visitor);
                accept(fundsReserve, "fundsReserve", visitor);
                accept(formCode, "formCode", visitor);
                accept(form, "form", visitor);
                accept(processNote, "processNote", visitor, ProcessNote.class);
                accept(communicationRequest, "communicationRequest", visitor, Reference.class);
                accept(insurance, "insurance", visitor, Insurance.class);
                accept(error, "error", visitor, Error.class);
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
        ClaimResponse other = (ClaimResponse) obj;
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
            Objects.equals(type, other.type) && 
            Objects.equals(subType, other.subType) && 
            Objects.equals(use, other.use) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(created, other.created) && 
            Objects.equals(insurer, other.insurer) && 
            Objects.equals(requestor, other.requestor) && 
            Objects.equals(request, other.request) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(disposition, other.disposition) && 
            Objects.equals(preAuthRef, other.preAuthRef) && 
            Objects.equals(preAuthPeriod, other.preAuthPeriod) && 
            Objects.equals(payeeType, other.payeeType) && 
            Objects.equals(item, other.item) && 
            Objects.equals(addItem, other.addItem) && 
            Objects.equals(adjudication, other.adjudication) && 
            Objects.equals(total, other.total) && 
            Objects.equals(payment, other.payment) && 
            Objects.equals(fundsReserve, other.fundsReserve) && 
            Objects.equals(formCode, other.formCode) && 
            Objects.equals(form, other.form) && 
            Objects.equals(processNote, other.processNote) && 
            Objects.equals(communicationRequest, other.communicationRequest) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(error, other.error);
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
                type, 
                subType, 
                use, 
                patient, 
                created, 
                insurer, 
                requestor, 
                request, 
                outcome, 
                disposition, 
                preAuthRef, 
                preAuthPeriod, 
                payeeType, 
                item, 
                addItem, 
                adjudication, 
                total, 
                payment, 
                fundsReserve, 
                formCode, 
                form, 
                processNote, 
                communicationRequest, 
                insurance, 
                error);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, type, use, patient, created, insurer, outcome).from(this);
    }

    public Builder toBuilder(ClaimResponseStatus status, CodeableConcept type, Use use, Reference patient, DateTime created, Reference insurer, RemittanceOutcome outcome) {
        return new Builder(status, type, use, patient, created, insurer, outcome).from(this);
    }

    public static Builder builder(ClaimResponseStatus status, CodeableConcept type, Use use, Reference patient, DateTime created, Reference insurer, RemittanceOutcome outcome) {
        return new Builder(status, type, use, patient, created, insurer, outcome);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ClaimResponseStatus status;
        private final CodeableConcept type;
        private final Use use;
        private final Reference patient;
        private final DateTime created;
        private final Reference insurer;
        private final RemittanceOutcome outcome;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept subType;
        private Reference requestor;
        private Reference request;
        private String disposition;
        private String preAuthRef;
        private Period preAuthPeriod;
        private CodeableConcept payeeType;
        private List<Item> item = new ArrayList<>();
        private List<AddItem> addItem = new ArrayList<>();
        private List<ClaimResponse.Item.Adjudication> adjudication = new ArrayList<>();
        private List<Total> total = new ArrayList<>();
        private Payment payment;
        private CodeableConcept fundsReserve;
        private CodeableConcept formCode;
        private Attachment form;
        private List<ProcessNote> processNote = new ArrayList<>();
        private List<Reference> communicationRequest = new ArrayList<>();
        private List<Insurance> insurance = new ArrayList<>();
        private List<Error> error = new ArrayList<>();

        private Builder(ClaimResponseStatus status, CodeableConcept type, Use use, Reference patient, DateTime created, Reference insurer, RemittanceOutcome outcome) {
            super();
            this.status = status;
            this.type = type;
            this.use = use;
            this.patient = patient;
            this.created = created;
            this.insurer = insurer;
            this.outcome = outcome;
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
         * A unique identifier assigned to this claim response.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Business Identifier for a claim response
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
         * A unique identifier assigned to this claim response.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business Identifier for a claim response
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
         * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
         * and/or a specialty service.
         * </p>
         * 
         * @param subType
         *     More granular claim type
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subType(CodeableConcept subType) {
            this.subType = subType;
            return this;
        }

        /**
         * <p>
         * The provider which is responsible for the claim, predetermination or preauthorization.
         * </p>
         * 
         * @param requestor
         *     Party responsible for the claim
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requestor(Reference requestor) {
            this.requestor = requestor;
            return this;
        }

        /**
         * <p>
         * Original request resource reference.
         * </p>
         * 
         * @param request
         *     Id of resource triggering adjudication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference request) {
            this.request = request;
            return this;
        }

        /**
         * <p>
         * A human readable description of the status of the adjudication.
         * </p>
         * 
         * @param disposition
         *     Disposition Message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder disposition(String disposition) {
            this.disposition = disposition;
            return this;
        }

        /**
         * <p>
         * Reference from the Insurer which is used in later communications which refers to this adjudication.
         * </p>
         * 
         * @param preAuthRef
         *     Preauthorization reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder preAuthRef(String preAuthRef) {
            this.preAuthRef = preAuthRef;
            return this;
        }

        /**
         * <p>
         * The time frame during which this authorization is effective.
         * </p>
         * 
         * @param preAuthPeriod
         *     Preauthorization reference effective period
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder preAuthPeriod(Period preAuthPeriod) {
            this.preAuthPeriod = preAuthPeriod;
            return this;
        }

        /**
         * <p>
         * Type of Party to be reimbursed: subscriber, provider, other.
         * </p>
         * 
         * @param payeeType
         *     Party to be paid any benefits payable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payeeType(CodeableConcept payeeType) {
            this.payeeType = payeeType;
            return this;
        }

        /**
         * <p>
         * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
         * groups of sub-details.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param item
         *     Adjudication for claim line items
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Item... item) {
            for (Item value : item) {
                this.item.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
         * groups of sub-details.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param item
         *     Adjudication for claim line items
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Collection<Item> item) {
            this.item = new ArrayList<>(item);
            return this;
        }

        /**
         * <p>
         * The first-tier service adjudications for payor added product or service lines.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder addItem(AddItem... addItem) {
            for (AddItem value : addItem) {
                this.addItem.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The first-tier service adjudications for payor added product or service lines.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder addItem(Collection<AddItem> addItem) {
            this.addItem = new ArrayList<>(addItem);
            return this;
        }

        /**
         * <p>
         * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder adjudication(ClaimResponse.Item.Adjudication... adjudication) {
            for (ClaimResponse.Item.Adjudication value : adjudication) {
                this.adjudication.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder adjudication(Collection<ClaimResponse.Item.Adjudication> adjudication) {
            this.adjudication = new ArrayList<>(adjudication);
            return this;
        }

        /**
         * <p>
         * Categorized monetary totals for the adjudication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder total(Total... total) {
            for (Total value : total) {
                this.total.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Categorized monetary totals for the adjudication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder total(Collection<Total> total) {
            this.total = new ArrayList<>(total);
            return this;
        }

        /**
         * <p>
         * Payment details for the adjudication of the claim.
         * </p>
         * 
         * @param payment
         *     Payment Details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payment(Payment payment) {
            this.payment = payment;
            return this;
        }

        /**
         * <p>
         * A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and 
         * for whom.
         * </p>
         * 
         * @param fundsReserve
         *     Funds reserved status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fundsReserve(CodeableConcept fundsReserve) {
            this.fundsReserve = fundsReserve;
            return this;
        }

        /**
         * <p>
         * A code for the form to be used for printing the content.
         * </p>
         * 
         * @param formCode
         *     Printed form identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder formCode(CodeableConcept formCode) {
            this.formCode = formCode;
            return this;
        }

        /**
         * <p>
         * The actual form, by reference or inclusion, for printing the content or an EOB.
         * </p>
         * 
         * @param form
         *     Printed reference or actual form
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder form(Attachment form) {
            this.form = form;
            return this;
        }

        /**
         * <p>
         * A note that describes or explains adjudication results in a human readable form.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processNote(ProcessNote... processNote) {
            for (ProcessNote value : processNote) {
                this.processNote.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A note that describes or explains adjudication results in a human readable form.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processNote(Collection<ProcessNote> processNote) {
            this.processNote = new ArrayList<>(processNote);
            return this;
        }

        /**
         * <p>
         * Request for additional supporting or authorizing information.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param communicationRequest
         *     Request for additional information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communicationRequest(Reference... communicationRequest) {
            for (Reference value : communicationRequest) {
                this.communicationRequest.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Request for additional supporting or authorizing information.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param communicationRequest
         *     Request for additional information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communicationRequest(Collection<Reference> communicationRequest) {
            this.communicationRequest = new ArrayList<>(communicationRequest);
            return this;
        }

        /**
         * <p>
         * Financial instruments for reimbursement for the health care products and services specified on the claim.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Insurance... insurance) {
            for (Insurance value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Financial instruments for reimbursement for the health care products and services specified on the claim.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Collection<Insurance> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * <p>
         * Errors encountered during the processing of the adjudication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param error
         *     Processing errors
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder error(Error... error) {
            for (Error value : error) {
                this.error.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Errors encountered during the processing of the adjudication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param error
         *     Processing errors
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder error(Collection<Error> error) {
            this.error = new ArrayList<>(error);
            return this;
        }

        @Override
        public ClaimResponse build() {
            return new ClaimResponse(this);
        }

        private Builder from(ClaimResponse claimResponse) {
            id = claimResponse.id;
            meta = claimResponse.meta;
            implicitRules = claimResponse.implicitRules;
            language = claimResponse.language;
            text = claimResponse.text;
            contained.addAll(claimResponse.contained);
            extension.addAll(claimResponse.extension);
            modifierExtension.addAll(claimResponse.modifierExtension);
            identifier.addAll(claimResponse.identifier);
            subType = claimResponse.subType;
            requestor = claimResponse.requestor;
            request = claimResponse.request;
            disposition = claimResponse.disposition;
            preAuthRef = claimResponse.preAuthRef;
            preAuthPeriod = claimResponse.preAuthPeriod;
            payeeType = claimResponse.payeeType;
            item.addAll(claimResponse.item);
            addItem.addAll(claimResponse.addItem);
            adjudication.addAll(claimResponse.adjudication);
            total.addAll(claimResponse.total);
            payment = claimResponse.payment;
            fundsReserve = claimResponse.fundsReserve;
            formCode = claimResponse.formCode;
            form = claimResponse.form;
            processNote.addAll(claimResponse.processNote);
            communicationRequest.addAll(claimResponse.communicationRequest);
            insurance.addAll(claimResponse.insurance);
            error.addAll(claimResponse.error);
            return this;
        }
    }

    /**
     * <p>
     * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or 
     * groups of sub-details.
     * </p>
     */
    public static class Item extends BackboneElement {
        private final PositiveInt itemSequence;
        private final List<PositiveInt> noteNumber;
        private final List<Adjudication> adjudication;
        private final List<Detail> detail;

        private volatile int hashCode;

        private Item(Builder builder) {
            super(builder);
            itemSequence = ValidationSupport.requireNonNull(builder.itemSequence, "itemSequence");
            noteNumber = Collections.unmodifiableList(builder.noteNumber);
            adjudication = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication"));
            detail = Collections.unmodifiableList(builder.detail);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A number to uniquely reference the claim item entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getItemSequence() {
            return itemSequence;
        }

        /**
         * <p>
         * The numbers associated with notes below which apply to the adjudication of this item.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getNoteNumber() {
            return noteNumber;
        }

        /**
         * <p>
         * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
         * simple product or service then this is the result of the adjudication of this item.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Adjudication}.
         */
        public List<Adjudication> getAdjudication() {
            return adjudication;
        }

        /**
         * <p>
         * A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Detail}.
         */
        public List<Detail> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (itemSequence != null) || 
                !noteNumber.isEmpty() || 
                !adjudication.isEmpty() || 
                !detail.isEmpty();
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
                    accept(itemSequence, "itemSequence", visitor);
                    accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                    accept(adjudication, "adjudication", visitor, Adjudication.class);
                    accept(detail, "detail", visitor, Detail.class);
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
            Item other = (Item) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(itemSequence, other.itemSequence) && 
                Objects.equals(noteNumber, other.noteNumber) && 
                Objects.equals(adjudication, other.adjudication) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    itemSequence, 
                    noteNumber, 
                    adjudication, 
                    detail);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(itemSequence, adjudication).from(this);
        }

        public Builder toBuilder(PositiveInt itemSequence, List<Adjudication> adjudication) {
            return new Builder(itemSequence, adjudication).from(this);
        }

        public static Builder builder(PositiveInt itemSequence, List<Adjudication> adjudication) {
            return new Builder(itemSequence, adjudication);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt itemSequence;
            private final List<Adjudication> adjudication;

            // optional
            private List<PositiveInt> noteNumber = new ArrayList<>();
            private List<Detail> detail = new ArrayList<>();

            private Builder(PositiveInt itemSequence, List<Adjudication> adjudication) {
                super();
                this.itemSequence = itemSequence;
                this.adjudication = new ArrayList<>(adjudication);
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
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder noteNumber(PositiveInt... noteNumber) {
                for (PositiveInt value : noteNumber) {
                    this.noteNumber.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber = new ArrayList<>(noteNumber);
                return this;
            }

            /**
             * <p>
             * A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param detail
             *     Adjudication for claim details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Detail... detail) {
                for (Detail value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param detail
             *     Adjudication for claim details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            @Override
            public Item build() {
                return new Item(this);
            }

            private Builder from(Item item) {
                id = item.id;
                extension.addAll(item.extension);
                modifierExtension.addAll(item.modifierExtension);
                noteNumber.addAll(item.noteNumber);
                detail.addAll(item.detail);
                return this;
            }
        }

        /**
         * <p>
         * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a 
         * simple product or service then this is the result of the adjudication of this item.
         * </p>
         */
        public static class Adjudication extends BackboneElement {
            private final CodeableConcept category;
            private final CodeableConcept reason;
            private final Money amount;
            private final Decimal value;

            private volatile int hashCode;

            private Adjudication(Builder builder) {
                super(builder);
                category = ValidationSupport.requireNonNull(builder.category, "category");
                reason = builder.reason;
                amount = builder.amount;
                value = builder.value;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * A code to indicate the information type of this adjudication record. Information types may include the value 
             * submitted, maximum values or percentages allowed or payable under the plan, amounts that: the patient is responsible 
             * for in aggregate or pertaining to this item; amounts paid by other coverages; and, the benefit payable for this item.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCategory() {
                return category;
            }

            /**
             * <p>
             * A code supporting the understanding of the adjudication result and explaining variance from expected amount.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getReason() {
                return reason;
            }

            /**
             * <p>
             * Monetary amount associated with the category.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getAmount() {
                return amount;
            }

            /**
             * <p>
             * A non-monetary value associated with the category. Mutually exclusive to the amount element above.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (category != null) || 
                    (reason != null) || 
                    (amount != null) || 
                    (value != null);
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
                        accept(category, "category", visitor);
                        accept(reason, "reason", visitor);
                        accept(amount, "amount", visitor);
                        accept(value, "value", visitor);
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
                Adjudication other = (Adjudication) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(category, other.category) && 
                    Objects.equals(reason, other.reason) && 
                    Objects.equals(amount, other.amount) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        category, 
                        reason, 
                        amount, 
                        value);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(category).from(this);
            }

            public Builder toBuilder(CodeableConcept category) {
                return new Builder(category).from(this);
            }

            public static Builder builder(CodeableConcept category) {
                return new Builder(category);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept category;

                // optional
                private CodeableConcept reason;
                private Money amount;
                private Decimal value;

                private Builder(CodeableConcept category) {
                    super();
                    this.category = category;
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
                 * A code supporting the understanding of the adjudication result and explaining variance from expected amount.
                 * </p>
                 * 
                 * @param reason
                 *     Explanation of adjudication outcome
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reason(CodeableConcept reason) {
                    this.reason = reason;
                    return this;
                }

                /**
                 * <p>
                 * Monetary amount associated with the category.
                 * </p>
                 * 
                 * @param amount
                 *     Monetary amount
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(Money amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * <p>
                 * A non-monetary value associated with the category. Mutually exclusive to the amount element above.
                 * </p>
                 * 
                 * @param value
                 *     Non-monetary value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Decimal value) {
                    this.value = value;
                    return this;
                }

                @Override
                public Adjudication build() {
                    return new Adjudication(this);
                }

                private Builder from(Adjudication adjudication) {
                    id = adjudication.id;
                    extension.addAll(adjudication.extension);
                    modifierExtension.addAll(adjudication.modifierExtension);
                    reason = adjudication.reason;
                    amount = adjudication.amount;
                    value = adjudication.value;
                    return this;
                }
            }
        }

        /**
         * <p>
         * A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
         * </p>
         */
        public static class Detail extends BackboneElement {
            private final PositiveInt detailSequence;
            private final List<PositiveInt> noteNumber;
            private final List<ClaimResponse.Item.Adjudication> adjudication;
            private final List<SubDetail> subDetail;

            private volatile int hashCode;

            private Detail(Builder builder) {
                super(builder);
                detailSequence = ValidationSupport.requireNonNull(builder.detailSequence, "detailSequence");
                noteNumber = Collections.unmodifiableList(builder.noteNumber);
                adjudication = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication"));
                subDetail = Collections.unmodifiableList(builder.subDetail);
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * A number to uniquely reference the claim detail entry.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link PositiveInt}.
             */
            public PositiveInt getDetailSequence() {
                return detailSequence;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
             */
            public List<PositiveInt> getNoteNumber() {
                return noteNumber;
            }

            /**
             * <p>
             * The adjudication results.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Adjudication}.
             */
            public List<ClaimResponse.Item.Adjudication> getAdjudication() {
                return adjudication;
            }

            /**
             * <p>
             * A sub-detail adjudication of a simple product or service.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link SubDetail}.
             */
            public List<SubDetail> getSubDetail() {
                return subDetail;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (detailSequence != null) || 
                    !noteNumber.isEmpty() || 
                    !adjudication.isEmpty() || 
                    !subDetail.isEmpty();
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
                        accept(detailSequence, "detailSequence", visitor);
                        accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                        accept(adjudication, "adjudication", visitor, ClaimResponse.Item.Adjudication.class);
                        accept(subDetail, "subDetail", visitor, SubDetail.class);
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
                Detail other = (Detail) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(detailSequence, other.detailSequence) && 
                    Objects.equals(noteNumber, other.noteNumber) && 
                    Objects.equals(adjudication, other.adjudication) && 
                    Objects.equals(subDetail, other.subDetail);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        detailSequence, 
                        noteNumber, 
                        adjudication, 
                        subDetail);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(detailSequence, adjudication).from(this);
            }

            public Builder toBuilder(PositiveInt detailSequence, List<ClaimResponse.Item.Adjudication> adjudication) {
                return new Builder(detailSequence, adjudication).from(this);
            }

            public static Builder builder(PositiveInt detailSequence, List<ClaimResponse.Item.Adjudication> adjudication) {
                return new Builder(detailSequence, adjudication);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final PositiveInt detailSequence;
                private final List<ClaimResponse.Item.Adjudication> adjudication;

                // optional
                private List<PositiveInt> noteNumber = new ArrayList<>();
                private List<SubDetail> subDetail = new ArrayList<>();

                private Builder(PositiveInt detailSequence, List<ClaimResponse.Item.Adjudication> adjudication) {
                    super();
                    this.detailSequence = detailSequence;
                    this.adjudication = new ArrayList<>(adjudication);
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
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder noteNumber(PositiveInt... noteNumber) {
                    for (PositiveInt value : noteNumber) {
                        this.noteNumber.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber = new ArrayList<>(noteNumber);
                    return this;
                }

                /**
                 * <p>
                 * A sub-detail adjudication of a simple product or service.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param subDetail
                 *     Adjudication for claim sub-details
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subDetail(SubDetail... subDetail) {
                    for (SubDetail value : subDetail) {
                        this.subDetail.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * A sub-detail adjudication of a simple product or service.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param subDetail
                 *     Adjudication for claim sub-details
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subDetail(Collection<SubDetail> subDetail) {
                    this.subDetail = new ArrayList<>(subDetail);
                    return this;
                }

                @Override
                public Detail build() {
                    return new Detail(this);
                }

                private Builder from(Detail detail) {
                    id = detail.id;
                    extension.addAll(detail.extension);
                    modifierExtension.addAll(detail.modifierExtension);
                    noteNumber.addAll(detail.noteNumber);
                    subDetail.addAll(detail.subDetail);
                    return this;
                }
            }

            /**
             * <p>
             * A sub-detail adjudication of a simple product or service.
             * </p>
             */
            public static class SubDetail extends BackboneElement {
                private final PositiveInt subDetailSequence;
                private final List<PositiveInt> noteNumber;
                private final List<ClaimResponse.Item.Adjudication> adjudication;

                private volatile int hashCode;

                private SubDetail(Builder builder) {
                    super(builder);
                    subDetailSequence = ValidationSupport.requireNonNull(builder.subDetailSequence, "subDetailSequence");
                    noteNumber = Collections.unmodifiableList(builder.noteNumber);
                    adjudication = Collections.unmodifiableList(builder.adjudication);
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * <p>
                 * A number to uniquely reference the claim sub-detail entry.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link PositiveInt}.
                 */
                public PositiveInt getSubDetailSequence() {
                    return subDetailSequence;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
                 */
                public List<PositiveInt> getNoteNumber() {
                    return noteNumber;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Adjudication}.
                 */
                public List<ClaimResponse.Item.Adjudication> getAdjudication() {
                    return adjudication;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (subDetailSequence != null) || 
                        !noteNumber.isEmpty() || 
                        !adjudication.isEmpty();
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
                            accept(subDetailSequence, "subDetailSequence", visitor);
                            accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                            accept(adjudication, "adjudication", visitor, ClaimResponse.Item.Adjudication.class);
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
                    SubDetail other = (SubDetail) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(subDetailSequence, other.subDetailSequence) && 
                        Objects.equals(noteNumber, other.noteNumber) && 
                        Objects.equals(adjudication, other.adjudication);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            subDetailSequence, 
                            noteNumber, 
                            adjudication);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(subDetailSequence).from(this);
                }

                public Builder toBuilder(PositiveInt subDetailSequence) {
                    return new Builder(subDetailSequence).from(this);
                }

                public static Builder builder(PositiveInt subDetailSequence) {
                    return new Builder(subDetailSequence);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final PositiveInt subDetailSequence;

                    // optional
                    private List<PositiveInt> noteNumber = new ArrayList<>();
                    private List<ClaimResponse.Item.Adjudication> adjudication = new ArrayList<>();

                    private Builder(PositiveInt subDetailSequence) {
                        super();
                        this.subDetailSequence = subDetailSequence;
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
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder noteNumber(PositiveInt... noteNumber) {
                        for (PositiveInt value : noteNumber) {
                            this.noteNumber.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber = new ArrayList<>(noteNumber);
                        return this;
                    }

                    /**
                     * <p>
                     * The adjudication results.
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder adjudication(ClaimResponse.Item.Adjudication... adjudication) {
                        for (ClaimResponse.Item.Adjudication value : adjudication) {
                            this.adjudication.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The adjudication results.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder adjudication(Collection<ClaimResponse.Item.Adjudication> adjudication) {
                        this.adjudication = new ArrayList<>(adjudication);
                        return this;
                    }

                    @Override
                    public SubDetail build() {
                        return new SubDetail(this);
                    }

                    private Builder from(SubDetail subDetail) {
                        id = subDetail.id;
                        extension.addAll(subDetail.extension);
                        modifierExtension.addAll(subDetail.modifierExtension);
                        noteNumber.addAll(subDetail.noteNumber);
                        adjudication.addAll(subDetail.adjudication);
                        return this;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * The first-tier service adjudications for payor added product or service lines.
     * </p>
     */
    public static class AddItem extends BackboneElement {
        private final List<PositiveInt> itemSequence;
        private final List<PositiveInt> detailSequence;
        private final List<PositiveInt> subdetailSequence;
        private final List<Reference> provider;
        private final CodeableConcept productOrService;
        private final List<CodeableConcept> modifier;
        private final List<CodeableConcept> programCode;
        private final Element serviced;
        private final Element location;
        private final Quantity quantity;
        private final Money unitPrice;
        private final Decimal factor;
        private final Money net;
        private final CodeableConcept bodySite;
        private final List<CodeableConcept> subSite;
        private final List<PositiveInt> noteNumber;
        private final List<ClaimResponse.Item.Adjudication> adjudication;
        private final List<Detail> detail;

        private volatile int hashCode;

        private AddItem(Builder builder) {
            super(builder);
            itemSequence = Collections.unmodifiableList(builder.itemSequence);
            detailSequence = Collections.unmodifiableList(builder.detailSequence);
            subdetailSequence = Collections.unmodifiableList(builder.subdetailSequence);
            provider = Collections.unmodifiableList(builder.provider);
            productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
            modifier = Collections.unmodifiableList(builder.modifier);
            programCode = Collections.unmodifiableList(builder.programCode);
            serviced = ValidationSupport.choiceElement(builder.serviced, "serviced", Date.class, Period.class);
            location = ValidationSupport.choiceElement(builder.location, "location", CodeableConcept.class, Address.class, Reference.class);
            quantity = builder.quantity;
            unitPrice = builder.unitPrice;
            factor = builder.factor;
            net = builder.net;
            bodySite = builder.bodySite;
            subSite = Collections.unmodifiableList(builder.subSite);
            noteNumber = Collections.unmodifiableList(builder.noteNumber);
            adjudication = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication"));
            detail = Collections.unmodifiableList(builder.detail);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Claim items which this service line is intended to replace.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getItemSequence() {
            return itemSequence;
        }

        /**
         * <p>
         * The sequence number of the details within the claim item which this line is intended to replace.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getDetailSequence() {
            return detailSequence;
        }

        /**
         * <p>
         * The sequence number of the sub-details within the details within the claim item which this line is intended to replace.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getSubdetailSequence() {
            return subdetailSequence;
        }

        /**
         * <p>
         * The providers who are authorized for the services rendered to the patient.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getProvider() {
            return provider;
        }

        /**
         * <p>
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
         * product, service, drug or other billing code for the item.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getProductOrService() {
            return productOrService;
        }

        /**
         * <p>
         * Item typification or modifiers codes to convey additional context for the product or service.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getModifier() {
            return modifier;
        }

        /**
         * <p>
         * Identifies the program under which this may be recovered.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getProgramCode() {
            return programCode;
        }

        /**
         * <p>
         * The date or dates when the service or product was supplied, performed or completed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getServiced() {
            return serviced;
        }

        /**
         * <p>
         * Where the product or service was provided.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getLocation() {
            return location;
        }

        /**
         * <p>
         * The number of repetitions of a service or product.
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
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
         * for the details of the group.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getUnitPrice() {
            return unitPrice;
        }

        /**
         * <p>
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
         * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getFactor() {
            return factor;
        }

        /**
         * <p>
         * The quantity times the unit price for an additional service or product or charge.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getNet() {
            return net;
        }

        /**
         * <p>
         * Physical service site on the patient (limb, tooth, etc.).
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
         * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getSubSite() {
            return subSite;
        }

        /**
         * <p>
         * The numbers associated with notes below which apply to the adjudication of this item.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
         */
        public List<PositiveInt> getNoteNumber() {
            return noteNumber;
        }

        /**
         * <p>
         * The adjudication results.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Adjudication}.
         */
        public List<ClaimResponse.Item.Adjudication> getAdjudication() {
            return adjudication;
        }

        /**
         * <p>
         * The second-tier service adjudications for payor added services.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Detail}.
         */
        public List<Detail> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !itemSequence.isEmpty() || 
                !detailSequence.isEmpty() || 
                !subdetailSequence.isEmpty() || 
                !provider.isEmpty() || 
                (productOrService != null) || 
                !modifier.isEmpty() || 
                !programCode.isEmpty() || 
                (serviced != null) || 
                (location != null) || 
                (quantity != null) || 
                (unitPrice != null) || 
                (factor != null) || 
                (net != null) || 
                (bodySite != null) || 
                !subSite.isEmpty() || 
                !noteNumber.isEmpty() || 
                !adjudication.isEmpty() || 
                !detail.isEmpty();
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
                    accept(itemSequence, "itemSequence", visitor, PositiveInt.class);
                    accept(detailSequence, "detailSequence", visitor, PositiveInt.class);
                    accept(subdetailSequence, "subdetailSequence", visitor, PositiveInt.class);
                    accept(provider, "provider", visitor, Reference.class);
                    accept(productOrService, "productOrService", visitor);
                    accept(modifier, "modifier", visitor, CodeableConcept.class);
                    accept(programCode, "programCode", visitor, CodeableConcept.class);
                    accept(serviced, "serviced", visitor);
                    accept(location, "location", visitor);
                    accept(quantity, "quantity", visitor);
                    accept(unitPrice, "unitPrice", visitor);
                    accept(factor, "factor", visitor);
                    accept(net, "net", visitor);
                    accept(bodySite, "bodySite", visitor);
                    accept(subSite, "subSite", visitor, CodeableConcept.class);
                    accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                    accept(adjudication, "adjudication", visitor, ClaimResponse.Item.Adjudication.class);
                    accept(detail, "detail", visitor, Detail.class);
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
            AddItem other = (AddItem) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(itemSequence, other.itemSequence) && 
                Objects.equals(detailSequence, other.detailSequence) && 
                Objects.equals(subdetailSequence, other.subdetailSequence) && 
                Objects.equals(provider, other.provider) && 
                Objects.equals(productOrService, other.productOrService) && 
                Objects.equals(modifier, other.modifier) && 
                Objects.equals(programCode, other.programCode) && 
                Objects.equals(serviced, other.serviced) && 
                Objects.equals(location, other.location) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(unitPrice, other.unitPrice) && 
                Objects.equals(factor, other.factor) && 
                Objects.equals(net, other.net) && 
                Objects.equals(bodySite, other.bodySite) && 
                Objects.equals(subSite, other.subSite) && 
                Objects.equals(noteNumber, other.noteNumber) && 
                Objects.equals(adjudication, other.adjudication) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    itemSequence, 
                    detailSequence, 
                    subdetailSequence, 
                    provider, 
                    productOrService, 
                    modifier, 
                    programCode, 
                    serviced, 
                    location, 
                    quantity, 
                    unitPrice, 
                    factor, 
                    net, 
                    bodySite, 
                    subSite, 
                    noteNumber, 
                    adjudication, 
                    detail);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(productOrService, adjudication).from(this);
        }

        public Builder toBuilder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
            return new Builder(productOrService, adjudication).from(this);
        }

        public static Builder builder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
            return new Builder(productOrService, adjudication);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept productOrService;
            private final List<ClaimResponse.Item.Adjudication> adjudication;

            // optional
            private List<PositiveInt> itemSequence = new ArrayList<>();
            private List<PositiveInt> detailSequence = new ArrayList<>();
            private List<PositiveInt> subdetailSequence = new ArrayList<>();
            private List<Reference> provider = new ArrayList<>();
            private List<CodeableConcept> modifier = new ArrayList<>();
            private List<CodeableConcept> programCode = new ArrayList<>();
            private Element serviced;
            private Element location;
            private Quantity quantity;
            private Money unitPrice;
            private Decimal factor;
            private Money net;
            private CodeableConcept bodySite;
            private List<CodeableConcept> subSite = new ArrayList<>();
            private List<PositiveInt> noteNumber = new ArrayList<>();
            private List<Detail> detail = new ArrayList<>();

            private Builder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
                super();
                this.productOrService = productOrService;
                this.adjudication = new ArrayList<>(adjudication);
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
             * Claim items which this service line is intended to replace.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder itemSequence(PositiveInt... itemSequence) {
                for (PositiveInt value : itemSequence) {
                    this.itemSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Claim items which this service line is intended to replace.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder itemSequence(Collection<PositiveInt> itemSequence) {
                this.itemSequence = new ArrayList<>(itemSequence);
                return this;
            }

            /**
             * <p>
             * The sequence number of the details within the claim item which this line is intended to replace.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detailSequence(PositiveInt... detailSequence) {
                for (PositiveInt value : detailSequence) {
                    this.detailSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The sequence number of the details within the claim item which this line is intended to replace.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detailSequence(Collection<PositiveInt> detailSequence) {
                this.detailSequence = new ArrayList<>(detailSequence);
                return this;
            }

            /**
             * <p>
             * The sequence number of the sub-details within the details within the claim item which this line is intended to replace.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param subdetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subdetailSequence(PositiveInt... subdetailSequence) {
                for (PositiveInt value : subdetailSequence) {
                    this.subdetailSequence.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The sequence number of the sub-details within the details within the claim item which this line is intended to replace.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param subdetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subdetailSequence(Collection<PositiveInt> subdetailSequence) {
                this.subdetailSequence = new ArrayList<>(subdetailSequence);
                return this;
            }

            /**
             * <p>
             * The providers who are authorized for the services rendered to the patient.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provider(Reference... provider) {
                for (Reference value : provider) {
                    this.provider.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The providers who are authorized for the services rendered to the patient.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provider(Collection<Reference> provider) {
                this.provider = new ArrayList<>(provider);
                return this;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param modifier
             *     Service/Product billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(CodeableConcept... modifier) {
                for (CodeableConcept value : modifier) {
                    this.modifier.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifier
             *     Service/Product billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(Collection<CodeableConcept> modifier) {
                this.modifier = new ArrayList<>(modifier);
                return this;
            }

            /**
             * <p>
             * Identifies the program under which this may be recovered.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder programCode(CodeableConcept... programCode) {
                for (CodeableConcept value : programCode) {
                    this.programCode.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Identifies the program under which this may be recovered.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder programCode(Collection<CodeableConcept> programCode) {
                this.programCode = new ArrayList<>(programCode);
                return this;
            }

            /**
             * <p>
             * The date or dates when the service or product was supplied, performed or completed.
             * </p>
             * 
             * @param serviced
             *     Date or dates of service or product delivery
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder serviced(Element serviced) {
                this.serviced = serviced;
                return this;
            }

            /**
             * <p>
             * Where the product or service was provided.
             * </p>
             * 
             * @param location
             *     Place of service or where product was supplied
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder location(Element location) {
                this.location = location;
                return this;
            }

            /**
             * <p>
             * The number of repetitions of a service or product.
             * </p>
             * 
             * @param quantity
             *     Count of products or services
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(Quantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * <p>
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * </p>
             * 
             * @param unitPrice
             *     Fee, charge or cost per item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unitPrice(Money unitPrice) {
                this.unitPrice = unitPrice;
                return this;
            }

            /**
             * <p>
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * </p>
             * 
             * @param factor
             *     Price scaling factor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder factor(Decimal factor) {
                this.factor = factor;
                return this;
            }

            /**
             * <p>
             * The quantity times the unit price for an additional service or product or charge.
             * </p>
             * 
             * @param net
             *     Total item cost
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder net(Money net) {
                this.net = net;
                return this;
            }

            /**
             * <p>
             * Physical service site on the patient (limb, tooth, etc.).
             * </p>
             * 
             * @param bodySite
             *     Anatomical location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder bodySite(CodeableConcept bodySite) {
                this.bodySite = bodySite;
                return this;
            }

            /**
             * <p>
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subSite(CodeableConcept... subSite) {
                for (CodeableConcept value : subSite) {
                    this.subSite.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subSite(Collection<CodeableConcept> subSite) {
                this.subSite = new ArrayList<>(subSite);
                return this;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder noteNumber(PositiveInt... noteNumber) {
                for (PositiveInt value : noteNumber) {
                    this.noteNumber.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber = new ArrayList<>(noteNumber);
                return this;
            }

            /**
             * <p>
             * The second-tier service adjudications for payor added services.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param detail
             *     Insurer added line details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Detail... detail) {
                for (Detail value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The second-tier service adjudications for payor added services.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param detail
             *     Insurer added line details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            @Override
            public AddItem build() {
                return new AddItem(this);
            }

            private Builder from(AddItem addItem) {
                id = addItem.id;
                extension.addAll(addItem.extension);
                modifierExtension.addAll(addItem.modifierExtension);
                itemSequence.addAll(addItem.itemSequence);
                detailSequence.addAll(addItem.detailSequence);
                subdetailSequence.addAll(addItem.subdetailSequence);
                provider.addAll(addItem.provider);
                modifier.addAll(addItem.modifier);
                programCode.addAll(addItem.programCode);
                serviced = addItem.serviced;
                location = addItem.location;
                quantity = addItem.quantity;
                unitPrice = addItem.unitPrice;
                factor = addItem.factor;
                net = addItem.net;
                bodySite = addItem.bodySite;
                subSite.addAll(addItem.subSite);
                noteNumber.addAll(addItem.noteNumber);
                detail.addAll(addItem.detail);
                return this;
            }
        }

        /**
         * <p>
         * The second-tier service adjudications for payor added services.
         * </p>
         */
        public static class Detail extends BackboneElement {
            private final CodeableConcept productOrService;
            private final List<CodeableConcept> modifier;
            private final Quantity quantity;
            private final Money unitPrice;
            private final Decimal factor;
            private final Money net;
            private final List<PositiveInt> noteNumber;
            private final List<ClaimResponse.Item.Adjudication> adjudication;
            private final List<SubDetail> subDetail;

            private volatile int hashCode;

            private Detail(Builder builder) {
                super(builder);
                productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                modifier = Collections.unmodifiableList(builder.modifier);
                quantity = builder.quantity;
                unitPrice = builder.unitPrice;
                factor = builder.factor;
                net = builder.net;
                noteNumber = Collections.unmodifiableList(builder.noteNumber);
                adjudication = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication"));
                subDetail = Collections.unmodifiableList(builder.subDetail);
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
             * product, service, drug or other billing code for the item.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getProductOrService() {
                return productOrService;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getModifier() {
                return modifier;
            }

            /**
             * <p>
             * The number of repetitions of a service or product.
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
             * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
             * for the details of the group.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getUnitPrice() {
                return unitPrice;
            }

            /**
             * <p>
             * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
             * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * <p>
             * The quantity times the unit price for an additional service or product or charge.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getNet() {
                return net;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
             */
            public List<PositiveInt> getNoteNumber() {
                return noteNumber;
            }

            /**
             * <p>
             * The adjudication results.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Adjudication}.
             */
            public List<ClaimResponse.Item.Adjudication> getAdjudication() {
                return adjudication;
            }

            /**
             * <p>
             * The third-tier service adjudications for payor added services.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link SubDetail}.
             */
            public List<SubDetail> getSubDetail() {
                return subDetail;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (productOrService != null) || 
                    !modifier.isEmpty() || 
                    (quantity != null) || 
                    (unitPrice != null) || 
                    (factor != null) || 
                    (net != null) || 
                    !noteNumber.isEmpty() || 
                    !adjudication.isEmpty() || 
                    !subDetail.isEmpty();
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
                        accept(productOrService, "productOrService", visitor);
                        accept(modifier, "modifier", visitor, CodeableConcept.class);
                        accept(quantity, "quantity", visitor);
                        accept(unitPrice, "unitPrice", visitor);
                        accept(factor, "factor", visitor);
                        accept(net, "net", visitor);
                        accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                        accept(adjudication, "adjudication", visitor, ClaimResponse.Item.Adjudication.class);
                        accept(subDetail, "subDetail", visitor, SubDetail.class);
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
                Detail other = (Detail) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(productOrService, other.productOrService) && 
                    Objects.equals(modifier, other.modifier) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(unitPrice, other.unitPrice) && 
                    Objects.equals(factor, other.factor) && 
                    Objects.equals(net, other.net) && 
                    Objects.equals(noteNumber, other.noteNumber) && 
                    Objects.equals(adjudication, other.adjudication) && 
                    Objects.equals(subDetail, other.subDetail);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        productOrService, 
                        modifier, 
                        quantity, 
                        unitPrice, 
                        factor, 
                        net, 
                        noteNumber, 
                        adjudication, 
                        subDetail);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(productOrService, adjudication).from(this);
            }

            public Builder toBuilder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
                return new Builder(productOrService, adjudication).from(this);
            }

            public static Builder builder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
                return new Builder(productOrService, adjudication);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept productOrService;
                private final List<ClaimResponse.Item.Adjudication> adjudication;

                // optional
                private List<CodeableConcept> modifier = new ArrayList<>();
                private Quantity quantity;
                private Money unitPrice;
                private Decimal factor;
                private Money net;
                private List<PositiveInt> noteNumber = new ArrayList<>();
                private List<SubDetail> subDetail = new ArrayList<>();

                private Builder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
                    super();
                    this.productOrService = productOrService;
                    this.adjudication = new ArrayList<>(adjudication);
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
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder modifier(CodeableConcept... modifier) {
                    for (CodeableConcept value : modifier) {
                        this.modifier.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder modifier(Collection<CodeableConcept> modifier) {
                    this.modifier = new ArrayList<>(modifier);
                    return this;
                }

                /**
                 * <p>
                 * The number of repetitions of a service or product.
                 * </p>
                 * 
                 * @param quantity
                 *     Count of products or services
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(Quantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * <p>
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * </p>
                 * 
                 * @param unitPrice
                 *     Fee, charge or cost per item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder unitPrice(Money unitPrice) {
                    this.unitPrice = unitPrice;
                    return this;
                }

                /**
                 * <p>
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * </p>
                 * 
                 * @param factor
                 *     Price scaling factor
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder factor(Decimal factor) {
                    this.factor = factor;
                    return this;
                }

                /**
                 * <p>
                 * The quantity times the unit price for an additional service or product or charge.
                 * </p>
                 * 
                 * @param net
                 *     Total item cost
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder net(Money net) {
                    this.net = net;
                    return this;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder noteNumber(PositiveInt... noteNumber) {
                    for (PositiveInt value : noteNumber) {
                        this.noteNumber.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber = new ArrayList<>(noteNumber);
                    return this;
                }

                /**
                 * <p>
                 * The third-tier service adjudications for payor added services.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param subDetail
                 *     Insurer added line items
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subDetail(SubDetail... subDetail) {
                    for (SubDetail value : subDetail) {
                        this.subDetail.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The third-tier service adjudications for payor added services.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param subDetail
                 *     Insurer added line items
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder subDetail(Collection<SubDetail> subDetail) {
                    this.subDetail = new ArrayList<>(subDetail);
                    return this;
                }

                @Override
                public Detail build() {
                    return new Detail(this);
                }

                private Builder from(Detail detail) {
                    id = detail.id;
                    extension.addAll(detail.extension);
                    modifierExtension.addAll(detail.modifierExtension);
                    modifier.addAll(detail.modifier);
                    quantity = detail.quantity;
                    unitPrice = detail.unitPrice;
                    factor = detail.factor;
                    net = detail.net;
                    noteNumber.addAll(detail.noteNumber);
                    subDetail.addAll(detail.subDetail);
                    return this;
                }
            }

            /**
             * <p>
             * The third-tier service adjudications for payor added services.
             * </p>
             */
            public static class SubDetail extends BackboneElement {
                private final CodeableConcept productOrService;
                private final List<CodeableConcept> modifier;
                private final Quantity quantity;
                private final Money unitPrice;
                private final Decimal factor;
                private final Money net;
                private final List<PositiveInt> noteNumber;
                private final List<ClaimResponse.Item.Adjudication> adjudication;

                private volatile int hashCode;

                private SubDetail(Builder builder) {
                    super(builder);
                    productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                    modifier = Collections.unmodifiableList(builder.modifier);
                    quantity = builder.quantity;
                    unitPrice = builder.unitPrice;
                    factor = builder.factor;
                    net = builder.net;
                    noteNumber = Collections.unmodifiableList(builder.noteNumber);
                    adjudication = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication"));
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * <p>
                 * When the value is a group code then this item collects a set of related claim details, otherwise this contains the 
                 * product, service, drug or other billing code for the item.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getProductOrService() {
                    return productOrService;
                }

                /**
                 * <p>
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
                 */
                public List<CodeableConcept> getModifier() {
                    return modifier;
                }

                /**
                 * <p>
                 * The number of repetitions of a service or product.
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
                 * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                 * for the details of the group.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Money}.
                 */
                public Money getUnitPrice() {
                    return unitPrice;
                }

                /**
                 * <p>
                 * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                 * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Decimal}.
                 */
                public Decimal getFactor() {
                    return factor;
                }

                /**
                 * <p>
                 * The quantity times the unit price for an additional service or product or charge.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Money}.
                 */
                public Money getNet() {
                    return net;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link PositiveInt}.
                 */
                public List<PositiveInt> getNoteNumber() {
                    return noteNumber;
                }

                /**
                 * <p>
                 * The adjudication results.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Adjudication}.
                 */
                public List<ClaimResponse.Item.Adjudication> getAdjudication() {
                    return adjudication;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (productOrService != null) || 
                        !modifier.isEmpty() || 
                        (quantity != null) || 
                        (unitPrice != null) || 
                        (factor != null) || 
                        (net != null) || 
                        !noteNumber.isEmpty() || 
                        !adjudication.isEmpty();
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
                            accept(productOrService, "productOrService", visitor);
                            accept(modifier, "modifier", visitor, CodeableConcept.class);
                            accept(quantity, "quantity", visitor);
                            accept(unitPrice, "unitPrice", visitor);
                            accept(factor, "factor", visitor);
                            accept(net, "net", visitor);
                            accept(noteNumber, "noteNumber", visitor, PositiveInt.class);
                            accept(adjudication, "adjudication", visitor, ClaimResponse.Item.Adjudication.class);
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
                    SubDetail other = (SubDetail) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(productOrService, other.productOrService) && 
                        Objects.equals(modifier, other.modifier) && 
                        Objects.equals(quantity, other.quantity) && 
                        Objects.equals(unitPrice, other.unitPrice) && 
                        Objects.equals(factor, other.factor) && 
                        Objects.equals(net, other.net) && 
                        Objects.equals(noteNumber, other.noteNumber) && 
                        Objects.equals(adjudication, other.adjudication);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            productOrService, 
                            modifier, 
                            quantity, 
                            unitPrice, 
                            factor, 
                            net, 
                            noteNumber, 
                            adjudication);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(productOrService, adjudication).from(this);
                }

                public Builder toBuilder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
                    return new Builder(productOrService, adjudication).from(this);
                }

                public static Builder builder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
                    return new Builder(productOrService, adjudication);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final CodeableConcept productOrService;
                    private final List<ClaimResponse.Item.Adjudication> adjudication;

                    // optional
                    private List<CodeableConcept> modifier = new ArrayList<>();
                    private Quantity quantity;
                    private Money unitPrice;
                    private Decimal factor;
                    private Money net;
                    private List<PositiveInt> noteNumber = new ArrayList<>();

                    private Builder(CodeableConcept productOrService, List<ClaimResponse.Item.Adjudication> adjudication) {
                        super();
                        this.productOrService = productOrService;
                        this.adjudication = new ArrayList<>(adjudication);
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
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder modifier(CodeableConcept... modifier) {
                        for (CodeableConcept value : modifier) {
                            this.modifier.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder modifier(Collection<CodeableConcept> modifier) {
                        this.modifier = new ArrayList<>(modifier);
                        return this;
                    }

                    /**
                     * <p>
                     * The number of repetitions of a service or product.
                     * </p>
                     * 
                     * @param quantity
                     *     Count of products or services
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder quantity(Quantity quantity) {
                        this.quantity = quantity;
                        return this;
                    }

                    /**
                     * <p>
                     * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees 
                     * for the details of the group.
                     * </p>
                     * 
                     * @param unitPrice
                     *     Fee, charge or cost per item
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder unitPrice(Money unitPrice) {
                        this.unitPrice = unitPrice;
                        return this;
                    }

                    /**
                     * <p>
                     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods 
                     * received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
                     * </p>
                     * 
                     * @param factor
                     *     Price scaling factor
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder factor(Decimal factor) {
                        this.factor = factor;
                        return this;
                    }

                    /**
                     * <p>
                     * The quantity times the unit price for an additional service or product or charge.
                     * </p>
                     * 
                     * @param net
                     *     Total item cost
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder net(Money net) {
                        this.net = net;
                        return this;
                    }

                    /**
                     * <p>
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder noteNumber(PositiveInt... noteNumber) {
                        for (PositiveInt value : noteNumber) {
                            this.noteNumber.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber = new ArrayList<>(noteNumber);
                        return this;
                    }

                    @Override
                    public SubDetail build() {
                        return new SubDetail(this);
                    }

                    private Builder from(SubDetail subDetail) {
                        id = subDetail.id;
                        extension.addAll(subDetail.extension);
                        modifierExtension.addAll(subDetail.modifierExtension);
                        modifier.addAll(subDetail.modifier);
                        quantity = subDetail.quantity;
                        unitPrice = subDetail.unitPrice;
                        factor = subDetail.factor;
                        net = subDetail.net;
                        noteNumber.addAll(subDetail.noteNumber);
                        return this;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Categorized monetary totals for the adjudication.
     * </p>
     */
    public static class Total extends BackboneElement {
        private final CodeableConcept category;
        private final Money amount;

        private volatile int hashCode;

        private Total(Builder builder) {
            super(builder);
            category = ValidationSupport.requireNonNull(builder.category, "category");
            amount = ValidationSupport.requireNonNull(builder.amount, "amount");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A code to indicate the information type of this adjudication record. Information types may include: the value 
         * submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible 
         * for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * <p>
         * Monetary total amount associated with the category.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (category != null) || 
                (amount != null);
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
                    accept(category, "category", visitor);
                    accept(amount, "amount", visitor);
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
            Total other = (Total) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(category, other.category) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    category, 
                    amount);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(category, amount).from(this);
        }

        public Builder toBuilder(CodeableConcept category, Money amount) {
            return new Builder(category, amount).from(this);
        }

        public static Builder builder(CodeableConcept category, Money amount) {
            return new Builder(category, amount);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept category;
            private final Money amount;

            private Builder(CodeableConcept category, Money amount) {
                super();
                this.category = category;
                this.amount = amount;
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
            public Total build() {
                return new Total(this);
            }

            private Builder from(Total total) {
                id = total.id;
                extension.addAll(total.extension);
                modifierExtension.addAll(total.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * Payment details for the adjudication of the claim.
     * </p>
     */
    public static class Payment extends BackboneElement {
        private final CodeableConcept type;
        private final Money adjustment;
        private final CodeableConcept adjustmentReason;
        private final Date date;
        private final Money amount;
        private final Identifier identifier;

        private volatile int hashCode;

        private Payment(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            adjustment = builder.adjustment;
            adjustmentReason = builder.adjustmentReason;
            date = builder.date;
            amount = ValidationSupport.requireNonNull(builder.amount, "amount");
            identifier = builder.identifier;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Whether this represents partial or complete payment of the benefits payable.
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
         * Total amount of all adjustments to this payment included in this transaction which are not related to this claim's 
         * adjudication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getAdjustment() {
            return adjustment;
        }

        /**
         * <p>
         * Reason for the payment adjustment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getAdjustmentReason() {
            return adjustmentReason;
        }

        /**
         * <p>
         * Estimated date the payment will be issued or the actual issue date of payment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Date}.
         */
        public Date getDate() {
            return date;
        }

        /**
         * <p>
         * Benefits payable less any payment adjustment.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Money}.
         */
        public Money getAmount() {
            return amount;
        }

        /**
         * <p>
         * Issuer's unique identifier for the payment instrument.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (adjustment != null) || 
                (adjustmentReason != null) || 
                (date != null) || 
                (amount != null) || 
                (identifier != null);
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
                    accept(type, "type", visitor);
                    accept(adjustment, "adjustment", visitor);
                    accept(adjustmentReason, "adjustmentReason", visitor);
                    accept(date, "date", visitor);
                    accept(amount, "amount", visitor);
                    accept(identifier, "identifier", visitor);
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
            Payment other = (Payment) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(adjustment, other.adjustment) && 
                Objects.equals(adjustmentReason, other.adjustmentReason) && 
                Objects.equals(date, other.date) && 
                Objects.equals(amount, other.amount) && 
                Objects.equals(identifier, other.identifier);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    adjustment, 
                    adjustmentReason, 
                    date, 
                    amount, 
                    identifier);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type, amount).from(this);
        }

        public Builder toBuilder(CodeableConcept type, Money amount) {
            return new Builder(type, amount).from(this);
        }

        public static Builder builder(CodeableConcept type, Money amount) {
            return new Builder(type, amount);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final Money amount;

            // optional
            private Money adjustment;
            private CodeableConcept adjustmentReason;
            private Date date;
            private Identifier identifier;

            private Builder(CodeableConcept type, Money amount) {
                super();
                this.type = type;
                this.amount = amount;
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
             * Total amount of all adjustments to this payment included in this transaction which are not related to this claim's 
             * adjudication.
             * </p>
             * 
             * @param adjustment
             *     Payment adjustment for non-claim issues
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder adjustment(Money adjustment) {
                this.adjustment = adjustment;
                return this;
            }

            /**
             * <p>
             * Reason for the payment adjustment.
             * </p>
             * 
             * @param adjustmentReason
             *     Explanation for the adjustment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder adjustmentReason(CodeableConcept adjustmentReason) {
                this.adjustmentReason = adjustmentReason;
                return this;
            }

            /**
             * <p>
             * Estimated date the payment will be issued or the actual issue date of payment.
             * </p>
             * 
             * @param date
             *     Expected date of payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(Date date) {
                this.date = date;
                return this;
            }

            /**
             * <p>
             * Issuer's unique identifier for the payment instrument.
             * </p>
             * 
             * @param identifier
             *     Business identifier for the payment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            @Override
            public Payment build() {
                return new Payment(this);
            }

            private Builder from(Payment payment) {
                id = payment.id;
                extension.addAll(payment.extension);
                modifierExtension.addAll(payment.modifierExtension);
                adjustment = payment.adjustment;
                adjustmentReason = payment.adjustmentReason;
                date = payment.date;
                identifier = payment.identifier;
                return this;
            }
        }
    }

    /**
     * <p>
     * A note that describes or explains adjudication results in a human readable form.
     * </p>
     */
    public static class ProcessNote extends BackboneElement {
        private final PositiveInt number;
        private final NoteType type;
        private final String text;
        private final CodeableConcept language;

        private volatile int hashCode;

        private ProcessNote(Builder builder) {
            super(builder);
            number = builder.number;
            type = builder.type;
            text = ValidationSupport.requireNonNull(builder.text, "text");
            language = builder.language;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A number to uniquely identify a note entry.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getNumber() {
            return number;
        }

        /**
         * <p>
         * The business purpose of the note text.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link NoteType}.
         */
        public NoteType getType() {
            return type;
        }

        /**
         * <p>
         * The explanation or description associated with the processing.
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
         * A code to define the language used in the text of the note.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getLanguage() {
            return language;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (number != null) || 
                (type != null) || 
                (text != null) || 
                (language != null);
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
                    accept(number, "number", visitor);
                    accept(type, "type", visitor);
                    accept(text, "text", visitor);
                    accept(language, "language", visitor);
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
            ProcessNote other = (ProcessNote) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(number, other.number) && 
                Objects.equals(type, other.type) && 
                Objects.equals(text, other.text) && 
                Objects.equals(language, other.language);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    number, 
                    type, 
                    text, 
                    language);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(text).from(this);
        }

        public Builder toBuilder(String text) {
            return new Builder(text).from(this);
        }

        public static Builder builder(String text) {
            return new Builder(text);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String text;

            // optional
            private PositiveInt number;
            private NoteType type;
            private CodeableConcept language;

            private Builder(String text) {
                super();
                this.text = text;
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
             * A number to uniquely identify a note entry.
             * </p>
             * 
             * @param number
             *     Note instance identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder number(PositiveInt number) {
                this.number = number;
                return this;
            }

            /**
             * <p>
             * The business purpose of the note text.
             * </p>
             * 
             * @param type
             *     display | print | printoper
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(NoteType type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * A code to define the language used in the text of the note.
             * </p>
             * 
             * @param language
             *     Language of the text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(CodeableConcept language) {
                this.language = language;
                return this;
            }

            @Override
            public ProcessNote build() {
                return new ProcessNote(this);
            }

            private Builder from(ProcessNote processNote) {
                id = processNote.id;
                extension.addAll(processNote.extension);
                modifierExtension.addAll(processNote.modifierExtension);
                number = processNote.number;
                type = processNote.type;
                language = processNote.language;
                return this;
            }
        }
    }

    /**
     * <p>
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     * </p>
     */
    public static class Insurance extends BackboneElement {
        private final PositiveInt sequence;
        private final Boolean focal;
        private final Reference coverage;
        private final String businessArrangement;
        private final Reference claimResponse;

        private volatile int hashCode;

        private Insurance(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            focal = ValidationSupport.requireNonNull(builder.focal, "focal");
            coverage = ValidationSupport.requireNonNull(builder.coverage, "coverage");
            businessArrangement = builder.businessArrangement;
            claimResponse = builder.claimResponse;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit 
         * order.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * <p>
         * A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getFocal() {
            return focal;
        }

        /**
         * <p>
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
         * will use these details to locate the patient's actual coverage within the insurer's information system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getCoverage() {
            return coverage;
        }

        /**
         * <p>
         * A business agreement number established between the provider and the insurer for special business processing purposes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getBusinessArrangement() {
            return businessArrangement;
        }

        /**
         * <p>
         * The result of the adjudication of the line items for the Coverage specified in this insurance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getClaimResponse() {
            return claimResponse;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                (focal != null) || 
                (coverage != null) || 
                (businessArrangement != null) || 
                (claimResponse != null);
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
                    accept(sequence, "sequence", visitor);
                    accept(focal, "focal", visitor);
                    accept(coverage, "coverage", visitor);
                    accept(businessArrangement, "businessArrangement", visitor);
                    accept(claimResponse, "claimResponse", visitor);
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
            Insurance other = (Insurance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(focal, other.focal) && 
                Objects.equals(coverage, other.coverage) && 
                Objects.equals(businessArrangement, other.businessArrangement) && 
                Objects.equals(claimResponse, other.claimResponse);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    focal, 
                    coverage, 
                    businessArrangement, 
                    claimResponse);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(sequence, focal, coverage).from(this);
        }

        public Builder toBuilder(PositiveInt sequence, Boolean focal, Reference coverage) {
            return new Builder(sequence, focal, coverage).from(this);
        }

        public static Builder builder(PositiveInt sequence, Boolean focal, Reference coverage) {
            return new Builder(sequence, focal, coverage);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final PositiveInt sequence;
            private final Boolean focal;
            private final Reference coverage;

            // optional
            private String businessArrangement;
            private Reference claimResponse;

            private Builder(PositiveInt sequence, Boolean focal, Reference coverage) {
                super();
                this.sequence = sequence;
                this.focal = focal;
                this.coverage = coverage;
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
             * A business agreement number established between the provider and the insurer for special business processing purposes.
             * </p>
             * 
             * @param businessArrangement
             *     Additional provider contract number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder businessArrangement(String businessArrangement) {
                this.businessArrangement = businessArrangement;
                return this;
            }

            /**
             * <p>
             * The result of the adjudication of the line items for the Coverage specified in this insurance.
             * </p>
             * 
             * @param claimResponse
             *     Adjudication results
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder claimResponse(Reference claimResponse) {
                this.claimResponse = claimResponse;
                return this;
            }

            @Override
            public Insurance build() {
                return new Insurance(this);
            }

            private Builder from(Insurance insurance) {
                id = insurance.id;
                extension.addAll(insurance.extension);
                modifierExtension.addAll(insurance.modifierExtension);
                businessArrangement = insurance.businessArrangement;
                claimResponse = insurance.claimResponse;
                return this;
            }
        }
    }

    /**
     * <p>
     * Errors encountered during the processing of the adjudication.
     * </p>
     */
    public static class Error extends BackboneElement {
        private final PositiveInt itemSequence;
        private final PositiveInt detailSequence;
        private final PositiveInt subDetailSequence;
        private final CodeableConcept code;

        private volatile int hashCode;

        private Error(Builder builder) {
            super(builder);
            itemSequence = builder.itemSequence;
            detailSequence = builder.detailSequence;
            subDetailSequence = builder.subDetailSequence;
            code = ValidationSupport.requireNonNull(builder.code, "code");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs 
         * outside of the item structure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getItemSequence() {
            return itemSequence;
        }

        /**
         * <p>
         * The sequence number of the detail within the line item submitted which contains the error. This value is omitted when 
         * the error occurs outside of the item structure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getDetailSequence() {
            return detailSequence;
        }

        /**
         * <p>
         * The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This 
         * value is omitted when the error occurs outside of the item structure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getSubDetailSequence() {
            return subDetailSequence;
        }

        /**
         * <p>
         * An error code, from a specified code system, which details why the claim could not be adjudicated.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCode() {
            return code;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (itemSequence != null) || 
                (detailSequence != null) || 
                (subDetailSequence != null) || 
                (code != null);
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
                    accept(itemSequence, "itemSequence", visitor);
                    accept(detailSequence, "detailSequence", visitor);
                    accept(subDetailSequence, "subDetailSequence", visitor);
                    accept(code, "code", visitor);
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
            Error other = (Error) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(itemSequence, other.itemSequence) && 
                Objects.equals(detailSequence, other.detailSequence) && 
                Objects.equals(subDetailSequence, other.subDetailSequence) && 
                Objects.equals(code, other.code);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    itemSequence, 
                    detailSequence, 
                    subDetailSequence, 
                    code);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(code).from(this);
        }

        public Builder toBuilder(CodeableConcept code) {
            return new Builder(code).from(this);
        }

        public static Builder builder(CodeableConcept code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;

            // optional
            private PositiveInt itemSequence;
            private PositiveInt detailSequence;
            private PositiveInt subDetailSequence;

            private Builder(CodeableConcept code) {
                super();
                this.code = code;
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
             * The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs 
             * outside of the item structure.
             * </p>
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder itemSequence(PositiveInt itemSequence) {
                this.itemSequence = itemSequence;
                return this;
            }

            /**
             * <p>
             * The sequence number of the detail within the line item submitted which contains the error. This value is omitted when 
             * the error occurs outside of the item structure.
             * </p>
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detailSequence(PositiveInt detailSequence) {
                this.detailSequence = detailSequence;
                return this;
            }

            /**
             * <p>
             * The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This 
             * value is omitted when the error occurs outside of the item structure.
             * </p>
             * 
             * @param subDetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subDetailSequence(PositiveInt subDetailSequence) {
                this.subDetailSequence = subDetailSequence;
                return this;
            }

            @Override
            public Error build() {
                return new Error(this);
            }

            private Builder from(Error error) {
                id = error.id;
                extension.addAll(error.extension);
                modifierExtension.addAll(error.modifierExtension);
                itemSequence = error.itemSequence;
                detailSequence = error.detailSequence;
                subDetailSequence = error.subDetailSequence;
                return this;
            }
        }
    }
}
