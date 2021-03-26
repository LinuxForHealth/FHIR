/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathSystemValue} node that wraps a {@link Boolean} value
 */
public class FHIRPathBooleanValue extends FHIRPathAbstractSystemValue {
    public static final FHIRPathBooleanValue TRUE = FHIRPathBooleanValue.booleanValue(true);
    public static final FHIRPathBooleanValue FALSE = FHIRPathBooleanValue.booleanValue(false);

    private final Boolean _boolean;

    protected FHIRPathBooleanValue(Builder builder) {
        super(builder);
        _boolean = Objects.requireNonNull(builder._boolean);
    }

    @Override
    public boolean isBooleanValue() {
        return true;
    }

    /**
     * The boolean value wrapped by this FHIRPathBooleanValue node
     *
     * @return
     *     the boolean value wrapped by this FHIRPathBooleanValue node
     */
    public Boolean _boolean() {
        return _boolean;
    }

    /**
     * Indicates whether the boolean value wrapped by this FHIRPathBooleanValue node is true
     *
     * @return
     *     true if the boolean value wrapped by this FHIRPathBooleanValue node is true, otherwise false
     */
    public boolean isTrue() {
        return Boolean.TRUE.equals(_boolean);
    }

    /**
     * Indicates whether the boolean value wrapped by this FHIRPathBooleanValue node is false
     *
     * @return
     *     true if the boolean value wrapped by this FHIRPathBooleanValue node is false, otherwise false
     */
    public boolean isFalse() {
        return Boolean.FALSE.equals(_boolean);
    }

    /**
     * Static factory method for creating FHIRPathBooleanValue instances from a {@link Boolean} value
     *
     * @param _boolean
     *    the {@link Boolean} value
     * @return
     *    a new FHIRPathBooleanValue instance
     */
    public static FHIRPathBooleanValue booleanValue(Boolean _boolean) {
        return FHIRPathBooleanValue.builder(_boolean).build();
    }

    /**
     * Static factory method for creating named FHIRPathBooleanValue instances from a {@link Boolean} value
     *
     * @param name
     *    the name
     * @param _boolean
     *    the {@link Boolean} value
     * @return
     *    a new named FHIRPathBooleanValue instance
     */
    public static FHIRPathBooleanValue booleanValue(String name, Boolean _boolean) {
        return FHIRPathBooleanValue.builder(_boolean).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, _boolean);
    }

    /**
     * Static factory method for creating builder instances from a {@link Boolean} value
     *
     * @param _boolean
     *     the boolean value
     * @return
     *     a new builder for building FHIRPathBooleanValue instances
     */
    public static Builder builder(Boolean _boolean) {
        return new Builder(FHIRPathType.SYSTEM_BOOLEAN, _boolean);
    }

    public static class Builder extends FHIRPathAbstractSystemValue.Builder {
        private final Boolean _boolean;

        private Builder(FHIRPathType type, Boolean _boolean) {
            super(type);
            this._boolean = _boolean;
        }

        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }

        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }

        /**
         * Build a FHIRPathBooleanValue instance using this builder
         *
         * @return
         *     a new FHIRPathBooleanValue instance
         */
        @Override
        public FHIRPathBooleanValue build() {
            return new FHIRPathBooleanValue(this);
        }
    }

    /**
     * Perform a logical OR operation between this FHIRPathBooleanValue and the parameter
     *
     * @param value
     *     the right operand
     * @return
     *     the result of the logical OR operation
     */
    public FHIRPathBooleanValue or(FHIRPathBooleanValue value) {
        return (_boolean || value._boolean) ? TRUE : FALSE;
    }

    /**
     * Perform a logical XOR operation between this FHIRPathBooleanValue and the parameter
     *
     * @param value
     *     the right operand
     * @return
     *     the result of the logical XOR operation
     */
    public FHIRPathBooleanValue xor(FHIRPathBooleanValue value) {
        return ((_boolean || value._boolean()) && !(_boolean && value._boolean())) ? TRUE : FALSE;
    }

    /**
     * Perform a logical AND operation between this FHIRPathBooleanValue and the parameter
     *
     * @param value
     *     the right operand
     * @return
     *     the result of the logical AND operation
     */
    public FHIRPathBooleanValue and(FHIRPathBooleanValue value) {
        return (_boolean && value._boolean()) ? TRUE : FALSE;
    }

    /**
     * Perform a logical IMPLIES operation between this FHIRPathBooleanValue and the parameter
     *
     * @param value
     *     the right operand
     * @return
     *     the result of the logical IMPLIES operation
     */
    public FHIRPathBooleanValue implies(FHIRPathBooleanValue value) {
        return (!_boolean || value._boolean()) ? TRUE : FALSE;
    }

    /**
     * Perform a logical NOT operation on this FHIRPathBooleanValue
     *
     * @param value
     *     the right operand
     * @return
     *     the result of the logical NOT operation
     */
    public FHIRPathBooleanValue not() {
        return _boolean ? FALSE : TRUE;
    }

    /**
     * Indicates whether this FHIRPathBooleanValue is comparable to the parameter
     *
     * @param
     *     the other {@link FHIRPathNode}
     * @return
     *     true if the parameter or its primitive value is a FHIRPathBooleanValue
     */
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        return other instanceof FHIRPathBooleanValue ||
                other.getValue() instanceof FHIRPathBooleanValue;
    }

    /**
     * Compare the boolean value wrapped by this FHIRPathBooleanValue node to the parameter
     *
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     0 if the boolean value wrapped by this FHIRPathBooleanValue node is equal to the parameter; a positive value if this FHIRPathBooleanValue is true and the parameter is false; and
     *     a negative value if this FHIRPathBooleanValue is false and the parameter is true
     */
    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathBooleanValue) {
            return _boolean.compareTo(((FHIRPathBooleanValue) other)._boolean());
        }
        return _boolean.compareTo(((FHIRPathBooleanValue) other.getValue())._boolean());
    }

    /**
     * Indicates whether the boolean value wrapped by this FHIRPathBooleanValue node is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the boolean value wrapped by this FHIRPathBooleanValue node is equal the parameter (or its primitive value), otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FHIRPathNode)) {
            return false;
        }
        FHIRPathNode other = (FHIRPathNode) obj;
        if (other instanceof FHIRPathBooleanValue) {
            return Objects.equals(_boolean, ((FHIRPathBooleanValue) other)._boolean());
        }
        if (other.getValue() instanceof FHIRPathBooleanValue) {
            return Objects.equals(_boolean, ((FHIRPathBooleanValue) other.getValue())._boolean());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(_boolean);
    }

    @Override
    public String toString() {
        return _boolean ? "true" : "false";
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
