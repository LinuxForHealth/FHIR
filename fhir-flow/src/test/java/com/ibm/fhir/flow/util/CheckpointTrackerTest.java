/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Unit test for the {@link CheckpointTracker}
 */
public class CheckpointTrackerTest {

    @Test
    public void testInOrder() {
        CheckpointTracker tracker = new CheckpointTracker();
        tracker.track(1);
        tracker.track(2);
        tracker.track(3);
        tracker.track(5);
        tracker.track(6);
        tracker.completed(1);
        assertEquals(tracker.getCheckpoint(), 1);
        tracker.completed(2);
        assertEquals(tracker.getCheckpoint(), 2);
        tracker.completed(3);
        assertEquals(tracker.getCheckpoint(), 3);
        tracker.completed(5);
        assertEquals(tracker.getCheckpoint(), 5);
        tracker.completed(6);
        assertEquals(tracker.getCheckpoint(), 6);
        assertTrue(tracker.isEmpty());
    }

    @Test
    public void testOutOfOrderCompletion() {
        CheckpointTracker tracker = new CheckpointTracker();
        tracker.track(1);
        tracker.track(2);
        tracker.track(3);
        tracker.track(5);
        tracker.track(6);
        tracker.completed(1);
        assertEquals(tracker.getCheckpoint(), 1);
        tracker.completed(5);
        assertEquals(tracker.getCheckpoint(), 1);
        tracker.completed(3);
        assertEquals(tracker.getCheckpoint(), 1);
        tracker.completed(2);
        assertEquals(tracker.getCheckpoint(), 5);
        tracker.completed(6);
        assertEquals(tracker.getCheckpoint(), 6);
        assertTrue(tracker.isEmpty());
    }

    @Test
    public void testMixed() {
        CheckpointTracker tracker = new CheckpointTracker();
        tracker.track(1);
        tracker.track(2);
        tracker.completed(1);
        assertEquals(tracker.getCheckpoint(), 1);
        tracker.track(3);
        tracker.track(5);
        tracker.completed(5);
        assertEquals(tracker.getCheckpoint(), 1);
        tracker.completed(3);
        assertEquals(tracker.getCheckpoint(), 1);
        tracker.completed(2);
        assertEquals(tracker.getCheckpoint(), 5);
        tracker.track(6);
        tracker.completed(6);
        assertEquals(tracker.getCheckpoint(), 6);
        assertTrue(tracker.isEmpty());        
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTrackOrder() {
        CheckpointTracker tracker = new CheckpointTracker();
        tracker.track(2);
        tracker.track(1);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testUnderflow() {
        CheckpointTracker tracker = new CheckpointTracker();
        tracker.track(1);
        tracker.completed(1);
        tracker.completed(2);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testDelayedUnderflow() {
        CheckpointTracker tracker = new CheckpointTracker();
        tracker.track(1);
        tracker.track(3);
        tracker.completed(1);
        tracker.completed(2);
        tracker.completed(3); // underflow triggered here
    }
}
