/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.path.util;

import java.util.Objects;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.visitor.CopyingVisitor;
import org.linuxforhealth.fhir.model.visitor.Visitable;

class ReplacingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private String pathToReplace;
    private Visitable newValue;

    /**
     * @param parent
     * @param elementName
     * @param pathToReplace
     * @param newValue
     * @throws IllegalArgumentException
     */
    public ReplacingVisitor(Visitable parent, String elementName, String pathToReplace, Visitable newValue) {
        this.pathToReplace = Objects.requireNonNull(pathToReplace, "pathToReplace");
        this.newValue = Objects.requireNonNull(newValue, "newValue") instanceof Code ?
                convertToCodeSubtype(parent, elementName, (Code)newValue) : newValue;
    }

    @Override
    public boolean visit(String elementName, int index, Visitable value) {
        if (pathToReplace.equals(getPath())) {
            if (newValue instanceof Element) {
                replace(((Element) newValue).toBuilder());
            } else if (newValue instanceof Resource) {
                replace(((Resource) newValue).toBuilder());
            }
            return false;
        }
        return true;
    }
}
