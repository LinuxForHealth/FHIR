/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.common.PreparedStatementHelper;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.params.api.IParameterIdentityCache;
import com.ibm.fhir.persistence.params.model.CommonTokenValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValueKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentKey;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentValue;

/**
 * PostgreSQL variant of the remote index message handler
 */
public class PlainPostgresParamValueProcessor extends PlainParamValueProcessor {
    private static final Logger logger = Logger.getLogger(PlainPostgresParamValueProcessor.class.getName());

    /**
     * Public constructor
     * 
     * @param connection
     * @param schemaName
     * @param cache
     */
    public PlainPostgresParamValueProcessor(Connection connection, String schemaName, IParameterIdentityCache cache) {
        super(new PostgresTranslator(), connection, schemaName, cache);
    }

    @Override
    protected String onConflict() {
        return "ON CONFLICT DO NOTHING";
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

            // For PostgreSQL, we can use the following pattern to reduce the number of round-trips we need
            // to make with the database because it's possible to combine ON CONFLICT DO NOTHING with RETURNING.
            // When there aren't any conflicts, the insert returns every id, so we insert and fetch the ids
            // in a single round-trip
            // INSERT INTO logical_resource_ident
            //      VALUES (?, ?),
            //             (?, ?),
            //             (?, ?)
            // ON CONFLICT DO NOTHING
            //   RETURNING logical_resource_id, resource_type_id, logical_id;
            unresolvedLogicalResourceIdents = addMissingLogicalResourceIdents(missing, logicalResourceIdentMap);

            final int unresolvedSize = unresolvedLogicalResourceIdents.size();
            if (unresolvedSize > 0) {
                // fetch any remaining values which weren't returned by the RETURNING clause because they
                // encountered a conflict (created in another transaction)
                logger.fine(() -> "resolveLogicalResourceIdents: fetch ids for LogicalResourceIdent records skipped due to conflict; count = " + unresolvedSize);
                List<LogicalResourceIdentValue> bad = fetchLogicalResourceIdentIds(missing, logicalResourceIdentMap);
                
                if (!bad.isEmpty()) {
                    // shouldn't happen, but let's protected against it anyway
                    throw new FHIRPersistenceException("Failed to create all logical_resource_ident values");
                }
            }
        }

        logger.fine("resolveLogicalResourceIdents: all resolved");
    }

    /**
     * PostgreSQL-specific approach to insert new values into logical_resource_ident
     * @param missing
     * @param logicalResourceIdentMap
     * @return logical resources which still need to be fetched
     * @throws FHIRPersistenceException
     */
    private List<LogicalResourceIdentValue> addMissingLogicalResourceIdents(List<LogicalResourceIdentValue> missing, 
            Map<LogicalResourceIdentKey, LogicalResourceIdentValue> logicalResourceIdentMap) throws FHIRPersistenceException {

        // The list of logical resources we didn't get ids for due to conflict
        List<LogicalResourceIdentValue> result = new ArrayList<>();

        // There's an upper bound on how many values we can add to a single statement, so we
        // may need to break the work up into smaller chunks
        int offset = 0;
        while (offset < missing.size()) {
            int remaining = missing.size() - offset;
            int subSize = Math.min(remaining, this.maxLogicalResourcesPerStatement);
            List<LogicalResourceIdentValue> sub = missing.subList(offset, offset+subSize); // remember toIndex is exclusive
            offset += subSize; // set up for the next iteration

            // common_token_value_id assigned as generated identity column
            final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
            StringBuilder insert = new StringBuilder();
            insert.append(" INSERT INTO logical_resource_ident(resource_type_id, logical_id, logical_resource_id) ");
            insert.append("      VALUES ");
            for (int i=0; i<sub.size(); i++) {
                if (i > 0) {
                    insert.append(", ");
                }
                insert.append("(?,?,");
                insert.append(nextVal); // next sequence value for common_token_value_id
                insert.append(")");
            }
            insert.append(" ON CONFLICT DO NOTHING ");
            insert.append("   RETURNING logical_resource_id, resource_type_id, logical_id");

            try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
                PreparedStatementHelper psh = new PreparedStatementHelper(ps);

                // track the logical resources we added then found
                Set<LogicalResourceIdentKey> unresolved = new HashSet<>();
                
                // a map of all the resourceTypes we've encountered so we can find them by id
                Map<Integer, String> resourceTypeMap = new HashMap<>();

                for (LogicalResourceIdentValue lr: sub) {
                    // add all the keys to a set, then later we remove everything returned
                    // so remaining in the set will be those records where we hit a conflict
                    LogicalResourceIdentKey key = new LogicalResourceIdentKey(lr.getResourceType(), lr.getLogicalId());
                    unresolved.add(key);
    
                    // keep track of code system names because we need them later
                    resourceTypeMap.put(lr.getResourceTypeId(), lr.getResourceType());
                    psh.setInt(lr.getResourceTypeId());
                    psh.setString(lr.getLogicalId());
                }
                ps.execute();

                // Now process the RETURNING ResultSet to assign the logicalResourceId value for everything we
                // actually inserted
                ResultSet returned = ps.getResultSet();
                while (returned.next()) {
                    final long logicalResourceId = returned.getLong(1);
                    final String resourceType = resourceTypeMap.get(returned.getInt(2));
                    final String logicalId = returned.getString(3);
                    LogicalResourceIdentKey key = new LogicalResourceIdentKey(resourceType, logicalId);
                    logicalResourceIdentMap.get(key).setLogicalResourceId(logicalResourceId);
                    unresolved.remove(key);
                }

                // Now unresolved should contain only common token value keys which weren't returned due to a conflict
                // (they were created in a concurrent transaction). This list will be resolved in another fetch
                for (LogicalResourceIdentKey key: unresolved) {
                    result.add(logicalResourceIdentMap.get(key));
                }

            } catch (SQLException x) {
                logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
                throw new FHIRPersistenceException("failed inserting new logical resource ident records");
            }
        }

        if (result.size() > 0 && logger.isLoggable(Level.FINE)) {
            logger.fine("logical_resource_ident conflict count: " + result.size());
        }
        return result;
    }

    @Override
    public void resolveCommonTokenValues(List<CommonTokenValue> unresolvedTokenValues, Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap) throws FHIRPersistenceException {
        Collections.sort(unresolvedTokenValues); // to avoid potential deadlocks
        List<CommonTokenValue> missing = fetchCommonTokenValueIds(unresolvedTokenValues, commonTokenValueMap);

        if (!missing.isEmpty()) {
            // must sort again because we can't rely on the order of missing
            Collections.sort(missing);

            // For PostgreSQL, we can use the following pattern to reduce the number of round-trips we need
            // to make with the database because it's possible to combine ON CONFLICT DO NOTHING with RETURNING.
            // When there aren't any conflicts, the insert returns every id, so we insert and fetch the ids
            // in a single round-trip
            // INSERT INTO common_token_values
            //      VALUES (?, ?),
            //             (?, ?),
            //             (?, ?)
            // ON CONFLICT DO NOTHING
            //   RETURNING common_token_value_id, code_system_id, token_value;
            unresolvedTokenValues = addMissingCommonTokenValues(missing, commonTokenValueMap);

            // All the previously missing values should now be in the database. But if there were conflicts,
            // some of the values have been created in other transactions, so we need to fetch those values
            // now.
            if (unresolvedTokenValues.size() > 0) {
                List<CommonTokenValue> bad = fetchCommonTokenValueIds(unresolvedTokenValues, commonTokenValueMap);
                
                if (!bad.isEmpty()) {
                    // shouldn't happen, but let's protected against it anyway
                    throw new FHIRPersistenceException("Failed to create all common token values");
                }
            }
        }
    }

    /**
     * PostgreSQL-specific approach to insert new values into common_token_values
     * @param missing
     * @param commonTokenValueMap
     * @return unresolved ids which now need to be fetched
     * @throws FHIRPersistenceException
     */
    protected List<CommonTokenValue> addMissingCommonTokenValues(List<CommonTokenValue> missing, Map<CommonTokenValueKey, CommonTokenValue> commonTokenValueMap) throws FHIRPersistenceException {
        // result filled with any values we don't get a RETURNING value for
        Set<CommonTokenValueKey> unresolved = new HashSet<>();


        // There's an upper bound on how many values we can add to a single statement, so we
        // may need to break the work up into smaller chunks
        int offset = 0;
        while (offset < missing.size()) {
            int remaining = missing.size() - offset;
            int subSize = Math.min(remaining, this.maxCommonTokenValuesPerStatement);
            List<CommonTokenValue> sub = missing.subList(offset, offset+subSize); // remember toIndex is exclusive
            offset += subSize; // set up for the next iteration

            // common_token_value_id assigned as generated identity column
            StringBuilder insert = new StringBuilder();
            insert.append(" INSERT INTO common_token_values (code_system_id, token_value) ");
            insert.append("      VALUES ");
            for (int i=0; i<sub.size(); i++) {
                if (i > 0) {
                    insert.append(", ");
                }
                insert.append("(?,?)");
            }
            insert.append(" ON CONFLICT DO NOTHING ");
            insert.append("   RETURNING common_token_value_id, code_system_id, token_value");
    
            // a map of all the code-systems we've encountered so we can find them by id
            Map<Integer, String> codeSystemMap = new HashMap<>();

            try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
                PreparedStatementHelper psh = new PreparedStatementHelper(ps);
                for (CommonTokenValue ctv: sub) {
                    // add all the keys to a set, then later we remove everything returned
                    // so remaining in the set will be those records where we hit a conflict
                    CommonTokenValueKey key = new CommonTokenValueKey(FIXED_SHARD, ctv.getCodeSystemValue().getCodeSystem(), ctv.getTokenValue());
                    unresolved.add(key);
    
                    // keep track of code system names because we need them later
                    codeSystemMap.put(ctv.getCodeSystemValue().getCodeSystemId(), ctv.getCodeSystemValue().getCodeSystem());
                    psh.setInt(ctv.getCodeSystemValue().getCodeSystemId());
                    psh.setString(ctv.getTokenValue());
                }
                ps.execute();

                // Now process the RETURNING ResultSet to assign the commonTokenValueId value for everything we
                // actually inserted
                ResultSet returned = ps.getResultSet();
                while (returned.next()) {
                    final long commonTokenValueId = returned.getLong(1);
                    final String codeSystem = codeSystemMap.get(returned.getInt(2));
                    final String tokenValue = returned.getString(3);
                    CommonTokenValueKey key = new CommonTokenValueKey(FIXED_SHARD, codeSystem, tokenValue);
                    commonTokenValueMap.get(key).setCommonTokenValueId(commonTokenValueId);
                    unresolved.remove(key);
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
                throw new FHIRPersistenceException("failed inserting new common token values");
            }
        }

        // Now unresolved should contain only common token value keys which weren't returned due to a conflict
        // (they were created in a concurrent transaction). This list will be resolved in another fetch
        List<CommonTokenValue> result = new ArrayList<>(unresolved.size());
        for (CommonTokenValueKey key: unresolved) {
            result.add(commonTokenValueMap.get(key));
        }
        return result;
    }
}