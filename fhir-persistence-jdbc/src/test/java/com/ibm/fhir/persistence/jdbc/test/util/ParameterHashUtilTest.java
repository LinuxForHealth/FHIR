/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.dto.CompositeParmVal;
import com.ibm.fhir.persistence.jdbc.dto.DateParmVal;
import com.ibm.fhir.persistence.jdbc.dto.LocationParmVal;
import com.ibm.fhir.persistence.jdbc.dto.NumberParmVal;
import com.ibm.fhir.persistence.jdbc.dto.QuantityParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ReferenceParmVal;
import com.ibm.fhir.persistence.jdbc.dto.StringParmVal;
import com.ibm.fhir.persistence.jdbc.dto.TokenParmVal;
import com.ibm.fhir.persistence.jdbc.util.ExtractedSearchParameters;
import com.ibm.fhir.persistence.jdbc.util.ParameterHashUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NumberParmBehaviorUtil;
import com.ibm.fhir.search.date.DateTimeHandler;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;

/**
 * Utility to do testing of the parameter hash utility.
 */
public class ParameterHashUtilTest {

    @Test
    public void testEmptyExtractedParameters() throws Exception {
        ParameterHashUtil util = new ParameterHashUtil();

        ExtractedSearchParameters esp1 = new ExtractedSearchParameters();
        ExtractedSearchParameters esp2 = new ExtractedSearchParameters();

        // Hashes not generated yet
        assertNull(esp1.getHash());
        assertNull(esp2.getHash());

        // Generate hashes
        esp1.generateHash(util);
        esp2.generateHash(util);

        // Check hashes
        String hash1 = esp1.getHash();
        String hash2 = esp2.getHash();
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertEquals(hash1, hash2);
    }

    @Test
    public void testExtractedParametersDifferentOrders() throws Exception {
        ParameterHashUtil util = new ParameterHashUtil();
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
        p5.setValueNumberLow(NumberParmBehaviorUtil.generateLowerBound(value5));
        p5.setValueNumberHigh(NumberParmBehaviorUtil.generateUpperBound(value5));

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
        ExtractedSearchParameters esp1 = new ExtractedSearchParameters();
        esp1.getParameters().addAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8a));
        ExtractedSearchParameters esp2 = new ExtractedSearchParameters();
        esp2.getParameters().addAll(Arrays.asList(p8b, p6, p4, p2, p1, p3, p5, p7));
        ExtractedSearchParameters esp3 = new ExtractedSearchParameters();
        esp3.getParameters().addAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8diff));
        ExtractedSearchParameters esp4 = new ExtractedSearchParameters();

        // Hashes not generated yet
        assertNull(esp1.getHash());
        assertNull(esp2.getHash());
        assertNull(esp3.getHash());
        assertNull(esp4.getHash());

        // Generate hashes
        esp1.generateHash(util);
        esp2.generateHash(util);
        esp3.generateHash(util);
        esp4.generateHash(util);

        // Check hashes
        String hash1 = esp1.getHash();
        String hash2 = esp2.getHash();
        String hash3 = esp3.getHash();
        String hash4 = esp4.getHash();
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotNull(hash3);
        assertNotNull(hash4);
        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
        assertNotEquals(hash1, hash4);
        assertNotEquals(hash3, hash4);
    }

    @Test
    public void testExtractedParametersSwappedValues() throws Exception {
        ParameterHashUtil util = new ParameterHashUtil();

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
        ExtractedSearchParameters espA1 = new ExtractedSearchParameters();
        espA1.getParameters().addAll(Arrays.asList(p1a, p2a));
        ExtractedSearchParameters espA2 = new ExtractedSearchParameters();
        espA2.getParameters().addAll(Arrays.asList(p2a, p1a));
        ExtractedSearchParameters espB1 = new ExtractedSearchParameters();
        espB1.getParameters().addAll(Arrays.asList(p1b, p2b));
        ExtractedSearchParameters espB2 = new ExtractedSearchParameters();
        espB2.getParameters().addAll(Arrays.asList(p2b, p1b));

        // Hashes not generated yet
        assertNull(espA1.getHash());
        assertNull(espB1.getHash());

        // Generate hashes
        espA1.generateHash(util);
        espA2.generateHash(util);
        espB1.generateHash(util);
        espB2.generateHash(util);

        // Check hashes
        String hashA1 = espA1.getHash();
        String hashA2 = espA2.getHash();
        String hashB1 = espB1.getHash();
        String hashB2 = espB2.getHash();
        assertNotNull(hashA1);
        assertNotNull(hashA2);
        assertNotNull(hashB1);
        assertNotNull(hashB2);
        assertEquals(hashA1, hashA2);
        assertNotEquals(hashA1, hashB1);
        assertEquals(hashB1, hashB2);
        assertNotEquals(hashA2, hashB2);
    }
}
