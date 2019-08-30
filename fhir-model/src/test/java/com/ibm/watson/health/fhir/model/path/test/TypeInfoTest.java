/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.model.path.ClassInfo;
import com.ibm.watson.health.fhir.model.path.ClassInfoElement;
import com.ibm.watson.health.fhir.model.path.FHIRPathType;
import com.ibm.watson.health.fhir.model.path.SimpleTypeInfo;
import com.ibm.watson.health.fhir.model.path.TupleTypeInfo;
import com.ibm.watson.health.fhir.model.path.TupleTypeInfoElement;
import com.ibm.watson.health.fhir.model.path.TypeInfo;
import com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil;
import com.ibm.watson.health.fhir.model.resource.Patient;

public class TypeInfoTest {
    @Test
    public void testSimpleTypeInfo() {
        TypeInfo actual = FHIRPathUtil.buildSimpleTypeInfo(FHIRPathType.SYSTEM_STRING);
        TypeInfo expected = new SimpleTypeInfo("System", "String", "System.Any");
        Assert.assertEquals(actual, expected);
    }
    
    @Test
    public void testClassInfo() {
        TypeInfo actual = FHIRPathUtil.buildClassInfo(FHIRPathType.FHIR_ADDRESS);
        List<ClassInfoElement> element = new ArrayList<>();
        element.add(new ClassInfoElement("use", "code"));
        element.add(new ClassInfoElement("type", "code"));
        element.add(new ClassInfoElement("text", "FHIR.string"));
        element.add(new ClassInfoElement("line", "List<FHIR.string>", false));
        element.add(new ClassInfoElement("city", "FHIR.string"));
        element.add(new ClassInfoElement("district", "FHIR.string"));
        element.add(new ClassInfoElement("state", "FHIR.string"));
        element.add(new ClassInfoElement("postalCode", "FHIR.string"));
        element.add(new ClassInfoElement("country", "FHIR.string"));
        element.add(new ClassInfoElement("period", "Period"));
        TypeInfo expected = new ClassInfo("FHIR", "Address", "FHIR.Element", element);
        Assert.assertEquals(actual, expected);
    }
    
    @Test
    public void testTupleTypeInfo() {
        TypeInfo actual = FHIRPathUtil.buildTupleTypeInfo(Patient.Contact.class);
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