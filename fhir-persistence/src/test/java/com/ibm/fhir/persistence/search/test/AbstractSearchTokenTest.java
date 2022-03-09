/*
 * (C) Copyright IBM Corp. 2018, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;

/**
 * @see https://hl7.org/fhir/r4/search.html#token
 */
public abstract class AbstractSearchTokenTest extends AbstractPLSearchTest {

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicToken.json");
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("token");
    }

    @Test
    public void testSearchToken_string() throws Exception {
        assertSearchReturnsSavedResource("string", "testString");
        assertSearchReturnsSavedResource("string", "TESTSTRING");
        assertSearchReturnsSavedResource("string", "|testString");
        assertSearchReturnsSavedResource("string", "|TESTSTRING");
        assertSearchDoesntReturnSavedResource("string", "other");
    }

    @Test
    public void testSearchToken_boolean() throws Exception {
        // For token parameters on elements of type ContactPoint, uri, or boolean,
        // the presence of the pipe symbol SHALL NOT be used - only the [parameter]=[code] form is allowed
        assertSearchReturnsSavedResource("boolean", "true");
        assertSearchDoesntReturnSavedResource("boolean", "false");
    }

    @Test
    public void testSearchToken_boolean_chained() throws Exception {
        // For token parameters on elements of type ContactPoint, uri, or boolean,
        // the presence of the pipe symbol SHALL NOT be used - only the [parameter]=[code] form is allowed
        assertSearchReturnsComposition("subject:Basic.boolean", "true");
        assertSearchDoesntReturnComposition("subject:Basic.boolean", "false");
    }

    @Test
    public void testSearchToken_boolean_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("boolean", Collections.singletonList("true"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchToken_boolean_missing() throws Exception {
        assertSearchReturnsSavedResource("boolean:missing", "false");
        assertSearchDoesntReturnSavedResource("boolean:missing", "true");

        assertSearchReturnsSavedResource("missing-boolean:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-boolean:missing", "false");
    }

    @Test
    public void testSearchToken_boolean_in() throws Exception {
        assertSearchReturnsSavedResource("boolean:in", "http://hl7.org/fhir/ValueSet/special-values");
        assertSearchDoesntReturnSavedResource("missing-boolean:in", "http://hl7.org/fhir/ValueSet/special-values");
        assertSearchDoesntReturnSavedResource("boolean:in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_boolean_not_in() throws Exception {
        assertSearchDoesntReturnSavedResource("boolean:not-in", "http://hl7.org/fhir/ValueSet/special-values");
        assertSearchReturnsSavedResource("missing-boolean:not-in", "http://hl7.org/fhir/ValueSet/special-values");
        assertSearchReturnsSavedResource("boolean:not-in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_boolean_above() throws Exception {
        assertSearchReturnsSavedResource("boolean:above", "http://terminology.hl7.org/CodeSystem/special-values|true");
        assertSearchDoesntReturnSavedResource("boolean:above", "http://terminology.hl7.org/CodeSystem/special-values|false");
        assertSearchDoesntReturnSavedResource("missing-boolean:above", "http://terminology.hl7.org/CodeSystem/special-values|true");
    }

    @Test
    public void testSearchToken_boolean_below() throws Exception {
        assertSearchReturnsSavedResource("boolean:below", "http://terminology.hl7.org/CodeSystem/special-values|true");
        assertSearchDoesntReturnSavedResource("boolean:below", "http://terminology.hl7.org/CodeSystem/special-values|false");
        assertSearchDoesntReturnSavedResource("missing-boolean:below", "http://terminology.hl7.org/CodeSystem/special-values|true");
    }

    @Test
    public void testSearchToken_code() throws Exception {
        // target value is "code" with no implicit or explicit system

        assertSearchReturnsSavedResource("code", "code");
        assertSearchReturnsSavedResource("code", "CODE");
        assertSearchReturnsSavedResource("code", "|code");
        assertSearchReturnsSavedResource("code", "|CODE");
        assertSearchDoesntReturnSavedResource("code", "other");
        // system is case-sensitive
        assertSearchReturnsSavedResource("text-status", "http://hl7.org/fhir/narrative-status|extensions");
        assertSearchDoesntReturnSavedResource("text-status", "http://hl7.org/fhir/narrative-status|EXTENSIONS");

        assertSearchReturnsSavedResource("text-status", "http://hl7.org/fhir/narrative-status|");
        assertSearchDoesntReturnSavedResource("text-status", "http://hl7.org/fhir/narrative-status||");
        assertSearchDoesntReturnSavedResource("text-status", "system|");
    }

    @Test
    public void testSearchToken_code_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.code", "code");
        assertSearchReturnsComposition("subject:Basic.code", "|code");
        assertSearchDoesntReturnComposition("subject:Basic.code", "other");
    }

    @Test
    public void testSearchToken_code_not() throws Exception {
        assertSearchDoesntReturnSavedResource("code:not", "code");
        assertSearchDoesntReturnSavedResource("code:not", "|code");
        assertSearchReturnsSavedResource("code:not", "other");

        assertSearchReturnsSavedResource("missing-code:not", "code");
    }

    @Test
    public void testSearchToken_code_chained_not() throws Exception {
        assertSearchDoesntReturnComposition("subject:Basic.code:not", "code");
        assertSearchDoesntReturnComposition("subject:Basic.code:not", "|code");
        assertSearchReturnsComposition("subject:Basic.code:not", "other");

        assertSearchReturnsComposition("subject:Basic.missing-code:not", "code");
    }

    @Test
    public void testSearchToken_code_missing() throws Exception {
        assertSearchReturnsSavedResource("code:missing", "false");
        assertSearchDoesntReturnSavedResource("code:missing", "true");

        assertSearchReturnsSavedResource("missing-code:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-code:missing", "false");
    }

    @Test
    public void testSearchToken_code_missing_boolean() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("code:missing", Collections.singletonList("false"));
        queryParms.put("boolean", Collections.singletonList("true"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("code:missing", Collections.singletonList("true"));
        queryParms.put("boolean", Collections.singletonList("true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
    }

    @Test
    public void testSearchToken_code_missing_boolean_missing() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("code:missing", Collections.singletonList("false"));
        queryParms.put("boolean:missing", Collections.singletonList("false"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("code:missing", Collections.singletonList("false"));
        queryParms.put("boolean:missing", Collections.singletonList("true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
    }

    @Test
    public void testSearchToken_boolean_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.boolean:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.boolean:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-boolean:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-boolean:missing", "false");
    }

    @Test
    public void testSearchToken_code_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.code:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.code:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-code:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-code:missing", "false");
    }

    @Test
    public void testSearchToken_code_in() throws Exception {
        assertSearchReturnsSavedResource("text-status:in", "http://hl7.org/fhir/ValueSet/narrative-status");
        assertSearchDoesntReturnSavedResource("code:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchDoesntReturnSavedResource("missing-code:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchDoesntReturnSavedResource("code:in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_code_not_in() throws Exception {
        assertSearchDoesntReturnSavedResource("text-status:not-in", "http://hl7.org/fhir/ValueSet/narrative-status");
        assertSearchReturnsSavedResource("code:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchReturnsSavedResource("missing-code:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchReturnsSavedResource("code:not-in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_code_above() throws Exception {
        assertSearchReturnsSavedResource("text-status:above", "http://hl7.org/fhir/narrative-status|extensions");
        assertSearchDoesntReturnSavedResource("text-status:above", "http://hl7.org/fhir/narrative-status|empty");
        assertSearchDoesntReturnSavedResource("missing-code:above", "http://hl7.org/fhir/narrative-status|extensions");
    }

    @Test
    public void testSearchToken_code_below() throws Exception {
        assertSearchReturnsSavedResource("text-status:below", "http://hl7.org/fhir/narrative-status|extensions");
        assertSearchDoesntReturnSavedResource("text-status:below", "http://hl7.org/fhir/narrative-status|empty");
        assertSearchDoesntReturnSavedResource("missing-code:below", "http://hl7.org/fhir/narrative-status|extensions");
    }

    @Test
    public void testSearchToken_CodeableConcept() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept", "code");
        assertSearchReturnsSavedResource("CodeableConcept", "CODE");
        // system is not in registry - treated as not case-sensitive
        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|code");
        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|CODE");
        // system is case-sensitive
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|AA");
        assertSearchDoesntReturnSavedResource("CodeableConcept-validCodeAndSystem", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|aa");

        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|");
        assertSearchDoesntReturnSavedResource("CodeableConcept", "http://example.org/codesystem||");
        assertSearchDoesntReturnSavedResource("CodeableConcept", "example.org/codesystem|");
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|");

        // This shouldn't return any results because the CodeableConcept has a system
        assertSearchDoesntReturnSavedResource("CodeableConcept", "|code");
    }

    @Test
    public void testSearchToken_CodeableConcept_or() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept", "foo,code,bar");
        assertSearchDoesntReturnSavedResource("CodeableConcept", "foo\\,code,bar");
        assertSearchDoesntReturnSavedResource("CodeableConcept", "foo,code\\,bar");
    }

    @Test
    public void testSearchToken_CodeableConcept_escaped() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|code");
        assertSearchDoesntReturnSavedResource("CodeableConcept", "http://example.org/codesystem\\|code");
    }

    @Test
    public void testSearchToken_CodeableConcept_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.CodeableConcept", "code");
        assertSearchReturnsComposition("subject:Basic.CodeableConcept", "http://example.org/codesystem|code");
        assertSearchReturnsComposition("subject:Basic.CodeableConcept", "http://example.org/codesystem|");

        // This shouldn't return any results because the CodeableConcept has a system
        assertSearchDoesntReturnComposition("subject:Basic.CodeableConcept", "|code");
    }

    @Test
    public void testSearchToken_CodeableConcept_multiCoded() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept-multiCoded", "http://example.org/codesystem|code");
        assertSearchReturnsSavedResource("CodeableConcept-multiCoded", "http://example.org/othersystem|code");
    }

    // Currently CodeableConcepts with no code are skipped
//    @Test
//    public void testSearchToken_CodeableConcept_NoCode() throws Exception {
//        assertSearchReturnsSavedResource("CodeableConcept-noCode", "http://example.org/codesystem|");
//    }

    @Test
    public void testSearchToken_CodeableConcept_not() throws Exception {
        assertSearchDoesntReturnSavedResource("CodeableConcept:not", "code");
        assertSearchDoesntReturnSavedResource("CodeableConcept:not", "http://example.org/codesystem|code");
        assertSearchReturnsSavedResource("CodeableConcept:not", "other");
        assertSearchReturnsSavedResource("CodeableConcept:not", "http://example.org/other|code");
        assertSearchReturnsSavedResource("missing-CodeableConcept:not", "code");
        assertSearchReturnsSavedResource("missing-CodeableConcept:not", "http://example.org/codesystem|code");
    }

    @Test
    public void testSearchToken_CodeableConcept_chained_not() throws Exception {
        assertSearchDoesntReturnComposition("subject:Basic.CodeableConcept:not", "code");
        assertSearchDoesntReturnComposition("subject:Basic.CodeableConcept:not", "http://example.org/codesystem|code");
        assertSearchReturnsComposition("subject:Basic.CodeableConcept:not", "other");
        assertSearchReturnsComposition("subject:Basic.CodeableConcept:not", "http://example.org/other|code");

        assertSearchReturnsComposition("subject:Basic.missing-CodeableConcept:not", "code");
        assertSearchReturnsComposition("subject:Basic.missing-CodeableConcept:not", "http://example.org/codesystem|code");
    }

    @Test
    public void testSearchToken_CodeableConcept_missing() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept:missing", "false");
        assertSearchDoesntReturnSavedResource("CodeableConcept:missing", "true");

        assertSearchReturnsSavedResource("missing-CodeableConcept:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:missing", "false");
    }

    @Test
    public void testSearchToken_CodeableConcept_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.CodeableConcept:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.CodeableConcept:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-CodeableConcept:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-CodeableConcept:missing", "false");
    }

    @Test
    public void testSearchToken_CodeableConcept_in() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:in", "http://hl7.org/fhir/ValueSet/observation-interpretation");
        assertSearchDoesntReturnSavedResource("CodeableConcept:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
    }

    @Test
    public void testSearchToken_CodeableConcept_not_in() throws Exception {
        assertSearchDoesntReturnSavedResource("CodeableConcept-validCodeAndSystem:not-in", "http://hl7.org/fhir/ValueSet/observation-interpretation");
        assertSearchReturnsSavedResource("CodeableConcept:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchReturnsSavedResource("missing-CodeableConcept:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
    }

    @Test
    public void testSearchToken_CodeableConcept_above() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:above", "http://hl7.org/fhir/issue-type|deleted");
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:above", "http://hl7.org/fhir/issue-type|not-found");
        assertSearchDoesntReturnSavedResource("CodeableConcept-validCodeAndSystem:above", "http://hl7.org/fhir/issue-type|processing");
        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:above", "http://hl7.org/fhir/issue-type|not-found");

        // Note:  https://terminology.hl7.org/CodeSystem-v3-ObservationValue.html is the one HL7 CodeSystem I found that is both
        // hierarchical AND case-insensitive, but unfortunately that one isn't included in FHIR R4B by default
    }

    @Test
    public void testSearchToken_CodeableConcept_below() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:below", "http://hl7.org/fhir/issue-type|processing");
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:below", "http://hl7.org/fhir/issue-type|not-found");
        assertSearchDoesntReturnSavedResource("CodeableConcept-validCodeAndSystem:below", "http://hl7.org/fhir/issue-type|deleted");
        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:below", "http://hl7.org/fhir/issue-type|not-found");
    }

    /**
     * v3-ObservationInterpretation is a "polyhierarchy" codesystem; we need https://github.com/IBM/FHIR/issues/3448 for that one
     */
    @Test(enabled = false)
    public void testSearchToken_CodeableConcept_above_poly() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|LL");
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|AA");
        assertSearchDoesntReturnSavedResource("CodeableConcept-validCodeAndSystem:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|A");
        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|LL");
    }

    /**
     * v3-ObservationInterpretation is a "polyhierarchy" codesystem; we need https://github.com/IBM/FHIR/issues/3448 for that one
     */
    @Test(enabled = false)
    public void testSearchToken_CodeableConcept_below_poly() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|A");
        assertSearchReturnsSavedResource("CodeableConcept-validCodeAndSystem:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|AA");
        assertSearchDoesntReturnSavedResource("CodeableConcept-validCodeAndSystem:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|LL");
        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|A");
    }

    @Test
    public void testSearchToken_Coding() throws Exception {
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|");


        assertSearchReturnsSavedResource("Coding", "code");
        assertSearchReturnsSavedResource("Coding", "CODE");
        // system not in registry - treated as not case-sensitive
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|CODE");
        // system is case-sensitive
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem", "http://hl7.org/fhir/issue-type|not-found");
        assertSearchDoesntReturnSavedResource("Coding-validCodeAndSystem", "http://hl7.org/fhir/issue-type|NOT-FOUND");
        // system is not case-sensitive
        assertSearchReturnsSavedResource("Coding-validCaseInsensitiveCodeAndSystem", "http://terminology.hl7.org/CodeSystem/v2-0535|S");
        assertSearchReturnsSavedResource("Coding-validCaseInsensitiveCodeAndSystem", "http://terminology.hl7.org/CodeSystem/v2-0535|s");

        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|");
        assertSearchDoesntReturnSavedResource("Coding", "http://example.org/codesystem||");
        assertSearchDoesntReturnSavedResource("Coding", "Http://example.org/codesystem|");
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem", "http://hl7.org/fhir/issue-type|");

        // This shouldn't return any results because the Coding has a system
        assertSearchDoesntReturnSavedResource("Coding", "|code");
    }

    @Test
    public void testSearchToken_Coding_or() throws Exception {
        assertSearchReturnsSavedResource("Coding", "foo,code,bar");
        assertSearchDoesntReturnSavedResource("Coding", "foo\\,code,bar");
        assertSearchDoesntReturnSavedResource("Coding", "foo,code\\,bar");
    }

    @Test
    public void testSearchToken_Coding_escaped() throws Exception {
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
        assertSearchDoesntReturnSavedResource("Coding", "http://example.org/codesystem\\|code");
    }

    @Test
    public void testSearchToken_Coding_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Coding", "code");
        assertSearchReturnsComposition("subject:Basic.Coding", "http://example.org/codesystem|code");
        assertSearchReturnsComposition("subject:Basic.Coding", "http://example.org/codesystem|");

        // This shouldn't return any results because the Coding has a system
        assertSearchDoesntReturnComposition("subject:Basic.Coding", "|code");
    }

    @Test
    public void testSearchToken_Coding_NoSystem() throws Exception {
        // target value is a coding with code "code" and no system
        assertSearchReturnsSavedResource("Coding-noSystem", "code");
        assertSearchReturnsSavedResource("Coding-noSystem", "CODE");
        assertSearchReturnsSavedResource("Coding-noSystem", "|code");
        assertSearchReturnsSavedResource("Coding-noSystem", "|CODE");
    }

    // Currently codings with no code are skipped
//    @Test
//    public void testSearchToken_Coding_NoCode() throws Exception {
//        assertSearchReturnsSavedResource("Coding-noCode", "http://example.org/codesystem|");
//    }

    @Test
    public void testSearchToken_Coding_not() throws Exception {
        assertSearchDoesntReturnSavedResource("Coding:not", "code");
        assertSearchDoesntReturnSavedResource("Coding:not", "http://example.org/codesystem|code");
        assertSearchReturnsSavedResource("Coding:not", "other");
        assertSearchReturnsSavedResource("Coding:not", "http://example.org/other|code");
        assertSearchReturnsSavedResource("missing-Coding:not", "code");
        assertSearchReturnsSavedResource("missing-Coding:not", "http://example.org/codesystem|code");
    }

    @Test
    public void testSearchToken_Coding_chained_not() throws Exception {
        assertSearchDoesntReturnComposition("subject:Basic.Coding:not", "code");
        assertSearchDoesntReturnComposition("subject:Basic.Coding:not", "http://example.org/codesystem|code");
        assertSearchReturnsComposition("subject:Basic.Coding:not", "other");
        assertSearchReturnsComposition("subject:Basic.Coding:not", "http://example.org/other|code");

        assertSearchReturnsComposition("subject:Basic.missing-Coding:not", "code");
        assertSearchReturnsComposition("subject:Basic.missing-Coding:not", "http://example.org/codesystem|code");
    }

    @Test
    public void testSearchToken_Coding_missing() throws Exception {
        assertSearchReturnsSavedResource("Coding:missing", "false");
        assertSearchDoesntReturnSavedResource("Coding:missing", "true");

        assertSearchReturnsSavedResource("missing-Coding:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Coding:missing", "false");
    }

    @Test
    public void testSearchToken_Coding_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Coding:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.Coding:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-Coding:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-Coding:missing", "false");
    }

    @Test
    public void testSearchToken_Coding_in() throws Exception {
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:in", "http://hl7.org/fhir/ValueSet/issue-type");
        assertSearchDoesntReturnSavedResource("Coding:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchDoesntReturnSavedResource("missing-Coding:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
    }

    @Test
    public void testSearchToken_Coding_not_in() throws Exception {
        assertSearchDoesntReturnSavedResource("Coding-validCodeAndSystem:not-in", "http://hl7.org/fhir/ValueSet/issue-type");
        assertSearchReturnsSavedResource("Coding:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchReturnsSavedResource("missing-Coding:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
    }

    @Test
    public void testSearchToken_Coding_above() throws Exception {
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:above", "http://hl7.org/fhir/issue-type|deleted");
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:above", "http://hl7.org/fhir/issue-type|not-found");
        assertSearchDoesntReturnSavedResource("Coding-validCodeAndSystem:above", "http://hl7.org/fhir/issue-type|processing");
        assertSearchDoesntReturnSavedResource("Coding-noSystem:above", "http://hl7.org/fhir/concept-property-type|code");
        assertSearchDoesntReturnSavedResource("missing-Coding:above", "http://hl7.org/fhir/issue-type|deleted");
    }

    @Test
    public void testSearchToken_Coding_below() throws Exception {
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:below", "http://hl7.org/fhir/issue-type|processing");
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:below", "http://hl7.org/fhir/issue-type|not-found");
        assertSearchDoesntReturnSavedResource("Coding-validCodeAndSystem:below", "http://hl7.org/fhir/issue-type|deleted");
        assertSearchDoesntReturnSavedResource("Coding-noSystem:above", "http://hl7.org/fhir/concept-property-type|code");
        assertSearchDoesntReturnSavedResource("missing-Coding:below", "http://hl7.org/fhir/issue-type|processing");
    }

    /**
     * v3-ObservationInterpretation is a "polyhierarchy" codesystem; we need https://github.com/IBM/FHIR/issues/3448 for that one
     */
    @Test(enabled = false)
    public void testSearchToken_Coding_above_poly() throws Exception {
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|LL");
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|AA");
        assertSearchDoesntReturnSavedResource("Coding-validCodeAndSystem:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|A");
        assertSearchDoesntReturnSavedResource("Coding-noSystem:above", "http://hl7.org/fhir/concept-property-type|code");
        assertSearchDoesntReturnSavedResource("missing-Coding:above", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|LL");
    }

    /**
     * v3-ObservationInterpretation is a "polyhierarchy" codesystem; we need https://github.com/IBM/FHIR/issues/3448 for that one
     */
    @Test(enabled = false)
    public void testSearchToken_Coding_below_poly() throws Exception {
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|A");
        assertSearchReturnsSavedResource("Coding-validCodeAndSystem:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|AA");
        assertSearchDoesntReturnSavedResource("Coding-validCodeAndSystem:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|LL");
        assertSearchDoesntReturnSavedResource("Coding-noSystem:above", "http://hl7.org/fhir/concept-property-type|code");
        assertSearchDoesntReturnSavedResource("missing-Coding:below", "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation|A");
    }

    @Test
    public void testSearchToken_Coding_NoSystem_in() throws Exception {
        assertSearchDoesntReturnSavedResource("Coding-noSystem:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchDoesntReturnSavedResource("Coding-noSystem:in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_Coding_NoSystem_not_in() throws Exception {
        assertSearchReturnsSavedResource("Coding-noSystem:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchReturnsSavedResource("Coding-noSystem:not-in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_Identifier() throws Exception {
        assertSearchReturnsSavedResource("Identifier", "code");
        assertSearchReturnsSavedResource("Identifier", "CODE");
        // system is not in registry - treated as not case-sensitive
        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|code");
        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|CODE");
        // system is case-sensitive
        assertSearchReturnsSavedResource("Identifier-validValueAndSystem", "http://hl7.org/fhir/identifier-use|official");
        assertSearchDoesntReturnSavedResource("Identifier-validValueAndSystem", "http://hl7.org/fhir/identifier-use|OFFICIAL");

        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|");
        assertSearchDoesntReturnSavedResource("Identifier", "http://example.org/identifiersystem||");
        assertSearchDoesntReturnSavedResource("Identifier", "http://example.org/IdentifierSystem|");
        assertSearchReturnsSavedResource("Identifier-validValueAndSystem", "http://hl7.org/fhir/identifier-use|");

        // This shouldn't return any results because the Identifier has a system
        assertSearchDoesntReturnSavedResource("Identifier", "|code");
    }

    @Test
    public void testSearchToken_Identifier_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Identifier", "code");
        assertSearchReturnsComposition("subject:Basic.Identifier", "http://example.org/identifiersystem|code");
        assertSearchReturnsComposition("subject:Basic.Identifier", "http://example.org/identifiersystem|");

        // This shouldn't return any results because the Identifier has a system
        assertSearchDoesntReturnComposition("subject:Basic.Identifier", "|code");
    }

    @Test
    public void testSearchToken_Identifier_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("Identifier-noSystem", "code");
        assertSearchReturnsSavedResource("Identifier-noSystem", "|code");
    }

    // Currently identifiers with no value are skipped
//    @Test
//    public void testSearchToken_Identifier_NoValue() throws Exception {
//        assertSearchReturnsSavedResource("Identifier-noValue", "http://example.org/identifiersystem|");
//    }

    @Test
    public void testSearchToken_Identifier_not() throws Exception {
        assertSearchDoesntReturnSavedResource("Identifier:not", "code");
        assertSearchDoesntReturnSavedResource("Identifier:not", "http://example.org/identifiersystem|code");
        assertSearchReturnsSavedResource("Identifier:not", "other");
        assertSearchReturnsSavedResource("Identifier:not", "http://example.org/other|code");

        assertSearchReturnsSavedResource("missing-Identifier:not", "code");
        assertSearchReturnsSavedResource("missing-Identifier:not", "http://example.org/identifiersystem|code");
    }

    @Test
    public void testSearchToken_Identifier_chained_not() throws Exception {
        assertSearchDoesntReturnComposition("subject:Basic.Identifier:not", "code");
        assertSearchDoesntReturnComposition("subject:Basic.Identifier:not", "http://example.org/identifiersystem|code");
        assertSearchReturnsComposition("subject:Basic.Identifier:not", "other");
        assertSearchReturnsComposition("subject:Basic.Identifier:not", "http://example.org/other|code");

        assertSearchReturnsComposition("subject:Basic.missing-Identifier:not", "code");
        assertSearchReturnsComposition("subject:Basic.missing-Identifier:not", "http://example.org/identifiersystem|code");
    }

    @Test
    public void testSearchToken_Identifier_missing() throws Exception {
        assertSearchReturnsSavedResource("Identifier:missing", "false");
        assertSearchDoesntReturnSavedResource("Identifier:missing", "true");

        assertSearchReturnsSavedResource("missing-Identifier:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Identifier:missing", "false");
    }

    @Test
    public void testSearchToken_Identifier_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Identifier:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.Identifier:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-Identifier:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-Identifier:missing", "false");
    }

    @Test
    public void testSearchToken_Identifier_in() throws Exception {
        assertSearchReturnsSavedResource("Identifier-validValueAndSystem:in", "http://hl7.org/fhir/ValueSet/identifier-use");
        assertSearchDoesntReturnSavedResource("Identifier:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchDoesntReturnSavedResource("missing-Identifier:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
    }

    @Test
    public void testSearchToken_Identifier_not_in() throws Exception {
        assertSearchDoesntReturnSavedResource("Identifier-validValueAndSystem:not-in", "http://hl7.org/fhir/ValueSet/identifier-use");
        assertSearchReturnsSavedResource("Identifier:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchReturnsSavedResource("missing-Identifier:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
    }

    @Test
    public void testSearchToken_Identifier_NoSystem_in() throws Exception {
        assertSearchDoesntReturnSavedResource("Identifier-noSystem:in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchDoesntReturnSavedResource("Identifier-noSystem:in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_Identifier_NoSystem_not_in() throws Exception {
        assertSearchReturnsSavedResource("Identifier-noSystem:not-in", "http://hl7.org/fhir/ValueSet/concept-property-type");
        assertSearchReturnsSavedResource("Identifier-noSystem:not-in", "http://hl7.org/fhir/ValueSet/observation-category");
    }

    @Test
    public void testSearchToken_Identifier_of_type() throws Exception {
        // system is case-sensitive
        assertSearchReturnsSavedResource("Identifier-validType:of-type", "http://terminology.hl7.org/CodeSystem/v2-0203|MR|code");
        assertSearchDoesntReturnSavedResource("Identifier-validType:of-type", "http://terminology.hl7.org/CodeSystem/v2-0203|mr|code");
        assertSearchDoesntReturnSavedResource("Identifier-validType:of-type", "http://terminology.hl7.org/CodeSystem/v2-0203|MR|badcode");
    }

    @Test
    public void testSearchToken_ContactPoint() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint", "(555) 675 5745");
    }

    @Test
    public void testSearchToken_ContactPoint_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.ContactPoint", "(555) 675 5745");
    }

    @Test
    public void testSearchToken_ContactPoint_URI() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-uri", "tel:+15556755745");
    }

    @Test
    public void testSearchToken_ContactPoint_HomeFax() throws Exception {

        assertSearchReturnsSavedResource("ContactPoint-homeFax", "(555) 675 5745");
    }

    @Test
    public void testSearchToken_ContactPoint_NoUse() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noUse", "test@example.com");
    }

    @Test
    public void testSearchToken_ContactPoint_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "test@example.com");
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "|test@example.com");
    }

    @Test
    public void testSearchToken_ContactPoint_not() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint:not", "(555) 555 5555");
        assertSearchDoesntReturnSavedResource("ContactPoint:not", "(555) 675 5745");
        assertSearchReturnsSavedResource("missing-ContactPoint:not", "(555) 555 5555");
    }

    @Test
    public void testSearchToken_ContactPoint_chained_not() throws Exception {
        assertSearchReturnsComposition("subject:Basic.ContactPoint:not", "(555) 555 5555");
        assertSearchDoesntReturnComposition("subject:Basic.ContactPoint:not", "(555) 675 5745");
        assertSearchReturnsComposition("subject:Basic.missing-ContactPoint:not", "(555) 555 5555");
    }

    @Test
    public void testSearchToken_ContactPoint_missing() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint:missing", "false");
        assertSearchDoesntReturnSavedResource("ContactPoint:missing", "true");

        assertSearchReturnsSavedResource("missing-ContactPoint:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-ContactPoint:missing", "false");
    }

    @Test
    public void testSearchToken_ContactPoint_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.ContactPoint:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.ContactPoint:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-ContactPoint:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-ContactPoint:missing", "false");
    }

}
