/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.load.listener;

import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_INPUT_RESOURCE_TYPE;
import static com.ibm.fhir.jbatch.bulkdata.common.Constants.IMPORT_INPUT_RESOURCE_URL;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import com.ibm.fhir.jbatch.bulkdata.load.data.ImportCheckPointData;

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
            String DSTypeInfo = dataSourceInfo.getString(IMPORT_INPUT_RESOURCE_TYPE);
            String DSDataLocationInfo = dataSourceInfo.getString(IMPORT_INPUT_RESOURCE_URL);
            inputUrlSequenceMap.put(DSTypeInfo + ":" + DSDataLocationInfo, sequnceNum++);
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