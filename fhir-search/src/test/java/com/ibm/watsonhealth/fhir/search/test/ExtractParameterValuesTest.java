/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareAbstractVisitor;
import com.ibm.watsonhealth.fhir.search.test.ExtractorValidator.Builder;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 * Extract Parameter Values Test
 * 
 * @author pbastide
 *
 */
public class ExtractParameterValuesTest extends BaseSearchTest {
    
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
    private void runTest(String file, Class<? extends Resource> cls, boolean debug, ExtractorValidator validator, boolean skip) throws Exception {
        // Are you debugging....
        // Set to true to enable, false to turn off
        PathAwareAbstractVisitor.DEBUG = debug;

        try (InputStream stream = ExtractParameterValuesTest.class.getResourceAsStream("/testdata/" + file)) {
            Resource observation = FHIRUtil.read(cls, Format.JSON, new InputStreamReader(stream));

            Map<SearchParameter, List<FHIRPathNode>> output;
            if (skip) {
                output = SearchUtil.extractParameterValues(observation, false);
            } else {
                output = SearchUtil.extractParameterValues(observation);
            }
            validator.validate(output);
        } catch (Exception e) {
            Assert.fail("Failed to process the test", e);
        }
    }

    private void runTest(String file, Class<? extends Resource> cls, boolean debug, ExtractorValidator validator) throws Exception {
        runTest(file, Observation.class, false, validator, false);
    }

    @Test
    public void testMinimumObservationWithAllSearchParameters() throws Exception {
        String testFile = "observation-empty.json";

        Builder builder = ExtractorValidator.Builder.builder();
        builder.add("code", "Diplotype Call");
        builder.add("combo-code", "Diplotype Call");
        builder.add("status", "final");
        builder.strict(true);

        runTest(testFile, Observation.class, false, builder.build());

    }

    @Test
    public void testMinimumObservationWithAllSearchParametersNoSkip() throws Exception {
        String testFile = "observation-empty.json";

        Builder builder = ExtractorValidator.Builder.builder();
        builder.add("code", "Diplotype Call");
        builder.add("combo-code", "Diplotype Call");
        builder.add("status", "final");
        builder.strict(false);

        runTest(testFile, Observation.class, false, builder.build(), true);

    }

    @Test
    public void testSomeObservationWithAllSearchParameters() throws Exception {
        String testFile = "observation-some.json";

        Builder builder = ExtractorValidator.Builder.builder();
        builder.add("_id", "example-diplotype1").strict(false);
        builder.add("_lastUpdated", "2018-12-27T22:37:54.724+11:00");

        runTest(testFile, Observation.class, false, builder.build());

    }

    @Test
    public void testFullObservationWithAllSearchParameters() throws Exception {
        String testFile = "observation-full.json";

        Builder builder = ExtractorValidator.Builder.builder();
        builder.add("_id", "example-diplotype1").strict(false);
        builder.add("_lastUpdated", "2018-12-27T22:37:54.724+11:00");

        runTest(testFile, Observation.class, false, builder.build());

    }
    
    @Test
    public void testWithEmptyExpressionAndTenant() throws Exception {
        // Looking only for built-in search parameters for "Patient".

        // Use tenant1 since it doesn't have any tenant-specific search parameters for resourceType Medication.
        FHIRRequestContext.set(new FHIRRequestContext("tenant5"));

        String testFile = "observation-some.json";

        Builder builder = ExtractorValidator.Builder.builder();
        builder.strict(false);
        
        runTest(testFile, Observation.class, false, builder.build());
    }

}
