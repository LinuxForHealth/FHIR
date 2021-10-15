/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * Test to verify the parameter_names.properties
 */
public class PopulateParameterNamesTest {
    private static final Logger LOGGER = Logger.getLogger(PopulateParameterNamesTest.class.getName());
    /**
     * This method is very intentional to verify the parameter_names.properties on EVERY build.
     * The mapping is intentionally managed, as these KEYS are inserted and used.
     */
    @Test
    public static void verifyParameterNames() throws IOException {

        Set<String> parameterNames = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(PopulateParameterNames.class.getClassLoader().getResourceAsStream("parameter_names.properties"), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String parameterName = line.strip();
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
                    fail("parameter_names.properties is missing parameter: " + searchParameter.toString());
                }
            }
        }
    }
}