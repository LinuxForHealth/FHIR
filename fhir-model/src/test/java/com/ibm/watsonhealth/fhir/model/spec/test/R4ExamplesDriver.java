/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.spec.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.parser.exception.FHIRParserException;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

/**
 * Run through all the examples from the R4 specification
 * @author rarnold
 *
 */
public class R4ExamplesDriver {
    private static final Logger logger = Logger.getLogger(R4ExamplesDriver.class.getName());
    // Location of the resource directory holding the examples. Note - these resources
    // will come from fhir-r4-spec-examples project, in case you're looking for them. We
    // do this to avoid rebuilding a huge jar full of static files every time.
    private static final String SPEC_DIR = "json";

    // All the examples which should pass validation
    private static final String ALL_FILE_INDEX = SPEC_DIR + "/all.txt";
    private static final String MINIMAL_FILE_INDEX = SPEC_DIR + "/minimal.txt";
    private static final String SPEC_FILE_INDEX = SPEC_DIR + "/spec.txt";

    // Call this processor for each of the examples, if given
    private IExampleProcessor processor;

    // Validate the resource
    private IExampleProcessor validator;

    // track some simple metrics
    private int testCount;
    private int successCount;
    Exception firstException = null;
    
    public static enum TestType {
        ALL, MINIMAL, SPEC
    }

    /**
     * Setter for the processor
     * @param p
     */
    public void setProcessor(IExampleProcessor p) {
        this.processor = p;
    }

    /**
     * Setter for the validation processor
     * @param p
     */
    public void setValidator(IExampleProcessor p) {
        this.validator = p;
    }

    /**
     * Use the minimal file set by default for our tests. This avoids issues with memory 
     * constraints on the build containers.
     * @throws Exception
     */
    public void processAllExamples() throws Exception {
        // Allow the build to override the default so we can really test everything
        String ttValue = System.getProperty(this.getClass().getName() + ".testType", TestType.MINIMAL.name());
        TestType tt = TestType.valueOf(ttValue);
        processExamples(tt);
    }
    
    /**
     * Process each of the examples we find in the SPEC_EXAMPLES path
     * @param testType select between all or minimal file sets for the test
     * @throws Exception
     */
    public void processExamples(TestType testType) throws Exception {
        String filename;
        switch (testType) {
        case ALL: 
            filename = ALL_FILE_INDEX;
            break;
        case MINIMAL: 
            filename = MINIMAL_FILE_INDEX;
            break;
        case SPEC: 
            filename = SPEC_FILE_INDEX;
            break;
        default:
            throw new IllegalArgumentException("shouldn't be necessary");
        }

        // filename will be a resource we read from the classpath
        processIndex(filename);
    }


    /**
     * Process the index file. Filename can be a resource on the classpath, or
     * a file from the file-system if prefixed with "file:..."
     * @param filename
     * @throws Exception
     */
    public void processIndex(String filename) throws Exception {
        // reset the state just in case we are called more than once
        this.firstException = null;
        this.testCount = 0;
        this.successCount = 0;

        long start = System.nanoTime();

        try {

            // Each line of this directory should be an example resource in json format
            try (BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream(filename), StandardCharsets.UTF_8))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length == 2) {
                        String expectation = tokens[0];
                        String example = tokens[1];
                        if (example.toUpperCase().endsWith(".JSON")) {
                            testCount++;
                            Expectation exp = Expectation.valueOf(expectation);

                            // all exception handling is delegated to the method, because
                            // it needs to take into account the expectation
                            processExample(SPEC_DIR + "/" + example, exp);
                        }
                    }
                }

            }

            // propagate the first exception so we fail the test
            if (firstException != null) {
                throw firstException;
            }
        }
        finally {
            if (testCount > 0) {
                long elapsed = (System.nanoTime() - start) / 1000000;

                // We count overall success if we successfully process the resource,
                // or if we got an expected exception earlier on
                logger.info("Overall success rate = " + successCount + "/" + testCount + " = " 
                        + (100*successCount / testCount) + "%. Took " + elapsed + " ms");
            }

        }
        
    }
    


    /**
     * Process the example file. If jsonFile is prefixed with "file:" then the file
     * will be read from the filesystem, otherwise it will be treated as a resource
     * on the classpath.
     * @param jsonFile
     */
    public void processExample(String jsonFile, Expectation expectation) throws Exception {
        System.out.println("Processing: " + jsonFile);
        Expectation actual;

        try {
            Resource resource = readResource(jsonFile);
            if (resource == null) {
                // this is bad, because we'd expect a FHIRParserException
                throw new AssertionError("readResource(" + jsonFile + ") returned null");
            }

            if (expectation == Expectation.PARSE) {
                // If we parsed the resource successfully but expected it to fail, it's a failed
                // test, so we don't try and process it any further
                actual = Expectation.OK;
                if (firstException == null) {
                    firstException = new FHIRParserException("Parse succeeded but should've failed", jsonFile, null);
                }
            }
            else {
                // validate and process the example
                actual = processExample(jsonFile, resource, expectation);
            }
        }
        catch (FHIRParserException fpx) {
            actual = Expectation.PARSE;
            if (expectation == Expectation.PARSE) {
                // successful test, even though we won't be able to validate/process
                successCount++;
            }
            else {
                // oops, hit an unexpected parse error
                logger.severe("readResource(" + jsonFile + ") unexpected failure: " + fpx.getMessage()
                + ", " + fpx.getPath());

                // continue processing the other files, but capture the first exception so we can fail the test
                // if needed
                if (firstException == null) {
                    firstException = fpx;
                }
            }
        }

        System.out.println(String.format("Processed: wanted:%11s got:%11s %s ", expectation.name(), actual.name(), jsonFile));

    }

    /**
     * Process the example resource
     * @param jsonFile so we know the name of the file if there's a problem
     * @param resource the parsed object
     */
    protected Expectation processExample(String jsonFile, Resource resource, Expectation expectation) throws Exception {
        Expectation actual = Expectation.OK;
        if (validator != null) {
            try {
                validator.process(jsonFile, resource);

                if (expectation == Expectation.VALIDATION) {
                    // this is a problem, because we expected validation to fail
                    resource = null; // prevent processing
                    logger.severe("validateResource(" + jsonFile + ") should've failed but didn't");
                    if (firstException == null) {
                        firstException = new FHIRParserException("Validation succeeded but should've failed", jsonFile, null);
                    }
                }
            }
            catch (Exception x) {
                // validation failed
                actual = Expectation.VALIDATION;
                resource = null; // stop any further processing

                if (expectation == Expectation.VALIDATION) {
                    // we expected an error, and we got it...so that's a success
                    successCount++;
                }
                else {
                    // oops, hit an unexpected validation error
                    logger.severe("validateResource(" + jsonFile + ") unexpected failure: " + x.getMessage());

                    // continue processing the other files
                    if (firstException == null) {
                        firstException = x;
                    }
                }
            }
        }

        // process the resource (as long as validation was successful
        if (processor != null && resource != null) {
            try {
                processor.process(jsonFile, resource);

                if (expectation == Expectation.PROCESS) {
                    // this is a problem, because we expected validation to fail
                    logger.severe("processResource(" + jsonFile + ") should've failed but didn't");
                    if (firstException == null) {
                        firstException = new FHIRParserException("Process succeeded but should've failed", jsonFile, null);
                    }
                }
                else {
                    // processed the resource successfully, and didn't expect an error
                    successCount++;
                }
            }
            catch (Exception x) {
                // say in which phase we failed
                actual = Expectation.PROCESS;

                if (expectation == Expectation.PROCESS) {
                    // we expected an error, and we got it...so that's a success
                    successCount++;
                }
                else {
                    // processing error, but didn't expect it
                    logger.log(Level.SEVERE, "processResource(" + jsonFile + ") unexpected failure: ", x);

                    // continue processing the other files
                    if (firstException == null) {
                        firstException = x;
                    }
                }

            }
        }

        return actual;
    }

    /**
     * This function reads the contents of a mock resource from the specified file, 
     * then de-serializes that into a Resource.
     * 
     * @param resourceClass
     *            the class associated with the resource type (e.g. Patient.class)
     * @param fileName
     *            the name of the file containing the mock resource (e.g. "testdata/Patient1.json")
     * @return the de-serialized mock resource
     * @throws FileNotFoundException
     * @throws JAXBException
     */
    public Resource readResource(String fileName) throws Exception {

        // We don't really care about knowing the resource type. We can check this later
        try (InputStream is = getInputStream(fileName)) {
            return FHIRParser.parser(Format.JSON).parse(is);
        }
    }
    
    /**
     * Read the given resource as an {@link InputStream}
     * @param resource
     * @return
     */
    protected InputStream getInputStream(String resource) throws IOException {
        InputStream result;
    
        if (resource.startsWith("file:")) {
            result = new FileInputStream(resource.substring(5));
        }
        else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            result = cl.getResourceAsStream(resource);
            if (result == null) {
                // Try the class's classloader instead
                result = getClass().getResourceAsStream(resource);
            }
            
            if (result == null) {
                throw new IllegalStateException("resource not found: " + resource);
            }
        }

        return result;
    }
    

}
