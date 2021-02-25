/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.io.IOException;
import java.io.InputStream;
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
    public static void verifyParameterNames() {
        Properties props = new Properties();
        boolean found = false;
        try (InputStream fis =
                PopulateParameterNames.class.getClassLoader().getResourceAsStream("parameter_names.properties")) {
            props.load(fis);

            Set<String> codes = new HashSet<>();
            for (SearchParamType.ValueSet spt : SearchParamType.ValueSet.values()) {
                Collection<SearchParameter> searchParametersForResourceType =
                        FHIRRegistry.getInstance().getSearchParameters(spt.value());
                for (SearchParameter searchParameter : searchParametersForResourceType) {
                    codes.add(searchParameter.getCode().getValue());
                }
            }

            // Find the Highest Value to start from:
            Integer highestValue = 1001;
            Map<String, Integer> valueMap = new HashMap<>();
            for (Entry<Object, Object> valueEntry : props.entrySet()) {
                Integer curVal = Integer.parseInt((String) valueEntry.getValue());
                String resource = (String) valueEntry.getKey();
                valueMap.put(resource, curVal);
                if (highestValue < curVal) {
                    highestValue = curVal;
                }
                codes.remove(resource);
            }

            // Check to see if something is missing
            for (String code : codes.stream().sorted().collect(Collectors.toList())) {
                LOGGER.info(code + "=" + highestValue++);
                found = true;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File access issue for parameter_names");
        }

        if (found) {
            throw new IllegalArgumentException("Parameter Name/Code are missing from parameter_names");
        }
    }
}