/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.schema;

import org.linuxforhealth.fhir.database.utils.api.ILeaseManagerConfig;

/**
 * LeaseManagerConfig is the configuration for the fhir-persistence-schema's LeaseManager.
 */
public class LeaseManagerConfig implements ILeaseManagerConfig {
    // The number of seconds to hold a lease
    private final int leaseTimeSeconds;

    // The host name of this instance
    private final String host;

    // Stay alive without requiring regular heartbeats
    private final boolean stayAlive;

    private LeaseManagerConfig(int leaseTimeSeconds, String host, boolean stayAlive) {
        this.leaseTimeSeconds = leaseTimeSeconds;
        this.host = host;
        this.stayAlive = stayAlive;
    }

    @Override
    public int getLeaseTimeSeconds() {
        return this.leaseTimeSeconds;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public boolean stayAlive() {
        return this.stayAlive;
    }

    /**
     * Factory method to create a {@link Builder} instance
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for fluent construction of LeaseManagerConfig
     */
    public static class Builder {
        private int leaseTimeSeconds;
        private String host;
        private boolean stayAlive;

        /**
         * Set the leaseTimeSeconds property
         * @param secs
         * @return
         */
        public Builder withLeaseTimeSeconds(int secs) {
            this.leaseTimeSeconds = secs;
            return this;
        }

        /**
         * Set the host property
         * @param host
         * @return
         */
        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        /**
         * Set the stayAlive property
         * @param flag
         * @return
         */
        public Builder withStayAlive(boolean flag) {
            this.stayAlive = flag;
            return this;
        }

        /**
         * Build the immutable LeaseManagerConfig object using
         * the current state of this {@link Builder}
         * @return
         */
        public LeaseManagerConfig build() {
            return new LeaseManagerConfig(this.leaseTimeSeconds, host, stayAlive);
        }
    }
}
