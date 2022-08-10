/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.test;

import static org.linuxforhealth.fhir.search.test.ExtractParameterValuesTest.runTest;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.search.test.ExtractorValidator.Builder;

/**
 * Test the Tag | https://www.hl7.org/fhir/r4/search.html#all
 *
 * For instance - GET [base]/Condition?_tag=http://acme.org/codes|needs-review
 */
public class SearchTagTest {

    @Test
    public void testSomeObservationWithAllSearchParameters() throws Exception {
        String testFile = "extract/tag-test.json";

        Builder builder = ExtractorValidator.builder().strict(false);
        builder.add("_security", "security");
        builder.add("_tag", "tag");
        builder.add("_profile", "http://example.com/fhir/profile/Profile");

        runTest(testFile, Patient.class, true, builder.build());

    }

}
