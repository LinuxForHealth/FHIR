/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.cadf.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.audit.AuditLogEventType;
import org.linuxforhealth.fhir.audit.cadf.enums.Action;
import org.linuxforhealth.fhir.audit.cadf.enums.ReporterRole;
import org.linuxforhealth.fhir.audit.cadf.enums.ResourceType;

public class AuditEnumTest {

    @Test(expectedExceptions = java.lang.IllegalArgumentException.class)
    public void testAuditLogEventType() {
        AuditLogEventType.fromValue("A");
    }

    @Test
    public void testAuditLogEventTypeValid() {
        AuditLogEventType a = AuditLogEventType.fromValue("fhir-operation");
        AuditLogEventType b = AuditLogEventType.fromValue("FHIR-OPERATION");
        assertEquals(a, b);

        assertEquals(a.value(), "fhir-operation");
        assertEquals(b.value(), "fhir-operation");
    }

    @Test
    public void testAction() {
        assertNotEquals(Action.allow.compareTo(Action.authenticate), 0);
        assertEquals(Action.create.compareTo(Action.create), 0);
        assertEquals(Action.create.toString(), "create");
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.data_security.toString(), "data/security");
    }

    @Test
    public void testReporterRole() {
        assertNotEquals(ReporterRole.modifier.compareTo(ReporterRole.observer),0);
        assertEquals(ReporterRole.relay.compareTo(ReporterRole.relay),0);
    }

}
