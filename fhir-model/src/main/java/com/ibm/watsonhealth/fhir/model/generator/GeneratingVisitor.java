/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.generator;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getConcreteTypeName;

import java.util.Stack;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.util.JsonSupport;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareAbstractVisitor;

public abstract class GeneratingVisitor extends PathAwareAbstractVisitor {
    protected final Stack<Class<?>> typeStack = new Stack<>();
    
    protected GeneratingVisitor() {
    }
    
    protected java.lang.String getChoiceElementName(java.lang.String name, Class<?> type) {
        return name + getConcreteTypeName(type.getSimpleName());
    }

    protected int getDepth() {
        return typeStack.size();
    }
    
    protected boolean isChoiceElement(java.lang.String name) {
        if (getDepth() > 1) {
            return JsonSupport.isChoiceElement(typeStack.get(getDepth() - 2), name);
        }
        return false;
    }

    @Override
    public void postVisit(Element element) {
        typeStack.pop();
    }

    @Override
    public void postVisit(Resource resource) {
        typeStack.pop();
    }

    @Override
    public boolean preVisit(Element element) {
        typeStack.push(element.getClass());
        return true;
    }

    @Override
    public boolean preVisit(Resource resource) {
        typeStack.push(resource.getClass());
        return true;
    }
}
