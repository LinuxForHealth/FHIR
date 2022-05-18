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
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.TestReportActionResult;
import com.ibm.fhir.model.type.code.TestReportParticipantType;
import com.ibm.fhir.model.type.code.TestReportResult;
import com.ibm.fhir.model.type.code.TestReportStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A summary of information based on the results of executing a TestScript.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "inv-1",
    level = "Rule",
    location = "TestReport.setup.action",
    description = "Setup action SHALL contain either an operation or assert but not both.",
    expression = "operation.exists() xor assert.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/TestReport"
)
@Constraint(
    id = "inv-2",
    level = "Rule",
    location = "TestReport.test.action",
    description = "Test action SHALL contain either an operation or assert but not both.",
    expression = "operation.exists() xor assert.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/TestReport"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class TestReport extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    private final String name;
    @Summary
    @Binding(
        bindingName = "TestReportStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The current status of the test script execution.",
        valueSet = "http://hl7.org/fhir/ValueSet/report-status-codes|4.3.0-cibuild"
    )
    @Required
    private final TestReportStatus status;
    @Summary
    @ReferenceTarget({ "TestScript" })
    @Required
    private final Reference testScript;
    @Summary
    @Binding(
        bindingName = "TestReportResult",
        strength = BindingStrength.Value.REQUIRED,
        description = "The overall execution result of the TestScript.",
        valueSet = "http://hl7.org/fhir/ValueSet/report-result-codes|4.3.0-cibuild"
    )
    @Required
    private final TestReportResult result;
    @Summary
    private final Decimal score;
    @Summary
    private final String tester;
    @Summary
    private final DateTime issued;
    private final List<Participant> participant;
    private final Setup setup;
    private final List<Test> test;
    private final Teardown teardown;

    private TestReport(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        name = builder.name;
        status = builder.status;
        testScript = builder.testScript;
        result = builder.result;
        score = builder.score;
        tester = builder.tester;
        issued = builder.issued;
        participant = Collections.unmodifiableList(builder.participant);
        setup = builder.setup;
        test = Collections.unmodifiableList(builder.test);
        teardown = builder.teardown;
    }

    /**
     * Identifier for the TestScript assigned for external purposes outside the context of FHIR.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * A free text natural language name identifying the executed TestScript.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * The current state of this test report.
     * 
     * @return
     *     An immutable object of type {@link TestReportStatus} that is non-null.
     */
    public TestReportStatus getStatus() {
        return status;
    }

    /**
     * Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching 
     * the `TestScript.url`.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getTestScript() {
        return testScript;
    }

    /**
     * The overall result from the execution of the TestScript.
     * 
     * @return
     *     An immutable object of type {@link TestReportResult} that is non-null.
     */
    public TestReportResult getResult() {
        return result;
    }

    /**
     * The final score (percentage of tests passed) resulting from the execution of the TestScript.
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getScore() {
        return score;
    }

    /**
     * Name of the tester producing this report (Organization or individual).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTester() {
        return tester;
    }

    /**
     * When the TestScript was executed and this TestReport was generated.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getIssued() {
        return issued;
    }

    /**
     * A participant in the test execution, either the execution engine, a client, or a server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Participant} that may be empty.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * The results of the series of required setup operations before the tests were executed.
     * 
     * @return
     *     An immutable object of type {@link Setup} that may be null.
     */
    public Setup getSetup() {
        return setup;
    }

    /**
     * A test executed from the test script.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Test} that may be empty.
     */
    public List<Test> getTest() {
        return test;
    }

    /**
     * The results of the series of operations required to clean up after all the tests were executed (successfully or 
     * otherwise).
     * 
     * @return
     *     An immutable object of type {@link Teardown} that may be null.
     */
    public Teardown getTeardown() {
        return teardown;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (name != null) || 
            (status != null) || 
            (testScript != null) || 
            (result != null) || 
            (score != null) || 
            (tester != null) || 
            (issued != null) || 
            !participant.isEmpty() || 
            (setup != null) || 
            !test.isEmpty() || 
            (teardown != null);
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
                accept(identifier, "identifier", visitor);
                accept(name, "name", visitor);
                accept(status, "status", visitor);
                accept(testScript, "testScript", visitor);
                accept(result, "result", visitor);
                accept(score, "score", visitor);
                accept(tester, "tester", visitor);
                accept(issued, "issued", visitor);
                accept(participant, "participant", visitor, Participant.class);
                accept(setup, "setup", visitor);
                accept(test, "test", visitor, Test.class);
                accept(teardown, "teardown", visitor);
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
        TestReport other = (TestReport) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(name, other.name) && 
            Objects.equals(status, other.status) && 
            Objects.equals(testScript, other.testScript) && 
            Objects.equals(result, other.result) && 
            Objects.equals(score, other.score) && 
            Objects.equals(tester, other.tester) && 
            Objects.equals(issued, other.issued) && 
            Objects.equals(participant, other.participant) && 
            Objects.equals(setup, other.setup) && 
            Objects.equals(test, other.test) && 
            Objects.equals(teardown, other.teardown);
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
                name, 
                status, 
                testScript, 
                this.result, 
                score, 
                tester, 
                issued, 
                participant, 
                setup, 
                test, 
                teardown);
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
        private Identifier identifier;
        private String name;
        private TestReportStatus status;
        private Reference testScript;
        private TestReportResult result;
        private Decimal score;
        private String tester;
        private DateTime issued;
        private List<Participant> participant = new ArrayList<>();
        private Setup setup;
        private List<Test> test = new ArrayList<>();
        private Teardown teardown;

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
         * Identifier for the TestScript assigned for external purposes outside the context of FHIR.
         * 
         * @param identifier
         *     External identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Informal name of the executed TestScript
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(com.ibm.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * A free text natural language name identifying the executed TestScript.
         * 
         * @param name
         *     Informal name of the executed TestScript
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The current state of this test report.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     completed | in-progress | waiting | stopped | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(TestReportStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching 
         * the `TestScript.url`.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link TestScript}</li>
         * </ul>
         * 
         * @param testScript
         *     Reference to the version-specific TestScript that was executed to produce this TestReport
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder testScript(Reference testScript) {
            this.testScript = testScript;
            return this;
        }

        /**
         * The overall result from the execution of the TestScript.
         * 
         * <p>This element is required.
         * 
         * @param result
         *     pass | fail | pending
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder result(TestReportResult result) {
            this.result = result;
            return this;
        }

        /**
         * The final score (percentage of tests passed) resulting from the execution of the TestScript.
         * 
         * @param score
         *     The final score (percentage of tests passed) resulting from the execution of the TestScript
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder score(Decimal score) {
            this.score = score;
            return this;
        }

        /**
         * Convenience method for setting {@code tester}.
         * 
         * @param tester
         *     Name of the tester producing this report (Organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #tester(com.ibm.fhir.model.type.String)
         */
        public Builder tester(java.lang.String tester) {
            this.tester = (tester == null) ? null : String.of(tester);
            return this;
        }

        /**
         * Name of the tester producing this report (Organization or individual).
         * 
         * @param tester
         *     Name of the tester producing this report (Organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder tester(String tester) {
            this.tester = tester;
            return this;
        }

        /**
         * When the TestScript was executed and this TestReport was generated.
         * 
         * @param issued
         *     When the TestScript was executed and this TestReport was generated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder issued(DateTime issued) {
            this.issued = issued;
            return this;
        }

        /**
         * A participant in the test execution, either the execution engine, a client, or a server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param participant
         *     A participant in the test execution, either the execution engine, a client, or a server
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participant(Participant... participant) {
            for (Participant value : participant) {
                this.participant.add(value);
            }
            return this;
        }

        /**
         * A participant in the test execution, either the execution engine, a client, or a server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param participant
         *     A participant in the test execution, either the execution engine, a client, or a server
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant = new ArrayList<>(participant);
            return this;
        }

        /**
         * The results of the series of required setup operations before the tests were executed.
         * 
         * @param setup
         *     The results of the series of required setup operations before the tests were executed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder setup(Setup setup) {
            this.setup = setup;
            return this;
        }

        /**
         * A test executed from the test script.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param test
         *     A test executed from the test script
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder test(Test... test) {
            for (Test value : test) {
                this.test.add(value);
            }
            return this;
        }

        /**
         * A test executed from the test script.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param test
         *     A test executed from the test script
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder test(Collection<Test> test) {
            this.test = new ArrayList<>(test);
            return this;
        }

        /**
         * The results of the series of operations required to clean up after all the tests were executed (successfully or 
         * otherwise).
         * 
         * @param teardown
         *     The results of running the series of required clean up steps
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder teardown(Teardown teardown) {
            this.teardown = teardown;
            return this;
        }

        /**
         * Build the {@link TestReport}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>testScript</li>
         * <li>result</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link TestReport}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid TestReport per the base specification
         */
        @Override
        public TestReport build() {
            TestReport testReport = new TestReport(this);
            if (validating) {
                validate(testReport);
            }
            return testReport;
        }

        protected void validate(TestReport testReport) {
            super.validate(testReport);
            ValidationSupport.requireNonNull(testReport.status, "status");
            ValidationSupport.requireNonNull(testReport.testScript, "testScript");
            ValidationSupport.requireNonNull(testReport.result, "result");
            ValidationSupport.checkList(testReport.participant, "participant", Participant.class);
            ValidationSupport.checkList(testReport.test, "test", Test.class);
            ValidationSupport.checkReferenceType(testReport.testScript, "testScript", "TestScript");
        }

        protected Builder from(TestReport testReport) {
            super.from(testReport);
            identifier = testReport.identifier;
            name = testReport.name;
            status = testReport.status;
            testScript = testReport.testScript;
            result = testReport.result;
            score = testReport.score;
            tester = testReport.tester;
            issued = testReport.issued;
            participant.addAll(testReport.participant);
            setup = testReport.setup;
            test.addAll(testReport.test);
            teardown = testReport.teardown;
            return this;
        }
    }

    /**
     * A participant in the test execution, either the execution engine, a client, or a server.
     */
    public static class Participant extends BackboneElement {
        @Binding(
            bindingName = "TestReportParticipantType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of participant.",
            valueSet = "http://hl7.org/fhir/ValueSet/report-participant-type|4.3.0-cibuild"
        )
        @Required
        private final TestReportParticipantType type;
        @Required
        private final Uri uri;
        private final String display;

        private Participant(Builder builder) {
            super(builder);
            type = builder.type;
            uri = builder.uri;
            display = builder.display;
        }

        /**
         * The type of participant.
         * 
         * @return
         *     An immutable object of type {@link TestReportParticipantType} that is non-null.
         */
        public TestReportParticipantType getType() {
            return type;
        }

        /**
         * The uri of the participant. An absolute URL is preferred.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getUri() {
            return uri;
        }

        /**
         * The display name of the participant.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDisplay() {
            return display;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (uri != null) || 
                (display != null);
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
                    accept(uri, "uri", visitor);
                    accept(display, "display", visitor);
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
            Participant other = (Participant) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(uri, other.uri) && 
                Objects.equals(display, other.display);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    uri, 
                    display);
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
            private TestReportParticipantType type;
            private Uri uri;
            private String display;

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
             * The type of participant.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     test-engine | client | server
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(TestReportParticipantType type) {
                this.type = type;
                return this;
            }

            /**
             * The uri of the participant. An absolute URL is preferred.
             * 
             * <p>This element is required.
             * 
             * @param uri
             *     The uri of the participant. An absolute URL is preferred
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder uri(Uri uri) {
                this.uri = uri;
                return this;
            }

            /**
             * Convenience method for setting {@code display}.
             * 
             * @param display
             *     The display name of the participant
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #display(com.ibm.fhir.model.type.String)
             */
            public Builder display(java.lang.String display) {
                this.display = (display == null) ? null : String.of(display);
                return this;
            }

            /**
             * The display name of the participant.
             * 
             * @param display
             *     The display name of the participant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder display(String display) {
                this.display = display;
                return this;
            }

            /**
             * Build the {@link Participant}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>uri</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Participant}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Participant per the base specification
             */
            @Override
            public Participant build() {
                Participant participant = new Participant(this);
                if (validating) {
                    validate(participant);
                }
                return participant;
            }

            protected void validate(Participant participant) {
                super.validate(participant);
                ValidationSupport.requireNonNull(participant.type, "type");
                ValidationSupport.requireNonNull(participant.uri, "uri");
                ValidationSupport.requireValueOrChildren(participant);
            }

            protected Builder from(Participant participant) {
                super.from(participant);
                type = participant.type;
                uri = participant.uri;
                display = participant.display;
                return this;
            }
        }
    }

    /**
     * The results of the series of required setup operations before the tests were executed.
     */
    public static class Setup extends BackboneElement {
        @Required
        private final List<Action> action;

        private Setup(Builder builder) {
            super(builder);
            action = Collections.unmodifiableList(builder.action);
        }

        /**
         * Action would contain either an operation or an assertion.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action} that is non-empty.
         */
        public List<Action> getAction() {
            return action;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !action.isEmpty();
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
                    accept(action, "action", visitor, Action.class);
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
            Setup other = (Setup) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(action, other.action);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    action);
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
            private List<Action> action = new ArrayList<>();

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
             * Action would contain either an operation or an assertion.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param action
             *     A setup operation or assert that was executed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Action... action) {
                for (Action value : action) {
                    this.action.add(value);
                }
                return this;
            }

            /**
             * Action would contain either an operation or an assertion.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param action
             *     A setup operation or assert that was executed
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder action(Collection<Action> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * Build the {@link Setup}
             * 
             * <p>Required elements:
             * <ul>
             * <li>action</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Setup}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Setup per the base specification
             */
            @Override
            public Setup build() {
                Setup setup = new Setup(this);
                if (validating) {
                    validate(setup);
                }
                return setup;
            }

            protected void validate(Setup setup) {
                super.validate(setup);
                ValidationSupport.checkNonEmptyList(setup.action, "action", Action.class);
                ValidationSupport.requireValueOrChildren(setup);
            }

            protected Builder from(Setup setup) {
                super.from(setup);
                action.addAll(setup.action);
                return this;
            }
        }

        /**
         * Action would contain either an operation or an assertion.
         */
        public static class Action extends BackboneElement {
            private final Operation operation;
            private final Assert _assert;

            private Action(Builder builder) {
                super(builder);
                operation = builder.operation;
                _assert = builder._assert;
            }

            /**
             * The operation performed.
             * 
             * @return
             *     An immutable object of type {@link Operation} that may be null.
             */
            public Operation getOperation() {
                return operation;
            }

            /**
             * The results of the assertion performed on the previous operations.
             * 
             * @return
             *     An immutable object of type {@link Assert} that may be null.
             */
            public Assert getAssert() {
                return _assert;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (operation != null) || 
                    (_assert != null);
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
                        accept(operation, "operation", visitor);
                        accept(_assert, "assert", visitor);
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
                Action other = (Action) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(operation, other.operation) && 
                    Objects.equals(_assert, other._assert);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        operation, 
                        _assert);
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
                private Operation operation;
                private Assert _assert;

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
                 * The operation performed.
                 * 
                 * @param operation
                 *     The operation to perform
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(Operation operation) {
                    this.operation = operation;
                    return this;
                }

                /**
                 * The results of the assertion performed on the previous operations.
                 * 
                 * @param _assert
                 *     The assertion to perform
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder _assert(Assert _assert) {
                    this._assert = _assert;
                    return this;
                }

                /**
                 * Build the {@link Action}
                 * 
                 * @return
                 *     An immutable object of type {@link Action}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Action per the base specification
                 */
                @Override
                public Action build() {
                    Action action = new Action(this);
                    if (validating) {
                        validate(action);
                    }
                    return action;
                }

                protected void validate(Action action) {
                    super.validate(action);
                    ValidationSupport.requireValueOrChildren(action);
                }

                protected Builder from(Action action) {
                    super.from(action);
                    operation = action.operation;
                    _assert = action._assert;
                    return this;
                }
            }

            /**
             * The operation performed.
             */
            public static class Operation extends BackboneElement {
                @Binding(
                    bindingName = "TestReportActionResult",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The result of the execution of an individual action.",
                    valueSet = "http://hl7.org/fhir/ValueSet/report-action-result-codes|4.3.0-cibuild"
                )
                @Required
                private final TestReportActionResult result;
                private final Markdown message;
                private final Uri detail;

                private Operation(Builder builder) {
                    super(builder);
                    result = builder.result;
                    message = builder.message;
                    detail = builder.detail;
                }

                /**
                 * The result of this operation.
                 * 
                 * @return
                 *     An immutable object of type {@link TestReportActionResult} that is non-null.
                 */
                public TestReportActionResult getResult() {
                    return result;
                }

                /**
                 * An explanatory message associated with the result.
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown} that may be null.
                 */
                public Markdown getMessage() {
                    return message;
                }

                /**
                 * A link to further details on the result.
                 * 
                 * @return
                 *     An immutable object of type {@link Uri} that may be null.
                 */
                public Uri getDetail() {
                    return detail;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (result != null) || 
                        (message != null) || 
                        (detail != null);
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
                            accept(result, "result", visitor);
                            accept(message, "message", visitor);
                            accept(detail, "detail", visitor);
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
                    Operation other = (Operation) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(result, other.result) && 
                        Objects.equals(message, other.message) && 
                        Objects.equals(detail, other.detail);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            this.result, 
                            message, 
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
                    private TestReportActionResult result;
                    private Markdown message;
                    private Uri detail;

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
                     * The result of this operation.
                     * 
                     * <p>This element is required.
                     * 
                     * @param result
                     *     pass | skip | fail | warning | error
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder result(TestReportActionResult result) {
                        this.result = result;
                        return this;
                    }

                    /**
                     * An explanatory message associated with the result.
                     * 
                     * @param message
                     *     A message associated with the result
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder message(Markdown message) {
                        this.message = message;
                        return this;
                    }

                    /**
                     * A link to further details on the result.
                     * 
                     * @param detail
                     *     A link to further details on the result
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder detail(Uri detail) {
                        this.detail = detail;
                        return this;
                    }

                    /**
                     * Build the {@link Operation}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>result</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Operation}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Operation per the base specification
                     */
                    @Override
                    public Operation build() {
                        Operation operation = new Operation(this);
                        if (validating) {
                            validate(operation);
                        }
                        return operation;
                    }

                    protected void validate(Operation operation) {
                        super.validate(operation);
                        ValidationSupport.requireNonNull(operation.result, "result");
                        ValidationSupport.requireValueOrChildren(operation);
                    }

                    protected Builder from(Operation operation) {
                        super.from(operation);
                        result = operation.result;
                        message = operation.message;
                        detail = operation.detail;
                        return this;
                    }
                }
            }

            /**
             * The results of the assertion performed on the previous operations.
             */
            public static class Assert extends BackboneElement {
                @Binding(
                    bindingName = "TestReportActionResult",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The result of the execution of an individual action.",
                    valueSet = "http://hl7.org/fhir/ValueSet/report-action-result-codes|4.3.0-cibuild"
                )
                @Required
                private final TestReportActionResult result;
                private final Markdown message;
                private final String detail;

                private Assert(Builder builder) {
                    super(builder);
                    result = builder.result;
                    message = builder.message;
                    detail = builder.detail;
                }

                /**
                 * The result of this assertion.
                 * 
                 * @return
                 *     An immutable object of type {@link TestReportActionResult} that is non-null.
                 */
                public TestReportActionResult getResult() {
                    return result;
                }

                /**
                 * An explanatory message associated with the result.
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown} that may be null.
                 */
                public Markdown getMessage() {
                    return message;
                }

                /**
                 * A link to further details on the result.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getDetail() {
                    return detail;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (result != null) || 
                        (message != null) || 
                        (detail != null);
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
                            accept(result, "result", visitor);
                            accept(message, "message", visitor);
                            accept(detail, "detail", visitor);
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
                    Assert other = (Assert) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(result, other.result) && 
                        Objects.equals(message, other.message) && 
                        Objects.equals(detail, other.detail);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            this.result, 
                            message, 
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
                    private TestReportActionResult result;
                    private Markdown message;
                    private String detail;

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
                     * The result of this assertion.
                     * 
                     * <p>This element is required.
                     * 
                     * @param result
                     *     pass | skip | fail | warning | error
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder result(TestReportActionResult result) {
                        this.result = result;
                        return this;
                    }

                    /**
                     * An explanatory message associated with the result.
                     * 
                     * @param message
                     *     A message associated with the result
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder message(Markdown message) {
                        this.message = message;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code detail}.
                     * 
                     * @param detail
                     *     A link to further details on the result
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
                     * A link to further details on the result.
                     * 
                     * @param detail
                     *     A link to further details on the result
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder detail(String detail) {
                        this.detail = detail;
                        return this;
                    }

                    /**
                     * Build the {@link Assert}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>result</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Assert}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Assert per the base specification
                     */
                    @Override
                    public Assert build() {
                        Assert _assert = new Assert(this);
                        if (validating) {
                            validate(_assert);
                        }
                        return _assert;
                    }

                    protected void validate(Assert _assert) {
                        super.validate(_assert);
                        ValidationSupport.requireNonNull(_assert.result, "result");
                        ValidationSupport.requireValueOrChildren(_assert);
                    }

                    protected Builder from(Assert _assert) {
                        super.from(_assert);
                        result = _assert.result;
                        message = _assert.message;
                        detail = _assert.detail;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * A test executed from the test script.
     */
    public static class Test extends BackboneElement {
        private final String name;
        private final String description;
        @Required
        private final List<Action> action;

        private Test(Builder builder) {
            super(builder);
            name = builder.name;
            description = builder.description;
            action = Collections.unmodifiableList(builder.action);
        }

        /**
         * The name of this test used for tracking/logging purposes by test engines.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * A short description of the test used by test engines for tracking and reporting purposes.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Action would contain either an operation or an assertion.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action} that is non-empty.
         */
        public List<Action> getAction() {
            return action;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (description != null) || 
                !action.isEmpty();
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
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
                    accept(action, "action", visitor, Action.class);
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
            Test other = (Test) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(description, other.description) && 
                Objects.equals(action, other.action);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    description, 
                    action);
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
            private String name;
            private String description;
            private List<Action> action = new ArrayList<>();

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
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Tracking/logging name of this test
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #name(com.ibm.fhir.model.type.String)
             */
            public Builder name(java.lang.String name) {
                this.name = (name == null) ? null : String.of(name);
                return this;
            }

            /**
             * The name of this test used for tracking/logging purposes by test engines.
             * 
             * @param name
             *     Tracking/logging name of this test
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Tracking/reporting short description of the test
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #description(com.ibm.fhir.model.type.String)
             */
            public Builder description(java.lang.String description) {
                this.description = (description == null) ? null : String.of(description);
                return this;
            }

            /**
             * A short description of the test used by test engines for tracking and reporting purposes.
             * 
             * @param description
             *     Tracking/reporting short description of the test
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Action would contain either an operation or an assertion.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param action
             *     A test operation or assert that was performed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Action... action) {
                for (Action value : action) {
                    this.action.add(value);
                }
                return this;
            }

            /**
             * Action would contain either an operation or an assertion.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param action
             *     A test operation or assert that was performed
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder action(Collection<Action> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * Build the {@link Test}
             * 
             * <p>Required elements:
             * <ul>
             * <li>action</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Test}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Test per the base specification
             */
            @Override
            public Test build() {
                Test test = new Test(this);
                if (validating) {
                    validate(test);
                }
                return test;
            }

            protected void validate(Test test) {
                super.validate(test);
                ValidationSupport.checkNonEmptyList(test.action, "action", Action.class);
                ValidationSupport.requireValueOrChildren(test);
            }

            protected Builder from(Test test) {
                super.from(test);
                name = test.name;
                description = test.description;
                action.addAll(test.action);
                return this;
            }
        }

        /**
         * Action would contain either an operation or an assertion.
         */
        public static class Action extends BackboneElement {
            private final TestReport.Setup.Action.Operation operation;
            private final TestReport.Setup.Action.Assert _assert;

            private Action(Builder builder) {
                super(builder);
                operation = builder.operation;
                _assert = builder._assert;
            }

            /**
             * An operation would involve a REST request to a server.
             * 
             * @return
             *     An immutable object of type {@link TestReport.Setup.Action.Operation} that may be null.
             */
            public TestReport.Setup.Action.Operation getOperation() {
                return operation;
            }

            /**
             * The results of the assertion performed on the previous operations.
             * 
             * @return
             *     An immutable object of type {@link TestReport.Setup.Action.Assert} that may be null.
             */
            public TestReport.Setup.Action.Assert getAssert() {
                return _assert;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (operation != null) || 
                    (_assert != null);
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
                        accept(operation, "operation", visitor);
                        accept(_assert, "assert", visitor);
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
                Action other = (Action) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(operation, other.operation) && 
                    Objects.equals(_assert, other._assert);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        operation, 
                        _assert);
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
                private TestReport.Setup.Action.Operation operation;
                private TestReport.Setup.Action.Assert _assert;

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
                 * An operation would involve a REST request to a server.
                 * 
                 * @param operation
                 *     The operation performed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(TestReport.Setup.Action.Operation operation) {
                    this.operation = operation;
                    return this;
                }

                /**
                 * The results of the assertion performed on the previous operations.
                 * 
                 * @param _assert
                 *     The assertion performed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder _assert(TestReport.Setup.Action.Assert _assert) {
                    this._assert = _assert;
                    return this;
                }

                /**
                 * Build the {@link Action}
                 * 
                 * @return
                 *     An immutable object of type {@link Action}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Action per the base specification
                 */
                @Override
                public Action build() {
                    Action action = new Action(this);
                    if (validating) {
                        validate(action);
                    }
                    return action;
                }

                protected void validate(Action action) {
                    super.validate(action);
                    ValidationSupport.requireValueOrChildren(action);
                }

                protected Builder from(Action action) {
                    super.from(action);
                    operation = action.operation;
                    _assert = action._assert;
                    return this;
                }
            }
        }
    }

    /**
     * The results of the series of operations required to clean up after all the tests were executed (successfully or 
     * otherwise).
     */
    public static class Teardown extends BackboneElement {
        @Required
        private final List<Action> action;

        private Teardown(Builder builder) {
            super(builder);
            action = Collections.unmodifiableList(builder.action);
        }

        /**
         * The teardown action will only contain an operation.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action} that is non-empty.
         */
        public List<Action> getAction() {
            return action;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !action.isEmpty();
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
                    accept(action, "action", visitor, Action.class);
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
            Teardown other = (Teardown) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(action, other.action);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    action);
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
            private List<Action> action = new ArrayList<>();

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
             * The teardown action will only contain an operation.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param action
             *     One or more teardown operations performed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(Action... action) {
                for (Action value : action) {
                    this.action.add(value);
                }
                return this;
            }

            /**
             * The teardown action will only contain an operation.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param action
             *     One or more teardown operations performed
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder action(Collection<Action> action) {
                this.action = new ArrayList<>(action);
                return this;
            }

            /**
             * Build the {@link Teardown}
             * 
             * <p>Required elements:
             * <ul>
             * <li>action</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Teardown}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Teardown per the base specification
             */
            @Override
            public Teardown build() {
                Teardown teardown = new Teardown(this);
                if (validating) {
                    validate(teardown);
                }
                return teardown;
            }

            protected void validate(Teardown teardown) {
                super.validate(teardown);
                ValidationSupport.checkNonEmptyList(teardown.action, "action", Action.class);
                ValidationSupport.requireValueOrChildren(teardown);
            }

            protected Builder from(Teardown teardown) {
                super.from(teardown);
                action.addAll(teardown.action);
                return this;
            }
        }

        /**
         * The teardown action will only contain an operation.
         */
        public static class Action extends BackboneElement {
            @Required
            private final TestReport.Setup.Action.Operation operation;

            private Action(Builder builder) {
                super(builder);
                operation = builder.operation;
            }

            /**
             * An operation would involve a REST request to a server.
             * 
             * @return
             *     An immutable object of type {@link TestReport.Setup.Action.Operation} that is non-null.
             */
            public TestReport.Setup.Action.Operation getOperation() {
                return operation;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (operation != null);
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
                        accept(operation, "operation", visitor);
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
                Action other = (Action) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(operation, other.operation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        operation);
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
                private TestReport.Setup.Action.Operation operation;

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
                 * An operation would involve a REST request to a server.
                 * 
                 * <p>This element is required.
                 * 
                 * @param operation
                 *     The teardown operation performed
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(TestReport.Setup.Action.Operation operation) {
                    this.operation = operation;
                    return this;
                }

                /**
                 * Build the {@link Action}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>operation</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Action}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Action per the base specification
                 */
                @Override
                public Action build() {
                    Action action = new Action(this);
                    if (validating) {
                        validate(action);
                    }
                    return action;
                }

                protected void validate(Action action) {
                    super.validate(action);
                    ValidationSupport.requireNonNull(action.operation, "operation");
                    ValidationSupport.requireValueOrChildren(action);
                }

                protected Builder from(Action action) {
                    super.from(action);
                    operation = action.operation;
                    return this;
                }
            }
        }
    }
}
