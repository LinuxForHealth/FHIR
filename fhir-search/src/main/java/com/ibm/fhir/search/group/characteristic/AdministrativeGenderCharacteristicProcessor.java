/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group.characteristic;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Group.Characteristic;
import com.ibm.fhir.model.type.CodeableConcept;

/**
 * AdministrativeGender Characteristic Processor processes into a Query Parameter. Gender is mapped to the
 * SearchParameter code gender using token modifiers.
 */
public class AdministrativeGenderCharacteristicProcessor implements CharacteristicProcessor {
    @Override
    public void process(Characteristic characteristic, String target, MultivaluedMap<String, String> queryParams) {
        if ("Patient".equals(target) && "http://hl7.org/fhir/administrative-gender".equals(characteristic.getCode().getCoding().get(0).getSystem().getValue())
                && "AdministrativeGender".equals(characteristic.getCode().getCoding().get(0).getCode().getValue())) {

            boolean exclude = characteristic.getExclude().getValue();
            String modifier = "";
            if (exclude) {
                modifier = ":not";
            }
            queryParams.add("gender" + modifier,
                characteristic.getValue()
                    .as(CodeableConcept.class).getCoding().get(0).getCode().getValue());
        }
    }
}