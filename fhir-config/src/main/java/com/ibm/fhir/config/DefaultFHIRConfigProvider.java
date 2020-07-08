/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config;

/**
 * Obtain configuration properties from the standard (file-based) 
 * FHIRConfiguration implementation.
 */
public class DefaultFHIRConfigProvider implements FHIRConfigProvider {

    @Override
    public PropertyGroup getPropertyGroup(String pgName) {
        return FHIRConfigHelper.getPropertyGroup(pgName);
    }
}