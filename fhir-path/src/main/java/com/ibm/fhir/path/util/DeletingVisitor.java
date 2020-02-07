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
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

class DeletingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Stack<Visitable> visitStack;
    
    private Visitable parent;
    private String elementNameToDelete;
    private Visitable toDelete;

    /**
     * @param parent
     * @param elementName
     * @param toDelete
     */
    public DeletingVisitor(Visitable parent, String elementName, Visitable toDelete) {
        this.visitStack = new Stack<Visitable>();
        this.parent = Objects.requireNonNull(parent);
        this.elementNameToDelete = Objects.requireNonNull(elementName);
        this.toDelete = Objects.requireNonNull(toDelete);
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
        if (visitStack.peek() == parent && type.isAssignableFrom(toDelete.getClass()) && elementName.equals(this.elementNameToDelete)) {
            for (int i = 0; i < visitables.size(); i++) {
                if (visitables.get(i) == toDelete) {
                    getList().remove(i);
                    markListDirty();
                }
            }
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToDelete)) {
            if (elementName.equals(this.elementNameToDelete) && resource == toDelete) {
                delete();
            }
        }
        visitStack.pop();
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToDelete)) {
            if (elementName.equals(this.elementNameToDelete) && element == toDelete) {
                delete();
            }
        }
        visitStack.pop();
    }
}
