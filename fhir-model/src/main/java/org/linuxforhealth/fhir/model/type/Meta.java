/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to 
 * the content might not always be associated with version changes to the resource.
 */
@Constraint(
    id = "meta-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/security-labels",
    expression = "security.exists() implies (security.all(memberOf('http://hl7.org/fhir/ValueSet/security-labels', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/Meta",
    generated = true
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class Meta extends Element {
    @Summary
    private final Id versionId;
    @Summary
    private final Instant lastUpdated;
    @Summary
    private final Uri source;
    @Summary
    private final List<Canonical> profile;
    @Summary
    @Binding(
        bindingName = "SecurityLabels",
        strength = BindingStrength.Value.EXTENSIBLE,
        valueSet = "http://hl7.org/fhir/ValueSet/security-labels"
    )
    private final List<Coding> security;
    @Summary
    @Binding(
        bindingName = "Tags",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/common-tags"
    )
    private final List<Coding> tag;

    private Meta(Builder builder) {
        super(builder);
        versionId = builder.versionId;
        lastUpdated = builder.lastUpdated;
        source = builder.source;
        profile = Collections.unmodifiableList(builder.profile);
        security = Collections.unmodifiableList(builder.security);
        tag = Collections.unmodifiableList(builder.tag);
    }

    /**
     * The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource 
     * is created, updated, or deleted.
     * 
     * @return
     *     An immutable object of type {@link Id} that may be null.
     */
    public Id getVersionId() {
        return versionId;
    }

    /**
     * When the resource last changed - e.g. when the version changed.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getLastUpdated() {
        return lastUpdated;
    }

    /**
     * A uri that identifies the source system of the resource. This provides a minimal amount of [Provenance](provenance.
     * html#) information that can be used to track or differentiate the source of information in the resource. The source 
     * may identify another FHIR server, document, message, database, etc.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getSource() {
        return source;
    }

    /**
     * A list of profiles (references to [StructureDefinition](structuredefinition.html#) resources) that this resource 
     * claims to conform to. The URL is a reference to [StructureDefinition.url](structuredefinition-definitions.
     * html#StructureDefinition.url).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getProfile() {
        return profile;
    }

    /**
     * Security labels applied to this resource. These tags connect specific resources to the overall security policy and 
     * infrastructure.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getSecurity() {
        return security;
    }

    /**
     * Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, 
     * and applications are not required to consider the tags when interpreting the meaning of a resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getTag() {
        return tag;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (versionId != null) || 
            (lastUpdated != null) || 
            (source != null) || 
            !profile.isEmpty() || 
            !security.isEmpty() || 
            !tag.isEmpty();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(versionId, "versionId", visitor);
                accept(lastUpdated, "lastUpdated", visitor);
                accept(source, "source", visitor);
                accept(profile, "profile", visitor, Canonical.class);
                accept(security, "security", visitor, Coding.class);
                accept(tag, "tag", visitor, Coding.class);
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
        Meta other = (Meta) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(versionId, other.versionId) && 
            Objects.equals(lastUpdated, other.lastUpdated) && 
            Objects.equals(source, other.source) && 
            Objects.equals(profile, other.profile) && 
            Objects.equals(security, other.security) && 
            Objects.equals(tag, other.tag);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                versionId, 
                lastUpdated, 
                source, 
                profile, 
                security, 
                tag);
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

    public static class Builder extends Element.Builder {
        private Id versionId;
        private Instant lastUpdated;
        private Uri source;
        private List<Canonical> profile = new ArrayList<>();
        private List<Coding> security = new ArrayList<>();
        private List<Coding> tag = new ArrayList<>();

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
         * The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource 
         * is created, updated, or deleted.
         * 
         * @param versionId
         *     Version specific identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder versionId(Id versionId) {
            this.versionId = versionId;
            return this;
        }

        /**
         * Convenience method for setting {@code lastUpdated}.
         * 
         * @param lastUpdated
         *     When the resource version last changed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lastUpdated(org.linuxforhealth.fhir.model.type.Instant)
         */
        public Builder lastUpdated(java.time.ZonedDateTime lastUpdated) {
            this.lastUpdated = (lastUpdated == null) ? null : Instant.of(lastUpdated);
            return this;
        }

        /**
         * When the resource last changed - e.g. when the version changed.
         * 
         * @param lastUpdated
         *     When the resource version last changed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastUpdated(Instant lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * A uri that identifies the source system of the resource. This provides a minimal amount of [Provenance](provenance.
         * html#) information that can be used to track or differentiate the source of information in the resource. The source 
         * may identify another FHIR server, document, message, database, etc.
         * 
         * @param source
         *     Identifies where the resource comes from
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Uri source) {
            this.source = source;
            return this;
        }

        /**
         * A list of profiles (references to [StructureDefinition](structuredefinition.html#) resources) that this resource 
         * claims to conform to. The URL is a reference to [StructureDefinition.url](structuredefinition-definitions.
         * html#StructureDefinition.url).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param profile
         *     Profiles this resource claims to conform to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder profile(Canonical... profile) {
            for (Canonical value : profile) {
                this.profile.add(value);
            }
            return this;
        }

        /**
         * A list of profiles (references to [StructureDefinition](structuredefinition.html#) resources) that this resource 
         * claims to conform to. The URL is a reference to [StructureDefinition.url](structuredefinition-definitions.
         * html#StructureDefinition.url).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param profile
         *     Profiles this resource claims to conform to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder profile(Collection<Canonical> profile) {
            this.profile = new ArrayList<>(profile);
            return this;
        }

        /**
         * Security labels applied to this resource. These tags connect specific resources to the overall security policy and 
         * infrastructure.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param security
         *     Security Labels applied to this resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder security(Coding... security) {
            for (Coding value : security) {
                this.security.add(value);
            }
            return this;
        }

        /**
         * Security labels applied to this resource. These tags connect specific resources to the overall security policy and 
         * infrastructure.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param security
         *     Security Labels applied to this resource
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder security(Collection<Coding> security) {
            this.security = new ArrayList<>(security);
            return this;
        }

        /**
         * Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, 
         * and applications are not required to consider the tags when interpreting the meaning of a resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param tag
         *     Tags applied to this resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder tag(Coding... tag) {
            for (Coding value : tag) {
                this.tag.add(value);
            }
            return this;
        }

        /**
         * Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, 
         * and applications are not required to consider the tags when interpreting the meaning of a resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param tag
         *     Tags applied to this resource
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder tag(Collection<Coding> tag) {
            this.tag = new ArrayList<>(tag);
            return this;
        }

        /**
         * Build the {@link Meta}
         * 
         * @return
         *     An immutable object of type {@link Meta}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Meta per the base specification
         */
        @Override
        public Meta build() {
            Meta meta = new Meta(this);
            if (validating) {
                validate(meta);
            }
            return meta;
        }

        protected void validate(Meta meta) {
            super.validate(meta);
            ValidationSupport.checkList(meta.profile, "profile", Canonical.class);
            ValidationSupport.checkList(meta.security, "security", Coding.class);
            ValidationSupport.checkList(meta.tag, "tag", Coding.class);
            ValidationSupport.requireValueOrChildren(meta);
        }

        protected Builder from(Meta meta) {
            super.from(meta);
            versionId = meta.versionId;
            lastUpdated = meta.lastUpdated;
            source = meta.source;
            profile.addAll(meta.profile);
            security.addAll(meta.security);
            tag.addAll(meta.tag);
            return this;
        }
    }
}
