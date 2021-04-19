/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cache.configuration;

import java.time.Duration;
import java.util.Objects;

/**
 * A configuration class used to create managed caches with size and/or time-based eviction policies
 */
public class Configuration {
    private final Long maximumSize;
    private final Duration duration;

    private Configuration(long maximumSize) {
        this(maximumSize, null);
    }

    private Configuration(Duration duration) {
        this(null, duration);
    }

    private Configuration(Long maximumSize, Duration duration) {
        this.maximumSize = maximumSize;
        this.duration = duration;
    }

    /**
     * The maximum size of the cache before entries are evicted
     *
     * @return
     *     the maximum size or null
     */
    public Long getMaximumSize() {
        return maximumSize;
    }

    /**
     * The duration of entries after they are written to the cache
     *
     * @return
     *     the duration or null
     */
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Configuration other = (Configuration) obj;
        return Objects.equals(maximumSize, other.maximumSize) && Objects.equals(duration, other.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximumSize, duration);
    }

    /**
     * A factory method for creating the configuration of a cache with a time-based eviction policy
     *
     * @param maximumSize
     *     the maximum size
     * @return
     *     a configuration instance
     */
    public static Configuration of(long maximumSize) {
        return new Configuration(maximumSize);
    }

    /**
     * A factory method for creating the configuration of a cache with both size and time-based eviction policies
     *
     * @param maximumSize
     *     the maximum size
     * @param duration
     *     the duration
     * @return
     *     a configuration instance
     */
    public static Configuration of(long maximumSize, Duration duration) {
        Objects.requireNonNull(duration, "duration");
        return new Configuration(maximumSize, duration);
    }

    /**
     * A factory method for creating the configuration of a cache with a time-based eviction policy
     *
     * @param duration
     *     the duration
     * @return
     *     a configuration instance
     */
    public static Configuration of(Duration duration) {
        Objects.requireNonNull(duration, "duration");
        return new Configuration(duration);
    }
}