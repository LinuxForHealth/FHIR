/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.dto.CompositeParmVal;
import com.ibm.fhir.persistence.jdbc.dto.DateParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.LocationParmVal;
import com.ibm.fhir.persistence.jdbc.dto.NumberParmVal;
import com.ibm.fhir.persistence.jdbc.dto.QuantityParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ReferenceParmVal;
import com.ibm.fhir.persistence.jdbc.dto.StringParmVal;
import com.ibm.fhir.persistence.jdbc.dto.TokenParmVal;
import com.ibm.fhir.persistence.jdbc.util.ParameterHashVisitor;
import com.ibm.fhir.persistence.jdbc.util.type.NewNumberParmBehaviorUtil;
import com.ibm.fhir.search.date.DateTimeHandler;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;

/**
 * Utility to do testing of the parameter hash utility.
 */
public class ParameterHashTest {

    @Test
    public void testNoExtractedParameters() throws Exception {
        List<ExtractedParameterValue> parameters1 = new ArrayList<>();
        List<ExtractedParameterValue> parameters2 = new ArrayList<>();

        // Sort parameters to ensure hash is deterministic
        sortExtractedParameterValues(parameters1);
        sortExtractedParameterValues(parameters2);

        // Visit parameters
        ParameterHashVisitor phv1 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters1) {
            p.accept(phv1);
        }
        String hash1 = phv1.getBase64Hash();
        assertNotNull(hash1);

        ParameterHashVisitor phv2 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters2) {
            p.accept(phv2);
        }
        String hash2 = phv2.getBase64Hash();
        assertNotNull(hash2);

        // Check hashes
        assertEquals(hash1, hash2);
    }

    @Test
    public void testNullValues() throws Exception {
        // Create two number param values, one with null low, and one with null high
        BigDecimal value = new BigDecimal(5);

        NumberParmVal num1 = new NumberParmVal();
        num1.setResourceType("Patient");
        num1.setName("code5");
        num1.setUrl("url5");
        num1.setVersion("version5");
        num1.setValueNumber(value);
        num1.setValueNumberLow(NewNumberParmBehaviorUtil.generateLowerBound(value));

        NumberParmVal num2 = new NumberParmVal();
        num2.setResourceType("Patient");
        num2.setName("code5");
        num2.setUrl("url5");
        num2.setVersion("version5");
        num2.setValueNumber(value);
        num2.setValueNumberHigh(NewNumberParmBehaviorUtil.generateUpperBound(value));

        List<ExtractedParameterValue> parameters1 = Arrays.asList(num1);
        List<ExtractedParameterValue> parameters2 = Arrays.asList(num2);

        // Sort parameters to ensure hash is deterministic
        sortExtractedParameterValues(parameters1);
        sortExtractedParameterValues(parameters2);

        // Visit parameters
        ParameterHashVisitor phv1 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters1) {
            p.accept(phv1);
        }
        String hash1 = phv1.getBase64Hash();
        assertNotNull(hash1);

        ParameterHashVisitor phv2 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters2) {
            p.accept(phv2);
        }
        String hash2 = phv2.getBase64Hash();
        assertNotNull(hash2);

        // Check hashes
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testExtractedParametersDifferentOrders() throws Exception {
        Instant instant = Instant.now();

        // Define some search parameter values
        StringParmVal p1 = new StringParmVal();
        p1.setResourceType("Patient");
        p1.setName("code1");
        p1.setUrl("url1");
        p1.setVersion("version1");
        p1.setValueString("valueString1");

        TokenParmVal p2 = new TokenParmVal();
        p2.setResourceType("Patient");
        p2.setName("code2");
        p2.setUrl("url2");
        p2.setVersion("version2");
        p2.setValueSystem("valueSystem2");
        p2.setValueCode("valueCode2");

        ReferenceParmVal p3 = new ReferenceParmVal();
        p3.setResourceType("Patient");
        p3.setName("code3");
        p3.setUrl("url3");
        p3.setVersion("version3");
        p3.setRefValue(new ReferenceValue("Patient", "value3", ReferenceType.LOGICAL, 1));

        QuantityParmVal p4 = new QuantityParmVal();
        p4.setResourceType("Patient");
        p4.setName("code4");
        p4.setUrl("url4");
        p4.setVersion("version4");
        p4.setValueNumber(new BigDecimal(4));
        p4.setValueNumberLow(new BigDecimal(3.9));
        p4.setValueNumberHigh(new BigDecimal(4.1));
        p4.setValueCode("valueCode4");
        p4.setValueSystem("valueSystem4");

        NumberParmVal p5 = new NumberParmVal();
        p5.setResourceType("Patient");
        p5.setName("code5");
        p5.setUrl("url5");
        p5.setVersion("version5");
        BigDecimal value5 = new BigDecimal(5);
        p5.setValueNumber(value5);
        p5.setValueNumberLow(NewNumberParmBehaviorUtil.generateLowerBound(value5));
        p5.setValueNumberHigh(NewNumberParmBehaviorUtil.generateUpperBound(value5));

        LocationParmVal p6 = new LocationParmVal();
        p6.setResourceType("Patient");
        p6.setName("code6");
        p6.setUrl("url6");
        p6.setVersion("version6");
        p6.setValueLatitude(6.6);
        p6.setValueLongitude(60.6);

        DateParmVal p7 = new DateParmVal();
        p7.setResourceType("Patient");
        p7.setName("code7");
        p7.setUrl("url7");
        p7.setVersion("version7");
        p7.setValueDateStart(DateTimeHandler.generateTimestamp(instant));
        p7.setValueDateEnd(DateTimeHandler.generateTimestamp(instant.plusSeconds(1)));

        CompositeParmVal p8a = new CompositeParmVal();
        p8a.setResourceType("Patient");
        p8a.setName("code8");
        p8a.setUrl("url8");
        p8a.setVersion("version8");
        p8a.addComponent(p1);
        p8a.addComponent(p5);

        CompositeParmVal p8b = new CompositeParmVal();
        p8b.setResourceType("Patient");
        p8b.setName("code8");
        p8b.setUrl("url8");
        p8b.setVersion("version8");
        p8b.addComponent(p5);
        p8b.addComponent(p1);

        CompositeParmVal p8diff = new CompositeParmVal();
        p8diff.setResourceType("Patient");
        p8diff.setName("code8");
        p8diff.setUrl("url8");
        p8diff.setVersion("version8");
        p8diff.addComponent(p4);
        p8diff.addComponent(p6);

        // Add the search parameters in different orders for esp1 and esp2, which still results in same hash,
        // but for esp3 and esp4, the search parameters are different, so they should not match the others
        List<ExtractedParameterValue> parameters1 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8a);
        List<ExtractedParameterValue> parameters2 = Arrays.asList(p8b, p6, p4, p2, p1, p3, p5, p7);
        List<ExtractedParameterValue> parameters3 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8diff);
        List<ExtractedParameterValue> parameters4 = Collections.emptyList();

        // Visit parameters
        ParameterHashVisitor phv1PreSort = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters1) {
            p.accept(phv1PreSort);
        }
        String hash1PreSort = phv1PreSort.getBase64Hash();
        assertNotNull(hash1PreSort);

        ParameterHashVisitor phv2PreSort = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters2) {
            p.accept(phv2PreSort);
        }
        String hash2PreSort = phv2PreSort.getBase64Hash();
        assertNotNull(hash2PreSort);

        // Check hashes (without sorting first)
        // They should not match, which shows that the sorting is needed to generate deterministic hashes
        assertNotEquals(hash1PreSort, hash2PreSort);

        // Sort parameters to ensure hash is deterministic
        sortExtractedParameterValues(parameters1);
        sortExtractedParameterValues(parameters2);
        sortExtractedParameterValues(parameters3);
        sortExtractedParameterValues(parameters4);

        // Visit parameters
        ParameterHashVisitor phv1 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters1) {
            p.accept(phv1);
        }
        String hash1 = phv1.getBase64Hash();
        assertNotNull(hash1);

        ParameterHashVisitor phv2 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters2) {
            p.accept(phv2);
        }
        String hash2 = phv2.getBase64Hash();
        assertNotNull(hash2);

        ParameterHashVisitor phv3 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters3) {
            p.accept(phv3);
        }
        String hash3 = phv3.getBase64Hash();
        assertNotNull(hash3);

        ParameterHashVisitor phv4 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parameters4) {
            p.accept(phv4);
        }
        String hash4 = phv4.getBase64Hash();
        assertNotNull(hash4);

        // Check hashes
        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
        assertNotEquals(hash1, hash4);
        assertNotEquals(hash3, hash4);
    }

    @Test
    public void testExtractedParametersSwappedValues() throws Exception {

        // Define some search parameter values
        StringParmVal p1a = new StringParmVal();
        p1a.setResourceType("Patient");
        p1a.setName("code1");
        p1a.setUrl("url1");
        p1a.setVersion("version1");
        p1a.setValueString("valueString1");

        StringParmVal p2a = new StringParmVal();
        p2a.setResourceType("Patient");
        p2a.setName("code2");
        p2a.setUrl("url2");
        p2a.setVersion("version2");
        p2a.setValueString("valueString2");

        // Define some search parameter values with the values swapped
        StringParmVal p1b = new StringParmVal();
        p1b.setResourceType("Patient");
        p1b.setName("code1");
        p1b.setUrl("url1");
        p1b.setVersion("version1");
        p1b.setValueString("valueString2");

        StringParmVal p2b = new StringParmVal();
        p2b.setResourceType("Patient");
        p2b.setName("code2");
        p2b.setUrl("url2");
        p2b.setVersion("version2");
        p2b.setValueString("valueString1");

        // Add the search parameters in which the values are swapped (espA1<-->espB1, espA2<-->espB2), so they should not match each other,
        // but if just the order of the parameters is swapped (espA1<-->espA2, espB1<-->espB2), then they do match
        List<ExtractedParameterValue> parametersA1 = Arrays.asList(p1a, p2a);
        List<ExtractedParameterValue> parametersA2 = Arrays.asList(p2a, p1a);
        List<ExtractedParameterValue> parametersB1 = Arrays.asList(p1b, p2b);
        List<ExtractedParameterValue> parametersB2 = Arrays.asList(p2b, p1b);

        // Sort parameters to ensure hash is deterministic
        sortExtractedParameterValues(parametersA1);
        sortExtractedParameterValues(parametersA2);
        sortExtractedParameterValues(parametersB1);
        sortExtractedParameterValues(parametersB2);

        // Visit parameters
        ParameterHashVisitor phvA1 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parametersA1) {
            p.accept(phvA1);
        }
        String hashA1 = phvA1.getBase64Hash();
        assertNotNull(hashA1);

        ParameterHashVisitor phvA2 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parametersA2) {
            p.accept(phvA2);
        }
        String hashA2 = phvA2.getBase64Hash();
        assertNotNull(hashA2);

        ParameterHashVisitor phvB1 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parametersB1) {
            p.accept(phvB1);
        }
        String hashB1 = phvB1.getBase64Hash();
        assertNotNull(hashB1);

        ParameterHashVisitor phvB2 = new ParameterHashVisitor();
        for (ExtractedParameterValue p: parametersB2) {
            p.accept(phvB2);
        }
        String hashB2 = phvB2.getBase64Hash();
        assertNotNull(hashB2);

        // Check hashes
        assertEquals(hashA1, hashA2);
        assertNotEquals(hashA1, hashB1);
        assertEquals(hashB1, hashB2);
        assertNotEquals(hashA2, hashB2);
    }

    /**
     * Sorts the extracted parameter values in natural order. If the list contains any composite parameter values,
     * those are sorted before the list itself is sorted. Since composite parameters cannot themselves contain composites,
     * doing this with a recursive call is ok.
     * @param extractedParameterValues the extracted parameter values
     */
    private void sortExtractedParameterValues(List<ExtractedParameterValue> extractedParameterValues) {
        for (ExtractedParameterValue extractedParameterValue : extractedParameterValues) {
            if (extractedParameterValue instanceof CompositeParmVal) {
                CompositeParmVal compositeParmVal = (CompositeParmVal) extractedParameterValue;
                sortExtractedParameterValues(compositeParmVal.getComponent());
            }
        }
        Collections.sort(extractedParameterValues);
    }
}
