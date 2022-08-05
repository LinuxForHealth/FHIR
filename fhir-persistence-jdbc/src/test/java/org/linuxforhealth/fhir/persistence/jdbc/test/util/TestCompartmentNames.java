/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.persistence.jdbc.util.CompartmentNames;

/**
 * Unit test for the CompartmentNames enum
 */
public class TestCompartmentNames {

    @Test
    public void testIdsAreUnique() {
        Set<Integer> ids = new HashSet<>();
        
        for (CompartmentNames cn: CompartmentNames.values()) {
            if (ids.contains(cn.getId())) {
                throw new IllegalStateException("Duplicate id assigned to CompartmentNames: " + cn.name() + " = " + cn.getId());
            } else {
                ids.add(cn.getId());
            }
        }
    }

    /**
     * It's important that the id values are never changed, because that would cause data corruption
     * in our schemas. This test just protects against accidental changes.
     */
    @Test
    public void testIdsAreFixed() {
        assertEquals(CompartmentNames.Patient.getId(),       0);
        assertEquals(CompartmentNames.Encounter.getId(),     1);
        assertEquals(CompartmentNames.RelatedPerson.getId(), 2);
        assertEquals(CompartmentNames.Practitioner.getId(),  3);
        assertEquals(CompartmentNames.Device.getId(),        4);
        
        // make sure someone sees this test if they add another compartment name
        assertEquals(CompartmentNames.values().length, 5);
    }
}
