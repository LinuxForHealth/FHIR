/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.context;

import com.ibm.watsonhealth.fhir.search.context.impl.FHIRSearchContextImpl;

/**
 * This factory class can be used to create instances of the FHIRSearchContext interface.
 */
public class FHIRSearchContextFactory {

    /**
     * Hide the default ctor.
     */
    private FHIRSearchContextFactory() {
    }

    /**
     * Returns a new instance of the FHIRSearchContext interface.
     */
    public static FHIRSearchContext createSearchContext() {
        return new FHIRSearchContextImpl();
    }
}
