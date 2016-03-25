/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.core;

/**
 * A collection of miscellaneous utility functions used by the various fhir-* projects.
 */
public class FHIRUtilities {
    
    public static String getObjectHandle(Object o) {
        return Integer.toHexString(System.identityHashCode(o));
    }
}
