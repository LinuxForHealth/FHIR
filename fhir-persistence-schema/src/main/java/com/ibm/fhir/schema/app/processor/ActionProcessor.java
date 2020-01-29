/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor;

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
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.dryrun.DryRunContainer;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.transaction.TransactionFactory;
import com.ibm.fhir.schema.app.processor.action.ISchemaAction;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

public class ActionProcessor {
    private static final Logger logger = Logger.getLogger(ActionProcessor.class.getName());

    private PoolConnectionProvider connectionPool;
    private ITransactionProvider transactionProvider;

    private IDatabaseTranslator translator = new Db2Translator();

    private ActionBean actionBean;

    public ActionProcessor(ActionBean actionBean) {
        this.actionBean = actionBean;
        if (actionBean.getDryRun()) {
            Db2Translator t = new Db2Translator();
            t.setDryRun(Boolean.TRUE);
            translator = t;
        }
    }

    /**
     * Create a simple connection pool associated with our data source so that we
     * can
     * perform the DDL deployment in parallel
     */
    protected void configureConnectionPool() {
        Db2PropertyAdapter adapter = new Db2PropertyAdapter(actionBean.getProperties());
        JdbcConnectionProvider cp = new JdbcConnectionProvider(this.translator, adapter);
        this.connectionPool      = new PoolConnectionProvider(cp, actionBean.getMaxConnectionPoolSize());
        this.transactionProvider = new SimpleTransactionProvider(this.connectionPool);
    }

    public void process(ISchemaAction action) throws Exception {
        try (Connection c = createConnection()) {
            try {
                JdbcTarget target = new JdbcTarget(c);
                Db2Adapter adapter = new Db2Adapter(target);
                if (actionBean.getDryRun()) {
                    DryRunContainer.getSingleInstance().setDryRun(actionBean.getDryRun());
                }
                action.run(actionBean, target, adapter, transactionProvider);
            } catch (Exception x) {
                c.rollback();
                throw x;
            }
            c.commit();
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    public void processTransaction(ISchemaAction action) throws SchemaActionException {
        if (actionBean.getDryRun()) {
            DryRunContainer.getSingleInstance().setDryRun(actionBean.getDryRun());
        }

        // Configure the connection pool
        configureConnectionPool();
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                Db2Adapter adapter = new Db2Adapter(connectionPool);
                action.run(actionBean, null, adapter, transactionProvider);
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
        Db2PropertyAdapter adapter = new Db2PropertyAdapter(actionBean.getProperties());
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(actionBean.getProperties());
        logger.info("Opening connection to DB2: " + url);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return connection;
    }
}