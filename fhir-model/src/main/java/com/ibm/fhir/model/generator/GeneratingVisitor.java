/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.generator;

import java.util.Set;
import java.util.Stack;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.PathAwareVisitor;

public abstract class GeneratingVisitor extends PathAwareVisitor {
    protected final Stack<Class<?>> typeStack = new Stack<>();

    protected GeneratingVisitor() {
        // for subclasses
    }

    protected final java.lang.String getChoiceElementName(java.lang.String name, Class<?> type) {
        if (!isChoiceElement(name)) {
            throw new IllegalArgumentException("Element '" + name + "' is not a choice element");
        }
        Class<?> modelClass = typeStack.get(getDepth() - 2);
        Set<Class<?>> choiceElementTypes = ModelSupport.getChoiceElementTypes(modelClass, name);
        if (!choiceElementTypes.contains(type)) {
            for (Class<?> choiceElementType : choiceElementTypes) {
                if (choiceElementType.isAssignableFrom(type)) {
                    type = choiceElementType;
                    break;
                }
            }
        }
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
