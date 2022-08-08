/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.config.test;

import jakarta.json.JsonObject;

import org.linuxforhealth.fhir.config.PropertyGroup;

public class TestMockPropertyGroup extends PropertyGroup {

    public TestMockPropertyGroup(JsonObject jsonObj, String foo) {
        super(jsonObj);
    }
}
