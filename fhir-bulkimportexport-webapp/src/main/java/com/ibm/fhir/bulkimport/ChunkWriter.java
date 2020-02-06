/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;

/**
 * Bulk import Chunk implementation - the Writer.
 *
 */
public class ChunkWriter extends AbstractItemWriter {
    private static final Logger logger = Logger.getLogger(ChunkWriter.class.getName());

    @Inject
    StepContext stepCtx;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = "fhir.tenant")
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = "fhir.datastoreid")
    String fhirDatastoreId;

    /**
     * @see javax.batch.api.chunk.AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
    }

    /**
     * @throws Exception
     * @see {@link javax.batch.api.chunk.AbstractItemWriter#writeItems(List)}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void writeItems(List<java.lang.Object> arg0) throws Exception {
        if (fhirTenant == null) {
            fhirTenant = "default";
            logger.info("writeItems: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.info("writeItems: Set DatastoreId to default!");
        }

        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());

        int imported = 0;
        for (Object objResJasonList : arg0) {
            List<Resource> fhirResourceList = (List<Resource>) objResJasonList;

            for (Resource fhirResource : fhirResourceList) {

                txn.begin();
                fhirPersistence.update(persistenceContext, fhirResource.getId(), fhirResource);
                txn.commit();
                imported++;
            }
        }
        CheckPointData chunkData = (CheckPointData) stepCtx.getTransientUserData();
        chunkData.setNumOfLinesToSkip(chunkData.getNumOfLinesToSkip() + imported);
        logger.info("writeItems: Imported " + imported + "fhir resources from " +  chunkData.getImportPartitionWorkitem());
    }

}
