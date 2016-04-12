/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;

public interface QueryBuilder<T> {
    T buildQuery(Class<? extends Resource> resourceType, List<Parameter> searchParameters);
}
