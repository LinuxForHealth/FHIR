/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.util;

import java.util.List;
import java.util.Stack;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

public class InsertingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Stack<Visitable> visitStack;
    
    private Visitable parent;
    private String elementName;
    private int index;
    private Visitable value;

    /**
     * @param parent
     * @param elementName
     * @param index
     * @param value
     */
    public InsertingVisitor(Visitable parent, String elementName, int index, Visitable value) {
        this.visitStack = new Stack<Visitable>();
        this.parent = parent;
        this.elementName = elementName;
        this.index = index;
        this.value = value;
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
        if (type.isAssignableFrom(value.getClass()) && elementName.equals(this.elementName)) {
            // XXX: assuming that we have the right parent is potentially dangerous, but needed until parent is always non-null
            if ((parent == null && index == 0) || visitStack.peek() == parent) {
                getList().add(index, value);
                markListDirty();
            }
        }
    }

    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        visitStack.pop();
    }

    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        visitStack.pop();
    }
}
