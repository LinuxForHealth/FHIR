/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location.bounding;

import java.util.Collections;
import java.util.List;

/**
 * This class is used to indicate a Missing search.
 */
public class BoundingMissing extends Bounding {

    private Boolean missing = Boolean.FALSE;

    public BoundingMissing() {
        // No Operation
    }

    public Boolean getMissing() {
        return missing;
    }

    public void setMissing(Boolean missing) {
        this.missing = missing;
    }

    public void validate() {
        // No Operation
    }

    public BoundingType getType() {
        return BoundingType.MISSING;
    }

    @Override
    public List<Double> getDataPoints() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "BoundingMissing [missing=" + missing + "]";
    }
}