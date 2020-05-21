/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.generator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.omop.mapping.Mapping;
import com.ibm.fhir.omop.table.Table;

public interface Generator {
    Map<Table, List<String>> generate(Mapping mapping, Map<Class<? extends Resource>, List<Resource>> resourceMap);
    void generate(Mapping mapping, Map<Class<? extends Resource>, List<Resource>> resourceMap, Map<Table, List<String>> tableDataMap);
    Map<String, Long> getReferenceMap();
    Set<String> getProcessedReferences();
    Set<String> getUnprocessedReferences();
}
