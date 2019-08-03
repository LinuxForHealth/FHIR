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
    
    public static void init() {
        // Activates the Class
        singleInstance.init();
    }
    
    /**
     * Returns the value types processor. 
     * 
     * @return
     */
    public static IValueTypes getValueTypesProcessor() {
        return singleInstance;
    }

}
