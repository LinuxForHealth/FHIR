/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;


/**
 * Configured hints to use for certain queries
 */
public interface QueryHints {
    
    /**
     * Get the hint value from the configuration
     * @return
     */
    String getHintValue(String hintProperty);

}
