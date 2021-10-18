/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValueResult;

/**
 * Contract for DAO implementations handling persistence of
 * resource references (and token parameters) with the
 * normalized schema introduced in issue 1366.
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
     * Add TOKEN_VALUE_MAP records, creating any CODE_SYSTEMS and COMMON_TOKEN_VALUES
     * as necessary
     * @param resourceType
     * @param xrefs
     * @param profileRecs
     * @param tagRecs
     * @param securityRecs
     */
    void addNormalizedValues(String resourceType, Collection<ResourceTokenValueRec> xrefs, Collection<ResourceProfileRec> profileRecs, Collection<ResourceTokenValueRec> tagRecs, Collection<ResourceTokenValueRec> securityRecs) throws FHIRPersistenceException;

    /**
     * Persist the records, which may span multiple resource types
     * @param records
     * @param profileRecs
     * @param tagRecs
     * @param securityRecs
     */
    void persist(Collection<ResourceTokenValueRec> records, Collection<ResourceProfileRec> profileRecs, Collection<ResourceTokenValueRec> tagRecs, Collection<ResourceTokenValueRec> securityRecs) throws FHIRPersistenceException;

    /**
     * Find the database id for the given token value and system
     * @param codeSystem
     * @param tokenValue
     * @return the matching id from common_token_values.common_token_value_id or null if not found
     */
    CommonTokenValueResult readCommonTokenValueId(String codeSystem, String tokenValue);

    /**
     * Find database ids for a set of common token values
     * @param tokenValues
     * @return a non-null, possibly-empty set of ids from common_token_values.common_token_value_id;
     *      CommonTokenValues with no corresponding record will be omitted from the set
     */
    Set<CommonTokenValueResult> readCommonTokenValueIds(Collection<CommonTokenValue> tokenValues);

    /**
     * Fetch the list of matching common_token_value_id records for the given tokenValue.
     * @param tokenValue
     * @return
     */
    List<Long> readCommonTokenValueIdList(String tokenValue);

    /**
     * Read the database canonical_id for the given value
     * @param canonicalValue
     * @return
     */
    Integer readCanonicalId(String canonicalValue);
}