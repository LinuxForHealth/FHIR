/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.builder.AbstractBuilder;
import com.ibm.watsonhealth.fhir.model.visitor.AbstractVisitable;

/**
 * <p>
 * Base definition for all elements in a resource.
 * </p>
 */
@Constraint(
    id = "ele-1",
    level = "Rule",
    location = "(base)",
    description = "All FHIR elements must have a @value or children",
    expression = "hasValue() or (children().count() > id.count())",
    modelChecked = true
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public abstract class Element extends AbstractVisitable {
    protected final java.lang.String id;
    protected final List<Extension> extension;

    protected Element(Builder builder) {
        id = builder.id;
        extension = Collections.unmodifiableList(builder.extension);
    }

    /**
     * <p>
     * Unique id for the element within a resource (for internal references). This may be any string value that does not 
     * contain spaces.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link java.lang.String}.
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * <p>
     * May be used to represent additional information that is not part of the basic definition of the element. To make the 
     * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
     * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
     * of the definition of the extension.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Extension}.
     */
    public List<Extension> getExtension() {
        return extension;
    }

    public <T extends Element> boolean is(Class<T> elementType) {
        return elementType.isInstance(this);
    }

    public <T extends Element> T as(Class<T> elementType) {
        return elementType.cast(this);
    }

    public boolean hasValue() {
        return false;
    }

    public boolean hasChildren() {
        return !extension.isEmpty();
    }

    public abstract Builder toBuilder();

    public static abstract class Builder extends AbstractBuilder<Element> {
        // optional
        protected java.lang.String id;
        protected List<Extension> extension = new ArrayList<>();

        protected Builder() {
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
        public Builder id(java.lang.String id) {
            this.id = id;
            return this;
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
        public Builder extension(Extension... extension) {
            for (Extension value : extension) {
                this.extension.add(value);
            }
            return this;
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
        public Builder extension(Collection<Extension> extension) {
            this.extension = new ArrayList<>(extension);
            return this;
        }

        @Override
        public abstract Element build();
    }
}
