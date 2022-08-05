/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.bucket.scanner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linuxforhealth.fhir.bucket.api.BucketLoaderJob;
import org.linuxforhealth.fhir.bucket.api.FileType;
import org.linuxforhealth.fhir.bucket.api.ResourceEntry;
import org.linuxforhealth.fhir.database.utils.thread.ThreadHandler;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.validation.FHIRValidator;
import org.linuxforhealth.fhir.validation.exception.FHIRValidationException;

/**
 * Reads local files directly and processes them to the {@link ResourceHandler} for processing.
 * Does not rely on COS or the fhirbucket database. Useful for simpler load testing and
 * loading something like a development database with test data
 */
public class ImmediateLocalFileReader {
    private static final Logger logger = Logger.getLogger(ImmediateLocalFileReader.class.getName());

    // The types of file we are supposed to process
    private final Set<FileType> fileTypes;

    // The base directory we scan for content to load
    private final String baseDirectory;

    // The handler which processes resources we've read
    private final Consumer<ResourceEntry> resourceHandler;

    // The pool used to parallelize loading
    private final ExecutorService pool;

    // active object thread
    private Thread mainLoopThread;

    // flow control
    private Lock lock = new ReentrantLock();
    private Condition resourceLimit = lock.newCondition();
    private int inflight = 0;

    // never allow more than this number of items to be allocated to this instance
    private int maxInflight;

    // allocate more items when the current inflight drops below this threshold
    private int rescanThreshold;

    // active object running flag
    private volatile boolean running = true;

    // The cost of a bundle compared to a single resource
    private final double bundleCostFactor;

    // A list of all the files we've found by scanning baseDirectory
    private final List<BucketLoaderJob> scannedJobList = new ArrayList<>();
    
    // Index into scannedJobList of the next job we want to allocate
    private int nextJobIndex = 0;

    /**
     * Public constructor
     * @param commonPool thread pool shared by the readers and request handler
     * @param fileType the file type this read is responsible for processing
     * @param baseDirectory
     * @param resourceHandler
     * @param poolSize
     * @param incremental
     * @param recycleSeconds
     * @param incrementalExact
     * @param bundleCostFactor
     */
    public ImmediateLocalFileReader(ExecutorService commonPool, Set<FileType> fileTypes, String baseDirectory, 
            Consumer<ResourceEntry> resourceHandler, int maxInflight, 
            double bundleCostFactor) {
        this.pool = commonPool;
        this.fileTypes = fileTypes;
        this.baseDirectory = baseDirectory;
        this.resourceHandler = resourceHandler;
        this.maxInflight = maxInflight;
        this.rescanThreshold = Math.max(1, maxInflight/2);
        this.bundleCostFactor = bundleCostFactor;
    }
    

    /**
     * Tell the main thread of this active object that it should start shutting down
     */
    public void signalStop() {
        if (this.running) {
            logger.info("Stopping Local File Reader");
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
        logger.info("Waiting for LocalFileReader to stop");

        if (mainLoopThread != null) {
            try {
                // give it a few seconds to respond
                mainLoopThread.join(5000);
            } catch (InterruptedException x) {
                logger.warning("CosReader loop did not terminate in 5000ms");
            }
        }
        logger.info("LocalFileReader stopped");
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

        // Before we start the main processing loop, do a scan of the directory to
        // build the full list of files we want to process (each represented as its
        // own job).
        if (this.running) {
            findFiles();
        }
        
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
                        logger.info("Jobs inflight: " + this.inflight + ", just allocated: " + allocated);
                        
                        // If we have nothing running and didn't allocate anything it
                        // means we're all done
                        if (this.inflight == 0 && allocated == 0) {
                            logger.info("All done; terminating");
                            this.running = false;
                        }
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
     * Scan the baseDirectory and create a BucketLoaderJob entry in the scannedJobList
     * @param dir
     */
    private void findFiles() {

        try {
            Files.walkFileTree(Paths.get(baseDirectory), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                  throws IOException {
                    if (!Files.isDirectory(file)) {
                        addFile(file, attrs.size());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException x) {
            throw new IllegalStateException("Failed to scan '" + this.baseDirectory + "'", x);
        }
        
        if (this.scannedJobList.isEmpty()) {
            throw new IllegalStateException("No loadable content found in '" + this.baseDirectory + "'");
        }
    }

    /**
     * Add the given file to the scannedJobList
     * @param file
     */
    private void addFile(Path file, long size) {
        final BucketLoaderJob job;
        
        if (file.getFileName().toString().endsWith(".ndjson") && fileTypes.contains(FileType.NDJSON)) {
            job = new BucketLoaderJob(-1, -1, "", "/", file.toString(), size, FileType.NDJSON, 1);
        } else if (file.getFileName().toString().endsWith(".json") && fileTypes.contains(FileType.JSON)) {
            job = new BucketLoaderJob(-1, -1, "", "/", file.toString(), size, FileType.JSON, 1);
        } else {
            logger.info("Ignoring unsupported file type: " + file.toString());
            job = null;
        }
        
        if (job != null) {
            logger.info(() -> "Adding job to scanned list: " + job.toString());
            this.scannedJobList.add(job);
        }
    }

    /**
     * Ask the database to allocate up to free jobs for this loader
     * instance to process.
     * @param free
     */
    private int allocateJobs(int free) {
        logger.info("allocateJobs: free = " + free);

        int remaining = scannedJobList.size() - nextJobIndex;
        int allocated = Math.min(remaining, free);
        
        if (allocated > 0) {
            // Allocate the next batch of jobs (files) from the scannedJobList
            final List<BucketLoaderJob> jobList = scannedJobList.subList(nextJobIndex, nextJobIndex+allocated);
            nextJobIndex += allocated;
            
            // Tell each job to call us back when they are done
            jobList.stream().forEach(job -> job.registerCallback(jd -> markJobDone(jd)));
            
            // add each job to the pool
            jobList.stream().forEach(job -> process(job));
        }

        return allocated;
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
        } finally {
            // As this job is now finally complete, we can reduce our inflight count, freeing up capacity
            // for new jobs to be allocated
            lock.lock();
            try {
                inflight--;
                logger.info("Job completed inflight count now: " + inflight);

                // Apply some hysteresis before we wake up the allocation
                // thread. This is so we fetch larger allocations in fewer
                // calls...although this isn't strictly necessary because
                // here we are reading from a prepared list.
                if (inflight < rescanThreshold) {
                    logger.info("triggering job fetch [local file]");
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
        // open the stream for the object and pass it to
        // our process method for reading
        try {
            logger.info("Processing job: " + job.toString());
            job.setProcessingStartTime(System.nanoTime());
            switch (job.getFileType()) {
            case NDJSON:
                // Process the object using our NDJSON reader
                readFile(job, br -> processNDJSON(job, br));
                break;
            case JSON:
                // Process the object using our JSON reader
                readFile(job, rdr -> processJSON(job, rdr));
                break;
            default:
                logger.warning("Unrecognized file type for job: " + job.toString());
                break;
            }
        } catch (Exception x) {
            // make sure we don't propagate exceptions back to the pool thread. This probably
            // means we hit a permission issue when scanning
            logger.log(Level.SEVERE, "Error processing job: " + job.toString(), x);
        }
    }

    /**
     * Read the file described by the job and feed the contents to the given consumer
     * @param job
     * @param consumer
     * @throws IOException
     */
    private void readFile(final BucketLoaderJob job, Consumer<BufferedReader> consumer) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(job.getObjectKey()), StandardCharsets.UTF_8))) {
            consumer.accept(br);
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
            // note the error and carry on
            logger.warning("failed to process job '" + job.toString() + "': " + x.getMessage());
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
                // note that we don't increment the job counter until we actually
                // submit something to the process queue
                StringReader lineReader = new StringReader(line);
                try {
                    success = process(job, FHIRParser.parser(Format.JSON).parse(lineReader), lineNumber++, line) && success;
                } catch (FHIRParserException x) {
                    // Something is wrong with this current line:
                    logger.log(Level.WARNING, line, x);
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
     * Process the resource parsed from the input stream. When processing 
     * NDJSON the lineNumber and line are logged when an error occurs to
     * help locate the corresponding entry in the file. When processing
     * JSON files, lineNumber doesn't but should be set to 0. The line
     * value can be blank.
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
        } catch (FHIRValidationException e) {
            logger.warning("Resource validation exception: " + e.getMessage());
        }

        if (result) {
            try {
                // Note if we hit an exception before the entry is queued, the
                // jobs entry count will not be incremented, so we don't need
                // to worry about adding to the completion counter
                resourceHandler.accept(new ResourceEntry(job, resource, lineNumber, costForResource(resource)));
            } catch (Exception x) {
                result = false;
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
}