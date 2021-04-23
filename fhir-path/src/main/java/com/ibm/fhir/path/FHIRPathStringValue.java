/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathSystemValue} node that wraps a {@link String} value
 */
public class FHIRPathStringValue extends FHIRPathAbstractSystemValue {
    public static final FHIRPathStringValue EMPTY_STRING = stringValue("");

    private final String string;

    protected FHIRPathStringValue(Builder builder) {
        super(builder);
        string = builder.string;
    }

    @Override
    public boolean isStringValue() {
        return true;
    }

    /**
     * The {@link String} value wrapped by this FHIRPathStringValue
     *
     * @return
     *     the {@link String} value wrapped by this FHIRPathStringValue
     */
    public String string() {
        return string;
    }

    /**
     * Static factory method for creating FHIRPathStringValue instances from a {@link String} value
     *
     * @param string
     *     the {@link String} value
     * @return
     *     a new FHIRPathStringValue instance
     */
    public static FHIRPathStringValue stringValue(String string) {
        return FHIRPathStringValue.builder(string).build();
    }

    /**
     * Static factory method for creating named FHIRPathStringValue instances from a {@link String} value
     *
     * @param name
     *     the name
     * @param string
     *     the {@link String} value
     * @return
     *     a new named FHIRPathStringValue instance
     */
    public static FHIRPathStringValue stringValue(String name, String string) {
        return FHIRPathStringValue.builder(string).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, string);
    }

    /**
     * Static factory method for creating builder instances from a {@link String} value
     *
     * @param string
     *     the {@link String} value
     * @return
     *     a new builder for building FHIRPathStringValue instances
     */
    public static Builder builder(String string) {
        return new Builder(FHIRPathType.SYSTEM_STRING, string);
    }

    public static class Builder extends FHIRPathAbstractSystemValue.Builder {
        private final String string;

        private Builder(FHIRPathType type, String string) {
            super(type);
            this.string = string;
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
         * Build a FHIRPathStringValue instance using this builder
         *
         * @return
         *     a new FHIRPathStringValue instance
         */
        @Override
        public FHIRPathStringValue build() {
            return new FHIRPathStringValue(this);
        }
    }

    /**
     * Concatenate the {@link String} value with the {@link String} value wrapped by another FHIRPathStringValue
     *
     * @param value
     *     the other FHIRPathStringValue
     * @return
     *     the result of concatenation between this FHIRPathStringValue and the parameter
     */
    public FHIRPathStringValue concat(FHIRPathStringValue value) {
        return stringValue(string.concat(value.string()));
    }

    /**
     * Indicates whether this FHIRPathStringValue starts with the prefix in the parameter
     *
     * @param prefix
     *     the prefix
     * @return
     *     true if this FHIRPathStringValue starts with the parameter, otherwise false
     */
    public boolean startsWith(FHIRPathStringValue prefix) {
        return string.startsWith(prefix.string());
    }

    /**
     * Indicates whether this FHIRPathStringValue ends with the suffix in the parameter
     *
     * @param suffix
     *     the suffix
     * @return
     *     true if this FHIRPathStringValue ends with the parameter, otherwise false
     */
    public boolean endsWith(FHIRPathStringValue suffix) {
        return string.endsWith(suffix.string());
    }

    /**
     * Replace occurrences of pattern in this FHIRPathStringValue with substitution
     *
     * @param pattern
     *     the pattern
     * @param substitution
     *     the substitution
     * @return
     *     the result of replacing occurrences of pattern found in this FHIRPathStringValue with substitution
     */
    public FHIRPathStringValue replace(FHIRPathStringValue pattern, FHIRPathStringValue substitution) {
        return stringValue(string.replace(pattern.string(), substitution.string()));
    }

    /**
     * Indicates whether the {@link String} value wrapped by this FHIRPathStringValue matches the regex in the parameter
     *
     * @param regex
     *     the regex
     * @return
     *     true if the {@link String} value wrapped by this FHIRPathStringValue matches the regex in the parameter
     */
    public boolean matches(FHIRPathStringValue regex) {
        return string.matches(regex.string());
    }

    /**
     * Replace matches of regex in this FHIRPathStringValue with substitution
     *
     * @param regex
     *     the regex
     * @param substitution
     *     the substitution
     * @return
     *     the result of replacing matches of regex in this FHIRPathStringValue with substitution
     */
    public FHIRPathStringValue replaceMatches(FHIRPathStringValue regex, FHIRPathStringValue substitution) {
        return stringValue(string.replaceAll(regex.string(), substitution.string()));
    }

    /**
     * Returns the part of this FHIRPathStringValue starting at position start
     *
     * @param start
     *     the start
     * @return
     *     the part of this FHIRPathStringValue starting at position start
     */
    public FHIRPathStringValue substring(int start) {
        return stringValue(string.substring(start));
    }

    /**
     * Returns the part of this FHIRPathStringValue starting at position start up to length number of characters
     *
     * @param start
     *     the start
     * @param length
     *     the length
     * @return
     *     the part of this FHIRPathStringValue starting at position start up to length number of characters
     */
    public FHIRPathStringValue substring(int start, int length) {
        return stringValue(string.substring(start, (start + length) > string.length() ? string.length() : (start + length)));
    }

    /**
     * Indicates whether this FHIRPathStringValue contains the substring in the parameter
     *
     * @param substring
     *     the substring
     * @return
     *     true if this FHIRPathStringValue contains the substring in the parameter, otherwise false
     */
    public boolean contains(FHIRPathStringValue substring) {
        return string.contains(substring.string());
    }

    /**
     * The length of this FHIRPathStringValue
     *
     * @return
     *     the length of this FHIRPathStringValue
     */
    public int length() {
        return string.length();
    }

    /**
     * Convert the characters in this FHIRPathStringValue to lower case
     *
     * @return
     *     the result of converting the characters in this FHIRPathString to lower case
     */
    public FHIRPathStringValue lower() {
        return stringValue(string.toLowerCase());
    }

    /**
     * Convert the characters in this FHIRPathStringValue to upper case
     *
     * @return
     *     the result of converting the characters in this FHIRPathString to upper case
     */
    public FHIRPathStringValue upper() {
        return stringValue(string.toUpperCase());
    }

    /**
     * Indicates whether this FHIRPathStringValue is comparable to the parameter
     *
     * @return
     *     true if the parameter or its primitive value is a {@link FHIRPathStringValue}, otherwise false
     */
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        return other instanceof FHIRPathStringValue ||
                other.getValue() instanceof FHIRPathStringValue;
    }

    /**
     * Compare the {@link String} value wrapped by this FHIRPathStringValue node to the parameter
     *
     * @param other
     *     the other {@link FHIRPathNode}
     * @return
     *     0 if the {@link String} value wrapped by this FHIRPathStringValue node is equal to the parameter; a positive value if this FHIRPathStringValue if the first non-matching character
     *     is greater than the character at the same index in the parameter; a positive value if this FHIRPathStringValue if the first non-matching character is less than the character at
     *     the same index in the parameter
     */
    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathStringValue) {
            return string.compareTo(((FHIRPathStringValue) other).string());
        }
        return string.compareTo(((FHIRPathStringValue) other.getValue()).string());
    }

    /**
     * Indicates whether the {@link String} value wrapped by this FHIRPathStringValue node is equal the parameter (or its primitive value)
     *
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the {@link String} value wrapped by this FHIRPathStringValue node is equal the parameter (or its primitive value), otherwise false
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
        if (other instanceof FHIRPathStringValue) {
            return Objects.equals(string, ((FHIRPathStringValue) other).string());
        }
        if (other.getValue() instanceof FHIRPathStringValue) {
            return Objects.equals(string, ((FHIRPathStringValue) other.getValue()).string());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(string);
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
