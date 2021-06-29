/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config;

import com.ibm.fhir.operation.bulkdata.config.impl.V2ConfigurationImpl;

/**
 * Controls the selection of the legacy versus the new implementation.
 */
public class ConfigurationFactory {
    private ConfigurationFactory() {
        // No Operation
    }

    public static ConfigurationAdapter getInstance() {
        return new V2ConfigurationImpl();
    }
}