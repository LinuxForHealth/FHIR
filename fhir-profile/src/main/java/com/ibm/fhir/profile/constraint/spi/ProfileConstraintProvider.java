/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.constraint.spi;

import com.ibm.fhir.model.constraint.spi.ConstraintProvider;

public interface ProfileConstraintProvider extends ConstraintProvider {
    boolean appliesTo(String url, String version);
}
