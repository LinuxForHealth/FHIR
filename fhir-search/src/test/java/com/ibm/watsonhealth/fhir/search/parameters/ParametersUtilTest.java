/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.parameters;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.search.test.BaseSearchTest;

/**
 * 
 * Tests ParametersUtil
 * 
 * @author pbastide
 *
 */
public class ParametersUtilTest extends BaseSearchTest {

    @Test
    public void testGetBuildInSearchParameterMap() {
        // Tests JSON
        Map<String, Map<String, SearchParameter>> params = ParametersUtil.getBuiltInSearchParameterMap();
        assertNotNull(params);
        ParametersUtil.print(System.out);
        assertEquals(134, params.size());
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testPopulateSearchParameterMapFromStreamXML() throws IOException {
        // Tests XML (once we support reading XML)
        try (InputStream stream = ParametersUtil.class.getClassLoader().getResourceAsStream("search-parameters.xml")) {
            Map<String, Map<String, SearchParameter>> params = ParametersUtil.populateSearchParameterMapFromStreamXML(stream);
            assertNotNull(params);
            ParametersUtil.print(System.out);
            assertEquals(134, params.size());
        }

    }

    @Test(expectedExceptions = {})
    public void testPopulateSearchParameterMapFromFile() throws IOException {
        File customSearchParams = new File("src/test/resources/config/tenant1/extension-search-parameters.json");
        System.out.println(customSearchParams.getAbsolutePath());
        Map<String, Map<String, SearchParameter>> params = ParametersUtil.populateSearchParameterMapFromFile(customSearchParams);
        System.out.println(params.keySet());

        // validates checks
        assertNotNull(params);
        assertFalse(params.isEmpty());
        assertEquals(params.size(), 3);

    }

    @Test
    public void testPrint() {
        // Test the output, OK, if it gets through.
        ParametersUtil.print(System.out);
        assertTrue(true);
    }

    @Test(expectedExceptions = {})
    public void testGetBuiltInSearchParameterMapByResourceTypeInvalid() {
        // getBuiltInSearchParameterMapByResourceType
        Map<String, SearchParameter> result = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Junk");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expectedExceptions = {})
    public void testGetBuiltInSearchParameterMapByResourceTypeValid() {
        // getBuiltInSearchParameterMapByResourceType
        Map<String, SearchParameter> result = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Observation");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test(expectedExceptions = { FileNotFoundException.class })
    public void testPopulateSearchParameterMapFromResourceNull() throws IOException {
        String invalidResourceName = "INVALID_RESOURCE";
        ParametersUtil.populateSearchParameterMapFromResource(invalidResourceName);
    }

    @Test
    public void testResourceDefaults() throws IOException {
        Map<String, SearchParameter> params1 = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Observation");
        Map<String, SearchParameter> params2 = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Resource");

        // Check that each returned "Resource" is included in the first set returned.
        assertNotNull(params1);
        assertNotNull(params2);
        assertEquals(params2.size(), 6);
        params2.keySet().stream().forEach(new Consumer<String>() {

            @Override
            public void accept(String resourceParam) {
                System.out.println("Checking Resource Param -> " + resourceParam);
                assertTrue(params1.containsKey(resourceParam));
            }

        });
    }

}
