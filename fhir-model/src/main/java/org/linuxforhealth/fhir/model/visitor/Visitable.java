/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.visitor;

import static org.linuxforhealth.fhir.model.util.ModelSupport.getTypeName;

/**
 * Visitable interface for FHIR model objects that accept a Visitor.
 * 
 * @see Visitor
 */
public interface Visitable {
    /**
     * Accept a Visitor and invoke the appropriate visit methods.
     * 
     * A typical implementation would look like this:
     * <pre>
     * if (visitor.preVisit(this)) {
     *     visitor.visitStart(elementName, elementIndex, this);
     *     if (visitor.visit(elementName, elementIndex, this)) {
     *         // visit children
     *     }
     *     visitor.visitEnd(elementName, elementIndex, this);
     *     visitor.postVisit(this);
     * }
     * </pre>
     * 
     * @param elementName
     *          the name of the element in the context of this visit
     * @param elementIndex 
     *          the index of the element in a list or -1 if it is not contained within a List
     * @param visitor
     *          the visitor to use
     */
    void accept(java.lang.String elementName, int elementIndex, Visitor visitor);
    
    /**
     * Accept a Visitor and invoke the appropriate visit methods.
     * 
     * This variant assumes that the element is not within a list.
     * 
     * @param elementName
     *          the name of the element in the context of this visit
     * @param visitor
     *          the visitor to use
     */
    default void accept(java.lang.String elementName, Visitor visitor) {
        accept(elementName, -1, visitor);
    }
    
    /**
     * Accept a Visitor and invoke the appropriate visit methods.
     * 
     * This variant uses ModelSupport to infer the name of element being visited and assumes the element is not within a list.
     * 
     * @param visitor
     *          the visitor to use
     */
    default void accept(Visitor visitor) {
        accept(getTypeName(getClass()), -1, visitor);
    }
}
