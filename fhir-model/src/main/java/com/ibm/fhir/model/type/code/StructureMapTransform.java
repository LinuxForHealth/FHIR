/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/map-transform")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class StructureMapTransform extends Code {
    /**
     * create
     * 
     * <p>create(type : string) - type is passed through to the application on the standard API, and must be known by it.
     */
    public static final StructureMapTransform CREATE = StructureMapTransform.builder().value(ValueSet.CREATE).build();

    /**
     * copy
     * 
     * <p>copy(source).
     */
    public static final StructureMapTransform COPY = StructureMapTransform.builder().value(ValueSet.COPY).build();

    /**
     * truncate
     * 
     * <p>truncate(source, length) - source must be stringy type.
     */
    public static final StructureMapTransform TRUNCATE = StructureMapTransform.builder().value(ValueSet.TRUNCATE).build();

    /**
     * escape
     * 
     * <p>escape(source, fmt1, fmt2) - change source from one kind of escaping to another (plain, java, xml, json). note that 
     * this is for when the string itself is escaped.
     */
    public static final StructureMapTransform ESCAPE = StructureMapTransform.builder().value(ValueSet.ESCAPE).build();

    /**
     * cast
     * 
     * <p>cast(source, type?) - case source from one type to another. target type can be left as implicit if there is one and 
     * only one target type known.
     */
    public static final StructureMapTransform CAST = StructureMapTransform.builder().value(ValueSet.CAST).build();

    /**
     * append
     * 
     * <p>append(source...) - source is element or string.
     */
    public static final StructureMapTransform APPEND = StructureMapTransform.builder().value(ValueSet.APPEND).build();

    /**
     * translate
     * 
     * <p>translate(source, uri_of_map) - use the translate operation.
     */
    public static final StructureMapTransform TRANSLATE = StructureMapTransform.builder().value(ValueSet.TRANSLATE).build();

    /**
     * reference
     * 
     * <p>reference(source : object) - return a string that references the provided tree properly.
     */
    public static final StructureMapTransform REFERENCE = StructureMapTransform.builder().value(ValueSet.REFERENCE).build();

    /**
     * dateOp
     * 
     * <p>Perform a date operation. *Parameters to be documented*.
     */
    public static final StructureMapTransform DATE_OP = StructureMapTransform.builder().value(ValueSet.DATE_OP).build();

    /**
     * uuid
     * 
     * <p>Generate a random UUID (in lowercase). No Parameters.
     */
    public static final StructureMapTransform UUID = StructureMapTransform.builder().value(ValueSet.UUID).build();

    /**
     * pointer
     * 
     * <p>Return the appropriate string to put in a reference that refers to the resource provided as a parameter.
     */
    public static final StructureMapTransform POINTER = StructureMapTransform.builder().value(ValueSet.POINTER).build();

    /**
     * evaluate
     * 
     * <p>Execute the supplied FHIRPath expression and use the value returned by that.
     */
    public static final StructureMapTransform EVALUATE = StructureMapTransform.builder().value(ValueSet.EVALUATE).build();

    /**
     * cc
     * 
     * <p>Create a CodeableConcept. Parameters = (text) or (system. Code[, display]).
     */
    public static final StructureMapTransform CC = StructureMapTransform.builder().value(ValueSet.CC).build();

    /**
     * c
     * 
     * <p>Create a Coding. Parameters = (system. Code[, display]).
     */
    public static final StructureMapTransform C = StructureMapTransform.builder().value(ValueSet.C).build();

    /**
     * qty
     * 
     * <p>Create a quantity. Parameters = (text) or (value, unit, [system, code]) where text is the natural representation e.
     * g. [comparator]value[space]unit.
     */
    public static final StructureMapTransform QTY = StructureMapTransform.builder().value(ValueSet.QTY).build();

    /**
     * id
     * 
     * <p>Create an identifier. Parameters = (system, value[, type]) where type is a code from the identifier type value set.
     */
    public static final StructureMapTransform ID = StructureMapTransform.builder().value(ValueSet.ID).build();

    /**
     * cp
     * 
     * <p>Create a contact details. Parameters = (value) or (system, value). If no system is provided, the system should be 
     * inferred from the content of the value.
     */
    public static final StructureMapTransform CP = StructureMapTransform.builder().value(ValueSet.CP).build();

    private volatile int hashCode;

    private StructureMapTransform(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating StructureMapTransform objects from a passed enum value.
     */
    public static StructureMapTransform of(ValueSet value) {
        switch (value) {
        case CREATE:
            return CREATE;
        case COPY:
            return COPY;
        case TRUNCATE:
            return TRUNCATE;
        case ESCAPE:
            return ESCAPE;
        case CAST:
            return CAST;
        case APPEND:
            return APPEND;
        case TRANSLATE:
            return TRANSLATE;
        case REFERENCE:
            return REFERENCE;
        case DATE_OP:
            return DATE_OP;
        case UUID:
            return UUID;
        case POINTER:
            return POINTER;
        case EVALUATE:
            return EVALUATE;
        case CC:
            return CC;
        case C:
            return C;
        case QTY:
            return QTY;
        case ID:
            return ID;
        case CP:
            return CP;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating StructureMapTransform objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static StructureMapTransform of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating StructureMapTransform objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating StructureMapTransform objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        StructureMapTransform other = (StructureMapTransform) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public StructureMapTransform build() {
            return new StructureMapTransform(this);
        }
    }

    public enum ValueSet {
        /**
         * create
         * 
         * <p>create(type : string) - type is passed through to the application on the standard API, and must be known by it.
         */
        CREATE("create"),

        /**
         * copy
         * 
         * <p>copy(source).
         */
        COPY("copy"),

        /**
         * truncate
         * 
         * <p>truncate(source, length) - source must be stringy type.
         */
        TRUNCATE("truncate"),

        /**
         * escape
         * 
         * <p>escape(source, fmt1, fmt2) - change source from one kind of escaping to another (plain, java, xml, json). note that 
         * this is for when the string itself is escaped.
         */
        ESCAPE("escape"),

        /**
         * cast
         * 
         * <p>cast(source, type?) - case source from one type to another. target type can be left as implicit if there is one and 
         * only one target type known.
         */
        CAST("cast"),

        /**
         * append
         * 
         * <p>append(source...) - source is element or string.
         */
        APPEND("append"),

        /**
         * translate
         * 
         * <p>translate(source, uri_of_map) - use the translate operation.
         */
        TRANSLATE("translate"),

        /**
         * reference
         * 
         * <p>reference(source : object) - return a string that references the provided tree properly.
         */
        REFERENCE("reference"),

        /**
         * dateOp
         * 
         * <p>Perform a date operation. *Parameters to be documented*.
         */
        DATE_OP("dateOp"),

        /**
         * uuid
         * 
         * <p>Generate a random UUID (in lowercase). No Parameters.
         */
        UUID("uuid"),

        /**
         * pointer
         * 
         * <p>Return the appropriate string to put in a reference that refers to the resource provided as a parameter.
         */
        POINTER("pointer"),

        /**
         * evaluate
         * 
         * <p>Execute the supplied FHIRPath expression and use the value returned by that.
         */
        EVALUATE("evaluate"),

        /**
         * cc
         * 
         * <p>Create a CodeableConcept. Parameters = (text) or (system. Code[, display]).
         */
        CC("cc"),

        /**
         * c
         * 
         * <p>Create a Coding. Parameters = (system. Code[, display]).
         */
        C("c"),

        /**
         * qty
         * 
         * <p>Create a quantity. Parameters = (text) or (value, unit, [system, code]) where text is the natural representation e.
         * g. [comparator]value[space]unit.
         */
        QTY("qty"),

        /**
         * id
         * 
         * <p>Create an identifier. Parameters = (system, value[, type]) where type is a code from the identifier type value set.
         */
        ID("id"),

        /**
         * cp
         * 
         * <p>Create a contact details. Parameters = (value) or (system, value). If no system is provided, the system should be 
         * inferred from the content of the value.
         */
        CP("cp");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating StructureMapTransform.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
