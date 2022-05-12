/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.api;


/**
 * Interface to hides the implementation of various caches we use during
 * ingestion persistence
 */
public interface IdentityCache {
    /**
     * Get the parameter_name_id value for the given parameterName
     * @param parameterName
     * @return the parameter_name_id or null if the value is not found in the cache
     */
    Integer getParameterNameId(String parameterName);

    /**
     * Get the code_system_id value for the given codeSystem value
     * @param codeSystem
     * @return the code_system_id or null if the value is not found in the cache
     */
    Integer getCodeSystemId(String codeSystem);

    /**
     * Get the common_token_value_id for the given codeSystem and tokenValue
     * @param shardKey
     * @param codeSystem
     * @param tokenValue
     * @return the common_token_value_id or null if the value is not found in the cache
     */
    Long getCommonTokenValueId(short shardKey, String codeSystem, String tokenValue);

    /**
     * Add the given parameterName to parameterNameId mapping to the cache
     * @param parameterName
     * @param parameterNameId
     */
    void addParameterName(String parameterName, int parameterNameId);

    /**
     * @param shardKey
     * @param url
     * @return
     */
    Long getCommonCanonicalValueId(short shardKey, String url);
}
