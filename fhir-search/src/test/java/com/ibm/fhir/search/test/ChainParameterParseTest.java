/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.ClinicalImpression;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * This TestNG test class contains methods that test the parsing of chain search parameters
 * in the SearchUtil class.
 */
public class ChainParameterParseTest extends BaseSearchTest {

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainInvalidModifierException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("organization:identifier.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainBadSearchParameterException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("badParm.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainBadSearchParameterException2() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("general-practitioner:Practitioner.badParm", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainNonReferenceTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("name.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainBadModifierResourceTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("general-practitioner:Condition.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainNoModifierResourceTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("general-practitioner.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainReferenceLogicalIdException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("link:Patient.general-practitioner", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainBadModifierLastParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("organization:Organization.name:badModifier", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainUnsupportedModifierLastParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("organization:Organization.name:type", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testChainResponseParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("organization:Organization._total", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testValidChainWithTokenSearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&general-practitioner:Organization.active=true";

        queryParameters.put("general-practitioner:Organization.active", Collections.singletonList("true"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("general-practitioner", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("Organization", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.TOKEN, nextParameter.getType());
        assertEquals("active", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals("true", nextParameter.getValues().get(0).getValueCode());
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainWithSearchParameterModifier() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&general-practitioner:Organization.active:missing=true";

        queryParameters.put("general-practitioner:Organization.active:missing", Collections.singletonList("true"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("general-practitioner", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("Organization", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.TOKEN, nextParameter.getType());
        assertEquals("active", nextParameter.getCode());
        assertEquals(Modifier.MISSING, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals("true", nextParameter.getValues().get(0).getValueCode());
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainMultiple() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Patient> resourceType = Patient.class;
        String queryString = "&link:Patient.organization:Organization.name=xxx";

        queryParameters.put("link:Patient.organization:Organization.name", Collections.singletonList("xxx"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(2, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("link", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("Patient", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.REFERENCE, nextParameter.getType());
        assertEquals("organization", nextParameter.getCode());
        assertEquals(Modifier.TYPE, nextParameter.getModifier());
        assertEquals("Organization", nextParameter.getModifierResourceTypeName());
        assertNotNull(nextParameter.getNextParameter());
        assertEquals(0, nextParameter.getValues().size());
        assertTrue(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        nextParameter = nextParameter.getNextParameter();
        assertEquals(Type.STRING, nextParameter.getType());
        assertEquals("name", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals(nextParameter.getValues().get(0).getValueString(), "xxx");
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Patient", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainWithNumberSearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<ClinicalImpression> resourceType = ClinicalImpression.class;
        String queryString = "&investigation:RiskAssessment.probability=50.5";

        queryParameters.put("investigation:RiskAssessment.probability", Collections.singletonList("50.5"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("investigation", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("RiskAssessment", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.NUMBER, nextParameter.getType());
        assertEquals("probability", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals(new BigDecimal("50.5"), nextParameter.getValues().get(0).getValueNumber());
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/ClinicalImpression", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainWithDateSearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<ClinicalImpression> resourceType = ClinicalImpression.class;
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        // If the Instant has a millseconds value of exactly 0, then the toString() will not include milliseconds in the search query,
        // which will cause the lower/upper bound to include all milliseconds within the second, instead of just the exact millisecond.
        // For the purpose of this testcase, just ensure that there is a non-0 value for milliseconds, so the toString() includes
        // milliseconds in the search query.
        if (now.get(ChronoField.MILLI_OF_SECOND) == 0) {
            now = now.plusMillis(1);
        }
        String queryString = "&investigation:RiskAssessment.date=" + now.toString();

        queryParameters.put("investigation:RiskAssessment.date", Collections.singletonList(now.toString()));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("investigation", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("RiskAssessment", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.DATE, nextParameter.getType());
        assertEquals("date", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals(now, nextParameter.getValues().get(0).getValueDateLowerBound());
        assertEquals(now, nextParameter.getValues().get(0).getValueDateUpperBound());
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/ClinicalImpression", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainWithReferenceSearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<ClinicalImpression> resourceType = ClinicalImpression.class;
        String queryString = "&investigation:RiskAssessment.encounter=Encounter/1";

        queryParameters.put("investigation:RiskAssessment.encounter", Collections.singletonList("Encounter/1"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("investigation", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("RiskAssessment", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.REFERENCE, nextParameter.getType());
        assertEquals("encounter", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        List<String> valueStrings = new ArrayList<>();
        for (QueryParameterValue value : nextParameter.getValues()) {
            valueStrings.add(value.getValueString());
        }
        assertTrue(valueStrings.contains("Encounter/1"));
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/ClinicalImpression", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainWithQuantitySearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<ClinicalImpression> resourceType = ClinicalImpression.class;
        String queryString = "&finding-ref:Observation.value-quantity=5.4";

        queryParameters.put("finding-ref:Observation.value-quantity", Collections.singletonList("5.4"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("finding-ref", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.QUANTITY, nextParameter.getType());
        assertEquals("value-quantity", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals(new BigDecimal("5.4"), nextParameter.getValues().get(0).getValueNumber());
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/ClinicalImpression", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainWithUriSearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Observation> resourceType = Observation.class;
        String queryString = "&part-of:Procedure.instantiates-uri=uri";

        queryParameters.put("part-of:Procedure.instantiates-uri", Collections.singletonList("uri"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("part-of", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("Procedure", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.URI, nextParameter.getType());
        assertEquals("instantiates-uri", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals("uri", nextParameter.getValues().get(0).getValueString());
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/Observation", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test
    public void testValidChainWithCompositeSearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<ClinicalImpression> resourceType = ClinicalImpression.class;
        String queryString = "&finding-ref:Observation.code-value-string=1234$test";

        queryParameters.put("finding-ref:Observation.code-value-string", Collections.singletonList("1234$test"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("finding-ref", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.COMPOSITE, nextParameter.getType());
        assertEquals("code-value-string", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        List<String> valueStrings = new ArrayList<>();
        for (QueryParameter component : nextParameter.getValues().get(0).getComponent()) {
            valueStrings.add(component.getValues().get(0).getValueCode());
            valueStrings.add(component.getValues().get(0).getValueString());
        }
        assertTrue(valueStrings.contains("1234"));
        assertTrue(valueStrings.contains("test"));
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com/ClinicalImpression", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainInvalidModifierException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("organization:identifier.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainBadSearchParameterException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient,Observation"));
        queryParameters.put("organization:Organization.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainBadSearchParameterException2() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("general-practitioner:Practitioner.badParm", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainNonReferenceTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("name.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainBadModifierResourceTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Condition,Observation"));
        queryParameters.put("subject:Condition.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainNoModifierResourceTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Condition,Observation"));
        queryParameters.put("subject.name", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainReferenceLogicalIdException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Condition,Observation"));
        queryParameters.put("subject:Patient.general-practitioner", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainBadModifierLastParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("organization:Organization.name:badModifier", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainUnsupportedModifierLastParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("organization:Organization.name:type", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testWholeSystemChainResponseParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("organization:Organization._total", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test
    public void testWholeSystemValidChainWithTokenSearchParameter() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        FHIRSearchContext searchContext;
        Class<Resource> resourceType = Resource.class;
        String queryString = "&general-practitioner:Organization.active=true&_type=Patient";

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("general-practitioner:Organization.active", Collections.singletonList("true"));
        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        assertNotNull(searchContext);
        List<QueryParameter> searchParameters = searchContext.getSearchParameters();
        assertNotNull(searchParameters);
        assertEquals(1, searchParameters.size());
        assertEquals(1, searchParameters.get(0).getChain().size());

        assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
        assertEquals("general-practitioner", searchParameters.get(0).getCode());
        assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
        assertEquals("Organization", searchParameters.get(0).getModifierResourceTypeName());
        assertNotNull(searchParameters.get(0).getNextParameter());
        assertEquals(0, searchParameters.get(0).getValues().size());
        assertTrue(searchParameters.get(0).isChained());
        assertFalse(searchParameters.get(0).isReverseChained());
        assertFalse(searchParameters.get(0).isInclusionCriteria());

        QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
        assertEquals(Type.TOKEN, nextParameter.getType());
        assertEquals("active", nextParameter.getCode());
        assertEquals(null, nextParameter.getModifier());
        assertEquals(null, nextParameter.getModifierResourceTypeName());
        assertNull(nextParameter.getNextParameter());
        assertEquals("true", nextParameter.getValues().get(0).getValueCode());
        assertFalse(nextParameter.isChained());
        assertFalse(nextParameter.isReverseChained());
        assertFalse(nextParameter.isInclusionCriteria());

        String selfUri = SearchHelper.buildSearchSelfUri("http://example.com", searchContext);
        assertTrue(selfUri.contains(queryString));
    }

}
