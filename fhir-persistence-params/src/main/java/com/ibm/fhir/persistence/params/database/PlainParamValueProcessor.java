/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.params.api.BatchParameterValue;
import com.ibm.fhir.persistence.params.api.IParamValueProcessor;
import com.ibm.fhir.persistence.params.api.IParameterIdentityCache;
import com.ibm.fhir.persistence.params.api.ParamSchemaConstants;
import com.ibm.fhir.persistence.params.model.CodeSystemValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValueKey;
import com.ibm.fhir.persistence.params.model.CommonTokenValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValueKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * Loads search parameter values into the target PostgreSQL database using
 * the plain (non-sharded) schema variant.
 */
public abstract class PlainParamValueProcessor implements IParamValueProcessor {
    private static final String CLASSNAME = PlainParamValueProcessor.class.getName();
    private static final Logger logger = Logger.getLogger(PlainParamValueProcessor.class.getName());
    private static final short FIXED_SHARD = 0;

    // the connection to use for the inserts
    protected final Connection connection;

    // The translator to handle variations in SQL syntax
    protected final IDatabaseTranslator translator;

    // The FHIR data schema
    protected final String schemaName;

    // the cache we use for various lookups
    protected final IParameterIdentityCache identityCache;

    // The processor used to process the batched parameter values after all the reference values are created
    private final PlainBatchParameterProcessor batchProcessor;

    protected final int maxLogicalResourcesPerStatement = 256;
    protected final int maxCodeSystemsPerStatement = 512;
    protected final int maxCommonTokenValuesPerStatement = 256;
    protected final int maxCommonCanonicalValuesPerStatement = 256;

    /**
     * Public constructor
     * 
     * @param translator
     * @param connection
     * @param schemaName
     * @param cache
     */
    public PlainParamValueProcessor(IDatabaseTranslator translator, Connection connection, String schemaName, IParameterIdentityCache cache) {
        this.translator = translator;
        this.connection = connection;
        this.schemaName = schemaName;
        this.identityCache = cache;
        this.batchProcessor = new PlainBatchParameterProcessor(connection);
    }

    @Override
    public void startBatch() {
        // always start with a clean slate
        batchProcessor.startBatch();
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
    public void publish(BatchParameterValue bpv) throws FHIRPersistenceException {
        bpv.apply(batchProcessor);
    }
    
    @Override
    public void pushBatch() throws FHIRPersistenceException {
        // Push any data we've accumulated so far. This may occur
        // if we cross a volume threshold, and will always occur as
        // the last step before the current transaction is committed,
        // Process the token values so that we can establish
        // any entries we need for common_token_values
        logger.fine("pushBatch: executing final batch statements");
        batchProcessor.pushBatch();
        logger.fine("pushBatch completed");
    }

    /**
     * Make sure we have values for all the code_systems we have collected
     * in the current
     * batch
     * @throws FHIRPersistenceException
     */
    public void resolveSystemValues(List<CodeSystemValue> unresolvedSystemValues, Map<String, CodeSystemValue> codeSystemValueMap) throws FHIRPersistenceException {

        // Sort the list first to avoid any deadlocks
        Collections.sort(unresolvedSystemValues);
        
        // identify which values aren't yet in the database
        List<CodeSystemValue> missing = fetchCodeSystemIds(unresolvedSystemValues, codeSystemValueMap);

        if (!missing.isEmpty()) {
            // need to sort the missing list because we can't guarantee its order matches unresolvedSystemValues
            Collections.sort(missing);
            addMissingCodeSystems(missing);

            // All the previously missing values should now be in the database. We need to fetch them again,
            // possibly having to use multiple queries
            List<CodeSystemValue> bad = fetchCodeSystemIds(missing, codeSystemValueMap);
            
            if (!bad.isEmpty()) {
                // shouldn't happend, but let's protected against it anyway
                throw new FHIRPersistenceException("Failed to create all code system values");
            }
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

    protected abstract String onConflict();

    /**
     * These code systems weren't found in the database, so we need to try and add them.
     * We have to deal with concurrency here - there's a chance another thread could also
     * be trying to add them. To avoid deadlocks, it's important to do any inserts in a
     * consistent order. At the end, we should be able to read back values for each entry
     * @param missing
     */
    protected void addMissingCodeSystems(List<CodeSystemValue> missing) throws FHIRPersistenceException {

        final String nextVal = translator.nextValue(schemaName, "fhir_ref_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO code_systems (code_system_id, code_system_name) VALUES (");
        insert.append(nextVal); // next sequence value
        insert.append(",?) ");
        insert.append(onConflict());

        try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
            int count = 0;
            for (CodeSystemValue csv: missing) {
                ps.setString(1, csv.getCodeSystem());
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

    /**
     * Fetch all the code_system_id values for the given list of CodeSystemValue objects.
     * @param unresolved
     * @return
     * @throws FHIRPersistenceException
     */
    protected List<CodeSystemValue> fetchCodeSystemIds(List<CodeSystemValue> unresolved, Map<String, CodeSystemValue> codeSystemValueMap) throws FHIRPersistenceException {
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
                    CodeSystemValue csv = codeSystemValueMap.get(rs.getString(2));
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
    public void resolveCommonTokenValues(List<CommonTokenValue> unresolvedTokenValues, Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap) throws FHIRPersistenceException {
        // identify which values aren't yet in the database
        Collections.sort(unresolvedTokenValues); // to avoid potential deadlocks
        List<CommonTokenValue> missing = fetchCommonTokenValueIds(unresolvedTokenValues, commonTokenValueMap);

        if (!missing.isEmpty()) {
            // must sort again because we can't rely on the order of missing
            Collections.sort(missing);

            addMissingCommonTokenValues(missing);
            // All the previously missing values should now be in the database. We need to fetch them again,
            // possibly having to use multiple queries
            List<CommonTokenValue> bad = fetchCommonTokenValueIds(missing, commonTokenValueMap);
            
            if (!bad.isEmpty()) {
                // shouldn't happend, but let's protected against it anyway
                throw new FHIRPersistenceException("Failed to create all common token values");
            }
        }
    }

    /**
     * Build and prepare a statement to fetch the common_token_value records
     * for all the given (unresolved) code system values
     * @param values
     * @return SELECT code_system, token_value, common_token_value_id
     * @throws SQLException
     */
    protected PreparedStatementWrapper buildCommonTokenValueSelectStatement(List<CommonTokenValue> values) throws SQLException {
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

    /**
     * Fetch the common_token_value_id values for the given list of CommonTokenValue objects.
     * @param unresolved
     * @return
     * @throws FHIRPersistenceException
     */
    protected List<CommonTokenValue> fetchCommonTokenValueIds(List<CommonTokenValue> unresolved, Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap) throws FHIRPersistenceException {
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
                    CommonTokenValue ctv = commonTokenValueMap.get(key);
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

        // common_token_value_id assigned as generated identity column
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_token_values (code_system_id, token_value) ");
        insert.append("     VALUES (?,?) ");
        insert.append(onConflict());

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

    @Override
    public void resolveCanonicalValues(List<CommonCanonicalValue> unresolvedCanonicalValues,
        Map<CommonCanonicalValueKey, CommonCanonicalValue> commonCanonicalValueMap) throws FHIRPersistenceException {
        // identify which values aren't yet in the database

        // Sort the unresolved list so we always process stuff in the same order
        Collections.sort(unresolvedCanonicalValues);
        
        List<CommonCanonicalValue> missing = fetchCanonicalIds(unresolvedCanonicalValues, commonCanonicalValueMap);

        if (!missing.isEmpty()) {
            // Sort on url to minimize deadlocks
            Collections.sort(missing);
            addMissingCommonCanonicalValues(missing);

            // All the previously missing values should now be in the database. We need to fetch them again,
            // possibly having to use multiple queries
            List<CommonCanonicalValue> bad = fetchCanonicalIds(missing, commonCanonicalValueMap);
            
            if (!bad.isEmpty()) {
                // shouldn't happen, but let's protected against it anyway
                throw new FHIRPersistenceException("Failed to create all canonical values");
            }
        }
    }

    /**
     * Fetch the common_canonical_id values for the given list of CommonCanonicalValue objects.
     * @param unresolved
     * @return
     * @throws FHIRPersistenceException
     */
    protected List<CommonCanonicalValue> fetchCanonicalIds(List<CommonCanonicalValue> unresolved, Map<CommonCanonicalValueKey, CommonCanonicalValue> commonCanonicalValueMap) throws FHIRPersistenceException {
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
                    CommonCanonicalValue ctv = commonCanonicalValueMap.get(key);
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

        // We have to use fhir_ref_sequence here because that is what is used elsewhere, even though as a bigint
        // we are meant to be using fhir_sequence. It's just a break in convention
        final String nextVal = translator.nextValue(schemaName, ParamSchemaConstants.CANONICAL_ID_SEQ);
        insert.append("INSERT INTO common_canonical_values (url, canonical_id) VALUES (?, ");
        insert.append(nextVal);
        insert.append(") ");
        insert.append(onConflict());

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

    @Override
    public void resolveParameterNames(List<ParameterNameValue> unresolvedParameterNames, Map<String, ParameterNameValue> parameterNameMap) throws FHIRPersistenceException {
        // We expect parameter names to have a very high cache hit rate and
        // so we simplify processing by simply iterating one-by-one for the
        // values we still need to resolve. The most important point here is
        // to do this in a sorted order to avoid deadlock issues because this
        // could be happening across multiple threads at the same time.
        logger.fine("resolveParameterNames: sorting unresolved names");
        Collections.sort(unresolvedParameterNames);

        try {
            for (ParameterNameValue pnv: unresolvedParameterNames) {
                logger.finer(() -> "fetching parameter_name_id for '" + pnv.getParameterName() + "'");
                Integer parameterNameId = getParameterNameIdFromDatabase(pnv.getParameterName());
                if (parameterNameId == null) {
                    parameterNameId = createParameterName(pnv.getParameterName());
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("assigned parameter_name_id '" + pnv.getParameterName() + "' = " + parameterNameId);
                    }

                    if (parameterNameId == null) {
                        // be defensive
                        throw new FHIRPersistenceException("parameter_name_id not assigned for '" + pnv.getParameterName());
                    }
                } else if (logger.isLoggable(Level.FINER)) {
                    logger.finer("read parameter_name_id '" + pnv.getParameterName() + "' = " + parameterNameId);
                }
                pnv.setParameterNameId(parameterNameId);
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("error resolving parameter names", x);
        } finally {
            logger.exiting(CLASSNAME, "resolveParameterNames");
        }
    }

    /**
     * Fetch the parameter_name_id for the given parameterName value
     * @param parameterName
     * @return
     * @throws SQLException
     */
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
    protected Integer createParameterName(String parameterName) throws FHIRPersistenceException {
        final String CALL = "{CALL " + schemaName + ".add_parameter_name(?, ?)}";
        Integer parameterNameId;
        try (CallableStatement stmt = connection.prepareCall(CALL)) {
            stmt.setString(1, parameterName);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            parameterNameId = stmt.getInt(2);
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "add parameter failed: " + CALL, x);
            throw new FHIRPersistenceException("add parameter failed for '" + parameterName + "'");
        }

        return parameterNameId;
    }

    @Override
    public void resetBatch() {
        // Called when a transaction has been rolled back because of a deadlock
        // or other retryable error and we want to try and process the batch again
        batchProcessor.reset();
    }

    @Override
    public void resolveLogicalResourceIdents(List<LogicalResourceIdentValue> unresolvedLogicalResourceIdents,
        Map<LogicalResourceIdentKey, LogicalResourceIdentValue> logicalResourceIdentMap) throws FHIRPersistenceException {
        logger.fine("resolveLogicalResourceIdents: fetching ids for unresolved LogicalResourceIdent records");

        // Sort the values first to help avoid deadlocks
        Collections.sort(unresolvedLogicalResourceIdents);

        // identify which values aren't yet in the database
        List<LogicalResourceIdentValue> missing = fetchLogicalResourceIdentIds(unresolvedLogicalResourceIdents, logicalResourceIdentMap);

        if (!missing.isEmpty()) {
            logger.fine("resolveLogicalResourceIdents: add missing LogicalResourceIdent records");
            Collections.sort(missing);
            addMissingLogicalResourceIdents(missing);

            // All the previously missing values should now be in the database. We need to fetch them again,
            // possibly having to use multiple queries
            logger.fine("resolveLogicalResourceIdents: fetch ids for missing LogicalResourceIdent records");
            List<LogicalResourceIdentValue> bad = fetchLogicalResourceIdentIds(missing, logicalResourceIdentMap);
            
            if (!bad.isEmpty()) {
                // shouldn't happen, but let's protected against it anyway
                throw new FHIRPersistenceException("Failed to create all logical_resource_ident values");
            }
        }

        logger.fine("resolveLogicalResourceIdents: all resolved");
    }

    /**
     * Build and prepare a statement to fetch the code_system_id and code_system_name
     * from the code_systems table for all the given (unresolved) code system values
     * @param values
     * @return
     * @throws SQLException
     */
    protected PreparedStatement buildLogicalResourceIdentSelectStatement(List<LogicalResourceIdentValue> values) throws SQLException {
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

        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO logical_resource_ident (resource_type_id, logical_id, logical_resource_id) VALUES (?,?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ");
        insert.append(onConflict());

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

    /**
     * Fetch logical_resource_id values for the given list of LogicalResourceIdent objects.
     * @param unresolved
     * @return
     * @throws FHIRPersistenceException
     */
    protected List<LogicalResourceIdentValue> fetchLogicalResourceIdentIds(List<LogicalResourceIdentValue> unresolved, Map<LogicalResourceIdentKey, LogicalResourceIdentValue> logicalResourceIdentMap) throws FHIRPersistenceException {
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
                    LogicalResourceIdentValue csv = logicalResourceIdentMap.get(key);
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