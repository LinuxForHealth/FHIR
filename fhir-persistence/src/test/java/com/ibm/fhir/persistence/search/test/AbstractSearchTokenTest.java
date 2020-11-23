/*
 * (C) Copyright IBM Corp. 2018, 2020
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
 * @author lmsurpre
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
        assertSearchReturnsSavedResource("string", "|testString");
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
    public void testSearchToken_code() throws Exception {
        assertSearchReturnsSavedResource("code", "code");
        assertSearchReturnsSavedResource("code", "|code");
    }

    @Test
    public void testSearchToken_code_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.code", "code");
        assertSearchReturnsComposition("subject:Basic.code", "|code");
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
    public void testSearchToken_CodeableConcept_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.CodeableConcept:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.CodeableConcept:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-CodeableConcept:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-CodeableConcept:missing", "false");
    }

    @Test
    public void testSearchToken_CodeableConcept() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept", "code");
        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|code");
//        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|");

        // This shouldn't return any results because the CodeableConcept has a system
//        assertSearchDoesntReturnSavedResource("CodeableConcept", "|code");
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
//        assertSearchReturnsComposition("subject:Basic.CodeableConcept", "http://example.org/codesystem|");

        // This shouldn't return any results because the CodeableConcept has a system
//        assertSearchDoesntReturnComposition("subject:Basic.CodeableConcept", "|code");
    }

    @Test
    public void testSearchDate_CodeableConcept_multiCoded() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept-multiCoded", "http://example.org/codesystem|code");
        assertSearchReturnsSavedResource("CodeableConcept-multiCoded", "http://example.org/othersystem|code");
    }

    // Currently CodeableConcepts with no code are skipped
//    @Test
//    public void testSearchDate_CodeableConcept_NoCode() throws Exception {
//        assertSearchReturnsSavedResource("CodeableConcept-noCode", "http://example.org/codesystem|");
//    }

    @Test
    public void testSearchToken_CodeableConcept_missing() throws Exception {
        assertSearchReturnsSavedResource("CodeableConcept:missing", "false");
        assertSearchDoesntReturnSavedResource("CodeableConcept:missing", "true");

        assertSearchReturnsSavedResource("missing-CodeableConcept:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:missing", "false");
    }

    @Test
    public void testSearchToken_Coding() throws Exception {
        assertSearchReturnsSavedResource("Coding", "code");
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
//        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|");

        // This shouldn't return any results because the Coding has a system
//        assertSearchDoesntReturnSavedResource("Coding", "|code");
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
//        assertSearchReturnsComposition("subject:Basic.Coding", "http://example.org/codesystem|");

        // This shouldn't return any results because the Coding has a system
//        assertSearchDoesntReturnComposition("subject:Basic.Coding", "|code");
    }

    @Test
    public void testSearchDate_Coding_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("Coding-noSystem", "code");
        assertSearchReturnsSavedResource("Coding-noSystem", "|code");
    }

    // Currently codings with no code are skipped
//    @Test
//    public void testSearchDate_Coding_NoCode() throws Exception {
//        assertSearchReturnsSavedResource("Coding-noCode", "http://example.org/codesystem|");
//    }

    @Test
    public void testSearchToken_Coding_missing() throws Exception {
        assertSearchReturnsSavedResource("Coding:missing", "false");
        assertSearchDoesntReturnSavedResource("Coding:missing", "true");

        assertSearchReturnsSavedResource("missing-Coding:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Coding:missing", "false");
    }

    /*
     * Currently, documented in our conformance statement. We do not support
     * modifiers on chained parameters. https://ibm.github.io/FHIR/Conformance#search-modifiers
     * Refer to https://github.com/IBM/FHIR/issues/473 to track the issue.
     */

//    @Test
//    public void testSearchToken_Coding_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.Coding:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.Coding:missing", "true");
//
//        assertSearchReturnsComposition("subject:Basic.missing-Coding:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-Coding:missing", "false");
//    }

    @Test
    public void testSearchToken_Identifier() throws Exception {
        assertSearchReturnsSavedResource("Identifier", "code");
        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|code");
//        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|");

        // This shouldn't return any results because the Identifier has a system
//        assertSearchDoesntReturnSavedResource("Identifier", "|code");
    }

    @Test
    public void testSearchToken_Identifier_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Identifier", "code");
        assertSearchReturnsComposition("subject:Basic.Identifier", "http://example.org/identifiersystem|code");
//        assertSearchReturnsComposition("subject:Basic.Identifier", "http://example.org/identifiersystem|");

        // This shouldn't return any results because the Identifier has a system
//        assertSearchDoesntReturnComposition("subject:Basic.Identifier", "|code");
    }

    @Test
    public void testSearchDate_Identifier_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("Identifier-noSystem", "code");
        assertSearchReturnsSavedResource("Identifier-noSystem", "|code");
    }

    // Currently identifiers with no value are skipped
//    @Test
//    public void testSearchDate_Identifier_NoValue() throws Exception {
//        assertSearchReturnsSavedResource("Identifier-noValue", "http://example.org/identifiersystem|");
//    }

    @Test
    public void testSearchToken_Identifier_missing() throws Exception {
        assertSearchReturnsSavedResource("Identifier:missing", "false");
        assertSearchDoesntReturnSavedResource("Identifier:missing", "true");

        assertSearchReturnsSavedResource("missing-Identifier:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Identifier:missing", "false");
    }

    /*
     * Currently, documented in our conformance statement. We do not support
     * modifiers on chained parameters. https://ibm.github.io/FHIR/Conformance#search-modifiers
     * Refer to https://github.com/IBM/FHIR/issues/473 to track the issue.
     */

//    @Test
//    public void testSearchToken_Identifier_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.Identifier:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.Identifier:missing", "true");
//
//        assertSearchReturnsComposition("subject:Basic.missing-Identifier:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-Identifier:missing", "false");
//    }

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
    public void testSearchDate_ContactPoint_HomeFax() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-homeFax", "(555) 675 5745");
    }
    @Test
    public void testSearchDate_ContactPoint_NoUse() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noUse", "test@example.com");
    }
    @Test
    public void testSearchDate_ContactPoint_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "test@example.com");
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "|test@example.com");
    }
}
