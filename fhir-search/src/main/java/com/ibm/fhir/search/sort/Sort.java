/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.search.parameters.SortParameter;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This class implements the _sort URL processing into internal SortParameters.
 * <a href="https://www.hl7.org/fhir/search.html#sort">FHIR Specification:
 * _sort<a/>
 */
public class Sort {
    private static final String CLASSNAME = Sort.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    // set as unmodifiable
    public static final List<String> SYSTEM_LEVEL_SORT_PARAMETER_NAMES =
            Collections.unmodifiableList(Arrays.asList("_id", "_lastUpdated"));

    /**
     * ascending and descending enumeration
     */
    public enum Direction {

        // r4 - https://www.hl7.org/fhir/r4/search.html#sort
        // uses chars as an optimization choice to compare rather than a string to string for a single char.
        // Each item in the comma separated list is a search parameter, optionally with a '-' prefix. The prefix
        // indicates decreasing order; in its absence, the parameter is applied in increasing order.
        // previously increasing was ASCENDING and decreasing was DESCENDING.
        DECREASING('-'),
        INCREASING('+');

        private char value;

        Direction(char value) {
            this.value = value;
        }

        public char value() {
            return value;
        }

        /**
         * from value is converted to a sort direction based on the use of '-' else it's
         * assumed to be the default increasing.
         *
         * @param value
         * @return
         */
        public static Direction fromValue(String value) {
            if (value == null || value.isEmpty()) {
                throw SearchExceptionUtil.buildNewIllegalArgumentException("invalid direction");
            } else {
                char tmpChar = value.charAt(0);
                Direction sortDirection = Direction.INCREASING;
                if (tmpChar == '-') {
                    sortDirection = Direction.DECREASING;
                }
                return sortDirection;
            }
        }
    }

    /**
     * Parses the _sort parameter.
     * @param resourceType the resource type
     * @param context the search context
     * @param sortParmValue the parameter value
     * @param lenient true if lenient, false if strict
     * @throws Exception an exception
     */
    public void parseSortParameter(Class<?> resourceType, FHIRSearchContext context,
            String sortParmValue, boolean lenient) throws Exception {
        parseSortParameter(resourceType.getSimpleName(), context, sortParmValue, lenient);
    }

    /**
     * Parses the _sort parameter.
     * @param resourceTypeName the resource type name
     * @param context the search context
     * @param sortParmValue the parameter value
     * @param lenient true if lenient, false if strict
     * @throws Exception an exception
     */
    public void parseSortParameter(String resourceTypeName, FHIRSearchContext context,
            String sortParmValue, boolean lenient) throws Exception {

        for (String sortParmCode : sortParmValue.split(",")) {
            // Each parameter now includes the direction.
            Sort.Direction sortDirection = Direction.fromValue(sortParmCode);

            // In the case, where we are DECREASING, we need to treat the CODE in a special
            // way by stripping the directional indicator.
            if (Sort.Direction.DECREASING.compareTo(sortDirection) == 0) {
                sortParmCode = sortParmCode.substring(1);
            }

            // Per the FHIR spec, the _sort parameter value is a search parameter. We need to determine what
            // type of search parameter.
            SearchParameter sortParmProxy = SearchUtil.getSearchParameter(resourceTypeName, sortParmCode);
            if (!isUndefinedOrLenient(resourceTypeName, sortParmCode, sortParmProxy, lenient)) {
                SearchConstants.Type sortParmType =
                        SearchConstants.Type.fromValue(sortParmProxy.getType().getValue());
                SortParameter sortParm = new SortParameter(sortParmCode, sortParmType, sortDirection);

                checkSystemLevel(resourceTypeName, sortParm.getCode());

                context.getSortParameters().add(sortParm);
            }
        }
    }

    /**
     * checks to see if undefined as a SearchParameter or Lenient
     *
     * @param resourceTypeName
     * @param sortParmCode
     * @param sortParmProxy
     * @param lenient
     * @return
     * @throws FHIRSearchException
     */
    public boolean isUndefinedOrLenient(String resourceTypeName, String sortParmCode, SearchParameter sortParmProxy,
            boolean lenient) throws FHIRSearchException {
        boolean result = sortParmProxy == null;
        if (result) {
            String msg = buildUndefinedSortParamMessage(resourceTypeName, sortParmCode);

            // Stop Processing if not lenient
            if (!lenient) {
                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
            }

            log.fine(msg);
        }
        return result;
    }

    /**
     * check system level searches with DESC/ASC are ONLY with _id and _lastUpdated
     *
     * @param resourceTypeName one of the FHIR Resources, and must be a non-null
     *                         value
     * @param code             the code to check.
     * @throws FHIRSearchException
     */
    public void checkSystemLevel(String resourceTypeName, String code) throws FHIRSearchException {
        if ("Resource".compareTo(resourceTypeName) == 0
                && !SYSTEM_LEVEL_SORT_PARAMETER_NAMES.contains(code)) {
            throw SearchExceptionUtil.buildNewInvalidSearchException(
                    buildNewInvalidSearchExceptionMessage(code));
        }
    }

    /**
     * builds exception message when the sort parameter used is undefined.
     *
     * @param sortParmCode the code that is passed in
     * @return String representing the exception message
     */
    public final String buildNewInvalidSearchExceptionMessage(String sortParmCode) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(sortParmCode);
        builder.append("] is not supported as a sort parameter on a system-level search.");
        return builder.toString();
    }

    /**
     * builds exception message when the sort parameter used is undefined.
     *
     * @param resourceTypeName the specific FHIR Resource passed in or general
     *                         RESOURCE
     * @param sortParmCode     the code that is passed in
     * @return String representing the exception message
     */
    public final String buildUndefinedSortParamMessage(String resourceTypeName, String sortParmCode) {
        StringBuilder builder = new StringBuilder();
        builder.append("Undefined sort parameter. resourceType=[");
        builder.append(resourceTypeName);
        builder.append("] sortParmCode=[");
        builder.append(sortParmCode);
        builder.append("]");
        return builder.toString();
    }
}