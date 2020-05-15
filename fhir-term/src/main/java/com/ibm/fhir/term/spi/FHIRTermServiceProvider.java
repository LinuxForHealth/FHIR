/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

import com.ibm.fhir.model.resource.ValueSet;

public interface FHIRTermServiceProvider {
    ValueSet expand(ValueSet valueSet);
}
