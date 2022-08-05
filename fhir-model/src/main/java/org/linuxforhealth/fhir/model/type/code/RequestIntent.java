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

@System("http://hl7.org/fhir/request-intent")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class RequestIntent extends Code {
    /**
     * Proposal
     * 
     * <p>The request is a suggestion made by someone/something that does not have an intention to ensure it occurs and 
     * without providing an authorization to act.
     */
    public static final RequestIntent PROPOSAL = RequestIntent.builder().value(Value.PROPOSAL).build();

    /**
     * Plan
     * 
     * <p>The request represents an intention to ensure something occurs without providing an authorization for others to act.
     */
    public static final RequestIntent PLAN = RequestIntent.builder().value(Value.PLAN).build();

    /**
     * Directive
     * 
     * <p>The request represents a legally binding instruction authored by a Patient or RelatedPerson.
     */
    public static final RequestIntent DIRECTIVE = RequestIntent.builder().value(Value.DIRECTIVE).build();

    /**
     * Order
     * 
     * <p>The request represents a request/demand and authorization for action by a Practitioner.
     */
    public static final RequestIntent ORDER = RequestIntent.builder().value(Value.ORDER).build();

    /**
     * Original Order
     * 
     * <p>The request represents an original authorization for action.
     */
    public static final RequestIntent ORIGINAL_ORDER = RequestIntent.builder().value(Value.ORIGINAL_ORDER).build();

    /**
     * Reflex Order
     * 
     * <p>The request represents an automatically generated supplemental authorization for action based on a parent 
     * authorization together with initial results of the action taken against that parent authorization.
     */
    public static final RequestIntent REFLEX_ORDER = RequestIntent.builder().value(Value.REFLEX_ORDER).build();

    /**
     * Filler Order
     * 
     * <p>The request represents the view of an authorization instantiated by a fulfilling system representing the details of 
     * the fulfiller's intention to act upon a submitted order.
     */
    public static final RequestIntent FILLER_ORDER = RequestIntent.builder().value(Value.FILLER_ORDER).build();

    /**
     * Instance Order
     * 
     * <p>An order created in fulfillment of a broader order that represents the authorization for a single activity 
     * occurrence. E.g. The administration of a single dose of a drug.
     */
    public static final RequestIntent INSTANCE_ORDER = RequestIntent.builder().value(Value.INSTANCE_ORDER).build();

    /**
     * Option
     * 
     * <p>The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or 
     * other constraints among a set of requests. Refer to [[[RequestGroup]]] for additional information on how this status 
     * is used.
     */
    public static final RequestIntent OPTION = RequestIntent.builder().value(Value.OPTION).build();

    private volatile int hashCode;

    private RequestIntent(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this RequestIntent as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating RequestIntent objects from a passed enum value.
     */
    public static RequestIntent of(Value value) {
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
         *     An enum constant for RequestIntent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public RequestIntent build() {
            RequestIntent requestIntent = new RequestIntent(this);
            if (validating) {
                validate(requestIntent);
            }
            return requestIntent;
        }

        protected void validate(RequestIntent requestIntent) {
            super.validate(requestIntent);
        }

        protected Builder from(RequestIntent requestIntent) {
            super.from(requestIntent);
            return this;
        }
    }

    public enum Value {
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
         * Factory method for creating RequestIntent.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding RequestIntent.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "proposal":
                return PROPOSAL;
            case "plan":
                return PLAN;
            case "directive":
                return DIRECTIVE;
            case "order":
                return ORDER;
            case "original-order":
                return ORIGINAL_ORDER;
            case "reflex-order":
                return REFLEX_ORDER;
            case "filler-order":
                return FILLER_ORDER;
            case "instance-order":
                return INSTANCE_ORDER;
            case "option":
                return OPTION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
