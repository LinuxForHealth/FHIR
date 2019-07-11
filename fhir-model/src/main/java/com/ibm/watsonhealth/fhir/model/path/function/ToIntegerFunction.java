/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import static com.ibm.watsonhealth.fhir.model.path.FHIRPathIntegerValue.integerValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.empty;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getPrimitiveValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.hasPrimitiveValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveValue;

public class ToIntegerFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "toInteger";
    }

    @Override
    public int getMinArity() {
        return 0;
    }

    @Override
    public int getMaxArity() {
        return 0;
    }

    public Collection<FHIRPathNode> apply(Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!hasPrimitiveValue(context)) {
            throw new IllegalArgumentException();
        }
        FHIRPathPrimitiveValue value = getPrimitiveValue(context);
        if (value.isNumberValue() && value.asNumberValue().isIntegerValue()) {
            return singleton(value);
        }
        if (value.isStringValue()) {
            String string = value.asStringValue().string();
            try {
                Integer integer = Integer.parseInt(string);
                return singleton(integerValue(integer));
            } catch (NumberFormatException e) {
                return empty();
            }
        }
        if (value.isBooleanValue()) {
            Boolean _boolean = value.asBooleanValue()._boolean();
            return singleton(integerValue(_boolean ? 1 : 0));
        }            
        return empty();        
    }
}
