/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;

import static com.ibm.fhir.model.type.String.string;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

/**
 * Copy the value of each element within a Resource/Element to a new element
 * with the same values, replacing {@code Reference.reference} values with a new value
 * 
 * @param <T> The type to copy. Only visitables of this type should be visited.
 */
public class ReferenceMappingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private static final Logger log = java.util.logging.Logger.getLogger(ReferenceMappingVisitor.class.getName());
    private Map<String, String> localRefMap;

    /**
     * @param localRefMap a mapping from the current Reference values to the desired ones
     */
    public ReferenceMappingVisitor(Map<String, String> localRefMap) {
        super();
        this.localRefMap = localRefMap;
    }

    @Override
    public boolean visit(String elementName, int elementIndex, Reference reference) {
        if (reference != null && reference.getReference() != null && reference.getReference().hasValue()) {
            String refValue = reference.getReference().getValue();
            String newRefValue = localRefMap.get(refValue);
            if (newRefValue != null) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Replacing '" + refValue + "' with new value '" + newRefValue + "'");
                }
                ((Reference.Builder) getBuilder()).reference(string(newRefValue));
                markDirty();
            } else {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Reference '" + refValue + "' is not replaced "
                            + "because it was not found in the local reference map");
                }
            }
        }
        return false;
    }
}
