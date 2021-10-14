/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.INameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterNameDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Postgres-specific extension of the {@link ResourceReferenceDAO} to work around
 * some SQL syntax and Postgres concurrency issues
 */
public class PostgresResourceReferenceDAO extends ResourceReferenceDAO {
    private static final Logger logger = Logger.getLogger(PostgresResourceReferenceDAO.class.getName());

    /**
     * Public constructor
     * @param t
     * @param c
     * @param schemaName
     * @param cache
     */
    public PostgresResourceReferenceDAO(IDatabaseTranslator t, Connection c, String schemaName, ICommonTokenValuesCache cache, INameIdCache<Integer> parameterNameCache) {
        super(t, c, schemaName, cache, parameterNameCache);
    }

    @Override
    public void doCodeSystemsUpsert(String paramList, Collection<String> sortedSystemNames) {
        // query is a negative outer join so we only pick the rows where
        // the row "s" from the actual table doesn't exist. Note the order by,
        // which is crucial to avoid deadlocks (even though adding code-systems
        // isn't that common).
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO code_systems (code_system_id, code_system_name) ");
        insert.append("     SELECT ").append(nextVal).append(", v.name ");
        insert.append("       FROM ");
        insert.append("    (VALUES ").append(paramList).append(" ) AS v(name) ");
        insert.append(" ON CONFLICT DO NOTHING ");

        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        try (PreparedStatement ps = getConnection().prepareStatement(insert.toString())) {
            // bind all the code_system_name values as parameters
            int a = 1;
            for (String name: sortedSystemNames) {
                ps.setString(a++, name);
            }

            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    public void doCanonicalValuesUpsert(String paramList, Collection<String> sortedURLS) {
        // Because of how PostgreSQL MVCC implementation, the insert from negative outer
        // join pattern doesn't work...you still hit conflicts. The PostgreSQL pattern
        // for upsert is ON CONFLICT DO NOTHING, which is what we use here:
        final String nextVal = getTranslator().nextValue(getSchemaName(), "fhir_ref_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_canonical_values (canonical_id, url) ");
        insert.append("     SELECT ").append(nextVal).append(", v.name ");
        insert.append("       FROM ");
        insert.append("    (VALUES ").append(paramList).append(" ) AS v(name) ");
        insert.append(" ON CONFLICT DO NOTHING ");

        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        try (PreparedStatement ps = getConnection().prepareStatement(insert.toString())) {
            // bind all the code_system_name values as parameters
            int a = 1;
            for (String name: sortedURLS) {
                ps.setString(a++, name);
            }

            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }
    }

    @Override
    protected void doCommonTokenValuesUpsert(String paramList, Collection<CommonTokenValue> sortedTokenValues) {
        // It would appear that Postgres MVCC doesn't properly handle the upsert pattern
        // based on not exists or a negative outer join (see the base class implementation
        // of this method for an example). It exposes a race condition, resulting in a
        // unique key duplicate value error. So instead, we have to use the Postgres custom
        // syntax to tell it to ignore any conflicts.
        StringBuilder insert = new StringBuilder();
        insert.append(" INSERT INTO common_token_values (token_value, code_system_id) ");
        insert.append("      SELECT v.token_value, v.code_system_id ");
        insert.append("        FROM (VALUES ").append(paramList).append(" ) AS v(token_value, code_system_id) ");
        insert.append("    ORDER BY v.code_system_id, v.token_value "); // minimize probability of deadlock
        insert.append(" ON CONFLICT DO NOTHING ");

        // Note, we use PreparedStatement here on purpose. Partly because it's
        // secure coding best practice, but also because many resources will have the
        // same number of parameters, and hopefully we'll therefore share a small subset
        // of statements for better performance. Although once the cache warms up, this
        // shouldn't be called at all.
        try (PreparedStatement ps = getConnection().prepareStatement(insert.toString())) {
            // bind all the name values as parameters
            int a = 1;
            for (CommonTokenValue tv: sortedTokenValues) {
                ps.setString(a++, tv.getTokenValue());
                ps.setInt(a++, tv.getCodeSystemId());
            }

            ps.executeUpdate();
        } catch (SQLException x) {
            logger.log(Level.SEVERE, insert.toString(), x);
            throw getTranslator().translate(x);
        }
    }
    
    @Override
    protected int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
        final ParameterNameDAO pnd = new PostgresParameterNamesDAO(getConnection(), getSchemaName());
        return pnd.readOrAddParameterNameId(parameterName);
    }
}