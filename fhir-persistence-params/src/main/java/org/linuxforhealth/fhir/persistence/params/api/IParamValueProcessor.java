/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.api;

import java.util.List;
import java.util.Map;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.params.model.CodeSystemValue;
import org.linuxforhealth.fhir.persistence.params.model.CommonCanonicalValue;
import org.linuxforhealth.fhir.persistence.params.model.CommonCanonicalValueKey;
import org.linuxforhealth.fhir.persistence.params.model.CommonTokenValue;
import org.linuxforhealth.fhir.persistence.params.model.CommonTokenValueKey;
import org.linuxforhealth.fhir.persistence.params.model.LogicalResourceIdentKey;
import org.linuxforhealth.fhir.persistence.params.model.LogicalResourceIdentValue;
import org.linuxforhealth.fhir.persistence.params.model.ParameterNameValue;

/**
 * Interface for a processor which can handle the different parameter types defined at the schema level.
 * Implementations may need to collect values first before executing any actual inserts. This is required
 * so that the processor can resolve any referenced foreign key values (like common_token_value,
 * common_canonical_value for example).
 */
public interface IParamValueProcessor {

    /**
     * Publish the given parameter value (which by now should have any FK references it needs)
     * @param bpv
     */
    void publish(BatchParameterValue bpv) throws FHIRPersistenceException;

    /**
     * Initialize the processor ready to start a new batch
     */
    void startBatch();

    /**
     * Close out the current batch, completing the processing for any parameter values
     * collected so far
     */
    void close();

    /**
     * Push any data we've accumulated so far. This may occur
     * if we cross a volume or functional threshold, and will
     * always occur as the last step before the current transaction
     * is committed.
     * @throws FHIRPersistenceException
     */
    void pushBatch() throws FHIRPersistenceException;

    /**
     * Call when a transaction has been rolled back because of a deadlock
     * or other retryable error and we want to try and process the batch again
     */
    void resetBatch();

    /**
     * Make sure we have values for all the logical_resource_ident values
     * we have collected in the current batch. Need to make sure these are
     * added in order to minimize deadlocks. Note that because we may create
     * new logical_resource_ident records, we could be blocked by the main
     * add_any_resource procedure run within the server CREATE/UPDATE
     * @param unresolvedLogicalResourceIdents
     * @param logicalResourceIdentMap
     * @throws FHIRPersistenceException
     */
    void resolveLogicalResourceIdents(List<LogicalResourceIdentValue> unresolvedLogicalResourceIdents,
        Map<LogicalResourceIdentKey, LogicalResourceIdentValue> logicalResourceIdentMap) throws FHIRPersistenceException;

    /**
     * @param unresolvedParameterNames
     * @param parameterNameMap
     * @throws FHIRPersistenceException
     */
    void resolveParameterNames(List<ParameterNameValue> unresolvedParameterNames, Map<String, ParameterNameValue> parameterNameMap) throws FHIRPersistenceException;

    /**
     * @param unresolvedSystemValues
     * @param codeSystemValueMap
     * @throws FHIRPersistenceException
     */
    void resolveSystemValues(List<CodeSystemValue> unresolvedSystemValues, Map<String, CodeSystemValue> codeSystemValueMap) throws FHIRPersistenceException;

    /**
     * @param unresolvedTokenValues
     * @param commonTokenValueMap
     * @throws FHIRPersistenceException
     */
    void resolveCommonTokenValues(List<CommonTokenValue> unresolvedTokenValues, Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap) throws FHIRPersistenceException;

    /**
     * @param unresolvedCanonicalValues
     * @param commonCanonicalValueMap
     * @throws FHIRPersistenceException
     */
    void resolveCanonicalValues(List<CommonCanonicalValue> unresolvedCanonicalValues,
            Map<CommonCanonicalValueKey, CommonCanonicalValue> commonCanonicalValueMap) throws FHIRPersistenceException;

}
