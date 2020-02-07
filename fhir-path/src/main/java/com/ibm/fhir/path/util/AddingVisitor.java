/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.util;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

class AddingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Stack<Visitable> visitStack;
    
    private Visitable parent;
    private String elementNameToAdd;
    private Visitable value;

    /**
     * @param parent
     * @param elementName
     * @param value
     */
    public AddingVisitor(Visitable parent, String elementName, Visitable value) {
        this.visitStack = new Stack<Visitable>();
        this.parent = Objects.requireNonNull(parent);
        this.elementNameToAdd = Objects.requireNonNull(elementName);
        this.value = Objects.requireNonNull(value) instanceof Code ?
                convertToCodeSubtype(parent, elementName, (Code)value) : value;
    }
    
    @Override
    protected void doVisitStart(String elementName, int elementIndex, Resource resource) {
        visitStack.push(resource);
    }
    
    @Override
    protected void doVisitStart(String elementName, int elementIndex, Element element) {
        visitStack.push(element);
    }
    
    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (visitStack.peek() == parent && type.isAssignableFrom(value.getClass()) && elementName.equals(this.elementNameToAdd)) {
            getList().add(value);
            markListDirty();
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToAdd)) {
            if (resource == parent) {
                value.accept(this.elementNameToAdd, this);
            } else if (resource == value) {
                markDirty();
            } else if (elementName.equals(this.elementNameToAdd)) {
                // XXX: assuming that we have the right parent is potentially dangerous
                throw new IllegalStateException("Add cannot replace an existing value");
            }
        }
        visitStack.pop();
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToAdd)) {
            if (element == parent) {
                value.accept(this.elementNameToAdd, this);
            } else if (element == value) {
                markDirty();
            } else if (elementName.equals(this.elementNameToAdd)) {
                // XXX: assuming that we have the right parent is potentially dangerous
                throw new IllegalStateException("Add cannot replace an existing value");
            }
        }
        visitStack.pop();
    }
}
