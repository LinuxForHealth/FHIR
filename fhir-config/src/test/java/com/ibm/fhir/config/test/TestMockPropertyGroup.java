/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config.test;

import javax.json.JsonObject;

import com.ibm.fhir.config.PropertyGroup;

public class TestMockPropertyGroup extends PropertyGroup {

    public TestMockPropertyGroup(JsonObject jsonObj, String foo) {
        super(jsonObj);
    }
}
