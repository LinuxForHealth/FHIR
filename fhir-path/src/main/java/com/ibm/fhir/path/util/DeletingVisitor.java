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

public class DeletingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Visitable parent;
    private String elementNameToDelete;
    private Visitable toDelete;

    /**
     * @param parent
     * @param elementName
     * @param toDelete
     */
    public DeletingVisitor(Visitable parent, String elementName, Visitable toDelete) {
        this.parent = parent;
        this.elementNameToDelete = elementName;
        this.toDelete = toDelete;
    }
    
    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (type.isAssignableFrom(toDelete.getClass()) && elementName.equals(this.elementNameToDelete)) {
            // XXX: assuming that we have the right parent is potentially dangerous
            if (getBuilder().build().equals(parent)) {
                for (int i = 0; i < visitables.size(); i++) {
                    if (visitables.get(i) == toDelete) {
                        getList().remove(i);
                        markListDirty();
                    }
                }
            }
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToDelete)) {
            if (elementName.equals(this.elementNameToDelete) && resource == toDelete) {
                delete();
            }
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToDelete)) {
            if (elementName.equals(this.elementNameToDelete) && element == toDelete) {
                delete();
            }
        }
    }
}
