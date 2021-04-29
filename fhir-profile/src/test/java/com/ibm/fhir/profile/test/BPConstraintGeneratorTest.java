/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.test;

import java.util.List;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.profile.ProfileSupport;

public class BPConstraintGeneratorTest {
    public static void main(String[] args) throws Exception {
        StructureDefinition profile = ProfileSupport.getProfile("http://hl7.org/fhir/StructureDefinition/bp");
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        constraints.forEach(System.out::println);
    }
}
