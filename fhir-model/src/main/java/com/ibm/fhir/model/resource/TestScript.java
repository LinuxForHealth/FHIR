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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.AssertionDirectionType;
import com.ibm.fhir.model.type.code.AssertionOperatorType;
import com.ibm.fhir.model.type.code.AssertionResponseTypes;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.FHIRDefinedType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.TestScriptRequestMethodCode;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR 
 * specification.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "tst-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-1",
    level = "Rule",
    location = "TestScript.setup.action",
    description = "Setup action SHALL contain either an operation or assert but not both.",
    expression = "operation.exists() xor assert.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-2",
    level = "Rule",
    location = "TestScript.test.action",
    description = "Test action SHALL contain either an operation or assert but not both.",
    expression = "operation.exists() xor assert.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-3",
    level = "Rule",
    location = "TestScript.variable",
    description = "Variable can only contain one of expression, headerField or path.",
    expression = "expression.empty() or headerField.empty() or path.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-4",
    level = "Rule",
    location = "TestScript.metadata",
    description = "TestScript metadata capability SHALL contain required or validated or both.",
    expression = "capability.required.exists() or capability.validated.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-5",
    level = "Rule",
    location = "TestScript.setup.action.assert",
    description = "Only a single assertion SHALL be present within setup action assert element.",
    expression = "extension.exists() or (contentType.count() + expression.count() + headerField.count() + minimumId.count() + navigationLinks.count() + path.count() + requestMethod.count() + resource.count() + responseCode.count() + response.count()  + validateProfileId.count() <=1)",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-6",
    level = "Rule",
    location = "TestScript.test.action.assert",
    description = "Only a single assertion SHALL be present within test action assert element.",
    expression = "extension.exists() or (contentType.count() + expression.count() + headerField.count() + minimumId.count() + navigationLinks.count() + path.count() + requestMethod.count() + resource.count() + responseCode.count() + response.count() + validateProfileId.count() <=1)",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-7",
    level = "Rule",
    location = "TestScript.setup.action.operation",
    description = "Setup operation SHALL contain either sourceId or targetId or params or url.",
    expression = "sourceId.exists() or (targetId.count() + url.count() + params.count() = 1) or (type.code in ('capabilities' |'search' | 'transaction' | 'history'))",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-8",
    level = "Rule",
    location = "TestScript.test.action.operation",
    description = "Test operation SHALL contain either sourceId or targetId or params or url.",
    expression = "sourceId.exists() or (targetId.count() + url.count() + params.count() = 1) or (type.code in ('capabilities' | 'search' | 'transaction' | 'history'))",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-9",
    level = "Rule",
    location = "TestScript.teardown.action.operation",
    description = "Teardown operation SHALL contain either sourceId or targetId or params or url.",
    expression = "sourceId.exists() or (targetId.count() + url.count() + params.count() = 1) or (type.code in ('capabilities' | 'search' | 'transaction' | 'history'))",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-10",
    level = "Rule",
    location = "TestScript.setup.action.assert",
    description = "Setup action assert SHALL contain either compareToSourceId and compareToSourceExpression, compareToSourceId and compareToSourcePath or neither.",
    expression = "compareToSourceId.empty() xor (compareToSourceExpression.exists() or compareToSourcePath.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-11",
    level = "Rule",
    location = "TestScript.test.action.assert",
    description = "Test action assert SHALL contain either compareToSourceId and compareToSourceExpression, compareToSourceId and compareToSourcePath or neither.",
    expression = "compareToSourceId.empty() xor (compareToSourceExpression.exists() or compareToSourcePath.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-12",
    level = "Rule",
    location = "TestScript.setup.action.assert",
    description = "Setup action assert response and responseCode SHALL be empty when direction equals request",
    expression = "(response.empty() and responseCode.empty() and direction = 'request') or direction.empty() or direction = 'response'",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "tst-13",
    level = "Rule",
    location = "TestScript.test.action.assert",
    description = "Test action assert response and response and responseCode SHALL be empty when direction equals request",
    expression = "(response.empty() and responseCode.empty() and direction = 'request') or direction.empty() or direction = 'response'",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript"
)
@Constraint(
    id = "testScript-14",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript",
    generated = true
)
@Constraint(
    id = "testScript-15",
    level = "Warning",
    location = "origin.profile",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/testscript-profile-origin-types",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/testscript-profile-origin-types', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript",
    generated = true
)
@Constraint(
    id = "testScript-16",
    level = "Warning",
    location = "destination.profile",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/testscript-profile-destination-types",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/testscript-profile-destination-types', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript",
    generated = true
)
@Constraint(
    id = "testScript-17",
    level = "Warning",
    location = "setup.action.operation.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/testscript-operation-codes",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/testscript-operation-codes', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/TestScript",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class TestScript extends DomainResource {
    @Summary
    @Required
    private final Uri url;
    @Summary
    private final Identifier identifier;
    @Summary
    private final String version;
    @Summary
    @Required
    private final String name;
    @Summary
    private final String title;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.0.1"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    private final Boolean experimental;
    @Summary
    private final DateTime date;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    private final Markdown description;
    @Summary
    private final List<UsageContext> useContext;
    @Summary
    @Binding(
        bindingName = "Jurisdiction",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Countries and regions within which this artifact is targeted for use.",
        valueSet = "http://hl7.org/fhir/ValueSet/jurisdiction"
    )
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final Markdown copyright;
    private final List<Origin> origin;
    private final List<Destination> destination;
    private final Metadata metadata;
    private final List<Fixture> fixture;
    private final List<Reference> profile;
    private final List<Variable> variable;
    private final Setup setup;
    private final List<Test> test;
    private final Teardown teardown;

    private TestScript(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = builder.identifier;
        version = builder.version;
        name = builder.name;
        title = builder.title;
        status = builder.status;
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        copyright = builder.copyright;
        origin = Collections.unmodifiableList(builder.origin);
        destination = Collections.unmodifiableList(builder.destination);
        metadata = builder.metadata;
        fixture = Collections.unmodifiableList(builder.fixture);
        profile = Collections.unmodifiableList(builder.profile);
        variable = Collections.unmodifiableList(builder.variable);
        setup = builder.setup;
        test = Collections.unmodifiableList(builder.test);
        teardown = builder.teardown;
    }

    /**
     * An absolute URI that is used to identify this test script when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this test script is (or will be) published. This URL can be the target of 
     * a canonical reference. It SHALL remain the same when the test script is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that is non-null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this test script when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the test script when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the test script author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the test script. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the test script.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this test script. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this test script is authored for testing purposes (or education/evaluation/marketing) 
     * and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the test script was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the test script changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the test script.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * A free text natural language description of the test script from a consumer's perspective.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate test script instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the test script is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this test script is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the test script.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * An abstract server used in operations within this test script in the origin element.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Origin} that may be empty.
     */
    public List<Origin> getOrigin() {
        return origin;
    }

    /**
     * An abstract server used in operations within this test script in the destination element.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Destination} that may be empty.
     */
    public List<Destination> getDestination() {
        return destination;
    }

    /**
     * The required capability must exist and are assumed to function correctly on the FHIR server being tested.
     * 
     * @return
     *     An immutable object of type {@link Metadata} that may be null.
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Fixture} that may be empty.
     */
    public List<Fixture> getFixture() {
        return fixture;
    }

    /**
     * Reference to the profile to be used for validation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getProfile() {
        return profile;
    }

    /**
     * Variable is set based either on element value in response body or on header field value in the response headers.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Variable} that may be empty.
     */
    public List<Variable> getVariable() {
        return variable;
    }

    /**
     * A series of required setup operations before tests are executed.
     * 
     * @return
     *     An immutable object of type {@link Setup} that may be null.
     */
    public Setup getSetup() {
        return setup;
    }

    /**
     * A test in this script.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Test} that may be empty.
     */
    public List<Test> getTest() {
        return test;
    }

    /**
     * A series of operations required to clean up after all the tests are executed (successfully or otherwise).
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
            (url != null) || 
            (identifier != null) || 
            (version != null) || 
            (name != null) || 
            (title != null) || 
            (status != null) || 
            (experimental != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (purpose != null) || 
            (copyright != null) || 
            !origin.isEmpty() || 
            !destination.isEmpty() || 
            (metadata != null) || 
            !fixture.isEmpty() || 
            !profile.isEmpty() || 
            !variable.isEmpty() || 
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
                accept(url, "url", visitor);
                accept(identifier, "identifier", visitor);
                accept(version, "version", visitor);
                accept(name, "name", visitor);
                accept(title, "title", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(copyright, "copyright", visitor);
                accept(origin, "origin", visitor, Origin.class);
                accept(destination, "destination", visitor, Destination.class);
                accept(metadata, "metadata", visitor);
                accept(fixture, "fixture", visitor, Fixture.class);
                accept(profile, "profile", visitor, Reference.class);
                accept(variable, "variable", visitor, Variable.class);
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
        TestScript other = (TestScript) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(url, other.url) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(version, other.version) && 
            Objects.equals(name, other.name) && 
            Objects.equals(title, other.title) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(origin, other.origin) && 
            Objects.equals(destination, other.destination) && 
            Objects.equals(metadata, other.metadata) && 
            Objects.equals(fixture, other.fixture) && 
            Objects.equals(profile, other.profile) && 
            Objects.equals(variable, other.variable) && 
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
                url, 
                identifier, 
                version, 
                name, 
                title, 
                status, 
                experimental, 
                date, 
                publisher, 
                contact, 
                description, 
                useContext, 
                jurisdiction, 
                purpose, 
                copyright, 
                origin, 
                destination, 
                metadata, 
                fixture, 
                profile, 
                variable, 
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
        private Uri url;
        private Identifier identifier;
        private String version;
        private String name;
        private String title;
        private PublicationStatus status;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private List<Origin> origin = new ArrayList<>();
        private List<Destination> destination = new ArrayList<>();
        private Metadata metadata;
        private List<Fixture> fixture = new ArrayList<>();
        private List<Reference> profile = new ArrayList<>();
        private List<Variable> variable = new ArrayList<>();
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
         * An absolute URI that is used to identify this test script when it is referenced in a specification, model, design or 
         * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this test script is (or will be) published. This URL can be the target of 
         * a canonical reference. It SHALL remain the same when the test script is stored on different servers.
         * 
         * <p>This element is required.
         * 
         * @param url
         *     Canonical identifier for this test script, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this test script when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * 
         * @param identifier
         *     Additional identifier for the test script
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     Business version of the test script
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(com.ibm.fhir.model.type.String)
         */
        public Builder version(java.lang.String version) {
            this.version = (version == null) ? null : String.of(version);
            return this;
        }

        /**
         * The identifier that is used to identify this version of the test script when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the test script author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the test script
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this test script (computer friendly)
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
         * A natural language name identifying the test script. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this test script (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this test script (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #title(com.ibm.fhir.model.type.String)
         */
        public Builder title(java.lang.String title) {
            this.title = (title == null) ? null : String.of(title);
            return this;
        }

        /**
         * A short, descriptive, user-friendly title for the test script.
         * 
         * @param title
         *     Name for this test script (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this test script. Enables tracking the life-cycle of the content.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | active | retired | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(PublicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Convenience method for setting {@code experimental}.
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #experimental(com.ibm.fhir.model.type.Boolean)
         */
        public Builder experimental(java.lang.Boolean experimental) {
            this.experimental = (experimental == null) ? null : Boolean.of(experimental);
            return this;
        }

        /**
         * A Boolean value to indicate that this test script is authored for testing purposes (or education/evaluation/marketing) 
         * and is not intended to be used for genuine usage.
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder experimental(Boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        /**
         * The date (and optionally time) when the test script was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the test script changes.
         * 
         * @param date
         *     Date last changed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Convenience method for setting {@code publisher}.
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #publisher(com.ibm.fhir.model.type.String)
         */
        public Builder publisher(java.lang.String publisher) {
            this.publisher = (publisher == null) ? null : String.of(publisher);
            return this;
        }

        /**
         * The name of the organization or individual that published the test script.
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * A free text natural language description of the test script from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the test script
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate test script instances.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(UsageContext... useContext) {
            for (UsageContext value : useContext) {
                this.useContext.add(value);
            }
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate test script instances.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * A legal or geographic region in which the test script is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for test script (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * A legal or geographic region in which the test script is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for test script (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * Explanation of why this test script is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this test script is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the test script.
         * 
         * @param copyright
         *     Use and/or publishing restrictions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder copyright(Markdown copyright) {
            this.copyright = copyright;
            return this;
        }

        /**
         * An abstract server used in operations within this test script in the origin element.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param origin
         *     An abstract server representing a client or sender in a message exchange
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder origin(Origin... origin) {
            for (Origin value : origin) {
                this.origin.add(value);
            }
            return this;
        }

        /**
         * An abstract server used in operations within this test script in the origin element.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param origin
         *     An abstract server representing a client or sender in a message exchange
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder origin(Collection<Origin> origin) {
            this.origin = new ArrayList<>(origin);
            return this;
        }

        /**
         * An abstract server used in operations within this test script in the destination element.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param destination
         *     An abstract server representing a destination or receiver in a message exchange
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder destination(Destination... destination) {
            for (Destination value : destination) {
                this.destination.add(value);
            }
            return this;
        }

        /**
         * An abstract server used in operations within this test script in the destination element.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param destination
         *     An abstract server representing a destination or receiver in a message exchange
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder destination(Collection<Destination> destination) {
            this.destination = new ArrayList<>(destination);
            return this;
        }

        /**
         * The required capability must exist and are assumed to function correctly on the FHIR server being tested.
         * 
         * @param metadata
         *     Required capability that is assumed to function correctly on the FHIR server being tested
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder metadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        /**
         * Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param fixture
         *     Fixture in the test script - by reference (uri)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fixture(Fixture... fixture) {
            for (Fixture value : fixture) {
                this.fixture.add(value);
            }
            return this;
        }

        /**
         * Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param fixture
         *     Fixture in the test script - by reference (uri)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder fixture(Collection<Fixture> fixture) {
            this.fixture = new ArrayList<>(fixture);
            return this;
        }

        /**
         * Reference to the profile to be used for validation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param profile
         *     Reference of the validation profile
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder profile(Reference... profile) {
            for (Reference value : profile) {
                this.profile.add(value);
            }
            return this;
        }

        /**
         * Reference to the profile to be used for validation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param profile
         *     Reference of the validation profile
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder profile(Collection<Reference> profile) {
            this.profile = new ArrayList<>(profile);
            return this;
        }

        /**
         * Variable is set based either on element value in response body or on header field value in the response headers.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param variable
         *     Placeholder for evaluated elements
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder variable(Variable... variable) {
            for (Variable value : variable) {
                this.variable.add(value);
            }
            return this;
        }

        /**
         * Variable is set based either on element value in response body or on header field value in the response headers.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param variable
         *     Placeholder for evaluated elements
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder variable(Collection<Variable> variable) {
            this.variable = new ArrayList<>(variable);
            return this;
        }

        /**
         * A series of required setup operations before tests are executed.
         * 
         * @param setup
         *     A series of required setup operations before tests are executed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder setup(Setup setup) {
            this.setup = setup;
            return this;
        }

        /**
         * A test in this script.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param test
         *     A test in this script
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
         * A test in this script.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param test
         *     A test in this script
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
         * A series of operations required to clean up after all the tests are executed (successfully or otherwise).
         * 
         * @param teardown
         *     A series of required clean up steps
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder teardown(Teardown teardown) {
            this.teardown = teardown;
            return this;
        }

        /**
         * Build the {@link TestScript}
         * 
         * <p>Required elements:
         * <ul>
         * <li>url</li>
         * <li>name</li>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link TestScript}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid TestScript per the base specification
         */
        @Override
        public TestScript build() {
            TestScript testScript = new TestScript(this);
            if (validating) {
                validate(testScript);
            }
            return testScript;
        }

        protected void validate(TestScript testScript) {
            super.validate(testScript);
            ValidationSupport.requireNonNull(testScript.url, "url");
            ValidationSupport.requireNonNull(testScript.name, "name");
            ValidationSupport.requireNonNull(testScript.status, "status");
            ValidationSupport.checkList(testScript.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(testScript.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(testScript.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(testScript.origin, "origin", Origin.class);
            ValidationSupport.checkList(testScript.destination, "destination", Destination.class);
            ValidationSupport.checkList(testScript.fixture, "fixture", Fixture.class);
            ValidationSupport.checkList(testScript.profile, "profile", Reference.class);
            ValidationSupport.checkList(testScript.variable, "variable", Variable.class);
            ValidationSupport.checkList(testScript.test, "test", Test.class);
        }

        protected Builder from(TestScript testScript) {
            super.from(testScript);
            url = testScript.url;
            identifier = testScript.identifier;
            version = testScript.version;
            name = testScript.name;
            title = testScript.title;
            status = testScript.status;
            experimental = testScript.experimental;
            date = testScript.date;
            publisher = testScript.publisher;
            contact.addAll(testScript.contact);
            description = testScript.description;
            useContext.addAll(testScript.useContext);
            jurisdiction.addAll(testScript.jurisdiction);
            purpose = testScript.purpose;
            copyright = testScript.copyright;
            origin.addAll(testScript.origin);
            destination.addAll(testScript.destination);
            metadata = testScript.metadata;
            fixture.addAll(testScript.fixture);
            profile.addAll(testScript.profile);
            variable.addAll(testScript.variable);
            setup = testScript.setup;
            test.addAll(testScript.test);
            teardown = testScript.teardown;
            return this;
        }
    }

    /**
     * An abstract server used in operations within this test script in the origin element.
     */
    public static class Origin extends BackboneElement {
        @Required
        private final Integer index;
        @Binding(
            bindingName = "TestScriptProfileOriginType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The type of origin profile the test system supports.",
            valueSet = "http://hl7.org/fhir/ValueSet/testscript-profile-origin-types"
        )
        @Required
        private final Coding profile;

        private Origin(Builder builder) {
            super(builder);
            index = builder.index;
            profile = builder.profile;
        }

        /**
         * Abstract name given to an origin server in this test script. The name is provided as a number starting at 1.
         * 
         * @return
         *     An immutable object of type {@link Integer} that is non-null.
         */
        public Integer getIndex() {
            return index;
        }

        /**
         * The type of origin profile the test system supports.
         * 
         * @return
         *     An immutable object of type {@link Coding} that is non-null.
         */
        public Coding getProfile() {
            return profile;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (index != null) || 
                (profile != null);
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
                    accept(index, "index", visitor);
                    accept(profile, "profile", visitor);
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
            Origin other = (Origin) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(index, other.index) && 
                Objects.equals(profile, other.profile);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    index, 
                    profile);
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
            private Integer index;
            private Coding profile;

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
             * Convenience method for setting {@code index}.
             * 
             * <p>This element is required.
             * 
             * @param index
             *     The index of the abstract origin server starting at 1
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #index(com.ibm.fhir.model.type.Integer)
             */
            public Builder index(java.lang.Integer index) {
                this.index = (index == null) ? null : Integer.of(index);
                return this;
            }

            /**
             * Abstract name given to an origin server in this test script. The name is provided as a number starting at 1.
             * 
             * <p>This element is required.
             * 
             * @param index
             *     The index of the abstract origin server starting at 1
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder index(Integer index) {
                this.index = index;
                return this;
            }

            /**
             * The type of origin profile the test system supports.
             * 
             * <p>This element is required.
             * 
             * @param profile
             *     FHIR-Client | FHIR-SDC-FormFiller
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder profile(Coding profile) {
                this.profile = profile;
                return this;
            }

            /**
             * Build the {@link Origin}
             * 
             * <p>Required elements:
             * <ul>
             * <li>index</li>
             * <li>profile</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Origin}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Origin per the base specification
             */
            @Override
            public Origin build() {
                Origin origin = new Origin(this);
                if (validating) {
                    validate(origin);
                }
                return origin;
            }

            protected void validate(Origin origin) {
                super.validate(origin);
                ValidationSupport.requireNonNull(origin.index, "index");
                ValidationSupport.requireNonNull(origin.profile, "profile");
                ValidationSupport.requireValueOrChildren(origin);
            }

            protected Builder from(Origin origin) {
                super.from(origin);
                index = origin.index;
                profile = origin.profile;
                return this;
            }
        }
    }

    /**
     * An abstract server used in operations within this test script in the destination element.
     */
    public static class Destination extends BackboneElement {
        @Required
        private final Integer index;
        @Binding(
            bindingName = "TestScriptProfileDestinationType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The type of destination profile the test system supports.",
            valueSet = "http://hl7.org/fhir/ValueSet/testscript-profile-destination-types"
        )
        @Required
        private final Coding profile;

        private Destination(Builder builder) {
            super(builder);
            index = builder.index;
            profile = builder.profile;
        }

        /**
         * Abstract name given to a destination server in this test script. The name is provided as a number starting at 1.
         * 
         * @return
         *     An immutable object of type {@link Integer} that is non-null.
         */
        public Integer getIndex() {
            return index;
        }

        /**
         * The type of destination profile the test system supports.
         * 
         * @return
         *     An immutable object of type {@link Coding} that is non-null.
         */
        public Coding getProfile() {
            return profile;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (index != null) || 
                (profile != null);
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
                    accept(index, "index", visitor);
                    accept(profile, "profile", visitor);
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
            Destination other = (Destination) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(index, other.index) && 
                Objects.equals(profile, other.profile);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    index, 
                    profile);
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
            private Integer index;
            private Coding profile;

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
             * Convenience method for setting {@code index}.
             * 
             * <p>This element is required.
             * 
             * @param index
             *     The index of the abstract destination server starting at 1
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #index(com.ibm.fhir.model.type.Integer)
             */
            public Builder index(java.lang.Integer index) {
                this.index = (index == null) ? null : Integer.of(index);
                return this;
            }

            /**
             * Abstract name given to a destination server in this test script. The name is provided as a number starting at 1.
             * 
             * <p>This element is required.
             * 
             * @param index
             *     The index of the abstract destination server starting at 1
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder index(Integer index) {
                this.index = index;
                return this;
            }

            /**
             * The type of destination profile the test system supports.
             * 
             * <p>This element is required.
             * 
             * @param profile
             *     FHIR-Server | FHIR-SDC-FormManager | FHIR-SDC-FormReceiver | FHIR-SDC-FormProcessor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder profile(Coding profile) {
                this.profile = profile;
                return this;
            }

            /**
             * Build the {@link Destination}
             * 
             * <p>Required elements:
             * <ul>
             * <li>index</li>
             * <li>profile</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Destination}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Destination per the base specification
             */
            @Override
            public Destination build() {
                Destination destination = new Destination(this);
                if (validating) {
                    validate(destination);
                }
                return destination;
            }

            protected void validate(Destination destination) {
                super.validate(destination);
                ValidationSupport.requireNonNull(destination.index, "index");
                ValidationSupport.requireNonNull(destination.profile, "profile");
                ValidationSupport.requireValueOrChildren(destination);
            }

            protected Builder from(Destination destination) {
                super.from(destination);
                index = destination.index;
                profile = destination.profile;
                return this;
            }
        }
    }

    /**
     * The required capability must exist and are assumed to function correctly on the FHIR server being tested.
     */
    public static class Metadata extends BackboneElement {
        private final List<Link> link;
        @Required
        private final List<Capability> capability;

        private Metadata(Builder builder) {
            super(builder);
            link = Collections.unmodifiableList(builder.link);
            capability = Collections.unmodifiableList(builder.capability);
        }

        /**
         * A link to the FHIR specification that this test is covering.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Link} that may be empty.
         */
        public List<Link> getLink() {
            return link;
        }

        /**
         * Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Capability} that is non-empty.
         */
        public List<Capability> getCapability() {
            return capability;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !link.isEmpty() || 
                !capability.isEmpty();
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
                    accept(link, "link", visitor, Link.class);
                    accept(capability, "capability", visitor, Capability.class);
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
            Metadata other = (Metadata) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(link, other.link) && 
                Objects.equals(capability, other.capability);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    link, 
                    capability);
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
            private List<Link> link = new ArrayList<>();
            private List<Capability> capability = new ArrayList<>();

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
             * A link to the FHIR specification that this test is covering.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param link
             *     Links to the FHIR specification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder link(Link... link) {
                for (Link value : link) {
                    this.link.add(value);
                }
                return this;
            }

            /**
             * A link to the FHIR specification that this test is covering.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param link
             *     Links to the FHIR specification
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder link(Collection<Link> link) {
                this.link = new ArrayList<>(link);
                return this;
            }

            /**
             * Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param capability
             *     Capabilities that are assumed to function correctly on the FHIR server being tested
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder capability(Capability... capability) {
                for (Capability value : capability) {
                    this.capability.add(value);
                }
                return this;
            }

            /**
             * Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param capability
             *     Capabilities that are assumed to function correctly on the FHIR server being tested
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder capability(Collection<Capability> capability) {
                this.capability = new ArrayList<>(capability);
                return this;
            }

            /**
             * Build the {@link Metadata}
             * 
             * <p>Required elements:
             * <ul>
             * <li>capability</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Metadata}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Metadata per the base specification
             */
            @Override
            public Metadata build() {
                Metadata metadata = new Metadata(this);
                if (validating) {
                    validate(metadata);
                }
                return metadata;
            }

            protected void validate(Metadata metadata) {
                super.validate(metadata);
                ValidationSupport.checkList(metadata.link, "link", Link.class);
                ValidationSupport.checkNonEmptyList(metadata.capability, "capability", Capability.class);
                ValidationSupport.requireValueOrChildren(metadata);
            }

            protected Builder from(Metadata metadata) {
                super.from(metadata);
                link.addAll(metadata.link);
                capability.addAll(metadata.capability);
                return this;
            }
        }

        /**
         * A link to the FHIR specification that this test is covering.
         */
        public static class Link extends BackboneElement {
            @Required
            private final Uri url;
            private final String description;

            private Link(Builder builder) {
                super(builder);
                url = builder.url;
                description = builder.description;
            }

            /**
             * URL to a particular requirement or feature within the FHIR specification.
             * 
             * @return
             *     An immutable object of type {@link Uri} that is non-null.
             */
            public Uri getUrl() {
                return url;
            }

            /**
             * Short description of the link.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (url != null) || 
                    (description != null);
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
                        accept(url, "url", visitor);
                        accept(description, "description", visitor);
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
                Link other = (Link) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(url, other.url) && 
                    Objects.equals(description, other.description);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        url, 
                        description);
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
                private Uri url;
                private String description;

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
                 * URL to a particular requirement or feature within the FHIR specification.
                 * 
                 * <p>This element is required.
                 * 
                 * @param url
                 *     URL to the specification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder url(Uri url) {
                    this.url = url;
                    return this;
                }

                /**
                 * Convenience method for setting {@code description}.
                 * 
                 * @param description
                 *     Short description
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
                 * Short description of the link.
                 * 
                 * @param description
                 *     Short description
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Build the {@link Link}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>url</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Link}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Link per the base specification
                 */
                @Override
                public Link build() {
                    Link link = new Link(this);
                    if (validating) {
                        validate(link);
                    }
                    return link;
                }

                protected void validate(Link link) {
                    super.validate(link);
                    ValidationSupport.requireNonNull(link.url, "url");
                    ValidationSupport.requireValueOrChildren(link);
                }

                protected Builder from(Link link) {
                    super.from(link);
                    url = link.url;
                    description = link.description;
                    return this;
                }
            }
        }

        /**
         * Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.
         */
        public static class Capability extends BackboneElement {
            @Required
            private final Boolean required;
            @Required
            private final Boolean validated;
            private final String description;
            private final List<Integer> origin;
            private final Integer destination;
            private final List<Uri> link;
            @Required
            private final Canonical capabilities;

            private Capability(Builder builder) {
                super(builder);
                required = builder.required;
                validated = builder.validated;
                description = builder.description;
                origin = Collections.unmodifiableList(builder.origin);
                destination = builder.destination;
                link = Collections.unmodifiableList(builder.link);
                capabilities = builder.capabilities;
            }

            /**
             * Whether or not the test execution will require the given capabilities of the server in order for this test script to 
             * execute.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that is non-null.
             */
            public Boolean getRequired() {
                return required;
            }

            /**
             * Whether or not the test execution will validate the given capabilities of the server in order for this test script to 
             * execute.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that is non-null.
             */
            public Boolean getValidated() {
                return validated;
            }

            /**
             * Description of the capabilities that this test script is requiring the server to support.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            /**
             * Which origin server these requirements apply to.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer} that may be empty.
             */
            public List<Integer> getOrigin() {
                return origin;
            }

            /**
             * Which server these requirements apply to.
             * 
             * @return
             *     An immutable object of type {@link Integer} that may be null.
             */
            public Integer getDestination() {
                return destination;
            }

            /**
             * Links to the FHIR specification that describes this interaction and the resources involved in more detail.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
             */
            public List<Uri> getLink() {
                return link;
            }

            /**
             * Minimum capabilities required of server for test script to execute successfully. If server does not meet at a minimum 
             * the referenced capability statement, then all tests in this script are skipped.
             * 
             * @return
             *     An immutable object of type {@link Canonical} that is non-null.
             */
            public Canonical getCapabilities() {
                return capabilities;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (required != null) || 
                    (validated != null) || 
                    (description != null) || 
                    !origin.isEmpty() || 
                    (destination != null) || 
                    !link.isEmpty() || 
                    (capabilities != null);
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
                        accept(required, "required", visitor);
                        accept(validated, "validated", visitor);
                        accept(description, "description", visitor);
                        accept(origin, "origin", visitor, Integer.class);
                        accept(destination, "destination", visitor);
                        accept(link, "link", visitor, Uri.class);
                        accept(capabilities, "capabilities", visitor);
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
                Capability other = (Capability) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(required, other.required) && 
                    Objects.equals(validated, other.validated) && 
                    Objects.equals(description, other.description) && 
                    Objects.equals(origin, other.origin) && 
                    Objects.equals(destination, other.destination) && 
                    Objects.equals(link, other.link) && 
                    Objects.equals(capabilities, other.capabilities);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        required, 
                        validated, 
                        description, 
                        origin, 
                        destination, 
                        link, 
                        capabilities);
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
                private Boolean required;
                private Boolean validated;
                private String description;
                private List<Integer> origin = new ArrayList<>();
                private Integer destination;
                private List<Uri> link = new ArrayList<>();
                private Canonical capabilities;

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
                 * Convenience method for setting {@code required}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param required
                 *     Are the capabilities required?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #required(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder required(java.lang.Boolean required) {
                    this.required = (required == null) ? null : Boolean.of(required);
                    return this;
                }

                /**
                 * Whether or not the test execution will require the given capabilities of the server in order for this test script to 
                 * execute.
                 * 
                 * <p>This element is required.
                 * 
                 * @param required
                 *     Are the capabilities required?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder required(Boolean required) {
                    this.required = required;
                    return this;
                }

                /**
                 * Convenience method for setting {@code validated}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param validated
                 *     Are the capabilities validated?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #validated(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder validated(java.lang.Boolean validated) {
                    this.validated = (validated == null) ? null : Boolean.of(validated);
                    return this;
                }

                /**
                 * Whether or not the test execution will validate the given capabilities of the server in order for this test script to 
                 * execute.
                 * 
                 * <p>This element is required.
                 * 
                 * @param validated
                 *     Are the capabilities validated?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder validated(Boolean validated) {
                    this.validated = validated;
                    return this;
                }

                /**
                 * Convenience method for setting {@code description}.
                 * 
                 * @param description
                 *     The expected capabilities of the server
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
                 * Description of the capabilities that this test script is requiring the server to support.
                 * 
                 * @param description
                 *     The expected capabilities of the server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Convenience method for setting {@code origin}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param origin
                 *     Which origin server these requirements apply to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #origin(com.ibm.fhir.model.type.Integer)
                 */
                public Builder origin(java.lang.Integer... origin) {
                    for (java.lang.Integer value : origin) {
                        this.origin.add((value == null) ? null : Integer.of(value));
                    }
                    return this;
                }

                /**
                 * Which origin server these requirements apply to.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param origin
                 *     Which origin server these requirements apply to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder origin(Integer... origin) {
                    for (Integer value : origin) {
                        this.origin.add(value);
                    }
                    return this;
                }

                /**
                 * Which origin server these requirements apply to.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param origin
                 *     Which origin server these requirements apply to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder origin(Collection<Integer> origin) {
                    this.origin = new ArrayList<>(origin);
                    return this;
                }

                /**
                 * Convenience method for setting {@code destination}.
                 * 
                 * @param destination
                 *     Which server these requirements apply to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #destination(com.ibm.fhir.model.type.Integer)
                 */
                public Builder destination(java.lang.Integer destination) {
                    this.destination = (destination == null) ? null : Integer.of(destination);
                    return this;
                }

                /**
                 * Which server these requirements apply to.
                 * 
                 * @param destination
                 *     Which server these requirements apply to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder destination(Integer destination) {
                    this.destination = destination;
                    return this;
                }

                /**
                 * Links to the FHIR specification that describes this interaction and the resources involved in more detail.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param link
                 *     Links to the FHIR specification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder link(Uri... link) {
                    for (Uri value : link) {
                        this.link.add(value);
                    }
                    return this;
                }

                /**
                 * Links to the FHIR specification that describes this interaction and the resources involved in more detail.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param link
                 *     Links to the FHIR specification
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder link(Collection<Uri> link) {
                    this.link = new ArrayList<>(link);
                    return this;
                }

                /**
                 * Minimum capabilities required of server for test script to execute successfully. If server does not meet at a minimum 
                 * the referenced capability statement, then all tests in this script are skipped.
                 * 
                 * <p>This element is required.
                 * 
                 * @param capabilities
                 *     Required Capability Statement
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder capabilities(Canonical capabilities) {
                    this.capabilities = capabilities;
                    return this;
                }

                /**
                 * Build the {@link Capability}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>required</li>
                 * <li>validated</li>
                 * <li>capabilities</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Capability}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Capability per the base specification
                 */
                @Override
                public Capability build() {
                    Capability capability = new Capability(this);
                    if (validating) {
                        validate(capability);
                    }
                    return capability;
                }

                protected void validate(Capability capability) {
                    super.validate(capability);
                    ValidationSupport.requireNonNull(capability.required, "required");
                    ValidationSupport.requireNonNull(capability.validated, "validated");
                    ValidationSupport.checkList(capability.origin, "origin", Integer.class);
                    ValidationSupport.checkList(capability.link, "link", Uri.class);
                    ValidationSupport.requireNonNull(capability.capabilities, "capabilities");
                    ValidationSupport.requireValueOrChildren(capability);
                }

                protected Builder from(Capability capability) {
                    super.from(capability);
                    required = capability.required;
                    validated = capability.validated;
                    description = capability.description;
                    origin.addAll(capability.origin);
                    destination = capability.destination;
                    link.addAll(capability.link);
                    capabilities = capability.capabilities;
                    return this;
                }
            }
        }
    }

    /**
     * Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.
     */
    public static class Fixture extends BackboneElement {
        @Required
        private final Boolean autocreate;
        @Required
        private final Boolean autodelete;
        private final Reference resource;

        private Fixture(Builder builder) {
            super(builder);
            autocreate = builder.autocreate;
            autodelete = builder.autodelete;
            resource = builder.resource;
        }

        /**
         * Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each 
         * server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup 
         * section.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getAutocreate() {
            return autocreate;
        }

        /**
         * Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each 
         * server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.
         * teardown section.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getAutodelete() {
            return autodelete;
        }

        /**
         * Reference to the resource (containing the contents of the resource needed for operations).
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getResource() {
            return resource;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (autocreate != null) || 
                (autodelete != null) || 
                (resource != null);
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
                    accept(autocreate, "autocreate", visitor);
                    accept(autodelete, "autodelete", visitor);
                    accept(resource, "resource", visitor);
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
            Fixture other = (Fixture) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(autocreate, other.autocreate) && 
                Objects.equals(autodelete, other.autodelete) && 
                Objects.equals(resource, other.resource);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    autocreate, 
                    autodelete, 
                    resource);
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
            private Boolean autocreate;
            private Boolean autodelete;
            private Reference resource;

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
             * Convenience method for setting {@code autocreate}.
             * 
             * <p>This element is required.
             * 
             * @param autocreate
             *     Whether or not to implicitly create the fixture during setup
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #autocreate(com.ibm.fhir.model.type.Boolean)
             */
            public Builder autocreate(java.lang.Boolean autocreate) {
                this.autocreate = (autocreate == null) ? null : Boolean.of(autocreate);
                return this;
            }

            /**
             * Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each 
             * server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup 
             * section.
             * 
             * <p>This element is required.
             * 
             * @param autocreate
             *     Whether or not to implicitly create the fixture during setup
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder autocreate(Boolean autocreate) {
                this.autocreate = autocreate;
                return this;
            }

            /**
             * Convenience method for setting {@code autodelete}.
             * 
             * <p>This element is required.
             * 
             * @param autodelete
             *     Whether or not to implicitly delete the fixture during teardown
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #autodelete(com.ibm.fhir.model.type.Boolean)
             */
            public Builder autodelete(java.lang.Boolean autodelete) {
                this.autodelete = (autodelete == null) ? null : Boolean.of(autodelete);
                return this;
            }

            /**
             * Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each 
             * server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.
             * teardown section.
             * 
             * <p>This element is required.
             * 
             * @param autodelete
             *     Whether or not to implicitly delete the fixture during teardown
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder autodelete(Boolean autodelete) {
                this.autodelete = autodelete;
                return this;
            }

            /**
             * Reference to the resource (containing the contents of the resource needed for operations).
             * 
             * @param resource
             *     Reference of the resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Reference resource) {
                this.resource = resource;
                return this;
            }

            /**
             * Build the {@link Fixture}
             * 
             * <p>Required elements:
             * <ul>
             * <li>autocreate</li>
             * <li>autodelete</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Fixture}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Fixture per the base specification
             */
            @Override
            public Fixture build() {
                Fixture fixture = new Fixture(this);
                if (validating) {
                    validate(fixture);
                }
                return fixture;
            }

            protected void validate(Fixture fixture) {
                super.validate(fixture);
                ValidationSupport.requireNonNull(fixture.autocreate, "autocreate");
                ValidationSupport.requireNonNull(fixture.autodelete, "autodelete");
                ValidationSupport.requireValueOrChildren(fixture);
            }

            protected Builder from(Fixture fixture) {
                super.from(fixture);
                autocreate = fixture.autocreate;
                autodelete = fixture.autodelete;
                resource = fixture.resource;
                return this;
            }
        }
    }

    /**
     * Variable is set based either on element value in response body or on header field value in the response headers.
     */
    public static class Variable extends BackboneElement {
        @Required
        private final String name;
        private final String defaultValue;
        private final String description;
        private final String expression;
        private final String headerField;
        private final String hint;
        private final String path;
        private final Id sourceId;

        private Variable(Builder builder) {
            super(builder);
            name = builder.name;
            defaultValue = builder.defaultValue;
            description = builder.description;
            expression = builder.expression;
            headerField = builder.headerField;
            hint = builder.hint;
            path = builder.path;
            sourceId = builder.sourceId;
        }

        /**
         * Descriptive name for this variable.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * A default, hard-coded, or user-defined value for this variable.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDefaultValue() {
            return defaultValue;
        }

        /**
         * A free text natural language description of the variable and its purpose.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * The FHIRPath expression to evaluate against the fixture body. When variables are defined, only one of either 
         * expression, headerField or path must be specified.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getExpression() {
            return expression;
        }

        /**
         * Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getHeaderField() {
            return headerField;
        }

        /**
         * Displayable text string with hint help information to the user when entering a default value.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getHint() {
            return hint;
        }

        /**
         * XPath or JSONPath to evaluate against the fixture body. When variables are defined, only one of either expression, 
         * headerField or path must be specified.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getPath() {
            return path;
        }

        /**
         * Fixture to evaluate the XPath/JSONPath expression or the headerField against within this variable.
         * 
         * @return
         *     An immutable object of type {@link Id} that may be null.
         */
        public Id getSourceId() {
            return sourceId;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (defaultValue != null) || 
                (description != null) || 
                (expression != null) || 
                (headerField != null) || 
                (hint != null) || 
                (path != null) || 
                (sourceId != null);
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
                    accept(defaultValue, "defaultValue", visitor);
                    accept(description, "description", visitor);
                    accept(expression, "expression", visitor);
                    accept(headerField, "headerField", visitor);
                    accept(hint, "hint", visitor);
                    accept(path, "path", visitor);
                    accept(sourceId, "sourceId", visitor);
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
            Variable other = (Variable) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(defaultValue, other.defaultValue) && 
                Objects.equals(description, other.description) && 
                Objects.equals(expression, other.expression) && 
                Objects.equals(headerField, other.headerField) && 
                Objects.equals(hint, other.hint) && 
                Objects.equals(path, other.path) && 
                Objects.equals(sourceId, other.sourceId);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    defaultValue, 
                    description, 
                    expression, 
                    headerField, 
                    hint, 
                    path, 
                    sourceId);
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
            private String defaultValue;
            private String description;
            private String expression;
            private String headerField;
            private String hint;
            private String path;
            private Id sourceId;

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
             * <p>This element is required.
             * 
             * @param name
             *     Descriptive name for this variable
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
             * Descriptive name for this variable.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     Descriptive name for this variable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Convenience method for setting {@code defaultValue}.
             * 
             * @param defaultValue
             *     Default, hard-coded, or user-defined value for this variable
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #defaultValue(com.ibm.fhir.model.type.String)
             */
            public Builder defaultValue(java.lang.String defaultValue) {
                this.defaultValue = (defaultValue == null) ? null : String.of(defaultValue);
                return this;
            }

            /**
             * A default, hard-coded, or user-defined value for this variable.
             * 
             * @param defaultValue
             *     Default, hard-coded, or user-defined value for this variable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder defaultValue(String defaultValue) {
                this.defaultValue = defaultValue;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Natural language description of the variable
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
             * A free text natural language description of the variable and its purpose.
             * 
             * @param description
             *     Natural language description of the variable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Convenience method for setting {@code expression}.
             * 
             * @param expression
             *     The FHIRPath expression against the fixture body
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #expression(com.ibm.fhir.model.type.String)
             */
            public Builder expression(java.lang.String expression) {
                this.expression = (expression == null) ? null : String.of(expression);
                return this;
            }

            /**
             * The FHIRPath expression to evaluate against the fixture body. When variables are defined, only one of either 
             * expression, headerField or path must be specified.
             * 
             * @param expression
             *     The FHIRPath expression against the fixture body
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder expression(String expression) {
                this.expression = expression;
                return this;
            }

            /**
             * Convenience method for setting {@code headerField}.
             * 
             * @param headerField
             *     HTTP header field name for source
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #headerField(com.ibm.fhir.model.type.String)
             */
            public Builder headerField(java.lang.String headerField) {
                this.headerField = (headerField == null) ? null : String.of(headerField);
                return this;
            }

            /**
             * Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.
             * 
             * @param headerField
             *     HTTP header field name for source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder headerField(String headerField) {
                this.headerField = headerField;
                return this;
            }

            /**
             * Convenience method for setting {@code hint}.
             * 
             * @param hint
             *     Hint help text for default value to enter
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #hint(com.ibm.fhir.model.type.String)
             */
            public Builder hint(java.lang.String hint) {
                this.hint = (hint == null) ? null : String.of(hint);
                return this;
            }

            /**
             * Displayable text string with hint help information to the user when entering a default value.
             * 
             * @param hint
             *     Hint help text for default value to enter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder hint(String hint) {
                this.hint = hint;
                return this;
            }

            /**
             * Convenience method for setting {@code path}.
             * 
             * @param path
             *     XPath or JSONPath against the fixture body
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #path(com.ibm.fhir.model.type.String)
             */
            public Builder path(java.lang.String path) {
                this.path = (path == null) ? null : String.of(path);
                return this;
            }

            /**
             * XPath or JSONPath to evaluate against the fixture body. When variables are defined, only one of either expression, 
             * headerField or path must be specified.
             * 
             * @param path
             *     XPath or JSONPath against the fixture body
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * Fixture to evaluate the XPath/JSONPath expression or the headerField against within this variable.
             * 
             * @param sourceId
             *     Fixture Id of source expression or headerField within this variable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sourceId(Id sourceId) {
                this.sourceId = sourceId;
                return this;
            }

            /**
             * Build the {@link Variable}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Variable}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Variable per the base specification
             */
            @Override
            public Variable build() {
                Variable variable = new Variable(this);
                if (validating) {
                    validate(variable);
                }
                return variable;
            }

            protected void validate(Variable variable) {
                super.validate(variable);
                ValidationSupport.requireNonNull(variable.name, "name");
                ValidationSupport.requireValueOrChildren(variable);
            }

            protected Builder from(Variable variable) {
                super.from(variable);
                name = variable.name;
                defaultValue = variable.defaultValue;
                description = variable.description;
                expression = variable.expression;
                headerField = variable.headerField;
                hint = variable.hint;
                path = variable.path;
                sourceId = variable.sourceId;
                return this;
            }
        }
    }

    /**
     * A series of required setup operations before tests are executed.
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
             *     A setup operation or assert to perform
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
             *     A setup operation or assert to perform
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
             * The operation to perform.
             * 
             * @return
             *     An immutable object of type {@link Operation} that may be null.
             */
            public Operation getOperation() {
                return operation;
            }

            /**
             * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
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
                 * The operation to perform.
                 * 
                 * @param operation
                 *     The setup operation to perform
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(Operation operation) {
                    this.operation = operation;
                    return this;
                }

                /**
                 * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
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
             * The operation to perform.
             */
            public static class Operation extends BackboneElement {
                @Binding(
                    bindingName = "TestScriptOperationCode",
                    strength = BindingStrength.Value.EXTENSIBLE,
                    description = "The allowable operation code types.",
                    valueSet = "http://hl7.org/fhir/ValueSet/testscript-operation-codes"
                )
                private final Coding type;
                @Binding(
                    bindingName = "FHIRDefinedType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "A list of all the concrete types defined in this version of the FHIR specification - Data Types and Resource Types.",
                    valueSet = "http://hl7.org/fhir/ValueSet/defined-types|4.0.1"
                )
                private final FHIRDefinedType resource;
                private final String label;
                private final String description;
                @Binding(
                    bindingName = "MimeType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The mime type of an attachment. Any valid mime type is allowed.",
                    valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.0.1"
                )
                private final Code accept;
                @Binding(
                    bindingName = "MimeType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The mime type of an attachment. Any valid mime type is allowed.",
                    valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.0.1"
                )
                private final Code contentType;
                private final Integer destination;
                @Required
                private final Boolean encodeRequestUrl;
                @Binding(
                    bindingName = "TestScriptRequestMethodCode",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The allowable request method or HTTP operation codes.",
                    valueSet = "http://hl7.org/fhir/ValueSet/http-operations|4.0.1"
                )
                private final TestScriptRequestMethodCode method;
                private final Integer origin;
                private final String params;
                private final List<RequestHeader> requestHeader;
                private final Id requestId;
                private final Id responseId;
                private final Id sourceId;
                private final Id targetId;
                private final String url;

                private Operation(Builder builder) {
                    super(builder);
                    type = builder.type;
                    resource = builder.resource;
                    label = builder.label;
                    description = builder.description;
                    accept = builder.accept;
                    contentType = builder.contentType;
                    destination = builder.destination;
                    encodeRequestUrl = builder.encodeRequestUrl;
                    method = builder.method;
                    origin = builder.origin;
                    params = builder.params;
                    requestHeader = Collections.unmodifiableList(builder.requestHeader);
                    requestId = builder.requestId;
                    responseId = builder.responseId;
                    sourceId = builder.sourceId;
                    targetId = builder.targetId;
                    url = builder.url;
                }

                /**
                 * Server interaction or operation type.
                 * 
                 * @return
                 *     An immutable object of type {@link Coding} that may be null.
                 */
                public Coding getType() {
                    return type;
                }

                /**
                 * The type of the resource. See http://build.fhir.org/resourcelist.html.
                 * 
                 * @return
                 *     An immutable object of type {@link FHIRDefinedType} that may be null.
                 */
                public FHIRDefinedType getResource() {
                    return resource;
                }

                /**
                 * The label would be used for tracking/logging purposes by test engines.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getLabel() {
                    return label;
                }

                /**
                 * The description would be used by test engines for tracking and reporting purposes.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getDescription() {
                    return description;
                }

                /**
                 * The mime-type to use for RESTful operation in the 'Accept' header.
                 * 
                 * @return
                 *     An immutable object of type {@link Code} that may be null.
                 */
                public Code getAccept() {
                    return accept;
                }

                /**
                 * The mime-type to use for RESTful operation in the 'Content-Type' header.
                 * 
                 * @return
                 *     An immutable object of type {@link Code} that may be null.
                 */
                public Code getContentType() {
                    return contentType;
                }

                /**
                 * The server where the request message is destined for. Must be one of the server numbers listed in TestScript.
                 * destination section.
                 * 
                 * @return
                 *     An immutable object of type {@link Integer} that may be null.
                 */
                public Integer getDestination() {
                    return destination;
                }

                /**
                 * Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful 
                 * client behavior. Set to false when communicating with a server that does not support encoded url paths.
                 * 
                 * @return
                 *     An immutable object of type {@link Boolean} that is non-null.
                 */
                public Boolean getEncodeRequestUrl() {
                    return encodeRequestUrl;
                }

                /**
                 * The HTTP method the test engine MUST use for this operation regardless of any other operation details.
                 * 
                 * @return
                 *     An immutable object of type {@link TestScriptRequestMethodCode} that may be null.
                 */
                public TestScriptRequestMethodCode getMethod() {
                    return method;
                }

                /**
                 * The server where the request message originates from. Must be one of the server numbers listed in TestScript.origin 
                 * section.
                 * 
                 * @return
                 *     An immutable object of type {@link Integer} that may be null.
                 */
                public Integer getOrigin() {
                    return origin;
                }

                /**
                 * Path plus parameters after [type]. Used to set parts of the request URL explicitly.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getParams() {
                    return params;
                }

                /**
                 * Header elements would be used to set HTTP headers.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link RequestHeader} that may be empty.
                 */
                public List<RequestHeader> getRequestHeader() {
                    return requestHeader;
                }

                /**
                 * The fixture id (maybe new) to map to the request.
                 * 
                 * @return
                 *     An immutable object of type {@link Id} that may be null.
                 */
                public Id getRequestId() {
                    return requestId;
                }

                /**
                 * The fixture id (maybe new) to map to the response.
                 * 
                 * @return
                 *     An immutable object of type {@link Id} that may be null.
                 */
                public Id getResponseId() {
                    return responseId;
                }

                /**
                 * The id of the fixture used as the body of a PUT or POST request.
                 * 
                 * @return
                 *     An immutable object of type {@link Id} that may be null.
                 */
                public Id getSourceId() {
                    return sourceId;
                }

                /**
                 * Id of fixture used for extracting the [id], [type], and [vid] for GET requests.
                 * 
                 * @return
                 *     An immutable object of type {@link Id} that may be null.
                 */
                public Id getTargetId() {
                    return targetId;
                }

                /**
                 * Complete request URL.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getUrl() {
                    return url;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (type != null) || 
                        (resource != null) || 
                        (label != null) || 
                        (description != null) || 
                        (accept != null) || 
                        (contentType != null) || 
                        (destination != null) || 
                        (encodeRequestUrl != null) || 
                        (method != null) || 
                        (origin != null) || 
                        (params != null) || 
                        !requestHeader.isEmpty() || 
                        (requestId != null) || 
                        (responseId != null) || 
                        (sourceId != null) || 
                        (targetId != null) || 
                        (url != null);
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
                            accept(resource, "resource", visitor);
                            accept(label, "label", visitor);
                            accept(description, "description", visitor);
                            accept(accept, "accept", visitor);
                            accept(contentType, "contentType", visitor);
                            accept(destination, "destination", visitor);
                            accept(encodeRequestUrl, "encodeRequestUrl", visitor);
                            accept(method, "method", visitor);
                            accept(origin, "origin", visitor);
                            accept(params, "params", visitor);
                            accept(requestHeader, "requestHeader", visitor, RequestHeader.class);
                            accept(requestId, "requestId", visitor);
                            accept(responseId, "responseId", visitor);
                            accept(sourceId, "sourceId", visitor);
                            accept(targetId, "targetId", visitor);
                            accept(url, "url", visitor);
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
                        Objects.equals(type, other.type) && 
                        Objects.equals(resource, other.resource) && 
                        Objects.equals(label, other.label) && 
                        Objects.equals(description, other.description) && 
                        Objects.equals(accept, other.accept) && 
                        Objects.equals(contentType, other.contentType) && 
                        Objects.equals(destination, other.destination) && 
                        Objects.equals(encodeRequestUrl, other.encodeRequestUrl) && 
                        Objects.equals(method, other.method) && 
                        Objects.equals(origin, other.origin) && 
                        Objects.equals(params, other.params) && 
                        Objects.equals(requestHeader, other.requestHeader) && 
                        Objects.equals(requestId, other.requestId) && 
                        Objects.equals(responseId, other.responseId) && 
                        Objects.equals(sourceId, other.sourceId) && 
                        Objects.equals(targetId, other.targetId) && 
                        Objects.equals(url, other.url);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            type, 
                            resource, 
                            label, 
                            description, 
                            accept, 
                            contentType, 
                            destination, 
                            encodeRequestUrl, 
                            method, 
                            origin, 
                            params, 
                            requestHeader, 
                            requestId, 
                            responseId, 
                            sourceId, 
                            targetId, 
                            url);
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
                    private Coding type;
                    private FHIRDefinedType resource;
                    private String label;
                    private String description;
                    private Code accept;
                    private Code contentType;
                    private Integer destination;
                    private Boolean encodeRequestUrl;
                    private TestScriptRequestMethodCode method;
                    private Integer origin;
                    private String params;
                    private List<RequestHeader> requestHeader = new ArrayList<>();
                    private Id requestId;
                    private Id responseId;
                    private Id sourceId;
                    private Id targetId;
                    private String url;

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
                     * Server interaction or operation type.
                     * 
                     * @param type
                     *     The operation code type that will be executed
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder type(Coding type) {
                        this.type = type;
                        return this;
                    }

                    /**
                     * The type of the resource. See http://build.fhir.org/resourcelist.html.
                     * 
                     * @param resource
                     *     Resource type
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder resource(FHIRDefinedType resource) {
                        this.resource = resource;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code label}.
                     * 
                     * @param label
                     *     Tracking/logging operation label
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #label(com.ibm.fhir.model.type.String)
                     */
                    public Builder label(java.lang.String label) {
                        this.label = (label == null) ? null : String.of(label);
                        return this;
                    }

                    /**
                     * The label would be used for tracking/logging purposes by test engines.
                     * 
                     * @param label
                     *     Tracking/logging operation label
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder label(String label) {
                        this.label = label;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code description}.
                     * 
                     * @param description
                     *     Tracking/reporting operation description
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
                     * The description would be used by test engines for tracking and reporting purposes.
                     * 
                     * @param description
                     *     Tracking/reporting operation description
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder description(String description) {
                        this.description = description;
                        return this;
                    }

                    /**
                     * The mime-type to use for RESTful operation in the 'Accept' header.
                     * 
                     * @param accept
                     *     Mime type to accept in the payload of the response, with charset etc.
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder accept(Code accept) {
                        this.accept = accept;
                        return this;
                    }

                    /**
                     * The mime-type to use for RESTful operation in the 'Content-Type' header.
                     * 
                     * @param contentType
                     *     Mime type of the request payload contents, with charset etc.
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder contentType(Code contentType) {
                        this.contentType = contentType;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code destination}.
                     * 
                     * @param destination
                     *     Server responding to the request
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #destination(com.ibm.fhir.model.type.Integer)
                     */
                    public Builder destination(java.lang.Integer destination) {
                        this.destination = (destination == null) ? null : Integer.of(destination);
                        return this;
                    }

                    /**
                     * The server where the request message is destined for. Must be one of the server numbers listed in TestScript.
                     * destination section.
                     * 
                     * @param destination
                     *     Server responding to the request
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder destination(Integer destination) {
                        this.destination = destination;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code encodeRequestUrl}.
                     * 
                     * <p>This element is required.
                     * 
                     * @param encodeRequestUrl
                     *     Whether or not to send the request url in encoded format
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #encodeRequestUrl(com.ibm.fhir.model.type.Boolean)
                     */
                    public Builder encodeRequestUrl(java.lang.Boolean encodeRequestUrl) {
                        this.encodeRequestUrl = (encodeRequestUrl == null) ? null : Boolean.of(encodeRequestUrl);
                        return this;
                    }

                    /**
                     * Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful 
                     * client behavior. Set to false when communicating with a server that does not support encoded url paths.
                     * 
                     * <p>This element is required.
                     * 
                     * @param encodeRequestUrl
                     *     Whether or not to send the request url in encoded format
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder encodeRequestUrl(Boolean encodeRequestUrl) {
                        this.encodeRequestUrl = encodeRequestUrl;
                        return this;
                    }

                    /**
                     * The HTTP method the test engine MUST use for this operation regardless of any other operation details.
                     * 
                     * @param method
                     *     delete | get | options | patch | post | put | head
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder method(TestScriptRequestMethodCode method) {
                        this.method = method;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code origin}.
                     * 
                     * @param origin
                     *     Server initiating the request
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #origin(com.ibm.fhir.model.type.Integer)
                     */
                    public Builder origin(java.lang.Integer origin) {
                        this.origin = (origin == null) ? null : Integer.of(origin);
                        return this;
                    }

                    /**
                     * The server where the request message originates from. Must be one of the server numbers listed in TestScript.origin 
                     * section.
                     * 
                     * @param origin
                     *     Server initiating the request
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder origin(Integer origin) {
                        this.origin = origin;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code params}.
                     * 
                     * @param params
                     *     Explicitly defined path parameters
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #params(com.ibm.fhir.model.type.String)
                     */
                    public Builder params(java.lang.String params) {
                        this.params = (params == null) ? null : String.of(params);
                        return this;
                    }

                    /**
                     * Path plus parameters after [type]. Used to set parts of the request URL explicitly.
                     * 
                     * @param params
                     *     Explicitly defined path parameters
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder params(String params) {
                        this.params = params;
                        return this;
                    }

                    /**
                     * Header elements would be used to set HTTP headers.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param requestHeader
                     *     Each operation can have one or more header elements
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder requestHeader(RequestHeader... requestHeader) {
                        for (RequestHeader value : requestHeader) {
                            this.requestHeader.add(value);
                        }
                        return this;
                    }

                    /**
                     * Header elements would be used to set HTTP headers.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param requestHeader
                     *     Each operation can have one or more header elements
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder requestHeader(Collection<RequestHeader> requestHeader) {
                        this.requestHeader = new ArrayList<>(requestHeader);
                        return this;
                    }

                    /**
                     * The fixture id (maybe new) to map to the request.
                     * 
                     * @param requestId
                     *     Fixture Id of mapped request
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder requestId(Id requestId) {
                        this.requestId = requestId;
                        return this;
                    }

                    /**
                     * The fixture id (maybe new) to map to the response.
                     * 
                     * @param responseId
                     *     Fixture Id of mapped response
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder responseId(Id responseId) {
                        this.responseId = responseId;
                        return this;
                    }

                    /**
                     * The id of the fixture used as the body of a PUT or POST request.
                     * 
                     * @param sourceId
                     *     Fixture Id of body for PUT and POST requests
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder sourceId(Id sourceId) {
                        this.sourceId = sourceId;
                        return this;
                    }

                    /**
                     * Id of fixture used for extracting the [id], [type], and [vid] for GET requests.
                     * 
                     * @param targetId
                     *     Id of fixture used for extracting the [id], [type], and [vid] for GET requests
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder targetId(Id targetId) {
                        this.targetId = targetId;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code url}.
                     * 
                     * @param url
                     *     Request URL
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #url(com.ibm.fhir.model.type.String)
                     */
                    public Builder url(java.lang.String url) {
                        this.url = (url == null) ? null : String.of(url);
                        return this;
                    }

                    /**
                     * Complete request URL.
                     * 
                     * @param url
                     *     Request URL
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder url(String url) {
                        this.url = url;
                        return this;
                    }

                    /**
                     * Build the {@link Operation}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>encodeRequestUrl</li>
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
                        ValidationSupport.requireNonNull(operation.encodeRequestUrl, "encodeRequestUrl");
                        ValidationSupport.checkList(operation.requestHeader, "requestHeader", RequestHeader.class);
                        ValidationSupport.requireValueOrChildren(operation);
                    }

                    protected Builder from(Operation operation) {
                        super.from(operation);
                        type = operation.type;
                        resource = operation.resource;
                        label = operation.label;
                        description = operation.description;
                        accept = operation.accept;
                        contentType = operation.contentType;
                        destination = operation.destination;
                        encodeRequestUrl = operation.encodeRequestUrl;
                        method = operation.method;
                        origin = operation.origin;
                        params = operation.params;
                        requestHeader.addAll(operation.requestHeader);
                        requestId = operation.requestId;
                        responseId = operation.responseId;
                        sourceId = operation.sourceId;
                        targetId = operation.targetId;
                        url = operation.url;
                        return this;
                    }
                }

                /**
                 * Header elements would be used to set HTTP headers.
                 */
                public static class RequestHeader extends BackboneElement {
                    @Required
                    private final String field;
                    @Required
                    private final String value;

                    private RequestHeader(Builder builder) {
                        super(builder);
                        field = builder.field;
                        value = builder.value;
                    }

                    /**
                     * The HTTP header field e.g. "Accept".
                     * 
                     * @return
                     *     An immutable object of type {@link String} that is non-null.
                     */
                    public String getField() {
                        return field;
                    }

                    /**
                     * The value of the header e.g. "application/fhir+xml".
                     * 
                     * @return
                     *     An immutable object of type {@link String} that is non-null.
                     */
                    public String getValue() {
                        return value;
                    }

                    @Override
                    public boolean hasChildren() {
                        return super.hasChildren() || 
                            (field != null) || 
                            (value != null);
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
                                accept(field, "field", visitor);
                                accept(value, "value", visitor);
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
                        RequestHeader other = (RequestHeader) obj;
                        return Objects.equals(id, other.id) && 
                            Objects.equals(extension, other.extension) && 
                            Objects.equals(modifierExtension, other.modifierExtension) && 
                            Objects.equals(field, other.field) && 
                            Objects.equals(value, other.value);
                    }

                    @Override
                    public int hashCode() {
                        int result = hashCode;
                        if (result == 0) {
                            result = Objects.hash(id, 
                                extension, 
                                modifierExtension, 
                                field, 
                                value);
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
                        private String field;
                        private String value;

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
                         * Convenience method for setting {@code field}.
                         * 
                         * <p>This element is required.
                         * 
                         * @param field
                         *     HTTP header field name
                         * 
                         * @return
                         *     A reference to this Builder instance
                         * 
                         * @see #field(com.ibm.fhir.model.type.String)
                         */
                        public Builder field(java.lang.String field) {
                            this.field = (field == null) ? null : String.of(field);
                            return this;
                        }

                        /**
                         * The HTTP header field e.g. "Accept".
                         * 
                         * <p>This element is required.
                         * 
                         * @param field
                         *     HTTP header field name
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder field(String field) {
                            this.field = field;
                            return this;
                        }

                        /**
                         * Convenience method for setting {@code value}.
                         * 
                         * <p>This element is required.
                         * 
                         * @param value
                         *     HTTP headerfield value
                         * 
                         * @return
                         *     A reference to this Builder instance
                         * 
                         * @see #value(com.ibm.fhir.model.type.String)
                         */
                        public Builder value(java.lang.String value) {
                            this.value = (value == null) ? null : String.of(value);
                            return this;
                        }

                        /**
                         * The value of the header e.g. "application/fhir+xml".
                         * 
                         * <p>This element is required.
                         * 
                         * @param value
                         *     HTTP headerfield value
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder value(String value) {
                            this.value = value;
                            return this;
                        }

                        /**
                         * Build the {@link RequestHeader}
                         * 
                         * <p>Required elements:
                         * <ul>
                         * <li>field</li>
                         * <li>value</li>
                         * </ul>
                         * 
                         * @return
                         *     An immutable object of type {@link RequestHeader}
                         * @throws IllegalStateException
                         *     if the current state cannot be built into a valid RequestHeader per the base specification
                         */
                        @Override
                        public RequestHeader build() {
                            RequestHeader requestHeader = new RequestHeader(this);
                            if (validating) {
                                validate(requestHeader);
                            }
                            return requestHeader;
                        }

                        protected void validate(RequestHeader requestHeader) {
                            super.validate(requestHeader);
                            ValidationSupport.requireNonNull(requestHeader.field, "field");
                            ValidationSupport.requireNonNull(requestHeader.value, "value");
                            ValidationSupport.requireValueOrChildren(requestHeader);
                        }

                        protected Builder from(RequestHeader requestHeader) {
                            super.from(requestHeader);
                            field = requestHeader.field;
                            value = requestHeader.value;
                            return this;
                        }
                    }
                }
            }

            /**
             * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
             */
            public static class Assert extends BackboneElement {
                private final String label;
                private final String description;
                @Binding(
                    bindingName = "AssertionDirectionType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The type of direction to use for assertion.",
                    valueSet = "http://hl7.org/fhir/ValueSet/assert-direction-codes|4.0.1"
                )
                private final AssertionDirectionType direction;
                private final String compareToSourceId;
                private final String compareToSourceExpression;
                private final String compareToSourcePath;
                @Binding(
                    bindingName = "MimeType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The mime type of an attachment. Any valid mime type is allowed.",
                    valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.0.1"
                )
                private final Code contentType;
                private final String expression;
                private final String headerField;
                private final String minimumId;
                private final Boolean navigationLinks;
                @Binding(
                    bindingName = "AssertionOperatorType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The type of operator to use for assertion.",
                    valueSet = "http://hl7.org/fhir/ValueSet/assert-operator-codes|4.0.1"
                )
                private final AssertionOperatorType operator;
                private final String path;
                @Binding(
                    bindingName = "TestScriptRequestMethodCode",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The allowable request method or HTTP operation codes.",
                    valueSet = "http://hl7.org/fhir/ValueSet/http-operations|4.0.1"
                )
                private final TestScriptRequestMethodCode requestMethod;
                private final String requestURL;
                @Binding(
                    bindingName = "FHIRDefinedType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "A list of all the concrete types defined in this version of the FHIR specification - Data Types and Resource Types.",
                    valueSet = "http://hl7.org/fhir/ValueSet/defined-types|4.0.1"
                )
                private final FHIRDefinedType resource;
                @Binding(
                    bindingName = "AssertionResponseTypes",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The type of response code to use for assertion.",
                    valueSet = "http://hl7.org/fhir/ValueSet/assert-response-code-types|4.0.1"
                )
                private final AssertionResponseTypes response;
                private final String responseCode;
                private final Id sourceId;
                private final Id validateProfileId;
                private final String value;
                @Required
                private final Boolean warningOnly;

                private Assert(Builder builder) {
                    super(builder);
                    label = builder.label;
                    description = builder.description;
                    direction = builder.direction;
                    compareToSourceId = builder.compareToSourceId;
                    compareToSourceExpression = builder.compareToSourceExpression;
                    compareToSourcePath = builder.compareToSourcePath;
                    contentType = builder.contentType;
                    expression = builder.expression;
                    headerField = builder.headerField;
                    minimumId = builder.minimumId;
                    navigationLinks = builder.navigationLinks;
                    operator = builder.operator;
                    path = builder.path;
                    requestMethod = builder.requestMethod;
                    requestURL = builder.requestURL;
                    resource = builder.resource;
                    response = builder.response;
                    responseCode = builder.responseCode;
                    sourceId = builder.sourceId;
                    validateProfileId = builder.validateProfileId;
                    value = builder.value;
                    warningOnly = builder.warningOnly;
                }

                /**
                 * The label would be used for tracking/logging purposes by test engines.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getLabel() {
                    return label;
                }

                /**
                 * The description would be used by test engines for tracking and reporting purposes.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getDescription() {
                    return description;
                }

                /**
                 * The direction to use for the assertion.
                 * 
                 * @return
                 *     An immutable object of type {@link AssertionDirectionType} that may be null.
                 */
                public AssertionDirectionType getDirection() {
                    return direction;
                }

                /**
                 * Id of the source fixture used as the contents to be evaluated by either the "source/expression" or "sourceId/path" 
                 * definition.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getCompareToSourceId() {
                    return compareToSourceId;
                }

                /**
                 * The FHIRPath expression to evaluate against the source fixture. When compareToSourceId is defined, either 
                 * compareToSourceExpression or compareToSourcePath must be defined, but not both.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getCompareToSourceExpression() {
                    return compareToSourceExpression;
                }

                /**
                 * XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either 
                 * compareToSourceExpression or compareToSourcePath must be defined, but not both.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getCompareToSourcePath() {
                    return compareToSourcePath;
                }

                /**
                 * The mime-type contents to compare against the request or response message 'Content-Type' header.
                 * 
                 * @return
                 *     An immutable object of type {@link Code} that may be null.
                 */
                public Code getContentType() {
                    return contentType;
                }

                /**
                 * The FHIRPath expression to be evaluated against the request or response message contents - HTTP headers and payload.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getExpression() {
                    return expression;
                }

                /**
                 * The HTTP header field name e.g. 'Location'.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getHeaderField() {
                    return headerField;
                }

                /**
                 * The ID of a fixture. Asserts that the response contains at a minimum the fixture specified by minimumId.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getMinimumId() {
                    return minimumId;
                }

                /**
                 * Whether or not the test execution performs validation on the bundle navigation links.
                 * 
                 * @return
                 *     An immutable object of type {@link Boolean} that may be null.
                 */
                public Boolean getNavigationLinks() {
                    return navigationLinks;
                }

                /**
                 * The operator type defines the conditional behavior of the assert. If not defined, the default is equals.
                 * 
                 * @return
                 *     An immutable object of type {@link AssertionOperatorType} that may be null.
                 */
                public AssertionOperatorType getOperator() {
                    return operator;
                }

                /**
                 * The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getPath() {
                    return path;
                }

                /**
                 * The request method or HTTP operation code to compare against that used by the client system under test.
                 * 
                 * @return
                 *     An immutable object of type {@link TestScriptRequestMethodCode} that may be null.
                 */
                public TestScriptRequestMethodCode getRequestMethod() {
                    return requestMethod;
                }

                /**
                 * The value to use in a comparison against the request URL path string.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getRequestURL() {
                    return requestURL;
                }

                /**
                 * The type of the resource. See http://build.fhir.org/resourcelist.html.
                 * 
                 * @return
                 *     An immutable object of type {@link FHIRDefinedType} that may be null.
                 */
                public FHIRDefinedType getResource() {
                    return resource;
                }

                /**
                 * okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | 
                 * preconditionFailed | unprocessable.
                 * 
                 * @return
                 *     An immutable object of type {@link AssertionResponseTypes} that may be null.
                 */
                public AssertionResponseTypes getResponse() {
                    return response;
                }

                /**
                 * The value of the HTTP response code to be tested.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getResponseCode() {
                    return responseCode;
                }

                /**
                 * Fixture to evaluate the XPath/JSONPath expression or the headerField against.
                 * 
                 * @return
                 *     An immutable object of type {@link Id} that may be null.
                 */
                public Id getSourceId() {
                    return sourceId;
                }

                /**
                 * The ID of the Profile to validate against.
                 * 
                 * @return
                 *     An immutable object of type {@link Id} that may be null.
                 */
                public Id getValidateProfileId() {
                    return validateProfileId;
                }

                /**
                 * The value to compare to.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Whether or not the test execution will produce a warning only on error for this assert.
                 * 
                 * @return
                 *     An immutable object of type {@link Boolean} that is non-null.
                 */
                public Boolean getWarningOnly() {
                    return warningOnly;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (label != null) || 
                        (description != null) || 
                        (direction != null) || 
                        (compareToSourceId != null) || 
                        (compareToSourceExpression != null) || 
                        (compareToSourcePath != null) || 
                        (contentType != null) || 
                        (expression != null) || 
                        (headerField != null) || 
                        (minimumId != null) || 
                        (navigationLinks != null) || 
                        (operator != null) || 
                        (path != null) || 
                        (requestMethod != null) || 
                        (requestURL != null) || 
                        (resource != null) || 
                        (response != null) || 
                        (responseCode != null) || 
                        (sourceId != null) || 
                        (validateProfileId != null) || 
                        (value != null) || 
                        (warningOnly != null);
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
                            accept(label, "label", visitor);
                            accept(description, "description", visitor);
                            accept(direction, "direction", visitor);
                            accept(compareToSourceId, "compareToSourceId", visitor);
                            accept(compareToSourceExpression, "compareToSourceExpression", visitor);
                            accept(compareToSourcePath, "compareToSourcePath", visitor);
                            accept(contentType, "contentType", visitor);
                            accept(expression, "expression", visitor);
                            accept(headerField, "headerField", visitor);
                            accept(minimumId, "minimumId", visitor);
                            accept(navigationLinks, "navigationLinks", visitor);
                            accept(operator, "operator", visitor);
                            accept(path, "path", visitor);
                            accept(requestMethod, "requestMethod", visitor);
                            accept(requestURL, "requestURL", visitor);
                            accept(resource, "resource", visitor);
                            accept(response, "response", visitor);
                            accept(responseCode, "responseCode", visitor);
                            accept(sourceId, "sourceId", visitor);
                            accept(validateProfileId, "validateProfileId", visitor);
                            accept(value, "value", visitor);
                            accept(warningOnly, "warningOnly", visitor);
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
                        Objects.equals(label, other.label) && 
                        Objects.equals(description, other.description) && 
                        Objects.equals(direction, other.direction) && 
                        Objects.equals(compareToSourceId, other.compareToSourceId) && 
                        Objects.equals(compareToSourceExpression, other.compareToSourceExpression) && 
                        Objects.equals(compareToSourcePath, other.compareToSourcePath) && 
                        Objects.equals(contentType, other.contentType) && 
                        Objects.equals(expression, other.expression) && 
                        Objects.equals(headerField, other.headerField) && 
                        Objects.equals(minimumId, other.minimumId) && 
                        Objects.equals(navigationLinks, other.navigationLinks) && 
                        Objects.equals(operator, other.operator) && 
                        Objects.equals(path, other.path) && 
                        Objects.equals(requestMethod, other.requestMethod) && 
                        Objects.equals(requestURL, other.requestURL) && 
                        Objects.equals(resource, other.resource) && 
                        Objects.equals(response, other.response) && 
                        Objects.equals(responseCode, other.responseCode) && 
                        Objects.equals(sourceId, other.sourceId) && 
                        Objects.equals(validateProfileId, other.validateProfileId) && 
                        Objects.equals(value, other.value) && 
                        Objects.equals(warningOnly, other.warningOnly);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            label, 
                            description, 
                            direction, 
                            compareToSourceId, 
                            compareToSourceExpression, 
                            compareToSourcePath, 
                            contentType, 
                            expression, 
                            headerField, 
                            minimumId, 
                            navigationLinks, 
                            operator, 
                            path, 
                            requestMethod, 
                            requestURL, 
                            resource, 
                            response, 
                            responseCode, 
                            sourceId, 
                            validateProfileId, 
                            value, 
                            warningOnly);
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
                    private String label;
                    private String description;
                    private AssertionDirectionType direction;
                    private String compareToSourceId;
                    private String compareToSourceExpression;
                    private String compareToSourcePath;
                    private Code contentType;
                    private String expression;
                    private String headerField;
                    private String minimumId;
                    private Boolean navigationLinks;
                    private AssertionOperatorType operator;
                    private String path;
                    private TestScriptRequestMethodCode requestMethod;
                    private String requestURL;
                    private FHIRDefinedType resource;
                    private AssertionResponseTypes response;
                    private String responseCode;
                    private Id sourceId;
                    private Id validateProfileId;
                    private String value;
                    private Boolean warningOnly;

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
                     * Convenience method for setting {@code label}.
                     * 
                     * @param label
                     *     Tracking/logging assertion label
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #label(com.ibm.fhir.model.type.String)
                     */
                    public Builder label(java.lang.String label) {
                        this.label = (label == null) ? null : String.of(label);
                        return this;
                    }

                    /**
                     * The label would be used for tracking/logging purposes by test engines.
                     * 
                     * @param label
                     *     Tracking/logging assertion label
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder label(String label) {
                        this.label = label;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code description}.
                     * 
                     * @param description
                     *     Tracking/reporting assertion description
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
                     * The description would be used by test engines for tracking and reporting purposes.
                     * 
                     * @param description
                     *     Tracking/reporting assertion description
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder description(String description) {
                        this.description = description;
                        return this;
                    }

                    /**
                     * The direction to use for the assertion.
                     * 
                     * @param direction
                     *     response | request
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder direction(AssertionDirectionType direction) {
                        this.direction = direction;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code compareToSourceId}.
                     * 
                     * @param compareToSourceId
                     *     Id of the source fixture to be evaluated
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #compareToSourceId(com.ibm.fhir.model.type.String)
                     */
                    public Builder compareToSourceId(java.lang.String compareToSourceId) {
                        this.compareToSourceId = (compareToSourceId == null) ? null : String.of(compareToSourceId);
                        return this;
                    }

                    /**
                     * Id of the source fixture used as the contents to be evaluated by either the "source/expression" or "sourceId/path" 
                     * definition.
                     * 
                     * @param compareToSourceId
                     *     Id of the source fixture to be evaluated
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder compareToSourceId(String compareToSourceId) {
                        this.compareToSourceId = compareToSourceId;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code compareToSourceExpression}.
                     * 
                     * @param compareToSourceExpression
                     *     The FHIRPath expression to evaluate against the source fixture
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #compareToSourceExpression(com.ibm.fhir.model.type.String)
                     */
                    public Builder compareToSourceExpression(java.lang.String compareToSourceExpression) {
                        this.compareToSourceExpression = (compareToSourceExpression == null) ? null : String.of(compareToSourceExpression);
                        return this;
                    }

                    /**
                     * The FHIRPath expression to evaluate against the source fixture. When compareToSourceId is defined, either 
                     * compareToSourceExpression or compareToSourcePath must be defined, but not both.
                     * 
                     * @param compareToSourceExpression
                     *     The FHIRPath expression to evaluate against the source fixture
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder compareToSourceExpression(String compareToSourceExpression) {
                        this.compareToSourceExpression = compareToSourceExpression;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code compareToSourcePath}.
                     * 
                     * @param compareToSourcePath
                     *     XPath or JSONPath expression to evaluate against the source fixture
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #compareToSourcePath(com.ibm.fhir.model.type.String)
                     */
                    public Builder compareToSourcePath(java.lang.String compareToSourcePath) {
                        this.compareToSourcePath = (compareToSourcePath == null) ? null : String.of(compareToSourcePath);
                        return this;
                    }

                    /**
                     * XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either 
                     * compareToSourceExpression or compareToSourcePath must be defined, but not both.
                     * 
                     * @param compareToSourcePath
                     *     XPath or JSONPath expression to evaluate against the source fixture
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder compareToSourcePath(String compareToSourcePath) {
                        this.compareToSourcePath = compareToSourcePath;
                        return this;
                    }

                    /**
                     * The mime-type contents to compare against the request or response message 'Content-Type' header.
                     * 
                     * @param contentType
                     *     Mime type to compare against the 'Content-Type' header
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder contentType(Code contentType) {
                        this.contentType = contentType;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code expression}.
                     * 
                     * @param expression
                     *     The FHIRPath expression to be evaluated
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #expression(com.ibm.fhir.model.type.String)
                     */
                    public Builder expression(java.lang.String expression) {
                        this.expression = (expression == null) ? null : String.of(expression);
                        return this;
                    }

                    /**
                     * The FHIRPath expression to be evaluated against the request or response message contents - HTTP headers and payload.
                     * 
                     * @param expression
                     *     The FHIRPath expression to be evaluated
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder expression(String expression) {
                        this.expression = expression;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code headerField}.
                     * 
                     * @param headerField
                     *     HTTP header field name
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #headerField(com.ibm.fhir.model.type.String)
                     */
                    public Builder headerField(java.lang.String headerField) {
                        this.headerField = (headerField == null) ? null : String.of(headerField);
                        return this;
                    }

                    /**
                     * The HTTP header field name e.g. 'Location'.
                     * 
                     * @param headerField
                     *     HTTP header field name
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder headerField(String headerField) {
                        this.headerField = headerField;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code minimumId}.
                     * 
                     * @param minimumId
                     *     Fixture Id of minimum content resource
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #minimumId(com.ibm.fhir.model.type.String)
                     */
                    public Builder minimumId(java.lang.String minimumId) {
                        this.minimumId = (minimumId == null) ? null : String.of(minimumId);
                        return this;
                    }

                    /**
                     * The ID of a fixture. Asserts that the response contains at a minimum the fixture specified by minimumId.
                     * 
                     * @param minimumId
                     *     Fixture Id of minimum content resource
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder minimumId(String minimumId) {
                        this.minimumId = minimumId;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code navigationLinks}.
                     * 
                     * @param navigationLinks
                     *     Perform validation on navigation links?
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #navigationLinks(com.ibm.fhir.model.type.Boolean)
                     */
                    public Builder navigationLinks(java.lang.Boolean navigationLinks) {
                        this.navigationLinks = (navigationLinks == null) ? null : Boolean.of(navigationLinks);
                        return this;
                    }

                    /**
                     * Whether or not the test execution performs validation on the bundle navigation links.
                     * 
                     * @param navigationLinks
                     *     Perform validation on navigation links?
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder navigationLinks(Boolean navigationLinks) {
                        this.navigationLinks = navigationLinks;
                        return this;
                    }

                    /**
                     * The operator type defines the conditional behavior of the assert. If not defined, the default is equals.
                     * 
                     * @param operator
                     *     equals | notEquals | in | notIn | greaterThan | lessThan | empty | notEmpty | contains | notContains | eval
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder operator(AssertionOperatorType operator) {
                        this.operator = operator;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code path}.
                     * 
                     * @param path
                     *     XPath or JSONPath expression
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #path(com.ibm.fhir.model.type.String)
                     */
                    public Builder path(java.lang.String path) {
                        this.path = (path == null) ? null : String.of(path);
                        return this;
                    }

                    /**
                     * The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.
                     * 
                     * @param path
                     *     XPath or JSONPath expression
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder path(String path) {
                        this.path = path;
                        return this;
                    }

                    /**
                     * The request method or HTTP operation code to compare against that used by the client system under test.
                     * 
                     * @param requestMethod
                     *     delete | get | options | patch | post | put | head
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder requestMethod(TestScriptRequestMethodCode requestMethod) {
                        this.requestMethod = requestMethod;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code requestURL}.
                     * 
                     * @param requestURL
                     *     Request URL comparison value
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #requestURL(com.ibm.fhir.model.type.String)
                     */
                    public Builder requestURL(java.lang.String requestURL) {
                        this.requestURL = (requestURL == null) ? null : String.of(requestURL);
                        return this;
                    }

                    /**
                     * The value to use in a comparison against the request URL path string.
                     * 
                     * @param requestURL
                     *     Request URL comparison value
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder requestURL(String requestURL) {
                        this.requestURL = requestURL;
                        return this;
                    }

                    /**
                     * The type of the resource. See http://build.fhir.org/resourcelist.html.
                     * 
                     * @param resource
                     *     Resource type
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder resource(FHIRDefinedType resource) {
                        this.resource = resource;
                        return this;
                    }

                    /**
                     * okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | 
                     * preconditionFailed | unprocessable.
                     * 
                     * @param response
                     *     okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | 
                     *     preconditionFailed | unprocessable
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder response(AssertionResponseTypes response) {
                        this.response = response;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code responseCode}.
                     * 
                     * @param responseCode
                     *     HTTP response code to test
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #responseCode(com.ibm.fhir.model.type.String)
                     */
                    public Builder responseCode(java.lang.String responseCode) {
                        this.responseCode = (responseCode == null) ? null : String.of(responseCode);
                        return this;
                    }

                    /**
                     * The value of the HTTP response code to be tested.
                     * 
                     * @param responseCode
                     *     HTTP response code to test
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder responseCode(String responseCode) {
                        this.responseCode = responseCode;
                        return this;
                    }

                    /**
                     * Fixture to evaluate the XPath/JSONPath expression or the headerField against.
                     * 
                     * @param sourceId
                     *     Fixture Id of source expression or headerField
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder sourceId(Id sourceId) {
                        this.sourceId = sourceId;
                        return this;
                    }

                    /**
                     * The ID of the Profile to validate against.
                     * 
                     * @param validateProfileId
                     *     Profile Id of validation profile reference
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder validateProfileId(Id validateProfileId) {
                        this.validateProfileId = validateProfileId;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code value}.
                     * 
                     * @param value
                     *     The value to compare to
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #value(com.ibm.fhir.model.type.String)
                     */
                    public Builder value(java.lang.String value) {
                        this.value = (value == null) ? null : String.of(value);
                        return this;
                    }

                    /**
                     * The value to compare to.
                     * 
                     * @param value
                     *     The value to compare to
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder value(String value) {
                        this.value = value;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code warningOnly}.
                     * 
                     * <p>This element is required.
                     * 
                     * @param warningOnly
                     *     Will this assert produce a warning only on error?
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #warningOnly(com.ibm.fhir.model.type.Boolean)
                     */
                    public Builder warningOnly(java.lang.Boolean warningOnly) {
                        this.warningOnly = (warningOnly == null) ? null : Boolean.of(warningOnly);
                        return this;
                    }

                    /**
                     * Whether or not the test execution will produce a warning only on error for this assert.
                     * 
                     * <p>This element is required.
                     * 
                     * @param warningOnly
                     *     Will this assert produce a warning only on error?
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder warningOnly(Boolean warningOnly) {
                        this.warningOnly = warningOnly;
                        return this;
                    }

                    /**
                     * Build the {@link Assert}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>warningOnly</li>
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
                        ValidationSupport.requireNonNull(_assert.warningOnly, "warningOnly");
                        ValidationSupport.requireValueOrChildren(_assert);
                    }

                    protected Builder from(Assert _assert) {
                        super.from(_assert);
                        label = _assert.label;
                        description = _assert.description;
                        direction = _assert.direction;
                        compareToSourceId = _assert.compareToSourceId;
                        compareToSourceExpression = _assert.compareToSourceExpression;
                        compareToSourcePath = _assert.compareToSourcePath;
                        contentType = _assert.contentType;
                        expression = _assert.expression;
                        headerField = _assert.headerField;
                        minimumId = _assert.minimumId;
                        navigationLinks = _assert.navigationLinks;
                        operator = _assert.operator;
                        path = _assert.path;
                        requestMethod = _assert.requestMethod;
                        requestURL = _assert.requestURL;
                        resource = _assert.resource;
                        response = _assert.response;
                        responseCode = _assert.responseCode;
                        sourceId = _assert.sourceId;
                        validateProfileId = _assert.validateProfileId;
                        value = _assert.value;
                        warningOnly = _assert.warningOnly;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * A test in this script.
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
             *     A test operation or assert to perform
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
             *     A test operation or assert to perform
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
            private final TestScript.Setup.Action.Operation operation;
            private final TestScript.Setup.Action.Assert _assert;

            private Action(Builder builder) {
                super(builder);
                operation = builder.operation;
                _assert = builder._assert;
            }

            /**
             * An operation would involve a REST request to a server.
             * 
             * @return
             *     An immutable object of type {@link TestScript.Setup.Action.Operation} that may be null.
             */
            public TestScript.Setup.Action.Operation getOperation() {
                return operation;
            }

            /**
             * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
             * 
             * @return
             *     An immutable object of type {@link TestScript.Setup.Action.Assert} that may be null.
             */
            public TestScript.Setup.Action.Assert getAssert() {
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
                private TestScript.Setup.Action.Operation operation;
                private TestScript.Setup.Action.Assert _assert;

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
                 *     The setup operation to perform
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(TestScript.Setup.Action.Operation operation) {
                    this.operation = operation;
                    return this;
                }

                /**
                 * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
                 * 
                 * @param _assert
                 *     The setup assertion to perform
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder _assert(TestScript.Setup.Action.Assert _assert) {
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
     * A series of operations required to clean up after all the tests are executed (successfully or otherwise).
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
             *     One or more teardown operations to perform
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
             *     One or more teardown operations to perform
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
            private final TestScript.Setup.Action.Operation operation;

            private Action(Builder builder) {
                super(builder);
                operation = builder.operation;
            }

            /**
             * An operation would involve a REST request to a server.
             * 
             * @return
             *     An immutable object of type {@link TestScript.Setup.Action.Operation} that is non-null.
             */
            public TestScript.Setup.Action.Operation getOperation() {
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
                private TestScript.Setup.Action.Operation operation;

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
                 *     The teardown operation to perform
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(TestScript.Setup.Action.Operation operation) {
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
