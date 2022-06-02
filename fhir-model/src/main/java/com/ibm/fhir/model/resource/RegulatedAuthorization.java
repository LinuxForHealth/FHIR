/*
 * (C) Copyright IBM Corp. 2019, 2022
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
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.CodeableReference;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Regulatory approval, clearance or licencing related to a regulated product, treatment, facility or activity that is 
 * cited in a guidance, regulation, rule or legislative act. An example is Market Authorization relating to a Medicinal 
 * Product.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "regulatedAuthorization-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/publication-status",
    expression = "status.exists() implies (status.memberOf('http://hl7.org/fhir/ValueSet/publication-status', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/RegulatedAuthorization",
    generated = true
)
@Constraint(
    id = "regulatedAuthorization-1",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/product-intended-use",
    expression = "intendedUse.exists() implies (intendedUse.memberOf('http://hl7.org/fhir/ValueSet/product-intended-use', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/RegulatedAuthorization",
    generated = true
)
@Constraint(
    id = "regulatedAuthorization-2",
    level = "Warning",
    location = "case.status",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/publication-status",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/publication-status', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/RegulatedAuthorization",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class RegulatedAuthorization extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "MedicinalProductDefinition", "BiologicallyDerivedProduct", "NutritionProduct", "PackagedProductDefinition", "SubstanceDefinition", "DeviceDefinition", "ResearchStudy", "ActivityDefinition", "PlanDefinition", "ObservationDefinition", "Practitioner", "Organization", "Location" })
    private final List<Reference> subject;
    @Summary
    @Binding(
        bindingName = "RegulatedAuthorizationType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Overall type of this authorization.",
        valueSet = "http://hl7.org/fhir/ValueSet/regulated-authorization-type"
    )
    private final CodeableConcept type;
    @Summary
    private final Markdown description;
    @Summary
    @Binding(
        bindingName = "Jurisdiction",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Jurisdiction codes",
        valueSet = "http://hl7.org/fhir/ValueSet/jurisdiction"
    )
    private final List<CodeableConcept> region;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.PREFERRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status"
    )
    private final CodeableConcept status;
    @Summary
    private final DateTime statusDate;
    @Summary
    private final Period validityPeriod;
    @Summary
    private final CodeableReference indication;
    @Summary
    @Binding(
        bindingName = "ProductIntendedUse",
        strength = BindingStrength.Value.PREFERRED,
        description = "The overall intended use of a product.",
        valueSet = "http://hl7.org/fhir/ValueSet/product-intended-use"
    )
    private final CodeableConcept intendedUse;
    @Summary
    @Binding(
        bindingName = "RegulatedAuthorizationBasis",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A legal or regulatory framework against which an authorization is granted, or other reasons for it.",
        valueSet = "http://hl7.org/fhir/ValueSet/regulated-authorization-basis"
    )
    private final List<CodeableConcept> basis;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference holder;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference regulator;
    @Summary
    private final Case _case;

    private RegulatedAuthorization(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        subject = Collections.unmodifiableList(builder.subject);
        type = builder.type;
        description = builder.description;
        region = Collections.unmodifiableList(builder.region);
        status = builder.status;
        statusDate = builder.statusDate;
        validityPeriod = builder.validityPeriod;
        indication = builder.indication;
        intendedUse = builder.intendedUse;
        basis = Collections.unmodifiableList(builder.basis);
        holder = builder.holder;
        regulator = builder.regulator;
        _case = builder._case;
    }

    /**
     * Business identifier for the authorization, typically assigned by the authorizing body.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The product type, treatment, facility or activity that is being authorized.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * Overall type of this authorization, for example drug marketing approval, orphan drug designation.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * General textual supporting information.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * The territory (e.g., country, jurisdiction etc.) in which the authorization has been granted.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getRegion() {
        return region;
    }

    /**
     * The status that is authorised e.g. approved. Intermediate states and actions can be tracked with cases and 
     * applications.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * The date at which the current status was assigned.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getStatusDate() {
        return statusDate;
    }

    /**
     * The time period in which the regulatory approval, clearance or licencing is in effect. As an example, a Marketing 
     * Authorization includes the date of authorization and/or an expiration date.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * Condition for which the use of the regulated product applies.
     * 
     * @return
     *     An immutable object of type {@link CodeableReference} that may be null.
     */
    public CodeableReference getIndication() {
        return indication;
    }

    /**
     * The intended use of the product, e.g. prevention, treatment, diagnosis.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getIntendedUse() {
        return intendedUse;
    }

    /**
     * The legal or regulatory framework against which this authorization is granted, or other reasons for it.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getBasis() {
        return basis;
    }

    /**
     * The organization that has been granted this authorization, by some authoritative body (the 'regulator').
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getHolder() {
        return holder;
    }

    /**
     * The regulatory authority or authorizing body granting the authorization. For example, European Medicines Agency (EMA), 
     * Food and Drug Administration (FDA), Health Canada (HC), etc.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRegulator() {
        return regulator;
    }

    /**
     * The case or regulatory procedure for granting or amending a regulated authorization. An authorization is granted in 
     * response to submissions/applications by those seeking authorization. A case is the administrative process that deals 
     * with the application(s) that relate to this and assesses them. Note: This area is subject to ongoing review and the 
     * workgroup is seeking implementer feedback on its use (see link at bottom of page).
     * 
     * @return
     *     An immutable object of type {@link Case} that may be null.
     */
    public Case getCase() {
        return _case;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !subject.isEmpty() || 
            (type != null) || 
            (description != null) || 
            !region.isEmpty() || 
            (status != null) || 
            (statusDate != null) || 
            (validityPeriod != null) || 
            (indication != null) || 
            (intendedUse != null) || 
            !basis.isEmpty() || 
            (holder != null) || 
            (regulator != null) || 
            (_case != null);
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
                accept(subject, "subject", visitor, Reference.class);
                accept(type, "type", visitor);
                accept(description, "description", visitor);
                accept(region, "region", visitor, CodeableConcept.class);
                accept(status, "status", visitor);
                accept(statusDate, "statusDate", visitor);
                accept(validityPeriod, "validityPeriod", visitor);
                accept(indication, "indication", visitor);
                accept(intendedUse, "intendedUse", visitor);
                accept(basis, "basis", visitor, CodeableConcept.class);
                accept(holder, "holder", visitor);
                accept(regulator, "regulator", visitor);
                accept(_case, "case", visitor);
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
        RegulatedAuthorization other = (RegulatedAuthorization) obj;
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
            Objects.equals(type, other.type) && 
            Objects.equals(description, other.description) && 
            Objects.equals(region, other.region) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusDate, other.statusDate) && 
            Objects.equals(validityPeriod, other.validityPeriod) && 
            Objects.equals(indication, other.indication) && 
            Objects.equals(intendedUse, other.intendedUse) && 
            Objects.equals(basis, other.basis) && 
            Objects.equals(holder, other.holder) && 
            Objects.equals(regulator, other.regulator) && 
            Objects.equals(_case, other._case);
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
                type, 
                description, 
                region, 
                status, 
                statusDate, 
                validityPeriod, 
                indication, 
                intendedUse, 
                basis, 
                holder, 
                regulator, 
                _case);
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
        private List<Reference> subject = new ArrayList<>();
        private CodeableConcept type;
        private Markdown description;
        private List<CodeableConcept> region = new ArrayList<>();
        private CodeableConcept status;
        private DateTime statusDate;
        private Period validityPeriod;
        private CodeableReference indication;
        private CodeableConcept intendedUse;
        private List<CodeableConcept> basis = new ArrayList<>();
        private Reference holder;
        private Reference regulator;
        private Case _case;

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
         * Business identifier for the authorization, typically assigned by the authorizing body.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for the authorization, typically assigned by the authorizing body
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
         * Business identifier for the authorization, typically assigned by the authorizing body.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for the authorization, typically assigned by the authorizing body
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
         * The product type, treatment, facility or activity that is being authorized.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * <li>{@link BiologicallyDerivedProduct}</li>
         * <li>{@link NutritionProduct}</li>
         * <li>{@link PackagedProductDefinition}</li>
         * <li>{@link SubstanceDefinition}</li>
         * <li>{@link DeviceDefinition}</li>
         * <li>{@link ResearchStudy}</li>
         * <li>{@link ActivityDefinition}</li>
         * <li>{@link PlanDefinition}</li>
         * <li>{@link ObservationDefinition}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param subject
         *     The product type, treatment, facility or activity that is being authorized
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference... subject) {
            for (Reference value : subject) {
                this.subject.add(value);
            }
            return this;
        }

        /**
         * The product type, treatment, facility or activity that is being authorized.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * <li>{@link BiologicallyDerivedProduct}</li>
         * <li>{@link NutritionProduct}</li>
         * <li>{@link PackagedProductDefinition}</li>
         * <li>{@link SubstanceDefinition}</li>
         * <li>{@link DeviceDefinition}</li>
         * <li>{@link ResearchStudy}</li>
         * <li>{@link ActivityDefinition}</li>
         * <li>{@link PlanDefinition}</li>
         * <li>{@link ObservationDefinition}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param subject
         *     The product type, treatment, facility or activity that is being authorized
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject = new ArrayList<>(subject);
            return this;
        }

        /**
         * Overall type of this authorization, for example drug marketing approval, orphan drug designation.
         * 
         * @param type
         *     Overall type of this authorization, for example drug marketing approval, orphan drug designation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * General textual supporting information.
         * 
         * @param description
         *     General textual supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * The territory (e.g., country, jurisdiction etc.) in which the authorization has been granted.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param region
         *     The territory in which the authorization has been granted
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder region(CodeableConcept... region) {
            for (CodeableConcept value : region) {
                this.region.add(value);
            }
            return this;
        }

        /**
         * The territory (e.g., country, jurisdiction etc.) in which the authorization has been granted.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param region
         *     The territory in which the authorization has been granted
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder region(Collection<CodeableConcept> region) {
            this.region = new ArrayList<>(region);
            return this;
        }

        /**
         * The status that is authorised e.g. approved. Intermediate states and actions can be tracked with cases and 
         * applications.
         * 
         * @param status
         *     The status that is authorised e.g. approved. Intermediate states can be tracked with cases and applications
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * The date at which the current status was assigned.
         * 
         * @param statusDate
         *     The date at which the current status was assigned
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusDate(DateTime statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * The time period in which the regulatory approval, clearance or licencing is in effect. As an example, a Marketing 
         * Authorization includes the date of authorization and/or an expiration date.
         * 
         * @param validityPeriod
         *     The time period in which the regulatory approval etc. is in effect, e.g. a Marketing Authorization includes the date 
         *     of authorization and/or expiration date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validityPeriod(Period validityPeriod) {
            this.validityPeriod = validityPeriod;
            return this;
        }

        /**
         * Condition for which the use of the regulated product applies.
         * 
         * @param indication
         *     Condition for which the use of the regulated product applies
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder indication(CodeableReference indication) {
            this.indication = indication;
            return this;
        }

        /**
         * The intended use of the product, e.g. prevention, treatment, diagnosis.
         * 
         * @param intendedUse
         *     The intended use of the product, e.g. prevention, treatment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder intendedUse(CodeableConcept intendedUse) {
            this.intendedUse = intendedUse;
            return this;
        }

        /**
         * The legal or regulatory framework against which this authorization is granted, or other reasons for it.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param basis
         *     The legal/regulatory framework or reasons under which this authorization is granted
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basis(CodeableConcept... basis) {
            for (CodeableConcept value : basis) {
                this.basis.add(value);
            }
            return this;
        }

        /**
         * The legal or regulatory framework against which this authorization is granted, or other reasons for it.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param basis
         *     The legal/regulatory framework or reasons under which this authorization is granted
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder basis(Collection<CodeableConcept> basis) {
            this.basis = new ArrayList<>(basis);
            return this;
        }

        /**
         * The organization that has been granted this authorization, by some authoritative body (the 'regulator').
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param holder
         *     The organization that has been granted this authorization, by the regulator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder holder(Reference holder) {
            this.holder = holder;
            return this;
        }

        /**
         * The regulatory authority or authorizing body granting the authorization. For example, European Medicines Agency (EMA), 
         * Food and Drug Administration (FDA), Health Canada (HC), etc.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param regulator
         *     The regulatory authority or authorizing body granting the authorization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder regulator(Reference regulator) {
            this.regulator = regulator;
            return this;
        }

        /**
         * The case or regulatory procedure for granting or amending a regulated authorization. An authorization is granted in 
         * response to submissions/applications by those seeking authorization. A case is the administrative process that deals 
         * with the application(s) that relate to this and assesses them. Note: This area is subject to ongoing review and the 
         * workgroup is seeking implementer feedback on its use (see link at bottom of page).
         * 
         * @param _case
         *     The case or regulatory procedure for granting or amending a regulated authorization. Note: This area is subject to 
         *     ongoing review and the workgroup is seeking implementer feedback on its use (see link at bottom of page)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _case(Case _case) {
            this._case = _case;
            return this;
        }

        /**
         * Build the {@link RegulatedAuthorization}
         * 
         * @return
         *     An immutable object of type {@link RegulatedAuthorization}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid RegulatedAuthorization per the base specification
         */
        @Override
        public RegulatedAuthorization build() {
            RegulatedAuthorization regulatedAuthorization = new RegulatedAuthorization(this);
            if (validating) {
                validate(regulatedAuthorization);
            }
            return regulatedAuthorization;
        }

        protected void validate(RegulatedAuthorization regulatedAuthorization) {
            super.validate(regulatedAuthorization);
            ValidationSupport.checkList(regulatedAuthorization.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(regulatedAuthorization.subject, "subject", Reference.class);
            ValidationSupport.checkList(regulatedAuthorization.region, "region", CodeableConcept.class);
            ValidationSupport.checkList(regulatedAuthorization.basis, "basis", CodeableConcept.class);
            ValidationSupport.checkReferenceType(regulatedAuthorization.subject, "subject", "MedicinalProductDefinition", "BiologicallyDerivedProduct", "NutritionProduct", "PackagedProductDefinition", "SubstanceDefinition", "DeviceDefinition", "ResearchStudy", "ActivityDefinition", "PlanDefinition", "ObservationDefinition", "Practitioner", "Organization", "Location");
            ValidationSupport.checkReferenceType(regulatedAuthorization.holder, "holder", "Organization");
            ValidationSupport.checkReferenceType(regulatedAuthorization.regulator, "regulator", "Organization");
        }

        protected Builder from(RegulatedAuthorization regulatedAuthorization) {
            super.from(regulatedAuthorization);
            identifier.addAll(regulatedAuthorization.identifier);
            subject.addAll(regulatedAuthorization.subject);
            type = regulatedAuthorization.type;
            description = regulatedAuthorization.description;
            region.addAll(regulatedAuthorization.region);
            status = regulatedAuthorization.status;
            statusDate = regulatedAuthorization.statusDate;
            validityPeriod = regulatedAuthorization.validityPeriod;
            indication = regulatedAuthorization.indication;
            intendedUse = regulatedAuthorization.intendedUse;
            basis.addAll(regulatedAuthorization.basis);
            holder = regulatedAuthorization.holder;
            regulator = regulatedAuthorization.regulator;
            _case = regulatedAuthorization._case;
            return this;
        }
    }

    /**
     * The case or regulatory procedure for granting or amending a regulated authorization. An authorization is granted in 
     * response to submissions/applications by those seeking authorization. A case is the administrative process that deals 
     * with the application(s) that relate to this and assesses them. Note: This area is subject to ongoing review and the 
     * workgroup is seeking implementer feedback on its use (see link at bottom of page).
     */
    public static class Case extends BackboneElement {
        @Summary
        private final Identifier identifier;
        @Summary
        @Binding(
            bindingName = "RegulatedAuthorizationCaseType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The type of a case involved in an application.",
            valueSet = "http://hl7.org/fhir/ValueSet/regulated-authorization-case-type"
        )
        private final CodeableConcept type;
        @Summary
        @Binding(
            bindingName = "PublicationStatus",
            strength = BindingStrength.Value.PREFERRED,
            description = "The lifecycle status of an artifact.",
            valueSet = "http://hl7.org/fhir/ValueSet/publication-status"
        )
        private final CodeableConcept status;
        @Summary
        @Choice({ Period.class, DateTime.class })
        private final Element date;
        @Summary
        private final List<RegulatedAuthorization.Case> application;

        private Case(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            type = builder.type;
            status = builder.status;
            date = builder.date;
            application = Collections.unmodifiableList(builder.application);
        }

        /**
         * Identifier by which this case can be referenced.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * The defining type of case.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The status associated with the case.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStatus() {
            return status;
        }

        /**
         * Relevant date for this case.
         * 
         * @return
         *     An immutable object of type {@link Period} or {@link DateTime} that may be null.
         */
        public Element getDate() {
            return date;
        }

        /**
         * A regulatory submission from an organization to a regulator, as part of an assessing case. Multiple applications may 
         * occur over time, with more or different information to support or modify the submission or the authorization. The 
         * applications can be considered as steps within the longer running case or procedure for this authorization process.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Case} that may be empty.
         */
        public List<RegulatedAuthorization.Case> getApplication() {
            return application;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identifier != null) || 
                (type != null) || 
                (status != null) || 
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
                    accept(status, "status", visitor);
                    accept(date, "date", visitor);
                    accept(application, "application", visitor, RegulatedAuthorization.Case.class);
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
            Case other = (Case) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(type, other.type) && 
                Objects.equals(status, other.status) && 
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
                    status, 
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
            private CodeableConcept status;
            private Element date;
            private List<RegulatedAuthorization.Case> application = new ArrayList<>();

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
             * Identifier by which this case can be referenced.
             * 
             * @param identifier
             *     Identifier by which this case can be referenced
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * The defining type of case.
             * 
             * @param type
             *     The defining type of case
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The status associated with the case.
             * 
             * @param status
             *     The status associated with the case
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(CodeableConcept status) {
                this.status = status;
                return this;
            }

            /**
             * Relevant date for this case.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Period}</li>
             * <li>{@link DateTime}</li>
             * </ul>
             * 
             * @param date
             *     Relevant date for this case
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(Element date) {
                this.date = date;
                return this;
            }

            /**
             * A regulatory submission from an organization to a regulator, as part of an assessing case. Multiple applications may 
             * occur over time, with more or different information to support or modify the submission or the authorization. The 
             * applications can be considered as steps within the longer running case or procedure for this authorization process.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param application
             *     Applications submitted to obtain a regulated authorization. Steps within the longer running case or procedure
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder application(RegulatedAuthorization.Case... application) {
                for (RegulatedAuthorization.Case value : application) {
                    this.application.add(value);
                }
                return this;
            }

            /**
             * A regulatory submission from an organization to a regulator, as part of an assessing case. Multiple applications may 
             * occur over time, with more or different information to support or modify the submission or the authorization. The 
             * applications can be considered as steps within the longer running case or procedure for this authorization process.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param application
             *     Applications submitted to obtain a regulated authorization. Steps within the longer running case or procedure
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder application(Collection<RegulatedAuthorization.Case> application) {
                this.application = new ArrayList<>(application);
                return this;
            }

            /**
             * Build the {@link Case}
             * 
             * @return
             *     An immutable object of type {@link Case}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Case per the base specification
             */
            @Override
            public Case build() {
                Case _case = new Case(this);
                if (validating) {
                    validate(_case);
                }
                return _case;
            }

            protected void validate(Case _case) {
                super.validate(_case);
                ValidationSupport.choiceElement(_case.date, "date", Period.class, DateTime.class);
                ValidationSupport.checkList(_case.application, "application", RegulatedAuthorization.Case.class);
                ValidationSupport.requireValueOrChildren(_case);
            }

            protected Builder from(Case _case) {
                super.from(_case);
                identifier = _case.identifier;
                type = _case.type;
                status = _case.status;
                date = _case.date;
                application.addAll(_case.application);
                return this;
            }
        }
    }
}
