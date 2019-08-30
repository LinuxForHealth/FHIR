/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.search.valuetypes;

import com.ibm.watson.health.fhir.search.valuetypes.impl.ValueTypesR4Impl;

/**
 * Value Types logic is split out to encapsulate custom logic related to search.
 * 
 * <br>
 * Load the class in the classloader to initialize static members. Call this before using the class in order to avoid a
 * slight performance hit on first use.
 *
 * @author pbastide
 *
 */
public class ValueTypesFactory {

    private static IValueTypes singleInstance = new ValueTypesR4Impl();
    
    private ValueTypesFactory() {
        // No Operation
    }

    /**
     * Returns the value types processor.
     * 
     * @return
     */
    public static IValueTypes getValueTypesProcessor() {

        return singleInstance;
    }

    /**
     * Loads the class in the classloader to initialize static members. Call this before using the class in order to
     * avoid a slight performance hit on first use.
     */
    public static void init() {
        // Loads the class and activates the ValueTypes R4Impl
    }

}
