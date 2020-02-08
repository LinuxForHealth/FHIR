/*
 * (C) Copyright IBM Corp. 2020
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
     */
    public InsertingVisitor(Visitable parent, String parentPath, String elementName, int index, Visitable value) {
        this.parentPath = Objects.requireNonNull(parentPath);
        this.elementNameToInsert = Objects.requireNonNull(elementName);
        this.index = index;
        this.value = Objects.requireNonNull(value) instanceof Code ?
                convertToCodeSubtype(parent, elementName, (Code)value) : value;
    }

    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (getPath().equals(parentPath) && elementName.equals(this.elementNameToInsert)) {
            getList().add(index, value);
            markListDirty();
        }
    }
}
