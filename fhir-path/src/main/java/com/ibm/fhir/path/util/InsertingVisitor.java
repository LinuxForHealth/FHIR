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
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

class InsertingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Stack<Visitable> visitStack;
    
    private Visitable parent;
    private String elementNameToInsert;
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
        this.parent = Objects.requireNonNull(parent);
        this.elementNameToInsert = Objects.requireNonNull(elementName);
        this.index = index;
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
        if (visitStack.peek() == parent && type.isAssignableFrom(value.getClass()) && elementName.equals(this.elementNameToInsert)) {
            getList().add(index, value);
            markListDirty();
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
