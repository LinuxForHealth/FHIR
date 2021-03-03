/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import com.ibm.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;

/**
 * Adapts the partitionSummaries and DatasourceArray into an Exit Status
 */
public class ExitStatus {

    private static final Logger logger = Logger.getLogger(ExitStatus.class.getName());

    private JsonArray dataSourceArray = null;
    private List<ImportCheckPointData> partitionSummaries = null;

    public ExitStatus(JsonArray dataSourceArray, List<ImportCheckPointData> partitionSummaries) {
        this.dataSourceArray = dataSourceArray;
        this.partitionSummaries = partitionSummaries;
    }

    public String generateResultExitStatus() {
        Map<String, Integer> inputUrlSequenceMap = new HashMap<>();
        int sequnceNum = 0;
        for (JsonValue jsonValue : dataSourceArray) {
            JsonObject dataSourceInfo = jsonValue.asJsonObject();
            String dSTypeInfo = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_TYPE);
            String dSDataLocationInfo = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_URL);
            inputUrlSequenceMap.put(dSTypeInfo + ":" + dSDataLocationInfo, sequnceNum++);
        }

        String resultInExitStatus[] = new String[sequnceNum];
        for (ImportCheckPointData partitionSummary : partitionSummaries) {
            String key = partitionSummary.getImportPartitionResourceType() + ":" + partitionSummary.getImportPartitionWorkitem();
            if (!inputUrlSequenceMap.containsKey(key)) {
                // Highly unlikely to hit now that the partition-resource-type is fixed
                // So... this means that the Key is some how mutated.
                logger.warning("Partition Key is incorrect '" + key + "'");
            }
            int index = inputUrlSequenceMap.get(partitionSummary.getImportPartitionResourceType() + ":" + partitionSummary.getImportPartitionWorkitem());
            resultInExitStatus[index] = partitionSummary.getNumOfImportedResources() + ":" + partitionSummary.getNumOfImportFailures();
        }
        return Arrays.toString(resultInExitStatus);
    }
}