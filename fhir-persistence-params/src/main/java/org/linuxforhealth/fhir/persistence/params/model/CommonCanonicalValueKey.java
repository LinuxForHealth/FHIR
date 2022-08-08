/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.model;

import java.util.Objects;

/**
 * A key used to identify a common_canonical_value record in our distributed schema
 * variant
 */
public class CommonCanonicalValueKey {
    private final short shardKey;
    private final String url;

    /**
     * Public constructor
     * @param shardKey
     * @param codeSystem
     * @param tokenValue
     */
    public CommonCanonicalValueKey(short shardKey, String url) {
        this.shardKey = shardKey;
        this.url = Objects.requireNonNull(url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, shardKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommonCanonicalValueKey) {
            CommonCanonicalValueKey that = (CommonCanonicalValueKey)obj;
            return this.shardKey == that.shardKey
                    && this.url.equals(that.url);
        }
        return false;
    }
}