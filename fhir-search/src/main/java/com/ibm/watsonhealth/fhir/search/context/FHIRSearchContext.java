/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.context;

import java.util.List;

import com.ibm.watsonhealth.fhir.search.Parameter;

public interface FHIRSearchContext {
    List<Parameter> getSearchParameters();
}
