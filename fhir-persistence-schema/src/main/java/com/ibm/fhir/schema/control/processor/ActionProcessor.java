/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control.processor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.transaction.TransactionFactory;
import com.ibm.fhir.schema.app.dryrun.DryRunConnection;
import com.ibm.fhir.schema.app.dryrun.DryRunJdbcConnectionProvider;
import com.ibm.fhir.schema.control.processor.action.ISchemaAction;

public class ActionProcessor {
    private static final Logger logger = Logger.getLogger(ActionProcessor.class.getName());

    private PoolConnectionProvider connectionPool;
    private ITransactionProvider transactionProvider;
    private int maxConnectionPoolSize;

    private IDatabaseTranslator translator;
    private Boolean dryRun = Boolean.FALSE;
    private Properties properties;

    public ActionProcessor(Properties properties, IDatabaseTranslator translator, Boolean dryRun,
            int maxConnectionPoolSize) {
        this.properties            = properties;
        this.translator            = translator;
        this.dryRun                = dryRun;
        this.maxConnectionPoolSize = maxConnectionPoolSize;
    }

    /**
     * Create a simple connection pool associated with our data source so that we
     * can
     * perform the DDL deployment in parallel
     */
    protected void configureConnectionPool() {
        Db2PropertyAdapter adapter = new Db2PropertyAdapter(this.properties);

        // Selectively switch to the DryRun provider or the Connection.
        JdbcConnectionProvider cp = new JdbcConnectionProvider(this.translator, adapter);
        if (this.dryRun) {
            cp = new DryRunJdbcConnectionProvider(this.translator, adapter);
        }

        this.connectionPool      = new PoolConnectionProvider(cp, this.maxConnectionPoolSize);
        this.transactionProvider = new SimpleTransactionProvider(this.connectionPool);
    }

    public void process(ISchemaAction action) {
        try (Connection c = createConnection()) {
            try {
                JdbcTarget target = new JdbcTarget(c);
                Db2Adapter adapter = new Db2Adapter(target);
                if (dryRun) {
                    action.dryRun(target, adapter, transactionProvider);
                } else {
                    action.run(target, adapter, transactionProvider);
                }
            } catch (Exception x) {
                c.rollback();
                throw x;
            }
            c.commit();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    public void processTransaction(ISchemaAction action) {
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                if (dryRun) {
                    action.dryRun(null, adapter, transactionProvider);
                } else {
                    action.run(null, adapter, transactionProvider);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Connect to the target database
     * 
     * @return
     */
    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        Db2PropertyAdapter adapter = new Db2PropertyAdapter(this.properties);
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(properties);
        logger.info("Opening connection to DB2: " + url);
        Connection connection;
        try {
            // Dry Run
            if (dryRun) {
                Connection wrappedConnection = DriverManager.getConnection(url, connectionProperties);
                connection = new DryRunConnection(wrappedConnection);
            } else {
                connection = DriverManager.getConnection(url, connectionProperties);
            }
            connection.setAutoCommit(false);
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return connection;
    }
}