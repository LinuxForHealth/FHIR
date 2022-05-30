/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.ResultSetReader;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.ProfileParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.ReferenceParameter;
import com.ibm.fhir.persistence.index.RemoteIndexMessage;
import com.ibm.fhir.persistence.index.SearchParameterValue;
import com.ibm.fhir.persistence.index.SecurityParameter;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TagParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.remote.index.api.BatchParameterValue;
import com.ibm.fhir.remote.index.api.IdentityCache;
import com.ibm.fhir.remote.index.batch.BatchDateParameter;
import com.ibm.fhir.remote.index.batch.BatchLocationParameter;
import com.ibm.fhir.remote.index.batch.BatchNumberParameter;
import com.ibm.fhir.remote.index.batch.BatchProfileParameter;
import com.ibm.fhir.remote.index.batch.BatchQuantityParameter;
import com.ibm.fhir.remote.index.batch.BatchReferenceParameter;
import com.ibm.fhir.remote.index.batch.BatchSecurityParameter;
import com.ibm.fhir.remote.index.batch.BatchStringParameter;
import com.ibm.fhir.remote.index.batch.BatchTagParameter;
import com.ibm.fhir.remote.index.batch.BatchTokenParameter;

/**
 * Loads search parameter values into the target PostgreSQL database using
 * the plain (non-sharded) schema variant.
 */
public class PlainPostgresMessageHandler extends BaseMessageHandler {
    private static final Logger logger = Logger.getLogger(PlainPostgresMessageHandler.class.getName());
    private static final short FIXED_SHARD = 0;

    // the connection to use for the inserts
    protected final Connection connection;

    // We're a PostgreSQL DAO, so we now which translator to use
    protected final IDatabaseTranslator translator = new PostgresTranslator();

    // The FHIR data schema
    protected final String schemaName;

    // the cache we use for various lookups
    protected final IdentityCache identityCache;

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
    
    // The processed values we've collected
    private final List<BatchParameterValue> batchedParameterValues = new ArrayList<>();

    // The processor used to process the batched parameter values after all the reference values are created
    private final PlainBatchParameterProcessor batchProcessor;

    protected final int maxLogicalResourcesPerStatement = 256;
    protected final int maxCodeSystemsPerStatement = 512;
    protected final int maxCommonTokenValuesPerStatement = 256;
    protected final int maxCommonCanonicalValuesPerStatement = 256;
    private boolean rollbackOnly;

    /**
     * Public constructor
     * 
     * @param connection
     * @param schemaName
     * @param cache
     * @param maxReadyTimeMs
     */
    public PlainPostgresMessageHandler(Connection connection, String schemaName, IdentityCache cache, long maxReadyTimeMs) {
        super(maxReadyTimeMs);
        this.connection = connection;
        this.schemaName = schemaName;
        this.identityCache = cache;
        this.batchProcessor = new PlainBatchParameterProcessor(connection);
    }

    @Override
    protected void startBatch() {
        // always start with a clean slate
        batchedParameterValues.clear();
        unresolvedLogicalResourceIdents.clear();
        unresolvedParameterNames.clear();
        unresolvedSystemValues.clear();
        unresolvedTokenValues.clear();
        unresolvedCanonicalValues.clear();
        batchProcessor.startBatch();
    }

    @Override
    protected void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    @Override
    public void close() {
        try {
            batchProcessor.close();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "close batchProcessor failed" , t);
        }
    }

    @Override
    protected void endTransaction() throws FHIRPersistenceException {
        boolean committed = false;
        try {
            if (!this.rollbackOnly) {
                logger.fine("Committing transaction");
                connection.commit();
                committed = true;

                // any values from parameter_names, code_systems and common_token_values
                // are now committed to the database, so we can publish their record ids
                // to the shared cache which makes them accessible from other threads 
                publishCachedValues();
            } else {
                // something went wrong...try to roll back the transaction before we close
                // everything
                try {
                    connection.rollback();
                } catch (SQLException x) {
                    // It could very well be that we've lost touch with the database in which case
                    // the rollback will also fail. Not much we can do, although we don't bother
                    // with a stack trace here because it's just more noise for the log file, and
                    // the exception that triggered the rollback is already going to be propagated
                    // and logged.
                    logger.severe("Rollback failed; reason=[" + x.getMessage() + "]");
                }
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("commit failed", x);
        } finally {
            if (!committed) {
                // The maps may contain ids that were not committed to the database so
                // we should clean them out in case we decide to reuse this consumer
                this.logicalResourceIdentMap.clear();
                this.parameterNameMap.clear();
                this.codeSystemValueMap.clear();
                this.commonTokenValueMap.clear();
                this.commonCanonicalValueMap.clear();
            }
        }
    }

    /**
     * After the transaction has been committed, we can publish certain values to the
     * shared identity caches
     */
    private void publishCachedValues() {
        // all the unresolvedParameterNames should be resolved at this point
        for (ParameterNameValue pnv: this.unresolvedParameterNames) {
            identityCache.addParameterName(pnv.getParameterName(), pnv.getParameterNameId());
        }
    }

    @Override
    protected void pushBatch() throws FHIRPersistenceException {
        // Push any data we've accumulated so far. This may occur
        // if we cross a volume threshold, and will always occur as
        // the last step before the current transaction is committed,
        // Process the token values so that we can establish
        // any entries we need for common_token_values
        resolveLogicalResourceIdents();
        resolveParameterNames();
        resolveCodeSystems();
        resolveCommonTokenValues();
        resolveCommonCanonicalValues();

        // Now that all the lookup values should've been resolved, we can go ahead
        // and push the parameters to the JDBC batch insert statements via the
        // batchProcessor
        for (BatchParameterValue v: this.batchedParameterValues) {
            v.apply(batchProcessor);
        }
        batchProcessor.pushBatch();
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
    
    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, StringParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchStringParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, LocationParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchLocationParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TokenParameter p) throws FHIRPersistenceException {
        CommonTokenValue ctv = lookupCommonTokenValue(p.getValueSystem(), p.getValueCode());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchTokenParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, TagParameter p) throws FHIRPersistenceException {
        CommonTokenValue ctv = lookupCommonTokenValue(p.getValueSystem(), p.getValueCode());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchTagParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, SecurityParameter p) throws FHIRPersistenceException {
        CommonTokenValue ctv = lookupCommonTokenValue(p.getValueSystem(), p.getValueCode());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchSecurityParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ProfileParameter p) throws FHIRPersistenceException {
        CommonCanonicalValue ctv = lookupCommonCanonicalValue(p.getUrl());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchProfileParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        CodeSystemValue csv = lookupCodeSystemValue(p.getValueSystem());
        this.batchedParameterValues.add(new BatchQuantityParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, csv));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, NumberParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchNumberParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, DateParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchDateParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    protected void process(String tenantId, String requestShard, String resourceType, String logicalId, long logicalResourceId, ReferenceParameter p) throws FHIRPersistenceException {
        logger.fine(() -> "Processing reference parameter value:" + p.toString());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        LogicalResourceIdentValue lriv = lookupLogicalResourceIdentValue(p.getResourceType(), p.getLogicalId());
        this.batchedParameterValues.add(new BatchReferenceParameter(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, p, lriv));
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
            this.unresolvedLogicalResourceIdents.add(result);
        }
        return result;
    }

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

    /**
     * Make sure we have values for all the code_systems we have collected
     * in the current
     * batch
     * @throws FHIRPersistenceException
     */
    private void resolveCodeSystems() throws FHIRPersistenceException {
        // identify which values aren't yet in the database
        List<CodeSystemValue> missing = fetchCodeSystemIds(unresolvedSystemValues);

        if (!missing.isEmpty()) {
            addMissingCodeSystems(missing);
        }

        // All the previously missing values should now be in the database. We need to fetch them again,
        // possibly having to use multiple queries
        List<CodeSystemValue> bad = fetchCodeSystemIds(missing);

        if (!bad.isEmpty()) {
            // shouldn't happend, but let's protected against it anyway
            throw new FHIRPersistenceException("Failed to create all code system values");
        }
    }

    /**
     * Build and prepare a statement to fetch the code_system_id and code_system_name
     * from the code_systems table for all the given (unresolved) code system values
     * @param values
     * @return
     * @throws SQLException
     */
    private PreparedStatement buildCodeSystemSelectStatement(List<CodeSystemValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT code_system_id, code_system_name FROM code_systems WHERE code_system_name IN (");
        for (int i=0; i<values.size(); i++) {
            if (i > 0) {
                query.append(",");
            }
            query.append("?");
        }
        query.append(")");
        PreparedStatement ps = connection.prepareStatement(query.toString());
        // bind the parameter values
        int param = 1;
        for (CodeSystemValue csv: values) {
            ps.setString(param++, csv.getCodeSystem());
        }
        return ps;
    }

    /**
     * These code systems weren't found in the database, so we need to try and add them.
     * We have to deal with concurrency here - there's a chance another thread could also
     * be trying to add them. To avoid deadlocks, it's important to do any inserts in a
     * consistent order. At the end, we should be able to read back values for each entry
     * @param missing
     */
    protected void addMissingCodeSystems(List<CodeSystemValue> missing) throws FHIRPersistenceException {
        List<String> values = missing.stream().map(csv -> csv.getCodeSystem()).collect(Collectors.toList());
        // Sort the code system values first to help avoid deadlocks
        Collections.sort(values); // natural ordering for String is fine here

        final String nextVal = translator.nextValue(schemaName, "fhir_ref_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO code_systems (code_system_id, code_system_name) VALUES (");
        insert.append(nextVal); // next sequence value
        insert.append(",?) ON CONFLICT DO NOTHING");

        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            int count = 0;
            for (String codeSystem: values) {
                ps.setString(1, codeSystem);
                ps.addBatch();
                if (++count == this.maxCodeSystemsPerStatement) {
                    // not too many statements in a single batch
                    ps.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "code systems fetch failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("code systems fetch failed");
        }
    }

    private List<CodeSystemValue> fetchCodeSystemIds(List<CodeSystemValue> unresolved) throws FHIRPersistenceException {
        // track which values aren't yet in the database
        List<CodeSystemValue> missing = new ArrayList<>();

        int offset = 0;
        while (offset < unresolved.size()) {
            int remaining = unresolved.size() - offset;
            int subSize = Math.min(remaining, this.maxCodeSystemsPerStatement);
            List<CodeSystemValue> sub = unresolved.subList(offset, offset+subSize); // remember toIndex is exclusive
            offset += subSize; // set up for the next iteration
            try (PreparedStatement ps = buildCodeSystemSelectStatement(sub)) {
                ResultSet rs = ps.executeQuery();
                // We can't rely on the order of result rows matching the order of the in-list,
                // so we have to go back to our map to look up each CodeSystemValue
                int resultCount = 0;
                while (rs.next()) {
                    resultCount++;
                    CodeSystemValue csv = this.codeSystemValueMap.get(rs.getString(2));
                    if (csv != null) {
                        csv.setCodeSystemId(rs.getInt(1));
                    } else {
                        // can't really happen, but be defensive
                        throw new FHIRPersistenceException("code systems query returned an unexpected value");
                    }
                }

                // Most of the time we'll get everything, so we can bypass the check for
                // missing values
                if (resultCount == 0) {
                    // 100% miss
                    missing.addAll(sub);
                } else if (resultCount < subSize) {
                    // need to scan the sub list and see which values we don't yet have ids for
                    for (CodeSystemValue csv: sub) {
                        if (csv.getCodeSystemId() == null) {
                            missing.add(csv);
                        }
                    }
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "code systems fetch failed", x);
                throw new FHIRPersistenceException("code systems fetch failed");
            }
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }

    /**
     * Make sure we have values for all the common_token_value records we have collected
     * in the current batch
     * @throws FHIRPersistenceException
     */
    private void resolveCommonTokenValues() throws FHIRPersistenceException {
        // identify which values aren't yet in the database
        List<CommonTokenValue> missing = fetchCommonTokenValueIds(unresolvedTokenValues);

        if (!missing.isEmpty()) {
            // Sort first to minimize deadlocks
            Collections.sort(missing, (a,b) -> {
                int result = a.getTokenValue().compareTo(b.getTokenValue());
                if (result == 0) {
                    result = Integer.compare(a.getCodeSystemValue().getCodeSystemId(), b.getCodeSystemValue().getCodeSystemId());
                    if (result == 0) {
                        result = Short.compare(a.getShardKey(), b.getShardKey());
                    }
                }
                return result;
            });
            addMissingCommonTokenValues(missing);
        }

        // All the previously missing values should now be in the database. We need to fetch them again,
        // possibly having to use multiple queries
        List<CommonTokenValue> bad = fetchCommonTokenValueIds(missing);

        if (!bad.isEmpty()) {
            // shouldn't happend, but let's protected against it anyway
            throw new FHIRPersistenceException("Failed to create all common token values");
        }
    }

    /**
     * Build and prepare a statement to fetch the common_token_value records
     * for all the given (unresolved) code system values
     * @param values
     * @return SELECT code_system, token_value, common_token_value_id
     * @throws SQLException
     */
    private PreparedStatementWrapper buildCommonTokenValueSelectStatement(List<CommonTokenValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        // need the code_system name - so we join back to the code_systems table as well
        query.append("SELECT cs.code_system_name, c.token_value, c.common_token_value_id ");
        query.append("  FROM common_token_values c");
        query.append("  JOIN code_systems cs ON (cs.code_system_id = c.code_system_id)");
        query.append("  JOIN (VALUES ");

        // Create a (codeSystem, tokenValue) tuple for each of the CommonTokenValue records
        boolean first = true;
        for (CommonTokenValue ctv: values) {
            if (first) {
                first = false;
            } else {
                query.append(",");
            }
            query.append("(");
            query.append(ctv.getCodeSystemValue().getCodeSystemId()); // literal for code_system_id
            query.append(",?)"); // bind variable for the token-value
        }
        query.append(") AS v(code_system_id, token_value) ");
        query.append(" ON (c.code_system_id = v.code_system_id AND c.token_value = v.token_value)");

        // Create the prepared statement and bind the values
        final String statementText = query.toString();
        PreparedStatement ps = connection.prepareStatement(statementText);

        // bind the parameter values
        int param = 1;
        for (CommonTokenValue ctv: values) {
            ps.setString(param++, ctv.getTokenValue());
        }
        return new PreparedStatementWrapper(statementText, ps);
    }
    
    private List<CommonTokenValue> fetchCommonTokenValueIds(List<CommonTokenValue> unresolved) throws FHIRPersistenceException {
        // track which values aren't yet in the database
        List<CommonTokenValue> missing = new ArrayList<>();

        int offset = 0;
        while (offset < unresolved.size()) {
            int remaining = unresolved.size() - offset;
            int subSize = Math.min(remaining, this.maxCommonTokenValuesPerStatement);
            List<CommonTokenValue> sub = unresolved.subList(offset, offset+subSize); // remember toIndex is exclusive
            offset += subSize; // set up for the next iteration
            String sql = null; // the SQL text for logging when there's an error
            try (PreparedStatementWrapper ps = buildCommonTokenValueSelectStatement(sub)) {
                sql = ps.getStatementText();
                ResultSet rs = ps.executeQuery();
                // We can't rely on the order of result rows matching the order of the in-list,
                // so we have to go back to our map to look up each CodeSystemValue
                int resultCount = 0;
                while (rs.next()) {
                    resultCount++;
                    CommonTokenValueKey key = new CommonTokenValueKey(FIXED_SHARD, rs.getString(1), rs.getString(2));
                    CommonTokenValue ctv = this.commonTokenValueMap.get(key);
                    if (ctv != null) {
                        ctv.setCommonTokenValueId(rs.getLong(3));
                    } else {
                        // can't really happen, but be defensive
                        throw new FHIRPersistenceException("common token values query returned an unexpected value");
                    }
                }

                // Optimize the check for missing values
                if (resultCount == 0) {
                    // 100% miss
                    missing.addAll(sub);
                } else if (resultCount < subSize) {
                    // need to scan the sub list and see which values we don't yet have ids for
                    for (CommonTokenValue ctv: sub) {
                        if (ctv.getCommonTokenValueId() == null) {
                            missing.add(ctv);
                        }
                    }
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "common token values fetch failed. SQL=[" + sql + "]", x);
                throw new FHIRPersistenceException("common token values fetch failed");
            }
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }

    /**
     * Add the values we think are missing from the database. The given list should be
     * sorted to reduce deadlocks
     * @param missing
     * @throws FHIRPersistenceException
     */
    protected void addMissingCommonTokenValues(List<CommonTokenValue> missing) throws FHIRPersistenceException {

        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_token_values (code_system_id, token_value) ");
        insert.append("     VALUES (?,?) ");
        insert.append("ON CONFLICT DO NOTHING");

        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            int count = 0;
            for (CommonTokenValue ctv: missing) {
                ps.setInt(1, ctv.getCodeSystemValue().getCodeSystemId());
                ps.setString(2, ctv.getTokenValue());
                ps.addBatch();
                if (++count == this.maxCommonTokenValuesPerStatement) {
                    // not too many statements in a single batch
                    ps.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("failed inserting new common token values");
        }
    }

    /**
     * Make sure we have values for all the common_canonical_value records we have collected
     * in the current batch
     * @throws FHIRPersistenceException
     */
    private void resolveCommonCanonicalValues() throws FHIRPersistenceException {
        // identify which values aren't yet in the database
        List<CommonCanonicalValue> missing = fetchCanonicalIds(unresolvedCanonicalValues);

        if (!missing.isEmpty()) {
            // Sort on url to minimize deadlocks
            Collections.sort(missing, (a,b) -> {
                return a.getUrl().compareTo(b.getUrl());
            });
            addMissingCommonCanonicalValues(missing);
        }

        // All the previously missing values should now be in the database. We need to fetch them again,
        // possibly having to use multiple queries
        List<CommonCanonicalValue> bad = fetchCanonicalIds(missing);

        if (!bad.isEmpty()) {
            // shouldn't happen, but let's protected against it anyway
            throw new FHIRPersistenceException("Failed to create all canonical values");
        }
    }

    private List<CommonCanonicalValue> fetchCanonicalIds(List<CommonCanonicalValue> unresolved) throws FHIRPersistenceException {
        // track which values aren't yet in the database
        List<CommonCanonicalValue> missing = new ArrayList<>();

        int offset = 0;
        while (offset < unresolved.size()) {
            int remaining = unresolved.size() - offset;
            int subSize = Math.min(remaining, this.maxCommonCanonicalValuesPerStatement);
            List<CommonCanonicalValue> sub = unresolved.subList(offset, offset+subSize); // remember toIndex is exclusive
            offset += subSize; // set up for the next iteration
            String sql = null; // the SQL text for logging when there's an error
            try (PreparedStatementWrapper ps = buildCommonCanonicalValueSelectStatement(sub)) {
                sql = ps.getStatementText();
                ResultSet rs = ps.executeQuery();
                // We can't rely on the order of result rows matching the order of the in-list,
                // so we have to go back to our map to look up each CodeSystemValue
                int resultCount = 0;
                while (rs.next()) {
                    resultCount++;
                    CommonCanonicalValueKey key = new CommonCanonicalValueKey(FIXED_SHARD, rs.getString(1));
                    CommonCanonicalValue ctv = this.commonCanonicalValueMap.get(key);
                    if (ctv != null) {
                        ctv.setCanonicalId(rs.getLong(2));
                    } else {
                        // can't really happen, but be defensive
                        throw new FHIRPersistenceException("common canonical values query returned an unexpected value");
                    }
                }

                // Optimize the check for missing values
                if (resultCount == 0) {
                    // 100% miss
                    missing.addAll(sub);
                } else if (resultCount < subSize) {
                    // need to scan the sub list and see which values we don't yet have ids for
                    for (CommonCanonicalValue ctv: sub) {
                        if (ctv.getCanonicalId() == null) {
                            missing.add(ctv);
                        }
                    }
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "common canonical values fetch failed. SQL=[" + sql + "]", x);
                throw new FHIRPersistenceException("common canonical values fetch failed");
            }
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }

    /**
     * Build and prepare a statement to fetch the common_token_value records
     * for all the given (unresolved) code system values
     * @param values
     * @return SELECT code_system, token_value, common_token_value_id
     * @throws SQLException
     */
    private PreparedStatementWrapper buildCommonCanonicalValueSelectStatement(List<CommonCanonicalValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT c.url, c.canonical_id ");
        query.append("  FROM common_canonical_values c ");
        query.append("  WHERE c.url IN (");

        // add bind variables for each url we need to fetch
        boolean first = true;
        for (CommonCanonicalValue ctv: values) {
            if (first) {
                first = false;
            } else {
                query.append(",");
            }
            query.append("?"); // bind variable for the url
        }
        query.append(")");

        // Create the prepared statement and bind the values
        final String statementText = query.toString();
        logger.finer(() -> "fetch common canonical values [" + statementText + "]");
        PreparedStatement ps = connection.prepareStatement(statementText);

        // bind the parameter values
        int param = 1;
        for (CommonCanonicalValue ctv: values) {
            ps.setString(param++, ctv.getUrl());
        }
        return new PreparedStatementWrapper(statementText, ps);
    }

    /**
     * Add the values we think are missing from the database. The given list should be
     * sorted to reduce deadlocks
     * @param missing
     * @throws FHIRPersistenceException
     */
    protected void addMissingCommonCanonicalValues(List<CommonCanonicalValue> missing) throws FHIRPersistenceException {

        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_canonical_values (url) VALUES (?) ");
        insert.append("ON CONFLICT DO NOTHING");

        final String DML = insert.toString();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addMissingCanonicalIds: " + DML);
        }
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            int count = 0;
            for (CommonCanonicalValue ctv: missing) {
                logger.finest(() -> "Adding canonical value [" + ctv.toString() + "]");
                ps.setString(1, ctv.getUrl());
                ps.addBatch();
                if (++count == this.maxCommonCanonicalValuesPerStatement) {
                    // not too many statements in a single batch
                    ps.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("failed inserting new common canonical values");
        }
    }

    /**
     * Make sure all the parameter names we've seen in the batch exist
     * in the database and have ids.
     * @throws FHIRPersistenceException
     */
    private void resolveParameterNames() throws FHIRPersistenceException {
        // We expect parameter names to have a very high cache hit rate and
        // so we simplify processing by simply iterating one-by-one for the
        // values we still need to resolve. The most important point here is
        // to do this in a sorted order to avoid deadlock issues because this
        // could be happening across multiple consumer threads at the same time.
        Collections.sort(this.unresolvedParameterNames, (a,b) -> {
            return a.getParameterName().compareTo(b.getParameterName());
        });

        try {
            for (ParameterNameValue pnv: this.unresolvedParameterNames) {
                Integer parameterNameId = getParameterNameIdFromDatabase(pnv.getParameterName());
                if (parameterNameId == null) {
                    parameterNameId = createParameterName(pnv.getParameterName());
                }
                pnv.setParameterNameId(parameterNameId);
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("error resolving parameter names", x);
        }
    }

    private Integer getParameterNameIdFromDatabase(String parameterName) throws SQLException {
        String SQL = "SELECT parameter_name_id FROM parameter_names WHERE parameter_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, parameterName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        // no entry in parameter_names
        return null;
    }

    /**
     * Create the parameter name using the stored procedure which handles any concurrency
     * issue we may have
     * @param parameterName
     * @return
     */
    private Integer createParameterName(String parameterName) throws SQLException {
        final String CALL = "{CALL " + schemaName + ".add_parameter_name(?, ?)}";
        Integer parameterNameId;
        try (CallableStatement stmt = connection.prepareCall(CALL)) {
            stmt.setString(1, parameterName);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            parameterNameId = stmt.getInt(2);
        }

        return parameterNameId;
    }

    @Override
    protected void resetBatch() {
        // Called when a transaction has been rolled back because of a deadlock
        // or other retryable error and we want to try and process the batch again
        batchProcessor.reset();
    }

    /**
     * Build the check ready query
     * @param messagesByResourceType
     * @return
     */
    private String buildCheckReadyQuery(Map<String,List<RemoteIndexMessage>> messagesByResourceType) {
        // The trouble here is that we'll end up with a unique query for every single
        // batch of messages we process (which the database then need to parse etc).
        // This may introduce scaling issues, in which case we should consider 
        // individual queries for each resource type using bind variables, perhaps
        // going so far as using multiple statements with a power-of-2 number of bind
        // variables. But JDBC doesn't support batching of select statements, so
        // the alternative there would be to insert-as-select into a global temp table
        // and then simply select from that. Fairly straightforward, but a lot more
        // work so only worth doing if we identify contention here.

        StringBuilder select = new StringBuilder();
        // SELECT lr.logical_resource_id, lr.resource_type, lr.logical_id, xlr.version_id, lr.last_updated, lr.parameter_hash
        //   FROM logical_resources AS lr,
        //        patient_logical_resources AS xlr
        //  WHERE lr.logical_resource_id = xlr.logical_resource_id
        //    AND xlr.logical_resource_id IN (1,2,3,4)
        //  UNION ALL
        // SELECT lr.logical_resource_id, lr.resource_type, lr.logical_id, xlr.version_id, lr.last_updated, lr.parameter_hash
        //   FROM logical_resources AS lr,
        //        observation_logical_resources AS xlr
        //  WHERE lr.logical_resource_id = xlr.logical_resource_id
        //    AND xlr.logical_resource_id IN (5,6,7)
        boolean first = true;
        for (Map.Entry<String, List<RemoteIndexMessage>> entry: messagesByResourceType.entrySet()) {
            final String resourceType = entry.getKey();
            final List<RemoteIndexMessage> messages = entry.getValue();
            final String inlist = messages.stream().map(m -> Long.toString(m.getData().getLogicalResourceId())).collect(Collectors.joining(","));
            if (first) {
                first = false;
            } else {
                select.append(" UNION ALL ");
            }
            select.append(" SELECT lr.logical_resource_id, '" + resourceType + "' AS resource_type, lr.logical_id, xlr.version_id, lr.last_updated, lr.parameter_hash ");
            select.append("   FROM logical_resources AS lr, ");
            select.append(resourceType).append("_logical_resources AS xlr ");
            select.append("  WHERE lr.logical_resource_id = xlr.logical_resource_id ");
            select.append("    AND xlr.logical_resource_id IN (").append(inlist).append(")");
        }
        
        return select.toString();
    }

    @Override
    protected void checkReady(List<RemoteIndexMessage> messages, List<RemoteIndexMessage> okToProcess, List<RemoteIndexMessage> notReady) throws FHIRPersistenceException {
        // Get a list of all the resources for which we can see the current logical resource data.
        // If the resource doesn't yet exist or its version meta doesn't the message
        // then we add to the notReady list. If the resource version meta already
        // exceeds the message, then we'll skip processing altogether because it
        // means that there should be another message in the queue with more
        // up-to-date parameters
        Map<Long,RemoteIndexMessage> messageMap = new HashMap<>();
        Map<String,List<RemoteIndexMessage>> messagesByResourceType = new HashMap<>();
        for (RemoteIndexMessage msg: messages) {
            Long logicalResourceId = msg.getData().getLogicalResourceId();
            messageMap.put(logicalResourceId, msg);

            // split out the messages per resource type because we need to read from xx_logical_resources
            List<RemoteIndexMessage> values = messagesByResourceType.computeIfAbsent(msg.getData().getResourceType(), k -> new ArrayList<>());
            values.add(msg);
        }

        Set<Long> found = new HashSet<>();
        final String checkReadyQuery = buildCheckReadyQuery(messagesByResourceType);
        logger.fine(() -> "check ready query: " + checkReadyQuery);
        try (PreparedStatement ps = connection.prepareStatement(checkReadyQuery)) {
            ResultSet rs = ps.executeQuery();
            // wrap the ResultSet in a reader for easier consumption
            ResultSetReader rsReader = new ResultSetReader(rs);
            while (rsReader.next()) {
                LogicalResourceValue lrv = LogicalResourceValue.builder()
                        .withLogicalResourceId(rsReader.getLong())
                        .withResourceType(rsReader.getString())
                        .withLogicalId(rsReader.getString())
                        .withVersionId(rsReader.getInt())
                        .withLastUpdated(rsReader.getTimestamp())
                        .withParameterHash(rsReader.getString())
                        .build();
                RemoteIndexMessage m = messageMap.get(lrv.getLogicalResourceId());
                if (m == null) {
                    throw new IllegalStateException("query returned a logical resource which we didn't request");
                }

                // Check the values from the database to see if they match
                // the information in the message.
                if (m.getData().getVersionId() == lrv.getVersionId()) {
                    // only process this message if the parameter hash and lastUpdated
                    // times match - which is a good check that we're storing parameters
                    // from the correct transaction. If these don't match, we can simply
                    // say we found the data but don't need to process the message.
                    final Instant dbLastUpdated = lrv.getLastUpdated().toInstant();
                    final Instant msgLastUpdated = m.getData().getLastUpdated();
                    if (lrv.getParameterHash().equals(m.getData().getParameterHash()) 
                            && dbLastUpdated.equals(msgLastUpdated)) {
                        okToProcess.add(m);
                    } else {
                        logger.warning("Parameter message must match both parameter_hash and last_updated. Must be from an uncommitted transaction so ignoring: " + m.toString());
                    }
                    found.add(lrv.getLogicalResourceId()); // won't be marked as missing
                } else if (m.getData().getVersionId() > lrv.getVersionId()) {
                    // we can skip processing this record because the database has already
                    // been updated with a newer version. Identify the record as having been
                    // found so we don't keep waiting for it
                    found.add(lrv.getLogicalResourceId());
                }
                // if the version in the database is prior to version in the message we
                // received it means that the server transaction hasn't been committed...
                // so we have to wait just as though it were missing altogether
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "prepare failed: " + checkReadyQuery, x);
            throw new FHIRPersistenceException("prepare query failed");
        }

        if (found.size() < messages.size()) {
            // identify the missing records and add to the notReady list
            for (RemoteIndexMessage m: messages) {
                if (!found.contains(m.getData().getLogicalResourceId())) {
                    notReady.add(m);
                }
            }
        }
    }
    
    
    
    
    /**
     * Make sure we have values for all the logical_resource_ident values
     * we have collected in the current batch. Need to make sure these are
     * added in order to minimize deadlocks. Note that because we may create
     * new logical_resource_ident records, we could be blocked by the main
     * add_any_resource procedure run within the server CREATE/UPDATE
     * transaction.
     * @throws FHIRPersistenceException
     */
    private void resolveLogicalResourceIdents() throws FHIRPersistenceException {
        // identify which values aren't yet in the database
        List<LogicalResourceIdentValue> missing = fetchLogicalResourceIdentIds(unresolvedLogicalResourceIdents);

        if (!missing.isEmpty()) {
            addMissingLogicalResourceIdents(missing);
        }

        // All the previously missing values should now be in the database. We need to fetch them again,
        // possibly having to use multiple queries
        List<LogicalResourceIdentValue> bad = fetchLogicalResourceIdentIds(missing);

        if (!bad.isEmpty()) {
            // shouldn't happen, but let's protected against it anyway
            throw new FHIRPersistenceException("Failed to create all logical_resource_ident values");
        }
    }

    /**
     * Build and prepare a statement to fetch the code_system_id and code_system_name
     * from the code_systems table for all the given (unresolved) code system values
     * @param values
     * @return
     * @throws SQLException
     */
    private PreparedStatement buildLogicalResourceIdentSelectStatement(List<LogicalResourceIdentValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT rt.resource_type, lri.logical_id, lri.logical_resource_id ");
        query.append("  FROM logical_resource_ident AS lri ");
        query.append("  JOIN (VALUES ");
        for (int i=0; i<values.size(); i++) {
            if (i > 0) {
                query.append(",");
            }
            query.append("(?,?)");
        }
        query.append(") AS v(resource_type_id, logical_id) ");
        query.append("    ON (lri.resource_type_id = v.resource_type_id AND lri.logical_id = v.logical_id)");
        query.append("  JOIN resource_types AS rt ON (rt.resource_type_id = v.resource_type_id)"); // convenient to get the resource type name here
        PreparedStatement ps = connection.prepareStatement(query.toString());
        // bind the parameter values
        int param = 1;
        for (LogicalResourceIdentValue val: values) {
            ps.setInt(param++, val.getResourceTypeId());
            ps.setString(param++, val.getLogicalId());
        }
        logger.fine(() -> "logicalResourceIdents: " + query.toString());
        return ps;
    }

    /**
     * These logical_resource_ident values weren't found in the database, so we need to try and add them.
     * We have to deal with concurrency here - there's a chance another thread could also
     * be trying to add them. To avoid deadlocks, it's important to do any inserts in a
     * consistent order. At the end, we should be able to read back values for each entry
     * @param missing
     */
    protected void addMissingLogicalResourceIdents(List<LogicalResourceIdentValue> missing) throws FHIRPersistenceException {
        // Sort the values first to help avoid deadlocks
        Collections.sort(missing, (a,b) -> {
            return a.compareTo(b);
        });

        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO logical_resource_ident (resource_type_id, logical_id, logical_resource_id) VALUES (?,?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ON CONFLICT DO NOTHING");

        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            int count = 0;
            for (LogicalResourceIdentValue value: missing) {
                if (value.getResourceTypeId() == null) {
                    logger.severe("bad value: " + value);
                }
                ps.setInt(1, value.getResourceTypeId());
                ps.setString(2, value.getLogicalId());
                ps.addBatch();
                if (++count == this.maxLogicalResourcesPerStatement) {
                    // not too many statements in a single batch
                    ps.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "logical_resource_ident insert failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("logical_resource_ident insert failed");
        }
    }

    private List<LogicalResourceIdentValue> fetchLogicalResourceIdentIds(List<LogicalResourceIdentValue> unresolved) throws FHIRPersistenceException {
        // track which values aren't yet in the database
        List<LogicalResourceIdentValue> missing = new ArrayList<>();

        int offset = 0;
        while (offset < unresolved.size()) {
            int remaining = unresolved.size() - offset;
            int subSize = Math.min(remaining, this.maxCodeSystemsPerStatement);
            List<LogicalResourceIdentValue> sub = unresolved.subList(offset, offset+subSize); // remember toIndex is exclusive
            offset += subSize; // set up for the next iteration
            try (PreparedStatement ps = buildLogicalResourceIdentSelectStatement(sub)) {
                ResultSet rs = ps.executeQuery();
                // We can't rely on the order of result rows matching the order of the in-list,
                // so we have to go back to our map to look up each LogicalResourceIdentValue
                int resultCount = 0;
                while (rs.next()) {
                    resultCount++;
                    LogicalResourceIdentKey key = new LogicalResourceIdentKey(rs.getString(1), rs.getString(2));
                    LogicalResourceIdentValue csv = this.logicalResourceIdentMap.get(key);
                    if (csv != null) {
                        csv.setLogicalResourceId(rs.getLong(3));
                    } else {
                        // can't really happen, but be defensive
                        throw new FHIRPersistenceException("logical resource ident query returned an unexpected value");
                    }
                }

                // Most of the time we'll get everything, so we can bypass the check for
                // missing values
                if (resultCount == 0) {
                    // 100% miss
                    missing.addAll(sub);
                } else if (resultCount < subSize) {
                    // need to scan the sub list and see which values we don't yet have ids for
                    for (LogicalResourceIdentValue csv: sub) {
                        if (csv.getLogicalResourceId() == null) {
                            missing.add(csv);
                        }
                    }
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "logical resource ident fetch failed", x);
                throw new FHIRPersistenceException("logical resource ident fetch failed");
            }
        }

        // Return the list of CodeSystemValues which don't yet have a database entry
        return missing;
    }
    
    
}