/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout;

import java.math.BigDecimal;

/**
 * Utility functions for help with BigDecimal
 */
public class BigDecimalUtil {

    public static BigDecimal generateLowerBound(BigDecimal original) {
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * (original.scale() + 1));
        return original.subtract(scaleFactor);
    }

    public static BigDecimal generateUpperBound(BigDecimal original) {
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * (original.scale() + 1));
        return original.add(scaleFactor);
    }

}
