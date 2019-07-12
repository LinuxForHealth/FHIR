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
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.TestReportActionResult;
import com.ibm.watsonhealth.fhir.model.type.TestReportParticipantType;
import com.ibm.watsonhealth.fhir.model.type.TestReportResult;
import com.ibm.watsonhealth.fhir.model.type.TestReportStatus;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A summary of information based on the results of executing a TestScript.
 * </p>
 */
@Constraint(
    id = "inv-1",
    level = "Rule",
    location = "TestReport.setup.action",
    description = "Setup action SHALL contain either an operation or assert but not both.",
    expression = "operation.exists() xor assert.exists()"
)
@Constraint(
    id = "inv-2",
    level = "Rule",
    location = "TestReport.test.action",
    description = "Test action SHALL contain either an operation or assert but not both.",
    expression = "operation.exists() xor assert.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class TestReport extends DomainResource {
    private final Identifier identifier;
    private final String name;
    private final TestReportStatus status;
    private final Reference testScript;
    private final TestReportResult result;
    private final Decimal score;
    private final String tester;
    private final DateTime issued;
    private final List<Participant> participant;
    private final Setup setup;
    private final List<Test> test;
    private final Teardown teardown;

    private volatile int hashCode;

    private TestReport(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        name = builder.name;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        testScript = ValidationSupport.requireNonNull(builder.testScript, "testScript");
        result = ValidationSupport.requireNonNull(builder.result, "result");
        score = builder.score;
        tester = builder.tester;
        issued = builder.issued;
        participant = Collections.unmodifiableList(builder.participant);
        setup = builder.setup;
        test = Collections.unmodifiableList(builder.test);
        teardown = builder.teardown;
    }

    /**
     * <p>
     * Identifier for the TestScript assigned for external purposes outside the context of FHIR.
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
     * A free text natural language name identifying the executed TestScript.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * The current state of this test report.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TestReportStatus}.
     */
    public TestReportStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching 
     * the `TestScript.url`.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getTestScript() {
        return testScript;
    }

    /**
     * <p>
     * The overall result from the execution of the TestScript.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TestReportResult}.
     */
    public TestReportResult getResult() {
        return result;
    }

    /**
     * <p>
     * The final score (percentage of tests passed) resulting from the execution of the TestScript.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Decimal}.
     */
    public Decimal getScore() {
        return score;
    }

    /**
     * <p>
     * Name of the tester producing this report (Organization or individual).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTester() {
        return tester;
    }

    /**
     * <p>
     * When the TestScript was executed and this TestReport was generated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getIssued() {
        return issued;
    }

    /**
     * <p>
     * A participant in the test execution, either the execution engine, a client, or a server.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Participant}.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * <p>
     * The results of the series of required setup operations before the tests were executed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Setup}.
     */
    public Setup getSetup() {
        return setup;
    }

    /**
     * <p>
     * A test executed from the test script.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Test}.
     */
    public List<Test> getTest() {
        return test;
    }

    /**
     * <p>
     * The results of the series of operations required to clean up after all the tests were executed (successfully or 
     * otherwise).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Teardown}.
     */
    public Teardown getTeardown() {
        return teardown;
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
                result, 
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
        return new Builder(status, testScript, result).from(this);
    }

    public Builder toBuilder(TestReportStatus status, Reference testScript, TestReportResult result) {
        return new Builder(status, testScript, result).from(this);
    }

    public static Builder builder(TestReportStatus status, Reference testScript, TestReportResult result) {
        return new Builder(status, testScript, result);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final TestReportStatus status;
        private final Reference testScript;
        private final TestReportResult result;

        // optional
        private Identifier identifier;
        private String name;
        private Decimal score;
        private String tester;
        private DateTime issued;
        private List<Participant> participant = new ArrayList<>();
        private Setup setup;
        private List<Test> test = new ArrayList<>();
        private Teardown teardown;

        private Builder(TestReportStatus status, Reference testScript, TestReportResult result) {
            super();
            this.status = status;
            this.testScript = testScript;
            this.result = result;
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
         * Identifier for the TestScript assigned for external purposes outside the context of FHIR.
         * </p>
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
         * <p>
         * A free text natural language name identifying the executed TestScript.
         * </p>
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
         * <p>
         * The final score (percentage of tests passed) resulting from the execution of the TestScript.
         * </p>
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
         * <p>
         * Name of the tester producing this report (Organization or individual).
         * </p>
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
         * <p>
         * When the TestScript was executed and this TestReport was generated.
         * </p>
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
         * <p>
         * A participant in the test execution, either the execution engine, a client, or a server.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A participant in the test execution, either the execution engine, a client, or a server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param participant
         *     A participant in the test execution, either the execution engine, a client, or a server
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant = new ArrayList<>(participant);
            return this;
        }

        /**
         * <p>
         * The results of the series of required setup operations before the tests were executed.
         * </p>
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
         * <p>
         * A test executed from the test script.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A test executed from the test script.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param test
         *     A test executed from the test script
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder test(Collection<Test> test) {
            this.test = new ArrayList<>(test);
            return this;
        }

        /**
         * <p>
         * The results of the series of operations required to clean up after all the tests were executed (successfully or 
         * otherwise).
         * </p>
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

        @Override
        public TestReport build() {
            return new TestReport(this);
        }

        private Builder from(TestReport testReport) {
            id = testReport.id;
            meta = testReport.meta;
            implicitRules = testReport.implicitRules;
            language = testReport.language;
            text = testReport.text;
            contained.addAll(testReport.contained);
            extension.addAll(testReport.extension);
            modifierExtension.addAll(testReport.modifierExtension);
            identifier = testReport.identifier;
            name = testReport.name;
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
     * <p>
     * A participant in the test execution, either the execution engine, a client, or a server.
     * </p>
     */
    public static class Participant extends BackboneElement {
        private final TestReportParticipantType type;
        private final Uri uri;
        private final String display;

        private volatile int hashCode;

        private Participant(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            uri = ValidationSupport.requireNonNull(builder.uri, "uri");
            display = builder.display;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * The type of participant.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link TestReportParticipantType}.
         */
        public TestReportParticipantType getType() {
            return type;
        }

        /**
         * <p>
         * The uri of the participant. An absolute URL is preferred.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getUri() {
            return uri;
        }

        /**
         * <p>
         * The display name of the participant.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDisplay() {
            return display;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (uri != null) || 
                (display != null);
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
                    accept(uri, "uri", visitor);
                    accept(display, "display", visitor);
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
            return new Builder(type, uri).from(this);
        }

        public Builder toBuilder(TestReportParticipantType type, Uri uri) {
            return new Builder(type, uri).from(this);
        }

        public static Builder builder(TestReportParticipantType type, Uri uri) {
            return new Builder(type, uri);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final TestReportParticipantType type;
            private final Uri uri;

            // optional
            private String display;

            private Builder(TestReportParticipantType type, Uri uri) {
                super();
                this.type = type;
                this.uri = uri;
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
             * The display name of the participant.
             * </p>
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

            @Override
            public Participant build() {
                return new Participant(this);
            }

            private Builder from(Participant participant) {
                id = participant.id;
                extension.addAll(participant.extension);
                modifierExtension.addAll(participant.modifierExtension);
                display = participant.display;
                return this;
            }
        }
    }

    /**
     * <p>
     * The results of the series of required setup operations before the tests were executed.
     * </p>
     */
    public static class Setup extends BackboneElement {
        private final List<Action> action;

        private volatile int hashCode;

        private Setup(Builder builder) {
            super(builder);
            action = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.action, "action"));
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Action would contain either an operation or an assertion.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action}.
         */
        public List<Action> getAction() {
            return action;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                !action.isEmpty();
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
                    accept(action, "action", visitor, Action.class);
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
            return new Builder(action).from(this);
        }

        public Builder toBuilder(List<Action> action) {
            return new Builder(action).from(this);
        }

        public static Builder builder(List<Action> action) {
            return new Builder(action);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<Action> action;

            private Builder(List<Action> action) {
                super();
                this.action = action;
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
            public Setup build() {
                return new Setup(this);
            }

            private Builder from(Setup setup) {
                id = setup.id;
                extension.addAll(setup.extension);
                modifierExtension.addAll(setup.modifierExtension);
                return this;
            }
        }

        /**
         * <p>
         * Action would contain either an operation or an assertion.
         * </p>
         */
        public static class Action extends BackboneElement {
            private final Operation operation;
            private final Assert _assert;

            private volatile int hashCode;

            private Action(Builder builder) {
                super(builder);
                operation = builder.operation;
                _assert = builder._assert;
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * The operation performed.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Operation}.
             */
            public Operation getOperation() {
                return operation;
            }

            /**
             * <p>
             * The results of the assertion performed on the previous operations.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Assert}.
             */
            public Assert getassert() {
                return _assert;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (operation != null) || 
                    (_assert != null);
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
                        accept(operation, "operation", visitor);
                        accept(_assert, "assert", visitor);
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
                // optional
                private Operation operation;
                private Assert _assert;

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
                 * The operation performed.
                 * </p>
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
                 * <p>
                 * The results of the assertion performed on the previous operations.
                 * </p>
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

                @Override
                public Action build() {
                    return new Action(this);
                }

                private Builder from(Action action) {
                    id = action.id;
                    extension.addAll(action.extension);
                    modifierExtension.addAll(action.modifierExtension);
                    operation = action.operation;
                    _assert = action._assert;
                    return this;
                }
            }

            /**
             * <p>
             * The operation performed.
             * </p>
             */
            public static class Operation extends BackboneElement {
                private final TestReportActionResult result;
                private final Markdown message;
                private final Uri detail;

                private volatile int hashCode;

                private Operation(Builder builder) {
                    super(builder);
                    result = ValidationSupport.requireNonNull(builder.result, "result");
                    message = builder.message;
                    detail = builder.detail;
                    if (!hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * The result of this operation.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link TestReportActionResult}.
                 */
                public TestReportActionResult getResult() {
                    return result;
                }

                /**
                 * <p>
                 * An explanatory message associated with the result.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown}.
                 */
                public Markdown getMessage() {
                    return message;
                }

                /**
                 * <p>
                 * A link to further details on the result.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Uri}.
                 */
                public Uri getDetail() {
                    return detail;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        (result != null) || 
                        (message != null) || 
                        (detail != null);
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
                            accept(result, "result", visitor);
                            accept(message, "message", visitor);
                            accept(detail, "detail", visitor);
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
                            result, 
                            message, 
                            detail);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(result).from(this);
                }

                public Builder toBuilder(TestReportActionResult result) {
                    return new Builder(result).from(this);
                }

                public static Builder builder(TestReportActionResult result) {
                    return new Builder(result);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final TestReportActionResult result;

                    // optional
                    private Markdown message;
                    private Uri detail;

                    private Builder(TestReportActionResult result) {
                        super();
                        this.result = result;
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
                     * An explanatory message associated with the result.
                     * </p>
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
                     * <p>
                     * A link to further details on the result.
                     * </p>
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

                    @Override
                    public Operation build() {
                        return new Operation(this);
                    }

                    private Builder from(Operation operation) {
                        id = operation.id;
                        extension.addAll(operation.extension);
                        modifierExtension.addAll(operation.modifierExtension);
                        message = operation.message;
                        detail = operation.detail;
                        return this;
                    }
                }
            }

            /**
             * <p>
             * The results of the assertion performed on the previous operations.
             * </p>
             */
            public static class Assert extends BackboneElement {
                private final TestReportActionResult result;
                private final Markdown message;
                private final String detail;

                private volatile int hashCode;

                private Assert(Builder builder) {
                    super(builder);
                    result = ValidationSupport.requireNonNull(builder.result, "result");
                    message = builder.message;
                    detail = builder.detail;
                    if (!hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * The result of this assertion.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link TestReportActionResult}.
                 */
                public TestReportActionResult getResult() {
                    return result;
                }

                /**
                 * <p>
                 * An explanatory message associated with the result.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown}.
                 */
                public Markdown getMessage() {
                    return message;
                }

                /**
                 * <p>
                 * A link to further details on the result.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getDetail() {
                    return detail;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        (result != null) || 
                        (message != null) || 
                        (detail != null);
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
                            accept(result, "result", visitor);
                            accept(message, "message", visitor);
                            accept(detail, "detail", visitor);
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
                            result, 
                            message, 
                            detail);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(result).from(this);
                }

                public Builder toBuilder(TestReportActionResult result) {
                    return new Builder(result).from(this);
                }

                public static Builder builder(TestReportActionResult result) {
                    return new Builder(result);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final TestReportActionResult result;

                    // optional
                    private Markdown message;
                    private String detail;

                    private Builder(TestReportActionResult result) {
                        super();
                        this.result = result;
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
                     * An explanatory message associated with the result.
                     * </p>
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
                     * <p>
                     * A link to further details on the result.
                     * </p>
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

                    @Override
                    public Assert build() {
                        return new Assert(this);
                    }

                    private Builder from(Assert _assert) {
                        id = _assert.id;
                        extension.addAll(_assert.extension);
                        modifierExtension.addAll(_assert.modifierExtension);
                        message = _assert.message;
                        detail = _assert.detail;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * A test executed from the test script.
     * </p>
     */
    public static class Test extends BackboneElement {
        private final String name;
        private final String description;
        private final List<Action> action;

        private volatile int hashCode;

        private Test(Builder builder) {
            super(builder);
            name = builder.name;
            description = builder.description;
            action = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.action, "action"));
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * The name of this test used for tracking/logging purposes by test engines.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * A short description of the test used by test engines for tracking and reporting purposes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * Action would contain either an operation or an assertion.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action}.
         */
        public List<Action> getAction() {
            return action;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (description != null) || 
                !action.isEmpty();
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
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
                    accept(action, "action", visitor, Action.class);
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
            return new Builder(action).from(this);
        }

        public Builder toBuilder(List<Action> action) {
            return new Builder(action).from(this);
        }

        public static Builder builder(List<Action> action) {
            return new Builder(action);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<Action> action;

            // optional
            private String name;
            private String description;

            private Builder(List<Action> action) {
                super();
                this.action = action;
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
             * The name of this test used for tracking/logging purposes by test engines.
             * </p>
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
             * <p>
             * A short description of the test used by test engines for tracking and reporting purposes.
             * </p>
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

            @Override
            public Test build() {
                return new Test(this);
            }

            private Builder from(Test test) {
                id = test.id;
                extension.addAll(test.extension);
                modifierExtension.addAll(test.modifierExtension);
                name = test.name;
                description = test.description;
                return this;
            }
        }

        /**
         * <p>
         * Action would contain either an operation or an assertion.
         * </p>
         */
        public static class Action extends BackboneElement {
            private final TestReport.Setup.Action.Operation operation;
            private final TestReport.Setup.Action.Assert _assert;

            private volatile int hashCode;

            private Action(Builder builder) {
                super(builder);
                operation = builder.operation;
                _assert = builder._assert;
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * An operation would involve a REST request to a server.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Operation}.
             */
            public TestReport.Setup.Action.Operation getOperation() {
                return operation;
            }

            /**
             * <p>
             * The results of the assertion performed on the previous operations.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Assert}.
             */
            public TestReport.Setup.Action.Assert getassert() {
                return _assert;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (operation != null) || 
                    (_assert != null);
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
                        accept(operation, "operation", visitor);
                        accept(_assert, "assert", visitor);
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
                // optional
                private TestReport.Setup.Action.Operation operation;
                private TestReport.Setup.Action.Assert _assert;

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
                 * An operation would involve a REST request to a server.
                 * </p>
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
                 * <p>
                 * The results of the assertion performed on the previous operations.
                 * </p>
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

                @Override
                public Action build() {
                    return new Action(this);
                }

                private Builder from(Action action) {
                    id = action.id;
                    extension.addAll(action.extension);
                    modifierExtension.addAll(action.modifierExtension);
                    operation = action.operation;
                    _assert = action._assert;
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * The results of the series of operations required to clean up after all the tests were executed (successfully or 
     * otherwise).
     * </p>
     */
    public static class Teardown extends BackboneElement {
        private final List<Action> action;

        private volatile int hashCode;

        private Teardown(Builder builder) {
            super(builder);
            action = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.action, "action"));
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * The teardown action will only contain an operation.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Action}.
         */
        public List<Action> getAction() {
            return action;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                !action.isEmpty();
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
                    accept(action, "action", visitor, Action.class);
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
            return new Builder(action).from(this);
        }

        public Builder toBuilder(List<Action> action) {
            return new Builder(action).from(this);
        }

        public static Builder builder(List<Action> action) {
            return new Builder(action);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<Action> action;

            private Builder(List<Action> action) {
                super();
                this.action = action;
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
            public Teardown build() {
                return new Teardown(this);
            }

            private Builder from(Teardown teardown) {
                id = teardown.id;
                extension.addAll(teardown.extension);
                modifierExtension.addAll(teardown.modifierExtension);
                return this;
            }
        }

        /**
         * <p>
         * The teardown action will only contain an operation.
         * </p>
         */
        public static class Action extends BackboneElement {
            private final TestReport.Setup.Action.Operation operation;

            private volatile int hashCode;

            private Action(Builder builder) {
                super(builder);
                operation = ValidationSupport.requireNonNull(builder.operation, "operation");
                if (!hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * An operation would involve a REST request to a server.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Operation}.
             */
            public TestReport.Setup.Action.Operation getOperation() {
                return operation;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (operation != null);
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
                        accept(operation, "operation", visitor);
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
                return new Builder(operation).from(this);
            }

            public Builder toBuilder(TestReport.Setup.Action.Operation operation) {
                return new Builder(operation).from(this);
            }

            public static Builder builder(TestReport.Setup.Action.Operation operation) {
                return new Builder(operation);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final TestReport.Setup.Action.Operation operation;

                private Builder(TestReport.Setup.Action.Operation operation) {
                    super();
                    this.operation = operation;
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
                public Action build() {
                    return new Action(this);
                }

                private Builder from(Action action) {
                    id = action.id;
                    extension.addAll(action.extension);
                    modifierExtension.addAll(action.modifierExtension);
                    return this;
                }
            }
        }
    }
}
