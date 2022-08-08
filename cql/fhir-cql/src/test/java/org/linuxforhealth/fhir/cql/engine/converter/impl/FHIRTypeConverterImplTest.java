/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.cql.engine.converter.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.LocalDate;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.cql.engine.converter.FHIRTypeConverter;
import org.linuxforhealth.fhir.cql.translator.TestHelper;
import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Quantity;

public class FHIRTypeConverterImplTest {

    FHIRTypeConverter typeConverter;

    @BeforeMethod
    public void setup() {
        typeConverter = new FHIRTypeConverterImpl();
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
        assertEquals( cqlDateTime.getDateTime().toLocalDate(), expected );
    }
    
    @Test
    public void testToCqlConceptNullCodes() throws Exception {
        // trying to find a path where no codes causes a null array
        // when I did it with the builder without setting the codes, I got
        // an empty array. Same here, but this seems more likely to find
        // a whole in the logic.
        Condition condition = (Condition) TestHelper.getTestResource("Condition.json");
        
        CodeableConcept noCodes = condition.getCode();
        
        org.opencds.cqf.cql.engine.runtime.Concept concept = typeConverter.toCqlConcept(noCodes);
        assertNotNull(concept);
        assertEquals("Something", concept.getDisplay());
        assertNotNull(noCodes.getCoding());
    }
    
    @Test
    public void testToCqlQuantityNullValue() {
        Quantity quantity = Quantity.builder().code(Code.of("123")).build();
        
        org.opencds.cqf.cql.engine.runtime.Quantity cqlQuantity = typeConverter.toCqlQuantity(quantity);
        assertNotNull(cqlQuantity);
        assertEquals(cqlQuantity.getValue().floatValue(), 0.0f);
        assertEquals(cqlQuantity.getUnit(), "1");
    }
}
