/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A text note which also contains information about who made the statement and when.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Annotation extends Element {
    private final Element author;
    private final DateTime time;
    private final Markdown text;

    private volatile int hashCode;

    private Annotation(Builder builder) {
        super(builder);
        author = ValidationSupport.choiceElement(builder.author, "author", Reference.class, String.class);
        time = builder.time;
        text = ValidationSupport.requireNonNull(builder.text, "text");
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * <p>
     * The individual responsible for making the annotation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getAuthor() {
        return author;
    }

    /**
     * <p>
     * Indicates when this particular annotation was made.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getTime() {
        return time;
    }

    /**
     * <p>
     * The text of the annotation in markdown format.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getText() {
        return text;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (author != null) || 
            (time != null) || 
            (text != null);
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(author, "author", visitor);
                accept(time, "time", visitor);
                accept(text, "text", visitor);
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
        Annotation other = (Annotation) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(author, other.author) && 
            Objects.equals(time, other.time) && 
            Objects.equals(text, other.text);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                author, 
                time, 
                text);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(text).from(this);
    }

    public Builder toBuilder(Markdown text) {
        return new Builder(text).from(this);
    }

    public static Builder builder(Markdown text) {
        return new Builder(text);
    }

    public static class Builder extends Element.Builder {
        // required
        private final Markdown text;

        // optional
        private Element author;
        private DateTime time;

        private Builder(Markdown text) {
            super();
            this.text = text;
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
         * Adds new element(s) to existing list
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
         * The individual responsible for making the annotation.
         * </p>
         * 
         * @param author
         *     Individual responsible for the annotation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Element author) {
            this.author = author;
            return this;
        }

        /**
         * <p>
         * Indicates when this particular annotation was made.
         * </p>
         * 
         * @param time
         *     When the annotation was made
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder time(DateTime time) {
            this.time = time;
            return this;
        }

        @Override
        public Annotation build() {
            return new Annotation(this);
        }

        private Builder from(Annotation annotation) {
            id = annotation.id;
            extension.addAll(annotation.extension);
            author = annotation.author;
            time = annotation.time;
            return this;
        }
    }
}
