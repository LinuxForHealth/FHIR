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
import com.ibm.watsonhealth.fhir.model.type.BundleType;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HTTPVerb;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.SearchEntryMode;
import com.ibm.watsonhealth.fhir.model.type.Signature;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A container for a collection of resources.
 * </p>
 */
@Constraint(
    id = "bdl-1",
    level = "Rule",
    location = "(base)",
    description = "total only when a search or history",
    expression = "total.empty() or (type = 'searchset') or (type = 'history')"
)
@Constraint(
    id = "bdl-2",
    level = "Rule",
    location = "(base)",
    description = "entry.search only when a search",
    expression = "entry.search.empty() or (type = 'searchset')"
)
@Constraint(
    id = "bdl-3",
    level = "Rule",
    location = "(base)",
    description = "entry.request mandatory for batch/transaction/history, otherwise prohibited",
    expression = "entry.all(request.exists() = (%resource.type = 'batch' or %resource.type = 'transaction' or %resource.type = 'history'))"
)
@Constraint(
    id = "bdl-4",
    level = "Rule",
    location = "(base)",
    description = "entry.response mandatory for batch-response/transaction-response/history, otherwise prohibited",
    expression = "entry.all(response.exists() = (%resource.type = 'batch-response' or %resource.type = 'transaction-response' or %resource.type = 'history'))"
)
@Constraint(
    id = "bdl-5",
    level = "Rule",
    location = "Bundle.entry",
    description = "must be a resource unless there's a request or response",
    expression = "resource.exists() or request.exists() or response.exists()"
)
@Constraint(
    id = "bdl-7",
    level = "Rule",
    location = "(base)",
    description = "FullUrl must be unique in a bundle, or else entries with the same fullUrl must have different meta.versionId (except in history bundles)",
    expression = "(type = 'history') or entry.where(fullUrl.exists()).select(fullUrl&resource.meta.versionId).isDistinct()"
)
@Constraint(
    id = "bdl-8",
    level = "Rule",
    location = "Bundle.entry",
    description = "fullUrl cannot be a version specific reference",
    expression = "fullUrl.contains('/_history/').not()"
)
@Constraint(
    id = "bdl-9",
    level = "Rule",
    location = "(base)",
    description = "A document must have an identifier with a system and a value",
    expression = "type = 'document' implies (identifier.system.exists() and identifier.value.exists())"
)
@Constraint(
    id = "bdl-10",
    level = "Rule",
    location = "(base)",
    description = "A document must have a date",
    expression = "type = 'document' implies (meta.lastUpdated.hasValue())"
)
@Constraint(
    id = "bdl-11",
    level = "Rule",
    location = "(base)",
    description = "A document must have a Composition as the first resource",
    expression = "type = 'document' implies entry.first().resource.is(Composition)"
)
@Constraint(
    id = "bdl-12",
    level = "Rule",
    location = "(base)",
    description = "A message must have a MessageHeader as the first resource",
    expression = "type = 'message' implies entry.first().resource.is(MessageHeader)"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Bundle extends Resource {
    private final Identifier identifier;
    private final BundleType type;
    private final Instant timestamp;
    private final UnsignedInt total;
    private final List<Link> link;
    private final List<Entry> entry;
    private final Signature signature;

    private volatile int hashCode;

    private Bundle(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        type = ValidationSupport.requireNonNull(builder.type, "type");
        timestamp = builder.timestamp;
        total = builder.total;
        link = Collections.unmodifiableList(builder.link);
        entry = Collections.unmodifiableList(builder.entry);
        signature = builder.signature;
    }

    /**
     * <p>
     * A persistent identifier for the bundle that won't change as a bundle is copied from server to server.
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
     * Indicates the purpose of this bundle - how it is intended to be used.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link BundleType}.
     */
    public BundleType getType() {
        return type;
    }

    /**
     * <p>
     * The date/time that the bundle was assembled - i.e. when the resources were placed in the bundle.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * <p>
     * If a set of search matches, this is the total number of entries of type 'match' across all pages in the search. It 
     * does not include search.mode = 'include' or 'outcome' entries and it does not provide a count of the number of entries 
     * in the Bundle.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt}.
     */
    public UnsignedInt getTotal() {
        return total;
    }

    /**
     * <p>
     * A series of links that provide context to this bundle.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Link}.
     */
    public List<Link> getLink() {
        return link;
    }

    /**
     * <p>
     * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
     * history only).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Entry}.
     */
    public List<Entry> getEntry() {
        return entry;
    }

    /**
     * <p>
     * Digital Signature - base64 encoded. XML-DSig or a JWT.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Signature}.
     */
    public Signature getSignature() {
        return signature;
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
                accept(identifier, "identifier", visitor);
                accept(type, "type", visitor);
                accept(timestamp, "timestamp", visitor);
                accept(total, "total", visitor);
                accept(link, "link", visitor, Link.class);
                accept(entry, "entry", visitor, Entry.class);
                accept(signature, "signature", visitor);
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
        Bundle other = (Bundle) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(timestamp, other.timestamp) && 
            Objects.equals(total, other.total) && 
            Objects.equals(link, other.link) && 
            Objects.equals(entry, other.entry) && 
            Objects.equals(signature, other.signature);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                identifier, 
                type, 
                timestamp, 
                total, 
                link, 
                entry, 
                signature);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type).from(this);
    }

    public Builder toBuilder(BundleType type) {
        return new Builder(type).from(this);
    }

    public static Builder builder(BundleType type) {
        return new Builder(type);
    }

    public static class Builder extends Resource.Builder {
        // required
        private final BundleType type;

        // optional
        private Identifier identifier;
        private Instant timestamp;
        private UnsignedInt total;
        private List<Link> link = new ArrayList<>();
        private List<Entry> entry = new ArrayList<>();
        private Signature signature;

        private Builder(BundleType type) {
            super();
            this.type = type;
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
         * A persistent identifier for the bundle that won't change as a bundle is copied from server to server.
         * </p>
         * 
         * @param identifier
         *     Persistent identifier for the bundle
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
         * The date/time that the bundle was assembled - i.e. when the resources were placed in the bundle.
         * </p>
         * 
         * @param timestamp
         *     When the bundle was assembled
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * <p>
         * If a set of search matches, this is the total number of entries of type 'match' across all pages in the search. It 
         * does not include search.mode = 'include' or 'outcome' entries and it does not provide a count of the number of entries 
         * in the Bundle.
         * </p>
         * 
         * @param total
         *     If search, the total number of matches
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder total(UnsignedInt total) {
            this.total = total;
            return this;
        }

        /**
         * <p>
         * A series of links that provide context to this bundle.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param link
         *     Links related to this Bundle
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
         * <p>
         * A series of links that provide context to this bundle.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param link
         *     Links related to this Bundle
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder link(Collection<Link> link) {
            this.link = new ArrayList<>(link);
            return this;
        }

        /**
         * <p>
         * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
         * history only).
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param entry
         *     Entry in the bundle - will have a resource or information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entry(Entry... entry) {
            for (Entry value : entry) {
                this.entry.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
         * history only).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param entry
         *     Entry in the bundle - will have a resource or information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entry(Collection<Entry> entry) {
            this.entry = new ArrayList<>(entry);
            return this;
        }

        /**
         * <p>
         * Digital Signature - base64 encoded. XML-DSig or a JWT.
         * </p>
         * 
         * @param signature
         *     Digital Signature
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder signature(Signature signature) {
            this.signature = signature;
            return this;
        }

        @Override
        public Bundle build() {
            return new Bundle(this);
        }

        private Builder from(Bundle bundle) {
            id = bundle.id;
            meta = bundle.meta;
            implicitRules = bundle.implicitRules;
            language = bundle.language;
            identifier = bundle.identifier;
            timestamp = bundle.timestamp;
            total = bundle.total;
            link.addAll(bundle.link);
            entry.addAll(bundle.entry);
            signature = bundle.signature;
            return this;
        }
    }

    /**
     * <p>
     * A series of links that provide context to this bundle.
     * </p>
     */
    public static class Link extends BackboneElement {
        private final String relation;
        private final Uri url;

        private volatile int hashCode;

        private Link(Builder builder) {
            super(builder);
            relation = ValidationSupport.requireNonNull(builder.relation, "relation");
            url = ValidationSupport.requireNonNull(builder.url, "url");
        }

        /**
         * <p>
         * A name which details the functional use for this link - see [http://www.iana.org/assignments/link-relations/link-
         * relations.xhtml#link-relations-1](http://www.iana.org/assignments/link-relations/link-relations.xhtml#link-relations-
         * 1).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getRelation() {
            return relation;
        }

        /**
         * <p>
         * The reference details for the link.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getUrl() {
            return url;
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
                    accept(relation, "relation", visitor);
                    accept(url, "url", visitor);
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
            Link other = (Link) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(relation, other.relation) && 
                Objects.equals(url, other.url);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    relation, 
                    url);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(relation, url).from(this);
        }

        public Builder toBuilder(String relation, Uri url) {
            return new Builder(relation, url).from(this);
        }

        public static Builder builder(String relation, Uri url) {
            return new Builder(relation, url);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String relation;
            private final Uri url;

            private Builder(String relation, Uri url) {
                super();
                this.relation = relation;
                this.url = url;
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
            public Link build() {
                return new Link(this);
            }

            private Builder from(Link link) {
                id = link.id;
                extension.addAll(link.extension);
                modifierExtension.addAll(link.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
     * history only).
     * </p>
     */
    public static class Entry extends BackboneElement {
        private final List<Bundle.Link> link;
        private final Uri fullUrl;
        private final Resource resource;
        private final Search search;
        private final Request request;
        private final Response response;

        private volatile int hashCode;

        private Entry(Builder builder) {
            super(builder);
            link = Collections.unmodifiableList(builder.link);
            fullUrl = builder.fullUrl;
            resource = builder.resource;
            search = builder.search;
            request = builder.request;
            response = builder.response;
        }

        /**
         * <p>
         * A series of links that provide context to this entry.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Link}.
         */
        public List<Bundle.Link> getLink() {
            return link;
        }

        /**
         * <p>
         * The Absolute URL for the resource. The fullUrl SHALL NOT disagree with the id in the resource - i.e. if the fullUrl is 
         * not a urn:uuid, the URL shall be version-independent URL consistent with the Resource.id. The fullUrl is a version 
         * independent reference to the resource. The fullUrl element SHALL have a value except that: 
         * * fullUrl can be empty on a POST (although it does not need to when specifying a temporary id for reference in the 
         * bundle)
         * * Results from operations might involve resources that are not identified.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getFullUrl() {
            return fullUrl;
        }

        /**
         * <p>
         * The Resource for the entry. The purpose/meaning of the resource is determined by the Bundle.type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Resource}.
         */
        public Resource getResource() {
            return resource;
        }

        /**
         * <p>
         * Information about the search process that lead to the creation of this entry.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Search}.
         */
        public Search getSearch() {
            return search;
        }

        /**
         * <p>
         * Additional information about how this entry should be processed as part of a transaction or batch. For history, it 
         * shows how the entry was processed to create the version contained in the entry.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Request}.
         */
        public Request getRequest() {
            return request;
        }

        /**
         * <p>
         * Indicates the results of processing the corresponding 'request' entry in the batch or transaction being responded to 
         * or what the results of an operation where when returning history.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Response}.
         */
        public Response getResponse() {
            return response;
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
                    accept(link, "link", visitor, Bundle.Link.class);
                    accept(fullUrl, "fullUrl", visitor);
                    accept(resource, "resource", visitor);
                    accept(search, "search", visitor);
                    accept(request, "request", visitor);
                    accept(response, "response", visitor);
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
            Entry other = (Entry) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(link, other.link) && 
                Objects.equals(fullUrl, other.fullUrl) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(search, other.search) && 
                Objects.equals(request, other.request) && 
                Objects.equals(response, other.response);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    link, 
                    fullUrl, 
                    resource, 
                    search, 
                    request, 
                    response);
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
            private List<Bundle.Link> link = new ArrayList<>();
            private Uri fullUrl;
            private Resource resource;
            private Search search;
            private Request request;
            private Response response;

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
             * A series of links that provide context to this entry.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param link
             *     Links related to this entry
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder link(Bundle.Link... link) {
                for (Bundle.Link value : link) {
                    this.link.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A series of links that provide context to this entry.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param link
             *     Links related to this entry
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder link(Collection<Bundle.Link> link) {
                this.link = new ArrayList<>(link);
                return this;
            }

            /**
             * <p>
             * The Absolute URL for the resource. The fullUrl SHALL NOT disagree with the id in the resource - i.e. if the fullUrl is 
             * not a urn:uuid, the URL shall be version-independent URL consistent with the Resource.id. The fullUrl is a version 
             * independent reference to the resource. The fullUrl element SHALL have a value except that: 
             * * fullUrl can be empty on a POST (although it does not need to when specifying a temporary id for reference in the 
             * bundle)
             * * Results from operations might involve resources that are not identified.
             * </p>
             * 
             * @param fullUrl
             *     URI for resource (Absolute URL server address or URI for UUID/OID)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fullUrl(Uri fullUrl) {
                this.fullUrl = fullUrl;
                return this;
            }

            /**
             * <p>
             * The Resource for the entry. The purpose/meaning of the resource is determined by the Bundle.type.
             * </p>
             * 
             * @param resource
             *     A resource in the bundle
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Resource resource) {
                this.resource = resource;
                return this;
            }

            /**
             * <p>
             * Information about the search process that lead to the creation of this entry.
             * </p>
             * 
             * @param search
             *     Search related information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder search(Search search) {
                this.search = search;
                return this;
            }

            /**
             * <p>
             * Additional information about how this entry should be processed as part of a transaction or batch. For history, it 
             * shows how the entry was processed to create the version contained in the entry.
             * </p>
             * 
             * @param request
             *     Additional execution information (transaction/batch/history)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder request(Request request) {
                this.request = request;
                return this;
            }

            /**
             * <p>
             * Indicates the results of processing the corresponding 'request' entry in the batch or transaction being responded to 
             * or what the results of an operation where when returning history.
             * </p>
             * 
             * @param response
             *     Results of execution (transaction/batch/history)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder response(Response response) {
                this.response = response;
                return this;
            }

            @Override
            public Entry build() {
                return new Entry(this);
            }

            private Builder from(Entry entry) {
                id = entry.id;
                extension.addAll(entry.extension);
                modifierExtension.addAll(entry.modifierExtension);
                link.addAll(entry.link);
                fullUrl = entry.fullUrl;
                resource = entry.resource;
                search = entry.search;
                request = entry.request;
                response = entry.response;
                return this;
            }
        }

        /**
         * <p>
         * Information about the search process that lead to the creation of this entry.
         * </p>
         */
        public static class Search extends BackboneElement {
            private final SearchEntryMode mode;
            private final Decimal score;

            private volatile int hashCode;

            private Search(Builder builder) {
                super(builder);
                mode = builder.mode;
                score = builder.score;
            }

            /**
             * <p>
             * Why this entry is in the result set - whether it's included as a match or because of an _include requirement, or to 
             * convey information or warning information about the search process.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link SearchEntryMode}.
             */
            public SearchEntryMode getMode() {
                return mode;
            }

            /**
             * <p>
             * When searching, the server's search ranking score for the entry.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getScore() {
                return score;
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
                        accept(mode, "mode", visitor);
                        accept(score, "score", visitor);
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
                Search other = (Search) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(mode, other.mode) && 
                    Objects.equals(score, other.score);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        mode, 
                        score);
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
                private SearchEntryMode mode;
                private Decimal score;

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
                 * Why this entry is in the result set - whether it's included as a match or because of an _include requirement, or to 
                 * convey information or warning information about the search process.
                 * </p>
                 * 
                 * @param mode
                 *     match | include | outcome - why this is in the result set
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder mode(SearchEntryMode mode) {
                    this.mode = mode;
                    return this;
                }

                /**
                 * <p>
                 * When searching, the server's search ranking score for the entry.
                 * </p>
                 * 
                 * @param score
                 *     Search ranking (between 0 and 1)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder score(Decimal score) {
                    this.score = score;
                    return this;
                }

                @Override
                public Search build() {
                    return new Search(this);
                }

                private Builder from(Search search) {
                    id = search.id;
                    extension.addAll(search.extension);
                    modifierExtension.addAll(search.modifierExtension);
                    mode = search.mode;
                    score = search.score;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Additional information about how this entry should be processed as part of a transaction or batch. For history, it 
         * shows how the entry was processed to create the version contained in the entry.
         * </p>
         */
        public static class Request extends BackboneElement {
            private final HTTPVerb method;
            private final Uri url;
            private final String ifNoneMatch;
            private final Instant ifModifiedSince;
            private final String ifMatch;
            private final String ifNoneExist;

            private volatile int hashCode;

            private Request(Builder builder) {
                super(builder);
                method = ValidationSupport.requireNonNull(builder.method, "method");
                url = ValidationSupport.requireNonNull(builder.url, "url");
                ifNoneMatch = builder.ifNoneMatch;
                ifModifiedSince = builder.ifModifiedSince;
                ifMatch = builder.ifMatch;
                ifNoneExist = builder.ifNoneExist;
            }

            /**
             * <p>
             * In a transaction or batch, this is the HTTP action to be executed for this entry. In a history bundle, this indicates 
             * the HTTP action that occurred.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link HTTPVerb}.
             */
            public HTTPVerb getMethod() {
                return method;
            }

            /**
             * <p>
             * The URL for this entry, relative to the root (the address to which the request is posted).
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
             * If the ETag values match, return a 304 Not Modified status. See the API documentation for ["Conditional Read"](http.
             * html#cread).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getIfNoneMatch() {
                return ifNoneMatch;
            }

            /**
             * <p>
             * Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.
             * html#cread).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Instant}.
             */
            public Instant getIfModifiedSince() {
                return ifModifiedSince;
            }

            /**
             * <p>
             * Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource 
             * Contention"](http.html#concurrency).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getIfMatch() {
                return ifMatch;
            }

            /**
             * <p>
             * Instruct the server not to perform the create if a specified resource already exists. For further information, see the 
             * API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what 
             * follows the "?" (not including the "?").
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getIfNoneExist() {
                return ifNoneExist;
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
                        accept(method, "method", visitor);
                        accept(url, "url", visitor);
                        accept(ifNoneMatch, "ifNoneMatch", visitor);
                        accept(ifModifiedSince, "ifModifiedSince", visitor);
                        accept(ifMatch, "ifMatch", visitor);
                        accept(ifNoneExist, "ifNoneExist", visitor);
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
                Request other = (Request) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(method, other.method) && 
                    Objects.equals(url, other.url) && 
                    Objects.equals(ifNoneMatch, other.ifNoneMatch) && 
                    Objects.equals(ifModifiedSince, other.ifModifiedSince) && 
                    Objects.equals(ifMatch, other.ifMatch) && 
                    Objects.equals(ifNoneExist, other.ifNoneExist);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        method, 
                        url, 
                        ifNoneMatch, 
                        ifModifiedSince, 
                        ifMatch, 
                        ifNoneExist);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(method, url).from(this);
            }

            public Builder toBuilder(HTTPVerb method, Uri url) {
                return new Builder(method, url).from(this);
            }

            public static Builder builder(HTTPVerb method, Uri url) {
                return new Builder(method, url);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final HTTPVerb method;
                private final Uri url;

                // optional
                private String ifNoneMatch;
                private Instant ifModifiedSince;
                private String ifMatch;
                private String ifNoneExist;

                private Builder(HTTPVerb method, Uri url) {
                    super();
                    this.method = method;
                    this.url = url;
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
                 * If the ETag values match, return a 304 Not Modified status. See the API documentation for ["Conditional Read"](http.
                 * html#cread).
                 * </p>
                 * 
                 * @param ifNoneMatch
                 *     For managing cache currency
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifNoneMatch(String ifNoneMatch) {
                    this.ifNoneMatch = ifNoneMatch;
                    return this;
                }

                /**
                 * <p>
                 * Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.
                 * html#cread).
                 * </p>
                 * 
                 * @param ifModifiedSince
                 *     For managing cache currency
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifModifiedSince(Instant ifModifiedSince) {
                    this.ifModifiedSince = ifModifiedSince;
                    return this;
                }

                /**
                 * <p>
                 * Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource 
                 * Contention"](http.html#concurrency).
                 * </p>
                 * 
                 * @param ifMatch
                 *     For managing update contention
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifMatch(String ifMatch) {
                    this.ifMatch = ifMatch;
                    return this;
                }

                /**
                 * <p>
                 * Instruct the server not to perform the create if a specified resource already exists. For further information, see the 
                 * API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what 
                 * follows the "?" (not including the "?").
                 * </p>
                 * 
                 * @param ifNoneExist
                 *     For conditional creates
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifNoneExist(String ifNoneExist) {
                    this.ifNoneExist = ifNoneExist;
                    return this;
                }

                @Override
                public Request build() {
                    return new Request(this);
                }

                private Builder from(Request request) {
                    id = request.id;
                    extension.addAll(request.extension);
                    modifierExtension.addAll(request.modifierExtension);
                    ifNoneMatch = request.ifNoneMatch;
                    ifModifiedSince = request.ifModifiedSince;
                    ifMatch = request.ifMatch;
                    ifNoneExist = request.ifNoneExist;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Indicates the results of processing the corresponding 'request' entry in the batch or transaction being responded to 
         * or what the results of an operation where when returning history.
         * </p>
         */
        public static class Response extends BackboneElement {
            private final String status;
            private final Uri location;
            private final String etag;
            private final Instant lastModified;
            private final Resource outcome;

            private volatile int hashCode;

            private Response(Builder builder) {
                super(builder);
                status = ValidationSupport.requireNonNull(builder.status, "status");
                location = builder.location;
                etag = builder.etag;
                lastModified = builder.lastModified;
                outcome = builder.outcome;
            }

            /**
             * <p>
             * The status code returned by processing this entry. The status SHALL start with a 3 digit HTTP code (e.g. 404) and may 
             * contain the standard HTTP description associated with the status code.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getStatus() {
                return status;
            }

            /**
             * <p>
             * The location header created by processing this operation, populated if the operation returns a location.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Uri}.
             */
            public Uri getLocation() {
                return location;
            }

            /**
             * <p>
             * The Etag for the resource, if the operation for the entry produced a versioned resource (see [Resource Metadata and 
             * Versioning](http.html#versioning) and [Managing Resource Contention](http.html#concurrency)).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getEtag() {
                return etag;
            }

            /**
             * <p>
             * The date/time that the resource was modified on the server.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Instant}.
             */
            public Instant getLastModified() {
                return lastModified;
            }

            /**
             * <p>
             * An OperationOutcome containing hints and warnings produced as part of processing this entry in a batch or transaction.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Resource}.
             */
            public Resource getOutcome() {
                return outcome;
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
                        accept(status, "status", visitor);
                        accept(location, "location", visitor);
                        accept(etag, "etag", visitor);
                        accept(lastModified, "lastModified", visitor);
                        accept(outcome, "outcome", visitor);
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
                Response other = (Response) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(status, other.status) && 
                    Objects.equals(location, other.location) && 
                    Objects.equals(etag, other.etag) && 
                    Objects.equals(lastModified, other.lastModified) && 
                    Objects.equals(outcome, other.outcome);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        status, 
                        location, 
                        etag, 
                        lastModified, 
                        outcome);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(status).from(this);
            }

            public Builder toBuilder(String status) {
                return new Builder(status).from(this);
            }

            public static Builder builder(String status) {
                return new Builder(status);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final String status;

                // optional
                private Uri location;
                private String etag;
                private Instant lastModified;
                private Resource outcome;

                private Builder(String status) {
                    super();
                    this.status = status;
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
                 * The location header created by processing this operation, populated if the operation returns a location.
                 * </p>
                 * 
                 * @param location
                 *     The location (if the operation returns a location)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder location(Uri location) {
                    this.location = location;
                    return this;
                }

                /**
                 * <p>
                 * The Etag for the resource, if the operation for the entry produced a versioned resource (see [Resource Metadata and 
                 * Versioning](http.html#versioning) and [Managing Resource Contention](http.html#concurrency)).
                 * </p>
                 * 
                 * @param etag
                 *     The Etag for the resource (if relevant)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder etag(String etag) {
                    this.etag = etag;
                    return this;
                }

                /**
                 * <p>
                 * The date/time that the resource was modified on the server.
                 * </p>
                 * 
                 * @param lastModified
                 *     Server's date time modified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder lastModified(Instant lastModified) {
                    this.lastModified = lastModified;
                    return this;
                }

                /**
                 * <p>
                 * An OperationOutcome containing hints and warnings produced as part of processing this entry in a batch or transaction.
                 * </p>
                 * 
                 * @param outcome
                 *     OperationOutcome with hints and warnings (for batch/transaction)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder outcome(Resource outcome) {
                    this.outcome = outcome;
                    return this;
                }

                @Override
                public Response build() {
                    return new Response(this);
                }

                private Builder from(Response response) {
                    id = response.id;
                    extension.addAll(response.extension);
                    modifierExtension.addAll(response.modifierExtension);
                    location = response.location;
                    etag = response.etag;
                    lastModified = response.lastModified;
                    outcome = response.outcome;
                    return this;
                }
            }
        }
    }
}
