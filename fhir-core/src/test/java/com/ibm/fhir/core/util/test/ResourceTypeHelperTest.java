/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util.test;

import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.util.ResourceTypeHelper;

public class ResourceTypeHelperTest {
    @Test
    public void testGetNewOrBreakingResourceTypeNames() {
        Set<String> newOrBreakingResourceTypeNames = ResourceTypeHelper.getNewOrBreakingResourceTypeNames();
        assertEquals(newOrBreakingResourceTypeNames.size(), 16, "number of new or breaking resource types");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetNewOrBreakingResourceTypeNamesInvalidAddTo() {
        Set<String> newOrBreakingResourceTypeNames = ResourceTypeHelper.getNewOrBreakingResourceTypeNames();
        newOrBreakingResourceTypeNames.add("test");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testGetNewOrBreakingResourceTypeNamesInvalidRemove() {
        Set<String> newOrBreakingResourceTypeNames = ResourceTypeHelper.getNewOrBreakingResourceTypeNames();
        newOrBreakingResourceTypeNames.remove("Ingredient");
    }

    @Test
    public void testGetResourceTypesFor() {
        Set<String> r4Types = ResourceTypeHelper.getResourceTypesFor(FHIRVersionParam.VERSION_40);
        assertEquals(r4Types.size(), 125, "number of r4 resource types");

        Set<String> r4bTypes = ResourceTypeHelper.getResourceTypesFor(FHIRVersionParam.VERSION_43);
        assertEquals(r4bTypes.size(), 141, "number of r4b resource types");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testInvalidAdd() {
        Set<String> newOrBreakingResourceTypeNames = ResourceTypeHelper.getResourceTypesFor(FHIRVersionParam.VERSION_40);
        newOrBreakingResourceTypeNames.add("test");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testInvalidRemove() {
        Set<String> newOrBreakingResourceTypeNames = ResourceTypeHelper.getResourceTypesFor(FHIRVersionParam.VERSION_43);
        newOrBreakingResourceTypeNames.remove("Ingredient");
    }
}