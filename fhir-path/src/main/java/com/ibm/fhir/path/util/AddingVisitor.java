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

public class AddingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Visitable parent;
    private String elementNameToAdd;
    private Visitable value;

    /**
     * @param parent
     * @param elementName
     * @param value
     */
    public AddingVisitor(Visitable parent, String elementName, Visitable value) {
        this.parent = parent;
        this.elementNameToAdd = elementName;
        this.value = value;
    }
    
    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (type.isAssignableFrom(value.getClass()) && elementName.equals(this.elementNameToAdd)) {
            // XXX: assuming that we have the right parent is potentially dangerous
            if (getBuilder().build().equals(parent)) {
                getList().add(value);
                markListDirty();
            }
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToAdd)) {
            if (resource == parent) {
                value.accept(this.elementNameToAdd, this);
            } else if (resource == value) {
                markDirty();
            } else if (elementName.equals(this.elementNameToAdd)) {
                // XXX: assuming that we have the right parent is potentially dangerous
                throw new IllegalStateException("Add cannot replace an existing value");
            }
        }
    }
    
    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        if (!ModelSupport.isRepeatingElement(parent.getClass(), this.elementNameToAdd)) {
            if (element == parent) {
                value.accept(this.elementNameToAdd, this);
            } else if (element == value) {
                markDirty();
            } else if (elementName.equals(this.elementNameToAdd)) {
                // XXX: assuming that we have the right parent is potentially dangerous
                throw new IllegalStateException("Add cannot replace an existing value");
            }
        }
    }
}
