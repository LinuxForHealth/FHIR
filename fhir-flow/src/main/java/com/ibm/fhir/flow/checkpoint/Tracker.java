/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.checkpoint;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.flow.api.ICheckpointTracker;
import com.ibm.fhir.flow.api.ITrackerTicket;

/**
 * Tracks units of work which arrive in a sequence but can be
 * executed in parallel. A checkpoint represents a point in the
 * sequence for which all units up to and including that point
 * have been completed. If processing needs to be restarted,
 * the checkpoint can be used to request work units that come
 * after that point in the sequence.
 * Implemented as our own double-linked list, where each node is also
 * used as the returned ITrackerTicket. This makes it really efficient
 * to remove the node from the chain upon completion, and track the
 * largest completed requestId
 */
public class Tracker implements ICheckpointTracker {

    // The maximum completed requestId for which all work prior has also been completed
    private long checkpoint = -1;

    // keep synchronization internal
    private final Object monitor = new Object();

    /**
     * Inner class representing a double-linked list node with
     * additional properties
     */
    private static class Node implements ITrackerTicket {
        private Node(Tracker tracker, long requestId) {
            this.tracker = tracker;
            this.maxRequestId = requestId;
        }

        // The previous node in the chain
        private Node prev;

        // The next node in the chain
        private Node next;

        // The parent tracker...easier to read than making this a non-static inner class
        private Tracker tracker;

        // The oldest completed piece of work older than than this node
        private long maxRequestId;

        @Override
        public void complete() {
            if (this.tracker != null) {
                synchronized(tracker.monitor) {
                    tracker.completed(this);
                }
            } else {
                throw new IllegalStateException("complete already called");
            }
        }
    }

    // The head of the queue to which we add new items
    private Node head;

    // The tail of the queue, representing the oldest item of work
    private Node tail;

    @Override
    public ITrackerTicket track(long requestId) {
        synchronized(monitor) {
            return addNode(requestId);
        }
    }

    @Override
    public List<ITrackerTicket> track(List<Long> requestIds) {
        List<ITrackerTicket> tickets = new ArrayList<>(requestIds.size());
        synchronized(monitor) {
            for (Long requestId: requestIds) {
                tickets.add(addNode(requestId));
            }
        }
        return tickets;
    }

    /**
     * Add a node to track the given requestId
     * @param requestId
     * @return
     */
    private Node addNode(long requestId) {
        Node currentTail = tail;
        Node newTail = new Node(this, requestId);
        newTail.prev = currentTail; // may be null, of course
        
        this.tail = newTail;
        
        if (this.head == null) {
            this.head = newTail;
        } else {
            currentTail.next = newTail;
        }
        
        return newTail;
    }

    /**
     * This node has been completed, so remove it from the chain
     * @param node
     */
    private void completed(Node node) {
        // Unlink the node and update the checkpoint if required
        if (this.head == null) {
            throw new IllegalStateException("queue is empty");
        }

        // Make sure we can't be called again
        node.tracker = null;

        if (node == head) {
            // Advance the checkpoint to the maxRequestId held by this node
            this.checkpoint = node.maxRequestId;

            if (node == tail) {
                head = tail = null;
            } else {
                node.next.prev = null;
                this.head = node.next;
            }
        } else if (node == tail) {
            // shift maxRequestId to the previous node
            node.prev.maxRequestId = node.maxRequestId;
            tail = node.prev;
            node.prev = null;
            tail.next = null;
        } else {
            // mid queue node
            node.prev.maxRequestId = node.maxRequestId;
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.next = null;
            node.prev = null;
        }
    }

    @Override
    public long getCheckpoint() {
        synchronized(monitor) {
            return this.checkpoint;
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized(monitor) {
            return this.head == null;
        }
    }
}