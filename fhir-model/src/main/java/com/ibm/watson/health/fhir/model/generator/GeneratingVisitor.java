/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.generator;

import java.util.Stack;

import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.Element;
import com.ibm.watson.health.fhir.model.util.ModelSupport;
import com.ibm.watson.health.fhir.model.visitor.PathAwareAbstractVisitor;

public abstract class GeneratingVisitor extends PathAwareAbstractVisitor {
    protected final Stack<Class<?>> typeStack = new Stack<>();
    
    protected GeneratingVisitor() {
        // for subclasses
    }
    
    protected final java.lang.String getChoiceElementName(java.lang.String name, Class<?> type) {
        return ModelSupport.getChoiceElementName(name, type);
    }

    protected final int getDepth() {
        return typeStack.size();
    }
    
    protected final boolean isChoiceElement(java.lang.String name) {
        if (getDepth() > 1) {
            return ModelSupport.isChoiceElement(typeStack.get(getDepth() - 2), name);
        }
        return false;
    }

    @Override
    public final void postVisit(Element element) {
        typeStack.pop();
    }

    @Override
    public final void postVisit(Resource resource) {
        typeStack.pop();
    }

    @Override
    public final boolean preVisit(Element element) {
        typeStack.push(element.getClass());
        return true;
    }

    @Override
    public final boolean preVisit(Resource resource) {
        typeStack.push(resource.getClass());
        return true;
    }
}
