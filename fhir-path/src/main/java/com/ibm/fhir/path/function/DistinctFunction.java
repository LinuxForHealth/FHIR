/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

public class DistinctFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "distinct";
    }

    @Override
    public int getMinArity() {
        return 0;
    }

    @Override
    public int getMaxArity() {
        return 0;
    }
}
