/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.parameters;

import com.ibm.watsonhealth.fhir.search.SearchConstants;
import com.ibm.watsonhealth.fhir.search.SearchConstants.SortDirection;
import com.ibm.watsonhealth.fhir.search.SearchConstants.Type;

/**
 * This class encapsulates data related to a FHIR sort parameter. 
 * 
 * Refactored to simplify the code and push common enums into SearchConstants. 
 * 
 * @see http://www.hl7.org/fhir/search.html#sort
 * 
 * @author markd
 * @author pbastide 
 *
 */
public class SortParameter implements Comparable<SortParameter> {
    
    private String name;
    private Type type;
    private SortDirection direction;
    // The is the location of the sort parameter within the query string that was input to the REST search API.
    private int queryStringIndex;
    
    public SortParameter(String parmName, Type parmType, SortDirection sortDirection, int queryStringIndex) {
        super();
        this.name = parmName;
        this.type = parmType;
        this.direction = sortDirection;
        this.queryStringIndex = queryStringIndex;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public int getQueryStringIndex() {
        return queryStringIndex;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("_sort:");
        buffer.append(this.getDirection().value());
        buffer.append("=");
        buffer.append(this.getName());
        
        return buffer.toString();
    }

    @Override
    public int compareTo(SortParameter anotherSortParm) {
        return Integer.compare(this.getQueryStringIndex(), anotherSortParm.getQueryStringIndex());
         
    }
}
