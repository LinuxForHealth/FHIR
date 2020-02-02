/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.util;

import java.util.List;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

public class ReplacingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Visitable parent;
    private String elementNameToReplace;
    private Visitable oldValue;
    private Visitable newValue;

    /**
     * @param parent
     * @param elementName
     * @param toDelete
     */
    public ReplacingVisitor(Visitable parent, String elementName, Visitable oldValue, Visitable newValue) {
        this.parent = parent;
        this.elementNameToReplace = elementName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (type.isAssignableFrom(newValue.getClass()) && elementName.equals(this.elementNameToReplace)) {
            // XXX: assuming that we have the right parent is potentially dangerous
            if (getBuilder().build().equals(parent)) {
                for (int i = 0; i < visitables.size(); i++) {
                    if (visitables.get(i) == oldValue) {
                        getList().set(i, newValue);
                        markListDirty();
                    }
                }
            }
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToReplace)) {
            if (elementName.equals(this.elementNameToReplace) && resource == oldValue) {
                replace(((Resource) newValue).toBuilder());
            }
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToReplace)) {
            if (elementName.equals(this.elementNameToReplace) && element == oldValue) {
                replace(((Element) newValue).toBuilder());
            }
        }
    }
}
