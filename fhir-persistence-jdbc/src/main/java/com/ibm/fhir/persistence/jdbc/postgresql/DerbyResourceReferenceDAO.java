/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;


/**
 * Postgres-specific extension of the {@link ResourceReferenceDAO} to work around
 * some SQL syntax and Postgres concurrency issues
 */
public class DerbyResourceReferenceDAO extends ResourceReferenceDAO {
    private static final Logger logger = Logger.getLogger(PostgresResourceReferenceDAO.class.getName());
    
    private static final int BATCH_SIZE = 100;
    
    /**
     * Public constructor
     * @param t
     * @param c
     * @param schemaName
     * @param cache
     */
    public DerbyResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache) {
        super(t, c, schemaName, cache);
    }
    
    @Override
    public void doCodeSystemsUpsert(String paramList, Collection<String> systemNames) {
        
        // We'll assume that the rows will be processed in the order they are
        // inserted, although there's not really a guarantee this is the case
        final List<String> sortedNames = new ArrayList<>(systemNames);
        sortedNames.sort((String left, String right) -> left.compareTo(right));

        // Derby doesn't like really huge VALUES lists, so we instead need
        // to go with a declared temporary table.
        final String insert = "INSERT INTO SESSION.code_systems_tmp (code_system_name) VALUES (?)";
        int batchCount = 0;
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
            for (String systemName: systemNames) {
                ps.setString(1, systemName);
                ps.addBatch();
                
                if (++batchCount == BATCH_SIZE) {
                    ps.executeBatch();
                    batchCount = 0;
                }
            }
            
            if (batchCount > 0) {
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }
        

        // Upsert values. Can't use an order by in this situation because
        // Derby doesn't like this when pulling values from the sequence,
        // which seems like a defect, because the values should only be
        // evaluated after the join and where clauses.
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        StringBuilder upsert = new StringBuilder();
        upsert.append("INSERT INTO code_systems (code_system_id, code_system_name) ");
        upsert.append("          SELECT ").append(nextVal).append(", src.code_system_name ");
        upsert.append("            FROM SESSION.code_systems_tmp src ");
        upsert.append(" LEFT OUTER JOIN code_systems cs ");
        upsert.append("              ON cs.code_system_name = src.code_system_name ");
        upsert.append("           WHERE cs.code_system_name IS NULL ");
        
        try (Statement s = getConnection().createStatement()) {
            s.executeUpdate(upsert.toString());
        } catch (SQLException x) {
            logger.log(Level.SEVERE, upsert.toString(), x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void doCommonTokenValuesUpsert(String paramList, Collection<CommonTokenValue> tokenValues) {
        
        final String insert = "INSERT INTO SESSION.common_token_values_tmp(token_value, code_system_id) VALUES (?, ?)";
        int batchCount = 0;
        try (PreparedStatement ps = getConnection().prepareStatement(insert)) {
            for (CommonTokenValue ctv: tokenValues) {
                ps.setString(1, ctv.getTokenValue());
                ps.setInt(2, ctv.getCodeSystemId());
                ps.addBatch();
                
                if (++batchCount == BATCH_SIZE) {
                    ps.executeBatch();
                    batchCount = 0;
                }
            }
            
            if (batchCount > 0) {
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }

        // Upsert the values from the declared global temp table into common_token_values
        // ORDER BY helps to minimize the chance of deadlocks
        StringBuilder upsert = new StringBuilder();
        upsert.append("INSERT INTO common_token_values (token_value, code_system_id) ");
        upsert.append("     SELECT src.token_value, src.code_system_id ");
        upsert.append("       FROM SESSION.common_token_values_tmp src ");
        upsert.append(" LEFT OUTER JOIN common_token_values ctv ");
        upsert.append("              ON ctv.token_value = src.token_value ");
        upsert.append("             AND ctv.code_system_id = src.code_system_id ");
        upsert.append("      WHERE ctv.token_value IS NULL ");
        upsert.append("   ORDER BY src.token_value, src.code_system_id");
        
        try (Statement s = getConnection().createStatement()) {
            s.executeUpdate(upsert.toString());
        } catch (SQLException x) {
            logger.log(Level.SEVERE, upsert.toString(), x);
            throw getTranslator().translate(x);
        }
    }
    
    @Override
    public void upsertCommonTokenValues(List<ResourceTokenValueRec> values) {
        // Special case for Derby so we don't try and create monster SQL statements
        // resulting in a stack overflow when Derby attempts to parse it.
        
        // Unique list so we don't try and create the same name more than once.
        // Ignore any null token-values, because we don't want to (can't) store
        // them in our common token values table.
        Set<CommonTokenValue> tokenValues = values.stream().filter(x -> x.getTokenValue() != null).map(xr -> new CommonTokenValue(xr.getCodeSystemValueId(), xr.getTokenValue())).collect(Collectors.toSet());
        
        if (tokenValues.isEmpty()) {
            // nothing to do
            return;
        }

        final String paramListStr = null;
        doCommonTokenValuesUpsert(paramListStr, tokenValues);

        // Fetch the ids for all the records we need.
        // Simple join against the tmp table populated by the previous call should do it
        StringBuilder select = new StringBuilder();
        select.append("     SELECT ctv.code_system_id, ctv.token_value, ctv.common_token_value_id ");
        select.append("       FROM common_token_values ctv, ");
        select.append("            SESSION.common_token_values_tmp tmp ");
        select.append("      WHERE ctv.token_value = tmp.token_value ");
        select.append("        AND ctv.code_system_id = tmp.code_system_id ");
        
        Map<CommonTokenValue, Long> idMap = new HashMap<>();
        try (PreparedStatement ps = getConnection().prepareStatement(select.toString())) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // code_system_id, token_value
                CommonTokenValue key = new CommonTokenValue(rs.getInt(1), rs.getString(2));
                idMap.put(key, rs.getLong(3));
            }
        } catch (SQLException x) {
            throw getTranslator().translate(x);
        }
        
        // Now update the ids for all the matching systems in our list
        for (ResourceTokenValueRec xr: values) {
            // ignore entries with null tokenValue elements - we don't store them in common_token_values
            if (xr.getTokenValue() != null) {
                CommonTokenValue key = new CommonTokenValue(xr.getCodeSystemValueId(), xr.getTokenValue());
                Long id = idMap.get(key);
                if (id != null) {
                    xr.setCommonTokenValueId(id);
    
                    // update the thread-local cache with this id. The values aren't committed to the shared cache
                    // until the transaction commits
                    getCache().addTokenValue(key, id);
                }
            }
        }
    }
}