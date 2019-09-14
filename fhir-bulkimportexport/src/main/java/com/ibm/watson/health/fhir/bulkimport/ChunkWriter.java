/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkimport;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.waston.health.fhir.bulkcommon.Constants;
import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watson.health.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watson.health.fhir.persistence.helper.FHIRTransactionHelper;

/**
 * Bulk import Chunk implementation - the Writer.
 * 
 * @author Albert Wang
 */
public class ChunkWriter extends AbstractItemWriter {
    private final static Logger logger = Logger.getLogger(ChunkWriter.class.getName());

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
     * @see AbstractItemWriter#AbstractItemWriter()
     */
    public ChunkWriter() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    /**
     * @throws Exception
     * @see AbstractItemWriter#writeItems(List<java.lang.Object>)
     */
    @SuppressWarnings("unchecked")
    public void writeItems(List<java.lang.Object> arg0) throws Exception {

        if (fhirTenant == null) {
            fhirTenant = "default";
            log("writeItems", "Set tenant to default!");
        }
        
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            log("readItem", "Set DatastoreId to default!");
        }
        
        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);

        int imported = 0;
        for (Object objResJasonList : arg0) {
            List<String> resJsonList = (List<String>) objResJasonList;

            for (String resJson : resJsonList) {

                JSONArray jsonJArray = new JSONArray(resJson);

                for (int i = 0; i < jsonJArray.length(); i++) {
                    JSONObject json_data = jsonJArray.getJSONObject(i);
                    Resource fhirRes = FHIRParser.parser(Format.JSON).parse(new StringReader(json_data.toString()));
                    FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
                    txn.begin();
                    fhirPersistence.update(persistenceContext, fhirRes.getId().getValue(), fhirRes);
                    log("writeItems", "Imported " + fhirRes.getClass().getSimpleName() + " with Id "
                            + fhirRes.getId().getValue());
                    txn.commit();
                    imported++;
                }
            }

            log("writeItems", "SubTotal Import: " + imported);
        }
    }

}
