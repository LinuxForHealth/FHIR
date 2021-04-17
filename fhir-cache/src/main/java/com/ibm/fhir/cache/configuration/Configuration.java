/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cache.configuration;

import java.time.Duration;
import java.util.Objects;

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

    public Long getMaximumSize() {
        return maximumSize;
    }

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

    public static Configuration of(long maximumSize) {
        return new Configuration(maximumSize);
    }

    public static Configuration of(long maximumSize, Duration duration) {
        Objects.requireNonNull(duration, "duration");
        return new Configuration(maximumSize, duration);
    }

    public static Configuration of(Duration duration) {
        Objects.requireNonNull(duration, "duration");
        return new Configuration(duration);
    }
}