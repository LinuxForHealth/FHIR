/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.group.characteristic;

import javax.ws.rs.core.MultivaluedMap;

import org.linuxforhealth.fhir.model.resource.Group.Characteristic;
import org.linuxforhealth.fhir.model.type.CodeableConcept;

/**
 * PregnancyStatus Characteristic Processor processes into a Query Parameter. Pregnancy status is mapped to the
 * SearchParameter code gender using token modifiers.
 */
public class PregnancyStatusCharacteristicProcessor implements CharacteristicProcessor {
    @Override
    public void process(Characteristic characteristic, String target, MultivaluedMap<String, String> queryParams) {
        if ("Observation".equals(target) && "http://loinc.org".equals(characteristic.getCode().getCoding().get(0).getSystem().getValue())
                && "82810-3".equals(characteristic.getCode().getCoding().get(0).getCode().getValue())) {

            boolean exclude = characteristic.getExclude().getValue();
            String modifier = "";
            if (exclude) {
                modifier = ":not";
            }

            // Refer to Search Parameters https://www.hl7.org/fhir/observation.html
            queryParams.add("component-value-concept" + modifier,
                characteristic.getValue()
                    .as(CodeableConcept.class).getCoding().get(0).getCode().getValue());
        }
    }
}