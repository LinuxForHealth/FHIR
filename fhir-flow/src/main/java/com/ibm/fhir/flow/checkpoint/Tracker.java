/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.checkpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ibm.fhir.flow.api.ICheckpointTracker;
import com.ibm.fhir.flow.api.ITrackerTicket;

/**
 * Tracks units of work which arrive in a sequence but can be
 * executed in parallel. A checkpoint represents a point in the
 * sequence for which all units up to and including that point
 * have been completed. If processing needs to be restarted,
 * the checkpoint can be used to request work units that come
 * after that point in the sequence.
 * 
 * Implemented as our own single-linked list, where each node is also
 * used as the returned ITrackerTicket. Upon completion, we simply
 * mark the node as done.
 * 
 * Completed nodes are not removed from the queue until a checkpoint
 * is requested. This allows us to avoid any synchronization on the
 * queue data structure which could lead to contention. This means 
 * that the {@link #addNode(long)} and {@link #getCheckpoint()} methods
 * are not thread-safe by design. Only the main history fetch controller
 * thread should be calling these methods anyway, so this is not a
 * problem in practice. 
 */
public class Tracker implements ICheckpointTracker {

    // The maximum completed requestId for which all work prior has also been completed
    private long checkpoint = -1;

    /**
     * Inner class representing a double-linked list node with
     * additional properties
     */
    private static class Node implements ITrackerTicket {
        private Node(Tracker tracker, long requestId) {
            this.tracker = tracker;
            this.requestId = requestId;
        }

        // The next node in the chain
        private Node next;

        // The parent tracker...easier to read than making this a non-static inner class
        private Tracker tracker;

        // The oldest completed piece of work older than than this node
        private long requestId;

        // The completion status of this node
        AtomicBoolean completed = new AtomicBoolean(false);

        @Override
        public void complete() {
            if (this.tracker != null) {
                tracker.completed(this);
            } else {
                throw new IllegalStateException("complete already called");
            }
        }
    }

    // The head of the queue (oldest item)
    private Node head;

    // The tail of the queue (newest item)
    private Node tail;

    @Override
    public ITrackerTicket track(long requestId) {
        return addNode(requestId);
    }

    @Override
    public List<ITrackerTicket> track(List<Long> requestIds) {
        List<ITrackerTicket> tickets = new ArrayList<>(requestIds.size());
        for (Long requestId: requestIds) {
            tickets.add(addNode(requestId));
        }
        return tickets;
    }

    /**
     * Add a node to the tail of the queue to track the given requestId
     * @param requestId
     * @return
     */
    private Node addNode(long requestId) {
        Node oldTail = tail;
        Node newTail = new Node(this, requestId);

        this.tail = newTail;

        if (this.head == null) {
            // queue was empty, so head and tail now point to the same new node
            this.head = newTail;
        } else {
            // link the old tail back to the new tail
            oldTail.next = newTail;
        }

        return newTail;
    }

    /**
     * Mark the node as completed. The checkpoint won't be updated until we
     * call prune to flush away any completed nodes at the head of the queue
     * @param node
     */
    private void completed(Node node) {
        if (this.head == null) {
            throw new IllegalStateException("queue is empty");
        }

        // Make sure we can't be called again
        node.tracker = null;
        node.completed.set(true);
    }

    @Override
    public long getCheckpoint() {
        // Only the main history thread asks for the checkpoint so we don't need
        // to do any locking
        prune();
        return this.checkpoint;
    }

    @Override
    public boolean isEmpty() {
        prune();
        return this.head == null;
    }

    /**
     * Remove completed nodes from the head and update the checkpoint
     */
    private void prune() {
        while (this.head != null && this.head.completed.get()) {
            // update the checkpoint and remove the head from the queue
            this.checkpoint = this.head.requestId;
            this.head = this.head.next;
        }
    }
}