/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.test;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static com.ibm.watson.health.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.model.resource.SearchParameter;
import com.ibm.watson.health.fhir.model.type.Code;
import com.ibm.watson.health.fhir.model.type.Markdown;
import com.ibm.watson.health.fhir.model.type.PublicationStatus;
import com.ibm.watson.health.fhir.model.type.ResourceType;
import com.ibm.watson.health.fhir.model.type.SearchParamType;
import com.ibm.watson.health.fhir.model.type.Uri;
import com.ibm.watson.health.fhir.persistence.exception.FHIRPersistenceProcessorException;
import com.ibm.watson.health.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watson.health.fhir.persistence.jdbc.util.JDBCParameterBuilder;

/**
 * 
 * @author pbastide
 *
 */
public class JDBCParameterBuilderTimeTest {

    @Test
    public void testYearMonthProcess() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder builder = new JDBCParameterBuilder();
        SearchParameter.Builder builderSP =
                SearchParameter.builder().url(Uri.of("test")).name(string("test")).status(PublicationStatus.DRAFT).description(Markdown.of("test")).code(Code.of("test")).base(Arrays.asList(ResourceType.ACCOUNT)).type(SearchParamType.NUMBER);
        builderSP.expression(string("test"));
        
        YearMonth value = YearMonth.now();
        
        List<Parameter> parameters = builder.process(builderSP.build(), value);
        assertNotNull(parameters);
        assertFalse(parameters.isEmpty());
        assertEquals(parameters.size(),1);
         
    }
    
    @Test
    public void testYearProcess() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder builder = new JDBCParameterBuilder();
        SearchParameter.Builder builderSP =
                SearchParameter.builder().url(Uri.of("test")).name(string("test")).status(PublicationStatus.DRAFT).description(Markdown.of("test")).code(Code.of("test")).base(Arrays.asList(ResourceType.ACCOUNT)).type(SearchParamType.NUMBER);
        builderSP.expression(string("test"));
        
        Year value = Year.now();
        
        List<Parameter> parameters = builder.process(builderSP.build(), value);
        assertNotNull(parameters);
        assertFalse(parameters.isEmpty());
        assertEquals(parameters.size(),1);
         
    }
    
    @Test
    public void testLocalDateProcess() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder builder = new JDBCParameterBuilder();
        SearchParameter.Builder builderSP =
                SearchParameter.builder().url(Uri.of("test")).name(string("test")).status(PublicationStatus.DRAFT).description(Markdown.of("test")).code(Code.of("test")).base(Arrays.asList(ResourceType.ACCOUNT)).type(SearchParamType.NUMBER);
        builderSP.expression(string("test"));
        
        LocalDate value = LocalDate.now();
        
        List<Parameter> parameters = builder.process(builderSP.build(), value);
        assertNotNull(parameters);
        assertFalse(parameters.isEmpty());
        assertEquals(parameters.size(),1);
         
    }
    
    @Test
    public void testZonedDateTimeProcess() throws FHIRPersistenceProcessorException {
        JDBCParameterBuilder builder = new JDBCParameterBuilder();
        SearchParameter.Builder builderSP =
                SearchParameter.builder().url(Uri.of("test")).name(string("test")).status(PublicationStatus.DRAFT).description(Markdown.of("test")).code(Code.of("test")).base(Arrays.asList(ResourceType.ACCOUNT)).type(SearchParamType.NUMBER);
        builderSP.expression(string("test"));
        
        ZonedDateTime value = ZonedDateTime.now();
        
        List<Parameter> parameters = builder.process(builderSP.build(), value);
        assertNotNull(parameters);
        assertFalse(parameters.isEmpty());
        assertEquals(parameters.size(),1);
         
    }

}
