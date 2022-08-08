/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTarget;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * JdbcTarget for the database
 */
public class JdbcTarget implements IDatabaseTarget {
    private static final Logger logger = Logger.getLogger(JdbcTarget.class.getName());
    
    private final Connection connection;
    
    /**
     * Public constructor
     * @param c
     */
    public JdbcTarget(Connection c) {
        this.connection = c;
    }

    @Override
    public void runStatement(IDatabaseTranslator translator, String ddl) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(ddl);
        }
        
        // Execute the DDL (no parameters)
        try (Statement s = connection.createStatement()) {
            s.executeUpdate(ddl);
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    @Override
    public void runStatementWithInt(IDatabaseTranslator translator, String sql, int value) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest(sql + "; value = " + value);
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, value);
            ps.executeUpdate(sql);
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    @Override
    public void runStatement(IDatabaseTranslator translator, IDatabaseStatement statement) {
        statement.run(translator, connection);
    }

    @Override
    public <T> T runStatement(IDatabaseTranslator translator, IDatabaseSupplier<T> supplier) {
        // execute the statement using the given translator and the connection held by this
        return supplier.run(translator, connection);
    }

}
