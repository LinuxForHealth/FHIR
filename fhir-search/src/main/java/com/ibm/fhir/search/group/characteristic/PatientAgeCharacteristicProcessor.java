/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group.characteristic;

import java.time.ZonedDateTime;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Group.Characteristic;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.code.QuantityComparator;

/**
 * Patient Age Characteristic Processor processes Quantity with Years into the appropriate search parameters. This is
 * only valid for the Resource Type Patient. The Age is mapped to the SearchParameter code birthdate.
 */
public class PatientAgeCharacteristicProcessor implements CharacteristicProcessor {

    @Override
    public void process(Characteristic characteristic, String target, MultivaluedMap<String, String> queryParams) {
        if ("Patient".equals(target)
                && "29553-5".equals(characteristic.getCode().getCoding().get(0).getCode().getValue())) {
            Quantity age = characteristic.getValue().as(Quantity.class);
            QuantityComparator comparator = age.getComparator();
            boolean exclude = characteristic.getExclude().getValue();

            if (age.getUnit() != null && "years".equals(age.getUnit().getValue())) {
                String val = fromQuantityComparator(comparator, exclude);
                ZonedDateTime zdt = ZonedDateTime.now().minusYears(age.getValue().getValue().intValue());
                queryParams.add("birthdate", val + zdt.getYear());
            }
        }
    }

    /*
     * Small Explaination to the Logic.
     *
     * Since age relative to birthdate is a moving window, tomorrow is one day different
     * than today. We need to consider the conversation to birthdate.
     *
     * This means - find window and flip the direction.
     *
     */
    private String fromQuantityComparator(QuantityComparator comparator, Boolean exclude) {
        // The default is actually empty, which equates to 'eq'.
        String result = "";
        // Default is NO
        if (exclude == null) {
            exclude = Boolean.FALSE;
        }

        if (comparator == null) {
            if (!exclude.booleanValue()) {
                result = "eq";
            } else {
                result = "ne";
            }
        } else {
            // Example: Age = 56
            // Birthdate: 2020 becomes 1964
            QuantityComparator.Value vs = comparator.getValueAsEnum();
            if (QuantityComparator.Value.LESS_THAN.equals(vs)) {
                // gt1964
                if (!exclude.booleanValue()) {
                    result = "gt";
                } else {
                    result = "le";
                }
            } else if (QuantityComparator.Value.LESS_OR_EQUALS.equals(vs)) {
                // ge1964
                if (!exclude.booleanValue()) {
                    result = "ge";
                } else {
                    result = "lt";
                }
            } else if (QuantityComparator.Value.GREATER_OR_EQUALS.equals(vs)) {
                // le1964
                if (!exclude.booleanValue()) {
                    result = "le";
                } else {
                    result = "gt";
                }
            } else if (QuantityComparator.Value.GREATER_THAN.equals(vs)) {
                // lt1964
                if (!exclude.booleanValue()) {
                    result = "lt";
                } else {
                    result = "ge";
                }
            }
        }
        return result;
    }
}