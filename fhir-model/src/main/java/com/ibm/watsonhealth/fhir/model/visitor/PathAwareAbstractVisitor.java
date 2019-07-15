/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.visitor;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;

public abstract class PathAwareAbstractVisitor extends AbstractVisitor {
    public static boolean DEBUG = false;
    
    protected final Stack<java.lang.String> nameStack = new Stack<>();
    protected final Stack<String> pathStack = new Stack<>();
    protected final Stack<Integer> indexStack = new Stack<>();
    
    protected abstract void doVisitEnd(String elementName, Element element);
    protected abstract void doVisitEnd(String elementName, List<? extends Visitable> visitables, Class<?> type);
    protected abstract void doVisitEnd(String elementName, Resource resource);
    protected abstract void doVisitStart(String elementName, Element element);
    protected abstract void doVisitStart(String elementName, List<? extends Visitable> visitables, Class<?> type);
    protected abstract void doVisitStart(String elementName, Resource resource);

    protected String getPath() {
        return pathStack.stream().collect(Collectors.joining("."));
    }

    protected void pathStackPush(String elementName, int index) {
        if (index != -1) {
            pathStack.push(elementName + "[" + index + "]");
        } else {
            pathStack.push(elementName);
        }
        if (DEBUG) {
            System.out.println(getPath());
        }
    }
    
    @Override
    public final void visitEnd(java.lang.String elementName, Element element) {
        doVisitEnd(elementName, element);
        pathStack.pop();
    }
    
    @Override
    public final void visitEnd(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {            
        doVisitEnd(elementName, visitables, type);
        nameStack.pop();
        indexStack.pop();
    }
    
    @Override
    public final void visitEnd(java.lang.String elementName, Resource resource) {
        doVisitEnd(elementName, resource);
        pathStack.pop();
    }
    
    @Override
    public final void visitStart(java.lang.String elementName, Element element) {
        int index = -1;
        if (elementName == null) {
            elementName = nameStack.peek();
            index = indexStack.peek();
        }
        pathStackPush(elementName, index);
        doVisitStart(elementName, element);
        if (index != -1) {
            indexStack.set(indexStack.size() - 1, indexStack.peek() + 1);
        }
    }
    
    @Override
    public final void visitStart(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {
        nameStack.push(elementName);
        indexStack.push(0);
        doVisitStart(elementName, visitables, type);
    }
    
    @Override
    public final void visitStart(java.lang.String elementName, Resource resource) {
        int index = -1;
        if (elementName == null) {
            elementName = nameStack.peek();
            index = indexStack.peek();
        }
        pathStackPush(elementName, index);
        doVisitStart(elementName, resource);
        if (index != -1) {
            indexStack.set(indexStack.size() - 1, indexStack.peek() + 1);
        }
    }
}
