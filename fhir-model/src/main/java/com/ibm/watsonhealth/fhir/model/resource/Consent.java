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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ConsentDataMeaning;
import com.ibm.watsonhealth.fhir.model.type.ConsentProvisionType;
import com.ibm.watsonhealth.fhir.model.type.ConsentState;
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
 * A record of a healthcare consumer’s choices, which permits or denies identified recipient(s) or recipient role(s) to 
 * perform one or more actions within a given policy context, for specific purposes and periods of time.
 * </p>
 */
@Constraint(
    id = "ppc-1",
    level = "Rule",
    location = "(base)",
    description = "Either a Policy or PolicyRule",
    expression = "policy.exists() or policyRule.exists()"
)
@Constraint(
    id = "ppc-2",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=privacy, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='patient-privacy').exists().not()"
)
@Constraint(
    id = "ppc-3",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=research, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='research').exists().not()"
)
@Constraint(
    id = "ppc-4",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=adr, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='adr').exists().not()"
)
@Constraint(
    id = "ppc-5",
    level = "Rule",
    location = "(base)",
    description = "IF Scope=treatment, there must be a patient",
    expression = "patient.exists() or scope.coding.where(system='something' and code='treatment').exists().not()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Consent extends DomainResource {
    private final List<Identifier> identifier;
    private final ConsentState status;
    private final CodeableConcept scope;
    private final List<CodeableConcept> category;
    private final Reference patient;
    private final DateTime dateTime;
    private final List<Reference> performer;
    private final List<Reference> organization;
    private final Element source;
    private final List<Policy> policy;
    private final CodeableConcept policyRule;
    private final List<Verification> verification;
    private final Provision provision;

    private volatile int hashCode;

    private Consent(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        scope = ValidationSupport.requireNonNull(builder.scope, "scope");
        category = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.category, "category"));
        patient = builder.patient;
        dateTime = builder.dateTime;
        performer = Collections.unmodifiableList(builder.performer);
        organization = Collections.unmodifiableList(builder.organization);
        source = ValidationSupport.choiceElement(builder.source, "source", Attachment.class, Reference.class);
        policy = Collections.unmodifiableList(builder.policy);
        policyRule = builder.policyRule;
        verification = Collections.unmodifiableList(builder.verification);
        provision = builder.provision;
    }

    /**
     * <p>
     * Unique identifier for this copy of the Consent Statement.
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
     * Indicates the current state of this consent.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ConsentState}.
     */
    public ConsentState getStatus() {
        return status;
    }

    /**
     * <p>
     * A selector of the type of consent being presented: ADR, Privacy, Treatment, Research. This list is now extensible.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getScope() {
        return scope;
    }

    /**
     * <p>
     * A classification of the type of consents found in the statement. This element supports indexing and retrieval of 
     * consent statements.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * The patient/healthcare consumer to whom this consent applies.
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
     * When this Consent was issued / created / indexed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDateTime() {
        return dateTime;
    }

    /**
     * <p>
     * Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the 
     * Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or 
     * limitations on authorizations and enforcement of prohibitions.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The organization that manages the consent, and the framework within which it is executed.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getOrganization() {
        return organization;
    }

    /**
     * <p>
     * The source on which this consent statement is based. The source might be a scanned original paper form, or a reference 
     * to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the 
     * original consent document.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getSource() {
        return source;
    }

    /**
     * <p>
     * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
     * defined jurisdictionally, or in law.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Policy}.
     */
    public List<Policy> getPolicy() {
        return policy;
    }

    /**
     * <p>
     * A reference to the specific base computable regulation or policy.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPolicyRule() {
        return policyRule;
    }

    /**
     * <p>
     * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
     * or another authorized person.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Verification}.
     */
    public List<Verification> getVerification() {
        return verification;
    }

    /**
     * <p>
     * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Provision}.
     */
    public Provision getProvision() {
        return provision;
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
        return new Builder(status, scope, category).from(this);
    }

    public Builder toBuilder(ConsentState status, CodeableConcept scope, List<CodeableConcept> category) {
        return new Builder(status, scope, category).from(this);
    }

    public static Builder builder(ConsentState status, CodeableConcept scope, List<CodeableConcept> category) {
        return new Builder(status, scope, category);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ConsentState status;
        private final CodeableConcept scope;
        private final List<CodeableConcept> category;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Reference patient;
        private DateTime dateTime;
        private List<Reference> performer = new ArrayList<>();
        private List<Reference> organization = new ArrayList<>();
        private Element source;
        private List<Policy> policy = new ArrayList<>();
        private CodeableConcept policyRule;
        private List<Verification> verification = new ArrayList<>();
        private Provision provision;

        private Builder(ConsentState status, CodeableConcept scope, List<CodeableConcept> category) {
            super();
            this.status = status;
            this.scope = scope;
            this.category = category;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Unique identifier for this copy of the Consent Statement.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Unique identifier for this copy of the Consent Statement.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Identifier for this record (external references)
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
         * The patient/healthcare consumer to whom this consent applies.
         * </p>
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
         * <p>
         * When this Consent was issued / created / indexed.
         * </p>
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
         * <p>
         * Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the 
         * Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or 
         * limitations on authorizations and enforcement of prohibitions.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Either the Grantor, which is the entity responsible for granting the rights listed in a Consent Directive or the 
         * Grantee, which is the entity responsible for complying with the Consent Directive, including any obligations or 
         * limitations on authorizations and enforcement of prohibitions.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param performer
         *     Who is agreeing to the policy and rules
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Collection<Reference> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * <p>
         * The organization that manages the consent, and the framework within which it is executed.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The organization that manages the consent, and the framework within which it is executed.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param organization
         *     Custodian of the consent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder organization(Collection<Reference> organization) {
            this.organization = new ArrayList<>(organization);
            return this;
        }

        /**
         * <p>
         * The source on which this consent statement is based. The source might be a scanned original paper form, or a reference 
         * to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the 
         * original consent document.
         * </p>
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
         * <p>
         * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
         * defined jurisdictionally, or in law.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
         * defined jurisdictionally, or in law.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param policy
         *     Policies covered by this consent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder policy(Collection<Policy> policy) {
            this.policy = new ArrayList<>(policy);
            return this;
        }

        /**
         * <p>
         * A reference to the specific base computable regulation or policy.
         * </p>
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
         * <p>
         * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
         * or another authorized person.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
         * or another authorized person.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param verification
         *     Consent Verified by patient or family
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder verification(Collection<Verification> verification) {
            this.verification = new ArrayList<>(verification);
            return this;
        }

        /**
         * <p>
         * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
         * </p>
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

        @Override
        public Consent build() {
            return new Consent(this);
        }

        private Builder from(Consent consent) {
            id = consent.id;
            meta = consent.meta;
            implicitRules = consent.implicitRules;
            language = consent.language;
            text = consent.text;
            contained.addAll(consent.contained);
            extension.addAll(consent.extension);
            modifierExtension.addAll(consent.modifierExtension);
            identifier.addAll(consent.identifier);
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
     * <p>
     * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
     * defined jurisdictionally, or in law.
     * </p>
     */
    public static class Policy extends BackboneElement {
        private final Uri authority;
        private final Uri uri;

        private volatile int hashCode;

        private Policy(Builder builder) {
            super(builder);
            authority = builder.authority;
            uri = builder.uri;
            if (!hasValue() && !hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Entity or Organization having regulatory jurisdiction or accountability for enforcing policies pertaining to Consent 
         * Directives.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getAuthority() {
            return authority;
        }

        /**
         * <p>
         * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
         * defined jurisdictionally, or in law.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getUri() {
            return uri;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (authority != null) || 
                (uri != null);
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
                    accept(authority, "authority", visitor);
                    accept(uri, "uri", visitor);
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
            // optional
            private Uri authority;
            private Uri uri;

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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * Entity or Organization having regulatory jurisdiction or accountability for enforcing policies pertaining to Consent 
             * Directives.
             * </p>
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
             * <p>
             * The references to the policies that are included in this consent scope. Policies may be organizational, but are often 
             * defined jurisdictionally, or in law.
             * </p>
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

            @Override
            public Policy build() {
                return new Policy(this);
            }

            private Builder from(Policy policy) {
                id = policy.id;
                extension.addAll(policy.extension);
                modifierExtension.addAll(policy.modifierExtension);
                authority = policy.authority;
                uri = policy.uri;
                return this;
            }
        }
    }

    /**
     * <p>
     * Whether a treatment instruction (e.g. artificial respiration yes or no) was verified with the patient, his/her family 
     * or another authorized person.
     * </p>
     */
    public static class Verification extends BackboneElement {
        private final Boolean verified;
        private final Reference verifiedWith;
        private final DateTime verificationDate;

        private volatile int hashCode;

        private Verification(Builder builder) {
            super(builder);
            verified = ValidationSupport.requireNonNull(builder.verified, "verified");
            verifiedWith = builder.verifiedWith;
            verificationDate = builder.verificationDate;
            if (!hasValue() && !hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Has the instruction been verified.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getVerified() {
            return verified;
        }

        /**
         * <p>
         * Who verified the instruction (Patient, Relative or other Authorized Person).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getVerifiedWith() {
            return verifiedWith;
        }

        /**
         * <p>
         * Date verification was collected.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getVerificationDate() {
            return verificationDate;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (verified != null) || 
                (verifiedWith != null) || 
                (verificationDate != null);
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
                    accept(verified, "verified", visitor);
                    accept(verifiedWith, "verifiedWith", visitor);
                    accept(verificationDate, "verificationDate", visitor);
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
            return new Builder(verified).from(this);
        }

        public Builder toBuilder(Boolean verified) {
            return new Builder(verified).from(this);
        }

        public static Builder builder(Boolean verified) {
            return new Builder(verified);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Boolean verified;

            // optional
            private Reference verifiedWith;
            private DateTime verificationDate;

            private Builder(Boolean verified) {
                super();
                this.verified = verified;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * Who verified the instruction (Patient, Relative or other Authorized Person).
             * </p>
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
             * <p>
             * Date verification was collected.
             * </p>
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

            @Override
            public Verification build() {
                return new Verification(this);
            }

            private Builder from(Verification verification) {
                id = verification.id;
                extension.addAll(verification.extension);
                modifierExtension.addAll(verification.modifierExtension);
                verifiedWith = verification.verifiedWith;
                verificationDate = verification.verificationDate;
                return this;
            }
        }
    }

    /**
     * <p>
     * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
     * </p>
     */
    public static class Provision extends BackboneElement {
        private final ConsentProvisionType type;
        private final Period period;
        private final List<Actor> actor;
        private final List<CodeableConcept> action;
        private final List<Coding> securityLabel;
        private final List<Coding> purpose;
        private final List<Coding> clazz;
        private final List<CodeableConcept> code;
        private final Period dataPeriod;
        private final List<Data> data;
        private final List<Consent.Provision> provision;

        private volatile int hashCode;

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
            if (!hasValue() && !hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Action to take - permit or deny - when the rule conditions are met. Not permitted in root rule, required in all nested 
         * rules.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ConsentProvisionType}.
         */
        public ConsentProvisionType getType() {
            return type;
        }

        /**
         * <p>
         * The timeframe in this rule is valid.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * <p>
         * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
         * 'admitting officers').
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Actor}.
         */
        public List<Actor> getActor() {
            return actor;
        }

        /**
         * <p>
         * Actions controlled by this Rule.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getAction() {
            return action;
        }

        /**
         * <p>
         * A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled 
         * by this exception.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding}.
         */
        public List<Coding> getSecurityLabel() {
            return securityLabel;
        }

        /**
         * <p>
         * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding}.
         */
        public List<Coding> getPurpose() {
            return purpose;
        }

        /**
         * <p>
         * The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA 
         * document, or some other type that indicates what sort of information the consent relates to.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding}.
         */
        public List<Coding> getClazz() {
            return clazz;
        }

        /**
         * <p>
         * If this code is found in an instance, then the rule applies.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * <p>
         * Clinical or Operational Relevant period of time that bounds the data controlled by this rule.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getDataPeriod() {
            return dataPeriod;
        }

        /**
         * <p>
         * The resources controlled by this rule if specific resources are referenced.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Data}.
         */
        public List<Data> getData() {
            return data;
        }

        /**
         * <p>
         * Rules which provide exceptions to the base rule or subrules.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Provision}.
         */
        public List<Consent.Provision> getProvision() {
            return provision;
        }

        @Override
        protected boolean hasChildren() {
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
            // optional
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * Action to take - permit or deny - when the rule conditions are met. Not permitted in root rule, required in all nested 
             * rules.
             * </p>
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
             * <p>
             * The timeframe in this rule is valid.
             * </p>
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
             * <p>
             * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
             * 'admitting officers').
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
             * 'admitting officers').
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param actor
             *     Who|what controlled by this rule (or group, by role)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Collection<Actor> actor) {
                this.actor = new ArrayList<>(actor);
                return this;
            }

            /**
             * <p>
             * Actions controlled by this Rule.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Actions controlled by this Rule.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param action
             *     Actions controlled by this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Collection<CodeableConcept> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * <p>
             * A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled 
             * by this exception.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * A security label, comprised of 0..* security label fields (Privacy tags), which define which resources are controlled 
             * by this exception.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param securityLabel
             *     Security Labels that define affected resources
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder securityLabel(Collection<Coding> securityLabel) {
                this.securityLabel = new ArrayList<>(securityLabel);
                return this;
            }

            /**
             * <p>
             * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this rule.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param purpose
             *     Context of activities covered by this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder purpose(Collection<Coding> purpose) {
                this.purpose = new ArrayList<>(purpose);
                return this;
            }

            /**
             * <p>
             * The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA 
             * document, or some other type that indicates what sort of information the consent relates to.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * The class of information covered by this rule. The type can be a FHIR resource type, a profile on a type, or a CDA 
             * document, or some other type that indicates what sort of information the consent relates to.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param clazz
             *     e.g. Resource Type, Profile, CDA, etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder clazz(Collection<Coding> clazz) {
                this.clazz = new ArrayList<>(clazz);
                return this;
            }

            /**
             * <p>
             * If this code is found in an instance, then the rule applies.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * If this code is found in an instance, then the rule applies.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param code
             *     e.g. LOINC or SNOMED CT code, etc. in the content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * <p>
             * Clinical or Operational Relevant period of time that bounds the data controlled by this rule.
             * </p>
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
             * <p>
             * The resources controlled by this rule if specific resources are referenced.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * The resources controlled by this rule if specific resources are referenced.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param data
             *     Data controlled by this rule
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder data(Collection<Data> data) {
                this.data = new ArrayList<>(data);
                return this;
            }

            /**
             * <p>
             * Rules which provide exceptions to the base rule or subrules.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Rules which provide exceptions to the base rule or subrules.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param provision
             *     Nested Exception Rules
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provision(Collection<Consent.Provision> provision) {
                this.provision = new ArrayList<>(provision);
                return this;
            }

            @Override
            public Provision build() {
                return new Provision(this);
            }

            private Builder from(Provision provision) {
                id = provision.id;
                extension.addAll(provision.extension);
                modifierExtension.addAll(provision.modifierExtension);
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
         * <p>
         * Who or what is controlled by this rule. Use group to identify a set of actors by some property they share (e.g. 
         * 'admitting officers').
         * </p>
         */
        public static class Actor extends BackboneElement {
            private final CodeableConcept role;
            private final Reference reference;

            private volatile int hashCode;

            private Actor(Builder builder) {
                super(builder);
                role = ValidationSupport.requireNonNull(builder.role, "role");
                reference = ValidationSupport.requireNonNull(builder.reference, "reference");
                if (!hasValue() && !hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * How the individual is involved in the resources content that is described in the exception.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getRole() {
                return role;
            }

            /**
             * <p>
             * The resource that identifies the actor. To identify actors by type, use group to identify a set of actors by some 
             * property they share (e.g. 'admitting officers').
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getReference() {
                return reference;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (role != null) || 
                    (reference != null);
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
                        accept(role, "role", visitor);
                        accept(reference, "reference", visitor);
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
                return new Builder(role, reference).from(this);
            }

            public Builder toBuilder(CodeableConcept role, Reference reference) {
                return new Builder(role, reference).from(this);
            }

            public static Builder builder(CodeableConcept role, Reference reference) {
                return new Builder(role, reference);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final CodeableConcept role;
                private final Reference reference;

                private Builder(CodeableConcept role, Reference reference) {
                    super();
                    this.role = role;
                    this.reference = reference;
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
                 * Adds new element(s) to the existing list
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
                 * Adds new element(s) to the existing list
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
                public Actor build() {
                    return new Actor(this);
                }

                private Builder from(Actor actor) {
                    id = actor.id;
                    extension.addAll(actor.extension);
                    modifierExtension.addAll(actor.modifierExtension);
                    return this;
                }
            }
        }

        /**
         * <p>
         * The resources controlled by this rule if specific resources are referenced.
         * </p>
         */
        public static class Data extends BackboneElement {
            private final ConsentDataMeaning meaning;
            private final Reference reference;

            private volatile int hashCode;

            private Data(Builder builder) {
                super(builder);
                meaning = ValidationSupport.requireNonNull(builder.meaning, "meaning");
                reference = ValidationSupport.requireNonNull(builder.reference, "reference");
                if (!hasValue() && !hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * How the resource reference is interpreted when testing consent restrictions.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ConsentDataMeaning}.
             */
            public ConsentDataMeaning getMeaning() {
                return meaning;
            }

            /**
             * <p>
             * A reference to a specific resource that defines which resources are covered by this consent.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getReference() {
                return reference;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (meaning != null) || 
                    (reference != null);
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
                        accept(meaning, "meaning", visitor);
                        accept(reference, "reference", visitor);
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
                return new Builder(meaning, reference).from(this);
            }

            public Builder toBuilder(ConsentDataMeaning meaning, Reference reference) {
                return new Builder(meaning, reference).from(this);
            }

            public static Builder builder(ConsentDataMeaning meaning, Reference reference) {
                return new Builder(meaning, reference);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final ConsentDataMeaning meaning;
                private final Reference reference;

                private Builder(ConsentDataMeaning meaning, Reference reference) {
                    super();
                    this.meaning = meaning;
                    this.reference = reference;
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
                 * Adds new element(s) to the existing list
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
                 * Adds new element(s) to the existing list
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
                public Data build() {
                    return new Data(this);
                }

                private Builder from(Data data) {
                    id = data.id;
                    extension.addAll(data.extension);
                    modifierExtension.addAll(data.modifierExtension);
                    return this;
                }
            }
        }
    }
}
