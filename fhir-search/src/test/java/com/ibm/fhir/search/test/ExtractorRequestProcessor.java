/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.Map;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.spec.test.IExampleProcessor;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * Tests parameter extraction
 */
public class ExtractorRequestProcessor implements IExampleProcessor {
    private static SearchHelper searchHelper = new SearchHelper();

    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        try {
            Map<SearchParameter, List<FHIRPathNode>> output = searchHelper.extractParameterValues(resource, false);

            assertNotNull(output);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on " + jsonFile);
        }

    }
}
