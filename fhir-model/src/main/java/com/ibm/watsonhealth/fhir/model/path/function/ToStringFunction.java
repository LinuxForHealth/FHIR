/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.*;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNumberValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathType;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.String;


import static com.ibm.watsonhealth.fhir.model.path.FHIRPathStringValue.stringValue;

public class ToStringFunction extends FHIRPathAbstractFunction {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("'T'HH:mm:ss.SSS[XXX]");
    
    @Override
    public java.lang.String getName() {
        return "toString";
    }

    @Override
    public int getMinArity() {
        return 0;
    }

    @Override
    public int getMaxArity() {
        return 0;
    }
    
    @Override
    public Collection<FHIRPathNode> apply(Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!isSingleton(context)) {
            throw new IllegalArgumentException();
        }
        if (hasPrimitiveValue(context)) {
            FHIRPathPrimitiveValue value = getPrimitiveValue(context);
            if (value.isDateTimeValue()) {
                FHIRPathDateTimeValue dateTimeValue = value.asDateTimeValue();
                if (!dateTimeValue.isPartial()) {
                    return singleton(stringValue(DATE_TIME_FORMATTER.format(dateTimeValue.dateTime())));
                } else {
                    return singleton(stringValue(dateTimeValue.dateTime().toString()));
                }
            } else if (value.isTimeValue()) {
                FHIRPathTimeValue timeValue = value.asTimeValue();
                return singleton(stringValue(TIME_FORMATTER.format(timeValue.time())));
            } else if (value.isNumberValue()) {
                FHIRPathNumberValue numberValue = value.asNumberValue();
                return singleton(stringValue(numberValue.number().toString()));
            } else if (value.isBooleanValue()) {
                FHIRPathBooleanValue booleanValue = value.asBooleanValue();
                return singleton(stringValue(java.lang.String.format("'%s'", booleanValue._boolean().toString())));
            }
        } else {
            FHIRPathNode node = getSingleton(context);
            if (node.isElementNode() && FHIRPathType.FHIR_QUANTITY.equals(node.type())) {
                Quantity quantity = (Quantity) node.asElementNode().element();
                if (quantity.getValue() != null && quantity.getUnit() != null) {
                    Decimal value = quantity.getValue();
                    String unit = quantity.getUnit();
                    if (value.getValue() != null && unit.getValue() != null) {
                        return singleton(stringValue(java.lang.String.format("%s '%s'", value.getValue().toString(), unit.getValue())));
                    }
                }
            }
        }
        return empty();
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(TIME_FORMATTER.format(LocalTime.now()));
        System.out.println(TIME_FORMATTER.format(OffsetTime.now()));
    }
}
