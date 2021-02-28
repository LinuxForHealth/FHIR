/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.jbatch.bulkdata.export.system.resource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.jbatch.bulkdata.common.Constants;
import com.ibm.fhir.jbatch.bulkdata.export.data.TransientUserData;
import com.ibm.fhir.jbatch.bulkdata.export.dto.ReadResultDTO;
import com.ibm.fhir.jbatch.bulkdata.export.system.ChunkReader;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 *
 */
public class SystemExportResourceHandler {

    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    boolean isSingleCosObject = false;

    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources,
    // loadedResourceIds and isDoDuplicationCheck are always reset when moving to the next resource type.
    Set<String> loadedResourceIds = new HashSet<>();

    public SystemExportResourceHandler() {
        // No Operation
    }

    public void fillChunkDataBuffer(String source, String exportFormat, TransientUserData chunkData, ReadResultDTO dto) throws Exception {
        boolean isDoDuplicationCheck = ConfigurationFactory.getInstance().shouldSourceCheckDuplicate(source);
        int resSubTotal = 0;
        if (chunkData == null) {
            logger.warning("fillChunkDataBuffer: chunkData is null, this should never happen!");
            throw new Exception("fillChunkDataBuffer: chunkData is null, this should never happen!");
        }

        for (Resource res : dto.getResources()) {
            if (res == null || (isDoDuplicationCheck && loadedResourceIds.contains(res.getId()))) {
                continue;
            }

            try {
                // No need to fill buffer for parquet because we're letting spark write to COS;
                // we don't need to control the Multi-part upload like in the NDJSON case
                if (!FHIRMediaType.APPLICATION_PARQUET.equals(exportFormat)) {
                    FHIRGenerator.generator(Format.JSON).generate(res, chunkData.getBufferStream());
                    chunkData.getBufferStream().write(Constants.NDJSON_LINESEPERATOR);
                }
                resSubTotal++;
                if (isDoDuplicationCheck && res.getId() != null) {
                    loadedResourceIds.add(res.getId());
                }
            } catch (FHIRGeneratorException e) {
                // TODO write OperationOutcome to COS for these errors
                if (res.getId() != null) {
                    logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing resources with id '"
                            + res.getId() + "'", e);
                } else {
                    logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing resources with no id", e);
                }
            } catch (IOException e) {
                logger.warning("fillChunkDataBuffer: chunkDataBuffer written error!");
                throw e;
            }
        }
        chunkData.setCurrentUploadResourceNum(chunkData.getCurrentUploadResourceNum() + resSubTotal);
        chunkData.setCurrentUploadSize(chunkData.getCurrentUploadSize() + chunkData.getBufferStream().size());
        chunkData.setTotalResourcesNum(chunkData.getTotalResourcesNum() + resSubTotal);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("fillChunkDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                        + chunkData.getBufferStream().size());
        }
    }
}
