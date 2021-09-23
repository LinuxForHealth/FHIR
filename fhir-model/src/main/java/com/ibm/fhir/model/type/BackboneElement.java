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

import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;

/**
 * Base definition for all elements that are defined inside a resource - but not those in a data type.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public abstract class BackboneElement extends Element {
    @Summary
    protected final List<Extension> modifierExtension;

    protected BackboneElement(Builder builder) {
        super(builder);
        modifierExtension = Collections.unmodifiableList(builder.modifierExtension);
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
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Extension} that may be empty.
     */
    public List<Extension> getModifierExtension() {
        return modifierExtension;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !modifierExtension.isEmpty();
    }

    @Override
    public abstract Builder toBuilder();

    public static abstract class Builder extends Element.Builder {
        protected List<Extension> modifierExtension = new ArrayList<>();

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
        public Builder modifierExtension(Extension... modifierExtension) {
            for (Extension value : modifierExtension) {
                this.modifierExtension.add(value);
            }
            return this;
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
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            this.modifierExtension = new ArrayList<>(modifierExtension);
            return this;
        }

        @Override
        public abstract BackboneElement build();

        protected void validate(BackboneElement backboneElement) {
            super.validate(backboneElement);
            ValidationSupport.checkList(backboneElement.modifierExtension, "modifierExtension", Extension.class);
        }

        protected Builder from(BackboneElement backboneElement) {
            super.from(backboneElement);
            modifierExtension.addAll(backboneElement.modifierExtension);
            return this;
        }
    }
}
