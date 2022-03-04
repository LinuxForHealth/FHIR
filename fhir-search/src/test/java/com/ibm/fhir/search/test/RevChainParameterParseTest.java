/*
 * (C) Copyright IBM Corp. 2020, 2022
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

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This TestNG test class contains methods that test the parsing of reverse chain search parameters
 * (_has) in the SearchUtil class.
 */
public class RevChainParameterParseTest extends BaseSearchTest {

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainWithTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Resource> resourceType = Resource.class;

        queryParameters.put("_type", Collections.singletonList("Patient"));
        queryParameters.put("_has:Observation:subject:code", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainParseException1() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainParseException2() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Observation", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainParseException3() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:extra:_has:Encounter:reason-reference:status", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainBadReferenceTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:BadType:subject:code", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainBadReferenceSearchParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:badSearchParm:code", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainBadSearchParmParseException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:badSearchParm", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainNonReferenceParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:code:code", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainNonMatchingTypeException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:encounter:code", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainBadModifierException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:code:badModifier", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainUnsupportedModifierException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:code:contains", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainResponseParmException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:_total", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainModifierNotAllowedException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:encounter:contains.status", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainTypeNotAllowedException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:code.status", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainTypeMismatchException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:part-of:Condition.status", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainTypeModifierMismatchException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:part-of.status", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainReferenceLogicalIdException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:performer", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testReverseChainChainedReferenceLogicalIdException() throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<>();
        Class<Patient> resourceType = Patient.class;

        queryParameters.put("_has:Procedure:subject:encounter:Encounter.diagnosis", Collections.singletonList("xxx"));
        searchHelper.parseQueryParameters(resourceType, queryParameters);
    }

  @Test
  public void testValidReverseChainWithTokenSearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:code=12345";

      queryParameters.put("_has:Observation:subject:code", Collections.singletonList("12345"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.TOKEN, nextParameter.getType());
      assertEquals("code", nextParameter.getCode());
      assertEquals(null, nextParameter.getModifier());
      assertEquals(null, nextParameter.getModifierResourceTypeName());
      assertNull(nextParameter.getNextParameter());
      assertEquals("12345", nextParameter.getValues().get(0).getValueCode());
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithSearchParameterModifier() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:code:missing=12345";

      queryParameters.put("_has:Observation:subject:code:missing", Collections.singletonList("12345"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.TOKEN, nextParameter.getType());
      assertEquals("code", nextParameter.getCode());
      assertEquals(Modifier.MISSING, nextParameter.getModifier());
      assertEquals(null, nextParameter.getModifierResourceTypeName());
      assertNull(nextParameter.getNextParameter());
      assertEquals("12345", nextParameter.getValues().get(0).getValueCode());
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainMultiple() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:_has:Encounter:reason-reference:location=Location/12345";

      queryParameters.put("_has:Observation:subject:_has:Encounter:reason-reference:location", Collections.singletonList("Location/12345"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(2, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.REFERENCE, nextParameter.getType());
      assertEquals("reason-reference", nextParameter.getCode());
      assertEquals(Modifier.TYPE, nextParameter.getModifier());
      assertEquals("Encounter", nextParameter.getModifierResourceTypeName());
      assertNotNull(nextParameter.getNextParameter());
      assertEquals(0, nextParameter.getValues().size());
      assertTrue(nextParameter.isChained());
      assertTrue(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      nextParameter = nextParameter.getNextParameter();
      assertEquals(Type.REFERENCE, nextParameter.getType());
      assertEquals("location", nextParameter.getCode());
      assertEquals(null, nextParameter.getModifier());
      assertEquals(null, nextParameter.getModifierResourceTypeName());
      assertNull(nextParameter.getNextParameter());
      List<String> valueStrings = new ArrayList<>();
      for (QueryParameterValue value : nextParameter.getValues()) {
          valueStrings.add(value.getValueString());
      }
      assertTrue(valueStrings.contains("Location/12345"));
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithChainSearchParm() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:performer:Practitioner.given=Sue";

      queryParameters.put("_has:Observation:subject:performer:Practitioner.given", Collections.singletonList("Sue"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(2, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.REFERENCE, nextParameter.getType());
      assertEquals("performer", nextParameter.getCode());
      assertEquals(Modifier.TYPE, nextParameter.getModifier());
      assertEquals("Practitioner", nextParameter.getModifierResourceTypeName());
      assertNotNull(nextParameter.getNextParameter());
      assertEquals(0, nextParameter.getValues().size());
      assertTrue(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      nextParameter = nextParameter.getNextParameter();
      assertEquals(Type.STRING, nextParameter.getType());
      assertEquals("given", nextParameter.getCode());
      assertEquals(null, nextParameter.getModifier());
      assertEquals(null, nextParameter.getModifierResourceTypeName());
      assertNull(nextParameter.getNextParameter());
      assertEquals("Sue", nextParameter.getValues().get(0).getValueString());
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithChainSearchParmMultiple() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:performer:Organization.endpoint:Endpoint.name=test";

      queryParameters.put("_has:Observation:subject:performer:Organization.endpoint.name", Collections.singletonList("test"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(3, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.REFERENCE, nextParameter.getType());
      assertEquals("performer", nextParameter.getCode());
      assertEquals(Modifier.TYPE, nextParameter.getModifier());
      assertEquals("Organization", nextParameter.getModifierResourceTypeName());
      assertNotNull(nextParameter.getNextParameter());
      assertEquals(0, nextParameter.getValues().size());
      assertTrue(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      nextParameter = nextParameter.getNextParameter();
      assertEquals(Type.REFERENCE, nextParameter.getType());
      assertEquals("endpoint", nextParameter.getCode());
      assertEquals(Modifier.TYPE, nextParameter.getModifier());
      assertEquals("Endpoint", nextParameter.getModifierResourceTypeName());
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
      assertEquals("test", nextParameter.getValues().get(0).getValueString());
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithChainSearchParmWithModifier() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:performer:Practitioner.given:exact=Sue";

      queryParameters.put("_has:Observation:subject:performer:Practitioner.given:exact", Collections.singletonList("Sue"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(2, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.REFERENCE, nextParameter.getType());
      assertEquals("performer", nextParameter.getCode());
      assertEquals(Modifier.TYPE, nextParameter.getModifier());
      assertEquals("Practitioner", nextParameter.getModifierResourceTypeName());
      assertNotNull(nextParameter.getNextParameter());
      assertEquals(0, nextParameter.getValues().size());
      assertTrue(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      nextParameter = nextParameter.getNextParameter();
      assertEquals(Type.STRING, nextParameter.getType());
      assertEquals("given", nextParameter.getCode());
      assertEquals(Modifier.EXACT, nextParameter.getModifier());
      assertEquals(null, nextParameter.getModifierResourceTypeName());
      assertNull(nextParameter.getNextParameter());
      assertEquals("Sue", nextParameter.getValues().get(0).getValueString());
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithStringSearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:value-string=12345";

      queryParameters.put("_has:Observation:subject:value-string", Collections.singletonList("12345"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.STRING, nextParameter.getType());
      assertEquals("value-string", nextParameter.getCode());
      assertEquals(null, nextParameter.getModifier());
      assertEquals(null, nextParameter.getModifierResourceTypeName());
      assertNull(nextParameter.getNextParameter());
      assertEquals("12345", nextParameter.getValues().get(0).getValueString());
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithNumberSearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:RiskAssessment:subject:probability=50.5";

      queryParameters.put("_has:RiskAssessment:subject:probability", Collections.singletonList("50.5"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("RiskAssessment", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
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

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithDateSearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
      // If the Instant has a millseconds value of exactly 0, then the toString() will not include milliseconds in the search query,
      // which will cause the lower/upper bound to include all milliseconds within the second, instead of just the exact millisecond.
      // For the purpose of this testcase, just ensure that there is a non-0 value for milliseconds, so the toString() includes
      // milliseconds in the search query.
      if (now.get(ChronoField.MILLI_OF_SECOND) == 0) {
          now = now.plusMillis(1);
      }
      String queryString = "&_has:RiskAssessment:subject:date=" + now.toString();

      queryParameters.put("_has:RiskAssessment:subject:date", Collections.singletonList(now.toString()));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("RiskAssessment", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
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

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithReferenceSearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:RiskAssessment:subject:condition=Condition/1";

      queryParameters.put("_has:RiskAssessment:subject:condition", Collections.singletonList("Condition/1"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("RiskAssessment", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
      assertFalse(searchParameters.get(0).isInclusionCriteria());

      QueryParameter nextParameter = searchParameters.get(0).getNextParameter();
      assertEquals(Type.REFERENCE, nextParameter.getType());
      assertEquals("condition", nextParameter.getCode());
      assertEquals(null, nextParameter.getModifier());
      assertEquals(null, nextParameter.getModifierResourceTypeName());
      assertNull(nextParameter.getNextParameter());
      List<String> valueStrings = new ArrayList<>();
      for (QueryParameterValue value : nextParameter.getValues()) {
          valueStrings.add(value.getValueString());
      }
      assertTrue(valueStrings.contains("Condition/1"));
      assertFalse(nextParameter.isChained());
      assertFalse(nextParameter.isReverseChained());
      assertFalse(nextParameter.isInclusionCriteria());

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithQuantitySearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:value-quantity=5.4";

      queryParameters.put("_has:Observation:subject:value-quantity", Collections.singletonList("5.4"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
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

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithUriSearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Procedure:subject:instantiates-uri=uri";

      queryParameters.put("_has:Procedure:subject:instantiates-uri", Collections.singletonList("uri"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Procedure", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
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

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

  @Test
  public void testValidReverseChainWithCompositeSearchParameter() throws Exception {
      Map<String, List<String>> queryParameters = new HashMap<>();
      FHIRSearchContext searchContext;
      Class<Patient> resourceType = Patient.class;
      String queryString = "&_has:Observation:subject:code-value-string=1234$test";

      queryParameters.put("_has:Observation:subject:code-value-string", Collections.singletonList("1234$test"));
      searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
      assertNotNull(searchContext);
      List<QueryParameter> searchParameters = searchContext.getSearchParameters();
      assertNotNull(searchParameters);
      assertEquals(1, searchParameters.size());
      assertEquals(1, searchParameters.get(0).getChain().size());

      assertEquals(Type.REFERENCE, searchParameters.get(0).getType());
      assertEquals("subject", searchParameters.get(0).getCode());
      assertEquals(Modifier.TYPE, searchParameters.get(0).getModifier());
      assertEquals("Observation", searchParameters.get(0).getModifierResourceTypeName());
      assertNotNull(searchParameters.get(0).getNextParameter());
      assertEquals(0, searchParameters.get(0).getValues().size());
      assertTrue(searchParameters.get(0).isChained());
      assertTrue(searchParameters.get(0).isReverseChained());
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

      String selfUri = SearchUtil.buildSearchSelfUri("http://example.com/Patient", searchContext);
      assertTrue(selfUri.contains(queryString));
  }

}
