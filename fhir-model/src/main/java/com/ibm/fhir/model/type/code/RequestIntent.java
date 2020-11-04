/*
 * (C) Copyright IBM Corp. 2019, 2020
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

@System("http://hl7.org/fhir/request-intent")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class RequestIntent extends Code {
    /**
     * Proposal
     * 
     * <p>The request is a suggestion made by someone/something that does not have an intention to ensure it occurs and 
     * without providing an authorization to act.
     */
    public static final RequestIntent PROPOSAL = RequestIntent.builder().value(ValueSet.PROPOSAL).build();

    /**
     * Plan
     * 
     * <p>The request represents an intention to ensure something occurs without providing an authorization for others to act.
     */
    public static final RequestIntent PLAN = RequestIntent.builder().value(ValueSet.PLAN).build();

    /**
     * Directive
     * 
     * <p>The request represents a legally binding instruction authored by a Patient or RelatedPerson.
     */
    public static final RequestIntent DIRECTIVE = RequestIntent.builder().value(ValueSet.DIRECTIVE).build();

    /**
     * Order
     * 
     * <p>The request represents a request/demand and authorization for action by a Practitioner.
     */
    public static final RequestIntent ORDER = RequestIntent.builder().value(ValueSet.ORDER).build();

    /**
     * Original Order
     * 
     * <p>The request represents an original authorization for action.
     */
    public static final RequestIntent ORIGINAL_ORDER = RequestIntent.builder().value(ValueSet.ORIGINAL_ORDER).build();

    /**
     * Reflex Order
     * 
     * <p>The request represents an automatically generated supplemental authorization for action based on a parent 
     * authorization together with initial results of the action taken against that parent authorization.
     */
    public static final RequestIntent REFLEX_ORDER = RequestIntent.builder().value(ValueSet.REFLEX_ORDER).build();

    /**
     * Filler Order
     * 
     * <p>The request represents the view of an authorization instantiated by a fulfilling system representing the details of 
     * the fulfiller's intention to act upon a submitted order.
     */
    public static final RequestIntent FILLER_ORDER = RequestIntent.builder().value(ValueSet.FILLER_ORDER).build();

    /**
     * Instance Order
     * 
     * <p>An order created in fulfillment of a broader order that represents the authorization for a single activity 
     * occurrence. E.g. The administration of a single dose of a drug.
     */
    public static final RequestIntent INSTANCE_ORDER = RequestIntent.builder().value(ValueSet.INSTANCE_ORDER).build();

    /**
     * Option
     * 
     * <p>The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or 
     * other constraints among a set of requests. Refer to [[[RequestGroup]]] for additional information on how this status 
     * is used.
     */
    public static final RequestIntent OPTION = RequestIntent.builder().value(ValueSet.OPTION).build();

    private volatile int hashCode;

    private RequestIntent(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating RequestIntent objects from a passed enum value.
     */
    public static RequestIntent of(ValueSet value) {
        switch (value) {
        case PROPOSAL:
            return PROPOSAL;
        case PLAN:
            return PLAN;
        case DIRECTIVE:
            return DIRECTIVE;
        case ORDER:
            return ORDER;
        case ORIGINAL_ORDER:
            return ORIGINAL_ORDER;
        case REFLEX_ORDER:
            return REFLEX_ORDER;
        case FILLER_ORDER:
            return FILLER_ORDER;
        case INSTANCE_ORDER:
            return INSTANCE_ORDER;
        case OPTION:
            return OPTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating RequestIntent objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static RequestIntent of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating RequestIntent objects from a passed string value.
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
     * Inherited factory method for creating RequestIntent objects from a passed string value.
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
        RequestIntent other = (RequestIntent) obj;
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
        public RequestIntent build() {
            return new RequestIntent(this);
        }
    }

    public enum ValueSet {
        /**
         * Proposal
         * 
         * <p>The request is a suggestion made by someone/something that does not have an intention to ensure it occurs and 
         * without providing an authorization to act.
         */
        PROPOSAL("proposal"),

        /**
         * Plan
         * 
         * <p>The request represents an intention to ensure something occurs without providing an authorization for others to act.
         */
        PLAN("plan"),

        /**
         * Directive
         * 
         * <p>The request represents a legally binding instruction authored by a Patient or RelatedPerson.
         */
        DIRECTIVE("directive"),

        /**
         * Order
         * 
         * <p>The request represents a request/demand and authorization for action by a Practitioner.
         */
        ORDER("order"),

        /**
         * Original Order
         * 
         * <p>The request represents an original authorization for action.
         */
        ORIGINAL_ORDER("original-order"),

        /**
         * Reflex Order
         * 
         * <p>The request represents an automatically generated supplemental authorization for action based on a parent 
         * authorization together with initial results of the action taken against that parent authorization.
         */
        REFLEX_ORDER("reflex-order"),

        /**
         * Filler Order
         * 
         * <p>The request represents the view of an authorization instantiated by a fulfilling system representing the details of 
         * the fulfiller's intention to act upon a submitted order.
         */
        FILLER_ORDER("filler-order"),

        /**
         * Instance Order
         * 
         * <p>An order created in fulfillment of a broader order that represents the authorization for a single activity 
         * occurrence. E.g. The administration of a single dose of a drug.
         */
        INSTANCE_ORDER("instance-order"),

        /**
         * Option
         * 
         * <p>The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or 
         * other constraints among a set of requests. Refer to [[[RequestGroup]]] for additional information on how this status 
         * is used.
         */
        OPTION("option");

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
         * Factory method for creating RequestIntent.ValueSet values from a passed string value.
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
