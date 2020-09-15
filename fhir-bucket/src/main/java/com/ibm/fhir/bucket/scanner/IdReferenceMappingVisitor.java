/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import static com.ibm.fhir.model.type.String.string;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;


/**
 * An extension of the ReferenceMappingVisitor which also updates the "id" of the resource
 * because it is being changed from a POST to a PUT, and therefore the id must align with
 * the identity of the resource according to the corresponding fullUrl in the Bundle
 */
public class IdReferenceMappingVisitor extends ReferenceMappingVisitor<Resource> {
    private static final Logger logger = Logger.getLogger(IdReferenceMappingVisitor.class.getName());
    private static final String CONTAINED = "contained";
    private final String newId;
    
    /**
     * Public constructor
     * @param localRefMap
     */
    public IdReferenceMappingVisitor(Map<String, String> localRefMap, String newId) {
        super(localRefMap);
        this.newId = newId;
    }
    
    @Override
    public boolean visit(String elementName, int elementIndex, Resource resource) {
        
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("visit " + elementName + ", " + elementIndex + ", " + resource.getClass().getSimpleName());
        }
        
        // don't replace the ids of any contained resources
        if (!CONTAINED.equals(elementName)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Replacing current Resource id '" + resource.getId() + "' with '" + newId + "'");
            }
            ((Resource.Builder) getBuilder()).id(newId);
        }
        
        markDirty();
        
        // continue visiting children of this resource
        return true;
    }

    @Override
    public boolean visit(String elementName, int elementIndex, DomainResource resource) {

        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("visit " + elementName + ", " + elementIndex + ", " + resource.getClass().getSimpleName());
        }

        // don't replace the ids of any contained resources
        if (!CONTAINED.equals(elementName)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Replacing current DomainResource id '" + resource.getId() + "' with '" + newId + "'");
            }
            ((DomainResource.Builder) getBuilder()).id(newId);
        }

        markDirty();
        
        // continue visiting children of this resource
        return true;
    }

}
