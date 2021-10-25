/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bucket.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.fhir.bucket.api.BucketLoaderJob;
import com.ibm.fhir.bucket.api.BucketPath;
import com.ibm.fhir.bucket.api.FileType;
import com.ibm.fhir.bucket.api.ResourceBundleError;
import com.ibm.fhir.bucket.api.ResourceEntry;
import com.ibm.fhir.bucket.api.ResourceRef;
import com.ibm.fhir.bucket.cos.COSClient;
import com.ibm.fhir.database.utils.thread.ThreadHandler;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
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
public class COSReader {
    private static final Logger logger = Logger.getLogger(COSReader.class.getName());

    // The type of file we are supposed to process
    private final FileType fileType;

    // Abstraction of our connection to cloud object storage
    private final COSClient client;

    // The handler which processes resources we've read from COS
    private final Consumer<ResourceEntry> resourceHandler;

    // The pool used to parallelize loading
    private final ExecutorService pool;

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

    // Try to skip over rows we've already processed in a bundle
    private final boolean incremental;

    // Only process lines for which no resources have been recorded. More expensive.
    private final boolean incrementalExact;

    // Number of seconds before recycling completed bundles so we can keep loading on a continuous basis
    private int recycleSeconds;

    // The cost of a bundle compared to a single resource
    private final double bundleCostFactor;

    private final List<BucketPath> bucketPaths;

    /**
     * Public constructor
     * @param commonPool thread pool shared by the readers and request handler
     * @param fileType the file type this read is responsible for processing
     * @param client
     * @param resourceHandler
     * @param poolSize
     * @param da
     * @param incremental
     * @param recycleSeconds
     */
    public COSReader(ExecutorService commonPool, FileType fileType, COSClient client, Consumer<ResourceEntry> resourceHandler, int maxInflight, DataAccess da, boolean incremental, int recycleSeconds, boolean incrementalExact,
        double bundleCostFactor, Collection<BucketPath> bucketPaths) {
        this.pool = commonPool;
        this.fileType = fileType;
        this.client = client;
        this.resourceHandler = resourceHandler;
        this.dataAccess = da;
        this.maxInflight = maxInflight;
        this.rescanThreshold = Math.max(1, maxInflight/2);
        this.incremental = incremental;
        this.recycleSeconds = recycleSeconds;
        this.incrementalExact = incrementalExact;
        this.bundleCostFactor = bundleCostFactor;
        this.bucketPaths = new ArrayList<>(bucketPaths);
    }

    /**
     * Tell the main thread of this active object that it should start shutting down
     */
    public void signalStop() {
        if (this.running) {
            logger.info("Stopping COS reader");
            this.running = false;
        }

        if (mainLoopThread != null) {
            // interrupt it in case it's stuck doing IO
            this.mainLoopThread.interrupt();

            // wake up the thread if it's currently waiting on a condition
            lock.lock();
            try {
                resourceLimit.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Tell the main loop thread to stop
     */
    public void waitForStop() {
        signalStop();
        logger.info("Waiting for COS reader to stop");

        if (mainLoopThread != null) {
            try {
                // give it a few seconds to respond
                mainLoopThread.join(5000);
            } catch (InterruptedException x) {
                logger.warning("CosReader loop did not terminate in 5000ms");
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
                // wait here until inflight drops below the rescanThreshold
                while (running && this.inflight >= this.rescanThreshold) {
                    resourceLimit.await();
                }

                // Calculate the max number of jobs we can request to be allocated
                // to this instance from the database. Need to be careful with
                // possible contention here because we are holding the lock during
                // the allocation database call.
                if (running) {
                    free = this.maxInflight - this.inflight;
                    if (free > 0) {
                        allocated = allocateJobs(free);
                        this.inflight += allocated;
                        logger.info("Jobs inflight["  + fileType.name() + "] " + this.inflight + ", just allocated: " + allocated);
                    }
                }
            } catch (InterruptedException x) {
                // NOP
            } catch (Exception x) {
                // Probably database connection error, so we take a good long pause
                // before trying again as these things are never fixed quickly
                logger.severe("Error in main allocation loop. Sleeping before retry");
                if (this.running) {
                    ThreadHandler.safeSleep(ThreadHandler.MINUTE);
                }
            } finally {
                lock.unlock();
            }

            if (running && allocated < free) {
                // We have more capacity than work is currently available in the database,
                // so take a nap before checking again
                logger.fine("No work. Napping");
                ThreadHandler.safeSleep(ThreadHandler.TEN_SECONDS);
            }
        }
    }

    /**
     * Ask the database to allocate up to free jobs for this loader
     * instance to process.
     * @param free
     */
    private int allocateJobs(int free) {
        logger.info("allocateJobs[" + fileType.name() + "]: free = " + free);
        List<BucketLoaderJob> jobList = new ArrayList<>();
        dataAccess.allocateJobs(jobList, fileType, free, recycleSeconds, this.bucketPaths);

        logger.info("Allocated job count["  + fileType.name() + "]: " + jobList.size());

        // Tell each job to call us back when they are done
        jobList.stream().forEach(job -> job.registerCallback(jd -> markJobDone(jd)));

        // add each job to the pool
        jobList.stream().forEach(job -> process(job));

        return jobList.size();
    }

    /**
     * Callback when the last record in the job completes
     * @param job
     */
    protected void markJobDone(final BucketLoaderJob job) {
        // The number of seconds the whole job took to complete (including queue time)
        double elapsedSeconds = (job.getProcessingEndTime() - job.getProcessingStartTime()) / 1e9;

        // The response time of the last call...which is useful if the request is one big bundle
        double lastCallTime = job.getLastCallResponseTime() / 1e3;

        int resources = job.getTotalResourceCount();
        double rps = Double.NaN;
        if (lastCallTime > 0) {
            rps = resources / lastCallTime;
        }

        try {
            // Log some useful info so we can eyeball rate/progress in the logs
            logger.info(String.format("Completed entry: %s [took %.3f secs, lastCall: %.3f secs, resources: %d, rate: %.1f resources/sec]", job.toString(), elapsedSeconds, lastCallTime, resources, rps));
            dataAccess.markJobDone(job);
        } finally {
            // As this job is now finally complete, we can reduce our inflight count, freeing up capacity
            // for new jobs to be allocated
            lock.lock();
            try {
                inflight--;
                logger.info("Job completed[" + fileType.name() + "] inflight count now: " + inflight);

                // Apply some hysteresis before we wake up the allocation
                // thread. This is so we fetch larger allocations in fewer
                // calls to the database
                if (inflight < rescanThreshold) {
                    logger.info("triggering job fetch[" + fileType.name() + "]");
                    resourceLimit.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Submit the job to the internal threadpool
     * @param job
     */
    private void process(BucketLoaderJob job) {
        pool.submit(() -> {
            processThr(job);
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
            job.setProcessingStartTime(System.nanoTime());
            switch (job.getFileType()) {
            case NDJSON:
                // Process the object using our NDJSON reader
                client.process(job.getBucketName(), job.getObjectKey(), br -> processNDJSON(job, br));
                break;
            case JSON:
                // Process the object using our JSON reader
                client.process(job.getBucketName(), job.getObjectKey(), rdr -> processJSON(job, rdr));
                break;
            default:
                logger.warning("Unrecognized file type for job: " + job.toString());
                break;
            }
        } catch (Exception x) {
            // make sure we don't propagate exceptions back to the pool thread. This probably
            // means we are having problems talking to COS or the FHIRBUCKET tracking database
            // because parsing and processing errors are handled inside the processNDJSON/processJSON
            // methods
            logger.log(Level.SEVERE, "Error processing job: " + job.toString(), x);
        }
    }

    /**
     * Process a JSON stream (as opposed to an NDJSON stream)
     * @param job
     * @param reader
     */
    private void processJSON(final BucketLoaderJob job, final Reader reader) {
        final int lineNumber = 0;
        try {
            process(job, FHIRParser.parser(Format.JSON).parse(reader), lineNumber, "");
        } catch (FHIRParserException x) {
            // record the error in the database
            ResourceBundleError error = new ResourceBundleError(lineNumber, "Parse error: " + x.getMessage());
            dataAccess.recordErrors(job.getResourceBundleLoadId(), lineNumber, Collections.singletonList(error));
        } finally {
            // Note that the job done callback will only be triggered when both file processing is done
            // AND all the work that was generated from the file is also finished.
            job.fileProcessingComplete();
        }
    }

    /**
     * What's the processing cost for this resource? We weight bundles with
     * a heavier cost because typically they are much larger and take longer
     * to process.
     * @param r
     * @return
     */
    private int costForResource(Resource r) {
        // Bundles tend to be a lot larger, so we limit how many we try to
        // process in parallel.
        if (r.is(Bundle.class)) {
            // Estimate a cost based on the number of entries in the bundle
            // multiplied by the cost factor. Cost can never be < 1.
            Bundle b = r.as(Bundle.class);
            return Math.max(1, (int)(this.bundleCostFactor * b.getEntry().size()));
        } else {
            return 1;
        }
    }

    /**
     * Read the resources from the given reader
     * @param is
     * @return
     */
    public void processNDJSON(final BucketLoaderJob job, final BufferedReader br) {

        int skipLines = 0;
        if (this.incremental && !incrementalExact) {
            Integer maxLineNumber = getLastProcessedLineNumber(job);
            if (maxLineNumber != null) {
                // line numbers start at 0
                skipLines = maxLineNumber + 1;

                logger.info(job.toString() + "; previously processed, so skipping lines: " + skipLines);
            }
        }

        // Reading as a continuous stream appears to be problematic,
        // so we have to take a line-based approach
        boolean success = true;
        try {
            int lineNumber = 0;
            String line;

            // skip lines we've already processed if we're doing an incremental load
            while (lineNumber < skipLines && (line = br.readLine()) != null) {
                lineNumber++;
            }

            while ((line = br.readLine()) != null) {
                // Skip this line if we can find logical ids have already been recorded for it
                // (from a previous load run). This is version-specific, so if the version has
                // changed, then we reload everything again.
                if (incrementalExact && getLogicalIdsForLine(job, lineNumber).size() > 0) {
                    continue;
                }

                // note that we don't increment the job counter until we actually
                // submit something to the process queue
                StringReader lineReader = new StringReader(line);
                try {
                    success = process(job, FHIRParser.parser(Format.JSON).parse(lineReader), lineNumber++, line) && success;
                } catch (FHIRParserException x) {
                    // Something is wrong with this current line:
                    logger.log(Level.WARNING, line, x);

                    // Record the error in the database
                    ResourceBundleError error = new ResourceBundleError(lineNumber, "Parse error: " + x.getMessage());
                    dataAccess.recordErrors(job.getResourceBundleLoadId(), lineNumber, Collections.singletonList(error));
                }
            }
        } catch (IOException x) {
            // errors will be logged where this exception is handled
            throw new IllegalStateException(x);
        } finally {
            job.fileProcessingComplete();
        }
    }


    /**
     * Process the resource parsed from the input stream
     * @param details of the job being processed
     * @param resource parsed from the resource_bundle being processed
     * @param lineNumber the line number of this resource in the source
     * @param line the original resource string, for logging if we need to
     */
    protected boolean process(BucketLoaderJob job, Resource resource, int lineNumber, String line) {
        boolean result = false;

        try {
            validateInput(resource);
            result = true;
        } catch (FHIROperationException e) {
            logger.warning("Resource validation failed: " + e.getMessage());

            String info = e.getIssues().stream()
                    .flatMap(issue -> Stream.of(issue.getDetails()))
                    .flatMap(details -> Stream.of(details.getText()))
                    .flatMap(text -> Stream.of(text.getValue()))
                    .collect(Collectors.joining(", "));

            if (logger.isLoggable(Level.FINER)) {
                logger.finer("Validation warnings for input resource: [" + info + "]");
            }

            ResourceBundleError error = new ResourceBundleError(lineNumber, info);
            dataAccess.recordErrors(job.getResourceBundleLoadId(), lineNumber, Collections.singletonList(error));

        } catch (FHIRValidationException e) {
            logger.warning("Resource validation exception: " + e.getMessage());
            ResourceBundleError error = new ResourceBundleError(lineNumber, e.getMessage());
            dataAccess.recordErrors(job.getResourceBundleLoadId(), lineNumber, Collections.singletonList(error));
        }

        if (result) {
            try {
                // Note if we hit an exception before the entry is queued, the
                // jobs entry count will not be incremented, so we don't need
                // to worry about adding to the completion counter
                resourceHandler.accept(new ResourceEntry(job, resource, lineNumber, costForResource(resource)));
            } catch (Exception x) {
                result = false;
                ResourceBundleError error = new ResourceBundleError(lineNumber, x.getMessage());
                dataAccess.recordErrors(job.getResourceBundleLoadId(), lineNumber, Collections.singletonList(error));

                if (logger.isLoggable(Level.FINE)) {
                    // write the full error and resource to the log file
                    logger.fine(job.getObjectKey() + "[" + lineNumber + "]: " + line);
                    logger.fine(x.getMessage());
                }
            }
        }

        return result;
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
            } else if (logger.isLoggable(Level.FINER)) {
                // optionally log any warnings
                String info = issues.stream()
                            .flatMap(issue -> Stream.of(issue.getDetails()))
                            .flatMap(details -> Stream.of(details.getText()))
                            .flatMap(text -> Stream.of(text.getValue()))
                            .collect(Collectors.joining(", "));
                logger.finer("Validation warnings for input resource: [" + info + "]");
            }
        }
        return issues;
    }

    /**
     * Get the last processed line for the bundle and version described by the
     * {@link BucketLoaderJob} record
     * @param job
     * @return
     */
    private Integer getLastProcessedLineNumber(BucketLoaderJob job) {
        return dataAccess.getLastProcessedLineNumber(job.getResourceBundleId(), job.getVersion());
    }

    /**
     * Find the set of logicalIds have been created for the given line
     * of the bundle/version described in the given BucketLoaderJob. This
     * can be useful
     * @param job
     * @param lineNumber
     * @return
     */
    private List<ResourceRef> getLogicalIdsForLine(BucketLoaderJob job, int lineNumber) {
        return dataAccess.getResourceRefsForLine(job.getResourceBundleId(), job.getVersion(), lineNumber);
    }
}