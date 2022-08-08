/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CommonTokenValue;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ResourceReferenceValue;

/**
 * Provides access to all the identity information we need when processing
 * resources and their parameters
 */
public interface JDBCIdentityCache {

    /**
     * Get the database id for the named resourceType. Reads from a cache or database
     * if required.
     * @param resourceType
     * @return
     * @throws FHIRPersistenceException
     */
    Integer getResourceTypeId(String resourceType) throws FHIRPersistenceException;

    /**
     * Get the resource type name for the resourceTypeId. Reads from a cache or database
     * if required.
     * @param resourceTypeId
     * @return
     * @throws FHIRPersistenceException
     */
    String getResourceTypeName(Integer resourceTypeId) throws FHIRPersistenceException;

    /**
     * Get the database id for the named code-system. Creates new records if necessary
     * @param codeSystem
     * @return
     * @throws FHIRPersistenceException
     */
    Integer getCodeSystemId(String codeSystem) throws FHIRPersistenceException;

    /**
     * Get the database id for the given canonical value. Read only. If the value
     * does not exist, -1 is returned.
     * @param canonicalValue
     * @return
     * @throws FHIRPersistenceException
     */
    Long getCanonicalId(String canonicalValue) throws FHIRPersistenceException;

    /**
     * Get the database id for the given (resourceType, logicalId) tuple. This
     * represents records in logical_resource_ident which may be created before
     * the actual resource is created.
     * @param resourceType
     * @param logicalId
     * @return
     */
    Long getLogicalResourceId(String resourceType, String logicalId) throws FHIRPersistenceException;

    /**
     * Get the database id for the given parameter name. Creates new records if necessary.
     * @param parameterName
     * @return
     * @throws FHIRPersistenceException
     */
    Integer getParameterNameId(String parameterName) throws FHIRPersistenceException;

    /**
     * Get the common_token_value_id for the given tokenValue and codeSystem. Reads from
     * a cache, or the database if not found in the cache.
     * @param codeSystem
     * @param tokenValue
     * @return The common token value id or null if it doesn't exist
     */
    Long getCommonTokenValueId(String codeSystem, String tokenValue);

    /**
     * Get the common_token_value_ids for the given tokenValues. Reads from
     * a cache, or the database if not found in the cache.
     * CommonTokenValues with no corresponding record in the database will
     * be omitted from the result set.
     * @param tokenValues
     * @return A non-null, possibly-empty set of common token value ids
     */
    Set<Long> getCommonTokenValueIds(Collection<CommonTokenValue> tokenValues);

    /**
     * Get the logical_resource_ids for the given referenceValues. Reads from
     * a cache, or the database if not found in the cache. Values with no
     * corresponding record in the database will be omitted from the result set.
     * @param referenceValues
     * @return a non-null, possibly empty set of logical_resource_ids.
     */
    Set<Long> getLogicalResourceIds(Collection<ResourceReferenceValue> referenceValues) throws FHIRPersistenceException;

    /**
     * Get a list of matching common_token_value_id values. Implementations may decide
     * to cache, but only if the cache can be invalidated when the list changes due to
     * ingestion. The simplest approach is to always read from the database. The performance
     * benefit to the FHIR search query this is being used for is orders of magnitude
     * greater than the cost of this query (think 80+ seconds to 250 milliseconds improvement
     * in search query time).
     * @param tokenValue
     * @return
     */
    List<Long> getCommonTokenValueIdList(String tokenValue);

    /**
     * Get a list of logical_resource_id values matching the given logicalId without
     * knowing the resource type. This means we could get back multiple ids, one per
     * resource type, such as:
     * <ul>
     * <li>Claim/foo
     * <li>Observation/foo
     * <li>Patient/foo
     * </ul>
     * @param tokenValue
     * @return
     */
    List<Long> getLogicalResourceIdList(String logicalId) throws FHIRPersistenceException;

    /**
     * Get the list of all resource type names.
     * @return
     */
    List<String> getResourceTypeNames() throws FHIRPersistenceException;

    /**
     * Get the list of all resource type ids.
     * @return
     */
    List<Integer> getResourceTypeIds() throws FHIRPersistenceException;
}