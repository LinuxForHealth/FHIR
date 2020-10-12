/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.UUID;

import com.ibm.fhir.persistence.util.LogicalIdentityProvider;

/**
 * Provides identity strings using random UUID for uniqueness but
 * prefixed with an encoded time string to improve database locality
 * when used in b-tree indexes.
 */
public class TimestampPrefixedUUID implements LogicalIdentityProvider {

    @Override
    public String createNewIdentityValue() {
        // It's OK to use milli-time here. It doesn't matter too much if the time changes
        // because we're not using the timestamp to determine uniqueness in any way. The
        // timestamp prefix is purely to help push index writes to the right hand side
        // of the btree, minimizing the number of physical reads likely required 
        // during ingestion when an index is too large to be fully cached.
        long millis = System.currentTimeMillis();
        
        // String encoding. Needs to collate correctly, so don't use any
        // byte-based encoding which would be sensitive to endian issues. For simplicity,
        // hex is sufficient, although a custom encoding using the full character set
        // supported by FHIR identifiers would be a little more compact (== smaller indexes).
        // Do not use Base64.
        String prefix = Long.toHexString(millis);
        
        UUID uuid = UUID.randomUUID();
        
        StringBuilder result = new StringBuilder();
        result.append(prefix);
        result.append("-"); // redundant, but more visually appealing.
        result.append(uuid.toString());
        return result.toString();
    }
}