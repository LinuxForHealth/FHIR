/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.client.ClientPropertyAdapter;
import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.flow.api.IFlowWriter;
import com.ibm.fhir.flow.impl.DownstreamFHIRWriter;
import com.ibm.fhir.flow.impl.DownstreamLogWriter;
import com.ibm.fhir.flow.impl.FlowPool;
import com.ibm.fhir.flow.impl.UpstreamFHIRHistoryReader;

/**
 * Demonstration of FHIR system synchronization. Upstream changes are tracked
 * using the whole-system history API. The stream of changes is fed to a thread
 * pool to fetch the actual resources in parallel (VREAD). The stream is also fed
 * to an output queue along with a future which is resolved when the pool reads
 * the resource. The output queue maintains the same order as the input.
 * Different output adapters can be used to feed the stream of changes into a
 * downstream system, keeping it in sync with the state of the upstream system.
 * 
 * This fhir-flow app holds state to represent where we currently sit in the
 * sequence of changes on the upstream server. This allows us to restart from
 * a known point in order to guarantee that we don't miss anything.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private final Properties upstreamProperties = new Properties();
    private final Properties downstreamProperties = new Properties();
    private int partitionCount = 16;
    private int partitionQueueSize = 128;
    private String upstreamTenant = "default";
    private String downstreamTenant = "default";

    // should the log writer log the JSON payload data
    private boolean logData;

    // How many vreads to pack into one Bundle request
    private int vreadBatchSize = 10;

    // Deserialize and serialize the resource instead of just passing through the byte array. Slower.
    private boolean parseResource;

    // How many simultaneous vreads
    private int readerPoolSize = 32;

    // how many resources do we request for each upstream whole-system history call
    private int resourcesPerHistoryCall = 512;

    // Start processing from this point in the change stream
    private long changeIdMarker = -1;

    // How many seconds to run for (default forever)
    private long runDurationSeconds = -1;

    // How many seconds to wait for queued work to complete after the scan completes
    private long drainForSeconds = 600;

    /**
     * Parse the command line arguments
     * @param args
     */
    public void parseArgs(String[] args) {
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--upstream-properties":
                if (i < args.length + 1) {
                    loadUpstreamProperties(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --upstream-properties");
                }
                break;
            case "--upstream-tenant":
                if (i < args.length + 1) {
                    this.upstreamTenant = args[++i];
                } else {
                    throw new IllegalArgumentException("missing value for --upstream-tenant");
                }
                break;
            case "--downstream-tenant":
                if (i < args.length + 1) {
                    this.downstreamTenant = args[++i];
                } else {
                    throw new IllegalArgumentException("missing value for --downstream-tenant");
                }
                break;
            case "--downstream-properties":
                if (i < args.length + 1) {
                    loadDownstreamProperties(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --downstream-properties");
                }
                break;
            case "--partition-count":
                if (i < args.length + 1) {
                    this.partitionCount = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --partition-count");
                }
                break;
            case "--partition-queue-size":
                if (i < args.length + 1) {
                    this.partitionQueueSize = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --partition-queue-size");
                }
                break;
            case "--reader-pool-size":
                if (i < args.length + 1) {
                    this.readerPoolSize = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --reader-pool-size");
                }
                break;
            case "--run-duration":
                if (i < args.length + 1) {
                    this.runDurationSeconds = Long.parseLong(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --run-duration");
                }
                break;
            case "--drain-for-seconds":
                if (i < args.length + 1) {
                    this.drainForSeconds = Long.parseLong(args[++i]);
                } else {
                    throw new IllegalArgumentException("missing value for --drain-for-seconds");
                }
                break;
            case "--parse-resource":
                this.parseResource = true;
                break;
            case "--log-data":
                this.logData = true;
                break;
            default:
                throw new IllegalArgumentException("Bad arg: " + arg);
            }
        }
        
    }

    /**
     * Read the properties file for the upstream configuration
     * @param filename
     */
    private void loadUpstreamProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            this.upstreamProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }
    
    /**
     * Read the properties file for the downstream configuration
     * @param filename
     */
    private void loadDownstreamProperties(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            this.downstreamProperties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }        
    }
    /**
     * Run the flow
     */
    public void process() {
        // Wire together the components
        FHIRBucketClient upstreamClient = new FHIRBucketClient(new ClientPropertyAdapter(upstreamProperties));
        upstreamClient.init(upstreamTenant);

        // Create the thread-pool used by the flow pool to VREAD resources. Need to limit the
        // work queue size because we can probably read from upstream more quickly than we can
        // write to the downstream system. But we want the queue size to be at least as big
        // as the number of resource entries returned in one history call so that we don't
        // delay turning around and getting the next set
        final RejectedExecutionHandler rejectionPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        ExecutorService upstreamThreadPool = new ThreadPoolExecutor(readerPoolSize, readerPoolSize,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(resourcesPerHistoryCall), rejectionPolicy);
        
        // Create the pool used to handle the async vreads
        FlowPool readerPool = new FlowPool(upstreamClient, upstreamThreadPool, this.parseResource);

        // Set up our target
        IFlowWriter downstreamWriter;
        if (downstreamProperties.size() > 0) {
            logger.info("Using downstream tenant: " + this.downstreamTenant);
            FHIRBucketClient downstreamClient = new FHIRBucketClient(new ClientPropertyAdapter(downstreamProperties));
            downstreamClient.init(downstreamTenant);
            downstreamWriter = new DownstreamFHIRWriter(downstreamClient, partitionCount, partitionQueueSize);
        } else {
            downstreamWriter = new DownstreamLogWriter(partitionCount, partitionQueueSize, this.logData);
        }

        UpstreamFHIRHistoryReader historyReader = new UpstreamFHIRHistoryReader(this.resourcesPerHistoryCall, this.changeIdMarker, this.drainForSeconds);
        historyReader.setClient(upstreamClient);
        historyReader.setFlowPool(readerPool);
        historyReader.setFlowWriter(downstreamWriter);

        Duration runDuration = null;
        if (this.runDurationSeconds > 0) {
            runDuration = Duration.ofSeconds(runDurationSeconds);
        }

        // keep fetching for the configured duration, or forever if runDuration is null
        historyReader.fetch(runDuration);

        logger.info("Fetch complete. Shutting down");

        // Attempt to do an orderly shutdown of the pools, starting upstream and working
        // our way downstream
        upstreamThreadPool.shutdown();
        downstreamWriter.waitForShutdown();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Main m = new Main();
        try {
            m.parseArgs(args);
            m.process();
        } catch (Exception x) {
            logger.log(Level.SEVERE, "fhir-flow failed", x);
        }
    }

}
