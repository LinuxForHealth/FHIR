/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.cql.engine.searchparam;

import org.linuxforhealth.fhir.search.SearchConstants.Modifier;

public interface IQueryParameter {

    Boolean getMissing();

    Modifier getModifier();

    String getParameterValue();
}
