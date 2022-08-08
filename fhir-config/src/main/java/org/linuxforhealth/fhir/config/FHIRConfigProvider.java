/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.config;

/**
 * Allows access to the FHIR server configuration to be hidden behind an adapter,
 * decoupling the consumer from file-based FHIRConfiguration stuff.
 */
public interface FHIRConfigProvider {

    /**
     * Get the named PropertyGroup
     * @param pgName
     * @return
     */
    PropertyGroup getPropertyGroup(String pgName);
}
