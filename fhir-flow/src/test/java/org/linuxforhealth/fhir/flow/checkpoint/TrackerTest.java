/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.flow.checkpoint;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.flow.api.ITrackerTicket;

/**
 * Unit test for {@link Tracker}
 */
public class TrackerTest {
    @Test
    public void testFirst() {
        Tracker<String> t = new Tracker<>();
        ITrackerTicket ticket1 = t.track("a", 1);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), "a");
        assertTrue(t.isEmpty());
    }

    @Test
    public void testTwoInOrder() {
        Tracker<String> t = new Tracker<>();
        ITrackerTicket ticket1 = t.track("a", 1);
        ITrackerTicket ticket2 = t.track("b", 2);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), "a");
        ticket2.complete();
        assertEquals(t.getCheckpoint(), "a");
        ticket2.complete();
        assertEquals(t.getCheckpoint(), "b");
        assertTrue(t.isEmpty());
    }

    @Test
    public void testTwoReverseOrder() {
        Tracker<String> t = new Tracker<>();
        ITrackerTicket ticket1 = t.track("a", 1);
        ITrackerTicket ticket2 = t.track("b", 1);
        ticket2.complete();
        assertNull(t.getCheckpoint());
        ticket1.complete();
        assertEquals(t.getCheckpoint(), "b");
        assertTrue(t.isEmpty());
    }

    @Test
    public void testThreeInOrder() {
        Tracker<String> t = new Tracker<>();
        ITrackerTicket ticket1 = t.track("a", 1);
        ITrackerTicket ticket2 = t.track("b", 1);
        ITrackerTicket ticket3 = t.track("c", 1);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), "a");
        ticket2.complete();
        assertEquals(t.getCheckpoint(), "b");
        ticket3.complete();
        assertEquals(t.getCheckpoint(), "c");
        assertTrue(t.isEmpty());
    }

    @Test
    public void testThreeReverseOrder() {
        Tracker<String> t = new Tracker<>();
        ITrackerTicket ticket1 = t.track("a", 1);
        ITrackerTicket ticket2 = t.track("b", 1);
        ITrackerTicket ticket3 = t.track("c", 1);
        ticket2.complete();
        assertNull(t.getCheckpoint());
        ticket3.complete();
        assertNull(t.getCheckpoint());
        ticket1.complete();
        assertEquals(t.getCheckpoint(), "c");
        assertTrue(t.isEmpty());
    }

    @Test
    public void testCombined() {
        Tracker<String> t = new Tracker<>();
        ITrackerTicket ticket1 = t.track("a", 1);
        ITrackerTicket ticket2 = t.track("b", 1);
        ticket2.complete();
        assertNull(t.getCheckpoint());
        ITrackerTicket ticket3 = t.track("c", 1);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), "b");
        ticket3.complete();
        assertEquals(t.getCheckpoint(), "c");
        assertTrue(t.isEmpty());
    }

    @Test
    public void testAfterEmpty() {
        Tracker<String> t = new Tracker<>();
        ITrackerTicket ticket1 = t.track("a", 1);
        ITrackerTicket ticket2 = t.track("b", 1);
        ticket1.complete();
        ticket2.complete();
        assertEquals(t.getCheckpoint(), "b");
        assertTrue(t.isEmpty());
        ITrackerTicket ticket4 = t.track("d", 1);
        ITrackerTicket ticket5 = t.track("e", 1);
        assertEquals(t.getCheckpoint(), "b");
        ticket5.complete();
        assertEquals(t.getCheckpoint(), "b");
        ticket4.complete();
        assertEquals(t.getCheckpoint(), "e");
        assertTrue(t.isEmpty());
    }
}
