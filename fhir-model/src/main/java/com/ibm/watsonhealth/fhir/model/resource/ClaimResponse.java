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

    private ClaimResponse(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.type = ValidationSupport.requireNonNull(builder.type, "type");
        this.subType = builder.subType;
        this.use = ValidationSupport.requireNonNull(builder.use, "use");
        this.patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        this.created = ValidationSupport.requireNonNull(builder.created, "created");
        this.insurer = ValidationSupport.requireNonNull(builder.insurer, "insurer");
        this.requestor = builder.requestor;
        this.request = builder.request;
        this.outcome = ValidationSupport.requireNonNull(builder.outcome, "outcome");
        this.disposition = builder.disposition;
        this.preAuthRef = builder.preAuthRef;
        this.preAuthPeriod = builder.preAuthPeriod;
        this.payeeType = builder.payeeType;
        this.item = builder.item;
        this.addItem = builder.addItem;
        this.adjudication = builder.adjudication;
        this.total = builder.total;
        this.payment = builder.payment;
        this.fundsReserve = builder.fundsReserve;
        this.formCode = builder.formCode;
        this.form = builder.form;
        this.processNote = builder.processNote;
        this.communicationRequest = builder.communicationRequest;
        this.insurance = builder.insurance;
        this.error = builder.error;
    }

    /**
     * <p>
     * A unique identifier assigned to this claim response.
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
     *     A list containing immutable objects of type {@link Item}.
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
     *     A list containing immutable objects of type {@link AddItem}.
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
     *     A list containing immutable objects of type {@link Adjudication}.
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
     *     A list containing immutable objects of type {@link Total}.
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
     *     A list containing immutable objects of type {@link ProcessNote}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
     *     A list containing immutable objects of type {@link Insurance}.
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
     *     A list containing immutable objects of type {@link Error}.
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
    public Builder toBuilder() {
        Builder builder = new Builder(status, type, use, patient, created, insurer, outcome);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.subType = subType;
        builder.requestor = requestor;
        builder.request = request;
        builder.disposition = disposition;
        builder.preAuthRef = preAuthRef;
        builder.preAuthPeriod = preAuthPeriod;
        builder.payeeType = payeeType;
        builder.item.addAll(item);
        builder.addItem.addAll(addItem);
        builder.adjudication.addAll(adjudication);
        builder.total.addAll(total);
        builder.payment = payment;
        builder.fundsReserve = fundsReserve;
        builder.formCode = formCode;
        builder.form = form;
        builder.processNote.addAll(processNote);
        builder.communicationRequest.addAll(communicationRequest);
        builder.insurance.addAll(insurance);
        builder.error.addAll(error);
        return builder;
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
         * A unique identifier assigned to this claim response.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for a claim response
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
         * A unique identifier assigned to this claim response.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for a claim response
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
         * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient 
         * and/or a specialty service.
         * </p>
         * 
         * @param subType
         *     More granular claim type
         * 
         * @return
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param item
         *     Adjudication for claim line items
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param item
         *     Adjudication for claim line items
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder item(Collection<Item> item) {
            this.item.addAll(item);
            return this;
        }

        /**
         * <p>
         * The first-tier service adjudications for payor added product or service lines.
         * </p>
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param addItem
         *     Insurer added line items
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder addItem(Collection<AddItem> addItem) {
            this.addItem.addAll(addItem);
            return this;
        }

        /**
         * <p>
         * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
         * </p>
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param adjudication
         *     Header-level adjudication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder adjudication(Collection<ClaimResponse.Item.Adjudication> adjudication) {
            this.adjudication.addAll(adjudication);
            return this;
        }

        /**
         * <p>
         * Categorized monetary totals for the adjudication.
         * </p>
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param total
         *     Adjudication totals
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder total(Collection<Total> total) {
            this.total.addAll(total);
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
         */
        public Builder form(Attachment form) {
            this.form = form;
            return this;
        }

        /**
         * <p>
         * A note that describes or explains adjudication results in a human readable form.
         * </p>
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param processNote
         *     Note concerning adjudication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder processNote(Collection<ProcessNote> processNote) {
            this.processNote.addAll(processNote);
            return this;
        }

        /**
         * <p>
         * Request for additional supporting or authorizing information.
         * </p>
         * 
         * @param communicationRequest
         *     Request for additional information
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param communicationRequest
         *     Request for additional information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder communicationRequest(Collection<Reference> communicationRequest) {
            this.communicationRequest.addAll(communicationRequest);
            return this;
        }

        /**
         * <p>
         * Financial instruments for reimbursement for the health care products and services specified on the claim.
         * </p>
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurance(Collection<Insurance> insurance) {
            this.insurance.addAll(insurance);
            return this;
        }

        /**
         * <p>
         * Errors encountered during the processing of the adjudication.
         * </p>
         * 
         * @param error
         *     Processing errors
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param error
         *     Processing errors
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder error(Collection<Error> error) {
            this.error.addAll(error);
            return this;
        }

        @Override
        public ClaimResponse build() {
            return new ClaimResponse(this);
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

        private Item(Builder builder) {
            super(builder);
            this.itemSequence = ValidationSupport.requireNonNull(builder.itemSequence, "itemSequence");
            this.noteNumber = builder.noteNumber;
            this.adjudication = ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication");
            this.detail = builder.detail;
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
         *     A list containing immutable objects of type {@link PositiveInt}.
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
         *     A list containing immutable objects of type {@link Adjudication}.
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
         *     A list containing immutable objects of type {@link Detail}.
         */
        public List<Detail> getDetail() {
            return detail;
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
        public Builder toBuilder() {
            return Builder.from(this);
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
                this.adjudication = adjudication;
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
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber.addAll(noteNumber);
                return this;
            }

            /**
             * <p>
             * A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
             * </p>
             * 
             * @param detail
             *     Adjudication for claim details
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param detail
             *     Adjudication for claim details
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail.addAll(detail);
                return this;
            }

            @Override
            public Item build() {
                return new Item(this);
            }

            private static Builder from(Item item) {
                Builder builder = new Builder(item.itemSequence, item.adjudication);
                builder.id = item.id;
                builder.extension.addAll(item.extension);
                builder.modifierExtension.addAll(item.modifierExtension);
                builder.noteNumber.addAll(item.noteNumber);
                builder.detail.addAll(item.detail);
                return builder;
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

            private Adjudication(Builder builder) {
                super(builder);
                this.category = ValidationSupport.requireNonNull(builder.category, "category");
                this.reason = builder.reason;
                this.amount = builder.amount;
                this.value = builder.value;
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
            public Builder toBuilder() {
                return Builder.from(this);
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
                 */
                public Builder value(Decimal value) {
                    this.value = value;
                    return this;
                }

                @Override
                public Adjudication build() {
                    return new Adjudication(this);
                }

                private static Builder from(Adjudication adjudication) {
                    Builder builder = new Builder(adjudication.category);
                    builder.id = adjudication.id;
                    builder.extension.addAll(adjudication.extension);
                    builder.modifierExtension.addAll(adjudication.modifierExtension);
                    builder.reason = adjudication.reason;
                    builder.amount = adjudication.amount;
                    builder.value = adjudication.value;
                    return builder;
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

            private Detail(Builder builder) {
                super(builder);
                this.detailSequence = ValidationSupport.requireNonNull(builder.detailSequence, "detailSequence");
                this.noteNumber = builder.noteNumber;
                this.adjudication = ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication");
                this.subDetail = builder.subDetail;
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
             *     A list containing immutable objects of type {@link PositiveInt}.
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
             *     A list containing immutable objects of type {@link Adjudication}.
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
             *     A list containing immutable objects of type {@link SubDetail}.
             */
            public List<SubDetail> getSubDetail() {
                return subDetail;
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
            public Builder toBuilder() {
                return Builder.from(this);
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
                    this.adjudication = adjudication;
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
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber.addAll(noteNumber);
                    return this;
                }

                /**
                 * <p>
                 * A sub-detail adjudication of a simple product or service.
                 * </p>
                 * 
                 * @param subDetail
                 *     Adjudication for claim sub-details
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param subDetail
                 *     Adjudication for claim sub-details
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder subDetail(Collection<SubDetail> subDetail) {
                    this.subDetail.addAll(subDetail);
                    return this;
                }

                @Override
                public Detail build() {
                    return new Detail(this);
                }

                private static Builder from(Detail detail) {
                    Builder builder = new Builder(detail.detailSequence, detail.adjudication);
                    builder.id = detail.id;
                    builder.extension.addAll(detail.extension);
                    builder.modifierExtension.addAll(detail.modifierExtension);
                    builder.noteNumber.addAll(detail.noteNumber);
                    builder.subDetail.addAll(detail.subDetail);
                    return builder;
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

                private SubDetail(Builder builder) {
                    super(builder);
                    this.subDetailSequence = ValidationSupport.requireNonNull(builder.subDetailSequence, "subDetailSequence");
                    this.noteNumber = builder.noteNumber;
                    this.adjudication = builder.adjudication;
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
                 *     A list containing immutable objects of type {@link PositiveInt}.
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
                 *     A list containing immutable objects of type {@link Adjudication}.
                 */
                public List<ClaimResponse.Item.Adjudication> getAdjudication() {
                    return adjudication;
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
                public Builder toBuilder() {
                    return Builder.from(this);
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
                    public Builder modifierExtension(Collection<Extension> modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * <p>
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber.addAll(noteNumber);
                        return this;
                    }

                    /**
                     * <p>
                     * The adjudication results.
                     * </p>
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     * 
                     * @param adjudication
                     *     Subdetail level adjudication details
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder adjudication(Collection<ClaimResponse.Item.Adjudication> adjudication) {
                        this.adjudication.addAll(adjudication);
                        return this;
                    }

                    @Override
                    public SubDetail build() {
                        return new SubDetail(this);
                    }

                    private static Builder from(SubDetail subDetail) {
                        Builder builder = new Builder(subDetail.subDetailSequence);
                        builder.id = subDetail.id;
                        builder.extension.addAll(subDetail.extension);
                        builder.modifierExtension.addAll(subDetail.modifierExtension);
                        builder.noteNumber.addAll(subDetail.noteNumber);
                        builder.adjudication.addAll(subDetail.adjudication);
                        return builder;
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

        private AddItem(Builder builder) {
            super(builder);
            this.itemSequence = builder.itemSequence;
            this.detailSequence = builder.detailSequence;
            this.subdetailSequence = builder.subdetailSequence;
            this.provider = builder.provider;
            this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
            this.modifier = builder.modifier;
            this.programCode = builder.programCode;
            this.serviced = ValidationSupport.choiceElement(builder.serviced, "serviced", Date.class, Period.class);
            this.location = ValidationSupport.choiceElement(builder.location, "location", CodeableConcept.class, Address.class, Reference.class);
            this.quantity = builder.quantity;
            this.unitPrice = builder.unitPrice;
            this.factor = builder.factor;
            this.net = builder.net;
            this.bodySite = builder.bodySite;
            this.subSite = builder.subSite;
            this.noteNumber = builder.noteNumber;
            this.adjudication = ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication");
            this.detail = builder.detail;
        }

        /**
         * <p>
         * Claim items which this service line is intended to replace.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PositiveInt}.
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
         *     A list containing immutable objects of type {@link PositiveInt}.
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
         *     A list containing immutable objects of type {@link PositiveInt}.
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
         *     A list containing immutable objects of type {@link Reference}.
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
         *     A list containing immutable objects of type {@link CodeableConcept}.
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
         *     A list containing immutable objects of type {@link CodeableConcept}.
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
         *     A list containing immutable objects of type {@link CodeableConcept}.
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
         *     A list containing immutable objects of type {@link PositiveInt}.
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
         *     A list containing immutable objects of type {@link Adjudication}.
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
         *     A list containing immutable objects of type {@link Detail}.
         */
        public List<Detail> getDetail() {
            return detail;
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
                    accept(serviced, "serviced", visitor, true);
                    accept(location, "location", visitor, true);
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
        public Builder toBuilder() {
            return Builder.from(this);
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
                this.adjudication = adjudication;
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
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Claim items which this service line is intended to replace.
             * </p>
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param itemSequence
             *     Item sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder itemSequence(Collection<PositiveInt> itemSequence) {
                this.itemSequence.addAll(itemSequence);
                return this;
            }

            /**
             * <p>
             * The sequence number of the details within the claim item which this line is intended to replace.
             * </p>
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param detailSequence
             *     Detail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detailSequence(Collection<PositiveInt> detailSequence) {
                this.detailSequence.addAll(detailSequence);
                return this;
            }

            /**
             * <p>
             * The sequence number of the sub-details within the details within the claim item which this line is intended to replace.
             * </p>
             * 
             * @param subdetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param subdetailSequence
             *     Subdetail sequence number
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subdetailSequence(Collection<PositiveInt> subdetailSequence) {
                this.subdetailSequence.addAll(subdetailSequence);
                return this;
            }

            /**
             * <p>
             * The providers who are authorized for the services rendered to the patient.
             * </p>
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param provider
             *     Authorized providers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder provider(Collection<Reference> provider) {
                this.provider.addAll(provider);
                return this;
            }

            /**
             * <p>
             * Item typification or modifiers codes to convey additional context for the product or service.
             * </p>
             * 
             * @param modifier
             *     Service/Product billing modifiers
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifier
             *     Service/Product billing modifiers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder modifier(Collection<CodeableConcept> modifier) {
                this.modifier.addAll(modifier);
                return this;
            }

            /**
             * <p>
             * Identifies the program under which this may be recovered.
             * </p>
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param programCode
             *     Program the product or service is provided under
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder programCode(Collection<CodeableConcept> programCode) {
                this.programCode.addAll(programCode);
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder bodySite(CodeableConcept bodySite) {
                this.bodySite = bodySite;
                return this;
            }

            /**
             * <p>
             * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
             * </p>
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param subSite
             *     Anatomical sub-location
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subSite(Collection<CodeableConcept> subSite) {
                this.subSite.addAll(subSite);
                return this;
            }

            /**
             * <p>
             * The numbers associated with notes below which apply to the adjudication of this item.
             * </p>
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param noteNumber
             *     Applicable note numbers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                this.noteNumber.addAll(noteNumber);
                return this;
            }

            /**
             * <p>
             * The second-tier service adjudications for payor added services.
             * </p>
             * 
             * @param detail
             *     Insurer added line details
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param detail
             *     Insurer added line details
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Collection<Detail> detail) {
                this.detail.addAll(detail);
                return this;
            }

            @Override
            public AddItem build() {
                return new AddItem(this);
            }

            private static Builder from(AddItem addItem) {
                Builder builder = new Builder(addItem.productOrService, addItem.adjudication);
                builder.id = addItem.id;
                builder.extension.addAll(addItem.extension);
                builder.modifierExtension.addAll(addItem.modifierExtension);
                builder.itemSequence.addAll(addItem.itemSequence);
                builder.detailSequence.addAll(addItem.detailSequence);
                builder.subdetailSequence.addAll(addItem.subdetailSequence);
                builder.provider.addAll(addItem.provider);
                builder.modifier.addAll(addItem.modifier);
                builder.programCode.addAll(addItem.programCode);
                builder.serviced = addItem.serviced;
                builder.location = addItem.location;
                builder.quantity = addItem.quantity;
                builder.unitPrice = addItem.unitPrice;
                builder.factor = addItem.factor;
                builder.net = addItem.net;
                builder.bodySite = addItem.bodySite;
                builder.subSite.addAll(addItem.subSite);
                builder.noteNumber.addAll(addItem.noteNumber);
                builder.detail.addAll(addItem.detail);
                return builder;
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

            private Detail(Builder builder) {
                super(builder);
                this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                this.modifier = builder.modifier;
                this.quantity = builder.quantity;
                this.unitPrice = builder.unitPrice;
                this.factor = builder.factor;
                this.net = builder.net;
                this.noteNumber = builder.noteNumber;
                this.adjudication = ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication");
                this.subDetail = builder.subDetail;
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
             *     A list containing immutable objects of type {@link CodeableConcept}.
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
             *     A list containing immutable objects of type {@link PositiveInt}.
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
             *     A list containing immutable objects of type {@link Adjudication}.
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
             *     A list containing immutable objects of type {@link SubDetail}.
             */
            public List<SubDetail> getSubDetail() {
                return subDetail;
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
            public Builder toBuilder() {
                return Builder.from(this);
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
                    this.adjudication = adjudication;
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
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * <p>
                 * Item typification or modifiers codes to convey additional context for the product or service.
                 * </p>
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param modifier
                 *     Service/Product billing modifiers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder modifier(Collection<CodeableConcept> modifier) {
                    this.modifier.addAll(modifier);
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
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
                 *     A reference to this Builder instance.
                 */
                public Builder net(Money net) {
                    this.net = net;
                    return this;
                }

                /**
                 * <p>
                 * The numbers associated with notes below which apply to the adjudication of this item.
                 * </p>
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param noteNumber
                 *     Applicable note numbers
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                    this.noteNumber.addAll(noteNumber);
                    return this;
                }

                /**
                 * <p>
                 * The third-tier service adjudications for payor added services.
                 * </p>
                 * 
                 * @param subDetail
                 *     Insurer added line items
                 * 
                 * @return
                 *     A reference to this Builder instance.
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
                 * 
                 * @param subDetail
                 *     Insurer added line items
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder subDetail(Collection<SubDetail> subDetail) {
                    this.subDetail.addAll(subDetail);
                    return this;
                }

                @Override
                public Detail build() {
                    return new Detail(this);
                }

                private static Builder from(Detail detail) {
                    Builder builder = new Builder(detail.productOrService, detail.adjudication);
                    builder.id = detail.id;
                    builder.extension.addAll(detail.extension);
                    builder.modifierExtension.addAll(detail.modifierExtension);
                    builder.modifier.addAll(detail.modifier);
                    builder.quantity = detail.quantity;
                    builder.unitPrice = detail.unitPrice;
                    builder.factor = detail.factor;
                    builder.net = detail.net;
                    builder.noteNumber.addAll(detail.noteNumber);
                    builder.subDetail.addAll(detail.subDetail);
                    return builder;
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

                private SubDetail(Builder builder) {
                    super(builder);
                    this.productOrService = ValidationSupport.requireNonNull(builder.productOrService, "productOrService");
                    this.modifier = builder.modifier;
                    this.quantity = builder.quantity;
                    this.unitPrice = builder.unitPrice;
                    this.factor = builder.factor;
                    this.net = builder.net;
                    this.noteNumber = builder.noteNumber;
                    this.adjudication = ValidationSupport.requireNonEmpty(builder.adjudication, "adjudication");
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
                 *     A list containing immutable objects of type {@link CodeableConcept}.
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
                 *     A list containing immutable objects of type {@link PositiveInt}.
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
                 *     A list containing immutable objects of type {@link Adjudication}.
                 */
                public List<ClaimResponse.Item.Adjudication> getAdjudication() {
                    return adjudication;
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
                public Builder toBuilder() {
                    return Builder.from(this);
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
                        this.adjudication = adjudication;
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
                    public Builder modifierExtension(Collection<Extension> modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    /**
                     * <p>
                     * Item typification or modifiers codes to convey additional context for the product or service.
                     * </p>
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     * 
                     * @param modifier
                     *     Service/Product billing modifiers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder modifier(Collection<CodeableConcept> modifier) {
                        this.modifier.addAll(modifier);
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
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
                     *     A reference to this Builder instance.
                     */
                    public Builder net(Money net) {
                        this.net = net;
                        return this;
                    }

                    /**
                     * <p>
                     * The numbers associated with notes below which apply to the adjudication of this item.
                     * </p>
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
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
                     * 
                     * @param noteNumber
                     *     Applicable note numbers
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder noteNumber(Collection<PositiveInt> noteNumber) {
                        this.noteNumber.addAll(noteNumber);
                        return this;
                    }

                    @Override
                    public SubDetail build() {
                        return new SubDetail(this);
                    }

                    private static Builder from(SubDetail subDetail) {
                        Builder builder = new Builder(subDetail.productOrService, subDetail.adjudication);
                        builder.id = subDetail.id;
                        builder.extension.addAll(subDetail.extension);
                        builder.modifierExtension.addAll(subDetail.modifierExtension);
                        builder.modifier.addAll(subDetail.modifier);
                        builder.quantity = subDetail.quantity;
                        builder.unitPrice = subDetail.unitPrice;
                        builder.factor = subDetail.factor;
                        builder.net = subDetail.net;
                        builder.noteNumber.addAll(subDetail.noteNumber);
                        return builder;
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

        private Total(Builder builder) {
            super(builder);
            this.category = ValidationSupport.requireNonNull(builder.category, "category");
            this.amount = ValidationSupport.requireNonNull(builder.amount, "amount");
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
        public Builder toBuilder() {
            return Builder.from(this);
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
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            @Override
            public Total build() {
                return new Total(this);
            }

            private static Builder from(Total total) {
                Builder builder = new Builder(total.category, total.amount);
                builder.id = total.id;
                builder.extension.addAll(total.extension);
                builder.modifierExtension.addAll(total.modifierExtension);
                return builder;
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

        private Payment(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.adjustment = builder.adjustment;
            this.adjustmentReason = builder.adjustmentReason;
            this.date = builder.date;
            this.amount = ValidationSupport.requireNonNull(builder.amount, "amount");
            this.identifier = builder.identifier;
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
        public Builder toBuilder() {
            return Builder.from(this);
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            @Override
            public Payment build() {
                return new Payment(this);
            }

            private static Builder from(Payment payment) {
                Builder builder = new Builder(payment.type, payment.amount);
                builder.id = payment.id;
                builder.extension.addAll(payment.extension);
                builder.modifierExtension.addAll(payment.modifierExtension);
                builder.adjustment = payment.adjustment;
                builder.adjustmentReason = payment.adjustmentReason;
                builder.date = payment.date;
                builder.identifier = payment.identifier;
                return builder;
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

        private ProcessNote(Builder builder) {
            super(builder);
            this.number = builder.number;
            this.type = builder.type;
            this.text = ValidationSupport.requireNonNull(builder.text, "text");
            this.language = builder.language;
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
        public Builder toBuilder() {
            return Builder.from(this);
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder language(CodeableConcept language) {
                this.language = language;
                return this;
            }

            @Override
            public ProcessNote build() {
                return new ProcessNote(this);
            }

            private static Builder from(ProcessNote processNote) {
                Builder builder = new Builder(processNote.text);
                builder.id = processNote.id;
                builder.extension.addAll(processNote.extension);
                builder.modifierExtension.addAll(processNote.modifierExtension);
                builder.number = processNote.number;
                builder.type = processNote.type;
                builder.language = processNote.language;
                return builder;
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

        private Insurance(Builder builder) {
            super(builder);
            this.sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            this.focal = ValidationSupport.requireNonNull(builder.focal, "focal");
            this.coverage = ValidationSupport.requireNonNull(builder.coverage, "coverage");
            this.businessArrangement = builder.businessArrangement;
            this.claimResponse = builder.claimResponse;
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
        public Builder toBuilder() {
            return Builder.from(this);
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder claimResponse(Reference claimResponse) {
                this.claimResponse = claimResponse;
                return this;
            }

            @Override
            public Insurance build() {
                return new Insurance(this);
            }

            private static Builder from(Insurance insurance) {
                Builder builder = new Builder(insurance.sequence, insurance.focal, insurance.coverage);
                builder.id = insurance.id;
                builder.extension.addAll(insurance.extension);
                builder.modifierExtension.addAll(insurance.modifierExtension);
                builder.businessArrangement = insurance.businessArrangement;
                builder.claimResponse = insurance.claimResponse;
                return builder;
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

        private Error(Builder builder) {
            super(builder);
            this.itemSequence = builder.itemSequence;
            this.detailSequence = builder.detailSequence;
            this.subDetailSequence = builder.subDetailSequence;
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
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
        public Builder toBuilder() {
            return Builder.from(this);
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder subDetailSequence(PositiveInt subDetailSequence) {
                this.subDetailSequence = subDetailSequence;
                return this;
            }

            @Override
            public Error build() {
                return new Error(this);
            }

            private static Builder from(Error error) {
                Builder builder = new Builder(error.code);
                builder.id = error.id;
                builder.extension.addAll(error.extension);
                builder.modifierExtension.addAll(error.modifierExtension);
                builder.itemSequence = error.itemSequence;
                builder.detailSequence = error.detailSequence;
                builder.subDetailSequence = error.subDetailSequence;
                return builder;
            }
        }
    }
}
