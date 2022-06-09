/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core;

import java.util.List;
import java.util.function.Predicate;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.profile.constraint.spi.AbstractProfileConstraintProvider;

public class USCore500ConstraintModifier extends AbstractProfileConstraintProvider {
    private static final String targetUrl = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-questionnaireresponse";
    private static final String targetVersion = "5.0.0";

    @Override
    public boolean appliesTo(String url, String version) {
        return targetUrl.equals(url) && targetVersion.equals(version);
    }

    @Override
    protected void addRemovalPredicates(List<Predicate<Constraint>> removalPredicates) {
        removalPredicates.add(idEquals("sdcqr-2"));
    }

}
