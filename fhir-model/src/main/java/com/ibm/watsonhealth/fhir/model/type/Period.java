/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A time period defined by a start and end date and optionally time.
 * </p>
 */
@Constraint(
    id = "per-1",
    level = "Rule",
    location = "(base)",
    description = "If present, start SHALL have a lower value than end",
    expression = "start.hasValue().not() or end.hasValue().not() or (start <= end)"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Period extends Element {
    private final DateTime start;
    private final DateTime end;

    private Period(Builder builder) {
        super(builder);
        start = builder.start;
        end = builder.end;
    }

    /**
     * <p>
     * The start of the period. The boundary is inclusive.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getStart() {
        return start;
    }

    /**
     * <p>
     * The end of the period. If the end of the period is missing, it means no end was known or planned at the time the 
     * instance was created. The start may be in the past, and the end date in the future, which means that period is 
     * expected/planned to end at that time.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getEnd() {
        return end;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(start, "start", visitor);
                accept(end, "end", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.start = start;
        builder.end = end;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        // optional
        private DateTime start;
        private DateTime end;

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

        /**
         * <p>
         * The start of the period. The boundary is inclusive.
         * </p>
         * 
         * @param start
         *     Starting time with inclusive boundary
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder start(DateTime start) {
            this.start = start;
            return this;
        }

        /**
         * <p>
         * The end of the period. If the end of the period is missing, it means no end was known or planned at the time the 
         * instance was created. The start may be in the past, and the end date in the future, which means that period is 
         * expected/planned to end at that time.
         * </p>
         * 
         * @param end
         *     End time with inclusive boundary, if not ongoing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder end(DateTime end) {
            this.end = end;
            return this;
        }

        @Override
        public Period build() {
            return new Period(this);
        }
    }
}
