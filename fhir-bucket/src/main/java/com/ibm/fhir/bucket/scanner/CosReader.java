/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bucket.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.fhir.bucket.api.BucketLoaderJob;
import com.ibm.fhir.bucket.api.ResourceEntry;
import com.ibm.fhir.bucket.cos.CosClient;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * Grabs work from the bucket database and dispatches items
 * to the thread pool.
 *
 */
public class CosReader {
    private static final Logger logger = Logger.getLogger(CosReader.class.getName());
    
    // Abstraction of our connection to cloud object storage
    private final CosClient client;

    // The handler which processes resources we've read from COS
    private final Consumer<ResourceEntry> resourceHandler;
    
    // The number of threads in our thread pool
    private final int poolSize;

    // The pool used to parallelize loading
    private ExecutorService pool;
    
    // Access to the data in the bucket schema
    private final DataAccess dataAccess;
    
    // active object thread
    private Thread mainLoopThread;

    // flow control
    private Lock lock = new ReentrantLock();
    private Condition resourceLimit = lock.newCondition();
    private int inflight = 0;
    
    // never allow more than this number of items to be allocated to this instance
    private int maxInflight;
    
    // ask the database to allocate more items when the current inflight drops below this threshold
    private int rescanThreshold;

    // active object running flag
    private volatile boolean running = true;

    /**
     * Public constructor
     * @param client
     */
    public CosReader(CosClient client, Consumer<ResourceEntry> resourceHandler, int poolSize, DataAccess da) {
        this.client = client;
        this.resourceHandler = resourceHandler;
        this.poolSize = poolSize;
        pool = Executors.newFixedThreadPool(poolSize);
        this.dataAccess = da;
        this.maxInflight = 3 * poolSize;
        this.rescanThreshold = 2 * poolSize;
    }
    
    /**
     * Tell the main loop thread to stop
     */
    public void stop() {
        logger.info("Stopping COS reader");
        this.running = false;
        
        if (mainLoopThread != null) {
            this.mainLoopThread.interrupt();
            try {
                // give it a few seconds to respond
                mainLoopThread.join(5000);
            } catch (InterruptedException x) {
                logger.warning("Main loop thread did not terminate in 5000ms");
            }
        }
        logger.info("COS reader stopped");
    }

    /**
     * start the allocation thread
     */
    public void init() {
        mainLoopThread = new Thread(() -> mainAllocationLoop());
        mainLoopThread.start();
    }

    /**
     * The main loop of this active object
     */
    public void mainAllocationLoop() {
        while (this.running) {
            
            int free = 0;
            int allocated = 0;
            lock.lock();
            try {
                while (this.inflight >= this.rescanThreshold) {
                    resourceLimit.await();
                }
                
                // Calculate the max number of jobs we can request to be allocated
                // to this instance from the database. Need to be careful with
                // possible contention here because we are holding the lock during
                // the allocation database call. The alternative is to unlock, read,
                // then lock a second time if the allocation is smaller than free.
                // TODO...consider this change
                free = this.maxInflight - this.inflight;
                if (free > 0) {
                    allocated = allocateJobs(free);
                    this.inflight += allocated;
                }
            } catch (InterruptedException x) {
                // NOP
            } finally {
                lock.unlock();
            }

            if (allocated < free) {
                // We have more capacity than work is currently available in the database,
                // so take a nap before checking again
                logger.fine("No work. Napping");
                safeSleep(5000);
            }
        }
    }
    
    /**
     * Sleep this thread for the given milliseconds
     * @param millis
     */
    protected void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException x) {
            // NOP
        }
    }


    /**
     * Ask the database to allocate up to free jobs for this loader
     * instance to process.
     * @param free
     */
    private int allocateJobs(int free) {
        logger.info("allocateJobs: free = " + free);
        List<BucketLoaderJob> jobList = new ArrayList<>();
        dataAccess.allocateJobs(jobList, free);
        
        logger.info("Allocated job count: " + jobList.size());

        // add each job to the pool
        jobList.stream().forEach(job -> process(job));
        
        return jobList.size();
    }
    
    /**
     * Submit the job to the internal threadpool
     * @param job
     */
    private void process(BucketLoaderJob job) {
        pool.submit(() -> {
            try {
                processThr(job);
            } finally {
                // free up capacity
                lock.lock();
                try {
                    inflight--;
                    
                    // Apply some hysteresis before we wake up the allocation
                    // thread. This is so we fetch larger allocations in fewer
                    // calls to the database
                    if (inflight < rescanThreshold) {
                        resourceLimit.signal();
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    /**
     * Process this job in a thread-pool thread
     * @param job
     */
    private void processThr(final BucketLoaderJob job) {
        // Ask the COS client to open the stream for the object and pass it to
        // our process method for reading
        try {
            logger.info("Processing job: " + job.toString());
            client.process(job.getBucketName(), job.getObjectKey(), br -> process(job, br));
        } catch (Exception x) {
            // make sure we don't propagate exceptions back to the pool thread
            logger.log(Level.SEVERE, "Error processing job: " + job.toString(), x);
        }
    }

    /**
     * Read the resources from the given reader
     * @param is
     * @return
     */
    public void process(final BucketLoaderJob job, final BufferedReader br) {
        
        // Reading as a continuous stream appears to be problematic,
        // so we have to take a line-based approach
        try {
            int lineNumber = 0;
            String line;
            while ((line = br.readLine()) != null) {
                StringReader lineReader = new StringReader(line);
                process(job, FHIRParser.parser(Format.JSON).parse(lineReader), lineNumber++);
            }
        } catch (FHIRParserException x) {
            // errors will be logged where this exception is handled
            throw new IllegalStateException(x);
        } catch (IOException x) {
            // errors will be logged where this exception is handled
            throw new IllegalStateException(x);
        }
    }
    
    /**
     * Process the resource parsed from the input stream
     * @param r
     * @param lineNumber the line number of this resource in the source
     */
    protected void process(BucketLoaderJob job, Resource resource, int lineNumber) {
        
        try {
            validateInput(resource);
            resourceHandler.accept(new ResourceEntry(job, resource));
        } catch (FHIROperationException e) {
            logger.warning("Resource validation failed: " + e.getMessage());
            
            String info = e.getIssues().stream()
                    .flatMap(issue -> Stream.of(issue.getDetails()))
                    .flatMap(details -> Stream.of(details.getText()))
                    .flatMap(text -> Stream.of(text.getValue()))
                    .collect(Collectors.joining(", "));
            logger.warning("Validation warnings for input resource: [" + info + "]");
            
        } catch (FHIRValidationException e) {
            logger.warning("Resource validation failed: " + e.getMessage());
        }
    }

    /**
     * Validate the input resource and throw if there are validation errors
     * @param resource
     * @throws FHIRValidationException
     * @throws FHIROperationException
     */
    public List<OperationOutcome.Issue> validateInput(Resource resource)
            throws FHIRValidationException, FHIROperationException {
        List<OperationOutcome.Issue> issues = FHIRValidator.validator().validate(resource);
        if (!issues.isEmpty()) {
            boolean includesFailure = false;
            for (OperationOutcome.Issue issue : issues) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    includesFailure = true;
                }
            }

            if (includesFailure) {
                throw new FHIROperationException("Input resource failed validation.").withIssue(issues);
            } else if (logger.isLoggable(Level.FINE)) {
                // optionally log any warnings
                String info = issues.stream()
                            .flatMap(issue -> Stream.of(issue.getDetails()))
                            .flatMap(details -> Stream.of(details.getText()))
                            .flatMap(text -> Stream.of(text.getValue()))
                            .collect(Collectors.joining(", "));
                logger.fine("Validation warnings for input resource: [" + info + "]");
            }
        }
        return issues;
    }
}
