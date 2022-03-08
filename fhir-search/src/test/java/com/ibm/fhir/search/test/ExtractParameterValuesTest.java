/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.InsurancePlan;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.search.test.ExtractorValidator.Builder;

/**
 * Extract Parameter Values Test
 *
 * @author pbastide
 *
 */
public class ExtractParameterValuesTest extends BaseSearchTest {

    @Override
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    /**
     * run test
     *
     * @param file
     * @param cls
     * @param debug
     * @param validator
     * @param skip
     * @throws Exception
     */
    public static void runTest(String file, Class<? extends Resource> cls, boolean debug, ExtractorValidator validator, boolean skip) throws Exception {
        try (InputStream stream = ExtractParameterValuesTest.class.getResourceAsStream("/testdata/" + file)) {
            Resource res = FHIRParser.parser(Format.JSON).parse(stream);

            Map<SearchParameter, List<FHIRPathNode>> output;
            if (skip) {
                output = searchHelper.extractParameterValues(res, false);
            } else {
                output = searchHelper.extractParameterValues(res);
            }
            validator.validate(output);
        } catch (Exception e) {
            Assert.fail("Failed to process the test", e);
        }
    }

    public static void runTest(String file, Class<? extends Resource> cls, boolean debug, ExtractorValidator validator) throws Exception {
        runTest(file, Observation.class, false, validator, false);
    }

    @Test
    public void testMinimumObservationWithAllSearchParameters() throws Exception {
        String testFile = "extract/observation-empty.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("code", "Diplotype Call");
        builder.add("combo-code", "Diplotype Call");
        builder.add("status", "final");
        builder.add("code-value-concept", "Observation");
        builder.add("code-value-date", "Observation");
        builder.add("code-value-quantity", "Observation");
        builder.add("code-value-string", "Observation");
        builder.add("combo-code-value-concept", "Observation");
        builder.add("combo-code-value-quantity", "Observation");
        builder.strict(true);

        runTest(testFile, Observation.class, false, builder.build());

    }

    @Test
    public void testMinimumObservationWithAllSearchParametersNoSkip() throws Exception {
        String testFile = "extract/observation-empty.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("code", "Diplotype Call");
        builder.add("combo-code", "Diplotype Call");
        builder.add("status", "final");
        builder.strict(false);

        runTest(testFile, Observation.class, false, builder.build(), true);

    }

    @Test
    public void testSomeObservationWithAllSearchParameters() throws Exception {
        String testFile = "extract/observation-some.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("_id", "example-diplotype1").strict(false);
        builder.add("_lastUpdated", "2018-12-27T22:37:54.724+11:00");

        runTest(testFile, Observation.class, false, builder.build());

    }

    @Test
    public void testFullObservationWithAllSearchParameters() throws Exception {
        String testFile = "extract/observation-full.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("_id", "example-diplotype1").strict(false);
        builder.add("_lastUpdated", "2018-12-27T22:37:54.724+11:00");

        runTest(testFile, Observation.class, false, builder.build());

    }

    @Test
    public void testWithEmptyExpressionAndTenant() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant5"));

        String testFile = "extract/observation-some.json";

        Builder builder = ExtractorValidator.builder();
        builder.strict(false);

        runTest(testFile, Observation.class, false, builder.build());
    }

    @Test
    public void testInsurancePlanName() throws Exception {
        // Insurance Plan: name
        // Without any context, the default insuranceplan is:
        // <code> "expression" : "name | alias",</code>
        // Normally the expression is as such.
        // <code>"expression" : "InsurancePlan.name | InsurancePlan.alias",</code>

        String testFile = "extract/insuranceplan-name.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("name", "test");
        builder.add("phonetic", "test");
        builder.strict(true);

        runTest(testFile, InsurancePlan.class, false, builder.build());
    }

    @Test
    public void testInsurancePlanAlias() throws Exception {
        // Insurance Plan: name
        // Without any context, the default insuranceplan is:
        // <code> "expression" : "name | alias",</code>
        // Normally the expression is as such.
        // <code>"expression" : "InsurancePlan.name | InsurancePlan.alias",</code>

        String testFile = "extract/insuranceplan-alias.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("name", "test");
        builder.strict(true);

        runTest(testFile, InsurancePlan.class, false, builder.build(), false);
    }

    @Test
    public void testPatientDeceasedBoolean() throws Exception {
        // search-parameters.json -> Patient: deceased

        String testFile = "extract/patient-deceased-boolean.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("deceased", "true");
        builder.strict(false);

        runTest(testFile, Patient.class, false, builder.build(), false);
    }

    @Test
    public void testPatientDeceasedTime() throws Exception {
        // search-parameters.json -> Patient: deceased

        String testFile = "extract/patient-deceased-time.json";

        Builder builder = ExtractorValidator.builder();
        builder.add("deceased", "true");
        builder.strict(false);

        runTest(testFile, Patient.class, false, builder.build(), false);
    }
}
