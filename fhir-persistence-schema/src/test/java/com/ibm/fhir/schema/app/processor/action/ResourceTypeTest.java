/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.model.ResourceType;

/**
 * ResourceType Test
 */
public class ResourceTypeTest {
    @Test
    public void testResourceType() throws SchemaActionException {
        ResourceType r = new ResourceType();
        assertNotNull(r);
        r.setId(1);
        r.setName("Name");
        assertNotNull(r.getId());
        assertNotNull(r.getName());

        assertEquals(r.getId(), 1);
        assertEquals(r.getName(), "Name");
        assertEquals(r.toString(),"Name[1]");
    }
}