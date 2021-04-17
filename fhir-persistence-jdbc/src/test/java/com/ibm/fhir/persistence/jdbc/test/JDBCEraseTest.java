/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test;

import java.sql.Connection;
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

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.cache.CommonTokenValuesCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.NameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.test.common.AbstractEraseTest;

/**
 * JDBC test implementation of the Eras function provided by the persistence layer
 */
public class JDBCEraseTest extends AbstractEraseTest implements InitialContextFactory {

    private Properties testProps;

    private PoolConnectionProvider connectionPool;

    private FHIRPersistenceJDBCCache cache;

    public JDBCEraseTest() throws Exception {
        this.testProps = TestUtil.readTestProperties("test.jdbc.properties");
    }

    @Override
    public void bootstrapDatabase() throws Exception {
        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            IConnectionProvider cp = derbyInit.getConnectionProvider(false);
            this.connectionPool = new PoolConnectionProvider(cp, 1);
            ICommonTokenValuesCache rrc = new CommonTokenValuesCacheImpl(100, 100);
            cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new NameIdCache<Integer>(), rrc);
        }
    }

    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        if (this.connectionPool == null) {
            throw new IllegalStateException("Database not bootstrapped");
        }
        return new FHIRPersistenceJDBCImpl(this.testProps, this.connectionPool, cache);
    }

    @Override
    protected void shutdownPools() throws Exception {
        // Mark the pool as no longer in use. This allows the pool to check for
        // lingering open connections/transactions.
        if (this.connectionPool != null) {
            this.connectionPool.close();
        }
    }

    @Override
    protected void debugLocks() {
        // Exception running a query. Let's dump the lock table
        try (Connection c = connectionPool.getConnection()) {
            DerbyMaster.dumpLockInfo(c);
        } catch (SQLException x) {
            // just log the error...things are already bad if this method has been called
            logger.severe("dumpLockInfo - connection failure: " + x.getMessage());
        }
    }

    /*
     * The following code facilitates the erase operation testing.
     */
    private static final String CLS_FACTORY = "com.ibm.fhir.persistence.jdbc.test.JDBCEraseTest";

    @BeforeClass
    public void startupSvc() throws Exception {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, CLS_FACTORY);// + "$TestFactory");
    }

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

    @AfterClass
    public void shutdownSvc() {
        svc.shutdownNow();
    }

    private static ExecutorService svc = Executors.newFixedThreadPool(5);
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