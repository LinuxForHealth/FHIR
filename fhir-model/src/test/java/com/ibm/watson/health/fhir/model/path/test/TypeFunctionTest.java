/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.test;

import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.getSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.examples.ExamplesUtil;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.path.FHIRPathNode;
import com.ibm.watson.health.fhir.model.path.TupleTypeInfo;
import com.ibm.watson.health.fhir.model.path.TupleTypeInfoElement;
import com.ibm.watson.health.fhir.model.path.TypeInfo;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.watson.health.fhir.model.resource.Patient;

public class TypeFunctionTest {
    @Test
    public void testTypeFunction() throws Exception {
        Patient patient = FHIRParser.parser(Format.JSON).parse(ExamplesUtil.reader("json/spec/patient-example-a.json"));
        EvaluationContext evaluationContext = new EvaluationContext(patient);
        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext, "Patient.contact.type()");
        TypeInfo actual = getSingleton(result).asTypeInfoNode().typeInfo();
        List<TupleTypeInfoElement> element = new ArrayList<>();
        element.add(new TupleTypeInfoElement("relationship", "List<FHIR.CodeableConcept>", false));
        element.add(new TupleTypeInfoElement("name", "FHIR.HumanName"));
        element.add(new TupleTypeInfoElement("telecom", "List<FHIR.ContactPoint>", false));
        element.add(new TupleTypeInfoElement("address", "FHIR.Address"));
        element.add(new TupleTypeInfoElement("gender", "FHIR.code"));
        element.add(new TupleTypeInfoElement("organization", "FHIR.Reference"));
        element.add(new TupleTypeInfoElement("period", "FHIR.Period"));
        TypeInfo expected = new TupleTypeInfo(element);
        Assert.assertEquals(actual, expected);
    }
}