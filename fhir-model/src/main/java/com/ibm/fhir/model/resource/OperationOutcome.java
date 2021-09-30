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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A collection of error, warning, or information messages that result from a system action.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class OperationOutcome extends DomainResource {
    @Summary
    @Required
    private final List<Issue> issue;

    private OperationOutcome(Builder builder) {
        super(builder);
        issue = Collections.unmodifiableList(builder.issue);
    }

    /**
     * An error, warning, or information message that results from a system action.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Issue} that is non-empty.
     */
    public List<Issue> getIssue() {
        return issue;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !issue.isEmpty();
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
                accept(issue, "issue", visitor, Issue.class);
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
        OperationOutcome other = (OperationOutcome) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(issue, other.issue);
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
                issue);
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
        private List<Issue> issue = new ArrayList<>();

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
         * An error, warning, or information message that results from a system action.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param issue
         *     A single issue associated with the action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder issue(Issue... issue) {
            for (Issue value : issue) {
                this.issue.add(value);
            }
            return this;
        }

        /**
         * An error, warning, or information message that results from a system action.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param issue
         *     A single issue associated with the action
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder issue(Collection<Issue> issue) {
            this.issue = new ArrayList<>(issue);
            return this;
        }

        /**
         * Build the {@link OperationOutcome}
         * 
         * <p>Required elements:
         * <ul>
         * <li>issue</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link OperationOutcome}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid OperationOutcome per the base specification
         */
        @Override
        public OperationOutcome build() {
            OperationOutcome operationOutcome = new OperationOutcome(this);
            if (validating) {
                validate(operationOutcome);
            }
            return operationOutcome;
        }

        protected void validate(OperationOutcome operationOutcome) {
            super.validate(operationOutcome);
            ValidationSupport.checkNonEmptyList(operationOutcome.issue, "issue", Issue.class);
        }

        protected Builder from(OperationOutcome operationOutcome) {
            super.from(operationOutcome);
            issue.addAll(operationOutcome.issue);
            return this;
        }
    }

    /**
     * An error, warning, or information message that results from a system action.
     */
    public static class Issue extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "IssueSeverity",
            strength = BindingStrength.Value.REQUIRED,
            description = "How the issue affects the success of the action.",
            valueSet = "http://hl7.org/fhir/ValueSet/issue-severity|4.0.1"
        )
        @Required
        private final IssueSeverity severity;
        @Summary
        @Binding(
            bindingName = "IssueType",
            strength = BindingStrength.Value.REQUIRED,
            description = "A code that describes the type of issue.",
            valueSet = "http://hl7.org/fhir/ValueSet/issue-type|4.0.1"
        )
        @Required
        private final IssueType code;
        @Summary
        @Binding(
            bindingName = "IssueDetails",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A code that provides details as the exact issue.",
            valueSet = "http://hl7.org/fhir/ValueSet/operation-outcome"
        )
        private final CodeableConcept details;
        @Summary
        private final String diagnostics;
        @Summary
        private final List<String> location;
        @Summary
        private final List<String> expression;

        private Issue(Builder builder) {
            super(builder);
            severity = builder.severity;
            code = builder.code;
            details = builder.details;
            diagnostics = builder.diagnostics;
            location = Collections.unmodifiableList(builder.location);
            expression = Collections.unmodifiableList(builder.expression);
        }

        /**
         * Indicates whether the issue indicates a variation from successful processing.
         * 
         * @return
         *     An immutable object of type {@link IssueSeverity} that is non-null.
         */
        public IssueSeverity getSeverity() {
            return severity;
        }

        /**
         * Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code 
         * from the IssueType value set, and may additional provide its own code for the error in the details element.
         * 
         * @return
         *     An immutable object of type {@link IssueType} that is non-null.
         */
        public IssueType getCode() {
            return code;
        }

        /**
         * Additional details about the error. This may be a text description of the error or a system code that identifies the 
         * error.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getDetails() {
            return details;
        }

        /**
         * Additional diagnostic information about the issue.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDiagnostics() {
            return diagnostics;
        }

        /**
         * This element is deprecated because it is XML specific. It is replaced by issue.expression, which is format 
         * independent, and simpler to parse. 
         * 
         * <p>For resource issues, this will be a simple XPath limited to element names, repetition indicators and the default 
         * child accessor that identifies one of the elements in the resource that caused this issue to be raised. For HTTP 
         * errors, will be "http." + the parameter name.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getLocation() {
            return location;
        }

        /**
         * A [simple subset of FHIRPath](fhirpath.html#simple) limited to element names, repetition indicators and the default 
         * child accessor that identifies one of the elements in the resource that caused this issue to be raised.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getExpression() {
            return expression;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (severity != null) || 
                (code != null) || 
                (details != null) || 
                (diagnostics != null) || 
                !location.isEmpty() || 
                !expression.isEmpty();
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
                    accept(severity, "severity", visitor);
                    accept(code, "code", visitor);
                    accept(details, "details", visitor);
                    accept(diagnostics, "diagnostics", visitor);
                    accept(location, "location", visitor, String.class);
                    accept(expression, "expression", visitor, String.class);
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
            Issue other = (Issue) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(severity, other.severity) && 
                Objects.equals(code, other.code) && 
                Objects.equals(details, other.details) && 
                Objects.equals(diagnostics, other.diagnostics) && 
                Objects.equals(location, other.location) && 
                Objects.equals(expression, other.expression);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    severity, 
                    code, 
                    details, 
                    diagnostics, 
                    location, 
                    expression);
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
            private IssueSeverity severity;
            private IssueType code;
            private CodeableConcept details;
            private String diagnostics;
            private List<String> location = new ArrayList<>();
            private List<String> expression = new ArrayList<>();

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
             * Indicates whether the issue indicates a variation from successful processing.
             * 
             * <p>This element is required.
             * 
             * @param severity
             *     fatal | error | warning | information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder severity(IssueSeverity severity) {
                this.severity = severity;
                return this;
            }

            /**
             * Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code 
             * from the IssueType value set, and may additional provide its own code for the error in the details element.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Error or warning code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(IssueType code) {
                this.code = code;
                return this;
            }

            /**
             * Additional details about the error. This may be a text description of the error or a system code that identifies the 
             * error.
             * 
             * @param details
             *     Additional details about the error
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder details(CodeableConcept details) {
                this.details = details;
                return this;
            }

            /**
             * Convenience method for setting {@code diagnostics}.
             * 
             * @param diagnostics
             *     Additional diagnostic information about the issue
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #diagnostics(com.ibm.fhir.model.type.String)
             */
            public Builder diagnostics(java.lang.String diagnostics) {
                this.diagnostics = (diagnostics == null) ? null : String.of(diagnostics);
                return this;
            }

            /**
             * Additional diagnostic information about the issue.
             * 
             * @param diagnostics
             *     Additional diagnostic information about the issue
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diagnostics(String diagnostics) {
                this.diagnostics = diagnostics;
                return this;
            }

            /**
             * Convenience method for setting {@code location}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param location
             *     Deprecated: Path of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #location(com.ibm.fhir.model.type.String)
             */
            public Builder location(java.lang.String... location) {
                for (java.lang.String value : location) {
                    this.location.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * This element is deprecated because it is XML specific. It is replaced by issue.expression, which is format 
             * independent, and simpler to parse. 
             * 
             * <p>For resource issues, this will be a simple XPath limited to element names, repetition indicators and the default 
             * child accessor that identifies one of the elements in the resource that caused this issue to be raised. For HTTP 
             * errors, will be "http." + the parameter name.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param location
             *     Deprecated: Path of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder location(String... location) {
                for (String value : location) {
                    this.location.add(value);
                }
                return this;
            }

            /**
             * This element is deprecated because it is XML specific. It is replaced by issue.expression, which is format 
             * independent, and simpler to parse. 
             * 
             * <p>For resource issues, this will be a simple XPath limited to element names, repetition indicators and the default 
             * child accessor that identifies one of the elements in the resource that caused this issue to be raised. For HTTP 
             * errors, will be "http." + the parameter name.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param location
             *     Deprecated: Path of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder location(Collection<String> location) {
                this.location = new ArrayList<>(location);
                return this;
            }

            /**
             * Convenience method for setting {@code expression}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param expression
             *     FHIRPath of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #expression(com.ibm.fhir.model.type.String)
             */
            public Builder expression(java.lang.String... expression) {
                for (java.lang.String value : expression) {
                    this.expression.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * A [simple subset of FHIRPath](fhirpath.html#simple) limited to element names, repetition indicators and the default 
             * child accessor that identifies one of the elements in the resource that caused this issue to be raised.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param expression
             *     FHIRPath of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder expression(String... expression) {
                for (String value : expression) {
                    this.expression.add(value);
                }
                return this;
            }

            /**
             * A [simple subset of FHIRPath](fhirpath.html#simple) limited to element names, repetition indicators and the default 
             * child accessor that identifies one of the elements in the resource that caused this issue to be raised.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param expression
             *     FHIRPath of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder expression(Collection<String> expression) {
                this.expression = new ArrayList<>(expression);
                return this;
            }

            /**
             * Build the {@link Issue}
             * 
             * <p>Required elements:
             * <ul>
             * <li>severity</li>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Issue}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Issue per the base specification
             */
            @Override
            public Issue build() {
                Issue issue = new Issue(this);
                if (validating) {
                    validate(issue);
                }
                return issue;
            }

            protected void validate(Issue issue) {
                super.validate(issue);
                ValidationSupport.requireNonNull(issue.severity, "severity");
                ValidationSupport.requireNonNull(issue.code, "code");
                ValidationSupport.checkList(issue.location, "location", String.class);
                ValidationSupport.checkList(issue.expression, "expression", String.class);
                ValidationSupport.requireValueOrChildren(issue);
            }

            protected Builder from(Issue issue) {
                super.from(issue);
                severity = issue.severity;
                code = issue.code;
                details = issue.details;
                diagnostics = issue.diagnostics;
                location.addAll(issue.location);
                expression.addAll(issue.expression);
                return this;
            }
        }
    }
}
