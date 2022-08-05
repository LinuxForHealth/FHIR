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

@System("http://terminology.hl7.org/CodeSystem/subscription-search-modifier")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SubscriptionTopicFilterBySearchModifier extends Code {
    /**
     * =
     * 
     * <p>Used to match a value according to FHIR Search rules (e.g., Patient/123, Encounter/2002).
     */
    public static final SubscriptionTopicFilterBySearchModifier EQUALS = SubscriptionTopicFilterBySearchModifier.builder().value(Value.EQUALS).build();

    /**
     * Equal
     * 
     * <p>The value for the parameter in the resource is equal to the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier EQ = SubscriptionTopicFilterBySearchModifier.builder().value(Value.EQ).build();

    /**
     * Not Equal
     * 
     * <p>The value for the parameter in the resource is not equal to the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier NE = SubscriptionTopicFilterBySearchModifier.builder().value(Value.NE).build();

    /**
     * Greater Than
     * 
     * <p>The value for the parameter in the resource is greater than the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier GT = SubscriptionTopicFilterBySearchModifier.builder().value(Value.GT).build();

    /**
     * Less Than
     * 
     * <p>The value for the parameter in the resource is less than the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier LT = SubscriptionTopicFilterBySearchModifier.builder().value(Value.LT).build();

    /**
     * Greater Than or Equal
     * 
     * <p>The value for the parameter in the resource is greater or equal to the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier GE = SubscriptionTopicFilterBySearchModifier.builder().value(Value.GE).build();

    /**
     * Less Than or Equal
     * 
     * <p>The value for the parameter in the resource is less or equal to the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier LE = SubscriptionTopicFilterBySearchModifier.builder().value(Value.LE).build();

    /**
     * Starts After
     * 
     * <p>The value for the parameter in the resource starts after the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier SA = SubscriptionTopicFilterBySearchModifier.builder().value(Value.SA).build();

    /**
     * Ends Before
     * 
     * <p>The value for the parameter in the resource ends before the provided value.
     */
    public static final SubscriptionTopicFilterBySearchModifier EB = SubscriptionTopicFilterBySearchModifier.builder().value(Value.EB).build();

    /**
     * Approximately
     * 
     * <p>The value for the parameter in the resource is approximately the same to the provided value. Note that the 
     * recommended value for the approximation is 10% of the stated value (or for a date, 10% of the gap between now and the 
     * date), but systems may choose other values where appropriate.
     */
    public static final SubscriptionTopicFilterBySearchModifier AP = SubscriptionTopicFilterBySearchModifier.builder().value(Value.AP).build();

    /**
     * Above
     * 
     * <p>The search parameter is a concept with the form [system]|[code], and the search parameter tests whether the coding 
     * in a resource subsumes the specified search code.
     */
    public static final SubscriptionTopicFilterBySearchModifier ABOVE = SubscriptionTopicFilterBySearchModifier.builder().value(Value.ABOVE).build();

    /**
     * Below
     * 
     * <p>The search parameter is a concept with the form [system]|[code], and the search parameter tests whether the coding 
     * in a resource is subsumed by the specified search code.
     */
    public static final SubscriptionTopicFilterBySearchModifier BELOW = SubscriptionTopicFilterBySearchModifier.builder().value(Value.BELOW).build();

    /**
     * In
     * 
     * <p>The search parameter is a member of a Group or List, or the search parameter is a URI (relative or absolute) that 
     * identifies a value set, and the search parameter tests whether the value is present in the specified Group, List, or 
     * Value Set.
     */
    public static final SubscriptionTopicFilterBySearchModifier IN = SubscriptionTopicFilterBySearchModifier.builder().value(Value.IN).build();

    /**
     * Not In
     * 
     * <p>The search parameter is a member of a Group or List, or the search parameter is a URI (relative or absolute) that 
     * identifies a value set, and the search parameter tests whether the value is NOT present in the specified Group, List, 
     * or Value Set.
     */
    public static final SubscriptionTopicFilterBySearchModifier NOT_IN = SubscriptionTopicFilterBySearchModifier.builder().value(Value.NOT_IN).build();

    /**
     * Of Type
     * 
     * <p>The search parameter has the format system|code|value, where the system and code refer to a Identifier.type.coding.
     * system and .code, and match if any of the type codes match. All 3 parts must be present.
     */
    public static final SubscriptionTopicFilterBySearchModifier OF_TYPE = SubscriptionTopicFilterBySearchModifier.builder().value(Value.OF_TYPE).build();

    private volatile int hashCode;

    private SubscriptionTopicFilterBySearchModifier(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SubscriptionTopicFilterBySearchModifier as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SubscriptionTopicFilterBySearchModifier objects from a passed enum value.
     */
    public static SubscriptionTopicFilterBySearchModifier of(Value value) {
        switch (value) {
        case EQUALS:
            return EQUALS;
        case EQ:
            return EQ;
        case NE:
            return NE;
        case GT:
            return GT;
        case LT:
            return LT;
        case GE:
            return GE;
        case LE:
            return LE;
        case SA:
            return SA;
        case EB:
            return EB;
        case AP:
            return AP;
        case ABOVE:
            return ABOVE;
        case BELOW:
            return BELOW;
        case IN:
            return IN;
        case NOT_IN:
            return NOT_IN;
        case OF_TYPE:
            return OF_TYPE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SubscriptionTopicFilterBySearchModifier objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SubscriptionTopicFilterBySearchModifier of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SubscriptionTopicFilterBySearchModifier objects from a passed string value.
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
     * Inherited factory method for creating SubscriptionTopicFilterBySearchModifier objects from a passed string value.
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
        SubscriptionTopicFilterBySearchModifier other = (SubscriptionTopicFilterBySearchModifier) obj;
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
         *     An enum constant for SubscriptionTopicFilterBySearchModifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SubscriptionTopicFilterBySearchModifier build() {
            SubscriptionTopicFilterBySearchModifier subscriptionTopicFilterBySearchModifier = new SubscriptionTopicFilterBySearchModifier(this);
            if (validating) {
                validate(subscriptionTopicFilterBySearchModifier);
            }
            return subscriptionTopicFilterBySearchModifier;
        }

        protected void validate(SubscriptionTopicFilterBySearchModifier subscriptionTopicFilterBySearchModifier) {
            super.validate(subscriptionTopicFilterBySearchModifier);
        }

        protected Builder from(SubscriptionTopicFilterBySearchModifier subscriptionTopicFilterBySearchModifier) {
            super.from(subscriptionTopicFilterBySearchModifier);
            return this;
        }
    }

    public enum Value {
        /**
         * =
         * 
         * <p>Used to match a value according to FHIR Search rules (e.g., Patient/123, Encounter/2002).
         */
        EQUALS("="),

        /**
         * Equal
         * 
         * <p>The value for the parameter in the resource is equal to the provided value.
         */
        EQ("eq"),

        /**
         * Not Equal
         * 
         * <p>The value for the parameter in the resource is not equal to the provided value.
         */
        NE("ne"),

        /**
         * Greater Than
         * 
         * <p>The value for the parameter in the resource is greater than the provided value.
         */
        GT("gt"),

        /**
         * Less Than
         * 
         * <p>The value for the parameter in the resource is less than the provided value.
         */
        LT("lt"),

        /**
         * Greater Than or Equal
         * 
         * <p>The value for the parameter in the resource is greater or equal to the provided value.
         */
        GE("ge"),

        /**
         * Less Than or Equal
         * 
         * <p>The value for the parameter in the resource is less or equal to the provided value.
         */
        LE("le"),

        /**
         * Starts After
         * 
         * <p>The value for the parameter in the resource starts after the provided value.
         */
        SA("sa"),

        /**
         * Ends Before
         * 
         * <p>The value for the parameter in the resource ends before the provided value.
         */
        EB("eb"),

        /**
         * Approximately
         * 
         * <p>The value for the parameter in the resource is approximately the same to the provided value. Note that the 
         * recommended value for the approximation is 10% of the stated value (or for a date, 10% of the gap between now and the 
         * date), but systems may choose other values where appropriate.
         */
        AP("ap"),

        /**
         * Above
         * 
         * <p>The search parameter is a concept with the form [system]|[code], and the search parameter tests whether the coding 
         * in a resource subsumes the specified search code.
         */
        ABOVE("above"),

        /**
         * Below
         * 
         * <p>The search parameter is a concept with the form [system]|[code], and the search parameter tests whether the coding 
         * in a resource is subsumed by the specified search code.
         */
        BELOW("below"),

        /**
         * In
         * 
         * <p>The search parameter is a member of a Group or List, or the search parameter is a URI (relative or absolute) that 
         * identifies a value set, and the search parameter tests whether the value is present in the specified Group, List, or 
         * Value Set.
         */
        IN("in"),

        /**
         * Not In
         * 
         * <p>The search parameter is a member of a Group or List, or the search parameter is a URI (relative or absolute) that 
         * identifies a value set, and the search parameter tests whether the value is NOT present in the specified Group, List, 
         * or Value Set.
         */
        NOT_IN("not-in"),

        /**
         * Of Type
         * 
         * <p>The search parameter has the format system|code|value, where the system and code refer to a Identifier.type.coding.
         * system and .code, and match if any of the type codes match. All 3 parts must be present.
         */
        OF_TYPE("of-type");

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
         * Factory method for creating SubscriptionTopicFilterBySearchModifier.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SubscriptionTopicFilterBySearchModifier.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "=":
                return EQUALS;
            case "eq":
                return EQ;
            case "ne":
                return NE;
            case "gt":
                return GT;
            case "lt":
                return LT;
            case "ge":
                return GE;
            case "le":
                return LE;
            case "sa":
                return SA;
            case "eb":
                return EB;
            case "ap":
                return AP;
            case "above":
                return ABOVE;
            case "below":
                return BELOW;
            case "in":
                return IN;
            case "not-in":
                return NOT_IN;
            case "of-type":
                return OF_TYPE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
