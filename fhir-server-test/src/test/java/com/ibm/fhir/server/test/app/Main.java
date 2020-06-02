/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.spec.test.DriverMetrics;
import com.ibm.fhir.model.spec.test.DriverStats;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.server.test.examples.ExampleRequestProcessor;
import com.ibm.fhir.validation.test.ValidationProcessor;

/**
 * Simple main to exercise the R4 examples over HTTP. Uses a thread-pool to
 * introduce some parallelism which gives us an opportunity to test the
 * FHIR server for any concurrency issues as well as push it a little
 * harder to expose any performance hotspots.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    // number of nanoseconds in a second
    private static final double NANOS = 1e9;

    // The tenantId to use in requests to FHIR
    private String tenantId;

    // The size of the thread-pool to use for concurrent requests
    private int threads = 0;
    
    // The number of requests which can be submitted to the pool before blocking
    private int maxInflight;
    
    // Multiply the number of actual reads we do for each operation
    private int readIterations = 1;

    // The thread pool used for concurrent request processing
    private ExecutorService pool;

    // number of ms to wait for the thread pool to shut down
    private long poolShutdownWaitTime = 60000;
    
    // Configuration properties
    private Properties properties = new Properties();
    
    private Index index = Index.PERFORMANCE_JSON;

    /**
     * Parse the command line arguments
     *   --threads (n)
     *   --max-inflight (n)
     *   --tenantId (tenant-id)
     *   --prop-file (file.properties)
     * @param args
     */
    public void parseArgs(String[] args) {
        // really simple args, so nothing fancy needed here
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--prop-file":
                if (++i < args.length) {
                    loadPropertyFile(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --prop-file argument at posn: " + i);
                }
                break;
            case "--index":
                if (++i < args.length) {
                    this.index = Index.valueOf(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --index argument at posn: " + i);
                }
                break;
            case "--tenant-id":
                if (++i < args.length) {
                    this.tenantId = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --tenant-id argument at posn: " + i);
                }
                break;
            case "--threads":
                if (++i < args.length) {
                    this.threads = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --threads argument at posn: " + i);
                }
                break;
            case "--read-iterations":
                if (++i < args.length) {
                    this.readIterations = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --read-iterations argument at posn: " + i);
                }
                break;
            case "--max-inflight":
                if (++i < args.length) {
                    this.maxInflight = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --max-inflight argument at posn: " + i);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
    }
    
    /**
     * Read the properties file
     * @param filename
     */
    public void loadPropertyFile(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            properties.load(is);
        }
        catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }
        

    /**
     * Configure the class using the values collected from parseArgs
     */
    protected void configure() {
        if (threads > 0) {
            pool = Executors.newFixedThreadPool(threads);
        }
    }

    /**
     * Process the R4 examples
     */
    protected void process() throws Exception {
        final R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setValidator(new ValidationProcessor());

        // Collect some response time metrics for reading then processing each resource
        DriverMetrics dm = new DriverMetrics();
        driver.setMetrics(dm);
        
        // Inject the tenant-id into the properties so that it gets picked up by the FHIRClientImpl
        if (tenantId != null) {
            logger.info("Setting tenant-id = " + tenantId);
            this.properties.setProperty("fhirclient.tenant.id", tenantId);
        }
        
        TestContext context = new TestContext();
        context.setUp(this.properties);
        ExampleRequestProcessor erp = new ExampleRequestProcessor(context, tenantId, dm, this.readIterations);
        driver.setProcessor(erp);
        
        
        if (pool != null) {
            // reasonable default to make sure we don't stall the thread-pool pipeline
            if (maxInflight == 0) {
                maxInflight = threads * 2;
            }
            driver.setPool(pool, maxInflight);
        }
        
        // process the examples in the PERFORMANCE index (all examples < 1MB)
        long start = System.nanoTime();
        driver.processIndex(this.index);
        long elapsed = (System.nanoTime() - start) / DriverMetrics.NANOS_MS;
        if (elapsed == 0) {
            elapsed = 1; // unlikely, but just to be on the safe side
        }
        renderReport(dm, elapsed);
    }
    
    /**
     * Pretty-print the response time metrics collected during the run
     * @param dm
     * @param elapsed number of milliseconds elapsed
     */
    protected void renderReport(DriverMetrics dm, long elapsed) {
        // how many seconds did we run for?
        double secs = elapsed / 1000.0;
        dm.render(new DriverStats(System.out, secs));
    }

    /**
     * Release any resources
     */
    protected void shutdown() {
        if (pool != null) {
            logger.info("Shutting down pool");
            pool.shutdown();
            try {
                pool.awaitTermination(poolShutdownWaitTime, TimeUnit.MILLISECONDS);
                logger.info("Pool shutdown complete");
            }
            catch (InterruptedException x) {
                // Best effort to stop everything now
                logger.warning("Pool shutdown interrupted. Attempting to force termination");
                pool.shutdownNow();
            }
        }
    }

    /**
     * Main entry
     * @param args
     */
    public static void main(String[] args) {

        Main m = new Main();
        try {
            m.parseArgs(args);
            m.configure();
            
            long start = System.nanoTime();
            m.process();
            long end = System.nanoTime();
            
            logger.info(String.format("Took: %6.3f seconds", (end-start)/NANOS));
        }
        catch (Exception x) {
            logger.log(Level.SEVERE, "Failed to run", x);
        }
        finally {
            m.shutdown();
            System.exit(0);
        }
    }
}
