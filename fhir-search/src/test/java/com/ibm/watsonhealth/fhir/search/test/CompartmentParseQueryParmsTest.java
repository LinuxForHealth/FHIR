/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ibm.watsonhealth.fhir.model.Condition;
import com.ibm.watsonhealth.fhir.model.Device;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.Parameter.Type;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * This JUNIT test class contains methods that test the parsing of compartment related search data in the SearchUtil class.
 * @author markd
 *
 */
public class CompartmentParseQueryParmsTest {
	
	/**
	 * This method tests parsing compartment related query parms, passing an invalid compartment.
	 * @throws Exception
	 */
    @Test(expected = FHIRSearchException.class) 
    public void testInvalidComparmentName() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
    	SearchUtil.parseQueryParameters("bogusCompartmentName", "1", Observation.class, queryParameters);
    }
    
    /**
	 * This method tests parsing compartment related query parms, passing an invalid resource type for the compartment.
	 * @throws Exception
	 */
    @Test(expected = FHIRSearchException.class) 
    public void testInvalidResource() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
    	SearchUtil.parseQueryParameters("Patient", "1", Device.class, queryParameters);
    }
    
    //@Test 
    public void testSingleInclusionCriteria() throws Exception{
        Map<String, List<String>> queryParameters = new HashMap<>();
        String compartmentName = "Patient";
        String compartmentLogicalId = "11";
    	FHIRSearchContext context = SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, Condition.class, queryParameters);
    	
    	assertNotNull(context);
    	assertNotNull(context.getSearchParameters());
    	assertEquals(1, context.getSearchParameters().size());
    	Parameter parm1 = context.getSearchParameters().get(0);
    	assertEquals("patient",parm1.getName());
    	assertNull(parm1.getNextParameter());
    	assertEquals(Type.REFERENCE, parm1.getType());
    	assertEquals(1, parm1.getValues().size());
    	assertEquals(compartmentLogicalId, parm1.getValues().get(0));
    }
    
}
