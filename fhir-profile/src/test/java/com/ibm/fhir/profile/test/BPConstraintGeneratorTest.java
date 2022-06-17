/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.test;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;

public class BPConstraintGeneratorTest {
    private static final boolean DEBUG = false;

    @BeforeClass
    public void before() {
        FHIRRegistry.getInstance();
        FHIRRegistry.init();
    }

    @Test
    public void generateConstraints() throws Exception {
        StructureDefinition profile = ProfileSupport.getProfile("http://hl7.org/fhir/StructureDefinition/bp");
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 9);
        for (Constraint constraint : constraints) {
            if (DEBUG) {
                System.out.println(constraint.id() + "\t" + constraint.expression());
            }
        }
    }
}
