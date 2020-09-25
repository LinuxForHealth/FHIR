/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.util.Collection;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.ExternalResourceReferenceRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.LocalResourceReferenceRec;

/**
 *
 */
public interface IResourceReferenceDAO {

    /**
     * Get the cache used by the DAO
     * @return
     */
    IResourceReferenceCache getResourceReferenceCache();
    
    /**
     * Execute any statements with pending batch entries
     * @throws FHIRPersistenceException
     */
    void flush() throws FHIRPersistenceException;
    
    /**
     * Delete current external references for a given resource type and logical id. Typically
     * called when creating a new version of a resource or when re-indexing
     * @param resourceTypeId
     * @param logicalId
     */
    void deleteExternalReferences(int resourceTypeId, String logicalId);

    /**
     * Delete current local references for a given resource described by its
     * logical_resource_id. Typically called when creating a new version of a
     * resource or when re-indexing.
     * @param resourceType
     * @param logicalId
     */
    void deleteLocalReferences(long logicalResourceId);

    /**
     * Delete the membership this resource has with other compartments
     * @param logicalResourceId
     */
    void deleteLogicalResourceCompartments(long logicalResourceId);

    /**
     * Add the list of external references. Creates new external_system and external_reference_value
     * records as necessary
     * @param xrefs
     */
    void addExternalReferences(Collection<ExternalResourceReferenceRec> xrefs);

    /**
     * Add the local references (assumes the referenced records have been created
     * and the LocalResourceReferenceRec objects have been updated with the corresponding
     * database ids (foreign keys)
     * @param lrefs
     */
    void addLocalReferences(Collection<LocalResourceReferenceRec> lrefs);

    /**
     * Create a new logical id record for a resource which is the target of a reference
     * but which hasn't yet been loaded. This is simply a record in the global
     * logical_resources table which doesn't point to a current resource version.
     * TODO make sure the add_any_resource stored proc can handle this
     * @param resourceType
     * @param logicalId
     * @return the logical_resource_id of the record (existing or new)
     */
    long createGhostLogicalResource(String resourceType, String logicalId) throws FHIRPersistenceException;

}
