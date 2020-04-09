/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.operation.bulkdata.util.group.examples.AnnualObGynExample;
import com.ibm.fhir.operation.bulkdata.util.group.examples.AnnualWellnessExample;
import com.ibm.fhir.operation.bulkdata.util.group.examples.GroupExample;

/**
 * This class automates the generation of Dynamic Groups (Cohorts) for testing the Epic:
 * <a href="https://github.com/IBM/FHIR/issues/566">Bulk export for dynamic Groups based on characteristics #566</a>
 * <br>
 */
public class DynamicGroupGeneratorMain {

    private Bundle.Builder bundleBuilder = Bundle.builder();

    private List<Group> groups = new ArrayList<>();

    private List<GroupExample> groupExamples = Arrays.asList(new AnnualWellnessExample(), new AnnualObGynExample());

    public DynamicGroupGeneratorMain() {
        // No Op
    }

    // Profile: http://hl7.org/fhir/actualgroup.html
    // Enforces an actual group, rather than a definitional group
    public void generateActualGroup() {

    }

    // Profile: http://hl7.org/fhir/groupdefinition-definitions.html
    // Enforces a descriptive group that can be used in definitional resources 
    public void generateDescriptiveGroup() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DynamicGroupGeneratorMain main = new DynamicGroupGeneratorMain();
        main.generateActualGroup();
        main.generateDescriptiveGroup();
    }
}
