/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.builder.AbstractBuilder;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.AbstractVisitable;

/**
 * Base definition for all elements in a resource.
 */
@Constraint(
    id = "ele-1",
    level = "Rule",
    location = "(base)",
    description = "All FHIR elements must have a @value or children unless an empty Parameters resource",
    expression = "hasValue() or (children().count() > id.count()) or $this is Parameters",
    source = "http://hl7.org/fhir/StructureDefinition/Element",
    modelChecked = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public abstract class Element extends AbstractVisitable {
    protected final java.lang.String id;
    protected final List<Extension> extension;

    protected volatile int hashCode;

    protected Element(Builder builder) {
        id = builder.id;
        extension = Collections.unmodifiableList(builder.extension);
    }

    /**
     * Unique id for the element within a resource (for internal references). This may be any string value that does not 
     * contain spaces.
     * 
     * @return
     *     An immutable object of type {@link java.lang.String} that may be null.
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * May be used to represent additional information that is not part of the basic definition of the element. To make the 
     * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
     * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
     * of the definition of the extension.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Extension} that may be empty.
     */
    public List<Extension> getExtension() {
        return extension;
    }

    /**
     * @return
     *     true if the element can be cast to the requested elementType
     */
    public <T extends Element> boolean is(Class<T> elementType) {
        return elementType.isInstance(this);
    }

    /**
     * @throws ClassCastException
     *     when this element cannot be cast to the requested elementType
     */
    public <T extends Element> T as(Class<T> elementType) {
        return elementType.cast(this);
    }

    /**
     * @return
     *     true if the element is a FHIR primitive type and has a primitive value (as opposed to not having a value and just 
     *     having extensions), otherwise false
     */
    public boolean hasValue() {
        return false;
    }

    public boolean hasChildren() {
        return !extension.isEmpty();
    }

    /**
     * Create a new Builder from the contents of this Element
     */
    public abstract Builder toBuilder();

    public static abstract class Builder extends AbstractBuilder<Element> {
        protected java.lang.String id;
        protected List<Extension> extension = new ArrayList<>();

        protected Builder() {
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
        public Builder id(java.lang.String id) {
            this.id = id;
            return this;
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
        public Builder extension(Extension... extension) {
            for (Extension value : extension) {
                this.extension.add(value);
            }
            return this;
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
        public Builder extension(Collection<Extension> extension) {
            this.extension = new ArrayList<>(extension);
            return this;
        }

        @Override
        public abstract Element build();

        protected void validate(Element element) {
            ValidationSupport.checkList(element.extension, "extension", Extension.class);
            ValidationSupport.checkString(element.id);
        }

        protected Builder from(Element element) {
            id = element.id;
            extension.addAll(element.extension);
            return this;
        }
    }
}
