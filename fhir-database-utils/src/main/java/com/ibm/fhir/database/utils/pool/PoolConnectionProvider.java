/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Provides a simple connection pool with some thread-local behavior
 * so that requests for a connection within the same thread will
 * get the same connection back. Cooperates with the TransactionFactory
 * to provide a JEE-flavored experience, and makes it easy to write
 * code which will work in both J2SE and JEE environments.
 * 
 * Does not support distributed transactions.
 */
public class PoolConnectionProvider implements IConnectionProvider {
    private static final Logger logger = Logger.getLogger(PoolConnectionProvider.class.getName());
        
    // Concurrency control for the pool
    private final Lock lock = new ReentrantLock();
    private final Condition waitForConnectionCondition = lock.newCondition();
    
    // The number of connections allocated
    int allocated;
    
    // The list of free connections, available to be allocated
    private Queue<Connection> free = new LinkedList<>();
    
    // The connection provider we are decorating with pooling abilities
    private final IConnectionProvider connectionProvider;

    // Connection active on the current thread, if any
    private ThreadLocal<PooledConnection> activeConnection = new ThreadLocal<>();

    // The maximum number of connections allowed to be active
    private final int maxPoolSize;

    /**
     * Public constructor
     * @param cp
     * @param maxPoolSize
     */
    public PoolConnectionProvider(IConnectionProvider cp, int maxPoolSize) {
        this.connectionProvider = cp;
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // We use the same connection on a given thread each time it is requested
        PooledConnection result = activeConnection.get();
        if (result != null) {
            if (result.incOpenCount() > 1) {
                // likely a programming error such as not using try-with-resource
                logger.warning("Connection open count is > 1");
            }
            return result;
        }
        
        // No connection currently on this thread, so try to obtain the underlying
        // connection from the pool
        Connection c = null;
        lock.lock();
        try {
            boolean assigned = false;
            while (!assigned) {
                if (free.isEmpty()) {
                    if (this.allocated < this.maxPoolSize) {
                        this.allocated++;
                        assigned = true; // get connection outside of lock
                    }
                    else {
                        // block until a connection frees up
                        logger.info("Max connections allocated, waiting for connection to be freed");
                        this.waitForConnectionCondition.await();
                    }
                }
                else {
                    // simply take the next connection from the head of the list
                    logger.fine("Using db connection from pool");
                    c = free.poll();
                    logger.fine("Got db connection from pool: " + c.toString());
                    assigned = true;
                }
            }
        }
        catch (InterruptedException x) {
            throw new DataAccessException("Interrupted waiting for connection");
        }
        finally {
            lock.unlock();
        }
        
        // It might take a while to establish a new connection, so we do this after
        // releasing the above lock to try and maximize concurrency.
        if (c == null) {
            try {
                c = connectionProvider.getConnection();
            }
            catch (SQLException x) {
                // Failed to acquire a connection, so we need to relinquish
                // our allocation and give others a chance
                lock.lock();
                try {
                    this.allocated--;
                    this.waitForConnectionCondition.signal();
                }
                finally {
                    lock.unlock();
                }
                throw x;
            }
        }

        // Wrap the connection, and set it as active on this thread so we will always
        // use it until the current transaction is complete
        result = new PooledConnection(this, c);
        result.incOpenCount();
        activeConnection.set(result);
        
        return result;
    }
    
    /**
     * Called when the connection is closed, which in this case is pretty much a NOP,
     * because this connection will stay active on this thread until the transaction
     * commits
     * @param pc
     */
    protected void returnConnection(PooledConnection pc, boolean reuse) {
        PooledConnection active = activeConnection.get();
        
        // Just look out for programming errors
        if (active == null) {
            throw new IllegalStateException("No active connection");
        }
        else if (active != pc) {
            throw new IllegalStateException("Active connection mismatch");
        }
    }

    /**
     * Remove the active connection on this thread. Called when the transaction completes
     * @throws SQLException 
     */
    protected void clearActiveConnection() throws SQLException {
        PooledConnection pc = activeConnection.get();
        if (pc != null) {
            // If the open count of current connection is bigger than 0, then it means the connection is
            // not "closed" yet, then we need to close it to reduce the open count by 1 before the connection
            // is added back to the connection pool.
            // This could happen, e.g, in FHIRDbDAOImpl, the external connection is not closed after each 
            // operation, instead, the connection should be closed only after the whole transaction is committed 
            // or rolled back.
            if (pc.getOpenCount() > 0) {
                pc.close();
            }
            this.activeConnection.remove();
            if (pc.getOpenCount() != 0) {
                // Whoops. getConnection called again on the thread...possibly
                // indicates the prior connection wasn't closed
                logger.warning("PooledConnection open/close mismatch: " + pc.getOpenCount());
            }
            
            lock.lock();
            try {
                if (pc.isReusable()) {
                    // underlying connection should still be good, so add it back into the pool
                    logger.fine("Adding connection back to pool");
                    free.add(pc.getWrapped());
                }
                else {
                    // Connection appears to be broken, so just close it and walk away
                    logger.fine("Connection is broken, so closing it");
                    pc.forceClosed();
                    // We now have one less allocated connection, so need to reduce our
                    // count accordingly, which might unblock another thread waiting to
                    // create a new connection
                    this.allocated--;
                }
                
                // Wake up a thread waiting for an available connection
                this.waitForConnectionCondition.signal();
            }
            finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * Simple check to see if the exception is related to a connection error,
     * in which case the connection shouldn't be returned to the pool when closed
     * @param x
     * @return
     */
    protected boolean checkConnectionFailure(SQLException x) {
        return connectionProvider.getTranslator().isConnectionError(x);
    }

    @Override
    public IDatabaseTranslator getTranslator() {
        return this.connectionProvider.getTranslator();
    }

    @Override
    public void commitTransaction() throws SQLException {

        // Run commit on the connection associated with the current thread
        PooledConnection c = activeConnection.get();
        if (c != null) {
            try {
                logger.fine("Committing transaction");
                c.getWrapped().commit();
            }
            finally {
                clearActiveConnection();
            }
        }
        else {
            // NOP. This just means that no SQL statements were executed
            // and so there's nothing to do. Not a problem.
            logger.warning("No connection on this thread");
        }
        
    }

    @Override
    public void rollbackTransaction() throws SQLException {

        // Run commit on the connection associated with the current thread
        PooledConnection pc = activeConnection.get();
        if (pc != null) {
            try {
                logger.warning("Rolling back transaction");
                pc.getWrapped().rollback();
            }
            finally {
                // Throw out this connection completely, as we are concerned about
                // its state.
                pc.forceClosed();
                clearActiveConnection();
            }
        }
        else {
            // NOP. This just means that no SQL statements were executed
            // and so there's nothing to do. Not a problem.
            logger.warning("No connection on this thread");
        }
    }

    @Override
    public void describe(String prefix, StringBuilder cfg, String key) {
        // Not Implemented
    }

    /**
     * gets the pool size for the pooled connection
     */
    public int getPoolSize() {
        return this.maxPoolSize;
    }
}