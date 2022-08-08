/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

public class CheckModifiersFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "checkModifiers";
    }

    @Override
    public int getMinArity() {
        return 0;
    }

    @Override
    public int getMaxArity() {
        return 1;
    }
}
