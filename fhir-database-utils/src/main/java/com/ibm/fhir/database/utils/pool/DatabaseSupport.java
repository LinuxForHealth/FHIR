/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresPropertyAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;

/**
 * Support class for managing connections to a database for utility apps
 */
public class DatabaseSupport implements IConnectionProvider, ITransactionProvider {
    private static final int DEFAULT_CONNECTION_POOL_SIZE = 10;
    private final Properties dbProperties;
    private final DbType dbType;

    // The translator for the configured database type
    private IDatabaseTranslator translator;

    // The adapter configured for the type of database we're using
    private IDatabaseAdapter adapter;
    
    // Connection pool used to work alongside the transaction provider
    private PoolConnectionProvider connectionPool;

    // Simple transaction service for use outside of JEE
    private ITransactionProvider transactionProvider;
    
    private int connectionPoolSize = DEFAULT_CONNECTION_POOL_SIZE;

    /**
     * Public constructor
     * @param dbProperties
     * @param dbType
     */
    public DatabaseSupport(Properties dbProperties, DbType dbType) {
        this.dbProperties = dbProperties;
        this.dbType = dbType;
    }

    /**
     * Build the database configuration from the configured properties
     */
    public void init() {
        switch (this.dbType) {
        case DB2:
            configureForDb2();
            break;
        case DERBY:
            configureForDerby();
            break;
        case POSTGRESQL:
        case CITUS:
            configureForPostgresql();
            break;
        default:
            throw new IllegalStateException("Unsupported database type: " + this.dbType);
        }
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a Derby
     * database
     */
    private void configureForDerby() {
        DerbyPropertyAdapter propertyAdapter = new DerbyPropertyAdapter(dbProperties);

        this.translator = new DerbyTranslator();
        IConnectionProvider cp = new JdbcConnectionProvider(this.translator, propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, connectionPoolSize);
        this.connectionPool.setCloseOnAnyError();
        this.adapter = new DerbyAdapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a DB2
     * database
     */
    private void configureForDb2() {

        this.translator = new Db2Translator();
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        Db2PropertyAdapter propertyAdapter = new Db2PropertyAdapter(dbProperties);
        IConnectionProvider cp = new JdbcConnectionProvider(translator, propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, connectionPoolSize);
        this.adapter = new Db2Adapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }

    /**
     * Set up the connection pool and transaction provider for connecting to a DB2
     * database
     */
    private void configureForPostgresql() {
        this.translator = new PostgresTranslator();
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        PostgresPropertyAdapter propertyAdapter = new PostgresPropertyAdapter(dbProperties);
        IConnectionProvider cp = new JdbcConnectionProvider(translator, propertyAdapter);
        this.connectionPool = new PoolConnectionProvider(cp, connectionPoolSize);
        this.adapter = new PostgresAdapter(connectionPool);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);
    }
    
    /**
     * Get the configured database adapter
     * @return
     */
    public IDatabaseAdapter getDatabaseAdapter() {
        if (this.adapter == null) {
            throw new IllegalStateException("DatabaseSupport not initialized");
        }
        return this.adapter;
    }

    /**
     * Get the IDatabaseTranslator for the configured database type
     * @return
     */
    public IDatabaseTranslator getTranslator() {
        if (this.translator == null) {
            throw new IllegalStateException("DatabaseSupport not initialized");
        }
        return this.translator;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.connectionPool == null) {
            throw new IllegalStateException("DatabaseSupport not initialized");
        }
        
        return this.connectionPool.getConnection();
    }

    @Override
    public ITransaction getTransaction() {
        if (this.transactionProvider == null) {
            throw new IllegalStateException("DatabaseSupport not initialized");
        }
        return this.transactionProvider.getTransaction();
    }

    @Override
    public void commitTransaction() throws SQLException {
        connectionPool.commitTransaction();
    }

    @Override
    public void rollbackTransaction() throws SQLException {
        connectionPool.rollbackTransaction();
    }

    @Override
    public void describe(String prefix, StringBuilder cfg, String key) {
        connectionPool.describe(prefix, cfg, key);
    }
}
