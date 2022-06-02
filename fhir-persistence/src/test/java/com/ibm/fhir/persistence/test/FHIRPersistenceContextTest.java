/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.context.FHIRSearchContextFactory;

/**
 * Tests associated with the FHIRPersistenceContextImpl class.
 */
public class FHIRPersistenceContextTest {

    @Test
    public void test1() {
        FHIRPersistenceEvent pe = new FHIRPersistenceEvent();

        FHIRPersistenceContext ctxt = FHIRPersistenceContextFactory.createPersistenceContext(pe);
        assertNotNull(ctxt);
        assertNotNull(ctxt.getPersistenceEvent());
        assertEquals(pe, ctxt.getPersistenceEvent());
        assertNull(ctxt.getHistoryContext());
        assertNull(ctxt.getSearchContext());
    }

    @Test
    public void test2() {
        FHIRPersistenceEvent pe = new FHIRPersistenceEvent();
        FHIRHistoryContext hc = FHIRPersistenceContextFactory.createHistoryContext();
        assertNotNull(hc);

        FHIRPersistenceContext ctxt = FHIRPersistenceContextFactory.createPersistenceContext(pe, hc);
        assertNotNull(ctxt);
        assertNotNull(ctxt.getPersistenceEvent());
        assertEquals(pe, ctxt.getPersistenceEvent());
        assertNotNull(ctxt.getHistoryContext());
        assertEquals(hc, ctxt.getHistoryContext());
        assertNull(ctxt.getSearchContext());
    }

    @Test
    public void test3() {
        FHIRPersistenceEvent pe = new FHIRPersistenceEvent();
        FHIRSearchContext sc = FHIRSearchContextFactory.createSearchContext();
        assertNotNull(sc);

        FHIRPersistenceContext ctxt = FHIRPersistenceContextFactory.createPersistenceContext(pe, sc, "pat42");
        assertNotNull(ctxt);
        assertNotNull(ctxt.getPersistenceEvent());
        assertEquals(pe, ctxt.getPersistenceEvent());
        assertNotNull(ctxt.getSearchContext());
        assertEquals(sc, ctxt.getSearchContext());
        assertNull(ctxt.getHistoryContext());
        assertEquals("pat42", ctxt.getRequestShard());
    }
}
