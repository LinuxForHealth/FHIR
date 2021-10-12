/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.util;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A key which contains partition, tstamp and identifier values. This key can be
 * used to track changes being stored in a database. The partition value is used
 * to distribute the keyspace in a partitioned datastore (like Cassandra). This
 * means that jobs needing to scan the keys will need to scan each of the partitions,
 * but this is useful because it's easy to parallelize.
 * 
 * The tstamp field is simply the current UTC time. This creates a timeline which can
 * be used to scan records in approximate time-order. Because this is not unique,
 * algorithms such as reconciliation (looking for payloads stored in COS which aren't
 * in the RDBMS) can only checkpoint to the tstamp level, not an individual key. This is
 * OK, because restart from a checkpoint will be rare, and there should be relatively
 * few keys sharing the same tstamp (assuming millisecond accuracy).
 * 
 * The identifier is a random UUID and can be assumed to be globally unique.
 */
public class PartitionedSequentialKey {

    // 1024 partitions is a reasonable compromise to ensure adequate spread,
    // not requiring an excessive number of separate scans when doing
    // something like reconciliation
    private static final int MAX_PARTITIONS = 1024;
    
    // The keyspace partition
    private final int partition;
    
    // The UTC timestamp
    private final long tstamp;

    // The resource type name (e.g. "Patient")
    private final String resourceType;

    // The resource logical identifier, unique within a given resourceType
    private final String logicalId;
    
    // The version of the resource
    private final int version;

    /**
     * Protected constructor.
     * @param partition
     * @param tstamp
     * @param identifier
     */
    protected PartitionedSequentialKey(int partition, long tstamp, String resourceType, String logicalId, int version) {
        this.partition = partition;
        this.tstamp = tstamp;
        this.resourceType = resourceType;
        this.logicalId = logicalId;
        this.version = version;
    }

    /**
     * Generate a new instance of {@link PartitionedSequentialKey}.
     * @return
     */
    public static PartitionedSequentialKey generateKey(String resourceType, String logicalId, int version) {
        
        // Derive the partition from the tstamp, so all timestamps with the
        // same value are assigned to the same partition. We should still see
        // a good distribution of keys across partitions, because nodes in a cluster
        // are unlikely to be synchronized with millisecond accuracy, and parallel
        // threads are unlikely to all hit this method at the same time.
        long ts = Instant.now().toEpochMilli();
        int ptn = Long.hashCode(ts) % MAX_PARTITIONS;
        
        return new PartitionedSequentialKey(ptn, ts, resourceType, logicalId, version);
    }

    /**
     * @return the partition
     */
    public int getPartition() {
        return partition;
    }

    
    /**
     * @return the tstamp
     */
    public long getTstamp() {
        return tstamp;
    }

    
    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    
    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    
    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(logicalId, partition, resourceType, tstamp, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PartitionedSequentialKey other = (PartitionedSequentialKey) obj;
        return Objects.equals(logicalId, other.logicalId) && partition == other.partition && Objects.equals(resourceType, other.resourceType)
                && tstamp == other.tstamp && version == other.version;
    }
}