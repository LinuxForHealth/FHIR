/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.carin.bb;

import org.linuxforhealth.fhir.registry.util.PackageRegistryResourceProvider;

public class C4BB110ResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.us.carin-bb.110";
    }
}
