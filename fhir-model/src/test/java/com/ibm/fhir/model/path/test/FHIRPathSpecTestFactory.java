/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.model.path.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.testng.annotations.Factory;

import com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.util.XMLSupport;

/**
 * Executes all the FHIRPath tests shipped with the FHIRPath specification
 * @see <a href="http://build.fhir.org/ig/HL7/FHIRPath/branches/master/N1/tests.html">http://build.fhir.org/ig/HL7/FHIRPath/branches/master/N1/tests.html</a>
 */
public class FHIRPathSpecTestFactory {
    protected EvaluationContext observationContext, patientContext, questionnaireContext, valuesetContext;
    protected XMLStreamReader testFileReader;
    
    @Factory
    public Object[] executeTests() throws Exception {
        List<FHIRPathSpecTest> tests = new ArrayList<>();
        
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
                    String groupName = testFileReader.getAttributeValue(null, "name");
                    String groupDescription = testFileReader.getAttributeValue(null, "description");
                    StringBuilder groupMsg = new StringBuilder("Starting group " + groupName);
                    if (groupDescription != null) {
                        groupMsg.append(" (" + groupDescription + ")");
                    }
                    System.out.println(groupMsg);
                    break;
                case "test":
                    tests.add(createTest());
                    break;
                default:
                    break;
                }
            }
        }
        return tests.toArray();
    }
    
    private FHIRPathSpecTest createTest() throws Exception {
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
        
        return new FHIRPathSpecTest(testName, context, testExpression, outputs, "true".equals(isPredicate));
    }
}
