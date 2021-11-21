/**
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Run through all the examples from the R4 specification
 */
public class R4ExamplesDriver {
    private static final Logger logger = Logger.getLogger(R4ExamplesDriver.class.getName());

    // Call this processor for each of the examples, if given
    private IExampleProcessor processor;

    // Validate the resource
    private IExampleProcessor validator;

    // track some simple metrics
    private AtomicInteger testCount = new AtomicInteger();
    private AtomicInteger successCount = new AtomicInteger();
    private Exception firstException = null;

    // Optional pool if we want to process the examples in parallel
    private ExecutorService pool;

    // Limit the number of requests we submit to the pool
    private int maxInflight;
    private Lock lock = new ReentrantLock();
    private Condition runningCondition = lock.newCondition();
    private Condition inflightCondition = lock.newCondition();

    // The number of requests submitted but not yet completed (queued + running)
    private int currentlySubmittedCount;

    // optional metrics collection
    private DriverMetrics metrics;

    /**
     * Setter for the processor
     *
     * @param p
     */
    public void setProcessor(IExampleProcessor p) {
        this.processor = p;
    }

    /**
     * Setter for the metrics object
     * @param metrics
     */
    public void setMetrics(DriverMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Setter for the validation processor
     *
     * @param p
     */
    public void setValidator(IExampleProcessor p) {
        this.validator = p;
    }

    /**
     * Setter for the thread-pool. Used only if processing the examples in
     * parallel
     * @param pool the threadpool to use
     * @param maxInflight the maximum number of requests submitted to the pool before blocking
     */
    public void setPool(ExecutorService pool, int maxInflight) {
        this.pool = pool;
        this.maxInflight = maxInflight;
    }

    /**
     * Process all examples referenced from the index file.
     *
     * @throws Exception
     */
    public void processIndex(Index index) throws Exception {
        logger.info(String.format("Processing index '%s'", index));
        // reset the state just in case we are called more than once
        this.firstException = null;
        this.testCount.set(0);
        this.successCount.set(0);

        long start = System.nanoTime();

        List<ExampleProcessorException> errors = new ArrayList<>();
        try {
            // Each line of the index file should be a path to an example resource and an expected outcome
            try (BufferedReader br = new BufferedReader(ExamplesUtil.indexReader(index))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length == 2) {
                        String expectation = tokens[0];
                        String example = tokens[1];
                        if (example.toUpperCase().endsWith(".JSON")) {
                            testCount.incrementAndGet();
                            Expectation exp = Expectation.valueOf(expectation);
                            submitExample(errors, example, Format.JSON, exp);
                        }
                        else if (example.toUpperCase().endsWith(".XML")) {
                            testCount.incrementAndGet();
                            Expectation exp = Expectation.valueOf(expectation);
                            submitExample(errors, example, Format.XML, exp);
                        }
                        else {
                            logger.warning("Unable to infer format from '" + example + "'; example files must end in .json or .xml");
                        }
                    }
                }
            }

            // If we are running with a thread-pool, then we must wait for everything to complete
            if (pool != null) {
                waitForCompletion();
            }

            // propagate the first exception so we fail the test
            if (firstException != null) {
                throw firstException;
            }
        }
        finally {
            if (testCount.get() > 0) {
                long elapsed = (System.nanoTime() - start) / 1000000;

                // Just for formatting
                System.out.println();

                // We count overall success if we successfully process the resource,
                // or if we got an expected exception earlier on
                logger.info("Overall success rate = " + successCount + "/" + testCount + " = "
                        + (100*successCount.get() / testCount.get()) + "%. Took " + elapsed + " ms");
            }

            // We can access errors here safely because waitForCompletion called lock/unlock in this thread (in case you were wondering)
            for (ExampleProcessorException error : errors) {
                logger.warning(error.toString());
            }
        }
    }

    public void processExample(String file, Expectation expectation) throws ExampleProcessorException {
        processExample(file, Format.JSON, expectation);
    }

    /**
     * Submit the given example for processing
     * @param errors the list of errors to accumulate
     * @param file
     * @param format
     * @param expectation
     * @throws ExampleProcessorException
     */
    public void submitExample(List<ExampleProcessorException> errors, String file, Format format, Expectation expectation) throws ExampleProcessorException {
        if (pool != null) {
            // Wait until we have capacity. We do this to throttle the number of requests
            // submitted to pool, hopefully avoiding memory issues if ever we have a really
            // large index to process
            lock.lock();
            while (this.currentlySubmittedCount == this.maxInflight) {
                try {
                    this.inflightCondition.await(1000, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException x) {
                    // NOP
                }
            }
            currentlySubmittedCount++;
            lock.unlock();

            pool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        processExample(file, format, expectation);
                    } catch (ExampleProcessorException e) {
                        lock.lock();
                        errors.add(e);
                        lock.unlock();
                    }
                    finally {
                        lock.lock();
                        int oldCount = currentlySubmittedCount--;
                        if (oldCount == maxInflight) {
                            inflightCondition.signal();
                        }

                        if (currentlySubmittedCount == 0) {
                            runningCondition.signal();
                        }
                        lock.unlock();
                    }
                }
            });
        }
        else {
            // run in-line
            try {
                processExample(file, format, expectation);
            } catch (ExampleProcessorException e) {
                errors.add(e);
            }
        }
    }

    /**
     * Process the example file. If jsonFile is prefixed with "file:" then the file will be read from the filesystem,
     * otherwise it will be treated as a resource on the classpath.
     *
     * @param file
     * @param format
     * @param expectation
     * @throws ExampleProcessorException
     */
    public void processExample(String file, Format format, Expectation expectation)
        throws ExampleProcessorException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Processing: " + file);
        } else {
            System.out.print("."); // So we know it's not stalled
        }
        Expectation actual;

        try {
            Resource resource = readResource(file, format);
            if (resource == null) {
                // this is bad, because we'd expect a FHIRParserException
                throw new AssertionError("readResource(" + file + ") returned null");
            }

            if (expectation == Expectation.PARSE) {
                // If we parsed the resource successfully but expected it to fail, it's a failed
                // test, so we don't try and process it any further
                actual = Expectation.OK;
                ExampleProcessorException error =
                        new ExampleProcessorException(file, expectation, actual);
                if (firstException == null) {
                    firstException = error;
                }
                throw error;
            } else {
                // validate and process the example
                actual = processExample(file, resource, expectation);
            }
        } catch (ExampleProcessorException e) {
            throw e;
        } catch (FHIRParserException fpx) {
            actual = Expectation.PARSE;
            if (expectation == Expectation.PARSE) {
                // successful test, even though we won't be able to validate/process
                successCount.incrementAndGet();
            } else {
                // oops, hit an unexpected parse error
                System.out.println();
                logger.severe("readResource(" + file + ") unexpected failure: " + fpx.getMessage()
                        + ", " + fpx.getPath());

                // continue processing the other files, but capture the first exception so we can fail the test
                // if needed
                ExampleProcessorException error =
                        new ExampleProcessorException(file, expectation, actual, fpx);
                if (firstException == null) {
                    firstException = fpx;
                }
                throw error;
            }
        } catch (Exception e) {
            // continue processing the other files, but capture the first exception so we can fail the test
            // if needed
            ExampleProcessorException error =
                    new ExampleProcessorException(file, expectation, Expectation.PARSE, e);
            if (firstException == null) {
                firstException = e;
            }
            throw error;
        }

        logger.fine(String.format("Processed: wanted:%11s got:%11s %s ", expectation.name(), actual.name(), file));

    }

    /**
     * Process the example resource
     *
     * @param file
     *            so we know the name of the file if there's a problem
     * @param resource
     *            the parsed object
     * @param expectation
     */
    protected Expectation processExample(String file, Resource resource, Expectation expectation)
        throws ExampleProcessorException {
        Expectation actual = Expectation.OK;
        if (validator != null) {
            long start = System.nanoTime();
            try {
                validator.process(file, resource);

                if (expectation == Expectation.VALIDATION) {
                    // this is a problem, because we expected validation to fail
                    resource = null; // prevent processing
                    System.out.println();
                    logger.severe("validateResource(" + file + ") should've failed but didn't");
                    ExampleProcessorException error =
                            new ExampleProcessorException(file, expectation, actual);
                    if (firstException == null) {
                        firstException = error;
                    }
                    throw error;
                }
            } catch (Exception x) {
                // validation failed
                actual = Expectation.VALIDATION;
                resource = null; // stop any further processing

                if (expectation == Expectation.VALIDATION) {
                    // we expected an error, and we got it...so that's a success
                    successCount.incrementAndGet();
                } else {
                    // oops, hit an unexpected validation error
                    System.out.println();
                    logger.log(Level.SEVERE, "validateResource(" + file + ") unexpected failure.", x);

                    // continue processing the other files
                    ExampleProcessorException error =
                            new ExampleProcessorException(file, expectation, actual, x);
                    if (firstException == null) {
                        firstException = x;
                    }
                    throw error;
                }
            }
            finally {
                if (metrics != null) {
                    long validateEnd = System.nanoTime();
                    long validateTime = validateEnd - start;
                    metrics.addValidateTime(validateTime / DriverMetrics.NANOS_MS);
                }
            }
        }

        // process the resource (as long as validation was successful
        if (processor != null && resource != null) {
            long start = System.nanoTime();
            try {
                processor.process(file, resource);

                if (expectation == Expectation.PROCESS) {
                    // this is a problem, because we expected validation to fail
                    System.out.println();
                    logger.severe("processResource(" + file + ") should've failed but didn't");
                    ExampleProcessorException error =
                            new ExampleProcessorException(file, expectation, actual);
                    if (firstException == null) {
                        firstException = error;
                    }
                    throw error;
                } else {
                    // processed the resource successfully, and didn't expect an error
                    successCount.incrementAndGet();
                }
            } catch (Exception x) {
                // say in which phase we failed
                actual = Expectation.PROCESS;

                if (expectation == Expectation.PROCESS) {
                    // we expected an error, and we got it...so that's a success
                    successCount.incrementAndGet();
                } else {
                    // processing error, but didn't expect it
                    System.out.println();
                    logger.log(Level.SEVERE, "processResource(" + file
                            + ") unexpected failure: ", x);

                    // continue processing the other files
                    ExampleProcessorException error =
                            new ExampleProcessorException(file, expectation, actual, x);
                    if (firstException == null) {
                        firstException = x;
                    }
                    throw error;
                }

            }
            finally {
                if (metrics != null) {
                    long processEnd = System.nanoTime();
                    long processTime = processEnd - start;
                    metrics.addProcessTime(processTime / DriverMetrics.NANOS_MS);
                }
            }
        }

        return actual;
    }

    /**
     * This function reads the contents of a mock resource from the specified file, then de-serializes that into a
     * Resource.
     *
     * @param fileName
     *            the name of the file containing the mock resource (e.g. "testdata/Patient1.json")
     * @param format
     * @return the de-serialized mock resource
     * @throws Exception
     */
    public Resource readResource(String fileName, Format format) throws Exception {

        // We don't really care about knowing the resource type. We can check this later
        long start = System.nanoTime();
        try (Reader reader = ExamplesUtil.resourceReader(fileName)) {
            return FHIRParser.parser(format).parse(reader);
        }
        finally {
            if (metrics != null) {
                metrics.addReadTime((System.nanoTime() - start) / DriverMetrics.NANOS_MS);
            }
        }
    }

    /**
     * Block until all the submitted requests are completed
     */
    private void waitForCompletion() {
        lock.lock();
        logger.info("Waiting for all requests to complete (remaining = " + this.currentlySubmittedCount + ")");
        try {
            while (this.currentlySubmittedCount > 0) {
                try {
                    runningCondition.await(1000, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException x) {
                    throw new IllegalStateException(x);
                }
            }
        }
        finally {
            lock.unlock();
            logger.info("All requests complete");
        }
    }

}
