/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.Map;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.spec.test.IExampleProcessor;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.search.util.SearchHelper;

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
