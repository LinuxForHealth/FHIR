/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.string.util.strategy;

/**
 * Truncate the input String value based on various strategies. 
 */
public interface StringSizeControlStrategy {
    
    
    /**
     * used to uniquely identify the strategy.
     * @return the String size control strategy identifier
     */
    String getStrategyIdentifier();
    
    /**
     * Truncate the input String value to fit the input maxBytes(maximum bytes) and return the truncated string.
     * @param value the input String value.
     * @param maxBytes the maximum String byte size.
     * @return 
     * If the size of the input String "value" is more than maxBytes, then return the truncated string which fits the maxBytes size limit.
     * If the size t of the input String "value" is within the maxBytes, then return the original String.
     * @throws NullPointerException if value is null
     */
    String truncateString(String value, int maxBytes);

}
