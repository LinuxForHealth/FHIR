/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.core.util.test;

import static org.linuxforhealth.fhir.core.FHIRVersionParam.VERSION_40;
import static org.linuxforhealth.fhir.core.FHIRVersionParam.VERSION_43;
import static org.linuxforhealth.fhir.core.util.ResourceTypeHelper.getCompatibleResourceTypes;
import static org.linuxforhealth.fhir.core.util.ResourceTypeHelper.isCompatible;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.util.ResourceTypeHelper;

/**
 * Tests for the ResourceTypeHelper class
 */
public class ResourceTypeHelperTest {
    @Test
    public void testGetCompatibleResourceTypes() {
        Set<String> r4Types = getCompatibleResourceTypes(VERSION_40, VERSION_40);
        assertEquals(r4Types.size(), 146, "number of r4 resource types");

        Set<String> r4bTypes = ResourceTypeHelper.getCompatibleResourceTypes(VERSION_43, VERSION_43);
        assertEquals(r4bTypes.size(), 141, "number of r4b resource types");

        Set<String> backwardCompatibleTypes = ResourceTypeHelper.getCompatibleResourceTypes(VERSION_40, VERSION_43);
        assertEquals(backwardCompatibleTypes.size(), 126, "number of r4b resource types that are backwards-compatible with r4");

        Set<String> forwardCompatibleTypes = ResourceTypeHelper.getCompatibleResourceTypes(VERSION_43, VERSION_40);
        assertEquals(forwardCompatibleTypes.size(), 124, "number of r4 resource types that are forwards-compatible with r4b");
    }

    @Test
    public void testIsCompatible() {
        assertFalse(isCompatible("Evidence", VERSION_40, VERSION_43));
        assertTrue(isCompatible("ActivityDefinition", VERSION_40, VERSION_43));
        assertTrue(isCompatible("PlanDefinition", VERSION_40, VERSION_43));

        assertFalse(isCompatible("Evidence", VERSION_43, VERSION_40));
        assertFalse(isCompatible("ActivityDefinition", VERSION_43, VERSION_40));
        assertFalse(isCompatible("PlanDefinition", VERSION_43, VERSION_40));

        // While not particularly helpful, a valid concrete resourceType
        // should always be compatible within the same version
        assertTrue(isCompatible("Evidence", VERSION_40, VERSION_40));
        assertTrue(isCompatible("Evidence", VERSION_43, VERSION_43));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIsCompatibleAbstractResource() {
        isCompatible("Resource", VERSION_40, VERSION_43);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIsCompatibleInvalidKnownFhirVersion() {
        // ingredient was introduced in FHIR 4.3, so its not valid to ask
        // "is a valid instance of Ingredient in 4.0 expected to be valid in 4.3"
        isCompatible("Ingredient", VERSION_40, VERSION_43);
    }

    @Test
    public void testAllResourceTypeNames() {
        Set<String> allNames = ResourceTypeHelper.getAllResourceTypeNames();
        assertTrue(allNames.contains("Patient")); // In both R4B and R4
        assertTrue(allNames.contains("Ingredient")); // In R4B, not R4
        assertTrue(allNames.contains("MedicinalProductInteraction")); // In R4, not R4B
        assertFalse(allNames.contains("Resource")); // Abstract type, so not listed
    }
}
