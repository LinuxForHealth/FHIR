/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import static com.ibm.fhir.model.type.String.string;

import java.util.Map;
import java.util.logging.Logger;

import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

/**
 * Copy the value of each element within a Resource/Element to a new element
 * with the same values, replacing local Reference values with a new value
 * 
 * @author AlbertWang
 * @param <T> The type to copy. Only visitables of this type should be visited.
 */
public class ReferenceMappingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private static final Logger log = java.util.logging.Logger.getLogger(ReferenceMappingVisitor.class.getName());
    private Map<String, String> localRefMap;
    String errorMsg = null;

    /**
     * @param localRefMap
     */
    public ReferenceMappingVisitor(Map<String, String> localRefMap) {
        super();
        this.localRefMap = localRefMap;

    }

    @Override
    public boolean visit(String elementName, int elementIndex, Reference reference) {
        if (reference != null && reference.getReference() != null && reference.getReference().hasValue()) {
            String value = reference.getReference().getValue();
            // Use more generic urn: to handle all cases including the legacy integration tests
            if (value.startsWith("urn:")) {
                String externalRef = localRefMap.get(value);
                if (externalRef == null) {
                    errorMsg += "Local reference '" + value + "' is undefined in the request bundle. ";
                } else {
                    ((Reference.Builder) getBuilder()).reference(string(externalRef));
                    markDirty();
                    log.finer("Convert local ref '" + value + "' to external ref '" + externalRef + "'.");
                }
            }
        }
        return false;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

}
