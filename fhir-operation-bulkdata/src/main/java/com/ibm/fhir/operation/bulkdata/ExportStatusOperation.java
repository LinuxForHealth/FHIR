/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.IssueType;
import com.ibm.fhir.operation.AbstractOperation;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.util.BulkDataUtil;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.rest.FHIRResourceHelpers;

/**
 * Creates an Export of FHIR Data to NDJSON format
 * 
 * @link https://build.fhir.org/ig/HL7/bulk-data/index.html 
 *       BulkDataAccess IG: STU1
 * 
 *
 * @author pbastide
 *
 */
public class ExportStatusOperation extends AbstractOperation {

    private static final String FILE = "export-status.json";
    
    // This is duplicate 'caching' as in the BulkData $export operation. 
    private static BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();

    public ExportStatusOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        /**
         * Loads the operation definition file. In this case, there are three files, and only one is loaded.
         */
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE);) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext,
        Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        /*
         * This call is enabled only at the root. 
         */
                
        if(logicalId == null && versionId == null && resourceType == null) {
            
            // Eventually, Check if the operation's method is DELETE, and eventually branch 
            
            // 
            String job = BulkDataUtil.checkAndValidateJob(parameters);
            
            // For now, we're going to execute the status update, and check. 
            // If Base, Export Status (Else Invalid)
            return BulkDataFactory.getExport(cache).statusExport(job, operationContext, cache);
            
        } else {
            // Unsupported on Resource Type
            // Root operation is only supported, and we signal it back here. 
            // Don't get fancy, just send it back. 
            throw buildExceptionWithIssue("Invalid call $export-status operation call", IssueType.ValueSet.INVALID);
        }
        
    }
    
}
