/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.SortDirection;
import com.ibm.fhir.search.SearchConstants.Type;

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
public class SortParameter {
    
    private static final char EQUAL = '=';

    private String name;
    private Type type;
    private SortDirection direction;
    
    public SortParameter(String parmName, Type parmType, SortDirection sortDirection) {
        super();
        this.name = parmName;
        this.type = parmType;
        this.direction = sortDirection;
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

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(SearchConstants.SORT);
        buffer.append(SearchConstants.COLON_DELIMITER);
        buffer.append(this.getDirection().value());
        buffer.append(EQUAL);
        buffer.append(this.getName());
        return buffer.toString();
    }

}
