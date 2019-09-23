/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.persistence.exception.FHIRPersistenceProcessorException;
import com.ibm.fhir.persistence.helper.SearchParameterHelper;
import com.ibm.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.fhir.persistence.jdbc.util.JDBCParameterBuilder;

public class ParameterProcessorTest {
    // custom formatter providing the precision required by the unit tests
    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .appendFraction(ChronoField.MICRO_OF_SECOND, 6, 6, true)
            .appendPattern("XXX")
            .toFormatter();

    
    @Test
    public void testDate() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder parameterBuilder = new JDBCParameterBuilder();
        
        SearchParameter searchParameter = SearchParameterHelper.makeTestParameter("value");
        
        List<Parameter> params = parameterBuilder.process(searchParameter, Date.of("2016"));
        for (Parameter param : params) {
            assertEquals(timestampToString(param.getValueDateStart()), "2016-01-01T00:00:00.000000Z");
            assertEquals(timestampToString(param.getValueDate()), "2016-01-01T00:00:00.000000Z");
            assertEquals(timestampToString(param.getValueDateEnd()), "2016-12-31T23:59:59.999999Z");
        }
    }

    
    @Test
    public void testDateTime() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder parameterBuilder = new JDBCParameterBuilder();
        SearchParameter searchParameter = SearchParameterHelper.makeTestParameter("value");
        List<Parameter> params = parameterBuilder.process(searchParameter, DateTime.of("2016-01-01T10:10:10.1+04:00"));
        for (Parameter param : params) {
            assertEquals(timestampToString(param.getValueDate()), "2016-01-01T06:10:10.100000Z");
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
            assertEquals(param.getValueNumberHigh(), BigDecimal.valueOf(1));
        }
    }
    
    /**
     * Formats the given tstamp value as a string. Does not use Timestamp#toString()
     * because this adjusts the displayed string to local time
     * @param tstamp
     * @return
     */
    private String timestampToString(java.sql.Timestamp tstamp) {
        // do not use Timestamp#toString() because it converts to local timezone.
        // We need it rendered in UTC
        return tstamp.toInstant().atZone(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);
    }

}
