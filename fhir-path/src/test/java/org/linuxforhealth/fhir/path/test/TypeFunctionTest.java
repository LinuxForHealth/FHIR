/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.test;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.examples.ExamplesUtil;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.TupleTypeInfo;
import org.linuxforhealth.fhir.path.TupleTypeInfoElement;
import org.linuxforhealth.fhir.path.TypeInfo;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

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