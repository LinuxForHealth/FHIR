/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.visitor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.lang.model.SourceVersion;

import com.ibm.watson.health.fhir.model.builder.Builder;
import com.ibm.watson.health.fhir.model.resource.Bundle;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.Element;
import com.ibm.watson.health.fhir.model.util.ModelSupport;

/**
 * Copy the value of each element within a Resource/Element to a new element with the same values.
 *  
 * Note: this class is NOT threadsafe.  Only one object should be visited at a time.
 * 
 * @author lmsurpre
 * @param <T> The type to copy. Only visitables of this type should be visited.
 */
public class DeepCopyingVisitor<T extends Visitable> extends AbstractVisitor {
    private final Stack<Builder<?>> builderStack = new Stack<>();
    private Stack<List<Object>> listStack = new Stack<>();
    private Object result;
    
    protected Builder<?> builder;
    
    // subclasses may implement these to customize visit behavior without messing up our stacks
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {}
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {}
    protected void doVisitStart(String elementName, int elementIndex, Element element) {}
    protected void doVisitStart(String elementName, int elementIndex, Resource resource) {}
    
    /**
     * Retrieve a copy of the resource last visited.
     * 
     * @return null if no object has been visited yet
     * @throws ClassCastException if the copied object cannot be cast to type T
     */
    @SuppressWarnings("unchecked")
    public T getResult() {
        return (T)result;
    }
    
    @Override
    public boolean preVisit(Element element) {
        return true;
    }
    @Override
    public boolean preVisit(Resource resource) {
        return true;
    }
    
    /**
     * Subclasses may override doVisitStart
     */
    @Override
    public final void visitStart(java.lang.String elementName, int index, Element element) {
        builder = element.toBuilder();
        builderStack.push(builder);
        doVisitStart(elementName, index, element);
    }
    
    /**
     * Subclasses may override doVisitStart
     */
    @Override
    public final void visitStart(java.lang.String elementName, int index, Resource resource) {
        builder = resource.toBuilder();
        builderStack.push(builder);
        doVisitStart(elementName, index, resource);
    }
    
    /**
     * Subclasses may override doVisitEnd
     */
    @Override
    public final void visitEnd(java.lang.String elementName, int index, Element element) {
        doVisitEnd(elementName, index, element);
        _visitEnd(elementName, index, Element.class);
    }
    
    /**
     * Subclasses may override doVisitEnd
     */
    @Override
    public final void visitEnd(java.lang.String elementName, int index, Resource resource) {
        doVisitEnd(elementName, index, resource);
        _visitEnd(elementName, index, Resource.class);
    }
    
    private void _visitEnd(java.lang.String elementName, int index, Class<? extends Visitable> visitableClass) {
        Object obj = builderStack.pop().build();
        if (index != -1) {
            listStack.peek().add(obj);
        } else {
            if (builderStack.isEmpty()) {
                result = obj;
            } else {
                Builder<?> parentBuilder = builderStack.peek();
                
                MethodHandle methodHandle;
                try {
                    MethodType mt;
                    if (ModelSupport.isChoiceElement(parentBuilder.getClass().getEnclosingClass(), elementName) ||
                            isResourceContainer(parentBuilder, elementName)) {
                        // visitableClass is Element if its a choice element or Resource if "ResourceContainer"
                        mt = MethodType.methodType(parentBuilder.getClass(), visitableClass);
                    } else {
                        mt = MethodType.methodType(parentBuilder.getClass(), obj.getClass());
                    }
                    methodHandle = MethodHandles.publicLookup().findVirtual(parentBuilder.getClass(), setterName(elementName), mt);
                    methodHandle.invoke(parentBuilder, obj);
                } catch (Throwable t) {
                    throw new RuntimeException("Unexpected error while visiting " + parentBuilder.getClass() + "." + elementName, t);
                }
                builder = parentBuilder;
            }
        }
    }

    private boolean isResourceContainer(Builder<?> parentBuilder, String elementName) {
        return (parentBuilder instanceof Bundle.Entry.Builder && "resource".contentEquals(elementName)) ||
                (parentBuilder instanceof Bundle.Entry.Response.Builder && "outcome".contentEquals(elementName)) ||
                (parentBuilder instanceof Parameters.Parameter.Builder && "resource".contentEquals(elementName));
    }
    
    @Override
    public void visitStart(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        listStack.push(new ArrayList<Object>());
    }
    
    @Override
    public void visitEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        Builder<?> parentBuilder = builderStack.peek();
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        MethodType mt = MethodType.methodType(parentBuilder.getClass(), java.util.Collection.class);
        MethodHandle methodHandle;
        try {
            methodHandle = lookup.findVirtual(parentBuilder.getClass(), setterName(elementName), mt);
            methodHandle.invoke(parentBuilder, listStack.pop());
        } catch (Throwable t) {
            throw new RuntimeException("Unexpected error while visiting " + parentBuilder.getClass() + "." + elementName, t);
        }
    }
    
    private String setterName(String elementName) {
        if ("class".equals(elementName)) {
            return "clazz";
        }
        if (SourceVersion.isKeyword(elementName)) {
            return "_" + elementName;
        }
        return elementName;
    }
    
    @Override
    public void postVisit(Element element) {
    }
    @Override
    public void postVisit(Resource resource) {
    }
}
