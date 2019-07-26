/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.visitor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.ibm.watsonhealth.fhir.model.builder.AbstractBuilder;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;

/**
 * Copy the value of each element within a Resource/Element to a new element with the same values.
 *  
 * Note: this class is NOT threadsafe.  Only one object should be visited at a time.
 * 
 * @author lmsurpre
 * @param <T> The type to copy. Only visitables of this type should be visited.
 */
public class DeepCopyingVisitor<T extends Visitable> extends AbstractVisitor {
    private final Stack<AbstractBuilder<?>> builderStack = new Stack<>();
    private Stack<List<Object>> listStack = new Stack<>();
    private Object result;
    
    protected AbstractBuilder<?> builder;
    
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
        Object obj = builderStack.pop().build();
        if (index != -1) {
            listStack.peek().add(obj);
        } else {
            if (builderStack.isEmpty()) {
                // We should have an object of the right type because of the "first" check in preVisit 
                result = obj;
            } else {
                AbstractBuilder<?> parentBuilder = builderStack.peek();
                
                MethodHandle methodHandle;
                try {
                    MethodType mt = MethodType.methodType(parentBuilder.getClass(), obj.getClass());
                    methodHandle = MethodHandles.publicLookup().findVirtual(parentBuilder.getClass(), elementName, mt);
                    methodHandle.invoke(parentBuilder, obj);
                } catch (NoSuchMethodException e1) {
                    try {
                        MethodType mt = MethodType.methodType(parentBuilder.getClass(), Element.class);
                        methodHandle = MethodHandles.publicLookup().findVirtual(parentBuilder.getClass(), elementName, mt);
                        methodHandle.invoke(parentBuilder, obj);
                    } catch (Throwable e2) {
                        e1.printStackTrace();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                builder = parentBuilder;
            }
        }
    }
    
    /**
     * Subclasses may override doVisitEnd
     */
    @Override
    public final void visitEnd(java.lang.String elementName, int index, Resource resource) {
        doVisitEnd(elementName, index, resource);
        Object obj = builderStack.pop().build();
        if (index != -1) {
            listStack.peek().add(obj);
        } else {
            if (builderStack.isEmpty()) {
                // We should have an object of the right type if the caller is visiting a resource of the right type
                result = obj;
            } else {
                AbstractBuilder<?> parentBuilder = builderStack.peek();
                
                
                MethodHandle methodHandle;
                try {
                    MethodType mt = MethodType.methodType(parentBuilder.getClass(), obj.getClass());
                    methodHandle = MethodHandles.publicLookup().findVirtual(parentBuilder.getClass(), elementName, mt);
                    methodHandle.invoke(parentBuilder, obj);
                } catch (NoSuchMethodException e1) {
                    try {
                        MethodType mt = MethodType.methodType(parentBuilder.getClass(), Resource.class);
                        methodHandle = MethodHandles.publicLookup().findVirtual(parentBuilder.getClass(), elementName, mt);
                        methodHandle.invoke(parentBuilder, obj);
                    } catch (Throwable e2) {
                        e1.printStackTrace();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                builder = parentBuilder;
            }
        }
    }
    
    @Override
    public void visitStart(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        listStack.push(new ArrayList<Object>());
    }
    
    @Override
    public void visitEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        AbstractBuilder<?> parentBuilder = builderStack.peek();
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        MethodType mt = MethodType.methodType(parentBuilder.getClass(), java.util.Collection.class);
        MethodHandle methodHandle;
        try {
            methodHandle = lookup.findVirtual(parentBuilder.getClass(), elementName, mt);
            methodHandle.invoke(parentBuilder, listStack.pop());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void postVisit(Element element) {
    }
    @Override
    public void postVisit(Resource resource) {
    }
}
