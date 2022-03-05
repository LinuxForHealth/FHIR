/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.davinci.pdex;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class PDEX100ResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.us.davinci-pdex.100";
    }
}
