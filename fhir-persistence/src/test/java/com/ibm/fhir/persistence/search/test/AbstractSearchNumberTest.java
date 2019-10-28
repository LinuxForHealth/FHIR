/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.exception.FHIRSearchException;

/**
 * @author lmsurpre
 * @see https://hl7.org/fhir/r4/search.html#number
 */
public abstract class AbstractSearchNumberTest extends AbstractPLSearchTest {

    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicNumber.json");
    }

    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("number");
    }

    @Test
    public void testSearchNumber_integer() throws Exception {
        assertSearchReturnsSavedResource("integer", "12");
        assertSearchReturnsSavedResource("integer", "1.2e1");
        
        assertSearchReturnsSavedResource("integer", "lt13");
        assertSearchReturnsSavedResource("integer", "gt11");
        assertSearchReturnsSavedResource("integer", "le12");
        assertSearchReturnsSavedResource("integer", "le13");
        assertSearchReturnsSavedResource("integer", "ge12");
        assertSearchReturnsSavedResource("integer", "ge11");
        
        assertSearchReturnsSavedResource("integer", "ne13");
        assertSearchReturnsSavedResource("integer", "ap12");
    }
    
    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchNumber_integer_eb() throws Exception {
        assertSearchReturnsSavedResource("integer", "eb12");
    }
    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchNumber_integer_sa() throws Exception {
        assertSearchReturnsSavedResource("integer", "sa12");
    }
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testSearchNumber_integer_invalidPrefix() throws Exception {
        assertSearchReturnsSavedResource("integer", "zz12");
    }
    
    @Test
    public void testSearchNumber_integer_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.integer", "12");
        assertSearchReturnsComposition("subject:Basic.integer", "1.2e1");
        
        assertSearchReturnsComposition("subject:Basic.integer", "lt13");
        assertSearchReturnsComposition("subject:Basic.integer", "gt11");
        assertSearchReturnsComposition("subject:Basic.integer", "le12");
        assertSearchReturnsComposition("subject:Basic.integer", "le13");
        assertSearchReturnsComposition("subject:Basic.integer", "ge12");
        assertSearchReturnsComposition("subject:Basic.integer", "ge11");
        
        assertSearchReturnsComposition("subject:Basic.integer", "ne13");
        assertSearchReturnsComposition("subject:Basic.integer", "ap12");
    }
    
    @Test
    public void testSearchNumber_integer_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("integer", Collections.singletonList("12"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }
    
    @Test
    public void testSearchNumber_integer_missing() throws Exception {
        assertSearchReturnsSavedResource("integer:missing", "false");
        assertSearchDoesntReturnSavedResource("integer:missing", "true");
        
        assertSearchReturnsSavedResource("missing-integer:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-integer:missing", "false");
    }
    
    @Test
    public void testSearchNumber_integer_or() throws Exception {
        assertSearchReturnsSavedResource("integer", "12,99");
        assertSearchReturnsSavedResource("integer", "1.2e1,99");
        assertSearchReturnsSavedResource("integer", "99,12");
        assertSearchReturnsSavedResource("integer", "99,1.2e1");
        
        assertSearchReturnsSavedResource("integer", "lt13,99");
        assertSearchReturnsSavedResource("integer", "gt11,99");
        assertSearchReturnsSavedResource("integer", "le12,99");
        assertSearchReturnsSavedResource("integer", "le13,99");
        assertSearchReturnsSavedResource("integer", "ge12,99");
        assertSearchReturnsSavedResource("integer", "ge11,99");
        assertSearchReturnsSavedResource("integer", "99,lt13");
        assertSearchReturnsSavedResource("integer", "99,gt11");
        assertSearchReturnsSavedResource("integer", "99,le12");
        assertSearchReturnsSavedResource("integer", "99,le13");
        assertSearchReturnsSavedResource("integer", "99,ge12");
        assertSearchReturnsSavedResource("integer", "99,ge11");
        
        assertSearchReturnsSavedResource("integer", "ne13,99");
        assertSearchReturnsSavedResource("integer", "ap12,99");
        assertSearchReturnsSavedResource("integer", "99,ne13");
        assertSearchReturnsSavedResource("integer", "99,ap12");
    }

//    @Test
//    public void testSearchNumber_integer_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.integer:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.integer:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-integer:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-integer:missing", "false");
//    }
    
    @Test
    public void testSearchNumber_decimal() throws Exception {
        assertSearchDoesntReturnSavedResource("decimal", "99");
        assertSearchDoesntReturnSavedResource("decimal", "99.98499");

//        assertSearchReturnsSavedResource("decimal", "99.985");
        assertSearchReturnsSavedResource("decimal", "99.99");
        assertSearchReturnsSavedResource("decimal", "9999e-2");

//        assertSearchReturnsSavedResource("decimal", "99.99499");
        assertSearchDoesntReturnSavedResource("decimal", "99.995");
        assertSearchDoesntReturnSavedResource("decimal", "100");
        
        assertSearchReturnsSavedResource("decimal", "ne99");
        assertSearchReturnsSavedResource("decimal", "ne99.98499");
//        assertSearchDoesntReturnSavedResource("decimal", "ne99.985");
        assertSearchDoesntReturnSavedResource("decimal", "ne99.99");
        assertSearchDoesntReturnSavedResource("decimal", "ne9999e-2");

//        assertSearchDoesntReturnSavedResource("decimal", "ne99.99499");
        assertSearchReturnsSavedResource("decimal", "ne99.995");
        assertSearchReturnsSavedResource("decimal", "ne100");
        
        // Currently "ap" prefix works just like "eq" for non-ranges
//      assertSearchReturnsSavedResource("decimal", "ap99");
//      assertSearchReturnsSavedResource("decimal", "ap99.98499");
//      assertSearchReturnsSavedResource("decimal", "ap99.985");
        assertSearchReturnsSavedResource("decimal", "ap99.99");
        assertSearchReturnsSavedResource("decimal", "ap9999e-2");
//      assertSearchReturnsSavedResource("decimal", "ap99.99499");
//      assertSearchReturnsSavedResource("decimal", "ap99.995");
//      assertSearchReturnsSavedResource("decimal", "ap100");
      
        // For gt, lt, ge, le, sa, & eb, numbers are treated as if they have arbitrarily high precision
        // Question:  does that apply to just the search parameter value, or to the target values too?
        
        assertSearchDoesntReturnSavedResource("decimal", "lt99");
        assertSearchDoesntReturnSavedResource("decimal", "lt99.98499");
        assertSearchDoesntReturnSavedResource("decimal", "lt99.985");
        assertSearchDoesntReturnSavedResource("decimal", "lt99.99");
        assertSearchDoesntReturnSavedResource("decimal", "lt9999e-2");
        assertSearchReturnsSavedResource("decimal", "lt99.99499");
        assertSearchReturnsSavedResource("decimal", "lt99.995");
        assertSearchReturnsSavedResource("decimal", "lt100");
        
        assertSearchReturnsSavedResource("decimal", "gt99");
        assertSearchReturnsSavedResource("decimal", "gt99.98499");
        assertSearchReturnsSavedResource("decimal", "gt99.985");
        assertSearchReturnsSavedResource("decimal", "gt99");
        assertSearchDoesntReturnSavedResource("decimal", "gt99.99");
        assertSearchDoesntReturnSavedResource("decimal", "gt9999e-2");
        assertSearchDoesntReturnSavedResource("decimal", "gt99.99499");
        assertSearchDoesntReturnSavedResource("decimal", "gt99.995");
        assertSearchDoesntReturnSavedResource("decimal", "gt100");
        
        assertSearchDoesntReturnSavedResource("decimal", "le99");
        assertSearchDoesntReturnSavedResource("decimal", "le99.98499");
        assertSearchDoesntReturnSavedResource("decimal", "le99.985");
        assertSearchReturnsSavedResource("decimal", "le99.99");
        assertSearchReturnsSavedResource("decimal", "le9999e-2");
        assertSearchReturnsSavedResource("decimal", "le99.99499");
        assertSearchReturnsSavedResource("decimal", "le99.995");
        assertSearchReturnsSavedResource("decimal", "le100");
        
        assertSearchReturnsSavedResource("decimal", "ge99");
        assertSearchReturnsSavedResource("decimal", "ge99.98499");
        assertSearchReturnsSavedResource("decimal", "ge99.985");
        assertSearchReturnsSavedResource("decimal", "ge99.99");
        assertSearchReturnsSavedResource("decimal", "ge9999e-2");
        assertSearchDoesntReturnSavedResource("decimal", "ge99.99499");
        assertSearchDoesntReturnSavedResource("decimal", "ge99.995");
        assertSearchDoesntReturnSavedResource("decimal", "ge100");
        
        assertSearchReturnsSavedResource("decimal", "sa99");
        assertSearchReturnsSavedResource("decimal", "sa99.98499");
        assertSearchReturnsSavedResource("decimal", "sa99.985");
        assertSearchDoesntReturnSavedResource("decimal", "sa99.99");
        assertSearchDoesntReturnSavedResource("decimal", "sa9999e-2");
        assertSearchDoesntReturnSavedResource("decimal", "sa99.99499");
        assertSearchDoesntReturnSavedResource("decimal", "sa99.995");
        assertSearchDoesntReturnSavedResource("decimal", "sa100");
        
        assertSearchDoesntReturnSavedResource("decimal", "eb99");
        assertSearchDoesntReturnSavedResource("decimal", "eb99.98499");
        assertSearchDoesntReturnSavedResource("decimal", "eb99.985");
        assertSearchDoesntReturnSavedResource("decimal", "eb99.99");
        assertSearchDoesntReturnSavedResource("decimal", "eb9999e-2");
        assertSearchReturnsSavedResource("decimal", "eb99.99499");
        assertSearchReturnsSavedResource("decimal", "eb99.995");
        assertSearchReturnsSavedResource("decimal", "eb100");
    }
    
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testSearchNumber_decimal_invalidPrefix() throws Exception {
        assertSearchReturnsSavedResource("decimal", "zz99.99");
    }
    
    @Test
    public void testSearchNumber_decimal_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.decimal", "99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "9999e-2");
        
        assertSearchReturnsComposition("subject:Basic.decimal", "lt100");
        assertSearchReturnsComposition("subject:Basic.decimal", "gt99");
        assertSearchReturnsComposition("subject:Basic.decimal", "le99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "le100");
        assertSearchReturnsComposition("subject:Basic.decimal", "ge99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "ge99");
        
        assertSearchReturnsComposition("subject:Basic.decimal", "ne13");
        assertSearchReturnsComposition("subject:Basic.decimal", "ap99.99");
    }
    
    @Test
    public void testSearchNumber_decimal_missing() throws Exception {
        assertSearchReturnsSavedResource("decimal:missing", "false");
        assertSearchDoesntReturnSavedResource("decimal:missing", "true");
        
        assertSearchReturnsSavedResource("missing-decimal:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-decimal:missing", "false");
    }
    
    @Test
    public void testSearchNumber_decimal_or() throws Exception {
        assertSearchReturnsComposition("subject:Basic.decimal", "99.99,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "9999e-2,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,9999e-2");
        
        assertSearchReturnsComposition("subject:Basic.decimal", "lt100,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "gt99,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "le99.99,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "le100,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "ge99.99,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "ge99,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,lt100");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,gt99");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,le99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,le100");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,ge99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,ge99");
        
        assertSearchReturnsComposition("subject:Basic.decimal", "ne13,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "ap99.99,1");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,ne13");
        assertSearchReturnsComposition("subject:Basic.decimal", "1,ap99.99");
    }
    
//    @Test
//    public void testSearchNumber_decimal_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.decimal:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.decimal:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-decimal:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-decimal:missing", "false");
//    }
}
