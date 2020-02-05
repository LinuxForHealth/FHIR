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

public class MovingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Stack<Visitable> visitStack;
    
    private Visitable parent;
    private String elementName;
    private int sourceIndex;
    private int targetIndex;

    /**
     * @param parent
     * @param elementName
     * @param index
     * @param value
     */
    public MovingVisitor(Visitable parent, String elementName, int sourceIndex, int targetIndex) {
        this.visitStack = new Stack<Visitable>();
        this.parent = parent;
        this.elementName = elementName;
        this.sourceIndex = sourceIndex;
        this.targetIndex = targetIndex;
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
        if (visitStack.peek() == parent && elementName.equals(this.elementName)) {
            Visitable visitable = getList().remove(sourceIndex);
            getList().add(targetIndex, visitable);
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
