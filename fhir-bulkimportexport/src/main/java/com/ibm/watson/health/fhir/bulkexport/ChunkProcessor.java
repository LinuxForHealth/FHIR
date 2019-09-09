/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.chunk.ItemProcessor;

import com.ibm.watson.health.fhir.model.resource.Resource;

/**
 * Bulk export Chunk implementation - the Processor.
 * 
 * @author Albert Wang
 */
public class ChunkProcessor implements ItemProcessor {
    private final static Logger logger = Logger.getLogger(ChunkProcessor.class.getName());
    int cosBatchSize = 20;

    /**
     * Default constructor.
     */
    public ChunkProcessor() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    /**
     * @see ItemProcessor#processItem(Object)
     */
    @SuppressWarnings("unchecked")
    public Object processItem(Object arg0) {

        List<String> resStrings = new ArrayList<String>();
        List<Resource> resources = (List<Resource>) arg0;
        int count = 0;
        String combinedJsons = null;
        for (Resource res : resources) {
            count++;
            if (combinedJsons == null) {
                combinedJsons = "[" + res.toString();
            } else {
                combinedJsons = combinedJsons + "," + res.toString();
            }

            if (count == cosBatchSize) {
                combinedJsons = combinedJsons + "]";
                resStrings.add(combinedJsons);
                count = 0;
                combinedJsons = null;

            }
        }

        if (combinedJsons != null) {
            combinedJsons = combinedJsons + "]";
            resStrings.add(combinedJsons);
        }

        log("processItem", "processed resources: " + resources.size() + " created cos batches: " + resStrings.size());
        return resStrings;
    }

}
