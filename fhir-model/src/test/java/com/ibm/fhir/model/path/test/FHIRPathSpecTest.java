/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.model.path.test;

import static com.ibm.fhir.model.path.util.FHIRPathUtil.getBooleanValue;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.getDateTime;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.getNumberValue;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.getStringValue;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.getTime;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.singleton;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;

import com.ibm.fhir.model.path.FHIRPathNode;
import com.ibm.fhir.model.path.FHIRPathQuantityValue;
import com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.model.path.exception.FHIRPathException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.util.XMLSupport;

/**
 * Executes all the FHIRPath tests shipped with the FHIRPath specification
 * @see <a href="http://build.fhir.org/ig/HL7/FHIRPath/branches/master/N1/tests.html">http://build.fhir.org/ig/HL7/FHIRPath/branches/master/N1/tests.html</a>
 */
public class FHIRPathSpecTest implements ITest {
    protected final FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
    protected static EvaluationContext observationContext, patientContext, questionnaireContext, valuesetContext;
    protected static XMLStreamReader testFileReader;
    
    String testName;
    protected EvaluationContext context;
    TestExpression expression;
    List<ExpectedOutput> outputs;
    boolean isPredicate;
    
    public FHIRPathSpecTest(String testName, EvaluationContext context, TestExpression expression, List<ExpectedOutput> outputs) {
        this(testName, context, expression, outputs, false);
    }
    
    @Factory(dataProvider = "provideAllTestData")
    public FHIRPathSpecTest(String testName, EvaluationContext context, TestExpression expression, List<ExpectedOutput> outputs, boolean isPredicate) {
        this.testName = testName;
        this.context = context;
        this.expression = expression;
        this.outputs = outputs;
        this.isPredicate = isPredicate;
    }
    
    @Test
    private void executeTest() throws Exception {
        System.out.println(testName + ":  " + expression.text);
        
        Collection<FHIRPathNode> results = null;
        try {
            results = evaluator.evaluate(context, expression.text);
        } catch (FHIRPathException e) {
            if (!expression.isInvalid()) {
                
                Throwable cause = e.getCause();
                if (cause instanceof UnsupportedOperationException) {
                    throw new SkipException("skipping test of unsupported operation: " + cause.getMessage());
                } else if (cause instanceof IllegalArgumentException && cause.getMessage() != null &&
                        cause.getMessage().startsWith("Function") && cause.getMessage().endsWith("not found") ) {
                    throw new SkipException("skipping test of unsupported operation: " + cause.getMessage());
                }
                // unexpected error
                throw e;
            }
        } catch (Throwable t) {
            throw new IllegalStateException(testName + ": unexpected error while executing the expression", t);
        }
        
        if (expression.isInvalid() && results != null) {
            fail("Expected an exception for invalid expression[" + expression + "] but instead obtained " + results);
        } else if (results == null) {
            fail("Expected a valid result for expression[" + expression + "] but instead obtained null");
        }
        
        assertEquals(results.size(), outputs.size(), testName + ": number of results");

        if (isPredicate) {
            // just verifying that we have the right number of results is enough
            return;
        }

        int i = 0;
        for (FHIRPathNode result : results) {
            ExpectedOutput expectedOutput = outputs.get(i++);
            
            switch(expectedOutput.type) {
            case "boolean":
                assertEquals(getBooleanValue(singleton(result))._boolean().toString(), expectedOutput.text);
                break;
            case "code":
                assertEquals(getStringValue(singleton(result)).toString(), expectedOutput.text);
                break;
            case "date":
            case "dateTime":
                assertEquals(getDateTime(singleton(result)).toString(), expectedOutput.text);
                break;
            case "decimal":
            case "integer":
                assertEquals(getNumberValue(singleton(result)).toString(), expectedOutput.text);
                break;
            case "quantity":
                assertEquals(result.as(FHIRPathQuantityValue.class).toString(), expectedOutput.text);
                break;
            case "string":
                assertEquals(getStringValue(singleton(result)).toString(), expectedOutput.text);
                break;
            case "time":
                assertEquals(getTime(singleton(result)).toString(), expectedOutput.text);
                break;
            }
        }
    }
    
    static class TestExpression {
        final String text;
        final String invalidCode;
        
        public TestExpression(String expression) {
            this(expression, null);
        }
        
        public TestExpression(String expression, String invalidCode) {
            this.text = expression;
            this.invalidCode = invalidCode;
        }
        
        boolean isInvalid() {
            return invalidCode != null;
        }
    }
    
    static class ExpectedOutput {
        final String type;
        final String text;
        
        public ExpectedOutput(String type, String text) {
            this.type = type;
            this.text = text;
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
        
        Resource observation = TestUtil.readLocalResource("FHIRPath/input/observation-example.xml");
        Resource patient = TestUtil.readLocalResource("FHIRPath/input/patient-example.xml");
        Resource questionnaire = TestUtil.readLocalResource("FHIRPath/input/questionnaire-example.xml");
        Resource valueset = TestUtil.readLocalResource("FHIRPath/input/valueset-example-expansion.xml");
        observationContext = new EvaluationContext(observation);
        patientContext = new EvaluationContext(patient);
        questionnaireContext = new EvaluationContext(questionnaire);
        valuesetContext = new EvaluationContext(valueset);
        
        InputStream testFile = TestUtil.resolveFileLocation("FHIRPath/tests-fhir-r4.xml");
        testFileReader = XMLSupport.createXMLStreamReader(testFile);
        
        while (testFileReader.hasNext()) {
            
            switch (testFileReader.next()) {
            case XMLStreamReader.START_ELEMENT:
                String localName = testFileReader.getLocalName();
                switch (localName) {
                case "group":                  
                    /*
                    String groupName = testFileReader.getAttributeValue(null, "name");
                    String groupDescription = testFileReader.getAttributeValue(null, "description");
                    // TODO: use the groupName to define a TestNG group (or suite)
                    StringBuilder groupMsg = new StringBuilder("Starting group " + groupName);
                    if (groupDescription != null) {
                       groupMsg.append(" (" + groupDescription + ")");
                    }
                    System.out.println(groupMsg);
                    */
                    break;
                case "test":
                    testData.add(createSingleTest());
                    break;
                default:
                    break;
                }
            }
        }
        return testData.toArray(new Object[testData.size()][]);
    }
    
    private static Object[] createSingleTest() throws Exception {
        String testName = testFileReader.getAttributeValue(null, "name");
        String isPredicate = testFileReader.getAttributeValue(null, "predicate");
        
        String inputfile = testFileReader.getAttributeValue(null, "inputfile");
        EvaluationContext context;
        switch(inputfile) {
        case "observation-example.xml":
            context = observationContext;
            break;
        case "patient-example.xml":
            context = patientContext;
            break;
        case "questionnaire-example.xml":
            context = questionnaireContext;
            break;
        case "valueset-example-expansion.xml":
            context = valuesetContext;
            break;
        default:
            throw new IllegalStateException("unknown input file:" + inputfile);
        }
        
        testFileReader.nextTag();
        String invalidCode = testFileReader.getAttributeValue(null, "invalid");
        String expression = testFileReader.getElementText();
        FHIRPathSpecTest.TestExpression testExpression = new FHIRPathSpecTest.TestExpression(expression, invalidCode);
        
        List<FHIRPathSpecTest.ExpectedOutput> outputs = new ArrayList<>();
        a:while (testFileReader.hasNext()) {
            
            switch(testFileReader.next()) {
            case XMLStreamReader.START_ELEMENT:
                String type = testFileReader.getAttributeValue(null, "type");
                String text = testFileReader.getElementText();
                outputs.add(new FHIRPathSpecTest.ExpectedOutput(type, text));
                break;
            case XMLStreamReader.END_ELEMENT:
                String localName = testFileReader.getLocalName();
                if ("test".equals(localName)) {
                    break a;
                }
            }
        }
        
        return new Object[] {testName, context, testExpression, outputs, "true".equals(isPredicate)};
    }
}
