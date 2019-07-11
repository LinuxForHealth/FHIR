/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

public class ReplaceMatchesFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "replaceMatches";
    }

    @Override
    public int getMinArity() {
        return 2;
    }

    @Override
    public int getMaxArity() {
        return 2;
    }
}
