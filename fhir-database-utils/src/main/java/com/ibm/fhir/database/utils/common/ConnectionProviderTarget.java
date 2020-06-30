/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * An {@link IDatabaseTarget} which uses an {@link IConnectionProvider}
 * to obtain a connection which is then closed immediately for
 * each statement. Each run statement is committed before the
 * connection is closed. This target is not intended for use
 * with the ITransactionProvider/ITransaction implementation.
 */
public class ConnectionProviderTarget implements IDatabaseTarget {

    private final IConnectionProvider connectionProvider;
    
    /**
     * Public constructor
     * @param cp provides connections used for running statements
     */
    public ConnectionProviderTarget(IConnectionProvider cp) {
        this.connectionProvider = cp;
    }

    @Override
    public void runStatement(IDatabaseTranslator translator, String ddl) {
        // Execute the DDL (no parameters)
        try (Connection connection = connectionProvider.getConnection()) {
            try (Statement s = connection.createStatement()) {
                s.executeUpdate(ddl);
            } catch (SQLException x) {
                connection.rollback();
                throw x;
            }
            connection.commit();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    @Override
    public void runStatementWithInt(IDatabaseTranslator translator, String sql, int value) {
        // convenience for running a statement requiring a single int parameter
        try (Connection connection = connectionProvider.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, value);
                ps.executeUpdate(sql);
            } catch (SQLException x) {
                connection.rollback();
                throw x;
            }
            connection.commit();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    @Override
    public void runStatement(IDatabaseTranslator translator, IDatabaseStatement statement) {
        
        // run the statement on a fresh connection and commit right away
        try (Connection connection = connectionProvider.getConnection()) {
            try {
                statement.run(translator, connection);
            } catch (Throwable t) {
                connection.rollback();
                throw t;
            }
            connection.commit();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    @Override
    public <T> T runStatement(IDatabaseTranslator translator, IDatabaseSupplier<T> supplier) {
        // execute the statement using the given translator and a fresh connection
        // run the statement on a fresh connection and commit right away
        try (Connection connection = connectionProvider.getConnection()) {
            
            T result;
            try {
                result = supplier.run(translator, connection);
            } catch (Throwable t) {
                connection.rollback();
                throw t;
            }
            connection.commit();
            return result;
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}