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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
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
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DetectedIssueSeverity;
import com.ibm.fhir.model.type.code.DetectedIssueStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
 * patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "detectedIssue-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/detectedissue-category",
    expression = "code.exists() implies (code.memberOf('http://hl7.org/fhir/ValueSet/detectedissue-category', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/DetectedIssue",
    generated = true
)
@Constraint(
    id = "detectedIssue-1",
    level = "Warning",
    location = "mitigation.action",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/detectedissue-mitigation-action",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/detectedissue-mitigation-action', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/DetectedIssue",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DetectedIssue extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "DetectedIssueStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Indicates the status of the identified issue.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-status|4.0.1"
    )
    @Required
    private final DetectedIssueStatus status;
    @Summary
    @Binding(
        bindingName = "DetectedIssueCategory",
        strength = BindingStrength.Value.PREFERRED,
        description = "Codes identifying the general type of detected issue; e.g. Drug-drug interaction, Timing issue, Duplicate therapy, etc.",
        valueSet = "http://hl7.org/fhir/ValueSet/detectedissue-category"
    )
    private final CodeableConcept code;
    @Summary
    @Binding(
        bindingName = "DetectedIssueSeverity",
        strength = BindingStrength.Value.REQUIRED,
        description = "Indicates the potential degree of impact of the identified issue on the patient.",
        valueSet = "http://hl7.org/fhir/ValueSet/detectedissue-severity|4.0.1"
    )
    private final DetectedIssueSeverity severity;
    @Summary
    @ReferenceTarget({ "Patient" })
    private final Reference patient;
    @Summary
    @Choice({ DateTime.class, Period.class })
    private final Element identified;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Device" })
    private final Reference author;
    @Summary
    private final List<Reference> implicated;
    private final List<Evidence> evidence;
    private final String detail;
    private final Uri reference;
    private final List<Mitigation> mitigation;

    private DetectedIssue(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        code = builder.code;
        severity = builder.severity;
        patient = builder.patient;
        identified = builder.identified;
        author = builder.author;
        implicated = Collections.unmodifiableList(builder.implicated);
        evidence = Collections.unmodifiableList(builder.evidence);
        detail = builder.detail;
        reference = builder.reference;
        mitigation = Collections.unmodifiableList(builder.mitigation);
    }

    /**
     * Business identifier associated with the detected issue record.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Indicates the status of the detected issue.
     * 
     * @return
     *     An immutable object of type {@link DetectedIssueStatus} that is non-null.
     */
    public DetectedIssueStatus getStatus() {
        return status;
    }

    /**
     * Identifies the general type of issue identified.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     * 
     * @return
     *     An immutable object of type {@link DetectedIssueSeverity} that may be null.
     */
    public DetectedIssueSeverity getSeverity() {
        return severity;
    }

    /**
     * Indicates the patient whose record the detected issue is associated with.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The date or period when the detected issue was initially identified.
     * 
     * @return
     *     An immutable object of type {@link DateTime} or {@link Period} that may be null.
     */
    public Element getIdentified() {
        return identified;
    }

    /**
     * Individual or device responsible for the issue being raised. For example, a decision support application or a 
     * pharmacist conducting a medication review.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * Indicates the resource representing the current activity or proposed activity that is potentially problematic.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getImplicated() {
        return implicated;
    }

    /**
     * Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a 
     * GuidanceResponse or MeasureReport.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Evidence} that may be empty.
     */
    public List<Evidence> getEvidence() {
        return evidence;
    }

    /**
     * A textual explanation of the detected issue.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDetail() {
        return detail;
    }

    /**
     * The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getReference() {
        return reference;
    }

    /**
     * Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified 
     * by the detected issue from manifesting. Can also reflect an observation of known mitigating factors that may 
     * reduce/eliminate the need for any action.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Mitigation} that may be empty.
     */
    public List<Mitigation> getMitigation() {
        return mitigation;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (code != null) || 
            (severity != null) || 
            (patient != null) || 
            (identified != null) || 
            (author != null) || 
            !implicated.isEmpty() || 
            !evidence.isEmpty() || 
            (detail != null) || 
            (reference != null) || 
            !mitigation.isEmpty();
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
                accept(code, "code", visitor);
                accept(severity, "severity", visitor);
                accept(patient, "patient", visitor);
                accept(identified, "identified", visitor);
                accept(author, "author", visitor);
                accept(implicated, "implicated", visitor, Reference.class);
                accept(evidence, "evidence", visitor, Evidence.class);
                accept(detail, "detail", visitor);
                accept(reference, "reference", visitor);
                accept(mitigation, "mitigation", visitor, Mitigation.class);
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
        DetectedIssue other = (DetectedIssue) obj;
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
            Objects.equals(code, other.code) && 
            Objects.equals(severity, other.severity) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(identified, other.identified) && 
            Objects.equals(author, other.author) && 
            Objects.equals(implicated, other.implicated) && 
            Objects.equals(evidence, other.evidence) && 
            Objects.equals(detail, other.detail) && 
            Objects.equals(reference, other.reference) && 
            Objects.equals(mitigation, other.mitigation);
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
                code, 
                severity, 
                patient, 
                identified, 
                author, 
                implicated, 
                evidence, 
                detail, 
                reference, 
                mitigation);
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
        private DetectedIssueStatus status;
        private CodeableConcept code;
        private DetectedIssueSeverity severity;
        private Reference patient;
        private Element identified;
        private Reference author;
        private List<Reference> implicated = new ArrayList<>();
        private List<Evidence> evidence = new ArrayList<>();
        private String detail;
        private Uri reference;
        private List<Mitigation> mitigation = new ArrayList<>();

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
         * Business identifier associated with the detected issue record.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique id for the detected issue
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
         * Business identifier associated with the detected issue record.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique id for the detected issue
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
         * Indicates the status of the detected issue.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     registered | preliminary | final | amended +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(DetectedIssueStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Identifies the general type of issue identified.
         * 
         * @param code
         *     Issue Category, e.g. drug-drug, duplicate therapy, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
         * 
         * @param severity
         *     high | moderate | low
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder severity(DetectedIssueSeverity severity) {
            this.severity = severity;
            return this;
        }

        /**
         * Indicates the patient whose record the detected issue is associated with.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Associated patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The date or period when the detected issue was initially identified.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param identified
         *     When identified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identified(Element identified) {
            this.identified = identified;
            return this;
        }

        /**
         * Individual or device responsible for the issue being raised. For example, a decision support application or a 
         * pharmacist conducting a medication review.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param author
         *     The provider or device that identified the issue
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * Indicates the resource representing the current activity or proposed activity that is potentially problematic.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param implicated
         *     Problem resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder implicated(Reference... implicated) {
            for (Reference value : implicated) {
                this.implicated.add(value);
            }
            return this;
        }

        /**
         * Indicates the resource representing the current activity or proposed activity that is potentially problematic.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param implicated
         *     Problem resource
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder implicated(Collection<Reference> implicated) {
            this.implicated = new ArrayList<>(implicated);
            return this;
        }

        /**
         * Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a 
         * GuidanceResponse or MeasureReport.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param evidence
         *     Supporting evidence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder evidence(Evidence... evidence) {
            for (Evidence value : evidence) {
                this.evidence.add(value);
            }
            return this;
        }

        /**
         * Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a 
         * GuidanceResponse or MeasureReport.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param evidence
         *     Supporting evidence
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder evidence(Collection<Evidence> evidence) {
            this.evidence = new ArrayList<>(evidence);
            return this;
        }

        /**
         * Convenience method for setting {@code detail}.
         * 
         * @param detail
         *     Description and context
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #detail(com.ibm.fhir.model.type.String)
         */
        public Builder detail(java.lang.String detail) {
            this.detail = (detail == null) ? null : String.of(detail);
            return this;
        }

        /**
         * A textual explanation of the detected issue.
         * 
         * @param detail
         *     Description and context
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        /**
         * The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.
         * 
         * @param reference
         *     Authority for issue
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reference(Uri reference) {
            this.reference = reference;
            return this;
        }

        /**
         * Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified 
         * by the detected issue from manifesting. Can also reflect an observation of known mitigating factors that may 
         * reduce/eliminate the need for any action.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param mitigation
         *     Step taken to address
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder mitigation(Mitigation... mitigation) {
            for (Mitigation value : mitigation) {
                this.mitigation.add(value);
            }
            return this;
        }

        /**
         * Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified 
         * by the detected issue from manifesting. Can also reflect an observation of known mitigating factors that may 
         * reduce/eliminate the need for any action.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param mitigation
         *     Step taken to address
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder mitigation(Collection<Mitigation> mitigation) {
            this.mitigation = new ArrayList<>(mitigation);
            return this;
        }

        /**
         * Build the {@link DetectedIssue}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DetectedIssue}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DetectedIssue per the base specification
         */
        @Override
        public DetectedIssue build() {
            DetectedIssue detectedIssue = new DetectedIssue(this);
            if (validating) {
                validate(detectedIssue);
            }
            return detectedIssue;
        }

        protected void validate(DetectedIssue detectedIssue) {
            super.validate(detectedIssue);
            ValidationSupport.checkList(detectedIssue.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(detectedIssue.status, "status");
            ValidationSupport.choiceElement(detectedIssue.identified, "identified", DateTime.class, Period.class);
            ValidationSupport.checkList(detectedIssue.implicated, "implicated", Reference.class);
            ValidationSupport.checkList(detectedIssue.evidence, "evidence", Evidence.class);
            ValidationSupport.checkList(detectedIssue.mitigation, "mitigation", Mitigation.class);
            ValidationSupport.checkReferenceType(detectedIssue.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(detectedIssue.author, "author", "Practitioner", "PractitionerRole", "Device");
        }

        protected Builder from(DetectedIssue detectedIssue) {
            super.from(detectedIssue);
            identifier.addAll(detectedIssue.identifier);
            status = detectedIssue.status;
            code = detectedIssue.code;
            severity = detectedIssue.severity;
            patient = detectedIssue.patient;
            identified = detectedIssue.identified;
            author = detectedIssue.author;
            implicated.addAll(detectedIssue.implicated);
            evidence.addAll(detectedIssue.evidence);
            detail = detectedIssue.detail;
            reference = detectedIssue.reference;
            mitigation.addAll(detectedIssue.mitigation);
            return this;
        }
    }

    /**
     * Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a 
     * GuidanceResponse or MeasureReport.
     */
    public static class Evidence extends BackboneElement {
        @Binding(
            bindingName = "DetectedIssueEvidenceCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes that describes the types of evidence for a detected issue.",
            valueSet = "http://hl7.org/fhir/ValueSet/manifestation-or-symptom"
        )
        private final List<CodeableConcept> code;
        private final List<Reference> detail;

        private Evidence(Builder builder) {
            super(builder);
            code = Collections.unmodifiableList(builder.code);
            detail = Collections.unmodifiableList(builder.detail);
        }

        /**
         * A manifestation that led to the recording of this detected issue.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !code.isEmpty() || 
                !detail.isEmpty();
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
                    accept(code, "code", visitor, CodeableConcept.class);
                    accept(detail, "detail", visitor, Reference.class);
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
            Evidence other = (Evidence) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    detail);
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
            private List<CodeableConcept> code = new ArrayList<>();
            private List<Reference> detail = new ArrayList<>();

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
             * A manifestation that led to the recording of this detected issue.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Manifestation
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
             * A manifestation that led to the recording of this detected issue.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Manifestation
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
             * Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     Supporting information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Reference... detail) {
                for (Reference value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     Supporting information
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder detail(Collection<Reference> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            /**
             * Build the {@link Evidence}
             * 
             * @return
             *     An immutable object of type {@link Evidence}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Evidence per the base specification
             */
            @Override
            public Evidence build() {
                Evidence evidence = new Evidence(this);
                if (validating) {
                    validate(evidence);
                }
                return evidence;
            }

            protected void validate(Evidence evidence) {
                super.validate(evidence);
                ValidationSupport.checkList(evidence.code, "code", CodeableConcept.class);
                ValidationSupport.checkList(evidence.detail, "detail", Reference.class);
                ValidationSupport.requireValueOrChildren(evidence);
            }

            protected Builder from(Evidence evidence) {
                super.from(evidence);
                code.addAll(evidence.code);
                detail.addAll(evidence.detail);
                return this;
            }
        }
    }

    /**
     * Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified 
     * by the detected issue from manifesting. Can also reflect an observation of known mitigating factors that may 
     * reduce/eliminate the need for any action.
     */
    public static class Mitigation extends BackboneElement {
        @Binding(
            bindingName = "DetectedIssueMitigationAction",
            strength = BindingStrength.Value.PREFERRED,
            description = "Codes describing steps taken to resolve the issue or other circumstances that mitigate the risk associated with the issue; e.g. 'added concurrent therapy', 'prior therapy documented', etc.",
            valueSet = "http://hl7.org/fhir/ValueSet/detectedissue-mitigation-action"
        )
        @Required
        private final CodeableConcept action;
        private final DateTime date;
        @ReferenceTarget({ "Practitioner", "PractitionerRole" })
        private final Reference author;

        private Mitigation(Builder builder) {
            super(builder);
            action = builder.action;
            date = builder.date;
            author = builder.author;
        }

        /**
         * Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with 
         * the identified issue.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getAction() {
            return action;
        }

        /**
         * Indicates when the mitigating action was documented.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getDate() {
            return date;
        }

        /**
         * Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getAuthor() {
            return author;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (action != null) || 
                (date != null) || 
                (author != null);
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
                    accept(action, "action", visitor);
                    accept(date, "date", visitor);
                    accept(author, "author", visitor);
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
            Mitigation other = (Mitigation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(action, other.action) && 
                Objects.equals(date, other.date) && 
                Objects.equals(author, other.author);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    action, 
                    date, 
                    author);
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
            private CodeableConcept action;
            private DateTime date;
            private Reference author;

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
             * Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with 
             * the identified issue.
             * 
             * <p>This element is required.
             * 
             * @param action
             *     What mitigation?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(CodeableConcept action) {
                this.action = action;
                return this;
            }

            /**
             * Indicates when the mitigating action was documented.
             * 
             * @param date
             *     Date committed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(DateTime date) {
                this.date = date;
                return this;
            }

            /**
             * Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param author
             *     Who is committing?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder author(Reference author) {
                this.author = author;
                return this;
            }

            /**
             * Build the {@link Mitigation}
             * 
             * <p>Required elements:
             * <ul>
             * <li>action</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Mitigation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Mitigation per the base specification
             */
            @Override
            public Mitigation build() {
                Mitigation mitigation = new Mitigation(this);
                if (validating) {
                    validate(mitigation);
                }
                return mitigation;
            }

            protected void validate(Mitigation mitigation) {
                super.validate(mitigation);
                ValidationSupport.requireNonNull(mitigation.action, "action");
                ValidationSupport.checkReferenceType(mitigation.author, "author", "Practitioner", "PractitionerRole");
                ValidationSupport.requireValueOrChildren(mitigation);
            }

            protected Builder from(Mitigation mitigation) {
                super.from(mitigation);
                action = mitigation.action;
                date = mitigation.date;
                author = mitigation.author;
                return this;
            }
        }
    }
}
