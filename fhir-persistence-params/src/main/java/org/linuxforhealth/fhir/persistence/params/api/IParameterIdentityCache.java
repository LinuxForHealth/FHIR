/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.api;


/**
 * Interface to hides the implementation of various caches we use during
 * ingestion persistence.
 */
public interface IParameterIdentityCache {
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

    /**
     * Add the common canonical value to the cache
     * @param shardKey
     * @param url
     * @param commonCanonicalValueId
     */
    void addCommonCanonicalValue(short shardKey, String url, long commonCanonicalValueId);

    /**
     * Add the common token value to the cache.
     * @param shardKey
     * @param codeSystem
     * @param codeSystemId
     * @param tokenValue
     * @param commonTokenValueId
     */
    void addCommonTokenValue(short shardKey, String codeSystem, int codesSystemId, String tokenValue, long commonTokenValueId);

    /**
     * Add the code system value to the cache
     * @param codeSystem
     * @param codeSystemId
     */
    void addCodeSystem(String codeSystem, int codeSystemId);

    /**
     * Get the database resource_type_id value for the given resourceType value
     * @param resourceType
     * @return
     * @throws IllegalArgumentException if resourceType is not a valid resource type name
     */
    int getResourceTypeId(String resourceType);

    /**
     * Get the database logical_resource_id for the given resourceType/logicalId tuple.
     * @param resourceType
     * @param logicalId
     * @return
     */
    Long getLogicalResourceIdentId(String resourceType, String logicalId);

    /**
     * Add the logical_resource_ident mapping to the cache
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     */
    void addLogicalResourceIdent(String resourceType, String logicalId, long logicalResourceId);
}
