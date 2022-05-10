/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.util.Objects;

/**
 * A key used to identify a common_token_value record in our distributed schema
 * variant
 */
public class CommonTokenValueKey {
    private final short shardKey;
    private final String codeSystem;
    private final String tokenValue;

    /**
     * Public constructor
     * @param shardKey
     * @param codeSystem
     * @param tokenValue
     */
    public CommonTokenValueKey(short shardKey, String codeSystem, String tokenValue) {
        this.shardKey = shardKey;
        this.codeSystem = Objects.requireNonNull(codeSystem);
        this.tokenValue = Objects.requireNonNull(tokenValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeSystem, tokenValue, shardKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CommonTokenValueKey) {
            CommonTokenValueKey that = (CommonTokenValueKey)obj;
            return this.shardKey == that.shardKey
                    && this.codeSystem.equals(that.codeSystem)
                    && this.tokenValue.equals(that.tokenValue);
        }
        return false;
    }
}