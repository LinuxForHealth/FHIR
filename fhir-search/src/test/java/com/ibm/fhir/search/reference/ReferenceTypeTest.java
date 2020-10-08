/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.reference;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.reference.value.CanonicalUrlHandlerImpl;
import com.ibm.fhir.search.reference.value.IdHandlerImpl;
import com.ibm.fhir.search.reference.value.TypeIdHandlerImpl;
import com.ibm.fhir.search.reference.value.UrlHandlerImpl;

public class ReferenceTypeTest {
    @Test
    public void testIdHandler() {
        String incoming = "https://localhost:9443/fhir-server/api/v4";
        List<String> targets = Arrays.asList("Patient", "Group");
        List<QueryParameterValue> parameterValues = new ArrayList<>();
        List<String> values = new ArrayList<>();
        String valueString = "example";
        IdHandlerImpl impl = new IdHandlerImpl();
        impl.processParameter(incoming, targets, parameterValues, valueString, values);

        assertEquals(parameterValues.size(), 4);
        List<String> checkList = parameterValues.stream().map(m -> m.getValueString()).collect(Collectors.toList());
        assertTrue(checkList.contains("Patient/example"));
        assertTrue(checkList.contains("Group/example"));
        assertTrue(checkList.contains("https://localhost:9443/fhir-server/api/v4/Patient/example"));
        assertTrue(checkList.contains("https://localhost:9443/fhir-server/api/v4/Group/example"));
    }

    @Test
    public void testTypeIdHandlerNoOp() {
        String incoming = "https://localhost:9443/fhir-server/api/v4";
        List<String> targets = Arrays.asList("Patient", "Group");
        List<QueryParameterValue> parameterValues = new ArrayList<>();
        List<String> values = new ArrayList<>();

        // This does not match the Type/Id Reference Type
        String valueString = "example";
        TypeIdHandlerImpl impl = new TypeIdHandlerImpl();
        impl.processParameter(incoming, targets, parameterValues, valueString, values);
        assertEquals(parameterValues.size(), 0);
    }

    @Test
    public void testTypeIdHandler() {
        String incoming = "https://localhost:9443/fhir-server/api/v4";
        List<String> targets = Arrays.asList("Patient", "Group");
        List<QueryParameterValue> parameterValues = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String valueString = "Patient/example";
        TypeIdHandlerImpl impl = new TypeIdHandlerImpl();
        impl.processParameter(incoming, targets, parameterValues, valueString, values);
        List<String> checkList = parameterValues.stream().map(m -> m.getValueString()).collect(Collectors.toList());

        // There is only one, as the valueString is the visible value, and this is the hidden.
        assertEquals(parameterValues.size(), 1);
        assertTrue(checkList.contains("https://localhost:9443/fhir-server/api/v4/Patient/example"));
        assertTrue(parameterValues.get(0).isHidden());
    }

    @Test
    public void testUrlHandlerNoOpWithId() {
        String incoming = "https://localhost:9443/fhir-server/api/v4";
        List<String> targets = Arrays.asList("Patient", "Group");
        List<QueryParameterValue> parameterValues = new ArrayList<>();
        List<String> values = new ArrayList<>();

        // This does not match the Type/Id Reference Type
        String valueString = "example";
        UrlHandlerImpl impl = new UrlHandlerImpl();
        impl.processParameter(incoming, targets, parameterValues, valueString, values);
        assertEquals(parameterValues.size(), 0);
    }

    @Test
    public void testUrlHandlerNoOpWithTypeId() {
        String incoming = "https://localhost:9443/fhir-server/api/v4";
        List<String> targets = Arrays.asList("Patient", "Group");
        List<QueryParameterValue> parameterValues = new ArrayList<>();
        List<String> values = new ArrayList<>();

        // This does not match the Type/Id Reference Type
        String valueString = "Patient/example";
        UrlHandlerImpl impl = new UrlHandlerImpl();
        impl.processParameter(incoming, targets, parameterValues, valueString, values);
        assertEquals(parameterValues.size(), 0);
    }

    @Test
    public void testUrlHandler() {
        String incoming = "https://localhost:9443/fhir-server/api/v4";
        List<String> targets = Arrays.asList("Patient", "Group");
        List<QueryParameterValue> parameterValues = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String valueString = "https://localhost:9443/fhir-server/api/v4/Patient/example";
        UrlHandlerImpl impl = new UrlHandlerImpl();
        impl.processParameter(incoming, targets, parameterValues, valueString, values);
        List<String> checkList = parameterValues.stream().map(m -> m.getValueString()).collect(Collectors.toList());

        // There is only one, as the valueString is the visible value, and this is the hidden.
        assertEquals(parameterValues.size(), 1);
        assertTrue(checkList.contains("Patient/example"));
        assertTrue(parameterValues.get(0).isHidden());
    }

    @Test
    public void testCanonicalUrlHandler() {
        String incoming = "https://localhost:9443/fhir-server/api/v4";
        List<String> targets = Arrays.asList("Patient", "Group");
        List<QueryParameterValue> parameterValues = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String valueString = "https://localhost:9443/fhir-server/api/v4/Patient/example|1.0.0#vs1";
        CanonicalUrlHandlerImpl impl = new CanonicalUrlHandlerImpl();
        impl.processParameter(incoming, targets, parameterValues, valueString, values);
        List<String> checkList = parameterValues.stream().map(m -> m.getValueString()).collect(Collectors.toList());

        // There is only one, as the valueString is the visible value, and this is the hidden.
        assertEquals(parameterValues.size(), 2);
        assertTrue(checkList.contains("Patient/example"));
        assertTrue(checkList.contains("https://localhost:9443/fhir-server/api/v4/Patient/example"));
        assertTrue(parameterValues.get(0).isHidden());
    }
}