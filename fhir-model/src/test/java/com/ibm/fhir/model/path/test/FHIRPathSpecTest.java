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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamReader;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.path.FHIRPathNode;
import com.ibm.fhir.model.path.FHIRPathQuantityNode;
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
public class FHIRPathSpecTest {
    protected final FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
    protected EvaluationContext observationContext, patientContext, questionnaireContext, valuesetContext;
    protected XMLStreamReader testFileReader;
    
    @BeforeClass
    void setup() throws Exception {
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
    }
    
    @Test
    void executeTests() throws Exception {
        int totalCount = 0;
        int totalFailCount = 0;
        int groupCount = 0;
        int groupFailCount = 0;
        String groupName = null; 
        String groupDescription = null;
        
        boolean first = true;
        
        while (testFileReader.hasNext()) {
            
            switch (testFileReader.next()) {
            case XMLStreamReader.START_ELEMENT:
                String localName = testFileReader.getLocalName();
                switch (localName) {
                case "group":
                    if (!first) {
                        // pause real quick so group results are after relevant stack traces
                        TimeUnit.MILLISECONDS.sleep(1);
                        System.out.println(groupName + " result: " + (groupCount - groupFailCount) + " / " + groupCount);
                        System.out.println();
                        totalCount += groupCount;
                        totalFailCount += groupFailCount;
                        // reset the group counts
                        groupCount = 0;
                        groupFailCount = 0;
                    }
                    first = false;
                    
                    groupName = testFileReader.getAttributeValue(null, "name");
                    groupDescription = testFileReader.getAttributeValue(null, "description");
                    StringBuilder groupMsg = new StringBuilder("Starting group " + groupName);
                    if (groupDescription != null) {
                        groupMsg.append(" (" + groupDescription + ")");
                    }
                    System.out.println(groupMsg);
                    break;
                case "test":
                    try {
                        groupCount++;
                        executeTest();
                    } catch (FHIRPathException | AssertionError e) {
                        groupFailCount++;
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
                }
            }
        }
        // handle the last group
        totalCount += groupCount;
        totalFailCount += groupFailCount;
        TimeUnit.MILLISECONDS.sleep(1);
        System.out.println(groupName + " result: " + (groupCount - groupFailCount) + " / " + groupCount);
        System.out.println();
        
        // pause real quick so final results are at the bottom
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println("Final results: " + (totalCount - totalFailCount) + " / " + totalCount);
    }

    private void executeTest() throws Exception {
        String testName = testFileReader.getAttributeValue(null, "name");
        String inputfile = testFileReader.getAttributeValue(null, "inputfile");
        String isPredicate = testFileReader.getAttributeValue(null, "predicate");
        
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
        System.out.println(testName + ":  " + expression);
        
        Collection<FHIRPathNode> results = null;
        try {
            results = evaluator.evaluate(context, expression);
        } catch (FHIRPathException e) {
            if (invalidCode == null) {
                // unexpected error
                throw e;
            }
        } catch (Throwable t) {
            throw new IllegalStateException(testName + ": unexpected error while executing the expression", t);
        }
        
        List<String> outputs = new ArrayList<>();
        List<String> outputTypes = new ArrayList<>();
        
        a:while (testFileReader.hasNext()) {
            switch(testFileReader.next()) {
            case XMLStreamReader.START_ELEMENT:
                outputTypes.add(testFileReader.getAttributeValue(null, "type"));
                outputs.add(testFileReader.getElementText());
                break;
            case XMLStreamReader.END_ELEMENT:
                String localName2 = testFileReader.getLocalName();
                if ("test".equals(localName2)) {
                    break a;
                }
            }
        }
        
        if (invalidCode != null && results != null) {
            fail("Expected an exception for invalid expression[" + expression + "] but instead obtained " + results);
        } else if (results == null) {
            fail("Expected a valid result for expression[" + expression + "] but instead obtained null");
        }
        
        assertEquals(results.size(), outputs.size(), testName + ": number of results");

        if ("true".equals(isPredicate)) {
            // just verifying that we have the right number of results is enough
            return;
        }

        int i = 0;
        for (FHIRPathNode result : results) {
            switch(outputTypes.get(i)) {
            case "boolean":
                assertEquals(getBooleanValue(singleton(result))._boolean().toString(), outputs.get(i));
                break;
            case "code":
                assertEquals(getStringValue(singleton(result)).string(), outputs.get(i));
                break;
            case "date":
            case "dateTime":
                assertEquals(getDateTime(singleton(result)).toString(), outputs.get(i));
                break;
            case "decimal":
            case "integer":
                assertEquals(getNumberValue(singleton(result)).toString(), outputs.get(i));
                break;
            case "quantity":
                assertEquals(result.as(FHIRPathQuantityNode.class).toString(), outputs.get(i));
                break;
            case "string":
                assertEquals(getStringValue(singleton(result)).string(), outputs.get(i));
                break;
            case "time":
                assertEquals(getTime(singleton(result)).toString(), outputs.get(i));
                break;
            }
            i++;
        }
    }
}
