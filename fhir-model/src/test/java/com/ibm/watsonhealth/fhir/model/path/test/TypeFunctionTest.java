/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.test;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getSingleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.examples.ExamplesUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.TupleTypeInfo;
import com.ibm.watsonhealth.fhir.model.path.TupleTypeInfoElement;
import com.ibm.watsonhealth.fhir.model.path.TypeInfo;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.resource.Patient;

public class TypeFunctionTest {
    @Test
    public void testTypeFunction() throws Exception {
        Patient patient = FHIRParser.parser(Format.JSON).parse(ExamplesUtil.reader("json/spec/patient-example-a.json"));
        FHIRPathTree tree = FHIRPathTree.tree(patient);
        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator(tree).evaluate("Patient.contact.type()", tree.getRoot());
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