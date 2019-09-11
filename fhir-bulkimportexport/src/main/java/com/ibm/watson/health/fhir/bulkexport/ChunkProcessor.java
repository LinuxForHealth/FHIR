/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.watson.health.fhir.model.resource.Resource;

/**
 * Bulk export Chunk implementation - the Processor.
 * 
 * @author Albert Wang
 */
public class ChunkProcessor implements ItemProcessor {    
    @Inject
    JobContext jobContext;
    
    private final static Logger logger = Logger.getLogger(ChunkProcessor.class.getName());

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
        String combinedJsons = null;
        int dataSize = 0;
                        
        for (Resource res : resources) {
            String ndJsonLine = res.toString().replace("\r", "").replace("\n", "");
            if (combinedJsons == null) {
                combinedJsons = ndJsonLine;
            } else {
                combinedJsons = combinedJsons + "," + "\r\n" + ndJsonLine;
            }
        }

        if (combinedJsons != null) {
            resStrings.add(combinedJsons);
            dataSize += combinedJsons.getBytes(StandardCharsets.UTF_8).length; 
        }
        
        TransientUserData chunkData = (TransientUserData)jobContext.getTransientUserData();
        if (chunkData != null && chunkData.isSingleCosObject()) {
            chunkData.setCurrentPartSize(chunkData.getCurrentPartSize() + dataSize);
            log("processItem", "processed resources: " + resources.size() + " current part size: " + chunkData.getCurrentPartSize());
        } else {
            log("processItem", "processed resources: " + resources.size());
        }

        
        return resStrings;
    }

}
