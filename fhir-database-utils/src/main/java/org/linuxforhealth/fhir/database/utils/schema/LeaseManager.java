/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.DataAccessException;
import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.ILeaseManager;
import org.linuxforhealth.fhir.database.utils.api.ILeaseManagerConfig;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.thread.ThreadHandler;

/**
 * Manages acquisition and maintenance of the lease we need before we
 * are allowed to perform any schema changes
 */
public class LeaseManager implements ILeaseManager {
    private static final Logger logger = Logger.getLogger(LeaseManager.class.getName());

    // Unique to this instance. Not static to make testing easier
    private final String leaseId = UUID.randomUUID().toString();

    // How many nanos per second
    private static final long NANOS = 1000000000;

    // The translator for the type of database we are working with
    private final IDatabaseTranslator translator;

    // The connection pool
    private final IConnectionProvider connectionPool;

    // The (simple) transaction provider
    private final ITransactionProvider transactionProvider;

    // flag used to terminate running of the lease maintenance thread
    private volatile boolean running = true;

    // The thread used to maintain our lease
    private Thread leaseMaintenanceThr;

    // Flag used to determine current liveness
    private volatile boolean heartbeat = false;

    // The FHIR admin schema
    private final String adminSchema;

    // The FHIR data schema
    private final String schemaName;

    // The UTC instant describing our desired lease expiration time
    private volatile Instant leaseUntil;

    // Do we currently hold the lease?
    private volatile boolean gotLease;

    // The object containing our configuration details
    private final ILeaseManagerConfig config;

    /**
     * Public constructor
     * @param translator
     * @param connectionPool
     * @param transactionProvider
     * @param adminSchema
     * @param schemaName
     * @param config
     */
    public LeaseManager(IDatabaseTranslator translator, IConnectionProvider connectionPool, ITransactionProvider transactionProvider, String adminSchema, String schemaName, ILeaseManagerConfig config) {
        this.translator = translator;
        this.connectionPool = connectionPool;
        this.transactionProvider = transactionProvider;
        this.adminSchema = adminSchema;
        this.schemaName = schemaName;
        this.config = config;
    }

    @Override
    public void signalHeartbeat() {
        if (this.gotLease) {
            this.heartbeat = true;
        } else {
            throw new IllegalStateException("Lease expired");
        }
    }

    @Override
    public boolean hasLease() {
        return this.gotLease;
    }

    /**
     * Shut down the maintenance thread and wait for it so that we know it's
     * not running before we cancel the release
     */
    private void stopMaintenanceThreadAndWait() {
        this.running = false;
        this.gotLease = false;

        if (leaseMaintenanceThr != null) {
            leaseMaintenanceThr.interrupt();

            try {
                // No timeout here because we need to know the thread has terminated
                // before executing the cancel lease statement (to avoid a nasty race
                // condition).
                leaseMaintenanceThr.join();
                logger.fine("Lease maintenance thread terminated");
            } catch (InterruptedException x) {
                final String msg = "Interrupted waiting for least maintenance thread to terminate";
                logger.severe(msg);
                throw new DataAccessException(msg, x);
            }
        }
    }

    /**
     * Initial call to try to obtain the lease within the given number of seconds
     * If the lease is obtained, we set up a lease maintenance thread to refresh
     * the lease as long as heartbeat has been set to true since it last ran
     * @param seconds
     * @return
     */
    public boolean waitForLease(int seconds) {
        if (this.leaseMaintenanceThr != null) {
            throw new IllegalStateException("Lease already acquired once for this LeaseManager");
        }

        boolean timedOut = false;
        long endTime = System.nanoTime() + seconds * NANOS;
        int attempt = 1;
        while (!gotLease && !timedOut) {
            logger.info("Requesting update lease for schema '" + this.schemaName + "' [attempt " + attempt++ + "]");
            try (ITransaction tx = transactionProvider.getTransaction()) {
                // Establish our first lease expiry time
                this.leaseUntil = Instant.now().plusSeconds(config.getLeaseTimeSeconds());
                try (Connection c = connectionPool.getConnection()) {
                    GetLease cmd = getLeaseDAO();
                    gotLease = cmd.run(translator, c);
                } catch (SQLException x) {
                    throw translator.translate(x);
                }
            }

            if (gotLease) {
                // we were able to establish a lease for this instance, so kick off
                // the lease maintenance thread to make sure we hold onto it while
                // we're alive
                this.leaseMaintenanceThr = new Thread(() -> leaseMaintenanceLoop());
                this.leaseMaintenanceThr.start();
            } else {
                if (System.nanoTime() <= endTime) {
                    // Getting a lease is fairly lightweight compared to the rest of the schema update
                    // work, so trying every 1 second is reasonable
                    ThreadHandler.safeSleep(ThreadHandler.SECOND);
                } else {
                    timedOut = true;
                }
            }
        }

        return gotLease;
    }

    /**
     * The loop used to periodically refresh our lease while we're alive
     */
    private void leaseMaintenanceLoop() {
        Instant wakeupTime = Instant.now().plusSeconds(config.getLeaseTimeSeconds() / 2);
        while (this.running && this.gotLease) {
            ThreadHandler.sleepUntil(wakeupTime);

            if (this.heartbeat) {
                // reset the liveness heartbeat.
                this.heartbeat = false;
            } else if (!config.stayAlive()) {
                logger.warning("Property stayAlive not set and no liveness heartbeat; terminating");
                this.running = false;
                this.gotLease = false;
            }

            if (running) {
                // We're still active, so make sure we hold onto the lease
                logger.info("Refreshing lease for schema '" + schemaName + "'");
                refreshLease();

                // Compute the next time we want to wake up and refresh our lease
                // Make this relative to the previous wake-up-time so we keep a
                // regular schedule
                wakeupTime = wakeupTime.plusSeconds(config.getLeaseTimeSeconds() / 2);
            }
        }

        logger.fine("Lease maintenance loop terminated");
    }

    /**
     * Refresh the lease
     */
    private void refreshLease() {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            this.leaseUntil = Instant.now().plusSeconds(config.getLeaseTimeSeconds());
            try (Connection c = connectionPool.getConnection()) {
                GetLease cmd = getLeaseDAO();
                gotLease = cmd.run(translator, c);

                if (!gotLease) {
                    // this means we've lost the lease. This instance should start to terminate
                    logger.warning("Lease lost by this instance for schema '" + schemaName + "'");
                }
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        } finally {
            // If we lost our lease, then terminate the maintenance thread loop
            if (running && !gotLease) {
                running = false;
            }
        }
    }

    /**
     * Get the GetLease DAO appropriate for the current database
     * @return the GetLease DAO
     */
    private GetLease getLeaseDAO() {
        final GetLease result;
        switch (this.translator.getType()) {
        case POSTGRESQL:
        case CITUS:
            result = new GetLeasePostgresql(adminSchema, schemaName, config.getHost(), leaseId, leaseUntil);
            break;
        default:
            result = new GetLease(adminSchema, schemaName, config.getHost(), leaseId, leaseUntil);
        }
        return result;
    }

    /**
     * Cancel the lease if this instance is the current owner
     */
    public boolean cancelLease() {
        boolean result = false;
        logger.info("Canceling lease for schema '" + schemaName + "'");
        this.gotLease = false;
        // We must make sure we stop the lease maintenance thread before running
        // the CancelLease statement, otherwise we run into a race condition that
        // could renew the lease right after we canceled it
        try {
            stopMaintenanceThreadAndWait();
        } finally {
            try (ITransaction tx = transactionProvider.getTransaction()) {
                try (Connection c = connectionPool.getConnection()) {
                    CancelLease cmd = new CancelLease(adminSchema, schemaName, leaseId);
                    boolean canceled = cmd.run(translator, c);
                    if (canceled) {
                        logger.fine("Lease canceled for schema '" + schemaName + "'");
                        result = true;
                    } else {
                        logger.warning("Cancel lease ignored: Lease for schema '" + schemaName + "' is not owned by this instance");
                    }
                } catch (SQLException x) {
                    throw translator.translate(x);
                }
            }
        }

        return result;
    }
}