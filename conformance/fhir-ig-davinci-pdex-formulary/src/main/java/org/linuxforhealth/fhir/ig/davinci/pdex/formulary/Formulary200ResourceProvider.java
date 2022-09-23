/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.davinci.pdex.formulary;

import org.linuxforhealth.fhir.registry.util.PackageRegistryResourceProvider;

/**
 * A FHIRRegistryResourceProvider that provides DaVinci Payer Data Exchange (PDex) US Drug Formulary, Release 2.0.0 - US Realm STU resources.
 */
public class Formulary200ResourceProvider extends PackageRegistryResourceProvider {
    
    /**
     * Get the package id for this package registry resource provider.
     *
     * @return
     *     the package id for this package registry resource provider
     */
    @Override
    public String getPackageId() {
        return "hl7.fhir.us.davinci-pdex-formulary.200";
    }
}
