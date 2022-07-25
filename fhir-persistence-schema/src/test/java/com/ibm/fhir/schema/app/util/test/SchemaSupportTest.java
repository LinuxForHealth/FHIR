/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.app.util.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.schema.app.util.SchemaSupport;

/**
 * Unit tests for {@link SchemaSupport}
 */
public class SchemaSupportTest {
    
    @Test
    public void testResourceType() {
        SchemaSupport util = new SchemaSupport();
        
        // check resource-specific tables
        assertEquals(util.getResourceTypeFromTableName("patient_logical_resources"), "Patient");
        assertEquals(util.getResourceTypeFromTableName("patient_resources"), "Patient");
        assertEquals(util.getResourceTypeFromTableName("patient_str_values"), "Patient");
        assertEquals(util.getResourceTypeFromTableName("patient_resource_token_refs"), "Patient");
        assertEquals(util.getResourceTypeFromTableName("PATIENT_STR_VALUE"), "Patient"); // check case doesn't matter
        
        // check the whole-system tables
        assertEquals(util.getResourceTypeFromTableName("logical_resources"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("resources"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("resource_token_refs"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("str_values"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("date_values"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("common_token_values"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("common_canonical_values"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("logical_resource_tags"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("logical_resource_profiles"), "Resource");
        assertEquals(util.getResourceTypeFromTableName("logical_resource_security"), "Resource");
        
        // Reference tables we're not interested in
        assertNull(util.getResourceTypeFromTableName("WHOLE_SCHEMA_VERSION"));
        assertNull(util.getResourceTypeFromTableName("PARAMETER_NAMES"));
        assertNull(util.getResourceTypeFromTableName("RESOURCE_TYPES"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadName() {
        SchemaSupport util = new SchemaSupport();
        
        util.getResourceTypeFromTableName("BADTABLENAME");
    }

    @Test
    public void testUnknown() {
        SchemaSupport util = new SchemaSupport();
        
        assertNull(util.getResourceTypeFromTableName("BAD_TABLE"));
    }

    @Test
    public void testParamType() {
        SchemaSupport util = new SchemaSupport();

        // test for tables we want to associate with parameters
        assertTrue(util.isParamTable("patient_str_values"));
        assertTrue(util.isParamTable("patient_date_values"));
        assertTrue(util.isParamTable("patient_number_values"));
        assertTrue(util.isParamTable("patient_quantity_values"));
        assertTrue(util.isParamTable("patient_latlng_values"));
        assertTrue(util.isParamTable("patient_resource_token_refs"));
        assertTrue(util.isParamTable("patient_tags"));
        assertTrue(util.isParamTable("patient_profiles"));
        assertTrue(util.isParamTable("patient_security"));
        assertTrue(util.isParamTable("patient_current_refs"));

        // test for tables we don't want to associate with parameters
        assertFalse(util.isParamTable("logical_resources"));
        assertFalse(util.isParamTable("resources"));
        assertFalse(util.isParamTable("patient_logical_resources"));
        assertFalse(util.isParamTable("patient_resources"));
        assertFalse(util.isParamTable("common_token_values"));
        assertFalse(util.isParamTable("common_canonical_values"));
        assertFalse(util.isParamTable("whole_schema_version"));
    }
}
