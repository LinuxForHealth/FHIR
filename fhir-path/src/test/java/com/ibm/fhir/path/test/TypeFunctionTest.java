/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.path.util.FHIRPathUtil.getSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.TupleTypeInfo;
import com.ibm.fhir.path.TupleTypeInfoElement;
import com.ibm.fhir.path.TypeInfo;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class TypeFunctionTest {
    @Test
    public void testTypeFunction() throws Exception {
        Patient patient = FHIRParser.parser(Format.JSON).parse(ExamplesUtil.resourceReader("json/spec/patient-example-a.json"));
        EvaluationContext evaluationContext = new EvaluationContext(patient);
        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext, "Patient.contact.type()");
        TypeInfo actual = getSingleton(result).asTypeInfoNode().typeInfo();
        List<TupleTypeInfoElement> element = new ArrayList<>();
        element.add(new TupleTypeInfoElement("relationship", "FHIR.CodeableConcept", false));
        element.add(new TupleTypeInfoElement("name", "FHIR.HumanName", true));
        element.add(new TupleTypeInfoElement("telecom", "FHIR.ContactPoint", false));
        element.add(new TupleTypeInfoElement("address", "FHIR.Address", true));
        element.add(new TupleTypeInfoElement("gender", "FHIR.code", true));
        element.add(new TupleTypeInfoElement("organization", "FHIR.Reference", true));
        element.add(new TupleTypeInfoElement("period", "FHIR.Period", true));
        TypeInfo expected = new TupleTypeInfo(element);
        Assert.assertEquals(actual, expected);
    }
}