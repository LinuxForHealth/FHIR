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
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ConsentDataMeaning;
import com.ibm.fhir.model.type.code.ConsentProvisionType;
import com.ibm.fhir.model.type.code.ConsentState;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A record of a healthcare consumer’s choices, which permits or denies identified recipient(s) or recipient role(s) to 
 * perform one or more actions within a given policy context, for specific purposes and periods of time.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "ppc-1",
    level = "Rule",
    location = "(base)",
    description = "Either a Policy or PolicyRule",
    expression = "policy.exists() or policyRule.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Consent"
)
@Constraint(
    id = "ppc-2",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=privacy, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='patient-privacy').exists().not()",
    source = "http://hl7.org/fhir/StructureDefinition/Consent"
)
@Constraint(
    id = "ppc-3",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=research, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='research').exists().not()",
    source = "http://hl7.org/fhir/StructureDefinition/Consent"
)
@Constraint(
    id = "ppc-4",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=adr, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='adr').exists().not()",
    source = "http://hl7.org/fhir/StructureDefinition/Consent"
)
@Constraint(
    id = "ppc-5",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=treatment, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='treatment').exists().not()",
    source = "http://hl7.org/fhir/StructureDefinition/Consent"
)
@Constraint(
    id = "consent-6",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/consent-scope",
    expression = "scope.exists() and scope.memberOf('http://hl7.org/fhir/ValueSet/consent-scope', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Consent",
    generated = true
)
@Constraint(
    id = "consent-7",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/consent-category",
    expression = "category.exists() and category.all(memberOf('http://hl7.org/fhir/ValueSet/consent-category', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Consent",
    generated = true
)
@Constraint(
    id = "consent-8",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/consent-policy",
    expression = "policyRule.exists() implies (policyRule.memberOf('http://hl7.org/fhir/ValueSet/consent-policy', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Consent",
    generated = true
)
@Constraint(
    id = "consent-9",
    level = "Warning",
    location = "provision.actor.role",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/security-role-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/security-role-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Consent",
    generated = true
)
@Constraint(
    id = "consent-10",
    level = "Warning",
    location = "provision.securityLabel",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/security-labels",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/security-labels', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Consent",
    generated = true
)
@Constraint(
    id = "consent-11",
    level = "Warning",
    location = "provision.purpose",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-PurposeOfUse",
    expression = "$this.memberOf('http://terminology.hl7.org/ValueSet/v3-PurposeOfUse', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Consent",
    generated = true
)
@Constraint(
    id = "consent-12",
    level = "Warning",
    location = "provision.class",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/consent-content-class",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/consent-content-class', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Consent",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Consent extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ConsentState",
        strength = BindingStrength.Value.REQUIRED,
        description = "Indicates the state of the consent.",
        valueSet = "http://hl7.org/fhir/ValueSet/consent-state-codes|4.3.0"
    )
    @Required
    private final ConsentState status;
    @Summary
    @Binding(
        bindingName = "ConsentScope",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The four anticipated uses for the Consent Resource.",
        valueSet = "http://hl7.org/fhir/ValueSet/consent-scope"
    )
    @Required
    private final CodeableConcept scope;
    @Summary
    @Binding(
        bindingName = "ConsentCategory",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "A classification of the type of consents found in a consent statement.",
        valueSet = "http://hl7.org/fhir/ValueSet/consent-category"
    )
    @Required
    private final List<CodeableConcept> category;
    @Summary
    @ReferenceTarget({ "Patient" })
    private final Reference patient;
    @Summary
    private final DateTime dateTime;
    @Summary
    @ReferenceTarget({ "Organization", "Patient", "Practitioner", "RelatedPerson", "PractitionerRole" })
    private final List<Reference> performer;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> organization;
    @Summary
    @ReferenceTarget({ "Consent", "DocumentReference", "Contract", "QuestionnaireResponse" })
    @Choice({ Attachment.class, Reference.class })
    private final Element source;
    private final List<Policy> policy;
    @Summary
    @Binding(
        bindingName = "ConsentPolicyRule",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Regulatory policy examples.",
        valueSet = "http://hl7.org/fhir/ValueSet/consent-policy"
    )
    private final CodeableConcept policyRule;
    @Summary
    private final List<Verification> verification;
    @Summary
    private final Provision provision;

    private Consent(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        scope = builder.scope;
        category = Collections.unmodifiableList(builder.category);
        patient = builder.patient;
        dateTime = builder.dateTime;
        performer = Collections.unmodifiableList(builder.performer);
        organization = Collections.unmodifiableList(builder.organization);
        source = builder.source;
        policy = Collections.unmodifiableList(builder.policy);
        policyRule = builder.policyRule;
        verification = Collections.unmodifiableList(builder.verification);
        provision = builder.provision;
    }

    /**
     * Unique identifier for this copy of the Consent Statement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Indicates the current state of this consent.
     * 
     * @return
     *     An immutable object of type {@link ConsentState} that is non-null.
     */
    public ConsentState getStatus() {
        return status;
    }

    /**
     * A selector of the type of consent being presented: ADR, Privacy, Treatment, Research. This list is now extensible.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getScope() {
        return scope;
    }

    /**
     * A classification of the type of consents found in the statement. This element supports indexing and retrieval of 
     * consent statements.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that is non-empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * The patient/healthcare consumer to whom this consent applies.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * When this Consent was issued / created / indexed.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDateTime() {
        return dateTime;
    }

    /**
     * Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the 
     * Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or 
     * limitations on authorizations and enforcement of prohibitions.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPerformer() {
        return performer;
    }

    /**
     * The organization that manages the consent, and the framework within which it is executed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getOrganization() {
        return organization;
    }

    /**
     * The source on which this consent statement is based. The source might be a scanned original paper form, or a reference 
     * to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the 
     * original consent document.
     * 
     * @return
     *     An immutable object of type {@link Attachment} or {@link Reference} that may be null.
     */
    public Element getSource() {
        return source;
    }

    /**
     * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
     * defined jurisdictionally, or in law.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Policy} that may be empty.
     */
    public List<Policy> getPolicy() {
        return policy;
    }

    /**
     * A reference to the specific base computable regulation or policy.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPolicyRule() {
        return policyRule;
    }

    /**
     * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
     * or another authorized person.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Verification} that may be empty.
     */
    public List<Verification> getVerification() {
        return verification;
    }

    /**
     * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
     * 
     * @return
     *     An immutable object of type {@link Provision} that may be null.
     */
    public Provision getProvision() {
        return provision;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (scope != null) || 
            !category.isEmpty() || 
            (patient != null) || 
            (dateTime != null) || 
            !performer.isEmpty() || 
            !organization.isEmpty() || 
            (source != null) || 
            !policy.isEmpty() || 
            (policyRule != null) || 
            !verification.isEmpty() || 
            (provision != null);
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
                accept(status, "status", visitor);
                accept(scope, "scope", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(patient, "patient", visitor);
                accept(dateTime, "dateTime", visitor);
                accept(performer, "performer", visitor, Reference.class);
                accept(organization, "organization", visitor, Reference.class);
                accept(source, "source", visitor);
                accept(policy, "policy", visitor, Policy.class);
                accept(policyRule, "policyRule", visitor);
                accept(verification, "verification", visitor, Verification.class);
                accept(provision, "provision", visitor);
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
        Consent other = (Consent) obj;
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
            Objects.equals(scope, other.scope) && 
            Objects.equals(category, other.category) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(dateTime, other.dateTime) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(organization, other.organization) && 
            Objects.equals(source, other.source) && 
            Objects.equals(policy, other.policy) && 
            Objects.equals(policyRule, other.policyRule) && 
            Objects.equals(verification, other.verification) && 
            Objects.equals(provision, other.provision);
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
                scope, 
                category, 
                patient, 
                dateTime, 
                performer, 
                organization, 
                source, 
                policy, 
                policyRule, 
                verification, 
                provision);
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
        private ConsentState status;
        private CodeableConcept scope;
        private List<CodeableConcept> category = new ArrayList<>();
        private Reference patient;
        private DateTime dateTime;
        private List<Reference> performer = new ArrayList<>();
        private List<Reference> organization = new ArrayList<>();
        private Element source;
        private List<Policy> policy = new ArrayList<>();
        private CodeableConcept policyRule;
        private List<Verification> verification = new ArrayList<>();
        private Provision provision;

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
         * Unique identifier for this copy of the Consent Statement.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier for this record (external references)
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
         * Unique identifier for this copy of the Consent Statement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier for this record (external references)
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
         * Indicates the current state of this consent.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | proposed | active | rejected | inactive | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ConsentState status) {
            this.status = status;
            return this;
        }

        /**
         * A selector of the type of consent being presented: ADR, Privacy, Treatment, Research. This list is now extensible.
         * 
         * <p>This element is required.
         * 
         * @param scope
         *     Which of the four areas this resource covers (extensible)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder scope(CodeableConcept scope) {
            this.scope = scope;
            return this;
        }

        /**
         * A classification of the type of consents found in the statement. This element supports indexing and retrieval of 
         * consent statements.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param category
         *     Classification of the consent statement - for indexing/retrieval
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * A classification of the type of consents found in the statement. This element supports indexing and retrieval of 
         * consent statements.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param category
         *     Classification of the consent statement - for indexing/retrieval
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * The patient/healthcare consumer to whom this consent applies.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Who the consent applies to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * When this Consent was issued / created / indexed.
         * 
         * @param dateTime
         *     When this Consent was created or indexed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dateTime(DateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        /**
         * Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the 
         * Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or 
         * limitations on authorizations and enforcement of prohibitions.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param performer
         *     Who is agreeing to the policy and rules
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Reference... performer) {
            for (Reference value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the 
         * Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or 
         * limitations on authorizations and enforcement of prohibitions.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param performer
         *     Who is agreeing to the policy and rules
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder performer(Collection<Reference> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * The organization that manages the consent, and the framework within which it is executed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param organization
         *     Custodian of the consent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder organization(Reference... organization) {
            for (Reference value : organization) {
                this.organization.add(value);
            }
            return this;
        }

        /**
         * The organization that manages the consent, and the framework within which it is executed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param organization
         *     Custodian of the consent
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder organization(Collection<Reference> organization) {
            this.organization = new ArrayList<>(organization);
            return this;
        }

        /**
         * The source on which this consent statement is based. The source might be a scanned original paper form, or a reference 
         * to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the 
         * original consent document.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Attachment}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Consent}</li>
         * <li>{@link DocumentReference}</li>
         * <li>{@link Contract}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * </ul>
         * 
         * @param source
         *     Source from which this consent is taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Element source) {
            this.source = source;
            return this;
        }

        /**
         * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
         * defined jurisdictionally, or in law.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param policy
         *     Policies covered by this consent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder policy(Policy... policy) {
            for (Policy value : policy) {
                this.policy.add(value);
            }
            return this;
        }

        /**
         * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
         * defined jurisdictionally, or in law.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param policy
         *     Policies covered by this consent
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder policy(Collection<Policy> policy) {
            this.policy = new ArrayList<>(policy);
            return this;
        }

        /**
         * A reference to the specific base computable regulation or policy.
         * 
         * @param policyRule
         *     Regulation that this consents to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder policyRule(CodeableConcept policyRule) {
            this.policyRule = policyRule;
            return this;
        }

        /**
         * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
         * or another authorized person.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param verification
         *     Consent Verified by patient or family
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder verification(Verification... verification) {
            for (Verification value : verification) {
                this.verification.add(value);
            }
            return this;
        }

        /**
         * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
         * or another authorized person.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param verification
         *     Consent Verified by patient or family
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder verification(Collection<Verification> verification) {
            this.verification = new ArrayList<>(verification);
            return this;
        }

        /**
         * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
         * 
         * @param provision
         *     Constraints to the base Consent.policyRule
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder provision(Provision provision) {
            this.provision = provision;
            return this;
        }

        /**
         * Build the {@link Consent}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>scope</li>
         * <li>category</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Consent}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Consent per the base specification
         */
        @Override
        public Consent build() {
            Consent consent = new Consent(this);
            if (validating) {
                validate(consent);
            }
            return consent;
        }

        protected void validate(Consent consent) {
            super.validate(consent);
            ValidationSupport.checkList(consent.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(consent.status, "status");
            ValidationSupport.requireNonNull(consent.scope, "scope");
            ValidationSupport.checkNonEmptyList(consent.category, "category", CodeableConcept.class);
            ValidationSupport.checkList(consent.performer, "performer", Reference.class);
            ValidationSupport.checkList(consent.organization, "organization", Reference.class);
            ValidationSupport.choiceElement(consent.source, "source", Attachment.class, Reference.class);
            ValidationSupport.checkList(consent.policy, "policy", Policy.class);
            ValidationSupport.checkList(consent.verification, "verification", Verification.class);
            ValidationSupport.checkReferenceType(consent.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(consent.performer, "performer", "Organization", "Patient", "Practitioner", "RelatedPerson", "PractitionerRole");
            ValidationSupport.checkReferenceType(consent.organization, "organization", "Organization");
            ValidationSupport.checkReferenceType(consent.source, "source", "Consent", "DocumentReference", "Contract", "QuestionnaireResponse");
        }

        protected Builder from(Consent consent) {
            super.from(consent);
            identifier.addAll(consent.identifier);
            status = consent.status;
            scope = consent.scope;
            category.addAll(consent.category);
            patient = consent.patient;
            dateTime = consent.dateTime;
            performer.addAll(consent.performer);
            organization.addAll(consent.organization);
            source = consent.source;
            policy.addAll(consent.policy);
            policyRule = consent.policyRule;
            verification.addAll(consent.verification);
            provision = consent.provision;
            return this;
        }
    }

    /**
     * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
     * defined jurisdictionally, or in law.
     */
    public static class Policy extends BackboneElement {
        private final Uri authority;
        private final Uri uri;

        private Policy(Builder builder) {
            super(builder);
            authority = builder.authority;
            uri = builder.uri;
        }

        /**
         * Entity or Organization having regulatory jurisdiction or accountability for enforcing policies pertaining to Consent 
         * Directives.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getAuthority() {
            return authority;
        }

        /**
         * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
         * defined jurisdictionally, or in law.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getUri() {
            return uri;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (authority != null) || 
                (uri != null);
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
                    accept(authority, "authority", visitor);
                    accept(uri, "uri", visitor);
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
            Policy other = (Policy) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(authority, other.authority) && 
                Objects.equals(uri, other.uri);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    authority, 
                    uri);
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
            private Uri authority;
            private Uri uri;

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
             * Entity or Organization having regulatory jurisdiction or accountability for enforcing policies pertaining to Consent 
             * Directives.
             * 
             * @param authority
             *     Enforcement source for policy
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder authority(Uri authority) {
                this.authority = authority;
                return this;
            }

            /**
             * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
             * defined jurisdictionally, or in law.
             * 
             * @param uri
             *     Specific policy covered by this consent
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder uri(Uri uri) {
                this.uri = uri;
                return this;
            }

            /**
             * Build the {@link Policy}
             * 
             * @return
             *     An immutable object of type {@link Policy}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Policy per the base specification
             */
            @Override
            public Policy build() {
                Policy policy = new Policy(this);
                if (validating) {
                    validate(policy);
                }
                return policy;
            }

            protected void validate(Policy policy) {
                super.validate(policy);
                ValidationSupport.requireValueOrChildren(policy);
            }

            protected Builder from(Policy policy) {
                super.from(policy);
                authority = policy.authority;
                uri = policy.uri;
                return this;
            }
        }
    }

    /**
     * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
     * or another authorized person.
     */
    public static class Verification extends BackboneElement {
        @Summary
        @Required
        private final Boolean verified;
        @ReferenceTarget({ "Patient", "RelatedPerson" })
        private final Reference verifiedWith;
        private final DateTime verificationDate;

        private Verification(Builder builder) {
            super(builder);
            verified = builder.verified;
            verifiedWith = builder.verifiedWith;
            verificationDate = builder.verificationDate;
        }

        /**
         * Has the instruction been verified.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getVerified() {
            return verified;
        }

        /**
         * Who verified the instruction (Patient, Relative or other Authorized Person).
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getVerifiedWith() {
            return verifiedWith;
        }

        /**
         * Date verification was collected.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getVerificationDate() {
            return verificationDate;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (verified != null) || 
                (verifiedWith != null) || 
                (verificationDate != null);
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
                    accept(verified, "verified", visitor);
                    accept(verifiedWith, "verifiedWith", visitor);
                    accept(verificationDate, "verificationDate", visitor);
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
            Verification other = (Verification) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(verified, other.verified) && 
                Objects.equals(verifiedWith, other.verifiedWith) && 
                Objects.equals(verificationDate, other.verificationDate);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    verified, 
                    verifiedWith, 
                    verificationDate);
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
            private Boolean verified;
            private Reference verifiedWith;
            private DateTime verificationDate;

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
             * Convenience method for setting {@code verified}.
             * 
             * <p>This element is required.
             * 
             * @param verified
             *     Has been verified
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #verified(com.ibm.fhir.model.type.Boolean)
             */
            public Builder verified(java.lang.Boolean verified) {
                this.verified = (verified == null) ? null : Boolean.of(verified);
                return this;
            }

            /**
             * Has the instruction been verified.
             * 
             * <p>This element is required.
             * 
             * @param verified
             *     Has been verified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder verified(Boolean verified) {
                this.verified = verified;
                return this;
            }

            /**
             * Who verified the instruction (Patient, Relative or other Authorized Person).
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param verifiedWith
             *     Person who verified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder verifiedWith(Reference verifiedWith) {
                this.verifiedWith = verifiedWith;
                return this;
            }

            /**
             * Date verification was collected.
             * 
             * @param verificationDate
             *     When consent verified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder verificationDate(DateTime verificationDate) {
                this.verificationDate = verificationDate;
                return this;
            }

            /**
             * Build the {@link Verification}
             * 
             * <p>Required elements:
             * <ul>
             * <li>verified</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Verification}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Verification per the base specification
             */
            @Override
            public Verification build() {
                Verification verification = new Verification(this);
                if (validating) {
                    validate(verification);
                }
                return verification;
            }

            protected void validate(Verification verification) {
                super.validate(verification);
                ValidationSupport.requireNonNull(verification.verified, "verified");
                ValidationSupport.checkReferenceType(verification.verifiedWith, "verifiedWith", "Patient", "RelatedPerson");
                ValidationSupport.requireValueOrChildren(verification);
            }

            protected Builder from(Verification verification) {
                super.from(verification);
                verified = verification.verified;
                verifiedWith = verification.verifiedWith;
                verificationDate = verification.verificationDate;
                return this;
            }
        }
    }

    /**
     * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
     */
    public static class Provision extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ConsentProvisionType",
            strength = BindingStrength.Value.REQUIRED,
            description = "How a rule statement is applied, such as adding additional consent or removing consent.",
            valueSet = "http://hl7.org/fhir/ValueSet/consent-provision-type|4.3.0"
        )
        private final ConsentProvisionType type;
        @Summary
        private final Period period;
        private final List<Actor> actor;
        @Summary
        @Binding(
            bindingName = "ConsentAction",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Detailed codes for the consent action.",
            valueSet = "http://hl7.org/fhir/ValueSet/consent-action"
        )
        private final List<CodeableConcept> action;
        @Summary
        @Binding(
            bindingName = "SecurityLabels",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Security Labels from the Healthcare Privacy and Security Classification System.",
            valueSet = "http://hl7.org/fhir/ValueSet/security-labels"
        )
        private final List<Coding> securityLabel;
        @Summary
        @Binding(
            bindingName = "PurposeOfUse",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "What purposes of use are controlled by this exception. If more than one label is specified, operations must have all the specified labels.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-PurposeOfUse"
        )
        private final List<Coding> purpose;
        @Summary
        @Binding(
            bindingName = "ConsentContentClass",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The class (type) of information a consent rule covers.",
            valueSet = "http://hl7.org/fhir/ValueSet/consent-content-class"
        )
        private final List<Coding> clazz;
        @Summary
        @Binding(
            bindingName = "ConsentContentCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "If this code is found in an instance, then the exception applies.",
            valueSet = "http://hl7.org/fhir/ValueSet/consent-content-code"
        )
        private final List<CodeableConcept> code;
        @Summary
        private final Period dataPeriod;
        @Summary
        private final List<Data> data;
        private final List<Consent.Provision> provision;

        private Provision(Builder builder) {
            super(builder);
            type = builder.type;
            period = builder.period;
            actor = Collections.unmodifiableList(builder.actor);
            action = Collections.unmodifiableList(builder.action);
            securityLabel = Collections.unmodifiableList(builder.securityLabel);
            purpose = Collections.unmodifiableList(builder.purpose);
            clazz = Collections.unmodifiableList(builder.clazz);
            code = Collections.unmodifiableList(builder.code);
            dataPeriod = builder.dataPeriod;
            data = Collections.unmodifiableList(builder.data);
            provision = Collections.unmodifiableList(builder.provision);
        }

        /**
         * Action to take - permit or deny - when the rule conditions are met. Not permitted in root rule, required in all nested 
         * rules.
         * 
         * @return
         *     An immutable object of type {@link ConsentProvisionType} that may be null.
         */
        public ConsentProvisionType getType() {
            return type;
        }

        /**
         * The timeframe in this rule is valid.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
         * 'admitting officers').
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Actor} that may be empty.
         */
        public List<Actor> getActor() {
            return actor;
        }

        /**
         * Actions controlled by this Rule.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getAction() {
            return action;
        }

        /**
         * A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled 
         * by this exception.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
         */
        public List<Coding> getSecurityLabel() {
            return securityLabel;
        }

        /**
         * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
         */
        public List<Coding> getPurpose() {
            return purpose;
        }

        /**
         * The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA 
         * document, or some other type that indicates what sort of information the consent relates to.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
         */
        public List<Coding> getClazz() {
            return clazz;
        }

        /**
         * If this code is found in an instance, then the rule applies.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * Clinical or Operational Relevant period of time that bounds the data controlled by this rule.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getDataPeriod() {
            return dataPeriod;
        }

        /**
         * The resources controlled by this rule if specific resources are referenced.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Data} that may be empty.
         */
        public List<Data> getData() {
            return data;
        }

        /**
         * Rules which provide exceptions to the base rule or subrules.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Provision} that may be empty.
         */
        public List<Consent.Provision> getProvision() {
            return provision;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (period != null) || 
                !actor.isEmpty() || 
                !action.isEmpty() || 
                !securityLabel.isEmpty() || 
                !purpose.isEmpty() || 
                !clazz.isEmpty() || 
                !code.isEmpty() || 
                (dataPeriod != null) || 
                !data.isEmpty() || 
                !provision.isEmpty();
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
                    accept(type, "type", visitor);
                    accept(period, "period", visitor);
                    accept(actor, "actor", visitor, Actor.class);
                    accept(action, "action", visitor, CodeableConcept.class);
                    accept(securityLabel, "securityLabel", visitor, Coding.class);
                    accept(purpose, "purpose", visitor, Coding.class);
                    accept(clazz, "class", visitor, Coding.class);
                    accept(code, "code", visitor, CodeableConcept.class);
                    accept(dataPeriod, "dataPeriod", visitor);
                    accept(data, "data", visitor, Data.class);
                    accept(provision, "provision", visitor, Consent.Provision.class);
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
            Provision other = (Provision) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(period, other.period) && 
                Objects.equals(actor, other.actor) && 
                Objects.equals(action, other.action) && 
                Objects.equals(securityLabel, other.securityLabel) && 
                Objects.equals(purpose, other.purpose) && 
                Objects.equals(clazz, other.clazz) && 
                Objects.equals(code, other.code) && 
                Objects.equals(dataPeriod, other.dataPeriod) && 
                Objects.equals(data, other.data) && 
                Objects.equals(provision, other.provision);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    period, 
                    actor, 
                    action, 
                    securityLabel, 
                    purpose, 
                    clazz, 
                    code, 
                    dataPeriod, 
                    data, 
                    provision);
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
            private ConsentProvisionType type;
            private Period period;
            private List<Actor> actor = new ArrayList<>();
            private List<CodeableConcept> action = new ArrayList<>();
            private List<Coding> securityLabel = new ArrayList<>();
            private List<Coding> purpose = new ArrayList<>();
            private List<Coding> clazz = new ArrayList<>();
            private List<CodeableConcept> code = new ArrayList<>();
            private Period dataPeriod;
            private List<Data> data = new ArrayList<>();
            private List<Consent.Provision> provision = new ArrayList<>();

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
             * Action to take - permit or deny - when the rule conditions are met. Not permitted in root rule, required in all nested 
             * rules.
             * 
             * @param type
             *     deny | permit
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(ConsentProvisionType type) {
                this.type = type;
                return this;
            }

            /**
             * The timeframe in this rule is valid.
             * 
             * @param period
             *     Timeframe for this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
             * 'admitting officers').
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param actor
             *     Who|what controlled by this rule (or group, by role)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Actor... actor) {
                for (Actor value : actor) {
                    this.actor.add(value);
                }
                return this;
            }

            /**
             * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
             * 'admitting officers').
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param actor
             *     Who|what controlled by this rule (or group, by role)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder actor(Collection<Actor> actor) {
                this.actor = new ArrayList<>(actor);
                return this;
            }

            /**
             * Actions controlled by this Rule.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param action
             *     Actions controlled by this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(CodeableConcept... action) {
                for (CodeableConcept value : action) {
                    this.action.add(value);
                }
                return this;
            }

            /**
             * Actions controlled by this Rule.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param action
             *     Actions controlled by this rule
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder action(Collection<CodeableConcept> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled 
             * by this exception.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param securityLabel
             *     Security Labels that define affected resources
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder securityLabel(Coding... securityLabel) {
                for (Coding value : securityLabel) {
                    this.securityLabel.add(value);
                }
                return this;
            }

            /**
             * A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled 
             * by this exception.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param securityLabel
             *     Security Labels that define affected resources
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder securityLabel(Collection<Coding> securityLabel) {
                this.securityLabel = new ArrayList<>(securityLabel);
                return this;
            }

            /**
             * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param purpose
             *     Context of activities covered by this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder purpose(Coding... purpose) {
                for (Coding value : purpose) {
                    this.purpose.add(value);
                }
                return this;
            }

            /**
             * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param purpose
             *     Context of activities covered by this rule
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder purpose(Collection<Coding> purpose) {
                this.purpose = new ArrayList<>(purpose);
                return this;
            }

            /**
             * The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA 
             * document, or some other type that indicates what sort of information the consent relates to.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param clazz
             *     e.g. Resource Type, Profile, CDA, etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder clazz(Coding... clazz) {
                for (Coding value : clazz) {
                    this.clazz.add(value);
                }
                return this;
            }

            /**
             * The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA 
             * document, or some other type that indicates what sort of information the consent relates to.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param clazz
             *     e.g. Resource Type, Profile, CDA, etc.
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder clazz(Collection<Coding> clazz) {
                this.clazz = new ArrayList<>(clazz);
                return this;
            }

            /**
             * If this code is found in an instance, then the rule applies.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     e.g. LOINC or SNOMED CT code, etc. in the content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept... code) {
                for (CodeableConcept value : code) {
                    this.code.add(value);
                }
                return this;
            }

            /**
             * If this code is found in an instance, then the rule applies.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     e.g. LOINC or SNOMED CT code, etc. in the content
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * Clinical or Operational Relevant period of time that bounds the data controlled by this rule.
             * 
             * @param dataPeriod
             *     Timeframe for data controlled by this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dataPeriod(Period dataPeriod) {
                this.dataPeriod = dataPeriod;
                return this;
            }

            /**
             * The resources controlled by this rule if specific resources are referenced.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param data
             *     Data controlled by this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder data(Data... data) {
                for (Data value : data) {
                    this.data.add(value);
                }
                return this;
            }

            /**
             * The resources controlled by this rule if specific resources are referenced.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param data
             *     Data controlled by this rule
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder data(Collection<Data> data) {
                this.data = new ArrayList<>(data);
                return this;
            }

            /**
             * Rules which provide exceptions to the base rule or subrules.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param provision
             *     Nested Exception Rules
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provision(Consent.Provision... provision) {
                for (Consent.Provision value : provision) {
                    this.provision.add(value);
                }
                return this;
            }

            /**
             * Rules which provide exceptions to the base rule or subrules.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param provision
             *     Nested Exception Rules
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder provision(Collection<Consent.Provision> provision) {
                this.provision = new ArrayList<>(provision);
                return this;
            }

            /**
             * Build the {@link Provision}
             * 
             * @return
             *     An immutable object of type {@link Provision}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Provision per the base specification
             */
            @Override
            public Provision build() {
                Provision provision = new Provision(this);
                if (validating) {
                    validate(provision);
                }
                return provision;
            }

            protected void validate(Provision provision) {
                super.validate(provision);
                ValidationSupport.checkList(provision.actor, "actor", Actor.class);
                ValidationSupport.checkList(provision.action, "action", CodeableConcept.class);
                ValidationSupport.checkList(provision.securityLabel, "securityLabel", Coding.class);
                ValidationSupport.checkList(provision.purpose, "purpose", Coding.class);
                ValidationSupport.checkList(provision.clazz, "class", Coding.class);
                ValidationSupport.checkList(provision.code, "code", CodeableConcept.class);
                ValidationSupport.checkList(provision.data, "data", Data.class);
                ValidationSupport.checkList(provision.provision, "provision", Consent.Provision.class);
                ValidationSupport.requireValueOrChildren(provision);
            }

            protected Builder from(Provision provision) {
                super.from(provision);
                type = provision.type;
                period = provision.period;
                actor.addAll(provision.actor);
                action.addAll(provision.action);
                securityLabel.addAll(provision.securityLabel);
                purpose.addAll(provision.purpose);
                clazz.addAll(provision.clazz);
                code.addAll(provision.code);
                dataPeriod = provision.dataPeriod;
                data.addAll(provision.data);
                this.provision.addAll(provision.provision);
                return this;
            }
        }

        /**
         * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
         * 'admitting officers').
         */
        public static class Actor extends BackboneElement {
            @Binding(
                bindingName = "ConsentActorRole",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "How an actor is involved in the consent considerations.",
                valueSet = "http://hl7.org/fhir/ValueSet/security-role-type"
            )
            @Required
            private final CodeableConcept role;
            @ReferenceTarget({ "Device", "Group", "CareTeam", "Organization", "Patient", "Practitioner", "RelatedPerson", "PractitionerRole" })
            @Required
            private final Reference reference;

            private Actor(Builder builder) {
                super(builder);
                role = builder.role;
                reference = builder.reference;
            }

            /**
             * How the individual is involved in the resources content that is described in the exception.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getRole() {
                return role;
            }

            /**
             * The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some 
             * property they share (e.g. 'admitting officers').
             * 
             * @return
             *     An immutable object of type {@link Reference} that is non-null.
             */
            public Reference getReference() {
                return reference;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (role != null) || 
                    (reference != null);
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
                        accept(role, "role", visitor);
                        accept(reference, "reference", visitor);
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
                Actor other = (Actor) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(role, other.role) && 
                    Objects.equals(reference, other.reference);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        role, 
                        reference);
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
                private CodeableConcept role;
                private Reference reference;

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
                 * How the individual is involved in the resources content that is described in the exception.
                 * 
                 * <p>This element is required.
                 * 
                 * @param role
                 *     How the actor is involved
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder role(CodeableConcept role) {
                    this.role = role;
                    return this;
                }

                /**
                 * The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some 
                 * property they share (e.g. 'admitting officers').
                 * 
                 * <p>This element is required.
                 * 
                 * <p>Allowed resource types for this reference:
                 * <ul>
                 * <li>{@link Device}</li>
                 * <li>{@link Group}</li>
                 * <li>{@link CareTeam}</li>
                 * <li>{@link Organization}</li>
                 * <li>{@link Patient}</li>
                 * <li>{@link Practitioner}</li>
                 * <li>{@link RelatedPerson}</li>
                 * <li>{@link PractitionerRole}</li>
                 * </ul>
                 * 
                 * @param reference
                 *     Resource for the actor (or group, by role)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reference(Reference reference) {
                    this.reference = reference;
                    return this;
                }

                /**
                 * Build the {@link Actor}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>role</li>
                 * <li>reference</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Actor}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Actor per the base specification
                 */
                @Override
                public Actor build() {
                    Actor actor = new Actor(this);
                    if (validating) {
                        validate(actor);
                    }
                    return actor;
                }

                protected void validate(Actor actor) {
                    super.validate(actor);
                    ValidationSupport.requireNonNull(actor.role, "role");
                    ValidationSupport.requireNonNull(actor.reference, "reference");
                    ValidationSupport.checkReferenceType(actor.reference, "reference", "Device", "Group", "CareTeam", "Organization", "Patient", "Practitioner", "RelatedPerson", "PractitionerRole");
                    ValidationSupport.requireValueOrChildren(actor);
                }

                protected Builder from(Actor actor) {
                    super.from(actor);
                    role = actor.role;
                    reference = actor.reference;
                    return this;
                }
            }
        }

        /**
         * The resources controlled by this rule if specific resources are referenced.
         */
        public static class Data extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "ConsentDataMeaning",
                strength = BindingStrength.Value.REQUIRED,
                description = "How a resource reference is interpreted when testing consent restrictions.",
                valueSet = "http://hl7.org/fhir/ValueSet/consent-data-meaning|4.3.0"
            )
            @Required
            private final ConsentDataMeaning meaning;
            @Summary
            @Required
            private final Reference reference;

            private Data(Builder builder) {
                super(builder);
                meaning = builder.meaning;
                reference = builder.reference;
            }

            /**
             * How the resource reference is interpreted when testing consent restrictions.
             * 
             * @return
             *     An immutable object of type {@link ConsentDataMeaning} that is non-null.
             */
            public ConsentDataMeaning getMeaning() {
                return meaning;
            }

            /**
             * A reference to a specific resource that defines which resources are covered by this consent.
             * 
             * @return
             *     An immutable object of type {@link Reference} that is non-null.
             */
            public Reference getReference() {
                return reference;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (meaning != null) || 
                    (reference != null);
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
                        accept(meaning, "meaning", visitor);
                        accept(reference, "reference", visitor);
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
                Data other = (Data) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(meaning, other.meaning) && 
                    Objects.equals(reference, other.reference);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        meaning, 
                        reference);
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
                private ConsentDataMeaning meaning;
                private Reference reference;

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
                 * How the resource reference is interpreted when testing consent restrictions.
                 * 
                 * <p>This element is required.
                 * 
                 * @param meaning
                 *     instance | related | dependents | authoredby
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder meaning(ConsentDataMeaning meaning) {
                    this.meaning = meaning;
                    return this;
                }

                /**
                 * A reference to a specific resource that defines which resources are covered by this consent.
                 * 
                 * <p>This element is required.
                 * 
                 * @param reference
                 *     The actual data reference
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reference(Reference reference) {
                    this.reference = reference;
                    return this;
                }

                /**
                 * Build the {@link Data}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>meaning</li>
                 * <li>reference</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Data}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Data per the base specification
                 */
                @Override
                public Data build() {
                    Data data = new Data(this);
                    if (validating) {
                        validate(data);
                    }
                    return data;
                }

                protected void validate(Data data) {
                    super.validate(data);
                    ValidationSupport.requireNonNull(data.meaning, "meaning");
                    ValidationSupport.requireNonNull(data.reference, "reference");
                    ValidationSupport.requireValueOrChildren(data);
                }

                protected Builder from(Data data) {
                    super.from(data);
                    meaning = data.meaning;
                    reference = data.reference;
                    return this;
                }
            }
        }
    }
}
