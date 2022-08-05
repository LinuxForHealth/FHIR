/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class is part of the implementation for the Types and Reflection section of the FHIRPath specification:
 * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#types-and-reflection">FHIRPath Types and Reflection</a>
 */
public class ClassInfo implements TypeInfo {
    private final String name;
    private final String namespace;
    private final String baseType;
    private final List<ClassInfoElement> element;
    
    public ClassInfo(String namespace, String name, String baseType, List<ClassInfoElement> element) {
        this.namespace = Objects.requireNonNull(namespace);
        this.name = Objects.requireNonNull(name);
        this.baseType = Objects.requireNonNull(baseType);
        this.element = Collections.unmodifiableList(Objects.requireNonNull(element));
    }
    
    @Override
    public String getNamespace() {
        return namespace;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * The base type of this ClassInfo
     * 
     * @return
     *     the base type of this ClassInfo
     */
    public final String getBaseType() {
        return baseType;
    }
    
    /**
     * The list of elements that are part of this ClassInfo
     * 
     * @return
     *     the list of elements that are part of this ClassInfo
     */
    public List<ClassInfoElement> getElement() {
        return element;
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
        ClassInfo other = (ClassInfo) obj;
        return Objects.equals(name, other.name) && 
                Objects.equals(namespace, other.namespace) && 
                Objects.equals(baseType, other.baseType) && 
                Objects.equals(element, other.element);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, namespace, baseType, element);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String nl = System.lineSeparator();
        sb.append("ClassInfo {").append(nl);
        sb.append("  namespace: '").append(namespace).append("',").append(nl);
        sb.append("  name: '").append(name).append("',").append(nl);
        sb.append("  baseType: '").append(baseType).append("',").append(nl);
        sb.append("  element: {").append(nl);
        if (!element.isEmpty()) {
            for (ClassInfoElement e : element) {
                sb.append("    ").append(e).append(nl);
            }
        }
        sb.append("  }").append(nl);
        sb.append("}").append(nl);
        return sb.toString();
    }
}
