/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.RelatedArtifactType;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Related artifacts such as additional documentation, justification, or bibliographic references.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class RelatedArtifact extends Element {
    @Summary
    @Binding(
        bindingName = "RelatedArtifactType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The type of relationship to the related artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/related-artifact-type|4.0.1"
    )
    @Required
    private final RelatedArtifactType type;
    @Summary
    private final String label;
    @Summary
    private final String display;
    @Summary
    private final Markdown citation;
    @Summary
    private final Url url;
    @Summary
    private final Attachment document;
    @Summary
    private final Canonical resource;

    private RelatedArtifact(Builder builder) {
        super(builder);
        type = builder.type;
        label = builder.label;
        display = builder.display;
        citation = builder.citation;
        url = builder.url;
        document = builder.document;
        resource = builder.resource;
    }

    /**
     * The type of relationship to the related artifact.
     * 
     * @return
     *     An immutable object of type {@link RelatedArtifactType} that is non-null.
     */
    public RelatedArtifactType getType() {
        return type;
    }

    /**
     * A short label that can be used to reference the citation from elsewhere in the containing artifact, such as a footnote 
     * index.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getLabel() {
        return label;
    }

    /**
     * A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation 
     * format.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCitation() {
        return citation;
    }

    /**
     * A url for the artifact that can be followed to access the actual content.
     * 
     * @return
     *     An immutable object of type {@link Url} that may be null.
     */
    public Url getUrl() {
        return url;
    }

    /**
     * The document being referenced, represented as an attachment. This is exclusive with the resource element.
     * 
     * @return
     *     An immutable object of type {@link Attachment} that may be null.
     */
    public Attachment getDocument() {
        return document;
    }

    /**
     * The related resource, such as a library, value set, profile, or other knowledge resource.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getResource() {
        return resource;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (type != null) || 
            (label != null) || 
            (display != null) || 
            (citation != null) || 
            (url != null) || 
            (document != null) || 
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
                accept(type, "type", visitor);
                accept(label, "label", visitor);
                accept(display, "display", visitor);
                accept(citation, "citation", visitor);
                accept(url, "url", visitor);
                accept(document, "document", visitor);
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
        RelatedArtifact other = (RelatedArtifact) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(type, other.type) && 
            Objects.equals(label, other.label) && 
            Objects.equals(display, other.display) && 
            Objects.equals(citation, other.citation) && 
            Objects.equals(url, other.url) && 
            Objects.equals(document, other.document) && 
            Objects.equals(resource, other.resource);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                type, 
                label, 
                display, 
                citation, 
                url, 
                document, 
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

    public static class Builder extends Element.Builder {
        private RelatedArtifactType type;
        private String label;
        private String display;
        private Markdown citation;
        private Url url;
        private Attachment document;
        private Canonical resource;

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
         * The type of relationship to the related artifact.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     documentation | justification | citation | predecessor | successor | derived-from | depends-on | composed-of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(RelatedArtifactType type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code label}.
         * 
         * @param label
         *     Short label
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
         * A short label that can be used to reference the citation from elsewhere in the containing artifact, such as a footnote 
         * index.
         * 
         * @param label
         *     Short label
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder label(String label) {
            this.label = label;
            return this;
        }

        /**
         * Convenience method for setting {@code display}.
         * 
         * @param display
         *     Brief description of the related artifact
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
         * A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.
         * 
         * @param display
         *     Brief description of the related artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder display(String display) {
            this.display = display;
            return this;
        }

        /**
         * A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation 
         * format.
         * 
         * @param citation
         *     Bibliographic citation for the artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder citation(Markdown citation) {
            this.citation = citation;
            return this;
        }

        /**
         * A url for the artifact that can be followed to access the actual content.
         * 
         * @param url
         *     Where the artifact can be accessed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Url url) {
            this.url = url;
            return this;
        }

        /**
         * The document being referenced, represented as an attachment. This is exclusive with the resource element.
         * 
         * @param document
         *     What document is being referenced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder document(Attachment document) {
            this.document = document;
            return this;
        }

        /**
         * The related resource, such as a library, value set, profile, or other knowledge resource.
         * 
         * @param resource
         *     What resource is being referenced
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder resource(Canonical resource) {
            this.resource = resource;
            return this;
        }

        /**
         * Build the {@link RelatedArtifact}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link RelatedArtifact}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid RelatedArtifact per the base specification
         */
        @Override
        public RelatedArtifact build() {
            RelatedArtifact relatedArtifact = new RelatedArtifact(this);
            if (validating) {
                validate(relatedArtifact);
            }
            return relatedArtifact;
        }

        protected void validate(RelatedArtifact relatedArtifact) {
            super.validate(relatedArtifact);
            ValidationSupport.requireNonNull(relatedArtifact.type, "type");
            ValidationSupport.requireValueOrChildren(relatedArtifact);
        }

        protected Builder from(RelatedArtifact relatedArtifact) {
            super.from(relatedArtifact);
            type = relatedArtifact.type;
            label = relatedArtifact.label;
            display = relatedArtifact.display;
            citation = relatedArtifact.citation;
            url = relatedArtifact.url;
            document = relatedArtifact.document;
            resource = relatedArtifact.resource;
            return this;
        }
    }
}
