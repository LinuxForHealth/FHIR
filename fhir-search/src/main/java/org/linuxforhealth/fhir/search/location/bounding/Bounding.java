/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.location.bounding;

import java.util.List;

/**
 * Bounding defines an area that is searched with a location.
 * There are default constraints included in this implementation.
 */
public abstract class Bounding {
    private int instance = 0;

    /**
     * sets the instance number
     */
    public void setInstance(int instance) {
        this.instance = instance;
    }

    /**
     * @return the instance of the parameter
     */
    public int instance() { 
        return instance;
    }

    /**
     * validates the Longitude and Latitude is valid for the Bounding area.
     */
    public abstract void validate();

    /**
     * gets the coordinates in an ordered list
     * 
     * @return
     */
    public abstract List<Double> getDataPoints();

    /**
     * returns the bounding type - radius or box.
     * 
     * @return
     */
    public abstract BoundingType getType();
}