/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.export.system.resource;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.jbatch.export.system.ChunkReader;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * System Export Resource Handler
 */
public class SystemExportResourceHandler {

    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    protected ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

    protected Class<? extends Resource> resourceType;

    protected final SearchUtil searchHelper;

    public SystemExportResourceHandler() {
        searchHelper = new SearchUtil();
    }

    public void fillChunkData(String exportFormat, ExportTransientUserData chunkData, List<? extends Resource> resources) throws Exception {
        int resSubTotal = 0;
        if (chunkData == null) {
            String msg = "fillChunkDataBuffer: chunkData is null, this should never happen!";
            logger.warning(msg);
            throw new Exception(msg);
        }

        long priorSize = chunkData.getBufferStream().size();

        for (Resource res : resources) {
            try {
                // No need to fill buffer for parquet because we're letting spark write to COS;
                // we don't need to control the Multi-part upload like in the NDJSON case
                if (!FHIRMediaType.APPLICATION_PARQUET.equals(exportFormat)) {
                    FHIRGenerator.generator(Format.JSON).generate(res, chunkData.getBufferStream());
                    chunkData.getBufferStream().write(adapter.getEndOfFileDelimiter(null));
                }
                resSubTotal++;
            } catch (FHIRGeneratorException e) {
                // TODO write OperationOutcome to COS for these errors
                if (res.getId() != null) {
                    logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing " + res.getClass().getSimpleName() +
                            " with id '" + res.getId() + "'", e);
                } else {
                    logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing " + res.getClass().getSimpleName() +
                            " with no id", e);
                }
            } catch (IOException e) {
                logger.warning("fillChunkDataBuffer: chunkDataBuffer written error!");
                throw e;
            }
        }

        chunkData.addCurrentUploadResourceNum(resSubTotal);
        chunkData.addCurrentUploadSize(chunkData.getBufferStream().size() - priorSize);
        chunkData.addTotalResourcesNum(resSubTotal);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("fillChunkDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                        + chunkData.getBufferStream().size());
        }
    }

    /**
     * get the underlying search helper for working with FHIR search parameters
     */
    public SearchUtil getSearchHelper() {
        return searchHelper;
    }
}
