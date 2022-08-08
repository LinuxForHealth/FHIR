/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service.provider.test;

import org.linuxforhealth.fhir.term.service.provider.RegistryTermServiceProvider;
import org.linuxforhealth.fhir.term.spi.FHIRTermServiceProvider;

public class RegistryTermServiceProviderTest extends FHIRTermServiceProviderTest {
    @Override
    public FHIRTermServiceProvider createProvider() throws Exception {
        return new RegistryTermServiceProvider();
    }
}
