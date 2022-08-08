/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.flow.checkpoint;

import java.util.concurrent.atomic.AtomicInteger;

import org.linuxforhealth.fhir.flow.api.ICheckpointTracker;
import org.linuxforhealth.fhir.flow.api.ITrackerTicket;

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
public class Tracker<T> implements ICheckpointTracker<T> {

    // The most recent checkpoint value
    private T checkpointValue;

    // The total number of processed requests
    private long processed = 0;
    /**
     * Inner class representing a double-linked list node with
     * additional properties
     */
    private static class Node<T> implements ITrackerTicket {
        private Node(Tracker<T> tracker, T checkpointValue, int entryCount) {
            this.tracker = tracker;
            this.checkpointValue = checkpointValue;
            this.entryCount = entryCount;
            this.remaining = new AtomicInteger(entryCount);
        }

        // The next node in the chain
        private Node<T> next;

        // The parent tracker...easier to read than making this a non-static inner class
        private final Tracker<T> tracker;

        // The checkpoint associated with this particular node
        private final T checkpointValue;

        // The initial number of entries
        final int entryCount;

        // The number of entries remaining to be completed
        final AtomicInteger remaining;

        @Override
        public void complete() {
            // The parent tracker handles the completion logic, because
            // it may include unlinking this node
            if (this.tracker != null) {
                tracker.completed(this);
            } else {
                throw new IllegalStateException("complete already called");
            }
        }
    }

    // The head of the queue (oldest item)
    private Node<T> head;

    // The tail of the queue (newest item)
    private Node<T> tail;

    @Override
    public ITrackerTicket track(T checkpointValue, int workItems) {
        return addNode(checkpointValue, workItems);
    }

    /**
     * Add a node to the tail of the queue to track the given requestId
     * @param requestId
     * @return
     */
    private Node<T> addNode(T checkpointValue, int workItems) {
        Node<T> oldTail = tail;
        Node<T> newTail = new Node<>(this, checkpointValue, workItems);

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
     * Decrement the remaining work count held by this node. The checkpoint 
     * won't be updated until we call prune to flush away any completed 
     * nodes at the head of the queue.
     * @param node
     */
    private void completed(Node<T> node) {

        // Decrement the remaining count, but apply a consistency check to
        // make sure we're not calling complete more times than this node
        // was initialized with
        node.remaining.updateAndGet(i -> {
            if (i > 0) {
                return i - 1;
            } else {
                throw new RuntimeException("Completed called when none remaining");
            }
        });
    }

    @Override
    public T getCheckpoint() {
        // Only the main history thread asks for the checkpoint so we don't need
        // to do any locking
        prune();
        return this.checkpointValue;
    }

    @Override
    public boolean isEmpty() {
        prune();
        return this.head == null;
    }

    /**
     * Remove completed nodes from the head and update the checkpoint
     * to be the last completed node
     */
    private void prune() {
        while (this.head != null && this.head.remaining.get() == 0) {
            // advance the checkpoint and remove the head from the queue
            this.checkpointValue = this.head.checkpointValue;
            this.processed += this.head.entryCount;
            Node<T> tmp = this.head;
            this.head = this.head.next;
            tmp.next = null; // help the GC
        }
    }

    @Override
    public long getProcessed() {
        return this.processed;
    }
}