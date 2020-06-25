/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the Group Search Compiler
 */
public class GroupSearchCompilerTest {
    /*
     * json/ibm/bulk-data/group/age-range-blood-pressure-group.json
     * json/ibm/bulk-data/group/age-simple-group.json
     * json/ibm/bulk-data/group/age-range-group.json
     *
     * json/ibm/bulk-data/group/age-range-with-gender-group.json
     * json/ibm/bulk-data/group/age-range-with-gender-and-exclude-group.json
     */
    @Test
    public void testDisabledGroup() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = (Group) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-simple-disabled-group.json");
        compiler.groupToSearch(group);
    }
}
