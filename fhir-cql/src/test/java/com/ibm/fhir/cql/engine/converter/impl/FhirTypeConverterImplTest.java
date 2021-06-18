package com.ibm.fhir.cql.engine.converter.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ibm.fhir.cql.engine.converter.FhirTypeConverter;

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
}
