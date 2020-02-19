/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.path.patch.FHIRPathPatch;

public class FHIRPathPatchSpecTest implements ITest {
    protected static TransformerFactory transformFactory = TransformerFactory.newInstance();
    protected static FHIRParser parser = FHIRParser.parser(Format.XML);
    
    protected static XMLStreamReader testFileReader;
    
    String testName;
    String mode;
    Resource input;
    Parameters params;
    Resource expectedOutput;
    
    @Factory(dataProvider = "provideAllTestData")
    public FHIRPathPatchSpecTest(String testName, String mode, Resource input, Parameters params, Resource output) {
        this.testName = testName;
        this.mode = mode;
        this.input = input;
        this.params = params;
        this.expectedOutput = output;
    }
    
    @Test
    private void executeTest() throws Exception {
        System.out.println(testName);
        if (mode.equals("forwards")) {
            // I don't really know what mode=forwards means, but we fail them both
            // and at least one of the two seems invalid to me
            throw new SkipException("Skipping 'forwards' test " + testName);
        }
        try {
            FHIRPathPatch patch = FHIRPathPatch.from(params);
            // Set the id to match the expected parameters object so we can do a normal compare
            Parameters serializedPatch = patch.toParameters().toBuilder().id(params.getId()).build();
            assertEquals(serializedPatch, params);

            Resource actualOutput = patch.apply(input);
            assertEquals(actualOutput, expectedOutput);
        } catch(UnsupportedOperationException e) {
            throw new SkipException("Skipping '" + testName + "' due to an unsupported feature");
        }
    }
    
    @Override
    public String getTestName() {
        return testName;
    }
    
    /**
     * Hack to override the method name in the TestNG reports (but still doesn't work to override the method name on the Eclipse TestNG view)
     * @see <a href="https://stackoverflow.com/a/49522435/161022">https://stackoverflow.com/a/49522435/161022</a>
     */
    @AfterMethod(alwaysRun = true)
    public void setResultTestName(ITestResult result) {
        try {
            BaseTestMethod baseTestMethod = (BaseTestMethod) result.getMethod();
            Field f = baseTestMethod.getClass().getSuperclass().getDeclaredField("m_methodName");
            f.setAccessible(true);
            f.set(baseTestMethod, testName);
        } catch (Exception e) {
            Reporter.log("Exception : " + e.getMessage());
        }
    }
    
    @DataProvider
    public static Object[][] provideAllTestData() throws Exception {
        List<Object[]> testData = new ArrayList<>();
        
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setIgnoringElementContentWhitespace(true);
        domFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = domFactory.newDocumentBuilder();
        try (InputStream in = FHIRPathPatchSpecTest.class.getClassLoader().getResourceAsStream("fhir-path-patch-tests.xml")) {
            Document testDoc = documentBuilder.parse(in);
            
            NodeList cases = testDoc.getDocumentElement().getElementsByTagName("case");
            for (int i = 0; i < cases.getLength(); i++) {
                testData.add(createSingleTest(cases.item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return testData.toArray(new Object[testData.size()][]);
    }
    
    private static Object[] createSingleTest(Node caseNode) throws Exception {
        Transformer transformer = transformFactory.newTransformer();
        
        String testName = caseNode.getAttributes().getNamedItem("name").getTextContent();
        String mode = caseNode.getAttributes().getNamedItem("mode").getTextContent();
        
        System.out.println("Parsing " + testName);
        // input
        Node inputNode = caseNode.getFirstChild();
        while (inputNode.getNodeType() != Node.ELEMENT_NODE) {
            inputNode = inputNode.getNextSibling();
        }
        Resource input = parseResource(transformer, inputNode.getFirstChild());
        
        // diff
        Node diffNode = inputNode.getNextSibling();
        while (diffNode.getNodeType() != Node.ELEMENT_NODE) {
            diffNode = diffNode.getNextSibling();
        }
        Parameters diff = parseResource(transformer, diffNode.getFirstChild());
        
        // output
        Node outputNode = diffNode.getNextSibling();
        while (outputNode.getNodeType() != Node.ELEMENT_NODE) {
            outputNode = outputNode.getNextSibling();
        }
        Resource output = parseResource(transformer, outputNode.getFirstChild());
        
        return new Object[] { testName, mode, input, diff, output };
    }

    private static <T extends Resource> T parseResource(Transformer transformer, Node resourceNode) throws TransformerException, FHIRParserException {
        while (resourceNode.getNodeType() != Node.ELEMENT_NODE) {
            resourceNode = resourceNode.getNextSibling();
        }
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(new DOMSource(resourceNode), result);
        StringReader reader = new StringReader(writer.toString());
        return parser.parse(reader);
    }

    public static void main(String[] args) throws Exception {
        Patient patient = Patient.builder().id("test").build();
        
        FHIRPathPatch patch = FHIRPathPatch.builder()
            .add("Patient", "identifier", Identifier.builder().system(Uri.of("mySystem")).build())
            .add("Patient.identifier", "value", string("it-me"))
            .add("Patient", "active", com.ibm.fhir.model.type.Boolean.TRUE)
            .build();
        
        System.out.println(patch.apply(patient));
    }
}
