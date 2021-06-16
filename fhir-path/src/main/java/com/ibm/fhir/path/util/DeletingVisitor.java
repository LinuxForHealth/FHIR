/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.util;

import java.util.Objects;

import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

class DeletingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private String pathToDelete;

    public DeletingVisitor(String pathToDelete) {
        this.pathToDelete = Objects.requireNonNull(pathToDelete, "pathToDelete");
    }

    @Override
    public boolean visit(String elementName, int index, Visitable value) {
        if (pathToDelete.equals(getPath())) {
            delete();
            markDirty();
            return false;
        }
        return true;
    }
}