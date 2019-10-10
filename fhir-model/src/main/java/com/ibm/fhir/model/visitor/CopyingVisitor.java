/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.visitor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.lang.model.SourceVersion;

import com.ibm.fhir.model.builder.Builder;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.util.ModelSupport;

/**
 * Copy a Resource or Element. Because model objects are immutable, by default this will return a reference to
 * the exact same object that was originally visited.
 * 
 * However, subclasses may override this class in order to modify the copied Resource or Element
 * by setting new values on the current builder via ({@code getBuilder())) and marking it dirty via ({@code markDirty())).  
 *  
 * Note: this class is NOT threadsafe.  Only one object should be visited at a time.
 * 
 * @author lmsurpre
 * @param <T> The type to copy. Only visitables of this type should be visited.
 */
public class CopyingVisitor<T extends Visitable> extends DefaultVisitor {
    private final Stack<BuilderWrapper> builderStack = new Stack<>();
    private Stack<ListWrapper> listStack = new Stack<>();
    private Object result;
    
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
    
    public CopyingVisitor() {
        super(true);
    }
    
    /**
     * Subclasses may override doVisitStart
     */
    @Override
    public final void visitStart(java.lang.String elementName, int index, Element element) {
        builderStack.push(new ElementWrapper(element.toBuilder()));
        doVisitStart(elementName, index, element);
    }
    
    /**
     * Subclasses may override doVisitStart
     */
    @Override
    public final void visitStart(java.lang.String elementName, int index, Resource resource) {
        builderStack.push(new ResourceWrapper(resource.toBuilder()));
        doVisitStart(elementName, index, resource);
    }
    
    /**
     * Subclasses may override doVisitEnd
     */
    @Override
    public final void visitEnd(java.lang.String elementName, int index, Element element) {
        doVisitEnd(elementName, index, element);
        _visitEnd(elementName, index, element, Element.class);
    }
    
    /**
     * Subclasses may override doVisitEnd
     */
    @Override
    public final void visitEnd(java.lang.String elementName, int index, Resource resource) {
        doVisitEnd(elementName, index, resource);
        _visitEnd(elementName, index, resource, Resource.class);
    }
    
    private void _visitEnd(java.lang.String elementName, int index, Visitable visited, Class<? extends Visitable> elementOrResource) {
        BuilderWrapper wrapper = builderStack.pop();
        if (index != -1) {
            ListWrapper listWrapper = listStack.peek();
            if (wrapper.isDirty()) {
                listWrapper.dirty(true);
            }
            // No way to know if one of the other elements in the list will be dirty so we need to build them all
            listWrapper.getList().add(wrapper.getBuilder().build());
        } else {
            if (builderStack.isEmpty()) {
                if (wrapper.isDirty()) {
                    result = wrapper.getBuilder().build();
                } else {
                    result = visited;
                }
            } else {
                BuilderWrapper parent = builderStack.peek();
                
                if (wrapper.isDirty()) {
                    parent.dirty(true);
                    Builder<?> parentBuilder = parent.getBuilder();
                    Object obj = wrapper.getBuilder().build();
                    
                    MethodHandle methodHandle;
                    try {
                        MethodType mt;
                        if ((visited instanceof Element && ModelSupport.isChoiceElement(parentBuilder.getClass().getEnclosingClass(), elementName)) 
                                || (visited instanceof Resource && isResourceContainer(parentBuilder, elementName))) {
                            mt = MethodType.methodType(parentBuilder.getClass(), elementOrResource);
                        } else {
                            mt = MethodType.methodType(parentBuilder.getClass(), obj.getClass());
                        }
                        methodHandle = MethodHandles.publicLookup().findVirtual(parentBuilder.getClass(), setterName(elementName), mt);
                        methodHandle.invoke(parentBuilder, obj);
                    } catch (Throwable t) {
                        throw new RuntimeException("Unexpected error while visiting " + parentBuilder.getClass() + "." + elementName, t);
                    }
                }
                wrapper = parent;
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
        listStack.push(new ListWrapper(new ArrayList<Object>()));
    }
    
    @Override
    public void visitEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        ListWrapper listWrapper = listStack.pop();
        if (listWrapper.isDirty()) {
            BuilderWrapper parent = builderStack.peek();
            parent.dirty(true);
            Builder<?> parentBuilder = parent.getBuilder();
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            MethodType mt = MethodType.methodType(parentBuilder.getClass(), java.util.Collection.class);
            MethodHandle methodHandle;
            try {
                methodHandle = lookup.findVirtual(parentBuilder.getClass(), setterName(elementName), mt);
                methodHandle.invoke(parentBuilder, listWrapper.getList());
            } catch (Throwable t) {
                throw new RuntimeException("Unexpected error while visiting " + parentBuilder.getClass() + "." + elementName, t);
            }
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
    
    protected Builder<?> getBuilder() {
        return builderStack.peek().getBuilder();
    }
    
    protected void markDirty() {
        builderStack.peek().dirty(true);
    }

    private abstract class Markable {
        private boolean dirty = false;
        
        public boolean isDirty() {
            return dirty;
        }

        public void dirty(boolean dirty) {
            this.dirty = dirty;
        }
    }
    
    private class ListWrapper extends Markable {
        private final List<Object> list;
        
        public ListWrapper(List<Object> list) {
            this.list = list;
        }
        
        public List<Object> getList() {
            return list;
        }
    }
    
    private abstract class BuilderWrapper extends Markable {
        public abstract Builder<?> getBuilder();
    }
    
    private class ElementWrapper extends BuilderWrapper {
        private final Element.Builder builder;
        
        public ElementWrapper(Element.Builder builder) {
            // TODO can we wrap all the setters so that subclasses don't need to explicitly call markDirty()?
            this.builder = builder;
        }

        @Override
        public Element.Builder getBuilder() {
            return builder;
        }
    }
    
    private class ResourceWrapper extends BuilderWrapper {
        private final Resource.Builder builder;
        
        public ResourceWrapper(Resource.Builder builder) {
            // TODO can we wrap all the setters so that subclasses don't need to explicitly call markDirty()?
            this.builder = builder;
        }
        
        @Override
        public Resource.Builder getBuilder() {
            return builder;
        }
    }
}
