/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.audit.logging.api.AuditLogServiceFactory;
import com.ibm.fhir.audit.logging.api.impl.NoOpService;

public class AuditLogServiceTest {
    @Test
    public void testAuditLogEventTypeValid() {
        AuditLogServiceFactory f = new AuditLogServiceFactory();
        assertNotNull(f);
    }
    
    @Test
    public void testDisabledAuditLogService() throws Exception {
        NoOpService svc = new NoOpService();
        svc.initialize(null);
        svc.isEnabled();
        svc.logEntry(null);
        assertTrue(true);
    }
}