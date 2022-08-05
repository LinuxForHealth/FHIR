/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.util;

/**
 * Supports different strategies for creating identity strings
 */
public interface LogicalIdentityProvider {

    /**
     * Create a new identity string. The max length of the string will never
     * be greater than 100 (ASCII) characters
     * @return
     */
    String createNewIdentityValue();
}