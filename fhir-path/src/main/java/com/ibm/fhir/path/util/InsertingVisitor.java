/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.util;

import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

class InsertingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private String parentPath;
    private String elementNameToInsert;
    private int index;
    private Visitable value;

    /**
     * @param parent
     * @param elementName
     * @param index
     * @param value
     * @throws IllegalArgumentException
     */
    public InsertingVisitor(Visitable parent, String parentPath, String elementName, int index, Visitable value) {
        this.parentPath = Objects.requireNonNull(parentPath, "parentPath");
        this.elementNameToInsert = Objects.requireNonNull(elementName, "elementName");
        this.index = index;
        this.value = Objects.requireNonNull(value, "value") instanceof Code ?
                convertToCodeSubtype(parent, elementName, (Code)value) : value;
    }

    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (getPath().equals(parentPath) && elementName.equals(this.elementNameToInsert)) {
            if (!type.isInstance(value)) {
                throw new IllegalStateException("target " + type + " is not assignable from " + value.getClass());
            }
            getList().add(index, value);
            markListDirty();
        }
    }
}
