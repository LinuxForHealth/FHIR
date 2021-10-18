/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.util;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.HumanName;

/**
 * Unit tests for FHIRPersistenceUtil
 */
public class FHIRPersistenceUtilTest {

    /**
     * Test the injection of id and meta when the source resource does not have an id
     */
    @Test
    public void testCopyAndSetMetaNoId() {
        Patient patient = Patient.builder()
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .build();

        final com.ibm.fhir.model.type.Instant lastUpdated = FHIRPersistenceUtil.getUpdateTime();
        final String logicalId = "one";
        Patient prepared = FHIRPersistenceUtil.copyAndSetResourceMetaFields(patient, logicalId, 1, lastUpdated);
        
        assertEquals(prepared.getId(), logicalId);
        assertNotNull(prepared.getMeta());
        assertNotNull(prepared.getMeta().getVersionId());
        assertEquals(prepared.getMeta().getVersionId().getValue(), "1");
        assertNotNull(prepared.getMeta().getLastUpdated());
        assertEquals(prepared.getMeta().getLastUpdated(), lastUpdated);
    }

    /**
     * Test the injection of id and meta when the source resource has an id
     */
    @Test
    public void testCopyAndSetMetaId() {
        Patient patient = Patient.builder()
                .id("orig")
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .build();

        final com.ibm.fhir.model.type.Instant lastUpdated = FHIRPersistenceUtil.getUpdateTime();
        final String logicalId = "one";
        Patient prepared = FHIRPersistenceUtil.copyAndSetResourceMetaFields(patient, logicalId, 1, lastUpdated);
        
        assertEquals(prepared.getId(), logicalId);
        assertNotNull(prepared.getMeta());
        assertNotNull(prepared.getMeta().getVersionId());
        assertEquals(prepared.getMeta().getVersionId().getValue(), "1");
        assertNotNull(prepared.getMeta().getLastUpdated());
        assertEquals(prepared.getMeta().getLastUpdated(), lastUpdated);
    }
}