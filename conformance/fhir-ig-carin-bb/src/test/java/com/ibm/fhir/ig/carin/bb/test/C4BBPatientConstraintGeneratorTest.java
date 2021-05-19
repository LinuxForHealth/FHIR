/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.profile.ConstraintGenerator;

public class C4BBPatientConstraintGeneratorTest {
    @Test
    public void testConstraintGenerator() throws Exception {
        InputStream in = C4BBPatientConstraintGeneratorTest.class.getClassLoader().getResourceAsStream("JSON/StructureDefinition-C4BB-Patient.json");
        FHIRParser parser = FHIRParser.parser(Format.JSON);
        StructureDefinition profile = parser.parse(in);
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertTrue(hasConstraint(constraints, "meta.where(lastUpdated.exists() and profile.where($this = 'http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Patient|1.0.0').count() = 1).exists()"));
    }

    public boolean hasConstraint(List<Constraint> constraints, String expr) {
        for (Constraint constraint : constraints) {
            if (constraint.expression().equals(expr)) {
                return true;
            }
        }
        return false;
    }
}
