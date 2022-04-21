/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.checkpoint;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.flow.api.ITrackerTicket;

/**
 * Unit test for {@link Tracker}
 */
public class TrackerTest {
    @Test
    public void testFirst() {
        Tracker t = new Tracker();
        ITrackerTicket ticket1 = t.track(1);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), 1);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testTwoInOrder() {
        Tracker t = new Tracker();
        ITrackerTicket ticket1 = t.track(1);
        ITrackerTicket ticket2 = t.track(2);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), 1);
        ticket2.complete();
        assertEquals(t.getCheckpoint(), 2);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testTwoReverseOrder() {
        Tracker t = new Tracker();
        ITrackerTicket ticket1 = t.track(1);
        ITrackerTicket ticket2 = t.track(2);
        ticket2.complete();
        assertEquals(t.getCheckpoint(), -1);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), 2);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testThreeInOrder() {
        Tracker t = new Tracker();
        ITrackerTicket ticket1 = t.track(1);
        ITrackerTicket ticket2 = t.track(2);
        ITrackerTicket ticket3 = t.track(3);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), 1);
        ticket2.complete();
        assertEquals(t.getCheckpoint(), 2);
        ticket3.complete();
        assertEquals(t.getCheckpoint(), 3);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testThreeReverseOrder() {
        Tracker t = new Tracker();
        ITrackerTicket ticket1 = t.track(1);
        ITrackerTicket ticket2 = t.track(2);
        ITrackerTicket ticket3 = t.track(3);
        ticket2.complete();
        assertEquals(t.getCheckpoint(), -1);
        ticket3.complete();
        assertEquals(t.getCheckpoint(), -1);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), 3);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testCombined() {
        Tracker t = new Tracker();
        ITrackerTicket ticket1 = t.track(1);
        ITrackerTicket ticket2 = t.track(2);
        ticket2.complete();
        assertEquals(t.getCheckpoint(), -1);
        ITrackerTicket ticket3 = t.track(3);
        ticket1.complete();
        assertEquals(t.getCheckpoint(), 2);
        ticket3.complete();
        assertEquals(t.getCheckpoint(), 3);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testAfterEmpty() {
        Tracker t = new Tracker();
        ITrackerTicket ticket1 = t.track(1);
        ITrackerTicket ticket2 = t.track(2);
        ticket1.complete();
        ticket2.complete();
        assertEquals(t.getCheckpoint(), 2);
        assertTrue(t.isEmpty());
        ITrackerTicket ticket4 = t.track(4);
        ITrackerTicket ticket5 = t.track(5);
        assertEquals(t.getCheckpoint(), 2);
        ticket5.complete();
        assertEquals(t.getCheckpoint(), 2);
        ticket4.complete();
        assertEquals(t.getCheckpoint(), 5);
        assertTrue(t.isEmpty());
    }
}
