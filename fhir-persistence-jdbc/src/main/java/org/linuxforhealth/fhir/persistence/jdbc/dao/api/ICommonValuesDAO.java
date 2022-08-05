/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.linuxforhealth.fhir.persistence.jdbc.dto.CommonTokenValue;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CommonTokenValueResult;

/**
 * Contract for DAO implementations handling persistence of
 * resource references (and token parameters) with the
 * normalized schema introduced in issue 1366.
 */
public interface ICommonValuesDAO {

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
    Long readCanonicalId(String canonicalValue);
}