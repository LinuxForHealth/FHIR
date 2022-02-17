/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

    /**
     * Generates the EXIT_STATUS that is part of the batch job.
     * @return
     */
    public String generateResultExitStatus() {
        Map<String, ExitStatusVal> inputUrlSequenceMap = new HashMap<>();
        int sequenceNum = 0;
        for (JsonValue jsonValue : dataSourceArray) {
            JsonObject dataSourceInfo = jsonValue.asJsonObject();
            String dSTypeInfo = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_TYPE);
            String dSDataLocationInfo = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_URL);
            ExitStatusVal val = new ExitStatusVal();
            val.sequenceNum = sequenceNum++;
            inputUrlSequenceMap.put(dSTypeInfo + ":" + dSDataLocationInfo, val);
        }

        // Summarize the counts if they are matrixed work items.
        logger.fine(() -> "The partitions are " + partitionSummaries);
        for (ImportCheckPointData partitionSummary : partitionSummaries) {
            String key;
            if (partitionSummary.getMatrixWorkItem() == null) {
                key = partitionSummary.getImportPartitionResourceType() + ":" + partitionSummary.getImportPartitionWorkitem();
            } else {
                // must be matrixed
                // matrixWorkItem=test- maps to importPartitionWorkitem=test-import.ndjson and test-import-skip.ndjson
                key = partitionSummary.getImportPartitionResourceType() + ":" + partitionSummary.getMatrixWorkItem();
            }

            if (!inputUrlSequenceMap.containsKey(key)) {
                // Highly unlikely to hit now that the partition-resource-type is fixed
                // So... this means that the Key is some how mutated.
                logger.warning("Partition Key is incorrect '" + key + "' matrix='" + partitionSummary.getMatrixWorkItem() + "'");
            }

            ExitStatusVal val = inputUrlSequenceMap.get(key);
            val.numberOfResources += partitionSummary.getNumOfImportedResources();
            val.numberOfFailures += partitionSummary.getNumOfImportFailures();
        }

        List<ExitStatusVal> vals = new ArrayList<>(inputUrlSequenceMap.values());
        Collections.sort(vals, new ExitStatusValComparator());
        return Arrays.toString(vals.toArray());
    }

    /**
     * Uses to aggregate and order the Partition Data
     */
    private static class ExitStatusVal {
        int sequenceNum = 0;
        int numberOfResources = 0;
        int numberOfFailures = 0;
        @Override
        public String toString() {
            return numberOfResources + ":" + numberOfFailures;
        }
    }

    /**
     * Private Comparator to faciliate organizing the data so the calculations are correctly counted
     */
    private static class ExitStatusValComparator implements Comparator<ExitStatusVal> {
        @Override
        public int compare(ExitStatusVal o1, ExitStatusVal o2) {
            if (o1.sequenceNum == o2.sequenceNum) {
                return 0;
            } else if (o1.sequenceNum < o2.sequenceNum) {
                return 1;
            } else { // (o1.sequenceNum > o2.sequenceNum)
                return -1;
            }
        }
    }
}