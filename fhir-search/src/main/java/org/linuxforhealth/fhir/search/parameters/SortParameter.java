/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.parameters;

import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.sort.Sort;
import org.linuxforhealth.fhir.search.sort.Sort.Direction;

/**
 * This class encapsulates data related to a FHIR sort parameter.
 * <a href="http://www.hl7.org/fhir/search.html#sort">_sort</a>
 */
public class SortParameter {

    private static final char EQUAL = '=';

    private String code;
    private Type type;
    private Sort.Direction direction;

    public SortParameter(String paramCode, Type parmType, Sort.Direction sortDirection) {
        super();
        this.code      = paramCode;
        this.type      = parmType;
        this.direction = sortDirection;
    }

    public String getCode() {
        return code;
    }

    public Type getType() {
        return type;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(SearchConstants.SORT);

        buffer.append(EQUAL);
        if (Direction.DECREASING.compareTo(direction) == 0) {
            buffer.append(this.getDirection().value());
        }

        buffer.append(this.getCode());
        return buffer.toString();
    }

}