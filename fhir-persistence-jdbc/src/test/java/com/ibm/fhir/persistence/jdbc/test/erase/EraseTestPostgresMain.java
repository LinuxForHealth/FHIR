/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.erase;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresPropertyAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavorImpl;
import com.ibm.fhir.persistence.jdbc.dao.EraseResourceDAO;

/**
 * EraseTestMain is a test driver for the EraseResourceDAO
 * so it can be debugged during development.
 */
public class EraseTestPostgresMain implements InitialContextFactory {
    private static final String CLASSNAME = EraseTestPostgresMain.class.getSimpleName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private IDatabaseTranslator translator = new PostgresTranslator();
    private DbType dbType = DbType.POSTGRESQL;
    private Properties properties = null;

    private static ExecutorService svc = Executors.newFixedThreadPool(5);

    public EraseTestPostgresMain(Properties properties) {
        this.properties = properties;
    }

    protected void erase() throws Exception {
        try {
            try (Connection c = createConnection()) {
                System.out.println("Got a Connection");
                try {
                    FHIRDbFlavor flavor = new FHIRDbFlavorImpl(DbType.POSTGRESQL, true);
                    EraseResourceDAO dao = new EraseResourceDAO(c, translator, "fhirdata", flavor, null, null);

                    ResourceEraseRecord record = new ResourceEraseRecord(true);
                    EraseDTO eraseBean = new EraseDTO();
                    eraseBean.setResourceType("Patient");
                    eraseBean.setTimeout(30);

                    //      350001 178f70bd344-f0a13e93-88b9-4964-86ce-6376759bab55

                    eraseBean.setLogicalId("1791f19a126-28b0239c-f1a1-4d15-979f-920af8d12446");
                    System.out.println("Able to Erase...");
                    dao.erase(record, eraseBean);
                    System.out.println(record);
                } catch (Exception x) {
                    System.out.println("Error Condition Encountered.");
                    c.rollback();
                    throw x;
                }
                c.commit();
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        svc.shutdownNow();
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, CLS_FACTORY);
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/test.erase.postgres.properties"));
        EraseTestPostgresMain main = new EraseTestPostgresMain(properties);
        main.erase();
    }

    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = getPropertyAdapter(dbType, properties);
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(properties);
        logger.info("Opening connection to: " + url);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return connection;
    }


    /**
     * Load the driver class
     */
    public static void loadDriver(IDatabaseTranslator translator) {
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }


    public static JdbcPropertyAdapter getPropertyAdapter(DbType dbType, Properties props) {
        switch (dbType) {
        case DB2:
            return new Db2PropertyAdapter(props);
        case DERBY:
            return new DerbyPropertyAdapter(props);
        case POSTGRESQL:
            return new PostgresPropertyAdapter(props);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    public static IDatabaseAdapter getDbAdapter(DbType dbType, JdbcTarget target) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(target);
        case DERBY:
            return new DerbyAdapter(target);
        case POSTGRESQL:
            return new PostgresAdapter(target);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    public static IDatabaseAdapter getDbAdapter(DbType dbType, IConnectionProvider connectionProvider) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(connectionProvider);
        case DERBY:
            return new DerbyAdapter(connectionProvider);
        case POSTGRESQL:
            return new PostgresAdapter(connectionProvider);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }


    /*
     * The following code facilitates the erase operation testing.
     */
    private static final String CLS_FACTORY = "com.ibm.fhir.persistence.jdbc.test.JDBCEraseTest";

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, CLS_FACTORY);

        Context ctx = new Context() {

            @Override
            public Object lookup(Name name) throws NamingException {

                return null;
            }

            @Override
            public Object lookup(String name) throws NamingException {
                return facade;
            }

            @Override
            public void bind(Name name, Object obj) throws NamingException {


            }

            @Override
            public void bind(String name, Object obj) throws NamingException {


            }

            @Override
            public void rebind(Name name, Object obj) throws NamingException {


            }

            @Override
            public void rebind(String name, Object obj) throws NamingException {


            }

            @Override
            public void unbind(Name name) throws NamingException {


            }

            @Override
            public void unbind(String name) throws NamingException {


            }

            @Override
            public void rename(Name oldName, Name newName) throws NamingException {


            }

            @Override
            public void rename(String oldName, String newName) throws NamingException {


            }

            @Override
            public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {

                return null;
            }

            @Override
            public NamingEnumeration<NameClassPair> list(String name) throws NamingException {

                return null;
            }

            @Override
            public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {

                return null;
            }

            @Override
            public NamingEnumeration<Binding> listBindings(String name) throws NamingException {

                return null;
            }

            @Override
            public void destroySubcontext(Name name) throws NamingException {


            }

            @Override
            public void destroySubcontext(String name) throws NamingException {


            }

            @Override
            public Context createSubcontext(Name name) throws NamingException {

                return null;
            }

            @Override
            public Context createSubcontext(String name) throws NamingException {

                return null;
            }

            @Override
            public Object lookupLink(Name name) throws NamingException {

                return null;
            }

            @Override
            public Object lookupLink(String name) throws NamingException {

                return null;
            }

            @Override
            public NameParser getNameParser(Name name) throws NamingException {

                return null;
            }

            @Override
            public NameParser getNameParser(String name) throws NamingException {

                return null;
            }

            @Override
            public Name composeName(Name name, Name prefix) throws NamingException {

                return null;
            }

            @Override
            public String composeName(String name, String prefix) throws NamingException {

                return null;
            }

            @Override
            public Object addToEnvironment(String propName, Object propVal) throws NamingException {

                return null;
            }

            @Override
            public Object removeFromEnvironment(String propName) throws NamingException {

                return null;
            }

            @Override
            public Hashtable<?, ?> getEnvironment() throws NamingException {

                return null;
            }

            @Override
            public void close() throws NamingException {


            }

            @Override
            public String getNameInNamespace() throws NamingException {

                return null;
            }

        };
        ctx.bind("concurrent/fhir-erase", facade);
        return ctx;
    }


    private static ManagedExecutorService facade = new ManagedExecutorService() {

        @Override
        public void shutdown() {
        }

        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return svc.submit(task);
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return null;
        }

        @Override
        public Future<?> submit(Runnable task) {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        @Override
        public void execute(Runnable command) {

        }

    };
}
