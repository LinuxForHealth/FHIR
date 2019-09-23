/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.xml.bind.JAXBException;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import com.ibm.fhir.core.FHIRUtilities;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Reference;

public class FHIRModelTestBase {
    protected static final String NL = System.getProperty("line.separator");
    public static boolean DEBUG_JSON = false;
    public static boolean DEBUG_XML = false;

    /**
     * This is a list of pre-defined locations that we'll search in when looking for a mock data file.
     */
    protected static String[] searchPaths = { "./", "src/test/resources/", "src/test/resources/testdata/", "src/main/resources/" };
    
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
    public static <T extends Resource> T readResource(Class<T> resourceClass, String fileName) throws Exception {

        // We'll use the filename suffix to determine the format that we're reading.
        Format fmt = (fileName.endsWith(".json") ? Format.JSON : Format.XML);

        // Open the file.
        

        // Deserialize the file contents.
        try (Reader reader = new InputStreamReader(resolveFileLocation(fileName), Charset.forName("UTF-8"))) {
            T resource = FHIRParser.parser(fmt).parse(reader);
            return resource;
        }
    }
    
    
    /**
     * @return
     */
    public static JsonObjectBuilder getEmptyBundleJsonObjectBuilder() {
        JsonObjectBuilder bundleObject = Json.createBuilderFactory(null).createObjectBuilder();
        bundleObject.add("resourceType", "Bundle")
            .add("type", "batch");
        return bundleObject;  
    }
    
    
    public static JsonObject getRequestJsonObject(String method, String url) {
        JsonObjectBuilder requestObject = Json.createBuilderFactory(null).createObjectBuilder();
        requestObject.add("method", method)
            .add("url", url);
        return requestObject.build();  
    }    
    
    
    /**
     * @param fileName
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static JsonObject readJsonObjectFromFile(String fileName) throws IOException, Exception {
        
        try (Reader reader = new InputStreamReader(resolveFileLocation(fileName), Charset.forName("UTF-8"))) {
            
            JsonReader jsonReader = Json.createReader(reader);
            return jsonReader.readObject();
            
        }
    }

    /**
     * Loads an Observation resource from the specified file, then associates it with
     * the specified patient via a subject attribute.
     */
    protected Observation buildObservation(String patientId, String fileName) throws Exception {
    	// TODO review Reference id
        Observation observation = readResource(Observation.class, fileName)
        		.toBuilder()
            .subject(Reference.builder().reference(string("Patient/" + patientId)).build())
            .build();
        return observation;
    } 
    
    /**
     * Returns an InputStream for the specified fileName after searching in a few pre-defined locations.
     */
    protected static InputStream resolveFileLocation(String fileName) throws Exception {

        // First, try to use the filename as-is.
        File f = new File(fileName);
        if (f.exists()) {
            return new FileInputStream(f);
        }

        // Otherwise, look in our configured search path, one directory at a time.
        for (String path : searchPaths) {
            f = new File(path + fileName);
            if (f.exists()) {
                return new FileInputStream(f);
            }
        }
        
        // If we didn't find the file yet, then look on the classpath.
        String resourceName = (fileName.startsWith("json/spec/") ? fileName : "json/spec/" + fileName);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        if (is != null) {
            return is;
        }

        throw new FileNotFoundException("File '" + fileName + "' was not found.");
    }
    

    /**
     * Asserts that the "expected" and "actual" resource instances are equivalent.
     * The comparison is performed in a "lenient" manner, meaning that we'll ensure that all the fields
     * contained in the "expected" resource are also contained in the "actual" resource.
     * 
     * @param expected the known-good resource to compare against
     * @param actual the resource to be validated.
     */
    protected void assertResourceEquals(Resource expected, Resource actual) {
        assertResourceEquals(null, expected, actual);
    }
    
    /**
     * Asserts that the "expected" and "actual" resource instances are equivalent.
     * The comparison is performed in a "lenient" manner, meaning that we'll ensure that all the fields
     * contained in the "expected" resource are also contained in the "actual" resource.
     * 
     * @param msg a string to be included in any error messages
     * @param expected the known-good resource to compare against
     * @param actual the resource to be validated.
     */
    protected void assertResourceEquals(String msg, Resource expected, Resource actual) {
        String cleanMsg = (msg != null ? msg : "");
        if (!expected.getClass().equals(actual.getClass())) {
            fail(cleanMsg + ": resource type mismatch, expected resource of type: " + expected.getClass() + ", but was of type: " + actual.getClass());
        }

        // Serialize the 'expected' resource.
        String jsonExpected = null;
        try {
            jsonExpected = FHIRUtilities.stripNewLineWhitespaceIfPresentInDiv(FHIRUtilities.stripNamespaceIfPresentInDiv(writeResource(expected, Format.JSON)));
        } catch (Throwable t) {
            fail(cleanMsg + ": error serializing expected resource: " + t);
        }
        
        // Serialize the 'actual' resource.
        String jsonActual = null;
        try {
            jsonActual = FHIRUtilities.stripNewLineWhitespaceIfPresentInDiv(FHIRUtilities.stripNamespaceIfPresentInDiv(writeResource(actual, Format.JSON)));
        } catch (Throwable t) {
            fail(cleanMsg + ": error serializing actual resource: " + t);
        }
        
        // Finally, make sure the expected and actual resources are equivalent.
        // Note that we'll do a "lenient" comparison which means we'll make sure
        // that all the fields contained in "expected" also exist in "actual".
        // The "actual" resource in the REST API response will likely contain a couple of additional
        // fields (e.g. "id" and "meta") so we can't do a "strict" comparison.
        try {
            JSONAssert.assertEquals(jsonExpected, jsonActual, false);
        } catch (JSONException e) {
            fail(cleanMsg + ": expected resource: \n" + jsonExpected + "\nbut actual resource was:\n" + jsonActual);
        }
    }
    
    /**
     * Reads a properties file containing test related properties used by subclasses.
     * @return Properties - A Properties object containing the contents of the test.properties file.
     * @throws Exception
     */
    protected static Properties readTestProperties(String fileName) throws Exception {
        Properties properties = new Properties();
        try (InputStream is = resolveFileLocation(fileName)) {
            properties.load(is);
            return properties;
        }
    }
    
    /**
     * Serializes the specified resource according to 'fmt' (JSON/XML).
     */
    public static <T extends Resource> String writeResource(T resource, Format fmt) throws FHIRException {
        StringWriter sw = new StringWriter();
        FHIRGenerator.generator( fmt, false).generate(resource, sw);
        return sw.toString();
    }

    /**
     * Serializes the specified resource according to 'fmt' (JSON/XML).
     */
    public static <T extends Resource> String writeResource(T resource, Format fmt, boolean prettyPrint) throws FHIRException {
        StringWriter sw = new StringWriter();
        FHIRGenerator.generator( fmt, prettyPrint).generate(resource, sw);
        return sw.toString();
    }
    
    /**
     * Reads a JSON object from the specified file.
     */
    protected JsonObject readJsonObject(String fileName) throws Exception {
        JsonReader reader = Json.createReader(resolveFileLocation(fileName));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        return jsonObject;
    }
    
    protected void printOutputToConsole(DomainResource res, Format f) {
        try {
            FHIRGenerator.generator( f, false).generate(res, System.out);
            
            System.out.println("");
        } catch (FHIRException e) {
        	// TODO logging?
            e.printStackTrace();
        }
    }

    protected String getDataAsString(DomainResource res, Format f) {
        StringWriter writer = new StringWriter();
        try {
            FHIRGenerator.generator( f, false).generate(res, writer);
        } catch (FHIRException e) {
        	// TODO logging?
            e.printStackTrace();
        }
        return writer.toString();
    }

    protected String stripNamespaceIfPresentInXML(String str) {
        String removenameSpace = str.replace(" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"", "");
        return removenameSpace.replace("xhtml:", "");
    }

    protected void runJSONTestForResource(DomainResource res, String filePath) {
        // DEBUG: Print output to console for manual testing/verification
        if (DEBUG_JSON) {
            printOutputToConsole(res, Format.JSON);
        }

        // Get the generated JSON for Patient resource
        String jsonActualString = FHIRUtilities.stripNamespaceIfPresentInDiv(getDataAsString(res, Format.JSON));

        // Read the expected JSON for Patient resource from control document
        String jsonExpectedString = FHIRUtilities.stripNamespaceIfPresentInDiv(readFromFile(filePath));

        // Check if the document matches with the control document and throw exceptions if they differ
        try {
            JSONAssert.assertEquals(FHIRUtilities.stripNewLineWhitespaceIfPresentInDiv(jsonExpectedString), FHIRUtilities.stripNewLineWhitespaceIfPresentInDiv(jsonActualString), true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void runXMLTestForResource(DomainResource res, String filePath) {
        // DEBUG: Print output to console for manual testing/verification
        if (DEBUG_XML) {
            printOutputToConsole(res, Format.XML);
        }

        // Get the generated XML for Patient resource
        String xmlActualString = FHIRUtilities.stripNamespaceIfPresentInDiv(stripNamespaceIfPresentInXML(getDataAsString(res, Format.XML)));

        // Read the expected XML for Patient resource from control document
        String xmlExpectedString = FHIRUtilities.stripNamespaceIfPresentInDiv(stripNamespaceIfPresentInXML(readFromFile(filePath)));

        // Check if the document matches with the control document and throw exceptions if they differ
        try {
            String controlXml = FHIRUtilities.stripNewLineWhitespaceIfPresentInDiv(xmlExpectedString);
            String testXml = FHIRUtilities.stripNewLineWhitespaceIfPresentInDiv(xmlActualString);
            
            // Attribute order is always ignored.
            Diff diff = DiffBuilder.compare(Input.fromString(controlXml).build())
                .withTest(Input.fromString(testXml).build())
                .normalizeWhitespace()
                .ignoreWhitespace()
                .ignoreComments()
                .build();
            
            assertFalse("pieces of XML are not similar ", diff.hasDifferences());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read data for a resource from a control document (JSON/XML file)
     */
    public static String readFromFile(String filePath) {
        StringBuffer buffer = new StringBuffer();
        try (InputStreamReader isr = new InputStreamReader(FHIRUtilities.class.getClassLoader().getResourceAsStream(filePath));
                BufferedReader in = new BufferedReader(isr)) {

            String line = null;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
                buffer.append(NL);
            }
            return buffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convenience function to create a fhir-model String from a {@link java.lang.String}
     * @param str
     * @return
     */
    public static com.ibm.fhir.model.type.String str2model(String str) {
        return com.ibm.fhir.model.type.String.of(str);
    }
}
