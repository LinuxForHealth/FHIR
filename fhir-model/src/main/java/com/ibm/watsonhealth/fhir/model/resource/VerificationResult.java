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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Signature;
import com.ibm.watsonhealth.fhir.model.type.Status;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes validation requirements, source(s), status and dates for one or more elements.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class VerificationResult extends DomainResource {
    private final List<Reference> target;
    private final List<String> targetLocation;
    private final CodeableConcept need;
    private final Status status;
    private final DateTime statusDate;
    private final CodeableConcept validationType;
    private final List<CodeableConcept> validationProcess;
    private final Timing frequency;
    private final DateTime lastPerformed;
    private final Date nextScheduled;
    private final CodeableConcept failureAction;
    private final List<PrimarySource> primarySource;
    private final Attestation attestation;
    private final List<Validator> validator;

    private VerificationResult(Builder builder) {
        super(builder);
        this.target = builder.target;
        this.targetLocation = builder.targetLocation;
        this.need = builder.need;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.statusDate = builder.statusDate;
        this.validationType = builder.validationType;
        this.validationProcess = builder.validationProcess;
        this.frequency = builder.frequency;
        this.lastPerformed = builder.lastPerformed;
        this.nextScheduled = builder.nextScheduled;
        this.failureAction = builder.failureAction;
        this.primarySource = builder.primarySource;
        this.attestation = builder.attestation;
        this.validator = builder.validator;
    }

    /**
     * <p>
     * A resource that was validated.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getTarget() {
        return target;
    }

    /**
     * <p>
     * The fhirpath location(s) within the resource that was validated.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getTargetLocation() {
        return targetLocation;
    }

    /**
     * <p>
     * The frequency with which the target must be validated (none; initial; periodic).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getNeed() {
        return need;
    }

    /**
     * <p>
     * The validation status of the target (attested; validated; in process; requires revalidation; validation failed; 
     * revalidation failed).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Status}.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * <p>
     * When the validation status was updated.
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
     * What the target is validated against (nothing; primary source; multiple sources).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getValidationType() {
        return validationType;
    }

    /**
     * <p>
     * The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
     * standalone; in context).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getValidationProcess() {
        return validationProcess;
    }

    /**
     * <p>
     * Frequency of revalidation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Timing}.
     */
    public Timing getFrequency() {
        return frequency;
    }

    /**
     * <p>
     * The date/time validation was last completed (including failed validations).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getLastPerformed() {
        return lastPerformed;
    }

    /**
     * <p>
     * The date when target is next validated, if appropriate.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getNextScheduled() {
        return nextScheduled;
    }

    /**
     * <p>
     * The result if validation fails (fatal; warning; record only; none).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getFailureAction() {
        return failureAction;
    }

    /**
     * <p>
     * Information about the primary source(s) involved in validation.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link PrimarySource}.
     */
    public List<PrimarySource> getPrimarySource() {
        return primarySource;
    }

    /**
     * <p>
     * Information about the entity attesting to information.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Attestation}.
     */
    public Attestation getAttestation() {
        return attestation;
    }

    /**
     * <p>
     * Information about the entity validating information.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Validator}.
     */
    public List<Validator> getValidator() {
        return validator;
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.target.addAll(target);
        builder.targetLocation.addAll(targetLocation);
        builder.need = need;
        builder.statusDate = statusDate;
        builder.validationType = validationType;
        builder.validationProcess.addAll(validationProcess);
        builder.frequency = frequency;
        builder.lastPerformed = lastPerformed;
        builder.nextScheduled = nextScheduled;
        builder.failureAction = failureAction;
        builder.primarySource.addAll(primarySource);
        builder.attestation = attestation;
        builder.validator.addAll(validator);
        return builder;
    }

    public static Builder builder(Status status) {
        return new Builder(status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Status status;

        // optional
        private List<Reference> target = new ArrayList<>();
        private List<String> targetLocation = new ArrayList<>();
        private CodeableConcept need;
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

        private Builder(Status status) {
            super();
            this.status = status;
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
         * A resource that was validated.
         * </p>
         * 
         * @param target
         *     A resource that was validated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder target(Reference... target) {
            for (Reference value : target) {
                this.target.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A resource that was validated.
         * </p>
         * 
         * @param target
         *     A resource that was validated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder target(Collection<Reference> target) {
            this.target.addAll(target);
            return this;
        }

        /**
         * <p>
         * The fhirpath location(s) within the resource that was validated.
         * </p>
         * 
         * @param targetLocation
         *     The fhirpath location(s) within the resource that was validated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder targetLocation(String... targetLocation) {
            for (String value : targetLocation) {
                this.targetLocation.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The fhirpath location(s) within the resource that was validated.
         * </p>
         * 
         * @param targetLocation
         *     The fhirpath location(s) within the resource that was validated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder targetLocation(Collection<String> targetLocation) {
            this.targetLocation.addAll(targetLocation);
            return this;
        }

        /**
         * <p>
         * The frequency with which the target must be validated (none; initial; periodic).
         * </p>
         * 
         * @param need
         *     none | initial | periodic
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder need(CodeableConcept need) {
            this.need = need;
            return this;
        }

        /**
         * <p>
         * When the validation status was updated.
         * </p>
         * 
         * @param statusDate
         *     When the validation status was updated
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
         * What the target is validated against (nothing; primary source; multiple sources).
         * </p>
         * 
         * @param validationType
         *     nothing | primary | multiple
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validationType(CodeableConcept validationType) {
            this.validationType = validationType;
            return this;
        }

        /**
         * <p>
         * The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         * standalone; in context).
         * </p>
         * 
         * @param validationProcess
         *     The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         *     standalone; in context)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validationProcess(CodeableConcept... validationProcess) {
            for (CodeableConcept value : validationProcess) {
                this.validationProcess.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         * standalone; in context).
         * </p>
         * 
         * @param validationProcess
         *     The primary process by which the target is validated (edit check; value set; primary source; multiple sources; 
         *     standalone; in context)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validationProcess(Collection<CodeableConcept> validationProcess) {
            this.validationProcess.addAll(validationProcess);
            return this;
        }

        /**
         * <p>
         * Frequency of revalidation.
         * </p>
         * 
         * @param frequency
         *     Frequency of revalidation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder frequency(Timing frequency) {
            this.frequency = frequency;
            return this;
        }

        /**
         * <p>
         * The date/time validation was last completed (including failed validations).
         * </p>
         * 
         * @param lastPerformed
         *     The date/time validation was last completed (including failed validations)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lastPerformed(DateTime lastPerformed) {
            this.lastPerformed = lastPerformed;
            return this;
        }

        /**
         * <p>
         * The date when target is next validated, if appropriate.
         * </p>
         * 
         * @param nextScheduled
         *     The date when target is next validated, if appropriate
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder nextScheduled(Date nextScheduled) {
            this.nextScheduled = nextScheduled;
            return this;
        }

        /**
         * <p>
         * The result if validation fails (fatal; warning; record only; none).
         * </p>
         * 
         * @param failureAction
         *     fatal | warn | rec-only | none
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder failureAction(CodeableConcept failureAction) {
            this.failureAction = failureAction;
            return this;
        }

        /**
         * <p>
         * Information about the primary source(s) involved in validation.
         * </p>
         * 
         * @param primarySource
         *     Information about the primary source(s) involved in validation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder primarySource(PrimarySource... primarySource) {
            for (PrimarySource value : primarySource) {
                this.primarySource.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Information about the primary source(s) involved in validation.
         * </p>
         * 
         * @param primarySource
         *     Information about the primary source(s) involved in validation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder primarySource(Collection<PrimarySource> primarySource) {
            this.primarySource.addAll(primarySource);
            return this;
        }

        /**
         * <p>
         * Information about the entity attesting to information.
         * </p>
         * 
         * @param attestation
         *     Information about the entity attesting to information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder attestation(Attestation attestation) {
            this.attestation = attestation;
            return this;
        }

        /**
         * <p>
         * Information about the entity validating information.
         * </p>
         * 
         * @param validator
         *     Information about the entity validating information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validator(Validator... validator) {
            for (Validator value : validator) {
                this.validator.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Information about the entity validating information.
         * </p>
         * 
         * @param validator
         *     Information about the entity validating information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validator(Collection<Validator> validator) {
            this.validator.addAll(validator);
            return this;
        }

        @Override
        public VerificationResult build() {
            return new VerificationResult(this);
        }
    }

    /**
     * <p>
     * Information about the primary source(s) involved in validation.
     * </p>
     */
    public static class PrimarySource extends BackboneElement {
        private final Reference who;
        private final List<CodeableConcept> type;
        private final List<CodeableConcept> communicationMethod;
        private final CodeableConcept validationStatus;
        private final DateTime validationDate;
        private final CodeableConcept canPushUpdates;
        private final List<CodeableConcept> pushTypeAvailable;

        private PrimarySource(Builder builder) {
            super(builder);
            this.who = builder.who;
            this.type = builder.type;
            this.communicationMethod = builder.communicationMethod;
            this.validationStatus = builder.validationStatus;
            this.validationDate = builder.validationDate;
            this.canPushUpdates = builder.canPushUpdates;
            this.pushTypeAvailable = builder.pushTypeAvailable;
        }

        /**
         * <p>
         * Reference to the primary source.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getWho() {
            return who;
        }

        /**
         * <p>
         * Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
         * Registration Authority; legal source; issuing source; authoritative source).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * <p>
         * Method for communicating with the primary source (manual; API; Push).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getCommunicationMethod() {
            return communicationMethod;
        }

        /**
         * <p>
         * Status of the validation of the target against the primary source (successful; failed; unknown).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getValidationStatus() {
            return validationStatus;
        }

        /**
         * <p>
         * When the target was validated against the primary source.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getValidationDate() {
            return validationDate;
        }

        /**
         * <p>
         * Ability of the primary source to push updates/alerts (yes; no; undetermined).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCanPushUpdates() {
            return canPushUpdates;
        }

        /**
         * <p>
         * Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getPushTypeAvailable() {
            return pushTypeAvailable;
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
                    accept(who, "who", visitor);
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(communicationMethod, "communicationMethod", visitor, CodeableConcept.class);
                    accept(validationStatus, "validationStatus", visitor);
                    accept(validationDate, "validationDate", visitor);
                    accept(canPushUpdates, "canPushUpdates", visitor);
                    accept(pushTypeAvailable, "pushTypeAvailable", visitor, CodeableConcept.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
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
             * Reference to the primary source.
             * </p>
             * 
             * @param who
             *     Reference to the primary source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder who(Reference who) {
                this.who = who;
                return this;
            }

            /**
             * <p>
             * Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             * Registration Authority; legal source; issuing source; authoritative source).
             * </p>
             * 
             * @param type
             *     Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             *     Registration Authority; legal source; issuing source; authoritative source)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept... type) {
                for (CodeableConcept value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             * Registration Authority; legal source; issuing source; authoritative source).
             * </p>
             * 
             * @param type
             *     Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; 
             *     Registration Authority; legal source; issuing source; authoritative source)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type.addAll(type);
                return this;
            }

            /**
             * <p>
             * Method for communicating with the primary source (manual; API; Push).
             * </p>
             * 
             * @param communicationMethod
             *     Method for exchanging information with the primary source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder communicationMethod(CodeableConcept... communicationMethod) {
                for (CodeableConcept value : communicationMethod) {
                    this.communicationMethod.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Method for communicating with the primary source (manual; API; Push).
             * </p>
             * 
             * @param communicationMethod
             *     Method for exchanging information with the primary source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder communicationMethod(Collection<CodeableConcept> communicationMethod) {
                this.communicationMethod.addAll(communicationMethod);
                return this;
            }

            /**
             * <p>
             * Status of the validation of the target against the primary source (successful; failed; unknown).
             * </p>
             * 
             * @param validationStatus
             *     successful | failed | unknown
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder validationStatus(CodeableConcept validationStatus) {
                this.validationStatus = validationStatus;
                return this;
            }

            /**
             * <p>
             * When the target was validated against the primary source.
             * </p>
             * 
             * @param validationDate
             *     When the target was validated against the primary source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder validationDate(DateTime validationDate) {
                this.validationDate = validationDate;
                return this;
            }

            /**
             * <p>
             * Ability of the primary source to push updates/alerts (yes; no; undetermined).
             * </p>
             * 
             * @param canPushUpdates
             *     yes | no | undetermined
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder canPushUpdates(CodeableConcept canPushUpdates) {
                this.canPushUpdates = canPushUpdates;
                return this;
            }

            /**
             * <p>
             * Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).
             * </p>
             * 
             * @param pushTypeAvailable
             *     specific | any | source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder pushTypeAvailable(CodeableConcept... pushTypeAvailable) {
                for (CodeableConcept value : pushTypeAvailable) {
                    this.pushTypeAvailable.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).
             * </p>
             * 
             * @param pushTypeAvailable
             *     specific | any | source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder pushTypeAvailable(Collection<CodeableConcept> pushTypeAvailable) {
                this.pushTypeAvailable.addAll(pushTypeAvailable);
                return this;
            }

            @Override
            public PrimarySource build() {
                return new PrimarySource(this);
            }

            private static Builder from(PrimarySource primarySource) {
                Builder builder = new Builder();
                builder.id = primarySource.id;
                builder.extension.addAll(primarySource.extension);
                builder.modifierExtension.addAll(primarySource.modifierExtension);
                builder.who = primarySource.who;
                builder.type.addAll(primarySource.type);
                builder.communicationMethod.addAll(primarySource.communicationMethod);
                builder.validationStatus = primarySource.validationStatus;
                builder.validationDate = primarySource.validationDate;
                builder.canPushUpdates = primarySource.canPushUpdates;
                builder.pushTypeAvailable.addAll(primarySource.pushTypeAvailable);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Information about the entity attesting to information.
     * </p>
     */
    public static class Attestation extends BackboneElement {
        private final Reference who;
        private final Reference onBehalfOf;
        private final CodeableConcept communicationMethod;
        private final Date date;
        private final String sourceIdentityCertificate;
        private final String proxyIdentityCertificate;
        private final Signature proxySignature;
        private final Signature sourceSignature;

        private Attestation(Builder builder) {
            super(builder);
            this.who = builder.who;
            this.onBehalfOf = builder.onBehalfOf;
            this.communicationMethod = builder.communicationMethod;
            this.date = builder.date;
            this.sourceIdentityCertificate = builder.sourceIdentityCertificate;
            this.proxyIdentityCertificate = builder.proxyIdentityCertificate;
            this.proxySignature = builder.proxySignature;
            this.sourceSignature = builder.sourceSignature;
        }

        /**
         * <p>
         * The individual or organization attesting to information.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getWho() {
            return who;
        }

        /**
         * <p>
         * When the who is asserting on behalf of another (organization or individual).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getOnBehalfOf() {
            return onBehalfOf;
        }

        /**
         * <p>
         * The method by which attested information was submitted/retrieved (manual; API; Push).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCommunicationMethod() {
            return communicationMethod;
        }

        /**
         * <p>
         * The date the information was attested to.
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
         * A digital identity certificate associated with the attestation source.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSourceIdentityCertificate() {
            return sourceIdentityCertificate;
        }

        /**
         * <p>
         * A digital identity certificate associated with the proxy entity submitting attested information on behalf of the 
         * attestation source.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getProxyIdentityCertificate() {
            return proxyIdentityCertificate;
        }

        /**
         * <p>
         * Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of 
         * the attestation source.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Signature}.
         */
        public Signature getProxySignature() {
            return proxySignature;
        }

        /**
         * <p>
         * Signed assertion by the attestation source that they have attested to the information.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Signature}.
         */
        public Signature getSourceSignature() {
            return sourceSignature;
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
                    accept(who, "who", visitor);
                    accept(onBehalfOf, "onBehalfOf", visitor);
                    accept(communicationMethod, "communicationMethod", visitor);
                    accept(date, "date", visitor);
                    accept(sourceIdentityCertificate, "sourceIdentityCertificate", visitor);
                    accept(proxyIdentityCertificate, "proxyIdentityCertificate", visitor);
                    accept(proxySignature, "proxySignature", visitor);
                    accept(sourceSignature, "sourceSignature", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
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
             * The individual or organization attesting to information.
             * </p>
             * 
             * @param who
             *     The individual or organization attesting to information
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder who(Reference who) {
                this.who = who;
                return this;
            }

            /**
             * <p>
             * When the who is asserting on behalf of another (organization or individual).
             * </p>
             * 
             * @param onBehalfOf
             *     When the who is asserting on behalf of another (organization or individual)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder onBehalfOf(Reference onBehalfOf) {
                this.onBehalfOf = onBehalfOf;
                return this;
            }

            /**
             * <p>
             * The method by which attested information was submitted/retrieved (manual; API; Push).
             * </p>
             * 
             * @param communicationMethod
             *     The method by which attested information was submitted/retrieved
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder communicationMethod(CodeableConcept communicationMethod) {
                this.communicationMethod = communicationMethod;
                return this;
            }

            /**
             * <p>
             * The date the information was attested to.
             * </p>
             * 
             * @param date
             *     The date the information was attested to
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
             * A digital identity certificate associated with the attestation source.
             * </p>
             * 
             * @param sourceIdentityCertificate
             *     A digital identity certificate associated with the attestation source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder sourceIdentityCertificate(String sourceIdentityCertificate) {
                this.sourceIdentityCertificate = sourceIdentityCertificate;
                return this;
            }

            /**
             * <p>
             * A digital identity certificate associated with the proxy entity submitting attested information on behalf of the 
             * attestation source.
             * </p>
             * 
             * @param proxyIdentityCertificate
             *     A digital identity certificate associated with the proxy entity submitting attested information on behalf of the 
             *     attestation source
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder proxyIdentityCertificate(String proxyIdentityCertificate) {
                this.proxyIdentityCertificate = proxyIdentityCertificate;
                return this;
            }

            /**
             * <p>
             * Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of 
             * the attestation source.
             * </p>
             * 
             * @param proxySignature
             *     Proxy signature
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder proxySignature(Signature proxySignature) {
                this.proxySignature = proxySignature;
                return this;
            }

            /**
             * <p>
             * Signed assertion by the attestation source that they have attested to the information.
             * </p>
             * 
             * @param sourceSignature
             *     Attester signature
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder sourceSignature(Signature sourceSignature) {
                this.sourceSignature = sourceSignature;
                return this;
            }

            @Override
            public Attestation build() {
                return new Attestation(this);
            }

            private static Builder from(Attestation attestation) {
                Builder builder = new Builder();
                builder.id = attestation.id;
                builder.extension.addAll(attestation.extension);
                builder.modifierExtension.addAll(attestation.modifierExtension);
                builder.who = attestation.who;
                builder.onBehalfOf = attestation.onBehalfOf;
                builder.communicationMethod = attestation.communicationMethod;
                builder.date = attestation.date;
                builder.sourceIdentityCertificate = attestation.sourceIdentityCertificate;
                builder.proxyIdentityCertificate = attestation.proxyIdentityCertificate;
                builder.proxySignature = attestation.proxySignature;
                builder.sourceSignature = attestation.sourceSignature;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Information about the entity validating information.
     * </p>
     */
    public static class Validator extends BackboneElement {
        private final Reference organization;
        private final String identityCertificate;
        private final Signature attestationSignature;

        private Validator(Builder builder) {
            super(builder);
            this.organization = ValidationSupport.requireNonNull(builder.organization, "organization");
            this.identityCertificate = builder.identityCertificate;
            this.attestationSignature = builder.attestationSignature;
        }

        /**
         * <p>
         * Reference to the organization validating information.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getOrganization() {
            return organization;
        }

        /**
         * <p>
         * A digital identity certificate associated with the validator.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getIdentityCertificate() {
            return identityCertificate;
        }

        /**
         * <p>
         * Signed assertion by the validator that they have validated the information.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Signature}.
         */
        public Signature getAttestationSignature() {
            return attestationSignature;
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
                    accept(organization, "organization", visitor);
                    accept(identityCertificate, "identityCertificate", visitor);
                    accept(attestationSignature, "attestationSignature", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Reference organization) {
            return new Builder(organization);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference organization;

            // optional
            private String identityCertificate;
            private Signature attestationSignature;

            private Builder(Reference organization) {
                super();
                this.organization = organization;
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
             * A digital identity certificate associated with the validator.
             * </p>
             * 
             * @param identityCertificate
             *     A digital identity certificate associated with the validator
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder identityCertificate(String identityCertificate) {
                this.identityCertificate = identityCertificate;
                return this;
            }

            /**
             * <p>
             * Signed assertion by the validator that they have validated the information.
             * </p>
             * 
             * @param attestationSignature
             *     Validator signature
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder attestationSignature(Signature attestationSignature) {
                this.attestationSignature = attestationSignature;
                return this;
            }

            @Override
            public Validator build() {
                return new Validator(this);
            }

            private static Builder from(Validator validator) {
                Builder builder = new Builder(validator.organization);
                builder.id = validator.id;
                builder.extension.addAll(validator.extension);
                builder.modifierExtension.addAll(validator.modifierExtension);
                builder.identityCertificate = validator.identityCertificate;
                builder.attestationSignature = validator.attestationSignature;
                return builder;
            }
        }
    }
}
