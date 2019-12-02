/*
 * (C) Copyright IBM Corp. 2019
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
import com.ibm.fhir.search.util.SearchUtil;

/**
 * @author pbastide
 *
 */
public class ExtractorRequestProcessor implements IExampleProcessor {

    /*
     * (non-Javadoc)
     * @see com.ibm.fhir.model.spec.test.IExampleProcessor#process(java.lang.String,
     * com.ibm.fhir.model.resource.Resource)
     */
    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        try {
            Map<SearchParameter, List<FHIRPathNode>> output = SearchUtil.extractParameterValues(resource, false);

            assertNotNull(output);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed on " + jsonFile);
        }

    }
}
