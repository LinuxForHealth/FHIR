/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.type.code.SearchParamType;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

/**
 * Test to verify the parameter_names.properties
 */
public class PopulateParameterNamesTest {
    /**
     * This method is very intentional to verify the parameter_names.properties on EVERY build.
     * The mapping is intentionally managed, as these KEYS are inserted and used.
     */
    @Test
    public static void verifyParameterNames() throws IOException {

        // The parameter names were obtained by selecting all the parameter_name values
        // from the parameter_names table after a successful system integration test run.
        // Future runs of the system integration tests should no longer need to create
        // new rows, eliminating the possibility of deadlocks involving inserts to this 
        // table.
        Set<String> parameterNames = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(PopulateParameterNames.class.getClassLoader().getResourceAsStream("parameter_names.properties"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String parameterName = line.trim();
                parameterNames.add(parameterName);
            }
        }

        // Check that all the search parameters we know about are in the parameter_names file
        for (SearchParamType.Value spt : SearchParamType.Value.values()) {
            Collection<SearchParameter> searchParametersForResourceType =
                    FHIRRegistry.getInstance().getSearchParameters(spt.value());
            for (SearchParameter searchParameter : searchParametersForResourceType) {
                final String parameterName = searchParameter.getCode().getValue();
                if (!parameterNames.contains(parameterName)) {
                    fail("parameter_names.properties is missing parameter: " + searchParameter.getCode().getValue());
                }
            }
        }
    }
}