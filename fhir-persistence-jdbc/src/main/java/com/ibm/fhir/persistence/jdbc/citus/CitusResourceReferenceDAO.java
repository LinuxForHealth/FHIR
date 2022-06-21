/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.citus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import com.ibm.fhir.persistence.jdbc.dao.api.INameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.postgres.PostgresResourceReferenceDAO;

/**
 * Citus-specific extension of the {@link ResourceReferenceDAO} to work around
 * some Citus distribution limitations
 */
public class CitusResourceReferenceDAO extends PostgresResourceReferenceDAO {
    private static final Logger logger = Logger.getLogger(CitusResourceReferenceDAO.class.getName());

    /**
     * Public constructor
     * 
     * @param t
     * @param c
     * @param schemaName
     * @param cache
     * @param parameterNameCache
     * @param logicalResourceIdentCache
     */
    public CitusResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache, INameIdCache<Integer> parameterNameCache,
            ILogicalResourceIdentCache logicalResourceIdentCache) {
        super(t, c, schemaName, cache, parameterNameCache, logicalResourceIdentCache);
    }

    @Override
    public void doCodeSystemsUpsert(String paramList, Collection<String> sortedSystemNames) {
        // If we try using the PostgreSQL insert-as-select variant, Citus
        // rejects the statement, so instead we simplify things by grabbing
        // the id values from the sequence first, then simply submit as a
        // batch.
        List<Integer> sequenceValues = new ArrayList<>(sortedSystemNames.size());
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        final String SELECT = ""
                + "SELECT " + nextVal
                + "  FROM generate_series(1, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT)) {
            ps.setInt(1, sortedSystemNames.size());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sequenceValues.add(rs.getInt(1));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SELECT, x);
            throw getTranslator().translate(x);
        }

        final String INSERT = ""
                + " INSERT INTO code_systems (code_system_id, code_system_name) "
                + "      VALUES (?, ?) "
                + " ON CONFLICT DO NOTHING ";

        try (PreparedStatement ps = getConnection().prepareStatement(INSERT)) {
            int index=0;
            for (String csn: sortedSystemNames) {
                ps.setInt(1, sequenceValues.get(index++));
                ps.setString(2, csn);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INSERT, x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    public void doCanonicalValuesUpsert(String paramList, Collection<String> sortedURLS) {
        // Because of how PostgreSQL MVCC implementation, the insert from negative outer
        // join pattern doesn't work...you still hit conflicts. The PostgreSQL pattern
        // for upsert is ON CONFLICT DO NOTHING, which is what we use here:
        List<Integer> sequenceValues = new ArrayList<>(sortedURLS.size());
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        final String SELECT = ""
                + "SELECT " + nextVal
                + "  FROM generate_series(1, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT)) {
            ps.setInt(1, sortedURLS.size());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sequenceValues.add(rs.getInt(1));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SELECT, x);
            throw getTranslator().translate(x);
        }

        final String INSERT = ""
                + " INSERT INTO common_canonical_values (canonical_id, url) "
                + "      VALUES (?, ?) "
                + " ON CONFLICT DO NOTHING ";

        try (PreparedStatement ps = getConnection().prepareStatement(INSERT)) {
            int index=0;
            for (String csn: sortedURLS) {
                ps.setInt(1, sequenceValues.get(index++));
                ps.setString(2, csn);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INSERT, x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void doCommonTokenValuesUpsert(String paramList, Collection<CommonTokenValue> sortedTokenValues) {
        // In Citus, we can no longer use a generated id column, so we have to use
        // values from the fhir-sequence and insert the values directly
        List<Integer> sequenceValues = new ArrayList<>(sortedTokenValues.size());
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        final String SELECT = ""
                + "SELECT " + nextVal
                + "  FROM generate_series(1, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT)) {
            ps.setInt(1, sortedTokenValues.size());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sequenceValues.add(rs.getInt(1));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SELECT, x);
            throw getTranslator().translate(x);
        }

        final String INSERT = ""
                + " INSERT INTO common_token_values (common_token_value_id, token_value, code_system_id) "
                + "      VALUES (?, ?, ?) "
                + " ON CONFLICT DO NOTHING ";

        try (PreparedStatement ps = getConnection().prepareStatement(INSERT)) {
            int index=0;
            for (CommonTokenValue ctv: sortedTokenValues) {
                ps.setInt(1, sequenceValues.get(index++));
                ps.setString(2, ctv.getTokenValue());
                ps.setInt(3, ctv.getCodeSystemId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, INSERT, x);
            throw getTranslator().translate(x);
        }
    }
}