/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.NarrativeStatus;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A human-readable summary of the resource conveying the essential clinical and business information for the resource.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Narrative extends Element {
    private final NarrativeStatus status;
    private final org.w3c.dom.Element div;

    private Narrative(Builder builder) {
        super(builder);
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.div = ValidationSupport.requireNonNull(builder.div, "div");
    }

    /**
     * <p>
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or 
     * whether a human authored it and it may contain additional data.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link NarrativeStatus}.
     */
    public NarrativeStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The actual narrative content, a stripped down version of XHTML.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public org.w3c.dom.Element getDiv() {
        return div;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(status, "status", visitor);
                accept(div, "div", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, div);
        builder.id = id;
        builder.extension.addAll(extension);
        return builder;
    }

    public static Builder builder(NarrativeStatus status, org.w3c.dom.Element div) {
        return new Builder(status, div);
    }

    public static class Builder extends Element.Builder {
        // required
        private final NarrativeStatus status;
        private final org.w3c.dom.Element div;

        private Builder(NarrativeStatus status, org.w3c.dom.Element div) {
            super();
            this.status = status;
            this.div = div;
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
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Narrative build() {
            return new Narrative(this);
        }
    }
}
