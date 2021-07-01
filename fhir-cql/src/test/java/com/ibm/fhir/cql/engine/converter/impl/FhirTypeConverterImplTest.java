package com.ibm.fhir.cql.engine.converter.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.ibm.fhir.cql.engine.converter.FhirTypeConverter;
import com.ibm.fhir.model.type.DateTime;

public class FhirTypeConverterImplTest {
    
    FhirTypeConverter typeConverter;
    
    @Before
    public void setup() {
        typeConverter = new FhirTypeConverterImpl();
    }
    
    @Test
    public void testIntegerIsCqlType() {
        assertTrue( typeConverter.isCqlType(Integer.valueOf(100)) );
    }
    
    @Test
    public void testStringIsCqlType() {
        assertTrue( typeConverter.isCqlType("Hello,World!") );
    }
    
    @Test
    public void testConvertDateTimeWithLocalDateValue() {
        LocalDate expected = LocalDate.of(2020,06,27);
        DateTime fhirDateTime = DateTime.of( expected );
        org.opencds.cqf.cql.engine.runtime.DateTime cqlDateTime = typeConverter.toCqlDateTime(fhirDateTime);
        assertNotNull(cqlDateTime);
        assertEquals( expected, cqlDateTime.getDateTime().toLocalDate() );
    }
}
