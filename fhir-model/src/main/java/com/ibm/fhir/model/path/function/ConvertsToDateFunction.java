/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path.function;

public class ConvertsToDateFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "convertsToDate";
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
