/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;


import static com.ibm.fhir.path.util.FHIRPathUtil.compile;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.registry.FHIRRegistry;

public class ObservationProfileTest {
    @Test
    public void testObservationProfile() throws Exception {
        StructureDefinition profile = FHIRRegistry.getInstance().getResource("http://ibm.com/fhir/StructureDefinition/my-observation",  StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        constraints.forEach(System.out::println);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "component.exists().not()");
    }
}
