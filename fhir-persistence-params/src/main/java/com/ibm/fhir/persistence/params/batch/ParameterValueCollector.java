/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.MetricHandle;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.ProfileParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.ReferenceParameter;
import com.ibm.fhir.persistence.index.SearchParameterValue;
import com.ibm.fhir.persistence.index.SecurityParameter;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TagParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.persistence.params.api.BatchParameterValue;
import com.ibm.fhir.persistence.params.api.IParamValueCollector;
import com.ibm.fhir.persistence.params.api.IParamValueProcessor;
import com.ibm.fhir.persistence.params.api.IParameterIdentityCache;
import com.ibm.fhir.persistence.params.api.ParamMetrics;
import com.ibm.fhir.persistence.params.model.CodeSystemValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValueKey;
import com.ibm.fhir.persistence.params.model.CommonTokenValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValueKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * Collects together a set of parameter value objects in preparation for them to be stored in the database
 * as a series of batch insert operations
 */
public class ParameterValueCollector implements IParamValueCollector {
    private static final Logger logger = Logger.getLogger(ParameterValueCollector.class.getName());
    private static final short FIXED_SHARD = 0;

    // The processed values we've collected
    private final List<BatchParameterValue> batchedParameterValues = new ArrayList<>();

    // the cache we use for various lookups
    protected final IParameterIdentityCache identityCache;

    // All logical_resource_ident values we've seen
    private final Map<LogicalResourceIdentKey,LogicalResourceIdentValue> logicalResourceIdentMap = new HashMap<>();

    // All parameter names we've seen (cleared if there's a rollback)
    private final Map<String,ParameterNameValue> parameterNameMap = new HashMap<>();

    // A map of code system name to the value holding its codeSystemId from the database
    private final Map<String, CodeSystemValue> codeSystemValueMap = new HashMap<>();

    // A map to support lookup of CommonTokenValue records by key
    private final Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap = new HashMap<>();

    // A map to support lookup of CommonCanonicalValue records by key
    private final Map<CommonCanonicalValueKey, CommonCanonicalValue> commonCanonicalValueMap = new HashMap<>();

    // A list of all the logical_resource_ident values for which we don't yet know the logical_resource_id
    private final List<LogicalResourceIdentValue> unresolvedLogicalResourceIdents = new ArrayList<>();

    // All parameter names in the current transaction for which we don't yet know the parameter_name_id
    private final List<ParameterNameValue> unresolvedParameterNames = new ArrayList<>();

    // A list of all the CodeSystemValues for which we don't yet know the code_system_id
    private final List<CodeSystemValue> unresolvedSystemValues = new ArrayList<>();

    // A list of all the CommonTokenValues for which we don't yet know the common_token_value_id
    private final List<CommonTokenValue> unresolvedTokenValues = new ArrayList<>();

    // A list of all the CommonCanonicalValues for which we don't yet know the canonical_id
    private final List<CommonCanonicalValue> unresolvedCanonicalValues = new ArrayList<>();

    public ParameterValueCollector(IParameterIdentityCache identityCache) {
        this.identityCache = identityCache;
    }

    @Override
    public void reset() {
        batchedParameterValues.clear();
        unresolvedLogicalResourceIdents.clear();
        unresolvedParameterNames.clear();
        unresolvedSystemValues.clear();
        unresolvedTokenValues.clear();
        unresolvedCanonicalValues.clear();
        logicalResourceIdentMap.clear();
        parameterNameMap.clear();
        codeSystemValueMap.clear();
        commonTokenValueMap.clear();
        commonCanonicalValueMap.clear();
    }

    @Override
    public void publishValuesToCache() {
        for (ParameterNameValue pnv: this.unresolvedParameterNames) {
            logger.fine(() -> "Adding parameter-name to cache: '" + pnv.getParameterName() + "' -> " + pnv.getParameterNameId());
            identityCache.addParameterName(pnv.getParameterName(), pnv.getParameterNameId());
        }

        for (CommonCanonicalValue value: this.unresolvedCanonicalValues) {
            identityCache.addCommonCanonicalValue(FIXED_SHARD, value.getUrl(), value.getCanonicalId());
        }

        for (CodeSystemValue value: this.unresolvedSystemValues) {
            identityCache.addCodeSystem(value.getCodeSystem(), value.getCodeSystemId());
        }

        for (CommonTokenValue value: this.unresolvedTokenValues) {
            identityCache.addCommonTokenValue(FIXED_SHARD, value.getCodeSystemValue().getCodeSystem(), value.getCodeSystemValue().getCodeSystemId(), value.getTokenValue(), value.getCommonTokenValueId());
        }

        for (LogicalResourceIdentValue value: this.unresolvedLogicalResourceIdents) {
            identityCache.addLogicalResourceIdent(value.getResourceType(), value.getLogicalId(), value.getLogicalResourceId());
        }
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, StringParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchStringParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, LocationParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchLocationParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TokenParameter p) throws FHIRPersistenceException {
        CommonTokenValue ctv = lookupCommonTokenValue(p.getValueSystem(), p.getValueCode());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchTokenParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TagParameter p) throws FHIRPersistenceException {
        CommonTokenValue ctv = lookupCommonTokenValue(p.getValueSystem(), p.getValueCode());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchTagParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, SecurityParameter p) throws FHIRPersistenceException {
        CommonTokenValue ctv = lookupCommonTokenValue(p.getValueSystem(), p.getValueCode());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchSecurityParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ProfileParameter p) throws FHIRPersistenceException {
        CommonCanonicalValue ctv = lookupCommonCanonicalValue(p.getUrl());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchProfileParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        CodeSystemValue csv = lookupCodeSystemValue(p.getValueSystem());
        this.batchedParameterValues.add(new BatchQuantityParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, csv));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, NumberParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchNumberParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, DateParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchDateParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    public void collect(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ReferenceParameter p) throws FHIRPersistenceException {
        logger.fine(() -> "Processing reference parameter value:" + p.toString());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        LogicalResourceIdentValue lriv = lookupLogicalResourceIdentValue(p.getResourceType(), p.getLogicalId());
        this.batchedParameterValues.add(new BatchReferenceParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, lriv));
    }

    /**
     * Get the parameter name value for the given parameter value
     * @param p
     * @return
     */
    private ParameterNameValue getParameterNameId(SearchParameterValue p) throws FHIRPersistenceException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("get ParameterNameValue for [" + p.toString() + "]");
        }
        ParameterNameValue result = parameterNameMap.get(p.getName());
        if (result == null) {
            result = new ParameterNameValue(p.getName());
            parameterNameMap.put(p.getName(), result);

            // let's see if the id is available in the shared identity cache
            Integer parameterNameId = identityCache.getParameterNameId(p.getName());
            if (parameterNameId != null) {
                result.setParameterNameId(parameterNameId);
            } else {
                // ids will be created later (so that we can process them in order)
                unresolvedParameterNames.add(result);
            }
        }
        return result;
    }

    /**
     * Get the CodeSystemValue we've assigned for the given codeSystem value. This
     * may not yet have the actual code_system_id from the database yet - any values
     * we don't have will be assigned in a later phase (so we can do things neatly
     * in bulk).
     * @param codeSystem
     * @return
     */
    private CodeSystemValue lookupCodeSystemValue(String codeSystem) {
        CodeSystemValue result = this.codeSystemValueMap.get(codeSystem);
        if (result == null) {
            result = new CodeSystemValue(codeSystem);
            this.codeSystemValueMap.put(codeSystem, result);

            // Take this opportunity to see if we have a cached value for this codeSystem
            Integer codeSystemId = identityCache.getCodeSystemId(codeSystem);
            if (codeSystemId != null) {
                result.setCodeSystemId(codeSystemId);
            } else {
                // Stash for later resolution
                this.unresolvedSystemValues.add(result);
            }
        }
        return result;
    }

    /**
     * Get the CommonTokenValue we've assigned for the given (codeSystem, tokenValue) tuple.
     * The returned value may not yet have the actual common_token_value_id yet - we fetch
     * these values later and create new database records as necessary.
     * @param codeSystem
     * @param tokenValue
     * @return
     */
    private CommonTokenValue lookupCommonTokenValue(String codeSystem, String tokenValue) {
        if (tokenValue == null) {
            return null;
        }

        CommonTokenValueKey key = new CommonTokenValueKey(FIXED_SHARD, codeSystem, tokenValue);
        CommonTokenValue result = this.commonTokenValueMap.get(key);
        if (result == null) {
            CodeSystemValue csv = lookupCodeSystemValue(codeSystem);
            result = new CommonTokenValue(FIXED_SHARD, csv, tokenValue);
            this.commonTokenValueMap.put(key, result);

            // Take this opportunity to see if we have a cached value for this common token value
            Long commonTokenValueId = identityCache.getCommonTokenValueId(FIXED_SHARD, codeSystem, tokenValue);
            if (commonTokenValueId != null) {
                result.setCommonTokenValueId(commonTokenValueId);
            } else {
                this.unresolvedTokenValues.add(result);
            }
        }
        return result;
    }

    /**
     * Get the LogicalResourceIdentValue we've assigned for the given (resourceType, logicalId)
     * tuple. The returned value may not yet have the actual logical_resource_id yet - we fetch
     * these values later and create new database records as necessary
     * @param resourceType
     * @param logicalId
     * @return
     */
    private LogicalResourceIdentValue lookupLogicalResourceIdentValue(String resourceType, String logicalId) {
        LogicalResourceIdentKey key = new LogicalResourceIdentKey(resourceType, logicalId);
        LogicalResourceIdentValue result = this.logicalResourceIdentMap.get(key);
        if (result == null) {
            result = LogicalResourceIdentValue.builder()
                    .withResourceTypeId(identityCache.getResourceTypeId(resourceType))
                    .withResourceType(resourceType)
                    .withLogicalId(logicalId)
                    .build();
            this.logicalResourceIdentMap.put(key, result);

            // see if we can find the logical_resource_id from the cache
            Long logicalResourceId = identityCache.getLogicalResourceIdentId(resourceType, logicalId);
            if (logicalResourceId != null) {
                result.setLogicalResourceId(logicalResourceId);
            } else {
                // Add to the unresolved list to look up later
                this.unresolvedLogicalResourceIdents.add(result);
            }
        }
        return result;
    }

    /**
     * Get the CommonCanonicalValue we've assigned for the given url value.
     * The returned value may not yet have the actual canonical_id yet - we fetch
     * these values later and create new database records as necessary.
     * @param url
     * @return
     */
    private CommonCanonicalValue lookupCommonCanonicalValue(String url) {
        CommonCanonicalValueKey key = new CommonCanonicalValueKey(FIXED_SHARD, url);
        CommonCanonicalValue result = this.commonCanonicalValueMap.get(key);
        if (result == null) {
            result = new CommonCanonicalValue(FIXED_SHARD, url);
            this.commonCanonicalValueMap.put(key, result);

            // Take this opportunity to see if we have a cached value for this common token value
            Long canonicalId = identityCache.getCommonCanonicalValueId(FIXED_SHARD, url);
            if (canonicalId != null) {
                result.setCanonicalId(canonicalId);
            } else {
                this.unresolvedCanonicalValues.add(result);
            }
        }
        return result;
    }

    @Override
    public void publish(IParamValueProcessor processor) throws FHIRPersistenceException {
        // resolve any missing ids
        try (MetricHandle createMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PARAM_RESOLVE_LOGICAL_RESOURCES.name())) {
            processor.resolveLogicalResourceIdents(unresolvedLogicalResourceIdents, logicalResourceIdentMap);
        }
        try (MetricHandle createMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PARAM_RESOLVE_PARAMETER_NAMES.name())) {
            processor.resolveParameterNames(unresolvedParameterNames, parameterNameMap);
        }
        try (MetricHandle createMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PARAM_RESOLVE_CODE_SYSTEMS.name())) {
            processor.resolveSystemValues(unresolvedSystemValues, codeSystemValueMap);
        }
        try (MetricHandle createMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PARAM_RESOLVE_COMMON_TOKEN_VALUES.name())) {
            processor.resolveCommonTokenValues(unresolvedTokenValues, commonTokenValueMap);
        }
        try (MetricHandle createMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PARAM_RESOLVE_CANONICAL_VALUES.name())) {
            processor.resolveCanonicalValues(unresolvedCanonicalValues, commonCanonicalValueMap);
        }

        try (MetricHandle createMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PARAM_PUBLISH.name())) {
            for (BatchParameterValue v: this.batchedParameterValues) {
                processor.publish(v);
            }
        }
        // note: do not publish to the cache here...need to wait for the transaction to commit
    }
    
}
