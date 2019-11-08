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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;

import com.ibm.fhir.model.path.FHIRPathNode;
import com.ibm.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.model.path.exception.FHIRPathException;

/**
 * Executes all the FHIRPath tests shipped with the FHIRPath specification
 * @see <a href="http://build.fhir.org/ig/HL7/FHIRPath/branches/master/N1/tests.html">http://build.fhir.org/ig/HL7/FHIRPath/branches/master/N1/tests.html</a>
 */
public class FHIRPathSpecTest implements ITest {
    protected final FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
    
    String testName;
    protected EvaluationContext context;
    TestExpression expression;
    List<ExpectedOutput> outputs;
    boolean isPredicate;
    
    public FHIRPathSpecTest(String testName, EvaluationContext context, TestExpression expression, List<ExpectedOutput> outputs) {
        this(testName, context, expression, outputs, false);
    }
    
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
                assertEquals(getStringValue(singleton(result)).string(), expectedOutput.text);
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
                assertEquals(result.as(FHIRPathQuantityNode.class).toString(), expectedOutput.text);
                break;
            case "string":
                assertEquals(getStringValue(singleton(result)).string(), expectedOutput.text);
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
}
