/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.visitor;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.delimit;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.isKeyword;

import java.util.Stack;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;

public abstract class PathAwareAbstractVisitor extends AbstractVisitor implements PathAwareVisitor {
    public static boolean DEBUG = false;
    
    protected final Stack<String> pathStack = new Stack<>();
        
    // called by template methods
    protected abstract void doVisitEnd(String elementName, int elementIndex, Element element);
    protected abstract void doVisitEnd(String elementName, int elementIndex, Resource resource);
    protected abstract void doVisitStart(String elementName, int elementIndex, Element element);
    protected abstract void doVisitStart(String elementName, int elementIndex, Resource resource);
    
    @Override
    public final String getPath() {
        if (!pathStack.isEmpty()) {
            return pathStack.stream().collect(Collectors.joining("."));
        }
        return null;
    }
    
    private void pathStackPop() {
        pathStack.pop();
    }
    
    private void pathStackPush(String elementName, int index) {
        if (isKeyword(elementName)) {
            elementName = delimit(elementName);
        }
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
    public final void visitEnd(java.lang.String elementName, int elementIndex, Element element) {
        doVisitEnd(elementName, elementIndex, element);
        pathStackPop();
    }
    
    @Override
    public final void visitEnd(java.lang.String elementName, int elementIndex, Resource resource) {
        doVisitEnd(elementName, elementIndex, resource);
        pathStackPop();
    }
    
    @Override
    public final void visitStart(java.lang.String elementName, int elementIndex, Element element) {        
        pathStackPush(elementName, elementIndex);
        doVisitStart(elementName, elementIndex, element);
    }
    
    @Override
    public final void visitStart(java.lang.String elementName, int elementIndex, Resource resource) {
        pathStackPush(elementName, elementIndex);
        doVisitStart(elementName, elementIndex, resource);
    }
}
