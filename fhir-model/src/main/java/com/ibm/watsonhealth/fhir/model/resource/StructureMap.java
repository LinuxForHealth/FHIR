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
import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Contributor;
import com.ibm.watsonhealth.fhir.model.type.Count;
import com.ibm.watsonhealth.fhir.model.type.DataRequirement;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Distance;
import com.ibm.watsonhealth.fhir.model.type.Dosage;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Expression;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Oid;
import com.ibm.watsonhealth.fhir.model.type.ParameterDefinition;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.SampledData;
import com.ibm.watsonhealth.fhir.model.type.Signature;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.StructureMapContextType;
import com.ibm.watsonhealth.fhir.model.type.StructureMapGroupTypeMode;
import com.ibm.watsonhealth.fhir.model.type.StructureMapInputMode;
import com.ibm.watsonhealth.fhir.model.type.StructureMapModelMode;
import com.ibm.watsonhealth.fhir.model.type.StructureMapSourceListMode;
import com.ibm.watsonhealth.fhir.model.type.StructureMapTargetListMode;
import com.ibm.watsonhealth.fhir.model.type.StructureMapTransform;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.TriggerDefinition;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.type.Uuid;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A Map of relationships between 2 structures that can be used to transform data.
 * </p>
 */
@Constraint(
    id = "smp-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    id = "smp-1",
    level = "Rule",
    location = "StructureMap.group.rule.target",
    description = "Can only have an element if you have a context",
    expression = "element.exists() implies context.exists()"
)
@Constraint(
    id = "smp-2",
    level = "Rule",
    location = "StructureMap.group.rule.target",
    description = "Must have a contextType if you have a context",
    expression = "context.exists() implies contextType.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class StructureMap extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String name;
    private final String title;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final Markdown copyright;
    private final List<Structure> structure;
    private final List<Canonical> _import;
    private final List<Group> group;

    private volatile int hashCode;

    private StructureMap(Builder builder) {
        super(builder);
        url = ValidationSupport.requireNonNull(builder.url, "url");
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = ValidationSupport.requireNonNull(builder.name, "name");
        title = builder.title;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        copyright = builder.copyright;
        structure = Collections.unmodifiableList(builder.structure);
        _import = Collections.unmodifiableList(builder._import);
        group = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.group, "group"));
    }

    /**
     * <p>
     * An absolute URI that is used to identify this structure map when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this structure map is (or will be) published. This URL can be the target 
     * of a canonical reference. It SHALL remain the same when the structure map is stored on different servers.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * <p>
     * A formal identifier that is used to identify this structure map when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the structure map when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the structure map author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * A natural language name identifying the structure map. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for the structure map.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>
     * The status of this structure map. Enables tracking the life-cycle of the content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus}.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A Boolean value to indicate that this structure map is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * <p>
     * The date (and optionally time) when the structure map was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the structure map changes.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * <p>
     * The name of the organization or individual that published the structure map.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * <p>
     * Contact details to assist a user in finding and communicating with the publisher.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * <p>
     * A free text natural language description of the structure map from a consumer's perspective.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * <p>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate structure map instances.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext}.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * <p>
     * A legal or geographic region in which the structure map is intended to be used.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * <p>
     * Explanation of why this structure map is needed and why it has been designed as it has.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * <p>
     * A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the structure map.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * <p>
     * A structure definition used by this map. The structure definition may describe instances that are converted, or the 
     * instances that are produced.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Structure}.
     */
    public List<Structure> getStructure() {
        return structure;
    }

    /**
     * <p>
     * Other maps used by this map (canonical URLs).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getimport() {
        return _import;
    }

    /**
     * <p>
     * Organizes the mapping into manageable chunks for human review/ease of maintenance.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Group}.
     */
    public List<Group> getGroup() {
        return group;
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
                accept(url, "url", visitor);
                accept(identifier, "identifier", visitor, Identifier.class);
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
                accept(structure, "structure", visitor, Structure.class);
                accept(_import, "import", visitor, Canonical.class);
                accept(group, "group", visitor, Group.class);
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
        StructureMap other = (StructureMap) obj;
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
            Objects.equals(structure, other.structure) && 
            Objects.equals(_import, other._import) && 
            Objects.equals(group, other.group);
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
                structure, 
                _import, 
                group);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(url, name, status, group).from(this);
    }

    public Builder toBuilder(Uri url, String name, PublicationStatus status, List<Group> group) {
        return new Builder(url, name, status, group).from(this);
    }

    public static Builder builder(Uri url, String name, PublicationStatus status, List<Group> group) {
        return new Builder(url, name, status, group);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Uri url;
        private final String name;
        private final PublicationStatus status;
        private final List<Group> group;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String title;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private List<Structure> structure = new ArrayList<>();
        private List<Canonical> _import = new ArrayList<>();

        private Builder(Uri url, String name, PublicationStatus status, List<Group> group) {
            super();
            this.url = url;
            this.name = name;
            this.status = status;
            this.group = group;
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
         * A formal identifier that is used to identify this structure map when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the structure map
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
         * A formal identifier that is used to identify this structure map when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the structure map
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
         * The identifier that is used to identify this version of the structure map when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the structure map author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the structure map
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * <p>
         * A short, descriptive, user-friendly title for the structure map.
         * </p>
         * 
         * @param title
         *     Name for this structure map (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * <p>
         * A Boolean value to indicate that this structure map is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
         * </p>
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
         * <p>
         * The date (and optionally time) when the structure map was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the structure map changes.
         * </p>
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
         * <p>
         * The name of the organization or individual that published the structure map.
         * </p>
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
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * <p>
         * A free text natural language description of the structure map from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the structure map
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate structure map instances.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate structure map instances.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the structure map is intended to be used.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for structure map (if applicable)
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
         * <p>
         * A legal or geographic region in which the structure map is intended to be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for structure map (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * <p>
         * Explanation of why this structure map is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this structure map is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * <p>
         * A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the structure map.
         * </p>
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
         * <p>
         * A structure definition used by this map. The structure definition may describe instances that are converted, or the 
         * instances that are produced.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param structure
         *     Structure Definition used by this map
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder structure(Structure... structure) {
            for (Structure value : structure) {
                this.structure.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A structure definition used by this map. The structure definition may describe instances that are converted, or the 
         * instances that are produced.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param structure
         *     Structure Definition used by this map
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder structure(Collection<Structure> structure) {
            this.structure = new ArrayList<>(structure);
            return this;
        }

        /**
         * <p>
         * Other maps used by this map (canonical URLs).
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param _import
         *     Other maps used by this map (canonical URLs)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _import(Canonical... _import) {
            for (Canonical value : _import) {
                this._import.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Other maps used by this map (canonical URLs).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param _import
         *     Other maps used by this map (canonical URLs)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _import(Collection<Canonical> _import) {
            this._import = new ArrayList<>(_import);
            return this;
        }

        @Override
        public StructureMap build() {
            return new StructureMap(this);
        }

        private Builder from(StructureMap structureMap) {
            id = structureMap.id;
            meta = structureMap.meta;
            implicitRules = structureMap.implicitRules;
            language = structureMap.language;
            text = structureMap.text;
            contained.addAll(structureMap.contained);
            extension.addAll(structureMap.extension);
            modifierExtension.addAll(structureMap.modifierExtension);
            identifier.addAll(structureMap.identifier);
            version = structureMap.version;
            title = structureMap.title;
            experimental = structureMap.experimental;
            date = structureMap.date;
            publisher = structureMap.publisher;
            contact.addAll(structureMap.contact);
            description = structureMap.description;
            useContext.addAll(structureMap.useContext);
            jurisdiction.addAll(structureMap.jurisdiction);
            purpose = structureMap.purpose;
            copyright = structureMap.copyright;
            structure.addAll(structureMap.structure);
            _import.addAll(structureMap._import);
            return this;
        }
    }

    /**
     * <p>
     * A structure definition used by this map. The structure definition may describe instances that are converted, or the 
     * instances that are produced.
     * </p>
     */
    public static class Structure extends BackboneElement {
        private final Canonical url;
        private final StructureMapModelMode mode;
        private final String alias;
        private final String documentation;

        private volatile int hashCode;

        private Structure(Builder builder) {
            super(builder);
            url = ValidationSupport.requireNonNull(builder.url, "url");
            mode = ValidationSupport.requireNonNull(builder.mode, "mode");
            alias = builder.alias;
            documentation = builder.documentation;
            if (!hasValue() && !hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * The canonical reference to the structure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getUrl() {
            return url;
        }

        /**
         * <p>
         * How the referenced structure is used in this mapping.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link StructureMapModelMode}.
         */
        public StructureMapModelMode getMode() {
            return mode;
        }

        /**
         * <p>
         * The name used for this type in the map.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getAlias() {
            return alias;
        }

        /**
         * <p>
         * Documentation that describes how the structure is used in the mapping.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDocumentation() {
            return documentation;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (url != null) || 
                (mode != null) || 
                (alias != null) || 
                (documentation != null);
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
                    accept(url, "url", visitor);
                    accept(mode, "mode", visitor);
                    accept(alias, "alias", visitor);
                    accept(documentation, "documentation", visitor);
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
            Structure other = (Structure) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(url, other.url) && 
                Objects.equals(mode, other.mode) && 
                Objects.equals(alias, other.alias) && 
                Objects.equals(documentation, other.documentation);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    url, 
                    mode, 
                    alias, 
                    documentation);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(url, mode).from(this);
        }

        public Builder toBuilder(Canonical url, StructureMapModelMode mode) {
            return new Builder(url, mode).from(this);
        }

        public static Builder builder(Canonical url, StructureMapModelMode mode) {
            return new Builder(url, mode);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Canonical url;
            private final StructureMapModelMode mode;

            // optional
            private String alias;
            private String documentation;

            private Builder(Canonical url, StructureMapModelMode mode) {
                super();
                this.url = url;
                this.mode = mode;
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
             * The name used for this type in the map.
             * </p>
             * 
             * @param alias
             *     Name for type in this map
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder alias(String alias) {
                this.alias = alias;
                return this;
            }

            /**
             * <p>
             * Documentation that describes how the structure is used in the mapping.
             * </p>
             * 
             * @param documentation
             *     Documentation on use of structure
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(String documentation) {
                this.documentation = documentation;
                return this;
            }

            @Override
            public Structure build() {
                return new Structure(this);
            }

            private Builder from(Structure structure) {
                id = structure.id;
                extension.addAll(structure.extension);
                modifierExtension.addAll(structure.modifierExtension);
                alias = structure.alias;
                documentation = structure.documentation;
                return this;
            }
        }
    }

    /**
     * <p>
     * Organizes the mapping into manageable chunks for human review/ease of maintenance.
     * </p>
     */
    public static class Group extends BackboneElement {
        private final Id name;
        private final Id _extends;
        private final StructureMapGroupTypeMode typeMode;
        private final String documentation;
        private final List<Input> input;
        private final List<Rule> rule;

        private volatile int hashCode;

        private Group(Builder builder) {
            super(builder);
            name = ValidationSupport.requireNonNull(builder.name, "name");
            _extends = builder._extends;
            typeMode = ValidationSupport.requireNonNull(builder.typeMode, "typeMode");
            documentation = builder.documentation;
            input = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.input, "input"));
            rule = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.rule, "rule"));
            if (!hasValue() && !hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * A unique name for the group for the convenience of human readers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Id}.
         */
        public Id getName() {
            return name;
        }

        /**
         * <p>
         * Another group that this group adds rules to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Id}.
         */
        public Id getextends() {
            return _extends;
        }

        /**
         * <p>
         * If this is the default rule set to apply for the source type or this combination of types.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link StructureMapGroupTypeMode}.
         */
        public StructureMapGroupTypeMode getTypeMode() {
            return typeMode;
        }

        /**
         * <p>
         * Additional supporting documentation that explains the purpose of the group and the types of mappings within it.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDocumentation() {
            return documentation;
        }

        /**
         * <p>
         * A name assigned to an instance of data. The instance must be provided when the mapping is invoked.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Input}.
         */
        public List<Input> getInput() {
            return input;
        }

        /**
         * <p>
         * Transform Rule from source to target.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Rule}.
         */
        public List<Rule> getRule() {
            return rule;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (_extends != null) || 
                (typeMode != null) || 
                (documentation != null) || 
                !input.isEmpty() || 
                !rule.isEmpty();
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
                    accept(_extends, "extends", visitor);
                    accept(typeMode, "typeMode", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(input, "input", visitor, Input.class);
                    accept(rule, "rule", visitor, Rule.class);
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
            Group other = (Group) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(_extends, other._extends) && 
                Objects.equals(typeMode, other.typeMode) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(input, other.input) && 
                Objects.equals(rule, other.rule);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    _extends, 
                    typeMode, 
                    documentation, 
                    input, 
                    rule);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(name, typeMode, input, rule).from(this);
        }

        public Builder toBuilder(Id name, StructureMapGroupTypeMode typeMode, List<Input> input, List<Rule> rule) {
            return new Builder(name, typeMode, input, rule).from(this);
        }

        public static Builder builder(Id name, StructureMapGroupTypeMode typeMode, List<Input> input, List<Rule> rule) {
            return new Builder(name, typeMode, input, rule);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Id name;
            private final StructureMapGroupTypeMode typeMode;
            private final List<Input> input;
            private final List<Rule> rule;

            // optional
            private Id _extends;
            private String documentation;

            private Builder(Id name, StructureMapGroupTypeMode typeMode, List<Input> input, List<Rule> rule) {
                super();
                this.name = name;
                this.typeMode = typeMode;
                this.input = input;
                this.rule = rule;
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
             * Another group that this group adds rules to.
             * </p>
             * 
             * @param _extends
             *     Another group that this group adds rules to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder _extends(Id _extends) {
                this._extends = _extends;
                return this;
            }

            /**
             * <p>
             * Additional supporting documentation that explains the purpose of the group and the types of mappings within it.
             * </p>
             * 
             * @param documentation
             *     Additional description/explanation for group
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(String documentation) {
                this.documentation = documentation;
                return this;
            }

            @Override
            public Group build() {
                return new Group(this);
            }

            private Builder from(Group group) {
                id = group.id;
                extension.addAll(group.extension);
                modifierExtension.addAll(group.modifierExtension);
                _extends = group._extends;
                documentation = group.documentation;
                return this;
            }
        }

        /**
         * <p>
         * A name assigned to an instance of data. The instance must be provided when the mapping is invoked.
         * </p>
         */
        public static class Input extends BackboneElement {
            private final Id name;
            private final String type;
            private final StructureMapInputMode mode;
            private final String documentation;

            private volatile int hashCode;

            private Input(Builder builder) {
                super(builder);
                name = ValidationSupport.requireNonNull(builder.name, "name");
                type = builder.type;
                mode = ValidationSupport.requireNonNull(builder.mode, "mode");
                documentation = builder.documentation;
                if (!hasValue() && !hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * Name for this instance of data.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Id}.
             */
            public Id getName() {
                return name;
            }

            /**
             * <p>
             * Type for this instance of data.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getType() {
                return type;
            }

            /**
             * <p>
             * Mode for this instance of data.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link StructureMapInputMode}.
             */
            public StructureMapInputMode getMode() {
                return mode;
            }

            /**
             * <p>
             * Documentation for this instance of data.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getDocumentation() {
                return documentation;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (name != null) || 
                    (type != null) || 
                    (mode != null) || 
                    (documentation != null);
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
                        accept(type, "type", visitor);
                        accept(mode, "mode", visitor);
                        accept(documentation, "documentation", visitor);
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
                Input other = (Input) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(mode, other.mode) && 
                    Objects.equals(documentation, other.documentation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        name, 
                        type, 
                        mode, 
                        documentation);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(name, mode).from(this);
            }

            public Builder toBuilder(Id name, StructureMapInputMode mode) {
                return new Builder(name, mode).from(this);
            }

            public static Builder builder(Id name, StructureMapInputMode mode) {
                return new Builder(name, mode);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Id name;
                private final StructureMapInputMode mode;

                // optional
                private String type;
                private String documentation;

                private Builder(Id name, StructureMapInputMode mode) {
                    super();
                    this.name = name;
                    this.mode = mode;
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
                 * Type for this instance of data.
                 * </p>
                 * 
                 * @param type
                 *     Type for this instance of data
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(String type) {
                    this.type = type;
                    return this;
                }

                /**
                 * <p>
                 * Documentation for this instance of data.
                 * </p>
                 * 
                 * @param documentation
                 *     Documentation for this instance of data
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder documentation(String documentation) {
                    this.documentation = documentation;
                    return this;
                }

                @Override
                public Input build() {
                    return new Input(this);
                }

                private Builder from(Input input) {
                    id = input.id;
                    extension.addAll(input.extension);
                    modifierExtension.addAll(input.modifierExtension);
                    type = input.type;
                    documentation = input.documentation;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Transform Rule from source to target.
         * </p>
         */
        public static class Rule extends BackboneElement {
            private final Id name;
            private final List<Source> source;
            private final List<Target> target;
            private final List<StructureMap.Group.Rule> rule;
            private final List<Dependent> dependent;
            private final String documentation;

            private volatile int hashCode;

            private Rule(Builder builder) {
                super(builder);
                name = ValidationSupport.requireNonNull(builder.name, "name");
                source = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.source, "source"));
                target = Collections.unmodifiableList(builder.target);
                rule = Collections.unmodifiableList(builder.rule);
                dependent = Collections.unmodifiableList(builder.dependent);
                documentation = builder.documentation;
                if (!hasValue() && !hasChildren()) {
                    throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                }
            }

            /**
             * <p>
             * Name of the rule for internal references.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Id}.
             */
            public Id getName() {
                return name;
            }

            /**
             * <p>
             * Source inputs to the mapping.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Source}.
             */
            public List<Source> getSource() {
                return source;
            }

            /**
             * <p>
             * Content to create because of this mapping rule.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Target}.
             */
            public List<Target> getTarget() {
                return target;
            }

            /**
             * <p>
             * Rules contained in this rule.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Rule}.
             */
            public List<StructureMap.Group.Rule> getRule() {
                return rule;
            }

            /**
             * <p>
             * Which other rules to apply in the context of this rule.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Dependent}.
             */
            public List<Dependent> getDependent() {
                return dependent;
            }

            /**
             * <p>
             * Documentation for this instance of data.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getDocumentation() {
                return documentation;
            }

            @Override
            protected boolean hasChildren() {
                return super.hasChildren() || 
                    (name != null) || 
                    !source.isEmpty() || 
                    !target.isEmpty() || 
                    !rule.isEmpty() || 
                    !dependent.isEmpty() || 
                    (documentation != null);
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
                        accept(source, "source", visitor, Source.class);
                        accept(target, "target", visitor, Target.class);
                        accept(rule, "rule", visitor, StructureMap.Group.Rule.class);
                        accept(dependent, "dependent", visitor, Dependent.class);
                        accept(documentation, "documentation", visitor);
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
                Rule other = (Rule) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(source, other.source) && 
                    Objects.equals(target, other.target) && 
                    Objects.equals(rule, other.rule) && 
                    Objects.equals(dependent, other.dependent) && 
                    Objects.equals(documentation, other.documentation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        name, 
                        source, 
                        target, 
                        rule, 
                        dependent, 
                        documentation);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(name, source).from(this);
            }

            public Builder toBuilder(Id name, List<Source> source) {
                return new Builder(name, source).from(this);
            }

            public static Builder builder(Id name, List<Source> source) {
                return new Builder(name, source);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Id name;
                private final List<Source> source;

                // optional
                private List<Target> target = new ArrayList<>();
                private List<StructureMap.Group.Rule> rule = new ArrayList<>();
                private List<Dependent> dependent = new ArrayList<>();
                private String documentation;

                private Builder(Id name, List<Source> source) {
                    super();
                    this.name = name;
                    this.source = source;
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
                 * Content to create because of this mapping rule.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param target
                 *     Content to create because of this mapping rule
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder target(Target... target) {
                    for (Target value : target) {
                        this.target.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Content to create because of this mapping rule.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param target
                 *     Content to create because of this mapping rule
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder target(Collection<Target> target) {
                    this.target = new ArrayList<>(target);
                    return this;
                }

                /**
                 * <p>
                 * Rules contained in this rule.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param rule
                 *     Rules contained in this rule
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder rule(StructureMap.Group.Rule... rule) {
                    for (StructureMap.Group.Rule value : rule) {
                        this.rule.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Rules contained in this rule.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param rule
                 *     Rules contained in this rule
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder rule(Collection<StructureMap.Group.Rule> rule) {
                    this.rule = new ArrayList<>(rule);
                    return this;
                }

                /**
                 * <p>
                 * Which other rules to apply in the context of this rule.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
                 * 
                 * @param dependent
                 *     Which other rules to apply in the context of this rule
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder dependent(Dependent... dependent) {
                    for (Dependent value : dependent) {
                        this.dependent.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Which other rules to apply in the context of this rule.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param dependent
                 *     Which other rules to apply in the context of this rule
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder dependent(Collection<Dependent> dependent) {
                    this.dependent = new ArrayList<>(dependent);
                    return this;
                }

                /**
                 * <p>
                 * Documentation for this instance of data.
                 * </p>
                 * 
                 * @param documentation
                 *     Documentation for this instance of data
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder documentation(String documentation) {
                    this.documentation = documentation;
                    return this;
                }

                @Override
                public Rule build() {
                    return new Rule(this);
                }

                private Builder from(Rule rule) {
                    id = rule.id;
                    extension.addAll(rule.extension);
                    modifierExtension.addAll(rule.modifierExtension);
                    target.addAll(rule.target);
                    this.rule.addAll(rule.rule);
                    dependent.addAll(rule.dependent);
                    documentation = rule.documentation;
                    return this;
                }
            }

            /**
             * <p>
             * Source inputs to the mapping.
             * </p>
             */
            public static class Source extends BackboneElement {
                private final Id context;
                private final Integer min;
                private final String max;
                private final String type;
                private final Element defaultValue;
                private final String element;
                private final StructureMapSourceListMode listMode;
                private final Id variable;
                private final String condition;
                private final String check;
                private final String logMessage;

                private volatile int hashCode;

                private Source(Builder builder) {
                    super(builder);
                    context = ValidationSupport.requireNonNull(builder.context, "context");
                    min = builder.min;
                    max = builder.max;
                    type = builder.type;
                    defaultValue = ValidationSupport.choiceElement(builder.defaultValue, "defaultValue", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
                    element = builder.element;
                    listMode = builder.listMode;
                    variable = builder.variable;
                    condition = builder.condition;
                    check = builder.check;
                    logMessage = builder.logMessage;
                    if (!hasValue() && !hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * Type or variable this rule applies to.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Id}.
                 */
                public Id getContext() {
                    return context;
                }

                /**
                 * <p>
                 * Specified minimum cardinality for the element. This is optional; if present, it acts an implicit check on the input 
                 * content.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Integer}.
                 */
                public Integer getMin() {
                    return min;
                }

                /**
                 * <p>
                 * Specified maximum cardinality for the element - a number or a "*". This is optional; if present, it acts an implicit 
                 * check on the input content (* just serves as documentation; it's the default value).
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getMax() {
                    return max;
                }

                /**
                 * <p>
                 * Specified type for the element. This works as a condition on the mapping - use for polymorphic elements.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getType() {
                    return type;
                }

                /**
                 * <p>
                 * A value to use if there is no existing value in the source object.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Element}.
                 */
                public Element getDefaultValue() {
                    return defaultValue;
                }

                /**
                 * <p>
                 * Optional field for this source.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getElement() {
                    return element;
                }

                /**
                 * <p>
                 * How to handle the list mode for this element.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link StructureMapSourceListMode}.
                 */
                public StructureMapSourceListMode getListMode() {
                    return listMode;
                }

                /**
                 * <p>
                 * Named context for field, if a field is specified.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Id}.
                 */
                public Id getVariable() {
                    return variable;
                }

                /**
                 * <p>
                 * FHIRPath expression - must be true or the rule does not apply.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getCondition() {
                    return condition;
                }

                /**
                 * <p>
                 * FHIRPath expression - must be true or the mapping engine throws an error instead of completing.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getCheck() {
                    return check;
                }

                /**
                 * <p>
                 * A FHIRPath expression which specifies a message to put in the transform log when content matching the source rule is 
                 * found.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getLogMessage() {
                    return logMessage;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        (context != null) || 
                        (min != null) || 
                        (max != null) || 
                        (type != null) || 
                        (defaultValue != null) || 
                        (element != null) || 
                        (listMode != null) || 
                        (variable != null) || 
                        (condition != null) || 
                        (check != null) || 
                        (logMessage != null);
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
                            accept(context, "context", visitor);
                            accept(min, "min", visitor);
                            accept(max, "max", visitor);
                            accept(type, "type", visitor);
                            accept(defaultValue, "defaultValue", visitor);
                            accept(element, "element", visitor);
                            accept(listMode, "listMode", visitor);
                            accept(variable, "variable", visitor);
                            accept(condition, "condition", visitor);
                            accept(check, "check", visitor);
                            accept(logMessage, "logMessage", visitor);
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
                    Source other = (Source) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(context, other.context) && 
                        Objects.equals(min, other.min) && 
                        Objects.equals(max, other.max) && 
                        Objects.equals(type, other.type) && 
                        Objects.equals(defaultValue, other.defaultValue) && 
                        Objects.equals(element, other.element) && 
                        Objects.equals(listMode, other.listMode) && 
                        Objects.equals(variable, other.variable) && 
                        Objects.equals(condition, other.condition) && 
                        Objects.equals(check, other.check) && 
                        Objects.equals(logMessage, other.logMessage);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            context, 
                            min, 
                            max, 
                            type, 
                            defaultValue, 
                            element, 
                            listMode, 
                            variable, 
                            condition, 
                            check, 
                            logMessage);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(context).from(this);
                }

                public Builder toBuilder(Id context) {
                    return new Builder(context).from(this);
                }

                public static Builder builder(Id context) {
                    return new Builder(context);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final Id context;

                    // optional
                    private Integer min;
                    private String max;
                    private String type;
                    private Element defaultValue;
                    private String element;
                    private StructureMapSourceListMode listMode;
                    private Id variable;
                    private String condition;
                    private String check;
                    private String logMessage;

                    private Builder(Id context) {
                        super();
                        this.context = context;
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
                     * Specified minimum cardinality for the element. This is optional; if present, it acts an implicit check on the input 
                     * content.
                     * </p>
                     * 
                     * @param min
                     *     Specified minimum cardinality
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder min(Integer min) {
                        this.min = min;
                        return this;
                    }

                    /**
                     * <p>
                     * Specified maximum cardinality for the element - a number or a "*". This is optional; if present, it acts an implicit 
                     * check on the input content (* just serves as documentation; it's the default value).
                     * </p>
                     * 
                     * @param max
                     *     Specified maximum cardinality (number or *)
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder max(String max) {
                        this.max = max;
                        return this;
                    }

                    /**
                     * <p>
                     * Specified type for the element. This works as a condition on the mapping - use for polymorphic elements.
                     * </p>
                     * 
                     * @param type
                     *     Rule only applies if source has this type
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder type(String type) {
                        this.type = type;
                        return this;
                    }

                    /**
                     * <p>
                     * A value to use if there is no existing value in the source object.
                     * </p>
                     * 
                     * @param defaultValue
                     *     Default value if no value exists
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder defaultValue(Element defaultValue) {
                        this.defaultValue = defaultValue;
                        return this;
                    }

                    /**
                     * <p>
                     * Optional field for this source.
                     * </p>
                     * 
                     * @param element
                     *     Optional field for this source
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder element(String element) {
                        this.element = element;
                        return this;
                    }

                    /**
                     * <p>
                     * How to handle the list mode for this element.
                     * </p>
                     * 
                     * @param listMode
                     *     first | not_first | last | not_last | only_one
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder listMode(StructureMapSourceListMode listMode) {
                        this.listMode = listMode;
                        return this;
                    }

                    /**
                     * <p>
                     * Named context for field, if a field is specified.
                     * </p>
                     * 
                     * @param variable
                     *     Named context for field, if a field is specified
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder variable(Id variable) {
                        this.variable = variable;
                        return this;
                    }

                    /**
                     * <p>
                     * FHIRPath expression - must be true or the rule does not apply.
                     * </p>
                     * 
                     * @param condition
                     *     FHIRPath expression - must be true or the rule does not apply
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder condition(String condition) {
                        this.condition = condition;
                        return this;
                    }

                    /**
                     * <p>
                     * FHIRPath expression - must be true or the mapping engine throws an error instead of completing.
                     * </p>
                     * 
                     * @param check
                     *     FHIRPath expression - must be true or the mapping engine throws an error instead of completing
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder check(String check) {
                        this.check = check;
                        return this;
                    }

                    /**
                     * <p>
                     * A FHIRPath expression which specifies a message to put in the transform log when content matching the source rule is 
                     * found.
                     * </p>
                     * 
                     * @param logMessage
                     *     Message to put in log if source exists (FHIRPath)
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder logMessage(String logMessage) {
                        this.logMessage = logMessage;
                        return this;
                    }

                    @Override
                    public Source build() {
                        return new Source(this);
                    }

                    private Builder from(Source source) {
                        id = source.id;
                        extension.addAll(source.extension);
                        modifierExtension.addAll(source.modifierExtension);
                        min = source.min;
                        max = source.max;
                        type = source.type;
                        defaultValue = source.defaultValue;
                        element = source.element;
                        listMode = source.listMode;
                        variable = source.variable;
                        condition = source.condition;
                        check = source.check;
                        logMessage = source.logMessage;
                        return this;
                    }
                }
            }

            /**
             * <p>
             * Content to create because of this mapping rule.
             * </p>
             */
            public static class Target extends BackboneElement {
                private final Id context;
                private final StructureMapContextType contextType;
                private final String element;
                private final Id variable;
                private final List<StructureMapTargetListMode> listMode;
                private final Id listRuleId;
                private final StructureMapTransform transform;
                private final List<Parameter> parameter;

                private volatile int hashCode;

                private Target(Builder builder) {
                    super(builder);
                    context = builder.context;
                    contextType = builder.contextType;
                    element = builder.element;
                    variable = builder.variable;
                    listMode = Collections.unmodifiableList(builder.listMode);
                    listRuleId = builder.listRuleId;
                    transform = builder.transform;
                    parameter = Collections.unmodifiableList(builder.parameter);
                    if (!hasValue() && !hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * Type or variable this rule applies to.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Id}.
                 */
                public Id getContext() {
                    return context;
                }

                /**
                 * <p>
                 * How to interpret the context.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link StructureMapContextType}.
                 */
                public StructureMapContextType getContextType() {
                    return contextType;
                }

                /**
                 * <p>
                 * Field to create in the context.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getElement() {
                    return element;
                }

                /**
                 * <p>
                 * Named context for field, if desired, and a field is specified.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Id}.
                 */
                public Id getVariable() {
                    return variable;
                }

                /**
                 * <p>
                 * If field is a list, how to manage the list.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link StructureMapTargetListMode}.
                 */
                public List<StructureMapTargetListMode> getListMode() {
                    return listMode;
                }

                /**
                 * <p>
                 * Internal rule reference for shared list items.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Id}.
                 */
                public Id getListRuleId() {
                    return listRuleId;
                }

                /**
                 * <p>
                 * How the data is copied / created.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link StructureMapTransform}.
                 */
                public StructureMapTransform getTransform() {
                    return transform;
                }

                /**
                 * <p>
                 * Parameters to the transform.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Parameter}.
                 */
                public List<Parameter> getParameter() {
                    return parameter;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        (context != null) || 
                        (contextType != null) || 
                        (element != null) || 
                        (variable != null) || 
                        !listMode.isEmpty() || 
                        (listRuleId != null) || 
                        (transform != null) || 
                        !parameter.isEmpty();
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
                            accept(context, "context", visitor);
                            accept(contextType, "contextType", visitor);
                            accept(element, "element", visitor);
                            accept(variable, "variable", visitor);
                            accept(listMode, "listMode", visitor, StructureMapTargetListMode.class);
                            accept(listRuleId, "listRuleId", visitor);
                            accept(transform, "transform", visitor);
                            accept(parameter, "parameter", visitor, Parameter.class);
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
                    Target other = (Target) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(context, other.context) && 
                        Objects.equals(contextType, other.contextType) && 
                        Objects.equals(element, other.element) && 
                        Objects.equals(variable, other.variable) && 
                        Objects.equals(listMode, other.listMode) && 
                        Objects.equals(listRuleId, other.listRuleId) && 
                        Objects.equals(transform, other.transform) && 
                        Objects.equals(parameter, other.parameter);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            context, 
                            contextType, 
                            element, 
                            variable, 
                            listMode, 
                            listRuleId, 
                            transform, 
                            parameter);
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
                    private Id context;
                    private StructureMapContextType contextType;
                    private String element;
                    private Id variable;
                    private List<StructureMapTargetListMode> listMode = new ArrayList<>();
                    private Id listRuleId;
                    private StructureMapTransform transform;
                    private List<Parameter> parameter = new ArrayList<>();

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
                     * Type or variable this rule applies to.
                     * </p>
                     * 
                     * @param context
                     *     Type or variable this rule applies to
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder context(Id context) {
                        this.context = context;
                        return this;
                    }

                    /**
                     * <p>
                     * How to interpret the context.
                     * </p>
                     * 
                     * @param contextType
                     *     type | variable
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder contextType(StructureMapContextType contextType) {
                        this.contextType = contextType;
                        return this;
                    }

                    /**
                     * <p>
                     * Field to create in the context.
                     * </p>
                     * 
                     * @param element
                     *     Field to create in the context
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder element(String element) {
                        this.element = element;
                        return this;
                    }

                    /**
                     * <p>
                     * Named context for field, if desired, and a field is specified.
                     * </p>
                     * 
                     * @param variable
                     *     Named context for field, if desired, and a field is specified
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder variable(Id variable) {
                        this.variable = variable;
                        return this;
                    }

                    /**
                     * <p>
                     * If field is a list, how to manage the list.
                     * </p>
                     * <p>
                     * Adds new element(s) to the existing list
                     * </p>
                     * 
                     * @param listMode
                     *     first | share | last | collate
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder listMode(StructureMapTargetListMode... listMode) {
                        for (StructureMapTargetListMode value : listMode) {
                            this.listMode.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * If field is a list, how to manage the list.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param listMode
                     *     first | share | last | collate
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder listMode(Collection<StructureMapTargetListMode> listMode) {
                        this.listMode = new ArrayList<>(listMode);
                        return this;
                    }

                    /**
                     * <p>
                     * Internal rule reference for shared list items.
                     * </p>
                     * 
                     * @param listRuleId
                     *     Internal rule reference for shared list items
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder listRuleId(Id listRuleId) {
                        this.listRuleId = listRuleId;
                        return this;
                    }

                    /**
                     * <p>
                     * How the data is copied / created.
                     * </p>
                     * 
                     * @param transform
                     *     create | copy +
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder transform(StructureMapTransform transform) {
                        this.transform = transform;
                        return this;
                    }

                    /**
                     * <p>
                     * Parameters to the transform.
                     * </p>
                     * <p>
                     * Adds new element(s) to the existing list
                     * </p>
                     * 
                     * @param parameter
                     *     Parameters to the transform
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder parameter(Parameter... parameter) {
                        for (Parameter value : parameter) {
                            this.parameter.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * Parameters to the transform.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param parameter
                     *     Parameters to the transform
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder parameter(Collection<Parameter> parameter) {
                        this.parameter = new ArrayList<>(parameter);
                        return this;
                    }

                    @Override
                    public Target build() {
                        return new Target(this);
                    }

                    private Builder from(Target target) {
                        id = target.id;
                        extension.addAll(target.extension);
                        modifierExtension.addAll(target.modifierExtension);
                        context = target.context;
                        contextType = target.contextType;
                        element = target.element;
                        variable = target.variable;
                        listMode.addAll(target.listMode);
                        listRuleId = target.listRuleId;
                        transform = target.transform;
                        parameter.addAll(target.parameter);
                        return this;
                    }
                }

                /**
                 * <p>
                 * Parameters to the transform.
                 * </p>
                 */
                public static class Parameter extends BackboneElement {
                    private final Element value;

                    private volatile int hashCode;

                    private Parameter(Builder builder) {
                        super(builder);
                        value = ValidationSupport.requireChoiceElement(builder.value, "value", Id.class, String.class, Boolean.class, Integer.class, Decimal.class);
                        if (!hasValue() && !hasChildren()) {
                            throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                        }
                    }

                    /**
                     * <p>
                     * Parameter value - variable or literal.
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link Element}.
                     */
                    public Element getValue() {
                        return value;
                    }

                    @Override
                    protected boolean hasChildren() {
                        return super.hasChildren() || 
                            (value != null);
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
                                accept(value, "value", visitor);
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
                        Parameter other = (Parameter) obj;
                        return Objects.equals(id, other.id) && 
                            Objects.equals(extension, other.extension) && 
                            Objects.equals(modifierExtension, other.modifierExtension) && 
                            Objects.equals(value, other.value);
                    }

                    @Override
                    public int hashCode() {
                        int result = hashCode;
                        if (result == 0) {
                            result = Objects.hash(id, 
                                extension, 
                                modifierExtension, 
                                value);
                            hashCode = result;
                        }
                        return result;
                    }

                    @Override
                    public Builder toBuilder() {
                        return new Builder(value).from(this);
                    }

                    public Builder toBuilder(Element value) {
                        return new Builder(value).from(this);
                    }

                    public static Builder builder(Element value) {
                        return new Builder(value);
                    }

                    public static class Builder extends BackboneElement.Builder {
                        // required
                        private final Element value;

                        private Builder(Element value) {
                            super();
                            this.value = value;
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
                        public Parameter build() {
                            return new Parameter(this);
                        }

                        private Builder from(Parameter parameter) {
                            id = parameter.id;
                            extension.addAll(parameter.extension);
                            modifierExtension.addAll(parameter.modifierExtension);
                            return this;
                        }
                    }
                }
            }

            /**
             * <p>
             * Which other rules to apply in the context of this rule.
             * </p>
             */
            public static class Dependent extends BackboneElement {
                private final Id name;
                private final List<String> variable;

                private volatile int hashCode;

                private Dependent(Builder builder) {
                    super(builder);
                    name = ValidationSupport.requireNonNull(builder.name, "name");
                    variable = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.variable, "variable"));
                    if (!hasValue() && !hasChildren()) {
                        throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
                    }
                }

                /**
                 * <p>
                 * Name of a rule or group to apply.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Id}.
                 */
                public Id getName() {
                    return name;
                }

                /**
                 * <p>
                 * Variable to pass to the rule or group.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link String}.
                 */
                public List<String> getVariable() {
                    return variable;
                }

                @Override
                protected boolean hasChildren() {
                    return super.hasChildren() || 
                        (name != null) || 
                        !variable.isEmpty();
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
                            accept(variable, "variable", visitor, String.class);
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
                    Dependent other = (Dependent) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(name, other.name) && 
                        Objects.equals(variable, other.variable);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            name, 
                            variable);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(name, variable).from(this);
                }

                public Builder toBuilder(Id name, List<String> variable) {
                    return new Builder(name, variable).from(this);
                }

                public static Builder builder(Id name, List<String> variable) {
                    return new Builder(name, variable);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final Id name;
                    private final List<String> variable;

                    private Builder(Id name, List<String> variable) {
                        super();
                        this.name = name;
                        this.variable = variable;
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
                    public Dependent build() {
                        return new Dependent(this);
                    }

                    private Builder from(Dependent dependent) {
                        id = dependent.id;
                        extension.addAll(dependent.extension);
                        modifierExtension.addAll(dependent.modifierExtension);
                        return this;
                    }
                }
            }
        }
    }
}
