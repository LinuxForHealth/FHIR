/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.linuxforhealth.fhir.model.type.String.string;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Observation.Component;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.ObservationStatus;

public class FoodIntakeObservationTest {
    private static final String SYSTEM_SNOMED_CT = "http://snomed.info/sct";
    private static final String SYSTEM_UNITS_OF_MEASURE = "http://www.unitsofmeasure.org";

    public static void main(String[] args) throws Exception {     
        Observation foodIntakeObservation = Observation.builder()
            .status(ObservationStatus.FINAL)
            .code(codeableConcept(coding(SYSTEM_SNOMED_CT, "226379006", "Food intake")))
            .component(component(codeableConcept(coding(SYSTEM_SNOMED_CT, "226441002", "Fish intake")), 
                quantity(250, "grams", SYSTEM_UNITS_OF_MEASURE, "g")))
            .component(component(codeableConcept(coding(SYSTEM_SNOMED_CT, "226404003", "Milk intake")), 
                quantity(1, "cup", SYSTEM_UNITS_OF_MEASURE, "[cup_us]")))
            .build();
        FHIRGenerator.generator(Format.JSON, true).generate(foodIntakeObservation, System.out);
    }
    
    public static Component component(CodeableConcept code, Element value) {
        return Component.builder()
                .code(code)
                .value(value)
                .build();
    }
    
    public static CodeableConcept codeableConcept(Coding... coding) {
        return CodeableConcept.builder()
                .coding(coding)
                .build();
    }
    
    public static Coding coding(String system, String code, String display) {
        return Coding.builder()
                .system(Uri.of(system))
                .code(Code.of(code))
                .display(string(display))
                .build();
    }
    
    public static Quantity quantity(Number value, String unit, String system, String code) {
        return Quantity.builder()
                .value(Decimal.of(value))
                .unit(string(unit))
                .system(Uri.of(system))
                .code(Code.of(code))
                .build();
    }
}