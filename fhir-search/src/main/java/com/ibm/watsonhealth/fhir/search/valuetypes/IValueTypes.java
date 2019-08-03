/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.valuetypes;

import java.util.Set;

import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.parameters.Parameter;

/**
 * Encapsulates the definitions of the is Search type.
 * 
 * @author pbastide
 *
 */
public interface IValueTypes {

    /**
     * initializes / sets up the class. 
     */
    public void init();
    
    /**
     * checks the resource value types against the indicators of the search type DateRange search.
     * 
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRSearchException
     */
    public boolean isDateRangeSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException;

    /**
     * checks the resource value types against the indicators of the search type Date search.
     * 
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRSearchException
     */
    public boolean isDateSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException;

    /**
     * checks the resource value types against the indicators of the search type Range search.
     * 
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRSearchException
     */
    public boolean isRangeSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException;

    /**
     * checks the resource value types against the indicators of the search type Integer search.
     * 
     * @param resourceType
     * @param queryParm
     * @return
     * @throws FHIRSearchException
     */
    public boolean isIntegerSearch(Class<?> resourceType, Parameter queryParm) throws FHIRSearchException;

    /**
     * gets the value types for the given class and name of the parameter.
     * 
     * @param resourceType
     * @param name
     *            of the Parameter
     * @return
     * @throws Exception
     */
    public Set<Class<?>> getValueTypes(Class<?> resourceType, String name) throws FHIRSearchException;

}
