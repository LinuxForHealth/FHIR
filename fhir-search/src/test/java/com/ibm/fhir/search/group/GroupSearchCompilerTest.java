/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.ws.rs.core.MultivaluedMap;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the Group Search Compiler
 */
public class GroupSearchCompilerTest {
    @Test(expectedExceptions= {GroupSearchCompilerException.class})
    public void testDisabledGroup() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = (Group) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-simple-disabled-group.json");
        compiler.groupToSearch(group, "Patient");
    }

    @Test(expectedExceptions= {java.lang.NullPointerException.class})
    public void testGroupNull() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = null;
        compiler.groupToSearch(group, "Patient");
    }

    @Test
    public void testAgeRangeGroup() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = (Group) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-range-group.json");
        MultivaluedMap<String,String> queryParams = compiler.groupToSearch(group, "Patient");
        assertNotNull(queryParams);
        assertEquals(queryParams.size(),1);
        assertEquals(queryParams.get("birthdate").size(),2);
    }

    @Test
    public void testAgeSimpleGroup() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = (Group) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-simple-group.json");
        MultivaluedMap<String,String> queryParams = compiler.groupToSearch(group, "Patient");
        assertNotNull(queryParams);
        assertEquals(queryParams.size(),1);
        assertEquals(queryParams.get("birthdate").size(),1);
    }

    @Test
    public void testAgeRangeBloodPressureGroup() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = (Group) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-range-blood-pressure-group.json");
        MultivaluedMap<String,String> queryParams = compiler.groupToSearch(group, "Patient");
        assertNotNull(queryParams);
        assertEquals(queryParams.size(),1);
        assertEquals(queryParams.get("birthdate").size(),2);
    }

    @Test
    public void testAgeRangeGenderGroup() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = (Group) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-range-with-gender-group.json");
        MultivaluedMap<String,String> queryParams = compiler.groupToSearch(group, "Patient");
        assertNotNull(queryParams);
        assertEquals(queryParams.size(),2);
        assertEquals(queryParams.get("birthdate").size(),2);
        assertEquals(queryParams.get("gender").size(),1);
        assertEquals(queryParams.get("gender").get(0),"female");
    }

    @Test
    public void testAgeRangeGenderAndExcludeGroup() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        Group group = (Group) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-range-with-gender-and-exclude-group.json");
        MultivaluedMap<String,String> queryParams = compiler.groupToSearch(group, "Patient");
        assertNotNull(queryParams);
        assertEquals(queryParams.size(),2);
        assertEquals(queryParams.get("birthdate").size(),2);
        queryParams = compiler.groupToSearch(group, "Observation");
        assertNotNull(queryParams);
        assertEquals(queryParams.size(),1);
        assertEquals(queryParams.get("component-value-concept:not").size(),1);
        assertEquals(queryParams.get("component-value-concept:not").get(0),"LA15173-0");
    }
}