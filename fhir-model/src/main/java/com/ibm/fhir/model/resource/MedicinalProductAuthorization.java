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

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The regulatory authorization of a medicinal product.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductAuthorization extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "MedicinalProduct", "MedicinalProductPackaged" })
    private final Reference subject;
    @Summary
    private final List<CodeableConcept> country;
    @Summary
    private final List<CodeableConcept> jurisdiction;
    @Summary
    private final CodeableConcept status;
    @Summary
    private final DateTime statusDate;
    @Summary
    private final DateTime restoreDate;
    @Summary
    private final Period validityPeriod;
    @Summary
    private final Period dataExclusivityPeriod;
    @Summary
    private final DateTime dateOfFirstAuthorization;
    @Summary
    private final DateTime internationalBirthDate;
    @Summary
    private final CodeableConcept legalBasis;
    @Summary
    private final List<JurisdictionalAuthorization> jurisdictionalAuthorization;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference holder;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference regulator;
    @Summary
    private final Procedure procedure;

    private MedicinalProductAuthorization(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        subject = builder.subject;
        country = Collections.unmodifiableList(builder.country);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        status = builder.status;
        statusDate = builder.statusDate;
        restoreDate = builder.restoreDate;
        validityPeriod = builder.validityPeriod;
        dataExclusivityPeriod = builder.dataExclusivityPeriod;
        dateOfFirstAuthorization = builder.dateOfFirstAuthorization;
        internationalBirthDate = builder.internationalBirthDate;
        legalBasis = builder.legalBasis;
        jurisdictionalAuthorization = Collections.unmodifiableList(builder.jurisdictionalAuthorization);
        holder = builder.holder;
        regulator = builder.regulator;
        procedure = builder.procedure;
    }

    /**
     * Business identifier for the marketing authorization, as assigned by a regulator.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The medicinal product that is being authorized.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The country in which the marketing authorization has been granted.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCountry() {
        return country;
    }

    /**
     * Jurisdiction within a country.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * The status of the marketing authorization.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * The date at which the given status has become applicable.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getStatusDate() {
        return statusDate;
    }

    /**
     * The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getRestoreDate() {
        return restoreDate;
    }

    /**
     * The beginning of the time period in which the marketing authorization is in the specific status shall be specified A 
     * complete date consisting of day, month and year shall be specified using the ISO 8601 date format.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * A period of time after authorization before generic product applicatiosn can be submitted.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getDataExclusivityPeriod() {
        return dataExclusivityPeriod;
    }

    /**
     * The date when the first authorization was granted by a Medicines Regulatory Agency.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDateOfFirstAuthorization() {
        return dateOfFirstAuthorization;
    }

    /**
     * Date of first marketing authorization for a company's new medicinal product in any country in the World.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getInternationalBirthDate() {
        return internationalBirthDate;
    }

    /**
     * The legal framework against which this authorization is granted.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getLegalBasis() {
        return legalBasis;
    }

    /**
     * Authorization in areas within a country.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link JurisdictionalAuthorization} that may be empty.
     */
    public List<JurisdictionalAuthorization> getJurisdictionalAuthorization() {
        return jurisdictionalAuthorization;
    }

    /**
     * Marketing Authorization Holder.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getHolder() {
        return holder;
    }

    /**
     * Medicines Regulatory Agency.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRegulator() {
        return regulator;
    }

    /**
     * The regulatory procedure for granting or amending a marketing authorization.
     * 
     * @return
     *     An immutable object of type {@link Procedure} that may be null.
     */
    public Procedure getProcedure() {
        return procedure;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (subject != null) || 
            !country.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (status != null) || 
            (statusDate != null) || 
            (restoreDate != null) || 
            (validityPeriod != null) || 
            (dataExclusivityPeriod != null) || 
            (dateOfFirstAuthorization != null) || 
            (internationalBirthDate != null) || 
            (legalBasis != null) || 
            !jurisdictionalAuthorization.isEmpty() || 
            (holder != null) || 
            (regulator != null) || 
            (procedure != null);
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
                accept(subject, "subject", visitor);
                accept(country, "country", visitor, CodeableConcept.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(status, "status", visitor);
                accept(statusDate, "statusDate", visitor);
                accept(restoreDate, "restoreDate", visitor);
                accept(validityPeriod, "validityPeriod", visitor);
                accept(dataExclusivityPeriod, "dataExclusivityPeriod", visitor);
                accept(dateOfFirstAuthorization, "dateOfFirstAuthorization", visitor);
                accept(internationalBirthDate, "internationalBirthDate", visitor);
                accept(legalBasis, "legalBasis", visitor);
                accept(jurisdictionalAuthorization, "jurisdictionalAuthorization", visitor, JurisdictionalAuthorization.class);
                accept(holder, "holder", visitor);
                accept(regulator, "regulator", visitor);
                accept(procedure, "procedure", visitor);
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
        MedicinalProductAuthorization other = (MedicinalProductAuthorization) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(country, other.country) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusDate, other.statusDate) && 
            Objects.equals(restoreDate, other.restoreDate) && 
            Objects.equals(validityPeriod, other.validityPeriod) && 
            Objects.equals(dataExclusivityPeriod, other.dataExclusivityPeriod) && 
            Objects.equals(dateOfFirstAuthorization, other.dateOfFirstAuthorization) && 
            Objects.equals(internationalBirthDate, other.internationalBirthDate) && 
            Objects.equals(legalBasis, other.legalBasis) && 
            Objects.equals(jurisdictionalAuthorization, other.jurisdictionalAuthorization) && 
            Objects.equals(holder, other.holder) && 
            Objects.equals(regulator, other.regulator) && 
            Objects.equals(procedure, other.procedure);
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
                subject, 
                country, 
                jurisdiction, 
                status, 
                statusDate, 
                restoreDate, 
                validityPeriod, 
                dataExclusivityPeriod, 
                dateOfFirstAuthorization, 
                internationalBirthDate, 
                legalBasis, 
                jurisdictionalAuthorization, 
                holder, 
                regulator, 
                procedure);
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
        private Reference subject;
        private List<CodeableConcept> country = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private CodeableConcept status;
        private DateTime statusDate;
        private DateTime restoreDate;
        private Period validityPeriod;
        private Period dataExclusivityPeriod;
        private DateTime dateOfFirstAuthorization;
        private DateTime internationalBirthDate;
        private CodeableConcept legalBasis;
        private List<JurisdictionalAuthorization> jurisdictionalAuthorization = new ArrayList<>();
        private Reference holder;
        private Reference regulator;
        private Procedure procedure;

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
         * Business identifier for the marketing authorization, as assigned by a regulator.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for the marketing authorization, as assigned by a regulator
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
         * Business identifier for the marketing authorization, as assigned by a regulator.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for the marketing authorization, as assigned by a regulator
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
         * The medicinal product that is being authorized.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link MedicinalProduct}</li>
         * <li>{@link MedicinalProductPackaged}</li>
         * </ul>
         * 
         * @param subject
         *     The medicinal product that is being authorized
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The country in which the marketing authorization has been granted.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param country
         *     The country in which the marketing authorization has been granted
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder country(CodeableConcept... country) {
            for (CodeableConcept value : country) {
                this.country.add(value);
            }
            return this;
        }

        /**
         * The country in which the marketing authorization has been granted.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param country
         *     The country in which the marketing authorization has been granted
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder country(Collection<CodeableConcept> country) {
            this.country = new ArrayList<>(country);
            return this;
        }

        /**
         * Jurisdiction within a country.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Jurisdiction within a country
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * Jurisdiction within a country.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Jurisdiction within a country
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * The status of the marketing authorization.
         * 
         * @param status
         *     The status of the marketing authorization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * The date at which the given status has become applicable.
         * 
         * @param statusDate
         *     The date at which the given status has become applicable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusDate(DateTime statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.
         * 
         * @param restoreDate
         *     The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder restoreDate(DateTime restoreDate) {
            this.restoreDate = restoreDate;
            return this;
        }

        /**
         * The beginning of the time period in which the marketing authorization is in the specific status shall be specified A 
         * complete date consisting of day, month and year shall be specified using the ISO 8601 date format.
         * 
         * @param validityPeriod
         *     The beginning of the time period in which the marketing authorization is in the specific status shall be specified A 
         *     complete date consisting of day, month and year shall be specified using the ISO 8601 date format
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validityPeriod(Period validityPeriod) {
            this.validityPeriod = validityPeriod;
            return this;
        }

        /**
         * A period of time after authorization before generic product applicatiosn can be submitted.
         * 
         * @param dataExclusivityPeriod
         *     A period of time after authorization before generic product applicatiosn can be submitted
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dataExclusivityPeriod(Period dataExclusivityPeriod) {
            this.dataExclusivityPeriod = dataExclusivityPeriod;
            return this;
        }

        /**
         * The date when the first authorization was granted by a Medicines Regulatory Agency.
         * 
         * @param dateOfFirstAuthorization
         *     The date when the first authorization was granted by a Medicines Regulatory Agency
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dateOfFirstAuthorization(DateTime dateOfFirstAuthorization) {
            this.dateOfFirstAuthorization = dateOfFirstAuthorization;
            return this;
        }

        /**
         * Date of first marketing authorization for a company's new medicinal product in any country in the World.
         * 
         * @param internationalBirthDate
         *     Date of first marketing authorization for a company's new medicinal product in any country in the World
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder internationalBirthDate(DateTime internationalBirthDate) {
            this.internationalBirthDate = internationalBirthDate;
            return this;
        }

        /**
         * The legal framework against which this authorization is granted.
         * 
         * @param legalBasis
         *     The legal framework against which this authorization is granted
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legalBasis(CodeableConcept legalBasis) {
            this.legalBasis = legalBasis;
            return this;
        }

        /**
         * Authorization in areas within a country.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdictionalAuthorization
         *     Authorization in areas within a country
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdictionalAuthorization(JurisdictionalAuthorization... jurisdictionalAuthorization) {
            for (JurisdictionalAuthorization value : jurisdictionalAuthorization) {
                this.jurisdictionalAuthorization.add(value);
            }
            return this;
        }

        /**
         * Authorization in areas within a country.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdictionalAuthorization
         *     Authorization in areas within a country
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder jurisdictionalAuthorization(Collection<JurisdictionalAuthorization> jurisdictionalAuthorization) {
            this.jurisdictionalAuthorization = new ArrayList<>(jurisdictionalAuthorization);
            return this;
        }

        /**
         * Marketing Authorization Holder.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param holder
         *     Marketing Authorization Holder
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder holder(Reference holder) {
            this.holder = holder;
            return this;
        }

        /**
         * Medicines Regulatory Agency.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param regulator
         *     Medicines Regulatory Agency
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder regulator(Reference regulator) {
            this.regulator = regulator;
            return this;
        }

        /**
         * The regulatory procedure for granting or amending a marketing authorization.
         * 
         * @param procedure
         *     The regulatory procedure for granting or amending a marketing authorization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder procedure(Procedure procedure) {
            this.procedure = procedure;
            return this;
        }

        /**
         * Build the {@link MedicinalProductAuthorization}
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductAuthorization}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductAuthorization per the base specification
         */
        @Override
        public MedicinalProductAuthorization build() {
            MedicinalProductAuthorization medicinalProductAuthorization = new MedicinalProductAuthorization(this);
            if (validating) {
                validate(medicinalProductAuthorization);
            }
            return medicinalProductAuthorization;
        }

        protected void validate(MedicinalProductAuthorization medicinalProductAuthorization) {
            super.validate(medicinalProductAuthorization);
            ValidationSupport.checkList(medicinalProductAuthorization.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(medicinalProductAuthorization.country, "country", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductAuthorization.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductAuthorization.jurisdictionalAuthorization, "jurisdictionalAuthorization", JurisdictionalAuthorization.class);
            ValidationSupport.checkReferenceType(medicinalProductAuthorization.subject, "subject", "MedicinalProduct", "MedicinalProductPackaged");
            ValidationSupport.checkReferenceType(medicinalProductAuthorization.holder, "holder", "Organization");
            ValidationSupport.checkReferenceType(medicinalProductAuthorization.regulator, "regulator", "Organization");
        }

        protected Builder from(MedicinalProductAuthorization medicinalProductAuthorization) {
            super.from(medicinalProductAuthorization);
            identifier.addAll(medicinalProductAuthorization.identifier);
            subject = medicinalProductAuthorization.subject;
            country.addAll(medicinalProductAuthorization.country);
            jurisdiction.addAll(medicinalProductAuthorization.jurisdiction);
            status = medicinalProductAuthorization.status;
            statusDate = medicinalProductAuthorization.statusDate;
            restoreDate = medicinalProductAuthorization.restoreDate;
            validityPeriod = medicinalProductAuthorization.validityPeriod;
            dataExclusivityPeriod = medicinalProductAuthorization.dataExclusivityPeriod;
            dateOfFirstAuthorization = medicinalProductAuthorization.dateOfFirstAuthorization;
            internationalBirthDate = medicinalProductAuthorization.internationalBirthDate;
            legalBasis = medicinalProductAuthorization.legalBasis;
            jurisdictionalAuthorization.addAll(medicinalProductAuthorization.jurisdictionalAuthorization);
            holder = medicinalProductAuthorization.holder;
            regulator = medicinalProductAuthorization.regulator;
            procedure = medicinalProductAuthorization.procedure;
            return this;
        }
    }

    /**
     * Authorization in areas within a country.
     */
    public static class JurisdictionalAuthorization extends BackboneElement {
        @Summary
        private final List<Identifier> identifier;
        @Summary
        private final CodeableConcept country;
        @Summary
        private final List<CodeableConcept> jurisdiction;
        @Summary
        private final CodeableConcept legalStatusOfSupply;
        @Summary
        private final Period validityPeriod;

        private JurisdictionalAuthorization(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            country = builder.country;
            jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
            legalStatusOfSupply = builder.legalStatusOfSupply;
            validityPeriod = builder.validityPeriod;
        }

        /**
         * The assigned number for the marketing authorization.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * Country of authorization.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCountry() {
            return country;
        }

        /**
         * Jurisdiction within a country.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getJurisdiction() {
            return jurisdiction;
        }

        /**
         * The legal status of supply in a jurisdiction or region.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getLegalStatusOfSupply() {
            return legalStatusOfSupply;
        }

        /**
         * The start and expected end date of the authorization.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getValidityPeriod() {
            return validityPeriod;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !identifier.isEmpty() || 
                (country != null) || 
                !jurisdiction.isEmpty() || 
                (legalStatusOfSupply != null) || 
                (validityPeriod != null);
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
                    accept(identifier, "identifier", visitor, Identifier.class);
                    accept(country, "country", visitor);
                    accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                    accept(legalStatusOfSupply, "legalStatusOfSupply", visitor);
                    accept(validityPeriod, "validityPeriod", visitor);
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
            JurisdictionalAuthorization other = (JurisdictionalAuthorization) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(country, other.country) && 
                Objects.equals(jurisdiction, other.jurisdiction) && 
                Objects.equals(legalStatusOfSupply, other.legalStatusOfSupply) && 
                Objects.equals(validityPeriod, other.validityPeriod);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    country, 
                    jurisdiction, 
                    legalStatusOfSupply, 
                    validityPeriod);
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
            private List<Identifier> identifier = new ArrayList<>();
            private CodeableConcept country;
            private List<CodeableConcept> jurisdiction = new ArrayList<>();
            private CodeableConcept legalStatusOfSupply;
            private Period validityPeriod;

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
             * The assigned number for the marketing authorization.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     The assigned number for the marketing authorization
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
             * The assigned number for the marketing authorization.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     The assigned number for the marketing authorization
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
             * Country of authorization.
             * 
             * @param country
             *     Country of authorization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder country(CodeableConcept country) {
                this.country = country;
                return this;
            }

            /**
             * Jurisdiction within a country.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param jurisdiction
             *     Jurisdiction within a country
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder jurisdiction(CodeableConcept... jurisdiction) {
                for (CodeableConcept value : jurisdiction) {
                    this.jurisdiction.add(value);
                }
                return this;
            }

            /**
             * Jurisdiction within a country.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param jurisdiction
             *     Jurisdiction within a country
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
                this.jurisdiction = new ArrayList<>(jurisdiction);
                return this;
            }

            /**
             * The legal status of supply in a jurisdiction or region.
             * 
             * @param legalStatusOfSupply
             *     The legal status of supply in a jurisdiction or region
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder legalStatusOfSupply(CodeableConcept legalStatusOfSupply) {
                this.legalStatusOfSupply = legalStatusOfSupply;
                return this;
            }

            /**
             * The start and expected end date of the authorization.
             * 
             * @param validityPeriod
             *     The start and expected end date of the authorization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder validityPeriod(Period validityPeriod) {
                this.validityPeriod = validityPeriod;
                return this;
            }

            /**
             * Build the {@link JurisdictionalAuthorization}
             * 
             * @return
             *     An immutable object of type {@link JurisdictionalAuthorization}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid JurisdictionalAuthorization per the base specification
             */
            @Override
            public JurisdictionalAuthorization build() {
                JurisdictionalAuthorization jurisdictionalAuthorization = new JurisdictionalAuthorization(this);
                if (validating) {
                    validate(jurisdictionalAuthorization);
                }
                return jurisdictionalAuthorization;
            }

            protected void validate(JurisdictionalAuthorization jurisdictionalAuthorization) {
                super.validate(jurisdictionalAuthorization);
                ValidationSupport.checkList(jurisdictionalAuthorization.identifier, "identifier", Identifier.class);
                ValidationSupport.checkList(jurisdictionalAuthorization.jurisdiction, "jurisdiction", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(jurisdictionalAuthorization);
            }

            protected Builder from(JurisdictionalAuthorization jurisdictionalAuthorization) {
                super.from(jurisdictionalAuthorization);
                identifier.addAll(jurisdictionalAuthorization.identifier);
                country = jurisdictionalAuthorization.country;
                jurisdiction.addAll(jurisdictionalAuthorization.jurisdiction);
                legalStatusOfSupply = jurisdictionalAuthorization.legalStatusOfSupply;
                validityPeriod = jurisdictionalAuthorization.validityPeriod;
                return this;
            }
        }
    }

    /**
     * The regulatory procedure for granting or amending a marketing authorization.
     */
    public static class Procedure extends BackboneElement {
        @Summary
        private final Identifier identifier;
        @Summary
        @Required
        private final CodeableConcept type;
        @Summary
        @Choice({ Period.class, DateTime.class })
        private final Element date;
        @Summary
        private final List<MedicinalProductAuthorization.Procedure> application;

        private Procedure(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            type = builder.type;
            date = builder.date;
            application = Collections.unmodifiableList(builder.application);
        }

        /**
         * Identifier for this procedure.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * Type of procedure.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Date of procedure.
         * 
         * @return
         *     An immutable object of type {@link Period} or {@link DateTime} that may be null.
         */
        public Element getDate() {
            return date;
        }

        /**
         * Applcations submitted to obtain a marketing authorization.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Procedure} that may be empty.
         */
        public List<MedicinalProductAuthorization.Procedure> getApplication() {
            return application;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identifier != null) || 
                (type != null) || 
                (date != null) || 
                !application.isEmpty();
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
                    accept(identifier, "identifier", visitor);
                    accept(type, "type", visitor);
                    accept(date, "date", visitor);
                    accept(application, "application", visitor, MedicinalProductAuthorization.Procedure.class);
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
            Procedure other = (Procedure) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(type, other.type) && 
                Objects.equals(date, other.date) && 
                Objects.equals(application, other.application);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    type, 
                    date, 
                    application);
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
            private Identifier identifier;
            private CodeableConcept type;
            private Element date;
            private List<MedicinalProductAuthorization.Procedure> application = new ArrayList<>();

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
             * Identifier for this procedure.
             * 
             * @param identifier
             *     Identifier for this procedure
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * Type of procedure.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Type of procedure
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Date of procedure.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Period}</li>
             * <li>{@link DateTime}</li>
             * </ul>
             * 
             * @param date
             *     Date of procedure
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(Element date) {
                this.date = date;
                return this;
            }

            /**
             * Applcations submitted to obtain a marketing authorization.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param application
             *     Applcations submitted to obtain a marketing authorization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder application(MedicinalProductAuthorization.Procedure... application) {
                for (MedicinalProductAuthorization.Procedure value : application) {
                    this.application.add(value);
                }
                return this;
            }

            /**
             * Applcations submitted to obtain a marketing authorization.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param application
             *     Applcations submitted to obtain a marketing authorization
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder application(Collection<MedicinalProductAuthorization.Procedure> application) {
                this.application = new ArrayList<>(application);
                return this;
            }

            /**
             * Build the {@link Procedure}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Procedure}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Procedure per the base specification
             */
            @Override
            public Procedure build() {
                Procedure procedure = new Procedure(this);
                if (validating) {
                    validate(procedure);
                }
                return procedure;
            }

            protected void validate(Procedure procedure) {
                super.validate(procedure);
                ValidationSupport.requireNonNull(procedure.type, "type");
                ValidationSupport.choiceElement(procedure.date, "date", Period.class, DateTime.class);
                ValidationSupport.checkList(procedure.application, "application", MedicinalProductAuthorization.Procedure.class);
                ValidationSupport.requireValueOrChildren(procedure);
            }

            protected Builder from(Procedure procedure) {
                super.from(procedure);
                identifier = procedure.identifier;
                type = procedure.type;
                date = procedure.date;
                application.addAll(procedure.application);
                return this;
            }
        }
    }
}
