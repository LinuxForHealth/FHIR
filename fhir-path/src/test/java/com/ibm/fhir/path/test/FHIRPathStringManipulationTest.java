/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.testng.Assert.assertEquals;
import static com.ibm.fhir.path.util.FHIRPathUtil.getStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.getIntegerValue;

import java.io.Reader;
import java.util.Collection;

import org.testng.annotations.Test;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Appointment;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;

public class FHIRPathStringManipulationTest {
    private static final Appointment appointment = readAppointment();

    @Test
    public void testContainsFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.contains('proposed')");
        assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testContainsFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.contains('random')");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testContainsFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.contains('random')");
    }
    
    @Test
    public void testSubstringFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.substring(3)");
        assertEquals(getStringValue(result).toString(), "posed");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testSubstringFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.substring(3)");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testSubstringFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.substring(3)");
    }
    
    @Test
    public void testStartsWithFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.startsWith('pro')");
        assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testStartsWithFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.startsWith('random')");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testStartsWithFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.startsWith('random')");
    }
    
    @Test
    public void testEndsWithFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.endsWith('ed')");
        assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testEndsWithFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.endsWith('random')");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testEndsWithFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.endsWith('random')");
    }
    
    @Test
    public void testReplaceFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.replace('proposed', 'booked')");
        assertEquals(getStringValue(result).toString(), "booked");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testReplaceFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.replace('random1', 'random2')");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testReplaceFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.replace('random1', 'random2')");
    }
    
    @Test
    public void testMatchesFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.matches('.*')");
        assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testMatchesFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.matches('.*')");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testMatchesFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.matches('.*')");
    }
    
    @Test
    public void testReplaceMatchesFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.replaceMatches('.', 'a')");
        assertEquals(getStringValue(result).toString(), "aaaaaaaa");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testReplaceMatchesFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.replaceMatches('.*', 'booked')");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testReplaceMatchesFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.replaceMatches('.*', 'booked')");
    }
    
    @Test
    public void testLengthFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.length()");
        assertEquals(getIntegerValue(result).toString(), "8");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testLengthFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.length()");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection item must be of type String, but found 'Identifier'*" )
    public void testLengthFunctionForCollectionWithNonStringType() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.identifier.length()");
    }
    
    @Test
    public void testToStringFunctionForCollectionWithString() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(appointment, "Appointment.status.toString()");
        assertEquals(getStringValue(result).toString(), "proposed");
    }
    
    @Test(expectedExceptions = FHIRPathException.class, expectedExceptionsMessageRegExp = ".*Input collection must not contain more than one item.*" )
    public void testToStringFunctionForCollectionWithMultipleItems() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        evaluator.evaluate(appointment, "Appointment.participant.toString()");
    }
    
   
    private static Appointment readAppointment() {
        try (Reader reader = ExamplesUtil.resourceReader("json/spec/appointment-example-request.json")) {
            return FHIRParser.parser(Format.JSON).parse(reader);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
