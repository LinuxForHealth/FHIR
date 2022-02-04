/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.TimeZone;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceProcessorException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.dto.CompositeParmVal;
import com.ibm.fhir.persistence.jdbc.dto.DateParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.NumberParmVal;
import com.ibm.fhir.persistence.jdbc.dto.QuantityParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ReferenceParmVal;
import com.ibm.fhir.persistence.jdbc.dto.StringParmVal;
import com.ibm.fhir.persistence.jdbc.dto.TokenParmVal;
import com.ibm.fhir.persistence.jdbc.util.JDBCParameterBuildingVisitor;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Tests all valid combinations of search paramter types and data types
 * @see http://hl7.org/fhir/R4/search.html#table
 */
public class ParameterExtractionTest {
    private static final String SAMPLE_STRING = "test";
    private static final String SAMPLE_NON_NORMALIZED_TEXT_STRING = "Text  String";
    private static final String SAMPLE_NORMALIZED_TEXT_STRING = "text string";
    private static final String SAMPLE_URI = "http://example.com";
    private static final String SAMPLE_UNIT = "s";
    private static final String SAMPLE_REF_RESOURCE_TYPE = "Patient";
    private static final String SAMPLE_REF_ID = "abc";
    private static final String SAMPLE_REF_VERSION = "1";
    private static final String SAMPLE_REF = SAMPLE_REF_RESOURCE_TYPE + "/" + SAMPLE_REF_ID;
    private static final String SAMPLE_VERSIONED_REF = SAMPLE_REF_RESOURCE_TYPE + "/" + SAMPLE_REF_ID + "/_history/" + SAMPLE_REF_VERSION;
    private static final String SAMPLE_VERSIONED_CANONICAL_REF = SAMPLE_URI + "|" + SAMPLE_REF_VERSION;
    private static final String SAMPLE_DATE_START = "2016-01-01T00:00:00.000000Z";
    private static final String SAMPLE_DATE_END = "2016-01-02T00:00:00.000000Z";
    private static final String UNITSOFMEASURE = "http://unitsofmeasure.org";
    private static final String SEARCH_PARAM_CODE_VALUE = "value";

    private static final Extension SAMPLE_EXTENSION = Extension.builder().url(SAMPLE_URI).build();

    // custom formatter providing the precision required by the unit tests
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .appendFraction(ChronoField.MICRO_OF_SECOND, 6, 6, true)
            .appendPattern("XXX")
            .toFormatter();

    private static final SearchParameter.Builder searchParamBuilder = SearchParameter.builder()
            .url(Uri.of("http://ibm.com/fhir/test"))
            .name(string("test-param"))
            .status(PublicationStatus.DRAFT)
            .description(Markdown.of("#Test Parameter"))
            .code(Code.of(SEARCH_PARAM_CODE_VALUE))
            .base(ResourceTypeCode.BASIC);
    private static final SearchParameter numberSearchParam = searchParamBuilder.type(SearchParamType.NUMBER).build();
    private static final SearchParameter dateSearchParam = searchParamBuilder.type(SearchParamType.DATE).build();
    private static final SearchParameter referenceSearchParam = searchParamBuilder.type(SearchParamType.REFERENCE).build();
    private static final SearchParameter quantitySearchParam = searchParamBuilder.type(SearchParamType.QUANTITY).build();
    private static final SearchParameter uriSearchParam = searchParamBuilder.type(SearchParamType.URI).build();
    private static final SearchParameter stringSearchParam = searchParamBuilder.type(SearchParamType.STRING).build();
    private static final SearchParameter tokenSearchParam = searchParamBuilder.type(SearchParamType.TOKEN).build();

    @BeforeClass
    public void setSystemTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testBoolean() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        com.ibm.fhir.model.type.Boolean.TRUE.accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), "true");

        assertNullValueReturnsNoParameters(tokenSearchParam, com.ibm.fhir.model.type.Boolean.builder());
    }

    @Test
    public void testBoolean_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(tokenSearchParam, com.ibm.fhir.model.type.Boolean.builder());
    }

    @Test
    public void testCanonical() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder;
        Canonical canonical = Canonical.of(SAMPLE_URI);
        List<ExtractedParameterValue> params;

        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, referenceSearchParam);
        canonical.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 2, "Number of extracted parameters");
        assertEquals(((StringParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((StringParmVal) params.get(0)).getValueString(), SAMPLE_URI);
        assertEquals(((CompositeParmVal) params.get(1)).getName(), SEARCH_PARAM_CODE_VALUE + SearchConstants.CANONICAL_SUFFIX);
        List<ExtractedParameterValue> components = ((CompositeParmVal) params.get(1)).getComponent();
        assertEquals(components.size(), 2, "Number of components");
        assertEquals(((StringParmVal) components.get(0)).getName(),
            SearchUtil.makeCompositeSubCode(((CompositeParmVal) params.get(1)).getName(), SearchConstants.CANONICAL_COMPONENT_URI));
        assertEquals(((StringParmVal) components.get(0)).getValueString(), SAMPLE_URI);
        assertEquals(((StringParmVal) components.get(1)).getName(),
            SearchUtil.makeCompositeSubCode(((CompositeParmVal) params.get(1)).getName(), SearchConstants.CANONICAL_COMPONENT_VERSION));
        assertEquals(((StringParmVal) components.get(1)).getValueString(), null);

        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, referenceSearchParam);
        canonical = Canonical.of(SAMPLE_VERSIONED_CANONICAL_REF);
        canonical.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 2, "Number of extracted parameters");
        assertEquals(((StringParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((StringParmVal) params.get(0)).getValueString(), SAMPLE_VERSIONED_CANONICAL_REF);
        assertEquals(((CompositeParmVal) params.get(1)).getName(), SEARCH_PARAM_CODE_VALUE + SearchConstants.CANONICAL_SUFFIX);
        components = ((CompositeParmVal) params.get(1)).getComponent();
        assertEquals(components.size(), 2, "Number of components");
        assertEquals(((StringParmVal) components.get(0)).getName(),
            SearchUtil.makeCompositeSubCode(((CompositeParmVal) params.get(1)).getName(), SearchConstants.CANONICAL_COMPONENT_URI));
        assertEquals(((StringParmVal) components.get(0)).getValueString(), SAMPLE_URI);
        assertEquals(((StringParmVal) components.get(1)).getName(),
            SearchUtil.makeCompositeSubCode(((CompositeParmVal) params.get(1)).getName(), SearchConstants.CANONICAL_COMPONENT_VERSION));
        assertEquals(((StringParmVal) components.get(1)).getValueString(), SAMPLE_REF_VERSION);
        
        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, uriSearchParam);
        canonical = Canonical.of(SAMPLE_URI);
        canonical.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((StringParmVal) params.get(0)).getValueString(), SAMPLE_URI);
    }

    @Test
    public void testCanonical_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(referenceSearchParam, Canonical.builder());
        assertNullValueReturnsNoParameters(uriSearchParam, Canonical.builder());
    }

    @Test
    public void testCode() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        Code.of(SAMPLE_STRING).accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), SAMPLE_STRING);
    }

    @Test
    public void testCode_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(tokenSearchParam, Code.builder());
    }

    @Test
    public void testDate() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
        Date.of("2016").accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        for (ExtractedParameterValue param : params) {
            assertTrue(param instanceof DateParmVal);
            DateParmVal dateParam = (DateParmVal) param;
            assertEquals(timestampToString(dateParam.getValueDateStart()), SAMPLE_DATE_START);
        }
    }

    @Test
    public void testDate_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(dateSearchParam, Date.builder());
    }

    @Test
    public void testDateTime() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
        DateTime.of("2016-01-01T10:10:10.1+04:00").accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        for (ExtractedParameterValue param : params) {
            assertEquals(timestampToString(((DateParmVal) param).getValueDateStart()), "2016-01-01T06:10:10.100000Z");
        }
    }

    @Test
    public void testDateTime_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(dateSearchParam, DateTime.builder());
    }

    @Test
    public void testDecimal() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, numberSearchParam);
        Decimal.of(99.99).accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((NumberParmVal) params.get(0)).getValueNumber().doubleValue(), 99.99);
        assertEquals(((NumberParmVal) params.get(0)).getValueNumberLow().doubleValue(), 99.985);
        assertEquals(((NumberParmVal) params.get(0)).getValueNumberHigh().doubleValue(), 99.995);
    }

    @Test
    public void testDecimal_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(numberSearchParam, Decimal.builder());
    }

    @Test
    public void testId() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        Id.of("x").accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), "x");
    }

    @Test
    public void testId_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(tokenSearchParam, Id.builder());
    }

    @Test
    public void testInstant() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
        Instant now = Instant.now(ZoneOffset.UTC);
        now.accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(timestampToString(((DateParmVal) params.get(0)).getValueDateStart()), TIMESTAMP_FORMATTER.format(now.getValue()));
    }

    @Test
    public void testInstant_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(dateSearchParam, Instant.builder());
    }

    @Test
    public void testInteger() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, numberSearchParam);
        Integer.of(13).accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((NumberParmVal) params.get(0)).getValueNumber().intValue(), 13);
    }

    @Test
    public void testInteger_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(numberSearchParam, Integer.builder());
    }

    @Test
    public void testString() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder;
        com.ibm.fhir.model.type.String stringVal = string(SAMPLE_STRING);
        List<ExtractedParameterValue> params;

        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, stringSearchParam);
        stringVal.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((StringParmVal) params.get(0)).getValueString(), SAMPLE_STRING);

        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        stringVal.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), SAMPLE_STRING);
    }

    @Test
    public void testString_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(stringSearchParam, com.ibm.fhir.model.type.String.builder());
        assertNullValueReturnsNoParameters(tokenSearchParam, com.ibm.fhir.model.type.String.builder());
    }

    @Test
    public void testUri() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder;
        Uri uri = Uri.of(SAMPLE_URI);
        List<ExtractedParameterValue> params;

        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, referenceSearchParam);
        uri.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((ReferenceParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getValue(), SAMPLE_URI);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getTargetResourceType(), null);

        uri = Uri.of(SAMPLE_VERSIONED_REF);
        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, referenceSearchParam);
        uri.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((ReferenceParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getValue(), SAMPLE_REF_ID);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getVersion(), new java.lang.Integer(SAMPLE_REF_VERSION));
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getTargetResourceType(), SAMPLE_REF_RESOURCE_TYPE);

        uri = Uri.of(SAMPLE_URI);
        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, uriSearchParam);
        uri.accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((StringParmVal) params.get(0)).getValueString(), SAMPLE_URI);
    }

    @Test
    public void testUri_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(referenceSearchParam, Uri.builder());
        assertNullValueReturnsNoParameters(uriSearchParam, Uri.builder());
    }

    private void assertNullValueReturnsNoParameters(SearchParameter sp, Element.Builder builder) {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, sp);
        builder.extension(SAMPLE_EXTENSION).build().accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 0, "Number of extracted parameters");
    }


    @Test
    public void testAddress() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, stringSearchParam);
        Address.builder()
               .line(string("4025 S. Miami Blvd."))                    //0
               .city(string("Durham"))                                 //1
               .state(string("NC"))                                    //2
               .postalCode(string("27703"))                            //3
               .text(string("4025 S. Miami Blvd., Durham, NC 27703"))  //4
               .build()
               .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 5, "Number of extracted parameters");
        assertEquals(((StringParmVal) params.get(0)).getValueString(), "4025 S. Miami Blvd.");
        assertEquals(((StringParmVal) params.get(1)).getValueString(), "Durham");
        assertEquals(((StringParmVal) params.get(2)).getValueString(), "NC");
        assertEquals(((StringParmVal) params.get(3)).getValueString(), "27703");
        assertEquals(((StringParmVal) params.get(4)).getValueString(), "4025 S. Miami Blvd., Durham, NC 27703");
    }

    @Test
    public void testAddress_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(stringSearchParam, Address.builder());
    }

    @Test
    public void testAge() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Age.builder()
           .value(Decimal.of(1))
           .system(Uri.of(UNITSOFMEASURE))
           .code(Code.of("a"))
           .build()
           .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertTrue(params.get(0) instanceof QuantityParmVal);
        QuantityParmVal quantParam = (QuantityParmVal) params.get(0);
        assertEquals(quantParam.getValueNumber().intValue(), 1);
        assertEquals(quantParam.getValueSystem(), UNITSOFMEASURE);
        assertEquals(quantParam.getValueCode(), "a");
    }

    @Test
    public void testAge_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(quantitySearchParam, Age.builder());
    }

    @Test
    public void testCodeableConcept() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        CodeableConcept.builder()
                       .coding(Coding.builder().code(Code.of("a")).system(Uri.of(SAMPLE_URI)).display(string(SAMPLE_NON_NORMALIZED_TEXT_STRING + "a")).build())
                       .coding(Coding.builder().code(Code.of("b")).system(Uri.of(SAMPLE_URI)).build())
                       .coding(Coding.builder().code(Code.of("c")).system(Uri.of(SAMPLE_URI)).display(string(SAMPLE_NON_NORMALIZED_TEXT_STRING + "c")).build())
                       .text(string(SAMPLE_NON_NORMALIZED_TEXT_STRING))
                       .build()
                       .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 6, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), "a");
        assertEquals(((TokenParmVal) params.get(0)).getValueSystem(), SAMPLE_URI);
        assertEquals(((TokenParmVal) params.get(1)).getName(), SEARCH_PARAM_CODE_VALUE + SearchConstants.TEXT_MODIFIER_SUFFIX);
        assertEquals(((TokenParmVal) params.get(1)).getValueCode(), SAMPLE_NORMALIZED_TEXT_STRING + "a");
        assertEquals(((TokenParmVal) params.get(1)).getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
        assertEquals(((TokenParmVal) params.get(2)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((TokenParmVal) params.get(2)).getValueCode(), "b");
        assertEquals(((TokenParmVal) params.get(2)).getValueSystem(), SAMPLE_URI);
        assertEquals(((TokenParmVal) params.get(3)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((TokenParmVal) params.get(3)).getValueCode(), "c");
        assertEquals(((TokenParmVal) params.get(3)).getValueSystem(), SAMPLE_URI);
        assertEquals(((TokenParmVal) params.get(4)).getName(), SEARCH_PARAM_CODE_VALUE + SearchConstants.TEXT_MODIFIER_SUFFIX);
        assertEquals(((TokenParmVal) params.get(4)).getValueCode(), SAMPLE_NORMALIZED_TEXT_STRING + "c");
        assertEquals(((TokenParmVal) params.get(4)).getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
        assertEquals(((TokenParmVal) params.get(5)).getName(), SEARCH_PARAM_CODE_VALUE + SearchConstants.TEXT_MODIFIER_SUFFIX);
        assertEquals(((TokenParmVal) params.get(5)).getValueCode(), SAMPLE_NORMALIZED_TEXT_STRING);
        assertEquals(((TokenParmVal) params.get(5)).getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
    }

    @Test
    public void testCodeableConcept_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(tokenSearchParam, CodeableConcept.builder());
    }

    @Test
    public void testCoding() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        Coding.builder()
              .code(Code.of(SAMPLE_STRING))
              .system(Uri.of(SAMPLE_URI))
              .display(string(SAMPLE_NON_NORMALIZED_TEXT_STRING))
              .build()
              .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 2, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), SAMPLE_STRING);
        assertEquals(((TokenParmVal) params.get(0)).getValueSystem(), SAMPLE_URI);
        assertEquals(((TokenParmVal) params.get(1)).getName(), SEARCH_PARAM_CODE_VALUE + SearchConstants.TEXT_MODIFIER_SUFFIX);
        assertEquals(((TokenParmVal) params.get(1)).getValueCode(), SAMPLE_NORMALIZED_TEXT_STRING);
        assertEquals(((TokenParmVal) params.get(1)).getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
    }

    @Test
    public void testCoding_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(tokenSearchParam, Coding.builder());
    }

    @Test
    public void testContactPoint() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        ContactPoint.builder()
                    .system(ContactPointSystem.PHONE)
                    .value(string("5558675309"))
                    .build()
                    .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), "5558675309");
    }

    @Test
    public void testContactPoint_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(tokenSearchParam, ContactPoint.builder());
    }

    @Test
    public void testDuration() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Duration.builder()
                .value(Decimal.of(1))
                .system(Uri.of(UNITSOFMEASURE))
                .code(Code.of(SAMPLE_UNIT))
                .build()
                .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((QuantityParmVal) params.get(0)).getValueNumber().intValue(), 1);
        assertEquals(((QuantityParmVal) params.get(0)).getValueSystem(), UNITSOFMEASURE);
        assertEquals(((QuantityParmVal) params.get(0)).getValueCode(), SAMPLE_UNIT);
    }

    @Test
    public void testDuration_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(quantitySearchParam, Duration.builder());
    }

    @Test
    public void testHumanName() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, stringSearchParam);
        HumanName.builder()
                 .family(string("Simpson"))  //0
                 .given(string("Nick"))      //1
                 .prefix(string("Dr."))      //2
                 .suffix(string("III"))      //3
                 .text(string("Dr. Nick"))   //4
                 .build()
                 .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 5, "Number of extracted parameters");
        assertEquals(((StringParmVal) params.get(0)).getValueString(), "Simpson");
        assertEquals(((StringParmVal) params.get(1)).getValueString(), "Nick");
        assertEquals(((StringParmVal) params.get(2)).getValueString(), "Dr.");
        assertEquals(((StringParmVal) params.get(3)).getValueString(), "III");
        assertEquals(((StringParmVal) params.get(4)).getValueString(), "Dr. Nick");
    }

    @Test
    public void testHumanName_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(stringSearchParam, HumanName.builder());
    }

    @Test
    public void testIdentifier() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, tokenSearchParam);
        Identifier.builder()
                  .type(CodeableConcept.builder()
                      .coding(Coding.builder().code(Code.of("codea")).system(Uri.of("systema")).build())
                      .coding(Coding.builder().code(Code.of("codeb")).build())
                      .build())
                  .system(Uri.of(SAMPLE_URI))
                  .value(string("abc123"))
                  .build()
                  .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 3, "Number of extracted parameters");
        assertEquals(((TokenParmVal) params.get(0)).getValueSystem(), SAMPLE_URI);
        assertEquals(((TokenParmVal) params.get(0)).getValueCode(), "abc123");

        // Check composite parameters extracted for :of-type modifier
        String compositeCode = SEARCH_PARAM_CODE_VALUE + SearchConstants.OF_TYPE_MODIFIER_SUFFIX;

        CompositeParmVal cParmVal = (CompositeParmVal) params.get(1);
        assertEquals(cParmVal.getName(), compositeCode);
        assertEquals(cParmVal.getComponent().size(), 2, "Number of extracted components");
        TokenParmVal tokenParmVal = (TokenParmVal) cParmVal.getComponent().get(0);
        assertEquals(tokenParmVal.getName(), SearchUtil.makeCompositeSubCode(compositeCode, SearchConstants.OF_TYPE_MODIFIER_COMPONENT_TYPE));
        assertEquals(tokenParmVal.getValueSystem(), "systema");
        assertEquals(tokenParmVal.getValueCode(), "codea");
        tokenParmVal = (TokenParmVal) cParmVal.getComponent().get(1);
        assertEquals(tokenParmVal.getName(), SearchUtil.makeCompositeSubCode(compositeCode, SearchConstants.OF_TYPE_MODIFIER_COMPONENT_VALUE));
        assertEquals(tokenParmVal.getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
        assertEquals(tokenParmVal.getValueCode(), "abc123");

        cParmVal = (CompositeParmVal) params.get(2);
        assertEquals(cParmVal.getName(), compositeCode);
        assertEquals(cParmVal.getComponent().size(), 2, "Number of extracted components");
        tokenParmVal = (TokenParmVal) cParmVal.getComponent().get(0);
        assertEquals(tokenParmVal.getName(), SearchUtil.makeCompositeSubCode(compositeCode, SearchConstants.OF_TYPE_MODIFIER_COMPONENT_TYPE));
        assertEquals(tokenParmVal.getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
        assertEquals(tokenParmVal.getValueCode(), "codeb");
        tokenParmVal = (TokenParmVal) cParmVal.getComponent().get(1);
        assertEquals(tokenParmVal.getName(), SearchUtil.makeCompositeSubCode(compositeCode, SearchConstants.OF_TYPE_MODIFIER_COMPONENT_VALUE));
        assertEquals(tokenParmVal.getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
        assertEquals(tokenParmVal.getValueCode(), "abc123");
    }

    @Test
    public void testIdentifier_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(tokenSearchParam, Identifier.builder());
    }

    @Test
    public void testMoney() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Money.builder()
             .currency(Code.of("USD"))
             .value(Decimal.of(100))
             .build()
             .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((QuantityParmVal) params.get(0)).getValueCode(), "USD");
        assertEquals(((QuantityParmVal) params.get(0)).getValueNumber().intValue(), 100);
    }

    @Test
    public void testMoney_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(quantitySearchParam, Money.builder());
    }

    @Test
    public void testPeriod() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
        Period.builder()
              .start(DateTime.of(SAMPLE_DATE_START))
              .end(DateTime.of(SAMPLE_DATE_END))
              .build()
              .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(timestampToString(((DateParmVal) params.get(0)).getValueDateStart()), SAMPLE_DATE_START);
        assertEquals(timestampToString(((DateParmVal) params.get(0)).getValueDateEnd()), SAMPLE_DATE_END);
    }

    @Test
    public void testPeriod_nullStart() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
        Period.builder()
              .end(DateTime.of(SAMPLE_DATE_END))
              .build()
              .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(timestampToString(((DateParmVal) params.get(0)).getValueDateEnd()), SAMPLE_DATE_END);
    }

    @Test
    public void testPeriod_nullEnd() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
        Period.builder()
              .start(DateTime.of(SAMPLE_DATE_START))
              .build()
              .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(timestampToString(((DateParmVal) params.get(0)).getValueDateStart()), SAMPLE_DATE_START);
    }

    @Test
    public void testPeriod_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(dateSearchParam, Period.builder());
    }

    @Test
    public void testQuantity() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Quantity.builder()
                .value(Decimal.of(1))
                .system(Uri.of(UNITSOFMEASURE))
                .code(Code.of(SAMPLE_UNIT))
                .build()
                .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((QuantityParmVal) params.get(0)).getValueNumber().intValue(), 1);
        assertEquals(((QuantityParmVal) params.get(0)).getValueSystem(), UNITSOFMEASURE);
        assertEquals(((QuantityParmVal) params.get(0)).getValueCode(), SAMPLE_UNIT);
    }

    @Test
    public void testQuantity_valueOnly() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Quantity.builder()
                .value(Decimal.of(1))
                .build()
                .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((QuantityParmVal) params.get(0)).getValueNumber().intValue(), 1);
        assertEquals(((QuantityParmVal) params.get(0)).getValueSystem(), JDBCConstants.DEFAULT_TOKEN_SYSTEM);
        assertEquals(((QuantityParmVal) params.get(0)).getValueCode(), "");
    }

    @Test
    public void testQuantity_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(quantitySearchParam, Quantity.builder());
    }

    @Test
    public void testRange() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Range range = Range.builder()
                           .low(SimpleQuantity.builder()
                                              .code(Code.of(SAMPLE_UNIT))
                                              .system(Uri.of(UNITSOFMEASURE))
                                              .unit(string("seconds"))
                                              .value(Decimal.of(1))
                                              .build())
                           .high(SimpleQuantity.builder()
                                              .code(Code.of(SAMPLE_UNIT))
                                              .system(Uri.of(UNITSOFMEASURE))
                                              .unit(string("seconds"))
                                              .value(Decimal.of(2))
                                              .build())
                           .build();
        range.accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertTrue(params.get(0) instanceof QuantityParmVal);
        QuantityParmVal quantParam = (QuantityParmVal) params.get(0);
        assertEquals(quantParam.getValueNumberLow(), BigDecimal.valueOf(1));
        assertNull(quantParam.getValueNumber());
        assertEquals(quantParam.getValueNumberHigh(), BigDecimal.valueOf(2));
    }

    @Test
    public void testRange_nullHigh() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Range range = Range.builder()
                           .low(SimpleQuantity.builder()
                                              .code(Code.of(SAMPLE_UNIT))
                                              .system(Uri.of(UNITSOFMEASURE))
                                              .unit(string("seconds"))
                                              .value(Decimal.of(1))
                                              .build())
                           .build();
        range.accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertTrue(params.get(0) instanceof QuantityParmVal);
        QuantityParmVal quantParam = (QuantityParmVal) params.get(0);
        assertEquals(quantParam.getValueNumberLow(), BigDecimal.valueOf(1));
        assertNull(quantParam.getValueNumber());
        assertNull(quantParam.getValueNumberHigh());
    }

    @Test
    public void testRange_nullLow() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, quantitySearchParam);
        Range range = Range.builder()
                           .high(SimpleQuantity.builder()
                                              .code(Code.of(SAMPLE_UNIT))
                                              .system(Uri.of(UNITSOFMEASURE))
                                              .unit(string("seconds"))
                                              .value(Decimal.of(1))
                                              .build())
                           .build();
        range.accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertTrue(params.get(0) instanceof QuantityParmVal);
        QuantityParmVal quantParam = (QuantityParmVal) params.get(0);
        assertNull(quantParam.getValueNumberLow());
        assertNull(quantParam.getValueNumber());
        assertEquals(quantParam.getValueNumberHigh(), BigDecimal.valueOf(1));
    }

    @Test
    public void testRange_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(quantitySearchParam, Range.builder());
    }

    @Test
    public void testReference() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, referenceSearchParam);
        Reference.builder()
                 .reference(string(SAMPLE_REF))
                 .identifier(Identifier.builder()
                     .system(Uri.of(SAMPLE_URI))
                     .value(string(SAMPLE_STRING))
                     .build())
                 .build()
                 .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 2, "Number of extracted parameters");
        assertEquals(((ReferenceParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getValue(), SAMPLE_REF_ID);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getVersion(), null);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getTargetResourceType(), SAMPLE_REF_RESOURCE_TYPE);
        assertEquals(((TokenParmVal) params.get(1)).getName(), SEARCH_PARAM_CODE_VALUE + SearchConstants.IDENTIFIER_MODIFIER_SUFFIX);
        assertEquals(((TokenParmVal) params.get(1)).getValueSystem(), SAMPLE_URI);
        assertEquals(((TokenParmVal) params.get(1)).getValueCode(), SAMPLE_STRING);

        parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, referenceSearchParam);
        Reference.builder()
                .reference(string(SAMPLE_VERSIONED_REF))
                .build()
                .accept(parameterBuilder);
        params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        assertEquals(((ReferenceParmVal) params.get(0)).getName(), SEARCH_PARAM_CODE_VALUE);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getValue(), SAMPLE_REF_ID);
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getVersion(), new java.lang.Integer(SAMPLE_REF_VERSION));
        assertEquals(((ReferenceParmVal) params.get(0)).getRefValue().getTargetResourceType(), SAMPLE_REF_RESOURCE_TYPE);
    }

    @Test
    public void testReference_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(referenceSearchParam, Reference.builder());
    }

    @Test
    public void testTimingBounds() throws FHIRPersistenceProcessorException {
        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
        Period period = Period.builder()
                              .start(DateTime.of(SAMPLE_DATE_START))
                              .end(DateTime.of(SAMPLE_DATE_END))
                              .build();
        Timing.builder()
              .repeat(Timing.Repeat.builder().bounds(period).build())
              .build()
              .accept(parameterBuilder);
        List<ExtractedParameterValue> params = parameterBuilder.getResult();
        assertEquals(params.size(), 1, "Number of extracted parameters");
        ExtractedParameterValue param = params.get(0);
        assertEquals(timestampToString(((DateParmVal) param).getValueDateStart()), SAMPLE_DATE_START);
        assertEquals(timestampToString(((DateParmVal) param).getValueDateEnd()), SAMPLE_DATE_END);
    }

    @Test
    public void testTiming_null() throws FHIRPersistenceProcessorException {
        assertNullValueReturnsNoParameters(dateSearchParam, Timing.builder());
    }

    // Timing doesn't currently extract from "events"
//    @Test
//    public void testTimingEvents() throws FHIRPersistenceProcessorException {
//        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(SAMPLE_REF_RESOURCE_TYPE, dateSearchParam);
//        Timing.builder()
//              .event(DateTime.of(SAMPLE_DATE_START))
//              .event(DateTime.of(SAMPLE_DATE_END))
//              .build()
//              .accept(parameterBuilder);
//        List<Parameter> params = parameterBuilder.getResult();
//        assertEquals(params.size(), 1, "Number of extracted parameters");
//        Parameter param = params.get(0);
//        assertEquals(timestampToString(((DateParameter) param).getValueDateStart()), SAMPLE_DATE_START);
//        assertEquals(timestampToString(((DateParameter) param).getValueDateEnd()), SAMPLE_DATE_END);
//    }

    /**
     * Formats the given tstamp value as a string. Does not use Timestamp#toString()
     * because this adjusts the displayed string to local time
     * @param tstamp
     * @return
     */
    private String timestampToString(java.sql.Timestamp tstamp) {
        assertNotNull(tstamp);
        // do not use Timestamp#toString() because it converts to local timezone.
        // We need it rendered in UTC
        return tstamp.toInstant().atZone(ZoneOffset.UTC).format(TIMESTAMP_FORMATTER);
    }
}
