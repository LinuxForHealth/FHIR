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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The regulatory authorization of a medicinal product.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicinalProductAuthorization extends DomainResource {
    private final List<Identifier> identifier;
    private final Reference subject;
    private final List<CodeableConcept> country;
    private final List<CodeableConcept> jurisdiction;
    private final CodeableConcept status;
    private final DateTime statusDate;
    private final DateTime restoreDate;
    private final Period validityPeriod;
    private final Period dataExclusivityPeriod;
    private final DateTime dateOfFirstAuthorization;
    private final DateTime internationalBirthDate;
    private final CodeableConcept legalBasis;
    private final List<JurisdictionalAuthorization> jurisdictionalAuthorization;
    private final Reference holder;
    private final Reference regulator;
    private final Procedure procedure;

    private volatile int hashCode;

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
     * <p>
     * Business identifier for the marketing authorization, as assigned by a regulator.
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
     * The medicinal product that is being authorized.
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
     * The country in which the marketing authorization has been granted.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCountry() {
        return country;
    }

    /**
     * <p>
     * Jurisdiction within a country.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * <p>
     * The status of the marketing authorization.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * <p>
     * The date at which the given status has become applicable.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getStatusDate() {
        return statusDate;
    }

    /**
     * <p>
     * The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getRestoreDate() {
        return restoreDate;
    }

    /**
     * <p>
     * The beginning of the time period in which the marketing authorization is in the specific status shall be specified A 
     * complete date consisting of day, month and year shall be specified using the ISO 8601 date format.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * <p>
     * A period of time after authorization before generic product applicatiosn can be submitted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getDataExclusivityPeriod() {
        return dataExclusivityPeriod;
    }

    /**
     * <p>
     * The date when the first authorization was granted by a Medicines Regulatory Agency.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDateOfFirstAuthorization() {
        return dateOfFirstAuthorization;
    }

    /**
     * <p>
     * Date of first marketing authorization for a company's new medicinal product in any country in the World.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getInternationalBirthDate() {
        return internationalBirthDate;
    }

    /**
     * <p>
     * The legal framework against which this authorization is granted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getLegalBasis() {
        return legalBasis;
    }

    /**
     * <p>
     * Authorization in areas within a country.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link JurisdictionalAuthorization}.
     */
    public List<JurisdictionalAuthorization> getJurisdictionalAuthorization() {
        return jurisdictionalAuthorization;
    }

    /**
     * <p>
     * Marketing Authorization Holder.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getHolder() {
        return holder;
    }

    /**
     * <p>
     * Medicines Regulatory Agency.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRegulator() {
        return regulator;
    }

    /**
     * <p>
     * The regulatory procedure for granting or amending a marketing authorization.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Procedure}.
     */
    public Procedure getProcedure() {
        return procedure;
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
        // optional
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
         * Business identifier for the marketing authorization, as assigned by a regulator.
         * </p>
         * 
         * @param identifier
         *     Business identifier for the marketing authorization, as assigned by a regulator
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
         * Business identifier for the marketing authorization, as assigned by a regulator.
         * </p>
         * 
         * @param identifier
         *     Business identifier for the marketing authorization, as assigned by a regulator
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
         * The medicinal product that is being authorized.
         * </p>
         * 
         * @param subject
         *     The medicinal product that is being authorized
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
         * The country in which the marketing authorization has been granted.
         * </p>
         * 
         * @param country
         *     The country in which the marketing authorization has been granted
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder country(CodeableConcept... country) {
            for (CodeableConcept value : country) {
                this.country.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The country in which the marketing authorization has been granted.
         * </p>
         * 
         * @param country
         *     The country in which the marketing authorization has been granted
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder country(Collection<CodeableConcept> country) {
            this.country.addAll(country);
            return this;
        }

        /**
         * <p>
         * Jurisdiction within a country.
         * </p>
         * 
         * @param jurisdiction
         *     Jurisdiction within a country
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Jurisdiction within a country.
         * </p>
         * 
         * @param jurisdiction
         *     Jurisdiction within a country
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction.addAll(jurisdiction);
            return this;
        }

        /**
         * <p>
         * The status of the marketing authorization.
         * </p>
         * 
         * @param status
         *     The status of the marketing authorization
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * The date at which the given status has become applicable.
         * </p>
         * 
         * @param statusDate
         *     The date at which the given status has become applicable
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusDate(DateTime statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * <p>
         * The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.
         * </p>
         * 
         * @param restoreDate
         *     The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder restoreDate(DateTime restoreDate) {
            this.restoreDate = restoreDate;
            return this;
        }

        /**
         * <p>
         * The beginning of the time period in which the marketing authorization is in the specific status shall be specified A 
         * complete date consisting of day, month and year shall be specified using the ISO 8601 date format.
         * </p>
         * 
         * @param validityPeriod
         *     The beginning of the time period in which the marketing authorization is in the specific status shall be specified A 
         *     complete date consisting of day, month and year shall be specified using the ISO 8601 date format
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validityPeriod(Period validityPeriod) {
            this.validityPeriod = validityPeriod;
            return this;
        }

        /**
         * <p>
         * A period of time after authorization before generic product applicatiosn can be submitted.
         * </p>
         * 
         * @param dataExclusivityPeriod
         *     A period of time after authorization before generic product applicatiosn can be submitted
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dataExclusivityPeriod(Period dataExclusivityPeriod) {
            this.dataExclusivityPeriod = dataExclusivityPeriod;
            return this;
        }

        /**
         * <p>
         * The date when the first authorization was granted by a Medicines Regulatory Agency.
         * </p>
         * 
         * @param dateOfFirstAuthorization
         *     The date when the first authorization was granted by a Medicines Regulatory Agency
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dateOfFirstAuthorization(DateTime dateOfFirstAuthorization) {
            this.dateOfFirstAuthorization = dateOfFirstAuthorization;
            return this;
        }

        /**
         * <p>
         * Date of first marketing authorization for a company's new medicinal product in any country in the World.
         * </p>
         * 
         * @param internationalBirthDate
         *     Date of first marketing authorization for a company's new medicinal product in any country in the World
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder internationalBirthDate(DateTime internationalBirthDate) {
            this.internationalBirthDate = internationalBirthDate;
            return this;
        }

        /**
         * <p>
         * The legal framework against which this authorization is granted.
         * </p>
         * 
         * @param legalBasis
         *     The legal framework against which this authorization is granted
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder legalBasis(CodeableConcept legalBasis) {
            this.legalBasis = legalBasis;
            return this;
        }

        /**
         * <p>
         * Authorization in areas within a country.
         * </p>
         * 
         * @param jurisdictionalAuthorization
         *     Authorization in areas within a country
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdictionalAuthorization(JurisdictionalAuthorization... jurisdictionalAuthorization) {
            for (JurisdictionalAuthorization value : jurisdictionalAuthorization) {
                this.jurisdictionalAuthorization.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Authorization in areas within a country.
         * </p>
         * 
         * @param jurisdictionalAuthorization
         *     Authorization in areas within a country
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdictionalAuthorization(Collection<JurisdictionalAuthorization> jurisdictionalAuthorization) {
            this.jurisdictionalAuthorization.addAll(jurisdictionalAuthorization);
            return this;
        }

        /**
         * <p>
         * Marketing Authorization Holder.
         * </p>
         * 
         * @param holder
         *     Marketing Authorization Holder
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder holder(Reference holder) {
            this.holder = holder;
            return this;
        }

        /**
         * <p>
         * Medicines Regulatory Agency.
         * </p>
         * 
         * @param regulator
         *     Medicines Regulatory Agency
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder regulator(Reference regulator) {
            this.regulator = regulator;
            return this;
        }

        /**
         * <p>
         * The regulatory procedure for granting or amending a marketing authorization.
         * </p>
         * 
         * @param procedure
         *     The regulatory procedure for granting or amending a marketing authorization
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder procedure(Procedure procedure) {
            this.procedure = procedure;
            return this;
        }

        @Override
        public MedicinalProductAuthorization build() {
            return new MedicinalProductAuthorization(this);
        }

        private Builder from(MedicinalProductAuthorization medicinalProductAuthorization) {
            id = medicinalProductAuthorization.id;
            meta = medicinalProductAuthorization.meta;
            implicitRules = medicinalProductAuthorization.implicitRules;
            language = medicinalProductAuthorization.language;
            text = medicinalProductAuthorization.text;
            contained.addAll(medicinalProductAuthorization.contained);
            extension.addAll(medicinalProductAuthorization.extension);
            modifierExtension.addAll(medicinalProductAuthorization.modifierExtension);
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
     * <p>
     * Authorization in areas within a country.
     * </p>
     */
    public static class JurisdictionalAuthorization extends BackboneElement {
        private final List<Identifier> identifier;
        private final CodeableConcept country;
        private final List<CodeableConcept> jurisdiction;
        private final CodeableConcept legalStatusOfSupply;
        private final Period validityPeriod;

        private volatile int hashCode;

        private JurisdictionalAuthorization(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            country = builder.country;
            jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
            legalStatusOfSupply = builder.legalStatusOfSupply;
            validityPeriod = builder.validityPeriod;
        }

        /**
         * <p>
         * The assigned number for the marketing authorization.
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
         * Country of authorization.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCountry() {
            return country;
        }

        /**
         * <p>
         * Jurisdiction within a country.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getJurisdiction() {
            return jurisdiction;
        }

        /**
         * <p>
         * The legal status of supply in a jurisdiction or region.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getLegalStatusOfSupply() {
            return legalStatusOfSupply;
        }

        /**
         * <p>
         * The start and expected end date of the authorization.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getValidityPeriod() {
            return validityPeriod;
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
                    accept(country, "country", visitor);
                    accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                    accept(legalStatusOfSupply, "legalStatusOfSupply", visitor);
                    accept(validityPeriod, "validityPeriod", visitor);
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
            // optional
            private List<Identifier> identifier = new ArrayList<>();
            private CodeableConcept country;
            private List<CodeableConcept> jurisdiction = new ArrayList<>();
            private CodeableConcept legalStatusOfSupply;
            private Period validityPeriod;

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
             * The assigned number for the marketing authorization.
             * </p>
             * 
             * @param identifier
             *     The assigned number for the marketing authorization
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
             * The assigned number for the marketing authorization.
             * </p>
             * 
             * @param identifier
             *     The assigned number for the marketing authorization
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
             * Country of authorization.
             * </p>
             * 
             * @param country
             *     Country of authorization
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder country(CodeableConcept country) {
                this.country = country;
                return this;
            }

            /**
             * <p>
             * Jurisdiction within a country.
             * </p>
             * 
             * @param jurisdiction
             *     Jurisdiction within a country
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder jurisdiction(CodeableConcept... jurisdiction) {
                for (CodeableConcept value : jurisdiction) {
                    this.jurisdiction.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Jurisdiction within a country.
             * </p>
             * 
             * @param jurisdiction
             *     Jurisdiction within a country
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
                this.jurisdiction.addAll(jurisdiction);
                return this;
            }

            /**
             * <p>
             * The legal status of supply in a jurisdiction or region.
             * </p>
             * 
             * @param legalStatusOfSupply
             *     The legal status of supply in a jurisdiction or region
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder legalStatusOfSupply(CodeableConcept legalStatusOfSupply) {
                this.legalStatusOfSupply = legalStatusOfSupply;
                return this;
            }

            /**
             * <p>
             * The start and expected end date of the authorization.
             * </p>
             * 
             * @param validityPeriod
             *     The start and expected end date of the authorization
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder validityPeriod(Period validityPeriod) {
                this.validityPeriod = validityPeriod;
                return this;
            }

            @Override
            public JurisdictionalAuthorization build() {
                return new JurisdictionalAuthorization(this);
            }

            private Builder from(JurisdictionalAuthorization jurisdictionalAuthorization) {
                id = jurisdictionalAuthorization.id;
                extension.addAll(jurisdictionalAuthorization.extension);
                modifierExtension.addAll(jurisdictionalAuthorization.modifierExtension);
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
     * <p>
     * The regulatory procedure for granting or amending a marketing authorization.
     * </p>
     */
    public static class Procedure extends BackboneElement {
        private final Identifier identifier;
        private final CodeableConcept type;
        private final Element date;
        private final List<MedicinalProductAuthorization.Procedure> application;

        private volatile int hashCode;

        private Procedure(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            type = ValidationSupport.requireNonNull(builder.type, "type");
            date = ValidationSupport.choiceElement(builder.date, "date", Period.class, DateTime.class);
            application = Collections.unmodifiableList(builder.application);
        }

        /**
         * <p>
         * Identifier for this procedure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * Type of procedure.
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
         * Date of procedure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getDate() {
            return date;
        }

        /**
         * <p>
         * Applcations submitted to obtain a marketing authorization.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Procedure}.
         */
        public List<MedicinalProductAuthorization.Procedure> getApplication() {
            return application;
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
                    accept(identifier, "identifier", visitor);
                    accept(type, "type", visitor);
                    accept(date, "date", visitor);
                    accept(application, "application", visitor, MedicinalProductAuthorization.Procedure.class);
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
            return new Builder(type).from(this);
        }

        public Builder toBuilder(CodeableConcept type) {
            return new Builder(type).from(this);
        }

        public static Builder builder(CodeableConcept type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;

            // optional
            private Identifier identifier;
            private Element date;
            private List<MedicinalProductAuthorization.Procedure> application = new ArrayList<>();

            private Builder(CodeableConcept type) {
                super();
                this.type = type;
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
             * Identifier for this procedure.
             * </p>
             * 
             * @param identifier
             *     Identifier for this procedure
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * <p>
             * Date of procedure.
             * </p>
             * 
             * @param date
             *     Date of procedure
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder date(Element date) {
                this.date = date;
                return this;
            }

            /**
             * <p>
             * Applcations submitted to obtain a marketing authorization.
             * </p>
             * 
             * @param application
             *     Applcations submitted to obtain a marketing authorization
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder application(MedicinalProductAuthorization.Procedure... application) {
                for (MedicinalProductAuthorization.Procedure value : application) {
                    this.application.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Applcations submitted to obtain a marketing authorization.
             * </p>
             * 
             * @param application
             *     Applcations submitted to obtain a marketing authorization
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder application(Collection<MedicinalProductAuthorization.Procedure> application) {
                this.application.addAll(application);
                return this;
            }

            @Override
            public Procedure build() {
                return new Procedure(this);
            }

            private Builder from(Procedure procedure) {
                id = procedure.id;
                extension.addAll(procedure.extension);
                modifierExtension.addAll(procedure.modifierExtension);
                identifier = procedure.identifier;
                date = procedure.date;
                application.addAll(procedure.application);
                return this;
            }
        }
    }
}
