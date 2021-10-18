/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.util.Comparator;
import java.util.Objects;

import com.ibm.fhir.persistence.jdbc.dao.impl.TransactionDataImpl;

/**
 * DTO representing a record in COMMON_TOKEN_VALUES. These values are
 * generated when processing data from {@link TransactionDataImpl} to
 * collect a unique set of records we need to insert into
 * COMMON_TOKEN_VALUES table (if the row doesn't already exist). The
 * select/inserts must always be done in a deterministic order to
 * minimize the chance of a deadlock, hence the object is Comparable
 * to give it a natural sort order.
 */
public class CommonTokenValue implements Comparable<CommonTokenValue> {
    private static final Comparator<String> NULL_SAFE_COMPARATOR = Comparator.nullsFirst(String::compareTo);
    
    // We hold codeSystem here because we want to sort using the name, not the id, to help avoid deadlocks in Derby
    private final String codeSystem;

    private final int codeSystemId;

    // tokenValue can be null
    private final String tokenValue;

    /**
     * Construct a common token value from a codeSystemId and tokenValue
     * @param codeSystemId
     * @param tokenValue
     */
    public CommonTokenValue(String codeSystem, int codeSystemId, String tokenValue) {
        if (codeSystemId < 0) {
            // Called before the code-system record was created (or fetched from) the database
            throw new IllegalArgumentException("Invalid codeSystemId argument");
        }

        this.codeSystem = codeSystem;
        this.codeSystemId = codeSystemId;
        this.tokenValue = tokenValue;
    }

    /**
     * @return the codeSystemId
     */
    public int getCodeSystemId() {
        return codeSystemId;
    }

    /**
     * @return the tokenValue
     */
    public String getTokenValue() {
        return tokenValue;
    }

    @Override
    public int hashCode() {
        // We don't need to include codeSystem in the hash because codeSystemId is synonymous
        // with codeSystem as far as identity is concerned
        return Integer.hashCode(codeSystemId) * 37 + (tokenValue == null ? 7 : tokenValue.hashCode());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CommonTokenValue) {
            CommonTokenValue that = (CommonTokenValue)other;
            return this.codeSystemId == that.codeSystemId
                    && ( this.tokenValue == null && that.tokenValue == null
                            || this.tokenValue != null && this.tokenValue.equals(that.tokenValue)
                        );
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[codeSystemId=" + codeSystemId + ", tokenValue=" + tokenValue + "]";
    }

    @Override
    public int compareTo(CommonTokenValue other) {
        // allow CommonTokenValue objects to be sorted in a deterministic way. Note that
        // we sort on codeSystem not codeSystemId. This is to help avoid deadlocks with
        // Derby
        int result = NULL_SAFE_COMPARATOR.compare(codeSystem, other.codeSystem);
        if (result == 0) {
            result = NULL_SAFE_COMPARATOR.compare(tokenValue, other.tokenValue);
        }
        return result;
    }
}