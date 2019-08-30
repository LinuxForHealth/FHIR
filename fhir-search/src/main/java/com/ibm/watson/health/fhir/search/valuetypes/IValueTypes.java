/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.search.valuetypes;

import java.io.File;
import java.util.Map;
import java.util.Set;

import com.ibm.watson.health.fhir.search.exception.FHIRSearchException;
import com.ibm.watson.health.fhir.search.parameters.Parameter;

/**
 * Encapsulates the definitions of the is Search type.
 * 
 * @author pbastide
 *
 */
public interface IValueTypes {

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
     * @param code
     *            of the Parameter
     * @return
     * @throws Exception
     */
    public Set<Class<?>> getValueTypes(Class<?> resourceType, String code) throws FHIRSearchException;

    /**
     * loads a map from the file. 
     * 
     * @param f
     * @return
     */
    public Map<String, Set<Class<?>>> loadFromFile(File f);
    
}
