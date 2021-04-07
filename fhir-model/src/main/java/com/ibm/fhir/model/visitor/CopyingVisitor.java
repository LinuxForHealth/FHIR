/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.visitor;

import static com.ibm.fhir.model.util.ModelSupport.delimit;
import static com.ibm.fhir.model.util.ModelSupport.isKeyword;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;

import com.ibm.fhir.model.builder.Builder;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.util.ModelSupport;

import net.jcip.annotations.NotThreadSafe;

/**
 * Copy a Resource or Element. Because model objects are immutable, by default this will return a reference to
 * the exact same object that was originally visited.
 *
 * However, subclasses may override this class in order to modify the copied Resource or Element
 * by setting new values on the current builder via ({@link BuilderWrapper#getBuilder()) and
 * marking it dirty via ({@link BuilderWrapper#markDirty())).
 *
 * Note: this class is NOT threadsafe.  Only one object should be visited at a time.
 *
 * @param <T> The type to copy. Only visitables of this type should be visited.
 */
@NotThreadSafe
public class CopyingVisitor<T extends Visitable> extends DefaultVisitor {
    private final Stack<String> pathStack = new Stack<>();
    private final Stack<BuilderWrapper> builderStack = new Stack<>();
    private final Stack<ListWrapper> listStack = new Stack<>();
    private Object result;

    // subclasses may implement these to customize visit behavior without messing up our stacks
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {}
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {}
    protected void doVisitStart(String elementName, int elementIndex, Element element) {}
    protected void doVisitStart(String elementName, int elementIndex, Resource resource) {}
    protected void doVisitListStart(String elementName, List<? extends Visitable> visitables, Class<?> type) {}
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {}

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

    /**
     * Get the FHIRPath path of the Resource or Element currently being visited.
     *
     * This method is primarily for subclasses but can also be used externally to retrieve a path to the Resource
     * or Element that was being visited when an Exception occurs.
     *
     * @return The path of the Resource or Element currently being visited, the path that was being visited when an
     *         exception was thrown, or null if there is no Resource or Element being visited.
     * @implSpec Path segments are appended in the visitStart methods and removed in the visitEnd methods.
     */
    public final String getPath() {
        if (!pathStack.isEmpty()) {
            return pathStack.stream().collect(Collectors.joining("."));
        }
        return null;
    }

    public CopyingVisitor() {
        super(true);
    }

    /**
     * Reset the state of the CopyingVisitor.
     *
     * Invoke this method when visiting has failed and you want to clear the state in order to re-use the visitor.
     */
    public final void reset() {
        if (!pathStack.isEmpty()) {
            pathStack.clear();
        }
        if (!builderStack.isEmpty()) {
            builderStack.clear();
        }
        if (!listStack.isEmpty()) {
            listStack.clear();
        }
    }

    /**
     * Subclasses may override doVisitStart
     */
    @Override
    public final void visitStart(java.lang.String elementName, int index, Element element) {
        builderStack.push(new ElementWrapper(element.toBuilder()));
        pathStackPush(elementName, index);
        doVisitStart(elementName, index, element);
    }

    /**
     * Subclasses may override doVisitStart
     */
    @Override
    public final void visitStart(java.lang.String elementName, int index, Resource resource) {
        builderStack.push(new ResourceWrapper(resource.toBuilder()));
        pathStackPush(elementName, index);
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
        pathStackPop();
        BuilderWrapper wrapper = builderStack.pop();
        if (index != -1) {
            ListWrapper listWrapper = listStack.peek();
            if (wrapper.isDirty()) {
                listWrapper.dirty(true);
            }
            // No way to know if one of the other elements in the list will be dirty so we need to build them all
            Visitable item = wrapper.getBuilder().build();
            if (item != null) {
                listWrapper.getList().add(wrapper.getBuilder().build());
            }
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

                    Class<?> expectedType = ModelSupport.getElementType(parentBuilder.getClass().getEnclosingClass(), elementName);
                    if (obj != null && !expectedType.isInstance(obj)) {
                        throw new IllegalStateException("Expected argument of type " + expectedType + " but found " + obj.getClass());
                    }

                    try {
                        MethodType mt = MethodType.methodType(parentBuilder.getClass(), expectedType);
                        MethodHandle methodHandle = MethodHandles.publicLookup().findVirtual(parentBuilder.getClass(), setterName(elementName), mt);
                        methodHandle.invoke(parentBuilder, obj);
                    } catch (Throwable t) {
                        throw new RuntimeException("Unexpected error while visiting " + parentBuilder.getClass() + "." + elementName, t);
                    }
                }
                wrapper = parent;
            }
        }
    }

    /**
     * Subclasses may override doVisitListStart
     */
    @Override
    public void visitStart(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        doVisitListStart(elementName, visitables, type);
        listStack.push(new ListWrapper(new ArrayList<>()));
    }

    /**
     * Subclasses may override doVisitListEnd
     */
    @Override
    public void visitEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        doVisitListEnd(elementName, visitables, type);
        ListWrapper listWrapper = listStack.pop();
        if (listWrapper.isDirty()) {
            for (Visitable obj : listWrapper.getList()) {
                if (!type.isInstance(obj)) {
                    throw new IllegalStateException("Expected argument of type " + type + " but found " + obj.getClass());
                }
            }
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
    }

    protected Builder<?> getBuilder() {
        return builderStack.peek().getBuilder();
    }

    protected List<Visitable> getList() {
        return listStack.peek().getList();
    }

    protected void replace(Resource.Builder builder) {
        builderStack.pop();
        builderStack.push(new ResourceWrapper(builder));
        markDirty();
    }

    protected void replace(Element.Builder builder) {
        builderStack.pop();
        builderStack.push(new ElementWrapper(builder));
        markDirty();
    }

    protected void delete() {
        builderStack.pop();
        builderStack.push(new BuilderWrapper() {
            // Replace the current BuilderWrapper with with that just builds nulls
            @Override
            public Builder<? extends Visitable> getBuilder() {
                return new Builder<Visitable>() {
                    @Override
                    public Visitable build() {
                        return null;
                    }
                };
            }
        });
        markDirty();
    }

    protected void markDirty() {
        builderStack.peek().dirty(true);
    }

    protected void markListDirty() {
        listStack.peek().dirty(true);
    }

    protected Code convertToCodeSubtype(Visitable parent, String elementName, Code value) {
        Class<?> targetType = ModelSupport.getElementType(parent.getClass(), elementName);
        if (value.getClass() != targetType) {
            MethodType mt = MethodType.methodType(targetType, String.class);
            try {
                MethodHandle methodHandle = MethodHandles.publicLookup().findStatic(targetType, "of", mt);
                value = (Code) methodHandle.invoke(value.getValue());
            } catch (Throwable e) {
                throw new IllegalArgumentException("Value of type '" + value.getClass() +
                    "' cannot be used to populate target of type '" + targetType + "'");
            }
        }
        return value;
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
        private final List<Visitable> list;

        public ListWrapper(List<Visitable> list) {
            this.list = list;
        }

        public List<Visitable> getList() {
            return list;
        }
    }

    private abstract class BuilderWrapper extends Markable {
        public abstract Builder<? extends Visitable> getBuilder();
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
