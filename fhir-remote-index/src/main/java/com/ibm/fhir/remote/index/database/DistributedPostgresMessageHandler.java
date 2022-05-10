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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.SearchParameterValue;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.remote.index.api.BatchParameterValue;
import com.ibm.fhir.remote.index.api.IdentityCache;
import com.ibm.fhir.remote.index.batch.BatchDateParameter;
import com.ibm.fhir.remote.index.batch.BatchLocationParameter;
import com.ibm.fhir.remote.index.batch.BatchNumberParameter;
import com.ibm.fhir.remote.index.batch.BatchQuantityParameter;
import com.ibm.fhir.remote.index.batch.BatchStringParameter;
import com.ibm.fhir.remote.index.batch.BatchTokenParameter;

/**
 * Loads search parameter values into the target FHIR schema on
 * a PostgreSQL database.
 */
public class DistributedPostgresMessageHandler extends BaseMessageHandler {
    private static final Logger logger = Logger.getLogger(DistributedPostgresMessageHandler.class.getName());

    // the connection to use for the inserts
    private final Connection connection;

    // We're a PostgreSQL DAO, so we now which translator to use
    private final IDatabaseTranslator translator = new PostgresTranslator();

    // The FHIR data schema
    private final String schemaName;

    // the cache we use for various lookups
    private final IdentityCache identityCache;

    // All parameter names we've seen (cleared if there's a rollback)
    private final Map<String,ParameterNameValue> parameterNameMap = new HashMap<>();

    // A map of code system name to the value holding its codeSystemId from the database
    private final Map<String, CodeSystemValue> codeSystemValueMap = new HashMap<>();

    // A map to support lookup of CommonTokenValue records by key
    private final Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap = new HashMap<>();

    // All parameter names in the current transaction for which we don't yet know the parameter_name_id
    private final List<ParameterNameValue> unresolvedParameterNames = new ArrayList<>();

    // A list of all the CodeSystemValues for which we don't yet know the code_system_id
    private final List<CodeSystemValue> unresolvedSystemValues = new ArrayList<>();

    // A list of all the CommonTokenValues for which we don't yet know the common_token_value_id
    private final List<CommonTokenValue> unresolvedTokenValues = new ArrayList<>();
    
    // The processed values we've collected
    private final List<BatchParameterValue> batchedParameterValues = new ArrayList<>();

    // The processor used to process the batched parameter values after all the reference values are created
    private final JDBCBatchParameterProcessor batchProcessor;

    private final int maxCodeSystemsPerStatement = 512;
    private final int maxCommonTokenValuesPerStatement = 256;
    private boolean rollbackOnly;

    /**
     * Public constructor
     * 
     * @param connection
     */
    public DistributedPostgresMessageHandler(Connection connection, String schemaName, IdentityCache cache) {
        this.connection = connection;
        this.schemaName = schemaName;
        this.identityCache = cache;
        this.batchProcessor = new JDBCBatchParameterProcessor(connection);
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
        try {
            if (!this.rollbackOnly) {
                logger.fine("Committing transaction");
                connection.commit();
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
                    // with a stack trace here because it's just more noise for the log file.
                    logger.severe("Rollback failed; reason=[" + x.getMessage() + "]");
                }
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("commit failed", x);
        } finally {
            unresolvedParameterNames.clear();
            unresolvedSystemValues.clear();
            unresolvedTokenValues.clear();
        }
    }

    /**
     * After the transaction has been committed, we can publish certain values to the
     * shared identity caches
     */
    public void publishCachedValues() {
        // all the unresolvedParameterNames should be resolved at this point
        for (ParameterNameValue pnv: this.unresolvedParameterNames) {
            identityCache.addParameterName(pnv.getParameterName(), pnv.getParameterNameId());
        }
        this.unresolvedParameterNames.clear();
    }

    @Override
    protected void pushBatch() throws FHIRPersistenceException {
        // Push any data we've accumulated so far. This may occur
        // if we cross a volume threshold, and will always occur as
        // the last step before the current transaction is committed,
        // Process the token values so that we can establish
        // any entries we need for common_token_values
        resolveParameterNames();
        resolveCodeSystems();
        resolveCommonTokenValues();

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
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, StringParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchStringParameter(resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, LocationParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchLocationParameter(resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, TokenParameter p) throws FHIRPersistenceException {
        short shardKey = batchProcessor.encodeShardKey(resourceType, logicalId);
        CommonTokenValue ctv = lookupCommonTokenValue(shardKey, p.getValueSystem(), p.getValueCode());
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchTokenParameter(resourceType, logicalId, logicalResourceId, parameterNameValue, p, ctv));
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        CodeSystemValue csv = lookupCodeSystemValue(p.getValueSystem());
        this.batchedParameterValues.add(new BatchQuantityParameter(resourceType, logicalId, logicalResourceId, parameterNameValue, p, csv));
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, NumberParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchNumberParameter(resourceType, logicalId, logicalResourceId, parameterNameValue, p));
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, DateParameter p) throws FHIRPersistenceException {
        ParameterNameValue parameterNameValue = getParameterNameId(p);
        this.batchedParameterValues.add(new BatchDateParameter(resourceType, logicalId, logicalResourceId, parameterNameValue, p));
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
    private CommonTokenValue lookupCommonTokenValue(short shardKey, String codeSystem, String tokenValue) {
        CommonTokenValueKey key = new CommonTokenValueKey(shardKey, codeSystem, tokenValue);
        CommonTokenValue result = this.commonTokenValueMap.get(key);
        if (result == null) {
            CodeSystemValue csv = lookupCodeSystemValue(codeSystem);
            result = new CommonTokenValue(shardKey, csv, tokenValue);
            this.commonTokenValueMap.put(key, result);

            // Take this opportunity to see if we have a cached value for this common token value
            Long commonTokenValueId = identityCache.getCommonTokenValueId(shardKey, codeSystem, tokenValue);
            if (commonTokenValueId != null) {
                result.setCommonTokenValueId(commonTokenValueId);
            } else {
                this.unresolvedTokenValues.add(result);
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
     * Build and prepare a statement to fetch the code_system_id and code_system value
     * from the code_systems table for all the given (unresolved) code system values
     * @param values
     * @return
     * @throws SQLException
     */
    private PreparedStatement buildCodeSystemSelectStatement(List<CodeSystemValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT code_system_id, code_system FROM code_systems WHERE code_system IN (");
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
    private void addMissingCodeSystems(List<CodeSystemValue> missing) throws FHIRPersistenceException {
        List<String> values = missing.stream().map(csv -> csv.getCodeSystem()).collect(Collectors.toList());
        // Sort the code system values first to help avoid deadlocks
        Collections.sort(values); // natural ordering for String is fine here

        final String nextVal = translator.nextValue(schemaName, "fhir_ref_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO code_systems (code_system_id, code_system) VALUES (");
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
     * @return SELECT shard_key, code_system, token_value, common_token_value_id
     * @throws SQLException
     */
    private PreparedStatement buildCommonTokenValueSelectStatement(List<CommonTokenValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        // need the code_system name - so we join back to the code_systems table as well
        query.append("SELECT c.shard_key, cs.code_system, c.token_value, c.common_token_value_id ");
        query.append("  FROM common_token_values c");
        query.append("  JOIN code_systems cs ON (cs.code_system_id = c.code_system_id)");
        query.append("  JOIN VALUES (");
        
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
        query.append(" ON c.code_system_id = v.code_system_id AND c.token_value = v.token_value");

        // Create the prepared statement and bind the values
        PreparedStatement ps = connection.prepareStatement(query.toString());
        // bind the parameter values
        int param = 1;
        for (CommonTokenValue ctv: values) {
            ps.setString(param++, ctv.getTokenValue());
        }
        return ps;
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
            try (PreparedStatement ps = buildCommonTokenValueSelectStatement(sub)) {
                ResultSet rs = ps.executeQuery();
                // We can't rely on the order of result rows matching the order of the in-list,
                // so we have to go back to our map to look up each CodeSystemValue
                int resultCount = 0;
                while (rs.next()) {
                    resultCount++;
                    CommonTokenValueKey key = new CommonTokenValueKey(rs.getShort(1), rs.getString(2), rs.getString(3));
                    CommonTokenValue ctv = this.commonTokenValueMap.get(key);
                    if (ctv != null) {
                        ctv.setCommonTokenValueId(rs.getLong(4));
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
                logger.log(Level.SEVERE, "common token values fetch failed", x);
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
    private void addMissingCommonTokenValues(List<CommonTokenValue> missing) throws FHIRPersistenceException {

        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_token_values (shard_key, code_system_id, token_value, common_token_value_id) VALUES (");
        insert.append("?,?,?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ON CONFLICT DO NOTHING");

        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            int count = 0;
            for (CommonTokenValue ctv: missing) {
                ps.setShort(1, ctv.getShardKey());
                ps.setInt(2, ctv.getCodeSystemValue().getCodeSystemId());
                ps.setString(3, ctv.getTokenValue());
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

}