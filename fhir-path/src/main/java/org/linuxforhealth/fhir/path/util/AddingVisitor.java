/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.path.util;

import java.util.List;
import java.util.Objects;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.model.visitor.CopyingVisitor;
import org.linuxforhealth.fhir.model.visitor.Visitable;

class AddingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private String path;
    private String elementNameToAdd;
    private boolean isRepeatingElement;
    private Visitable value;

    /**
     * @param parent the resource or element to add to
     * @param parentPath a "simple" FHIRPath path to the parent of the element being added
     * @param elementName the name of the element to add
     * @param value the element to add
     * @throws IllegalArgumentException
     */
    public AddingVisitor(Visitable parent, String parentPath, String elementName, Visitable value) {
        this.path = Objects.requireNonNull(parentPath, "parentPath");
        this.elementNameToAdd = Objects.requireNonNull(elementName, "elementName");
        this.isRepeatingElement = ModelSupport.isRepeatingElement(parent.getClass(), elementName);
        this.value = Objects.requireNonNull(value, "value") instanceof Code ?
                convertToCodeSubtype(parent, elementName, (Code)value) : value;
    }

    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (getPath().equals(path) && elementName.equals(this.elementNameToAdd)) {
            if (!type.isAssignableFrom(value.getClass())) {
                throw new IllegalStateException("target " + type + " is not assignable from " + value.getClass());
            }
            getList().add(value);
            markListDirty();
        }
    }

    @Override
    public boolean visit(String elementName, int index, Visitable value) {
        if (!isRepeatingElement) {
            if (this.value == value) {
                markDirty();
            } else if (getPath().equals(path + "." + elementNameToAdd)) {
                throw new IllegalStateException("Add cannot replace an existing value at " + getPath());
            }
        }
        return true;
    }

    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
        if (!isRepeatingElement) {
            if (getPath().equals(path)) {
                value.accept(this.elementNameToAdd, this);
            }
        }
    }

    @Override
    protected void doVisitEnd(String elementName, int elementIndex, Element element) {
        if (!isRepeatingElement) {
            if (getPath().equals(path)) {
                value.accept(this.elementNameToAdd, this);
            }
        }
    }
}
