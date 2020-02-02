/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.util;

import java.util.List;

import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

public class MovingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private Visitable parent;
    private String elementName;
    private int sourceIndex;
    private int targetIndex;

    /**
     * @param parent
     * @param elementName
     * @param index
     * @param value
     */
    public MovingVisitor(Visitable parent, String elementName, int sourceIndex, int targetIndex) {
        this.parent = parent;
        this.elementName = elementName;
        this.sourceIndex = sourceIndex;
        this.targetIndex = targetIndex;
    }

    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (elementName.equals(this.elementName) && getBuilder().build().equals(parent)) {
            Visitable visitable = getList().remove(sourceIndex);
            getList().add(targetIndex, visitable);
            markListDirty();
        }
    }
}
