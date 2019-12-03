/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.visitor;

import static com.ibm.fhir.model.util.ModelSupport.delimit;
import static com.ibm.fhir.model.util.ModelSupport.isKeyword;

import java.util.Stack;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;

import net.jcip.annotations.NotThreadSafe;

/**
 * PathAwareVisitor extends the DefaultVisitor with a {@link #getPath()} method that can be used to get the FHIRPath
 * path of a Resource or Element during a visit.
 */
@NotThreadSafe
public class PathAwareVisitor extends DefaultVisitor {
    public static boolean DEBUG = false;

    private final Stack<String> pathStack = new Stack<>();

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
    
    /**
     * Reset the state of the PathAwareVisitor.
     * 
     * Invoke this method when visiting has failed and you want to clear the path in order to re-use the visitor.
     */
    public final void reset() {
        if (!pathStack.isEmpty()) {
            pathStack.clear();
        }
    }

    /**
     * @implSpec {@link #visitStart(String, int, Element)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitStart behavior for Elements.
     */
    protected void doVisitStart(String elementName, int elementIndex, Element element) { }

    /**
     * @implSpec {@link #visitStart(String, int, Resource)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitStart behavior for Resources.
     */
    protected void doVisitStart(String elementName, int elementIndex, Resource resource) { }

    /**
     * @implSpec {@link #visitEnd(String, int, Element)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitEnd behavior for Elements.
     */
    protected void doVisitEnd(String elementName, int elementIndex, Element element) { }

    /**
     * @implSpec {@link #visitEnd(String, int, Resource)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitEnd behavior for Resources.
     */
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) { }

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
    
    public PathAwareVisitor() {
        super(true);
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
