/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.flow.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Partitions incoming tasks using a partitioning function which is used
 * to distribute the tasks amongst a pool of worker threads.
 * Tasks in one partition are executed in order.
 */
public class PartitionExecutor<T> {

    private final List<SingleThreadWorker> partitions;
    private final Function<T, String> partitioner;
    private final Consumer<T> handler;
    private boolean running = true;
    /**
     * Create a fixed number of partitions. The partitioner function must be deterministic
     * (for a given value of T, the returned string must always be the same). The implementation
     * uses the string's hashCode to determine the partition.
     * 
     * @param partitionCount
     * @param partitionQueueSize
     * @param partitioner
     * @param handler
     */
    public PartitionExecutor(int partitionCount, int partitionQueueSize, Function<T, String> partitioner, Consumer<T> handler) {
        this.partitions = new ArrayList<>(partitionCount);
        this.partitioner = partitioner;
        this.handler = handler;
        for (int i=0; i<partitionCount; i++) {
            this.partitions.add(new SingleThreadWorker(partitionQueueSize));
        }
    }

    /**
     * Map the given value to a partition number
     * @param value
     * @return
     */
    private int getPartitionNumber(T value) {
        Objects.requireNonNull(value, "value");
        String partitionString = partitioner.apply(value);
        Objects.requireNonNull(partitionString, "partitionString");
        return Math.abs(partitionString.hashCode()) % partitions.size();
    }

    /**
     * Submit the item to process
     * @param item
     */
    public void submit(final T item) {
        if (running) {
            int partition = getPartitionNumber(item);
            partitions.get(partition).execute(() -> handler.accept(item));
        } else {
            throw new IllegalStateException("shut down");
        }
    }

    /**
     * Block any new work from being submitted
     */
    public void shutdown() {
        this.running = false;

        // Tell each partition worker to no accept new work
        // and drain its work queue
        for (SingleThreadWorker x: partitions) {
            x.setToDrain();
        }

        // TODO use an elapsed timer so that we don't end up waiting
        // too long if there are a large number of partitions to
        // shut down
        for (SingleThreadWorker x: partitions) {
            x.waitForDrain();
        }
    }

    /**
     * Block any new work from being submitted, and attempt to terminate
     * any work that is in progress
     */
    public void shutdownNow() {
        this.running = false;

    }
}
