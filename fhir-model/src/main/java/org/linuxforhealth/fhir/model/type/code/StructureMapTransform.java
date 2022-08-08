/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/map-transform")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class StructureMapTransform extends Code {
    /**
     * create
     * 
     * <p>create(type : string) - type is passed through to the application on the standard API, and must be known by it.
     */
    public static final StructureMapTransform CREATE = StructureMapTransform.builder().value(Value.CREATE).build();

    /**
     * copy
     * 
     * <p>copy(source).
     */
    public static final StructureMapTransform COPY = StructureMapTransform.builder().value(Value.COPY).build();

    /**
     * truncate
     * 
     * <p>truncate(source, length) - source must be stringy type.
     */
    public static final StructureMapTransform TRUNCATE = StructureMapTransform.builder().value(Value.TRUNCATE).build();

    /**
     * escape
     * 
     * <p>escape(source, fmt1, fmt2) - change source from one kind of escaping to another (plain, java, xml, json). note that 
     * this is for when the string itself is escaped.
     */
    public static final StructureMapTransform ESCAPE = StructureMapTransform.builder().value(Value.ESCAPE).build();

    /**
     * cast
     * 
     * <p>cast(source, type?) - case source from one type to another. target type can be left as implicit if there is one and 
     * only one target type known.
     */
    public static final StructureMapTransform CAST = StructureMapTransform.builder().value(Value.CAST).build();

    /**
     * append
     * 
     * <p>append(source...) - source is element or string.
     */
    public static final StructureMapTransform APPEND = StructureMapTransform.builder().value(Value.APPEND).build();

    /**
     * translate
     * 
     * <p>translate(source, uri_of_map) - use the translate operation.
     */
    public static final StructureMapTransform TRANSLATE = StructureMapTransform.builder().value(Value.TRANSLATE).build();

    /**
     * reference
     * 
     * <p>reference(source : object) - return a string that references the provided tree properly.
     */
    public static final StructureMapTransform REFERENCE = StructureMapTransform.builder().value(Value.REFERENCE).build();

    /**
     * dateOp
     * 
     * <p>Perform a date operation. *Parameters to be documented*.
     */
    public static final StructureMapTransform DATE_OP = StructureMapTransform.builder().value(Value.DATE_OP).build();

    /**
     * uuid
     * 
     * <p>Generate a random UUID (in lowercase). No Parameters.
     */
    public static final StructureMapTransform UUID = StructureMapTransform.builder().value(Value.UUID).build();

    /**
     * pointer
     * 
     * <p>Return the appropriate string to put in a reference that refers to the resource provided as a parameter.
     */
    public static final StructureMapTransform POINTER = StructureMapTransform.builder().value(Value.POINTER).build();

    /**
     * evaluate
     * 
     * <p>Execute the supplied FHIRPath expression and use the value returned by that.
     */
    public static final StructureMapTransform EVALUATE = StructureMapTransform.builder().value(Value.EVALUATE).build();

    /**
     * cc
     * 
     * <p>Create a CodeableConcept. Parameters = (text) or (system. Code[, display]).
     */
    public static final StructureMapTransform CC = StructureMapTransform.builder().value(Value.CC).build();

    /**
     * c
     * 
     * <p>Create a Coding. Parameters = (system. Code[, display]).
     */
    public static final StructureMapTransform C = StructureMapTransform.builder().value(Value.C).build();

    /**
     * qty
     * 
     * <p>Create a quantity. Parameters = (text) or (value, unit, [system, code]) where text is the natural representation e.
     * g. [comparator]value[space]unit.
     */
    public static final StructureMapTransform QTY = StructureMapTransform.builder().value(Value.QTY).build();

    /**
     * id
     * 
     * <p>Create an identifier. Parameters = (system, value[, type]) where type is a code from the identifier type value set.
     */
    public static final StructureMapTransform ID = StructureMapTransform.builder().value(Value.ID).build();

    /**
     * cp
     * 
     * <p>Create a contact details. Parameters = (value) or (system, value). If no system is provided, the system should be 
     * inferred from the content of the value.
     */
    public static final StructureMapTransform CP = StructureMapTransform.builder().value(Value.CP).build();

    private volatile int hashCode;

    private StructureMapTransform(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this StructureMapTransform as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating StructureMapTransform objects from a passed enum value.
     */
    public static StructureMapTransform of(Value value) {
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
        return new Builder().from(this);
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for StructureMapTransform
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public StructureMapTransform build() {
            StructureMapTransform structureMapTransform = new StructureMapTransform(this);
            if (validating) {
                validate(structureMapTransform);
            }
            return structureMapTransform;
        }

        protected void validate(StructureMapTransform structureMapTransform) {
            super.validate(structureMapTransform);
        }

        protected Builder from(StructureMapTransform structureMapTransform) {
            super.from(structureMapTransform);
            return this;
        }
    }

    public enum Value {
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

        Value(java.lang.String value) {
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
         * Factory method for creating StructureMapTransform.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding StructureMapTransform.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "create":
                return CREATE;
            case "copy":
                return COPY;
            case "truncate":
                return TRUNCATE;
            case "escape":
                return ESCAPE;
            case "cast":
                return CAST;
            case "append":
                return APPEND;
            case "translate":
                return TRANSLATE;
            case "reference":
                return REFERENCE;
            case "dateOp":
                return DATE_OP;
            case "uuid":
                return UUID;
            case "pointer":
                return POINTER;
            case "evaluate":
                return EVALUATE;
            case "cc":
                return CC;
            case "c":
                return C;
            case "qty":
                return QTY;
            case "id":
                return ID;
            case "cp":
                return CP;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
