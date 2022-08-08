/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.path.util;

import java.util.List;
import java.util.Objects;

import org.linuxforhealth.fhir.model.visitor.CopyingVisitor;
import org.linuxforhealth.fhir.model.visitor.Visitable;

class MovingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private String parentPath;
    private String elementName;
    private int sourceIndex;
    private int targetIndex;

    /**
     * @param parent
     * @param elementName
     * @param index
     * @param value
     */
    public MovingVisitor(String parentPath, String elementName, int sourceIndex, int targetIndex) {
        this.parentPath = Objects.requireNonNull(parentPath, "parentPath");
        this.elementName = Objects.requireNonNull(elementName, "elementName");
        this.sourceIndex = sourceIndex;
        this.targetIndex = targetIndex;
    }

    @Override
    protected void doVisitListEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
        if (getPath().equals(parentPath) && elementName.equals(this.elementName)) {
            Visitable visitable = getList().remove(sourceIndex);
            getList().add(targetIndex, visitable);
            markListDirty();
        }
    }
}
