/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.configuration.impl;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationAdapter;

/**
 * Grabs the Configuration Properties
 */
public class ConfigurationAdapterImpl implements ConfigurationAdapter {
    @Override
    public boolean enabled() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/operations/membermatch/enabled", false);
    }

    @Override
    public String getMemberStrategyKey() {
        return FHIRConfigHelper.getStringProperty("fhirServer/operations/membermatch/strategy", "default");
    }

    @Override
    public PropertyGroup getExtendedStrategyPropertyGroup() {
        return FHIRConfigHelper.getPropertyGroup("fhirServer/operations/membermatch/extendedProps");
    }

    @Override
    public boolean requireOldCoverage() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/operations/membermatch/requireOutputOldCoverage", false);
    }

    @Override
    public boolean requireMemberPatient() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/operations/membermatch/requireOutputMemberPatient", false);
    }
}