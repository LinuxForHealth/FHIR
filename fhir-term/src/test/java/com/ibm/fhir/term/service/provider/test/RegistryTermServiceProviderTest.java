/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.provider.test;

import com.ibm.fhir.term.service.provider.RegistryTermServiceProvider;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;

public class RegistryTermServiceProviderTest extends FHIRTermServiceProviderTest {
    @Override
    public FHIRTermServiceProvider createProvider() throws Exception {
        return new RegistryTermServiceProvider();
    }
}
