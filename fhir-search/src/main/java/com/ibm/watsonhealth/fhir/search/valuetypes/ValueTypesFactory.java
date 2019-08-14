/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.valuetypes;

import com.ibm.watsonhealth.fhir.search.valuetypes.impl.ValueTypesR4Impl;

/**
 * Value Types logic is split out to encapsulate custom logic related to search.
 * 
 * @author pbastide@us.ibm.com
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
     * add the class to the classloader. 
     */
    public static void init() {
        // Loads the class and activates the ValueTypes R4Impl
    }

}
