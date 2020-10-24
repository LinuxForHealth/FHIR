/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.util.Collection;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.LocalResourceReferenceRec;

/**
 *
 */
public interface IResourceReferenceDAO {

    /**
     * Get the cache used by the DAO
     * @return
     */
    ICommonTokenValuesCache getResourceReferenceCache();
    
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
     * Add TOKEN_VALUE_MAP records, creating any CODE_SYSTEMS and COMMON_TOKEN_VALUES
     * as necessary
     * @param resourceType
     * @param xrefs
     */
    void addCommonTokenValues(String resourceType, Collection<ResourceTokenValueRec> xrefs);

    /**
     * Persist the records, which may span multiple resource types
     * @param records
     */
    void persist(Collection<ResourceTokenValueRec> records);

}
