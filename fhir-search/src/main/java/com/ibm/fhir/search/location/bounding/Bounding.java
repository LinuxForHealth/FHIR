/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.bounding;

import java.util.List;

/**
 * Bounding defines an area that is searched with a location.
 * There are default constraints included in this implementation.
 */
public interface Bounding {

    /**
     * validates the Longitude and Latitude is valid for the Bounding area.
     */
    public void validate();
    
    /**
     * gets the coordinates in an ordered list
     * @return
     */
    public List<Double> getDataPoints();
}