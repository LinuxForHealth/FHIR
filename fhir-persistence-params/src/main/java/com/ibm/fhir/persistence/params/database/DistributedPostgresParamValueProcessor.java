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

import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.params.api.IParameterIdentityCache;
import com.ibm.fhir.persistence.params.api.ParamSchemaConstants;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValueKey;

/**
 * For the DISTRIBUTED schema variant used on databases such as Citus, we
 * can't use IDENTITY columns. Instead we have to use values generated
 * by a sequence, which means a slightly different INSERT statement
 * in certain cases
 */
public class DistributedPostgresParamValueProcessor extends PlainParamValueProcessor {
    private static final Logger logger = Logger.getLogger(DistributedPostgresParamValueProcessor.class.getName());

    /**
     * Public constructor
     * 
     * @param connection
     * @param schemaName
     * @param cache
     */
    public DistributedPostgresParamValueProcessor(Connection connection, String schemaName, IParameterIdentityCache cache) {
        super(new PostgresTranslator(), connection, schemaName, cache);
    }

    @Override
    protected String onConflict() {
        return "ON CONFLICT DO NOTHING";
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
     * PostgreSQL-specific approach to insert new values into 
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

            // For citus, we populate common_token_value_id using fhir_sequence
            final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
            StringBuilder insert = new StringBuilder();
            insert.append("INSERT INTO common_token_values (code_system_id, token_value, common_token_value_id) ");
            insert.append("     VALUES ");
            for (int i=0; i<sub.size(); i++) {
                if (i > 0) {
                    insert.append(", ");
                }
                insert.append("(?,?,");
                insert.append(nextVal); // next sequence value for common_token_value_id
                insert.append(")");
            }
            insert.append(" ON CONFLICT DO NOTHING ");
            insert.append("   RETURNING common_token_value_id, code_system_id, token_value");
    
            // a map of all the code-systems we've encountered so we can find them by id
            Map<Integer, String> codeSystemMap = new HashMap<>();

            try (PreparedStatement ps = connection.prepareStatement(insert.toString())) {
                for (CommonTokenValue ctv: sub) {
                    // add all the keys to a set, then later we remove everything returned
                    // so remaining in the set will be those records where we hit a conflict
                    CommonTokenValueKey key = new CommonTokenValueKey(FIXED_SHARD, ctv.getCodeSystemValue().getCodeSystem(), ctv.getTokenValue());
                    unresolved.add(key);
    
                    // keep track of code system names because we need them later
                    codeSystemMap.put(ctv.getCodeSystemValue().getCodeSystemId(), ctv.getCodeSystemValue().getCodeSystem());
                    ps.setInt(1, ctv.getCodeSystemValue().getCodeSystemId());
                    ps.setString(2, ctv.getTokenValue());
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

    @Override
    protected void addMissingCommonCanonicalValues(List<CommonCanonicalValue> missing) throws FHIRPersistenceException {

        // Need to use our own sequence number because distributed databases don't
        // like generated identity columns
        final String nextVal = translator.nextValue(schemaName, ParamSchemaConstants.CANONICAL_ID_SEQ);
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_canonical_values (url, canonical_id) ");
        insert.append(" OVERRIDING SYSTEM VALUE "); // we want to use our sequence number
        insert.append("     VALUES (?,");
        insert.append(nextVal); // next sequence value
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
}