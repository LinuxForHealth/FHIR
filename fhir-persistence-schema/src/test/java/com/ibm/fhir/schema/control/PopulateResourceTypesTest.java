/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.ibm.fhir.core.ResourceType;

/**
 * Test to verify the resource_types.properties
 */
public class PopulateResourceTypesTest {
    private static final Logger LOGGER = Logger.getLogger(PopulateResourceTypesTest.class.getName());

    /**
     * This method is very intentional to verify the resource_types.properties on EVERY build.
     * The mapping is intentionally managed, as these KEYS are inserted and used.
     */
    @Test
    public static void verify() {
        Properties props = new Properties();
        boolean found = false;
        try (InputStream fis =
                PopulateResourceTypes.class.getClassLoader().getResourceAsStream("resource_types.properties")) {
            props.load(fis);

            Set<String> resources = new HashSet<>();
            for (ResourceType rt : ResourceType.values()) {
                resources.add(rt.value());
            }

            // Find the Highest Value to start from:
            Integer highestValue = 0;
            Map<String, Integer> valueMap = new HashMap<>();
            for (Entry<Object, Object> valueEntry : props.entrySet()) {
                Integer curVal = Integer.parseInt((String) valueEntry.getValue());
                String resource = (String) valueEntry.getKey();
                valueMap.put(resource, curVal);
                if (highestValue < curVal) {
                    highestValue = curVal;
                }
                resources.remove(resource);
            }

            // Check to see if something is missing
            for (String resource : resources) {
                LOGGER.info(resource + "=" + highestValue++);
                found = true;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File access issue for resource_types");
        }

        if (found) {
            throw new IllegalArgumentException("Resources are missing from resource_types");
        }
    }
}