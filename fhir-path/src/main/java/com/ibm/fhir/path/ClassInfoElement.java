/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.util.Objects;

/**
 * This class is part of the implementation for the Types and Reflection section of the FHIRPath specification:
 * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#types-and-reflection">FHIRPath Types and Reflection</a>
 */
public class ClassInfoElement {
    private final String name;
    private final String type;
    private final boolean oneBased;

    public ClassInfoElement(String name, String type, boolean oneBased) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.oneBased = oneBased;
    }

    /**
     * The name of this ClassInfoElement
     *
     * @return
     *     the name of this ClassInfoElement
     */
    public String getName() {
        return name;
    }

    /**
     * The type of this ClassInfoElement
     *
     * @return
     *     the type of this ClassInfoElement
     */
    public String getType() {
        return type;
    }

    /**
     * Indicates whether this ClassInfoElement is one-based
     *
     * @return
     *     true if this ClassInfoElement is one-based, otherwise false
     */
    public boolean isOneBased() {
        return oneBased;
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
        ClassInfoElement other = (ClassInfoElement) obj;
        return Objects.equals(name, other.name) &&
                Objects.equals(type, other.type) &&
                Objects.equals(oneBased, other.oneBased);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, oneBased);
    }

    @Override
    public String toString() {
        if (oneBased) {
            return String.format("ClassInfoElement { name: '%s', type: '%s' }", name, type);
        }
        return String.format("ClassInfoElement { name: '%s', type: 'List<%s>', oneBased: 'false' }", name, type);
    }
}
