/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceProcessorException;
import com.ibm.watsonhealth.fhir.persistence.helper.SearchParameterHelper;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.JDBCParameterBuilder;

public class ParameterProcessorTest {
    @Test
    public void testDate() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder parameterBuilder = new JDBCParameterBuilder();
        
        SearchParameter searchParameter = SearchParameterHelper.makeTestParameter("value");
        
        List<Parameter> params = parameterBuilder.process(searchParameter, Date.of("2016"));
        for (Parameter param : params) {
            assertEquals(param.getValueDateStart().toString(), "2016-01-01 00:00:00.0");
            assertEquals(param.getValueDate().toString(), "2016-01-01 00:00:00.0");
            assertEquals(param.getValueDateEnd().toString(), "2016-12-31 23:59:59.999999");
        }
    }
    
    @Test
    public void testDateTime() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder parameterBuilder = new JDBCParameterBuilder();
        SearchParameter searchParameter = SearchParameterHelper.makeTestParameter("value");
        List<Parameter> params = parameterBuilder.process(searchParameter, Date.of("2016-01-01T10:10:10.1+04:00"));
        for (Parameter param : params) {
            assertEquals(param.getValueDate().toString(), "2016-01-01 06:10:10.1");
        }
    }
    
    @Test
    public void testRange() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder parameterBuilder = new JDBCParameterBuilder();
        SearchParameter searchParameter = SearchParameterHelper.makeTestParameter("value");
        // FHIRUtil.range creates a range with a high but no low
        List<Parameter> params = parameterBuilder.process(searchParameter, SearchParameterHelper.range("s", "seconds", "http://unitsofmeasure.org", 1));
        for (Parameter param : params) {
            assertNull(param.getValueNumberLow());
            assertNull(param.getValueNumber());
            assertEquals(param.getValueNumberHigh(), 1);
        }
    }
}
