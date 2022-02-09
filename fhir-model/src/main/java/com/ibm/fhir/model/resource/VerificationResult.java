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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.Status;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes validation requirements, source(s), status and dates for one or more elements.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "verificationResult-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/verificationresult-need",
    expression = "need.exists() implies (need.memberOf('http://hl7.org/fhir/ValueSet/verificationresult-need', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/VerificationResult",
    generated = true
)
@Constraint(
    id = "verificationResult-1",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/verificationresult-validation-type",
    expression = "validationType.exists() implies (validationType.memberOf('http://hl7.org/fhir/ValueSet/verificationresult-validation-type', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/VerificationResult",
    generated = true
)
@Constraint(
    id = "verificationResult-2",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/verificationresult-failure-action",
    expression = "failureAction.exists() implies (failureAction.memberOf('http://hl7.org/fhir/ValueSet/verificationresult-failure-action', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/VerificationResult",
    generated = true
)
@Constraint(
    id = "verificationResult-3",
    level = "Warning",
    location = "primarySource.validationStatus",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/verificationresult-validation-status",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/verificationresult-validation-status', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/VerificationResult",
    generated = true
)
@Constraint(
    id = "verificationResult-4",
    level = "Warning",
    location = "primarySource.canPushUpdates",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/verificationresult-can-push-updates",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/verificationresult-can-push-updates', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/VerificationResult",
    generated = true
)
@Constraint(
    id = "verificationResult-5",
    level = "Warning",
    location = "primarySource.pushTypeAvailable",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/verificationresult-push-type-available",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/verificationresult-push-type-available', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/VerificationResult",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class VerificationResult extends DomainResource {
    @Summary
    private final List<Reference> target;
    @Summary
    private final List<String> targetLocation;
    @Summary
    @Binding(
        bindingName = "need",
        strength = BindingStrength.Value.PREFERRED,
        description = "The frequency with which the target must be validated.",
        valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-need"
    )
    private final CodeableConcept need;
    @Summary
    @Binding(
        bindingName = "status",
        strength = BindingStrength.Value.REQUIRED,
        description = "The validation status of the target.",
        valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-status|4.3.0-CIBUILD"
    )
    @Required
    private final Status status;
    @Summary
    private final DateTime statusDate;
    @Summary
    @Binding(
        bindingName = "validation-type",
        strength = BindingStrength.Value.PREFERRED,
        description = "What the target is validated against.",
        valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-validation-type"
    )
    private final CodeableConcept validationType;
    @Summary
    @Binding(
        bindingName = "validation-process",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The primary process by which the target is validated.",
        valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-validation-process"
    )
    private final List<CodeableConcept> validationProcess;
    private final Timing frequency;
    private final DateTime lastPerformed;
    private final Date nextScheduled;
    @Summary
    @Binding(
        bindingName = "failure-action",
        strength = BindingStrength.Value.PREFERRED,
        description = "The result if validation fails.",
        valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-failure-action"
    )
    private final CodeableConcept failureAction;
    private final List<PrimarySource> primarySource;
    private final Attestation attestation;
    private final List<Validator> validator;

    private VerificationResult(Builder builder) {
        super(builder);
        target = Collections.unmodifiableList(builder.target);
        targetLocation = Collections.unmodifiableList(builder.targetLocation);
        need = builder.need;
        status = builder.status;
        statusDate = builder.statusDate;
        validationType = builder.validationType;
        validationProcess = Collections.unmodifiableList(builder.validationProcess);
        frequency = builder.frequency;
        lastPerformed = builder.lastPerformed;
        nextScheduled = builder.nextScheduled;
        failureAction = builder.failureAction;
        primarySource = Collections.unmodifiableList(builder.primarySource);
        attestation = builder.attestation;
        validator = Collections.unmodifiableList(builder.validator);
    }

    /**
     * A resource that was validated.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getTarget() {
        return target;
    }

    /**
     * The fhirpath location(s) within the resource that was validated.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getTargetLocation() {
        return targetLocation;
    }

    /**
     * The frequency with which the target must be validated (none; initial; periodic).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getNeed() {
        return need;
    }

    /**
     * The validation status of the target (attested; validated; in process; requires revalidation; validation failed; 
     * revalidation failed).
     * 
     * @return
     *     An immutable object of type {@link Status} that is non-null.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * When the validation status was updated.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getStatusDate() {
        return statusDate;
    }

    /**
     * What the target is validated against (nothing; primary source; multiple sources).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getValidationType() {
        return validationType;
    }

    /**
     * The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
     * standalone; in context).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getValidationProcess() {
        return validationProcess;
    }

    /**
     * Frequency of revalidation.
     * 
     * @return
     *     An immutable object of type {@link Timing} that may be null.
     */
    public Timing getFrequency() {
        return frequency;
    }

    /**
     * The date/time validation was last completed (including failed validations).
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getLastPerformed() {
        return lastPerformed;
    }

    /**
     * The date when target is next validated, if appropriate.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getNextScheduled() {
        return nextScheduled;
    }

    /**
     * The result if validation fails (fatal; warning; record only; none).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getFailureAction() {
        return failureAction;
    }

    /**
     * Information about the primary source(s) involved in validation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PrimarySource} that may be empty.
     */
    public List<PrimarySource> getPrimarySource() {
        return primarySource;
    }

    /**
     * Information about the entity attesting to information.
     * 
     * @return
     *     An immutable object of type {@link Attestation} that may be null.
     */
    public Attestation getAttestation() {
        return attestation;
    }

    /**
     * Information about the entity validating information.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Validator} that may be empty.
     */
    public List<Validator> getValidator() {
        return validator;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !target.isEmpty() || 
            !targetLocation.isEmpty() || 
            (need != null) || 
            (status != null) || 
            (statusDate != null) || 
            (validationType != null) || 
            !validationProcess.isEmpty() || 
            (frequency != null) || 
            (lastPerformed != null) || 
            (nextScheduled != null) || 
            (failureAction != null) || 
            !primarySource.isEmpty() || 
            (attestation != null) || 
            !validator.isEmpty();
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
                accept(target, "target", visitor, Reference.class);
                accept(targetLocation, "targetLocation", visitor, String.class);
                accept(need, "need", visitor);
                accept(status, "status", visitor);
                accept(statusDate, "statusDate", visitor);
                accept(validationType, "validationType", visitor);
                accept(validationProcess, "validationProcess", visitor, CodeableConcept.class);
                accept(frequency, "frequency", visitor);
                accept(lastPerformed, "lastPerformed", visitor);
                accept(nextScheduled, "nextScheduled", visitor);
                accept(failureAction, "failureAction", visitor);
                accept(primarySource, "primarySource", visitor, PrimarySource.class);
                accept(attestation, "attestation", visitor);
                accept(validator, "validator", visitor, Validator.class);
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
        VerificationResult other = (VerificationResult) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(target, other.target) && 
            Objects.equals(targetLocation, other.targetLocation) && 
            Objects.equals(need, other.need) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusDate, other.statusDate) && 
            Objects.equals(validationType, other.validationType) && 
            Objects.equals(validationProcess, other.validationProcess) && 
            Objects.equals(frequency, other.frequency) && 
            Objects.equals(lastPerformed, other.lastPerformed) && 
            Objects.equals(nextScheduled, other.nextScheduled) && 
            Objects.equals(failureAction, other.failureAction) && 
            Objects.equals(primarySource, other.primarySource) && 
            Objects.equals(attestation, other.attestation) && 
            Objects.equals(validator, other.validator);
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
                target, 
                targetLocation, 
                need, 
                status, 
                statusDate, 
                validationType, 
                validationProcess, 
                frequency, 
                lastPerformed, 
                nextScheduled, 
                failureAction, 
                primarySource, 
                attestation, 
                validator);
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
        private List<Reference> target = new ArrayList<>();
        private List<String> targetLocation = new ArrayList<>();
        private CodeableConcept need;
        private Status status;
        private DateTime statusDate;
        private CodeableConcept validationType;
        private List<CodeableConcept> validationProcess = new ArrayList<>();
        private Timing frequency;
        private DateTime lastPerformed;
        private Date nextScheduled;
        private CodeableConcept failureAction;
        private List<PrimarySource> primarySource = new ArrayList<>();
        private Attestation attestation;
        private List<Validator> validator = new ArrayList<>();

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
         * A resource that was validated.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param target
         *     A resource that was validated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder target(Reference... target) {
            for (Reference value : target) {
                this.target.add(value);
            }
            return this;
        }

        /**
         * A resource that was validated.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param target
         *     A resource that was validated
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder target(Collection<Reference> target) {
            this.target = new ArrayList<>(target);
            return this;
        }

        /**
         * Convenience method for setting {@code targetLocation}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param targetLocation
         *     The fhirpath location(s) within the resource that was validated
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #targetLocation(com.ibm.fhir.model.type.String)
         */
        public Builder targetLocation(java.lang.String... targetLocation) {
            for (java.lang.String value : targetLocation) {
                this.targetLocation.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * The fhirpath location(s) within the resource that was validated.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param targetLocation
         *     The fhirpath location(s) within the resource that was validated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder targetLocation(String... targetLocation) {
            for (String value : targetLocation) {
                this.targetLocation.add(value);
            }
            return this;
        }

        /**
         * The fhirpath location(s) within the resource that was validated.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param targetLocation
         *     The fhirpath location(s) within the resource that was validated
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder targetLocation(Collection<String> targetLocation) {
            this.targetLocation = new ArrayList<>(targetLocation);
            return this;
        }

        /**
         * The frequency with which the target must be validated (none; initial; periodic).
         * 
         * @param need
         *     none | initial | periodic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder need(CodeableConcept need) {
            this.need = need;
            return this;
        }

        /**
         * The validation status of the target (attested; validated; in process; requires revalidation; validation failed; 
         * revalidation failed).
         * 
         * <p>This element is required.
         * 
         * @param status
         *     attested | validated | in-process | req-revalid | val-fail | reval-fail
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        /**
         * When the validation status was updated.
         * 
         * @param statusDate
         *     When the validation status was updated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusDate(DateTime statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * What the target is validated against (nothing; primary source; multiple sources).
         * 
         * @param validationType
         *     nothing | primary | multiple
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validationType(CodeableConcept validationType) {
            this.validationType = validationType;
            return this;
        }

        /**
         * The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         * standalone; in context).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param validationProcess
         *     The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         *     standalone; in context)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validationProcess(CodeableConcept... validationProcess) {
            for (CodeableConcept value : validationProcess) {
                this.validationProcess.add(value);
            }
            return this;
        }

        /**
         * The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         * standalone; in context).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param validationProcess
         *     The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         *     standalone; in context)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder validationProcess(Collection<CodeableConcept> validationProcess) {
            this.validationProcess = new ArrayList<>(validationProcess);
            return this;
        }

        /**
         * Frequency of revalidation.
         * 
         * @param frequency
         *     Frequency of revalidation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder frequency(Timing frequency) {
            this.frequency = frequency;
            return this;
        }

        /**
         * The date/time validation was last completed (including failed validations).
         * 
         * @param lastPerformed
         *     The date/time validation was last completed (including failed validations)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastPerformed(DateTime lastPerformed) {
            this.lastPerformed = lastPerformed;
            return this;
        }

        /**
         * Convenience method for setting {@code nextScheduled}.
         * 
         * @param nextScheduled
         *     The date when target is next validated, if appropriate
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #nextScheduled(com.ibm.fhir.model.type.Date)
         */
        public Builder nextScheduled(java.time.LocalDate nextScheduled) {
            this.nextScheduled = (nextScheduled == null) ? null : Date.of(nextScheduled);
            return this;
        }

        /**
         * The date when target is next validated, if appropriate.
         * 
         * @param nextScheduled
         *     The date when target is next validated, if appropriate
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder nextScheduled(Date nextScheduled) {
            this.nextScheduled = nextScheduled;
            return this;
        }

        /**
         * The result if validation fails (fatal; warning; record only; none).
         * 
         * @param failureAction
         *     fatal | warn | rec-only | none
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder failureAction(CodeableConcept failureAction) {
            this.failureAction = failureAction;
            return this;
        }

        /**
         * Information about the primary source(s) involved in validation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param primarySource
         *     Information about the primary source(s) involved in validation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder primarySource(PrimarySource... primarySource) {
            for (PrimarySource value : primarySource) {
                this.primarySource.add(value);
            }
            return this;
        }

        /**
         * Information about the primary source(s) involved in validation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param primarySource
         *     Information about the primary source(s) involved in validation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder primarySource(Collection<PrimarySource> primarySource) {
            this.primarySource = new ArrayList<>(primarySource);
            return this;
        }

        /**
         * Information about the entity attesting to information.
         * 
         * @param attestation
         *     Information about the entity attesting to information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder attestation(Attestation attestation) {
            this.attestation = attestation;
            return this;
        }

        /**
         * Information about the entity validating information.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param validator
         *     Information about the entity validating information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validator(Validator... validator) {
            for (Validator value : validator) {
                this.validator.add(value);
            }
            return this;
        }

        /**
         * Information about the entity validating information.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param validator
         *     Information about the entity validating information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder validator(Collection<Validator> validator) {
            this.validator = new ArrayList<>(validator);
            return this;
        }

        /**
         * Build the {@link VerificationResult}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link VerificationResult}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid VerificationResult per the base specification
         */
        @Override
        public VerificationResult build() {
            VerificationResult verificationResult = new VerificationResult(this);
            if (validating) {
                validate(verificationResult);
            }
            return verificationResult;
        }

        protected void validate(VerificationResult verificationResult) {
            super.validate(verificationResult);
            ValidationSupport.checkList(verificationResult.target, "target", Reference.class);
            ValidationSupport.checkList(verificationResult.targetLocation, "targetLocation", String.class);
            ValidationSupport.requireNonNull(verificationResult.status, "status");
            ValidationSupport.checkList(verificationResult.validationProcess, "validationProcess", CodeableConcept.class);
            ValidationSupport.checkList(verificationResult.primarySource, "primarySource", PrimarySource.class);
            ValidationSupport.checkList(verificationResult.validator, "validator", Validator.class);
        }

        protected Builder from(VerificationResult verificationResult) {
            super.from(verificationResult);
            target.addAll(verificationResult.target);
            targetLocation.addAll(verificationResult.targetLocation);
            need = verificationResult.need;
            status = verificationResult.status;
            statusDate = verificationResult.statusDate;
            validationType = verificationResult.validationType;
            validationProcess.addAll(verificationResult.validationProcess);
            frequency = verificationResult.frequency;
            lastPerformed = verificationResult.lastPerformed;
            nextScheduled = verificationResult.nextScheduled;
            failureAction = verificationResult.failureAction;
            primarySource.addAll(verificationResult.primarySource);
            attestation = verificationResult.attestation;
            validator.addAll(verificationResult.validator);
            return this;
        }
    }

    /**
     * Information about the primary source(s) involved in validation.
     */
    public static class PrimarySource extends BackboneElement {
        @ReferenceTarget({ "Organization", "Practitioner", "PractitionerRole" })
        private final Reference who;
        @Summary
        @Binding(
            bindingName = "primary-source-type",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Type of the validation primary source.",
            valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-primary-source-type"
        )
        private final List<CodeableConcept> type;
        @Summary
        @Binding(
            bindingName = "communication-method",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Method for communicating with the data source (manual; API; Push).",
            valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-communication-method"
        )
        private final List<CodeableConcept> communicationMethod;
        @Binding(
            bindingName = "validation-status",
            strength = BindingStrength.Value.PREFERRED,
            description = "Status of the validation of the target against the primary source.",
            valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-validation-status"
        )
        private final CodeableConcept validationStatus;
        private final DateTime validationDate;
        @Summary
        @Binding(
            bindingName = "can-push-updates",
            strength = BindingStrength.Value.PREFERRED,
            description = "Ability of the primary source to push updates/alerts.",
            valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-can-push-updates"
        )
        private final CodeableConcept canPushUpdates;
        @Binding(
            bindingName = "push-type-available",
            strength = BindingStrength.Value.PREFERRED,
            description = "Type of alerts/updates the primary source can send.",
            valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-push-type-available"
        )
        private final List<CodeableConcept> pushTypeAvailable;

        private PrimarySource(Builder builder) {
            super(builder);
            who = builder.who;
            type = Collections.unmodifiableList(builder.type);
            communicationMethod = Collections.unmodifiableList(builder.communicationMethod);
            validationStatus = builder.validationStatus;
            validationDate = builder.validationDate;
            canPushUpdates = builder.canPushUpdates;
            pushTypeAvailable = Collections.unmodifiableList(builder.pushTypeAvailable);
        }

        /**
         * Reference to the primary source.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getWho() {
            return who;
        }

        /**
         * Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
         * Registration Authority; legal source; issuing source; authoritative source).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * Method for communicating with the primary source (manual; API; Push).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getCommunicationMethod() {
            return communicationMethod;
        }

        /**
         * Status of the validation of the target against the primary source (successful; failed; unknown).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getValidationStatus() {
            return validationStatus;
        }

        /**
         * When the target was validated against the primary source.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getValidationDate() {
            return validationDate;
        }

        /**
         * Ability of the primary source to push updates/alerts (yes; no; undetermined).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCanPushUpdates() {
            return canPushUpdates;
        }

        /**
         * Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getPushTypeAvailable() {
            return pushTypeAvailable;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (who != null) || 
                !type.isEmpty() || 
                !communicationMethod.isEmpty() || 
                (validationStatus != null) || 
                (validationDate != null) || 
                (canPushUpdates != null) || 
                !pushTypeAvailable.isEmpty();
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
                    accept(who, "who", visitor);
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(communicationMethod, "communicationMethod", visitor, CodeableConcept.class);
                    accept(validationStatus, "validationStatus", visitor);
                    accept(validationDate, "validationDate", visitor);
                    accept(canPushUpdates, "canPushUpdates", visitor);
                    accept(pushTypeAvailable, "pushTypeAvailable", visitor, CodeableConcept.class);
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
            PrimarySource other = (PrimarySource) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(who, other.who) && 
                Objects.equals(type, other.type) && 
                Objects.equals(communicationMethod, other.communicationMethod) && 
                Objects.equals(validationStatus, other.validationStatus) && 
                Objects.equals(validationDate, other.validationDate) && 
                Objects.equals(canPushUpdates, other.canPushUpdates) && 
                Objects.equals(pushTypeAvailable, other.pushTypeAvailable);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    who, 
                    type, 
                    communicationMethod, 
                    validationStatus, 
                    validationDate, 
                    canPushUpdates, 
                    pushTypeAvailable);
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
            private Reference who;
            private List<CodeableConcept> type = new ArrayList<>();
            private List<CodeableConcept> communicationMethod = new ArrayList<>();
            private CodeableConcept validationStatus;
            private DateTime validationDate;
            private CodeableConcept canPushUpdates;
            private List<CodeableConcept> pushTypeAvailable = new ArrayList<>();

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
             * Reference to the primary source.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param who
             *     Reference to the primary source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder who(Reference who) {
                this.who = who;
                return this;
            }

            /**
             * Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             * Registration Authority; legal source; issuing source; authoritative source).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             *     Registration Authority; legal source; issuing source; authoritative source)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept... type) {
                for (CodeableConcept value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             * Registration Authority; legal source; issuing source; authoritative source).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             *     Registration Authority; legal source; issuing source; authoritative source)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type = new ArrayList<>(type);
                return this;
            }

            /**
             * Method for communicating with the primary source (manual; API; Push).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param communicationMethod
             *     Method for exchanging information with the primary source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder communicationMethod(CodeableConcept... communicationMethod) {
                for (CodeableConcept value : communicationMethod) {
                    this.communicationMethod.add(value);
                }
                return this;
            }

            /**
             * Method for communicating with the primary source (manual; API; Push).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param communicationMethod
             *     Method for exchanging information with the primary source
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder communicationMethod(Collection<CodeableConcept> communicationMethod) {
                this.communicationMethod = new ArrayList<>(communicationMethod);
                return this;
            }

            /**
             * Status of the validation of the target against the primary source (successful; failed; unknown).
             * 
             * @param validationStatus
             *     successful | failed | unknown
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder validationStatus(CodeableConcept validationStatus) {
                this.validationStatus = validationStatus;
                return this;
            }

            /**
             * When the target was validated against the primary source.
             * 
             * @param validationDate
             *     When the target was validated against the primary source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder validationDate(DateTime validationDate) {
                this.validationDate = validationDate;
                return this;
            }

            /**
             * Ability of the primary source to push updates/alerts (yes; no; undetermined).
             * 
             * @param canPushUpdates
             *     yes | no | undetermined
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder canPushUpdates(CodeableConcept canPushUpdates) {
                this.canPushUpdates = canPushUpdates;
                return this;
            }

            /**
             * Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param pushTypeAvailable
             *     specific | any | source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder pushTypeAvailable(CodeableConcept... pushTypeAvailable) {
                for (CodeableConcept value : pushTypeAvailable) {
                    this.pushTypeAvailable.add(value);
                }
                return this;
            }

            /**
             * Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param pushTypeAvailable
             *     specific | any | source
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder pushTypeAvailable(Collection<CodeableConcept> pushTypeAvailable) {
                this.pushTypeAvailable = new ArrayList<>(pushTypeAvailable);
                return this;
            }

            /**
             * Build the {@link PrimarySource}
             * 
             * @return
             *     An immutable object of type {@link PrimarySource}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid PrimarySource per the base specification
             */
            @Override
            public PrimarySource build() {
                PrimarySource primarySource = new PrimarySource(this);
                if (validating) {
                    validate(primarySource);
                }
                return primarySource;
            }

            protected void validate(PrimarySource primarySource) {
                super.validate(primarySource);
                ValidationSupport.checkList(primarySource.type, "type", CodeableConcept.class);
                ValidationSupport.checkList(primarySource.communicationMethod, "communicationMethod", CodeableConcept.class);
                ValidationSupport.checkList(primarySource.pushTypeAvailable, "pushTypeAvailable", CodeableConcept.class);
                ValidationSupport.checkReferenceType(primarySource.who, "who", "Organization", "Practitioner", "PractitionerRole");
                ValidationSupport.requireValueOrChildren(primarySource);
            }

            protected Builder from(PrimarySource primarySource) {
                super.from(primarySource);
                who = primarySource.who;
                type.addAll(primarySource.type);
                communicationMethod.addAll(primarySource.communicationMethod);
                validationStatus = primarySource.validationStatus;
                validationDate = primarySource.validationDate;
                canPushUpdates = primarySource.canPushUpdates;
                pushTypeAvailable.addAll(primarySource.pushTypeAvailable);
                return this;
            }
        }
    }

    /**
     * Information about the entity attesting to information.
     */
    public static class Attestation extends BackboneElement {
        @Summary
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        private final Reference who;
        @Summary
        @ReferenceTarget({ "Organization", "Practitioner", "PractitionerRole" })
        private final Reference onBehalfOf;
        @Summary
        @Binding(
            bindingName = "communication-method",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Method for communicating with the data source (manual; API; Push).",
            valueSet = "http://hl7.org/fhir/ValueSet/verificationresult-communication-method"
        )
        private final CodeableConcept communicationMethod;
        @Summary
        private final Date date;
        private final String sourceIdentityCertificate;
        private final String proxyIdentityCertificate;
        private final Signature proxySignature;
        private final Signature sourceSignature;

        private Attestation(Builder builder) {
            super(builder);
            who = builder.who;
            onBehalfOf = builder.onBehalfOf;
            communicationMethod = builder.communicationMethod;
            date = builder.date;
            sourceIdentityCertificate = builder.sourceIdentityCertificate;
            proxyIdentityCertificate = builder.proxyIdentityCertificate;
            proxySignature = builder.proxySignature;
            sourceSignature = builder.sourceSignature;
        }

        /**
         * The individual or organization attesting to information.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getWho() {
            return who;
        }

        /**
         * When the who is asserting on behalf of another (organization or individual).
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getOnBehalfOf() {
            return onBehalfOf;
        }

        /**
         * The method by which attested information was submitted/retrieved (manual; API; Push).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCommunicationMethod() {
            return communicationMethod;
        }

        /**
         * The date the information was attested to.
         * 
         * @return
         *     An immutable object of type {@link Date} that may be null.
         */
        public Date getDate() {
            return date;
        }

        /**
         * A digital identity certificate associated with the attestation source.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSourceIdentityCertificate() {
            return sourceIdentityCertificate;
        }

        /**
         * A digital identity certificate associated with the proxy entity submitting attested information on behalf of the 
         * attestation source.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getProxyIdentityCertificate() {
            return proxyIdentityCertificate;
        }

        /**
         * Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of 
         * the attestation source.
         * 
         * @return
         *     An immutable object of type {@link Signature} that may be null.
         */
        public Signature getProxySignature() {
            return proxySignature;
        }

        /**
         * Signed assertion by the attestation source that they have attested to the information.
         * 
         * @return
         *     An immutable object of type {@link Signature} that may be null.
         */
        public Signature getSourceSignature() {
            return sourceSignature;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (who != null) || 
                (onBehalfOf != null) || 
                (communicationMethod != null) || 
                (date != null) || 
                (sourceIdentityCertificate != null) || 
                (proxyIdentityCertificate != null) || 
                (proxySignature != null) || 
                (sourceSignature != null);
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
                    accept(who, "who", visitor);
                    accept(onBehalfOf, "onBehalfOf", visitor);
                    accept(communicationMethod, "communicationMethod", visitor);
                    accept(date, "date", visitor);
                    accept(sourceIdentityCertificate, "sourceIdentityCertificate", visitor);
                    accept(proxyIdentityCertificate, "proxyIdentityCertificate", visitor);
                    accept(proxySignature, "proxySignature", visitor);
                    accept(sourceSignature, "sourceSignature", visitor);
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
            Attestation other = (Attestation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(who, other.who) && 
                Objects.equals(onBehalfOf, other.onBehalfOf) && 
                Objects.equals(communicationMethod, other.communicationMethod) && 
                Objects.equals(date, other.date) && 
                Objects.equals(sourceIdentityCertificate, other.sourceIdentityCertificate) && 
                Objects.equals(proxyIdentityCertificate, other.proxyIdentityCertificate) && 
                Objects.equals(proxySignature, other.proxySignature) && 
                Objects.equals(sourceSignature, other.sourceSignature);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    who, 
                    onBehalfOf, 
                    communicationMethod, 
                    date, 
                    sourceIdentityCertificate, 
                    proxyIdentityCertificate, 
                    proxySignature, 
                    sourceSignature);
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
            private Reference who;
            private Reference onBehalfOf;
            private CodeableConcept communicationMethod;
            private Date date;
            private String sourceIdentityCertificate;
            private String proxyIdentityCertificate;
            private Signature proxySignature;
            private Signature sourceSignature;

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
             * The individual or organization attesting to information.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param who
             *     The individual or organization attesting to information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder who(Reference who) {
                this.who = who;
                return this;
            }

            /**
             * When the who is asserting on behalf of another (organization or individual).
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param onBehalfOf
             *     When the who is asserting on behalf of another (organization or individual)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder onBehalfOf(Reference onBehalfOf) {
                this.onBehalfOf = onBehalfOf;
                return this;
            }

            /**
             * The method by which attested information was submitted/retrieved (manual; API; Push).
             * 
             * @param communicationMethod
             *     The method by which attested information was submitted/retrieved
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder communicationMethod(CodeableConcept communicationMethod) {
                this.communicationMethod = communicationMethod;
                return this;
            }

            /**
             * Convenience method for setting {@code date}.
             * 
             * @param date
             *     The date the information was attested to
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #date(com.ibm.fhir.model.type.Date)
             */
            public Builder date(java.time.LocalDate date) {
                this.date = (date == null) ? null : Date.of(date);
                return this;
            }

            /**
             * The date the information was attested to.
             * 
             * @param date
             *     The date the information was attested to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(Date date) {
                this.date = date;
                return this;
            }

            /**
             * Convenience method for setting {@code sourceIdentityCertificate}.
             * 
             * @param sourceIdentityCertificate
             *     A digital identity certificate associated with the attestation source
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #sourceIdentityCertificate(com.ibm.fhir.model.type.String)
             */
            public Builder sourceIdentityCertificate(java.lang.String sourceIdentityCertificate) {
                this.sourceIdentityCertificate = (sourceIdentityCertificate == null) ? null : String.of(sourceIdentityCertificate);
                return this;
            }

            /**
             * A digital identity certificate associated with the attestation source.
             * 
             * @param sourceIdentityCertificate
             *     A digital identity certificate associated with the attestation source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sourceIdentityCertificate(String sourceIdentityCertificate) {
                this.sourceIdentityCertificate = sourceIdentityCertificate;
                return this;
            }

            /**
             * Convenience method for setting {@code proxyIdentityCertificate}.
             * 
             * @param proxyIdentityCertificate
             *     A digital identity certificate associated with the proxy entity submitting attested information on behalf of the 
             *     attestation source
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #proxyIdentityCertificate(com.ibm.fhir.model.type.String)
             */
            public Builder proxyIdentityCertificate(java.lang.String proxyIdentityCertificate) {
                this.proxyIdentityCertificate = (proxyIdentityCertificate == null) ? null : String.of(proxyIdentityCertificate);
                return this;
            }

            /**
             * A digital identity certificate associated with the proxy entity submitting attested information on behalf of the 
             * attestation source.
             * 
             * @param proxyIdentityCertificate
             *     A digital identity certificate associated with the proxy entity submitting attested information on behalf of the 
             *     attestation source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder proxyIdentityCertificate(String proxyIdentityCertificate) {
                this.proxyIdentityCertificate = proxyIdentityCertificate;
                return this;
            }

            /**
             * Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of 
             * the attestation source.
             * 
             * @param proxySignature
             *     Proxy signature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder proxySignature(Signature proxySignature) {
                this.proxySignature = proxySignature;
                return this;
            }

            /**
             * Signed assertion by the attestation source that they have attested to the information.
             * 
             * @param sourceSignature
             *     Attester signature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sourceSignature(Signature sourceSignature) {
                this.sourceSignature = sourceSignature;
                return this;
            }

            /**
             * Build the {@link Attestation}
             * 
             * @return
             *     An immutable object of type {@link Attestation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Attestation per the base specification
             */
            @Override
            public Attestation build() {
                Attestation attestation = new Attestation(this);
                if (validating) {
                    validate(attestation);
                }
                return attestation;
            }

            protected void validate(Attestation attestation) {
                super.validate(attestation);
                ValidationSupport.checkReferenceType(attestation.who, "who", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.checkReferenceType(attestation.onBehalfOf, "onBehalfOf", "Organization", "Practitioner", "PractitionerRole");
                ValidationSupport.requireValueOrChildren(attestation);
            }

            protected Builder from(Attestation attestation) {
                super.from(attestation);
                who = attestation.who;
                onBehalfOf = attestation.onBehalfOf;
                communicationMethod = attestation.communicationMethod;
                date = attestation.date;
                sourceIdentityCertificate = attestation.sourceIdentityCertificate;
                proxyIdentityCertificate = attestation.proxyIdentityCertificate;
                proxySignature = attestation.proxySignature;
                sourceSignature = attestation.sourceSignature;
                return this;
            }
        }
    }

    /**
     * Information about the entity validating information.
     */
    public static class Validator extends BackboneElement {
        @ReferenceTarget({ "Organization" })
        @Required
        private final Reference organization;
        private final String identityCertificate;
        private final Signature attestationSignature;

        private Validator(Builder builder) {
            super(builder);
            organization = builder.organization;
            identityCertificate = builder.identityCertificate;
            attestationSignature = builder.attestationSignature;
        }

        /**
         * Reference to the organization validating information.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getOrganization() {
            return organization;
        }

        /**
         * A digital identity certificate associated with the validator.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getIdentityCertificate() {
            return identityCertificate;
        }

        /**
         * Signed assertion by the validator that they have validated the information.
         * 
         * @return
         *     An immutable object of type {@link Signature} that may be null.
         */
        public Signature getAttestationSignature() {
            return attestationSignature;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (organization != null) || 
                (identityCertificate != null) || 
                (attestationSignature != null);
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
                    accept(organization, "organization", visitor);
                    accept(identityCertificate, "identityCertificate", visitor);
                    accept(attestationSignature, "attestationSignature", visitor);
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
            Validator other = (Validator) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(organization, other.organization) && 
                Objects.equals(identityCertificate, other.identityCertificate) && 
                Objects.equals(attestationSignature, other.attestationSignature);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    organization, 
                    identityCertificate, 
                    attestationSignature);
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
            private Reference organization;
            private String identityCertificate;
            private Signature attestationSignature;

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
             * Reference to the organization validating information.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param organization
             *     Reference to the organization validating information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder organization(Reference organization) {
                this.organization = organization;
                return this;
            }

            /**
             * Convenience method for setting {@code identityCertificate}.
             * 
             * @param identityCertificate
             *     A digital identity certificate associated with the validator
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #identityCertificate(com.ibm.fhir.model.type.String)
             */
            public Builder identityCertificate(java.lang.String identityCertificate) {
                this.identityCertificate = (identityCertificate == null) ? null : String.of(identityCertificate);
                return this;
            }

            /**
             * A digital identity certificate associated with the validator.
             * 
             * @param identityCertificate
             *     A digital identity certificate associated with the validator
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identityCertificate(String identityCertificate) {
                this.identityCertificate = identityCertificate;
                return this;
            }

            /**
             * Signed assertion by the validator that they have validated the information.
             * 
             * @param attestationSignature
             *     Validator signature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder attestationSignature(Signature attestationSignature) {
                this.attestationSignature = attestationSignature;
                return this;
            }

            /**
             * Build the {@link Validator}
             * 
             * <p>Required elements:
             * <ul>
             * <li>organization</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Validator}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Validator per the base specification
             */
            @Override
            public Validator build() {
                Validator validator = new Validator(this);
                if (validating) {
                    validate(validator);
                }
                return validator;
            }

            protected void validate(Validator validator) {
                super.validate(validator);
                ValidationSupport.requireNonNull(validator.organization, "organization");
                ValidationSupport.checkReferenceType(validator.organization, "organization", "Organization");
                ValidationSupport.requireValueOrChildren(validator);
            }

            protected Builder from(Validator validator) {
                super.from(validator);
                organization = validator.organization;
                identityCertificate = validator.identityCertificate;
                attestationSignature = validator.attestationSignature;
                return this;
            }
        }
    }
}
