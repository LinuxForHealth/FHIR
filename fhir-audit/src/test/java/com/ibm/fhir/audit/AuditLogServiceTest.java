/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.audit.beans.AuditLogEntry;
import com.ibm.fhir.audit.beans.Context;
import com.ibm.fhir.audit.impl.NopService;

public class AuditLogServiceTest {
    @Test
    public void testAuditLogEventTypeValid() {
        AuditLogServiceFactory f = new AuditLogServiceFactory();
        assertNotNull(f);
    }

    @Test
    public void testDisabledAuditLogService() throws Exception {
        // Previously no-op
        NopService svc = new NopService();
        svc.initialize(null);
        svc.isLoggableOperation(null);
        svc.isEnabled();
        svc.logEntry(null);
        svc.stop(null);
        assertTrue(true);
    }

    @Test
    public void testAuditLogServiceIsLoggableOperation() {
        NopService svc = new NopService();
        assertFalse(svc.isLoggableOperation(null));
        AuditLogEntry entry = new AuditLogEntry(null, null, null, null, null);
        assertFalse(svc.isLoggableOperation(entry));
        entry.setContext(new Context());

        // Not an operation
        assertTrue(svc.isLoggableOperation(entry));
        entry.getContext().setOperationName("fred");
        assertTrue(svc.isLoggableOperation(entry));

        entry.getContext().setOperationName("healthcheck");
        assertFalse(svc.isLoggableOperation(entry));
    }
}