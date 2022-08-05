/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.visitor.CopyingVisitor;
import org.linuxforhealth.fhir.model.visitor.Visitable;

/**
 * Copy the value of each element within a Resource/Element to a new element
 * with the same values, replacing {@code Reference.reference} values with a new value
 * 
 * @param <T> The type to copy. Only visitables of this type should be visited.
 */
public class ReferenceMappingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private static final Logger log = java.util.logging.Logger.getLogger(ReferenceMappingVisitor.class.getName());
    private Map<String, String> localRefMap;
    private String localIdentifier;

    /**
     * @param localRefMap a mapping from the current Reference values to the desired ones
     * @param localIdentifier  a bundle entry fullUrl value - potentially used with relative references
     */
    public ReferenceMappingVisitor(Map<String, String> localRefMap, String localIdentifier) {
        super();
        this.localRefMap = localRefMap;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public boolean visit(String elementName, int elementIndex, Reference reference) {
        if (reference != null && reference.getReference() != null && reference.getReference().hasValue()) {
            String refValue = FHIRUtil.buildBundleReference(reference, localIdentifier);
            String newRefValue = localRefMap.get(refValue);
            if (newRefValue != null) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Replacing '" + refValue + "' with new value '" + newRefValue + "'");
                }
                ((Reference.Builder) getBuilder()).reference(string(newRefValue));
                markDirty();
            } else {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Reference '" + reference.getReference().getValue() + "' is not replaced "
                            + "because it was not found in the local reference map");
                }
            }
        }
        return false;
    }
}
