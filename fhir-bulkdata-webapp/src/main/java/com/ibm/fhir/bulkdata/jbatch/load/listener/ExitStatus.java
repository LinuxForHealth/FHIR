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

import com.ibm.fhir.bulkdata.jbatch.load.data.ImportCheckPointData;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

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
        int sequenceNum = 0;
        for (JsonValue jsonValue : dataSourceArray) {
            JsonObject dataSourceInfo = jsonValue.asJsonObject();
            String dSTypeInfo = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_TYPE);
            String dSDataLocationInfo = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_URL);
            inputUrlSequenceMap.put(dSTypeInfo + ":" + dSDataLocationInfo, sequenceNum++);
        }

        String[] resultInExitStatus = new String[sequenceNum];
        logger.fine(() -> "The partitions are " + partitionSummaries);
        for (ImportCheckPointData partitionSummary : partitionSummaries) {
            String key;
            if (partitionSummary.getMatrixWorkItem() == null) {
                key = partitionSummary.getImportPartitionResourceType() + ":" + partitionSummary.getImportPartitionWorkitem();
            } else {
                // must be matrixed
                key = partitionSummary.getImportPartitionResourceType() + ":" + partitionSummary.getMatrixWorkItem();
            }

            if (!inputUrlSequenceMap.containsKey(key)) {
                // Highly unlikely to hit now that the partition-resource-type is fixed
                // So... this means that the Key is some how mutated.
                logger.warning("Partition Key is incorrect '" + key + "' matrix='" + partitionSummary.getMatrixWorkItem() + "'");
            }

            int index = inputUrlSequenceMap.get(key);
            resultInExitStatus[index] = partitionSummary.getNumOfImportedResources() + ":" + partitionSummary.getNumOfImportFailures();
        }
        return Arrays.toString(resultInExitStatus);
    }
}