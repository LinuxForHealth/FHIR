/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import java.io.BufferedReader;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Run through all the examples from the R4 specification
 * 
 * @author rarnold
 *
 */
public class R4ExamplesDriver {
    private static final Logger logger = Logger.getLogger(R4ExamplesDriver.class.getName());
    // Location of the resource directory holding the examples. Note - these resources
    // will come from fhir-r4-spec-examples project, in case you're looking for them. We
    // do this to avoid rebuilding a huge jar full of static files every time.
    private static final String JSON_PATH = "json";
    private static final String XML_PATH = "xml";

    // All the examples which should pass validation
    private static final String ALL_FILE_INDEX = "/all.txt";
    private static final String MINIMAL_FILE_INDEX = "/minimal.txt";
    private static final String SPEC_FILE_INDEX = "/spec.txt";
    private static final String IBM_FILE_INDEX = "/ibm.txt";

    // Call this processor for each of the examples, if given
    private IExampleProcessor processor;

    // Validate the resource
    private IExampleProcessor validator;

    // track some simple metrics
    private int testCount;
    private int successCount;
    Exception firstException = null;

    public static enum TestType {
        ALL("ALL"), MINIMAL("MINIMAL"), SPEC("SPEC"), IBM("IBM");
        
        private String type; 
        
        TestType(String type){
            this.type = type;
        }
        
        public String getType() {
            return type;
        }
    }

    /**
     * Setter for the processor
     * 
     * @param p
     */
    public void setProcessor(IExampleProcessor p) {
        this.processor = p;
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
     * Use the minimal file set by default for our tests. This avoids issues with memory constraints on the build
     * containers.
     * 
     * @throws Exception
     */
    public void processAllExamples() throws Exception {
        // Allow the build to override the default so we can really test everything
        String ttValue = System.getProperty(this.getClass().getName()
                + ".testType", TestType.MINIMAL.toString());
        String ttFormat =
                System.getProperty(this.getClass().getName() + ".format", Format.JSON.toString());
        TestType tt = TestType.valueOf(ttValue);
        Format format = Format.valueOf(ttFormat);
        processExamples(tt, format);
    }

    /**
     * Process each of the examples we find in the SPEC_EXAMPLES path
     * 
     * @param testType
     *            select between all or minimal file sets for the test
     * @param format 
     * @throws Exception
     */
    public void processExamples(TestType testType, Format format) throws Exception {
        String dir;
        switch (format) {
        case JSON:
            dir = JSON_PATH;
            break;
        case XML:
            dir = XML_PATH;
            break;
        default:
            throw new IllegalArgumentException("Format '" + testType + "' is not supported.");
        }

        String filename;
        switch (testType) {
        case ALL:
            filename = dir + ALL_FILE_INDEX;
            break;
        case MINIMAL:
            filename = dir + MINIMAL_FILE_INDEX;
            break;
        case SPEC:
            filename = dir + SPEC_FILE_INDEX;
            break;
        case IBM:
            filename = dir + IBM_FILE_INDEX;
            break;
        default:
            throw new IllegalArgumentException("shouldn't be necessary");
        }

        // filename will be a resource we read from the classpath
        processIndex(filename, format);
    }

    public void processIndex(String filename) throws Exception {
        processIndex(filename, Format.JSON);
    }

    /**
     * Process the index file. Filename can be a resource on the classpath, or a file from the file-system if prefixed
     * with "file:..."
     * 
     * @param filename
     * @param format 
     * 
     * @throws Exception
     */
    public void processIndex(String filename, Format format) throws Exception {
        logger.info(String.format("Processing index file '%s' with format '%s'", filename, format.toString()));
        // reset the state just in case we are called more than once
        this.firstException = null;
        this.testCount = 0;
        this.successCount = 0;

        long start = System.nanoTime();

        List<ExampleProcessorException> errors = new ArrayList<>();
        try {
            // Each line of this directory should be an example resource in json format
            try (BufferedReader br = new BufferedReader(ExamplesUtil.reader(filename))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length == 2) {
                        String expectation = tokens[0];
                        String example = tokens[1];
                        if (example.toUpperCase().endsWith(".JSON") && format == Format.JSON) {
                            testCount++;
                            Expectation exp = Expectation.valueOf(expectation);

                            try {
                                processExample(JSON_PATH + "/" + example, format, exp);
                            } catch (ExampleProcessorException e) {
                                errors.add(e);
                            }
                        } else if (example.toUpperCase().endsWith(".XML") && format == Format.XML) {
                            testCount++;
                            Expectation exp = Expectation.valueOf(expectation);

                            try {
                                processExample(XML_PATH + "/" + example, format, exp);
                            } catch (ExampleProcessorException e) {
                                errors.add(e);
                            }
                        }
                    }
                }

            }

            // propagate the first exception so we fail the test
            if (firstException != null) {
                throw firstException;
            }
        } finally {
            if (testCount > 0) {
                long elapsed = (System.nanoTime() - start) / 1000000;

                // Just for formatting
                System.out.println();

                // We count overall success if we successfully process the resource,
                // or if we got an expected exception earlier on
                logger.info("Overall success rate = " + successCount + "/" + testCount + " = "
                        + (100 * successCount / testCount) + "%. Took " + elapsed + " ms");
            }
            for (ExampleProcessorException error : errors) {
                logger.warning(error.toString());
            }
        }
    }

    public void processExample(String file, Expectation expectation)
        throws ExampleProcessorException {
        processExample(file, Format.JSON, expectation);
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
            System.out.print("."); // So we know its not stalled
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
                successCount++;
            } else {
                // oops, hit an unexpected parse error
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
            try {
                validator.process(file, resource);

                if (expectation == Expectation.VALIDATION) {
                    // this is a problem, because we expected validation to fail
                    resource = null; // prevent processing
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
                    successCount++;
                } else {
                    // oops, hit an unexpected validation error
                    logger.severe("validateResource(" + file + ") unexpected failure: "
                            + x.getMessage());

                    // continue processing the other files
                    ExampleProcessorException error =
                            new ExampleProcessorException(file, expectation, actual, x);
                    if (firstException == null) {
                        firstException = x;
                    }
                    throw error;
                }
            }
        }

        // process the resource (as long as validation was successful
        if (processor != null && resource != null) {
            try {
                processor.process(file, resource);

                if (expectation == Expectation.PROCESS) {
                    // this is a problem, because we expected validation to fail
                    logger.severe("processResource(" + file + ") should've failed but didn't");
                    ExampleProcessorException error =
                            new ExampleProcessorException(file, expectation, actual);
                    if (firstException == null) {
                        firstException = error;
                    }
                    throw error;
                } else {
                    // processed the resource successfully, and didn't expect an error
                    successCount++;
                }
            } catch (Exception x) {
                // say in which phase we failed
                actual = Expectation.PROCESS;

                if (expectation == Expectation.PROCESS) {
                    // we expected an error, and we got it...so that's a success
                    successCount++;
                } else {
                    // processing error, but didn't expect it
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
        try (Reader reader = ExamplesUtil.reader(fileName)) {
            return FHIRParser.parser(format).parse(reader);
        }
    }

}
