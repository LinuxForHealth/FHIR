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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A collection of error, warning, or information messages that result from a system action.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class OperationOutcome extends DomainResource {
    private final List<Issue> issue;

    private OperationOutcome(Builder builder) {
        super(builder);
        issue = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.issue, "issue"));
    }

    /**
     * <p>
     * An error, warning, or information message that results from a system action.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Issue}.
     */
    public List<Issue> getIssue() {
        return issue;
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
                accept(issue, "issue", visitor, Issue.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(issue);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        return builder;
    }

    public static Builder builder(List<Issue> issue) {
        return new Builder(issue);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final List<Issue> issue;

        private Builder(List<Issue> issue) {
            super();
            this.issue = issue;
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
         * An error, warning, or information message that results from a system action.
         * </p>
         * 
         * @param issue
         *     A single issue associated with the action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder issue(Issue... issue) {
            for (Issue value : issue) {
                this.issue.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An error, warning, or information message that results from a system action.
         * </p>
         * 
         * @param issue
         *     A single issue associated with the action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder issue(Collection<Issue> issue) {
            this.issue.addAll(issue);
            return this;
        }

        @Override
        public OperationOutcome build() {
            return new OperationOutcome(this);
        }
    }

    /**
     * <p>
     * An error, warning, or information message that results from a system action.
     * </p>
     */
    public static class Issue extends BackboneElement {
        private final IssueSeverity severity;
        private final IssueType code;
        private final CodeableConcept details;
        private final String diagnostics;
        private final List<String> location;
        private final List<String> expression;

        private Issue(Builder builder) {
            super(builder);
            severity = ValidationSupport.requireNonNull(builder.severity, "severity");
            code = ValidationSupport.requireNonNull(builder.code, "code");
            details = builder.details;
            diagnostics = builder.diagnostics;
            location = Collections.unmodifiableList(builder.location);
            expression = Collections.unmodifiableList(builder.expression);
        }

        /**
         * <p>
         * Indicates whether the issue indicates a variation from successful processing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link IssueSeverity}.
         */
        public IssueSeverity getSeverity() {
            return severity;
        }

        /**
         * <p>
         * Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code 
         * from the IssueType value set, and may additional provide its own code for the error in the details element.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link IssueType}.
         */
        public IssueType getCode() {
            return code;
        }

        /**
         * <p>
         * Additional details about the error. This may be a text description of the error or a system code that identifies the 
         * error.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getDetails() {
            return details;
        }

        /**
         * <p>
         * Additional diagnostic information about the issue.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDiagnostics() {
            return diagnostics;
        }

        /**
         * <p>
         * This element is deprecated because it is XML specific. It is replaced by issue.expression, which is format 
         * independent, and simpler to parse. 
         * </p>
         * <p>
         * For resource issues, this will be a simple XPath limited to element names, repetition indicators and the default child 
         * accessor that identifies one of the elements in the resource that caused this issue to be raised. For HTTP errors, 
         * will be "http." + the parameter name.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link String}.
         */
        public List<String> getLocation() {
            return location;
        }

        /**
         * <p>
         * A [simple subset of FHIRPath](fhirpath.html#simple) limited to element names, repetition indicators and the default 
         * child accessor that identifies one of the elements in the resource that caused this issue to be raised.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link String}.
         */
        public List<String> getExpression() {
            return expression;
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
                    accept(severity, "severity", visitor);
                    accept(code, "code", visitor);
                    accept(details, "details", visitor);
                    accept(diagnostics, "diagnostics", visitor);
                    accept(location, "location", visitor, String.class);
                    accept(expression, "expression", visitor, String.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(IssueSeverity severity, IssueType code) {
            return new Builder(severity, code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final IssueSeverity severity;
            private final IssueType code;

            // optional
            private CodeableConcept details;
            private String diagnostics;
            private List<String> location = new ArrayList<>();
            private List<String> expression = new ArrayList<>();

            private Builder(IssueSeverity severity, IssueType code) {
                super();
                this.severity = severity;
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
             * Additional details about the error. This may be a text description of the error or a system code that identifies the 
             * error.
             * </p>
             * 
             * @param details
             *     Additional details about the error
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder details(CodeableConcept details) {
                this.details = details;
                return this;
            }

            /**
             * <p>
             * Additional diagnostic information about the issue.
             * </p>
             * 
             * @param diagnostics
             *     Additional diagnostic information about the issue
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder diagnostics(String diagnostics) {
                this.diagnostics = diagnostics;
                return this;
            }

            /**
             * <p>
             * This element is deprecated because it is XML specific. It is replaced by issue.expression, which is format 
             * independent, and simpler to parse. 
             * </p>
             * <p>
             * For resource issues, this will be a simple XPath limited to element names, repetition indicators and the default child 
             * accessor that identifies one of the elements in the resource that caused this issue to be raised. For HTTP errors, 
             * will be "http." + the parameter name.
             * </p>
             * 
             * @param location
             *     Deprecated: Path of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder location(String... location) {
                for (String value : location) {
                    this.location.add(value);
                }
                return this;
            }

            /**
             * <p>
             * This element is deprecated because it is XML specific. It is replaced by issue.expression, which is format 
             * independent, and simpler to parse. 
             * </p>
             * <p>
             * For resource issues, this will be a simple XPath limited to element names, repetition indicators and the default child 
             * accessor that identifies one of the elements in the resource that caused this issue to be raised. For HTTP errors, 
             * will be "http." + the parameter name.
             * </p>
             * 
             * @param location
             *     Deprecated: Path of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder location(Collection<String> location) {
                this.location.addAll(location);
                return this;
            }

            /**
             * <p>
             * A [simple subset of FHIRPath](fhirpath.html#simple) limited to element names, repetition indicators and the default 
             * child accessor that identifies one of the elements in the resource that caused this issue to be raised.
             * </p>
             * 
             * @param expression
             *     FHIRPath of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder expression(String... expression) {
                for (String value : expression) {
                    this.expression.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A [simple subset of FHIRPath](fhirpath.html#simple) limited to element names, repetition indicators and the default 
             * child accessor that identifies one of the elements in the resource that caused this issue to be raised.
             * </p>
             * 
             * @param expression
             *     FHIRPath of element(s) related to issue
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder expression(Collection<String> expression) {
                this.expression.addAll(expression);
                return this;
            }

            @Override
            public Issue build() {
                return new Issue(this);
            }

            private static Builder from(Issue issue) {
                Builder builder = new Builder(issue.severity, issue.code);
                builder.details = issue.details;
                builder.diagnostics = issue.diagnostics;
                builder.location.addAll(issue.location);
                builder.expression.addAll(issue.expression);
                return builder;
            }
        }
    }
}
